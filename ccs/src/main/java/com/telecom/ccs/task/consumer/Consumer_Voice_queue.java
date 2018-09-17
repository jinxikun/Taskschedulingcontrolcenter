package com.telecom.ccs.task.consumer;

import com.alibaba.fastjson.JSON;
import com.telecom.ccs.utils.file.TaskDto;
import com.telecom.ccs.utils.file.VoiceDto;
import com.telecom.ccs.utils.redis.RedisOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer_Voice_queue implements Runnable {

    private Logger logger = LoggerFactory.getLogger(Consumer_Voice_queue.class);

    private  String queueName;

    public Consumer_Voice_queue(String queueName){
        this.queueName = queueName;
    }
    @Override
    public void run() {

        while (true){

            TaskDto dto = getTaskFormVoiceQueue(queueName);

            if(dto!=null){
                // to do


            }else{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }







    public TaskDto getTaskFormVoiceQueue(String queueName){

            TaskDto dto = null;
            String  voiceTaskJson  = RedisOps.rightPop(queueName);
            if(voiceTaskJson!=null && !"".equals(voiceTaskJson)){
                 dto = (TaskDto) JSON.parse(voiceTaskJson);
            }

            return dto;
    }
}
