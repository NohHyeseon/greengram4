package com.greengram.greengram4.dm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class DmMsgSelDto {
    private int page;
    private int idm;
    @JsonIgnore
    private int startIdx;
    @JsonIgnore
    private int rowCount;

    public DmMsgSelDto(int page, int idm){
        this.rowCount = 10;
        this.page = page;
        this.startIdx = (page - 1) * rowCount;
        this.idm = idm;
    }
}
