package com.github.pampas.ui.vo.resp;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-01
 */
@Data
@ApiModel("网关实例Tree响应")
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GatewayTreeResp {

    private Integer id;

    private String label;

    private List<GatewayTreeItem> children;


    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class GatewayTreeItem {
        private Integer id;

        private String label;

        private String group;

        private boolean leaf = true;
    }

}
