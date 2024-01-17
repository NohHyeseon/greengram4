package com.greengram.greengram4.feed;


import com.greengram.greengram4.BaseIntegrationTest;
import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.feed.model.FeedInsDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class FeedIntegrationTest extends BaseIntegrationTest {
    @Test
    @Rollback(false)
    public void postFeed() throws Exception {
        FeedInsDto dto = new FeedInsDto();
        dto.setIuser(1);
        dto.setContents("통합 테승트 작업 중");
        dto.setLocation("그린컴퓨터학원");

        List<String> feedPics = new ArrayList();
        feedPics.add("https://postfiles.pstatic.net/MjAyMzA3MjFfMTU3/MDAxNjg5OTMwNDIwODky.pMKGsFr0vQZum3CfehzixIGqjrlRl0r6363zjCNg8O0g.hjiiLHEJNXRly56JKY0XzsXFjsD_loTHzmlSfnRsbTsg.JPEG.seoyoon120730/IMG_2428.jpg?type=w966");
       // dto.setPics(feedPics);

        String json = om.writeValueAsString(dto); //위의 값들을 문자열로 변환하는 부분  json으로
        System.out.println("json" + json); //given part

        MvcResult mr = mvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/feed")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json) //바디부분에 담아서 통신
                )
                .andExpect(status().isOk())//기대한값이냐
                .andDo(print())//그값을 콘솔에찍어
                .andReturn();//그리고 리턴해라
        String content = mr.getResponse().getContentAsString(); //응답해서 콘텐트 부분을 스트링으로서 리턴해준다
        ResVo vo = om.readValue(content, ResVo.class);//문자열을 객체타입으로바꿔줌 readVlaue라는 메소드를이용해서
        assertEquals(true, vo.getResult() > 0); //jupiter로 import


    }

    @Test
    @Rollback(false)
    public void delFeed() throws Exception {
//        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
//        params.add("iuser", "3");
//        params.add("ifeed","98");


        MvcResult mr = mvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/feed?iuser=1&ifeed=120")
                                //.params(params)
                )
                .andExpect(status().isOk())
                .andReturn();
        String content = mr.getResponse().getContentAsString(); //기븐한 부분을 다시 스트링으로 셋팅하는작업
        ResVo vo = om.readValue(content, ResVo.class);//스트링으로 셋팅했다가 다시 객체화하는작업
        assertEquals(1, vo.getResult());

    }

}
