package com.github.pampas.ui.service;

import com.github.pampas.ui.base.IngressService;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.vo.req.RouteRuleSaveReq;
import com.github.pampas.ui.vo.resp.GatewayInstanceResp;
import com.github.pampas.ui.vo.resp.RouteRuleResp;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-28
 */
public interface PampasRouteRuleService extends IngressService {
    /**
     * 获取指定路由规则详情
     *
     * @param ruleId the rule id
     * @return the route rule
     */
    Response<RouteRuleResp> getRouteRule(Integer ruleId);

    /**
     * 获取路由规则 列表
     *
     * @param name   the name
     * @param status the status
     * @return the route rule
     */
    Response<Result<RouteRuleResp>> getRouteRuleList(String name, Boolean status, Integer pageNum, Integer pageSize);

    /**
     * 获取路由规则关联的网关实例
     *
     * @param ruleId the rule id
     * @return the route rule rel gateway
     */
    Response<Result<GatewayInstanceResp>> getRouteRuleRelGateway(Integer ruleId);

    /**
     * 保存路由规则
     *
     * @param routeRule the route rule
     * @return the route rule
     */
    Response<RouteRuleResp> save(RouteRuleSaveReq routeRule);

    /**
     * 删除指定路由规则.
     *
     * @param ruleId the rule id
     */
    void delete(Integer ruleId);
}
