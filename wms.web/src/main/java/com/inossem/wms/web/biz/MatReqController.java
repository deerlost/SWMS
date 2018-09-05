package com.inossem.wms.web.biz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.inossem.wms.constant.Const;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizMatReqHead;
import com.inossem.wms.model.biz.BizMatReqItem;
import com.inossem.wms.model.biz.BizReceiptAttachment;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumMatReqBizType;
import com.inossem.wms.model.enums.EnumMatReqMatType;
import com.inossem.wms.model.enums.EnumMatReqStatus;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumSequence;
import com.inossem.wms.model.enums.EnumTemplateType;
import com.inossem.wms.model.page.PageHelper;
import com.inossem.wms.model.page.PageParameter;
import com.inossem.wms.model.vo.BizMatReqHeadVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IMatReqService;
import com.inossem.wms.service.intfc.IMatReq;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author Steven 领料管理
 */
@Controller
@RequestMapping("/biz/matreq")
public class MatReqController {
	private static final Logger logger = LoggerFactory.getLogger(MatReqController.class);

	@Autowired
	private IMatReqService matReqService;

	// @Autowired
	// private IUserService userService;
	//
	// @Autowired
	// private IFileService fileService;

	@Autowired
	private IMatReq matReq;

	@Autowired
	private ICommonService commomService;

	@Autowired
	private IDictionaryService dictionaryService;

	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject test() {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		int position_id = 0;
		try {
			commomService.bufferPrice();
			// position_id =
			// dictionaryService.getPositionIdByWhCodeAreaCodePositionCode("0004",
			// "001", "01-01-02");

			// int area_id =
			// dictionaryService.getAreaIdByWhCodeAndAreaCode("0004",
			// "001");
			// int locId = dictionaryService.getMoveTypeIdByMoveTypeCode("Z93");
			// String loccode =
			// dictionaryService.getMoveTypeByMoveTypeId(1000311);
			// int whId = dictionaryService.getWhIdByWhCode("0004");
			// String whCode = dictionaryService.getWhCodeByWhId(4);
			// BizReceiptAttachment receiptAttachment = new
			// BizReceiptAttachment();
			// receiptAttachment.setReceiptId(1);
			// receiptAttachment.setReceiptType((byte) 11);
			// receiptAttachment.setUserId("asdf");
			// receiptAttachment.setFileId("fileId");
			// receiptAttachment.setFileName("fileName");
			// receiptAttachment.setExt("ext");
			// receiptAttachment.setSize(123);
			// int a = fileService.addReceiptAttachment(receiptAttachment);
			// List<BizReceiptAttachmentVo> list =
			// fileService.getReceiptAttachments(1, (byte) 11);
			// System.out.println();

		} catch (Exception e) {
			logger.error(UtilString.STRING_EMPTY, e);
			// TODO: handle exception
		}

		return UtilResult.getResultToJson(true, errorCode, pageIndex, pageSize, total, position_id);
	}

	/**
	 * 获取领料单列表
	 * 
	 * @param str
	 * @return
	 */

	// 获取领料单列表
	@RequestMapping(value = "/get_mat_req_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getMatReqList(@RequestBody JSONObject json, CurrentUser user) {

		JSONArray body = new JSONArray();
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();

		try {

			// Map<String, DicFactoryVo> map = commomService.getFtyCodeMap();
			// String condition = json.getString("condition").trim();
			// pageIndex = PageHelper.getPageIndexFromJSON(json);
			// pageSize = PageHelper.getPageSizeFromJSON(json);
			// int totalCount = PageHelper.getTotalCountFromJSON(json);
			// boolean sortAscend = PageHelper.getSortAscendFromJSON(json);
			// String sortColumn = PageHelper.getSortColumnFromJSON(json);
			PageParameter pageParameter = PageHelper.getParameterFromJSON(json);
			pageIndex = pageParameter.getPageIndex();
			pageSize = pageParameter.getPageSize();

			JSONObject matReqObj = new JSONObject();

			int receiptType = json.getInt("receipt_type");
			// 状态参数使用-分隔，可以多状态查询
			String statusStr = json.getString("status_condition");
			String[] statusAry;
			if (statusStr.trim().length() > 0) {
				statusAry = statusStr.split("-");
				// 查询条件
				String query = UtilJSON.getStringOrEmptyFromJSON("query", json);
				// zgsbk为板块,直接查看用户公司的板块
				List<BizMatReqHeadVo> list = matReqService.getMatReqList(user.getUserId(), receiptType, statusAry,
						query, user.getBoardId(), pageParameter);

				if (list != null && list.size() > 0) {
					total = list.get(0).getTotalCount();
					for (BizMatReqHeadVo vo : list) {
						matReqObj.put("mat_req_id", vo.getMatReqId());
						matReqObj.put("mat_req_code", vo.getMatReqCode());
						// matReqObj.put("mat_req_id", vo.getMatReqId());
						// lldObj.put("receive_fty_id", vo.getReceiveFtyId());
						matReqObj.put("receive_fty_name", vo.getReceiveFtyName());
						matReqObj.put("mat_req_fty_name", vo.getMatReqFtyName());
						matReqObj.put("mat_type_name", vo.getMatTypeName());
						matReqObj.put("user_name", vo.getUserName());
						matReqObj.put("create_time", UtilString.getShortStringForDate(vo.getCreateTime()));
						matReqObj.put("status", vo.getStatus());
						matReqObj.put("status_name", vo.getStatusName());

						body.add(matReqObj);
					}
				}
			}

			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error(UtilString.STRING_EMPTY, e);
		}
		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, body);
	}

	/**
	 * 参考价格查询
	 * 
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/get_reference_price", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getReferencePrice(@RequestBody JSONObject json, CurrentUser user) {
		JSONArray body = new JSONArray();
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		try {
			int ftyId = json.getInt("fty_id");
			// 物料编码以逗号分隔
			String matCodes = json.getString("mat_codes");
			body = matReqService.getReferencePrice(matCodes.split(","), ftyId, user.getUserId());
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return UtilResult.getResultToJson(status, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), 5, body);
	}

	/**
	 * 暂存提交 估计saveBool submitBool判断是否提交
	 * 
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/save_and_submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveAndSubmit(@RequestBody JSONObject json, CurrentUser user) {
		JSONObject body = new JSONObject();
		// boolean saveBool = false;
		// boolean submitBool = false;
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();

		try {
			boolean submit = json.getBoolean("submit");
			// JSONObject headObj = json.getJSONObject("head");
			JSONArray itemAry = json.getJSONArray("item_list");
			JSONArray fileAry = json.getJSONArray("file_list");

			// int receipt_type = json.getInt("receipt_type");

			// 先判断是否新建领料单
			boolean newMatReq = false;
			int matReqId = 0;
			String matReqCode = null;
			if (json.containsKey("mat_req_id") && json.containsKey("mat_req_code")) {
				matReqId = json.getInt("mat_req_id");
				matReqCode = json.getString("mat_req_code");
			}
			BizMatReqHead bizMatReqHead = new BizMatReqHead();
			if (matReqId <= 0 && UtilString.isNullOrEmpty(matReqCode)) {
				matReqCode = commomService.getNextReceiptCode(EnumSequence.MAT_REQ.getValue());
				// body.put("lldh", lldh);
				newMatReq = true;
				bizMatReqHead.setMatReqId(null);
			} else {
				bizMatReqHead.setMatReqId(matReqId);
			}
			bizMatReqHead.setMatReqCode(matReqCode);

			bizMatReqHead.setReceiptType(EnumReceiptType.MAT_REQ_PRODUCTION.getValue());
			bizMatReqHead.setMatReqMatTypeId(UtilJSON.getIntFromJSON("mat_req_mat_type_id", json));// 物料类型
			bizMatReqHead.setMatReqBizTypeId(UtilJSON.getIntFromJSON("mat_req_biz_type_id", json));// 业务类型
			bizMatReqHead.setReceiveFtyId(UtilJSON.getIntFromJSON("receive_fty_id", json));// 接收工厂
			bizMatReqHead.setMatReqFtyId(UtilJSON.getIntFromJSON("mat_req_fty_id", json));// 领料工厂
			String matReqFtyCode = dictionaryService.getFtyCodeByFtyId(bizMatReqHead.getMatReqFtyId());
			// UtilJSON.getStringOrEmptyFromJSON("mat_req_fty_code", headObj);//
			// 领料工厂
			bizMatReqHead.setApplyFtyId(UtilJSON.getIntFromJSON("apply_fty_id", json));// 申请工厂
			bizMatReqHead.setInternalOrderCode(UtilJSON.getStringOrEmptyFromJSON("internal_order_code", json));// 内部订单
			bizMatReqHead.setInternalOrderName(UtilJSON.getStringOrEmptyFromJSON("internal_order_name", json));// 内部订单
			// bizMatReqHead.setInternalOrderCode(UtilString.STRING_EMPTY);//
			// 内部订单
			// bizMatReqHead.setInternalOrderName(UtilString.STRING_EMPTY);//
			// 内部订单
			bizMatReqHead.setUseDeptCode(UtilJSON.getStringOrEmptyFromJSON("use_dept_code", json)); // 领用区队/部门
			bizMatReqHead.setUseDeptName(UtilJSON.getStringOrEmptyFromJSON("use_dept_name", json));// 领用区队/部门
			bizMatReqHead.setIsBuildingProject(UtilJSON.getByteFromJSON("is_building_project", json));// 是否在建工程领料
			bizMatReqHead.setRemark(UtilJSON.getStringOrEmptyFromJSON("remark", json));// 备注
			bizMatReqHead.setIsPortable(UtilJSON.getByteFromJSON("is_portable", json));
			bizMatReqHead.setCreateUser(user.getUserId());
			if (newMatReq) {
				bizMatReqHead.setStatus(EnumMatReqStatus.MAT_REQ_STATUS_DRAFT.getValue());
			}

			//
			JSONArray bizMatReqHeadListForCheckMatnrForOther = new JSONArray();
			JSONObject bizMatReqHeadForCheckMatnrForOther;
			List<BizMatReqItem> bizMatReqItemList = new ArrayList<BizMatReqItem>();
			int rid = 0;
			boolean data_verification = true;
			BizMatReqItem bizMatReqItem;
			for (Object object : itemAry) {
				JSONObject itemObj = (JSONObject) object;
				bizMatReqItem = new BizMatReqItem();
				bizMatReqItem.setMatReqRid(++rid);
				bizMatReqItem.setMatId(UtilJSON.getIntFromJSON("mat_id", itemObj));// 物料编码
				String matCode = UtilJSON.getStringOrEmptyFromJSON("mat_code", itemObj);// 物料编码
				bizMatReqItem.setUnitId(UtilJSON.getIntFromJSON("unit_id", itemObj)); // 计量单位
				bizMatReqItem.setFtyId(bizMatReqHead.getMatReqFtyId());
				bizMatReqItem.setLocationId(UtilJSON.getIntFromJSON("location_id", itemObj));// 库存地点
				bizMatReqItem.setReferPrice(UtilJSON.getBigDecimalFromJSON("refer_price", itemObj)); // 参考价
				bizMatReqItem.setAssetAttr(UtilJSON.getByteFromJSON("asset_attr", itemObj));// 资产管控分类
				bizMatReqItem
						.setGeneralLedgerSubject(UtilJSON.getStringOrEmptyFromJSON("general_ledger_subject", itemObj));// 总账科目
				bizMatReqItem.setDemandQty(UtilJSON.getBigDecimalFromJSON("demand_qty", itemObj)); // 需求数量
				if (bizMatReqItem.getDemandQty().compareTo(BigDecimal.ZERO) <= 0) {
					data_verification = false;
				}
				bizMatReqItem.setDemandDate(UtilJSON.getDateFromJSON("demand_date", itemObj));// 需求日期
				bizMatReqItem.setWorkReceiptCode(UtilJSON.getStringOrEmptyFromJSON("work_receipt_code", itemObj)); // 工单号
				bizMatReqItem.setWorkReceiptName(UtilJSON.getStringOrEmptyFromJSON("work_receipt_name", itemObj)); // 工单号
				bizMatReqItem.setCostObjCode(UtilJSON.getStringOrEmptyFromJSON("work_receipt_code", itemObj)); // 工单号
				bizMatReqItem.setCostObjName(UtilJSON.getStringOrEmptyFromJSON("work_receipt_name", itemObj)); // 工单号
				bizMatReqItem.setDeviceCode(UtilJSON.getStringOrEmptyFromJSON("device_code", itemObj));// 设备号
				bizMatReqItem.setDeviceName(UtilJSON.getStringOrEmptyFromJSON("device_name", itemObj)); // 设备号
				bizMatReqItem.setRemark(UtilJSON.getStringOrEmptyFromJSON("remark", itemObj));// 备注使用用途

				bizMatReqItemList.add(bizMatReqItem);

				bizMatReqHeadForCheckMatnrForOther = new JSONObject();
				bizMatReqHeadForCheckMatnrForOther.put("ZLLDXH", String.valueOf(bizMatReqItem.getMatReqRid()));
				bizMatReqHeadForCheckMatnrForOther.put("MATNR", matCode);
				bizMatReqHeadForCheckMatnrForOther.put("WERKS", matReqFtyCode);
				bizMatReqHeadForCheckMatnrForOther.put("AUFNR", bizMatReqItem.getWorkReceiptCode());
				bizMatReqHeadListForCheckMatnrForOther.add(bizMatReqHeadForCheckMatnrForOther);
			}

			List<BizReceiptAttachment> bizReceiptAttachmentList = new ArrayList<BizReceiptAttachment>();
			BizReceiptAttachment bizReceiptAttachment;
			for (Object object : fileAry) {
				JSONObject attachmentObj = (JSONObject) object;
				bizReceiptAttachment = new BizReceiptAttachment();
				bizReceiptAttachment.setReceiptType(EnumReceiptType.MAT_REQ_PRODUCTION.getValue());
				bizReceiptAttachment.setUserId(user.getUserId());
				bizReceiptAttachment.setFileId(UtilJSON.getStringOrEmptyFromJSON("file_id", attachmentObj));
				bizReceiptAttachment.setFileName(UtilJSON.getStringOrEmptyFromJSON("file_name", attachmentObj));
				bizReceiptAttachment.setSize(attachmentObj.getInt("file_size"));
				String ext = UtilString.STRING_EMPTY;
				int extIndex = bizReceiptAttachment.getFileName().lastIndexOf('.');
				if (bizReceiptAttachment.getFileName().length() - extIndex < 10) {
					ext = bizReceiptAttachment.getFileName().substring(extIndex + 1);
				}

				bizReceiptAttachment.setExt(ext);
				bizReceiptAttachmentList.add(bizReceiptAttachment);
			}

			boolean saveLLD = true;// 存储是否可以保存领料单
			// 当业务类型选择其他时,调用接口29查看每个物料的ZTYPE[设备分类]是不是空

			// 2018-03-28其他中如果是研发类费用支出,不需要校验
			if (EnumMatReqMatType.MAT_REQ_MAT_TYPE_R_D_COST.getValue() != bizMatReqHead.getMatReqMatTypeId()) {
				// 2017-11-11 以上判断条件全部废弃,直接判断返回值.s就成功,e就失败
				if (EnumMatReqBizType.MAT_REQ_BIZ_TYPE_OTHER.getValue() == bizMatReqHead.getMatReqBizTypeId()) {
					saveLLD = false;
					String returnString = matReq.chechMatForOther(bizMatReqHeadListForCheckMatnrForOther,
							user.getUserId());
					if (returnString == null) {
						errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
						saveLLD = true;
					} else {
						errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
						errorString = returnString;
					}
				}
			}

			if (saveLLD) {

				// 执行领料单及领料单物料的修改
				// 针对领料单抬头执行修改,针对领料单物料执行先删除后添加
				body.put("mat_req_id", String.valueOf(matReqService.saveMatReq(newMatReq, bizMatReqHead,
						bizMatReqItemList, bizReceiptAttachmentList)));
				body.put("mat_req_code", bizMatReqHead.getMatReqCode());
				if (submit) {
					if (data_verification) {
						// 创建工作流
						// String url = String.format(Const.OA_PUSH_URL,
						// request.getRemoteAddr(), request.getServerPort());
						String url = String.format(Const.OA_PUSH_URL, UtilProperties.getInstance().getWmsUrl());
						String piid = matReqService.initWF(bizMatReqHead, user.getBoardId(), url);
						if (piid != null && piid.trim().length() > 0) {
							// 创建工作流成功,修改rkpf状态
							bizMatReqHead.setStatus(EnumMatReqStatus.MAT_REQ_STATUS_SUBMITTED.getValue());
							matReqService.updateStatus(bizMatReqHead);
							status = true;
							// submitBool = true;
						} else {
							errorCode = EnumErrorCode.ERROR_CODE_WORKFLOW_FAILURE.getValue();
							errorString = EnumErrorCode.ERROR_CODE_WORKFLOW_FAILURE.getName();
						}
					} else {
						// 必须填写需求数量
						errorCode = EnumErrorCode.ERROR_CODE_DEMAND_QTY_FAILURE.getValue();
						errorString = EnumErrorCode.ERROR_CODE_DEMAND_QTY_FAILURE.getName();
					}
				} else {
					status = true;
				}
			}
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			logger.error(UtilString.STRING_EMPTY, e);
		}

		// body.put("save", saveBool);
		// body.put("submit", submitBool);

		return UtilResult.getResultToJson(status, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);

	}

	/**
	 * 铁路板块 暂存提交 估计saveBool submitBool判断是否提交
	 * 
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/railway_save_and_submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveAndSubmitForRailway(@RequestBody JSONObject json, CurrentUser user) {
		JSONObject body = new JSONObject();
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();

		try {
			boolean submit = json.getBoolean("submit");
			JSONArray itemAry = json.getJSONArray("item_list");
			JSONArray fileAry = json.getJSONArray("file_list");

			// 先判断是否新建领料单
			boolean newMatReq = false;
			int matReqId = 0;
			String matReqCode = null;
			if (json.containsKey("mat_req_id") && json.containsKey("mat_req_code")) {
				matReqId = json.getInt("mat_req_id");
				matReqCode = json.getString("mat_req_code");
			}
			BizMatReqHead bizMatReqHead = new BizMatReqHead();
			if (matReqId <= 0 && UtilString.isNullOrEmpty(matReqCode)) {
				matReqCode = commomService.getNextReceiptCode(EnumSequence.MAT_REQ.getValue());
				newMatReq = true;
				bizMatReqHead.setMatReqId(null);
			} else {
				bizMatReqHead.setMatReqId(matReqId);
			}

			// 组装抬头
			bizMatReqHead.setMatReqCode(matReqCode);
			bizMatReqHead.setReceiptType(EnumReceiptType.MAT_REQ_PRODUCTION.getValue());
			bizMatReqHead.setMatReqMatTypeId(UtilJSON.getIntFromJSON("mat_req_mat_type_id", json));// 领用类型
			bizMatReqHead.setMatReqBizTypeId(0);// 业务类型
			bizMatReqHead.setReceiveFtyId(UtilJSON.getIntFromJSON("receive_fty_id", json));// 接收方
			bizMatReqHead.setMatReqFtyId(bizMatReqHead.getReceiveFtyId());// 领料工厂
																			// 和
																			// 接收方是一个工厂
			bizMatReqHead.setApplyFtyId(user.getFtyId());// 申请工厂
			bizMatReqHead.setInternalOrderCode(UtilJSON.getStringOrEmptyFromJSON("internal_order_code", json));// 内部订单
			bizMatReqHead.setInternalOrderName(UtilJSON.getStringOrEmptyFromJSON("internal_order_name", json));// 内部订单
			bizMatReqHead.setDeptCode(UtilJSON.getStringOrEmptyFromJSON("dept_code", json)); // 使用部门
			bizMatReqHead.setDeptName(UtilJSON.getStringOrEmptyFromJSON("dept_name", json));// 使用部门

			bizMatReqHead.setUseDeptCode(UtilJSON.getStringOrEmptyFromJSON("use_dept_code", json)); // 领用部门
																									// 改成接收方
																									// 2018-4-12
																									// 汪帮宇
			bizMatReqHead.setUseDeptName(UtilJSON.getStringOrEmptyFromJSON("use_dept_name", json));// 领用部门
																									// 改成接收方
																									// 2018-4-12
																									// 汪帮宇

			bizMatReqHead.setIsBuildingProject(UtilJSON.getByteFromJSON("is_building_project", json));// 是否在建工程领料
			bizMatReqHead.setRemark(UtilJSON.getStringOrEmptyFromJSON("remark", json));// 备注
			bizMatReqHead.setIsPortable(UtilJSON.getByteFromJSON("is_portable", json));
			// String matReqFtyCode =
			// dictionaryService.getFtyCodeByFtyId(bizMatReqHead.getMatReqFtyId());
			bizMatReqHead.setCreateUser(user.getUserId());
			if (newMatReq) {
				bizMatReqHead.setStatus(EnumMatReqStatus.MAT_REQ_STATUS_DRAFT.getValue());
			}

			// 组装明细
			List<BizMatReqItem> bizMatReqItemList = new ArrayList<BizMatReqItem>();
			int rid = 0;
			boolean data_verification = true;
			BizMatReqItem bizMatReqItem;
			for (Object object : itemAry) {
				JSONObject itemObj = (JSONObject) object;
				bizMatReqItem = new BizMatReqItem();
				bizMatReqItem.setMatReqRid(++rid);
				bizMatReqItem.setMatId(UtilJSON.getIntFromJSON("mat_id", itemObj));// 物料编码
				// String matCode =
				// UtilJSON.getStringOrEmptyFromJSON("mat_code", itemObj);//
				// 物料编码
				bizMatReqItem.setUnitId(UtilJSON.getIntFromJSON("unit_id", itemObj)); // 计量单位
				bizMatReqItem.setFtyId(bizMatReqHead.getMatReqFtyId());
				bizMatReqItem.setLocationId(UtilJSON.getIntFromJSON("location_id", itemObj));// 库存地点
				bizMatReqItem.setReferPrice(UtilJSON.getBigDecimalFromJSON("refer_price", itemObj)); // 参考价
				bizMatReqItem.setAssetAttr(UtilJSON.getByteFromJSON("asset_attr", itemObj));// 资产管控分类
				bizMatReqItem
						.setGeneralLedgerSubject(UtilJSON.getStringOrEmptyFromJSON("general_ledger_subject", itemObj));// 总账科目
				bizMatReqItem.setDemandQty(UtilJSON.getBigDecimalFromJSON("demand_qty", itemObj)); // 需求数量
				if (bizMatReqItem.getDemandQty().compareTo(BigDecimal.ZERO) <= 0) {
					data_verification = false;
				}
				bizMatReqItem.setDemandDate(UtilJSON.getDateFromJSON("demand_date", itemObj));// 需求日期
				bizMatReqItem.setWorkReceiptCode(UtilJSON.getStringOrEmptyFromJSON("work_receipt_code", itemObj)); // 工单号
				bizMatReqItem.setWorkReceiptName(UtilJSON.getStringOrEmptyFromJSON("work_receipt_name", itemObj)); // 工单号
				bizMatReqItem.setCostObjCode(UtilJSON.getStringOrEmptyFromJSON("work_receipt_code", itemObj)); // 工单号
				bizMatReqItem.setCostObjName(UtilJSON.getStringOrEmptyFromJSON("work_receipt_name", itemObj)); // 工单号
				bizMatReqItem.setDeviceCode(UtilJSON.getStringOrEmptyFromJSON("device_code", itemObj));// 设备号
				bizMatReqItem.setDeviceName(UtilJSON.getStringOrEmptyFromJSON("device_name", itemObj)); // 设备号
				bizMatReqItem.setRemark(UtilJSON.getStringOrEmptyFromJSON("remark", itemObj));// 备注使用用途

				bizMatReqItem.setCostObjCode(UtilJSON.getStringOrEmptyFromJSON("cost_obj_code", itemObj));// 成本对象
				bizMatReqItem.setCostObjName(UtilJSON.getStringOrEmptyFromJSON("cost_obj_name", itemObj));// 成本对象描述
				bizMatReqItem.setMoveTypeId(UtilJSON.getIntFromJSON("move_type_id", itemObj));// 移动类型
				bizMatReqItemList.add(bizMatReqItem);

			}

			// 组装附件
			List<BizReceiptAttachment> bizReceiptAttachmentList = new ArrayList<BizReceiptAttachment>();
			BizReceiptAttachment bizReceiptAttachment;
			for (Object object : fileAry) {
				JSONObject attachmentObj = (JSONObject) object;
				bizReceiptAttachment = new BizReceiptAttachment();
				bizReceiptAttachment.setReceiptType(EnumReceiptType.MAT_REQ_PRODUCTION.getValue());
				bizReceiptAttachment.setUserId(user.getUserId());
				bizReceiptAttachment.setFileId(UtilJSON.getStringOrEmptyFromJSON("file_id", attachmentObj));
				bizReceiptAttachment.setFileName(UtilJSON.getStringOrEmptyFromJSON("file_name", attachmentObj));
				bizReceiptAttachment.setSize(attachmentObj.getInt("file_size"));
				String ext = UtilString.STRING_EMPTY;
				int extIndex = bizReceiptAttachment.getFileName().lastIndexOf('.');
				if (bizReceiptAttachment.getFileName().length() - extIndex < 10) {
					ext = bizReceiptAttachment.getFileName().substring(extIndex + 1);
				}

				bizReceiptAttachment.setExt(ext);
				bizReceiptAttachmentList.add(bizReceiptAttachment);
			}

			boolean saveLLD = true;// 存储是否可以保存领料单
			if (saveLLD) {
				// 执行领料单及领料单物料的修改
				// 针对领料单抬头执行修改,针对领料单物料执行先删除后添加
				body.put("mat_req_id", String.valueOf(matReqService.saveMatReq(newMatReq, bizMatReqHead,
						bizMatReqItemList, bizReceiptAttachmentList)));
				body.put("mat_req_code", bizMatReqHead.getMatReqCode());
				if (submit) {
					if (data_verification) {
						// 创建工作流
						// String url = String.format(Const.OA_PUSH_URL,
						// request.getRemoteAddr(), request.getServerPort());
						String url = String.format(Const.OA_PUSH_URL, UtilProperties.getInstance().getWmsUrl());
						String piid = matReqService.initWF(bizMatReqHead, user.getBoardId(), url);
						if (piid != null && piid.trim().length() > 0) {
							// 创建工作流成功,修改rkpf状态
							bizMatReqHead.setStatus(EnumMatReqStatus.MAT_REQ_STATUS_SUBMITTED.getValue());
							matReqService.updateStatus(bizMatReqHead);
							status = true;
							// submitBool = true;
						} else {
							errorCode = EnumErrorCode.ERROR_CODE_WORKFLOW_FAILURE.getValue();
							errorString = EnumErrorCode.ERROR_CODE_WORKFLOW_FAILURE.getName();
						}
					} else {
						// 必须填写需求数量
						errorCode = EnumErrorCode.ERROR_CODE_DEMAND_QTY_FAILURE.getValue();
						errorString = EnumErrorCode.ERROR_CODE_DEMAND_QTY_FAILURE.getName();
					}
				} else {
					status = true;
				}
			}
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			logger.error(UtilString.STRING_EMPTY, e);
		}

		// body.put("save", saveBool);
		// body.put("submit", submitBool);

		return UtilResult.getResultToJson(status, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);

	}

	/**
	 * 删除领料单
	 * 
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/remove_mat_req/{mat_meq_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object removeMatReq(@PathVariable("mat_meq_id") int matReqId, CurrentUser user) {
		boolean body = false;
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		try {
			BizMatReqHeadVo matReqHead = matReqService.getMatReqHead(matReqId,
					EnumReceiptType.MAT_REQ_PRODUCTION.getValue());
			if (matReqHead.getStatus() == EnumMatReqStatus.MAT_REQ_STATUS_DRAFT.getValue()
					|| matReqHead.getStatus() == EnumMatReqStatus.MAT_REQ_STATUS_REJECT.getValue()) {
				body = matReqService.removeMatReq(matReqId);
				if (body) {
					errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
					status = true;
				}
			} else {
				errorCode = EnumErrorCode.ERROR_CODE_ONLY_DELETE_DRAFT.getValue();
			}
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return UtilResult.getResultToJson(status, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), 5, body);
	}

	/**
	 * 关闭领料单
	 * 
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/close_mat_req/{mat_meq_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object closeMatReq(@PathVariable("mat_meq_id") int matReqId, CurrentUser user) {
		boolean body = false;
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		try {
			BizMatReqHeadVo matReqHead = matReqService.getMatReqHead(matReqId,
					EnumReceiptType.MAT_REQ_PRODUCTION.getValue());
			// 非已关闭状态的领料单可以关闭
			if (matReqHead.getStatus() != EnumMatReqStatus.MAT_REQ_STATUS_CLOSED.getValue()) {
				body = matReqService.closeMatReq(matReqId);
				if (body) {
					errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
					status = true;
				}
			}
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return UtilResult.getResultToJson(status, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), 5, body);
	}

	/**
	 * 审批人审批
	 * 
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = {
			"/create_reserve/{mat_meq_id}", }, method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object createReserve(@PathVariable("mat_meq_id") int matReqId, CurrentUser user) {
		logger.info("POST requser with {}", matReqId);
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		try {
			String result = matReqService.createReserve(matReqId, user.getUserId());
			if (result != null) {
				status = false;
				errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
				errorString = result;
			}

		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
			logger.error("", e);
		}

		return UtilResult.getResultToJson(status, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), UtilString.STRING_EMPTY);

	}

	@RequestMapping(value = "/down_template", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void downTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BufferedOutputStream out = null;
		InputStream bis = null;

		try {
			String type = EnumTemplateType.TEMPLATE_MAT_REQ_MATERIAL_LIST.getValue();

			// 服务器端文件全路径
			String fileName;
			String downLoadName = EnumTemplateType.getDownLoadNameByValue(type);
			if (EnumTemplateType.toMap().containsKey(type)) {
				fileName = request.getSession().getServletContext().getRealPath("") + "template" + File.separator
						+ downLoadName;
			} else {
				fileName = "";
			}

			// 获取输入流
			bis = new BufferedInputStream(new FileInputStream(new File(fileName)));

			// 设置文件下载头

			response.addHeader("Content-Disposition", "attachment;filename=" + downLoadName);
			// 设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
			out = new BufferedOutputStream(response.getOutputStream());
			int len = 0;
			while ((len = bis.read()) != -1) {
				out.write(len);
				out.flush();
			}
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	@RequestMapping(value = "/upload_excel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void upLoadExcel(@RequestParam("file") MultipartFile fileInClient, @RequestParam("fty_code") String ftyCode,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			String fileName = matReqService.upLoadExcel(fileInClient,
					request.getSession().getServletContext().getRealPath(""));
			response.sendRedirect(
					"/wms/web/html/common/uploadresult.html?file_name=" + fileName + "&&fty_code=" + ftyCode);
		} catch (IOException e) {
			logger.error("", e);
		} catch (Exception e) {
			logger.error("", e);
		}

	}

	@RequestMapping(value = "/get_excel_data", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getExcelData(String file_name, String fty_code, HttpServletRequest request,
			CurrentUser user) {
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		JSONObject body = new JSONObject();
		try {
			body = matReqService.getExcelData(request.getSession().getServletContext().getRealPath(""), file_name,
					fty_code, user);
		} catch (Exception e) {
			logger.error("", e);
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, errorCode, body);
	}

}
