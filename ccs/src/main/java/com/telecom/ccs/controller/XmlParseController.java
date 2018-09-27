package com.telecom.ccs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.telecom.ccs.config.PropertiesConfig;
import com.telecom.ccs.entities.CCS;
import com.telecom.ccs.entities.SttInfo;
import com.telecom.ccs.utils.elasticsearch.ElasticSearchOps;
import com.telecom.ccs.utils.file.Dom4jUtil;
import com.telecom.ccs.utils.file.XmlTask;
import com.telecom.ccs.utils.http.ResponseInfo;
import com.telecom.ccs.utils.http.dto.ReceiveXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
public class XmlParseController {

    private Logger logger = LoggerFactory.getLogger(XmlParseController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PropertiesConfig propertiesConfig;

    @RequestMapping(value = "/service/xmlparse",method = RequestMethod.POST)
    public ResponseInfo   receiveXml(@RequestBody ReceiveXml info){

        logger.info("XmlParseController.receiveXml()请求参数："+info.toString());

        //① 接收并解析 xml  ， 更新 elasticsearch

        ResponseInfo responseInfo = new ResponseInfo();

        Dom4jUtil  dom = new Dom4jUtil();
        SttInfo sttInfo =  dom.parseSttInfo(info.getSttFilePath());

        logger.info("sttInfo: "+sttInfo.toString());

        String result =  ElasticSearchOps.searchDate(info.getProvinceCode(),"task",info.getSerialNumber());

        CCS ccs = null;
        if(result!=null && !"".equals(result)){
           ccs =  JSON.parseObject(result,new TypeReference<CCS>(){});
        }

        if(ccs==null){
            responseInfo.setRtnCode("-9999");
            responseInfo.setRtnMsg("es 查询结果未空");
            return responseInfo;
        }

        ccs.setSttInfo(sttInfo);


        try {
            ElasticSearchOps.insertJsonData(info.getProvinceCode(),"task",info.getSerialNumber(),JSON.toJSONString(ccs));
        } catch (IOException e) {
            e.printStackTrace();
        }





        //② 调用规则语义服务

        XmlTask xmlTask = new XmlTask();
        xmlTask.setProvinceCode(info.getProvinceCode());
        xmlTask.setSerialNumber(info.getSerialNumber());
        xmlTask.setSttFilePath(info.getSttFilePath());
        xmlTask.setTid("tid test");

        ResponseInfo responseInfoc = null;
        try {
             responseInfoc = restTemplate.postForObject(propertiesConfig.getSystem_voice_engine_url(), JSON.toJSONString(xmlTask), ResponseInfo.class);
        }catch (Exception e){
            responseInfo.setRtnCode("-9999");
            responseInfo.setRtnMsg("rest call xml_engine_server exception: "+ e.getMessage());
            return responseInfo;
        }


        logger.info("rest call xml_engine_server's response: "+responseInfoc.toString());

       responseInfo.setRtnCode("0");
       responseInfo.setRtnMsg("任务接收并下发规则语义服务");

       return responseInfo;

    }

}
