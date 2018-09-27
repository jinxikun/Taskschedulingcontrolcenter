package com.telecom.ccs.utils.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleSemanticInfo {
    private String serialNumber;
    private String provinceCode;
    private String tid; // 任务规则组标识
    private String rtnCode;
    private String rtnMsg;
    private List<CallTag> callTags;
    private List<RuleInfo> ruleInfos;

    @Override
    public String toString() {
        return "RuleSemanticInfo{" +
                "serialNumber='" + serialNumber + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", tid='" + tid + '\'' +
                ", rtnCode='" + rtnCode + '\'' +
                ", rtnMsg='" + rtnMsg + '\'' +
                ", callTags=" + callTags +
                ", ruleInfos=" + ruleInfos +
                '}';
    }
}
