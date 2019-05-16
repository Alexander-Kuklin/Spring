package com.epam.spring.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MyTime {

    public static Timestamp now() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

    private MyTime() {
    }
}
