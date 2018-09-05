package com.inossem.wms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {
	// @Value("#{serverConfigProperties.isldap}")
	// private String isldap;
	//
	// public String getIsldap() {
	// return isldap;
	// }
	//
	// public void setIsldap(String isldap) {
	// this.isldap = isldap;
	// }

	@Value("#{serverConfigProperties.ldapHost}")
	private String ldapHost;

	@Value("#{serverConfigProperties.ldpPassword}")
	private String ldpPassword;

	@Value("#{serverConfigProperties.loginDN}")
	private String loginDN;

	@Value("#{serverConfigProperties.objectDN}")
	private String objectDN;

	public String getLdapHost() {
		return ldapHost;
	}

	public void setLdapHost(String ldapHost) {
		this.ldapHost = ldapHost;
	}

	public String getLdpPassword() {
		return ldpPassword;
	}

	public void setLdpPassword(String ldpPassword) {
		this.ldpPassword = ldpPassword;
	}

	public String getLoginDN() {
		return loginDN;
	}

	public void setLoginDN(String loginDN) {
		this.loginDN = loginDN;
	}

	public String getObjectDN() {
		return objectDN;
	}

	public void setObjectDN(String objectDN) {
		this.objectDN = objectDN;
	}

	@Value("#{serverConfigProperties.attachmentPath}")
	private String attachmentPath;

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	@Value("#{serverConfigProperties.tempFilePath}")
	private String tempFilePath;

	public String getTempFilePath() {
		return tempFilePath;
	}

	public void setTempFilePath(String tempFilePath) {
		this.tempFilePath = tempFilePath;
	}
}
