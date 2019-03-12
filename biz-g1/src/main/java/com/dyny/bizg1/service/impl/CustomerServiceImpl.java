package com.dyny.bizg1.service.impl;

import com.dyny.bizg1.db.entity.Customer;
import com.dyny.bizg1.db.dao.CustomerMapper;
import com.dyny.bizg1.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wanggl
 * @since 2019-03-12
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

}
