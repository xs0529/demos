package com.example.demo.jwtsecurity.config.security;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 *  自定义的权限加载数据源，官方说明: https://docs.spring.io/spring-security/site/docs/5.1.3.RELEASE/reference/htmlsingle/#appendix-faq-dynamic-url-metadata
 *
 *  使用要点记录：https://blog.csdn.net/qushapos/article/details/84940810
 * </p>
 *
 * @author XieShuang
 * @version v1.0
 * @since 2019-01-25
 */
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation)o).getRequest();
        Iterator var3 = requestMap.entrySet().iterator();
        Map.Entry entry;
        do {
            if (!var3.hasNext()) {
                return null;
            }
            entry = (Map.Entry)var3.next();
        } while(!((RequestMatcher)entry.getKey()).matches(request));
        return (Collection)entry.getValue();
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }

    public static void loaderUrlAndRole(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap) {
        MyFilterInvocationSecurityMetadataSource.requestMap = requestMap;
    }
}
