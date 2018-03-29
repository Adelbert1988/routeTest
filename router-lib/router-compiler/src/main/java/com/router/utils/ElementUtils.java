package com.router.utils;

import com.squareup.javapoet.ClassName;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

public class ElementUtils {


    public static ClassName getClassName(TypeMirror typeMirror) {
        String fullyQualifiedClassName = typeMirror.toString();
        String simpleClassName = getSimpleName(fullyQualifiedClassName);
        String packageName = getPackageName(fullyQualifiedClassName);
        return ClassName.get(packageName, simpleClassName);
    }


    public static String getName(ClassName className) {
        return className.packageName()+"."+ className.simpleName();
    }

    /*public static String getClassName(Element element) {
        try {
            Class<?> clazz = annotation.type();
            qualifiedSuperClassName = clazz.getCanonicalName();
            simpleTypeName = clazz.getSimpleName();
        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            qualifiedSuperClassName = classTypeElement.getQualifiedName().toString();
            simpleTypeName = classTypeElement.getSimpleName().toString();
        }
    }*/


    private static String getPackageName(String fullyQualifiedClassName) {
        int dotIndex = fullyQualifiedClassName.lastIndexOf(".");
        return fullyQualifiedClassName.substring(0, dotIndex);
    }


    private static String getSimpleName(String fullyQualifiedClassName) {
        int dotIndex = fullyQualifiedClassName.lastIndexOf(".");
        return fullyQualifiedClassName.substring(dotIndex + 1, fullyQualifiedClassName.length());
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.size() == 0;
    }

}
