package com.greengram.greengram4.feed;

import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.feed.model.FeedCommentInsDto;
import com.greengram.greengram4.feed.model.FeedCommentSelDto;
import com.greengram.greengram4.feed.model.FeedCommentSelVo;
import com.greengram.greengram4.feed.model.FeedDelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedCommentService {
    private final FeedCommentMapper mapper;



    public ResVo postFeedComment(FeedCommentInsDto dto){
        int affectedRows= mapper.insFeedComment(dto);
        ResVo vo = new ResVo(dto.getIfeedComment());
        return  vo;
    }
    public List<FeedCommentSelVo> getFeedCommentAll(int ifeed){
        FeedCommentSelDto dto1=new FeedCommentSelDto();
        dto1.setIfeed(ifeed);
        dto1.setStartIdx(3);
        dto1.setRowCount(999);
        return mapper.selFeedCommentAll(dto1);
    }
    public ResVo delComment(FeedDelDto dto){
        ResVo vo = new ResVo(mapper.delComment(dto));
        return vo;
    }

}
