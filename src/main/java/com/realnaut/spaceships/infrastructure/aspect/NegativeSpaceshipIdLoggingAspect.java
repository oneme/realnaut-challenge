package com.realnaut.spaceships.infrastructure.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NegativeSpaceshipIdLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(NegativeSpaceshipIdLoggingAspect.class);

    @Before("execution(* com.realnaut.spaceships.core.service.SpaceshipService.getSpaceshipById(..)) && args(id)")
    public void logIfNegativeId(JoinPoint joinPoint, Long id) {
        if (id < 0) {
            logger.warn("Attempt to access a negative ID : {}", id);
        }
    }
}
