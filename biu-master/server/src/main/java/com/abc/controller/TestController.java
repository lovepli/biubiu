package com.abc.controller;

import com.abc.annotation.PermInfo;
import org.apache.shiro.authz.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by CaiBaoHong at 2018/4/17 14:16<br>
 */
@PermInfo(value = "测试模块模块", pval = "a:test")  //权限值，父级权限
@RestController
@RequestMapping("/test")
public class TestController {

    // 由于ShiroConfig中配置了该路径可以匿名访问，所以这接口不需要登录就能访问
    @GetMapping("/hello")
    public String hello() {
        return "hello spring boot";
    }

    // 如果ShiroConfig中没有配置该路径可以匿名访问，那么直接被登录过滤了。
    // 如果配置了可以匿名访问，那这里在没有登录的时候可以访问，但是用户登录后就不能访问
    /**
     * @RequiresGuest
     * 验证是否是一个guest的请求，与@RequiresUser完全相反。
     * 换言之，RequiresUser  == !RequiresGuest。
     * 此时subject.getPrincipal() 结果为null.
     * 即只允许没有登录的游客身份访问这个接口
     * @return
     */
    @RequiresGuest
    @GetMapping("/guest")
    public String guest() {
        return "@RequiresGuest";
    }

    /**
     * @RequiresAuthentication
     * 验证用户是否登录，等同于方法subject.isAuthenticated() 结果为true时。
     * @return
     */
    @RequiresAuthentication
    @GetMapping("/authn")
    public String authn() {
        return "@RequiresAuthentication";
    }

    /**
     * @RequiresUser
     * 验证用户是否被记忆，user有两种含义：
     * 一种是成功登录的（subject.isAuthenticated() 结果为true）；
     * 另外一种是被记忆的（subject.isRemembered()结果为true）
     * @return
     */
    @RequiresUser
    @GetMapping("/user")
    public String user() {
        return "@RequiresUser";
    }

    /**
     *@RequiresPermissions
     * 例如： @RequiresPermissions({"file:read", "write:aFile.txt"} )
     *       void someMethod();
     * 要求subject中必须同时含有file:read和write:aFile.txt的权限才能执行方法someMethod()。
     * 否则抛出异常AuthorizationException。
     * @return
     */
    @RequiresPermissions("a:mvn:install") //shiro自带的权限注解 权限值
    @GetMapping("/mvnInstall")
    public String mvnInstall() {
        return "mvn:install";
    }

    @RequiresPermissions("a:gradleBuild") //shiro自带的权限注解 权限值
    @PermInfo("构建gradle") //自己定义的的权限注解 属性1-权限名称 属性2-权限值
    @GetMapping("/gradleBuild")
    public String gradleBuild() {
        return "gradleBuild";
    }

    /**
     * @RequiresRoles
     * 例如：@RequiresRoles("aRoleName");
     *      void someMethod();
     * 如果subject中有aRoleName角色才可以访问方法someMethod。如果没有这个权限则会抛出异常AuthorizationException。
     * @return
     */
    @RequiresRoles("admin")  //这里提供一个数据库已经存在的admin角色测试
    //@RequiresRoles("js")
    @GetMapping("/js")
    public String js() {
        return "js programmer";
    }


}
