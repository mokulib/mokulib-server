package pers.liaohaolong.mokulibserver.config;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.config.properties.ImageProperties;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "mokulib.image")
@Component
public class ImageConfigurations {

    private final ServletContext servletContext;

    @NestedConfigurationProperty
    private ImageProperties avatars;

    @NestedConfigurationProperty
    private ImageProperties books;

    @Autowired
    public ImageConfigurations(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @PostConstruct
    public void init() throws IOException {
        // 打印所有支持的图片格式
        log.info("ImageIO read-supported formats: {}", String.join(", ", ImageIO.getReaderFormatNames()));
        log.info("ImageIO write-supported formats: {}", String.join(", ", ImageIO.getWriterFormatNames()));
        // 初始化子配置
        for (ImageProperties imageProperties : getAll())
            imageProperties.init(servletContext);
        // 绑定子配置到枚举类
        ImageType.AVATARS.setImageProperties(avatars);
        ImageType.BOOKS.setImageProperties(books);
    }

    public List<ImageProperties> getAll() {
        return List.of(avatars, books);
    }

    @Getter
    public enum ImageType {

        AVATARS,
        BOOKS;

        private ImageProperties imageProperties;

        public void setImageProperties(ImageProperties imageProperties) {
            if (this.imageProperties != null)
                throw new IllegalStateException("ImageProperties has already been set");

            this.imageProperties = imageProperties;
        }

    }

}
