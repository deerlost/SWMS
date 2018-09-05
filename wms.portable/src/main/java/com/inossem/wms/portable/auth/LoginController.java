package com.inossem.wms.portable.auth;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilToken;

import net.sf.json.JSONObject;

@Controller
public class LoginController {

	@Resource(name = "wmsUserDetailsService")
	private UserDetailsService userService;
	@Resource(name = "authenticationManager")
	private AuthenticationManager authManager;
	@Resource
	private IUserService userServiceImpl;

	/**
	 * 安卓端返回值
	 * 
	 * @param code
	 * @param msg
	 * @param token
	 * @param name
	 * @return
	 */
	public JSONObject getReturnJson(int code, String msg, String token, String name, String file_server_url) {

		JSONObject dataJson = new JSONObject();
		dataJson.put("code", code);
		dataJson.put("msg", msg);
		dataJson.put("token", token);
		dataJson.put("realName", name);
		dataJson.put("fileServerUrl", file_server_url);

		return UtilResult.getResultToJson(true, code, msg, 1, 1, 1, dataJson);
	}
	
	@RequestMapping(value = "/login.action", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object authenticate(HttpServletResponse response, @RequestParam String username,
			@RequestParam String password) {
		UserDetails userDetails = this.userService.loadUserByUsername(username);

		if (userDetails == null) {// 查找不到该用户返回到登录页
			return getReturnJson(0, "用户名不存在!", "", "", "");
		}
	
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		try {
			Authentication authentication = this.authManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			String token = UtilToken.createToken(userDetails);
			
			CurrentUser cuser = (CurrentUser) userDetails;
			User userData = new User();
			userData.setUserId(cuser.getUserId());
			userData.setLastLogin(new Date());
			userServiceImpl.updateUser(userData);

			return getReturnJson(1, "客户端提示信息", token, cuser.getUserName(),
					UtilProperties.getInstance().getFileServerUrl());

		} catch (InternalAuthenticationServiceException ie) {

			return getReturnJson(0, "用户名不存在!", "", "", "");

		} catch (BadCredentialsException ie) {

			return getReturnJson(0, "密码错误!", "", "", "");

		}
	}

	@RequestMapping(value = "/auth/401", method = RequestMethod.POST)
	public void test401(HttpServletResponse response) {
		throw new CredentialsExpiredException("");
	}

	@RequestMapping(value = "/auth/ping.action", method = RequestMethod.GET)
	public @ResponseBody JSONObject ping(HttpServletResponse response) {
		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), 1, 1, 1, new JSONObject());
	}
	

}
