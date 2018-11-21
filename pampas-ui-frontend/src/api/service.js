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



