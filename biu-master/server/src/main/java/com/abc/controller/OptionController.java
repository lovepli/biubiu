package com.abc.controller;

import com.abc.annotation.PermInfo;
import com.abc.entity.SysRole;
import com.abc.service.SysRoleService;
import com.abc.vo.Json;
import com.abc.vo.Option;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * created by CaiBaoHong at 2018/4/17 16:41<br>
 *     角色选项，相当于下拉框查询出角色列表
 */
@PermInfo(value = "选项模块", pval = "a:option")  //自定义权限注解
@RestController
@RequestMapping("/option")
public class OptionController {

    private static final Logger log = LoggerFactory.getLogger(OptionController.class);

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 查询角色选择下拉列表
     * @return
     */
    @GetMapping("/role")
    public Json listRoleOptions() {
        String oper = "list role options";
        log.info(oper);

        EntityWrapper<SysRole> params = new EntityWrapper<>();
        params.setSqlSelect("rid,rname,rval");  //TODO 传入角色id,角色名，角色值作为参数查询条件，使用方法类似mybatis的Example类的方法

        List<SysRole> list = sysRoleService.selectList(params);
        log.info("查询出的角色列表 {}",list);
        List<Option> options = list.stream().map(obj -> new Option(obj.getRid(), obj.getRname(),obj.getRval())).collect(Collectors.toList());
        return Json.succ(oper, "options", options);
    }


}
