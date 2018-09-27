package com.telecom.ccs.utils.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleInfo {
    private long ruleId;
    private String ruleName;
    private List<Segment> voiceSegment;

    @Override
    public String toString() {
        return "RuleInfo{" +
                "ruleId=" + ruleId +
                ", ruleName='" + ruleName + '\'' +
                ", voiceSegment=" + voiceSegment +
                '}';
    }
}
