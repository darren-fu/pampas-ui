import request from '@/utils/request'


/**
 * 查询路由规则列表
 * @param form_data
 * @param page_num
 * @param page_size
 */
export function get_rule_list(name, status, page_num, page_size) {
  return request({
    url: '/route_rule/list',
    method: 'get',
    params: {
      name: name,
      status: status,
      page_num: page_num,
      page_size: page_size
    }
  })
}


/**
 * 获取路由规则树形结构
 */
export function get_rule_tree() {
  return request({
    url: '/route_rule/tree',
    method: 'get',
    params: {}
  })
}

/**
 * 获取服务路由规则
 * @param form_data
 */
export function get_route_rule(id) {
  return request({
    url: '/route_rule/get',
    method: 'get',
    params: {id: id}
  })
}


/**
 * 获取服务路由关联的网关
 * @param form_data
 */
export function get_route_rule_rel(rule_id) {
  return request({
    url: '/route_rule/rel_gateway',
    method: 'get',
    params: {id: rule_id}
  })
}


/**
 * 保存服务路由规则
 * @param form_data
 */
export function save_route_rule(form_data) {
  return request({
    url: '/route_rule/save',
    method: 'post',
    data: form_data
  })
}



/**
 * 保存服务路由规则
 * @param form_data
 */
export function save_route_rule_rel(form_data) {
  return request({
    url: '/route_rule/save_rel',
    method: 'post',
    data: form_data
  })
}
