package com.greengram.greengram4.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration { //r권한을 어디다 줄지 설정

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean//기본꺼 쓰지말고 이거 쓰기위해서 customizing함
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(http -> http.disable()) //화면이 없는 경우 필요없음
                .csrf(csrf -> csrf.disable()) //스프링이 기본으로 제공해주는 보안기법 화면이 없을경우 쓸 필요없음 그래서 끔
                .formLogin(formLogin -> formLogin.disable())//화면안써서 사용안함
                //비활성화하여  Post 요청이 가능하도록 한다.
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/user/signin",
                                        "api/user/signup", //검증없이 요청익 ㅏ능하게 만듬
                                        "/error",
                                        "/err",
                                        "/",
                                        "/feed",
                                        "/feed/**",
                                        "/fimg/**",
                                        "/profile",
                                        "/profile/**",
                                        "/css/**",
                                        "/pic/**",
                                        "/signin",
                                        "/signup",
                                        "/index.html",
                                        "/static/**",
                                        "/swagger.html",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",// 다통과시킨다
                                        "/api/open/**",
                                        "/api/user/refresh-token"
                                ).permitAll()

                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) //얘먼저거치고 간다
                //add는 기존꺼 그대로 두고 추가, set은 기존꺼 다 날리고 셋팅,UsernampePa위에꺼 전에 먼저 이필터를 끼우겠다
                .exceptionHandling(except -> {
                    except.authenticationEntryPoint(new JwtAuthenticationEntryPoint())//얘도 마찬가지
                            .accessDeniedHandler(new JwtAccessDenitedHandler()); //<-객체생성해서 사용
                })
                //permitall은 무사 통과시켜준다는 뜻 , matchers 매칭해줌
                //.anyRequest().authenticated())//로그인을 해야만 쓸수있음 ,
                //회원가입시 인증이필요하다? 를 꺼줌 매칭
                .build();

    }

    @Bean //빈등록 시켜줌
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
