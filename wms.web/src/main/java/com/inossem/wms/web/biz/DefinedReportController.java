package com.inossem.wms.web.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.vo.BizDefinedReportVo;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDefinedReportService;
import com.inossem.wms.service.dic.IDicWarehouseService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 自定义报表
 * @author sw
 *
 */
@Controller
public class DefinedReportController {
	
	private static final Logger logger = LoggerFactory.getLogger(DefinedReportController.class);
	
	@Autowired
	private IDefinedReportService reportService;
	
	@Autowired
	private ICommonService commonService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IDicWarehouseService warehouseService;
	
	/**
	 * 获取可以自定义的字段
	 * @return
	 */
	@RequestMapping(value = { "/biz/query/defined/get_report_field" }, method = { RequestMethod.GET }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getDefinedField(Byte type) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String, Object> result = new HashMap<String,Object>();
		try {
			result = reportService.getDefinedField(type);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		}catch (Exception e) {
			logger.error("自定义报表", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		} 
		return UtilResult.getResultToJson(status, error_code,result);
	}
	

	/**
	 * 获取需要的数据
	 * @param user
	 * @return
	 */
	@SuppressWarnings("serial")
	@RequestMapping(value = { "/biz/query/defined/get_base_data" }, method = { RequestMethod.GET }, produces = {"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getBaseData(CurrentUser user) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		Map<String, Object> result = new HashMap<String,Object>();
		try {
			result.put("user_list", userService.listAll(new HashMap<String,Object>(){{put("paging",false);}}));
			result.put("fty_location_list", commonService.listFtyLocationForUser(user.getUserId(), null));
			result.put("warehouse_list", warehouseService.queryWarehouseList());
			result.put("productline_installation_list", commonService.selectProductLineListAndDicInstallationList(user.getUserId()));
			result.put("class_type_list", commonService.getClassTypeList());
			result.put("output_receipt_type_list",Arrays.asList(
					new HashMap<String,Object>(){{
						put("receipt_type_name",EnumReceiptType.STOCK_OUTPUT_SALE.getName());
						put("status_list",
						Arrays.asList(		
							new HashMap<String,Object>(){{
								put("status",EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
								put("status_name",EnumReceiptStatus.RECEIPT_UNFINISH.getName());
							}},
							new HashMap<String,Object>(){{
								put("status",EnumReceiptStatus.RECEIPT_INVOICED.getValue());
								put("status_name",EnumReceiptStatus.RECEIPT_INVOICED.getName());
							}},
							new HashMap<String,Object>(){{
								put("status",EnumReceiptStatus.RECEIPT_FINISH.getValue());
								put("status_name",EnumReceiptStatus.RECEIPT_FINISH.getName());
							}},
							new HashMap<String,Object>(){{
								put("status",EnumReceiptStatus.RECEIPT_WRITEOFF.getValue());
								put("status_name",EnumReceiptStatus.RECEIPT_WRITEOFF.getName());
							}}
						));
					}},
					new HashMap<String,Object>(){{
						put("receipt_type_name",EnumReceiptType.STOCK_OUTPUT_SCRAP.getName());
						put("status_list",
							Arrays.asList(		
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_SUBMIT.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_WORK.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_WORK.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_FINISH.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_FINISH.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_WRITEOFF.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_WRITEOFF.getName());
								}}
							)
						);
					}},
					new HashMap<String,Object>(){{
						put("receipt_type_name",EnumReceiptType.STOCK_OUTPUT_OTHER.getValue());
						put("status_list",
							Arrays.asList(		
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_SUBMIT.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_WORK.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_WORK.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_FINISH.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_FINISH.getName());
								}}
							)
						);
					}},
					new HashMap<String,Object>(){{
						put("receipt_type_name",EnumReceiptType.STOCK_OUTPUT_STRAIGTH.getName());
						put("status_list",
							Arrays.asList(		
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_TRANSPORT.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_TRANSPORT.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_INVOICED.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_INVOICED.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_FINISH.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_FINISH.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_UNFINISH.getName());
								}}
							)
						);
					}},
					new HashMap<String,Object>(){{
						put("receipt_type_name",EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getName());
						put("status_list",
							Arrays.asList(		
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_SUBMIT.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_WORK.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_WORK.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_INVOICED.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_INVOICED.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_SORTING.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_SORTING.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_FINISH.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_FINISH.getName());
								}}
							)
						);
					}},
					new HashMap<String,Object>(){{
						put("receipt_type_name",EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getName());
						put("status_list",
							Arrays.asList(		
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_UNFINISH.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_BEOUTPUT.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_BEOUTPUT.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_INVOICED.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_INVOICED.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_WRITEOFF.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_WRITEOFF.getName());
								}},
								new HashMap<String,Object>(){{
									put("status",EnumReceiptStatus.RECEIPT_FINISH.getValue());
									put("status_name",EnumReceiptStatus.RECEIPT_FINISH.getName());
								}}
							)
						);
					}}
			));
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		}catch (Exception e) {
			logger.error("自定义报表", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		} 
		return UtilResult.getResultToJson(status, error_code,result);
	}
	
	/**
	 * 获取详情
	 * @param defined_id
	 * @return
	 */
	@RequestMapping(value = { "/biz/query/defined/get_report_detail" }, method = { RequestMethod.GET }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getReportDetail(Integer defined_id) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		BizDefinedReportVo result = new BizDefinedReportVo();
		try {
			result = reportService.getDetailById(defined_id);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		}catch (Exception e) {
			logger.error("自定义报表", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		} 
		return UtilResult.getResultToJson(status, error_code,result);
	}
	
	/**
	 * 自定义报表列表页
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = { "/biz/query/defined/get_report_list_all","/biz/query/defined/get_report_list_by_user" }, method = { RequestMethod.POST }, produces = {"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getDefinedReportList(HttpServletRequest request,@RequestBody JSONObject json,CurrentUser user) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		List<BizDefinedReportVo> result = new ArrayList<BizDefinedReportVo>();
		Map<String, Object> param = new HashMap<String, Object>();
		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			param.put("keyword", UtilObject.getStringOrEmpty(json.get("keyword")));
			JSONArray array = json.getJSONArray("defined_type");
			param.put("typeList",array);
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			param.put("paging", true);
			param.put("pageIndex", pageIndex);
			param.put("pageSize", pageSize);
			param.put("totalCount", totalCount);
			param.put("sortColumn", sortColumn);
			param.put("sortAscend", sortAscend);
			String url = request.getServletPath();
			if(url.equals("/biz/query/defined/get_report_list_all")) {
				param.put("createUser", user.getUserId());
			}
			if(url.equals("/biz/query/defined/get_report_list_by_user")) {
				param.put("userId", user.getUserId());
			}
			result = reportService.getDefinedReportList(param);
			if(result != null && !result.isEmpty()) {
				total = result.get(0).getTotalCount();
			}
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("自定义报表列表", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, result);
	}
	
	/**
	 * 获取详情
	 * @param defined_id
	 * @return
	 */
	@RequestMapping(value = { "/biz/query/defined/get_detail" }, method = { RequestMethod.GET }, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody JSONObject getDetail(Integer defined_id) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		BizDefinedReportVo result = new BizDefinedReportVo();
		try {
			result = reportService.getDetailById(defined_id);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		}catch (Exception e) {
			logger.error("自定义报表", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		} 
		return UtilResult.getResultToJson(status, error_code,result);
	}
	
	/**
	 * 删除
	 * @param defined_id
	 * @return
	 */
	@RequestMapping(value = { "/biz/query/defined/delete_defined_report" }, method = { RequestMethod.GET }, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody JSONObject delete(Integer defined_id) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String msg = "";
		try {
			reportService.deleteById(defined_id);
			msg = "成功";
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		}catch (Exception e) {
			logger.error("自定义报表", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = "失败";
		} 
		return UtilResult.getResultToJson(status, error_code,msg,"");
	}
	

	/**
	 * 保存数据
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/query/defined/save_defined_report" }, method = { RequestMethod.POST }, produces = {"application/json;charset=UTF-8"})
	public @ResponseBody JSONObject saveDefinedReport(@RequestBody JSONObject json,CurrentUser user) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String msg = "";
		JSONObject result = new JSONObject();
		try {
			Integer definedId = reportService.saveData(json, user.getUserId());
			result.put("defined_id", definedId);
			msg = "成功";
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		}catch (Exception e) {
			logger.error("自定义报表", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			msg = "失败";
		} 
		return UtilResult.getResultToJson(status, error_code,msg,result);
	}
	
	
	/**
	 * 获取数据列表
	 * @param request
	 * @param json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/biz/query/defined/get_data_list"}, method = { RequestMethod.POST }, produces = {"application/json;charset=UTF-8" })
	public @ResponseBody JSONObject getDataList(HttpServletRequest request,@RequestBody JSONObject json,CurrentUser user) {
		boolean status = true;
		int error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			result = reportService.getDataList(json);
			total = reportService.getDataCount(json);
			error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			logger.error("自定义报表列表", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, pageIndex, pageSize, total, result);
	}
	
	
}
