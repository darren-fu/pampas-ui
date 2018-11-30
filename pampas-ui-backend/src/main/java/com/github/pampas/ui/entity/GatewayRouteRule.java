package com.github.pampas.ui.entity;

import com.github.pampas.storage.entity.GatewayRouteRuleRel;
import lombok.Data;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-28
 */
@Data
public class GatewayRouteRule extends GatewayRouteRuleRel {

    /**
     * rule name
     */
    private String ruleName;

    private String ruleRemark;


    private Boolean relStatus;

    private Boolean ruleStatus;
}
