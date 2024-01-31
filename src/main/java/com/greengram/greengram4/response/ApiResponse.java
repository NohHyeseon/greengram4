package com.greengram.greengram4.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiResponse<T> { //제네릭 타입을 언제든지 변경해서 사용할 수 있다.
    private final String path;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY) //Json데이터가 없다면 추가해서 보여주지마라
    private final T data;//만약 FeedPicsInsDto를 리턴하면 얘 타입이 됨 ResVo가 될수있고 다 될 수 있음
}