package com.github.pampas.ui.controller;

import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.GatewayCommonService;
import com.github.pampas.ui.vo.resp.GatewaySpiResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-14
 */
@RestController
@CrossOrigin
public class PampasCommonController {
    private static final Logger log = LoggerFactory.getLogger(PampasAdminController.class);

    @Autowired
    private GatewayCommonService gatewayCommonService;

    @RequestMapping(value = "/common/get_spi_list", method = RequestMethod.GET)
    public Response<Result<GatewaySpiResp>> getSpiList(@RequestParam(value = "spi", required = true) String spiClz,
                                                       @RequestParam(value = "group", required = false) String gatewayGroup,
                                                       @RequestParam(value = "instance_id", required = false) String gatewayInstanceId) {
        return gatewayCommonService.getSpiClassList(spiClz, gatewayGroup, gatewayInstanceId);
    }
}
