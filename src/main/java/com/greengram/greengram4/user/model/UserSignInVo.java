package com.greengram.greengram4.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInVo {
    private int iuser;
    private int result;
    private String nm;
    private String pic;
    private String firebaseToken;
    private String accessToken;


}
