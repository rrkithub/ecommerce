package com.rkit.e.commerce.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Log before method execution
    @Before("execution(* com.rkit.e.commerce.service..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Executing method: {} with arguments: {}",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()));
    }

    // Log after method execution
    @AfterReturning(value = "execution(* com.rkit.e.commerce.service..*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Method {} executed successfully. Returned: {}",
                joinPoint.getSignature().toShortString(),
                result);
    }

    // Log when an exception occurs
    @AfterThrowing(value = "execution(* com.rkit.e.commerce.service..*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        logger.error("Exception in method: {}. Error: {}",
                joinPoint.getSignature().toShortString(),
                ex.getMessage());
    }
}
