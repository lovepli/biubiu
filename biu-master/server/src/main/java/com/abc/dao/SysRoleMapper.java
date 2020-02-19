package com.abc.dao;

import com.abc.entity.SysRole;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 查询用户角色
     * @param userId
     * @return
     */
    List<SysRole> getRolesByUserId(@Param("userId") String userId);

    /**
     * 查找系统用户的角色
     * @param userId
     * @return
     */
    List<String> getRoleIdsByUserId(@Param("userId") String userId);

    /**
     * 集合中是否包含管理员的角色值rid
     * @param rids
     * @param rval
     * @return
     */
    Boolean checkRidsContainRval(@Param("rids")List<String> rids,@Param("rval")String rval);

    /**
     * 查询是否是管理员 TODO SQL的写法？？？
     * @param uid
     * @param rval
     * @return
     */
    Boolean checkUidContainRval(@Param("uid")String uid,@Param("rval")String rval);

}
