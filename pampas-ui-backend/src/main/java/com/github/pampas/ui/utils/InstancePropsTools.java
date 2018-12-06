package com.github.pampas.ui.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pampas.common.tools.JsonTools;
import com.github.pampas.storage.entity.ServiceInstance;
import com.github.pampas.ui.vo.req.InstanceSaveReq;
import com.github.pampas.ui.vo.resp.InstanceResp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * 私有方法持有类
 */
public class InstancePropsTools {

    private static final Logger log = LoggerFactory.getLogger(InstancePropsTools.class);

    /**
     * 转换 prop为keyAndVal
     *
     * @param instances
     * @param instanceResps
     */
    public static void convertProJsonToKeyAndVal(List<ServiceInstance> instances, List<InstanceResp> instanceResps) {
        for (ServiceInstance instance : instances) {
            Optional<InstanceResp> same = instanceResps.stream().filter(v -> v.getId().equals(instance.getId())).findFirst();
            if (same.isPresent()) {
                convertProJsonToKeyAndVal(instance, same.get());
            }
        }
    }

    public static void convertProJsonToKeyAndVal(ServiceInstance instance, InstanceResp instanceResp) {
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

    public static List<InstanceResp> convertInstanceToResp(List<ServiceInstance> instances) {
        List<InstanceResp> instanceResps = BeanTools.copyBeans(instances, InstanceResp.class);
        for (ServiceInstance instance : instances) {
            Optional<InstanceResp> same = instanceResps.stream().filter(v -> v.getId().equals(instance.getId())).findFirst();
            if (same.isPresent()) {
                convertProJsonToKeyAndVal(instance, same.get());
            }
        }
        return instanceResps;
    }

}
