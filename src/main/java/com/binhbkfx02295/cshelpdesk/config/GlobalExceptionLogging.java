package com.binhbkfx02295.cshelpdesk.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Aspect
@Slf4j
public class GlobalExceptionLogging {

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void atServices() {}

    @Around("atServices()")
    public Object doLogging(ProceedingJoinPoint jp) throws Throwable {
        long start = System.nanoTime();
        long end;
        try {
            Object result = jp.proceed();
            end = System.nanoTime();

            log.info("[OK] execution done: {}ms at {}.{}()",
                    (end - start) / 1_000_000,
                    jp.getSignature().getClass().getSimpleName(),
                    jp.getSignature().getName()
            );

            return result;
        } catch (Throwable ex) {
            end = System.nanoTime();

            log.info("[EXCEPTION] {}ms after {}.{}()",
                    (end - start) / 1_000_000,
                    jp.getSignature().getClass().getSimpleName(),
                    jp.getSignature().getName()
            );
            throw ex;
        }
    }
}
