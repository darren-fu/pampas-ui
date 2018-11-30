package com.github.pampas.ui.service;

import com.github.pampas.storage.entity.GatewayInstance;
import com.github.pampas.storage.entity.GatewayRouteRuleRel;
import com.github.pampas.storage.entity.RouteRule;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.base.GatewayInstanceService;
import com.github.pampas.ui.service.base.RouteRuleService;
import com.github.pampas.ui.utils.BeanTools;
import com.github.pampas.ui.vo.req.GatewayInstanceListReq;
import com.github.pampas.ui.vo.req.GatewayInstanceSaveReq;
import com.github.pampas.ui.vo.resp.GatewayInstanceResp;
import com.github.pampas.ui.vo.resp.RouteRuleResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-30
 */
@Service
public class PampasGatewayServiceImpl implements PampasGatewayService {

    @Autowired
    private GatewayInstanceService gatewayInstanceService;

    @Autowired
    private RouteRuleService routeRuleService;

    @Override
    public Response<GatewayInstanceResp> getGateway(Integer gatewayId) {
        GatewayInstance gateway = gatewayInstanceService.getGateway(gatewayId);
        GatewayInstanceResp resp = BeanTools.copyBean(gateway, GatewayInstanceResp.class);
        Result<GatewayRouteRuleRel> routeRuleRel = gatewayInstanceService.getRouteRuleRel(gatewayId);
        resp.setRelRuleNum(routeRuleRel.getCount());
        return Response.buildSuccessResponseWithInfo(resp);
    }

    @Override
    public Response<Result<GatewayInstanceResp>> getGatewayList(GatewayInstanceListReq req, Integer pageNum, Integer pageSize) {
        Result<GatewayInstance> gatewayListResult = gatewayInstanceService.getGatewayList(req, pageNum, pageSize);
        Map<String, Long> countMap = gatewayInstanceService.countRouteRuleRel(gatewayListResult.getData().stream().map(v -> v.getId()).toArray(Integer[]::new));
        List<GatewayInstanceResp> respList = BeanTools.copyBeans(gatewayListResult.getData(), GatewayInstanceResp.class);
        respList.stream().forEach(v -> v.setRelRuleNum(countMap.getOrDefault(v.getInstanceId(), 0L).intValue()));
        return Response.buildSuccessResponseWithResult(respList, gatewayListResult.getCount());
    }

    @Override
    public Response<Result<RouteRuleResp>> getRouteRuleRel(Integer gatewayId) {
        Result<GatewayRouteRuleRel> routeRuleRel = gatewayInstanceService.getRouteRuleRel(gatewayId);
        List<Integer> ruleIdList = routeRuleRel.getData().stream().mapToInt(GatewayRouteRuleRel::getRouteRuleId).boxed().collect(Collectors.toList());
        List<RouteRule> routeRuleList = routeRuleService.getRouteRuleList(ruleIdList);
        List<RouteRuleResp> routeRuleRespList = BeanTools.copyBeans(routeRuleList, RouteRuleResp.class);
        return Response.buildSuccessResponseWithResult(routeRuleRespList, routeRuleRespList.size());
    }

    @Override
    public GatewayInstanceResp save(GatewayInstanceSaveReq req) {
        return null;
    }
}
