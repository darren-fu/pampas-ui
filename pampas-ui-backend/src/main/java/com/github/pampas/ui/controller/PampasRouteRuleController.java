package com.github.pampas.ui.controller;

import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.PampasRouteRuleService;
import com.github.pampas.ui.vo.req.RouteRuleSaveReq;
import com.github.pampas.ui.vo.req.RuleRelGatewaySaveReq;
import com.github.pampas.ui.vo.resp.GatewayInstanceResp;
import com.github.pampas.ui.vo.resp.GatewayTreeResp;
import com.github.pampas.ui.vo.resp.RouteRuleResp;
import com.github.pampas.ui.vo.resp.RouteRuleTreeResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-28
 */
@RestController
@CrossOrigin
public class PampasRouteRuleController {

    @Autowired
    private PampasRouteRuleService ruteRuleService;

    @RequestMapping(value = "/route_rule/get", method = RequestMethod.GET)
    public Response<RouteRuleResp> getRouteRule(@RequestParam("id") Integer ruleId) {
        return ruteRuleService.getRouteRule(ruleId);
    }

    @RequestMapping(value = "/route_rule/list", method = RequestMethod.GET)
    public Response<Result<RouteRuleResp>> listRouteRule(@RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "status", required = false) Boolean status,
                                                         @RequestParam(value = "page_num", required = false) Integer pageNum,
                                                         @RequestParam(value = "page_size", required = false) Integer pageSize) {
        return ruteRuleService.getRouteRuleList(name, status, pageNum, pageSize);
    }

    @RequestMapping(value = "/route_rule/tree", method = RequestMethod.GET)
    public Response<Result<RouteRuleTreeResp>> getGatewayList() {
        return ruteRuleService.getRouteRuleTree();
    }


    @RequestMapping(value = "/route_rule/rel_gateway", method = RequestMethod.GET)
    public Response<Result<GatewayInstanceResp>> getRouteRuleRelGateway(@RequestParam("id") Integer ruleId) {
        return ruteRuleService.getRouteRuleRelGateway(ruleId);
    }

    @RequestMapping(value = "/route_rule/save", method = RequestMethod.POST)
    public Response<RouteRuleResp> saveRouteRule(@RequestBody RouteRuleSaveReq req) {
        return ruteRuleService.save(req);
    }

    @RequestMapping(value = "/route_rule/save_rel", method = RequestMethod.POST)
    public Response saveRouteRuleRelGateway(@RequestBody RuleRelGatewaySaveReq req) {
        return ruteRuleService.saveRel(req);
    }

}
