package com.github.pampas.ui.controller;

import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.vo.req.UserLoginReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Description:
 * User: darrenfu
 * Date: 2018-10-06
 */
@RestController
@RequestMapping("/admin")
public class PampasAdminController {

    private static final Logger log = LoggerFactory.getLogger(PampasAdminController.class);

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public Response login(@RequestBody UserLoginReq req) {
        log.info("用户登录");
        return Response.buildSuccessEmptyResponse();
    }

}
