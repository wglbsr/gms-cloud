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
  return request({
    url: 'http://localhost:8620/dataRule/',
    method: 'post',
    dataRule
  })
}


function deleteByKey(key) {
  return request({
    url: 'http://localhost:8620/dataRule/' + key,
    method: 'delete'
  })
}

function deleteByKeys(keys) {
  return request({
    url: 'http://localhost:8620/dataRule/',
    method: 'delete',
    keys
  })
}

function update(dataRule) {
  return request({
    url: 'http://localhost:8620/dataRule/',
    method: 'put',
    dataRule
  })
}

export {getList, getOne, deleteByKey, deleteByKeys, create, update};
