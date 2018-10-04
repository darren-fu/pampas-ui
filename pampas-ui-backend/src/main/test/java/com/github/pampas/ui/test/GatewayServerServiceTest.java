package com.github.pampas.ui.test;

import com.github.pampas.storage.entity.DBGatewayServer;
import com.github.pampas.ui.service.GatewayServerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-09-19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.github.pampas.ui.Application.class)
@WebAppConfiguration
public class GatewayServerServiceTest {

    @Autowired
    private GatewayServerService gatewayServerService;

    @Test
    public void testQuery(){

        List<DBGatewayServer> query = gatewayServerService.query();
        System.out.println(query);
    }

}
