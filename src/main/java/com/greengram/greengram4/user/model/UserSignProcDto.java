package com.greengram.greengram4.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignProcDto {
    private int iuser;
    private String upw;
    private String nm;
    private String pic;
    private String firebaseToken;
}

