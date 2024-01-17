package com.greengram.greengram4.feed;

import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.feed.model.FeedCommentInsDto;
import com.greengram.greengram4.feed.model.FeedCommentSelVo;
import com.greengram.greengram4.feed.model.FeedDelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed/comment")
public class FeedCommnetController {
    private final FeedCommentService service;

    @PostMapping
    public ResVo postFeedCommnet(@RequestBody FeedCommentInsDto dto){
        log.info("dto: {}", dto);
        return service.postFeedComment(dto);
    }

    @GetMapping
    public List<FeedCommentSelVo> getFeedCommentAll(int ifeed){

        return service.getFeedCommentAll(ifeed);

    }
    @DeleteMapping
    public ResVo delComment(FeedDelDto dto){
        return service.delComment(dto);
    }
}
