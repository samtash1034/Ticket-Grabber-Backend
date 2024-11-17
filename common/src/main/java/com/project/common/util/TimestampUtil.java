package com.project.common.util;

import java.sql.Timestamp;

public class TimestampUtil {

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
