package com.infy.utility;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Component
@Aspect
public class LoggingAspect {
	public static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);

	@AfterThrowing(pointcut = "execution(* com.infy.service.*Impl.*(..))", throwing = "exception")
	public void logServiceException(Exception exception) throws Exception {
		LOGGER.error(exception.getMessage(), exception);
	}

}
