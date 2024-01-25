package com.greengram.greengram4.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice // advice는 aop용어, aop는 중간에 슬롯처럼 끼워서 사용하는 클래스(보통 로그, 트랜잭션, 예외처리할때 쓰임)
//필터는 튕겨낼때(로그인처리), 인터셉터는 원래는 spring 외부에서 작용 boot버전 부터는 내장이라 필터랑 비슷
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
    //서버는 기본적으로 예외로 던지면 예외 처리를 해주지만 우리가 예외번호를 지정해주고 싶을때 만들어서 보냄

    // @Valid 어노테이션으로 넘어오는 에러 처리
    @Override //자식타입
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex, CommonErrorCode.INVALID_PARAMETER);
    } //ex안에 valid내용 다 들어가있음

    /*@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e){
        log.warn("handleIllegalArgument",e);
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER);
    }*/

    /*@ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("handleMethodArgumentNotValidException",e);
        *//*List<String> errors = new ArrayList<>();
        for (FieldError lfe : e.getBindingResult().getFieldErrors()){
            errors.add(lfe.getDefaultMessage());
        }*//*
        //stream 으로 위 코드를 변경, stream은 1회용
        //map은 getFieldErrors와 사이즈가 같은 것을 하나 더 만들어서 리턴해줌
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map( lfe -> lfe.getDefaultMessage())
                .map(str -> {
                    System.out.println(str);
                    return "a";
                })
                .collect(Collectors.toList());
        String errStr = "[" + String.join("\n, ", errors) + "]";
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER, errors.toString());
    }*/

    //대부분의 에러처리는 서버에러로 잡음
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e){
        log.warn("handleException",e);
        return handleExceptionInternal(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    //우리가 작업하다가 특정예외를 터트리고 싶어 RestApiException을 만듦
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleRestApiException(RestApiException e){
        log.warn("handleRestApiException",e);
        return handleExceptionInternal(e.getErrorCode());
    }



    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return handleExceptionInternal(errorCode, null);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode
            , String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(message == null
                        ? makeErrorResponse(errorCode)
                        : makeErrorResponse(errorCode, message));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return makeErrorResponse(errorCode, errorCode.getMessage());
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }

    // @Valid 어노테이션으로 넘어오는 에러 처리 메세지를 보내기 위한 메소드 ,, 부모 타입
    private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(e, errorCode));
        //위의 ex가 e로 들어오고 handleExceptionInternal
    }

    // 코드 가독성을 위해 에러 처리 메세지를 만드는 메소드 분리
    private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {
        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult() //최상위를 보내면 코드가 유연해져서 위 부모코드를 보냄
                .getFieldErrors()
                .stream()
                //.map(ErrorResponse.ValidationError::of) //밑 두개 다 똑같은말이다
                //.map(item -> ErrorResponse.ValidationError.of(item))
                .map(item -> {return ErrorResponse.ValidationError.of(item);})//item안에 필드에러 객체 주소값이 나오고
                //of라는 메소드를 호출 할 때 item을 보내줌 이너클래스안의 메소드라 세번 호출함 errorResponse 클래스안의 크래스안의 메소드
                //얘가 리턴하는건 스트림이므로 리스트(?)와 비슷한 타입
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .valid(validationErrorList)
                .build();
    }


}