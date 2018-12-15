package com.github.pampas.ui.service.base;

import com.github.pampas.storage.entity.GatewaySpi;
import com.github.pampas.storage.entity.GatewaySpiCondition;
import com.github.pampas.storage.mapper.GatewaySpiMapper;
import com.github.pampas.ui.mapper.GatewaySpiCustomMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-14
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class GatewaySpiServiceImpl implements GatewaySpiService {

    private static final Logger log = LoggerFactory.getLogger(GatewaySpiServiceImpl.class);

    @Autowired
    private GatewaySpiMapper gatewaySpiMapper;

    @Autowired
    private GatewaySpiCustomMapper spiCustomMapper;

    @Override
    public GatewaySpi getSpiByName(String spiName) {
        GatewaySpiCondition condition = new GatewaySpiCondition();
        condition.createCriteria().andSpiNameEqualTo(spiName);
        condition.setPageInfo(1, 1);
        List<GatewaySpi> spiList = gatewaySpiMapper.selectByExample(condition);

        return spiList.size() > 0 ? spiList.get(0) : null;
    }

    @Override
    public List<GatewaySpi> listBySpiInterface(Class clz, String group, String gatewayInstanceId) {

        GatewaySpiCondition condition = new GatewaySpiCondition();
        GatewaySpiCondition.Criteria criteria = condition.createCriteria();
        if (StringUtils.isNotEmpty(group)) {
            criteria.andGatewayGroupEqualTo(group);
        }
        if (StringUtils.isNotEmpty(gatewayInstanceId)) {
            criteria.andGatewayInstanceIdEqualTo(gatewayInstanceId);
        }

        List<GatewaySpi> spiList = spiCustomMapper.selectSpiClassList(clz.getName(), group, gatewayInstanceId);
        log.info("获取SPI:{}列表:{}", clz.getSimpleName(), spiList);
        return spiList;
    }
}
