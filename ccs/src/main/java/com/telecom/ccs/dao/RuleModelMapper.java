package com.telecom.ccs.dao;

import com.telecom.ccs.model.Test;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RuleModelMapper {

    public Test selectRulesByProvince(String province);
}
