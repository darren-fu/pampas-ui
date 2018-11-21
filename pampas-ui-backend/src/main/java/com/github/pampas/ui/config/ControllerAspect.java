package com.github.pampas.ui.config;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 *
 */
@Aspect
@Component
public class ControllerAspect {
    private static final Logger log = LoggerFactory.getLogger(ControllerAspect.class);

    private static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Around("execution(public * com.github.pampas..*.*(..)) && @target(org.springframework.web.bind.annotation.RestController)")
    public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        boolean success = true;
        long start = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } catch (Throwable ex) {
            success = false;
            throw ex;
        } finally {
            long cost = System.currentTimeMillis() - start;
            String methodName = pjp.getSignature().getName();
            Object[] args = pjp.getArgs();
            if (success && log.isDebugEnabled()) {
                log.debug("[{}]请求:{},参数:{},结果:{}", success ? "成功" : "失败", methodName, ArrayUtils.toString(args), result);
            }
            if (!success && log.isWarnEnabled()) {
                log.debug("[{}]请求:{},参数:{},结果:{}", success ? "成功" : "失败", methodName, ArrayUtils.toString(args), result);
            }

        }
    }


    /**
     * Validate.
     *
     * @param <T> the type parameter
     * @param t   the t
     */
    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        if (constraintViolations.size() > 0) {
            String validateError = "";
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                validateError += constraintViolation.getMessage() + ";";
            }
            throw new IllegalArgumentException(validateError);
        }
    }

}
