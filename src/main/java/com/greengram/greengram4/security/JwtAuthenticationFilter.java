package com.greengram.greengram4.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { //서블릿앞단에서 무조건 얘를 거침 튕겨낼수도있는데 여기선아님
    //헤더에 토큰이 있다면 로그인한사람으로 처리함 Authentication 에 값이 있으면 로그인한사용자

    private final JwtTokenProvider jwtTokenProvider;

    @Override //요청때 마다 헤더에 authentication이 있으면 실행됨 없으면 안담고 있으면 담음
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolvToken(request);//토큰값을보내줌 요청이될때마다 실행됨 request에 토큰이 담겨져있는지확인
        log.info("JwtAuthentication-Token: {} ", token);

        if (token != null && jwtTokenProvider.isValidateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token); //Authentication에 담는것을 리턴해줌
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth); //여기서 토큰값을 담아줌
            } // 이 메소드 전체 과정을 통해 로그인처리가 되었는지 확인
        }

        filterChain.doFilter(request, response); //위에서 사용하고 다시 다음필터에 값넘겨줌
    }
}
