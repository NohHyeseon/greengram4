package com.greengram.greengram4.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greengram.greengram4.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.greengram.greengram4") //스웨거에서 처리하는것을 통과시켜줌
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper om;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        //컨트롤러의 반환값이 객체일 때만 return true
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    } //return true일시 String이 리턴될때 다른방식 사용하기 때문에 true라고 하지않음

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse servletResponse =
                ((ServletServerHttpResponse) response).getServletResponse();

        int status = servletResponse.getStatus();
        HttpStatus resolve = HttpStatus.resolve(status); //상태 코드를 알아내기 위한 로직
        String path = request.getURI().getPath(); //요청주소값
        if(resolve != null) {
            if(resolve.is2xxSuccessful()) { //200대 코드라면 밑을 리턴하게
                return ApiResponse.builder()
                        .path(path)
                        .data(body)
                        .code("Success")
                        .message("통신 성공")
                        .build();
            } else if(body instanceof ErrorResponse) { //body부분이 응답부분, 만약 에러라면 밑의 map리턴
                Map<String, Object> map = om.convertValue(body, Map.class);
                map.put("path", path);//path를 집어넣기 위해 만든로직 해쉬맵 형식으로 리턴하기 위해
                return map;
            }
        }
        return body;
    }
}