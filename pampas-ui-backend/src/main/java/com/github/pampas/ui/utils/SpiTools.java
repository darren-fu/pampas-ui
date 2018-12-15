package com.github.pampas.ui.utils;

import com.github.pampas.common.exec.Filter;
import com.github.pampas.common.loadbalance.LoadBalancer;
import javafx.concurrent.Worker;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-14
 */
public class SpiTools {

    /**
     * 返回spi接口 class
     *
     * @param interfaceName
     * @return
     */
    public static Class chooseSpiClz(String interfaceName) {
        if (LoadBalancer.class.getName().indexOf(interfaceName) > -1) {
            return LoadBalancer.class;
        }
        if (Worker.class.getName().indexOf(interfaceName) > -1) {
            return Worker.class;
        }
        if (Filter.class.getName().indexOf(interfaceName) > -1) {
            return Filter.class;
        }
        return null;
    }
}
