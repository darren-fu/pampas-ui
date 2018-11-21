package com.github.pampas.ui.config;

import com.github.pampas.ui.base.BusinessException;
import com.github.pampas.ui.base.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-19
 */
@RestControllerAdvice
public class ExceptionAspect {
    private static final Logger log = LoggerFactory.getLogger(ExceptionAspect.class);

    //其他异常，输出500错误
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response> returnIllArgError(Exception ex, HttpServletRequest request) {
        log.error("IllegalArgumentException:{}", ex.getMessage(), ex);
        return new ResponseEntity<>(new Response("10" + "00", ex.getMessage()), HttpStatus.OK);
    }

    //其他异常，输出500错误
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response> returnBusiError(BusinessException ex, HttpServletRequest request) {
        log.error("BusinessException:{}", ex.getMessage(), ex);
        return new ResponseEntity<>(new Response("10" + "01", ex.getMessage()), HttpStatus.OK);
    }

    //其他异常，输出500错误
    @ExceptionHandler()
    public ResponseEntity<Response> returnRuntimeError(Exception ex, HttpServletRequest request) {
        log.error("INTERNAL_SERVER_ERROR:{}", ex.getMessage(), ex);
        return new ResponseEntity<>(new Response("10" + "02", ex.getMessage()), HttpStatus.OK);
    }

}
