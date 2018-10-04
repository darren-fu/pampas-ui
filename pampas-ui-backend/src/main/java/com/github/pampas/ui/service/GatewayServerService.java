package com.github.pampas.ui.service;

import com.github.pampas.storage.entity.DBGatewayServer;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-09-19
 */
public interface GatewayServerService {

    List<DBGatewayServer> query();
}
