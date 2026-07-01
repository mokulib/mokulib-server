package pers.liaohaolong.mokulibserver;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(MybatisPlusConfiguration.class)
public @interface MokuLibServerTest {
}
