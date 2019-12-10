package com.tdpro.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PUserMonthKnot implements Serializable {
    private Long id;

    private Long uid;

    private BigDecimal knotPrice;

    private Integer year;

    private Integer month;

    private Integer newOrderNum;

    private BigDecimal newOrderPrice;

    private Date createTime;

    private BigDecimal rate;

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

    public BigDecimal getKnotPrice() {
        return knotPrice;
    }

    public void setKnotPrice(BigDecimal knotPrice) {
        this.knotPrice = knotPrice;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getNewOrderNum() {
        return newOrderNum;
    }

    public void setNewOrderNum(Integer newOrderNum) {
        this.newOrderNum = newOrderNum;
    }

    public BigDecimal getNewOrderPrice() {
        return newOrderPrice;
    }

    public void setNewOrderPrice(BigDecimal newOrderPrice) {
        this.newOrderPrice = newOrderPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uid=").append(uid);
        sb.append(", knotPrice=").append(knotPrice);
        sb.append(", year=").append(year);
        sb.append(", month=").append(month);
        sb.append(", newOrderNum=").append(newOrderNum);
        sb.append(", newOrderPrice=").append(newOrderPrice);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}