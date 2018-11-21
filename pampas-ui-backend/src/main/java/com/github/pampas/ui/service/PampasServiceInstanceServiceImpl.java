package com.github.pampas.ui.service;

import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.storage.entity.ServiceInstance;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.base.ServiceInstanceService;
import com.github.pampas.ui.utils.BeanTools;
import com.github.pampas.ui.utils.InstanceTools;
import com.github.pampas.ui.vo.req.InstanceListReq;
import com.github.pampas.ui.vo.req.InstanceSaveReq;
import com.github.pampas.ui.vo.resp.InstanceResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-20
 */
@Service
public class PampasServiceInstanceServiceImpl implements PampasServiceInstanceService {

    @Autowired
    private ServiceInstanceService instanceService;

    @Override
    public Response<InstanceResp> getInstance(Integer instanceId) {
        ServiceInstance serviceInstance = instanceService.getServiceInstance(instanceId);
        InstanceResp instanceResp = BeanTools.copyBean(serviceInstance, InstanceResp.class);
        return Response.buildSuccessResponseWithInfo(instanceResp);
    }

    @Override
    public Response<Result<InstanceResp>> getInstanceInService(Integer serviceId) {
        List<ServiceInstance> serviceInstanceList = instanceService.getServiceInstanceList(serviceId);
        List<InstanceResp> respList = BeanTools.copyBeans(serviceInstanceList, InstanceResp.class);
        return Response.buildSuccessResponseWithResult(respList, respList.size());
    }

    @Override
    public Response<Result<InstanceResp>> getInstanceList(InstanceListReq req, Integer pageNum, Integer pageSize) {
        ServiceInstance serviceInstance = BeanTools.copyBean(req, ServiceInstance.class);
        Result<ServiceInstance> instanceResult = instanceService.getServiceInstanceList(serviceInstance, pageNum, pageSize);
        List<InstanceResp> instanceRespList = BeanTools.copyBeans(instanceResult.getData(), InstanceResp.class);
        return Response.buildSuccessResponseWithResult(instanceRespList, instanceResult.getCount());
    }


    @Override
    public Response<InstanceResp> saveInstance(InstanceSaveReq req) {
        ServiceInstance serviceInstance = BeanTools.copyBean(req, ServiceInstance.class);
        if (serviceInstance.getId() == null) {
            AssertTools.notEmpty(serviceInstance.getServiceName(), "服务名称不能为空");
            serviceInstance.setInstanceId(InstanceTools.genInstanceId(serviceInstance));
        }

        ServiceInstance instance = instanceService.save(serviceInstance);
        return Response.buildSuccessResponseWithInfo(BeanTools.copyBean(instance, InstanceResp.class));
    }
}
