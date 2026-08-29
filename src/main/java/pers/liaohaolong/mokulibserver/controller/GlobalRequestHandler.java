package pers.liaohaolong.mokulibserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import pers.liaohaolong.mokulibserver.config.RequestHeaderConfigurations;

@Slf4j
@Component
public class GlobalRequestHandler implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        setAttribute(request, RequestHeaderConfigurations.CACHE_CONTROL);
        return true;
    }

    private void setAttribute(HttpServletRequest request, RequestHeaderConfigurations.Configuration<?> configuration) throws Exception {
        request.setAttribute(configuration.getKey(), configuration.parse(request.getHeader(configuration.getKey())));
    }

}
