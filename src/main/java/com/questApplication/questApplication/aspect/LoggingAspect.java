package com.questApplication.questApplication.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public
class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger ( LoggingAspect.class );


    @Pointcut("execution(* com.questApplication.questApplication.api.controller.*.*(..)) ||" +
              " execution(* com.questApplication.questApplication.business.concretes.*.*(..))")
    public void controllerAndServiceMethods(){}


    @Before("controllerAndServiceMethods()")
    public void logBeforeMethod( JoinPoint joinPoint ){
        log.info ( "method "+joinPoint.getSignature ()+" started" );
    }

    @AfterThrowing(value = "controllerAndServiceMethods()",throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint,Throwable exception){
        log.error ( "Hata oluştu: " + joinPoint.getSignature ().getName () );
        log.error ( "Hata mesajı: "+ exception.getMessage () );
    }

    @AfterReturning(value = "controllerAndServiceMethods()",returning = "result")
    public void logAfterReturning(JoinPoint joinPoint,Object result){
        log.info ( "İşlem başarıyla tamamlandı: "+joinPoint.getSignature ().getName () );
        log.info ( "Dönen sonuç: "+ result );
    }

    @Around ( "controllerAndServiceMethods()" )
    public Object logExecutionTime( ProceedingJoinPoint joinPoint ) throws Throwable{
        long start = System.currentTimeMillis ();
        Object proceed = joinPoint.proceed ();
        long executionTime = System.currentTimeMillis ()-start;

        log.info ( joinPoint.getSignature () +" metodu " + executionTime +" ms sürdü." );
        return  proceed;
    }

}
