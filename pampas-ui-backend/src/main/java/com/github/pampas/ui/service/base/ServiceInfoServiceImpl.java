package com.github.pampas.ui.service.base;

import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.storage.entity.ServiceCondition;
import com.github.pampas.storage.mapper.ServiceMapper;
import com.github.pampas.ui.base.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

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
}
