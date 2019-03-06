package com.dyny.userservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyny.userservice.db.dao.LogLoginMapper;
import com.dyny.userservice.db.entity.LogLogin;
import com.dyny.userservice.service.LogLoginService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wanggl
 * @since 2019-03-06
 */
@Service
public class LogLoginServiceImpl extends ServiceImpl<LogLoginMapper, LogLogin> implements LogLoginService {

}
