package com.telecom.ccs.utils.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XmlTask {
    private String provinceCode;
    private String serialNumber;
    private String sttFilePath;
    private String tid;


    @Override
    public String toString() {
        return "XmlTask{" +
                "provinceCode='" + provinceCode + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", sttFilePath='" + sttFilePath + '\'' +
                ", tid='" + tid + '\'' +
                '}';
    }
}
