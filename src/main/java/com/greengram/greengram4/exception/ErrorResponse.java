package com.greengram.greengram4.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {
    private final String code;
    private final String message;

    //valids가 null이 아니고size1이상 이라면 JSON 으로 만들어진다 아니면 안만들어진다.
    // Errors가 없다면 응답이 내려가지 않게 처리
    @JsonInclude(JsonInclude.Include.NON_EMPTY)//null, size가 0 이면 출력되지않고 서버에러가 뜨게 됨
    private final List<ValidationError> valid;

    @Getter
    @Builder
    @RequiredArgsConstructor//커스터마이징 한것임
    public static class ValidationError { //이너클래스에 static을 빼버리면 ValidationError가 위의
        //code, message에 접근할 수 있음 이 값들을 물고 있기 때문에 객체를 삭제할 수 없음
        // @Valid 로 에러가 들어왔을 때, 어느 필드에서 에러가 발생했는 지에 대한 응답 처리
        private final String field;
        private final String message;

        public static ValidationError of(final FieldError fieldError) {//final을 붙혀주는게 좋음 속도가 빠르고
            //return new ValidationError(fieldError.getField(), fielError.getDefaultMessage());

            return ValidationError.builder()
                    .field(fieldError.getField())// private String comment; 가 필드
                    .message(fieldError.getDefaultMessage()) //그안에 message가 "댓글내용은3자리이상입니다" 임
                    .build();
        }
    }
}