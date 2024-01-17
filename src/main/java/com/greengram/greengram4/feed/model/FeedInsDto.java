package com.greengram.greengram4.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class FeedInsDto {
    @JsonIgnore
    private int iuser;
    @JsonIgnore
    private int ifeed;
    private String contents;
    private String location;
    @JsonIgnore
    private List<MultipartFile> pics;



}
