package com.tdpro.entity;

import java.io.Serializable;

public class PSms implements Serializable {
    private Long id;

    private String smsName;

    private String smsPassword;

    private String smsHttpUrl;

    private String smsSigna;

    private Integer type;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSmsName() {
        return smsName;
    }

    public void setSmsName(String smsName) {
        this.smsName = smsName == null ? null : smsName.trim();
    }

    public String getSmsPassword() {
        return smsPassword;
    }

    public void setSmsPassword(String smsPassword) {
        this.smsPassword = smsPassword == null ? null : smsPassword.trim();
    }

    public String getSmsHttpUrl() {
        return smsHttpUrl;
    }

    public void setSmsHttpUrl(String smsHttpUrl) {
        this.smsHttpUrl = smsHttpUrl == null ? null : smsHttpUrl.trim();
    }

    public String getSmsSigna() {
        return smsSigna;
    }

    public void setSmsSigna(String smsSigna) {
        this.smsSigna = smsSigna == null ? null : smsSigna.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", smsName=").append(smsName);
        sb.append(", smsPassword=").append(smsPassword);
        sb.append(", smsHttpUrl=").append(smsHttpUrl);
        sb.append(", smsSigna=").append(smsSigna);
        sb.append(", type=").append(type);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}