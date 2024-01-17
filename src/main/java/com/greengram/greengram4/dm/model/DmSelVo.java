package com.greengram.greengram4.dm.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DmSelVo {
    private int idm;
    private String lastMsg;
    private String lastMsgAt;
    private int otherPersonIuser;
    private String otherPersonNm;
    private String otherPersonPic;
}
