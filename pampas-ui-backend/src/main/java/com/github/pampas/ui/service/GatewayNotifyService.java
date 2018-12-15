package com.github.pampas.ui.service;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-15
 */
public interface GatewayNotifyService {

    String notifyConfigLoaderWithKey(String group, String gatewayInstanceId, String configLoaderKey);

    String notifyConfigLoaderWithName(String group, String gatewayInstanceId, String configLoaderName);

}
