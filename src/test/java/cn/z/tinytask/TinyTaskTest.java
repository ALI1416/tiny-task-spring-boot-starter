package cn.z.tinytask;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <h1>轻量级集群任务测试</h1>
 *
 * <p>
 * createDate 2023/07/24 11:54:46
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootApplication
@SpringBootTest
@Slf4j
class TinyTaskTest {

    private final T4s t4s;

    @Autowired
    TinyTaskTest(T4s t4s) {
        this.t4s = t4s;
    }

    /**
     * 直接调用
     */
    @Test
    void test00Normal() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("tinytoken", "1234");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String token = t4s.getToken();
        log.info(token);
        assert "1234".equals(token);
    }

}
