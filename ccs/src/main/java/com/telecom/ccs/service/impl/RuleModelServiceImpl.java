package com.telecom.ccs.service.impl;

import com.telecom.ccs.dao.RuleModelMapper;
import com.telecom.ccs.model.Test;
import com.telecom.ccs.service.RuleModelService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RuleModelServiceImpl extends SqlSessionDaoSupport implements RuleModelService {


    @Autowired
    private RuleModelMapper ruleModelMapper;

    @Override
    public Test selectRulesByProvince(String province) {
       return ruleModelMapper.selectRulesByProvince(province);
    }

    @Override
    @Resource
    public void setSqlSessionFactory(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }
}
