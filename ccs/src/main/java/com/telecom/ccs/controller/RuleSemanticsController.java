package com.telecom.ccs.controller;

import com.telecom.ccs.utils.file.RuleSemanticInfo;
import com.telecom.ccs.utils.http.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RuleSemanticsController {

    Logger logger = LoggerFactory.getLogger(RuleSemanticsController.class);

    @RequestMapping(value = "/service/ruleSemantics",method = RequestMethod.POST)
    public ResponseInfo receive(RuleSemanticInfo info){

        logger.info("/service/ruleSemantics is called, info:"+info.toString());
        ResponseInfo responseInfo = new ResponseInfo("0","");

        return  responseInfo;
    }


}
