package com.github.pampas.ui.service.base;

import com.github.pampas.storage.entity.GatewayConfig;
import com.github.pampas.storage.entity.GatewayInstance;
import com.github.pampas.storage.entity.GatewayRouteRuleRel;
import com.github.pampas.storage.entity.GatewaySpi;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.vo.req.GatewayInstanceListReq;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-15
 */
public interface GatewayInstanceService {


    /**
     * Gets gateway.
     *
     * @param gatewayId the gateway id
     * @return the gateway
     */
    List<GatewayInstance> getGateway(Integer... gatewayId);

    /**
     * Gets gateway list.
     *
     * @param req      the req
     * @param pageNum  the page num
     * @param pageSize the page size
     * @return the gateway list
     */
    Result<GatewayInstance> getGatewayList(GatewayInstanceListReq req, Integer pageNum, Integer pageSize);

    /**
     * 获取网关管理的路由规则列表
     *
     * @param gatewayId the gateway id
     * @return the route rule rel
     */
    Result<GatewayRouteRuleRel> getRouteRuleRel(Integer gatewayId);

    /**
     * 统计网关管理路由规则数量
     *
     * @param gatewayId the gateway id
     * @return the map
     */
    Map<String, Long> countRouteRuleRel(Integer... gatewayId);

    /**
     * 保存新增、修改后的网关实例信息.
     *
     * @param gatewayInstance the gateway instance
     */
    void save(GatewayInstance gatewayInstance);

    /**
     * 路由规则关联网关
     *
     * @param gatewayIdList the gateway id list
     * @param ruleIdList    the rule id list
     */
    void saveRel(List<Integer> gatewayIdList, List<Integer> ruleIdList);


    List<GatewaySpi> getSpiList(String gatewayGroup, String gatewayInstanceId);

    List<GatewayConfig> getGatewayConfigList(String gatewayGroup, String gatewayInstanceId);
}
