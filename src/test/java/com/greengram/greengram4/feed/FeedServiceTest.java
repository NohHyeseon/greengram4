package com.greengram.greengram4.feed;

import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.feed.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)//필요한 일부 객체들만 객체화 시켜주는것
@Import({FeedService.class})//필요한 객체 자바에선 {}이 배열임
class FeedServiceTest {

    @MockBean//service가 객체화 되려면
    // 밑에있는 것들이 객체화가 돠야하긴 하지만 가짜로 만들어 주소값만 들어갈 수 있게 만들어줌
    private FeedMapper mapper;

    @MockBean
    private FeedPicsMapper pmapper;

    @MockBean
    private FeedCommentMapper cmapper;

    @MockBean
    private FeedFavMapper fmapper;

    @Autowired
    private FeedService service;

    @Test
    void postFeed() {
        when(mapper.insFeed(any())).thenReturn(1); // mapper메소드에 insfeed를 진짜 실행하지않고 1이return되게 만듬
        when(pmapper.insPic(any())).thenReturn(2);

        FeedInsDto dto = new FeedInsDto();// postFeed가 받는값이 FeedInsDto dto 라서 객체를생성하고
        //셋팅까지가 given공간

        //ResVo vo = service.postFeed(dto);// 여기서 실제로 사용해서 return타입이 ResVo 라서 여기에 넣는거
        // 호출하는 부분이 when

        verify(mapper).insFeed(any()); //mapper에서 insFeed를진짜 호출했는지 확인 넘겨준값을 사용했는지안했는진 알 수 없음
        verify(pmapper).insPic(any());
        //검증하는 공간이 then 테스트는 3단계로이뤄짐



    }

    @Test
    public void getFeedAll() {
        FeedSelVo feedSelVo1 = new FeedSelVo();// getFeedAll의 타입의객체 생성
        feedSelVo1.setIfeed(1); // 값이 제대로 넘어오는지 확인
        feedSelVo1.setContents("일번 feedSelvo");

        FeedSelVo feedSelVo2 = new FeedSelVo();
        feedSelVo2.setIfeed(2);//값이 제대로 넘어오는지 확인
        feedSelVo2.setContents("이번 feedSelvo");

        List<FeedSelVo> list = new ArrayList(); //feedselvo 타입의 객체주솟값 여러개 담을수 있음
        list.add(feedSelVo1);//add method를 사용해서 담을 수 있음
        list.add(feedSelVo2);//주소값은 무조건 8byte까지 담을 수 있음

        when(mapper.selFeed(any())).thenReturn(list);// 어떠한 값이 들어가면 그냥 list를 return 해라


        List<String> feed1Pics = Arrays.stream(new String[]{"a.jpg", "b.jpg"}).toList(); //배열을 arrayList로 변경하는것

        List<String> feed2Pics = new ArrayList();
        feed2Pics.add("가.jpg");
        feed2Pics.add("나.jpg"); //>>위의 Arrays.stream을 사용하지 않으면 이렇게 써야함

        when(pmapper.selFeedPic(1)).thenReturn(feed1Pics);//1보내면 feed1Pic을 보내고
        when(pmapper.selFeedPic(2)).thenReturn(feed2Pics);//2보내면 feed2Pic을 보내라


        FeedSelProcVo dto = new FeedSelProcVo();
        List<FeedSelVo> result = service.selFeed(dto); //1.2를 가지고 있는 list가 return

        assertEquals(list, result); //sout랑 같은역할 , list, result에 담긴 값이 같은지 비교


        for (FeedSelVo vo : list) {
            assertNotNull(vo.getPics());
            System.out.println("result: " + vo);//1,2 번 포문돌면서 FeedSelVo 안의 필드 값들 다 나오게

        } //ser


        for (int i = 0; i < result.size(); i++) { // for문 돌면서 1번방 2번방 각각의 사진을 넣어줌
            FeedSelVo rVo = result.get(i);
            FeedSelVo pVo = list.get(i);

            assertEquals(rVo.getPics(), pVo.getPics());


            assertEquals(pVo.getIfeed(), rVo.getIfeed());
            assertEquals(pVo.getContents(), rVo.getContents());
        }
// ------------- comment ---------------
        FeedCommentSelVo comment1 = new FeedCommentSelVo();
        comment1.setComment("기분좋아");
        comment1.setWriterNm("노혜선");

        FeedCommentSelVo comment2 = new FeedCommentSelVo();
        comment2.setComment("배고파");
        comment2.setWriterNm("조현민");


        FeedCommentSelVo comment3 = new FeedCommentSelVo();
        comment3.setComment("쉬는시간");
        comment3.setWriterNm("하나님");


        FeedCommentSelVo comment4 = new FeedCommentSelVo();
        comment4.setComment("월급줘");
        comment4.setWriterNm("박도흠");

        List<FeedCommentSelVo> Kingcomments = new ArrayList(); //com1,2를 배열에 담는다
        Kingcomments.add(comment1);//3개 이상넘어가면  size를 줄이는지 테스트를위함
        Kingcomments.add(comment2);
        Kingcomments.add(comment3);
        Kingcomments.add(comment4);

        when(cmapper.selFeedCommentAll(any())).thenReturn(Kingcomments);//kingcomments를 보내준다.


        List<FeedSelVo> result2 = service.selFeed(dto);//서비스에서 메소드호출
        for (FeedSelVo feed : result2) { //FeedSelVo에 있는 각각의 comments를 가지고 오기 위해서
            List<FeedCommentSelVo> realComment= feed.getComments();
            assertEquals(Kingcomments,realComment);
            assertEquals(3,Kingcomments.size());
        }
        // 댓글을 하나가 넘어오면 if문 실행X 1개넘어가면 사이즈=1
        //댓글을 하나만 넘겼을 때 사이즈-1 코멘트 리스트를 새로만들어서 서비스 새로호출
        List<FeedCommentSelVo> size1 = new ArrayList();
        size1.add(comment1);
        when(cmapper.selFeedCommentAll(any())).thenReturn(size1);
        List<FeedSelVo> result3 = service.selFeed(dto);
        for(FeedSelVo feed :result3){
            List<FeedCommentSelVo> realComment2 = feed.getComments();
            assertEquals(1,realComment2.size());
        } //n+1->

 // ------------------------------------------------------------------------


    }


}