package com.learnboot.universalpetcare.utils;

import java.util.Calendar;
import java.util.Date;

public class SystemUtil {

    private static final int EXPIRATION_TIME = 10;

    public static Date getExpirationDate() {
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(new Date().getTime()+EXPIRATION_TIME);
        calender.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calender.getTime().getTime());

    }

}
