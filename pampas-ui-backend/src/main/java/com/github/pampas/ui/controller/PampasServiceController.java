package com.github.pampas.ui.controller;


import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.PampasServiceInfoService;
import com.github.pampas.ui.vo.req.ServiceListReq;
import com.github.pampas.ui.vo.req.ServiceSaveReq;
import com.github.pampas.ui.vo.resp.ServiceResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-16
 */
@RestController
@CrossOrigin
public class PampasServiceController {

    @Autowired
    private PampasServiceInfoService serviceInfoService;

    @RequestMapping(value = "/service/get", method = RequestMethod.GET)
    public Response<ServiceResp> getService(@RequestParam("id") Integer id) {
        return serviceInfoService.getService(id);
    }

    @RequestMapping(value = "/service/list", method = RequestMethod.POST)
    public Response<Result<ServiceResp>> listService(@RequestBody ServiceListReq req,
                                                     @RequestParam("page_num") Integer pageNum,
                                                     @RequestParam("page_size") Integer pageSize) {
        return serviceInfoService.getServiceList(req, pageNum, pageSize);
    }


    @RequestMapping(value = "/service/save", method = RequestMethod.POST)
    public Response<ServiceResp> saveService(@RequestBody ServiceSaveReq req) {
        return serviceInfoService.saveService(req);
    }



}
