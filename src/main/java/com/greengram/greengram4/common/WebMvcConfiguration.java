package com.greengram.greengram4.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;


@Slf4j
@Configuration//스프링에 webMvcConfiguration라는 객체를 빈등록시켜서 필요한위치에서 실행시키게 해주는 역할
public class WebMvcConfiguration implements WebMvcConfigurer { //엔터시 에러안뜨게 해주는역할
    //index.
    private final String imgFolder;

    public WebMvcConfiguration(@Value("${file.dir}") String imgFolder) {
        this.imgFolder = imgFolder;
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/pic/**") //controller, static 먼저 뒤지고 다음순서
                .addResourceLocations("file:"+imgFolder);
        //satic의 pic뒤지지말고 위에꺼 뒤져라 add 여러개 추가 하겠다.


        registry//순서 중요
                .addResourceHandler("/**") //모든경로를 잡겠다 위의 경로 빼고
                .addResourceLocations("classpath:/static/**")//classpath는 resource다
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);
                        // If we actually hit a file, serve that. This is stuff like .js and .css files.
                        if (requestedResource.exists() && requestedResource.isReadable()) {
                            return requestedResource;
                        }
                        // Anything  else returns the index.
                        return new ClassPathResource("/static/index.html");
                    }
                });
    }
}