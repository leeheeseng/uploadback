package com.clonecod.clone.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class MethodNameConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        StackTraceElement[] callerData = event.getCallerData();
        if (callerData != null) {
            for (StackTraceElement element : callerData) {
                if (element.getClassName().startsWith("com.clonecod.clone")) {  // 패키지 이름 맞춤
                    return element.getMethodName();
                }
            }
        }
        return "unknown";
    }
}
