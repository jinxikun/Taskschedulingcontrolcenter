package com.telecom.ccs.task;

import com.alibaba.fastjson.JSON;
import com.telecom.ccs.config.PropertiesConfig;
import com.telecom.ccs.config.SpringApplicationContextUtil;
import com.telecom.ccs.entities.*;
import com.telecom.ccs.utils.elasticsearch.ElasticSearchOps;
import com.telecom.ccs.utils.file.TaskDto;
import com.telecom.ccs.utils.redis.RedisOps;
import com.telecom.ccs.utils.task.ProvinceScanPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OneProvinceTask  implements  Runnable{

    private Logger logger  = LoggerFactory.getLogger(OneProvinceTask.class);


    private PropertiesConfig propertiesConfig = SpringApplicationContextUtil.getBean("propertiesConfig",PropertiesConfig.class);

    private String province;
    public  OneProvinceTask(String province){
        this.province = province;
    }
    @Override
    public void run() {

        try {


            logger.info("task province:" + province + " is started ...");

            ProvinceScanPath provinceScanPath = new ProvinceScanPath(province);
            String scanPath = provinceScanPath.getCurrentScanPath(propertiesConfig.getSystem_ftp_relativePath());
            logger.info("Warning: province scan dir: " + scanPath);


            ArrayList<TaskDto> list = new ScanVoicetask().run(propertiesConfig.getSystem_ftp_server(), Integer.parseInt(propertiesConfig.getSystem_ftp_port()), propertiesConfig.getSystem_ftp_username(), propertiesConfig.getSystem_ftp_password(), scanPath);
            if (list == null) {
                logger.warn("Warning: Province:" + province + " no task to do !");
                return;
            }

            for (TaskDto taskDto : list) {

                VoiceInfo vi = tool(taskDto);


                CCS ccs = new CCS();
                ccs.setSerialNumber(taskDto.getRecordedInfo()[0]);
                ccs.setAudioPath(taskDto.getVoicePath());
                ccs.setVoiceInfo(vi);
                SttInfo sttInfo = new SttInfo();
                List<BusinessTag> businessTags = new ArrayList<BusinessTag>();
                List<RuleInfo> ruleInfos = new ArrayList<RuleInfo>();
                List<WorkScore> workScores = new ArrayList<WorkScore>();
                List<KeyWordsClustering> wordFrequency = new ArrayList<KeyWordsClustering>();
                List<TestInfo> testInfos = new ArrayList<TestInfo>();

                ccs.setSttInfo(sttInfo);
                ccs.setBusinessTags(businessTags);
                ccs.setRuleInfos(ruleInfos);
                ccs.setWorkScores(workScores);
                ccs.setWordFrequency(wordFrequency);
                ccs.setTestInfos(testInfos);

                logger.info("创建任务到缓存队列。。。" + JSON.toJSONString(vi));

                try {
                    ElasticSearchOps.insertJsonData(province, "task", taskDto.getRecordedInfo()[0], JSON.toJSONString(ccs));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                RedisOps.leftPush(province + "_voice_queue", JSON.toJSONString(taskDto));

                logger.info("redis queue:" + province + " leftpush ,json: " + JSON.toJSONString(taskDto));

            }


        }catch (Exception e){
            logger.error("CCS Error: system exception, please check your program.");
            e.printStackTrace();
        }

    }


    public VoiceInfo tool(TaskDto info){
        String[] in = info.getRecordedInfo();
        VoiceInfo vi  = new VoiceInfo();
        vi.setAudioSize(Long.parseLong(in[1]));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat rightsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = null;
        try {
            date = rightsdf.format(sdf.parse(in[2]));
        } catch (ParseException e) {
            e.printStackTrace();
        }


            vi.setCallStartTime(date);

        vi.setHoldDuration(Long.parseLong(in[3]));     // 通话时长通话时长

        try {
            date = rightsdf.format(sdf.parse(in[4]));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        vi.setCallEndTime(date);

        vi.setSeatGroup(in[5]);
        vi.setSeatId(in[6]);
        vi.setSeatName(in[7]);
        vi.setCustomerNumber(in[8]);
        vi.setSeatNumber(in[9]);
        vi.setCallDirection(Long.parseLong(in[10]));  // 呼叫方向
        vi.setProPhoneNum(in[11]);
        vi.setIsEachRecord(in[12]);
        vi.setOnHook(in[13]);
        vi.setCallerloc(in[14]);
        vi.setCustomerStart(in[15]);
        vi.setSatisfaction(in[16]);
        vi.setReCallFlag(Integer.parseInt(in[17]));
        // in[18] 为话务小结， 暂时无法确定
        // in[19] 为关联工单号， 暂时无法确定
        vi.setSendMsg(Integer.parseInt(in[20]));
        vi.setMessage(in[21]);


        return vi;




    }
}
