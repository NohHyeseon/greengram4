package com.greengram.greengram4.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greengram.greengram4.common.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component //빈등록을 해놔서 주소값을 가져올수있음
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final ObjectMapper om; //잭슨라이버리에있는친구 제이슨에서 객체로 그반대까지도 해줌
    private final AppProperties appProperties;
    private SecretKeySpec secretKeySpec;


    @PostConstruct //스프링이 켜질 때 한번 메소드를 호출할 때
    public void init() {
        this.secretKeySpec = new SecretKeySpec(appProperties.getJwt().getSecret().getBytes(),
                SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateAccessToken(MyPrincipal principal){
       return generateToken(principal, appProperties.getJwt().getAccessTokenExpiry());

    }
    public String generateRefreshToken(MyPrincipal principal){
        return generateToken(principal, appProperties.getJwt().getRefreshTokenExpiry());
    }


    //token만들기 (yaml 파일에 넣어둠)access(7200000), refresh(1296000000)
    private String generateToken(MyPrincipal principal, long tokenValidMs) {
//        Date now = new Date(); // ?
        return Jwts.builder()
                .claims(createClaims(principal)) //값 담기 메소드로만듬
                .issuedAt(new Date(System.currentTimeMillis())) //비밀번호 만든날짜
                .expiration(new Date(System.currentTimeMillis() + tokenValidMs)) //expriation만료시간
                .signWith(secretKeySpec) //키로 암호화가됨`
                .compact();
    }

    //토큰 값 생성하기
    private Claims createClaims(MyPrincipal principal) { //Claims는 (해시맵스타일) Map의 extends된친구
        try {
            String json = om.writeValueAsString(principal); //throw중 예외처리해줘야함

            return Jwts.claims()
                    .add("user", json) //나중에 권한도 들어갈 수 있음 일단 iuser값만 넣음
                    .build();
        } catch (Exception e) {
            return null;
        }

        //.add("user", principal) -> 일시 문자열로바꿔서 다시객체화할수있음 om사용으로
    }

    //토큰 뽑아내는법
    public String resolvToken(HttpServletRequest req) { //Http -> 요청이 오면 무조건 만들어지는 객체 요청시 날라오는 모든정보가 담겨져있음
        // resolvToken =>서버에서 요청되는 key 값 즉 value를 담아주는 것
        String auth = req.getHeader(appProperties.getJwt().getHeaderSchemeName()); //Beare 뒤 값이 넘어옴
        if (auth == null) {
            return null;
        }
        //Bearer dsfjklffdl Bearer로 시작하냐
        if (auth.startsWith(appProperties.getJwt().getTokenType())) { //starsWith는 무조건 리턴메소드여야함
            return auth.substring(appProperties.getJwt().getTokenType().length()).trim(); //Bearer 6return
        } //substring 인자값은 정수 하나만 보내고있다 index시작값부터 끝까지 6부터 끝까지 문자열잘라서 리턴함 즉 원본변경 X
        return null;

        //return auth==null ? null : auth.startsWith();//만약 null이라면 null리턴 아니라면
    }

    //유효한지확인
    public boolean isValidateToken(String token) {
        try {
            //만료기간이 현재시간보다 전이면 False, 현재시간이만료기간보다 후이면 false
            //만료기간이 현재시간보다 후면 true, 현재시간이 만료기간보다 전이면 true
            return !getAllClaims(token).getExpiration().before(new Date()); //만료기간이 지나지않았다
        } catch (Exception e) {
            return false;
        }
    }

    //검증하는방법
    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKeySpec)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Authentication getAuthentication(String token) { //
        UserDetails userDetails = getUserDetailsFromToken(token);

        return userDetails == null //null이 넘어왔으면
                ? null
                : new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());

    }

    public UserDetails getUserDetailsFromToken(String token) {
        try {
            Claims claims = getAllClaims(token);
            String json = (String) claims.get("user");
            MyPrincipal myPrincipal = om.readValue(json, MyPrincipal.class);
            return MyUserDetails.builder()
                    .myPrincipal(myPrincipal)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}