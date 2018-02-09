package com.wzh.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDateFormat {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private static Calendar calendar = Calendar.getInstance();

    public static String getDateStr(){
        Date time = calendar.getTime();
        String date = MyDateFormat.format.format(time);
        return date;
    }

}
