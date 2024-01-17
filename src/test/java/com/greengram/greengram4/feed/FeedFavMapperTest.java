package com.greengram.greengram4.feed;


import com.greengram.greengram4.feed.model.FeedDelDto;
import com.greengram.greengram4.feed.model.FeedFavDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //기존데이터베이스를 바꾸지않겠다. 마리아디비 그대로 사용
public class FeedFavMapperTest {
    @Autowired //스프링이 들고 있는 주소값 주입해달라 Required와 비슷한거임
    private FeedFavMapper fmapper;

    @Test
    public void insFeedFavTest() {
        FeedFavDto dto = new FeedFavDto();
        dto.setIfeed(7);
        dto.setIuser(1);

        List<FeedFavDto> preResult = fmapper.selFeedFavForTest(dto);
        assertEquals(0, preResult.size(), "첫번째 insert 확인");

        int affectedRows1 = fmapper.insFeedFav(dto);
        assertEquals(1, affectedRows1, "첫번째 insert");

        List<FeedFavDto> result = fmapper.selFeedFavForTest(dto);
        assertEquals(1, result.size(), "첫번째 insert 확인");

        dto.setIfeed(112);
        dto.setIuser(1);

        int affectedRows2 = fmapper.insFeedFav(dto);
        assertEquals(1, affectedRows2, "두번째 insert");

        List<FeedFavDto> result2 = fmapper.selFeedFavForTest(dto);
        assertEquals(1, result2.size(), "두번째 insert 확인");
    }

    @Test
    public void delFeedFavTest() {
        FeedFavDto dto = new FeedFavDto();
        dto.setIfeed(105);
        dto.setIuser(2);

        int affectedRows1 = fmapper.delFeedFav(dto);
        assertEquals(1, affectedRows1);

        int affectedRows2 = fmapper.delFeedFav(dto);
        assertEquals(0, affectedRows2);

        List<FeedFavDto> result2 = fmapper.selFeedFavForTest(dto);
        assertEquals(0, result2.size());
    }

    @Test
    public void delFeedFavAllTest() {
        final int ifeed =110;

        FeedFavDto selDto = new FeedFavDto();
        selDto.setIfeed(ifeed);
        List<FeedFavDto> selList = fmapper.selFeedFavForTest(selDto);

        FeedDelDto dto = new FeedDelDto();
        dto.setIfeed(ifeed);
        int delAffectedRows = fmapper.delFeedFavAll(dto);
        assertEquals(selList.size(), delAffectedRows);

        List<FeedFavDto> selList2 = fmapper.selFeedFavForTest(selDto);
        assertEquals(0, selList2.size());

    }
}
