package com.inossem.wms.portable.conf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.config.ServerConfig;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicBoard;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IStockQueryService;
import com.inossem.wms.service.dic.IDicWarehouseService;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/over_stock")
public class OverStockController {
	private static final Logger logger = LoggerFactory.getLogger(OverStockController.class);
	@Autowired
	private IStockQueryService stockQueryService;
	@Autowired
	private ServerConfig constantConfig;
	@Autowired
	private IDicWarehouseService dicWarehouseService;
	
	@Autowired
    private ICommonService commonService;
	/**
	 * 查询库存积压图表
	 * 
	 * @param cUser
	 * @param board_id
	 * @param fty_id
	 * @param location_id
	 * @return
	 */
	@RequestMapping(value = "/serach_stock_days_chart", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachStockDaysChart(CurrentUser cUser, String board_id, String fty_id,
			String wh_id) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		JSONObject returnMap = new JSONObject();

		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("boardId", board_id);
			param.put("ftyId", fty_id);
			param.put("whId", wh_id);

			returnMap = stockQueryService.getStockDaysChart(param);
		} catch (Exception e) {
			logger.error("库存积压图表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, returnMap);
	}

	/**
	 * 查询板块工厂库存地点
	 * 
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_base_info1", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getBaseInfo(CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			returnMap = stockQueryService.getBaseInfo(cUser);

			returnMap.put("fty_id", cUser.getFtyId());
			returnMap.put("location_id", cUser.getLocationList().get(0));
			List<Map<String,Object>>   wareHouseList=	dicWarehouseService.queryWarehouseList();
			returnMap.put("ware_house_list", wareHouseList);

		} catch (Exception e) {
			logger.error("板块工厂库存地点查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, returnMap);
	}
	

	/**
	 * 下载库存积压
	 * 
	 * @param zgsbk
	 * @param werks
	 * @param lgort
	 * @param response
	 */
	@RequestMapping(value = "/download_stock_days_chart", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void downloadStockDaysChart(String board_id, String fty_id, String wh_id,
			HttpServletResponse response) {

		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;
		List<Map<String, Object>> list = null;
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			// param.put("zgsbk", zgsbk);
			// param.put("werks", werks);
			// param.put("lgort", lgort);

			param.put("boardId", board_id);
			param.put("ftyId", fty_id);
			param.put("whId", wh_id);

			list = stockQueryService.getStockDaysList(param);

			Map<String, String> relationMap = new HashMap<String, String>();

			relationMap.put("mat_group_code", "物料组");
			relationMap.put("mat_group_name", "物料组描述");
			relationMap.put("mat_code", "物料编码");
			relationMap.put("mat_name", "物料描述");
			relationMap.put("unit_name", "计量单位");
			relationMap.put("board_name", "板块");
			relationMap.put("fty_name", "工厂名称");
			relationMap.put("location_name", "库存地点名称");
			relationMap.put("batch", "批次");
			relationMap.put("input_date", "入库日期");
			relationMap.put("days", "库龄");
			relationMap.put("day_type", "分类");
			relationMap.put("status_name", "库存状态");
			relationMap.put("qty", "库存数量");
			relationMap.put("price", "移动平均价");
			relationMap.put("amount", "金额");
			// relationMap.put("spec_stock", "特殊库存标识");
			// relationMap.put("spec_stock_code", "特殊库存代码");
			// relationMap.put("spec_stock_name", "特殊库存描述");

			List<String> orderList = new ArrayList<String>();
			orderList.add("物料组");
			orderList.add("物料组描述");
			orderList.add("物料编码");
			orderList.add("物料描述");
			orderList.add("计量单位");
			orderList.add("板块");
			orderList.add("工厂名称");
			orderList.add("库存地点名称");
			orderList.add("批次");
			orderList.add("入库日期");
			orderList.add("库龄");
			orderList.add("分类");
			orderList.add("库存状态");
			orderList.add("库存数量");
			orderList.add("移动平均价");
			orderList.add("金额");
			// orderList.add("特殊库存标识");
			// orderList.add("特殊库存代码");
			// orderList.add("特殊库存描述");

			String filePath = UtilExcel.getExcelRopertUrl("overstock", list, relationMap, orderList,
					constantConfig.getTempFilePath());

			fileInServer = new File(filePath);
			// 获取输入流
			bis = new BufferedInputStream(new FileInputStream(fileInServer));

			// 转码，免得文件名中文乱码
			String fileNameForUTF8 = URLEncoder.encode("overstock" + ".xls", "UTF-8");
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

		} catch (Exception e) {
			logger.error("", e);

		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					logger.error("", e);

				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("", e);

				}
			}
		}

	}

	@RequestMapping(value = "/serach_stock_days_first", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachStockDaysFirst(CurrentUser cUser,@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONObject returnData = new JSONObject();
		Map<String,Object> param=new HashMap<String,Object>();
		List<String> charInfo=new ArrayList<String>();
		try {
			String [] info= {"1年以上","6个月至1年","4个月至6个月","4个月以内"};
			param.put("boardId", UtilObject.getIntegerOrNull(json.get("board_id")));
			param.put("corpId", UtilObject.getIntegerOrNull(json.get("c")));
			param.put("type", UtilObject.getIntegerOrNull(json.get("type")));
			if(json.has("wh_id")) {
				param.put("whId", UtilObject.getIntegerOrNull(json.get("wh_id")));	
			}
			
			list=stockQueryService.selectFirstViewStockDays(param);	
			for (int i = 0; i < list.size(); i++) {
			BigDecimal qty=	  (BigDecimal) list.get(i).get("qty");	
			qty.setScale(5, BigDecimal.ROUND_HALF_UP);
			list.get(i).put("qty", qty)	;  
			}		
			
		    //返回值   
			Map<String ,Object> pram=new HashMap<String ,Object>();
			pram.put(info[0], 0);
			pram.put(info[1], 0);
			pram.put(info[2], 0);
			pram.put(info[3], 0);
			for (int i = 0; i < list.size(); i++) {
				Map<String ,Object> value=list.get(i);
				if(pram.containsKey(value.get("day_type"))) {
					
					pram.put(value.get("day_type").toString(), value.get("qty"));
					
				}				
			}	
			Map<String ,Object> pramString=new HashMap<String ,Object>();
			pramString.put("oneYear", pram.get("1年以上"));
			pramString.put("sixToYear", pram.get("6个月至1年"));
			pramString.put("fourToSix", pram.get("4个月至6个月"));
			pramString.put("smallFour", pram.get("4个月以内"));
			if(UtilObject.getIntegerOrNull(json.get("type"))==1) {
				pramString.put("unit_name", "KG");	
			}else if(UtilObject.getIntegerOrNull(json.get("type"))==2) {
				pramString.put("unit_name", "万元");
			}
		
			returnData.put("amout_list", pramString);
			//图表
			Map<String,  BigDecimal> returnMap = new HashMap<String,  BigDecimal>();
			
			JSONArray xAxisList = new JSONArray();
			
			JSONArray chartInfoList = new JSONArray();			
			
			for (int i = 0; i < list.size(); i++) {
                JSONObject xAxis = new JSONObject();				
				JSONArray yAxisValueList = new JSONArray();
				JSONObject yAxisValue = new JSONObject();
				charInfo.add(UtilObject.getStringOrEmpty(list.get(i).get("day_type")));
				if("1年以上".equals(list.get(i).get("day_type"))) {
					xAxis.put("xAxisName", UtilObject.getStringOrEmpty(list.get(i).get("day_type")));
					yAxisValue.put("yAxisValue",  UtilObject.getBigDecimalOrZero(list.get(i).get("qty")).setScale(5, BigDecimal.ROUND_HALF_UP));
				}else if("6个月至1年".equals(list.get(i).get("day_type"))) {
					xAxis.put("xAxisName", UtilObject.getStringOrEmpty(list.get(i).get("day_type")));
					yAxisValue.put("yAxisValue",  UtilObject.getBigDecimalOrZero(list.get(i).get("qty")).setScale(5, BigDecimal.ROUND_HALF_UP));
				}else if("4个月至6个月".equals(list.get(i).get("day_type"))) {
					xAxis.put("xAxisName", UtilObject.getStringOrEmpty(list.get(i).get("day_type")));
					yAxisValue.put("yAxisValue",  UtilObject.getBigDecimalOrZero(list.get(i).get("qty")).setScale(5, BigDecimal.ROUND_HALF_UP));
				}else if("4个月以内".equals(list.get(i).get("day_type"))) {
					xAxis.put("xAxisName", UtilObject.getStringOrEmpty(list.get(i).get("day_type")));
					yAxisValue.put("yAxisValue",  UtilObject.getBigDecimalOrZero(list.get(i).get("qty")).setScale(5, BigDecimal.ROUND_HALF_UP));
				}			
				
				yAxisValueList.add(yAxisValue);			
				xAxis.put("yAxisValueList", yAxisValueList);
				xAxisList.add(xAxis);				
			}
			
			BigDecimal max=new BigDecimal(0);
			for(int i=0;i<list.size() ;i++) {
				BigDecimal current	=UtilObject.getBigDecimalOrZero(list.get(i).get("qty")).setScale(5, BigDecimal.ROUND_HALF_UP);
				if(current.compareTo(max)==1) {
					max=current;
				}
			}			
			
			for(int i=0;i<charInfo.size();i++) {
				chartInfoList.add(this.getCharInfo(charInfo.get(i)));	
			}			
		
			returnData.put("chartInfoList", chartInfoList);
			returnData.put("leftAxisMaxValue", max);
			returnData.put("leftAxisUnit", "万元");
			returnData.put("title", "库存积压分析");

			returnData.put("xAxisList", xAxisList);
			JSONArray jsonArr = new JSONArray();
			returnData.put("xLimitLine", jsonArr);
			returnData.put("yLimitLine", jsonArr);
			
		} catch (Exception e) {
			logger.error("库存积压图表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, returnData);
	}	

		private JSONObject getCharInfo(String name) {
			JSONObject chartInfo = new JSONObject();
			chartInfo.put("name", name);
			chartInfo.put("type", "0");
			chartInfo.put("dependence", "0");
			return chartInfo;
		}
	
		
		@RequestMapping(value = "/serach_stock_days_second", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
		public @ResponseBody JSONObject serachStockDaysSecond(CurrentUser cUser,@RequestBody JSONObject json) {
			int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			boolean status = true;
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> amountList = new ArrayList<Map<String, Object>>();
			JSONObject returnData = new JSONObject();
			List<String> charInfo=new ArrayList<String>();
			Map<String,Object> param=new HashMap<String,Object>();
			try {	
				String [] info= {"1年以上","6个月至1年","4个月至6个月","4个月以内"};
				
				param.put("boardId", UtilObject.getIntegerOrNull(json.get("board_id")));
				param.put("corpId", UtilObject.getIntegerOrNull(json.get("corp_id")));
				param.put("type", UtilObject.getIntegerOrNull(json.get("type")));
				param.put("searchType", UtilObject.getIntegerOrNull(json.get("search_type")));
				if(json.has("wh_id")) {
					param.put("whId", UtilObject.getIntegerOrNull(json.get("wh_id")));	
				}
				
				list=stockQueryService.selectSecondViewStockDays(param);
				amountList=stockQueryService.selectSecondViewTotalAmount(param);
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < amountList.size(); j++) {
						if(list.get(i).get("wh_id")==amountList.get(j).get("wh_id")) {
							BigDecimal qty=	  (BigDecimal) amountList.get(j).get("qty");	
							qty.setScale(5, BigDecimal.ROUND_HALF_UP);					
							list.get(i).put("total_amount", qty);						
						}
					}
				}
				if(UtilObject.getIntegerOrNull(json.get("type"))==1) {
					returnData.put("unit_name", "KG");	
				}else if(UtilObject.getIntegerOrNull(json.get("type"))==2) {
					returnData.put("unit_name", "万元");
				}
				returnData.put("type_name", info[UtilObject.getIntegerOrNull(json.get("search_type"))-1]);
				returnData.put("wh_amout_list", list);
				Map<String,  BigDecimal> returnMap = new HashMap<String,  BigDecimal>();
				
				JSONArray xAxisList = new JSONArray();			
				
				for (int i = 0; i < list.size(); i++) {
					
	                JSONObject xAxis = new JSONObject();
	                xAxis.put("xAxisWhId", UtilObject.getStringOrEmpty(list.get(i).get("wh_id")));
					xAxis.put("xAxisName", UtilObject.getStringOrEmpty(list.get(i).get("wh_name")));
					charInfo.add(UtilObject.getStringOrEmpty(list.get(i).get("wh_name")));

					JSONArray yAxisValueList = new JSONArray();
					JSONObject yAxisValue = new JSONObject();
					yAxisValue.put("yAxisValue",  UtilObject.getBigDecimalOrZero(list.get(i).get("qty")).setScale(5, BigDecimal.ROUND_HALF_UP));
					yAxisValueList.add(yAxisValue);
			
					xAxis.put("yAxisValueList", yAxisValueList);

					xAxisList.add(xAxis);				
				}		
				
				BigDecimal max=new BigDecimal(0);
				for(int i=0;i<list.size() ;i++) {
					BigDecimal current	=UtilObject.getBigDecimalOrZero(list.get(i).get("qty")).setScale(5, BigDecimal.ROUND_HALF_UP);
					if(current.compareTo(max)==1) {
						max=current;
					}
				}
				returnData.put("leftAxisMaxValue", max);
				
				if(UtilObject.getIntegerOrNull(json.get("type"))==1) {
					returnData.put("leftAxisUnit", "KG");
				}else if(UtilObject.getIntegerOrNull(json.get("type"))==2) {
					returnData.put("leftAxisUnit", "万元");
				}
				
				if(UtilObject.getIntegerOrNull(json.get("search_type"))==1) {
					returnData.put("title", "1年以上库存积压");
				}else if(UtilObject.getIntegerOrNull(json.get("search_type"))==2) {
					returnData.put("title", "6个月至1年库存积压");
				}else if(UtilObject.getIntegerOrNull(json.get("search_type"))==3) {
					returnData.put("title", "4个月至6个月库存积压");
				}else if(UtilObject.getIntegerOrNull(json.get("search_type"))==4) {
					returnData.put("title", "4个月以内库存积压");
				}
								
				JSONArray chartInfoList = new JSONArray();
				for(int i=0;i<charInfo.size();i++) {
					chartInfoList.add(this.getCharInfo(charInfo.get(i)));	
				}			
			
				returnData.put("chartInfoList", chartInfoList);

				returnData.put("xAxisList", xAxisList);
				JSONArray jsonArr = new JSONArray();
				returnData.put("xLimitLine", jsonArr);
				returnData.put("yLimitLine", jsonArr);
				
			} catch (Exception e) {
				logger.error("库存积压图表查询 --", e);
				status = false;
				error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			}

			return UtilResult.getResultToJson(status, error_code, returnData);
		}
		
		@RequestMapping(value = "/serach_stock_days_third", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
		public @ResponseBody JSONObject serachStockDaysThird(@RequestBody JSONObject json) {
			int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			boolean status = true;
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			Map<String,Object> param=new HashMap<String,Object>();
			try {
				param.put("boardId", UtilObject.getIntegerOrNull(json.get("board_id")));
				param.put("corpId", UtilObject.getIntegerOrNull(json.get("corp_id")));
				param.put("type", UtilObject.getIntegerOrNull(json.get("type")));
				param.put("searchType", UtilObject.getIntegerOrNull(json.get("search_type")));
				param.put("whId", UtilObject.getIntegerOrNull(json.get("wh_id")));			
				list=stockQueryService.selectThirdViewStockDays(param);	
				
				for (int i = 0; i < list.size(); i++) {
					Map<String,Object> map=new HashMap<String,Object>();
					if(UtilObject.getIntegerOrNull(json.get("type"))==1) {
						map.put("unit_name", "千克")	;
						map.put("qty", list.get(i).get("qty"))	;
					}else if(UtilObject.getIntegerOrNull(json.get("type"))==2) {
						map.put("unit_name", "万元")	;
						map.put("qty", list.get(i).get("amount"))	;
					}
					map.put("mat_id", list.get(i).get("mat_id"))	;
					map.put("mat_code", list.get(i).get("mat_code"))	;
					map.put("mat_name", list.get(i).get("mat_name"))	;
					map.put("day_type", list.get(i).get("day_type"))	;
					map.put("wh_name", list.get(i).get("wh_name"))	;
					resultList.add(map);
				}
							
				
			} catch (Exception e) {
				logger.error("库存积压图表查询 --", e);
				status = false;
				error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			}
			UtilJSONConvert.setNullToEmpty(resultList);
			return UtilResult.getResultToJson(status, error_code, resultList);
		}
		
		
		// 根据板块得到公司、工厂、库存地点联动
		@RequestMapping(value = "/base_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
		public @ResponseBody Object baseInfo() {
			int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			boolean status = true;

			JSONObject body = new JSONObject();
			try {
				Calendar date = Calendar.getInstance();
			    String year = String.valueOf(date.get(Calendar.YEAR));
				List<DicBoard> bList = new ArrayList<DicBoard>();
				bList = commonService.getBordList();
				body.put("year",year);
				body.put("deaultBoardId",1);
				body.put("defaultCorpId", 1);
				body.put("boardList", bList);			

			} catch (Exception e) {
				logger.error("图表查询 --", e);
				status = false;
				error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			}

			JSONObject  obj = UtilResult.getResultToJson(status, error_code, body);
			UtilJSONConvert.setNullToEmpty(obj);
			return obj;

		}

}
