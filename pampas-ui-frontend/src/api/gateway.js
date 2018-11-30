import request from '@/utils/request'

/**
 * 获取服务信息
 * @param form_data
 */
export function get_gateway(gateway_id) {
  return request({
    url: '/gateway/get',
    method: 'get',
    params: {id: gateway_id}
  })
}


/**
 * 查询gateway实例列表
 * @param form_data
 * @param page_num
 * @param page_size
 */
export function get_gateway_list(form_data, page_num, page_size) {
  return request({
    url: '/gateway/list',
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
export function get_rel_rules(gateway_id) {
  return request({
    url: '/gateway/get_rel_rule',
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
    url: '/gateway/save',
    method: 'post',
    data: form_data
  })
}
