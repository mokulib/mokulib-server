package pers.liaohaolong.mokulibserver.security;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pers.liaohaolong.mokulibserver.annotation.PrePermission;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class PrePermissionAspect {

    /**
     * 检查方法调用权限
     *
     * @param joinPoint 切点，调用 joinPoint.proceed() 才会继续方法调用
     * @param prePermission 被拦截方法上的注解（自动注入）
     * @return 继续方法调用返回值
     * @throws Throwable 如果抛出此异常，切点方法将不执行
     */
    @Around("@annotation(prePermission)") // 匹配所有带有 PrePermission 注解的方法
    public Object checkPermission(ProceedingJoinPoint joinPoint, PrePermission prePermission) throws Throwable {

        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 执行到控制器时必然已经经过认证系统，认证系统对于未登录用户会授予匿名用户凭据，故而此处获取到的凭据必不为空
        assert authentication != null;

        // 收集用户权限
        Set<String> owned = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // 超级管理员
        if (owned.contains(Permission.ALL.getValue()))
            return joinPoint.proceed();

        // 判断
        if (!prePermission.strategy().match(prePermission.value(), owned))
            throw new AccessDeniedException("Access Denied" +
                    ". Signature=" + joinPoint.getSignature().toShortString() +
                    ", RequirePermission=" + Arrays.toString(prePermission.value()) +
                    ", MatchStrategy=" + prePermission.strategy().name() +
                    ", Permissions=" + owned
            );

        // 放行
        return joinPoint.proceed();

    }

}
