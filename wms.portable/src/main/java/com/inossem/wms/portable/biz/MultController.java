package com.inossem.wms.portable.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.biz.BizReserveOrderHead;
import com.inossem.wms.model.dic.DicWarehousePallet;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.vo.StockBatchVo;
import com.inossem.wms.model.vo.WfApproveGroupItemVo;
import com.inossem.wms.model.wf.WfApproveGroupHead;
import com.inossem.wms.model.wf.WfApproveGroupItem;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IMatReqService;
import com.inossem.wms.service.biz.IReturnService;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class MultController {

	private static final Logger logger = LoggerFactory.getLogger(MultController.class);
	
	@Autowired
	private IReturnService returnService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IMatReqService matReqService;
	
	@RequestMapping(value = { "/biz/input/other/roles", "/biz/input/exempt/roles", "/biz/input/inspect/roles",
			"/biz/input/platform/roles" ,"/biz/input/allocate/roles", "/biz/output/llck/roles", "/biz/output/xsck/roles", "/biz/output/cgth/roles","/biz/output/dbck/roles",
			"/biz/output/qtck/roles","/biz/output/ylck/roles","/biz/transport/roles","/biz/urgence/roles","/biz/return/matreq/roles",
			"/biz/return/sale/roles","/biz/return/reserve/roles", "/biz/output/dbck/roles"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getRoles() {

		List<Map<String, String>> roles = new ArrayList<Map<String, String>>();
		try {
			roles = commonService.getOperatorRoles();

		} catch (Exception e) {

			logger.error("经办人角色（岗位）", e);
		}

		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		return UtilResult.getResultToJson(true,errorCode, roles);
	}

	/**
	 * 用户查询 角色
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/exempt/user_list", "/biz/input/platform/user_list",
			"/biz/input/other/user_list","/biz/input/allocate/user_list",
			"/biz/output/llck/user_list", "/biz/output/xsck/user_list", "/biz/output/cgth/user_list","/biz/output/dbck/user_list",
			"/biz/output/qtck/user_list","/biz/output/ylck/user_list","/biz/transport/user_list","/biz/urgence/user_list" ,"/biz/return/matreq/user_list",
			"/biz/return/sale/user_list",
			"/biz/return/reserve/user_list"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getUserListByRole(String condition, Integer role_id) {

		List<User> findList = new ArrayList<User>();
		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
		try {
			User user = new User();
			if (StringUtils.hasText(condition) && role_id != null) {
				String sqlStr = "( u.user_name like '%" + condition + "%' or ";
				sqlStr += " u.user_id like '%" + condition + "%' or ";
				sqlStr += " u.org_name like '%" + condition + "%' )";
				sqlStr += " and ur.role_id = " + role_id;
				user.setSql(sqlStr);
			}
			findList = commonService.getOperatorUsers(user);
			for (User innerUser : findList) {
				HashMap<String, Object> userObj = new HashMap<String, Object>();

				userObj.put("user_id", UtilObject.getStringOrEmpty(innerUser.getUserId()));
				userObj.put("corp_name", UtilObject.getStringOrEmpty(innerUser.getCorpName()));
				userObj.put("user_name", UtilObject.getStringOrEmpty(innerUser.getUserName()));
				userObj.put("role_name", UtilObject.getStringOrEmpty(innerUser.getRoleName()));
				userObj.put("role_id", UtilObject.getStringOrEmpty(innerUser.getRoleId()));
				userObj.put("org_name", UtilObject.getStringOrEmpty(innerUser.getOrgName()));
				userObj.put("mobile", UtilObject.getStringOrEmpty(innerUser.getMobile()));
				userList.add(userObj);
			}
		} catch (Exception e) {
			logger.error("查询人员", e);
		}
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		return UtilResult.getResultToJson(true,errorCode, userList);
	}
	
	public Object getOperatorListByReceiptType(CurrentUser cuser, Byte ReceiptType) {

		List<WfApproveGroupHead> approveGroupList = new ArrayList<WfApproveGroupHead>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		try {
			WfApproveGroupHead group = new WfApproveGroupHead();
			group.setUserId(cuser.getUserId());
			group.setType(ReceiptType);
			approveGroupList = userService.getApproveGroups(group);
			if (approveGroupList != null && approveGroupList.size() > 0) {
				for (WfApproveGroupHead innerGroup : approveGroupList) {
					HashMap<String, Object> groupHashMap = new HashMap<String, Object>();
					groupHashMap.put("group_id", innerGroup.getGroupId() + "");
					groupHashMap.put("group_name", innerGroup.getGroupName());
					List<WfApproveGroupItemVo> approveGroupDetailList = new ArrayList<WfApproveGroupItemVo>();
					WfApproveGroupItem groupDetail = new WfApproveGroupItem();
					groupDetail.setGroupId(innerGroup.getGroupId());
					approveGroupDetailList = userService.listApproveGroupItem(groupDetail);
					innerGroup.setTypeName(EnumReceiptType.getNameByValue(innerGroup.getType()));
					innerGroup.setGroupItemList(approveGroupDetailList);

					if (approveGroupDetailList != null) {
						List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
						for (WfApproveGroupItemVo innerGroupDetail : approveGroupDetailList) {
							Map<String, Object> userObj = new HashMap<String, Object>();
							userObj.put("user_id", UtilObject.getStringOrEmpty(innerGroupDetail.getUserId()));
							userObj.put("corp_name", UtilObject.getStringOrEmpty(innerGroupDetail.getCorpName()));
							userObj.put("user_name", UtilObject.getStringOrEmpty(innerGroupDetail.getUserName()));
							userObj.put("role_name", UtilObject.getStringOrEmpty(innerGroupDetail.getRoleName()));
							userObj.put("role_id", UtilObject.getStringOrEmpty(innerGroupDetail.getRoleId()));
							userObj.put("org_name", UtilObject.getStringOrEmpty(innerGroupDetail.getOrgName()));
							userObj.put("mobile", UtilObject.getStringOrEmpty(innerGroupDetail.getMobile()));
							userList.add(userObj);
						}
						groupHashMap.put("groupItemList", userList);
						returnList.add(groupHashMap);
					}

				}
			}
		} catch (Exception e) {
			logger.error("常用经办人组基本信息获取", e);
		}

		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		return UtilResult.getResultToJson(true,errorCode, returnList);

	}

	/**
	 * 常用经办人组基本信息获取(入库)
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/inspect/get_operator_group", "/biz/input/exempt/get_operator_group", "/biz/input/platform/get_operator_group",
			"/biz/input/other/get_operator_group",
			"/biz/input/allocate/get_operator_group" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getOperatorGroupByInput(CurrentUser cuser) {

		return this.getOperatorListByReceiptType(cuser, EnumReceiptType.STOCK_INPUT.getValue());
	}

	/**
	 * 常用经办人组基本信息获取(出库)
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/llck/get_operator_group","/biz/output/xsck/get_operator_group","/biz/output/cgth/get_operator_group","/biz/output/dbck/get_operator_group",
			"/biz/output/qtck/get_operator_group", "/biz/output/ylck/get_operator_group","/biz/return/matreq/get_operator_group",
			"/biz/return/sale/get_operator_group",
			"/biz/return/reserve/get_operator_group"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getOperatorGroupByOutput(CurrentUser cuser) {
		return this.getOperatorListByReceiptType(cuser, EnumReceiptType.STOCK_OUTPUT.getValue());
	}

	/**
	 * 常用经办人组基本信息获取(转储)
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/biz/transport/get_operator_group", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getOperatorGroupByTransport(CurrentUser cuser) {
		return this.getOperatorListByReceiptType(cuser, EnumReceiptType.STOCK_TRANSPORT.getValue());
	}
	
	// 显示当前登录用户的库存地点
	@RequestMapping(value = "/auth/get_location_for_user", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getLocationForUser(CurrentUser cUser) {
		String userId = cUser.getUserId();
		JSONArray locAry = new JSONArray();
		try {
			locAry = commonService.listLocationForUser(userId,"");
		} catch (Exception e) {
			logger.error("显示当前登录用户的库存地点", e);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), locAry);
	}
	
	// 显示当前登录用户的库存地点
	@RequestMapping(value = "/auth/get_fty_location_for_user", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getFtyLocationForUser(CurrentUser cUser,String ftyType) {
		String userId = cUser.getUserId();
		JSONArray ftyAry = new JSONArray();
		try {
			ftyAry = commonService.listFtyLocationForUser(userId,ftyType);
		} catch (Exception e) {
			logger.error("显示当前登录用户的库存地点", e);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), ftyAry);
	}

	/**
	 * 物料列表
	 * 
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/matreq/material_list",
			"/biz/output/qtck/material_list" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object materialList(@RequestBody JSONObject json, CurrentUser user) {
		JSONArray body = new JSONArray();
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		try {

			// String ftyId = json.getString("fty_id"); // 工厂编号
			int locationId = json.getInt("location_id"); // 库存地点编号
			String condition = json.getString("condition"); // 查询条件
			String specStock = null;
			String matType = null;
			if (json.has("spec_stock")) {
				specStock = json.getString("spec_stock");
			}
			if (json.has("mat_type")) {
				matType = json.getString("mat_type");
			}
			List<StockBatchVo> list;

			list = matReqService.getStockBatch(locationId, condition, specStock,matType);
			JSONObject matJson;
			for (StockBatchVo stockBatchVo : list) {
				// if (mchb.getZasseta() == null ||
				// mchb.getZasseta().trim().length() == 0
				// || mchb.getZasseta().equals("99")) {
				// continue;
				// }
				matJson = new JSONObject();
				matJson.put("mat_id", stockBatchVo.getMatId());
				matJson.put("mat_code", stockBatchVo.getMatCode());
				matJson.put("mat_name", stockBatchVo.getMatName());
				matJson.put("unit_id", stockBatchVo.getUnitId());
				matJson.put("unit_code", stockBatchVo.getUnitCode());
				matJson.put("unit_name", stockBatchVo.getUnitName());
				matJson.put("qty", UtilString.getStringOrEmptyForObject(stockBatchVo.getQty()));
				// wlxx.put("werks", stockBatch.getWerks());
				// wlxx.put("name1", stockBatch.getWerks_name());
				matJson.put("location_id", stockBatchVo.getLocationId());
				matJson.put("location_code", stockBatchVo.getLocationCode());
				matJson.put("location_name", stockBatchVo.getLocationName());
				matJson.put("batch", stockBatchVo.getBatch());
				matJson.put("asset_attr", stockBatchVo.getAssetAttr());
				// matJson.put("refer_price", 0);
				matJson.put("decimal_place", stockBatchVo.getDecimalPlace());// 计量单位浮点数
				matJson.put("is_focus_batch", stockBatchVo.getIsFocusBatch());
				matJson.put("fty_id", stockBatchVo.getFtyId());
				// matJson.put("demand_qty", 0);// 需求数量
				// matJson.put("demand_date", "");// 需求日期
				body.add(matJson);
			}
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error(UtilString.STRING_EMPTY, e);
		}
		return UtilResult.getResultToJson(status, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), 5, body);
	}
	
	/**
	 * 获取预留单列表
	 * 
	 * @param
	 * @return List<Map<String,Object>>
	 */
	@RequestMapping(value = { "/biz/return/reserve/get_reserve_order_list",
			"/biz/output/ylck/get_reserve_order_list" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getReserveOrderList(String refer_receipt_code, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		// JSONObject obj = new JSONObject();
		List<BizReserveOrderHead> list = new ArrayList<BizReserveOrderHead>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			list = returnService.getReserveOrderList(refer_receipt_code, cUser.getUsername());

			// String s
			// ="{\"refer_receipt_code\":\"XSDH111\",\"reserve_cost_obj_code\":\"成本对象\",\"reserve_cost_obj_name\":\"成本对象描述\",\"reserve_create_user\":\"创建人\",\"reserve_create_time\":\"创建日期\",\"item_list\":[{\"rid\":\"序号1\",\"reserve_rid\":\"预留行项目号\",\"mat_id\":\"物料id\",\"mat_code\":\"物料编码\",\"mat_name\":\"物料描述\",\"fty_id\":\"工厂id\",\"fty_code\":\"工厂代码\",\"fty_name\":\"工厂描述\",\"location_id\":\"库存地点id\",\"location_code\":\"库存地点\",\"location_name\":\"库存地点描述\",\"move_type_name\":\"移动类型\",\"demand_qty\":\"100\",\"unit_name\":\"单位\",\"batch_spec_list\":[{\"batch_spec_code\":\"ZSCRQ\",\"batch_spec_value\":\"2012-12-11\",\"batch_spec_type\":\"2\",\"required\":\"0\",\"info\":[],\"display_index\":\"1\",\"edit\":\"1\"}],\"batch_material_spec_list\":[{\"batch_spec_code\":\"production_time\",\"batch_spec_value\":\"2012-12-11\",\"batch_spec_type\":\"2\",\"required\":\"0\",\"info\":[],\"display_index\":\"1\",\"edit\":\"1\"},{\"batch_spec_code\":\"validity_time\",\"batch_spec_value\":\"2012-12-11\",\"batch_spec_type\":\"2\",\"required\":\"0\",\"info\":[],\"display_index\":\"2\",\"edit\":\"1\"},{\"batch_spec_code\":\"contract_code\",\"batch_spec_value\":\"2012-12-11\",\"batch_spec_type\":\"1\",\"required\":\"0\",\"info\":[],\"display_index\":\"3\",\"edit\":\"0\"}]}]}";
			// obj = JSONObject.fromObject(s);
			status = true;
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("获取预留单列表", e);
		}

		// return UtilResult.getResultToJson(status, error_code, list);
		JSONObject object = UtilResult.getResultToJson(status, error_code, list);
		UtilJSONConvert.setNullToEmpty(object);
		return object;
	}
	
	// 仓库整理 基于托盘 获取新托盘
	@RequestMapping(value = { "/biz/task/pallet/get_new_pallet"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getNewPallet(Integer pallet_type_id, CurrentUser cUser) {

		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {
			DicWarehousePallet pallet = commonService.newPallet(cUser.getUserId());
			if (pallet != null && pallet.getPalletId() != null) {
				body.put("pallet_id", pallet.getPalletId());
				body.put("pallet_code", pallet.getPalletCode());
				body.put("max_payload", pallet.getMaxWeight());
			} else {
				throw new WMSException("新增托盘出错");
			}

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
		}

		JSONObject obj = UtilResult.getResultToJson(status, errorCode, errorString, body);
		return obj;

	}
	
}
