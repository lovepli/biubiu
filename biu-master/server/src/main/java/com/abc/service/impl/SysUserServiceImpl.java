package com.abc.service.impl;

import com.abc.dao.SysUserMapper;
import com.abc.entity.SysUser;
import com.abc.service.SysUserService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    /**
     * 根据昵称模糊查询所有系统用户 分页
     * @param page
     * @param nick  昵称
     * @return
     */
    @Override
    public Page<SysUser> queryUserIncludeRoles(Page page, String nick) {
        return page.setRecords(baseMapper.selectUserIncludeRoles(page,nick));
    }

}
