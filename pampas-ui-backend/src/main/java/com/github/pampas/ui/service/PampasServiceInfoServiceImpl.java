package com.github.pampas.ui.service;

import com.github.pampas.storage.entity.Service;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.service.base.ServiceInfoService;
import com.github.pampas.ui.service.base.ServiceInstanceService;
import com.github.pampas.ui.utils.BeanTools;
import com.github.pampas.ui.vo.req.ServiceListReq;
import com.github.pampas.ui.vo.req.ServiceSaveReq;
import com.github.pampas.ui.vo.resp.ServiceResp;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-16
 */
@org.springframework.stereotype.Service
public class PampasServiceInfoServiceImpl implements PampasServiceInfoService {

    @Autowired
    private ServiceInfoService serviceInfoService;

    @Autowired
    private ServiceInstanceService instanceService;

    @Override
    public Response<ServiceResp> getService(Integer id) {
        Service service = serviceInfoService.getService(id);
        ServiceResp serviceResp = BeanTools.copyBean(service, ServiceResp.class);
        return Response.buildSuccessResponseWithInfo(serviceResp);
    }

    @Override
    public Response<Result<ServiceResp>> getServiceList(ServiceListReq req, Integer pageNum, Integer pageSize) {
        Result<Service> serviceList = serviceInfoService.getServiceList(req.getServiceName(), req.getGroup(), pageNum, pageSize);
        List<ServiceResp> serviceRespList = BeanTools.copyBeans(serviceList.getData(), ServiceResp.class);
        return Response.buildSuccessResponseWithResult(serviceRespList, serviceList.getCount());
    }

    @Override
    public Response<ServiceResp> saveService(ServiceSaveReq req) {
        Service service = BeanTools.copyBean(req, Service.class);
        Service newService = serviceInfoService.saveService(service);
        ServiceResp serviceResp = BeanTools.copyBean(newService, ServiceResp.class);
        return Response.buildSuccessResponseWithInfo(serviceResp);
    }


}
