package com.github.pampas.ui.vo.resp;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-28
 */
@Data
@ApiModel("查询路由规则信息响应")
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RouteRuleResp {
    private Integer id;
    private String name;
    private String group;
    private String mappingHost;
    private Boolean status;
    private String remark;
    private Date insertTime;
    private Date updateTime;
    private Boolean isDel;

    private List<Map> ruleList;
}
