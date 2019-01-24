package com.example.demo.jwtsecurity.config.security.jwt;

import com.example.demo.jwtsecurity.config.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * </p>
 *
 * @author XieShuang
 * @version v1.0
 * @since 2019-01-07
 */
@Service
public class TokenService {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 如果用户已存在token，则将其加入黑名单
     * @param username
     */
    public void toBlacklist(String username){
        String key = MySecurityConstant.USER_TOKEN + ":" + username;
        if (redisUtils.hasKey(key)){
            String key2 = (String) redisUtils.get(key);
            redisUtils.set(MySecurityConstant.TOKEN_BLACKLIST+":"+key2,username,60*24*7);
        }
    }
}
