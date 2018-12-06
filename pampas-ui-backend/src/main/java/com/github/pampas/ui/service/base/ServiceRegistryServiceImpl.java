package com.github.pampas.ui.service.base;

import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.storage.entity.ServiceRegistry;
import com.github.pampas.storage.entity.ServiceRegistryCondition;
import com.github.pampas.storage.mapper.ServiceRegistryMapper;
import com.github.pampas.ui.base.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-04
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class ServiceRegistryServiceImpl implements ServiceRegistryService {

    private static final Logger log = LoggerFactory.getLogger(ServiceRegistryServiceImpl.class);

    @Autowired
    private ServiceRegistryMapper serviceRegistryMapper;

    @Override
    public ServiceRegistry getServiceRegistry(Integer id) {
        AssertTools.notNull(id, "ID不能为空");
        return serviceRegistryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ServiceRegistry> getServiceRegistry(List<Integer> idList) {
        AssertTools.notEmpty(idList, "ID不能为空");
        ServiceRegistryCondition condition = new ServiceRegistryCondition();
        condition.createCriteria().andIdIn(idList);

        return serviceRegistryMapper.selectByExample(condition);
    }

    @Override
    public Result<ServiceRegistry> getServiceRegistryList(Integer pageNum, Integer pageSize) {
        ServiceRegistryCondition condition = new ServiceRegistryCondition();
        condition.orderBy("id desc");
        Long count = null;
        if (pageNum != null && pageSize != null) {
            condition.setPageInfo(pageNum, pageSize);
            count = serviceRegistryMapper.countByExample(condition);
        }
        List<ServiceRegistry> serviceRegistries = serviceRegistryMapper.selectByExample(condition);
        log.info("查询注册中心列表{}:{}", serviceRegistries.size(), serviceRegistries);
        return Result.buildResult(serviceRegistries, count == null ? serviceRegistries.size() : count);
    }

    @Override
    @Transactional
    public ServiceRegistry saveServiceRegistry(ServiceRegistry serviceRegistry) {
        if (serviceRegistry.getId() != null) {
            int i = serviceRegistryMapper.updateByPrimaryKeySelective(serviceRegistry);
            AssertTools.isTrue(i == 1, "更新失败");
            log.info("更新注册中心成功:{}", serviceRegistry);
        } else {
            AssertTools.notEmpty(serviceRegistry.getName(), "名称不能为空");
            AssertTools.notEmpty(serviceRegistry.getType(), "类型不能为空");
            AssertTools.notEmpty(serviceRegistry.getAddress(), "地址不能为空");
            ServiceRegistryCondition condition = new ServiceRegistryCondition();
            condition.createCriteria().andNameEqualTo(serviceRegistry.getName());
            List<ServiceRegistry> serviceRegistries = serviceRegistryMapper.selectByExample(condition);
            AssertTools.isTrue(serviceRegistries.size() == 0, "存在相同名称的注册中心");
            int i = serviceRegistryMapper.insertSelective(serviceRegistry);
            AssertTools.isTrue(i == 1, "插入失败");
            log.info("新增注册中心成功:{}", serviceRegistry);
        }

        return this.getServiceRegistry(serviceRegistry.getId());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        ServiceRegistry serviceRegistry = this.getServiceRegistry(id);
        AssertTools.notNull(serviceRegistry, "不存在此注册中心");
        int i = serviceRegistryMapper.deleteByPrimaryKey(id);
        AssertTools.isTrue(i == 1, "删除失败");
        log.info("删除注册中心成功:{}", id);
    }
}
