package com.telecom.ccs.utils.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceScanPath {

    private String province;

    /**
     * 得到文件遍历目录
     */
    public  String getCurrentScanPath(String prefixPath,int period) {

        //使用Date
        Date date = null;
        date =  getBeforeByHourTime(period);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = sdf.format(date);

        String year = datetime.substring(0,4);
        String month = datetime.substring(5,7);
        String day = datetime.substring(8,10);
        String hour = datetime.substring(11,13);

        String relativePath =prefixPath+"/"+province+"/"+year+"/"+month+"/"+day+"/"+hour+"/";

       return relativePath;


    }


    /**
     * 得到当前时间的前N小时
     */
    public Date getBeforeByHourTime(int ihour){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - ihour);
        return calendar.getTime();

    }


}
