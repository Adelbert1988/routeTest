package com.router.processor;

import com.google.auto.service.AutoService;
import com.router.annotation.RouteProvider;
import com.router.utils.ElementUtils;
import com.router.utils.Logger;
import com.router.utils.RouteConstans;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * User: chw
 * Date: 2018/1/22
 */
@AutoService(Processor.class)
public class RouterProviderProcessor extends AbstractProcessor{

    private static final String MAPPING_PREFIX = "Router$$";
    private static final String MAPPING_SUFFIX = "$$ProviderMapping";

    private static final String AOP_PROVIDER_METHOD = RouteConstans.ROUTE_MAPPING_MANAGER + ".initProviderMapping()";

    private Logger mLogger;
    private Types mTypes;
    private Filer mFiler;
    private Messager mMessager;
    private Elements mElementUtils;

    private String mModuleName = "";
    private TypeMirror mProviderType;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mTypes = processingEnv.getTypeUtils();
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();
        mProviderType = mElementUtils.getTypeElement(RouteConstans.ROUTE_PROVIDER).asType();
        mLogger = new Logger(mMessager);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(RouteProvider.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        mLogger.d("routeProvider: " + RouteProvider.class.getCanonicalName());

        Map<String, String> options = processingEnv.getOptions();
        if (options != null && options.size() > 0) {
            mModuleName = options.get(RouteConstans.MODULE_NAME);
        }

        Set<? extends Element> annotationElements = roundEnvironment.getElementsAnnotatedWith(RouteProvider.class);

        List<Element> annotationList = new ArrayList<>();
        for (Element annotationElement : annotationElements) {
            processClassElement(annotationList, annotationElement);
        }

        if (annotationList.size() == 0) {
            mLogger.d("don't find RouteProvider annotate provider");
            return false;
        }

        try {
            generateRouteProviderCode(annotationList);
        } catch (IOException e) {
            e.printStackTrace();
            mLogger.error("Error creating mapping file", annotationList.get(0));
        }
        return true;
    }

    /**
     * 生成provider路由映射类
     * @param list
     * @throws IOException
     */
    private void generateRouteProviderCode(List<Element> list) throws IOException {
        ClassName mappingClassName = getModuleMappingClassName(list.get(0));
        MethodSpec addRouteProviderSpec = generateRouterProvider(list);

        TypeSpec.Builder mappingClassBuilder = TypeSpec.classBuilder(mappingClassName)
                .addAnnotation(AnnotationSpec.builder(Aspect.class).build())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(addRouteProviderSpec);

        JavaFile.builder(ClassName.get(((TypeElement)list.get(0))).packageName(), mappingClassBuilder.build())
                .build()
                .writeTo(mFiler);
    }

    /**
     * 批量添加activity映射
     * @param list
     * @return
     */
    private MethodSpec generateRouterProvider(List<Element> list) {
        CodeBlock.Builder routeInfoListBlock = CodeBlock.builder();
        for (Element element : list) {
            TypeMirror typeMirror = element.asType();
            if (mTypes.isSubtype(typeMirror, mProviderType)) {
                List<? extends TypeMirror> interfaces = ((TypeElement)element).getInterfaces();
                String providerName = "";
                for (TypeMirror mirror : interfaces) {
                    if (mTypes.isSameType(mirror, mProviderType)) {
                        providerName = element.toString();
                    } else if (mTypes.isSubtype(mirror, mProviderType)) {
                        providerName = mirror.toString();
                    }
                }

                ClassName destClass = ElementUtils.getClassName(element.asType());
                mLogger.d("qualifiedName: " + destClass.simpleName());
                routeInfoListBlock.add("$T.getInstance().addProviderMapping($S, $T.class);\n"
                        , RouteConstans.ROUTE_MAPPING_MANAGER, providerName, destClass);
            }

        }

        MethodSpec.Builder addRouteMethod = MethodSpec
                .methodBuilder("addRouterProvider")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(
                        AnnotationSpec.builder(After.class)
                                .addMember("value", "$S", "execution(* " + AOP_PROVIDER_METHOD + ")")
                                .build())
                .addCode(routeInfoListBlock.build());

        return addRouteMethod.build();

    }

    /**
     * 生成module的activity跳转映射类文件名
     * @param serviceElements
     * @return
     */
    private ClassName getModuleMappingClassName(Element serviceElements) {
        String packageName = ClassName.get((TypeElement) serviceElements).packageName();
        mLogger.d("Module packageName: " + packageName);
        String mappingClassName = MAPPING_PREFIX + mModuleName + MAPPING_SUFFIX;
        return ClassName.get(packageName, mappingClassName);
    }


    /**
     * 遍历出被RouteProvider注解的类
     * @param list
     * @param annotationElement
     */
    private void processClassElement(List<Element> list, Element annotationElement) {
        TypeElement typeElement = (TypeElement) annotationElement;
        if (!isValidClass(typeElement)) {
            mLogger.error("logError happen in RouterLinkProcessor, " +
                    RouteProvider.class.getSimpleName() + " must annotation on class", annotationElement);
        }

        RouteProvider providerAnnotation = typeElement.getAnnotation(RouteProvider.class);
        String routePath = providerAnnotation.value();

        if ("".equals(routePath)) {
                /*throw new IllegalArgumentException(
                        String.format("id() in @%s for class %s is null or empty! that's not allowed",
                                RouteProvider.class.getSimpleName(),
                                typeElement.getQualifiedName().toString                                                                                                     ng()));*/
            mLogger.error("error happen in RouterLinkProcessor, " +
                    RouteProvider.class.getSimpleName() + " value can not be null ", annotationElement);
        } else {
            list.add(typeElement);
        }

    }

    /**
     * 验证是否符合条件的类
     * @param classElement
     * @return
     */
    private boolean isValidClass(TypeElement classElement) {
        if (classElement.getKind() != ElementKind.CLASS) {
            mLogger.error("The annotation is only use in class " + classElement
                    .getQualifiedName().toString(), classElement);
            return false;
        }

        if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
            mLogger.error("The class is not public." + classElement
                    .getQualifiedName().toString(), classElement);
            return false;
        }

        if(classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            mLogger.error("The class is abstract. "
                    + classElement.getQualifiedName().toString()
                    + " You can't annotate abstract classes with "
                    + RouteProvider.class.getSimpleName(), classElement);
            return false;
        }

        return true;
    }
}
