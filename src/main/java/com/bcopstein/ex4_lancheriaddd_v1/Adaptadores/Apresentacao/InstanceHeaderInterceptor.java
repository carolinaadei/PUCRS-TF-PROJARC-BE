package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class InstanceHeaderInterceptor implements HandlerInterceptor {

    private final int port;

    public InstanceHeaderInterceptor(@Value("${server.port}") int port) {
        this.port = port;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            response.setHeader("X-Served-By", ip + ":" + port);
        } catch (UnknownHostException e) {
            response.setHeader("X-Served-By", "unknown:" + port);
        }
        return true;
    }
}
