package com.greengram.greengram4.common;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Slf4j
@Configuration //주소값을 받는다기보단 설정용을 명시화??? component해도 돌아가긴함
public class FirevasConfiguration {

    @Value("${fcm.certification}") //야믈 의존성 주입
    private String googleApplicationCredentials;

    @PostConstruct //생성자 다음 호출해라
    public void init() {
        try {
            InputStream serviceAccount =
                    new ClassPathResource(googleApplicationCredentials).getInputStream();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                log.info("FirebaseApp Initialization Complete !!!");
                FirebaseApp.initializeApp(options);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
