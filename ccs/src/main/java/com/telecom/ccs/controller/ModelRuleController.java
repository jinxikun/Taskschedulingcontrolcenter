package com.telecom.ccs.controller;

import com.telecom.ccs.model.Test;
import com.telecom.ccs.service.impl.RuleModelServiceImpl;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ComponentScan({"com.telecom.ccs.dao"})
public class ModelRuleController {

    Logger logger = LoggerFactory.getLogger(ModelRuleController.class);

    @Autowired
    private RuleModelServiceImpl service;

   @RequestMapping(value = "/service/rulemodel",method = RequestMethod.GET)
    public Test   select(){

       Test test = service.selectRulesByProvince("校长");

       return  test;
    }


}
