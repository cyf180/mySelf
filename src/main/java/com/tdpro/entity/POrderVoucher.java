package com.tdpro.entity;

import java.io.Serializable;

public class POrderVoucher implements Serializable {
    private Long id;

    private Long orderId;

    private Long uid;

    private Long userVoucherId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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
        sb.append(", orderId=").append(orderId);
        sb.append(", uid=").append(uid);
        sb.append(", userVoucherId=").append(userVoucherId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}