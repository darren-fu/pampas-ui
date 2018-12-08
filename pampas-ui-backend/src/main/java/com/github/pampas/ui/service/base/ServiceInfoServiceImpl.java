package com.github.pampas.ui.service.base;

import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.common.tools.JsonTools;
import com.github.pampas.storage.entity.ServiceCondition;
import com.github.pampas.storage.entity.ServiceInstance;
import com.github.pampas.storage.entity.ServiceRegistry;
import com.github.pampas.storage.mapper.ServiceMapper;
import com.github.pampas.ui.base.BusinessException;
import com.github.pampas.ui.base.ServiceTypeEnum;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.utils.DiscoveryClientContainer;
import com.github.pampas.ui.vo.req.InstanceSaveReq;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-16
 */
@Service
public class ServiceInfoServiceImpl implements ServiceInfoService {

    private static final Logger log = LoggerFactory.getLogger(ServiceInfoServiceImpl.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    @Lazy
    private DiscoveryClientContainer discoveryClientContainer;

    @Autowired
    private ServiceRegistryService registryService;

    @Autowired
    private ServiceInstanceService serviceInstanceService;

    @Override
    public com.github.pampas.storage.entity.Service getService(Integer id) {
        AssertTools.notNull(id, "ID不能为空");
        return serviceMapper.selectByPrimaryKey(id);
    }

    @Override
    public Result<com.github.pampas.storage.entity.Service> getServiceList(String serviceName, String group,
                                                                           Integer pageNum, Integer pageSize) {
        ServiceCondition condition = new ServiceCondition();
        ServiceCondition.Criteria criteria = condition.createCriteria();

        if (StringUtils.isNotEmpty(serviceName)) {
            criteria.andServiceNameLikeInsensitive("%" + serviceName + "%");
        }
        if (StringUtils.isNotEmpty(group)) {
            criteria.andGroupEqualTo(group);
        }
        condition.setPageInfo(pageNum, pageSize);
        long total = serviceMapper.countByExample(condition);
        List<com.github.pampas.storage.entity.Service> serviceList = serviceMapper.selectByExample(condition);

        log.info("查询服务列表:{},{}", total, serviceList);
        return Result.buildResult(serviceList, (int) total);
    }

    @Override
    public com.github.pampas.storage.entity.Service saveService(com.github.pampas.storage.entity.Service service) {
        if (service.getId() != null) {
            int i = serviceMapper.updateByPrimaryKeySelective(service);
            Assert.isTrue(i == 1, "保存失败");
            log.info("保存服务成功");
        } else {
            int i = serviceMapper.insertSelective(service);
            Assert.isTrue(i == 1, "保存失败");
            log.info("新增服务成功");
        }

        return service;
    }

    @Override
    public void deleteService(Integer id) {
        AssertTools.notNull(this.getService(id), "不存在的服务:" + id);

        int i = serviceMapper.deleteByPrimaryKey(id);
        AssertTools.isTrue(i == 1, "删除失败");
        log.info("删除服务成功");
    }

    @Override
    public List<ServiceInstance> getListInRegistry(ServiceTypeEnum type, String service, Integer registryId) {
        AssertTools.notNull(type, "类型不能为空");
        AssertTools.notNull(registryId, "注册中心不能为空");
        AssertTools.notEmpty(service, "服务名不能为空");

        if (type != ServiceTypeEnum.RESTful) {
            throw new BusinessException("目前只支持RESTful服务获取实例列表");
        }
        // Spring Cloud服务   ConsulClient
        ServiceRegistry serviceRegistry = registryService.getServiceRegistry(registryId);
        AssertTools.notNull(serviceRegistry, "注册中心不存在");
        DiscoveryClient discoveryClient = discoveryClientContainer.getDiscoveryClient(serviceRegistry.getId());
        AssertTools.notNull(discoveryClient, "不支持此注册中心查询列表:" + serviceRegistry.getAddress());
        List<String> services = discoveryClient.getServices();
        if (!services.contains(service)) {
            return Collections.EMPTY_LIST;
        }
        List<com.github.pampas.storage.entity.ServiceInstance> instanceList = new ArrayList<>();
        List<org.springframework.cloud.client.ServiceInstance> scInstanceList = discoveryClient.getInstances(service);
        for (org.springframework.cloud.client.ServiceInstance scInstance : scInstanceList) {
            com.github.pampas.storage.entity.ServiceInstance instance = new com.github.pampas.storage.entity.ServiceInstance();
            instance.setInstanceId(scInstance.getServiceId());
            instance.setHost(scInstance.getUri().getHost());
            instance.setPort(scInstance.getUri().getPort());
            instance.setProtocol(scInstance.getUri().getScheme());
            instance.setStatus(1);
            instance.setServiceName(service);
            List<InstanceSaveReq.KeyAndVal> keyAndValList = InstanceSaveReq.KeyAndVal.convertMapToKeyAndVal(scInstance.getMetadata());
            instance.setProps(JsonTools.nonNullMapper().toJson(keyAndValList));
            instanceList.add(instance);
        }

        return instanceList;
    }

    @Override
    @Transactional
    public void updateInstanceInService(Integer serviceId, List<ServiceInstance> instanceList, boolean flushBeforeUpdate) {
        com.github.pampas.storage.entity.Service service = this.getService(serviceId);
        AssertTools.notNull(service, "不存在此服务");
        for (ServiceInstance instance : instanceList) {
            instance.setServiceId(service.getId());
            instance.setServiceName(service.getServiceName());
            instance.setProtocol(service.getProtocol());
        }
        //查找当前已经存在的
        List<ServiceInstance> existInstanceList = serviceInstanceService.getServiceInstanceList(serviceId);
        List<Integer> existIdList = existInstanceList.stream().map(ServiceInstance::getId).collect(Collectors.toList());

        for (ServiceInstance serviceInstance : instanceList) {
            ServiceInstance save = serviceInstanceService.save(serviceInstance);
            existIdList.remove(save.getId());
        }
        if (flushBeforeUpdate) {
            for (Integer existId : existIdList) {
                //删除多余的
                serviceInstanceService.delete(existId);
            }
        }
        log.info("更新服务[{}]下的实例完成:删除:{}个,详情:{},保存:{}个,详情：{}", service.getServiceName(), existIdList.size(), existIdList, instanceList.size(), instanceList);

    }
}
