package com.taobao.tmalljpa.integration.shiro;

import com.taobao.tmalljpa.entity.User;
import com.taobao.tmalljpa.service.UserService;
import com.taobao.tmalljpa.util.ToolClass;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  shiro:用户账号登录验证，和权限管理核心类
 *  AuthorizingRealm 类继承自 AuthenticatingRealm, 但没有实现 AuthenticatingRealm 中的
 *  doGetAuthenticationInfo
 */
public class ShiroRealm extends AuthorizingRealm{

    @Autowired
    private UserService userService;

    //权限管理
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        /*  登录状态下调用调用获取权限方法时，会调用该方法，
            该方法需获取（数据库）用户的角色和权限注入 simpleAuthorizationInfo
        * */
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        return simpleAuthorizationInfo;
    }

    //账号登录验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        /*
        * 调用登录验证时，该方法会被调用，需向实现账号密码验证逻辑（获取数据库数据）
        * */
        ToolClass.err("realm account===");
        String username = authenticationToken.getPrincipal().toString();
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String password = new String(usernamePasswordToken.getPassword());
        User user = userService.findByName(username);
        //自行控制验证过程
        String encodePassword = new SimpleHash("md5",password,user.getSalt(),2).toString();
        ToolClass.out("222=password="+password+",salt="+user.getSalt()+",encodePassword="+encodePassword);
        if (encodePassword.equals(user.getPassword())){// 匹配成功
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user.getName(),password,getName());//getName() 是当前Realm的继承方法,通常返回当前类名 :databaseRealm
            return simpleAuthenticationInfo;
        }else {// 密码错误
            throw new AuthenticationException();
        }

        // 这里验证交由shiro默认类验证，在配置类中，配置该验证类。（也可自行验证，密码错误时手动抛出AuthenticationException，而不是抛出具体错误原因，免得给破解者提供帮助信息）
        //SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username,password, ByteSource.Util.bytes(user.getSalt()),getName());
        //return simpleAuthenticationInfo;
    }
}
