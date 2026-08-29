package pers.liaohaolong.mokulibserver.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

/**
 * <h3>请求头配置类</h3>
 */
public class RequestHeaderConfigurations {

    public static final String CACHE_CONTROL_KEY = "Cache-Control";
    public static final Configuration<Boolean> CACHE_CONTROL = Configuration.<Boolean>builder().key(CACHE_CONTROL_KEY).parser("no-cache"::equalsIgnoreCase).build();

    @Builder
    @AllArgsConstructor
    public static class Configuration<T> {

        @Getter
        private final String key;

        private final Parser<T> parser;

        public T parse(@Nullable String value) throws Exception {
            return parser.parse(value);
        }

    }

    @FunctionalInterface
    public interface Parser<T> {

        T parse(@Nullable String value) throws Exception;

    }

}
