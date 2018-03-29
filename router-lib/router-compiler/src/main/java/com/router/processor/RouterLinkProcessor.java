package com.router.processor;

import com.google.auto.service.AutoService;
import com.router.annotation.RouteLink;
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
import javax.lang.model.util.Elements;

/**
 * User: chw
 * Date: 2018/1/22
 */
@AutoService(Processor.class)
public class RouterLinkProcessor extends AbstractProcessor{

    private static final String MAPPING_PREFIX = "Router$$";
    private static final String MAPPING_SUFFIX = "$$ActivityMapping";

    private static final String AOP_LINK_METHOD = RouteConstans.ROUTE_MAPPING_MANAGER + ".initLinkMapping()";

    private Logger mLogger;
    private Filer mFiler;
    private Messager mMessager;
    private Elements mElementUtils;

    private String mModuleName = "";


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();

        mLogger = new Logger(mMessager);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(RouteLink.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        mLogger.d("routeLink: " + RouteLink.class.getCanonicalName());

        Map<String, String> options = processingEnv.getOptions();
        if (options != null && options.size() > 0) {
            mModuleName = options.get(RouteConstans.MODULE_NAME);
        }

        Set<? extends Element> annotationElements = roundEnvironment.getElementsAnnotatedWith(RouteLink.class);

        List<Element> annotationList = new ArrayList<>();
        for (Element annotationElement : annotationElements) {
            processClassElement(annotationList, annotationElement);
        }

        if (annotationList.size() == 0) {
            mLogger.d("don't find RouteLink annotate activity");
            return false;
        }

        try {
            generateRouteLinkCode(annotationList);
        } catch (IOException e) {
            e.printStackTrace();
            mLogger.error("Error creating mapping file", annotationList.get(0));
        }
        return true;
    }

    /**
     * 生成activity路由映射类
     * @param list
     * @throws IOException
     */
    private void generateRouteLinkCode(List<Element> list) throws IOException {
        ClassName mappingClassName = getModuleMappingClassName(list.get(0));
        MethodSpec addRouteLinkSpec = generateRouterActivity(list);

        TypeSpec.Builder mappingClassBuilder = TypeSpec.classBuilder(mappingClassName)
                .addAnnotation(AnnotationSpec.builder(Aspect.class).build())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(addRouteLinkSpec);

        JavaFile.builder(ClassName.get(((TypeElement)list.get(0))).packageName(), mappingClassBuilder.build())
                .build()
                .writeTo(mFiler);
    }

    /**
     * 批量添加activity映射
     * @param list
     * @return
     */
    private MethodSpec generateRouterActivity(List<Element> list) {
        CodeBlock.Builder routeInfoListBlock = CodeBlock.builder();
        for (Element element : list) {
            RouteLink routeLink = element.getAnnotation(RouteLink.class);
            ClassName destClass = ElementUtils.getClassName(element.asType());
            mLogger.d("qualifiedName: " + destClass.simpleName());
            routeInfoListBlock.add("$T.getInstance().addLinkMapping($S, $T.build($S, $T.class, $S, "
                            + routeLink.isNeedLogin() + ", " +  routeLink.extra() + "));\n"
                    , RouteConstans.ROUTE_MAPPING_MANAGER, routeLink.value()
                    , RouteConstans.ROUTE_INFO, routeLink.value(), destClass, routeLink.name());
        }

        MethodSpec.Builder addRouteLinkMethod = MethodSpec
                .methodBuilder("addRouterLink")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(
                        AnnotationSpec.builder(After.class)
                                .addMember("value", "$S", "execution(* " + AOP_LINK_METHOD + ")")
                                .build())
                .addCode(routeInfoListBlock.build());

        return addRouteLinkMethod.build();

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
     * 遍历出被RouteLink注解的类
     * @param list
     * @param annotationElement
     */
    private void processClassElement(List<Element> list, Element annotationElement) {
        TypeElement typeElement = (TypeElement) annotationElement;
        if (!isValidClass(typeElement)) {
            mLogger.error("logError happen in RouterLinkProcessor, " +
                    RouteLink.class.getSimpleName() + " must annotation on class", annotationElement);
        }

        RouteLink activityAnnotation = typeElement.getAnnotation(RouteLink.class);
        String routePath = activityAnnotation.value();

        if ("".equals(routePath)) {
                /*throw new IllegalArgumentException(
                        String.format("id() in @%s for class %s is null or empty! that's not allowed",
                                RouteLink.class.getSimpleName(),
                                typeElement.getQualifiedName().toString                                                                                                     ng()));*/
            mLogger.error("error happen in RouterLinkProcessor, " +
                    RouteLink.class.getSimpleName() + " value can not be null ", annotationElement);
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
                    + RouteLink.class.getSimpleName(), classElement);
            return false;
        }

        return true;
    }
}
