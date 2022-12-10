package com.banque.irrigationsystem.shared.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public static String getStringFormat(LocalDate date){
        return DATE_FORMATTER.format(date);
    }

}
