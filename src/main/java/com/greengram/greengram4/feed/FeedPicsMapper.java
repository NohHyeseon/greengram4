package com.greengram.greengram4.feed;


import com.greengram.greengram4.feed.model.FeedPicProcDto;
import com.greengram.greengram4.feed.model.FeedPicsInsDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedPicsMapper {
    int insPic(FeedPicsInsDto dto);
    List<String> selFeedPic(int ifeed);
    int delPic(int ifeed);

}
