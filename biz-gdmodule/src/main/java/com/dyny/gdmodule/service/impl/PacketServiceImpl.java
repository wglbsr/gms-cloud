package com.dyny.gdmodule.service.impl;

import com.dyny.gdmodule.db.entity.Packet;
import com.dyny.gdmodule.db.dao.PacketMapper;
import com.dyny.gdmodule.service.PacketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wanggl
 * @since 2019-08-13
 */
@Service
public class PacketServiceImpl extends ServiceImpl<PacketMapper, Packet> implements PacketService {

}
