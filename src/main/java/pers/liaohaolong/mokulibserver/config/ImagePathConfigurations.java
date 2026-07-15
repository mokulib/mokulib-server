package pers.liaohaolong.mokulibserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.config.properties.ImagePathProperties;

@Data
@Configuration
@ConfigurationProperties(prefix = "mokulib.image-path")
@Component
public class ImagePathConfigurations {

    @NestedConfigurationProperty
    private ImagePathProperties avatars;

    @NestedConfigurationProperty
    private ImagePathProperties books;

}
