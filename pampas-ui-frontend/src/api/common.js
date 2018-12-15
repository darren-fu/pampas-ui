import request from '@/utils/request'

/**
 * 获取gateway
 * @param form_data
 */
export function get_loadbalancers(group, instance_id) {
  return request({
    url: '/common/get_spi_list',
    method: 'get',
    params: {spi: 'LoadBalancer', group: group, instance_id: instance_id}
  })
}

