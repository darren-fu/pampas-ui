package com.github.pampas.ui.service;

import com.github.pampas.common.tools.AssertTools;
import com.github.pampas.storage.entity.GatewaySpi;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.base.GatewaySpiService;
import com.github.pampas.ui.utils.SpiTools;
import com.github.pampas.ui.vo.resp.GatewaySpiResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-14
 */
@Service
public class GatewayCommonServiceImpl implements GatewayCommonService {
    private static final Logger log = LoggerFactory.getLogger(GatewayCommonServiceImpl.class);

    @Autowired
    private GatewaySpiService gatewaySpiService;

    @Override
    public Response<Result<GatewaySpiResp>> getSpiClassList(String spiInterface, String group, String gatewayInstanceId) {
        Class clz = SpiTools.chooseSpiClz(spiInterface);
        AssertTools.notNull(clz, "SPI不能为空");
        List<GatewaySpi> spiList = gatewaySpiService.listBySpiInterface(clz, group, gatewayInstanceId);
        List<GatewaySpiResp> spiResps = spiList.stream().map(v -> {
            GatewaySpiResp spiResp = new GatewaySpiResp();
            spiResp.setSpiDesc(v.getSpiDesc());
            spiResp.setSpiName(v.getSpiName());
            return spiResp;
        }).collect(Collectors.toList());
        return Response.buildSuccessResponseWithResult(spiResps, spiList.size());
    }


}
