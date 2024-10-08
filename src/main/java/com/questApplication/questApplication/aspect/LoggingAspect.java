package com.questApplication.questApplication.aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.questApplication.questApplication.api.controller.*.*(..)) ||" +
            " execution(* com.questApplication.questApplication.business.concretes.*.*(..))")
    public void controllerAndServiceMethods() {}

    @Around("controllerAndServiceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Giriş: {}.{}() argümanlar = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), joinPoint.getArgs());
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Çıkış: {}.{}() sonuç = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (Throwable e) {
            throw e;
        }
    }
}
