package com.greengram.greengram4.user.model;

import lombok.Data;

@Data
public class UserSignupProcDto {
    private int iuser;
    private String uid;
    private String upw;
    private String nm;
    private String pic;


    public UserSignupProcDto(UserSignupDto dto){
        this.uid=dto.getUid();
        this.upw=dto.getUpw();
        this.nm=dto.getNm();
        this.pic=dto.getPic();
    }


}
