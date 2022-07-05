package com.wuxianggujun.annotation.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class Logger {
    private Messager messager;

    public Logger(Messager messager) {
        this.messager = messager;
    }

    public void i(CharSequence info) {
        messager.printMessage(Diagnostic.Kind.NOTE, info);
    }


    public void e(CharSequence error) {
        messager.printMessage(Diagnostic.Kind.ERROR, error);
    }

  public void w(CharSequence warning) {
        messager.printMessage(Diagnostic.Kind.WARNING, warning);
    }

    public void e(Throwable error) {
        messager.printMessage(Diagnostic.Kind.ERROR, "Catch Exception : [" + error.getMessage() + "]\n" + loadStackTrace(error));
    }


    private String loadStackTrace(Throwable error) {
        StringBuilder stringBuilder = new StringBuilder();
        StackTraceElement[] stackTrace = error.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            stringBuilder.append("\tat")
                    .append(stackTraceElement.toString())
                    .append("\n");
        }
        return stringBuilder.toString();
    }
}
