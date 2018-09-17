package com.telecom.ccs.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestInfo {
    private String workId;
    private String workName;
    private String workScore;

    private List<TestModelRules> testModelRules;

    @Override
    public String toString() {
        return "TestInfo{" +
                "workId='" + workId + '\'' +
                ", workName='" + workName + '\'' +
                ", workScore='" + workScore + '\'' +
                ", testModelRules=" + testModelRules +
                '}';
    }
}
