package com.greengram.greengram4.common;


import com.fasterxml.jackson.databind.deser.CreatorProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app") //prefix:전에 고정한다 야믈 파일에 app으로 변경한걸 가져온거임
public class AppProperties {

    private final Jwt jwt = new Jwt(); //이너클래스라도 객체화를 해야 사용가능해서


    @Getter
    @Setter
    public class Jwt{ //클래스안에 클레스 => 이너클래스
        private String secret;
        private String headerSchemeName;
        private String tokenType;
        private long accessTokenExpiry;
        private long refreshTokenExpiry;
        private int refreshTokenCookieMaxAge;
        private String uploadPrefixPath;


        public void setRefreshTokenExpiry(long refreshTokenExpiry){
            this.refreshTokenExpiry = refreshTokenExpiry;
            this.refreshTokenCookieMaxAge = (int)refreshTokenExpiry / 1000;
        }

    }

}
