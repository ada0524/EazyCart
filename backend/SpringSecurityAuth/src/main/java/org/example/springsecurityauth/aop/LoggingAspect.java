package org.example.springsecurityauth.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.example.springsecurityauth.exception.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@Aspect
@Component
public class LoggingAspect {

    private Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @AfterThrowing(value = "execution(* org.example.springsecurityauth.controller.*.*(..))", throwing = "ex")
    public void handleRegistrationException(JoinPoint joinPoint, Throwable ex) {
        logger.error("From LoggingAspect.logThrownException in controller: " + ex.getMessage() + ": " + joinPoint.getSignature());
    }
}
// Use logging aspect to log out messages on the entire controller layer.
// The exception message will not only be returned to the user via HTTP but also printed on the terminal.
