package pers.liaohaolong.mokulibserver;

import jakarta.servlet.ServletContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import pers.liaohaolong.mokulibserver.config.ImagePathConfigurations;

import java.io.IOException;

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
            Resource defaultResource = imagePathProperties.getResource();

            registry.addResourceHandler(visitPath)
                    .addResourceLocations(savePath)
                    .resourceChain(true)
                    .addResolver(new DefaultResourceResolver(defaultResource));

            log.info("Registering resource handler: mapping URL path {} to physical location {}.", visitPath, savePath);
        });
    }

    @AllArgsConstructor
    private static class DefaultResourceResolver extends PathResourceResolver {

        private final Resource defaultResource;

        @Override
        protected Resource getResource(@NonNull String resourcePath, @NonNull Resource location) throws IOException {
            // 查找资源
            Resource resource = location.createRelative(resourcePath);
            // 找不到则返回默认资源
            return resource.exists() && resource.isReadable() ? resource : defaultResource;
        }

    }

}
