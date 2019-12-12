package com.tdpro.entity;

import java.io.Serializable;
import java.util.Date;

public class PPayConfig implements Serializable {
    private Long id;

    private String name;

    private String desc;

    private String mchId;

    private String appId;

    private String paySecret;

    private Byte channel;

    private Byte payType;

    private Byte status;

    private Byte sort;

    private String certPath;

    private String backPath;

    private String userBackPath;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId == null ? null : mchId.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getPaySecret() {
        return paySecret;
    }

    public void setPaySecret(String paySecret) {
        this.paySecret = paySecret == null ? null : paySecret.trim();
    }

    public Byte getChannel() {
        return channel;
    }

    public void setChannel(Byte channel) {
        this.channel = channel;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getSort() {
        return sort;
    }

    public void setSort(Byte sort) {
        this.sort = sort;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath == null ? null : certPath.trim();
    }

    public String getBackPath() {
        return backPath;
    }

    public void setBackPath(String backPath) {
        this.backPath = backPath == null ? null : backPath.trim();
    }

    public String getUserBackPath() {
        return userBackPath;
    }

    public void setUserBackPath(String userBackPath) {
        this.userBackPath = userBackPath;
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
        sb.append(", name=").append(name);
        sb.append(", desc=").append(desc);
        sb.append(", mchId=").append(mchId);
        sb.append(", appId=").append(appId);
        sb.append(", paySecret=").append(paySecret);
        sb.append(", channel=").append(channel);
        sb.append(", payType=").append(payType);
        sb.append(", status=").append(status);
        sb.append(", sort=").append(sort);
        sb.append(", certPath=").append(certPath);
        sb.append(", backPath=").append(backPath);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}