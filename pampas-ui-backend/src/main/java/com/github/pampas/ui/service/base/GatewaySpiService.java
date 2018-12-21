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

    /**
     * 根据spiName获取SPI插件
     *
     * @param spiName the spi name
     * @return the spi by name
     */
    GatewaySpi getSpiByName(String spiName);

    /**
     * 根据spi_interface的class查询对应的SPI
     *
     * @param clz               the clz
     * @param group             the group
     * @param gatewayInstanceId the gateway instance id
     * @return the list
     */
    List<GatewaySpi> listBySpiInterface(Class clz, String group, String gatewayInstanceId);


    /**
     * 获取网关的SPI
     *
     * @param gatewayGroup      the gateway group
     * @param gatewayInstanceId the gateway instance id
     * @param spiInterface      the spi interface
     * @return the spi list
     */
    List<GatewaySpi> getSpiList(String gatewayGroup, String gatewayInstanceId, String spiInterface);

    /**
     * 获取网关的config
     *
     * @param gatewayGroup      the gateway group
     * @param gatewayInstanceId the gateway instance id
     * @param configSpiClass    the config spi class
     * @return the gateway config list
     */
    List<GatewayConfig> getGatewayConfigList(String gatewayGroup, String gatewayInstanceId, String configSpiClass);


    /**
     * 保存网关config
     *
     * @param configList the config list
     */
    void saveGatewayConfig(List<GatewayConfig> configList);

    /**
     * 保存网关SPI
     *
     * @param spiList the spi list
     */
    void saveGatewaySpi(List<GatewaySpi> spiList);
}
