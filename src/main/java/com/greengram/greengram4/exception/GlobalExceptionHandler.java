package com.greengram.greengram4.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice //spring aop 라는건데 중간에서 처리해주는역할 슬롯같은 컨트롤러전에 끼우는역할, 로그찍을때나 예외처리에 많이사용
public class GlobalExceptionHandler {
    //파라미터 에러
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("handleIllegalArgument", e);
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)//서버 메세지 외에 내가 설정한 메세지를 보내고 싶을 때 사용되는 메소드
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("handleMethodArgumentNotValidException", e);
//        List<String> errors = new ArrayList<>();
//        for(FieldError lfe : e.getBindingResult().getFieldErrors()){
//            errors.add(lfe.getDefaultMessage());
//        }
        //arraylist가 3이였다치면 map은 사이즈가 똑같은 애를 하나 더 만들어서 리턴해줌
        //필터는 사이즈가 달라질 수 있다 map은 달라질 수 없다
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(lfe -> lfe.getDefaultMessage())//String type을 return 함 이거를 a밑 collection상태로 변환한다
                .collect(Collectors.toList());
        String errStr = "[" + String.join(", ", errors) + "]";
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER, errors.toString());
    }


    //대부분의 에러처리는 서버에러가 터지게 만듬
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        log.warn("handleException", e);
        return handleExceptionInternal(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

//    List<String> errors = new ArrayList();
//    for(fildError)
//


    //customizing 한 에러메세지 , 특정한예외
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleRestApiException(RestApiException e) {
        log.warn("handleRestApiException", e);
        return handleExceptionInternal(e.getErrorCode());
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return handleExceptionInternal(errorCode, null);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(message == null
                        ? makeErrorResponse(errorCode)
                        : makeErrorResponse(errorCode, message)); //위 코드에서 밑 코드를 선택
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) { //원래 있던 에러메세지 사용
        return makeErrorResponse(errorCode, errorCode.getMessage());
    }


    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) { //커스터마이징해서 에러메세지 사용
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }

}
