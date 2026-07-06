package pers.liaohaolong.mokulibserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;

@Slf4j
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public @Nullable Object beforeBodyWrite(
            @Nullable Object body,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response
    ) {
        // 获取请求路径
        String path = request.getURI().getPath();
        // 访问 Swagger 接口时，直接返回
        if (path.contains("/v3/api-docs") || path.contains("/swagger-resources") || path.contains("/swagger-ui"))
            return body;

        // 接口返回 ResultDTO 时，直接返回，避免重复包装
        if (body instanceof ResultDTO)
            return body;

        // 接口返回 void 时，默认返回成功
        if (body == null)
            return ResultDTO.OK;

        // 接口返回 Object 时，视为 data 部分，包装返回
        return ResultDTO.ok().data(body).build();
    }

}
