package com.inossem.wms.web.biz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.inossem.wms.config.ServerConfig;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumMesErrorInfo;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.vo.BizStockOutputHeadVo;
import com.inossem.wms.service.biz.IOutPutCWService;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 出库
 * @author sw
 *
 */
@Controller
public class OutPutControllerCW {
	
	private static final Logger logger = LoggerFactory.getLogger(OutPutControllerCW.class);
	
	@Autowired
	private IOutPutCWService outPutService;

	@Autowired
	private ServerConfig constantConfig;

	/**
	 * 从SAP获取交货单(详情)
	 * 
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/sale/get_sales_view","/biz/output/straigth/get_sales_view" 
			,"/biz/output/urgent/get_sales_view" ,"/biz/output/book/get_sales_view"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> getSalesViewBySap(@RequestParam("refer_receipt_code")String refer_receipt_code,boolean is_urgent,HttpServletRequest request) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String,Object> result = new HashMap<String,Object>();
		String msg = "成功";
		try {
			if (!"".equals(refer_receipt_code)) {
				while (refer_receipt_code.length() < 10) { // 不够十位数字，自动补0
					refer_receipt_code = "0" + refer_receipt_code;
				}
				String url = request.getServletPath();
				Byte type = null;
				//销售出库
				if(url.equals("/biz/output/sale/get_sales_view")) {
					type = EnumReceiptType.STOCK_OUTPUT_SALE.getValue();
				}
				//直发出库
				if(url.equals("/biz/output/straigth/get_sales_view")) {
					type = EnumReceiptType.STOCK_OUTPUT_STRAIGTH.getValue();
				}
				//记账销售出库
				if(url.equals("/biz/output/book/get_sales_view")) {
					type = EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue();
				}
				//作业销售出库
				if(url.equals("/biz/output/urgent/get_sales_view")) {
					if(is_urgent) {
						//紧急库存
						type = EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue();
					}else {
						//普通库存
						type = EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue();
					}
				}
				result = outPutService.getDeliverOrderView(refer_receipt_code,type);
				error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			} else {
				logger.error("获取销售订单 --", "请输入查询条件");
				status = false;
				error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
			}
		} catch (WMSException ex){
			error_code = ex.getErrorCode();
			msg = ex.getMessage();
			status = false;
			ex.printStackTrace();
			logger.error("获取销售订单 --", ex);
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		} catch (Exception e) {
			logger.error("获取销售订单 --", e.getMessage());
			status = false;
			msg = "获取交货单信息失败";
			e.printStackTrace();
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToMap(status, error_code,msg, result);
	}


	/**
	 * 通用获取出库单列表
	 * @param json
	 * @return
     */
	@RequestMapping(value = { "/biz/output/sale/get_order_list", "/biz/output/scrap/get_order_list",
			"/biz/output/other/get_order_list","/biz/output/straigth/get_order_list",
			"/biz/output/urgent/get_order_list","/biz/output/book/get_order_list"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOrderList(@RequestBody JSONObject json,CurrentUser user) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		try {
			pageIndex = UtilObject.getIntOrZero(json.getInt("page_index"));
			pageSize = UtilObject.getIntOrZero(json.getInt("page_size"));
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = UtilObject.getStringOrEmpty(json.getString("sort_column"));
			param.put("keyword", UtilObject.getStringOrEmpty(json.getString("keyword")));
			param.put("receiptType",UtilObject.getByteOrNull(json.get("receipt_type")));
			JSONArray receiptTypeList = json.getJSONArray("receipt_type_list");
			param.put("receiptTypeList", receiptTypeList);
			JSONArray array = json.getJSONArray("status");
			param.put("createTime", UtilObject.getStringOrEmpty(json.get("create_time")));
			param.put("statusList",array);
			param.put("paging", true);
			param.put("pageIndex", pageIndex);
			param.put("pageSize", pageSize);
			param.put("totalCount", totalCount);
			param.put("sortAscend", sortAscend);
			param.put("sortColumn", sortColumn);
			param.put("userId", user.getUserId());
			list = outPutService.getOrderList(param);
			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("出库列表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, pageIndex,pageSize, total, list);
	}
	
	/**
	 * 通用待出库配货单
	 * @param json
	 * @return
     */
	@RequestMapping(value = { "/biz/output/sale/get_cargo_order_list"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getCargoOrderList(@RequestBody JSONObject json,CurrentUser user) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		List<Map<String, Object>> list = null;
		try {
			list = outPutService.getCargoOrderList(UtilObject.getStringOrEmpty(json.get("code")),user.getUserId());
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("出库列表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, list);
	}
	
	/**
	 * 开票出库单列表
	 * @param json
	 * @return
     */
	@RequestMapping(value = {"/conf/output/all/get_order_list"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOrderListAll(@RequestBody JSONObject json) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		try {
			pageIndex = UtilObject.getIntOrZero(json.getInt("page_index"));
			pageSize = UtilObject.getIntOrZero(json.getInt("page_size"));
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = UtilObject.getStringOrEmpty(json.getString("sort_column"));
			param.put("stockOutputCode", UtilObject.getStringOrNull(json.get("stock_output_code")));
			param.put("referReceiptCode", UtilObject.getStringOrNull(json.get("refer_receipt_code")));
			param.put("preorderId", UtilObject.getStringOrNull(json.get("preorder_id")));
			param.put("receiveName", UtilObject.getStringOrNull(json.get("receive_name")));
			param.put("userName", UtilObject.getStringOrNull(json.get("user_name")));
			param.put("matName", UtilObject.getStringOrNull(json.get("mat_name")));
			param.put("receiptType",UtilObject.getByteOrNull(json.get("receipt_type")));
			JSONArray receiptTypeList = json.getJSONArray("receipt_type_list");
			param.put("receiptTypeList", receiptTypeList);
			JSONArray array = json.getJSONArray("status");
			param.put("statusList",array);
			param.put("paging", true);
			param.put("pageIndex", pageIndex);
			param.put("pageSize", pageSize);
			param.put("totalCount", totalCount);
			param.put("sortAscend", sortAscend);
			param.put("sortColumn", sortColumn);
			list = outPutService.getOrderListAll(param);
			if (list != null && list.size() > 0) {
				Long totalCountLong = (Long) list.get(0).get("totalCount");
				total = totalCountLong.intValue();
			}
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("出库列表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, pageIndex,pageSize, total, list);
	}
	
	/**
	 * 开票出库单列表
	 * @param json
	 * @return
     */
	@RequestMapping(value = {"/conf/output/all/download_output_data"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void download(HttpServletRequest request,HttpServletResponse response,@RequestParam String stock_output_code,
			@RequestParam String refer_receipt_code,@RequestParam String preorder_id,@RequestParam String receive_name,
			@RequestParam String user_name,@RequestParam String mat_name,@RequestParam String status) {
		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;
		Map<String, Object> param = new HashMap<String, Object>();
		try {
			param.put("stockOutputCode", UtilObject.getStringOrNull(stock_output_code));
			param.put("referReceiptCode", UtilObject.getStringOrNull(refer_receipt_code));
			param.put("preorderId", UtilObject.getStringOrNull(preorder_id));
			param.put("receiveName", UtilObject.getStringOrNull(receive_name));
			param.put("userName", UtilObject.getStringOrNull(user_name));
			param.put("matName", UtilObject.getStringOrNull(mat_name));
			param.put("receiptTypeList", Arrays.asList(22, 25, 26, 27, 28));
			param.put("statusList",Arrays.asList(status.split(",")));
			param.put("paging", false);
			List<Map<String, Object>> list = outPutService.getOrderListAll(param);
			if(list!=null && !list.isEmpty()) {
				// 下载
				Map<String, String> relationMap = new HashMap<String, String>();

				relationMap.put("receive_name", "客户名称");
				relationMap.put("refer_receipt_code", "交货单号");
				relationMap.put("order_type_name", "订单类型");
				relationMap.put("refer_receipt_rid", "交货单行号");
				relationMap.put("mat_code", "物料编码");
				relationMap.put("mat_name", "物料描述");
				relationMap.put("output_qty", "出库数量");
				relationMap.put("unit_code", "单位");
				relationMap.put("location_name", "库存地点");
				relationMap.put("create_user", "创建人");
				relationMap.put("create_time", "创建日期");
				relationMap.put("status_name", "状态");
				relationMap.put("stock_output_code", "出库单号");
				relationMap.put("receipt_type_name", "出库类型");

				List<String> orderList = new ArrayList<String>();
				orderList.add("客户名称");
				orderList.add("交货单号");
				orderList.add("订单类型");
				orderList.add("交货单行号");
				orderList.add("物料编码");
				orderList.add("物料描述");
				orderList.add("出库数量");
				orderList.add("单位");
				orderList.add("库存地点");
				orderList.add("创建人");
				orderList.add("创建日期");
				orderList.add("状态");
				orderList.add("出库单号");
				orderList.add("出库类型");

				
				String root = constantConfig.getTempFilePath();

				String download_file_name = "出库开票信息";

				String filePath = UtilExcel.getExcelRopertUrl(download_file_name, list, relationMap, orderList, root);

				fileInServer = new File(filePath);
				// 获取输入流
				bis = new BufferedInputStream(new FileInputStream(fileInServer));

				// 转码，免得文件名中文乱码
				String fileNameForUTF8 = URLEncoder.encode(download_file_name + ".xls", "UTF-8");
				// 设置文件下载头
				response.addHeader("Content-Disposition", "attachment;filename=" + fileNameForUTF8);

				// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
				response.setContentType("multipart/form-data");
				out = new BufferedOutputStream(response.getOutputStream());
				int len = 0;
				while ((len = bis.read()) != -1) {
					out.write(len);
					out.flush();
				}
			}
		} catch (Exception e) {
			logger.error("出库列表查询 --", e);
		}
	}
	
	
	/**
	 * 通用获取出库明细
	 * @param stock_output_id
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/sale/get_order_view", "/biz/output/scrap/get_order_view",
			"/biz/output/other/get_order_view","/biz/output/straigth/get_order_view",
			"/biz/output/urgent/get_order_view","/biz/output/book/get_order_view" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getOrderView(Integer stock_output_id) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = true;
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			BizStockOutputHeadVo head = outPutService.getOrderView(stock_output_id);
			if(head!=null){
				result = outPutService.getOrderViewFormat(head);
			}
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("出库概览页查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, result);
	}
	
	
	/**
	 * 销售出库 紧急记账销售出库 提交并过账
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/sale/save_sales_order_sap","/biz/output/book/save_book_order_sap"  }, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveOrderToSAP(@RequestBody JSONObject jsonData, CurrentUser user,HttpServletRequest request) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = true;
		String msg = "成功";
		JSONObject result = new JSONObject();
		try {
			String url = request.getServletPath();
			Byte type = null;
			//销售出库
			if(url.equals("/biz/output/sale/save_sales_order_sap")) {
				type = EnumReceiptType.STOCK_OUTPUT_SALE.getValue();
			//紧急记账销售出库
			}else if(url.equals("/biz/output/book/save_book_order_sap")){
				type = EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue();
			}
			//保存单据
			result = outPutService.saveOutputOrderData(jsonData, user, false,type);
			//修改wms单据与sap单据
			msg = outPutService.udateWmsAndSapData(result.getInt("stock_output_id"), jsonData.getString("posting_date"),user.getUserId());	
			//mes同步
			outPutService.saveOutPutToMes(result.getInt("stock_output_id"),user.getUserId());
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("销售出库过账 --", e);
			status = false;
			msg = "失败";
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, msg, result);
	}

	/**
	 * 报废出库 其它出库 直发出库转储  紧急作业销售出库 提交
	 * 
	 * @param jsonData
	 * @param user
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/scrap/save_scrap_order","/biz/output/other/save_other_order",
			"/biz/output/straigth/save_straigth_order","/biz/output/urgent/save_urgent_order"} , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveOrder(@RequestBody JSONObject jsonData, CurrentUser user,HttpServletRequest request) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = true;
		String msg = "成功";
		JSONObject result = new JSONObject();
		try {
			String url = request.getServletPath();
			Byte type = null;
			//报废销售
			if(url.equals("/biz/output/scrap/save_scrap_order")) {
				type = EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue();
			}
			//其他出库
			if(url.equals("/biz/output/other/save_other_order")) {
				type = EnumReceiptType.STOCK_OUTPUT_OTHER.getValue();
			}
			//直发出库
			if(url.equals("/biz/output/straigth/save_straigth_order")) {
				type = EnumReceiptType.STOCK_OUTPUT_STRAIGTH.getValue();
			}
			//紧急作业销售出库
			if(url.equals("/biz/output/urgent/save_urgent_order")) {
				boolean is_urgent = jsonData.getBoolean("is_urgent");
				if(is_urgent) {
					//紧急库存
					type = EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue();
				}else {
					//真实库存
					type = EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue();
				}
			}
			//保存单据
			result = outPutService.saveOutputOrderData(jsonData, user, false,type);
			
			//如果是直发出库 进行转储过账
			if(type == EnumReceiptType.STOCK_OUTPUT_STRAIGTH.getValue()){
				//修改wms单据与sap单据
				msg = outPutService.udateWmsAndSapData(result.getInt("stock_output_id"), jsonData.getString("posting_date"),user.getUserId());
				//mes同步
				outPutService.saveTranSportToMes(result.getInt("stock_output_id"),user.getUserId());
			}
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("出库提交 --", e);
			msg = "失败";
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToMap(status, error_code, msg, result);
	}
	
	/**
	 * 直发出库 转储
	 * @param stock_output_id
	 * @param posting_date
	 * @param user
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/straigth/save_straigth_order"} , method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveStaigthOrder(Integer stock_output_id,String posting_date, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = true;
		String msg = "成功";
		JSONObject result = new JSONObject();
		try {
			//修改wms单据与sap单据
			msg = outPutService.udateWmsAndSapData(stock_output_id, posting_date,user.getUserId());
			//mes同步
			outPutService.saveOutPutToMes(stock_output_id,user.getUserId());
			result.put("stock_output_id", stock_output_id);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (WMSException e) {
			error_code = e.getErrorCode();
			e.printStackTrace();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("直发出库转储 --", e);
			e.printStackTrace();
			msg = "失败";
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToMap(status, error_code, msg, result);
	}
	
	
	/**
	 * 报废出库 销售出库(第一次失败) 直发出库 紧急作业销售出库 紧急记账销售出库(第一次失败) sap过账
	 * @param stock_output_id
	 * @param user
     * @return
     */
	@RequestMapping(value = {"/biz/output/scrap/save_scrap_order_sap","/biz/output/sale/save_sales_order_sap",
			"/biz/output/straigth/save_straigth_order_sap","/biz/output/urgent/save_urgent_order_sap"
		,"/biz/output/book/save_book_order_sap"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveOrderToSAP(Integer stock_output_id,String refer_receipt_code,String posting_date,CurrentUser user,HttpServletRequest request) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = true;
		String msg = "成功";
		JSONObject result = new JSONObject();
		try {
			String url = request.getServletPath();
			//紧急作业销售出库 用户自己输入交货单号校验并且补全信息
			if(url.equals("/biz/output/urgent/save_urgent_order_sap")) {
				if(refer_receipt_code!=null&&!"".equals(refer_receipt_code)) {
					this.outPutService.checkOrderBySapOrder(refer_receipt_code,stock_output_id);
				}
			}
			//修改wms单据与sap单据
			msg = outPutService.udateWmsAndSapData(stock_output_id, posting_date,user.getUserId());
			//mes同步
			this.outPutService.saveOutPutToMes(stock_output_id,user.getUserId());
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			result.put("stock_output_id", stock_output_id);
		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("出库过账 --", e);
			msg = "失败";
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, msg,result);
	}

	/**
	 * 其它出库出库 紧急作业销售出库 紧急记账销售出库
	 * @param stock_output_id
	 * @param posting_date
	 * @param user
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/other/save_other_output","/biz/output/urgent/save_urgent_output"
			,"/biz/output/book/save_book_output"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object saveOutput(Integer stock_output_id,String posting_date,CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		boolean status = true;
		String msg = "成功";
		JSONObject result = new JSONObject();
		try {
			result = this.outPutService.saveOrderOutput(stock_output_id,posting_date,user.getUserId());
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("", e);
		} catch (Exception e) {
			logger.error("其它出库出库 --", e);
			msg = "失败";
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToMap(status, error_code, msg, result);
	}
	
	
	/**
	 * 出库冲销
	 * @param stock_output_id
	 * @param user
	 * @return
	 */
	@RequestMapping(value = {  "/biz/output/sale/write_off", 
			"/biz/output/scrap/write_off","/biz/output/book/write_off"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object writeOff(Integer stock_output_id, CurrentUser user) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String msg = "成功";
		boolean status = true;
		JSONObject result = new JSONObject();
		try {
			//sap冲销
			msg = this.outPutService.writeOffAndUpdate(stock_output_id, user.getUserId());
			//mes冲销
			this.outPutService.mesWriteOff(stock_output_id, user.getUserId());
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			result.put("stock_output_id", stock_output_id);
		} catch (WMSException e) {
			error_code = e.getErrorCode();
			msg = e.getMessage();
			status = false;
			logger.error("冲销 --", e);
		} catch (Exception e) {
			logger.error("冲销 --", e);
			msg = "失败";
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToMap(status, error_code, msg, result);
	}
	
	
	/**
	 * 批量修改状态
	 * @param stock_output_id
	 * @param status
	 * @param cUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/conf/output/all/update_order_status"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> updateOrderStatus(@RequestBody JSONObject jsonData,CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String msg = "成功";
		boolean status = true;
		try {
			JSONArray array = jsonData.getJSONArray("ids");
			Byte status_ = UtilObject.getByteOrNull(jsonData.get("status"));
			this.outPutService.updateOutPutHead(array,cUser.getUserId(), status_);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (WMSException e) {
			logger.error("修改出库单 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error("修改出库单 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = "失败";
		}
		return UtilResult.getResultToMap(status, error_code, msg, "");
	}
	
	/**
	 * 通用删除出库单
	 * @param stock_output_id
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/sale/delete_order","/biz/output/other/delete_order","/biz/output/straight/delete_order",
			 "/biz/output/scrap/delete_order","/biz/output/urgent/delete_order","/biz/output/book/delete_order"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Map<String, Object> deleteOutputOrder(int stock_output_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String msg = "成功";
		boolean status = true;
		try {
			msg = this.outPutService.deleteOutputOrder(stock_output_id, cUser.getUserId());
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (WMSException e) {
			logger.error("删除出库单 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error("删除出库单 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = "失败";
		}
		return UtilResult.getResultToMap(status, error_code, msg, "");
	}
	
	
	/**
	 * 获取物料仓位库存
	 * @param jsonData
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/sale/get_mat_list_position","/biz/output/scrap/get_mat_list_position",
			"/biz/output/other/get_mat_list_position","/biz/output/straight/get_mat_list_position"
			,"/biz/output/urgent/get_mat_list_position","/biz/output/book/get_mat_list_position"}, 
			method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMatList(@RequestBody JSONObject jsonData,HttpServletRequest request) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> result = null;
		try {
			param.put("keyword1", jsonData.get("keyword1"));
			param.put("keyword2", jsonData.get("keyword2"));
			param.put("erpBatch", jsonData.get("keyword2"));
			param.put("ftyId", jsonData.get("fty_id"));
			param.put("ftyType", jsonData.get("fty_type"));
			param.put("locationId", jsonData.get("location_id"));
			param.put("stockTypeId", jsonData.get("stock_type_id"));
			param.put("isDefault", UtilObject.getIntOrZero(jsonData.get("is_default")));
			String url = request.getServletPath();
			//临时作业销售出库 判断是否是临时库存
			if(url.equals("/biz/output/urgent/get_mat_list_position")) {
				Boolean is_urgent = jsonData.getBoolean("is_urgent");
				if(is_urgent) {
					param.put("isUrgent",is_urgent);
				}else {
					param.put("isUrgent",null);
				}
			}
			result=outPutService.selectMatListPastion(param);
			for(Map<String,Object> map:result) {
				map.put("qty", map.get("stock_qty"));
			}
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("物料列表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status,error_code, result);
	}
	
	
	/**
	 * 获取工厂下的物料
	 * @param jsonData
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/sale/get_mat_list_fty","/biz/output/other/get_mat_list_fty",
			"/biz/output/straigth/get_mat_list_fty","/biz/output/urgent/get_mat_list_fty"
			,"/biz/output/book/get_mat_list_fty","/biz/output/scrap/get_mat_list_fty"},
			method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMatListByFtyType(@RequestBody JSONObject jsonData) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> result = null;
		try {
			param.put("keyword", jsonData.get("keyword"));
			param.put("ftyId", jsonData.get("fty_id"));
			result=outPutService.selectMaterialList(param);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("物料列表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status,error_code, result);
	}
	
	
	/**
	 * 获取班次集合
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/sale/get_class_type_list","/biz/output/other/get_class_type_list",
			"/biz/output/straigth/get_class_type_list","/biz/output/urgent/get_class_type_list"
			,"/biz/output/book/get_class_type_list","/biz/output/scrap/get_class_type_list"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getClassTypeList() {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			result=this.outPutService.selectClassTypeList();
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("班次集合查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status,error_code, result);
	}
	
	
	/**
	 * 获取移动类型列表
	 * @return
	 */
	@RequestMapping(value = {"/biz/output/other/get_move_type_list"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getMoveTypeList() {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		List<DicMoveType> result = new ArrayList<DicMoveType>();
		try {
			result=this.outPutService.selectMoveTypeList(EnumReceiptType.STOCK_OUTPUT_OTHER.getValue());
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("班次集合查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status,error_code, result);
	}
	
	
	/**
	 * 查询车辆司机信息
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/biz/output/urgent/get_derver_vehicle_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDerverInfo(CurrentUser cUser) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			result = this.outPutService.selectDriverInfo(cUser.getUserId());
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("班次集合查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status,error_code, result);
	}
	
	
	/**
	 * 重发MES同步
	 * @param stock_output_id
	 * @param business_type
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = { "/biz/output/sale/save_mes_again","/biz/output/straigth/save_mes_again",
			"/biz/output/urgent/save_mes_again","/biz/output/scrap/save_mes_again","/biz/output/book/save_mes_again"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveMesAgain(@RequestParam Integer stock_output_id,@RequestParam String business_type,CurrentUser cUser) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String msg = "";
		JSONObject result = new JSONObject();
		try {
			result = this.outPutService.saveMesAgain(stock_output_id, business_type, cUser.getUserId());
			msg = UtilObject.getStringOrEmpty(result.get("msg"));
			if(msg!= null && !msg.equals("")) {
				msg = EnumMesErrorInfo.check(msg);
				status = false;
			}else {
				msg = "成功";
			}
		    error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (WMSException e) {
			logger.error("重发mes同步 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error("重发mes同步 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = "系统异常";
		}
		return UtilResult.getResultToJson(status,error_code,msg,result);
	}
	 

	/**
	 * 重发MES同步
	 * @param stock_output_id
	 * @param business_type
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = { "/test"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveMesAgain() {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String msg = "";
		JSONObject result = new JSONObject();
		try {
			outPutService.test1();
		} catch (Exception e) {
			logger.error("重发mes同步 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = "系统异常";
		}
		return UtilResult.getResultToJson(status,error_code,msg,result);
	}
	
}
