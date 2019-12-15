package com.tdpro.entity;

import java.io.Serializable;
import java.util.Date;

public class PVoucherIssueLog implements Serializable {
    private Long id;

    private Long uid;

    private Long payUid;

    private Long voucherId;

    private String voucherName;

    private Integer type;

    private Date createTime;

    private Long userVoucherId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getPayUid() {
        return payUid;
    }

    public void setPayUid(Long payUid) {
        this.payUid = payUid;
    }

    public Long getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName == null ? null : voucherName.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUserVoucherId() {
        return userVoucherId;
    }

    public void setUserVoucherId(Long userVoucherId) {
        this.userVoucherId = userVoucherId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uid=").append(uid);
        sb.append(", payUid=").append(payUid);
        sb.append(", voucherId=").append(voucherId);
        sb.append(", voucherName=").append(voucherName);
        sb.append(", type=").append(type);
        sb.append(", createTime=").append(createTime);
        sb.append(", userVoucherId=").append(userVoucherId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}