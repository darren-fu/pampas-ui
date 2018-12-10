package com.github.pampas.ui.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pampas.common.route.rule.AbstractRule;
import com.github.pampas.common.route.rule.DubboRule;
import com.github.pampas.common.route.rule.HttpRule;
import com.github.pampas.common.route.rule.RuleTypeEnum;
import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.common.tools.JsonTools;
import com.github.pampas.storage.entity.GatewayInstance;
import com.github.pampas.storage.entity.RouteRule;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.base.RouteRuleService;
import com.github.pampas.ui.utils.BeanTools;
import com.github.pampas.ui.vo.req.RouteRuleSaveReq;
import com.github.pampas.ui.vo.req.RuleRelGatewaySaveReq;
import com.github.pampas.ui.vo.resp.GatewayInstanceResp;
import com.github.pampas.ui.vo.resp.RouteRuleResp;
import com.github.pampas.ui.vo.resp.RouteRuleTreeResp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-28
 */
@Service
public class PampasRouteRuleServiceImpl implements PampasRouteRuleService {

    private static final Logger log = LoggerFactory.getLogger(PampasRouteRuleServiceImpl.class);

    @Autowired
    private RouteRuleService routeRuleService;

    @Override
    public Response<RouteRuleResp> getRouteRule(Integer ruleId) {
        RouteRule routeRule = routeRuleService.getRouteRule(ruleId);
        RouteRuleResp routeRuleResp = BeanTools.copyBean(routeRule, RouteRuleResp.class);
        if (StringUtils.isNotEmpty(routeRule.getContent())) {
            List<Map> list = JsonTools.NON_NULL.fromJson(routeRule.getContent(), new TypeReference<List<Map>>() {
            });
            routeRuleResp.setRuleList(list);
        }
        return Response.buildSuccessResponseWithInfo(routeRuleResp);
    }

    @Override
    public Response<Result<RouteRuleResp>> getRouteRuleList(String name, Boolean status,
                                                            Integer pageNum, Integer pageSize) {
        Result<RouteRule> routeRule = routeRuleService.getRouteRuleList(name, status, pageNum, pageSize);
        List<RouteRuleResp> routeRuleRespList = BeanTools.copyBeans(routeRule.getData(), RouteRuleResp.class);
        return Response.buildSuccessResponseWithResult(routeRuleRespList, routeRule.getCount());
    }

    @Override
    public Response<Result<RouteRuleTreeResp>> getRouteRuleTree() {
        Result<RouteRule> routeRuleList = routeRuleService.getRouteRuleList(null, null, null, null);
        if (routeRuleList.getCount() == 0) {
            return Response.buildSuccessEmptyResponse();
        }
        int groupId = -1;

        Map<String, List<RouteRule>> routeRuleListMap = routeRuleList.getData().stream().collect(Collectors.groupingBy(RouteRule::getGroup));
        List<RouteRuleTreeResp> treeList = new ArrayList<>();
        for (Map.Entry<String, List<RouteRule>> entry : routeRuleListMap.entrySet()) {
            RouteRuleTreeResp ruleTreeResp = new RouteRuleTreeResp();
            ruleTreeResp.setId(--groupId);
            ruleTreeResp.setLabel("规则分组：" + entry.getKey());

            List<RouteRuleTreeResp.RuleTreeItem> treeItemList = entry.getValue().stream().map(v -> {
                RouteRuleTreeResp.RuleTreeItem treeItem = new RouteRuleTreeResp.RuleTreeItem();
                treeItem.setId(v.getId());
                treeItem.setLabel("[" + v.getId() + "]" + v.getName() + " " + v.getMappingHost());
                treeItem.setGroup(v.getGroup());
                treeItem.setStatus(v.getStatus());
                return treeItem;
            }).collect(Collectors.toList());
            ruleTreeResp.setChildren(treeItemList);
            treeList.add(ruleTreeResp);
        }
        return Response.buildSuccessResponseWithResult(treeList, treeList.size());
    }

    @Override
    public Response<Result<GatewayInstanceResp>> getRouteRuleRelGateway(Integer ruleId) {
        List<GatewayInstance> routeRuleRelGateway = routeRuleService.getRouteRuleRelGateway(ruleId);
        List<GatewayInstanceResp> gatewayInstanceRespList = BeanTools.copyBeans(routeRuleRelGateway, GatewayInstanceResp.class);
        return Response.buildSuccessResponseWithResult(gatewayInstanceRespList, gatewayInstanceRespList.size());
    }

    @Override
    public Response<RouteRuleResp> save(RouteRuleSaveReq saveReq) {
        RouteRule routeRule = BeanTools.copyBean(saveReq, RouteRule.class);

        if (saveReq.getMode() == 1) {
            routeRule.setContent(null);
        } else if (saveReq.getMode() == 2) {
            AssertTools.notNull(saveReq.getId(), "请先保存规则基本信息");
            List<AbstractRule> ruleList = new ArrayList<>(saveReq.getRuleList().size());
            JsonTools jsonTools = JsonTools.NON_NULL;
            // 格式化规则详情
            if (saveReq.getRuleList() != null && saveReq.getRuleList().size() > 0) {
                for (Map<String, String> ruleMap : saveReq.getRuleList()) {
                    String json = jsonTools.toJson(ruleMap);
                    String type = ruleMap.get("type");
                    AssertTools.isTrue(RuleTypeEnum.getEnum(type) != null, "非法的规则类型:" + type);
                    AbstractRule rule = null;
                    if (RuleTypeEnum.HTTP.getValue().equals(type)) {
                        rule = jsonTools.fromJson(json, HttpRule.class);
                        AssertTools.isTrue(rule.getMappingStrategy() != null, "非法的路径映射策略:" + ruleMap.get("mapping_strategy"));
                        AssertTools.isTrue(rule.getHostStrategy() != null, "非法的目标服务地址匹配策略:" + ruleMap.get("host_strategy"));

                    } else if (RuleTypeEnum.DUBBO.getValue().equals(type)) {
                        rule = jsonTools.fromJson(json, DubboRule.class);
                    } else if (RuleTypeEnum.GRPC.getValue().equals(type)) {

                    }
                    if (rule != null) {
                        ruleList.add(rule);
                        log.debug("转换规则Rule:{}, map:{}", rule, ruleMap);
                    }
                }
                AssertTools.notEmpty(ruleList, "转换的规则数量为0");
            }
            String content = jsonTools.toJson(ruleList);
            routeRule.setContent(content);
            routeRule.setName(null);
            routeRule.setGroup(null);
            routeRule.setMappingHost(null);
        }


        RouteRule save = routeRuleService.save(routeRule);
        RouteRuleResp routeRuleResp = BeanTools.copyBean(save, RouteRuleResp.class);
        return Response.buildSuccessResponseWithInfo(routeRuleResp);
    }

    @Override
    public void delete(Integer ruleId) {
        //todo 删除前判断是否被使用
        routeRuleService.delete(ruleId);
    }

    @Override
    public Response saveRel(RuleRelGatewaySaveReq req) {
        AssertTools.notEmpty(req.getRuleIdList(), "路由规则不能为空");
        routeRuleService.saveRel(req.getRuleIdList().get(0), req.getGatewayIdList());
        return Response.buildSuccessEmptyResponse();
    }

}
