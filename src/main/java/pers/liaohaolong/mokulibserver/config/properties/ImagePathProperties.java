package pers.liaohaolong.mokulibserver.config.properties;

import jakarta.servlet.ServletContext;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Slf4j
@Data
public class ImagePathProperties {

    /**
     * 图片保存路径模式
     */
    private PathMode pathMode;

    /**
     * 图片保存路径
     */
    private String savePath;

    /**
     * 图片访问路径。不写前 / 符号，不写后 / 符号，例如 <code>avatars</code>
     */
    private String visitPath;

    /**
     * 默认图片资源
     */
    @Setter(AccessLevel.NONE)
    private Resource resource;

    public void init() {
        this.resource = new ClassPathResource("static/default-" + visitPath + ".png");
    }

    public String getFullSavePath(ServletContext servletContext) {
        if (pathMode == PathMode.ABSOLUTE)
            return "file:" + savePath; // 绝对路径
        else
            return "file:" + servletContext.getRealPath("/" + savePath + "/"); // 相对路径需要通过 ServletContext 动态获取
    }

    public String getFullVisitPath() {
        return "/" + visitPath + "/**";
    }

    public enum PathMode {

        /**
         * 绝对路径，目录需手动确保存在。例如 <code>F:/avatars/</code>
         */
        ABSOLUTE,

        /**
         * 相对路径，相对于web项目根目录。不写前 / 符号，不写后 / 符号，例如 <code>avatars</code>
         */
        RELATIVE,

    }

}
