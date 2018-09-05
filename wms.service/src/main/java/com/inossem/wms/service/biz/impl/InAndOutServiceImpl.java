package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.inossem.wms.constant.Const;
import com.inossem.wms.dao.biz.BizStockInputItemMapper;
import com.inossem.wms.dao.biz.BizStockOutputItemMapper;
import com.inossem.wms.dao.stock.StockBatchBeginMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.enums.EnumDataType;
import com.inossem.wms.service.biz.IInAndOutService;
import com.inossem.wms.util.UtilDateTime;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilString;
import com.inossem.wms.util.UtilTimestamp;
import com.lmax.disruptor.util.Util;

@Service("inAndOutService")
@Transactional
public class InAndOutServiceImpl implements IInAndOutService {

	@Autowired
	private BizStockInputItemMapper bizStockInputItemMapper;
	@Autowired
	private BizStockOutputItemMapper bizStockOutputItemMapper;
	
	@Autowired
	private StockBatchBeginMapper stockBatchBeginDao;

	@Override
	public Map<String, Object> getOverview(String boardId, String corpId, String whId, int type ,String date) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", boardId);
		map.put("corpId", corpId);
		map.put("whId", whId);
		boolean currentMonth = true;
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		

		
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
				Date nextDay =  UtilDateTime.getDate(new Date(), 1);
				first = UtilString.getShortStringForDate(nextDay);
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
			
		}else{
			throw new WMSException("请选择时间");
		}
		
		
		
		map.put("type", type);
		map.put("groupMat", true);
		// 本期入库
		List<Map<String, Object>> inputList = bizStockInputItemMapper.getAllInput(map);

		BigDecimal inputData = new BigDecimal(0);

		List<Map<String, Object>> inputMatList = new ArrayList<Map<String, Object>>();

		if (inputList != null) {
			for (Map<String, Object> innerMap : inputList) {
				BigDecimal addB = new BigDecimal(0);
				if (type == EnumDataType.MONEY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("inputAmount"));
				} else if (type == EnumDataType.QTY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
				}
				inputData = inputData.add(addB);
				if (inputMatList.size() < 3&&addB.compareTo(new BigDecimal(0))==1) {
					Map<String, Object> matObj = new HashMap<String, Object>();
					matObj.put("name", UtilObject.getStringOrEmpty(innerMap.get("mat_name")));
					matObj.put("value", addB);
					inputMatList.add(matObj);
				}
			}
		}
		BigDecimal inputMatThreePercent = new BigDecimal(0);
		for (Map<String, Object> innerMap : inputMatList) {
			BigDecimal value = UtilObject.getBigDecimalOrZero(innerMap.get("value"));
			BigDecimal percent = value.divide(inputData, 5, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100));
			inputMatThreePercent = inputMatThreePercent.add(percent);
			innerMap.put("value", percent);
		}
		Map<String, Object> matObjInput = new HashMap<String, Object>();
		matObjInput.put("name", "其他");
		matObjInput.put("value", new BigDecimal(100).subtract(inputMatThreePercent));
		inputMatList.add(matObjInput);

		// 本期入库
		List<Map<String, Object>> outputList = bizStockOutputItemMapper.getAllOutput(map);

		BigDecimal outputData = new BigDecimal(0);

		List<Map<String, Object>> outputMatList = new ArrayList<Map<String, Object>>();

		if (outputList != null) {
			for (Map<String, Object> innerMap : outputList) {
				BigDecimal addB = new BigDecimal(0);
				if (type == EnumDataType.MONEY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("outputAmount"));
				} else if (type == EnumDataType.QTY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
				}
				outputData = outputData.add(addB);
				if (outputMatList.size() < 3&&addB.compareTo(new BigDecimal(0))==1) {
					Map<String, Object> matObj = new HashMap<String, Object>();
					matObj.put("name", UtilObject.getStringOrEmpty(innerMap.get("mat_name")));
					matObj.put("value", addB);
					outputMatList.add(matObj);
				}
			}
		}
		BigDecimal outputMatThreePercent = new BigDecimal(0);
		for (Map<String, Object> innerMap : outputMatList) {
			BigDecimal value = UtilObject.getBigDecimalOrZero(innerMap.get("value"));
			if(value.compareTo(BigDecimal.ZERO) == 0 ||
					inputData.compareTo(BigDecimal.ZERO) == 0 ) {
				continue;
			}
			BigDecimal percent = value.divide(inputData, 5, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100));
			outputMatThreePercent = outputMatThreePercent.add(percent);
			innerMap.put("value", percent);
		}
		Map<String, Object> matObjOut = new HashMap<String, Object>();
		matObjOut.put("name", "其他");
		matObjOut.put("value", new BigDecimal(100).subtract(outputMatThreePercent));
		outputMatList.add(matObjOut);
		map.remove("groupMat");

		Calendar calendar = Calendar.getInstance();
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		int mounth = currCal.get(Calendar.MONTH);
		if (mounth == 0) {
			mounth++;
		}
		calendar.clear();
		calendar.set(Calendar.YEAR, currentYear);
		Date currYearFirst = calendar.getTime();

		map.put("timeFr", format.format(currYearFirst));
		List<Map<String, Object>> allInput = bizStockInputItemMapper.getAllInput(map);
		List<Map<String, Object>> alloutput = bizStockOutputItemMapper.getAllOutput(map);

		BigDecimal allInputData = new BigDecimal(0);
		BigDecimal allOutputData = new BigDecimal(0);
		if (allInput != null) {
			for (Map<String, Object> innerMap : allInput) {
				BigDecimal addB = new BigDecimal(0);
				if (type == EnumDataType.MONEY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("inputAmount"));
				} else if (type == EnumDataType.QTY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
				}

				allInputData = allInputData.add(addB);
			}
		}
		if (alloutput != null) {
			for (Map<String, Object> innerMap : alloutput) {
				BigDecimal addB = new BigDecimal(0);
				if (type == EnumDataType.MONEY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("outputAmount"));
				} else if (type == EnumDataType.QTY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
				}

				allOutputData = allOutputData.add(addB);
			}
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();

		BigDecimal avgInput = allInputData.divide(new BigDecimal(mounth), 5, BigDecimal.ROUND_HALF_DOWN)
				;
		BigDecimal avgOutput = allOutputData.divide(new BigDecimal(mounth), 5, BigDecimal.ROUND_HALF_DOWN)
				;
		String unitName = "";
		if (type == EnumDataType.MONEY.getValue()) {
			unitName = "万元";
		} else if (type == EnumDataType.QTY.getValue()) {
			unitName = "KG";
		}
		returnMap.put("unitName", unitName);
		returnMap.put("inputData", inputData);
		returnMap.put("outputData", outputData);
		returnMap.put("avgInput", avgInput);
		returnMap.put("avgOutput", avgOutput);
		returnMap.put("inputMatList", inputMatList);
		returnMap.put("outputMatList", outputMatList);
		return returnMap;
	}

	@Override
	public Map<String, Object> getInfo(String boardId, String corpId, String whId, int type, Integer mounth,String date)
			throws Exception {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", boardId);
		map.put("whId", whId);
		map.put("corpId", corpId);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM");
		List<Map<String, Object>> xAxisList = new ArrayList<Map<String,Object>>();
		map.put("type", type);
		BigDecimal maxValue = new BigDecimal(0);
		BigDecimal allInput = new BigDecimal(0);
		BigDecimal allOutput = new BigDecimal(0);
		int  avg = 0;
		
		String dateAry[] = date.split("-");
		int year = UtilObject.getIntOrZero(dateAry[0]);
		int mounthNum = UtilObject.getIntOrZero(dateAry[1]);
		Date currentDate = new Date();
		String currentDay = UtilString.getShortStringForDate(currentDate);
		String dateAryCurrent[] = currentDay.split("-");
		int currentMonthNum = UtilObject.getIntOrZero(dateAryCurrent[1]);
		
		
		if(mounthNum>currentMonthNum){
			throw new WMSException("请选择正确月份");
		}
		
		for(int i=0;i<mounth;i++){
			
			
			
			
			Calendar cal_1 = Calendar.getInstance();// 获取当前日期
			cal_1.set(year, mounthNum, 1);
			cal_1.add(Calendar.MONTH, -(mounth-i));
			cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			Date firstDate = cal_1.getTime();
			String firstDay = format.format(firstDate);

			Calendar c = Calendar.getInstance();
			c.set(year, mounthNum, 1);
			c.add(Calendar.MONTH, -(mounth-i)+1);
			c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			Date endDate = c.getTime();
			String first = format.format(endDate);
			if(firstDate.compareTo(Const.BEGIN_DATE)<0){
				continue;
			}
			if(endDate.compareTo(currentDate)==1){
				first = format.format(UtilDateTime.getDate(currentDate, 1));
			}
			avg ++ ;
			map.put("timeFr", firstDay);

			map.put("timeTo", first);
			
			List<Map<String, Object>> inputData = bizStockInputItemMapper.getAllInput(map);
			List<Map<String, Object>> outputData = bizStockOutputItemMapper.getAllOutput(map);
			BigDecimal inputAmount = new BigDecimal(0);
			BigDecimal outputAmount = new BigDecimal(0);
			if(inputData!=null){
				for(Map<String, Object> innerMap:inputData){
					BigDecimal addB = new BigDecimal(0);
					if (type == EnumDataType.MONEY.getValue()) {
						addB = UtilObject.getBigDecimalOrZero(innerMap.get("inputAmount"));
					} else if (type == EnumDataType.QTY.getValue()) {
						addB = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
					}
					innerMap.put("yAxisValue", addB);
					inputAmount = inputAmount.add(addB);
				}
			}
			if(maxValue.compareTo(inputAmount)<0){
				maxValue = inputAmount;
			}
			if(outputData!=null){
				for(Map<String, Object> innerMap:outputData){
					BigDecimal addB = new BigDecimal(0);
					if (type == EnumDataType.MONEY.getValue()) {
						addB = UtilObject.getBigDecimalOrZero(innerMap.get("outputAmount"));
					} else if (type == EnumDataType.QTY.getValue()) {
						addB = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
					}
					innerMap.put("yAxisValue", addB);
					outputAmount = outputAmount.add(addB);
				}
			}
			if(maxValue.compareTo(outputAmount)<0){
				maxValue = outputAmount;
			}
			Map<String, Object> xAxis = new HashMap<String, Object>();
			
			xAxis.put("xAxisName", format2.format(firstDate));
			List<Map<String, Object>> yAxisValueList = new ArrayList<Map<String,Object>>();
			Map<String, Object> yAxisValueInput = new HashMap<String, Object>();
			Map<String, Object> yAxisValueOutput = new HashMap<String, Object>();
			yAxisValueInput.put("yAxisValue", inputAmount);
			yAxisValueOutput.put("yAxisValue", outputAmount);
			yAxisValueList.add(yAxisValueInput);
			yAxisValueList.add(yAxisValueOutput);
			xAxis.put("yAxisValueList", yAxisValueList);
			
			xAxisList.add(xAxis);
			allInput = allInput.add(inputAmount);
			allOutput = allOutput.add(outputAmount);
		}
		if(avg == 0){
			throw new WMSException("暂无数据");
		}
		BigDecimal avgInput = allInput.divide(new BigDecimal(avg),5,BigDecimal.ROUND_HALF_DOWN);
		BigDecimal avgOutput = allOutput.divide(new BigDecimal(avg),5,BigDecimal.ROUND_HALF_DOWN);
		List<Map<String, Object>> yLimitLine = new ArrayList<Map<String,Object>>();
		Map<String, Object> yLimitInput = new HashMap<String, Object>();
		List<Map<String, Object>> xLimitLine = new ArrayList<Map<String,Object>>();
		yLimitInput.put("title", "平均入库");
		yLimitInput.put("yAxisValue", avgInput);
		yLimitInput.put("dependence", "0");
		Map<String, Object> yLimitOutput = new HashMap<String, Object>();
		yLimitOutput.put("title", "平均出库");
		yLimitOutput.put("yAxisValue", avgOutput);
		yLimitOutput.put("dependence", "0");
		yLimitLine.add(yLimitInput);
		yLimitLine.add(yLimitOutput);
		
		List<Map<String, Object>> chartInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> chartInfoInput = new HashMap<String, Object>();
		chartInfoInput.put("name", "入库");
		chartInfoInput.put("type", 1);
		chartInfoInput.put("dependence", 0);
		Map<String, Object> chartInfoOutput = new HashMap<String, Object>();
		chartInfoOutput.put("name", "出库");
		chartInfoOutput.put("type", 1);
		chartInfoOutput.put("dependence", 0);
		chartInfoList.add(chartInfoInput);
		chartInfoList.add(chartInfoOutput);
		
		
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
		returnMap.put("title", "出入库统计分析报表");
		returnMap.put("chartInfoList", chartInfoList);
		returnMap.put("xAxisList", xAxisList);
		returnMap.put("xLimitLine", xLimitLine);
		returnMap.put("yLimitLine", yLimitLine);
		
		
		return returnMap;
	}

	@Override
	public Map<String, Object> getMatDetial(String boardId, String corpId, String whId, int type,String date)
			throws Exception {


		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", boardId);
		map.put("corpId", corpId);
		map.put("whId", whId);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		String dateAry[] = date.split("-");
		int year = UtilObject.getIntOrZero(dateAry[0]);
		int mounth = UtilObject.getIntOrZero(dateAry[1]);
		
		Date currentDate = new Date();
		String currentDay = UtilString.getShortStringForDate(currentDate);
		String dateAryCurrent[] = currentDay.split("-");
		int currentMonthNum = UtilObject.getIntOrZero(dateAryCurrent[1]);
		
		
		if(mounth>currentMonthNum){
			throw new WMSException("请选择正确月份");
		}
		
		
		cal_1.set(year, mounth -1, 1);
		
		String firstDay = format.format(cal_1.getTime());

		
		cal_1.add(Calendar.MONTH, 1);
		cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		
		
		String first = format.format(cal_1.getTime());

		map.put("timeFr", firstDay);

		map.put("timeTo", first);
		map.put("type", type);
		map.put("groupMat", true);
		// 本期入库
		List<Map<String, Object>> inputList = bizStockInputItemMapper.getAllInput(map);

		BigDecimal inputData = new BigDecimal(0);

		List<Map<String, Object>> inputMatList = new ArrayList<Map<String, Object>>();
		BigDecimal inputMat = new BigDecimal(0);
		if (inputList != null) {
			for (Map<String, Object> innerMap : inputList) {
				BigDecimal addB = new BigDecimal(0);
				if (type == EnumDataType.MONEY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("inputAmount"));
				} else if (type == EnumDataType.QTY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
				}
				inputData = inputData.add(addB);
				if (inputMatList.size() < 9 && addB.compareTo(new BigDecimal(0))==1) {
					Map<String, Object> matObj = new HashMap<String, Object>();
					matObj.put("name", UtilObject.getStringOrEmpty(innerMap.get("mat_name")));
					matObj.put("value", addB);
					inputMat = inputMat.add(addB);
					inputMatList.add(matObj);
				}
			}
		}
		
		
		Map<String, Object> matObjInput = new HashMap<String, Object>();
		matObjInput.put("name", "其他");
		matObjInput.put("value", inputData.subtract(inputMat));
		if(inputData.compareTo(inputMat)>0){
			inputMatList.add(matObjInput);
		}
		

		// 本期入库
		List<Map<String, Object>> outputList = bizStockOutputItemMapper.getAllOutput(map);

		BigDecimal outputData = new BigDecimal(0);

		List<Map<String, Object>> outputMatList = new ArrayList<Map<String, Object>>();
		BigDecimal outputMat = new BigDecimal(0);
		if (outputList != null) {
			for (Map<String, Object> innerMap : outputList) {
				BigDecimal addB = new BigDecimal(0);
				if (type == EnumDataType.MONEY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("outputAmount"));
				} else if (type == EnumDataType.QTY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
				}
				outputData = outputData.add(addB);
				if (outputMatList.size() < 9&&addB.compareTo(new BigDecimal(0))==1) {
					Map<String, Object> matObj = new HashMap<String, Object>();
					matObj.put("name", UtilObject.getStringOrEmpty(innerMap.get("mat_name")));
					matObj.put("value", addB);
					outputMat = outputMat.add(addB);
					outputMatList.add(matObj);
				}
			}
		}
		
		
		Map<String, Object> matObjOut = new HashMap<String, Object>();
		matObjOut.put("name", "其他");
		matObjOut.put("value", outputData.subtract(outputMat));
		if(outputData.compareTo(outputMat)>0){
			outputMatList.add(matObjOut);
		}
		

		Map<String, Object> returnMap = new HashMap<String, Object>();

		returnMap.put("inputMatList", inputMatList);
		returnMap.put("outputMatList", outputMatList);
		return returnMap;
	
		
	}

	@Override
	public Map<String, Object> getWhDetial(String boardId, String corpId, String whId, int type, String date)
			throws Exception {


		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", boardId);
		map.put("corpId", corpId);
		map.put("whId", whId);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		String dateAry[] = date.split("-");
		int year = UtilObject.getIntOrZero(dateAry[0]);
		int mounth = UtilObject.getIntOrZero(dateAry[1]);
		
		boolean currentMonth = true;
		
		// 选择月份
		
		String currentDay = UtilString.getShortStringForDate(new Date());
		String dateAryCurrent[] = currentDay.split("-");
		int currentMonthNum = UtilObject.getIntOrZero(dateAryCurrent[1]);
		if(mounth != currentMonthNum){
			currentMonth = false;
		}
		
		
		cal_1.set(year, mounth -1, 1);
		
		String firstDay = format.format(cal_1.getTime());

		
		
		
		cal_1.add(Calendar.MONTH, 1);
		cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String first = format.format(cal_1.getTime());

		map.put("timeFr", firstDay);

		map.put("timeTo", first);
		map.put("type", type);
		Map<Integer, Map<String, Object>> whMap = new HashMap<Integer, Map<String,Object>>();
		// 本期入库
		List<Map<String, Object>> inputList = bizStockInputItemMapper.getAllInput(map);

		BigDecimal inputData = new BigDecimal(0);

		if (inputList != null) {
			for (Map<String, Object> innerMap : inputList) {
				BigDecimal addB = new BigDecimal(0);
				Integer whIdKey = UtilObject.getIntegerOrNull(innerMap.get("wh_id"));
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("whId", whIdKey);
				param.put("createTime", first);
				Map<String, Object> endingBalance = null;
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
				BigDecimal endingBalanceQty = new BigDecimal(0);
				if (type == EnumDataType.MONEY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("inputAmount"));
					endingBalanceQty = UtilObject.getBigDecimalOrZero(endingBalance.get("qtyAmount"));
				} else if (type == EnumDataType.QTY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
					endingBalanceQty = UtilObject.getBigDecimalOrZero(endingBalance.get("qty"));
				}
				
				inputData = inputData.add(addB);
				innerMap.put("inputQty", addB);
				innerMap.put("outputQty", "0");
				innerMap.put("endingBalanceQty", endingBalanceQty);
				whMap.put(whIdKey, innerMap);
			}
		}
		
		
		

		// 本期入库
		List<Map<String, Object>> outputList = bizStockOutputItemMapper.getAllOutput(map);

		BigDecimal outputData = new BigDecimal(0);

		if (outputList != null) {
			for (Map<String, Object> innerMap : outputList) {
				BigDecimal addB = new BigDecimal(0);
				Integer whIdKey = UtilObject.getIntegerOrNull(innerMap.get("wh_id"));
				if (type == EnumDataType.MONEY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("outputAmount"));
				} else if (type == EnumDataType.QTY.getValue()) {
					addB = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
				}
				outputData = outputData.add(addB);
				
				if(whMap.containsKey(whIdKey)){
					Map<String , Object> inputMap = whMap.get(whIdKey);
					inputMap.put("outputQty", addB);
					whMap.put(whIdKey, inputMap);
				}else{
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("whId", whIdKey);
					param.put("createTime", first);
					Map<String, Object> endingBalance = null;
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
					BigDecimal endingBalanceQty = new BigDecimal(0);
					if (type == EnumDataType.MONEY.getValue()) {
						endingBalanceQty = UtilObject.getBigDecimalOrZero(endingBalance.get("qtyAmount"));
					} else if (type == EnumDataType.QTY.getValue()) {
						endingBalanceQty = UtilObject.getBigDecimalOrZero(endingBalance.get("qty"));
					}
					innerMap.put("inputQty", "0");
					innerMap.put("outputQty", addB);
					innerMap.put("endingBalanceQty", endingBalanceQty);
					whMap.put(whIdKey, innerMap);
				}
				
				
			}
		}
		
		
		
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		for(Integer key : whMap.keySet()){
			
			Map<String , Object> innerMap = whMap.get(key);
			BigDecimal outputQty = UtilObject.getBigDecimalOrZero(innerMap.get("outputQty"));
			BigDecimal inputQty = UtilObject.getBigDecimalOrZero(innerMap.get("inputQty"));
			BigDecimal endingBalanceQty = UtilObject.getBigDecimalOrZero(innerMap.get("endingBalanceQty"));
			BigDecimal startBalanceQty = endingBalanceQty.add(outputQty).subtract(inputQty);
			innerMap.put("startBalanceQty", startBalanceQty);
			returnList.add(innerMap);
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();

	//	returnMap.put("inputMatList", inputList);
	//	returnMap.put("outputMatList", outputList);
		returnMap.put("returnList", returnList);
		return returnMap;
	
		
	}

}
