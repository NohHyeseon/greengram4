package com.greengram.greengram4.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode { //enum이란?? Const를 대체할 수 있는 친구 상수 객체화해서 notFOund안에 주소값을 담겠다

    NOT_EXIST_USER_ID(HttpStatus.NOT_FOUND,"아이디가 존재하지 않습니다"),

    VALID_PASSWORD(HttpStatus.NOT_FOUND,"비밀번호를 확인해주세요"),

    NEED_SIGNIN(HttpStatus.UNAUTHORIZED ,"로그인이 필요합니다"), //세미콜론아니고 콤마로 여러개 표시해줘야함

    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND //NOTFOUND가 멤버필드
            , "refresh-token이 없습니다");


    private final HttpStatus httpStatus;
    private final String message;


}
