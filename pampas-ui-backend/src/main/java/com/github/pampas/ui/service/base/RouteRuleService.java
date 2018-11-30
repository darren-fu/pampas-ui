package com.github.pampas.ui.service.base;

import com.github.pampas.storage.entity.GatewayInstance;
import com.github.pampas.storage.entity.RouteRule;
import com.github.pampas.ui.base.vo.Result;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-28
 */
public interface RouteRuleService {

    /**
     * Gets route rule.
     *
     * @param ruleId the rule id
     * @return the route rule
     */
    RouteRule getRouteRule(Integer ruleId);


    List<RouteRule> getRouteRuleList(List<Integer> ruleIdList);

    /**
     * Gets route rule.
     *
     * @param name   the name
     * @param status the status
     * @return the route rule
     */
    Result<RouteRule> getRouteRuleList(String name, Boolean status, Integer pageNum, Integer pageSize);

    /**
     * Gets route rule rel gateway.
     *
     * @param ruleId the rule id
     * @return the route rule rel gateway
     */
    List<GatewayInstance> getRouteRuleRelGateway(Integer ruleId);

    /**
     * Save route rule.
     *
     * @param routeRule the route rule
     * @return the route rule
     */
    RouteRule save(RouteRule routeRule);

    /**
     * Delete.
     *
     * @param ruleId the rule id
     */
    void delete(Integer ruleId);
}
