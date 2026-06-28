package pers.liaohaolong.mokulibserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@SpringBootApplication
public class MokuLibServerApplication implements WebMvcConfigurer {

    static void main(String[] args) {
        SpringApplication.run(MokuLibServerApplication.class, args);
        log.info("OHH, I'm free! ^_^");
    }

}
