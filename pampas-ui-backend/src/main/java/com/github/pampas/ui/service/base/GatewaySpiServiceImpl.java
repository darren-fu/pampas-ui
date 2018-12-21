package com.github.pampas.ui.service.base;

import com.github.pampas.storage.entity.GatewayConfig;
import com.github.pampas.storage.entity.GatewayConfigCondition;
import com.github.pampas.storage.entity.GatewaySpi;
import com.github.pampas.storage.entity.GatewaySpiCondition;
import com.github.pampas.storage.mapper.GatewayConfigMapper;
import com.github.pampas.storage.mapper.GatewaySpiMapper;
import com.github.pampas.ui.mapper.GatewaySpiCustomMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private GatewayConfigMapper gatewayConfigMapper;

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

    @Override
    public List<GatewaySpi> getSpiList(String gatewayGroup, String gatewayInstanceId, String spiInterface) {
        GatewaySpiCondition condition = new GatewaySpiCondition();
        GatewaySpiCondition.Criteria criteria = condition.createCriteria();
        if (StringUtils.isNotEmpty(gatewayGroup)) {
            criteria.andGatewayGroupEqualTo(gatewayGroup);
        }
        if (StringUtils.isNotEmpty(gatewayInstanceId)) {
            criteria.andGatewayInstanceIdEqualTo(gatewayInstanceId);
        }
        if (StringUtils.isNotEmpty(spiInterface)) {
            criteria.andSpiInterfaceEqualTo(spiInterface);
        }

        condition.orderBy("`spi_interface` asc, `status` asc, `order` asc");

        List<GatewaySpi> gatewaySpiList = gatewaySpiMapper.selectByExample(condition);
        log.info("获取网关SPI列表:{}", gatewaySpiList);
        return gatewaySpiList;
    }

    @Override
    public List<GatewayConfig> getGatewayConfigList(String gatewayGroup, String gatewayInstanceId, String configSpiClass) {
        GatewayConfigCondition configCondition = new GatewayConfigCondition();
        if (StringUtils.isNoneEmpty(gatewayGroup, gatewayInstanceId)) {
            GatewayConfigCondition.Criteria criteria = configCondition.createCriteria();
            criteria.andGatewayGroupEqualTo(gatewayGroup);
            criteria.andGatewayInstanceIdEqualTo("");
            if (StringUtils.isNotEmpty(configSpiClass)) {
                criteria.andConfigSpiClassEqualTo(configSpiClass);
            }
            GatewayConfigCondition.Criteria orCriteria = configCondition.createCriteria().andGatewayGroupEqualTo(gatewayGroup)
                    .andGatewayInstanceIdEqualTo(gatewayInstanceId);
            if (StringUtils.isNotEmpty(configSpiClass)) {
                orCriteria.andConfigSpiClassEqualTo(configSpiClass);
            }
            configCondition.or(orCriteria);
        } else {
            GatewayConfigCondition.Criteria criteria = configCondition.createCriteria();
            configCondition.or();
            if (StringUtils.isNotEmpty(gatewayGroup)) {
                criteria.andGatewayGroupEqualTo(gatewayGroup);
            }
            if (StringUtils.isNotEmpty(gatewayInstanceId)) {
                criteria.andGatewayInstanceIdEqualTo(gatewayInstanceId);
            }
            if (StringUtils.isNotEmpty(configSpiClass)) {
                criteria.andConfigSpiClassEqualTo(configSpiClass);
            }
        }

        configCondition.orderBy("config_spi_interface, config_spi_class");
        List<GatewayConfig> gatewayConfigList = gatewayConfigMapper.selectByExample(configCondition);
        log.info("查询获取当前网关配置项{}条:{}", gatewayConfigList.size(), gatewayConfigList);
        return gatewayConfigList;
    }

    @Override
    @Transactional
    public void saveGatewayConfig(List<GatewayConfig> configList) {
        for (GatewayConfig gatewayConfig : configList) {
            if (gatewayConfig.getId() != null) {
                gatewayConfigMapper.updateByPrimaryKeySelective(gatewayConfig);
                log.info("保存<CONFIG>配置成功:{}", gatewayConfig);
            }
        }
    }

    @Override
    public void saveGatewaySpi(List<GatewaySpi> spiList) {
        for (GatewaySpi gatewaySpi : spiList) {
            if (gatewaySpi.getId() != null) {
                gatewaySpiMapper.updateByPrimaryKeySelective(gatewaySpi);
                log.info("保存<SPI>配置成功:{}", gatewaySpi);
            }
        }
    }
}
