package com.telecom.ccs.entities.xml;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Function_long_silence {

    private Items_search items;


    @Override
    public String toString() {
        return "Function_long_silence{" +
                "items=" + items +
                '}';
    }
}
