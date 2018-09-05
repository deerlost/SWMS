package com.inossem.wms.web.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.auth.MyInvocationSecurityMetadataSourceService;
import com.inossem.wms.model.auth.Resources;
import com.inossem.wms.model.auth.ResourcesRole;
import com.inossem.wms.model.auth.Role;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.page.PageHelper;
import com.inossem.wms.model.page.PageParameter;
import com.inossem.wms.service.auth.IRoleService;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/auth/role")
public class RoleController {
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private IRoleService role_Service;

	@Autowired
	private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;

	// 角色列表
	@RequestMapping(value = "/role_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject roleList(@RequestBody JSONObject json) {
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		List<Map<String, Object>> roleList = new ArrayList<Map<String, Object>>();
		int pageIndex = 1;
		int pageSize = 20;
		int totalCount = -1;
		try {

			PageParameter param = PageHelper.getParameterFromJSON(json);
			pageIndex = param.getPageIndex();
			pageSize = param.getPageSize();
			totalCount = param.getTotalCount();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("paging", true);
			map.put("totalCount", totalCount);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			map.put("sortAscend", param.isSortAscend());
			map.put("sortColumn", param.getSortColumn());
			map.put("keyword", UtilJSON.getStringOrNullFromJSON("condition", json));
			roleList = role_Service.selectRoleList(map);
			if (roleList != null && roleList.size() > 0) {
				Long totalCountLong = (Long) roleList.get(0).get("totalCount");
				totalCount = totalCountLong.intValue();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(UtilString.STRING_EMPTY, e);
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, totalCount, roleList);
	}

	// 为用户添加角色

	@RequestMapping(value = "/modify_resources_for_role", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject modifyResourcesForRole(@RequestBody JSONObject json) {
		boolean ret = false;
		try {
			String roleId = json.getString("role_id");
			String resourcesIds = json.getString("resources_ids");

			// 删除resources_role表中所有role_id=roleId的数据
			int roleid = Integer.parseInt(roleId);
			role_Service.deleteByRoleId(roleid);

			// 向添加resources_role表中插入数据，role_id=roleId,resourcesIds

			if (resourcesIds != null && resourcesIds.length() > 0) {
				ResourcesRole resourcesRole = null;
				for (String resourcesId : resourcesIds.split(",")) {
					int resourcesid = Integer.parseInt(resourcesId);
					resourcesRole = new ResourcesRole();
					resourcesRole.setResourcesId(resourcesid);

					resourcesRole.setRoleId(Integer.parseInt(roleId));
					role_Service.addResourcesRole(resourcesRole);
				}
			}

			ret = true;
		} catch (Exception e) {
			logger.error("", e);
		}
		JSONObject head = new JSONObject();
		head.put("error_code", 0);
		head.put("error_string", "Success");
		head.put("page_index", 0);
		head.put("page_size", 1);
		head.put("total", 5);

		JSONObject obj = new JSONObject();
		obj.put("head", head);
		obj.put("body", true);
		((MyInvocationSecurityMetadataSourceService) filterInvocationSecurityMetadataSource).loadResourceDefine();
		return obj;
	}

	@RequestMapping(value = "/show_resources_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject showResourcesList(@RequestBody JSONObject json) {
		boolean ret = false;
		// 已有resourcesIds和要添加的resourcesIds进行排重
		ArrayList<Resources> resourceslist = null;
		ArrayList<Resources> allResourceslist = null;
		Role role = null;
		ArrayList<Resources> exResourceslist = new ArrayList<Resources>();
		try {
			String roleId = json.getString("role_id");
			// 角色
			role = role_Service.selectRoleByRoleId(Integer.parseInt(roleId));
			// 所有权限
			allResourceslist = role_Service.selectAllResources();
			// 已有权限
			resourceslist = role_Service.selectResourcesById(roleId);
			// 没有权限
			for (Resources resources : allResourceslist) {
				boolean flag = true;
				for (Resources r : resourceslist) {
					if (resources.getResourcesId().equals(r.getResourcesId())) {
						flag = false;
						break;
					}
				}
				if (flag) {
					exResourceslist.add(resources);
				}
			}
			ret = true;
		} catch (Exception e) {
			logger.error("", e);
		}
		JSONObject head = new JSONObject();
		head.put("error_code", 0);
		head.put("error_string", "Success");
		head.put("page_index", 0);
		head.put("page_size", 1);
		head.put("total", 5);

		// JSONObject obj = new JSONObject();

		JSONObject body = new JSONObject();
		body.put("resourceslist", resourceslist);
		body.put("exResourceslist", exResourceslist);

		JSONObject obj = new JSONObject();
		obj.put("head", head);
		obj.put("body", body);
		return obj;
	}

	// 代码优化showRoleMsg

	@RequestMapping(value = "/show_role_msg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject showRoleMsg(@RequestBody JSONObject json) {
		Role role = null;
		ArrayList<Resources> resourceslist = null;
		ArrayList<Resources> allResourceslist = null;
		JSONObject rolejson = new JSONObject();
		JSONObject resourcesjson = new JSONObject();
		JSONArray resourcesAry = new JSONArray();
		JSONArray exResourcesAry = new JSONArray();
		JSONArray allResourcesAry = new JSONArray();
		String roleId = json.getString("role_id").trim();

		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		try {
			// 角色
			if (roleId.equals("")) {
				rolejson.put("roleId", "");
				rolejson.put("roleName", "");
			} else {
				role = role_Service.selectRoleByRoleId(Integer.parseInt(roleId));
				rolejson.put("roleId", role.getRoleId());
				rolejson.put("roleName", role.getRoleName());
			}

			// 权限(含resources_url)
			allResourceslist = role_Service.selectAllResourcesHaveUrl();

			if (roleId.equals("")) {
				for (Resources resources : allResourceslist) {
					resourcesjson.put("resourcesId", resources.getResourcesId());
					resourcesjson.put("resourcesName",
							UtilString.getStringOrEmptyForObject(resources.getResourcesName()));
					exResourcesAry.add(resourcesjson);
				}
			} else {
				// 已选择权限
				resourceslist = role_Service.selectResourcesById(roleId);
				for (Resources resources : resourceslist) {
					resourcesjson.put("resourcesId", resources.getResourcesId());
					resourcesjson.put("resourcesName",
							UtilString.getStringOrEmptyForObject(resources.getResourcesName()));
					resourcesAry.add(resourcesjson);
				}
				// 未选择权限
				for (Resources resources : allResourceslist) {
					boolean flag = true;
					for (Resources r : resourceslist) {
						if (resources.getResourcesId().equals(r.getResourcesId())) {
							flag = false;
							break;
						}
					}
					if (flag) {
						resourcesjson.put("resourcesId", resources.getResourcesId());
						resourcesjson.put("resourcesName",
								UtilString.getStringOrEmptyForObject(resources.getResourcesName()));
						exResourcesAry.add(resourcesjson);
					}
				}

			}
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("", e);
		}

		JSONObject body = new JSONObject();
		body.put("role", rolejson);
		body.put("resourcesAry", resourcesAry);
		body.put("exResourcesAry", exResourcesAry);

		return UtilResult.getResultToJson(status, errorCode, body);

	}

	// 代码优化newModifyRole
	@RequestMapping(value = "/modify_role", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject modifyRole(@RequestBody JSONObject json) {
		boolean ret = true;
		int checkResult = 0;
		// checkResult=0 修改或添加成功 checkResult=1 roleId 重复
		// checkResult=2 roleName重复 checkResult=3roleId和roleName都重复
		boolean edit = json.containsKey("edit") ? json.getBoolean("edit") : true;
		String roleId = json.getString("role_id");
		String roleName = json.getString("role_name");

		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		try {
			Role roleCount;
			ArrayList<Role> roles = null;
			// 判断角色ID及名称是否重复
			roles = role_Service.selectRoleByRoleName(roleName);
			if (!edit) {
				roleCount = role_Service.selectRoleByRoleId(UtilObject.getIntegerOrNull(Integer.parseInt(roleId)));
				if (roleCount != null && roles.size() == 0) {
					ret = false;
					checkResult = 1;
				}
				if (roleCount == null && roles.size() != 0) {
					ret = false;
					checkResult = 2;
				}
				if (roleCount != null && roles.size() != 0) {
					ret = false;
					checkResult = 3;
				}
			} else {
				String firstRoleName = role_Service.selectRoleByRoleId(UtilObject.getIntegerOrNull(Integer.parseInt(roleId))).getRoleName();
				if (!firstRoleName.equals(roleName)) {
					if (roles.size() != 0) {
						ret = false;
						checkResult = 2;
					}
				}

			}

			if (ret) {
				if (json.containsKey("resources_ids")) {
					String resourcesIds = json.getString("resources_ids");

					// 修改时删除resources_role表中所有role_id=roleId的数据
					if (edit) {
						int roleid = Integer.parseInt(roleId);
						role_Service.deleteByRoleId(roleid);
					}
					// 向resources_role表中插入数据，role_id=roleId,resourcesIds
					if (resourcesIds != null && resourcesIds.length() > 0) {
						ResourcesRole resourcesRole = null;
						for (String resourcesId : resourcesIds.split(",")) {
							int resourcesid = Integer.parseInt(resourcesId);
							resourcesRole = new ResourcesRole();
							resourcesRole.setResourcesId(resourcesid);
							resourcesRole.setRoleId(Integer.parseInt(roleId));
							role_Service.addResourcesRole(resourcesRole);
						}
					}
				}

				if (json.containsKey("role")) {
					if (!edit) {
						Role roleNew = new Role();
						roleNew.setRoleId(Integer.parseInt(roleId));
						role_Service.addRole(roleNew);
					}
					Role role = new Role();
					role.setRoleId(Integer.parseInt(roleId));
					role.setRoleName(roleName);
					role_Service.modifyRole(role);
				}
			}
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("", e);
		}
		// 返回的ret=false代表角色已存在，不能添加
		JSONObject body = new JSONObject();
		body.put("ret", ret);
		body.put("number", checkResult);

		((MyInvocationSecurityMetadataSourceService) filterInvocationSecurityMetadataSource).loadResourceDefine();

		return UtilResult.getResultToJson(status, errorCode, body);
	}

	@RequestMapping(value = "/add_role", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addRole(@RequestBody JSONObject json) {
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int raddnum = 0;
		String roleId = json.getString("role_id");
		String roleName = json.getString("role_name");
		try {
			Role r = new Role();
			r.setRoleId(Integer.parseInt(roleId));
			r.setRoleName(roleName);
			raddnum = role_Service.addRole(r);
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("", e);
		}

		JSONObject body = new JSONObject();
		body.put("flag", true);
		body.put("id", raddnum);

		((MyInvocationSecurityMetadataSourceService) filterInvocationSecurityMetadataSource).loadResourceDefine();
		return UtilResult.getResultToJson(status, errorCode, body);
	}

	// 判断新添加的roleId是否已存在
	@RequestMapping(value = "/is_role_id", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object isRoleId(@RequestBody JSONObject json) {
		boolean ret = true;
		String roleId = json.getString("role_id");
		String roleName = json.getString("role_name");
		boolean bln = roleId.equals("");
		// ret=true 可以使用 number=0
		// ret=false 不可以使用 number=1 ID重复 2 name重复
		Role roleCount;
		ArrayList<Role> roles = null;
		int number = 0;

		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		try {
			roles = role_Service.selectRoleByRoleName(roleName);
			roleCount = role_Service.selectRoleByRoleId(UtilObject.getIntegerOrNull(roleId));
			if (!roleId.equals("")) {
				if (roleCount != null && roles.size() == 0) {
					ret = false;
					number = 1;
				} else if (roleCount == null && roles.size() != 0) {
					ret = false;
					number = 2;
				} else if (roleCount != null && roles.size() != 0) {
					ret = false;
					number = 3;
				}
			} else {
				if (roles.size() != 0) {
					ret = false;
					number = 2;
				}
			}
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("", e);
		}

		JSONObject body = new JSONObject();
		body.put("ret", ret);
		body.put("number", number);

		return UtilResult.getResultToJson(status, errorCode, body);

	}

	// 代码优化 newIsRoleId

	// @RequestMapping(value = "/newIsRoleId", method = RequestMethod.POST,
	// produces = "application/json;charset=UTF-8")
	/*
	 * private int newIsRoleId(String role_id, String roleName, Boolean edit) {
	 * boolean ret = true; // edit = true 修改 edit = false 添加 // number=0 可以使用
	 * number=1 ID重复 number=2 name重复 int checkResult = 0; try { Role role =
	 * role_Service.selectRoleByRoleId(Integer.parseInt(role_id));
	 * ArrayList<Role> roles = role_Service.selectRoleByRoleName(roleName); if
	 * (!edit) { if (role != null && roles.size() == 0) { checkResult = 1; } if
	 * (role == null && roles.size() != 0) { checkResult = 2; } if (role != null
	 * && roles.size() != 0) { checkResult = 3; } } else { if (roles.size() !=
	 * 0) { checkResult = 2; } } } catch (Exception e) { logger.error("", e); }
	 * return checkResult; }
	 */

}
