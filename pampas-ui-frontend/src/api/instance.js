import request from '@/utils/request'

/**
 * 获取服务信息
 * @param form_data
 */
export function get_instance_in_service(service_id) {
  return request({
    url: '/instance/get_by_service',
    method: 'get',
    params: {service_id: service_id}
  })
}


/**
 * 查询服务实例列表
 * @param form_data
 * @param page_num
 * @param page_size
 */
export function get_instance_list(form_data, page_num, page_size) {
  return request({
    url: '/instance/list',
    method: 'post',
    data: form_data,
    params: {
      page_num: page_num,
      page_size: page_size
    }
  })
}

/**
 * 获取服务实例信息
 * @param form_data
 */
export function get_instance(id) {
  return request({
    url: '/instance/get',
    method: 'get',
    params: {id: id}
  })
}

/**
 * 保存服务实例信息
 * @param form_data
 */
export function save_instance(form_data) {
  return request({
    url: '/instance/save',
    method: 'post',
    data: form_data
  })
}
