package com.github.pampas.ui.mapper;

import com.github.pampas.storage.entity.GatewaySpi;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-14
 */
public interface GatewaySpiCustomMapper {

    List<GatewaySpi> selectSpiClassList(@Param("spi_interface") String spiInterface,
                                        @Param("gateway_group") String group,
                                        @Param("gateway_instance_id") String gatewayInstanceId);

}
