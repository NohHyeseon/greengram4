package com.greengram.greengram4.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoVo {
    private int feedCnt;
    private int favCnt;
    private String nm;
    private String createdAt;
    private String pic;
    private int follower; // 팔로워 수 (targetIuser를팔로우하는사람)
    private int following; //targetIuser가 팔로우하는사람
    private int followState;//1-loginedIuser가 targetIuser 팔로우만 한 상황
    //2-targetIuser가 loginIuser를 팔로우만 한 상황
    //3- 맞팔
}
