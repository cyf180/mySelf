package com.tdpro.entity;

import java.io.Serializable;
import java.util.Date;

public class PRole implements Serializable {
    private Long id;

    private String roleRank;

    private String roleName;

    private Date roleTime;

    private Integer roleStatus;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleRank() {
        return roleRank;
    }

    public void setRoleRank(String roleRank) {
        this.roleRank = roleRank == null ? null : roleRank.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public Date getRoleTime() {
        return roleTime;
    }

    public void setRoleTime(Date roleTime) {
        this.roleTime = roleTime;
    }

    public Integer getRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(Integer roleStatus) {
        this.roleStatus = roleStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", roleRank=").append(roleRank);
        sb.append(", roleName=").append(roleName);
        sb.append(", roleTime=").append(roleTime);
        sb.append(", roleStatus=").append(roleStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}