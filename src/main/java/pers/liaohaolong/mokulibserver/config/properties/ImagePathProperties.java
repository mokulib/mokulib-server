package pers.liaohaolong.mokulibserver.config.properties;

import lombok.Data;

@Data
public class ImagePathProperties {

    private PathMode pathMode;

    private String path;

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
