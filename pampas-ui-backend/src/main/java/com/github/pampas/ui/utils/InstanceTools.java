package com.github.pampas.ui.utils;

import com.github.pampas.storage.entity.ServiceInstance;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-20
 */
public class InstanceTools {

    /**
     * 生成serviceInstance ID.
     *
     * @param serviceInstance the service instance
     * @return the string
     */
    public static String genInstanceId(ServiceInstance serviceInstance) {
        return serviceInstance.getServiceName() + "#" + serviceInstance.getHost() + ":" + serviceInstance.getPort();
    }
}
