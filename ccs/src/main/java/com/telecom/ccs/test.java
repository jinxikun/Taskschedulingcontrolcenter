package com.telecom.ccs;

import ch.qos.logback.core.net.SyslogOutputStream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat rightsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = null;

        try {
            date = rightsdf.format(sdf.parse("20180917111111"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(date);

        Date date1 = null;
        try {
           date1 =  rightsdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(date1);

    }
}
