package com.tdpro.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PUserPay implements Serializable {
    private Long id;

    private String payNo;

    private Long uid;

    private BigDecimal payPrice;

    private Integer payState;

    private String backNo;

    private BigDecimal backPrice;

    private Date createTime;

    private Date backTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo == null ? null : payNo.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getBackNo() {
        return backNo;
    }

    public void setBackNo(String backNo) {
        this.backNo = backNo == null ? null : backNo.trim();
    }

    public BigDecimal getBackPrice() {
        return backPrice;
    }

    public void setBackPrice(BigDecimal backPrice) {
        this.backPrice = backPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBackTime() {
        return backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", payNo=").append(payNo);
        sb.append(", uid=").append(uid);
        sb.append(", payPrice=").append(payPrice);
        sb.append(", payState=").append(payState);
        sb.append(", backNo=").append(backNo);
        sb.append(", backPrice=").append(backPrice);
        sb.append(", createTime=").append(createTime);
        sb.append(", backTime=").append(backTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}