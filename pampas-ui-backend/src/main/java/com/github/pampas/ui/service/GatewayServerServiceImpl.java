package com.github.pampas.ui.service;

import com.github.pampas.storage.dao.GatewayServerDao;
import com.github.pampas.storage.entity.DBGatewayServer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-09-19
 */
@Service
public class GatewayServerServiceImpl implements GatewayServerService {

    public List<DBGatewayServer> query() {
        GatewayServerDao dao = new GatewayServerDao();
        return dao.queryByExample(new DBGatewayServer());
    }


}
