package com.inossem.wms.conf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.config.ServerConfig;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.enums.EnumBoard;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumVolumeStatisticsType;
import com.inossem.wms.service.biz.IStockQueryService;
import com.inossem.wms.service.dic.IDicWarehouseService;
import com.inossem.wms.service.dic.IJobStatisService;
import com.inossem.wms.util.UtilDateTime;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONObject;

@Controller
public class JobStatisController {

	private static final Logger logger = LoggerFactory.getLogger(JobStatisController.class);
	
	@Autowired
	private IJobStatisService jobStatisService ;
	
	
	@Autowired
	private ServerConfig constantConfig;
	
	@Autowired
	private IStockQueryService stockQueryService;
	
	@Autowired
	private IDicWarehouseService dicWarehouseService;
	/**
	 * 查询板块工厂库存地点
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value="/conf/jobstatis/get_base_info1", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getBaseInfo(CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		Map<String,Object> returnMap = new HashMap<String,Object>();
		  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {	
			returnMap = stockQueryService.getBaseInfo(cUser);
			returnMap.put("fty_id", cUser.getLocationList().get(0).split("-")[0]);
			returnMap.put("location_id", cUser.getLocationList().get(0).split("-")[1]);			
			Calendar cal = Calendar.getInstance();
			
			returnMap.put("end_time", sdf.format(cal.getTime()));	
			
			cal.add(Calendar.MONTH, -1);
			//cal.set(Calendar.DATE, 1);			
			returnMap.put("start_time", sdf.format(cal.getTime()));			
			cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
					
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
	 * 作业量统计
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/conf/jobstatis/get_job_statis_pictureList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getJobStatisPictureList(CurrentUser cuser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		boolean status = true;
        
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> zylList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		List<Map<String, Object>> xLimitLine = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> yLimitLine = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> typeList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> xAxisList = new ArrayList<Map<String, Object>>();
		int leftAxisMaxValue = 0;
		BigDecimal rightAxisMaxValue = new BigDecimal(0);
		try {
			String locationId=null;
			String whId=null;
			String boardId = json.getString("board_id");
			String ftyId = json.getString("fty_id");
			if(json.containsKey("location_id")) {
			 locationId = json.getString("location_id");
			}
			if(json.containsKey("wh_id")) {
				whId = json.getString("wh_id");
			}
			
			String startTime = json.getString("start_time");
			String endTime = json.getString("end_time");
			String createUser = json.getString("create_user");

			if (StringUtils.hasText(boardId)) {
				map.put("boardId", boardId);
			}
			if (StringUtils.hasText(ftyId)) {
				map.put("ftyId", ftyId);
			}
//			if (StringUtils.hasText(locationId)) {
//				map.put("locationId", locationId);
//			}
			map.put("whId", whId);
			if (StringUtils.hasText(startTime)) {
				map.put("startTime", UtilString.getDateForString(startTime));
			}
			if (StringUtils.hasText(endTime)) {
				map.put("endTime", UtilDateTime.getDate(UtilString.getDateForString(endTime), 1));
			}
			if (StringUtils.hasText(createUser)) {
				map.put("createUser", createUser);
			}
			//得到所有的类型集合
			typeList = EnumVolumeStatisticsType.toTypeList();
			//查询出类型对应操作次数
			zylList = jobStatisService.getJobStatis(map);
			returnList = this.getVSList(zylList);
			if (returnList != null && returnList.size() > 0) {
				for (Map<String, Object> innerMap : returnList) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("xAxisName", innerMap.get("createUserName"));
					List<Map<String, Object>> yAxisValueList = new ArrayList<Map<String, Object>>();
					Map<String, Object> yAxisValueRK = new HashMap<String, Object>();
					yAxisValueRK.put("yAxisValue", UtilString.getStringOrEmptyForObject(innerMap.get("inputNum")));
					Map<String, Object> yAxisValueCHK = new HashMap<String, Object>();
					yAxisValueCHK.put("yAxisValue", UtilString.getStringOrEmptyForObject(innerMap.get("outputNum")));
					Map<String, Object> yAxisValueSJ = new HashMap<String, Object>();
					yAxisValueSJ.put("yAxisValue", UtilString.getStringOrEmptyForObject(innerMap.get("taskUpNum")));
					Map<String, Object> yAxisValueXJ = new HashMap<String, Object>();
					yAxisValueXJ.put("yAxisValue",UtilString.getStringOrEmptyForObject(innerMap.get("taskDownNum")));
					
					Map<String, Object> yAxisValueZCC = new HashMap<String, Object>();
					yAxisValueZCC.put("yAxisValue", UtilString.getStringOrEmptyForObject(innerMap.get("transportOutNum")));					
					Map<String, Object> yAxisValueZCR = new HashMap<String, Object>();
					yAxisValueZCR.put("yAxisValue", UtilString.getStringOrEmptyForObject(innerMap.get("transportInNum")));
					Map<String, Object> yAxisValueZYC = new HashMap<String, Object>();
					yAxisValueZYC.put("yAxisValue", UtilString.getStringOrEmptyForObject(innerMap.get("transTakOutNum"))); 
					Map<String, Object> yAxisValueZYR = new HashMap<String, Object>();
					yAxisValueZYR.put("yAxisValue", UtilString.getStringOrEmptyForObject(innerMap.get("transTakInNum")));					
					
					Map<String, Object> yAxisValuePD = new HashMap<String, Object>();					
					yAxisValuePD.put("yAxisValue", UtilString.getStringOrEmptyForObject(innerMap.get("takeNum")));
					Map<String, Object> yAxisValueALL = new HashMap<String, Object>();
					yAxisValueALL.put("yAxisValue", UtilString.getStringOrEmptyForObject(innerMap.get("allNum")));
					Map<String, Object> yAxisValuePDA = new HashMap<String, Object>();
					yAxisValuePDA.put("yAxisValue",UtilString.getStringOrEmptyForObject(innerMap.get("pdaNum")));
					Map<String, Object> yAxisValuePDAPercent = new HashMap<String, Object>();
					yAxisValuePDAPercent.put("yAxisValue", this.getPercentNum((BigDecimal) innerMap.get("pdaPercent")));
					int allNum = (int) innerMap.get("allNum");
					BigDecimal pdaPercent = (BigDecimal) innerMap.get("pdaPercent");
					if (allNum > leftAxisMaxValue) {
						leftAxisMaxValue = allNum;
					}
					if (pdaPercent.compareTo(rightAxisMaxValue) == 1) {
						rightAxisMaxValue = pdaPercent;
					}
					yAxisValueList.add(yAxisValueRK);
					yAxisValueList.add(yAxisValueCHK);
					yAxisValueList.add(yAxisValueSJ);
					yAxisValueList.add(yAxisValueXJ);
					
					yAxisValueList.add(yAxisValueZCC);
					yAxisValueList.add(yAxisValueZCR);
					yAxisValueList.add(yAxisValueZYC);
					yAxisValueList.add(yAxisValueZYR);
					
					yAxisValueList.add(yAxisValuePD);
					yAxisValueList.add(yAxisValueALL);
					yAxisValueList.add(yAxisValuePDA);
					yAxisValueList.add(yAxisValuePDAPercent);
					resultMap.put("yAxisValueList", yAxisValueList);
					xAxisList.add(resultMap);
				}
			}
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
			logger.error("作业量统计", e);
		}


		HashMap<String, Object> body = new HashMap<String, Object>();
		//所有类型
		body.put("chartInfoList", typeList);
		body.put("title", "作业量统计");
		body.put("rightAxisUnit", "%");
		body.put("leftAxisUnit", "笔");
		//左侧最大值
		body.put("leftAxisMaxValue", leftAxisMaxValue);
		//右侧最大值
		body.put("rightAxisMaxValue", this.getPercentNum(rightAxisMaxValue));
		
		body.put("xLimitLine", xLimitLine);
		body.put("yLimitLine", yLimitLine);
		body.put("xAxisList", xAxisList);
		

		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, body);

	}
	
	
	/**
	 * 导出Excel
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/conf/jobstatis/download_job_statis_pictureList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void downloadJobStatisList(CurrentUser cuser, HttpServletRequest request,HttpServletResponse response) throws IOException {
		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;
		
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> zylList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			String boardId = request.getParameter("board_id");
			String ftyId = request.getParameter("fty_id");
			String whId = request.getParameter("wh_id");
			String startTime = request.getParameter("start_time");
			String endTime = request.getParameter("end_time");
			String createUser = request.getParameter("create_user");

			if (StringUtils.hasText(boardId)) {
				map.put("boardId", boardId);
			}
			if (StringUtils.hasText(ftyId)) {
				map.put("ftyId", ftyId);
			}
			if (StringUtils.hasText(whId)) {
				map.put("whId", whId);
			}
			if (StringUtils.hasText(startTime)) {
				map.put("startTime", UtilString.getDateForString(startTime));
			}
			if (StringUtils.hasText(endTime)) {
				map.put("endTime", UtilDateTime.getDate(UtilString.getDateForString(endTime), 1));
			}
			if (StringUtils.hasText(createUser)) {
				map.put("createUser", createUser);
			}
			zylList = jobStatisService.getJobStatis(map);
			returnList = this.getVSList(zylList);
			
			

			// 下载
			Map<String, String> relationMap = new HashMap<String, String>();

			relationMap.put("boardName", "板块");
			relationMap.put("ftyName", "工厂");
			relationMap.put("whName", "仓库号");
			relationMap.put("createUserName", "创建人");
			relationMap.put("inputNum", "入库笔数");
			relationMap.put("outputNum", "出库笔数");
			relationMap.put("taskUpNum", "上架笔数");
			relationMap.put("taskDownNum", "下架笔数");
			relationMap.put("transportOutNum", "转储出笔数");
			relationMap.put("transportInNum", "转储入笔数");
			relationMap.put("transTakOutNum", "转运出笔数");
			relationMap.put("transTakInNum", "转运入笔数");
			relationMap.put("takeNum", "盘点笔数");
			relationMap.put("allNum", "总计笔数");
			relationMap.put("pdaNum", "PDA操作笔数");
			relationMap.put("pdaPercentStr", "PDA应用率");
			
			
			List<String> orderList = new ArrayList<String>();
			orderList.add("板块");
			orderList.add("工厂");
			orderList.add("仓库号");
			orderList.add("创建人");
			orderList.add("入库笔数");
			orderList.add("出库笔数");
			orderList.add("上架笔数");
			orderList.add("下架笔数");
			orderList.add("转储出笔数");
			orderList.add("转储入笔数");
			orderList.add("转运出笔数");
			orderList.add("转运入笔数");
			orderList.add("盘点笔数");
			orderList.add("总计笔数");
			orderList.add("PDA操作笔数");
			orderList.add("PDA应用率");
			String root = constantConfig.getTempFilePath();

			String download_file_name = "quantityOfWork";// 作业量

			
			
			String filePath = UtilExcel.getExcelRopertUrl(download_file_name, returnList, relationMap, orderList, root);

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
	
	
	
	/**
	 * 15.1.1 创建人
	 * 
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/conf/jobstatis/get_user_list_by_location_id", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getUserListByLocationId(CurrentUser cuser, 
			String wh_id) {

		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		boolean status = true;

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		List<User> users=new ArrayList<User>();
		try {
			    users = jobStatisService.getUserListByWhId(UtilObject.getIntegerOrNull(wh_id));
			 
				if (users != null) {
					for (User u : users) {
						HashMap<String, Object> user = new HashMap<String, Object>();
						user.put("user_id", u.getUserId());
						user.put("user_name", UtilString.getStringOrEmptyForObject(u.getUserName()));
						returnList.add(user);
					}
				}			

		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
			logger.error("创建人", e);
		}


		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, returnList);

	}

	
	
	private List<Map<String, Object>> getVSList(List<Map<String, Object>> vsList) {
		Map<String, Object> userMap = new HashMap<String, Object>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (vsList != null && vsList.size() > 0) {

			for (Map<String, Object> innerMap : vsList) {

				String ftyId = UtilString.getStringOrEmptyForObject(innerMap.get("fty_id"));
				String ftyCode = UtilString.getStringOrEmptyForObject(innerMap.get("fty_code"));
				String locationId = UtilString.getStringOrEmptyForObject(innerMap.get("wh_id"));
				String locationCode = UtilString.getStringOrEmptyForObject(innerMap.get("wh_code"));
				String create_userString = UtilString.getStringOrEmptyForObject(innerMap.get("create_user"));
				String create_user_nameString =UtilString.getStringOrEmptyForObject(innerMap.get("create_user_name"));
				String ftyName = UtilString.getStringOrEmptyForObject(innerMap.get("fty_name"));
				String locationName = UtilString.getStringOrEmptyForObject(innerMap.get("wh_name"));
				Integer type = UtilObject.getIntegerOrNull(innerMap.get("type"));
				int boardIdint = UtilObject.getIntOrZero(innerMap.get("board_id"));
				int allnum = UtilObject.getIntOrZero(innerMap.get("allnum"));
				int pdaNum = UtilObject.getIntOrZero(innerMap.get("pdaNum"));
				//String userKey = ftyId + "-" + locationId + "-" + create_userString;
				String userKey =   create_userString;
				if (userMap.containsKey(userKey)) {
					LinkedHashMap<String, Object> returnMap = (LinkedHashMap<String, Object>) userMap.get(userKey);
					int innerNum = 0;

					switch (type) {
					case 10:
						innerNum = (int) returnMap.get("inputNum");
						innerNum += allnum;
						returnMap.put("inputNum", innerNum);

						break;
					case 20:
						innerNum = (int) returnMap.get("outputNum");
						innerNum += allnum;
						returnMap.put("outputNum", innerNum);
						break;
					case 34:
						innerNum = (int) returnMap.get("taskUpNum");
						innerNum += allnum;
						returnMap.put("taskUpNum", innerNum);
						break;
					case 33:
						innerNum = (int) returnMap.get("taskDownNum");
						innerNum += allnum;
						returnMap.put("taskDownNum", innerNum);
						break;
					case 51:
						innerNum = (int) returnMap.get("transportOutNum");
						innerNum += allnum;
						returnMap.put("transportOutNum", innerNum);
						break;
						
					case 52:
						innerNum = (int) returnMap.get("transportInNum");
						innerNum += allnum;
						returnMap.put("transportInNum", innerNum);
						break;
						
					case 53:
						innerNum = (int) returnMap.get("transTakOutNum");
						innerNum += allnum;
						returnMap.put("transTakOutNum", innerNum);
						break;
						
						
					case 54:
						innerNum = (int) returnMap.get("transTakInNum");
						innerNum += allnum;
						returnMap.put("transTakInNum", innerNum);
						break;
								
					case 71:
						innerNum = (int) returnMap.get("takeNum");
						innerNum += allnum;
						returnMap.put("takeNum", innerNum);
						break;
				   	default:
						break;

					}
					int countNum = (int) returnMap.get("allNum");
					countNum += allnum;
					returnMap.put("allNum", countNum);
					int countpdaNum = (int) returnMap.get("pdaNum");
					countpdaNum += pdaNum;
					returnMap.put("pdaNum", countpdaNum);
					userMap.put(userKey, returnMap);
				} else {
					LinkedHashMap<String, Object> returnMap = new LinkedHashMap<String, Object>();

					returnMap.put("boardName", EnumBoard.getNameByValue(boardIdint));
					returnMap.put("ftyCode", ftyCode);
					returnMap.put("ftyName", ftyName);
					returnMap.put("whCode", locationCode);
					returnMap.put("whName", locationName);
					returnMap.put("createUser", create_userString);
					returnMap.put("createUserName", create_user_nameString);
					returnMap.put("inputNum", 0);
					returnMap.put("outputNum", 0);
					returnMap.put("taskUpNum", 0);
					returnMap.put("taskDownNum", 0);
					returnMap.put("transportOutNum", 0);
					returnMap.put("transportInNum", 0);
					returnMap.put("transTakOutNum", 0); 
					returnMap.put("transTakInNum", 0);
					returnMap.put("takeNum", 0);
					returnMap.put("allNum", allnum) ;
					returnMap.put("pdaNum", pdaNum);
					switch (type) {
					case 10:
						returnMap.put("inputNum", allnum);
						break;
					case 20:
						returnMap.put("outputNum", allnum);
						break;
					case 34:
						returnMap.put("taskUpNum", allnum);
						break;
					case 33:
						returnMap.put("taskDownNum", allnum);
						break;
					case 51:
						returnMap.put("transportOutNum", allnum);
						break;
					case 52:
						returnMap.put("transportInNum", allnum);
						break;
					case 53:
						returnMap.put("transTakOutNum", allnum);
						break;
					case 54:
						returnMap.put("transTakInNum", allnum);
						break;
					case 71:
						returnMap.put("takeNum", allnum);
						break;
					default:
						break;
					}
					userMap.put(userKey, returnMap);
				}

			}

			Set<Entry<String, Object>> entries = userMap.entrySet();

			for (Entry<String, Object> entry : entries) {
				LinkedHashMap<String, Object> returnMap = (LinkedHashMap<String, Object>) entry.getValue();
				BigDecimal allNum = new BigDecimal((int) returnMap.get("allNum"));
				BigDecimal pdaNum = new BigDecimal((int) returnMap.get("pdaNum"));
				//pda占总的数
				BigDecimal pdaPercent = pdaNum.divide(allNum, 4, BigDecimal.ROUND_HALF_UP);

				returnMap.put("pdaPercent", pdaPercent);
				//得到百分数
				returnMap.put("pdaPercentStr", this.getPercentString(pdaPercent));
				returnList.add(returnMap);
			}

			this.sortJobStatisList(returnList);

		}

		return returnList;
	}
	
	/**
	 * 排序 按工厂code 库存地点code 人员id 排序
	 * @param zylList
	 */
	private void sortJobStatisList(List<Map<String, Object>> zylList) {
		Collections.sort(zylList, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {

				String ftyCodeStringo1 = (String) o1.get("ftyCode");
				String ftyCodeStringo2 = (String) o2.get("ftyCode");
				String locationStringo1 = (String) o1.get("whCode");
				String locationStringo2 = (String) o2.get("whCode");
				String userStringo1 = (String) o1.get("createUser");
				String userStringo2 = (String) o2.get("createUser");
				if (ftyCodeStringo1.compareTo(ftyCodeStringo2) > 0) {
					return 1;
				} else if (ftyCodeStringo1.compareTo(ftyCodeStringo2) < 0) {
					return -1;
				} else {
					if (locationStringo1.compareTo(locationStringo2) > 0) {
						return 1;
					} else if (locationStringo1.compareTo(locationStringo2) < 0) {
						return -1;
					} else {
						if (userStringo1.compareTo(userStringo2) > 0) {
							return 1;
						} else if (userStringo1.compareTo(userStringo2) < 0) {
							return -1;
						} else {
							return 0;
						}
					}
				}

			}
		});

	}
	
	
	
	private String getPercentString(BigDecimal value) {
		String valueString = "0%";
		if (value != null) {

		} else {
			value = new BigDecimal(0);
		}
		value = value.multiply(new BigDecimal(100));
		valueString = new DecimalFormat("#0.00").format(value) + "%";
		return valueString;
	}

	private String getPercentNum(BigDecimal value) {
		String valueString = "0%";
		if (value != null) {

		} else {
			value = new BigDecimal(0);
		}
		value = value.multiply(new BigDecimal(100));
		valueString = new DecimalFormat("#0.00").format(value);
		return valueString;
	}
}
