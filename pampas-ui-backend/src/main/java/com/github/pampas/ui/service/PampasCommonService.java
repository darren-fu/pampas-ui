package com.github.pampas.ui.service;

import com.github.pampas.ui.base.IngressService;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.vo.resp.GatewaySpiResp;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-14
 */
public interface PampasCommonService extends IngressService{

    Response<Result<GatewaySpiResp>> getSpiClassList(String spiInterface, String group, String gatewayInstanceId);
}
