package com.github.pampas.ui.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 结果包装类
 * Created by erhu on 2017/1/23.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel("结果")
public class Result<T> implements Serializable {
    @ApiModelProperty(notes = "结果")
    private List<T> data;
    @ApiModelProperty(notes = "元数据")
    private Integer count;

    public static <E> Result<E> buildResult(List<E> data, Integer count) {
        Result<E> result = new Result<E>();
        if (data == null) {
            // 保证返回列表不是null
            data = Collections.EMPTY_LIST;
        }
        result.setData(data);
        result.setCount(count);
        return (Result<E>) result;

    }


}
