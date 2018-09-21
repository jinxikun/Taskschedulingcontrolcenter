package com.telecom.ccs.service;

import com.telecom.ccs.model.Test;

public interface RuleModelService  {

    public Test selectRulesByProvince(String province);
}
