package com.github.pampas.ui.service;

import com.github.pampas.ui.base.IngressService;
import com.github.pampas.ui.base.ServiceTypeEnum;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.vo.req.ServiceListReq;
import com.github.pampas.ui.vo.req.ServiceRegistrySaveReq;
import com.github.pampas.ui.vo.req.ServiceSaveReq;
import com.github.pampas.ui.vo.resp.InstanceResp;
import com.github.pampas.ui.vo.resp.ServiceRegistryResp;
import com.github.pampas.ui.vo.resp.ServiceResp;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-16
 */
public interface PampasServiceInfoService extends IngressService {


    /**
     * 根据ID查询服务详情
     *
     * @param id the id
     * @return the service
     */
    Response<ServiceResp> getService(Integer id);


    /**
     * 查询服务列表
     *
     * @param req      the req
     * @param pageNum  the page num
     * @param pageSize the page size
     * @return service list
     */
    Response<Result<ServiceResp>> getServiceList(ServiceListReq req, Integer pageNum, Integer pageSize);

    /**
     * 保存服务信息
     *
     * @param req the req
     * @return response response
     */
    Response<ServiceResp> saveService(ServiceSaveReq req);

    /**
     * Delete service response.
     *
     * @param id the id
     * @return the response
     */
    Response deleteService(Integer id);


    //////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 根据ID查询注册中心
     *
     * @param id the id
     * @return the service
     */
    Response<ServiceRegistryResp> getRegistry(Integer id);


    /**
     * 查询服务列表
     *
     * @param pageNum  the page num
     * @param pageSize the page size
     * @return service list
     */
    Response<Result<ServiceRegistryResp>> getRegistryList(Integer pageNum, Integer pageSize);

    /**
     * 保存服务信息
     *
     * @param req the req
     * @return response response
     */
    Response<ServiceRegistryResp> saveRegistry(ServiceRegistrySaveReq req);

    /**
     * Delete registry response.
     *
     * @param id the id
     * @return the response
     */
    Response deleteRegistry(Integer id);


    /**
     * 获取该服务在注册中心的实例
     *
     * @param type       the type
     * @param service    the service
     * @param registryId the registry id
     * @return list in registry
     */
    Response<Result<InstanceResp>> getListInRegistry(ServiceTypeEnum type, String service, Integer registryId);

    /**
     * 使用注册中心的实例信息更新服务实例列表
     *
     * @param serviceId         the service id
     * @param flushBeforeUpdate the flush before update
     * @return response
     */
    Response updateServiceInstanceFromRegistry(Integer serviceId, Boolean flushBeforeUpdate);

}
