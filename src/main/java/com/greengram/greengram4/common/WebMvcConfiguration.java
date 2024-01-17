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

        registry.addResourceHandler("/pic/**")
                .addResourceLocations("file:"+imgFolder);
        //satic의 pic뒤지지말고 위에꺼 뒤져라

        registry
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/static/**")
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