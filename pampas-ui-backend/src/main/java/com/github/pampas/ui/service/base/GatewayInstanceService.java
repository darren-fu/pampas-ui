package com.github.pampas.ui.service.base;

import com.github.pampas.storage.entity.GatewayInstance;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-15
 */
public interface GatewayInstanceService {
    /**
     * 保存新增、修改后的网关实例信息.
     *
     * @param gatewayInstance the gateway instance
     */
    void save(GatewayInstance gatewayInstance);
}
