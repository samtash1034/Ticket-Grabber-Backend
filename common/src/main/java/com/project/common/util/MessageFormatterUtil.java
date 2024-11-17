package com.project.common.util;

public class MessageFormatterUtil {

    public static String formatMessage(String message, Object[] args) {
        if (message != null && args != null) {
            message = String.format(message, args);
        }
        return message;
    }
}
