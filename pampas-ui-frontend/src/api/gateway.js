import request from '@/utils/request'

/**
 * 获取gateway
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
 * 获取网关树形结构
 */
export function get_gateway_tree() {
  return request({
    url: '/gateway/tree',
    method: 'get',
    params: {}
  })
}

/**
 * 获取关联路由规则信息
 * @param form_data
 */
export function get_rel_rules(gateway_id) {
  return request({
    url: '/gateway/get_rel_rule',
    method: 'get',
    params: {id: gateway_id}
  })
}

/**
 * 保存
 * @param form_data
 */
export function save_instance(form_data) {
  return request({
    url: '/gateway/save',
    method: 'post',
    data: form_data
  })
}


/**
 * 保存服务路由规则
 * @param form_data
 */
export function save_gateway_rule_rel(form_data) {
  return request({
    url: '/gateway/save_rel',
    method: 'post',
    data: form_data
  })
}

/**
 * 获取gateway CONFIG
 * @param form_data
 */
export function get_gateway_config(id, group, instance_id) {
  return request({
    url: '/gateway/get_config_list',
    method: 'get',
    params: {id: id, group: group, instance_id: instance_id}
  })
}


/**
 * 获取gateway SPI
 * @param form_data
 */
export function get_gateway_spi(id, group, instance_id) {
  return request({
    url: '/gateway/get_spi_list',
    method: 'get',
    params: {id: id, group: group, instance_id: instance_id}
  })
}



