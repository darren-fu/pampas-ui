package com.github.pampas.ui.service.base;

import com.github.pampas.storage.entity.GatewayConfig;
import com.github.pampas.storage.entity.GatewaySpi;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-14
 */
public interface GatewaySpiService {

    GatewaySpi getSpiByName(String spiName);

    List<GatewaySpi> listBySpiInterface(Class clz, String group, String gatewayInstanceId);


    List<GatewaySpi> getSpiList(String gatewayGroup, String gatewayInstanceId);

    List<GatewayConfig> getGatewayConfigList(String gatewayGroup, String gatewayInstanceId, String configSpiClass);


    void saveGatewayConfig(List<GatewayConfig> configList);
}
