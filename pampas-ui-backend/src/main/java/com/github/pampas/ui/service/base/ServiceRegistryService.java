package com.github.pampas.ui.service.base;

import com.github.pampas.storage.entity.ServiceRegistry;
import com.github.pampas.ui.base.vo.Result;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-16
 */
public interface ServiceRegistryService {

    /**
     * 根据service id获取服务详情
     *
     * @param id the id
     * @return the service
     */
    ServiceRegistry getServiceRegistry(Integer id);


    List<ServiceRegistry> getServiceRegistry(List<Integer> idList);


    /**
     * 查询服务列表
     *
     * @param pageNum  the page num
     * @param pageSize the page size
     * @return service list
     */
    Result<ServiceRegistry> getServiceRegistryList(Integer pageNum, Integer pageSize);


    /**
     * 保存服务信息（新增、修改）
     *
     * @param serviceRegistry the service registry
     * @return service service registry
     */
    ServiceRegistry saveServiceRegistry(ServiceRegistry serviceRegistry);


    /**
     * Delete.
     *
     * @param id the id
     */
    void delete(Integer id);
}
