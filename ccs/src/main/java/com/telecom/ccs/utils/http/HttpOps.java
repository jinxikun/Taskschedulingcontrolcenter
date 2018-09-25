package com.telecom.ccs.utils.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
public class HttpOps {

    private static Logger logger = LoggerFactory.getLogger(HttpOps.class);

    @Autowired
    private RestTemplate restTemplate;

    private static RestTemplate myrestTemplate;

    @PostConstruct
    public void init(){
        myrestTemplate = restTemplate;
    }

    public  static  byte[] get(String url){

        byte[] buff = myrestTemplate.getForObject(url,byte[].class);

        return buff;
    }



    public static  ResponseInfo  post(String url,String json){
       ResponseInfo responseInfo =  myrestTemplate.postForObject(url,json, ResponseInfo.class);
       logger.info(responseInfo.toString());

       return responseInfo;
    }
}
