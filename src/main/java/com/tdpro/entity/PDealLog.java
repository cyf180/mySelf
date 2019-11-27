package com.tdpro.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PDealLog implements Serializable {
    private Long id;

    private String dealName;

    private Long orderId;

    private BigDecimal dealAmount;

    private BigDecimal balance;

    private Long uid;

    private Long bUid;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName == null ? null : dealName.trim();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(BigDecimal dealAmount) {
        this.dealAmount = dealAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getbUid() {
        return bUid;
    }

    public void setbUid(Long bUid) {
        this.bUid = bUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", dealName=").append(dealName);
        sb.append(", orderId=").append(orderId);
        sb.append(", dealAmount=").append(dealAmount);
        sb.append(", balance=").append(balance);
        sb.append(", uid=").append(uid);
        sb.append(", bUid=").append(bUid);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}