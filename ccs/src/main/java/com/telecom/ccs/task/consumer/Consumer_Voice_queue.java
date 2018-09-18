package com.telecom.ccs.task.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.telecom.ccs.utils.file.TaskDto;
import com.telecom.ccs.utils.http.HttpOps;
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


                String text = "天气 不错";
                String url = "http://192.168.14.203:80/tts.php?text="+text+"";

                logger.error("Thread_name: "+Thread.currentThread().getName());

                byte[] buff =  HttpOps.get(url);
                logger.info("语音合成返回：测试http 调用 ");

            }else{
                try {
                    Thread.sleep(5000);
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
                logger.info("voicejson: "+voiceTaskJson);
                 dto = (TaskDto) JSON.parseObject(voiceTaskJson,new TypeReference<TaskDto>(){});
            }

            return dto;
    }
}
