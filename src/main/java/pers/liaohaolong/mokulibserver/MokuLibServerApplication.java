package pers.liaohaolong.mokulibserver;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import pers.liaohaolong.mokulibserver.config.ImageConfigurations;
import pers.liaohaolong.mokulibserver.config.properties.ImageProperties;

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

    private final ImageConfigurations imageConfigurations;

    static void main(String[] args) {
        SpringApplication.run(MokuLibServerApplication.class, args);
        log.info("OHH, I'm free! ^_^");
    }

    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        // 配置图片资源路径映射
        imageConfigurations.getAll().forEach(properties -> {
            String pathPattern = properties.getPathPattern();
            String resourceLocation = properties.getResourceLocation();
            ClassPathResource defaultResource = properties.getDefaultResource();

            registry.addResourceHandler(pathPattern)
                    .addResourceLocations(resourceLocation)
                    .resourceChain(true)
                    .addResolver(new DefaultResourceResolver(properties));

            log.info("Registering resource handler: mapping URL path {} to physical location {}, default resource {}.",
                    pathPattern, properties.getAbsolutePath(), defaultResource.getPath());
        });
    }

    private static class DefaultResourceResolver extends PathResourceResolver {

        private final String formatName;

        private final ClassPathResource defaultResource;

        public DefaultResourceResolver(ImageProperties properties) {
            this.formatName = properties.getSaveFormat();
            this.defaultResource = properties.getDefaultResource();
        }

        @Override
        protected Resource getResource(@NonNull String resourcePath, @NonNull Resource location) throws IOException {
            // http://localhost:8080/books/1: resourcePath=1,location=file:F:/moku/books/
            if (!resourcePath.endsWith("." + this.formatName))
                // 无后缀资源自动映射
                resourcePath = resourcePath + "." + this.formatName;
            // 查找资源
            Resource resource = location.createRelative(resourcePath);
            // 找不到则返回默认资源
            return resource.exists() && resource.isReadable() ? resource : this.defaultResource;
        }

    }

}
