package pers.liaohaolong.mokulibserver;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * <p>为传统的 WAR 包部署方式提供 Spring Boot 应用的启动入口。</p>
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected @NonNull SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MokuLibServerApplication.class);
    }

}
