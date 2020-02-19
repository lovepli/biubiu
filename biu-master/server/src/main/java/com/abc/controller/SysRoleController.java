package com.abc.controller;

import com.abc.annotation.PermInfo;
import com.abc.constant.PermType;
import com.abc.entity.SysPerm;
import com.abc.entity.SysRole;
import com.abc.entity.SysRolePerm;
import com.abc.service.SysPermService;
import com.abc.service.SysRolePermService;
import com.abc.service.SysRoleService;
import com.abc.service.SysUserRoleService;
import com.abc.vo.Json;
import com.abc.vo.UpdateRolePermVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * created by CaiBaoHong at 2018/4/17 16:41<br>
 *
 */
@PermInfo(value = "系统角色模块") //自定义权限注解 value权限名称 pval权限值
@RequiresPermissions("a:sys:role") //shiro自带的权限注解的使用 默认为权限值
@RestController
@RequestMapping("/sys_role")
public class SysRoleController {

    private static final Logger log = LoggerFactory.getLogger(SysRoleController.class);

    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysPermService permService;
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SysRolePermService rolePermService;

    @PermInfo("添加角色")
    @RequiresPermissions("a:role:add")
    @PostMapping
    public Json add(@RequestBody String body) {

        String oper = "add role";
        SysRole role = JSON.parseObject(body, SysRole.class);

        if (StringUtils.isBlank(role.getRval())) {
            return Json.fail(oper, "权限值不能为空");
        }

        SysRole roleDB = roleService.selectOne(new EntityWrapper<SysRole>().eq("rval", role.getRval()));
        if (roleDB != null) {
            return Json.fail(oper, "角色值已存在：" + role.getRval());
        }

        //保存新用户数据
        role.setCreated(new Date());
        boolean success = roleService.insert(role);
        return Json.result(oper, success)
                .data("rid",role.getRid())
                .data("created",role.getCreated());
    }

    @PermInfo("删除角色")
    @RequiresPermissions("a:role:delete")
    @DeleteMapping
    public Json delete(@RequestBody String body) {

        String oper = "delete role";
        log.info("{}, body: {}", oper, body);

        JSONObject jsonObj = JSON.parseObject(body);
        String rid = jsonObj.getString("rid");

        if (StringUtils.isBlank(rid)) {
            return Json.fail(oper, "无法删除角色：参数为空（角色id）");
        }

        boolean success = roleService.deleteById(rid);
        return Json.result(oper, success);
    }

    /**
     * 分页查询出所有角色
     * @param body
     * @return
     */
    @PermInfo("查询出所有角色")
    @RequiresPermissions("a:role:query")  //添加了注解，需要有权限才能调用！！！
    @PostMapping("/query")
    public Json query(@RequestBody String body) {

        String oper = "query role";
        log.info("{}, body: {}", oper, body);

        JSONObject json = JSON.parseObject(body);
        String rname = json.getString("rname"); //获取json字符串的String类型的值

        int current = json.getIntValue("current");//获取json字符串的int类型的值
        int size = json.getIntValue("size");
        if (current == 0) {  //传递空值，第一页
            current = 1;
        }
        if (size == 0) {  //每页显示10条数据
            size = 10;
        }

        Wrapper<SysRole> queryParams = new EntityWrapper<>();  //封装条件对象
        queryParams.orderBy("created", false); //按照创建时间排序 不
        queryParams.orderBy("updated", false);//按照更新时间排序 不
        if (StringUtils.isNotBlank(rname)) {
            queryParams.like("rname", rname);  //条件 角色名称
        }
        Page<SysRole> page = roleService.selectPage(new Page<>(current, size), queryParams); //分页查询，结果显示的是角色表的信息
        return Json.succ(oper).data("page", page);
    }

    /**
     * 修改角色信息
     * @param body
     * @return
     */
    @PermInfo("编辑角色信息")
    @RequiresPermissions("a:role:update")  //添加了注解，需要有权限才能调用！！！
    @PatchMapping("/update")
    public Json update(@RequestBody String body) {

        String oper = "update role";
        log.info("{}, body: {}", oper, body);

        SysRole role = JSON.parseObject(body, SysRole.class); //json字符串转换为实体类对象SysRole
        if (StringUtils.isBlank(role.getRid())) {
            return Json.fail(oper, "无法更新角色：参数为空（角色id）");
        }
        role.setUpdated(new Date());
        boolean success = roleService.updateById(role);
        return Json.result(oper, success).data("updated", role.getUpdated());
    }

    /**
     * 修改角色权限
     * @param vo
     * @return
     */
    @PermInfo("修改角色权限")
    @RequiresPermissions("a:role:perm:update")  //添加了注解，需要有权限才能调用！！！
    @PatchMapping("/perm")
    public Json updateRolePerm(@RequestBody UpdateRolePermVo vo) {

        String oper = "update role's permissions";

        if (StringUtils.isBlank(vo.getRid())) {
            return Json.fail(oper, "无法更新角色的权限：参数为空（角色id）");
        }
        if (vo.getPtype()==null){
            return Json.fail(oper, "无法更新角色的权限：参数为空（权限类型）");
        }
        final String rid = vo.getRid();  //TODO 为什么要使用final???
        final Integer ptype = vo.getPtype();
        final List<String> pvals = vo.getPvals();//TODO 获取所有权限值，前端传过来的是数组？这里是集合？？

        Wrapper<SysRolePerm> deleteRelationParam = new EntityWrapper<SysRolePerm>().eq("role_id", rid).eq("perm_type", ptype);
        boolean deleteRelationSucc = rolePermService.delete(deleteRelationParam);  //删除角色权限表的一条纪录
        if (!deleteRelationSucc) {
            return Json.fail(oper, "无法解除原来的角色-权限关系");
        }

        if (!pvals.isEmpty()){
            List<SysRolePerm> list = vo.getPvals().stream().map(pval -> new SysRolePerm(rid, pval,ptype)).collect(Collectors.toList());
            boolean addSucc = rolePermService.insertBatch(list);
            return Json.result(oper, addSucc);
        }
        return Json.succ(oper);
    }

    /**
     * 添加角色权限
     * @param body
     * @return
     */
    @PermInfo("添加角色权限")
    @RequiresPermissions("a:role:perm:add")  //添加了注解，需要有权限才能调用！！！
    @PostMapping("/perm")
    public Json addPerm(@RequestBody String body){
        String oper = "add role's permissions";

        JSONObject json = JSON.parseObject(body);
        String rid = json.getString("rid");
        Integer ptype = json.getInteger("ptype");
        String pval = json.getString("pval");

        boolean success = rolePermService.insert(new SysRolePerm(rid, pval, ptype));
        return Json.result(oper,success);
    }

    /**
     * 删除角色权限
     * @param body
     * @return
     */
    @PermInfo("删除角色权限")
    @RequiresPermissions("a:role:perm:delete")  //添加了注解，需要有权限才能调用！！！
    @DeleteMapping("/perm")
    public Json deletePerm(@RequestBody String body){
        String oper = "delete role's permissions";

        JSONObject json = JSON.parseObject(body);
        String rid = json.getString("rid");
        Integer ptype = json.getInteger("ptype");
        String pval = json.getString("pval");

        Wrapper<SysRolePerm> deleteParam = new EntityWrapper<SysRolePerm>()
                .eq("role_id", rid)
                .eq("perm_val", pval)
                .eq("perm_type", ptype);
        boolean success = rolePermService.delete(deleteParam);
        return Json.succ(oper,success);
    }

    /**
     * 查询用户角色权限
     * @param rid
     * @return
     */
    @PermInfo("查找角色权限")
    @RequiresPermissions("a:role:perm:find")  //添加了注解，需要有权限才能调用！！！
    @GetMapping("/{rid}/perms")
    public Json findRolePerms(@PathVariable String rid){
        String oper = "find role perms";
        log.info("{}, rid: {}", oper, rid);
        if (StringUtils.isBlank(rid)){
            return Json.fail(oper, "无法查询当前角色的权限值：参数为空（角色id）");
        }
        SysRole role = roleService.selectById(rid);
        List<SysPerm> perms = permService.getPermsByRoleId(rid);
        Map<Integer, List<SysPerm>> permMap = perms.stream().collect(Collectors.groupingBy(SysPerm::getPtype));

        List<String> menuPvals = permMap.getOrDefault(PermType.MENU, new ArrayList<>()).stream()
                .filter(perm->perm.getLeaf()==true).map(SysPerm::getPval).collect(Collectors.toList());

        List<String> btnPvals = permMap.getOrDefault(PermType.BUTTON, new ArrayList<>()).stream()
                .map(SysPerm::getPval).collect(Collectors.toList());

        List<String> apiPvals = permMap.getOrDefault(PermType.API, new ArrayList<>()).stream()
                .filter(perm->perm.getLeaf()==true).map(SysPerm::getPval).collect(Collectors.toList());

        return Json.succ(oper)
                .data("role",role)
                .data("menuPvals",menuPvals)
                .data("btnPvals",btnPvals)
                .data("apiPvals",apiPvals);
    }

}
