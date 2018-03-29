package com.router.utils;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class Logger {
    private Messager msg;

    public Logger(Messager messager) {
        msg = messager;
    }


    public void info(CharSequence info, Element element) {
        msg.printMessage(Diagnostic.Kind.NOTE, info, element);
    }

    public void error(CharSequence error, Element element) {
        msg.printMessage(Diagnostic.Kind.ERROR, error, element);
    }


    public void error(Throwable error, Element element) {
        msg.printMessage(Diagnostic.Kind.ERROR, "An exception is encountered, [" + error.getMessage() + "]" + "\n" + formatStackTrace(error.getStackTrace()), element);

    }

    public void warning(CharSequence warning, Element element) {
        msg.printMessage(Diagnostic.Kind.WARNING, warning, element);

    }

    private String formatStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            sb.append("    at ").append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public void d(String msg) {
        System.out.print(msg + "\n");
    }
}
