package com.github.pampas.ui.service;

import com.github.pampas.common.base.PampasConsts;
import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.common.tools.StringBuilderFactory;
import com.github.pampas.storage.entity.GatewayConfig;
import com.github.pampas.storage.entity.GatewayInstance;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.service.base.GatewayInstanceService;
import com.github.pampas.ui.service.base.GatewaySpiService;
import com.github.pampas.ui.utils.HttpTools;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-17
 */
@Service
public class GatewayNotifyServiceImpl implements PampasNotifyService {

    private static final Logger log = LoggerFactory.getLogger(GatewayNotifyServiceImpl.class);

    @Autowired
    private GatewaySpiService gatewaySpiService;

    @Autowired
    private GatewayInstanceService gatewayInstanceService;

    @Override
    public String notifyConfigLoaderWithKey(String group, String gatewayInstanceId, String configLoaderKey) {
        return null;
    }

    @Override
    public String notifyConfigLoaderWithName(String group, String gatewayInstanceId, String configLoaderName) {
        return null;
    }

    @Override
    public Response notifyGatewayConfigUpdate(String group, String gatewayInstanceId, String configSpiClass) {
        AssertTools.notEmpty(configSpiClass, "必须指定推送的配置模块");
        AssertTools.notEmpty(group, "必须指定推送的网关分组");

        List<GatewayConfig> gatewayConfigList = gatewaySpiService.getGatewayConfigList(group, gatewayInstanceId, configSpiClass);
        AssertTools.notEmpty(gatewayConfigList, "不存在的配置");
        String spiName = gatewayConfigList.get(0).getConfigSpiName();
        List<GatewayInstance> gatewayList = gatewayInstanceService.getGatewayList(group, gatewayInstanceId);
        AssertTools.notEmpty(gatewayList, "没有网关可推送");
        String result = "";
        int success = 0;
        int failed = 0;
        for (GatewayInstance gatewayInstance : gatewayList) {
            String url = "http://" + gatewayInstance.getHost() + ":" + gatewayInstance.getProxyPort() + PampasConsts.GATEWAY_REQ_PREFIX
                    + "/" + PampasConsts.GatewayOperation.REFRESH_DEFINABLE_CONFIG_BY_CLASS_NAME
                    + "/" + spiName;
            log.info("通知网关更新自定义配置:[{},{},{}],URL:{}", group, gatewayInstanceId, configSpiClass, url);
            try {
                CloseableHttpResponse closeableHttpResponse = HttpTools.doGet(url);
                if (closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
                    log.info("推送成功:{}", url);
                    success++;
                }
            } catch (Exception e) {
                result += e.getMessage() + ";";
                failed++;
                log.error("推送:{}失败:{}", url, e.getMessage(), e);
            }
        }
        StringBuilder sb = StringBuilderFactory.DEFAULT.stringBuilder();
        sb.append("推送完成!")
                .append("成功：").append(success).append("个");
        if (failed > 0) {
            sb.append("；失败：" + failed).append("个");
            sb.append("；详情：" + result);
        }
        return Response.buildSuccessResponseWithInfo(sb.toString());
    }
}
