package com.greengram.greengram4.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    // refresh token 담아서 보내는 작업
    public Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }
    public void setCookie(HttpServletResponse response,String name, String value, int maxAge){
        Cookie cookie = new Cookie(name,value);
        cookie.setPath("/"); //루트주소값대로 쿠키가 만들어진다 모든 주소값에 쿠키가만들어진다???
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);

    }
    public void deleteCookie(HttpServletResponse response, String name){ //request가 nm값으로 response를 지운다??
        Cookie cookie = new Cookie(name,null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
