package com.github.pampas.ui.service;

import com.github.pampas.ui.base.IngressService;
import com.github.pampas.ui.base.vo.Response;
import com.github.pampas.ui.base.vo.Result;
import com.github.pampas.ui.vo.req.ServiceListReq;
import com.github.pampas.ui.vo.req.ServiceSaveReq;
import com.github.pampas.ui.vo.resp.InstanceResp;
import com.github.pampas.ui.vo.resp.ServiceResp;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-16
 */
public interface PampasServiceInfoService extends IngressService {


    /**
     * 根据ID查询服务详情
     *
     * @param id the id
     * @return the service
     */
    Response<ServiceResp> getService(Integer id);


    /**
     * 查询服务列表
     *
     * @param req      the req
     * @param pageNum  the page num
     * @param pageSize the page size
     * @return service list
     */
    Response<Result<ServiceResp>> getServiceList(ServiceListReq req, Integer pageNum, Integer pageSize);

    /**
     * 保存服务信息
     *
     * @param req the req
     * @return response
     */
    Response<ServiceResp> saveService(ServiceSaveReq req);



}
