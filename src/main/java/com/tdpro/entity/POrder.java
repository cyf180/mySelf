package com.tdpro.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class POrder implements Serializable {
    private Long id;

    private String orderNo;

    private Long uid;

    private Long goodsId;

    private Integer zoneType;

    private Integer isSuit;

    private Integer state;

    private String goodsName;

    private BigDecimal totalPrice;

    private BigDecimal realPrice;

    private Integer number;

    private Integer payType;

    private String receiptName;

    private String receiptPhone;

    private String receiptSite;

    private String suiteName;

    private Integer isDel;

    private Date createTime;

    private Date payTime;

    private String wxOrderNo;

    private BigDecimal callbackPrice;

    private String logisticsName;

    private String logisticsNo;

    private Date seendTime;

    private String userNote;

    private String backNote;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getZoneType() {
        return zoneType;
    }

    public void setZoneType(Integer zoneType) {
        this.zoneType = zoneType;
    }

    public Integer getIsSuit() {
        return isSuit;
    }

    public void setIsSuit(Integer isSuit) {
        this.isSuit = isSuit;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName == null ? null : receiptName.trim();
    }

    public String getReceiptPhone() {
        return receiptPhone;
    }

    public void setReceiptPhone(String receiptPhone) {
        this.receiptPhone = receiptPhone == null ? null : receiptPhone.trim();
    }

    public String getReceiptSite() {
        return receiptSite;
    }

    public void setReceiptSite(String receiptSite) {
        this.receiptSite = receiptSite == null ? null : receiptSite.trim();
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName == null ? null : suiteName.trim();
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getWxOrderNo() {
        return wxOrderNo;
    }

    public void setWxOrderNo(String wxOrderNo) {
        this.wxOrderNo = wxOrderNo == null ? null : wxOrderNo.trim();
    }

    public BigDecimal getCallbackPrice() {
        return callbackPrice;
    }

    public void setCallbackPrice(BigDecimal callbackPrice) {
        this.callbackPrice = callbackPrice;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName == null ? null : logisticsName.trim();
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo == null ? null : logisticsNo.trim();
    }

    public Date getSeendTime() {
        return seendTime;
    }

    public void setSeendTime(Date seendTime) {
        this.seendTime = seendTime;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote == null ? null : userNote.trim();
    }

    public String getBackNote() {
        return backNote;
    }

    public void setBackNote(String backNote) {
        this.backNote = backNote == null ? null : backNote.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", uid=").append(uid);
        sb.append(", goodsId=").append(goodsId);
        sb.append(", zoneType=").append(zoneType);
        sb.append(", isSuit=").append(isSuit);
        sb.append(", state=").append(state);
        sb.append(", goodsName=").append(goodsName);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", realPrice=").append(realPrice);
        sb.append(", number=").append(number);
        sb.append(", payType=").append(payType);
        sb.append(", receiptName=").append(receiptName);
        sb.append(", receiptPhone=").append(receiptPhone);
        sb.append(", receiptSite=").append(receiptSite);
        sb.append(", suiteName=").append(suiteName);
        sb.append(", isDel=").append(isDel);
        sb.append(", createTime=").append(createTime);
        sb.append(", payTime=").append(payTime);
        sb.append(", wxOrderNo=").append(wxOrderNo);
        sb.append(", callbackPrice=").append(callbackPrice);
        sb.append(", logisticsName=").append(logisticsName);
        sb.append(", logisticsNo=").append(logisticsNo);
        sb.append(", seendTime=").append(seendTime);
        sb.append(", userNote=").append(userNote);
        sb.append(", backNote=").append(backNote);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}