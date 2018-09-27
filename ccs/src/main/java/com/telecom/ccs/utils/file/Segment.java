package com.telecom.ccs.utils.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Segment {
    private double startTime;
    private double endTime;

    @Override
    public String toString() {
        return "Segment{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
