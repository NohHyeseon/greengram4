package com.greengram.greengram4.dm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class DmSelDto {
    private int loginedIuser;
    private int page;


    @JsonIgnore
    private int startIdx;
    @JsonIgnore
    private int rowCount;

}
