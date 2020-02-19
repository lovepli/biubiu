package com.abc.shiro;


import com.abc.entity.SysUser;
import com.abc.service.SysPermService;
import com.abc.service.SysRoleService;
import com.abc.service.SysUserService;
import com.abc.vo.AuthVo;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 这个类是参照JDBCRealm写的，主要是自定义了如何查询用户信息，如何查询用户的角色和权限，如何校验密码等逻辑
 */
public class UserRealm extends AuthorizingRealm {

    private static final Logger log =LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private SysUserService userService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysPermService permService;

    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        //设置用于匹配密码的CredentialsMatcher
        HashedCredentialsMatcher hashMatcher = new HashedCredentialsMatcher();
        hashMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
        hashMatcher.setStoredCredentialsHexEncoded(false);
        hashMatcher.setHashIterations(1024);
        super.setCredentialsMatcher(hashMatcher);
    }

    /**
     * 认证
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //null usernames are invalid
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        SysUser user = (SysUser) getAvailablePrincipal(principals);
        Set<AuthVo> roles = user.getRoles();
        Set<AuthVo> perms = user.getPerms();
        log.info("获取角色权限信息: roles: {}, perms: {}",roles,perms);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles.stream().map(AuthVo::getVal).collect(Collectors.toSet()));
        info.setStringPermissions(perms.stream().map(AuthVo::getVal).collect(Collectors.toSet()));
        return info;
    }

    /**
     * 验证当前登录的Subject
     * AuthController.login()方法中执行Subject.login()时 执行此方法
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户名
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        //获取用户名
        //String username2 = (String) token.getPrincipal(); //UsernamePasswordToken与AuthenticationToken的继承关系
        // 获取用户密码
        //String password2 = new String((char[]) token.getCredentials());
        //根据用户名和密码查询出用户SysUser

        if (username == null) {
            throw new AccountException("用户名不能为空");
        }
        //根据用户名查询出用户
        SysUser user = userService.selectOne(new EntityWrapper<SysUser>().eq("uname", username));
        if (user == null) {
            throw new UnknownAccountException("找不到用户（"+username+"）的帐号信息");
        }

        //查询用户的角色和权限存到SimpleAuthenticationInfo中，这样在其它地方
        //SecurityUtils.getSubject().getPrincipal()就能拿出用户的所有信息，包括角色和权限
        Set<AuthVo> roles = roleService.getRolesByUserId(user.getUid());
        Set<AuthVo> perms = permService.getPermsByUserId(user.getUid());
        user.getRoles().addAll(roles);
        user.getPerms().addAll(perms);

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPwd(), getName()); //三个参数
        if (user.getSalt() != null) {
            info.setCredentialsSalt(ByteSource.Util.bytes(user.getSalt())); //密码加盐处理
        }

        //这里也可以把用户信息存入到session中
        // SecurityUtils.getSubject().getSession().setAttribute(Constants.SESSION_USER_INFO, user);
        return info;

    }





}
