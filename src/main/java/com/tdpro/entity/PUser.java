package com.tdpro.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PUser implements Serializable {
    private Long id;
    @ApiModelProperty(value = "是否是会员(0:不是 1:是)")
    private Integer isUser;
    @ApiModelProperty(value = "手机号")
    private String phone;
    private String payPassword;
    @ApiModelProperty(value = "-1:禁用 0:正常")
    private Integer state;
    @ApiModelProperty(value = "推荐人uid")
    private Long strawUid;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "真实姓名")
    private String name;
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
    @ApiModelProperty(value = "积分")
    private BigDecimal integral;
    @ApiModelProperty(value = "总积分")
    private BigDecimal totalIntegral;
    @ApiModelProperty(value = "开户行")
    private String bankName;
    @ApiModelProperty(value = "开户支行")
    private String bankBranch;
    @ApiModelProperty(value = "银行卡号")
    private String bankCard;
    @ApiModelProperty(value = "身份证")
    private String idCard;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "禁用时间")
    private Date disableTime;
    @ApiModelProperty(value = "解禁时间")
    private Date uncoilTime;
    private String strawPath;
    @ApiModelProperty(value = "结算利率id")
    private Long knotId;
    @ApiModelProperty(value = "结算总金额")
    private BigDecimal knotAmount;
    @ApiModelProperty(value = "团队单品数")
    private Integer teamOneNum;
    @ApiModelProperty(value = "团队套装数")
    private Integer teamSuitNum;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsUser() {
        return isUser;
    }

    public void setIsUser(Integer isUser) {
        this.isUser = isUser;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword == null ? null : payPassword.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getStrawUid() {
        return strawUid;
    }

    public void setStrawUid(Long strawUid) {
        this.strawUid = strawUid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public BigDecimal getTotalIntegral() {
        return totalIntegral;
    }

    public void setTotalIntegral(BigDecimal totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch == null ? null : bankBranch.trim();
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard == null ? null : bankCard.trim();
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(Date disableTime) {
        this.disableTime = disableTime;
    }

    public Date getUncoilTime() {
        return uncoilTime;
    }

    public void setUncoilTime(Date uncoilTime) {
        this.uncoilTime = uncoilTime;
    }

    public String getStrawPath() {
        return strawPath;
    }

    public void setStrawPath(String strawPath) {
        this.strawPath = strawPath == null ? null : strawPath.trim();
    }

    public Long getKnotId() {
        return knotId;
    }

    public void setKnotId(Long knotId) {
        this.knotId = knotId;
    }

    public BigDecimal getKnotAmount() {
        return knotAmount;
    }

    public void setKnotAmount(BigDecimal knotAmount) {
        this.knotAmount = knotAmount;
    }

    public Integer getTeamOneNum() {
        return teamOneNum;
    }

    public void setTeamOneNum(Integer teamOneNum) {
        this.teamOneNum = teamOneNum;
    }

    public Integer getTeamSuitNum() {
        return teamSuitNum;
    }

    public void setTeamSuitNum(Integer teamSuitNum) {
        this.teamSuitNum = teamSuitNum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", isUser=").append(isUser);
        sb.append(", phone=").append(phone);
        sb.append(", payPassword=").append(payPassword);
        sb.append(", state=").append(state);
        sb.append(", strawUid=").append(strawUid);
        sb.append(", nickName=").append(nickName);
        sb.append(", name=").append(name);
        sb.append(", balance=").append(balance);
        sb.append(", integral=").append(integral);
        sb.append(", totalIntegral=").append(totalIntegral);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankBranch=").append(bankBranch);
        sb.append(", bankCard=").append(bankCard);
        sb.append(", idCard=").append(idCard);
        sb.append(", createTime=").append(createTime);
        sb.append(", disableTime=").append(disableTime);
        sb.append(", uncoilTime=").append(uncoilTime);
        sb.append(", strawPath=").append(strawPath);
        sb.append(", knotId=").append(knotId);
        sb.append(", knotAmount=").append(knotAmount);
        sb.append(", teamOneNum=").append(teamOneNum);
        sb.append(", teamSuitNum=").append(teamSuitNum);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}