package com.inossem.wms.web.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.inossem.wms.auth.RoleService;
import com.inossem.wms.exception.PrimaryKeyExistedException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.Department;
import com.inossem.wms.model.auth.ORG;
import com.inossem.wms.model.auth.Resources;
import com.inossem.wms.model.auth.Role;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.auth.UserRole;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.dic.DicMoveReason;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.dic.DicProductLine;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumOperatorTypes;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumPrinterType;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.page.PageHelper;
import com.inossem.wms.model.page.PageParameter;
import com.inossem.wms.model.rel.RelUserStockLocation;
import com.inossem.wms.model.vo.DicFactoryVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.model.vo.RelUserStockLocationVo;
import com.inossem.wms.model.vo.WfApproveGroupItemVo;
import com.inossem.wms.model.wf.WfApproveGroupHead;
import com.inossem.wms.model.wf.WfApproveGroupItem;
import com.inossem.wms.service.auth.IRoleService;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IFileService;
import com.inossem.wms.service.dic.IDicCorpService;
import com.inossem.wms.service.dic.IDicFactoryService;
import com.inossem.wms.service.dic.IDicMoveReasonService;
import com.inossem.wms.service.dic.IDicMoveTypeService;
import com.inossem.wms.service.dic.IDicStockLocationService;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilNumber;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/auth")
public class UserController {

	// private static Logger logger =
	// LogManager.getLogger(UserController.class.getName());
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Resource
	private IDicCorpService dicCorpService;

	@Resource
	private IDicFactoryService dicFactoryService;

	@Resource
	private IDictionaryService dictionaryService;

	@Resource
	private IUserService userService;

	@Resource
	private IRoleService role_Service;

	@Resource
	private IDicStockLocationService dicStockLocationService;

	@Resource
	private RoleService roleService;

	@Autowired
	private IFileService fileService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private IDicMoveTypeService moveTypeService;

	@Autowired
	private IDicMoveReasonService moveReasonService;

	@RequestMapping("/show_user")
	public ModelAndView toIndex(HttpServletRequest request) {
		String userId = request.getParameter("id");
		logger.info("Get requser with id={}", userId);
		String user_name = null;
		try {
			User user = this.userService.getUserById(userId);
			user_name = user.getUserName();
		} catch (Exception e) {
			logger.error("", e);
		}
		logger.info("Get return user name={}", user_name);
		ModelAndView mav = new ModelAndView("userInfo");
		mav.addObject("user", user_name);
		return mav;
	}

	/**
	 * 获取菜单
	 * 
	 * @date 2017年9月14日
	 * @author 高海涛
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/get_menu", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getMenu(HttpServletRequest request, CurrentUser cuser) {

		// 获取角色
		List<Integer> role = cuser.getRoleCode();
		// 获取资源
		ArrayList<Resources> resources = roleService.getResources(role);

		JSONObject body = new JSONObject();
		body.put("resources", this.userService.settingMenu(resources));
		body.put("userId", cuser.getUserId());
		body.put("userName", cuser.getUserName());
		body.put("fileServer", UtilProperties.getInstance().getFileServerUrl());
		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), body);
	}

	// 通过path传递参数：http://127.0.0.1:8080/bizservice/auth/user/1
	// 返回json: {"id":1,"loginName":"aaa"}
	@RequestMapping(value = "/user/{uid}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getUser(@PathVariable("uid") String uid, CurrentUser cuser) {
		// User cuser=this.currentUser;
		logger.info("Get requser with id={}", uid);
		// User u = userService.selectByPrimaryKey("a123456");
		// User cuser=WebContext.getInstant().getCurrentUser();
		logger.info("Current user with name={}", cuser.getUserName());
		User user = null;
		try {
			user = this.userService.getUserById(uid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}

		return user;
	}

	@RequestMapping(value = "/user_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getUserInfo(String uid, CurrentUser cuser) {
		boolean status = false;
		JSONObject body = new JSONObject();
		int errorCode = EnumErrorCode.ERROR_CODE_DEMAND_QTY_FAILURE.getValue();
		User user = null;
		try {
			ArrayList<Role> role_list = new ArrayList<Role>();
			List<DicProductLine> product_line = new ArrayList<DicProductLine>();
			Map<String, Object> product_line_user = new HashMap<String, Object>();
			if (!UtilString.isNullOrEmpty(uid)) {
				user = this.userService.getUserById(uid);
				JSONObject userObj = new JSONObject();
				userObj.put("user_id", user.getUserId());
				userObj.put("user_name", user.getUserName());
				userObj.put("sex", UtilNumber.getIntOrZeroForInteger(user.getSex()));
				userObj.put("employer_number", UtilString.getStringOrEmptyForObject(user.getEmployerNumber()));
				userObj.put("mail", UtilString.getStringOrEmptyForObject(user.getMail()));
				userObj.put("mobile", UtilString.getStringOrEmptyForObject(user.getMobile()));
				userObj.put("org_id", UtilString.getStringOrEmptyForObject(user.getOrgId()));
				userObj.put("org_name", UtilString.getStringOrEmptyForObject(user.getOrgName()));
				userObj.put("corp_id", UtilNumber.getIntOrZeroForInteger(user.getCorpId()));
				userObj.put("corp_code", UtilString.getStringOrEmptyForObject(user.getCorpCode()));
				userObj.put("corp_name", UtilString.getStringOrEmptyForObject(user.getCorpName()));
				userObj.put("fty_id", UtilNumber.getIntOrZeroForInteger(user.getFtyId()));
				userObj.put("fty_code", UtilString.getStringOrEmptyForObject(user.getFtyCode()));
				userObj.put("fty_name", UtilString.getStringOrEmptyForObject(user.getFtyName()));
				userObj.put("department", UtilNumber.getIntOrZeroForInteger(user.getDepartment()));
				userObj.put("department_name", UtilString.getStringOrEmptyForObject(user.getDepartmentName()));
				body.put("user", userObj);

				// 当前用户的权限
				role_list = userService.getRoleByUserId(uid);
				body.put("role_list", role_list);

				body.put("location_list", commonService.listLocationForUser(uid, ""));
			} else {
				body.put("user", new JSONObject());

				body.put("role_list", new JSONArray());

				body.put("location_list", new JSONArray());
			}
			if (uid != null && uid != "") {
				product_line_user = userService.getRelUserProductLine(uid);
			}
			if (product_line_user != null && product_line_user.containsKey("product_line_id")) {
				body.put("product_line_id", product_line_user.get("product_line_id"));
				body.put("id", product_line_user.get("id"));
			}
			product_line = userService.selectAllProductLine();
			body.put("product_line", product_line);
			JSONArray children = new JSONArray();

			List<ORG> list = userService.listAllOrg();

			JSONArray ary = new JSONArray();
			list.stream().filter(o -> "yitaigroup".equalsIgnoreCase(o.getParentOrgId())).forEach(o -> {
				JSONObject orgjson = new JSONObject();
				orgjson.put("org_name", o.getOrgName());
				orgjson.put("org_id", o.getOrgId());
				ary.add(orgjson);
			});

			for (Object childJSon : ary) {
				children.add(setChildNode((JSONObject) childJSon, list));
			}

			body.put("org_list", children);

			JSONArray ftyAry = new JSONArray();
			for (DicFactoryVo fty : dicFactoryService.getAllFactory()) {
				JSONObject ftyObj = new JSONObject();
				ftyObj.put("fty_id", fty.getFtyId());
				ftyObj.put("fty_code", fty.getFtyCode());
				ftyObj.put("fty_name", fty.getFtyName());
				ftyAry.add(ftyObj);
			}

			body.put("all_fty_list", ftyAry);
			// 所有权限
			ArrayList<Role> all_role_list = userService.getRoleByUserId(null);
			for (Role role : role_list) {
				for (Role allRole : all_role_list) {
					if (allRole.getRoleId().compareTo(role.getRoleId()) == 0) {
						all_role_list.remove(allRole);
						break;
					}
				}
			}
			body.put("all_role_list", all_role_list);
			Map<String, Object> printer = new HashMap<String, Object>();
			if (user != null) {
				printer = userService.getPrinterList(UtilObject.getStringOrNull(user.getUserId()));
			} else {

				printer = userService.getPrinterList(null);
			}
			if (printer != null && !printer.isEmpty()) {
				body.putAll(printer);
			}
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			// TODO Auto-generated catch block
			logger.error(UtilString.STRING_EMPTY, e);
		}

		// return user;
		return UtilResult.getResultToJson(status, errorCode, body);
	}

	/**
	 * 用户修改
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/edit_user", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object editUser(@RequestBody JSONObject json) {
		// 1 如果是添加 新建一个账户,其他字段修改
		// 2 location_list 这个用户的库存地点权限rel_user_stock_location
		// 3 role_list 这个用户的所有角色wms_user_role

		boolean status = false;
		JSONObject body = new JSONObject();
		int errorCode = EnumErrorCode.ERROR_CODE_DEMAND_QTY_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_DEMAND_QTY_FAILURE.getName();
		User user = null;
		try {
			// 编辑传true，新增传false
			boolean edit = json.containsKey("edit") ? json.getBoolean("edit") : true;
			String userId = UtilJSON.getStringOrNullFromJSON("user_id", json);
			if (UtilString.isNullOrEmpty(userId)) {
				errorString = "账户不能为空";
			} else {
				// user = new User();

				if (!edit) {
					User userCount = userService.getUserById(userId);
					if (userCount != null) {
						throw new PrimaryKeyExistedException("账户已经存在");
					} else {
						// 如果是新建,直接新建一个,即变成修改
						User userNew = new User();
						userNew.setUserId(userId);
						userNew.setIsLocked(false);
						userNew.setIsSyn(0);
						userNew.setPassword(UtilObject.getStringOrEmpty(json.get("password")));
						userNew.setCorpId(0);
						userNew.setFtyId(0);
						userService.addUser(userNew);
					}
				}

				user = this.userService.getUserById(userId);

				user.setUserId(user.getUserId());
				user.setUserName(UtilJSON.getStringOrEmptyFromJSON("user_name", json));
				user.setSex(UtilJSON.getIntFromJSON("sex", json));
				user.setEmployerNumber(UtilJSON.getStringOrEmptyFromJSON("employer_number", json));
				user.setMail(UtilJSON.getStringOrEmptyFromJSON("mail", json));
				user.setMobile(UtilJSON.getStringOrEmptyFromJSON("mobile", json));
				user.setOrgId(UtilJSON.getStringOrEmptyFromJSON("org_id", json));
				user.setOrgName(UtilJSON.getStringOrEmptyFromJSON("org_name", json));
				user.setCorpId(UtilJSON.getIntFromJSON("corp_id", json));
				user.setFtyId(UtilJSON.getIntFromJSON("fty_id", json));
				user.setDepartment(UtilJSON.getIntFromJSON("department", json));
				userService.modifyUser(user);

				if (json.containsKey("location_list")) {
					// 删除rel_user_stock_location表中所有user_id=userId的数据
					userService.deleteLocationByUserId(userId);

					JSONArray locationAry = json.getJSONArray("location_list");
					// 向s_u_lgort_rel表中插入数据
					if (locationAry != null && locationAry.size() > 0) {
						RelUserStockLocation relUserLoc = null;
						for (Object obj : locationAry) {
							JSONObject o = (JSONObject) obj;
							String uid = o.getString("user_id");
							String ftyCode = o.getString("fty_code");
							String locationCode = o.getString("location_code");
							int ftyId = dictionaryService.getFtyIdByFtyCode(ftyCode);
							int locationId = dictionaryService.getLocIdByFtyCodeAndLocCode(ftyCode, locationCode);
							relUserLoc = new RelUserStockLocation();
							relUserLoc.setUserId(uid);
							relUserLoc.setFtyId(ftyId);
							relUserLoc.setLocationId(locationId);
							relUserLoc.setLocationIndex((byte) 127);
							userService.addRelUserStockLocation(relUserLoc);
						}
					}
				}

				if (json.containsKey("role_list")) {
					// 删除rel_user_stock_location表中所有user_id=userId的数据
					// userService.delet(userId);
					role_Service.deleteByUserId(userId);
					JSONArray roleAry = json.getJSONArray("role_list");
					// 去重map
					Map<Integer, Object> mapRoleIds = new HashMap<Integer, Object>();
					// 向s_u_lgort_rel表中插入数据
					if (roleAry != null && roleAry.size() > 0) {
						UserRole userRole = null;
						for (Object obj : roleAry) {
							JSONObject o = (JSONObject) obj;
							String uid = o.getString("user_id");
							int roleId = o.getInt("role_id");
							if (mapRoleIds.size() == 0 || !mapRoleIds.containsKey(roleId)) {
								// 不重复,则执行插入
								mapRoleIds.put(roleId, obj);
								userRole = new UserRole();
								userRole.setUserId(uid);
								userRole.setRoleId(roleId);
								userService.addUserRole(userRole);
							}
						}
					}
				}
				if (json.get("product_line_id") != null) {
					// 用户与产品线关联
					if (json.containsKey("id")) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", UtilObject.getIntegerOrNull(json.get("id")));
						map.put("product_line_id", UtilObject.getIntOrZero(json.get("product_line_id")));
						map.put("user_id", UtilObject.getStringOrEmpty(json.get("user_id")));
						userService.updateRelProductUser(map);
					} else if (json.containsKey("product_line_id") && json.get("product_line_id") != null) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("product_line_id", UtilObject.getIntOrZero(json.get("product_line_id")));
						map.put("user_id", UtilObject.getStringOrEmpty(json.get("user_id")));
						userService.insertRelProductUser(map);
					}
				}
				if (UtilObject.getIntegerOrNull(json.get("label_printer_id")) != null) {
					userService.insertPrinterRelUser(userId,
							UtilObject.getIntegerOrNull(json.get("label_printer_id")),
							EnumPrinterType.LABEL_PRINTER.getValue());
				}
				if (UtilObject.getIntegerOrNull(json.get("paper_printer_id")) != null) {
					userService.insertPrinterRelUser(userId,
							UtilObject.getIntegerOrNull(json.get("paper_printer_id")),
							EnumPrinterType.PAPER_PRINTER.getValue());
				}
				status = true;
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
			}
		} catch (PrimaryKeyExistedException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			logger.error(UtilString.STRING_EMPTY, e);
		}

		// return user;
		return UtilResult.getResultToJson(status, errorCode, errorString, body);
	}

	// 通过body传递参数：http://127.0.0.1:8080/bizservice/auth/user?loginName=bbb
	@RequestMapping(value = "/user", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object add(User u) {
		logger.info("member的add方法");
		logger.info("member:" + u);
		int uid = 0;
		try {
			uid = this.userService.addUser(u);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
		JSONObject obj = new JSONObject();
		obj.put("flag", true);
		obj.put("id", uid);
		return obj;
	}

	@RequestMapping(value = "/add_user", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addUser(@RequestBody JSONObject json) {
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		int uaddnum = 0;
		String userId = json.getString("user_id");
		String userName = json.getString("userName");
		String password = json.getString("password");
		String mobile = json.getString("mobile");
		try {
			User u = new User();
			u.setUserId(userId);
			u.setUserName(userName);
			u.setPassword(password);
			u.setMobile(mobile);
			uaddnum = userService.addUser(u);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(UtilString.STRING_EMPTY, e);
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		JSONObject body = new JSONObject();
		body.put("flag", true);
		body.put("id", uaddnum);
		return UtilResult.getResultToJson(status, errorCode, body);
	}

	// 通过body传递参数：http://127.0.0.1:8080/bizservice/auth/user
	// header: Content-Type:application/json;charset=utf-8
	// body: {"id":1,"loginName":"ccc"}
	@RequestMapping(value = "/user", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object updateUser(@RequestBody User u) {
		boolean ret = false;
		logger.info("get user : id-{},loginName-{}", u.getUserId(), u.getUserName());
		try {
			this.userService.modifyUser(u);
			ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
		JSONObject obj = new JSONObject();
		obj.put("flag", ret);
		return obj;
	}

	@RequestMapping(value = "/getuser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getUser(CurrentUser cuser) {
		// User user = cuser;
		JSONObject body = new JSONObject();
		body.put("user_name", cuser.getUserName());
		body.put("user_id", cuser.getUserId());
		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), body);

	}

	// 获取所有用户列表
	@RequestMapping(value = "/get_users", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getUsers(@RequestBody JSONObject json) {
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
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
			map.put("keyword", UtilJSON.getStringOrNullFromJSON("keyword", json));
			userList = userService.listAll(map);
			if (userList != null && userList.size() > 0) {
				Long totalCountLong = (Long) userList.get(0).get("totalCount");
				totalCount = totalCountLong.intValue();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(UtilString.STRING_EMPTY, e);
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, totalCount, userList);
	}

	// 为用户修改角色

	@RequestMapping(value = "/modify_role_for_user", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject modifyRoleForUser(@RequestBody JSONObject json) {
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		try {
			String userId = json.getString("user_id");
			String roleIds = json.getString("roleIds");

			// 删除user_role表中所有user_id=userId的数据
			userService.deleteByUserId(userId);

			// 向user_role表中插入数据，user_id=userId,roleIds
			if (roleIds != null && roleIds.length() > 0) {
				UserRole userRole = null;
				for (String roleId : roleIds.split(",")) {
					int roleid = Integer.parseInt(roleId);
					userRole = new UserRole();
					userRole.setUserId(userId);
					userRole.setRoleId(roleid);
					userService.addUserRole(userRole);
				}
			}

			// ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(UtilString.STRING_EMPTY, e);
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, errorCode, UtilString.STRING_EMPTY);
	}

	// 根据工厂id查询department

	@RequestMapping(value = "/get_department", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDepartment(@RequestBody JSONObject json) {
		int ftyId = json.getInt("fty_id");
		ArrayList<Department> departments = new ArrayList<Department>();
		departments = userService.listDepartmentByFactory(dictionaryService.getFtyCodeByFtyId(ftyId));
		JSONArray departmentAry = new JSONArray();
		if (departments != null && departments.size() > 0) {
			for (Department dept : departments) {
				JSONObject deptobj = new JSONObject();
				deptobj.put("departmentId", dept.getDepartmentId());
				deptobj.put("departmentName", dept.getName());
				departmentAry.add(deptobj);
			}
		}
		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), departmentAry);

	}

	// 判断新添加的userId是否已存在
	@RequestMapping(value = "/is_user_id", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject isUserId(@RequestBody JSONObject json) {
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String userId = json.getString("user_id");
		// ret=true 可以使用
		// ret=false 不可以使用
		User userCount;
		boolean body = false;
		try {
			userCount = userService.getUserById(userId);
			if (userCount == null) {
				body = true;
			}
			status = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(UtilString.STRING_EMPTY, e);
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, errorCode, body);

	}

	@RequestMapping(value = "/select_all_org", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectAllOrg() {

		// String parentOrgId = json.getString("OrgId");
		// JSONArray orgAry = new JSONArray();
		// ArrayList<ORG> childrenOrgs =
		// userService.selectChildrenOrg(parentOrgId);
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<ORG> orgs = userService.listAllOrg();
		if (orgs != null) {
			for (ORG org : orgs) {
				String tmp = String.format("%s,%s,'%s',\"javascript:setvalue('%s','%s')\",'%s','_self',false",
						org.getOrgId(), org.getParentOrgId(), org.getOrgName(), org.getOrgId(), org.getOrgName(),
						org.getOrgName());
				list.add(tmp);
			}
		}
		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), list);

	}

	// 树形结构接口
	@RequestMapping(value = "/org_tree", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object orgTree() {
		List<ORG> list = userService.listAllOrg();

		JSONArray ary = new JSONArray();
		list.stream().filter(o -> "yitaigroup".equalsIgnoreCase(o.getParentOrgId())).forEach(o -> {
			JSONObject json = new JSONObject();
			json.put("text", o.getOrgName());
			json.put("id", o.getOrgId());
			ary.add(json);
		});
		JSONArray children = new JSONArray();
		for (Object childJSon : ary) {
			children.add(setChildNode((JSONObject) childJSon, list));
		}
		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), children);

	}

	private JSONObject setChildNode(JSONObject currentJSon, List<ORG> list) {
		String id = currentJSon.getString("org_id");
		JSONArray ary = new JSONArray();
		list.stream().filter(o -> id.equalsIgnoreCase(o.getParentOrgId())).forEach(o -> {
			JSONObject json = new JSONObject();
			json.put("org_name", o.getOrgName());
			json.put("org_id", o.getOrgId());
			ary.add(json);
		});
		// if (ary.size() > 0) {
		// currentJSon.put("nodes", ary);
		// }
		JSONArray children = new JSONArray();
		for (Object childJSon : ary) {
			children.add(setChildNode((JSONObject) childJSon, list));
		}
		if (children.size() > 0) {
			currentJSon.put("nodes", children);
		}
		return currentJSon;
	}

	// @RequestMapping(value = "/zdgys", method = RequestMethod.GET, produces =
	// "application/json;charset=UTF-8")
	// public @ResponseBody Map<String, Object> zdgys(int a, int b) {
	// JSONObject head = new JSONObject();
	// head.put("error_code", 0);
	// head.put("error_string", "Success");
	// head.put("page_index", 0);
	// head.put("page_size", 1);
	// head.put("total", 5);
	//
	// // 返回的ret=false代表用户已存在，不能添加
	// JSONObject obj = new JSONObject();
	// obj.put("head", head);
	// obj.put("body", getzdgys(a, b));
	// return obj;
	// }

	// private int getzdgys(int a, int b) {
	// if (a == b) {
	// return a;
	// }
	// if (a < b) {
	// int tmp = a;
	// a = b;
	// b = tmp;
	// }
	// int c = a % b;
	// if (c == 0) {
	// return b;
	// } else {
	// return getzdgys(b, c);
	// }
	// }

	// 通过公司id查其下所有工厂
	@RequestMapping(value = "/get_fty_for_corpid", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getFtyForCorpId(@RequestBody JSONObject json) {
		JSONArray ftyAry = new JSONArray();
		try {
			int corp_id = json.getInt("corp_id");

			if (corp_id > 0) {
				List<DicFactory> ftyList = dicFactoryService.getFactoryByCorpId(corp_id);
				for (DicFactory fty : ftyList) {
					JSONObject ftyJson = new JSONObject();
					ftyJson.put("fty_id", fty.getFtyId());
					ftyJson.put("fty_code", fty.getFtyCode());
					ftyJson.put("fty_name", fty.getFtyName());
					ftyAry.add(ftyJson);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), ftyAry);

	}

	// 通过工厂id查其下所有库存地点
	@RequestMapping(value = "/get_location_for_ftyid", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getLocationForFtyId(@RequestBody JSONObject json) {

		JSONArray locAry = new JSONArray();
		try {
			int ftyId = json.getInt("fty_id");
			if (ftyId > 0) {
				List<RelUserStockLocationVo> locationList = dicStockLocationService.listByFtyId(ftyId);
				String ftyCode = dictionaryService.getFtyCodeByFtyId(ftyId);
				for (RelUserStockLocationVo loc : locationList) {
					JSONObject locObj = new JSONObject();
					locObj.put("fty_id", ftyId);
					locObj.put("fty_code", ftyCode);
					locObj.put("location_id", loc.getLocationId());
					locObj.put("location_code", loc.getLocationCode());
					locObj.put("location_name", loc.getLocationName());
					locObj.put("wh_id", loc.getWhId());
					locObj.put("wh_code", loc.getWhCode());
					locObj.put("wh_name", loc.getWhName());
					locAry.add(locObj);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), locAry);
	}

	// 显示当前登录用户的库存地点
	@RequestMapping(value = "/get_location_for_user", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getLocationForUser(CurrentUser cUser) {
		String userId = cUser.getUserId();
		JSONArray locAry = new JSONArray();
		try {
			locAry = commonService.listLocationForUser(userId, "");
		} catch (Exception e) {
			logger.error("显示当前登录用户的库存地点", e);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), locAry);
	}

	// 显示当前登录用户的库存地点
	@RequestMapping(value = "/get_fty_location_for_user", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getFtyLocationForUser(CurrentUser cUser, String fty_type) {
		String userId = cUser.getUserId();
		JSONArray ftyAry = new JSONArray();
		try {
			ftyAry = commonService.listFtyLocationForUser(userId, fty_type);
		} catch (Exception e) {
			logger.error("显示当前登录用户的库存地点", e);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), ftyAry);
	}

	// 显示当前登录用户的库存地点
	@RequestMapping(value = "/get_fty_location_for_user_fty", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getFtyLocationForUserFty(CurrentUser cUser) {
		List<DicStockLocationVo> locationList = new ArrayList<DicStockLocationVo>();
		JSONArray body = new JSONArray();
		try {
			locationList = dicStockLocationService.getLocationsByFtyId(cUser.getFtyId());
			for (DicStockLocationVo vo : locationList) {
				JSONObject json = new JSONObject();
				if (vo.getStatus().intValue() != 1) {
					// 必须上线的
					continue;
				}
				json.put("wh_id", String.valueOf(vo.getWhId()));
				json.put("fty_id", vo.getFtyId().intValue());
				json.put("fty_code", vo.getFtyCode());
				json.put("fty_name", vo.getFtyName());
				json.put("location_id", String.valueOf(vo.getLocationId()));
				json.put("location_code", vo.getLocationCode());
				json.put("location_name", vo.getLocationName());
				body.add(json);
			}
		} catch (Exception e) {
			logger.error("显示当前登录用户的库存地点", e);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
	}

	// 显示当前登录用户同板块的库存地点
	@RequestMapping(value = "/get_fty_location_for_board", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getFtyLocationForBoard(CurrentUser cUser, String fty_id, String receipt_type) {
		JSONArray ftyAry = new JSONArray();
		try {
			ftyAry = commonService.listFtyLocationForBoardId(cUser.getBoardId());

			if (!UtilString.isNullOrEmpty(receipt_type) && !UtilString.isNullOrEmpty(fty_id)) {
				if (EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue() == Byte.valueOf(receipt_type)
						|| EnumReceiptType.STOCK_TRANSPORT_STOCK.getValue() == Byte.valueOf(receipt_type)) {

					for (int i = 0; i < ftyAry.size(); i++) {
						JSONObject ftyJson = ftyAry.getJSONObject(i);
						if (fty_id.equals(UtilObject.getStringOrEmpty(ftyJson.get("fty_id")))) {
							JSONArray ftyAry2 = new JSONArray();
							ftyAry2.add(ftyJson);
							return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
									EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(),
									EnumPage.TOTAL_COUNT.getValue(), ftyAry2);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("显示当前登录用户同板块的库存地点", e);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), ftyAry);
	}

	// 根据单据类型获取移动类型
	@RequestMapping(value = "/get_move_type_by_receipt_type/{receipt_type}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMoveTypeByReceiptType(@PathVariable("receipt_type") int receiptType) {
		// String userId = cUser.getUserId();
		JSONArray moveTypeAry = new JSONArray();
		List<DicMoveType> locationList;
		try {
			if (receiptType <= 0) {
				locationList = commonService.getMoveTypeByReceiptType(null);
			} else {
				locationList = moveTypeService.getMoveTypeByReceiptType(receiptType);
			}

			for (DicMoveType moveType : locationList) {
				JSONObject moveTypeObj = new JSONObject();
				moveTypeObj.put("move_type_id", UtilString.getStringOrEmptyForObject(moveType.getMoveTypeId()));
				moveTypeObj.put("move_type_code", UtilString.getStringOrEmptyForObject(moveType.getMoveTypeCode()));
				moveTypeObj.put("move_type_name", UtilString.getStringOrEmptyForObject(moveType.getMoveTypeName()));
				moveTypeObj.put("spec_stock", UtilString.getStringOrEmptyForObject(moveType.getSpecStock()));
				moveTypeAry.add(moveTypeObj);
			}
		} catch (Exception e) {
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(),
				moveTypeAry);
	}

	// 根据业务类型获取移动类型
	@RequestMapping(value = "/get_move_type_by_biz_type/{biz_type}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMoveTypeByBizType(@PathVariable("biz_type") int receiptType) {
		// String userId = cUser.getUserId();
		JSONArray moveTypeAry = new JSONArray();
		List<DicMoveType> locationList;
		try {
			if (receiptType <= 0) {
				locationList = commonService.getMoveTypeByReceiptType(null);
			} else {
				locationList = moveTypeService.getMoveTypeByBizType(receiptType);
			}

			for (DicMoveType moveType : locationList) {
				JSONObject moveTypeObj = new JSONObject();
				moveTypeObj.put("move_type_id", UtilString.getStringOrEmptyForObject(moveType.getMoveTypeId()));
				moveTypeObj.put("move_type_code", UtilString.getStringOrEmptyForObject(moveType.getMoveTypeCode()));
				moveTypeObj.put("move_type_name", UtilString.getStringOrEmptyForObject(moveType.getMoveTypeName()));
				moveTypeObj.put("spec_stock", UtilString.getStringOrEmptyForObject(moveType.getSpecStock()));
				moveTypeAry.add(moveTypeObj);
			}
		} catch (Exception e) {
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(),
				moveTypeAry);
	}

	// 根据移动类型获取移动原因
	@RequestMapping(value = "/get_move_reason_by_move_type/{move_type_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMoveReasonByMoveType(@PathVariable("move_type_id") int moveTypeId) {
		// String userId = cUser.getUserId();
		JSONArray moveReasonAry = new JSONArray();
		List<DicMoveReason> moveReasonList = moveReasonService.getMoveTypeByMoveTypeId(moveTypeId);

		for (DicMoveReason moveReason : moveReasonList) {
			JSONObject moveReasonObj = new JSONObject();
			moveReasonObj.put("reason_id", UtilString.getStringOrEmptyForObject(moveReason.getReasonId()));
			moveReasonObj.put("reason_name", UtilString.getStringOrEmptyForObject(moveReason.getReasonName()));
			moveReasonObj.put("reason_code", UtilString.getStringOrEmptyForObject(moveReason.getReasonCode()));
			moveReasonObj.put("move_type_id", UtilString.getStringOrEmptyForObject(moveReason.getMoveTypeId()));
			moveReasonAry.add(moveReasonObj);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(),
				moveReasonAry);
	}

	/**
	 * 删除附件
	 * 
	 * @param attachmentId
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/remove_attachment/{attachment_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object removeAttachment(@PathVariable("attachment_id") int attachmentId, CurrentUser user) {
		JSONObject body = new JSONObject();
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		// String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();

		try {
			fileService.removeReceiptAttachment(attachmentId, user.getUserId());
			status = true;
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			// error_string = "程序异常";
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return UtilResult.getResultToJson(status, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
	}

	/**
	 * 常用经办人组基本信息获取
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/get_operator_base_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getOperatorBaseInfo(CurrentUser cuser) {

		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		List<Map<String, String>> typeList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> roleList = new ArrayList<Map<String, String>>();
		JSONObject body = new JSONObject();

		try {
			typeList = commonService.getOperatorTypes();
			roleList = commonService.getOperatorRoles();
		} catch (Exception e) {
			logger.error("常用经办人组基本信息获取", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}
		body.put("roleList", roleList);
		body.put("typeList", typeList);

		return UtilResult.getResultToJson(status, errorCode, body);
	}

	/**
	 * 常用经办人组获取
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/get_operator_group", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getOperatorGroup(CurrentUser cuser) {

		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		List<WfApproveGroupHead> approveGroupList = new ArrayList<WfApproveGroupHead>();
		try {
			WfApproveGroupHead group = new WfApproveGroupHead();
			group.setUserId(cuser.getUserId());

			approveGroupList = userService.getApproveGroups(group);
			if (approveGroupList != null && approveGroupList.size() > 0) {
				for (WfApproveGroupHead innerGroup : approveGroupList) {
					innerGroup.setTypeName(EnumOperatorTypes.getTypeNameByTypeId(innerGroup.getType()));
				}
			}
		} catch (Exception e) {
			logger.error("常用经办人组基本信息获取", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}

		return UtilResult.getResultToJson(status, errorCode, approveGroupList);
	}

	/**
	 * 常用经办人组获取
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/get_operator_group_details", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getOperatorGroupDetails(CurrentUser cuser, Integer groupId) {

		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();

		List<WfApproveGroupItemVo> approveGroupDetailsList = new ArrayList<WfApproveGroupItemVo>();
		List<WfApproveGroupHead> groupList = new ArrayList<WfApproveGroupHead>();
		WfApproveGroupHead group = new WfApproveGroupHead();
		try {
			WfApproveGroupItem groupDetail = new WfApproveGroupItem();
			groupDetail.setGroupId(groupId);
			approveGroupDetailsList = userService.listApproveGroupItem(groupDetail);
			WfApproveGroupHead findgroup = new WfApproveGroupHead();
			findgroup.setGroupId(groupId);
			groupList = userService.getApproveGroups(findgroup);
			if (groupList != null && groupList.size() == 1) {
				group = groupList.get(0);
				status = true;
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				group.setGroupItemList(approveGroupDetailsList);
			}
		} catch (Exception e) {
			logger.error("常用经办人组基本信息获取", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, errorCode, group);
	}

	/**
	 * 删除常用经办人组
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/delete_operator_group/{groupId}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object deleteOperatorGroup(CurrentUser cuser, @PathVariable("groupId") Integer groupId) {

		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		try {
			WfApproveGroupHead group = new WfApproveGroupHead();
			group.setGroupId(groupId);

			userService.deleteApproveGroup(group);
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		} catch (Exception e) {
			logger.error("删除常用经办人组", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, errorCode, new JSONObject());
	}

	public Object addOperatorGroup(WfApproveGroupHead group, CurrentUser cUser) {

		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		try {
			group.setUserId(cUser.getUserId());
			if (group.getGroupItemList() != null && group.getGroupItemList().size() > 0) {
				userService.addApproveGroup(group);
				status = true;
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			}

		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, errorCode, new JSONObject());
	}

	public Object updateOperatorGroup(WfApproveGroupHead group) {
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();

		try {
			userService.updateApproveGroup(group);
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, errorCode, new JSONObject());
	}

	@RequestMapping(value = "/add_or_update_operator_group", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addOrUpdateOperatorGroup(@RequestBody JSONObject json, CurrentUser cUser) {

		Object obj = new Object();
		WfApproveGroupHead group = new WfApproveGroupHead();
		try {
			group = UtilJSONConvert.fromJsonToHump(json, WfApproveGroupHead.class);

		} catch (Exception e) {
			boolean status = false;
			int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
			return UtilResult.getResultToJson(status, errorCode, new JSONObject());
		}

		if (group != null) {
			if (group.getGroupId() != null) {
				if (group.getGroupItemList() != null) {
					for (WfApproveGroupItemVo groupDetail : group.getGroupItemList()) {
						groupDetail.setGroupId(group.getGroupId());
					}
				}
				obj = this.updateOperatorGroup(group);
			} else {
				obj = this.addOperatorGroup(group, cUser);
			}

		}

		return obj;
	}

	// 给用户配置库存地点 排序功能

	@RequestMapping(value = "/change_location_for_user", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object changeLocationForUser(@RequestBody JSONObject json, CurrentUser cuser) {
		JSONArray locationList = json.getJSONArray("location_list");
		String userId = cuser.getUserId();
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		try {
			if (locationList != null && locationList.size() > 0) {
				userService.deleteLocationByUserId(userId);
				RelUserStockLocation relUserLoc = null;
				for (Object obj : locationList) {
					JSONObject o = (JSONObject) obj;
					Integer ftyId = o.getInt("fty_id");
					Integer locationId = o.getInt("loc_id");
					Integer locationIndex = o.getInt("loc_index");
					relUserLoc = new RelUserStockLocation();
					relUserLoc.setUserId(userId);
					relUserLoc.setFtyId(ftyId);
					relUserLoc.setLocationId(locationId);
					relUserLoc.setLocationIndex(UtilObject.getByteOrNull(locationIndex));
					userService.addRelUserStockLocation(relUserLoc);
				}

				status = true;
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			}
		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, errorCode, new JSONObject());
	}

	// 显示当前登录用户的库存地点

	@RequestMapping(value = "/show_location_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object showLocationList(CurrentUser cUser) {

		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		JSONArray ftyList = new JSONArray();
		try {
			ftyList = commonService.listLocationForUser(cUser.getUserId(), "");

		} catch (Exception e) {
			logger.error("免检入库免检原因", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, ftyList);
		return obj;
	}

	/**
	 * 修改密码
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/change_password", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object changePassword(@RequestBody JSONObject json, CurrentUser cUser) {
		String msg = "修改密码成功！";
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("userId", cUser.getUserId());
			map.put("password", UtilObject.getStringOrEmpty(json.get("password")));
			userService.changePassword(map);
		} catch (Exception e) {
			msg = "修改失败！";
			logger.error("修改密码", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, errorCode, msg, "");
	}

	/**
	 * 打印机列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/get_printer_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object get_printer_list() {
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = userService.getPrinterList(null);
		} catch (Exception e) {
			logger.error("查询打印机列表", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, result);
		return obj;
	}

}
