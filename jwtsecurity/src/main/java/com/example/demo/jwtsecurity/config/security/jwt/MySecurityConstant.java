package com.example.demo.jwtsecurity.config.security.jwt;

/**
 * @author: 谢霜
 * @Date: 2018/09/13 上午 11:30
 * @Description:
 */
public interface MySecurityConstant {
    /**
     * token分割 token生成之后再添上这一段再次混淆加密，取出时先分割再解密，随便乱填
     */
    String TOKEN_SPLIT = "kjsdafidfnfasu ";

    /**
     * JWT签名加密key
     */
    String JWT_SIGN_KEY = "xieshuang";

    /**
     * token参数头
     */
    String HEADER = "accessToken";

    /**
     * 权限参数头
     */
    String AUTHORITIES = "authorities";

    /**
     * 用户选择JWT保存时间参数头
     */
    String SAVE_LOGIN = "saveLogin";

    /**
     * 禁止登陆的token黑名单key
     */
    String TOKEN_BLACKLIST = "tokenblacklist";

    /**
     * 已经登陆的token key
     */
    String USER_TOKEN = "usertoken";
}
