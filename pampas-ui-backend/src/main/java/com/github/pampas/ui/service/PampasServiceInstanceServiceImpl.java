package com.github.pampas.ui.service;

import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.common.tools.JsonTools;
import com.github.pampas.storage.entity.ServiceInstance;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.base.ServiceInstanceService;
import com.github.pampas.ui.utils.BeanTools;
import com.github.pampas.ui.utils.InstancePropsTools;
import com.github.pampas.ui.utils.InstanceTools;
import com.github.pampas.ui.vo.req.InstanceListReq;
import com.github.pampas.ui.vo.req.InstanceSaveReq;
import com.github.pampas.ui.vo.resp.InstanceResp;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(PampasServiceInstanceServiceImpl.class);

    @Autowired
    private ServiceInstanceService instanceService;


    @Override
    public Response<InstanceResp> getInstance(Integer instanceId) {
        ServiceInstance serviceInstance = instanceService.getServiceInstance(instanceId);
        InstanceResp instanceResp = BeanTools.copyBean(serviceInstance, InstanceResp.class);
        InstancePropsTools.convertProJsonToKeyAndVal(serviceInstance, instanceResp);
        return Response.buildSuccessResponseWithInfo(instanceResp);
    }

    @Override
    public Response<Result<InstanceResp>> getInstanceInService(Integer serviceId) {
        List<ServiceInstance> serviceInstanceList = instanceService.getServiceInstanceList(serviceId);
        List<InstanceResp> respList = BeanTools.copyBeans(serviceInstanceList, InstanceResp.class);
        InstancePropsTools.convertProJsonToKeyAndVal(serviceInstanceList, respList);

        return Response.buildSuccessResponseWithResult(respList, respList.size());
    }

    @Override
    public Response<Result<InstanceResp>> getInstanceList(InstanceListReq req, Integer pageNum, Integer pageSize) {
        ServiceInstance serviceInstance = BeanTools.copyBean(req, ServiceInstance.class);
        Result<ServiceInstance> instanceResult = instanceService.getServiceInstanceList(serviceInstance, pageNum, pageSize);
        List<InstanceResp> instanceRespList = BeanTools.copyBeans(instanceResult.getData(), InstanceResp.class);
        InstancePropsTools.convertProJsonToKeyAndVal(instanceResult.getData(), instanceRespList);

        return Response.buildSuccessResponseWithResult(instanceRespList, instanceResult.getCount());
    }


    @Override
    public Response<InstanceResp> saveInstance(InstanceSaveReq req) {
        ServiceInstance serviceInstance = BeanTools.copyBean(req, ServiceInstance.class);
        if (serviceInstance.getId() == null) {
            AssertTools.notEmpty(serviceInstance.getServiceName(), "服务名称不能为空");
        }
        //转换成Json 存储
        if (CollectionUtils.isNotEmpty(req.getPropList())) {
            serviceInstance.setProps(JsonTools.NON_NULL.toJson(req.getPropList()));
        }
        ServiceInstance instance = instanceService.save(serviceInstance);
        InstanceResp instanceResp = BeanTools.copyBean(instance, InstanceResp.class);
        InstancePropsTools.convertProJsonToKeyAndVal(instance, instanceResp);

        return Response.buildSuccessResponseWithInfo(instanceResp);
    }

}
