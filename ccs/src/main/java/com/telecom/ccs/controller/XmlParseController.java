package com.telecom.ccs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.telecom.ccs.entities.CCS;
import com.telecom.ccs.entities.SttInfo;
import com.telecom.ccs.utils.elasticsearch.ElasticSearchOps;
import com.telecom.ccs.utils.file.Dom4jUtil;
import com.telecom.ccs.utils.http.dto.ReceiveXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class XmlParseController {

    private Logger logger = LoggerFactory.getLogger(XmlParseController.class);

    @RequestMapping(value = "/service/xmlparse",method = RequestMethod.POST)
    public boolean receiveXml(@RequestBody ReceiveXml info){

        logger.info("XmlParseController.receiveXml()请求参数："+info.toString());

        // to do 解析 xml  ， 更新 elasticsearch , 发送任务给质检引擎

        Dom4jUtil  dom = new Dom4jUtil();
        SttInfo sttInfo =  dom.parseSttInfo("");

        logger.info("sttInfo: "+sttInfo.toString());

        String result =  ElasticSearchOps.searchDate("shandong","task","123432011");

        CCS ccs = null;
        if(result!=null && !"".equals(result)){
           ccs =  JSON.parseObject(result,new TypeReference<CCS>(){});
        }

        if(ccs==null){
            return false;
        }

        ccs.setSttInfo(sttInfo);


        try {
            ElasticSearchOps.insertJsonData("shandong","task","1234320",JSON.toJSONString(ccs));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;

    }

}
