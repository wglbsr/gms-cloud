import request from '@/utils/http'

export function getList(params) {
  return request({
    url: 'http://localhost:8620/dataRule/page',
    method: 'get',
    params
  })
}
