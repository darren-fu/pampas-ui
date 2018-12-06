import request from '@/utils/request'

/**
 * 查询服务列表
 * @param form_data
 * @param page_num
 * @param page_size
 */
export function get_service_list(form_data, page_num, page_size) {
  return request({
    url: '/service/list',
    method: 'post',
    data: form_data,
    params: {
      page_num: page_num,
      page_size: page_size
    }
  })
}

/**
 * 获取服务信息
 * @param form_data
 */
export function get_service(id) {
  return request({
    url: '/service/get',
    method: 'get',
    params: {id: id}
  })
}


/**
 * 查询注册中心上指定服务的实例列表
 * @param form_data
 * @param page_num
 * @param page_size
 */
export function check_service_instance_list(type, service_name, registry_id) {
  return request({
    url: '/service/instance/list_by_registry',
    method: 'get',
    params: {
      type: type,
      service_name: service_name,
      registry_id: registry_id
    }
  })
}

/**
 * 保存服务信息
 * @param form_data
 */
export function save_service(form_data) {
  return request({
    url: '/service/save',
    method: 'post',
    data: form_data
  })
}

/**
 * 删除服务信息
 * @param form_data
 */
export function delete_service(id) {
  return request({
    url: '/service/delete',
    method: 'post',
    data: {id: id}
  })
}



