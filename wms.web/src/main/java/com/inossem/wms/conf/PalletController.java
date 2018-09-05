package com.inossem.wms.conf;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicWarehousePallet;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.dic.IDicWarehousePalletService;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 托盘管理数据传输类
 * 
 * @author 刘宇 2018.01.23
 */
@Controller
@RequestMapping("/conf/pallet")
public class PalletController {

	private static final Logger logger = LoggerFactory.getLogger(PalletController.class);

	@Resource
	private IDicWarehousePalletService palletService;
	@Resource
	private ICommonService  commonService;
	/** 
	 * 查看托盘列表/关键字查询
	 * 
	 * @author 刘宇 2018.01.09
	 * @return
	 */
	@RequestMapping(value = "/list_pallet", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listPallet(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		Integer aInteger = null;
		JSONArray body = new JSONArray();
		try {
			String condition = json.getString("condition");
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			List<DicWarehousePallet> pallets = palletService.listPallets(pageSize, pageIndex, total, condition,
					UtilString.STRING_EMPTY, aInteger, sortAscend, sortColumn); // 获取托盘列表页托盘集合
			if (pallets.size() > 0) {
				total = pallets.get(0).getTotalCount();
			}
			for (DicWarehousePallet palletOne : pallets) {
				JSONObject palletObject = new JSONObject();
				palletObject.put("pallet_id", palletOne.getPalletId()); // 托盘表自增主键
				palletObject.put("pallet_code", palletOne.getPalletCode()); // 托盘号
				//palletObject.put("pallet_name", palletOne.getPalletName()); // 托盘类型
				//palletObject.put("wh_id", palletOne.getWhId()); // 仓库号
				//palletObject.put("wh_name", palletOne.getWh_name()); // 仓库描述
				palletObject.put("freeze", palletOne.getFreeze()); // 冻结标示 0 非冻结
																	// // 1冻结
				palletObject.put("freeze_id", palletOne.getFreezeId()); // 默认0
				//palletObject.put("status", palletOne.getStatus()); // 空托盘标识,0-空
				palletObject.put("max_weight", palletOne.getMaxWeight()); // 最大承重
				//palletObject.put("unit_weight", palletOne.getUnitWeight()); // 最大承重单位
				palletObject.put("is_delete", palletOne.getIsDelete()); // 空托盘标识,0-空
				palletObject.put("create_time", UtilString.getLongStringForDate(palletOne.getCreateTime())); // 最大承重
				palletObject.put("modify_time", UtilString.getLongStringForDate(palletOne.getModifyTime())); // 最大承重单位
				palletObject.put("create_user", palletOne.getCreateUser());
				palletObject.put("create_user_name", palletOne.getCreateUserName());
				body.add(palletObject);
			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
		}
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

	/**
	 * 添加托盘
	 * 
	 * @author 刘宇 2018.01.08
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/add_pallet", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addPallet(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "成功";
		boolean errorStatus = true;
        String pallet="";
		try {	
			
			String palletCode =commonService.getNextReceiptCode("warehouse_pallet"); // 托盘号
			while (palletCode.length() < 5) { // 不够五位数字，自动补0
				palletCode = "0" + palletCode;
			}
			String date = DateUtil.formatDate(new Date(), "yyMMdd");
			 pallet="T" + date + palletCode;
			//String palletName = json.getString("pallet_name"); // 托盘类型
			//String whId = json.getString("wh_id"); // 仓库号
			String stringFreeze = json.getString("freeze"); // 冻结标示 0 非冻结 1冻结
			//String stringFreezeId = json.getString("freeze_id"); // 默认0
			//String stringStatus = json.getString("status"); // 空托盘标识,0-空
			String stringMaxWeight = json.getString("max_weight"); // 最大承重
			//String unitWeight = json.getString("unit_weight"); // 最大承重单位
			Map<String, Object> errorMap = palletService.addPallet(errorCode, errorString, errorStatus, pallet,
					"", 0+"", stringFreeze, 0+"", 0+"",stringMaxWeight, "",user);
			errorCode = (int) errorMap.get("errorCode");
			errorString = (String) errorMap.get("errorString");
			errorStatus = (boolean) errorMap.get("errorStatus");
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
	 * 修改单个托盘
	 * 
	 * @author 刘宇 2018.01.09
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/update_pallet", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject updatePallet(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "成功";
		boolean errorStatus = true;

		try {

			String stringPalletId = json.getString("pallet_id"); // 托盘表自增主键
			//String palletName = json.getString("pallet_name"); // 托盘类型
			String stringFreeze = json.getString("freeze"); // 冻结标示 0 非冻结 1冻结
			//String stringFreezeId = json.getString("freeze_id"); // 默认0
			//String stringStatus = json.getString("status"); // 空托盘标识,0-空
			String stringMaxWeight = json.getString("max_weight"); // 最大承重
			//String unitWeight = json.getString("unit_weight"); // 最大承重单位
			Map<String, Object> errorMap = palletService.updatePallet(errorCode, errorString, errorStatus,
					stringPalletId, "", stringFreeze, 0+"", 0+"", stringMaxWeight,
					0+"");
			errorCode = (int) errorMap.get("errorCode");
			errorString = (String) errorMap.get("errorString");
			errorStatus = (boolean) errorMap.get("errorStatus");
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
	 * 批量修改托盘
	 * 
	 * @author 刘宇 2018.01.09
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/update_pallets", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject updatePallets(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "成功";
		boolean errorStatus = true;
		try {
			String palletName = json.getString("pallet_name");// 托盘类型
			String stringFreeze = json.getString("freeze");// 冻结标示 0 非冻结 1冻结
			String stringMaxWeight = json.getString("max_weight");// 最大承重
			String stringManyPalletId = json.getString("many_pallet_id"); // 多个托盘表自增主键
			Map<String, Object> errorMap = palletService.updatePalletS(errorCode, errorString, errorStatus, palletName,
					stringFreeze, stringMaxWeight, stringManyPalletId);
			errorCode = (int) errorMap.get("errorCode");
			errorString = (String) errorMap.get("errorString");
			errorStatus = (boolean) errorMap.get("errorStatus");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			errorString = "程序异常";
			errorStatus = false;
		}
		String body = UtilString.STRING_EMPTY;
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
	}

	/**
	 * 查看所有仓库号和厂库
	 * 
	 * @author 刘宇 2018.01.10
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list_all_ware_house", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject listWareHouse(CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		JSONArray body = new JSONArray();
		try {
			List<Map<String, Object>> objCkList = palletService.listWhIdAndWhNameForPalle();
			for (Map<String, Object> mapCk : objCkList) {
				JSONObject ckJSon = new JSONObject();
				ckJSon.put("wh_id", mapCk.get("wh_id")); // 仓库号
				ckJSon.put("wh_name", mapCk.get("wh_name")); // 仓库描述
				body.add(ckJSon);
			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
		}
		JSONObject obj = new JSONObject();
		obj.put("body", body);
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);

	}

	/**
	 * 批量添加托盘
	 * 
	 * @author 刘宇 2018.01.11
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/add_pallets", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject addPallets(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "成功";
		boolean errorStatus = true;
		try {
			String palletCodeBeginAdd = json.getString("pallet_code_begin"); // 起始托盘号
			String palletCodeEndAdd = json.getString("pallet_code_end"); // 结束托盘号
			String palletName = json.getString("pallet_name"); // 托盘类型
			String whId = json.getString("wh_id"); // 仓库号
			String stringFreeze = json.getString("freeze"); // 冻结标示 0 非冻结 1冻结
			String stringFreezeId = json.getString("freeze_id"); // 默认0
			String stringStatus = json.getString("status"); // 空托盘标识,0-空
			String stringMaxWeight = json.getString("max_weight"); // 最大承重
			String unitWeight = json.getString("unit_weight"); // 最大承重单位
			Map<String, Object> errorMap = palletService.addPallets(errorCode, errorString, errorStatus,
					palletCodeBeginAdd, palletCodeEndAdd, palletName, whId, stringFreeze, stringFreezeId, stringStatus,
					stringMaxWeight, unitWeight);
			errorCode = (int) errorMap.get("errorCode");
			errorString = (String) errorMap.get("errorString");
			errorStatus = (boolean) errorMap.get("errorStatus");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			errorString = "程序异常";
			errorStatus = false;
		}
		String body = UtilString.STRING_EMPTY;
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
	}

	/**
	 * 打印托盘信息
	 * 
	 * @author 刘宇 2018.01.15
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/print_pallet", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object printPallet(@RequestBody JSONObject json, CurrentUser user) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;

		JSONObject body = new JSONObject();
		try {

			String manyPalletId = json.getString("many_pallet_id"); // 多个或一个托盘表自增主键

			String file_name = palletService.printPallet(manyPalletId);

			body.put("file_name", file_name);

			if (!"".equals(file_name)) {
				// java.net.URI uri = new
				// java.net.URI(PropertiesUtil.getInstance().getPrintUrl()
				// + "/wms/print/outputfiles/label/" + fileName + ".xls");
				// java.awt.Desktop.getDesktop().browse(uri);
				body.put("url", UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/label/" + file_name
						+ ".xls");

			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), body);
	}

}
