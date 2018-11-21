package com.github.pampas.ui.service.base;

import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.storage.entity.ServiceInstance;
import com.github.pampas.storage.entity.ServiceInstanceCondition;
import com.github.pampas.storage.mapper.ServiceInstanceMapper;
import com.github.pampas.ui.base.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-15
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class ServiceInstanceServiceImpl implements ServiceInstanceService {

    private static final Logger log = LoggerFactory.getLogger(ServiceInstanceServiceImpl.class);

    @Autowired
    private ServiceInstanceMapper serviceInstanceMapper;

    @Override
    public ServiceInstance getServiceInstance(Integer id) {
        ServiceInstance serviceInstance = serviceInstanceMapper.selectByPrimaryKey(id);
        return serviceInstance;
    }

    @Override
    public List<ServiceInstance> getServiceInstanceList(Integer serviceId) {
        AssertTools.notNull(serviceId, "service id不能为空");
        ServiceInstanceCondition condition = new ServiceInstanceCondition();
        condition.createCriteria().andServiceIdEqualTo(serviceId);
        List<ServiceInstance> serviceInstances = serviceInstanceMapper.selectByExample(condition);
        return serviceInstances;
    }

    @Override
    public Result<ServiceInstance> getServiceInstanceList(ServiceInstance instance, Integer pageNum, Integer pageSize) {
        ServiceInstanceCondition condition = new ServiceInstanceCondition();

        ServiceInstanceCondition.Criteria criteria = condition.createCriteria();

        if (StringUtils.isNotEmpty(instance.getHost())) {
            criteria.andHostLike("%" + instance.getHost() + "%");
        }

        if (StringUtils.isNotEmpty(instance.getHostName())) {
            criteria.andHostNameLike("%" + instance.getHost() + "%");
        }


        condition.setPageInfo(pageNum, pageSize);
        long count = serviceInstanceMapper.countByExample(condition);
        List<ServiceInstance> serviceInstances = serviceInstanceMapper.selectByExample(condition);
        return Result.buildResult(serviceInstances, (int) count);
    }


    @Override
    public ServiceInstance save(ServiceInstance serviceInstance) {
        AssertTools.notNull(serviceInstance, "serviceInstance不能为空");

        if (serviceInstance.getId() != null) {
            ServiceInstance existInstance = this.getServiceInstance(serviceInstance.getId());
            AssertTools.notNull(existInstance, "不存在此instance:" + serviceInstance.getId());
            int i = serviceInstanceMapper.updateByPrimaryKeySelective(serviceInstance);
            log.info("更新服务实例:{}", serviceInstance);
        } else {
            AssertTools.notEmpty(serviceInstance.getInstanceId(), "instance ID不能为空");
            ServiceInstanceCondition condition = new ServiceInstanceCondition();
            condition.createCriteria()
                    .andServiceNameEqualTo(serviceInstance.getServiceName())
                    .andInstanceIdEqualTo(serviceInstance.getInstanceId());

            int i = serviceInstanceMapper.insertSelective(serviceInstance);
            log.info("新增服务实例:{}", serviceInstance);
        }
        return this.getServiceInstance(serviceInstance.getId());
    }
}
