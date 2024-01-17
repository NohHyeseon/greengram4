package com.greengram.greengram4.feed.model;

import lombok.Data;

import java.util.List;

@Data
public class FeedPicProcDto {
    private int ifeed;
    private List<String> pics;


    public FeedPicProcDto(FeedInsProcDto dto){
        this.ifeed=dto.getIfeed();
        this.pics=dto.getPics();
    }

    public FeedPicProcDto() {

    }
}
