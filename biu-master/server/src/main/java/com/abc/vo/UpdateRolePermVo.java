package com.abc.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 更新角色权限  参数对象
 */
public class UpdateRolePermVo implements Serializable{

    private String rid;//角色ID
    private Integer ptype;//权限类型
    private List<String> pvals = new ArrayList<>(); //权限值

    public Integer getPtype() {
        return ptype;
    }

    public void setPtype(Integer ptype) {
        this.ptype = ptype;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public List<String> getPvals() {
        return pvals;
    }

    public void setPvals(List<String> pvals) {
        this.pvals = pvals;
    }
}
