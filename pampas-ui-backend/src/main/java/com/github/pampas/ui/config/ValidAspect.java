package com.github.pampas.ui.config;

import com.github.pampas.ui.base.vo.Request;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;


@Aspect
@Component
public class ValidAspect {
    private static final Logger logger = LoggerFactory.getLogger(ValidAspect.class);

    private static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Before("execution(public * com.github.pampas..*.*(..)) && this(com.github.pampas.ui.base.IngressService)")
    public void validHandle(JoinPoint joinPoint) {
        Object obj[] = joinPoint.getArgs();
        for (Object o : obj) {
            if (o instanceof Request) {
                validate(o);
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
