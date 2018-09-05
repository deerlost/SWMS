package com.inossem.wms.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.inossem.wms.model.auth.Resources;
import com.inossem.wms.model.auth.Role;

public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

	@Resource
	private RoleService roleService;

	public MyInvocationSecurityMetadataSourceService(RoleService roleService) {
		this.roleService = roleService;
		loadResourceDefine();
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object arg0) throws IllegalArgumentException {
		// object 是一个URL，被用户请求的url。
		String url = ((FilterInvocation) arg0).getRequestUrl();

		int firstQuestionMarkIndex = url.indexOf("?");

		if (firstQuestionMarkIndex != -1) {
			url = url.substring(0, firstQuestionMarkIndex);
		}

		Iterator<String> ite = resourceMap.keySet().iterator();
		Collection<ConfigAttribute> returnCollection = new ArrayList<ConfigAttribute>();
		returnCollection.add(new SecurityConfig("ROLE_NO_USER"));
		while (ite.hasNext()) {
			String resURL = ite.next();
			if (resURL != null && !resURL.equals("")) {
				RequestMatcher requestMatcher = new AntPathRequestMatcher(resURL);
				if (requestMatcher.matches(((FilterInvocation) arg0).getHttpRequest())) {
					returnCollection.addAll(resourceMap.get(resURL));
					// return resourceMap.get(resURL);
				}
			}
		}
		return returnCollection;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 从数据库中获取已配置role的所有权限,存入resourceMap 以供后续使用
	 * 
	 * @date 2017年9月16日
	 * @author 高海涛
	 */
	public void loadResourceDefine() {

		resourceMap = new HashMap<String, Collection<ConfigAttribute>>();

		// 查询数据库，获取所有role的id
		ArrayList<Role> roles = this.roleService.getRole();

		// 创建一个集合 保存全部可用角色ID 以供共通权限使用
		Collection<ConfigAttribute> roleAll = new ArrayList<ConfigAttribute>();
		for (Role role : roles) {
			ConfigAttribute ca = new SecurityConfig(role.getRoleId().toString());
			roleAll.add(ca);
		}

		/*
		 * 应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问。
		 * sparta
		 */
		for (Role role : roles) {

			ConfigAttribute ca = new SecurityConfig(role.getRoleId().toString());
			List<Integer> r = new ArrayList<Integer>();
			r.add(role.getRoleId());
			// 在数据库中查询角色id可以访问的所有资源
			ArrayList<Resources> resources = this.roleService.getResources(r);

			// 角色0的资源为共通资源 ,全部角色可以使用
			if (role.getRoleId() == 0) {
				for (Resources res : resources) {
					if (!"".equals(res.getResourcesPath()) || res.getResourcesPath() != null) {
						resourceMap.put(res.getResourcesPath(), roleAll);
					}
				}
			} else {
				for (Resources res : resources) {
					String url = res.getResourcesPath();
					if (!"".equals(url) && url != null) {
						/*
						 * 判断资源文件和权限的对应关系，如果已经存在相关的资源url，则要通过该url为key提取出权限集合，
						 * 将权限增加到权限集合中。 sparta
						 */
						if (resourceMap.containsKey(url)) {

							Collection<ConfigAttribute> value = resourceMap.get(url);
							value.add(ca);
							resourceMap.put(url, value);
						} else {
							Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
							atts.add(ca);
							resourceMap.put(url, atts);
						}
					}
				}
			}
		}
	}

}
