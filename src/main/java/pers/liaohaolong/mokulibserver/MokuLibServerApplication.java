package pers.liaohaolong.mokulibserver;

import jakarta.servlet.ServletContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pers.liaohaolong.mokulibserver.config.ImagePathConfigurations;

/**
 * <h3>MokuLib Server 启动类</h3>
 */
@Slf4j
@AllArgsConstructor
@EnableAsync
@EnableScheduling
@EnableMethodSecurity
@SpringBootApplication
@MapperScan("pers.liaohaolong.mokulibserver.dao")
public class MokuLibServerApplication implements WebMvcConfigurer {

    private final ServletContext servletContext;

    private final ImagePathConfigurations imagePathConfigurations;

    static void main(String[] args) {
        SpringApplication.run(MokuLibServerApplication.class, args);
        log.info("OHH, I'm free! ^_^");
    }

    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        // 配置图片资源路径映射
        imagePathConfigurations.getAll().forEach(imagePathProperties -> {
            String visitPath = imagePathProperties.getFullVisitPath();
            String savePath = imagePathProperties.getFullSavePath(servletContext);

            registry.addResourceHandler(visitPath).addResourceLocations(savePath);

            log.info("addResourceHandler: {} -> {}", visitPath, savePath);
        });
    }

}
