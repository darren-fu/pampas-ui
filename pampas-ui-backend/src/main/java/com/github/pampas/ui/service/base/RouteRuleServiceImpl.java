package com.github.pampas.ui.service.base;

import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.storage.entity.*;
import com.github.pampas.storage.mapper.GatewayInstanceMapper;
import com.github.pampas.storage.mapper.GatewayRouteRuleRelMapper;
import com.github.pampas.storage.mapper.RouteRuleMapper;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.mapper.RouteRuleCustomMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-28
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class RouteRuleServiceImpl implements RouteRuleService {
    private static final Logger log = LoggerFactory.getLogger(RouteRuleServiceImpl.class);

    @Autowired
    private RouteRuleMapper routeRuleMapper;

    @Autowired
    private RouteRuleCustomMapper routeRuleCustomMapper;

    @Autowired
    private GatewayRouteRuleRelMapper gatewayRouteRuleRelMapper;

    @Autowired
    private GatewayInstanceMapper gatewayInstanceMapper;

    @Override
    public RouteRule getRouteRule(Integer ruleId) {
        AssertTools.notNull(ruleId, "id不能为空");
        RouteRule routeRule = routeRuleMapper.selectByPrimaryKey(ruleId);
        log.info("查询规则:{}", routeRule.getId());
        return routeRule;
    }

    @Override
    public List<RouteRule> getRouteRuleList(List<Integer> ruleIdList) {
        RouteRuleCondition condition = new RouteRuleCondition();
        RouteRuleCondition.Criteria criteria = condition.createCriteria();
        criteria.andIdIn(ruleIdList);
        List<RouteRule> routeRules = routeRuleMapper.selectByExample(condition);
        routeRules.stream().forEach(v -> v.setContent(null));
        log.info("查询规则列表:{}", routeRules);
        return routeRules;
    }

    @Override
    public Result<RouteRule> getRouteRuleList(String name, Boolean status,
                                              Integer pageNum, Integer pageSize) {
        RouteRuleCondition condition = new RouteRuleCondition();
        RouteRuleCondition.Criteria criteria = condition.createCriteria();
        if (StringUtils.isNotEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (status != null) {
            criteria.andStatusEqualTo(status);
        }
        condition.setPageInfo(pageNum, pageSize);
        condition.orderBy("id desc");
        long count = routeRuleMapper.countByExample(condition);
        List<RouteRule> routeRuleList = routeRuleMapper.selectByExample(condition);
        return Result.buildResult(routeRuleList, count);
    }

    @Override
    public List<GatewayInstance> getRouteRuleRelGateway(Integer ruleId) {
        //确认rule存在
        RouteRule routeRule = this.getRouteRule(ruleId);
        AssertTools.notNull(routeRule, "不存在的路由规则" + ruleId);
        AssertTools.isTrue(routeRule.getStatus(), "状态");
        GatewayRouteRuleRelCondition relCondition = new GatewayRouteRuleRelCondition();
        relCondition.createCriteria().andRouteRuleIdEqualTo(ruleId);
        List<GatewayRouteRuleRel> gatewayRouteRuleRelList = gatewayRouteRuleRelMapper.selectByExample(relCondition);

        List<String> gatewayInstanceIdList = gatewayRouteRuleRelList.stream().map(GatewayRouteRuleRel::getGatewayInstanceId).distinct().collect(Collectors.toList());
        log.info("查询{}关联的网关ID:{}", ruleId, ArrayUtils.toString(gatewayInstanceIdList));
        GatewayInstanceCondition condition = new GatewayInstanceCondition();
        condition.createCriteria().andInstanceIdIn(gatewayInstanceIdList);
        List<GatewayInstance> gatewayInstanceList = gatewayInstanceMapper.selectByExample(condition);
        return gatewayInstanceList;
    }

    @Override
    public RouteRule save(RouteRule routeRule) {
        if (routeRule.getId() != null) {
            int i = routeRuleMapper.updateByPrimaryKeySelective(routeRule);
            AssertTools.isTrue(i == 1, "更新失败");
            log.info("更新路由规则完成:{}", routeRule);
        } else {
            int i = routeRuleMapper.insertSelective(routeRule);
            AssertTools.isTrue(i == 1, "插入失败");
            log.info("新增路由规则完成:{}", routeRule);
        }
        return this.getRouteRule(routeRule.getId());
    }

    @Override
    public void delete(Integer ruleId) {
        AssertTools.notNull(ruleId, "id不能为空");
        int i = routeRuleMapper.deleteByPrimaryKey(ruleId);
        AssertTools.isTrue(i == 1, "删除失败");
        log.info("删除路由规则成功:{}", ruleId);
    }
}

