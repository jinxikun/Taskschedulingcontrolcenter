package com.telecom.ccs.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestModelRules {
    private long  modelId;
    private String  modelName;
    private long  ruleId;
    private String  ruleName;
    private double startTime;
    private double endTime;
    private int  ruleScore;
    private int  matchStatus;
    private Date inputTime;
    private String  proNote;
    private Date  proTime;
    private long  proUserId;

    @Override
    public String toString() {
        return "TestModelRules{" +
                "modelId=" + modelId +
                ", modelName='" + modelName + '\'' +
                ", ruleId=" + ruleId +
                ", ruleName='" + ruleName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", ruleScore=" + ruleScore +
                ", matchStatus=" + matchStatus +
                ", inputTime=" + inputTime +
                ", proNote='" + proNote + '\'' +
                ", proTime=" + proTime +
                ", proUserId=" + proUserId +
                '}';
    }
}
