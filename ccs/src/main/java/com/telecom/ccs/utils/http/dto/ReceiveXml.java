package com.telecom.ccs.utils.http.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveXml {
    private  String  serialNumber;
    private  String  sttFilePath;
    private  String  provinceCode;
    private  String  rtnCode;
    private  String  rtnMsg;

    @Override
    public String toString() {
        return "ReceiveXml{" +
                "serialNumber='" + serialNumber + '\'' +
                ", sttFilePath='" + sttFilePath + '\'' +
                ", province='" + provinceCode + '\'' +
                ", rtnCode='" + rtnCode + '\'' +
                ", rtnMsg='" + rtnMsg + '\'' +
                '}';
    }
}
