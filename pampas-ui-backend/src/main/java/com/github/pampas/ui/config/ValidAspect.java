package com.github.pampas.ui.config;

import com.github.pampas.ui.base.vo.Request;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.Collection;
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
            if (o != null && o instanceof Request) {
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
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(t);
        Field[] allFields = FieldUtils.getAllFields(t.getClass());
        for (Field field : allFields) {
            field.setAccessible(true);
            Object val = ReflectionUtils.getField(field, t);
            if (val == null) {
                continue;
            }
            if ( val instanceof Request) {
                constraintViolations.addAll(validator.validate(val));
            }
            if(val instanceof Collection){
                ((Collection) val).forEach(v->constraintViolations.addAll(validator.validate(v)));
            }
        }
        if (constraintViolations.size() > 0) {
            String validateError = "";
            for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
                validateError += constraintViolation.getMessage() + ";";
            }
            throw new IllegalArgumentException(validateError);
        }
    }

}
