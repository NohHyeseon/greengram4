package com.greengram.greengram4.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor


public enum FeedErrorCode implements ErrorCode {

    IMPOSSIBLE_REG_COMMENT(HttpStatus.BAD_REQUEST,"댓글을 등록할 수 없습니다"),//빈칸이거나 null일때 터지도록
    PICS_MORE_THEN_ONE(HttpStatus.BAD_REQUEST,"사진은 1장 이상을 선택 하세요.");


    private final HttpStatus httpStatus;
    private final String message;

}
