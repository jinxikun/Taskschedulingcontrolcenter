package com.telecom.ccs.utils.file.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 定时器线程池
 */

public class TheadExecutorScheduleUtil {

    private static Logger logger = LoggerFactory.getLogger(TheadExecutorScheduleUtil.class);



    private static  ScheduledExecutorService executorService = Executors.newScheduledThreadPool(32);  //线城市对应省份数



    public static  void executeScheduleTask(Runnable task){
        executorService.scheduleAtFixedRate(task,2,100,TimeUnit.SECONDS);   // 延迟周期时间参数后续进行修改

     }




}
