package com.github.pampas.ui.controller;

import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.PampasServiceInfoService;
import com.github.pampas.ui.service.PampasServiceInstanceService;
import com.github.pampas.ui.vo.req.InstanceListReq;
import com.github.pampas.ui.vo.req.InstanceSaveReq;
import com.github.pampas.ui.vo.req.ServiceListReq;
import com.github.pampas.ui.vo.req.ServiceSaveReq;
import com.github.pampas.ui.vo.resp.InstanceResp;
import com.github.pampas.ui.vo.resp.ServiceResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-20
 */
@RestController
@CrossOrigin
public class PampasServiceInstanceController {

    @Autowired
    private PampasServiceInstanceService instanceService;

    @RequestMapping(value = "/instance/get_by_service", method = RequestMethod.GET)
    public Response<Result<InstanceResp>> getInstanceInService(@RequestParam("service_id") Integer serviceId) {
        return instanceService.getInstanceInService(serviceId);
    }


    @RequestMapping(value = "/instance/get", method = RequestMethod.GET)
    public Response<InstanceResp> getService(@RequestParam("id") Integer id) {
        return instanceService.getInstance(id);
    }

    @RequestMapping(value = "/instance/list", method = RequestMethod.POST)
    public Response<Result<InstanceResp>> listService(@RequestBody InstanceListReq req,
                                                     @RequestParam("page_num") Integer pageNum,
                                                     @RequestParam("page_size") Integer pageSize) {
        return instanceService.getInstanceList(req, pageNum, pageSize);
    }


    @RequestMapping(value = "/instance/save", method = RequestMethod.POST)
    public Response<InstanceResp> saveService(@RequestBody InstanceSaveReq req) {
        return instanceService.saveInstance(req);
    }
}
