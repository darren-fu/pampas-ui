package com.github.pampas.ui.service.base;

import com.github.pampas.storage.entity.Service;
import com.github.pampas.storage.entity.ServiceInstance;
import com.github.pampas.ui.base.ServiceTypeEnum;
import com.github.pampas.ui.base.vo.Result;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-16
 */
public interface ServiceInfoService {

    /**
     * 根据service id获取服务详情
     *
     * @param id the id
     * @return the service
     */
    Service getService(Integer id);


    /**
     * 查询服务列表
     *
     * @param serviceName
     * @param group
     * @param pageNum     the page num
     * @param pageSize    the page size
     * @return service list
     */
    Result<Service> getServiceList(String serviceName, String group,
                                   Integer pageNum, Integer pageSize);


    /**
     * 保存服务信息（新增、修改）
     *
     * @param service the service
     * @return service
     */
    Service saveService(Service service);

    void deleteService(Integer id);

    List<ServiceInstance> getListInRegistry(ServiceTypeEnum type, String service, Integer registryId);


    void updateInstanceInService(Integer serviceId, List<ServiceInstance> instanceList, boolean flushBeforeUpdate);
}
