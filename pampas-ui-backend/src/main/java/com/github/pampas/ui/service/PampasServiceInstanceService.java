package com.github.pampas.ui.service;

import com.github.pampas.ui.base.IngressService;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.vo.req.InstanceListReq;
import com.github.pampas.ui.vo.req.InstanceSaveReq;
import com.github.pampas.ui.vo.resp.InstanceResp;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-20
 */
public interface PampasServiceInstanceService extends IngressService {

    /**
     * 查询实例详细信息
     *
     * @param instanceId the instance id
     * @return the instance
     */
    Response<InstanceResp> getInstance(Integer instanceId);


    /**
     * 查询service下的instance列表
     *
     * @param serviceId the service id
     * @return instance in service
     */
    Response<Result<InstanceResp>> getInstanceInService(Integer serviceId);


    /**
     * 查询服务列表
     *
     * @param req      the req
     * @param pageNum  the page num
     * @param pageSize the page size
     * @return service list
     */
    Response<Result<InstanceResp>> getInstanceList(InstanceListReq req, Integer pageNum, Integer pageSize);

    /**
     * 保存服务信息
     *
     * @param req the req
     * @return response response
     */
    Response<InstanceResp> saveInstance(InstanceSaveReq req);

}
