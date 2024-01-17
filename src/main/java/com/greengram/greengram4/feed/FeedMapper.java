package com.greengram.greengram4.feed;

import com.greengram.greengram4.feed.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedMapper {
    int insFeed(FeedInsDto dto);
    List<FeedSelVo> selFeed(FeedSelProcVo dto);

    FeedDelDto selOneFeed(int ifeed);
    int delFeed(int ifeed);
    int delComment(int ifeed);
    int delPic(int ifeed);
    int delFav(int ifeed);
}
