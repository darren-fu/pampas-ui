package com.github.pampas.ui.service.base;

import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.storage.entity.*;
import com.github.pampas.storage.mapper.GatewayInstanceMapper;
import com.github.pampas.storage.mapper.GatewayRouteRuleRelMapper;
import com.github.pampas.storage.mapper.RouteRuleMapper;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.mapper.RouteRuleCustomMapper;
import com.github.pampas.ui.utils.IteratorTools;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private GatewayInstanceService gatewayInstanceService;

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
        Long count = null;
        if (pageNum != null && pageSize != null) {
            condition.setPageInfo(pageNum, pageSize);
            count = routeRuleMapper.countByExample(condition);
        }
        condition.orderBy("id desc");
        List<RouteRule> routeRuleList = routeRuleMapper.selectByExample(condition);
        return Result.buildResult(routeRuleList, count == null ? routeRuleList.size() : count);
    }

    @Override
    public List<GatewayRouteRuleRel> getRouteRuleRelList(Integer ruleId) {
        //确认rule存在
        RouteRule routeRule = this.getRouteRule(ruleId);
        AssertTools.notNull(routeRule, "不存在的路由规则" + ruleId);
        AssertTools.isTrue(routeRule.getStatus(), "状态");
        GatewayRouteRuleRelCondition relCondition = new GatewayRouteRuleRelCondition();
        relCondition.createCriteria().andRouteRuleIdEqualTo(ruleId);
        List<GatewayRouteRuleRel> gatewayRouteRuleRelList = gatewayRouteRuleRelMapper.selectByExample(relCondition);
        log.info("查询网关和路由关联列表:{}", gatewayRouteRuleRelList);
        return gatewayRouteRuleRelList;
    }

    @Override
    public List<GatewayInstance> getRouteRuleRelGateway(Integer ruleId) {
        List<GatewayRouteRuleRel> routeRuleRelList = this.getRouteRuleRelList(ruleId);
        List<String> gatewayInstanceIdList = routeRuleRelList.stream().map(GatewayRouteRuleRel::getGatewayInstanceId).distinct().collect(Collectors.toList());
        log.info("查询{}关联的网关ID:{}", ruleId, ArrayUtils.toString(gatewayInstanceIdList));

        if (CollectionUtils.isEmpty(gatewayInstanceIdList)) {
            return Collections.EMPTY_LIST;
        }
        GatewayInstanceCondition condition = new GatewayInstanceCondition();
        condition.createCriteria().andInstanceIdIn(gatewayInstanceIdList);
        List<GatewayInstance> gatewayInstanceList = gatewayInstanceMapper.selectByExample(condition);
        return gatewayInstanceList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer ruleId) {
        AssertTools.notNull(ruleId, "id不能为空");
        int i = routeRuleMapper.deleteByPrimaryKey(ruleId);
        AssertTools.isTrue(i == 1, "删除失败");
        log.info("删除路由规则成功:{}", ruleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRel(Integer ruleId, List<Integer> gatewayIdList) {
        AssertTools.notNull(ruleId, "路由规则不能为空");
        RouteRule routeRule = this.getRouteRule(ruleId);
        AssertTools.notNull(routeRule, "不存在的路由规则");
//        AssertTools.notEmpty(gatewayIdList, "网关不能为空");
        List<GatewayInstance> relGatewayList = this.getRouteRuleRelGateway(ruleId);
        List<String> instanceIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(gatewayIdList)) {
            List<GatewayInstance> gatewayInstanceList = gatewayInstanceService.getGateway(gatewayIdList.toArray(new Integer[0]));
            Map<Integer, String> gatewayIdMap = IteratorTools.toMap(gatewayInstanceList, GatewayInstance::getId, GatewayInstance::getInstanceId);
            for (Integer gid : gatewayIdList) {
                AssertTools.isTrue(gatewayIdMap.containsKey(gid), "不存在的网关:" + gid);
                instanceIdList.add(gatewayIdMap.get(gid));
            }
        }

        GatewayRouteRuleRelCondition relCondition = new GatewayRouteRuleRelCondition();
        GatewayRouteRuleRelCondition.Criteria criteria = relCondition.createCriteria();
        criteria.andRouteRuleIdEqualTo(ruleId);
        int num = gatewayRouteRuleRelMapper.deleteByExample(relCondition);
        log.info("删除网关和路由规则关系:{}",num);

        if (instanceIdList.size() > 0) {
            for (String instanceId : instanceIdList) {
                GatewayRouteRuleRel routeRuleRel = new GatewayRouteRuleRel();
                routeRuleRel.setRouteRuleId(ruleId);
                routeRuleRel.setGatewayInstanceId(instanceId);
                int i = gatewayRouteRuleRelMapper.insertSelective(routeRuleRel);
                AssertTools.isTrue(i == 1, "插入失败");
                log.info("新增网关和路由规则关系:{}",routeRuleRel);

            }
        }
        log.info("保存路由规则:{} 和网关关系成功:{}", ruleId, gatewayIdList);
    }
}

