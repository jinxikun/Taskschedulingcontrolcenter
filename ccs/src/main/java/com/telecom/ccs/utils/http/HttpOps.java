package com.telecom.ccs.utils.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
public class HttpOps {

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
}
