package com.telecom.ccs.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoiceInfo {

    private   long audioSize;
    private String callStartTime  ;
    private String callEndTime  ;
    private long  holdDuration;
    private String  customerNumber;
    private String  seatNumber;
    private long  callDirection;
    private String  groupId;
    private String  seatGroup;
    private String  seatId;
    private String  seatName;
    private String  proPhoneNum;
    private String  inputTime;

    private String  province;
    private String  isEachRecord;
    private String  onHook;
    private String  callerloc;
    private String  customerStart;
    private String  satisfaction;
    private String  dissatisfactionMsg;
    private int  reCallFlag;
    private String workNumber;
    private int sendMsg;
    private String message;
    private int proStatus;


    @Override
    public String toString() {
        return "VoiceInfo{" +
                "audioSize=" + audioSize +
                ", callStartTime=" + callStartTime +
                ", callEndTime=" + callEndTime +
                ", holdDuration=" + holdDuration +
                ", customerNumber='" + customerNumber + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", callDirection=" + callDirection +
                ", groupId='" + groupId + '\'' +
                ", seatGroup='" + seatGroup + '\'' +
                ", seatId='" + seatId + '\'' +
                ", seatName='" + seatName + '\'' +
                ", proPhoneNum='" + proPhoneNum + '\'' +
                ", inputTime=" + inputTime +
                ", province='" + province + '\'' +
                ", isEachRecord='" + isEachRecord + '\'' +
                ", onHook='" + onHook + '\'' +
                ", callerloc='" + callerloc + '\'' +
                ", customerStart='" + customerStart + '\'' +
                ", satisfaction='" + satisfaction + '\'' +
                ", dissatisfactionMsg='" + dissatisfactionMsg + '\'' +
                ", reCallFlag=" + reCallFlag +
                ", workNumber='" + workNumber + '\'' +
                ", sendMsg=" + sendMsg +
                ", message='" + message + '\'' +
                ", proStatus='" + proStatus + '\'' +
                '}';
    }
}
