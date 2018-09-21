package com.telecom.ccs.task;

import com.telecom.ccs.config.PropertiesConfig;
import com.telecom.ccs.config.SpringApplicationContextUtil;
import com.telecom.ccs.utils.file.thread.TheadExecutorScheduleUtil;
import com.telecom.ccs.utils.file.thread.ThreadExecutorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

public class CcsTaskDispatch  implements Runnable{

    protected Logger logger = LoggerFactory.getLogger(CcsTaskDispatch.class);

    private PropertiesConfig propertiesConfig = SpringApplicationContextUtil.getBean("propertiesConfig",PropertiesConfig.class);


    @Override
    public void run() {

        logger.info("各省份质检调度任务开始执行 ...");


        //山东
        if(propertiesConfig.isSystem_dispatch_shandong()){
          TheadExecutorScheduleUtil.executeScheduleTask(new OneProvinceTask("shandong"));
          //  TheadExecutorScheduleUtil.executeScheduleTask2(new OneProvinceTask("shandong"));
/*          while (true) {

              logger.info("cycle...");
              try {
                  future.get();
              } catch (InterruptedException e) {
                  logger.error("interruptedException");
                  e.printStackTrace();
              } catch (ExecutionException e) {
                  logger.error(" shandong schedule task ExecutionException!");
                  e.printStackTrace();
                  future.cancel(true);
                  logger.error(" shandong schedule task reboot!");
                  future = TheadExecutorScheduleUtil.executeScheduleTask(new OneProvinceTask("shandong"));
              }

          }*/

        }


        //河北
        if(propertiesConfig.isSystem_dispatch_hebei()){
            TheadExecutorScheduleUtil.executeScheduleTask(new OneProvinceTask("hebei"));
        }


        //北京
        if(propertiesConfig.isSystem_dispatch_beijing()){
            TheadExecutorScheduleUtil.executeScheduleTask(new OneProvinceTask("beijing"));
        }



        //北京
        if(false){
            TheadExecutorScheduleUtil.executeScheduleTask(new OneProvinceTask("北京"));
        }






    }




}
