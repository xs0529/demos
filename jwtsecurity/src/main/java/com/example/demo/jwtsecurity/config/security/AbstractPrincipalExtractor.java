package com.example.demo.jwtsecurity.config.security;

import com.example.demo.jwtsecurity.system.entity.Role;
import com.example.demo.jwtsecurity.system.entity.User;
import com.example.demo.jwtsecurity.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractPrincipalExtractor implements PrincipalExtractor {

  @Autowired
  UserService userService;
  //用户openid
  public abstract User getUserByOpenId(String id);
  //用户角色，用“FACEBOOK"代表facebook用户，”GITHUB"代表"github用户
  public abstract Role getUserRoleByOauth2ClientName();

  @Override
  public Object extractPrincipal(Map<String, Object> map) {
    //得到对于的社交平台的openid
    String id =  map.get("id").toString();
    // Check if we've already registered this uer
    System.out.println("id: " + id);
    User user = getUserByOpenId(id);
    if (user == null) {
      // If we haven't registered this user yet, create a new one
//      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//      // This Details object exposes a token that allows us to interact with Facebook on this user's behalf
//      String token = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
      user = new User();
      user.setUsername(map.get("id").toString());
      // Set the default Roles for users registered via Facebook
      List<Role> authorities = new ArrayList<>();
      Role role = new Role();
      role.setName("USER");
      authorities.add(role);
      //Oauth2Client客戶端特有角色
      authorities.add(getUserRoleByOauth2ClientName());
      userService.save(user);
    }
    return user.getUsername();
  }
}
