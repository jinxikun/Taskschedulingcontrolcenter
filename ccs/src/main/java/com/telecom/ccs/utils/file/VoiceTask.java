package com.telecom.ccs.utils.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoiceTask {
    private String serialNumber;
    private String provinceCode;
    private String wavFilePath;
    private String toFilePath;

    @Override
    public String toString() {
        return "VoiceTask{" +
                "serialNumber='" + serialNumber + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", wavFilePath='" + wavFilePath + '\'' +
                ", toFilePath='" + toFilePath + '\'' +
                '}';
    }
}
