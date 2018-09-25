package com.telecom.ccs.utils.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseInfo {
    private String rtnCode;
    private String rtnMsg;

    @Override
    public String toString() {
        return "ResponseInfo{" +
                "rtnCode='" + rtnCode + '\'' +
                ", rtnMsg='" + rtnMsg + '\'' +
                '}';
    }
}
