package com.inossem.wms.model.vo;

import java.util.Date;

import com.inossem.wms.model.wf.WfReceiptUser;

public class WfReceiptUserVo extends WfReceiptUser {

	private String typeName;

	public String getTypeName() {
		return typeName;
	}

	public void setType_name(String typeName) {
		this.typeName = typeName;
	}

	private String sp_user_id;

	public String getSp_user_id() {
		return sp_user_id;
	}

	public void setSp_user_id(String sp_user_id) {
		this.sp_user_id = sp_user_id == null ? null : sp_user_id.trim();
	}

	private String orgName;

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName == null ? null : orgName.trim();
	}

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUser_name(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	private Date create_time;

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	private String zpiid;

	public String getZpiid() {
		return zpiid;
	}

	public void setZpiid(String zpiid) {
		this.zpiid = zpiid == null ? null : zpiid.trim();
	}

	private String corpName;

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName == null ? null : corpName.trim();
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment == null ? null : comment.trim();
	}
}
