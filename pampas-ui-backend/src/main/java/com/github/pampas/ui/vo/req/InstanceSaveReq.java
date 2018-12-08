package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.vo.Request;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-16
 */
@Data
@ApiModel(value = "保存服务实例请求")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InstanceSaveReq implements Request {

    private Integer id;

    private String instanceId;

    @NotNull(message = "服务ID不能为空")
    private Integer serviceId;

    @NotEmpty(message = "服务名称不能为空")
    private String serviceName;

    @NotEmpty(message = "服务地址不能为空")
    private String host;

    private String hostName;

    private String version;
    private String room;

    @NotNull(message = "服务端口不能为空")
    private Integer port;

    private Integer weight;

    private Integer status;

    private String group;

    private String remark;

    private List<KeyAndVal> propList;

    @Data
    @ApiModel(value = "服务实例属性")
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class KeyAndVal implements Request{

        @NotEmpty(message = "属性Key不能为空")
        private String key;

        private String value;

        public static List<KeyAndVal> convertMapToKeyAndVal(Map<String,String> map){
            List<KeyAndVal> list = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                KeyAndVal keyAndVal = new KeyAndVal();
                keyAndVal.key = entry.getKey();
                keyAndVal.value = entry.getValue();
                list.add(keyAndVal);
            }
            return list;
        }
    }

}
