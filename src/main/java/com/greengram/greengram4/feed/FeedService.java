package com.greengram.greengram4.feed;


import com.greengram.greengram4.common.MyFileUtils;
import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.exception.FeedErrorCode;
import com.greengram.greengram4.exception.RestApiException;
import com.greengram.greengram4.feed.model.*;
import com.greengram.greengram4.security.AuthenticationFacade;
import com.greengram.greengram4.security.MyPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.greengram.greengram4.common.Const.FEED_COMMENT_FIRST_CNT;

@Slf4j
@RequiredArgsConstructor
@Service
public class FeedService {
    private final FeedMapper mapper;
    private final FeedPicsMapper pmapper;
    private final FeedFavMapper fmapper;
    private final FeedCommentMapper cmapper;
    private final AuthenticationFacade authenticationFacade; //로그인한 아이유저값이 들어감 DI받게 해주는것
    private final MyFileUtils myFileUtils;

    @Transactional //만약 맵퍼 두부분중 한군데가 실패하면 성공한부분 롤백되고 아예 에러터짐
    public FeedPicsInsDto postFeed(FeedInsDto dto) {
        if(dto.getPics() == null){
            throw new RestApiException(FeedErrorCode.PICS_MORE_THEN_ONE);
        }

        dto.setIuser(authenticationFacade.getLoginUserPk()); //Pk받는부분
        log.info("dto: {}", dto);

        int feedAffectedRows = mapper.insFeed(dto);
        String target = "/feed/" + dto.getIfeed();

        FeedPicsInsDto dto2 = new FeedPicsInsDto();
        dto2.setIfeed(dto.getIfeed());
        for (MultipartFile file : dto.getPics()) {
            String saveFileNm = myFileUtils.transferTo(file, target);
            dto2.getPics().add(saveFileNm);
        }
        int feedPicsAffectedRows = pmapper.insPic(dto2);

//        // Test시 맵퍼 xml 파일과 연결이 안되서 일을 못하니까 테스트에서 값을 넣어서 보내줌
//        MyPrincipal myPrincipal = new MyPrincipal();
//
//        log.info("feedAffectedRows, {}", feedAffectedRows);
//        FeedPicProcDto pic = new FeedPicProcDto(dto1);
//        int feedAffectedRows2 = pmapper.insPic(pic);
//        log.info("feedAffectedRows2, {}", feedAffectedRows2);
//        ResVo vo = new ResVo(pic.getIfeed());
        return dto2;
    }

    public List<FeedSelVo> selFeed(FeedSelProcVo dto) {

        List<FeedSelVo> feeds = (mapper.selFeed(dto)); // 사진과 코멘트는 가지고오지않음
        log.info("feeds {}", feeds);
        FeedCommentSelDto fcDto = new FeedCommentSelDto();
        fcDto.setStartIdx(0);
        fcDto.setRowCount(4);
        for (FeedSelVo feed : feeds) { //왼쪽이 하나값 오른쪽이 여러개피드
            List<String> pics = pmapper.selFeedPic(feed.getIfeed());


            log.info("pics {}", pics);

            // 피드에 사진 넣어야됨
            // 넣음 당해야 되는 피드 : feed
            // 넣어 져야 하는 사진 : pics
            feed.setPics(pics);


            fcDto.setIfeed(feed.getIfeed());
            List<FeedCommentSelVo> comments = cmapper.selFeedCommentAll(fcDto);
            feed.setComments(comments);


            if (comments.size() == FEED_COMMENT_FIRST_CNT) {//4
                feed.setIsMoreComment(1);
                comments.remove(comments.size() - 1);
            }


            //전체 피드를 가져온후 여러개 피드에 ifeed값이 매칭되는 사진들을 하나씩 넣어준다
            //feed가 끝날때까지 List<String> pic = pmapper.selFeedPic(feed.getIfeed());

        }

        log.info("feeds {}", feeds);
//        -- 1page 0~19 (20ㄱㅐ) 2page 20~49 (20개)
//        -- (page-1)*20
        return feeds;
    }

    public ResVo toggleFeedFav(FeedFavDto dto) {
        //ResVo - resutl 값은 삭제했을 시(좋아요 취소) 0, return 등록했을시 1이 return
        //좋아요가 눌려져있으면 del 아니면 insert
        int del = fmapper.delFeedFav(dto);
        if (del == 1) {
            return new ResVo(0);
        } else {
            return new ResVo(fmapper.insFeedFav(dto));
        }
    }

    public ResVo delFeed(FeedDelDto dto) {

        FeedDelDto iuser = mapper.selOneFeed(dto.getIfeed());

        if (iuser == null || dto.getIuser() != iuser.getIuser()) {
            return new ResVo(0);
        }
        if (dto.getIuser() == iuser.getIuser()) {
            mapper.delPic(dto.getIfeed());
            mapper.delComment(dto.getIfeed());
            mapper.delFav(dto.getIfeed());
            mapper.delFeed(dto.getIfeed());
            return new ResVo(1);
        }
        return new ResVo(1);
    }


}
