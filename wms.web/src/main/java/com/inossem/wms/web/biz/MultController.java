package com.inossem.wms.web.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.constant.Const;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.biz.BizReserveOrderHead;
import com.inossem.wms.model.dic.DicCorp;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.dic.DicMaterialReqBizType;
import com.inossem.wms.model.dic.DicMaterialReqMatType;
import com.inossem.wms.model.dic.DicReceiver;
import com.inossem.wms.model.dic.DicWarehouse;
import com.inossem.wms.model.dic.DicWarehouseArea;
import com.inossem.wms.model.dic.DicWarehousePallet;
import com.inossem.wms.model.dic.DicWarehousePosition;
import com.inossem.wms.model.enums.EnumBoard;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumMatReqStatus;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumSpecStock;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumStockType;
import com.inossem.wms.model.key.DicWarehousePositionKey;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.vo.ApproveUserVo;
import com.inossem.wms.model.vo.BizAllocateHeadVo;
import com.inossem.wms.model.vo.BizMatReqHeadVo;
import com.inossem.wms.model.vo.BizMatReqItemVo;
import com.inossem.wms.model.vo.BizStocktakeBaseInfoVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.model.vo.RelUserStockLocationVo;
import com.inossem.wms.model.vo.StockBatchVo;
import com.inossem.wms.model.vo.SupplierVo;
import com.inossem.wms.model.vo.WfApproveGroupItemVo;
import com.inossem.wms.model.wf.WfApproveGroupHead;
import com.inossem.wms.model.wf.WfApproveGroupItem;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.service.biz.IAllocService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IFileService;
import com.inossem.wms.service.biz.IInputService;
import com.inossem.wms.service.biz.IMatReqService;
import com.inossem.wms.service.biz.IMyReceiptService;
import com.inossem.wms.service.biz.IReturnService;
import com.inossem.wms.service.biz.IStocktakeService;
import com.inossem.wms.service.biz.ITaskService;
import com.inossem.wms.service.biz.IUrgenceService;
import com.inossem.wms.service.dic.IDicReceiverService;
import com.inossem.wms.service.dic.IDicStockLocationService;
import com.inossem.wms.service.intfc.IMatReq;
import com.inossem.wms.util.UtilConst;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class MultController {
	private static final Logger logger = LoggerFactory.getLogger(MultController.class);
	@Autowired
	private IDictionaryService dictionaryService;

	@Autowired
	private IDicStockLocationService dicStockLocationService;

	@Autowired
	private ITaskService taskServiceImpl;

	@Autowired
	private IMyReceiptService myReceiptService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IInputService rkglService;

	@Autowired
	private IMatReq matReq;

	@Autowired
	private IMatReqService matReqService;

	@Autowired
	private IFileService fileService;
	@Autowired
	private IAllocService allocService;

	@Autowired
	private IUrgenceService urgenceInAndOutStockService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private IReturnService returnService;

	@Resource
	private IDicReceiverService dicReceiverService;
	/**
	 * 库存盘点服务接口
	 */
	@Autowired
	private IStocktakeService stocktakeService;

	// 根据仓库号获取存储区
	@RequestMapping(value = { "/biz/task/warehouse/area_list/{wh_id}",
			"/biz/bizdoc/warehouse/area_list/{wh_id}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject areaList(@PathVariable("wh_id") int whId) {
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		JSONArray body = new JSONArray();
		List<DicWarehouseArea> list = new ArrayList<DicWarehouseArea>();
		try {
			list = taskServiceImpl.selectByKey(whId);
			JSONObject areaObject;
			for (DicWarehouseArea dicWarehouseArea : list) {
				if (UtilProperties.getInstance().getDefaultCCQ().equalsIgnoreCase(dicWarehouseArea.getAreaCode())) {
					continue;
				}
				areaObject = new JSONObject();
				areaObject.put("area_id", dicWarehouseArea.getAreaId());
				areaObject.put("area_code", dicWarehouseArea.getAreaCode());
				areaObject.put("area_name", dicWarehouseArea.getAreaName());
				body.add(areaObject);
			}
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("仓位列表", e);
		}

		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, 5, body);

	}

	// 获得所有仓库号和下拉存储区
	@RequestMapping(value = "/conf/area/warehouse_and_area", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject warehouseAndAreaList(CurrentUser user) {
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		JSONArray body = new JSONArray();
		List<DicWarehouseArea> arealist = new ArrayList<DicWarehouseArea>();
		List<DicWarehouse> warehouseList = new ArrayList<DicWarehouse>();
		try {
			warehouseList = commonService.selectAllWarehouse();
			if (warehouseList != null && warehouseList.size() > 0) {
				JSONObject warehouseObject = null;
				for (DicWarehouse dicWarehouse : warehouseList) {
					warehouseObject = new JSONObject();
					warehouseObject.put("wh_id", dicWarehouse.getWhId());
					warehouseObject.put("wh_code", dicWarehouse.getWhCode());
					warehouseObject.put("wh_name", dicWarehouse.getWhName());

					arealist = taskServiceImpl.selectByKey(dicWarehouse.getWhId());
					JSONArray areaAry = new JSONArray();
					if (arealist != null && arealist.size() > 0) {
						JSONObject areaObject;
						for (DicWarehouseArea dicWarehouseArea : arealist) {
							areaObject = new JSONObject();
							areaObject.put("area_id", dicWarehouseArea.getAreaId());
							areaObject.put("area_code", dicWarehouseArea.getAreaCode());
							areaObject.put("area_name", dicWarehouseArea.getAreaName());
							areaAry.add(areaObject);
						}
					}

					warehouseObject.put("area_list", areaAry);
					body.add(warehouseObject);
				}

			}

			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("获取所有仓库号存储区", e);
		}

		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, 5, body);

	}

	@RequestMapping(value = { "/biz/task/warehouse/position_list",
			"/biz/bizdoc/warehouse/position_list" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject positionList(@RequestBody JSONObject json) {
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		JSONObject body = new JSONObject();
		try {
			int whId = json.getInt("wh_id");// 仓库号
			int areaId = json.getInt("area_id");// 存储区
			String positionCode = json.getString("position_code");// 仓位

			DicWarehousePositionKey key = new DicWarehousePositionKey();
			key.setAreaId(areaId);
			key.setWhId(whId);
			key.setPositionCode(positionCode);

			DicWarehousePosition position = taskServiceImpl.selectDicWarehousePositionByKey(key);
			if (position == null) {
				body.put("position", false);
				body.put("havenMaterial", "");
			} else {
				int positionId = position.getPositionId();
				String areaCode = dictionaryService.getAreaCodeByAreaId(position.getAreaId());
				position.setAreaCode(areaCode);
				StockPosition stockPosition = new StockPosition();
				stockPosition.setWhId(whId);
				stockPosition.setAreaId(areaId);
				stockPosition.setPositionId(positionId);

				String havenMaterial = taskServiceImpl.getHavenMaterial(stockPosition);
				body.put("position", true);
				body.put("positionObj", position);
				body.put("havenMaterial", UtilString.getStringOrEmptyForObject(havenMaterial));
			}
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("仓位列表", e);
			body.put("position", false);
			body.put("havenMaterial", "");
		}

		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, 5, body);
	}

	/**
	 * 领料工厂 煤炭板块可以跨公司，非煤炭板块只能同公司
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/matreq/mat_req_factory",
			"/biz/matreqquery/mat_req_factory" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object matReqFactory(CurrentUser user) {
		boolean status = false;
		JSONArray body = new JSONArray();
		JSONObject ftyObj;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		try {/*

			if (EnumBoard.COAL.getValue() == user.getBoardId() || EnumBoard.RAILWAY.getValue() == user.getBoardId()) {
				// 煤炭板块跨公司，即工厂所属公司的板块2
				// 所有相同板块的公司的所有下属工厂可以领料
				// 即所有板块2的公司的所有下属工厂
				// list = llglService.getLLGCListByGC(user.getZgsbk());

				// 2017-10-18修改成直接查询用户的库存地点数据中的工厂,工厂需要去重
				List<RelUserStockLocationVo> list;
				if (EnumBoard.COAL.getValue() == user.getBoardId()) {
					list = userService.listLocForUser(user.getUserId());
				} else {
					list = userService.listLocForBoardId(user.getBoardId());
				}
				HashMap<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();
				for (RelUserStockLocationVo loc : list) {
					int ftyId = loc.getFtyId();
					// 去重
					if (!map.containsKey(ftyId)) {
						ftyObj = new JSONObject();
						ftyObj.put("fty_id", ftyId);
						ftyObj.put("fty_code", loc.getFtyCode());
						ftyObj.put("fty_name", loc.getFtyName());
						JSONArray ary = new JSONArray();
						JSONObject locObj = new JSONObject();
						locObj.put("location_id", loc.getLocationId());
						locObj.put("location_code", loc.getLocationCode());
						locObj.put("location_name", loc.getLocationName());
						ary.add(locObj);
						ftyObj.put("locations", ary);
						// body.add(ftyObj);
						map.put(ftyId, ftyObj);
					} else {
						ftyObj = map.get(ftyId);
						JSONArray ary = (JSONArray) (ftyObj.get("locations"));
						JSONObject locObj = new JSONObject();
						locObj.put("location_id", loc.getLocationId());
						locObj.put("location_code", loc.getLocationCode());
						locObj.put("location_name", loc.getLocationName());
						ary.add(locObj);
						ftyObj.put("locations", ary);
						// body.add(ftyObj);
						map.put(ftyId, ftyObj);
					}
				}
				for (Integer key : map.keySet()) {
					body.add(map.get(key));
				}

			} else {
				// 非煤炭板块不能跨公司，只能在同属一个公司下的工厂领料
				List<DicFactory> list = matReqService.getMatReqFactoryListByCorpId(user.getCorpId());
				for (DicFactory fty : list) {
					ftyObj = new JSONObject();
					ftyObj.put("fty_id", fty.getFtyId());
					ftyObj.put("fty_code", fty.getFtyCode());
					ftyObj.put("fty_name", fty.getFtyName());
					List<DicStockLocationVo> locList = dicStockLocationService.getLocationsByFtyId(fty.getFtyId());
					JSONArray ary = new JSONArray();
					JSONObject locObj;
					for (DicStockLocationVo loc : locList) {
						locObj = new JSONObject();
						locObj.put("location_id", loc.getLocationId());
						locObj.put("location_code", loc.getLocationCode());
						locObj.put("location_name", loc.getLocationName());
						ary.add(locObj);
					}
					ftyObj.put("locations", ary);
					body.add(ftyObj);
				}
			}
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		*/} catch (Exception e) {
			logger.error(UtilString.STRING_EMPTY, e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), 5, body);
	}

	/**
	 * 基础信息 创建领料单时查看，板块、物料类型、业务类型、部门列表、承包商列表、项目列表
	 * 
	 * @param zllfl
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/matreq/basic_info", "/biz/matreqquery/basic_info",
			"/biz/myreceipt/basic_info" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject basicInfo(String receipt_type, CurrentUser user) {
		JSONObject body = new JSONObject();
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		try {

			int receiptType = Integer.parseInt(receipt_type);

			body.put("user_name", user.getUserName());// 领料申请人
			body.put("apply_fty_id", user.getFtyId()); // "申请单位Id" 申请人工厂，
			body.put("apply_fty_code", user.getFtyCode()); // "申请单位编号" 申请人工厂，
			body.put("apply_fty_name", user.getFtyName()); // "申请单位名称" 申请人工厂，
			body.put("board_id", user.getBoardId()); // 业务板块
			// 业务板块描述
			body.put("board_name", EnumBoard.getNameByValue(user.getBoardId()));

			// 领料业务类型
			List<DicMaterialReqBizType> bizTypeList = matReqService
					.getMatReqBizTypeByReceiptTypeAndBoardId(user.getBoardId());

			JSONArray bizTypeAry = new JSONArray();
			JSONObject bizTypeObj;
			for (DicMaterialReqBizType bizType : bizTypeList) {
				bizTypeObj = new JSONObject();
				bizTypeObj.put("key", bizType.getMatReqBizTypeId());
				bizTypeObj.put("value", bizType.getBizTypeName());
				bizTypeAry.add(bizTypeObj);
			}

			body.put("bizType", bizTypeAry);

			// 领料物料类型
			List<DicMaterialReqMatType> matTypeList = matReqService.getMatReqMatTypeByBoardId(receiptType,
					user.getBoardId());

			JSONArray matTypeAry = new JSONArray();
			JSONObject matTypeObj;
			for (DicMaterialReqMatType matType : matTypeList) {
				matTypeObj = new JSONObject();
				matTypeObj.put("key", matType.getMatReqMatTypeId());
				matTypeObj.put("value", matType.getMatTypeName());
				matTypeAry.add(matTypeObj);
			}

			body.put("matType", matTypeAry);
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, 5, body);
	}

	// 获取领料单
	@RequestMapping(value = { "/biz/matreq/matreq/{mat_req_id}", "/biz/matreqquery/matreq/{mat_req_id}",
			"/biz/myreceipt/matreq/{mat_req_id}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getMatReq(@PathVariable("mat_req_id") int matReqId, CurrentUser user) {
		JSONObject body = new JSONObject();
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		try {
			// long llldh = Long.parseLong(lldh);
			BizMatReqHeadVo matReqHead = matReqService.getMatReqHead(matReqId,
					EnumReceiptType.MAT_REQ_PRODUCTION.getValue());

			// 在领料单提交状态时,需要审批,lasttask即是否最后一个审批任务，
			// 因为最后一个审批人【财务负责人】需要选择移动类型及成本对象
			if (EnumMatReqStatus.MAT_REQ_STATUS_SUBMITTED.getValue() == matReqHead.getStatus()) {
				body.put("lasttask", matReqService.lastTask(matReqId, matReqHead.getPiid(), user.getUserId()));
			} else {
				body.put("lasttask", false);
			}
			body.put("mat_req_id", matReqHead.getMatReqId());// 领料单id
			body.put("mat_req_code", matReqHead.getMatReqCode());// 领料单编号，
			body.put("apply_fty_id", UtilString.getStringOrEmptyForObject(matReqHead.getApplyFtyId()));// 申请单位
			body.put("apply_fty_code", UtilString.getStringOrEmptyForObject(matReqHead.getApplyFtyCode()));// 申请单位名称
			body.put("apply_fty_name", UtilString.getStringOrEmptyForObject(matReqHead.getApplyFtyName()));// 申请单位名称
			body.put("mat_req_fty_id", UtilString.getStringOrEmptyForObject(matReqHead.getMatReqFtyId()));// 领料工厂
			body.put("mat_req_fty_code", UtilString.getStringOrEmptyForObject(matReqHead.getMatReqFtyCode()));// 领料工厂名称
			body.put("mat_req_fty_name", UtilString.getStringOrEmptyForObject(matReqHead.getMatReqFtyName()));// 领料工厂名称
			body.put("receive_fty_id", UtilString.getStringOrEmptyForObject(matReqHead.getReceiveFtyId()));// 接收工厂
			body.put("receive_fty_code", UtilString.getStringOrEmptyForObject(matReqHead.getReceiveFtyCode()));// 接收方工厂名称
			body.put("receive_fty_name", UtilString.getStringOrEmptyForObject(matReqHead.getReceiveFtyName()));// 接收方工厂名称
			body.put("create_time", UtilString.getShortStringForDate(matReqHead.getCreateTime()));// 申请时间
			body.put("user_id", UtilString.getStringOrEmptyForObject(matReqHead.getCreateUser()));// 领料申请人
			body.put("user_name", UtilString.getStringOrEmptyForObject(matReqHead.getUserName()));// 领料申请人名称
			// body.put("zcjbm", rkpf.getZcjbm());
			body.put("mat_req_mat_type_id", UtilString.getStringOrEmptyForObject(matReqHead.getMatReqMatTypeId()));// 物料类型
			body.put("mat_type_name", matReqHead.getMatTypeName());// 物料类型
			body.put("mat_req_biz_type_id", UtilString.getStringOrEmptyForObject(matReqHead.getMatReqBizTypeId()));// 业务类型
			body.put("biz_type_name", matReqHead.getBizTypeName());// 物料类型
			body.put("internal_order_code", UtilString.getStringOrEmptyForObject(matReqHead.getInternalOrderCode()));// 内部订单
			body.put("internal_order_name", UtilString.getStringOrEmptyForObject(matReqHead.getInternalOrderName()));// 内部订单
			body.put("dept_code", UtilString.getStringOrEmptyForObject(matReqHead.getDeptCode()));// 使用部门
			body.put("dept_name", UtilString.getStringOrEmptyForObject(matReqHead.getDeptName()));// 使用部门
			body.put("use_dept_code", UtilString.getStringOrEmptyForObject(matReqHead.getUseDeptCode()));// 领用区队/部门
			body.put("use_dept_name", UtilString.getStringOrEmptyForObject(matReqHead.getUseDeptName()));// 领用区队/部门
			body.put("is_building_project", UtilString.getStringOrEmptyForObject(matReqHead.getIsBuildingProject()));// 在建工程标示，1非在建，2在建
			body.put("remark", UtilString.getStringOrEmptyForObject(matReqHead.getRemark()));// 备注
			body.put("contractor", UtilString.getStringOrEmptyForObject(matReqHead.getContractor()));// 承包商
			body.put("project", UtilString.getStringOrEmptyForObject(matReqHead.getProject()));// 项目，在建期填写
			body.put("status", matReqHead.getStatus());// 状态"1-草稿2-已提交3-审批通过4-驳回5-已完成"
			body.put("status_name", matReqHead.getStatusName());// 状态"1-草稿2-已提交3-审批通过4-驳回5-已完成"
			body.put("board_id", matReqHead.getBoardId());// 所属板块，"1-煤制油板块2-煤炭板块3-铁路板块4-其他板块"
			body.put("is_portable", matReqHead.getIsPortable());// 是否移动设备
			body.put("board_name", EnumBoard.getNameByValue(matReqHead.getBoardId()));

			JSONArray matReqItemAry = new JSONArray();
			JSONObject matReqItemObj;

			List<BizMatReqItemVo> list = matReqService.getMatReqItemList(matReqId);
			for (BizMatReqItemVo matReqItem : list) {

				matReqItemObj = new JSONObject();

				matReqItemObj.put("mat_req_id", matReqItem.getMatReqId());// 领料单行号
				matReqItemObj.put("mat_id", matReqItem.getMatId());// 物料id
				matReqItemObj.put("mat_code", matReqItem.getMatCode());// 物料编码
				matReqItemObj.put("mat_name", matReqItem.getMatName());// 物料描述
				matReqItemObj.put("unit_id", matReqItem.getUnitId());// 计量单位
				matReqItemObj.put("unit_code", matReqItem.getUnitCode());// 计量单位
				matReqItemObj.put("unit_name", matReqItem.getUnitName());// 计量单位描述
				matReqItemObj.put("location_id", matReqItem.getLocationId());// 库存地点
				matReqItemObj.put("location_code", matReqItem.getLocationCode());// 库存地点
				matReqItemObj.put("location_name", matReqItem.getLocationName());// 库存地点
				// matReqItemObj.put("werks", matReqItem.getWerks());// 领料工厂
				matReqItemObj.put("current_qty", UtilString.getStringForBigDecimal(matReqItem.getCurrentQty()));// 当前库存
				matReqItemObj.put("refer_price", UtilString.getStringForBigDecimal(matReqItem.getReferPrice()));// 参考价格
				matReqItemObj.put("asset_attr", UtilString.getStringOrEmptyForByte(matReqItem.getAssetAttr()));// 资产管控分类
				matReqItemObj.put("cost_obj_code", matReqItem.getCostObjCode());// 成本对象
				matReqItemObj.put("cost_obj_name", matReqItem.getCostObjName());// 成本对象描述
				matReqItemObj.put("move_type_id", matReqItem.getMoveTypeId());// 移动类型
				matReqItemObj.put("move_type_code", matReqItem.getMoveTypeCode());// 移动类型
				// 移动类型
				matReqItemObj.put("move_type_name", matReqItem.getMoveTypeName());// 移动类型

				matReqItemObj.put("spec_stock", matReqItem.getSpecStock());// 特殊库存标识
				matReqItemObj.put("demand_qty", UtilString.getStringForBigDecimal(matReqItem.getDemandQty()));// 需求数量
				matReqItemObj.put("demand_date", UtilString.getShortStringForDate(matReqItem.getDemandDate()));// 需求数量
				matReqItemObj.put("work_receipt_code",
						UtilString.getStringOrEmptyForObject(matReqItem.getWorkReceiptCode()));// 工单号
				matReqItemObj.put("work_receipt_name",
						UtilString.getStringOrEmptyForObject(matReqItem.getWorkReceiptName()));// 工单号
				matReqItemObj.put("device_code", UtilString.getStringOrEmptyForObject(matReqItem.getDeviceCode()));// 设备号
				matReqItemObj.put("device_name", UtilString.getStringOrEmptyForObject(matReqItem.getDeviceName()));// 设备号
				matReqItemObj.put("remark", matReqItem.getRemark());// 备注/使用用途
				matReqItemObj.put("take_delivery_qty", matReqItem.getTakeDeliveryQty());// 已提货数量
				matReqItemObj.put("general_ledger_subject",
						UtilString.getStringOrEmptyForObject(matReqItem.getGeneralLedgerSubject()));// 总账科目
				matReqItemObj.put("decimal_place", UtilString.getStringOrEmptyForObject(matReqItem.getDecimalPlace()));// 计量单位浮点数
				// lldiObj.put("rsnum",
				// StringUtil.objToString(resb.getRsnum()));// 预留号'
				// lldiObj.put("rspos",
				// StringUtil.objToString(resb.getRspos()));// 预留行项目'
				// lldiObj.put("ebeln",
				// StringUtil.objToString(resb.getEbeln()));// 采购订单号'
				// lldiObj.put("ebelp",
				// StringUtil.objToString(resb.getEbelp()));// 采购订单行号'
				matReqItemAry.add(matReqItemObj);//
			}
			body.put("item_list", matReqItemAry);

			body.put("file_list",
					fileService.getReceiptAttachments(matReqId, EnumReceiptType.MAT_REQ_PRODUCTION.getValue()));

			body.put("user_list",
					commonService.getReceiptUserForMatReq(matReqId, EnumReceiptType.MAT_REQ_PRODUCTION.getValue()));

			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error(UtilString.STRING_EMPTY, e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, 5, body);
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

			list = matReqService.getStockBatch(locationId, condition, specStock, matType);
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
	 * 成本中心查询 1领用区队/部门 2内部订单 3设备号
	 * 
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/matreq/get_cost_center", "/biz/matreqquery/get_cost_center",
			"/biz/myreceipt/get_cost_center" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getCostCenter(@RequestBody JSONObject json, CurrentUser user) {
		JSONArray body = new JSONArray();
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		try {
			// String zpiid = llglService.initProcess(Long.parseLong(zlldh));
			int ftyId = json.getInt("fty_id");// 工厂
			String costObjCode = json.getString("cost_obj_code");// 代码
			String costObjName = json.getString("cost_obj_name"); // 描述
			String matCode = json.getString("mat_code"); // 物料编码
			// 类型 1-成本中心 2-工单 3-设备号 4-资产号 5-WBS 6-总账科目
			int costObjType = json.getInt("cost_obj_type");

			String ftyCode = dictionaryService.getFtyCodeByFtyId(ftyId);

			// JSONArray ckjAry = new JSONArray();
			body = matReq.getCostCenter(ftyCode, costObjCode, costObjName, matCode, costObjType, user.getUserId());
			// body.put("ckj", ckjAry);

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return UtilResult.getResultToJson(status, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
	}

	/**
	 * 成本对象查询
	 * 
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/biz/myreceipt/get_cost_object", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getCostObject(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = false;
		JSONArray body = new JSONArray();
		try {
			// String zpiid = llglService.initProcess(Long.parseLong(zlldh));
			int ftyId = json.getInt("fty_Id");// 工厂
			String costObjCode = json.getString("cost_obj_code");// 成本对象
			String costObjName = json.getString("cost_obj_name"); // 成本对象描述
			String moveType = json.getString("move_type"); // 移动类型
			String ftyCode = dictionaryService.getFtyCodeByFtyId(ftyId);
			// JSONArray ckjAry = new JSONArray();
			body = matReq.getCostObject(moveType, ftyCode, costObjCode, costObjName, user.getUserId());
			// body.put("ckj", ckjAry);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			status = true;
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return UtilResult.getResultToJson(status, errorCode, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
	}

	/**
	 * 15.1.2 调拨单详情
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = { "/biz/alloc/alloc/alloc_info/{allocate_id}",
			"/biz/myreceipt/alloc/alloc_info/{allocate_id}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getAllocInfo(CurrentUser cuser, @PathVariable("allocate_id") Integer allocate_id) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();

		// List<Map<String, Object>> allot_jbr_list = new ArrayList<Map<String,
		// Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("allocateId", allocate_id);
		// map.put("createUser", cuser.getUserId());

		Map<String, Object> body = new HashMap<String, Object>();

		List<BizAllocateHeadVo> allotList = new ArrayList<BizAllocateHeadVo>();

		try {
			allotList = allocService.getAllotListByCondition(map);

			if (allotList != null && allotList.size() == 1) {
				BizAllocateHeadVo allotObj = allotList.get(0);
				body.put("allocate_id", allotObj.getAllocateId());
				body.put("allocate_code", allotObj.getAllocateCode());
				body.put("corp_name", allotObj.getCorpName());
				body.put("org_name", allotObj.getOrgName());
				body.put("create_time", UtilString.getShortStringForDate(allotObj.getCreateTime()));

				body.put("create_user_name", allotObj.getUserName());
				body.put("demand_date", UtilString.getShortStringForDate(allotObj.getDemandDate()));
				body.put("fty_input", allotObj.getFtyInput());
				body.put("fty_input_name", allotObj.getFtyName());

				body.put("location_input", allotObj.getLocationInput());
				body.put("location_input_name", allotObj.getLocationName());

				body.put("distributed", allotObj.getDistributed());
				body.put("remark", allotObj.getRemark());
				body.put("status", allotObj.getStatus());
				body.put("status_name", UtilConst.getStatusNameByAllocate(allotObj.getStatus()));

				// 行项目
				Map<String, Object> itemmap = new HashMap<String, Object>();
				itemmap.put("allocateId", allotObj.getAllocateId());
				List<Map<String, Object>> itemList = allocService.getAllotItemsByCondition(itemmap);

				if (itemList != null && itemList.size() > 0) {
					for (Map<String, Object> mapItem : itemList)
						if (allotObj.getStatus() == 0) {
							Map<String, Object> nummap = new HashMap<String, Object>();
							nummap.put("ftyId", allotObj.getFtyOutput());
							nummap.put("locationId", allotObj.getLocationOutput());
							nummap.put("matId", mapItem.get("mat_id"));

							List<StockBatchVo> findList = allocService.getMaterials(nummap);
							if (findList != null && findList.size() == 1) {
								mapItem.put("stock_qty", findList.get(0).getQty());
							}
						}
				}

				body.put("item_list", itemList);
				// 经办人
				List<ApproveUserVo> receiptUser = commonService.getReceiptUser(allotObj.getAllocateId(),
						EnumReceiptType.ALLOCATE.getValue());

				body.put("user_list", receiptUser);

				body.put("file_list", fileService.getReceiptAttachments(allotObj.getAllocateId(),
						EnumReceiptType.ALLOCATE.getValue()));

				body.put("fty_output", allotObj.getFtyOutput());
				body.put("fty_output_name", allotObj.getFtyOutputName());
				body.put("location_output", allotObj.getLocationOutput());
				body.put("location_output_name", allotObj.getLocationOutputName());
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			} else {
				errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
			}
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("调拨单详情", e);
		}
		return UtilResult.getResultToJson(true, errorCode, body);
	}

	/**
	 * 物料描述列表
	 * 
	 * @param id
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/other/11/get_material_list", "/biz/urgence/get_in_mat",
			"/biz/bizdoc/get_material_list" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getMaterialList(String condition) {
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		List<DicMaterial> list = new ArrayList<DicMaterial>();
		try {
			DicMaterial material = new DicMaterial();
			material.setCondition(condition);

			list = rkglService.getMaterialList(material);
		} catch (Exception e) {
			logger.error("物料描述列表", e);
		}
		JSONObject obj = UtilResult.getResultToJson(true, errorCode, 0, 10, 5, list);
		return obj;

	}

	/**
	 * 查询出库物料
	 * 
	 * @date 2017年11月21日
	 * @author sangjl
	 * @param json
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/urgence/get_out_mat" }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")

	public @ResponseBody JSONObject getOutMat(@RequestBody JSONObject json) {
		String condition = json.getString("condition").trim();

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("condition", condition);
		param.put("locationId", json.get("location_id"));
		if (json.has("spec_stock")) {
			param.put("specStock", UtilObject.getStringOrEmpty(json.get("spec_stock")));
		}
		if (json.has("receipt_type")) {
			param.put("receiptType", json.get("receipt_type"));
		}

		List<Map<String, Object>> list = urgenceInAndOutStockService.getOutMat(param);
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		return UtilResult.getResultToJson(true, error_code, 0, 10, 5, list);
	}

	@RequestMapping(value = { "/biz/input/other/roles", "/biz/input/exempt/roles", "/biz/input/inspect/roles",
			"/biz/input/allocate/roles", "/biz/input/platform/roles", "/biz/output/llck/roles",
			"/biz/output/xsck/roles", "/biz/output/cgth/roles", "/biz/output/qtck/roles", "/biz/transport/roles",
			"/biz/urgence/roles", "/biz/return/matreq/roles", "/biz/return/sale/roles", "/biz/output/dbck/roles",
			"/biz/output/ylck/roles", "/biz/return/reserve/roles",
			"/biz/alloc/alloc/roles" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getRoles() {

		List<Map<String, String>> roles = new ArrayList<Map<String, String>>();
		try {
			roles = commonService.getOperatorRoles();
		} catch (Exception e) {
			logger.error("经办人角色（岗位）", e);
		}

		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		return UtilResult.getResultToJson(true, errorCode, roles);
	}

	/**
	 * 用户查询 角色
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/exempt/user_list", "/biz/input/platform/user_list",
			"/biz/input/allocate/user_list", "/biz/input/other/user_list", "/biz/output/llck/user_list",
			"/biz/output/xsck/user_list", "/biz/output/dbck/user_list", "/biz/output/cgth/user_list",
			"/biz/output/ylck/user_list", "/biz/output/qtck/user_list", "/biz/transport/user_list",
			"/biz/urgence/user_list", "/biz/return/matreq/user_list", "/biz/return/sale/user_list",
			"/biz/return/reserve/user_list", "/biz/alloc/alloc/user_list",
			"/auth/user_list" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
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
		return UtilResult.getResultToJson(true, errorCode, userList);
	}

	/**
	 * 用户查询 角色
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = { "/conf/jobstatis/user_list",
			"/auth/approve/user_list" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getUserList(String condition) {

		List<User> findList = new ArrayList<User>();
		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
		try {
			User user = new User();
			if (StringUtils.hasText(condition)) {
				String sqlStr = "( user_name like '%" + condition + "%' or ";
				sqlStr += " user_id like '%" + condition + "%' or ";
				sqlStr += " org_name like '%" + condition + "%' )";
				sqlStr += " AND u.corp_id !=0 AND u.fty_id !=0";
				user.setSql(sqlStr);

			}

			findList = commonService.getUsers(user);
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
		return UtilResult.getResultToJson(true, errorCode, userList);
	}

	/**
	 * 获取库存状态和特殊库存
	 * 
	 * @author 刘宇2018.02.09
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = {
			"/biz/stockquery/get_status_and_spec_stock" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getStatusAndSpecStock(CurrentUser cUser) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		JSONObject StatusAndSpecStockJSon = new JSONObject();
		try {
			StatusAndSpecStockJSon.put("status", EnumStockStatus.toList());
			StatusAndSpecStockJSon.put("stock_type", EnumStockType.toList());
			StatusAndSpecStockJSon.put("spec_stock", EnumSpecStock.toList());
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
		}
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total,
				StatusAndSpecStockJSon);
	}

	/**
	 * 审批人审批
	 * 
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/matreq/task_mat_req_approve",
			"/biz/myreceipt/task_mat_req_approve" }, method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object taskMatReqApprove(@RequestBody JSONObject json, CurrentUser user) {
		logger.info("POST requser with {}", json);
		// HashMap<String, Object> obj = new HashMap<String, Object>();
		// JSONObject head = new JSONObject();
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		try {/*

			// String error_string = "Success";
			int receiptId = json.getInt("receipt_id");
			String processInstanceId = json.getString("piid");
			// String inspect_person = json.getString("inspect_person");
			String comment = json.getString("comment");
			boolean approve = json.getBoolean("approve");

			// String url = String.format(Const.OA_PUSH_URL,
			// request.getRemoteAddr(), request.getServerPort());
			String url = String.format(Const.OA_PUSH_URL, UtilProperties.getInstance().getWmsUrl());
			// 是否最后审批人
			if (json.containsKey("lasttask") && json.getBoolean("lasttask")) {
				// 如果是审批不通过,和其他审批人执行相同的方法
				if (approve) {
					String deptCode = UtilJSON.getStringOrEmptyFromJSON("dept_code", json);
					String deptName = UtilJSON.getStringOrEmptyFromJSON("dept_name", json);
					JSONArray itemList = json.getJSONArray("item_list");
					String resultMsg = myReceiptService.lastTaskApprove(receiptId, processInstanceId, user.getUserId(),
							deptCode, deptName, itemList, comment, url);
					if (resultMsg != null) {
						status = false;
						errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
						errorString = resultMsg;
					} else {
						// 如果审批完成,直接创建预留单,是否创建成功都不管
						resultMsg = matReqService.createReserve(receiptId, user.getUserId());
					}
				} else {
					int result = myReceiptService.taskApprove(receiptId, processInstanceId, user.getUserId(), approve,
							comment, url);
					if (result < 0) {
						status = false;
						errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
						errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
					}
				}
			} else {
				int resultStatus = myReceiptService.taskApprove(receiptId, processInstanceId, user.getUserId(), approve,
						comment, url);
				if (resultStatus < 0) {
					status = false;
					errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
					errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
				} else if (resultStatus == EnumMatReqStatus.MAT_REQ_STATUS_APPROVED.getValue()) {
					// 如果审批完成,直接创建预留单,是否创建成功都不管
					BizMatReqHeadVo head = matReqService.getMatReqHead(receiptId,
							EnumReceiptType.MAT_REQ_PRODUCTION.getValue());
					User createUser = userService.getUserById(head.getCreateUser());
					DicCorp corp = dictionaryService.getCorpIdMap().get(createUser.getCorpId());
					if (corp.getBoardId() == EnumBoard.RAILWAY.getValue()) {
						String resultMsg = matReqService.createReserve(receiptId, user.getUserId());
						// to do

					} else {
						head.setStatus(EnumMatReqStatus.MAT_REQ_STATUS_COMPLETED.getValue());
						matReqService.updateStatus(head);
					}
				} else {
					// 审批通过但未完成
				}
			}
		*/} catch (WMSException e) {
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
		return UtilResult.getResultToJson(true, errorCode, returnList);

	}

	/**
	 * 常用经办人组基本信息获取(入库)
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/inspect/get_operator_group", "/biz/input/exempt/get_operator_group",
			"/biz/input/platform/get_operator_group", "/biz/input/other/get_operator_group",
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
	@RequestMapping(value = { "/biz/output/llck/get_operator_group", "/biz/output/xsck/get_operator_group",
			"/biz/output/cgth/get_operator_group", "/biz/output/qtck/get_operator_group",
			"/biz/output/dbck/get_operator_group", "/biz/output/ylck/get_operator_group",
			"/biz/return/matreq/get_operator_group", "/biz/return/sale/get_operator_group",
			"/biz/return/reserve/get_operator_group", "/biz/alloc/alloc/get_operator_group",
			"/biz/alloc/delivery/get_operator_group" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
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

	/**
	 * @Description: 【进入修改状态的盘点单时】根据盘点凭证号获取盘点凭证基本信息【不包含明细数据，只有抬头信息和汇总的明细数据信息】
	 *               【07-01-2.jpg 库存盘点-创建盘点表-盘点详情页面数据接口】 【07-02-2.jpg
	 *               库存盘点-盘点结果录入-盘点详情页面数据接口】 【07-03-2.jpg
	 *               库存盘点-盘点结果确认-盘点详情页面数据接口】 【07-04-2.jpg
	 *               库存盘点-盘点结果过账-盘点详情页面数据接口】
	 * @param ivnum
	 *            盘点凭证号
	 * @param ymbh
	 *            创建人传1，盘点人传2，默认传1
	 * @return
	 */
//	@RequestMapping(value = { "/biz/stocktake/stocktake/{stocktake_id}/{ymbh}",
//			"/biz/myreceipt/stocktake/{stocktake_id}/{ymbh}" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
//	public @ResponseBody Map<String, Object> findpdpzBypdpzh(@PathVariable("stocktake_id") Integer stocktakeId,
//			@PathVariable("ymbh") String ymbh, CurrentUser cUser) throws Exception {
//
//		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
//		try {
//			String userId = cUser.getUserId();
//			BizStocktakeBaseInfoVo dbLink = stocktakeService.findStocktakeHeadByStocktakeId(stocktakeId, ymbh, userId);
//			return UtilResult.getResultToMap(true, errorCode, dbLink);
//		} catch (Exception e) {
//			logger.error("", e);
//			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
//			return UtilResult.getResultToMap(false, errorCode, new Object());
//		}
//	}

	// // 获取审批人类别
	// @RequestMapping(value = { "/biz/stocktake/sprlist/{stocktake_id}",
	// "/biz/myreceipt/sprkcpdlist/{stocktake_id}" }, method =
	// RequestMethod.GET, produces = "application/json;charset=UTF-8")
	// public @ResponseBody Object getKCPDSprList(@PathVariable("stocktake_id")
	// String stocktakeId) {
	// // JSONArray body = new JSONArray();
	// // ArrayList<HashMap<String, Object>> userListMap = new
	// // ArrayList<HashMap<String, Object>>();
	// JSONArray jsonary = new JSONArray();
	// try {
	// In livnum = Integer.parseInt(stocktakeId);
	//
	// List<ReceiptApprove> list =
	// wdspService.getSPUserByReceiptIDAndType(livnum, Const.RECEIPT_TYPE_KCPD);
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	// if (list != null && list.size() > 0) {
	//
	// for (ReceiptApprove ra : list) {
	// JSONObject taskJson = new JSONObject();
	//
	// taskJson.put("butxt", ra.getButxt());
	// taskJson.put("zbm", ra.getOrg_name());
	// taskJson.put("user_name", ra.getUser_name());
	//
	// String approve_name;
	// if (ra.getApprove() == null) {
	// taskJson.put("approve", "--");
	// approve_name = "--";
	// } else {
	// taskJson.put("approve", ra.getApprove());
	// switch (ra.getApprove()) {
	// case 0:
	// approve_name = "--";
	// break;
	// case 1:
	// approve_name = "通过";
	// break;
	// case 2:
	// approve_name = "不通过";
	// break;
	// case 3:
	// approve_name = "被动不通过";
	// break;
	// default:
	// approve_name = "--";
	// break;
	// }
	// }
	// taskJson.put("approve_name", approve_name);
	// taskJson.put("comment", ra.getComment());
	// if (ra.getApproveTime() == null) {
	// taskJson.put("approve_time", "--");
	// } else {
	// taskJson.put("approve_time", sdf.format(ra.getApproveTime()));
	// }
	//
	// taskJson.put("id", ra.getUserId());
	// // taskJson.put("spdw", ra.getButxt());
	//
	// // taskJson.put("zname", ra.getUser_name());
	// jsonary.add(taskJson);
	// }
	// }
	// } catch (Exception e) {
	// // logger.error("", e);
	// }
	//
	// JSONObject head = new JSONObject();
	// head.put("error_code", 0);
	// head.put("error_string", "Success");
	// head.put("page_index", 0);
	// head.put("page_size", 1);
	// head.put("total", 5);
	//
	// JSONObject obj = new JSONObject();
	// obj.put("head", head);
	// obj.put("body", jsonary);
	// return obj;
	// }

	@RequestMapping(value = { "/conf/jobstatis/get_base_info", "/biz/turnover/get_base_info",
			"/biz/stockquery/get_base_info" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getDataAnalysisBaseInfo(CurrentUser cUser) throws Exception {

		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		JSONObject body = new JSONObject();
		boolean status = true;
		try {
			body = commonService.listBoardFtyAndLocationForUser(cUser.getUserId());
		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}
		return UtilResult.getResultToMap(status, errorCode, body);
	}

	/**
	 * 供应商列表
	 * 
	 * @param id
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = { "/biz/input/other/get_supplier_list",
			"/biz/transport/other/get_supplier_list" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getSupplierList(String supplier_code, String supplier_name) {

		List<SupplierVo> list = new ArrayList<SupplierVo>();
		Integer errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		Integer pageIndex = EnumPage.PAGE_INDEX.getValue();
		Integer pageSize = EnumPage.PAGE_SIZE.getValue();
		Integer total = EnumPage.TOTAL_COUNT.getValue();
		try {
			SupplierVo find = new SupplierVo();
			find.setSupplierCode(supplier_code);
			find.setSupplierName(supplier_name);

			list = commonService.getSupplierList(find);

		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("供应商列表", e);
		}

		JSONObject obj = UtilResult.getResultToJson(true, errorCode, pageIndex, pageSize, total, list);
		return obj;
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
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
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
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (WMSException e) {
			errorCode = e.getErrorCode();
			errorString = e.getMessage();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error("获取预留单列表", e);
		}

		// return UtilResult.getResultToJson(status, error_code, list);
		JSONObject object = UtilResult.getResultToJson(status, errorCode, list);
		UtilJSONConvert.setNullToEmpty(object);
		return object;
	}

	// MyTask 其他出库

	/**
	 * 获取所有接收方
	 */
	@RequestMapping(value = { "/biz/output/qtck/get_jsf",
			"/biz/matreq/get_jsf" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listAllDicReceiver(CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONArray body = new JSONArray();
		try {
			List<DicReceiver> receiverList = dicReceiverService.listAllDicReceiver();

			for (DicReceiver receiver : receiverList) {
				JSONObject receiverJSon = new JSONObject();
				receiverJSon.put("id", receiver.getId());// 接收方表主键
				receiverJSon.put("receive_code", receiver.getReceiveCode());// 接收方代码
				receiverJSon.put("receive_name", receiver.getReceiveName());// 接收方描述
				body.add(receiverJSon);
			}
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
			logger.error("获取接收方 --", e);
		}
		return UtilResult.getResultToJson(status, errorCode, 1, 50, 50, body);
	}

	// 上架作业-1.1.8 获取新托盘
	@RequestMapping(value = { "/biz/task/shelves/get_new_pallet", "/biz/task/removal/get_new_pallet","/biz/task/pallet/get_new_pallet",
			"/biz/urgent/product/transport/get_new_pallet",	"/biz/product/transport/get_new_pallet" ,"/biz/group_task/get_new_pallet"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getNewPallet(Integer pallet_type_id,CurrentUser cUser) {

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
