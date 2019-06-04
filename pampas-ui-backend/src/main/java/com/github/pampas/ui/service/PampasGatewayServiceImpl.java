package com.github.pampas.ui.service;

import com.github.pampas.common.base.PampasConsts;
import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.common.tools.StreamTools;
import com.github.pampas.storage.entity.*;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.base.GatewayInstanceService;
import com.github.pampas.ui.service.base.GatewaySpiService;
import com.github.pampas.ui.service.base.RouteRuleService;
import com.github.pampas.ui.utils.BeanTools;
import com.github.pampas.ui.vo.req.*;
import com.github.pampas.ui.vo.resp.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-30
 */
@SuppressWarnings("Duplicates")
@Service
public class PampasGatewayServiceImpl implements PampasGatewayService {

    @Autowired
    private GatewayInstanceService gatewayInstanceService;

    @Autowired
    private RouteRuleService routeRuleService;

    @Autowired
    private GatewaySpiService gatewaySpiService;

    @Autowired
    private PampasNotifyService pampasNotifyService;

    @Override
    public Response<GatewayInstanceResp> getGateway(Integer gatewayId) {
        List<GatewayInstance> gatewayInstanceList = gatewayInstanceService.getGateway(gatewayId);
        if (CollectionUtils.isEmpty(gatewayInstanceList)) {
            return Response.buildSuccessEmptyResponse();
        }
        GatewayInstance gateway = gatewayInstanceList.get(0);
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

        if (CollectionUtils.isEmpty(ruleIdList)) {
            return Response.buildSuccessResponseWithResult(Collections.EMPTY_LIST, 0);
        }
        List<RouteRule> routeRuleList = routeRuleService.getRouteRuleList(ruleIdList);
        List<RouteRuleResp> routeRuleRespList = BeanTools.copyBeans(routeRuleList, RouteRuleResp.class);
        return Response.buildSuccessResponseWithResult(routeRuleRespList, routeRuleRespList.size());
    }

    @Override
    public GatewayInstanceResp save(GatewayInstanceSaveReq req) {
        return null;
    }

    @Override
    public Response saveRel(RuleRelGatewaySaveReq req) {
        AssertTools.notEmpty(req.getGatewayIdList(), "网关ID不能为空");
        gatewayInstanceService.saveRel(req.getGatewayIdList(), req.getRuleIdList());

        List<GatewayInstance> gatewayInstanceList = gatewayInstanceService.getGateway(req.getGatewayIdList());
        for (GatewayInstance instance : gatewayInstanceList) {
            pampasNotifyService.notifyConfigLoaderWithKey(instance, PampasConsts.ConfigLoaderKey.ROUTE_RULE);
            pampasNotifyService.notifyConfigLoaderWithName(instance, "config-loader-auth-filter");
        }

        return Response.buildSuccessEmptyResponse();
    }

    @Override
    public Response<Result<GatewayTreeResp>> getGatewayTree() {
        Result<GatewayInstance> gatewayListResult = gatewayInstanceService.getGatewayList(new GatewayInstanceListReq(), null, null);
        if (gatewayListResult.getCount() == 0) {
            return Response.buildSuccessEmptyResponse();
        }
        int groupId = 0;

        Map<String, List<GatewayInstance>> gatewayListMap = gatewayListResult.getData().stream().collect(Collectors.groupingBy(GatewayInstance::getGroup));
        List<GatewayTreeResp> gatewayTreeRespList = new ArrayList<>();
        for (Map.Entry<String, List<GatewayInstance>> entry : gatewayListMap.entrySet()) {
            GatewayTreeResp gatewayTreeResp = new GatewayTreeResp();
            gatewayTreeResp.setId(--groupId);
            gatewayTreeResp.setLabel("分组：" + entry.getKey());

            List<GatewayTreeResp.GatewayTreeItem> treeItemList = entry.getValue().stream().map(v -> {
                GatewayTreeResp.GatewayTreeItem treeItem = new GatewayTreeResp.GatewayTreeItem();
                treeItem.setId(v.getId());
                treeItem.setLabel(v.getHostName() + " - " + v.getServerName() + " - " + v.getHost() + ":" + v.getProxyPort());
                treeItem.setGroup(v.getGroup());
                return treeItem;
            }).collect(Collectors.toList());
            gatewayTreeResp.setChildren(treeItemList);
            gatewayTreeRespList.add(gatewayTreeResp);
        }
        return Response.buildSuccessResponseWithResult(gatewayTreeRespList, gatewayTreeRespList.size());
    }

    @Override
    public Response<Result<GatewayConfigResp>> getGatewayConfigList(Integer gatewayId, String gatewayGroup, String gatewayInstanceId, String spiClass) {
        String instanceId = gatewayInstanceId;
        if (gatewayId != null) {
            List<GatewayInstance> gateway = gatewayInstanceService.getGateway(gatewayId);
            AssertTools.notEmpty(gateway, "不存在此网关:" + gatewayId);
            gatewayGroup = gateway.get(0).getGroup();
            instanceId = gateway.get(0).getInstanceId();
        }
        AssertTools.notEmpty(gatewayGroup, "网关分组不能为空");
        List<GatewayConfig> gatewayConfigList = gatewaySpiService.getGatewayConfigList(gatewayGroup, instanceId, spiClass);
        List<GatewayConfigResp> gatewayConfigResps = BeanTools.copyBeans(gatewayConfigList, GatewayConfigResp.class);

        Map<String, List<GatewayConfigResp>> listMap = StreamTools.groupBy(gatewayConfigResps, GatewayConfigResp::getConfigSpiClass);
        Map cache = new HashMap();
        for (GatewayConfigResp configResp : gatewayConfigResps) {
            List<GatewayConfigResp> list = listMap.get(configResp.getConfigSpiClass());
            cache.computeIfAbsent(configResp.getConfigSpiClass(), v -> {
                configResp.setRowSpan(list.size());
                return 1;
            });
        }
        cache.clear();

        return Response.buildSuccessResponseWithResult(gatewayConfigResps, gatewayConfigResps.size());
    }

    @Override
    public Response<Result<GatewaySpiResp>> getGatewaySpiList(Integer gatewayId, String gatewayGroup, String gatewayInstanceId, String spiInterface) {
        if (gatewayId != null) {
            List<GatewayInstance> gateway = gatewayInstanceService.getGateway(gatewayId);
            AssertTools.notEmpty(gateway, "不存在此网关:" + gatewayId);
            gatewayGroup = gateway.get(0).getGroup();
            gatewayInstanceId = gateway.get(0).getInstanceId();
        }
        AssertTools.notEmpty(gatewayGroup, "网关分组不能为空");
        List<GatewaySpi> spiList = gatewaySpiService.getSpiList(gatewayGroup, gatewayInstanceId, spiInterface);
        List<GatewaySpiResp> spiResps = BeanTools.copyBeans(spiList, GatewaySpiResp.class);

        Map<String, List<GatewaySpiResp>> listMap = StreamTools.groupBy(spiResps, GatewaySpiResp::getSpiInterface);
        Map cache = new HashMap();
        for (GatewaySpiResp spiResp : spiResps) {
            List<GatewaySpiResp> list = listMap.get(spiResp.getSpiInterface());
            cache.computeIfAbsent(spiResp.getSpiInterface(), v -> {
                spiResp.setRowSpan(list.size());
                return 1;
            });
        }

        return Response.buildSuccessResponseWithResult(spiResps, spiResps.size());
    }

    @Override
    public Response saveGatewayConfig(GatewayConfigSaveReq req) {
        List<GatewayConfigSaveReq.KeyAndVal> list = req.getList();
        List<GatewayConfig> gatewayConfigList = BeanTools.copyBeans(list, GatewayConfig.class);
        gatewayConfigList.forEach(v -> v.setPush(""));
        gatewaySpiService.saveGatewayConfig(gatewayConfigList);
        return Response.buildSuccessEmptyResponse();
    }

    @Override
    public Response saveGatewaySpi(GatewaySpiSaveReq req) {

        AssertTools.notEmpty(req.getList(), "SPI不能为空");
        List<GatewaySpi> spiList = BeanTools.copyBeans(req.getList(), GatewaySpi.class);
        spiList.forEach(v -> v.setPush("N"));
        gatewaySpiService.saveGatewaySpi(spiList);
        return Response.buildSuccessEmptyResponse();
    }
}
