package com.fever.alf.challenge.provider_integration.infrastructure.monitoring;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceLoggingAspect {

    private final PlanMetrics metrics;

    public PerformanceLoggingAspect(PlanMetrics metrics) {
        this.metrics = metrics;
    }

    @Around("execution(* com.fever.alf.challenge.provider_integration.infrastructure.web.*Controller.*(..))")
    public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        try {
            Object[] args = joinPoint.getArgs();
            log.info("Executing {}.{} with parameters: {}", className, methodName, args);

            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            log.info("Method {}.{} completed in {} ms", className, methodName, executionTime);
            metrics.recordProviderLatency(executionTime);

            if (executionTime > 500) {
                log.warn("High latency detected in {}.{}: {} ms", className, methodName, executionTime);
            }

            return result;
        } catch (Exception e) {
            long failureTime = System.currentTimeMillis() - startTime;
            log.error("Exception in {}.{} after {} ms: {}", className, methodName, failureTime, e.getMessage(), e);
            throw e;
        }
    }
}
