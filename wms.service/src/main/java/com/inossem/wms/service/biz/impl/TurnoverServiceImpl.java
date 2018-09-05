package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.inossem.wms.constant.Const;
import com.inossem.wms.dao.biz.BizStockInputItemMapper;
import com.inossem.wms.dao.biz.BizStockOutputItemMapper;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.stock.StockBatchBeginMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.enums.EnumDataType;
import com.inossem.wms.model.enums.EnumTurnOverType;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.ITurnoverService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service("turnoverService")
public class TurnoverServiceImpl implements ITurnoverService {

	@Resource
	private IDictionaryService dictionaryService;

	@Autowired
	private DicStockLocationMapper stockLocationDao;

	
	// @Autowired
	// private BizMaterialDocItemMapper mDocItemDao;

	@Autowired
	private BizStockInputItemMapper bizStockInputItemMapper;
	@Autowired
	private BizStockOutputItemMapper bizStockOutputItemMapper;
	
	@Autowired
	private StockBatchBeginMapper stockBatchBeginDao;

	@Override
	public JSONArray selectInfoData(int boardId) {
		JSONArray body = new JSONArray();

		List<Map<String, Object>> corpList = stockLocationDao.selectCorpListByBoardId(boardId);
		for (Map<String, Object> corp : corpList) {
			JSONObject corpJson = new JSONObject();
			corpJson.put("id", corp.get("corp_id"));// 公司id
			corpJson.put("key", corp.get("corp_code"));// 公司code
			corpJson.put("value", corp.get("corp_name"));// 公司名称

			List<Map<String, Object>> factoryList = stockLocationDao
					.selectFactoryListByCorpId(UtilObject.getIntOrZero(corp.get("corp_id")));
			JSONArray factoryArr = new JSONArray();
			for (Map<String, Object> factory : factoryList) {
				JSONObject factoryJson = new JSONObject();
				factoryJson.put("id", factory.get("fty_id"));// 工厂id
				factoryJson.put("key", factory.get("fty_code"));// 工厂id
				factoryJson.put("value", factory.get("fty_name"));// 厂名称

				List<Map<String, Object>> locationList = stockLocationDao
						.selectLocationListByFtyId(UtilObject.getIntOrZero(factory.get("fty_id")));
				JSONArray locationArr = new JSONArray();
				for (Map<String, Object> location : locationList) {
					JSONObject locationJson = new JSONObject();
					locationJson.put("id", location.get("location_id"));// 库存地点id
					locationJson.put("key", location.get("location_code"));// 库存地点id
					locationJson.put("value", location.get("location_name"));// 库存地点名称
					locationArr.add(locationJson);
				}
				factoryJson.put("body", locationArr);// 下属库存地点
				factoryArr.add(factoryJson);
			}
			corpJson.put("body", factoryArr);// 下属工厂

			body.add(corpJson);
		}
		return body;
	}

	@Override
	public List<Map<String, Object>> selectMatGroup(Map<String, Object> paramMap) {

		return stockLocationDao.selectMatGroup(paramMap);
	}

	@Override
	public JSONArray getReportDetail(String boardId, String ftyId, String whId, String matGroupId, String timeFr, String timeTo) throws Exception  {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", boardId);
		map.put("ftyId", ftyId);
		map.put("whId", whId);
		map.put("matGroupId", matGroupId);
		map.put("timeFr", timeFr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
        Calendar c = Calendar.getInstance(); 
		
	    c.setTime(sdf.parse(timeTo));  
	    c.add(Calendar.DAY_OF_MONTH, 1);
	    String timeToAddOne=  sdf.format(c.getTime());
	     
	     map.put("timeTo", timeToAddOne);
		//物料组名map
		Map<String, String> matGroupCodeNameMap = new HashMap<String, String>();
		
		
		//本期入库金额 ftyId, whId, matGroupId, inputAmount, inputDetails
		List<Map<String, Object>> inputList = bizStockInputItemMapper.getInputAmount(map);//本期入库金额
		Map<String, BigDecimal>  inputMap = new HashMap<String, BigDecimal>();
		for (Map<String, Object> map2 : inputList) {
			BigDecimal inputMoney = new BigDecimal(0);
			//物料组名
			String inputMatklMoney =  UtilString.getStringOrEmptyForObject(map2.get("mat_group_name"));
			inputMap.put(inputMatklMoney, inputMoney);
			matGroupCodeNameMap.put(inputMatklMoney,UtilString.getStringOrEmptyForObject(map2.get("mat_group_code")));	
		}
		for (Map<String, Object> map2 : inputList) {
			BigDecimal inputMoney = new BigDecimal(UtilObject.getStringOrEmpty(map2.get("inputAmount")));
			//物料组名
			String inputMatklMoney =  UtilString.getStringOrEmptyForObject(map2.get("mat_group_name"));
			inputMap.put(inputMatklMoney, inputMap.get(inputMatklMoney).add(inputMoney));
			matGroupCodeNameMap.put(inputMatklMoney,UtilString.getStringOrEmptyForObject(map2.get("mat_group_code")));
		}
		//本期出库金额  ftyId, whId, matGroupId, outputAmount, outputDetails
		List<Map<String, Object>> outputList = bizStockOutputItemMapper.getOutputAmount(map);//本期出库金额
		Map<String, BigDecimal>  outnputMap = new HashMap<String, BigDecimal>();
		for (Map<String, Object> map2 : outputList) {
			BigDecimal outnputMoney = new BigDecimal(0);
			String outputMatklMoney =  UtilString.getStringOrEmptyForObject(map2.get("mat_group_name"));
			outnputMap.put(outputMatklMoney, outnputMoney);
		}
		for (Map<String, Object> map2 : outputList) {
			BigDecimal outnputMoney = new BigDecimal(UtilObject.getStringOrEmpty(map2.get("outputAmount")));
			String outputMatklMoney =  UtilString.getStringOrEmptyForObject(map2.get("mat_group_name"));
			outnputMap.put(outputMatklMoney, outnputMap.get(outputMatklMoney).add(outnputMoney));
		}
		
		Map<String, Object> moneyMap = new HashMap<String, Object>();
		moneyMap.put("boardId", boardId);
		moneyMap.put("ftyId", ftyId);
        moneyMap.put("whId", whId);
        moneyMap.put("matGroupId", matGroupId);
        //当前库存金额 ftyId, whId, matGroupId, money
		List<Map<String,Object>> moneyList = stockLocationDao.selectNowAmountByParam(moneyMap);
		//物料组名  金额
		Map<String, BigDecimal>  moneyResult = new HashMap<String, BigDecimal>();
		
		for(Map<String,Object> money : moneyList) {
			moneyResult.put(UtilObject.getStringOrEmpty(money.get("mat_group_name")), new BigDecimal(0));		
		}		
		for(Map<String,Object> money : moneyList) {
			moneyResult.put((String) money.get("mat_group_name"), moneyResult.get(money.get("mat_group_name")).add(UtilObject.getBigDecimalOrZero(money.get("money"))));
		
		}
		
		
		//----期末库存金额start
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//Calendar c = Calendar.getInstance(); 
		
	    //c.setTime(sdf.parse(timeTo));  
	    //c.add(Calendar.DAY_OF_MONTH, 1);
		String timeFrOther = sdf.format(c.getTime());
		
		c.setTime(new Date()); 
		c.add(Calendar.DAY_OF_MONTH, 1);
		String timeToOther = sdf.format(c.getTime());
				
		Map<String, Object> mapOther = new HashMap<String, Object>();
		mapOther.put("ftyId", ftyId);
		mapOther.put("whId", whId);
		mapOther.put("matGroupId", matGroupId);
		mapOther.put("timeFr", timeFrOther);
		mapOther.put("timeTo", timeToOther);
		//本期与当前间隔入库金额    ftyId, locationId, matGroupId, inputAmount, inputDetails
		List<Map<String, Object>> inputOtherList = bizStockInputItemMapper.getInputAmount(mapOther);
		Map<String, BigDecimal>  inputOtherMap = new HashMap<String, BigDecimal>();
		
		for (Map<String, Object> map2 : inputOtherList) {
			BigDecimal inputMoney = new BigDecimal(0);
			String inputMatklMoney =  UtilString.getStringOrEmptyForObject(map2.get("mat_group_name"));
			inputOtherMap.put(inputMatklMoney, inputMoney);
		}
		for (Map<String, Object> map2 : inputOtherList) {
			BigDecimal inputMoney = new BigDecimal(map2.get("inputAmount").toString());
			String inputMatklMoney =  UtilString.getStringOrEmptyForObject(map2.get("mat_group_name"));
			inputOtherMap.put(inputMatklMoney, inputOtherMap.get(inputMatklMoney).add(inputMoney));
		}
				
		//本期与当前间隔出库金额   ftyId, locationId, matGroupId, outputAmount, outputDetails
		List<Map<String, Object>> outputOtherList = bizStockOutputItemMapper.getOutputAmount(mapOther);
		Map<String, BigDecimal>  outnputOtherMap = new HashMap<String, BigDecimal>();
		for (Map<String, Object> map2 : outputOtherList) {
			BigDecimal outnputMoney = new BigDecimal(0);
			String outputMatklMoney =  UtilString.getStringOrEmptyForObject(map2.get("mat_group_name"));
			outnputOtherMap.put(outputMatklMoney, outnputMoney);
		}
		for (Map<String, Object> map2 : outputOtherList) {
			BigDecimal outnputMoney = new BigDecimal(map2.get("outputAmount").toString());
			String outputMatklMoney =  UtilString.getStringOrEmptyForObject(map2.get("mat_group_name"));
			outnputOtherMap.put(outputMatklMoney,outnputOtherMap.get(outputMatklMoney).add(outnputMoney) );
		}
     
		
		//少情况    间隔出库和间隔入库同时存在时才起作用    做成用两个循环 期末map
		Map<String, BigDecimal>  currentMap = new HashMap<String, BigDecimal>();
//		for (String key : moneyResult.keySet()) {
//			for (String outkey : outnputOtherMap.keySet()) {
//				//当当前库存与间隔出库有相同的
//				if (key.equals(outkey)) {
//					for (String inkey : inputOtherMap.keySet()) {
//						if (key.equals(inkey)) {							
//							BigDecimal currentMoney = moneyResult.get(key).add(outnputOtherMap.get(outkey).subtract(inputOtherMap.get(inkey)));
//							currentMap.put(key, currentMoney);					
//							break;				
//				     	}						
//			        }
//			//break;
//					
//			 }
//		  }
//	   }
		//当前库存加上本期与当前间隔出库金额
		for (String key : moneyResult.keySet()) {
			for (String outkey : outnputOtherMap.keySet()) {
				if (key.equals(outkey)) {
					BigDecimal currentMoney = moneyResult.get(key).add(outnputOtherMap.get(outkey));
					currentMap.put(key, currentMoney);					
					break;											
				     }
				}		
		}		
		//当前库存减去本期与当前间隔入库金额
		for (String key : moneyResult.keySet()) {
			for (String inkey : inputOtherMap.keySet()) {
				//
				if (key.equals(inkey)) {
					//当前减去入库  currentmap 为加过出库的
					if(currentMap.containsKey(inkey)) {
						BigDecimal currentMoney = currentMap.get(key).subtract(inputOtherMap.get(inkey));
						currentMap.put(key, currentMoney);	
						//当入库里面没有   但是出库有
					}else  {
						BigDecimal currentMoney = moneyResult.get(key).subtract(inputOtherMap.get(inkey));
						currentMap.put(key, currentMoney);	
					}
								
					break;											
				     }
				}		
		}
		
		
		//----期末库存金额end
		
		//存储当前库存、物料凭证发生出库入库的所有物料组合集
		Set<String> allMatkl=new HashSet<String>();
		
		for (String currentKey : currentMap.keySet()) {
			allMatkl.add(currentKey);
		}
		for (String inputKey : inputMap.keySet()) {
			allMatkl.add(inputKey);
		}
		for (String outputKey : outnputMap.keySet()) {
			allMatkl.add(outputKey);
		}
			
		
		
		//----整合所有金额Start
		JSONArray resultAry = new JSONArray();
		//List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (Object object : allMatkl) {
			String key = UtilObject.getStringOrEmpty(object);
			
			if (!currentMap.keySet().contains(key)) {
				//currentMap.put(key, BigDecimal.ZERO);
			currentMap.put(key,  moneyResult.get(key));
			}
			if (!inputMap.keySet().contains(key)) {
				inputMap.put(key, BigDecimal.ZERO);
			}
			if (!outnputMap.keySet().contains(key)) {
				outnputMap.put(key, BigDecimal.ZERO);
			}
		}
		
		
		for (String currentKey : currentMap.keySet()) {
			JSONObject innerObject = new JSONObject();
			
			innerObject.put("mat_group_code", matGroupCodeNameMap.get(currentKey));
			innerObject.put("mat_group_name", currentKey);// 物料组描述
			for (String inputKey : inputMap.keySet()) {
				if (inputKey.equals(currentKey)) {
					for (String outputKey : outnputMap.keySet()) {
						if (outputKey.equals(currentKey)) {
							BigDecimal finalMoney = UtilObject.getBigDecimalOrZero(currentMap.get(currentKey));//期末库存金额A
							BigDecimal outputMoney = UtilObject.getBigDecimalOrZero(outnputMap.get(outputKey));//本期出库金额B
							BigDecimal inputMoney = UtilObject.getBigDecimalOrZero(inputMap.get(inputKey));//本期入库金额C
							BigDecimal beginMoney = finalMoney.add(outputMoney).subtract(inputMoney);//期初库存金额D=A+B-C
							BigDecimal averageMoney = (finalMoney.add(beginMoney)).divide(new BigDecimal("2"), 2);// 平均存货金额E=(A+D)/2
							BigDecimal times = BigDecimal.ZERO;
							BigDecimal days = BigDecimal.ZERO;
							if (averageMoney.compareTo(BigDecimal.ZERO)!=0) {
								times = outputMoney.divide(averageMoney, 3);// 存货周转次数F=B/E
								if(times.compareTo(BigDecimal.ZERO)!=0) {
								days = new BigDecimal(365).divide(times, 3);// 存货周转天数G=365/F
								}
							}	
							innerObject.put("finalMoney", finalMoney.setScale(2, RoundingMode.HALF_UP));
							innerObject.put("outnputMoney", outputMoney.setScale(2, RoundingMode.HALF_UP));
							innerObject.put("inputMoney", inputMoney.setScale(2, RoundingMode.HALF_UP));
							innerObject.put("beginMoney", beginMoney.setScale(2, RoundingMode.HALF_UP));
							innerObject.put("averageMoney", averageMoney.setScale(2, RoundingMode.HALF_UP));
							innerObject.put("times", times);
							innerObject.put("days", days);
						//	innerObject.put("boardId", boardId);
						///	innerObject.put("boardName", EnumBoard.getNameByValue(UtilObject.getIntegerOrNull(boardId)));
						//	Integer corpId = dictionaryService.getFtyIdMap().get(UtilObject.getIntegerOrNull(ftyId)).getCorpId();
							
							//innerObject.put("corpName", dictionaryService.getCorpIdMap().get(corpId).getCorpName());
							
//							innerObject.put("locationName", dictionaryService.getLocationIdMap()
//									.get(UtilObject.getIntegerOrNull(locationId)).getLocationName());
							//改动   按仓库查询
					    	//innerObject.put("whName", dicWarehouseMapper.selectByPrimaryKey(UtilObject.getIntegerOrNull(whId)).getWhName());
								//	.get(UtilObject.getIntegerOrNull(locationId)).getLocationName());
							
//							innerObject.put("ftyName", dictionaryService.getFtyIdMap()
//									.get(UtilObject.getIntegerOrNull(ftyId)).getFtyName());
							//innerObject.put("days", days.setScale(2, RoundingMode.HALF_UP));
							resultAry.add(innerObject);
						}
					}
				}

			}
		}
		
		
		//----整合所有金额end
		
		return resultAry;
	}

	@Override
	public List<Map<String, Object>> downloadTurnover(Map<String, Object> param) {
		return bizStockInputItemMapper.downloadTurnover(param);
	}

	@Override
	public Map<String, Object> getOverview(String boardId, String corpId, String whId, String date, int type)
			throws Exception {
		// 查询板块 公司下所有数据
		BigDecimal inputData = new BigDecimal(0);
		BigDecimal outputData = new BigDecimal(0);
		BigDecimal startData = new BigDecimal(0);
		BigDecimal endData = new BigDecimal(0);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
		boolean currentMonth = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", boardId);
		map.put("whId", whId);
		map.put("corpId", corpId);
		map.put("type", type);
		if(StringUtils.hasText(date)){
			// 选择月份
			String dateAry[] = date.split("-");
			int year = UtilObject.getIntOrZero(dateAry[0]);
			int mounth = UtilObject.getIntOrZero(dateAry[1]);
			String currentDay = UtilString.getShortStringForDate(new Date());
			String dateAryCurrent[] = currentDay.split("-");
			int currentMonthNum = UtilObject.getIntOrZero(dateAryCurrent[1]);
			if(mounth != currentMonthNum){
				currentMonth = false;
			}
			if(mounth>currentMonthNum){
				throw new WMSException("请选择正确月份");
			}
			Calendar cal_1 = Calendar.getInstance();// 获取当前日期
			String firstDay = "";
			String first = "";
			if(currentMonth){
				// 查询本月
				cal_1.add(Calendar.MONTH, 0);
				cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				firstDay = format.format(cal_1.getTime());
				first = currentDay;
			}else{
				// 查询前一月
				cal_1.set(year, mounth-1, 1);
				
				firstDay = format.format(cal_1.getTime());
				
				cal_1.add(Calendar.MONTH, 1);
				cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				first = format.format(cal_1.getTime());
			}

			map.put("timeFr", firstDay);

			map.put("timeTo", first);
			List<Map<String, Object>> inputList = bizStockInputItemMapper.getInputAmount(map);//本期入库金额
			List<Map<String, Object>> outputList = bizStockOutputItemMapper.getOutputAmount(map);//本期出库金额
			
			
			if(inputList!=null&&inputList.size()>0){
				for(Map<String, Object> innerMap:inputList){
					BigDecimal addB = new BigDecimal(0);
					if (type == EnumDataType.MONEY.getValue()) {
						addB = UtilObject.getBigDecimalOrZero(innerMap.get("inputAmount"));
					} else if (type == EnumDataType.QTY.getValue()) {
						addB = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
					}
					inputData = inputData.add(addB);
				}
			}
			if(outputList!=null&&outputList.size()>0){
				for(Map<String, Object> innerMap:outputList){
					BigDecimal addB = new BigDecimal(0);
					if (type == EnumDataType.MONEY.getValue()) {
						addB = UtilObject.getBigDecimalOrZero(innerMap.get("outputAmount"));
					} else if (type == EnumDataType.QTY.getValue()) {
						addB = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
					}
					outputData = outputData.add(addB);
				}
			}
			Map<String, Object> param = new HashMap<String, Object>();
			
			
			param.put("boardId", UtilObject.getIntegerOrNull(boardId));
			param.put("whId", UtilObject.getIntegerOrNull(whId));
			param.put("corpId", UtilObject.getIntegerOrNull(corpId));
			Map<String, Object> endingBalance = null;
			BigDecimal endingBalanceQty = new BigDecimal(0);
			if(currentMonth){
				endingBalance = stockBatchBeginDao.selectCurrentStockByParam(param);
			}else{
				
				param.put("createTime", first);
				endingBalance = stockBatchBeginDao.selectByParam(param);
				
			}
			if(endingBalance!=null){
				if (type == EnumDataType.MONEY.getValue()) {
					endingBalanceQty = UtilObject.getBigDecimalOrZero(endingBalance.get("qtyAmount"));
				} else if (type == EnumDataType.QTY.getValue()) {
					endingBalanceQty = UtilObject.getBigDecimalOrZero(endingBalance.get("qty"));
				}
			}
			
			endData = endingBalanceQty;
			startData = endData.add(outputData).subtract(inputData);
			
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String unitName = "";
		if (type == EnumDataType.MONEY.getValue()) {
			unitName = "万元";
		} else if (type == EnumDataType.QTY.getValue()) {
			unitName = "KG";
		}
		returnMap.put("inputData", inputData);
		returnMap.put("outputData", outputData);
		returnMap.put("startData", startData);
		returnMap.put("endData", endData);
		returnMap.put("unitName", unitName);
		
		return returnMap;
	}

	@Override
	public Map<String, Object> getAvgview(String boardId, String corpId, String whId, String date, int type,int month)
			throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("boardId", UtilObject.getIntegerOrNull(boardId));
		param.put("whId", UtilObject.getIntegerOrNull(whId));
		
		param.put("corpId", UtilObject.getIntegerOrNull(corpId));
		param.put("type", type);
		boolean currentMonth = true;
		BigDecimal maxValue = new BigDecimal(0);
		if(StringUtils.hasText(date)){
			// 选择月份
			String dateAry[] = date.split("-");
			int year = UtilObject.getIntOrZero(dateAry[0]);
			int mounth = UtilObject.getIntOrZero(dateAry[1]);
			String currentDay = UtilString.getShortStringForDate(new Date());
			String dateAryCurrent[] = currentDay.split("-");
			int currentMonthNum = UtilObject.getIntOrZero(dateAryCurrent[1]);
			if(mounth != currentMonthNum){
				currentMonth = false;
			}
			if(mounth>currentMonthNum){
				throw new WMSException("请选择正确月份");
			}
			List<Map<String, Object>> xAxisList = new ArrayList<Map<String,Object>>();
			for(int i=0;i<month;i++){
				
				Calendar cal_1 = Calendar.getInstance();// 获取当前日期
				cal_1.set(year, mounth, 1);
				cal_1.add(Calendar.MONTH, -(month-i));
				cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				Date firstDate = cal_1.getTime();
				String firstDay = format.format(firstDate);

				Calendar c = Calendar.getInstance();
				c.set(year, mounth, 1);
				c.add(Calendar.MONTH, -(month-i-1));
				c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				String first = format.format(c.getTime());
				if(firstDate.compareTo(Const.BEGIN_DATE)<0){
					continue;
				}
				Map<String, Object> startBalance = null;
				BigDecimal startBalanceQty = new BigDecimal(0);
				param.put("createTime", firstDay);
				startBalance = stockBatchBeginDao.selectByParam(param);
				
				Map<String, Object> endingBalance = null;
				BigDecimal endingBalanceQty = new BigDecimal(0);
				if(currentMonth){
					param.remove("createTime");
					endingBalance = stockBatchBeginDao.selectCurrentStockByParam(param);
				}else{
					
					param.put("createTime", first);
					endingBalance = stockBatchBeginDao.selectByParam(param);
					
				}
				if(startBalance!=null){
					if (type == EnumDataType.MONEY.getValue()) {
						startBalanceQty = UtilObject.getBigDecimalOrZero(startBalance.get("qtyAmount"));
					} else if (type == EnumDataType.QTY.getValue()) {
						startBalanceQty = UtilObject.getBigDecimalOrZero(startBalance.get("qty"));
					}
				}
				
				if(endingBalance!=null){
					if (type == EnumDataType.MONEY.getValue()) {
						endingBalanceQty = UtilObject.getBigDecimalOrZero(endingBalance.get("qtyAmount"));
					} else if (type == EnumDataType.QTY.getValue()) {
						endingBalanceQty = UtilObject.getBigDecimalOrZero(endingBalance.get("qty"));
					}
				}
				BigDecimal avgBalanceQty = (startBalanceQty.add(endingBalanceQty)).divide(new BigDecimal(2),5,BigDecimal.ROUND_HALF_DOWN);
				if(maxValue.compareTo(avgBalanceQty)<0){
					maxValue = avgBalanceQty;
				}
				
				Map<String, Object> xAxis = new HashMap<String, Object>();
				
				xAxis.put("xAxisName", format2.format(firstDate));
				List<Map<String, Object>> yAxisValueList = new ArrayList<Map<String,Object>>();
				Map<String, Object> yAxisValueInput = new HashMap<String, Object>();
				yAxisValueInput.put("yAxisValue", avgBalanceQty);
				
				yAxisValueList.add(yAxisValueInput);
				
				xAxis.put("yAxisValueList", yAxisValueList);
				
				xAxisList.add(xAxis);
				
				
			}
			
			
			List<Map<String, Object>> chartInfoList = new ArrayList<Map<String,Object>>();
			Map<String, Object> chartInfoInput = new HashMap<String, Object>();
			chartInfoInput.put("name", "平均存货");
			chartInfoInput.put("type", 1);
			chartInfoInput.put("dependence", 0);
			
			chartInfoList.add(chartInfoInput);
			
			
			
			returnMap.put("leftAxisMaxValue", maxValue);
			returnMap.put("rightAxisMaxValue", "");
			String unitName = "";
			if (type == EnumDataType.MONEY.getValue()) {
				unitName = "万元";
			} else if (type == EnumDataType.QTY.getValue()) {
				unitName = "KG";
			}
			returnMap.put("leftAxisUnit", unitName);
			returnMap.put("rightAxisUnit", "");
			returnMap.put("title", "平均存货");
			returnMap.put("chartInfoList", chartInfoList);
			returnMap.put("xAxisList", xAxisList);
			if(xAxisList.size()==0){
				throw new WMSException("暂无数据");
			}
			List<Map<String, Object>> yLimitLine = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> xLimitLine = new ArrayList<Map<String,Object>>();
			returnMap.put("xLimitLine", xLimitLine);
			returnMap.put("yLimitLine", yLimitLine);
			
			
			
			
		}else{
			throw new WMSException("请选择月份");
		}
		
			
		return returnMap;
	}

	@Override
	public List<Map<String, Object>> getWhAndMatGroupView(String boardId, String corpId, String whId, String date, int type, String matGroupId, boolean groupMat)
			throws Exception {
		// 查询板块 公司下所有数据
		List<Map<String, Object>> returnList= new ArrayList<Map<String,Object>>();
		
	
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
		boolean currentMonth = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", boardId);
		map.put("whId", whId);
		map.put("groupWh", true);
		map.put("corpId", corpId);
		map.put("type", type);
		if(StringUtils.hasText(matGroupId)){
			map.put("matGroupId", matGroupId);
		}
		if(groupMat){
			map.put("groupMat", groupMat);
		}
		if(StringUtils.hasText(date)){
			// 选择月份
			String dateAry[] = date.split("-");
			int year = UtilObject.getIntOrZero(dateAry[0]);
			int mounth = UtilObject.getIntOrZero(dateAry[1]);
			String currentDay = UtilString.getShortStringForDate(new Date());
			String dateAryCurrent[] = currentDay.split("-");
			int currentMonthNum = UtilObject.getIntOrZero(dateAryCurrent[1]);
			if(mounth != currentMonthNum){
				currentMonth = false;
			}
			
			Calendar cal_1 = Calendar.getInstance();// 获取当前日期
			String firstDay = "";
			String first = "";
			if(currentMonth){
				// 查询本月
				cal_1.add(Calendar.MONTH, 0);
				cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				firstDay = format.format(cal_1.getTime());
				first = currentDay;
			}else{
				// 查询前一月
				cal_1.set(year, mounth -1, 1);
				
				firstDay = format.format(cal_1.getTime());
				
				cal_1.add(Calendar.MONTH, 1);
				cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				first = format.format(cal_1.getTime());
			}

			map.put("timeFr", firstDay);

			map.put("timeTo", first);
			List<Map<String, Object>> inputList = bizStockInputItemMapper.getInputAmount(map);//本期入库金额
			List<Map<String, Object>> outputList = bizStockOutputItemMapper.getOutputAmount(map);//本期出库金额
			Map<String, Map<String, Object>> whMatGroupMap = new HashMap<String, Map<String,Object>>();
			
			if(inputList!=null&&inputList.size()>0){
				for(Map<String, Object> innerMap:inputList){
					BigDecimal addB = new BigDecimal(0);
					if (type == EnumDataType.MONEY.getValue()) {
						addB = UtilObject.getBigDecimalOrZero(innerMap.get("inputAmount"));
					} else if (type == EnumDataType.QTY.getValue()) {
						addB = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
					}
					
					Map<String, Object> param = new HashMap<String, Object>();
					
					Integer whIdKey = UtilObject.getIntOrZero(innerMap.get("wh_id"));
					param.put("boardId", boardId);
					param.put("whId", whIdKey);
					param.put("corpId", corpId);
					
					Map<String, Object> endingBalance = null;
					BigDecimal endingBalanceQty = new BigDecimal(0);
					if(currentMonth){
						param.remove("createTime");
						endingBalance = stockBatchBeginDao.selectCurrentStockByParam(param);
					}else{
						
						param.put("createTime", first);
						endingBalance = stockBatchBeginDao.selectByParam(param);
						
					}
					
					
					if(endingBalance==null){
						endingBalance = new HashMap<String, Object>();
						endingBalance.put("qtyAmount",0);
						endingBalance.put("qty",0);
					}
					if (type == EnumDataType.MONEY.getValue()) {
						endingBalanceQty = UtilObject.getBigDecimalOrZero(endingBalance.get("qtyAmount"));
					} else if (type == EnumDataType.QTY.getValue()) {
						endingBalanceQty = UtilObject.getBigDecimalOrZero(endingBalance.get("qty"));
					}
					
					
					String key = UtilObject.getIntOrZero(innerMap.get("wh_id"))+"-"+UtilObject.getIntOrZero(innerMap.get("mat_group_id"));
					if(groupMat){
						key += UtilObject.getIntOrZero(innerMap.get("mat_id"));
					}
					
					innerMap.put("startData", new BigDecimal(0));
					innerMap.put("inputData", addB);
					innerMap.put("outputData", new BigDecimal(0));
					innerMap.put("endData", endingBalanceQty);
					whMatGroupMap.put(key, innerMap);
				}
			}
			if(outputList!=null&&outputList.size()>0){
				for(Map<String, Object> innerMap:outputList){
					BigDecimal addB = new BigDecimal(0);
					if (type == EnumDataType.MONEY.getValue()) {
						addB = UtilObject.getBigDecimalOrZero(innerMap.get("outputAmount"));
					} else if (type == EnumDataType.QTY.getValue()) {
						addB = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
					}
					
					String key = UtilObject.getIntOrZero(innerMap.get("wh_id"))+"-"+UtilObject.getIntOrZero(innerMap.get("mat_group_id"));
					if(groupMat){
						key += UtilObject.getIntOrZero(innerMap.get("mat_id"));
					}
					if(whMatGroupMap.containsKey(key)){
						Map<String , Object> inputMap = whMatGroupMap.get(key);
						inputMap.put("outputQty", addB);
						inputMap.put("outputData", addB);
						whMatGroupMap.put(key, inputMap);
					}else{
						
						Map<String, Object> param = new HashMap<String, Object>();
						
						Integer whIdKey = UtilObject.getIntOrZero(innerMap.get("wh_id"));
						param.put("boardId", boardId);
						param.put("whId", whIdKey);
						param.put("corpId", corpId);
						
						Map<String, Object> endingBalance = null;
						BigDecimal endingBalanceQty = new BigDecimal(0);
						if(currentMonth){
							param.remove("createTime");
							endingBalance = stockBatchBeginDao.selectCurrentStockByParam(param);
						}else{
							
							param.put("createTime", first);
							endingBalance = stockBatchBeginDao.selectByParam(param);
							
						}
						
						
						if(endingBalance==null){
							endingBalance = new HashMap<String, Object>();
							endingBalance.put("qtyAmount",0);
							endingBalance.put("qty",0);
						}
						if (type == EnumDataType.MONEY.getValue()) {
							endingBalanceQty = UtilObject.getBigDecimalOrZero(endingBalance.get("qtyAmount"));
						} else if (type == EnumDataType.QTY.getValue()) {
							endingBalanceQty = UtilObject.getBigDecimalOrZero(endingBalance.get("qty"));
						}
						innerMap.put("startData", new BigDecimal(0));
						innerMap.put("inputData", new BigDecimal(0));
						innerMap.put("outputData", addB);
						innerMap.put("endData", endingBalanceQty);
						whMatGroupMap.put(key, innerMap);
					}
					
				}
			}
			
			for(String key : whMatGroupMap.keySet()){
				
				Map<String, Object> innerMap = whMatGroupMap.get(key);
				BigDecimal outputData = UtilObject.getBigDecimalOrZero(innerMap.get("outputData")).setScale(5, BigDecimal.ROUND_HALF_UP);
				BigDecimal inputData = UtilObject.getBigDecimalOrZero(innerMap.get("inputData")).setScale(5, BigDecimal.ROUND_HALF_UP);
				BigDecimal endData = UtilObject.getBigDecimalOrZero(innerMap.get("endData")).setScale(5, BigDecimal.ROUND_HALF_UP);
				BigDecimal startData = (endData.add(outputData).subtract(inputData)).setScale(5, BigDecimal.ROUND_HALF_UP);
				
				BigDecimal avgData = (startData.add(endData)).divide(new BigDecimal(2),5,BigDecimal.ROUND_HALF_DOWN);
				BigDecimal overTims = new BigDecimal(0);
				if(avgData.compareTo(BigDecimal.ZERO)!=0){
					overTims = outputData.divide(avgData,5,BigDecimal.ROUND_HALF_DOWN);
				}
				BigDecimal overDay = new BigDecimal(0);
				if(overTims.compareTo(BigDecimal.ZERO)!=0){
					overDay = (new BigDecimal(365)).divide(overTims,5,BigDecimal.ROUND_HALF_DOWN);
				}
				if(avgData.compareTo(BigDecimal.ZERO)==0){
					overTims = new BigDecimal(365);
					overDay = new BigDecimal(1);
				}
				innerMap.put("startData", startData);
				innerMap.put("endData", endData);
				innerMap.put("outputData", outputData);
				innerMap.put("inputData", inputData);
				innerMap.put("overTims", overTims);
				innerMap.put("overDay", overDay);
				
				returnList.add(innerMap);
				
			}
			
		}
		this.sortByOverTimsDesc(returnList);
		
		return returnList;
	}
	void sortByOverTimsDesc(List<Map<String, Object>> returnList) throws Exception{
		
		Collections.sort(returnList, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
            	BigDecimal overTims1 = UtilObject.getBigDecimalOrZero(o1.get("overTims"));
            	BigDecimal overTims2 = UtilObject.getBigDecimalOrZero(o2.get("overTims"));
                return overTims2.compareTo(overTims1);
            }
        });
		
		
	}

	@Override
	public Map<String, Object> getMatView(String boardId, String corpId, String whId, String date, int type, int matNum,
			String matGroupId,int showType) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Map<String, Object>> matList = this.getWhAndMatGroupView(boardId, corpId, whId, date, type, matGroupId, true);
		
		BigDecimal leftMaxValue = new BigDecimal(0);
		BigDecimal rightMaxValue = new BigDecimal(0);
		List<Map<String, Object>> xAxisList = new ArrayList<Map<String,Object>>();
		int count = 0;
		if(matList!=null&&matList.size()>0){
			for(Map<String, Object> innerMap:matList){
				if(count==matNum){
					break;
				}
				count++;
				Map<String, Object> xAxis = new HashMap<String, Object>();
				
				xAxis.put("xAxisName", UtilObject.getStringOrEmpty(innerMap.get("mat_name")));
				List<Map<String, Object>> yAxisValueList = new ArrayList<Map<String,Object>>();
				if(showType ==EnumTurnOverType.TURN_OVER_DAY.getValue()){
					Map<String, Object> yAxisValueInput = new HashMap<String, Object>();
					yAxisValueInput.put("yAxisValue", UtilObject.getBigDecimalOrZero(innerMap.get("overDay")));
					
					yAxisValueList.add(yAxisValueInput);
					leftMaxValue = UtilObject.getBigDecimalOrZero(innerMap.get("overDay"));
				}else if(showType ==EnumTurnOverType.TURN_OVER_TIMES.getValue()){
					Map<String, Object> yAxisValueInput = new HashMap<String, Object>();
					yAxisValueInput.put("yAxisValue", UtilObject.getBigDecimalOrZero(innerMap.get("overTims")));
					if(count==0){
						leftMaxValue = UtilObject.getBigDecimalOrZero(innerMap.get("overTims"));
					}
					yAxisValueList.add(yAxisValueInput);
				}else if(showType ==EnumTurnOverType.ALL.getValue()){
					Map<String, Object> yAxisValueInput = new HashMap<String, Object>();
					yAxisValueInput.put("yAxisValue", UtilObject.getBigDecimalOrZero(innerMap.get("overDay")));
					
					Map<String, Object> yAxisValueInput1 = new HashMap<String, Object>();
					yAxisValueInput1.put("yAxisValue", UtilObject.getBigDecimalOrZero(innerMap.get("overTims")));
					yAxisValueList.add(yAxisValueInput1);
					yAxisValueList.add(yAxisValueInput);
					if(count==0){
						leftMaxValue = UtilObject.getBigDecimalOrZero(innerMap.get("overTims"));
					}
					rightMaxValue = UtilObject.getBigDecimalOrZero(innerMap.get("overDay"));
				}
				
				
				xAxis.put("yAxisValueList", yAxisValueList);
				
				xAxisList.add(xAxis);
				
			}
			List<Map<String, Object>> chartInfoList = new ArrayList<Map<String,Object>>();
			
			if(showType ==EnumTurnOverType.TURN_OVER_DAY.getValue()){
				Map<String, Object> chartInfoInput = new HashMap<String, Object>();
				chartInfoInput.put("name", "周转天数");
				chartInfoInput.put("type", 1);
				chartInfoInput.put("dependence", 0);
				chartInfoList.add(chartInfoInput);
			}else if(showType ==EnumTurnOverType.TURN_OVER_TIMES.getValue()){
				Map<String, Object> chartInfoInput = new HashMap<String, Object>();
				chartInfoInput.put("name", "周转次数");
				chartInfoInput.put("type", 1);
				chartInfoInput.put("dependence", 0);
				chartInfoList.add(chartInfoInput);
			}else if(showType ==EnumTurnOverType.ALL.getValue()){
				Map<String, Object> chartInfoInput = new HashMap<String, Object>();
				chartInfoInput.put("name", "周转次数");
				chartInfoInput.put("type", 1);
				chartInfoInput.put("dependence", 0);
				chartInfoList.add(chartInfoInput);
				Map<String, Object> chartInfoInput1 = new HashMap<String, Object>();
				chartInfoInput1.put("name", "周转天数");
				chartInfoInput1.put("type", 1);
				chartInfoInput1.put("dependence", 1);
				chartInfoList.add(chartInfoInput1);
			}
			
			
			if(showType ==EnumTurnOverType.ALL.getValue()){
				returnMap.put("leftAxisMaxValue", leftMaxValue);
				returnMap.put("rightAxisMaxValue", rightMaxValue);
			}else{
				returnMap.put("leftAxisMaxValue", leftMaxValue);
				returnMap.put("rightAxisMaxValue", "");
			}
			
			String leftUnitName = "";
			String rightUnitName = "";
			if(showType ==EnumTurnOverType.TURN_OVER_DAY.getValue()){
				leftUnitName = "天";
			}else if(showType ==EnumTurnOverType.TURN_OVER_TIMES.getValue()){
				leftUnitName = "次";
			}else if(showType ==EnumTurnOverType.ALL.getValue()){
				leftUnitName = "次";
				rightUnitName = "天";
			}
			
			
			returnMap.put("leftAxisUnit", leftUnitName);
			returnMap.put("rightAxisUnit", rightUnitName);
			returnMap.put("title", "平均存货");
			returnMap.put("chartInfoList", chartInfoList);
			returnMap.put("xAxisList", xAxisList);
			List<Map<String, Object>> yLimitLine = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> xLimitLine = new ArrayList<Map<String,Object>>();
			returnMap.put("xLimitLine", xLimitLine);
			returnMap.put("yLimitLine", yLimitLine);
			
		}else{
			throw new WMSException("缺少数据");
		}
		
		return returnMap;
	}
	

}
