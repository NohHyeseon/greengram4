package com.greengram.greengram4.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice //spring aop 라는건데 중간에서 처리해주는역할 슬롯같은 컨트롤러전에 끼우는역할, 로그찍을때나 예외처리에 많이사용
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    //파라미터 에러
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("handleIllegalArgument", e);
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER);
    }
    //대부분의 에러처리는 서버에러가 터지게 만듬
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e){
        log.warn("handleException", e);
        return handleExceptionInternal(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    //customizing 한 에러메세지 , 특정한예외
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleRestApiException(RestApiException e){
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
                        : makeErrorResponse(errorCode, message));
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
