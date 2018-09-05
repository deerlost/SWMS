package com.inossem.wms.portable.conf;

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
import org.springframework.web.bind.annotation.CrossOrigin;
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

import net.sf.json.JSONArray;
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
	 * 作业量统计
	 * 
	 * @param cuser
	 * @return
	 */

	@RequestMapping(value = "/conf/jobstatis/get_job_statis_pictureList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getJobStatisPictureList(CurrentUser cuser) {

		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		boolean status = true;
        
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> zylList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> head=new HashMap<String,Object>();
		List<Map<String, Object>> detail=new ArrayList<Map<String, Object>>();
		try {		
			//查询出类型对应操作次数
			zylList = jobStatisService.getJobStatisForPortable(map);			
			head = this.getVSListHead(zylList);
			returnList = this.getVSListItem(zylList);
			detail = this.getVSListDetail(zylList);
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
			logger.error("作业量统计", e);
		}


		HashMap<String, Object> body = new HashMap<String, Object>();
		
		body.put("head", head);
		body.put("returnList", returnList);
		body.put("detail", detail);
		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, body);

	}
	
	private List<Map<String, Object>> getVSListItem(List<Map<String, Object>> vsList) {
		Map<String, Object> whMap = new HashMap<String, Object>();
//		Map<String, Object> userMap = new HashMap<String, Object>();
		Map<String, Map<String, Object>> allUserMap = new HashMap<String, Map<String, Object>>();
		Map<String, Map<String, Object>> whAndUserMap = new HashMap<String, Map<String, Object>>();
		Map<String, Map<String, Object>> whAndUserAllNumMap = new HashMap<String, Map<String, Object>>();
		Map<String, Map<String, Object>> whAndUserPdaNumMap = new HashMap<String, Map<String, Object>>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (vsList != null && vsList.size() > 0) {
			
			for (Map<String, Object> innerMap : vsList) {
				String wh_id = UtilString.getStringOrEmptyForObject(innerMap.get("wh_id"));	
				String create_user = UtilString.getStringOrEmptyForObject(innerMap.get("create_user_name"));	
				String wh_name = UtilString.getStringOrEmptyForObject(innerMap.get("wh_name"));
				String wh_code = UtilString.getStringOrEmptyForObject(innerMap.get("wh_code"));
				
				int allnum = UtilObject.getIntOrZero(innerMap.get("allnum"));
				int pdaNum = UtilObject.getIntOrZero(innerMap.get("pdaNum"));
				
				if (whMap.containsKey(wh_id)) {
					if (whAndUserMap.containsKey(wh_id)) {
//						if (whAndUserMap.get(wh_id).containsKey(create_user)) {
							Map<String, Object> userMap = whAndUserMap.get(wh_id);
							userMap.put(create_user, UtilObject.getIntOrZero(userMap.get(create_user))+1);
							whAndUserMap.put(wh_id, userMap);
//						}else {
//							Map<String, Object> userMap = whAndUserMap.get(wh_id);
//							userMap.put(create_user, UtilObject.getIntOrZero(1));
//							whAndUserMap.put(wh_id, userMap);
//						}
					}else {
						Map<String, Object> userMap = new HashMap<String, Object>();
						userMap.put(create_user, 1);
						whAndUserMap.put(wh_id, userMap);
					}
					
					if (whAndUserAllNumMap.containsKey(wh_id)) {
//						if (whAndUserAllNumMap.get(wh_id).containsKey(create_user)) {
							Map<String, Object> userMap = whAndUserAllNumMap.get(wh_id);
							userMap.put(create_user, UtilObject.getIntOrZero(userMap.get(create_user))+UtilObject.getIntOrZero(innerMap.get("allnum")));
							whAndUserAllNumMap.put(wh_id, userMap);
//						}else {
//							Map<String, Object> userMap = whAndUserAllNumMap.get(wh_id);
//							userMap.put(create_user, UtilObject.getIntOrZero(innerMap.get("allnum")));
//							whAndUserAllNumMap.put(wh_id, userMap);
//						}
					}else {
						Map<String, Object> userMap = new HashMap<String, Object>();
						userMap.put(create_user, UtilObject.getIntOrZero(innerMap.get("allnum")));
						whAndUserAllNumMap.put(wh_id, userMap);
					}
					
					if (whAndUserPdaNumMap.containsKey(wh_id)) {
//						if (whAndUserPdaNumMap.get(wh_id).containsKey(create_user)) {
							Map<String, Object> userMap = whAndUserPdaNumMap.get(wh_id);
							userMap.put(create_user, UtilObject.getIntOrZero(userMap.get(create_user))+UtilObject.getIntOrZero(innerMap.get("pdaNum")));
							whAndUserPdaNumMap.put(wh_id, userMap);
//						}else {
//							Map<String, Object> userMap = whAndUserPdaNumMap.get(wh_id);
//							userMap.put(create_user, UtilObject.getIntOrZero(innerMap.get("pdaNum")));
//							whAndUserPdaNumMap.put(wh_id, userMap);
//						}
					}else {
						Map<String, Object> userMap = new HashMap<String, Object>();
						userMap.put(create_user, UtilObject.getIntOrZero(innerMap.get("pdaNum")));
						whAndUserPdaNumMap.put(wh_id, userMap);
					}
					
					LinkedHashMap<String, Object> returnMap = (LinkedHashMap<String, Object>) whMap.get(wh_id);						
					int countNum = (int) returnMap.get("allNum");
					countNum += allnum;
					returnMap.put("allNum", countNum);
					int countpdaNum = (int) returnMap.get("pdaNum");
					countpdaNum += pdaNum;
					returnMap.put("pdaNum", countpdaNum);					
					whMap.put(wh_id, returnMap);
				} else {
					LinkedHashMap<String, Object> returnMap = new LinkedHashMap<String, Object>();
					returnMap.put("whName", wh_name);
					returnMap.put("wh_code", wh_code);
					returnMap.put("wh_id", wh_id);
					returnMap.put("allNum", allnum);
					returnMap.put("pdaNum", pdaNum);
					Map<String, Object> userMap = new HashMap<String, Object>();
					userMap.put(create_user, 1);
					whAndUserMap.put(wh_id, userMap);
					
					Map<String, Object> userAllNumMap = new HashMap<String, Object>();
					userAllNumMap.put(create_user, UtilObject.getIntOrZero(innerMap.get("allnum")));
					whAndUserAllNumMap.put(wh_id, userAllNumMap);
					
					Map<String, Object> userPdaNumMap = new HashMap<String, Object>();
					userPdaNumMap.put(create_user, UtilObject.getIntOrZero(innerMap.get("pdaNum")));
					whAndUserPdaNumMap.put(wh_id, userPdaNumMap);
					
					whMap.put(wh_id, returnMap);
				}
			}

			Set<Entry<String, Object>> entries = whMap.entrySet();

			for (Entry<String, Object> entry : entries) {
				LinkedHashMap<String, Object> returnMap = (LinkedHashMap<String, Object>) entry.getValue();
				BigDecimal allNum = new BigDecimal((int) returnMap.get("allNum"));
				BigDecimal pdaNum = new BigDecimal((int) returnMap.get("pdaNum"));
				//pda占总的数
				BigDecimal pdaPercent = pdaNum.divide(allNum, 4, BigDecimal.ROUND_HALF_UP);
				returnMap.put("pdaPercent", pdaPercent);
				//得到百分数
				returnMap.put("pdaPercentStr", this.getPercentString(pdaPercent));
				int wh_id=UtilObject.getIntOrZero(returnMap.get("wh_id")) ;
				Map<String, Object> m=whAndUserMap.get(UtilString.getStringOrEmptyForObject(wh_id));
				Map<String, Object> mAllNum=whAndUserAllNumMap.get(UtilString.getStringOrEmptyForObject(wh_id));
				Map<String, Object> mPdaNum=whAndUserPdaNumMap.get(UtilString.getStringOrEmptyForObject(wh_id));
				int num=0;
//				int numAll=0;
//				int numPda=0;				
//				for(String s:m.keySet()){
//					num=num+UtilObject.getIntOrZero(m.get(s));
//				}
				List<Map<String, Object>> userAll = new ArrayList<Map<String, Object>>();
				int i=1;
				
				JSONArray userArray = new JSONArray();
				JSONArray userAllNumArray = new JSONArray();
				JSONArray userPdaNumArray = new JSONArray();
				
				for(String s:mAllNum.keySet()){	
					Map<String, Object> all = new HashMap<String, Object>();
					all.put("i", i++);
					all.put("name", s);
					all.put("pdaNum", UtilObject.getIntOrZero(mPdaNum.get(s)));
					all.put("allNum", UtilObject.getIntOrZero(mAllNum.get(s)));	
					all.put("rate", this.getPercentString(new BigDecimal(UtilObject.getIntOrZero(mPdaNum.get(s))).divide(new BigDecimal(UtilObject.getIntOrZero(mAllNum.get(s))), 4, BigDecimal.ROUND_HALF_UP)));					
					userAll.add(all);
					userArray.add(s);
					userAllNumArray.add(UtilObject.getIntOrZero(mAllNum.get(s)));
					userPdaNumArray.add( UtilObject.getIntOrZero(mPdaNum.get(s)));
				}
				returnMap.put("userArray", userArray);
				returnMap.put("userAllNumArray", userAllNumArray);
				returnMap.put("userPdaNumArray",userPdaNumArray);
//				Map<String, Object> userpda = new HashMap<String, Object>();
//				for(String s:mPdaNum.keySet()){					
//					if(userpda.containsKey(mPdaNum.get(s))) {
//						userpda.put(mPdaNum.get(s).toString(), UtilObject.getIntOrZero(userpda.get(s))+UtilObject.getIntOrZero(mPdaNum.get(s)));
//					}else {
//						userpda.put(mPdaNum.get(s).toString(), UtilObject.getIntOrZero(mPdaNum.get(s)));
//					}
//				}
				returnMap.put("num", m.size());
				returnMap.put("user", userAll);
//				returnMap.put("mAllNum", userAll);
//				returnMap.put("mPdaNum", userpda);
//				returnMap.put("numAll", numAll);
//				returnMap.put("numPda", numPda);
				returnList.add(returnMap);
			}
//			this.sortJobStatisList(returnList);
		}
		return returnList;
	}
	
	
	private List<Map<String, Object>> getVSListDetail(List<Map<String, Object>> vsList) {
		Map<String, Map<String, Object>> allUserMap = new HashMap<String, Map<String, Object>>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (vsList != null && vsList.size() > 0) {
			
			for (Map<String, Object> innerMap : vsList) {		
				String create_user = UtilString.getStringOrEmptyForObject(innerMap.get("create_user_name"));	
				int allnum = UtilObject.getIntOrZero(innerMap.get("allnum"));
				int pdaNum = UtilObject.getIntOrZero(innerMap.get("pdaNum"));
				if (allUserMap.containsKey(create_user)) {					
					Map<String, Object> userAllnumMap = allUserMap.get(create_user);
					userAllnumMap.put("allnum", UtilObject.getIntOrZero(userAllnumMap.get("allnum"))+allnum);
					userAllnumMap.put("pdaNum", UtilObject.getIntOrZero(userAllnumMap.get("pdaNum"))+pdaNum);
					allUserMap.put(create_user, userAllnumMap);						
				}else {
					Map<String, Object> userAllnumMap = new HashMap<String, Object>();
					userAllnumMap.put("allnum", allnum);
					userAllnumMap.put("pdaNum", pdaNum);
					allUserMap.put(create_user, userAllnumMap);	
				}
			}
			int i=1;
			for(String s:allUserMap.keySet()){	
				Map<String, Object> userAllnumMap =allUserMap.get(s); 
				
				userAllnumMap.put("name", s);
				userAllnumMap.put("rate", this.getPercentString(new BigDecimal(UtilObject.getIntOrZero(userAllnumMap.get("pdaNum"))).divide(new BigDecimal(UtilObject.getIntOrZero(userAllnumMap.get("allnum"))), 4, BigDecimal.ROUND_HALF_UP)));
				returnList.add(userAllnumMap);
			}
		}
		
		this.sortJobStatisList(returnList);
		return returnList;
	}
	
	
	private Map<String, Object> getVSListHead(List<Map<String, Object>> vsList) {
		Map<String, Object> map = new HashMap<String, Object>();
		int allnum=0;
		int pdaNum=0;
		if (vsList != null && vsList.size() > 0) {
			for (Map<String, Object> innerMap : vsList) {				
				allnum += UtilObject.getIntOrZero(innerMap.get("allnum"));
				pdaNum += UtilObject.getIntOrZero(innerMap.get("pdaNum"));		
			}
			BigDecimal pdaPercent = new BigDecimal(pdaNum).divide(new BigDecimal(allnum), 4, BigDecimal.ROUND_HALF_UP);
			map.put("pdaPercentName", "总应用率");
			map.put("pdaPercent", this.getPercentString(pdaPercent));
		}
		map.put("allnum", allnum);	
		map.put("pdaNum", pdaNum);
		map.put("webNum", new BigDecimal(allnum).subtract(new BigDecimal(pdaNum)));
		
		map.put("allnumName", "总单数");	
		map.put("pdaNumName", "移动作业量");
		map.put("webNumName","桌面操作应用");
		return map;
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
	 * 排序 
	 * @param zylList
	 */
	private void sortJobStatisList(List<Map<String, Object>> zylList) {
		Collections.sort(zylList, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {

				int allNumStringo1 = (int) o1.get("allnum");
				int allNumStringo2 = (int) o2.get("allnum");
				
				if (allNumStringo1-allNumStringo2 < 0) {
					return 1;
				} else if (allNumStringo1-allNumStringo2 > 0) {
					return -1;
				} else {
					return 0;
				}

			}
		});

	}
	
	private void sortJobStatisListByAllNum(List<Map<String, Object>> zylList) {
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
		valueString = new DecimalFormat("#0").format(value) + "%";
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
