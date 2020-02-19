package com.abc.service;

import com.abc.entity.SysRole;
import com.abc.vo.AuthVo;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 获取用户的角色
     * @param userId
     * @return
     */
    Set<AuthVo> getRolesByUserId(String userId);

    /**
     * 查找系统用户的角色
     * @param userId
     * @return
     */
    List<String> getRoleIdsByUserId(String userId);

    /**
     * 集合中是否包含管理员的角色值rid
     * @param rids
     * @param rval
     * @return
     */
    boolean checkRidsContainRval(List<String> rids, String rval);

    /**
     *查询是否是管理员
     * @param uid
     * @param rval
     * @return
     */
    boolean checkUidContainRval(String uid, String rval);

}
