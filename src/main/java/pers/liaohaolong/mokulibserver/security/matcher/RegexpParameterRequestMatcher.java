package pers.liaohaolong.mokulibserver.security.matcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.intellij.lang.annotations.RegExp;
import org.jspecify.annotations.NonNull;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Map;

/**
 * <h3>正则表达式请求参数匹配器</h3>
 *
 * <p>用于检查指定的请求参数的值是否匹配正则表达式。</p>
 */
@SuppressWarnings("ClassCanBeRecord")
@Getter
public class RegexpParameterRequestMatcher implements RequestMatcher {

    private final String parameterName;

    @RegExp
    private final String regexp;

    public RegexpParameterRequestMatcher(@NotNull String parameterName, @RegExp @NotNull String regexp) {
        this.parameterName = parameterName;
        this.regexp = regexp;
    }

    @Override
    public boolean matches(@NotNull HttpServletRequest request) {
        String parameterValue = request.getParameter(parameterName);
        return parameterValue != null && parameterValue.matches(regexp);
    }

    @Override
    public @NonNull MatchResult matcher(@NonNull HttpServletRequest request) {
        return matches(request) ? MatchResult.match(Map.of(parameterName, request.getParameter(parameterName))) : MatchResult.notMatch();
    }

}
