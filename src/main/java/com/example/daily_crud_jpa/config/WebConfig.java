package com.example.daily_crud_jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SPA(Single Page Application) 라우팅 처리를 위한 설정
 * Vue Router 등의 클라이언트 사이드 라우팅을 지원하기 위해
 * API 요청 및 정적 리소스를 제외한 모든 경로를 index.html로 포워딩
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

        /**
         * 정적 리소스(assets, vite.svg 등) 핸들러 추가
         */
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // /assets/** 경로의 정적 리소스를 명시적으로 처리
                registry.addResourceHandler("/assets/**")
                                .addResourceLocations("classpath:/static/assets/");

                // vite.svg 등의 정적 파일 처리
                registry.addResourceHandler("/*.svg", "/*.ico", "/*.png", "/*.jpg")
                                .addResourceLocations("classpath:/static/");
        }

        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
                // /api 및 정적 리소스를 제외한 모든 경로를 index.html로 포워딩
                // Vue Router의 클라이언트 사이드 라우팅 지원

                // 확장자가 없는 경로만 index.html로 포워딩 (정적 파일 제외)
                registry.addViewController("/{x:[\\w\\-]+}")
                                .setViewName("forward:/index.html");
                registry.addViewController("/**/{x:[\\w\\-]+}")
                                .setViewName("forward:/index.html");
        }
}
