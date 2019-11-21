package com.tdpro.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class PKnotConfig implements Serializable {
    private Long id;

    private Integer smallValue;

    private Integer bigValue;

    private BigDecimal rate;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSmallValue() {
        return smallValue;
    }

    public void setSmallValue(Integer smallValue) {
        this.smallValue = smallValue;
    }

    public Integer getBigValue() {
        return bigValue;
    }

    public void setBigValue(Integer bigValue) {
        this.bigValue = bigValue;
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
        sb.append(", smallValue=").append(smallValue);
        sb.append(", bigValue=").append(bigValue);
        sb.append(", rate=").append(rate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}