package com.github.pampas.ui.service.base;

import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.storage.entity.*;
import com.github.pampas.storage.mapper.GatewayConfigMapper;
import com.github.pampas.storage.mapper.GatewayInstanceMapper;
import com.github.pampas.storage.mapper.GatewayRouteRuleRelMapper;
import com.github.pampas.storage.mapper.GatewaySpiMapper;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.utils.IteratorTools;
import com.github.pampas.ui.vo.req.GatewayInstanceListReq;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-15
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class GatewayInstanceServiceImpl implements GatewayInstanceService {

    private static final Logger log = LoggerFactory.getLogger(GatewayInstanceServiceImpl.class);

    @Autowired
    private GatewayInstanceMapper gatewayInstanceMapper;

    @Autowired
    private GatewayRouteRuleRelMapper gatewayRouteRuleRelMapper;

    @Autowired
    private RouteRuleService routeRuleService;

    @Autowired
    private GatewaySpiMapper gatewaySpiMapper;

    @Autowired
    private GatewayConfigMapper gatewayConfigMapper;


    @Override
    public List<GatewayInstance> getGateway(Integer... gatewayId) {
        GatewayInstanceCondition condition = new GatewayInstanceCondition();
        GatewayInstanceCondition.Criteria criteria = condition.createCriteria();
        criteria.andIdIn(Arrays.asList(gatewayId));

        return gatewayInstanceMapper.selectByExample(condition);
    }

    @Override
    public Result<GatewayInstance> getGatewayList(GatewayInstanceListReq req, Integer pageNum, Integer pageSize) {
        GatewayInstanceCondition condition = new GatewayInstanceCondition();
        GatewayInstanceCondition.Criteria criteria = condition.createCriteria();

        if (StringUtils.isNotEmpty(req.getGroup())) {
            criteria.andGroupLike("%" + req.getGroup() + "%");
        }
        if (StringUtils.isNotEmpty(req.getName())) {
            criteria.andServerNameLike("%" + req.getName() + "%");
        }
        if (StringUtils.isNotEmpty(req.getInstanceId())) {
            criteria.andInstanceIdLike("%" + req.getInstanceId() + "%");
        }
        if (StringUtils.isNotEmpty(req.getHost())) {
            criteria.andHostLike("%" + req.getHost() + "%");
        }
        if (StringUtils.isNotEmpty(req.getHostName())) {
            criteria.andHostNameLike("%" + req.getHostName() + "%");
        }
        condition.orderBy("id desc");
        if (pageNum != null && pageSize != null) {
            condition.setPageInfo(pageNum, pageSize);
        }
        long count = gatewayInstanceMapper.countByExample(condition);
        List<GatewayInstance> gatewayInstances = gatewayInstanceMapper.selectByExample(condition);
        log.info("查询网关列表:{}", gatewayInstances);
        return Result.buildResult(gatewayInstances, count);
    }

    @Override
    public Result<GatewayRouteRuleRel> getRouteRuleRel(Integer gatewayId) {
        List<GatewayInstance> gatewayInstanceList = this.getGateway(gatewayId);

        if (CollectionUtils.isEmpty(gatewayInstanceList)) {
            return Result.buildResult(Collections.EMPTY_LIST, 0);
        }
        GatewayInstance gateway = gatewayInstanceList.get(0);
        if (gateway == null) {
            return Result.buildResult(Collections.EMPTY_LIST, 0);
        }
        GatewayRouteRuleRelCondition condition = new GatewayRouteRuleRelCondition();
        GatewayRouteRuleRelCondition.Criteria criteria = condition.createCriteria();
        criteria.andGatewayInstanceIdEqualTo(gateway.getInstanceId());

        List<GatewayRouteRuleRel> gatewayRouteRuleRels = gatewayRouteRuleRelMapper.selectByExample(condition);
        log.info("获取关联网关:{}的规则列表:{}", gatewayRouteRuleRels.stream().map(GatewayRouteRuleRel::getRouteRuleId).distinct().collect(Collectors.toList()));
        return Result.buildResult(gatewayRouteRuleRels, gatewayRouteRuleRels.size());
    }

    @Override
    public Map<String, Long> countRouteRuleRel(Integer... gatewayId) {


        GatewayInstanceCondition condition = new GatewayInstanceCondition();
        GatewayInstanceCondition.Criteria criteria = condition.createCriteria();
        criteria.andIdIn(Arrays.asList(gatewayId));

        List<GatewayInstance> gatewayInstances = gatewayInstanceMapper.selectByExample(condition);

        List<String> idList = gatewayInstances.stream().map(GatewayInstance::getInstanceId).collect(Collectors.toList());

        GatewayRouteRuleRelCondition relCondition = new GatewayRouteRuleRelCondition();
        GatewayRouteRuleRelCondition.Criteria relCriteria = relCondition.createCriteria();
        relCriteria.andGatewayInstanceIdIn(idList);
        List<GatewayRouteRuleRel> gatewayRouteRuleRels = gatewayRouteRuleRelMapper.selectByExample(relCondition);

        Map<String, Long> collect = gatewayRouteRuleRels.stream().collect(Collectors.groupingBy(GatewayRouteRuleRel::getGatewayInstanceId, Collectors.counting()));
        log.info("获取关联网关的规则统计:{}", collect);
        return collect;
    }

    @Override
    public void save(GatewayInstance gatewayInstance) {

        GatewayInstanceCondition condition = new GatewayInstanceCondition();
        condition.createCriteria().andInstanceIdEqualTo(gatewayInstance.getInstanceId());

        List<GatewayInstance> gatewayInstances = gatewayInstanceMapper.selectByExample(condition);

        if (CollectionUtils.isEmpty(gatewayInstances)) {
            gatewayInstanceMapper.insert(gatewayInstance);
            log.info("新增网关:{}", gatewayInstance);
        } else {
            AssertTools.isTrue(gatewayInstances.size() == 1, "存在重复的gateway:" + gatewayInstance.getInstanceId());

            gatewayInstance.setId(gatewayInstances.get(0).getId());
            gatewayInstanceMapper.updateByPrimaryKeySelective(gatewayInstance);
            log.info("更新网关:{}", gatewayInstance);
        }


    }

    @Override
    @Transactional
    public void saveRel(List<Integer> gatewayIdList, List<Integer> ruleIdList) {
        AssertTools.notNull(gatewayIdList, "网关不能为空");
        List<String> instanceIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(gatewayIdList)) {
            List<GatewayInstance> gatewayInstanceList = this.getGateway(gatewayIdList.toArray(new Integer[0]));
            Map<Integer, String> gatewayIdMap = IteratorTools.toMap(gatewayInstanceList, GatewayInstance::getId, GatewayInstance::getInstanceId);
            for (Integer gid : gatewayIdList) {
                AssertTools.isTrue(gatewayIdMap.containsKey(gid), "不存在的网关:" + gid);
                instanceIdList.add(gatewayIdMap.get(gid));
            }
        }

        if (CollectionUtils.isNotEmpty(ruleIdList)) {
            List<RouteRule> routeRuleList = routeRuleService.getRouteRuleList(ruleIdList);
            List<Integer> routeRuleIdList = routeRuleList.stream().map(RouteRule::getId).collect(Collectors.toList());
            for (Integer gid : ruleIdList) {
                AssertTools.isTrue(routeRuleIdList.contains(gid), "不存在的路由规则:" + gid);
            }
        }

        GatewayRouteRuleRelCondition relCondition = new GatewayRouteRuleRelCondition();
        GatewayRouteRuleRelCondition.Criteria criteria = relCondition.createCriteria();
        criteria.andGatewayInstanceIdIn(instanceIdList);
        int num = gatewayRouteRuleRelMapper.deleteByExample(relCondition);
        log.info("删除网关和路由规则关系:{}", num);

        if (ruleIdList.size() > 0) {
            for (String instanceId : instanceIdList) {
                for (Integer ruleId : ruleIdList) {
                    GatewayRouteRuleRel routeRuleRel = new GatewayRouteRuleRel();
                    routeRuleRel.setGatewayInstanceId(instanceId);
                    routeRuleRel.setRouteRuleId(ruleId);
                    int i = gatewayRouteRuleRelMapper.insertSelective(routeRuleRel);
                    AssertTools.isTrue(i == 1, "插入失败");
                    log.info("新增网关和路由规则关系:{}", routeRuleRel);
                }
            }

        }
        log.info("保存路由规则:{} 和网关关系成功:{}", ruleIdList, gatewayIdList);
    }

    @Override
    public List<GatewaySpi> getSpiList(String gatewayGroup, String gatewayInstanceId) {
        GatewaySpiCondition condition = new GatewaySpiCondition();
        GatewaySpiCondition.Criteria criteria = condition.createCriteria();
        if (StringUtils.isNotEmpty(gatewayGroup)) {
            criteria.andGatewayGroupEqualTo(gatewayGroup);
        }
        if (StringUtils.isNotEmpty(gatewayInstanceId)) {
            criteria.andGatewayInstanceIdEqualTo(gatewayInstanceId);
        }
        List<GatewaySpi> gatewaySpiList = gatewaySpiMapper.selectByExample(condition);
        log.info("获取网关SPI列表:{}", gatewaySpiList);
        return gatewaySpiList;
    }

    @Override
    public List<GatewayConfig> getGatewayConfigList(String gatewayGroup, String gatewayInstanceId) {
        GatewayConfigCondition configCondition = new GatewayConfigCondition();
        GatewayConfigCondition.Criteria criteria = configCondition.createCriteria();
        if (StringUtils.isNotEmpty(gatewayGroup)) {
            criteria.andGatewayGroupEqualTo(gatewayGroup);
        }
        if (StringUtils.isNotEmpty(gatewayInstanceId)) {
            criteria.andGatewayInstanceIdEqualTo(gatewayInstanceId);
        }
        configCondition.orderBy("config_spi_interface, config_spi_class");
        List<GatewayConfig> gatewayConfigList = gatewayConfigMapper.selectByExample(configCondition);
        log.info("查询获取当前网关配置项{}条:{}", gatewayConfigList.size(), gatewayConfigList);
        return gatewayConfigList;
    }
}
