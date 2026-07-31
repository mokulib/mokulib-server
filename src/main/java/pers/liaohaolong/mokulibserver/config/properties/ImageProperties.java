package pers.liaohaolong.mokulibserver.config.properties;

import jakarta.servlet.ServletContext;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
public class ImageProperties {

    @Setter(AccessLevel.NONE)
    private ServletContext servletContext;

    /**
     * 图片访问路径<br>
     * 不写前后 / 符号：<code>avatars</code>
     */
    private String visitPath;

    /**
     * 图片保存路径模式
     */
    private PathMode pathMode;

    /**
     * 图片保存路径<br>
     * 绝对路径时：<code>F:/avatars/</code><br>
     * 相对路径时：<code>avatars</code>
     */
    private String savePath;

    /**
     * 图片保存格式
     */
    private String saveFormat;

    /**
     * 默认图片资源
     */
    @Setter(AccessLevel.NONE)
    private ClassPathResource defaultResource;

    public void init(ServletContext servletContext) throws IOException {
        // 保存 ServletContext
        this.servletContext = servletContext;
        // 初始化保存路径
        Files.createDirectories(Path.of(getAbsolutePath()));
        // 图片保存格式规范化
        this.saveFormat = this.saveFormat.toLowerCase();
        // 检查图片保存格式是否支持
        if (isUnsupportedWriteFormat(this.saveFormat))
            throw new IOException("不支持的图片保存格式：" + this.saveFormat);
        // 初始化默认图片资源
        this.defaultResource = new ClassPathResource("static/default-" + this.visitPath + "." + this.saveFormat);
        // 检查默认图片资源是否存在
        if (!defaultResource.exists())
            throw new IOException("默认图片资源不存在：" + defaultResource.getPath());
    }

    /**
     * 获取 url 访问路径
     */
    public String getPathPattern() {
        return "/" + visitPath + "/**";
    }

    /**
     * 获取图片资源路径，带 <code>file:</code> 前缀的路径
     *
     * @return 图片资源路径
     */
    public String getResourceLocation() {
        if (pathMode == PathMode.ABSOLUTE) // 绝对路径
            return "file:" + savePath;
        if (pathMode == PathMode.RELATIVE) // 相对路径需要通过 ServletContext 动态获取
            return "file:" + servletContext.getRealPath("/" + savePath + "/");
        throw new RuntimeException("图片保存路径模式错误");
    }

    /**
     * 获取图片保存绝对路径
     *
     * @return 图片保存绝对路径
     */
    public String getAbsolutePath() {
        if (pathMode == PathMode.ABSOLUTE) // 绝对路径
            return savePath;
        if (pathMode == PathMode.RELATIVE) // 相对路径需要通过 ServletContext 动态获取
            return servletContext.getRealPath("/" + savePath + "/");
        throw new RuntimeException("图片保存路径模式错误");
    }

    /**
     * 获取图片保存绝对路径
     *
     * @param fileName 文件名
     * @param formatName 图片格式
     * @return 图片保存绝对路径
     */
    public String getAbsolutePath(String fileName, String formatName) {
        return getAbsolutePath() + fileName + "." + formatName;
    }

    private static final Set<String> SUPPORTED_WRITE_FORMAT_NAMES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(ImageIO.getWriterFormatNames())));

    /**
     * 检查图片保存格式是否支持
     *
     * @param formatName 图片保存格式
     * @return 是否支持
     */
    public static boolean isUnsupportedWriteFormat(@Nullable String formatName) {
        return formatName == null  || !SUPPORTED_WRITE_FORMAT_NAMES.contains(formatName.toLowerCase());
    }

    public enum PathMode {

        /**
         * 绝对路径
         */
        ABSOLUTE,

        /**
         * 相对路径
         */
        RELATIVE,

    }

}
