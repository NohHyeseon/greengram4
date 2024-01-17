package com.greengram.greengram4.feed;

import com.greengram.greengram4.feed.model.FeedPicProcDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedPicsMapperTest {
    private FeedPicProcDto dto;

    public  FeedPicsMapperTest() {
        this.dto = new FeedPicProcDto();
        this.dto.setIfeed(10);
        List<String> pics = new ArrayList<>();
        pics.add("test1");
        pics.add("test2");
        this.dto.setPics(pics);
    }

    @Autowired
    FeedPicsMapper mapper; // @MybatisTest 애노테이션으로 인해 들어올 수 있음 >>객체화를 시켜줌 service, controller는 안됨

    @BeforeEach// 테스트전 한 구간만 "각각" 먼저 테스트를 한다
    public void beforEach() {

        int affectedRows = mapper.delPic(dto.getIfeed());
    }

//    @Test
//    void insPicTest() {
//        List<String> preList = mapper.selFeedPic(dto.getIfeed());
//        assertEquals(0, preList.size());
//
//
//        int insAffedctedRows = mapper.insPic(this.dto);
//        assertEquals(dto.getPics().size(), insAffedctedRows);
//
//        List<String> afterList= mapper.selFeedPic(dto.getIfeed());
//        assertEquals(dto.getPics().size(), afterList.size());
//
//        for(int i=0; i<dto.getPics().size(); i++){
//            assertEquals(dto.getPics().get(i), afterList.get(i));
//        }
//
//
//
//    }

    @Test
    void selFeedPic() {
    }


}