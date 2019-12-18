package com.tdpro.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class PAdmin implements Serializable {
    private Long id;

    private String phone;

    private String name;

    private Integer six;

    private String password;

    private Long rid;

    private Integer state;

    private Date disableTime;

    private Date liftingTime;

    private Date createTime;

    private String oldPassword;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSix() {
        return six;
    }

    public void setSix(Integer six) {
        this.six = six;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(Date disableTime) {
        this.disableTime = disableTime;
    }

    public Date getLiftingTime() {
        return liftingTime;
    }

    public void setLiftingTime(Date liftingTime) {
        this.liftingTime = liftingTime;
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
        sb.append(", phone=").append(phone);
        sb.append(", name=").append(name);
        sb.append(", six=").append(six);
        sb.append(", password=").append(password);
        sb.append(", rid=").append(rid);
        sb.append(", state=").append(state);
        sb.append(", disableTime=").append(disableTime);
        sb.append(", liftingTime=").append(liftingTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}