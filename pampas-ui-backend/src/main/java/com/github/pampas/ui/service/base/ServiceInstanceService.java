package com.github.pampas.ui.service.base;

import com.github.pampas.storage.entity.ServiceInstance;
import com.github.pampas.ui.base.vo.Result;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-15
 */
public interface ServiceInstanceService {

    /**
     * 根据ID获取指定实例 详情
     *
     * @param instanceId the id
     * @return the service instance
     */
    ServiceInstance getServiceInstance(Integer instanceId);


    /**
     * 根据Service ID获取实例 列表
     *
     * @param serviceId the service id
     * @return the service instance list
     */
    List<ServiceInstance> getServiceInstanceList(Integer serviceId);


    /**
     * 查询服务实例 列表 分页
     *
     * @param instance the server instance
     * @param pageNum        the page num
     * @param pageSize       the page size
     * @return the service instance list
     */
    Result<ServiceInstance> getServiceInstanceList(ServiceInstance instance, Integer pageNum, Integer pageSize);


    /**
     * 保存服务实例信息.
     *
     * @param serviceInstance the service instance
     */
    ServiceInstance save(ServiceInstance serviceInstance);


    void delete(Integer id);

    Integer deleteByServiceId(Integer serviceId);

}
