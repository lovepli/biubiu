package com.abc.controller;

import com.abc.annotation.PermInfo;
import com.abc.constant.Root;
import com.abc.entity.SysUser;
import com.abc.entity.SysUserRole;
import com.abc.service.SysRoleService;
import com.abc.service.SysUserRoleService;
import com.abc.service.SysUserService;
import com.abc.util.PageUtils;
import com.abc.vo.Json;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by CaiBaoHong at 2018/4/17 16:41<br>
 * 方法层面进行权限控制 和接口层面进行权限控制
 *   这一个模块和其他的模块不同，对方法进行细粒度的权限控制
 */
@PermInfo(value = "系统用户模块", pval = "a:sys:user") //自定义权限注解 value权限名称 pval权限值 TODO 在类上加权限注解代表父级权限
@RestController
@RequestMapping("/sys_user")
public class SysUserController {

    private static final Logger log = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @PermInfo("添加系统用户") //自定义权限注解 value权限名称 pval权限值 TODO 方法添加注解的作用代表子级权限 这个是对方法进行控制
    @RequiresPermissions("a:sys:user:add")  //shiro自带的权限注解的使用 默认为权限值  这里的
    @PostMapping                     //TODO 没有value值，根据传的参数来看是进哪个controller ???
    public Json add(@RequestBody String body) {

        String oper = "add sys user";
        log.info("{}, body: {}",oper,body);

        SysUser user = JSON.parseObject(body, SysUser.class);//json字符串转对象

        if (StringUtils.isEmpty(user.getUname())) {
            return Json.fail(oper, "用户帐号名不能为空");
        }
        if (StringUtils.isEmpty(user.getPwd())) {
            return Json.fail(oper, "密码不能为空");
        }
        //判断是否存在相同用户名的对象
        SysUser userDB = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("uname", user.getUname()));
        if (userDB != null) {
            return Json.fail(oper, "用户已注册");
        }

        //密码加密
        RandomNumberGenerator saltGen = new SecureRandomNumberGenerator();
        String salt = saltGen.nextBytes().toBase64();
        String hashedPwd = new Sha256Hash(user.getPwd(), salt, 1024).toBase64();
        //保存新用户数据
        user.setPwd(hashedPwd);  //TODO 登录的时候没有检查对比从数据库查询出来的密码？
        user.setSalt(salt);
        user.setCreated(new Date());

        boolean success = sysUserService.insert(user); // TODO 这里添加的系统用户还没有添加任何角色，所以user_role表没有对应的role_id ,可以在添加用户的时候给一个默认的用户角色
        return Json.result(oper, success)
                .data("uid",user.getUid())
                .data("created",user.getCreated());
    }

    @PermInfo("删除系统用户")  //TODO 改进：这个pname字段值没有存到数据库里,是手动录入动!!!
    @RequiresPermissions("a:sys:user:del")
    @DeleteMapping
    public Json delete(@RequestBody String body) {

        String oper = "delete user";
        log.info("{}, body: {}",oper,body);

        JSONObject jsonObj = JSON.parseObject(body);//json字符串转对象
        String uid = jsonObj.getString("uid");
        if (StringUtils.isEmpty(uid)) {
            return Json.fail(oper, "无法删除用户：参数为空（用户id）");
        }

        //限制：不能删当前登录用户
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.equals(uid,user.getUid())){
            return Json.fail(oper, "系统限制：不能删除当前登录账号");
        }

        //检查：不能删除管理员
        boolean containRoot = sysRoleService.checkUidContainRval(uid, Root.ROLE_VAL);
        if (containRoot){
            return Json.fail(oper,"不能删除管理员用户");
        }

        boolean success = sysUserService.deleteById(uid);  //根据用户ID删除用户
        sysUserRoleService.delete(new EntityWrapper<SysUserRole>().eq("user_id",uid)); //根据用户ID删除用户角色表的纪录
        return Json.result(oper, success);
    }

    @PermInfo("更新系统用户的角色")
    @RequiresPermissions("a:sys:user:role:update")
    @PatchMapping("/role")      //TODO 方法添加注解的作用代表子级权限 这个是进一步的对接口进行控制
    public Json updateUserRole(@RequestBody String body) {

        String oper = "update user's roles";
        log.info("{}, body: {}",oper,body);

        JSONObject json = JSON.parseObject(body);
        final String uid = json.getString("uid"); //	"uid": "1227225528402993154",

        //从传入的参数中得到所有的角色ID值
        List<String> rids = json.getJSONArray("rids").toJavaList(String.class);//将参数中的数组对象转换为list集合
        //	"rids": ["1002748319131680769","1002806220860923906", "1002806266750803970"]

        //检查：不能含有管理员角色
        boolean containRoot = sysRoleService.checkRidsContainRval(rids, Root.ROLE_VAL);
        if (containRoot){
            return Json.fail(oper,"不能给非管理员用户赋予管理员角色");
        }

        //删除：原来绑定的角色                  TODO 根据用户ID user_id值删除用户角色user_role表的一条纪录
        boolean deleteSucc = sysUserRoleService.delete(new EntityWrapper<SysUserRole>().eq("user_id", uid));
        if (!deleteSucc) {
            return Json.fail(oper, "无法解除原来的用户-角色关系");
        }

        //更新：绑定新的角色                   TODO stream的用法 同一个用户ID 绑定多个角色id 返回用户角色表的新的集合？？？
        List<SysUserRole> list = rids.stream().map(roleId -> new SysUserRole(uid, roleId)).collect(Collectors.toList());

        if (!rids.isEmpty()){
            boolean addSucc = sysUserRoleService.insertBatch(list);
            return Json.result(oper, addSucc);
        }
        return Json.succ(oper);
    }

    @PermInfo("查询所有系统用户")
    @RequiresPermissions("a:sys:user:list")
    @PostMapping("/query")
    public Json query(@RequestBody String body) {
        String oper = "query user";
        log.info("{}, body: {}", oper, body);
        JSONObject json = JSON.parseObject(body);
        String nick = json.getString("nick");
        //根据昵称模糊查询所有系统用户并分页显示
        Page<SysUser> page = sysUserService.queryUserIncludeRoles(PageUtils.getPageParam(json), nick);
        return Json.succ(oper).data("page", page);
    }

    @PermInfo("查询系统用户信息")
    @RequiresPermissions("a:sys:user:info")
    @GetMapping("/info")
    public Json userInfo() {
        String oper = "query user info";
        log.info("{}", oper);
        // TODO 从Subject/session中查询出用户信息Object userInfo
        Object userInfo = SecurityUtils.getSubject().getPrincipal();
        return Json.succ(oper, "userInfo", userInfo);
    }

    @PermInfo("更新系统用户的信息")
    @RequiresPermissions("a:sys:user:info:update")
    @PatchMapping("/info")
    public Json update(@RequestBody String body) {

        String oper = "update user";
        log.info("{}, body: {}", oper, body);

        SysUser user = JSON.parseObject(body, SysUser.class);

        if (StringUtils.isNotBlank(user.getPwd())){
            //密码加密
            RandomNumberGenerator saltGen = new SecureRandomNumberGenerator(); //TODO 登录的是是时候没有进行登录密码加密，直接填写的是明文密码。但是添加和修改的时候都是加密的密码，为什么会不用比较？
            String salt = saltGen.nextBytes().toBase64();
            String hashedPwd = new Sha256Hash(user.getPwd(), salt, 1024).toBase64();
            user.setPwd(hashedPwd);
            user.setSalt(salt);
        }else{
            user.setPwd(null);
            user.setSalt(null);
        }

        user.setUname(null);
        user.setCreated(null);
        user.setUpdated(new Date());

        //boolean success = sysUserService.update(user,new EntityWrapper<User>().eq("uid",user.getUid()));
        boolean success = sysUserService.updateById(user);

        return Json.result(oper, success).data("updated",user.getUpdated());
    }

    /**
     * 修改用户密码
     * @param body
     * @return
     */
    @PatchMapping("/pwd")
    public Json updatePwd(@RequestBody String body) {

        String oper = "update pwd";
        log.info("{}, body: {}", oper, body);

        JSONObject json = JSON.parseObject(body);
        String pwd = json.getString("pwd");

        if (StringUtils.isBlank(pwd)) {
            return Json.fail(oper,"无法更新密码：密码为空");
        }
        //密码加密
        RandomNumberGenerator saltGen = new SecureRandomNumberGenerator();
        String salt = saltGen.nextBytes().toBase64();
        String hashedPwd = new Sha256Hash(pwd, salt, 1024).toBase64();
        SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();

        SysUser updateData = new SysUser();
        updateData.setUid(currentUser.getUid());
        updateData.setPwd(hashedPwd);
        updateData.setSalt(salt);
        updateData.setUpdated(new Date());
        boolean success = sysUserService.updateById(updateData);
        return Json.result(oper, success).data("updated",updateData.getUpdated());
    }

    @PermInfo("查找系统用户的角色")
    @RequiresPermissions("a:sys:user:role:find")
    @GetMapping("/{uid}/roles")
    public Json findUserRoles(@PathVariable String uid){
        String oper = "find user roles";
        log.info("{}, uid: {}", oper, uid);
        if (StringUtils.isBlank(uid)){
            return Json.fail(oper, "无法查询当前用户的角色值：参数为空（用户id）");
        }
        List<String> rids = sysRoleService.getRoleIdsByUserId(uid);//返回用户拥有的所有角色id
        return Json.succ(oper,"rids",rids);
    }

}
