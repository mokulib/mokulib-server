package pers.liaohaolong.mokulibserver.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import pers.liaohaolong.mokulibserver.security.matcher.RegexpParameterRequestMatcher;

public final class LoginFilterConfigurations {

    public static final RequestMatcher LOGIN_REQUEST_MATCHER = PathPatternRequestMatcher.withDefaults().matcher("/login");

    public static final RequestMatcher PASSWORD_LOGIN_REQUEST_MATCHER = new AndRequestMatcher(
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/login"),
            new RegexpParameterRequestMatcher("username", RegexpConfigurations.EMAIL_REGEXP),
            new RegexpParameterRequestMatcher("password", RegexpConfigurations.PASSWORD_REGEXP)
    );

    public static final RequestMatcher CAPTCHA_LOGIN_REQUEST_MATCHER = new AndRequestMatcher(
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/login"),
            new RegexpParameterRequestMatcher("username", RegexpConfigurations.EMAIL_REGEXP),
            new RegexpParameterRequestMatcher("password", RegexpConfigurations.EMAIL_CAPTCHA_REGEXP)
    );

}
