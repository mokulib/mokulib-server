package pers.liaohaolong.mokulibserver.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.config.properties.ImagePathProperties;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "mokulib.image-path")
@Component
public class ImagePathConfigurations {

    @NestedConfigurationProperty
    private ImagePathProperties avatars;

    @NestedConfigurationProperty
    private ImagePathProperties books;

    @PostConstruct
    public void init() {
        // 初始化
        getAll().forEach(ImagePathProperties::init);
    }

    public List<ImagePathProperties> getAll() {
        return List.of(avatars, books);
    }

}
