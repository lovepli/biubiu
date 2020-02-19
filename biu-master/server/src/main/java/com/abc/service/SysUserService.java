package com.abc.service;

import com.abc.entity.SysUser;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

public interface SysUserService extends IService<SysUser> {

    /**
     * 根据昵称模糊查询所有系统用户 分页
     * @param page
     * @param nick  昵称
     * @return
     */
    Page<SysUser> queryUserIncludeRoles(Page page, String nick);

}
