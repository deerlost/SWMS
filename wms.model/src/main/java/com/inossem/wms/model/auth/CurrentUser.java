package com.inossem.wms.model.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.inossem.wms.model.vo.RelUserStockLocationVo;

public class CurrentUser extends User implements Serializable, UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 库存地点格式为 工厂id- 库存地点id
	private List<String> locationList = new ArrayList<String>();
	private List<Integer> roleCode;
	private List<RelUserStockLocationVo> relUserStockLocationVo;

	public List<RelUserStockLocationVo> getInventoryLocation() {
		return relUserStockLocationVo;
	}

	public void setInventoryLocation(List<RelUserStockLocationVo> inventoryLocation) {
		this.relUserStockLocationVo = inventoryLocation;
	}

	public CurrentUser() {
	}

	public CurrentUser(User u) {
		this.setCardNumber(u.getCardNumber());
		this.setCreateTime(u.getCreateTime());
		this.setDisplayOrder(u.getDisplayOrder());
		this.setEmployerNumber(u.getEmployerNumber());
		this.setFailAttempts(u.getFailAttempts());
		this.setIsLocked(u.getIsLocked());
		this.setLastFailAttempt(u.getLastFailAttempt());
		this.setLastLogin(u.getLastLogin());
		this.setLoginenable(u.getLoginenable());
		this.setMail(u.getMail());
		this.setMobile(u.getMobile());
		this.setModifyTime(u.getModifyTime());
		this.setOrgId(u.getOrgId());
		this.setOrgName(u.getOrgName());
		this.setPassword(u.getPassword());
		this.setSex(u.getSex());
		this.setTitle(u.getTitle());
		this.setUserId(u.getUserId());
		this.setUserName(u.getUserName());
		this.setUserType(u.getUserType());
		this.setCorpCode(u.getCorpCode());
		this.setCorpId(u.getCorpId());
		this.setCorpName(u.getCorpName());
		this.setFtyCode(u.getFtyCode());
		this.setFtyName(u.getFtyName());
		this.setFtyId(u.getFtyId());
		this.setBoardId(u.getBoardId());
		this.setIsSyn(u.getIsSyn());
		this.setDepartment(u.getDepartment());
		/*
		 * switch (u.getUserId()) { case "a0001":
		 * this.getLGORT().add("3000-3000"); this.getLGORT().add("3000-3001");
		 * // this.getLGORT().add("3020-0001"); //
		 * this.getLGORT().add("3020-0088"); //
		 * this.getLGORT().add("3020-0100"); //
		 * this.getLGORT().add("3020-0200"); //
		 * this.getLGORT().add("3020-3020"); //
		 * this.getLGORT().add("3020-3021"); //
		 * this.getLGORT().add("3020-3022"); //
		 * this.getLGORT().add("3020-3023"); //
		 * this.getLGORT().add("3020-3024"); //
		 * this.getLGORT().add("3020-3025"); //
		 * this.getLGORT().add("3020-3026"); //
		 * this.getLGORT().add("3020-3027"); //
		 * this.getLGORT().add("3020-3028"); //
		 * this.getLGORT().add("3020-3029"); //
		 * this.getLGORT().add("3020-302A"); //
		 * this.getLGORT().add("3020-302B"); //
		 * this.getLGORT().add("3020-302E"); //
		 * this.getLGORT().add("3020-302F"); //
		 * this.getLGORT().add("3020-302G"); //
		 * this.getLGORT().add("3020-302H"); //
		 * this.getLGORT().add("3020-302L"); break; case "a0003": //
		 * this.getLGORT().add("1000-0001"); this.getLGORT().add("2120-2013");
		 * this.getLGORT().add("2120-2014"); this.getLGORT().add("2120-2015");
		 * this.getLGORT().add("2120-2016"); break; // case "a0004": //
		 * this.getLGORT().add("1000-0001"); // break; default:
		 * this.getLGORT().add("1000-0001"); this.getLGORT().add("2000-0001");
		 * this.getLGORT().add("2000-0002"); this.getLGORT().add("2000-0003");
		 * this.getLGORT().add("2000-0004"); // this.setBukrs("1000");
		 * this.getLGORT().add("100R-0001"); this.getLGORT().add("100R-0003");
		 * this.getLGORT().add("100R-S001"); // this.setBukrs("100R"); //
		 * this.getLGORT().add("A001-0001"); break; }
		 */

	}

	public List<String> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<String> locationList) {
		this.locationList = locationList;
	}

	public List<Integer> getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(List<Integer> roleCode) {
		this.roleCode = roleCode;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 从数据中获取用户全部的role
		List<Integer> roles = this.roleCode;

		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

		for (int i = 0; i < roles.size(); i++) {
			authorities.add(new SimpleGrantedAuthority(roles.get(i).toString()));
		}
		return authorities;
	}

	@Override
	public String getUsername() {
		return this.getUserId();
	}

	public String getDisplayUsername() {
		return this.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
