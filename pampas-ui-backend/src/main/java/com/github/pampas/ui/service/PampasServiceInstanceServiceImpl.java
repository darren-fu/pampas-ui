package com.github.pampas.ui.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.common.tools.JsonTools;
import com.github.pampas.storage.entity.ServiceInstance;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.base.ServiceInstanceService;
import com.github.pampas.ui.utils.BeanTools;
import com.github.pampas.ui.utils.InstanceTools;
import com.github.pampas.ui.vo.req.InstanceListReq;
import com.github.pampas.ui.vo.req.InstanceSaveReq;
import com.github.pampas.ui.vo.resp.InstanceResp;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    private static InnerMethodHolder inner = new InnerMethodHolder();

    @Override
    public Response<InstanceResp> getInstance(Integer instanceId) {
        ServiceInstance serviceInstance = instanceService.getServiceInstance(instanceId);
        InstanceResp instanceResp = BeanTools.copyBean(serviceInstance, InstanceResp.class);
        inner.convertProJsonToKeyAndVal(serviceInstance, instanceResp);
        return Response.buildSuccessResponseWithInfo(instanceResp);
    }

    @Override
    public Response<Result<InstanceResp>> getInstanceInService(Integer serviceId) {
        List<ServiceInstance> serviceInstanceList = instanceService.getServiceInstanceList(serviceId);
        List<InstanceResp> respList = BeanTools.copyBeans(serviceInstanceList, InstanceResp.class);
        inner.convertProJsonToKeyAndVal(serviceInstanceList, respList);

        return Response.buildSuccessResponseWithResult(respList, respList.size());
    }

    @Override
    public Response<Result<InstanceResp>> getInstanceList(InstanceListReq req, Integer pageNum, Integer pageSize) {
        ServiceInstance serviceInstance = BeanTools.copyBean(req, ServiceInstance.class);
        Result<ServiceInstance> instanceResult = instanceService.getServiceInstanceList(serviceInstance, pageNum, pageSize);
        List<InstanceResp> instanceRespList = BeanTools.copyBeans(instanceResult.getData(), InstanceResp.class);
        inner.convertProJsonToKeyAndVal(instanceResult.getData(), instanceRespList);

        return Response.buildSuccessResponseWithResult(instanceRespList, instanceResult.getCount());
    }


    @Override
    public Response<InstanceResp> saveInstance(InstanceSaveReq req) {
        ServiceInstance serviceInstance = BeanTools.copyBean(req, ServiceInstance.class);
        if (serviceInstance.getId() == null) {
            AssertTools.notEmpty(serviceInstance.getServiceName(), "服务名称不能为空");
            serviceInstance.setInstanceId(InstanceTools.genInstanceId(serviceInstance));
        }
        //转换成Json 存储
        if (CollectionUtils.isNotEmpty(req.getPropList())) {
            serviceInstance.setProps(JsonTools.nonNullMapper().toJson(req.getPropList()));
        }
        ServiceInstance instance = instanceService.save(serviceInstance);
        InstanceResp instanceResp = BeanTools.copyBean(instance, InstanceResp.class);
        inner.convertProJsonToKeyAndVal(instance, instanceResp);

        return Response.buildSuccessResponseWithInfo(instanceResp);
    }

    /**
     * 私有方法持有类
     */
    private static class InnerMethodHolder {

        /**
         * 转换 prop为keyAndVal
         *
         * @param instances
         * @param instanceResps
         */
        private void convertProJsonToKeyAndVal(List<ServiceInstance> instances, List<InstanceResp> instanceResps) {
            for (ServiceInstance instance : instances) {
                Optional<InstanceResp> same = instanceResps.stream().filter(v -> v.getId().equals(instance.getId())).findFirst();
                if (same.isPresent()) {
                    convertProJsonToKeyAndVal(instance, same.get());
                }
            }
        }

        private void convertProJsonToKeyAndVal(ServiceInstance instance, InstanceResp instanceResp) {
            if (StringUtils.isNotEmpty(instance.getProps())) {
                try {
                    List<InstanceSaveReq.KeyAndVal> keyAndValList = JsonTools.nonNullMapper().fromJson(instance.getProps(), new TypeReference<List<InstanceSaveReq.KeyAndVal>>() {
                    });
                    instanceResp.setPropList(keyAndValList);

                } catch (Exception ex) {
                    log.error("转换实例属性失败{}:{}", instance.getProps(), ex.getMessage(), ex);
                }
            }
        }

    }
}
