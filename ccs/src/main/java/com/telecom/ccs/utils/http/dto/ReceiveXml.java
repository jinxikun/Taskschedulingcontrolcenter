package com.telecom.ccs.utils.http.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveXml {
    private  String  engineNo;
    private  String  province;
    private  String  xmlPath;

    @Override
    public String toString() {
        return "ReceiveXml{" +
                "engineNo='" + engineNo + '\'' +
                ", province='" + province + '\'' +
                ", xmlPath='" + xmlPath + '\'' +
                '}';
    }
}
