package com.github.pampas.ui.service.base;

import com.github.pampas.storage.entity.GatewayInstance;
import com.github.pampas.storage.entity.GatewayRouteRuleRel;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.vo.req.GatewayInstanceListReq;

import java.util.Map;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-15
 */
public interface GatewayInstanceService {


    GatewayInstance getGateway(Integer gatewayId);

    Result<GatewayInstance> getGatewayList(GatewayInstanceListReq req, Integer pageNum, Integer pageSize);

    Result<GatewayRouteRuleRel> getRouteRuleRel(Integer gatewayId);

    Map<String, Long> countRouteRuleRel(Integer... gatewayId);

    /**
     * 保存新增、修改后的网关实例信息.
     *
     * @param gatewayInstance the gateway instance
     */
    void save(GatewayInstance gatewayInstance);
}
