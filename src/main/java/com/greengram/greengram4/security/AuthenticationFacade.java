package com.greengram.greengram4.security;

import com.google.api.BackendRule;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade { //service단에서 사용 (controller,service 어디서 쓸지 정해두는게 좋음 )
    public MyUserDetails getLoginUser() {
        return (MyUserDetails) SecurityContextHolder //
                .getContext()
                .getAuthentication() //여기까지 authentication이 넘어옴 토큰값 가져옴
                .getPrincipal(); // userDetails가 들어감
    }


    public int getLoginUserPk() {
        MyUserDetails myUserDetails = getLoginUser();
        return myUserDetails == null?
                0
                : myUserDetails
                .getMyPrincipal()
                .getIuser();
    }
}
