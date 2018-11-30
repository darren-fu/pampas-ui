package com.github.pampas.ui.mapper;

import com.github.pampas.ui.entity.GatewayRouteRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-28
 */
@Mapper
public interface RouteRuleCustomMapper {

    List<GatewayRouteRule> selectGatewayRouteRules(@Param("gateway_instance_id") String gatewayInstanceId,
                                                   @Param("rule_id") Integer ruleId,
                                                   @Param("rel_status") Boolean relStatus);

}
