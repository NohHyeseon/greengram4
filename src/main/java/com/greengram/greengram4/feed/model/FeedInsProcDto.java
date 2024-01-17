package com.greengram.greengram4.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class FeedInsProcDto {
    @JsonIgnore
    private int ifeed;
    @JsonIgnore
    private int iuser;
    private String contents;
    private String location;
    private List<String> pics;


    public FeedInsProcDto(FeedInsDto dto){

        this.iuser=dto.getIuser();
        this.contents=dto.getContents();
        this.location=dto.getLocation();
//        this.pics=dto.getPics();
    }

    public FeedInsProcDto() {


    }
}
