package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.vo.Request;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-28
 */
@Data
@ApiModel(value = "保存服务路由规则请求")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RouteRuleSaveReq implements Request {

    /**
     * 模式 1 保存基础信息
     * 2 保存 规则详情
     */
    private int mode = 1;

    private Integer id;

    private String name;

    private String mappingHost;

    private String remark;

    private Boolean status;

    private List<Map<String, String>> ruleList;

    @Data
    @ApiModel(value = "服务路由规则条目")
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class RuleItem implements Request {
    }

}
