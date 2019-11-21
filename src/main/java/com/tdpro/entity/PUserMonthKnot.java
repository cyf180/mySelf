package com.tdpro.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PUserMonthKnot implements Serializable {
    private Long id;

    private Long uid;

    private BigDecimal knotPrice;

    private Integer knotOrderNum;

    private Date knotTime;

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

    public BigDecimal getKnotPrice() {
        return knotPrice;
    }

    public void setKnotPrice(BigDecimal knotPrice) {
        this.knotPrice = knotPrice;
    }

    public Integer getKnotOrderNum() {
        return knotOrderNum;
    }

    public void setKnotOrderNum(Integer knotOrderNum) {
        this.knotOrderNum = knotOrderNum;
    }

    public Date getKnotTime() {
        return knotTime;
    }

    public void setKnotTime(Date knotTime) {
        this.knotTime = knotTime;
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
        sb.append(", knotPrice=").append(knotPrice);
        sb.append(", knotOrderNum=").append(knotOrderNum);
        sb.append(", knotTime=").append(knotTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}