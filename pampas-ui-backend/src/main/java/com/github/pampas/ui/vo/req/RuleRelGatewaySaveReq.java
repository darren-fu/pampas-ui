package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.vo.Request;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-02
 */
@Data
@ApiModel(value = "关联路由规则与网关请求")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RuleRelGatewaySaveReq implements Request {

    private List<Integer> ruleIdList;


    private List<Integer> gatewayIdList;

}
