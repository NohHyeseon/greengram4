package com.greengram.greengram4.feed;

import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.feed.model.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class FeedController {
    private final FeedService service;

    @Operation(summary = " 피드등록 ", description = "피드등록처리")
    @PostMapping
    //public ResVo postFeed(@RequestBody FeedInsDto dto) {
    public FeedPicsInsDto postFeed(@RequestPart List<MultipartFile> pics, @RequestPart FeedInsDto dto) {
        log.info("pics: {}", pics);
        log.info("dto: {}", dto);
        dto.setPics(pics);
        return service.postFeed(dto);

//        System.out.println(vo.getResult());
//        return vo;
    }


    @GetMapping
    public List<FeedSelVo> selFeed(FeedSelProcVo dto) {
        log.info("dto = {}", dto);
        return service.selFeed(dto);
    }

    @GetMapping("/fav")
    public ResVo toggleFeedFav(FeedFavDto dto) {
        return service.toggleFeedFav(dto);

    }

    @DeleteMapping
    public ResVo delFeed(FeedDelDto dto) {
        log.info("dto: {}", dto);
        return service.delFeed(dto);
    }

}
