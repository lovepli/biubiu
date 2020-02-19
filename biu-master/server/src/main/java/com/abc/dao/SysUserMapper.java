package com.abc.dao;

import com.abc.entity.SysUser;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据昵称模糊查询所有系统用户 分页
     * @param page
     * @param nick
     * @return
     */
    List<SysUser> selectUserIncludeRoles(Pagination page, @Param("nick")String nick);

}
