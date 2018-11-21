package com.github.pampas.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by darrenfu on 18-2-1.
 *
 * @author: darrenfu
 * @date: 18-2-1
 */
@SpringBootApplication
@ComponentScan(value = {"com.github.pampas"})
@EnableSwagger2
public class PampasApplication {
    public static void main(String[] args) {
        SpringApplication.run(PampasApplication.class, args);
    }

}
