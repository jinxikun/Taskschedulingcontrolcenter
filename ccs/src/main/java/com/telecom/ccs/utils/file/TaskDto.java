package com.telecom.ccs.utils.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private String province;
    private String voicePath;
    private long voieceSize;
    private String txtPath;
    private String[] recordedInfo;

    public TaskDto(String voicePath, long voieceSize, String txtPath, String[] recordedInfo) {
        this.voicePath = voicePath;
        this.voieceSize = voieceSize;
        this.txtPath = txtPath;
        this.recordedInfo = recordedInfo;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "province='" + province + '\'' +
                ", voicePath='" + voicePath + '\'' +
                ", voieceSize=" + voieceSize +
                ", txtPath='" + txtPath + '\'' +
                ", recordedInfo=" + Arrays.toString(recordedInfo) +
                '}';
    }
}
