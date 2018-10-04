package com.github.pampas.ui.controller;

import com.github.pampas.storage.entity.DBGatewayServer;
import com.github.pampas.ui.service.GatewayServerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by darrenfu on 18-2-1.
 *
 * @author: darrenfu
 * @date: 18-2-1
 */
@Api
@RestController
public class PampasApiController {

    @Autowired
    private GatewayServerService gatewayServerService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "OK";
    }


    @ApiOperation("查询网关服务列表")
    @RequestMapping(value = "/gateway_server/query", method = RequestMethod.POST)
    public List<DBGatewayServer> queryGatewayServer() {
        return gatewayServerService.query();
    }
}
