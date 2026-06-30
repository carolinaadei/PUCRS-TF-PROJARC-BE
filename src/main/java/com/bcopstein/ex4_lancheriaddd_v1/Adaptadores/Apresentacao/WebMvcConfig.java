package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final InstanceHeaderInterceptor instanceHeaderInterceptor;

    public WebMvcConfig(InstanceHeaderInterceptor instanceHeaderInterceptor) {
        this.instanceHeaderInterceptor = instanceHeaderInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(instanceHeaderInterceptor);
    }
}
