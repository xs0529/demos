package com.example.demo.jwtsecurity.system.service.impl;

import com.example.demo.jwtsecurity.system.entity.Log;
import com.example.demo.jwtsecurity.system.mapper.LogMapper;
import com.example.demo.jwtsecurity.system.service.LogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 谢霜
 * @since 2019-01-07
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

}
