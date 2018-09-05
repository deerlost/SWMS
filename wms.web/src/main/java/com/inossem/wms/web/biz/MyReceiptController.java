package com.inossem.wms.web.biz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.constant.Const;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockOutputHead;
import com.inossem.wms.model.biz.BizStockOutputReturnHead;
import com.inossem.wms.model.biz.BizStockTransportHead;
import com.inossem.wms.model.dic.DicReceiptType;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.log.LogReceiptUser;
import com.inossem.wms.model.page.PageHelper;
import com.inossem.wms.model.page.PageParameter;
import com.inossem.wms.model.vo.ApproveUserMatReqVo;
import com.inossem.wms.model.vo.ApproveUserVo;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IInputService;
import com.inossem.wms.service.biz.IMyReceiptService;
import com.inossem.wms.service.biz.IOutPutService;
import com.inossem.wms.service.biz.IReturnService;
import com.inossem.wms.service.biz.IStocktakeService;
import com.inossem.wms.service.biz.ITransportService;
import com.inossem.wms.service.intfc.IApprove;
import com.inossem.wms.util.UtilREST;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/biz/myreceipt")
public class MyReceiptController {
	private static Logger logger = LoggerFactory.getLogger(MyReceiptController.class);
	// @Autowired
	// private ICommonService commomService;

	@Autowired
	private IMyReceiptService myReceiptService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private IStocktakeService stocktakeService;

	@Autowired
	private IApprove IApproveService;

	@Resource
	private IUserService userService;

	@Resource
	private IInputService inputService;

	@Resource
	private IOutPutService outputService;

	@Resource
	private ITransportService transportService;

	@Resource
	private IReturnService returnService;

	/**
	 * 审批类型
	 * 
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/tasktype", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getApproveTaskType(CurrentUser cUser) {
		logger.info("POST requser with {}", cUser.getUserId());
		List<DicReceiptType> tasktypeList = new ArrayList<DicReceiptType>();
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		try {

			tasktypeList = myReceiptService.getApproveTaskType(cUser.getUserId());

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return UtilResult.getResultToJson(status, errorCode, tasktypeList);
	}

	/**
	 * 单据审批人列表
	 * 
	 * @param receipt_id
	 * @param receipt_type
	 * @return
	 *//*
		 * @RequestMapping(value = "/getApproveUserList", method =
		 * RequestMethod.GET, produces = "application/json;charset=UTF-8")
		 * public @ResponseBody Object getApproveUserList(int receipt_id, int
		 * receipt_type) { JSONArray body = new JSONArray(); boolean status =
		 * false; int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		 * try {
		 * 
		 * JSONObject sprObj; List<ApproveUserVo> list =
		 * commomService.getReceiptUser(receipt_id, receipt_type); //
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); for
		 * (ApproveUserVo approveUserVo : list) { sprObj = new JSONObject();
		 * sprObj.put("receipt_id", approveUserVo.getReceiptId());
		 * sprObj.put("user_id", approveUserVo.getUserId());
		 * sprObj.put("user_name", approveUserVo.getUserName());
		 * sprObj.put("corp_name", approveUserVo.getCorpName());
		 * sprObj.put("org_name", approveUserVo.getOrgName());
		 * sprObj.put("approve_name", approveUserVo.getApproveName());
		 * sprObj.put("comment", approveUserVo.getComment()); body.add(sprObj);
		 * }
		 * 
		 * errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue(); status =
		 * true; } catch (Exception e) { errorCode =
		 * EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		 * logger.error(UtilString.STRING_EMPTY, e); } return
		 * UtilResult.getResultToJson(status, errorCode, body);
		 * 
		 * }
		 */
	/**
	 * 需要审批的任务
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/task", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getApproveTaskList(@RequestBody JSONObject json, CurrentUser cUser) {
		logger.info("POST requser with {}", cUser.getUserId());
		List<Map<String, Object>> taskList = new ArrayList<Map<String, Object>>();
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		String beginDate = null;
		String endDate = null;
		JSONArray receiptType = null;
		boolean approved = false;
		try {
			PageParameter pageParameter = PageHelper.getParameterFromJSON(json);
			pageIndex = pageParameter.getPageIndex();
			pageSize = pageParameter.getPageSize();
			if (json.containsKey("receipt_type")) {
				receiptType = json.getJSONArray("receipt_type");
			}

			if (json.containsKey("begin_date")) {
				String tmp = json.getString("begin_date");
				if (!UtilString.isNullOrEmpty(tmp)) {
					beginDate = tmp;
				}
			}
			if (json.containsKey("end_date")) {

				String tmp = json.getString("end_date");
				if (!UtilString.isNullOrEmpty(tmp)) {
					Date date = UtilString.getDateForString(tmp);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.add(Calendar.DAY_OF_MONTH, 1);
					endDate = UtilString.getShortStringForDate(cal.getTime());
				}
			}
			// 是否已经审批
			// 就参数是ysp
			if (json.containsKey("approved")) {
				approved = json.getBoolean("approved");
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", cUser.getUserId());
			map.put("paging", true);
			map.put("pageIndex", pageParameter.getPageIndex());
			map.put("pageSize", pageParameter.getPageSize());
			map.put("totalCount", pageParameter.getTotalCount());
			map.put("sortAscend", pageParameter.isSortAscend());
			map.put("sortColumn", pageParameter.getSortColumn());
			map.put("beginDate", beginDate);
			map.put("endDate", endDate);

			List<ApproveUserMatReqVo> voList;
			if (receiptType != null
					&& (receiptType.contains(String.valueOf(EnumReceiptType.MAT_REQ_PRODUCTION.getValue())))
					|| receiptType.contains(String.valueOf(EnumReceiptType.MAT_REQ_BUILD.getValue()))) {
				if (approved) {
					// 领料单已审批任务
					voList = myReceiptService.getMatReqApprovedTasks(map);
				} else {
					// 领料单未审批任务
					voList = myReceiptService.getMatReqApproveTasks(map);
				}
			} else {
				List<Integer> typeList = new ArrayList<Integer>();
				if (receiptType != null && receiptType.size() > 0) {
					receiptType.forEach(p -> typeList.add(Integer.parseInt(p.toString())));
				}
				if (typeList.size() > 0) {
					map.put("typeAry", typeList);
				}
				if (approved) {
					voList = myReceiptService.getApprovedTasks(map);
				} else {
					voList = myReceiptService.getApproveTasks(map);
				}
			}

			if (voList != null && voList.size() > 0) {
				// SPTask spTask = list.get(0);
				total = voList.get(0).getTotalCount();

				for (ApproveUserMatReqVo vo : voList) {
					HashMap<String, Object> taskMap = new HashMap<String, Object>();
					taskMap.put("receipt_type", vo.getReceiptType());
					taskMap.put("receipt_type_name", EnumReceiptType.getNameByValue(vo.getReceiptType()));
					taskMap.put("receipt_id", vo.getReceiptId());
					taskMap.put("receipt_code", vo.getReceiptCode());
					taskMap.put("org_name", UtilString.getStringOrEmptyForObject(vo.getOrgName()));
					taskMap.put("user_name", UtilString.getStringOrEmptyForObject(vo.getUserName()));
					taskMap.put("node_name", UtilString.getStringOrEmptyForObject(vo.getNodeName()));
					taskMap.put("create_user_id", UtilString.getStringOrEmptyForObject(vo.getCreateUserId()));
					taskMap.put("create_user_name", UtilString.getStringOrEmptyForObject(vo.getCreateUserName()));
					taskMap.put("create_time", UtilString.getShortStringForDate(vo.getCreateTime()));
					taskMap.put("piid", vo.getPiid());
					taskList.add(taskMap);
				}
			}

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error(UtilString.STRING_EMPTY, e);
		}
		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, taskList);

	}

	@RequestMapping(value = "/task_default_approve", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object taskFaultApprove(@RequestBody JSONObject json, CurrentUser user) {
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean result = false;
		try {
			int receiptId = json.getInt("receipt_id");
			String processInstanceId = json.getString("piid");
			int receiptType = json.getInt("receipt_type");

			result = commonService.taskDefaultApprove(receiptId, (byte) receiptType, processInstanceId,
					user.getUserId());
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return UtilResult.getResultToJson(status, errorCode, result);
	}

	/**
	 * 库存盘点审批 专用
	 * 
	 * @param json
	 * @return
	 */
//	@RequestMapping(value = {
//			"/task_stocktake_approve" }, method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
//	public @ResponseBody Object taskStocktakeApprove(@RequestBody JSONObject json, CurrentUser user) {
//		boolean status = false;
//		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
//		boolean result = true;
//		// HashMap<String, Object> obj = new HashMap<String, Object>();
//		// JSONObject head = new JSONObject();
//		try {
//			String ivnum = json.getString("stocktake_id");
//			Integer livnum = Integer.parseInt(ivnum);
//			String processInstanceId = json.getString("piid");
//			// String inspect_person = json.getString("inspect_person");
//			String comment = json.getString("comment");
//			boolean approve = json.getBoolean("approve");
//
//			// int result = dhysService.inspect(zysdh, processInstanceId,
//			// "a123458", approve, comment);
//			int res = stocktakeService.taskKCPDApprove(livnum, processInstanceId, user.getUserId(), approve, comment);
//			if (res == Const.RECEIPT_APPROVE_COMPLETE) {
//				// 审批已经完成
//				// 添加解除仓位冻结功能（不论仓位是否冻结都进行解除更新操作）
//				stocktakeService.batchUpdatePdlock(livnum);
//			} else if (res == Const.RECEIPT_APPROVE_INCOMPLETE) {
//				result = false;
//			}
//			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
//			status = true;
//
//		} catch (Exception e) {
//			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
//			logger.error(UtilString.STRING_EMPTY, e);
//		}
//
//		return UtilResult.getResultToJson(status, errorCode, result);
//	}

	/**
	 * approve
	 * 
	 * @author 刘宇 2018.03.29
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/approve_uesr_list_for_material_doc", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject approveUesrListForMaterialDoc(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "成功";
		boolean errorStatus = true;

		try {
			String userId = user.getUserId();
			String firstUserName = json.getString("first_user_name");
			String firstTime = json.getString("first_time");
			String secondUserName = json.getString("second_user_name");
			String secondTime = json.getString("second_time");
			String extraUserName = json.getString("extra_user_name");
			String extraTime = json.getString("extra_time");
			JSONArray ary = json.getJSONArray("ary");
			ArrayList<LogReceiptUser> list = new ArrayList<LogReceiptUser>();
			if (UtilString.isNullOrEmpty(firstUserName) || UtilString.isNullOrEmpty(secondUserName)) {
				errorString = "失败";
				errorStatus = false;
			} else {
				if (UtilString.isNullOrEmpty(extraUserName)) {
					extraUserName = secondUserName;
					extraTime = secondTime;
				}
				for (Object object : ary) {
					JSONObject jsonObj = (JSONObject) object;
					LogReceiptUser logReceiptUser = new LogReceiptUser();
					logReceiptUser.setFirstUserName(firstUserName);
					logReceiptUser.setFirstTime(UtilString.getDateForString(firstTime));
					logReceiptUser.setSecondUserName(secondUserName);
					logReceiptUser.setSecondTime(UtilString.getDateForString(secondTime));
					logReceiptUser.setExtraUserName(extraUserName);
					logReceiptUser.setExtraTime(UtilString.getDateForString(extraTime));
					logReceiptUser.setReceiptCode(jsonObj.getString("receipt_code"));
					logReceiptUser.setMatDocCode(jsonObj.getString("mat_doc_code"));
					logReceiptUser.setMoveTypeCode(jsonObj.getString("move_type_code"));
					logReceiptUser.setCreateUserName(jsonObj.getString("create_user_name"));
					list.add(logReceiptUser);
				}
				errorStatus = IApproveService.getVerprBySap(list, userId);
			}

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("", e);
			errorString = "程序异常";
			errorStatus = false;
		}

		String body = UtilString.STRING_EMPTY;

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
	}

	/**
	 * 最后审批人审批
	 * 
	 * @author 刘宇 重构
	 * 
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/task_last_approve_for_role", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object taskApproveForRole(@RequestBody JSONObject json, CurrentUser user) {
		logger.info("POST requser with {}", json);
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "成功";
		boolean errorStatus = true;
		HashMap<String, Object> obj = new HashMap<String, Object>();
		JSONObject head = new JSONObject();
		try {

			int receiptId = json.getInt("receipt_id");
			String processInstanceId = json.getString("piid");
			// String inspect_person = json.getString("inspect_person");
			// String comment = json.getString("comment");
			// boolean approve = json.getBoolean("approve");

			String type = json.getString("receipt_type");
			boolean islast = commonService.lastRoleTask(receiptId, processInstanceId, user.getUserId());
			// 是否最后审批人
			if (islast) {
				int docType = this.getDocType(Byte.parseByte(type));

				String createUserName = "";
				String createUserId = "";
				String reCodeString = "";
				if (docType == 1) {
					BizStockInputHead inPutObj = inputService.getBizStockInputHeadByStockInputId(receiptId);
					createUserId = inPutObj.getCreateUser();
					reCodeString = inPutObj.getStockInputCode();
				} else if (docType == 2) {
					BizStockOutputHead outPutObj = outputService.getBizStockOutputHeadByStockOutPutId(receiptId);
					createUserId = outPutObj.getCreateUser();
					reCodeString = outPutObj.getStockOutputCode();
				} else if (docType == 3) {
					BizStockTransportHead transportObj = transportService
							.getBizStockTransportHeadByStockTransportId(receiptId);
					createUserId = transportObj.getCreateUser();
					reCodeString = transportObj.getStockTransportCode();
				} else if (docType == 4) {
					BizStockOutputReturnHead returnObj = returnService.getBizStockOutputReturnHeadByReturnId(receiptId);
					createUserId = returnObj.getCreateUser();
					reCodeString = returnObj.getReturnCode();
				} else {
					commonService.taskDefaultApprove(receiptId, Byte.parseByte(type), processInstanceId,
							user.getUserId());
					head.put("error_code", 0);
					head.put("error_string", "审批通过");
				}
				User createUser = userService.getUserById(createUserId);
				createUserName = createUser.getUserName();

				// SAP30接口

				JSONObject params = new JSONObject();

				JSONObject I_IMPORT = new JSONObject();
				String ZERNAM = user.getUserId();

				String ZIMENO = "";

				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String dateStr = sdf.format(date);
				String ZDATE = dateStr.substring(0, 10);
				String ZTIME = dateStr.substring(11, 19);
				I_IMPORT.put("ZTYPE", "30");
				I_IMPORT.put("ZERNAM", ZERNAM);
				I_IMPORT.put("ZIMENO", ZIMENO);
				I_IMPORT.put("ZDATE", ZDATE);
				I_IMPORT.put("ZTIME", ZTIME);
				// I_IMPORT.put("ZDJBH", I_ZDJBH);
				// I_IMPORT.put("ZDJBH", zmkpf.getz);
				JSONArray I_LIST = new JSONArray();
				List<Map<String, Object>> matDocList = commonService
						.listBizMaterialDocHeadByRefereceiptIdAndMatDocType(receiptId, Byte.parseByte(type));
				if (matDocList != null) {

					List<ApproveUserVo> receiptList = commonService.getReceiptUser(receiptId, Byte.parseByte(type));
					Collections.sort(receiptList, new Comparator<ApproveUserVo>() {
						@Override
						public int compare(ApproveUserVo o1, ApproveUserVo o2) {
							if (o1.getId() == o2.getId()) {
								return 0;
							} else if (o1.getId() > o2.getId()) {
								return 1;
							} else {
								return -1;
							}
						}

					});

					for (Map<String, Object> matDoc : matDocList) {
						JSONObject item = new JSONObject();
						item.put("MBLNR", matDoc.get("mat_doc_code") + "");
						item.put("ZTOINDEX", reCodeString);

						if (receiptList != null && receiptList.size() == 1) {
							item.put("ZIN_CHECK1", receiptList.get(0).getUserName());// 入库一级审核人
							item.put("ZIN_CHDATA1", this.getDateString(new Date()));// 入库一级审核日期
							item.put("ZOUT_CHECK1", receiptList.get(0).getUserName());// 出库一级审核人
							item.put("ZOUT_CHDATA1", this.getDateString(new Date()));// 出库一级审核日期
							item.put("ZIN_CHECK2", receiptList.get(0).getUserName());// 入库二级审核人
							item.put("ZIN_CHDATA2", this.getDateString(new Date()));// 入库二级审核日期
							item.put("ZOUT_CHECK2", receiptList.get(0).getUserName());// 出库二级审核人
							item.put("ZOUT_CHDATA2", this.getDateString(new Date()));//
						} else if (receiptList != null && receiptList.size() > 1) {
							item.put("ZIN_CHECK1", receiptList.get(0).getUserName());// 入库一级审核人
							item.put("ZIN_CHDATA1", this.getDateString(receiptList.get(0).getApproveTime()));// 入库一级审核日期
							item.put("ZOUT_CHECK1", receiptList.get(0).getUserName());// 出库一级审核人
							item.put("ZOUT_CHDATA1", this.getDateString(receiptList.get(0).getApproveTime()));// 出库一级审核日期
						}
						if (receiptList != null && receiptList.size() == 2) {
							item.put("ZIN_CHECK2", receiptList.get(1).getUserName());// 入库二级审核人
							item.put("ZIN_CHDATA2", this.getDateString(new Date()));// 入库二级审核日期
							item.put("ZOUT_CHECK2", receiptList.get(1).getUserName());// 出库二级审核人
							item.put("ZOUT_CHDATA2", this.getDateString(new Date()));//
						} else if (receiptList != null && receiptList.size() > 2) {
							item.put("ZIN_CHECK2", receiptList.get(1).getUserName());// 入库二级审核人
							item.put("ZIN_CHDATA2", this.getDateString(receiptList.get(1).getApproveTime()));// 入库二级审核日期
							item.put("ZOUT_CHECK2", receiptList.get(1).getUserName());// 出库二级审核人
							item.put("ZOUT_CHDATA2", this.getDateString(receiptList.get(1).getApproveTime()));//
						}

						if (receiptList != null && receiptList.size() > 0) {
							item.put("ZPRINCIPAL", receiptList.get(receiptList.size() - 1).getUserName());// 仓库主管审核
							// 最后一个
							item.put("Z_CHECKDATA", this.getDateString(new Date()));// 仓库主管审核日期
							// 当前日期
						}

						item.put("BWART", matDoc.get("move_type_code"));// 移动类型
						item.put("ZOPERATOR", createUserName);// 业务员 创建人
						I_LIST.add(item);
					}
				}

				params.put("I_IMPORT", I_IMPORT);
				params.put("I_ZBACK", I_LIST);

				JSONObject returnObj = new JSONObject();
				returnObj = UtilREST.executePostJSONTimeOut(
						Const.SAP_API_URL + "int_30?sap-client=" + Const.SAP_API_CLIENT, params, 3);
				JSONObject RETURN = returnObj.getJSONObject("RETURN");

				String TYPE = RETURN.getString("TYPE");
				String MESSAGE = RETURN.getString("MESSAGE");
				if ("S".equals(TYPE)) {
					commonService.taskDefaultApprove(receiptId, Byte.parseByte(type), processInstanceId,
							user.getUserId());
					errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
					errorString = MESSAGE;
				} else {
					errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
					errorString = MESSAGE;
				}

			} else {
				commonService.taskDefaultApprove(receiptId, Byte.parseByte(type), processInstanceId, user.getUserId());
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				errorString = "审批通过";
			}
		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
			errorString = "程序异常";
		}

		obj.put("head", head);
		String body = UtilString.STRING_EMPTY;

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
	}

	private String getDateString(Object str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (str == null)
			return "";
		return sdf.format((Date) str);
	}

	private byte getDocType(Byte type) {
		ArrayList<Byte> inPutType = new ArrayList<Byte>();
		inPutType.add(EnumReceiptType.STOCK_INPUT_INSPECT.getValue());
		inPutType.add(EnumReceiptType.STOCK_INPUT_EXEMPT_INSPECT.getValue());
		inPutType.add(EnumReceiptType.STOCK_INPUT_OTHER.getValue());
		inPutType.add(EnumReceiptType.STOCK_INPUT_PLATFORM.getValue());
		inPutType.add(EnumReceiptType.STOCK_INPUT_ALLOCATE.getValue());
		ArrayList<Byte> outPutType = new ArrayList<Byte>();
		outPutType.add(EnumReceiptType.STOCK_OUTPUT_MAT_REQ.getValue());
		outPutType.add(EnumReceiptType.STOCK_OUTPUT_PURCHASE_RETURN.getValue());
		outPutType.add(EnumReceiptType.STOCK_OUTPUT_OTHER.getValue());
		outPutType.add(EnumReceiptType.STOCK_OUTPUT_SALE.getValue());
		outPutType.add(EnumReceiptType.STOCK_OUTPUT_ALLCOTE.getValue());
		ArrayList<Byte> tranSportType = new ArrayList<Byte>();
		tranSportType.add(EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue());
		tranSportType.add(EnumReceiptType.STOCK_TRANSPORT_STOCK.getValue());
		tranSportType.add(EnumReceiptType.STOCK_TRANSPORT_FACTORY.getValue());
		ArrayList<Byte> returnType = new ArrayList<Byte>();
		returnType.add(EnumReceiptType.STOCK_RETURN_MAT_REQ.getValue());
		returnType.add(EnumReceiptType.STOCK_RETURN_SALE.getValue());
		returnType.add(EnumReceiptType.STOCK_RETURN_RESERVE.getValue());
		if (inPutType.contains(type)) {
			return 1;
		} else if (outPutType.contains(type)) {
			return 2;
		} else if (tranSportType.contains(type)) {
			return 3;
		} else if (returnType.contains(type)) {
			return 4;
		} else {
			return 0;
		}
	}
}
