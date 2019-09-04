import request from '@/utils/http'

function getList(params) {
  return request({
    url: 'http://localhost:8620/dataRule/page',
    method: 'get',
    params
  })
}

function getOne(key) {
  return request({
    url: 'http://localhost:8620/dataRule/' + key,
    method: 'get'
  })
}

function create(dataRule) {
  return request.post(
    'http://localhost:8620/dataRule/create',
    dataRule
  )
}


function deleteByKey(key) {
  return request.delete(
    'http://localhost:8620/dataRule/' + key,
  )
}

function deleteByKeys(keys) {
  return request.post(
    'http://localhost:8620/dataRule/delete',
    keys
  )
}

function update(dataRule) {
  return request.put(
    'http://localhost:8620/dataRule/update',
    dataRule
  )
}

export {getList, getOne, deleteByKey, deleteByKeys, create, update};
