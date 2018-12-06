import request from '@/utils/request'

/**
 * 查询服务列表
 * @param form_data
 * @param page_num
 * @param page_size
 */
export function get_registry_list(form_data, page_num, page_size) {
  return request({
    url: '/registry/list',
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
export function get_registry(id) {
  return request({
    url: '/registry/get',
    method: 'get',
    params: {id: id}
  })
}

/**
 * 保存服务信息
 * @param form_data
 */
export function save_registry(form_data) {
  return request({
    url: '/registry/save',
    method: 'post',
    data: form_data
  })
}

/**
 * 删除注册中心
 * @param id
 */
export function delete_registry(id) {
  return request({
    url: '/registry/delete',
    method: 'post',
    data: {id: id}
  })
}


