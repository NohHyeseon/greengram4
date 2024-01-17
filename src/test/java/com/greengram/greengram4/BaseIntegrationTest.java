package com.greengram.greengram4;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Import(CharEncodingConfig.class)
//@MockMvcConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //랜덤한 포트를이용해테스트함
@AutoConfigureMockMvc
@Transactional //트랜잭션걸때쓰임
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseIntegrationTest {

    @Autowired protected MockMvc mvc;//포스트맨역할
    @Autowired protected ObjectMapper om; //객체를 Json으로 그반대도 가능 ->통신을 데이터형태로 하기위해
    //dependency injection 의존성 주입 @Autowired덕분에 가능
    //di가 되게 가능한게 Ioc덕분임

    //이 클래스가 존재하는 이유: 공통된것들이 있어서 상속받기위해;??
    //
}
