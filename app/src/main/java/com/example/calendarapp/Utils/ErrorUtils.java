package com.example.calendarapp.Utils;

public class ErrorUtils {
    public static String getCause(Throwable e){
        return e.getCause() != null
                ? e.getCause().getMessage()
                : e.getMessage();
    }
}
