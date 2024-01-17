package com.greengram.greengram4.feed.model;

import lombok.Data;

import java.util.List;

@Data
public class FeedSelVo { //전체피드 가져오기
    private int ifeed;
    private String contents;
    private String location;
    private String createdAt;
    private String writerIuser;
    private String writerNm;
    private String writerPic;
    private List<String> pics;

    private int isFav; //1:좋아요했음, 0:좋아요아님
    private List<FeedCommentSelVo> comments;
    private int isMoreComment;//0:댓글이 더 없엄 1:댓글이 더 있음
}
