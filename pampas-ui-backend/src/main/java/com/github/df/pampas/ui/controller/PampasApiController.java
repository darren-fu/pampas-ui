package com.github.df.pampas.ui.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by darrenfu on 18-2-1.
 *
 * @author: darrenfu
 * @date: 18-2-1
 */
@RestController
public class PampasApiController {

    @RequestMapping("/test")
    public String test() {
        return "OK";
    }
}
