package com.github.pampas.ui.utils;

import com.ecwid.consul.v1.ConsulClient;
import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.storage.entity.ServiceRegistry;
import com.github.pampas.ui.base.RegistryTypeEnum;
import com.github.pampas.ui.service.base.ServiceRegistryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryClient;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-05
 */
@Component
public class DiscoveryClientContainer implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private ServiceRegistryService registryService;

    @Autowired
    private ConsulDiscoveryProperties consulDiscoveryProperties;

    private static ConcurrentHashMap<Integer, DiscoveryClient> discoveryClientMap = new ConcurrentHashMap<>();


    public DiscoveryClient getDiscoveryClient(Integer registryId) {
        ServiceRegistry serviceRegistry = registryService.getServiceRegistry(registryId);
        String address = serviceRegistry.getAddress();
        AssertTools.notEmpty(address, "地址为空");
        String[] addressArr = address.split(",");
        // Consul 只支持个地址
        if (RegistryTypeEnum.Consul.getValue().equals(serviceRegistry.getType())) {
            ConsulDiscoveryClient.LocalResolver lifecycleRegistrationResolver = new SimpleRegistrationResolver();
            DiscoveryClient discoveryClient = discoveryClientMap.computeIfAbsent(registryId, v -> {
                HostAndPort hostAndPort = HostAndPort.build(addressArr[0]);
                ConsulClient consulClient = new ConsulClient(hostAndPort.getHost(), hostAndPort.getPort());
                ConsulDiscoveryClient consulDiscoveryClient = new ConsulDiscoveryClient(consulClient, consulDiscoveryProperties, lifecycleRegistrationResolver);
                return consulDiscoveryClient;
            });
            return discoveryClient;
        }

        for (String add : addressArr) {
            HostAndPort hostAndPort = HostAndPort.build(add);
//            ZookeeperDiscoveryClient zookeeperDiscoveryClient = new ZookeeperDiscoveryClient();

            // todo zookeeper etcd
        }
        return null;
    }

    public void deleteDiscoveryClientCache(Integer registryId) {
        discoveryClientMap.remove(registryId);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Data
    @AllArgsConstructor
    private static class HostAndPort {
        private String host;
        private Integer port;

        protected static HostAndPort build(String address) {
            String[] hostAndPort = address.split(":");
            AssertTools.isTrue(hostAndPort.length == 2, "错误的address格式:" + address);
            HostAndPort hap = new HostAndPort(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
            return hap;
        }
    }


    public class SimpleRegistrationResolver implements ConsulDiscoveryClient.LocalResolver {
        @Override
        public String getInstanceId() {
//            applicationContext.getBean()
            return null;
        }

        @Override
        public Integer getPort() {
            return null;
        }
    }
}
