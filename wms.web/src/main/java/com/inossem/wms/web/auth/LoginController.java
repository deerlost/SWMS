package com.inossem.wms.web.auth;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.auth.RoleService;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.util.UtilToken;

@Controller
public class LoginController {
	@Resource(name = "wmsUserDetailsService")
	private UserDetailsService userService;
	@Resource
	private RoleService roleService;
	@Resource(name = "authenticationManager")
	private AuthenticationManager authManager;
	@Resource
	private IUserService userServiceImpl;

	/**
	 * 普通用户登录
	 * 
	 * @param response
	 * @param username
	 * @param password
	 * @param request
	 */
	@RequestMapping(value = "/login.action", method = RequestMethod.POST)
	public void authenticate(HttpServletResponse response, @RequestParam String username, @RequestParam String password,
			HttpServletRequest request) {
		String url = request.getParameter("url");
		try {

			UserDetails userDetails = this.userService.loadUserByUsername(username);

			if (userDetails == null) {// 查找不到该用户返回到登录页
				try {
					response.sendRedirect("login.html?msg=1");
				} catch (IOException e) {
					e.printStackTrace();
				}

				return;
			}
			
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
			Authentication authentication = this.authManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			// 获取用户
			CurrentUser cuser = (CurrentUser) userDetails;
			User userData = new User();
			userData.setUserId(cuser.getUserId());
			userData.setLastLogin(new Date());
			userServiceImpl.updateUser(userData);

			String token = UtilToken.createToken(userDetails);

			Cookie cookie = new Cookie("X-Auth-Token", token);
			cookie.setMaxAge(30 * 60);// 设置为30min
			cookie.setPath("/");
			response.addCookie(cookie);

			try {
				if (url == null || "".equals(url)) {
					response.sendRedirect("index.html");
				} else {
					response.sendRedirect(url);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (InternalAuthenticationServiceException ie) {
			try {
				if (url == null || "".equals(url)) {
					response.sendRedirect("login.html?msg=1");
				} else {
					response.sendRedirect("login.html?msg=1&url=" + url);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (BadCredentialsException be) {
			try {
				if (url == null || "".equals(url)) {
					response.sendRedirect("login.html?msg=2");
				} else {
					response.sendRedirect("login.html?msg=2&url=" + url);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping(value = "/token.action", method = RequestMethod.POST)
	public @ResponseBody Object getToken(HttpServletResponse response) {
		String username = "admin";
		// String password="123456";
		/*
		 * Reload user as password of authentication principal will be null
		 * after authorization and password is needed for token generation
		 */
		UserDetails userDetails = this.userService.loadUserByUsername(username);

		String token = UtilToken.createToken(userDetails);
		return token;

		// return "index.html";
		// return JsonUtil.getJsonStr(map);
	}

}
