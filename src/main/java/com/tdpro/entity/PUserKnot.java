package com.tdpro.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PUserKnot implements Serializable {
    private Long id;

    private Long uid;

    private Long payUid;

    private Long orderId;

    private Long monthId;

    private BigDecimal knotPrice;

    private BigDecimal payPrice;

    private Integer knotType;

    private Date createTime;

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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getMonthId() {
        return monthId;
    }

    public void setMonthId(Long monthId) {
        this.monthId = monthId;
    }

    public BigDecimal getKnotPrice() {
        return knotPrice;
    }

    public void setKnotPrice(BigDecimal knotPrice) {
        this.knotPrice = knotPrice;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    public Integer getKnotType() {
        return knotType;
    }

    public void setKnotType(Integer knotType) {
        this.knotType = knotType;
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
        sb.append(", uid=").append(uid);
        sb.append(", payUid=").append(payUid);
        sb.append(", orderId=").append(orderId);
        sb.append(", monthId=").append(monthId);
        sb.append(", knotPrice=").append(knotPrice);
        sb.append(", payPrice=").append(payPrice);
        sb.append(", knotType=").append(knotType);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}