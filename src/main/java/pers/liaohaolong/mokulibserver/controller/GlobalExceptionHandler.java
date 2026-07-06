package pers.liaohaolong.mokulibserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import pers.liaohaolong.mokulibserver.dto.ResultDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;

/**
 * <h3>全局异常处理器</h3>
 *
 * <p>此处理器的优先级高于 {@link org.springframework.boot.webmvc.error.ErrorController}.</p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * <h3>处理认证拒绝异常</h3>
     *
     * <p>
     *     {@link AuthorizationDeniedException} 认证拒绝异常：表示接口权限认证失败，通常由用户凭证已过期或越权访问产生。
     * </p>
     *
     * @param ignore 异常
     * @return {@link ResultDTO}
     */
    @ExceptionHandler({AuthorizationDeniedException.class})
    public ResultDTO authorizationDeniedException(AuthorizationDeniedException ignore) {
        return ResultDTO.error().businessType("认证").message("访问拒绝.").build();
    }

    /**
     * <h3>处理请求方法不支持异常</h3>
     *
     * <p>
     *     {@link HttpRequestMethodNotSupportedException} 请求方法不支持异常：表示请求方法不支持。
     * </p>
     * <p>不应由前端页面引起，前端无需考虑对此状态码的处理。</p>
     *
     * @param ignore 异常
     * @return {@link ResultDTO}
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResultDTO handleHttpRequestMethodNotSupportedException(@NotNull Exception ignore) {
        return ResultDTO.error().message("请求方法不支持.").build();
    }

    /**
     * <h3>处理请求错误异常</h3>
     *
     * <p>
     *     {@link HttpMessageNotReadableException} 请求体不可读异常：表示请求体为空或格式错误。<br>
     *     {@link MissingServletRequestParameterException} 缺少请求参数异常：表示请求参数缺失，由 {@link org.springframework.web.bind.annotation.RequestParam} 产生。<br>
     *     {@link MethodArgumentTypeMismatchException} 参数类型不匹配异常：表示请求参数类型不匹配，由 {@link org.springframework.web.bind.annotation.RequestParam} 产生。
     * </p>
     * <p>不应由前端页面引起，前端无需考虑对此状态码的处理。</p>
     *
     * @param ignore 异常
     * @return {@link ResultDTO}
     */
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResultDTO handleBadRequestException(@NotNull Exception ignore) {
        return ResultDTO.error().message("请求错误.").build();
    }

    /**
     * <h3>处理请求体校验失败异常</h3>
     *
     * <p>
     *     {@link MethodArgumentNotValidException} 方法参数无效异常：表示请求体校验失败。
     * </p>
     *
     * @param e 异常
     * @return {@link ResultDTO}
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResultDTO handleMethodArgumentNotValidException(@NotNull MethodArgumentNotValidException e) {
        return ResultDTO.error()
                .message(e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage())
                .data(e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList())
                .build();
    }

    /**
     * <h3>处理请求参数或路径参数校验失败异常</h3>
     *
     * <p>
     *     {@link HandlerMethodValidationException} 参数校验失败异常：由 Controller 的方法的参数的参数校验注解产生。
     * </p>
     *
     * @param e 异常
     * @return {@link ResultDTO}
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResultDTO handleHandlerMethodValidationException(@NotNull HandlerMethodValidationException e) {
        return ResultDTO.error()
                .message(e.getParameterValidationResults().getFirst().getResolvableErrors().getFirst().getDefaultMessage())
                .data(e.getParameterValidationResults().getFirst().getResolvableErrors().stream().map(MessageSourceResolvable::getDefaultMessage).toList())
                .build();
    }

    /**
     * <h3>处理资源未找到异常</h3>
     *
     * <p>
     *     {@link NoResourceFoundException} 资源未找到异常：表示请求的资源不存在。
     * </p>
     *
     * @param ignore 异常
     * @return {@link ResultDTO}
     */
    @ExceptionHandler({NoResourceFoundException.class})
    public ResultDTO handleNoResourceFoundException(@NotNull Exception ignore) {
        return ResultDTO.error().message("资源未找到.").build();
    }

    /**
     * <h3>处理业务异常</h3>
     *
     * <p>
     *     {@link BusinessException} 业务异常：表示业务无法执行用户期望的请求。
     * </p>
     *
     * @param e 异常
     * @return {@link ResultDTO}
     */
    @ExceptionHandler({BusinessException.class})
    public ResultDTO handleBusinessException(@NotNull BusinessException e) {
        return ResultDTO.error().message(e.getMessage()).build();
    }

    /**
     * <h3>处理不应出现的异常</h3>
     *
     * <p>
     *     {@link DataAccessException} 数据访问异常：表示 MySQL 数据库操作异常。<br>
     *     {@link Exception} 未知异常
     * </p>
     *
     * @param e 异常
     * @return {@link ResultDTO}
     */
    @ExceptionHandler({
            DataAccessException.class,
            Exception.class})
    public ResultDTO handleUncontrollableException(@NotNull Exception e) {
        log.warn("Ah! T_T\n", e);
        return ResultDTO.error().message("服务器错误，请稍后重试.").data(e.getMessage()).build();
    }

}
