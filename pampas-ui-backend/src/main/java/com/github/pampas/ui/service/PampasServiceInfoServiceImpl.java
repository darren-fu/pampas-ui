package com.github.pampas.ui.service;

import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.storage.entity.GatewaySpi;
import com.github.pampas.storage.entity.Service;
import com.github.pampas.storage.entity.ServiceInstance;
import com.github.pampas.storage.entity.ServiceRegistry;
import com.github.pampas.ui.base.ProtocolTypeEnum;
import com.github.pampas.ui.base.ServiceTypeEnum;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.base.GatewaySpiService;
import com.github.pampas.ui.service.base.ServiceInfoService;
import com.github.pampas.ui.service.base.ServiceInstanceService;
import com.github.pampas.ui.service.base.ServiceRegistryService;
import com.github.pampas.ui.utils.BeanTools;
import com.github.pampas.ui.utils.DiscoveryClientContainer;
import com.github.pampas.ui.utils.InstancePropsTools;
import com.github.pampas.ui.utils.IteratorTools;
import com.github.pampas.ui.vo.req.ServiceListReq;
import com.github.pampas.ui.vo.req.ServiceRegistrySaveReq;
import com.github.pampas.ui.vo.req.ServiceSaveReq;
import com.github.pampas.ui.vo.resp.InstanceResp;
import com.github.pampas.ui.vo.resp.ServiceRegistryResp;
import com.github.pampas.ui.vo.resp.ServiceResp;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-16
 */
@org.springframework.stereotype.Service
public class PampasServiceInfoServiceImpl implements PampasServiceInfoService {

    @Autowired
    private ServiceInfoService serviceInfoService;

    @Autowired
    private ServiceInstanceService instanceService;

    @Autowired
    private ServiceRegistryService serviceRegistryService;

    @Autowired
    private DiscoveryClientContainer discoveryClientContainer;

    @Autowired
    private GatewaySpiService gatewaySpiService;

    @Override
    public Response<ServiceResp> getService(Integer id) {
        Service service = serviceInfoService.getService(id);
        ServiceResp serviceResp = BeanTools.copyBean(service, ServiceResp.class);

        if (service != null && service.getRegistryId() != null) {
            ServiceRegistry serviceRegistry = serviceRegistryService.getServiceRegistry(service.getRegistryId());
            serviceResp.setRegistryName(serviceRegistry == null ? null : serviceRegistry.getName());
        }
        if (StringUtils.isNotEmpty(service.getLoadbalancer())) {
            GatewaySpi spiByName = gatewaySpiService.getSpiByName(service.getLoadbalancer());
            serviceResp.setLoadbalancerName(spiByName == null ? null : spiByName.getSpiDesc());
        }
        return Response.buildSuccessResponseWithInfo(serviceResp);
    }

    @Override
    public Response<Result<ServiceResp>> getServiceList(ServiceListReq req, Integer pageNum, Integer pageSize) {
        Result<Service> serviceList = serviceInfoService.getServiceList(req.getServiceName(), req.getGroup(), pageNum, pageSize);
        List<ServiceResp> serviceRespList = BeanTools.copyBeans(serviceList.getData(), ServiceResp.class);

        if (CollectionUtils.isNotEmpty(serviceRespList)) {
            List<Integer> registryIdList = serviceList.getData().stream().map(Service::getRegistryId).collect(Collectors.toList());
            List<ServiceRegistry> serviceRegistryList = serviceRegistryService.getServiceRegistry(registryIdList);
            Map<Integer, String> registryIdAndNameMap = IteratorTools.toMap(serviceRegistryList, ServiceRegistry::getId, ServiceRegistry::getName);
            serviceRespList.forEach(v -> v.setRegistryName(registryIdAndNameMap.get(v.getRegistryId())));
        }
        return Response.buildSuccessResponseWithResult(serviceRespList, serviceList.getCount());
    }

    @Override
    public Response<ServiceResp> saveService(ServiceSaveReq req) {
        Service service = BeanTools.copyBean(req, Service.class);
        service.setType(req.getType().getValue());
        service.setProtocol(ProtocolTypeEnum.HTTP.getValue());
        if (req.getType() == ServiceTypeEnum.RESTful) {
            service.setProtocol(ProtocolTypeEnum.HTTP.getValue());
        } else if (req.getType() == ServiceTypeEnum.DUBBO) {
            service.setProtocol(ProtocolTypeEnum.DUBBO.getValue());
        } else if (req.getType() == ServiceTypeEnum.GRPC) {
            service.setProtocol(ProtocolTypeEnum.GRPC.getValue());
        }

        if (service.getRegistryId() != null) {
            ServiceRegistry serviceRegistry = serviceRegistryService.getServiceRegistry(service.getRegistryId());
            AssertTools.notNull(serviceRegistry, "不存在的注册中心:" + service.getRegistryId());
        }
        Service newService = serviceInfoService.saveService(service);
        ServiceResp serviceResp = BeanTools.copyBean(newService, ServiceResp.class);
        return Response.buildSuccessResponseWithInfo(serviceResp);
    }

    @Override
    public Response deleteService(Integer id) {
        serviceInfoService.deleteService(id);
        //todo 删除前判断是否被使用
        return Response.buildSuccessEmptyResponse();
    }

    @Override
    public Response<ServiceRegistryResp> getRegistry(Integer id) {
        ServiceRegistry serviceRegistry = serviceRegistryService.getServiceRegistry(id);
        ServiceRegistryResp serviceRegistryResp = BeanTools.copyBean(serviceRegistry, ServiceRegistryResp.class);
        return Response.buildSuccessResponseWithInfo(serviceRegistryResp);
    }

    @Override
    public Response<Result<ServiceRegistryResp>> getRegistryList(Integer pageNum, Integer pageSize) {
        Result<ServiceRegistry> serviceRegistryList = serviceRegistryService.getServiceRegistryList(pageNum, pageSize);
        List<ServiceRegistryResp> serviceRegistryResps = BeanTools.copyBeans(serviceRegistryList.getData(), ServiceRegistryResp.class);
        return Response.buildSuccessResponseWithResult(serviceRegistryResps, serviceRegistryList.getCount());
    }

    @Override
    public Response<ServiceRegistryResp> saveRegistry(ServiceRegistrySaveReq req) {
        ServiceRegistry serviceRegistry = BeanTools.copyBean(req, ServiceRegistry.class);
        serviceRegistry.setType(req.getType().getValue());
        serviceRegistry.setPattern(req.getPattern().getValue());
        ServiceRegistry newServiceRegistry = serviceRegistryService.saveServiceRegistry(serviceRegistry);
        ServiceRegistryResp serviceRegistryResp = BeanTools.copyBean(newServiceRegistry, ServiceRegistryResp.class);
        return Response.buildSuccessResponseWithInfo(serviceRegistryResp);
    }

    @Override
    public Response deleteRegistry(Integer id) {
        serviceRegistryService.delete(id);
        //todo 删除前判断是否被使用
        return Response.buildSuccessEmptyResponse();
    }

    @Override
    public Response<Result<InstanceResp>> getListInRegistry(ServiceTypeEnum type, String service, Integer registryId) {
        List<com.github.pampas.storage.entity.ServiceInstance> listInRegistry = serviceInfoService.getListInRegistry(type, service, registryId);

        return Response.buildSuccessResponseWithResult(InstancePropsTools.convertInstanceToResp(listInRegistry), listInRegistry.size());
    }

    @Override
    public Response updateServiceInstanceFromRegistry(Integer serviceId, Boolean flushBeforeUpdate) {
        Service service = serviceInfoService.getService(serviceId);
        AssertTools.notNull(service, "服务不存在");
        List<ServiceInstance> listInRegistry = serviceInfoService.getListInRegistry(ServiceTypeEnum.getEnum(service.getType()),
                service.getServiceName(), service.getRegistryId());
        serviceInfoService.updateInstanceInService(serviceId, listInRegistry, ObjectUtils.defaultIfNull(flushBeforeUpdate, false));
        return null;
    }


}
