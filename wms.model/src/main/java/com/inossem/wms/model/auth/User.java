package com.inossem.wms.model.auth;

import java.util.Date;

import com.inossem.wms.model.BaseEntity;

public class User extends BaseEntity {
	private String departmentName;

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * 公司描述
	 */
	private String corpName;

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	/**
	 * 工厂描述
	 */
	private String ftyName;

	public String getFtyName() {
		return ftyName;
	}

	public void setFtyName(String ftyName) {
		this.ftyName = ftyName;
	}

	/**
	 * 角色ID
	 */
	private int roleId;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	// 是否为伊泰验证用户
	public int isSyn;

	public int getIsSyn() {
		return isSyn;
	}

	public void setIsSyn(int isSyn) {
		this.isSyn = isSyn;
	}

	private String userId;

	private String userName;

	private String password;

	private Integer sex;

	private String employerNumber;

	private String cardNumber;

	private String mobile;

	private String mail;

	private String title;

	private String userType;

	private Date createTime;

	private Date modifyTime;

	private String orgId;

	private String orgName;

	private String displayOrder;

	private String loginenable;

	private Date lastLogin;

	private Integer failAttempts;

	private Date lastFailAttempt;

	private Boolean isLocked;

	private String corpCode;

	private String ftyCode;

	private Integer boardId;

	private int department;

	public Integer getCorpId() {
		return corpId;
	}

	public void setCorpId(Integer corpId) {
		this.corpId = corpId;
	}

	public Integer getFtyId() {
		return ftyId;
	}

	public void setFtyId(Integer ftyId) {
		this.ftyId = ftyId;
	}

	private Integer corpId;

	private Integer ftyId;

	public int getDepartment() {
		return department;
	}

	public void setDepartment(int department) {
		this.department = department;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getEmployerNumber() {
		return employerNumber;
	}

	public void setEmployerNumber(String employerNumber) {
		this.employerNumber = employerNumber == null ? null : employerNumber.trim();
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber == null ? null : cardNumber.trim();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail == null ? null : mail.trim();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType == null ? null : userType.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId == null ? null : orgId.trim();
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName == null ? null : orgName.trim();
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder == null ? null : displayOrder.trim();
	}

	public String getLoginenable() {
		return loginenable;
	}

	public void setLoginenable(String loginenable) {
		this.loginenable = loginenable == null ? null : loginenable.trim();
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Integer getFailAttempts() {
		return failAttempts;
	}

	public void setFailAttempts(Integer failAttempts) {
		this.failAttempts = failAttempts;
	}

	public Date getLastFailAttempt() {
		return lastFailAttempt;
	}

	public void setLastFailAttempt(Date lastFailAttempt) {
		this.lastFailAttempt = lastFailAttempt;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode == null ? null : corpCode.trim();
	}

	public String getFtyCode() {
		return ftyCode;
	}

	public void setFtyCode(String ftyCode) {
		this.ftyCode = ftyCode == null ? null : ftyCode.trim();
	}

	public Integer getBoardId() {
		return boardId;
	}

	public void setBoardId(Integer boardId) {
		this.boardId = boardId;
	}
}