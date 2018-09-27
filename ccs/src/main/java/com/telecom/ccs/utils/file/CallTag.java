package com.telecom.ccs.utils.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallTag {
    private long tagId;
    private String tagName;

    @Override
    public String toString() {
        return "CallTag{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}
