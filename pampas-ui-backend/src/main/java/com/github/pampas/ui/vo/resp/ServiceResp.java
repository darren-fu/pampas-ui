package com.github.pampas.ui.vo.resp;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-16
 */
@Data
@ApiModel("查询Service响应")
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ServiceResp {

    private Integer id;

    private String group;

    private String serviceName;

    private String remark;

    private Date insertTime;

    private Date updateTime;

}
