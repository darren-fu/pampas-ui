package com.github.pampas.ui.controller;


import com.github.pampas.ui.base.ServiceTypeEnum;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.PampasServiceInfoService;
import com.github.pampas.ui.vo.req.IdOperationReq;
import com.github.pampas.ui.vo.req.ServiceListReq;
import com.github.pampas.ui.vo.req.ServiceRegistrySaveReq;
import com.github.pampas.ui.vo.req.ServiceSaveReq;
import com.github.pampas.ui.vo.resp.InstanceResp;
import com.github.pampas.ui.vo.resp.ServiceRegistryResp;
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


    @RequestMapping(value = "/service/delete", method = RequestMethod.POST)
    public Response<ServiceResp> saveService(@RequestBody IdOperationReq req) {
        return serviceInfoService.deleteService(req.getId());
    }

    @RequestMapping(value = "/service/instance/list_by_registry", method = RequestMethod.GET)
    public Response<Result<InstanceResp>> saveService(@RequestParam(value = "type", required = false, defaultValue = "spring cloud") String type,
                                                      @RequestParam("service_name") String serviceName,
                                                      @RequestParam("registry_id") Integer registryId) {
        return serviceInfoService.getListInRegistry(ServiceTypeEnum.getEnum(type), serviceName, registryId);
    }

    @RequestMapping(value = "/service/instance/update_by_registry", method = RequestMethod.GET)
    public Response updateServiceInstanceFromRegistry(@RequestParam("service_id") Integer serviceId,
                                                      @RequestParam(value = "flush", required = false) Boolean flushBeforeUpdate) {
        return serviceInfoService.updateServiceInstanceFromRegistry(serviceId, flushBeforeUpdate);
    }


/********************************************************************************/
//注册中心

    /********************************************************************************/

    @RequestMapping(value = "/registry/get", method = RequestMethod.GET)
    public Response<ServiceRegistryResp> getRegistry(@RequestParam("id") Integer id) {
        return serviceInfoService.getRegistry(id);
    }

    @RequestMapping(value = "/registry/list", method = RequestMethod.POST)
    public Response<Result<ServiceRegistryResp>> listRegistry(@RequestBody(required = false) ServiceListReq req,
                                                              @RequestParam(value = "page_num", required = false) Integer pageNum,
                                                              @RequestParam(value = "page_size", required = false) Integer pageSize) {
        return serviceInfoService.getRegistryList(pageNum, pageSize);
    }


    @RequestMapping(value = "/registry/save", method = RequestMethod.POST)
    public Response<ServiceRegistryResp> saveRegistry(@RequestBody ServiceRegistrySaveReq req) {
        return serviceInfoService.saveRegistry(req);
    }


    @RequestMapping(value = "/registry/delete", method = RequestMethod.POST)
    public Response deleteRegistry(@RequestBody IdOperationReq req) {
        return serviceInfoService.deleteRegistry(req.getId());
    }

}
