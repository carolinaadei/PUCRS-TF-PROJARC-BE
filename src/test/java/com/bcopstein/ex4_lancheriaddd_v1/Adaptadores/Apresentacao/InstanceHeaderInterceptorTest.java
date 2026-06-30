package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class InstanceHeaderInterceptorTest {

    @Test
    void preHandle_setsXServedByHeader() throws Exception {
        var interceptor = new InstanceHeaderInterceptor(8080);
        var response = new MockHttpServletResponse();

        boolean result = interceptor.preHandle(new MockHttpServletRequest(), response, new Object());

        assertThat(result).isTrue();
        assertThat(response.getHeader("X-Served-By")).isNotNull();
        assertThat(response.getHeader("X-Served-By")).endsWith(":8080");
    }
}
