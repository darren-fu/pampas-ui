package com.github.pampas.ui.service;

import com.github.pampas.storage.entity.GatewayInstance;
import com.github.pampas.ui.base.IngressService;
import com.github.pampas.ui.base.vo.Response;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-15
 */
public interface PampasNotifyService extends IngressService {

    Response notifyConfigLoaderWithKey(GatewayInstance gatewayInstance, String configLoaderKey);
    Response notifyConfigLoaderWithName(GatewayInstance gatewayInstance, String configLoaderName);
    String notifyConfigLoaderWithKey(String group, String gatewayInstanceId, String configLoaderKey);

    String notifyConfigLoaderWithName(String group, String gatewayInstanceId, String configLoaderName);


    Response notifyGatewayConfigUpdate(String group, String gatewayInstanceId, String configSpiClass);
    Response notifyGatewaySpiUpdate(String group, String gatewayInstanceId, String spiInterface);
}
