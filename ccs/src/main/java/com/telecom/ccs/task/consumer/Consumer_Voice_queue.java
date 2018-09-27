package com.telecom.ccs.task.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.telecom.ccs.config.PropertiesConfig;
import com.telecom.ccs.config.SpringApplicationContextUtil;
import com.telecom.ccs.utils.file.TaskDto;
import com.telecom.ccs.utils.file.VoiceTask;
import com.telecom.ccs.utils.http.HttpOps;
import com.telecom.ccs.utils.http.ResponseInfo;
import com.telecom.ccs.utils.redis.RedisOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.telecom.ccs.utils.http.HttpOps.post;

public class Consumer_Voice_queue implements Runnable {

    private Logger logger = LoggerFactory.getLogger(Consumer_Voice_queue.class);

    private  String queueName;
    private String provinceCode;

    private PropertiesConfig propertiesConfig = SpringApplicationContextUtil.getBean("propertiesConfig",PropertiesConfig.class);

    public Consumer_Voice_queue(String queueName,String provinceCode){
        this.queueName = queueName;
        this.provinceCode = provinceCode;
    }
    @Override
    public void run() {

        while (true){

            TaskDto dto = getTaskFormVoiceQueue(queueName);

            if(dto!=null){
                // to do

                VoiceTask voiceTask = new VoiceTask();
                voiceTask.setProvinceCode(provinceCode);
                voiceTask.setSerialNumber(dto.getRecordedInfo()[0]);
                voiceTask.setWavFilePath(dto.getVoicePath());

                File file = new File(dto.getVoicePath());

                if(file.getParent().endsWith("wav")){
                    voiceTask.setToFilePath(file.getParent().substring(0,file.getParent().length()-3)+"stt/"+file.getName()+".xml");
                }else{
                    voiceTask.setToFilePath(file.getPath()+".xml");
                }



                ResponseInfo responseInfo =   HttpOps.post(propertiesConfig.getSystem_voice_engine_url(),JSON.toJSONString(voiceTask));
                logger.info("语音任务发送： "+voiceTask.toString()+" http post 调用结果： "+responseInfo.toString());

            }else{

                logger.warn("Redis queue:"+queueName+" has no task to pop.");
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
