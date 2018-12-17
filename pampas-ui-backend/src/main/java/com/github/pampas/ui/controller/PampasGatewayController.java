package com.github.pampas.ui.controller;

import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.PampasGatewayService;
import com.github.pampas.ui.service.PampasNotifyService;
import com.github.pampas.ui.vo.req.GatewayConfigSaveReq;
import com.github.pampas.ui.vo.req.GatewayInstanceListReq;
import com.github.pampas.ui.vo.req.GatewayInstanceSaveReq;
import com.github.pampas.ui.vo.req.RuleRelGatewaySaveReq;
import com.github.pampas.ui.vo.resp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-30
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RestController
@CrossOrigin
public class PampasGatewayController {

    @Autowired
    private PampasGatewayService gatewayService;

    @Autowired
    private PampasNotifyService notifyService;

    /**
     * Gets gateway.
     *
     * @param gatewayId the gateway id
     * @return the gateway
     */
    @RequestMapping(value = "/gateway/get", method = RequestMethod.GET)
    public Response<GatewayInstanceResp> getGateway(@RequestParam("id") Integer gatewayId) {
        return gatewayService.getGateway(gatewayId);
    }

    /**
     * Gets gateway list.
     *
     * @param req the req
     * @return the gateway list
     */
    @RequestMapping(value = "/gateway/list", method = RequestMethod.POST)
    public Response<Result<GatewayInstanceResp>> getGatewayList(@RequestBody GatewayInstanceListReq req,
                                                                @RequestParam("page_num") Integer pageNum,
                                                                @RequestParam("page_size") Integer pageSize) {
        return gatewayService.getGatewayList(req, pageNum, pageSize);
    }


    @RequestMapping(value = "/gateway/tree", method = RequestMethod.GET)
    public Response<Result<GatewayTreeResp>> getGatewayList() {
        return gatewayService.getGatewayTree();
    }

    /**
     * Gets route rule rel.
     *
     * @param gatewayId the gateway id
     * @return the route rule rel
     */
    @RequestMapping(value = "/gateway/get_rel_rule", method = RequestMethod.GET)
    public Response<Result<RouteRuleResp>> getRouteRuleRel(@RequestParam("id") Integer gatewayId) {
        return gatewayService.getRouteRuleRel(gatewayId);
    }

    /**
     * 保存新增、修改后的网关实例信息.
     *
     * @param req the req
     * @return the gateway instance resp
     */
    @RequestMapping(value = "/gateway/save", method = RequestMethod.GET)
    public GatewayInstanceResp save(@RequestBody GatewayInstanceSaveReq req) {
        return gatewayService.save(req);
    }


    @RequestMapping(value = "/gateway/save_rel", method = RequestMethod.POST)
    public Response saveRouteRuleRelGateway(@RequestBody RuleRelGatewaySaveReq req) {
        return gatewayService.saveRel(req);
    }


    @RequestMapping(value = "/gateway/get_config_list", method = RequestMethod.GET)
    public Response<Result<GatewayConfigResp>> getConfigList(@RequestParam(value = "id", required = false) Integer gatewayId,
                                                             @RequestParam(value = "group", required = false) String gatewayGroup,
                                                             @RequestParam(value = "instance_id", required = false) String gatewayInstanceId,
                                                             @RequestParam(value = "spi_class", required = false) String spiClass) {
        return gatewayService.getGatewayConfigList(gatewayId, gatewayGroup, gatewayInstanceId, spiClass);
    }

    @RequestMapping(value = "/gateway/get_spi_list", method = RequestMethod.GET)
    public Response<Result<GatewaySpiResp>> getSpiList(@RequestParam(value = "id", required = false) Integer gatewayId,
                                                       @RequestParam(value = "group", required = false) String gatewayGroup,
                                                       @RequestParam(value = "instance_id", required = false) String gatewayInstanceId) {
        return gatewayService.getGatewaySpiList(gatewayId, gatewayGroup, gatewayInstanceId);
    }


    @RequestMapping(value = "/gateway/save_config", method = RequestMethod.POST)
    public Response saveGatewayConfig(@RequestBody GatewayConfigSaveReq req) {
        return gatewayService.saveGatewayConfig(req);
    }


    @RequestMapping(value = "/gateway/notify_config", method = RequestMethod.GET)
    public Response getSpiList(
            @RequestParam(value = "group", required = false) String gatewayGroup,
            @RequestParam(value = "instance_id", required = false) String gatewayInstanceId,
            @RequestParam(value = "spi_class", required = false) String configSpiClass) {
        return notifyService.notifyGatewayConfigUpdate(gatewayGroup, gatewayInstanceId, configSpiClass);
    }


}
