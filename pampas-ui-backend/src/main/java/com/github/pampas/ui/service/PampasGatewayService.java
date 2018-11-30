package com.github.pampas.ui.service;

import com.github.pampas.storage.entity.GatewayInstance;
import com.github.pampas.ui.base.IngressService;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.vo.req.GatewayInstanceListReq;
import com.github.pampas.ui.vo.req.GatewayInstanceSaveReq;
import com.github.pampas.ui.vo.resp.GatewayInstanceResp;
import com.github.pampas.ui.vo.resp.RouteRuleResp;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-30
 */
public interface PampasGatewayService extends IngressService {


    /**
     * Gets gateway.
     *
     * @param gatewayId the gateway id
     * @return the gateway
     */
    Response<GatewayInstanceResp> getGateway(Integer gatewayId);

    /**
     * Gets gateway list.
     *
     * @param req the req
     * @return the gateway list
     */
    Response<Result<GatewayInstanceResp>> getGatewayList(GatewayInstanceListReq req,  Integer pageNum, Integer pageSize);

    /**
     * Gets route rule rel.
     *
     * @param gatewayId the gateway id
     * @return the route rule rel
     */
    Response<Result<RouteRuleResp>> getRouteRuleRel(Integer gatewayId);

    /**
     * 保存新增、修改后的网关实例信息.
     *
     * @param req the req
     * @return the gateway instance resp
     */
    GatewayInstanceResp save(GatewayInstanceSaveReq req);
}
