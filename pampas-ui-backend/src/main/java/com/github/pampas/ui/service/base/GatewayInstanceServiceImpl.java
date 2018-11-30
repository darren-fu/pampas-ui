package com.github.pampas.ui.service.base;

import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.storage.entity.GatewayInstance;
import com.github.pampas.storage.entity.GatewayInstanceCondition;
import com.github.pampas.storage.entity.GatewayRouteRuleRel;
import com.github.pampas.storage.entity.GatewayRouteRuleRelCondition;
import com.github.pampas.storage.mapper.GatewayInstanceMapper;
import com.github.pampas.storage.mapper.GatewayRouteRuleRelMapper;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.vo.req.GatewayInstanceListReq;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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


    @Override
    public GatewayInstance getGateway(Integer gatewayId) {
        return gatewayInstanceMapper.selectByPrimaryKey(gatewayId);
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
        condition.setPageInfo(pageNum, pageSize);
        long count = gatewayInstanceMapper.countByExample(condition);
        List<GatewayInstance> gatewayInstances = gatewayInstanceMapper.selectByExample(condition);
        log.info("查询网关列表:{}", gatewayInstances);
        return Result.buildResult(gatewayInstances, count);
    }

    @Override
    public Result<GatewayRouteRuleRel> getRouteRuleRel(Integer gatewayId) {
        GatewayInstance gateway = this.getGateway(gatewayId);
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
}
