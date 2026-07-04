package pers.liaohaolong.mokulibserver.security.matcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.intellij.lang.annotations.RegExp;
import org.jspecify.annotations.NonNull;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Map;

/**
 * <h3>正则表达式请求头匹配器</h3>
 *
 * <p>用于检查指定的请求头的值是否匹配正则表达式。</p>
 */
@SuppressWarnings("ClassCanBeRecord")
@Getter
public class RegexpRequestHeaderRequestMatcher implements RequestMatcher {

    private final String headerName;

    @RegExp
    private final String regex;

    public RegexpRequestHeaderRequestMatcher(@NotNull String headerName, @RegExp @NotNull String regex) {
        this.headerName = headerName;
        this.regex = regex;
    }

    @Override
    public boolean matches(@NotNull HttpServletRequest request) {
        String headerValue = request.getHeader(headerName);
        return headerValue != null && headerValue.matches(regex);
    }

    @Override
    public @NonNull MatchResult matcher(@NonNull HttpServletRequest request) {
        return matches(request) ? MatchResult.match(Map.of(headerName, request.getHeader(headerName))) : MatchResult.notMatch();
    }
}
