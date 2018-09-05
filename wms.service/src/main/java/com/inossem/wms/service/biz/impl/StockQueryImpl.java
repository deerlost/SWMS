package com.inossem.wms.service.biz.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.inossem.wms.dao.batch.BatchMaterialMapper;
import com.inossem.wms.dao.biz.BizMaterialDocItemMapper;
import com.inossem.wms.dao.dic.DicBoardMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicUnitMapper;
import com.inossem.wms.dao.dic.DicWarehouseMapper;
import com.inossem.wms.dao.stock.StockBatchMapper;
import com.inossem.wms.dao.stock.StockPositionMapper;
import com.inossem.wms.model.ExcelCellType;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.dic.DicBoard;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.vo.MatCodeVo;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IStockQueryService;
import com.inossem.wms.util.UtilDateTime;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 库存分页查询功能实现
 * 
 * @author 刘宇 2018.02.26
 *
 */
@Transactional
@Service("stockQueryService")
public class StockQueryImpl implements IStockQueryService {
	@Autowired
	private BatchMaterialMapper batchMaterialDao;

	@Autowired
	private StockPositionMapper stockPositionDao;

	@Autowired
	private StockBatchMapper stockBatchDao;

	@Autowired
	private DicUnitMapper unitDao;

	@Autowired
	private BizMaterialDocItemMapper bizMaterialDocItemDao;

	@Autowired
	private IDictionaryService dictionaryService;

	@Autowired
	private DicBoardMapper dicBoardMapper;

	@Autowired
	private DicFactoryMapper dicFactoryMapper;

	@Autowired
	private DicWarehouseMapper dicWarehouseMapper;
	/*
	 * @Override public List<Map<String, Object>>
	 * listStockPositionOnPaging(List<MatCodeVo> utilMatCodes, String matCode,
	 * String matName, String specStockCode, String inputDateBegin, String
	 * inputDateEnd, String positionCodeBegin, String positionCodeEnd, String
	 * specStockName, String areaCodeBegin, String areaCodeEnd, String specStock,
	 * String status, String ftyId, int pageIndex, String locationId, int pageSize,
	 * int total, boolean paging) throws Exception { Map<String, Object> map = new
	 * HashMap<String, Object>(); map.put("utilMatCodes", utilMatCodes);// 物料编码集合
	 * map.put("matCode", matCode);// 物料编码（判断用） map.put("matName", matName);// 物料描述
	 * map.put("specStockCode", specStockCode);// 特殊库存代码 SimpleDateFormat clsFormat
	 * = new SimpleDateFormat("yyyy-MM-dd"); if (StringUtils.hasText(inputDateBegin)
	 * && StringUtils.hasText(inputDateEnd)) { map.put("inputDateBegin",
	 * clsFormat.parse(inputDateBegin));// 入库日期起 map.put("inputDateEnd",
	 * UtilDateTime.getDate(clsFormat.parse(inputDateEnd), 1));// 入库日期止 } else if
	 * (StringUtils.hasText(inputDateBegin) && !StringUtils.hasText(inputDateEnd)) {
	 * map.put("inputDateBegin", clsFormat.parse(inputDateBegin));
	 * map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateBegin),
	 * 1)); } else if (!StringUtils.hasText(inputDateBegin) &&
	 * StringUtils.hasText(inputDateEnd)) { map.put("inputDateBegin",
	 * clsFormat.parse(inputDateEnd)); map.put("inputDateEnd",
	 * UtilDateTime.getDate(clsFormat.parse(inputDateEnd), 1)); }
	 * map.put("positionCodeBegin", positionCodeBegin);// 仓位开始
	 * map.put("positionCodeEnd", positionCodeEnd);// 仓位结束 map.put("specStockName",
	 * specStockName);// 特殊库存描述 map.put("areaCodeBegin", areaCodeBegin);// 储存区开始
	 * map.put("areaCodeEnd", areaCodeEnd);// 储存区结束 map.put("specStock",
	 * specStock);// 特殊库存 map.put("status", status);// 库存状态 map.put("ftyId",
	 * ftyId);// 工厂编号 map.put("locationId", locationId);// 库存地点编号
	 * map.put("pageSize", pageSize); map.put("pageIndex", pageIndex);
	 * map.put("paging", paging);
	 * 
	 * return stockPositionDao.selectStockPositionOnPaging(map); }
	 */
	/*
	 * @Override public List<Map<String, Object>>
	 * listStockBatchOnPaging(List<MatCodeVo> utilMatCodes, String matCode, String
	 * matGroupCodeBegin, String matGroupCodeEnd, String specStockCode, String
	 * inputDateBegin, String inputDateEnd, String matName, String specStockName,
	 * String ftyId, String locationId, String specStock, String status, int
	 * pageIndex, int pageSize, int total, boolean paging,List<Integer> locationIds)
	 * throws Exception {
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>();
	 * map.put("utilMatCodes", utilMatCodes);// 物料编码集合 map.put("matCode",
	 * matCode);// 物料编码（判断用） map.put("matGroupCodeBegin", matGroupCodeBegin);//
	 * 物料组编码始 map.put("matGroupCodeEnd", matGroupCodeEnd);// 物料组编码止1
	 * map.put("specStockCode", specStockCode);// 特殊库存编码
	 * 
	 * SimpleDateFormat clsFormat = new SimpleDateFormat("yyyy-MM-dd");
	 * 
	 * if (StringUtils.hasText(inputDateBegin) && StringUtils.hasText(inputDateEnd))
	 * { map.put("inputDateBegin", clsFormat.parse(inputDateBegin));// 入库日期起
	 * map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateEnd),
	 * 1));// 入库日期止 } else if (StringUtils.hasText(inputDateBegin) &&
	 * !StringUtils.hasText(inputDateEnd)) { map.put("inputDateBegin",
	 * clsFormat.parse(inputDateBegin)); map.put("inputDateEnd",
	 * UtilDateTime.getDate(clsFormat.parse(inputDateBegin), 1)); } else if
	 * (!StringUtils.hasText(inputDateBegin) && StringUtils.hasText(inputDateEnd)) {
	 * map.put("inputDateBegin", clsFormat.parse(inputDateEnd));
	 * map.put("inputDateEnd", UtilDateTime.getDate(clsFormat.parse(inputDateEnd),
	 * 1)); } map.put("matName", matName);// 物料描述 map.put("specStockName",
	 * specStockName);// 特殊库存描述 map.put("ftyId", ftyId);// 工厂id
	 * map.put("locationId", locationId);// 库存地点编码 map.put("specStock",
	 * specStock);// 特殊库存 map.put("status", status);// 库存状态 map.put("pageSize",
	 * pageSize); map.put("pageIndex", pageIndex); map.put("paging", paging);
	 * if(locationIds!=null&&locationIds.size()>0){ map.put("locationIds",
	 * locationIds); } return stockBatchDao.selectStockBatchOnPaging(map); }
	 */
	@Override
	public List<Map<String, Object>> listOutAndInStockOnPaging(String createTimeBegin, String createTimeEnd,
			String createUser, String moveTypeCode, String ftyId, String locationId, String specStockCode,
			String matCode, String matName, String bizTypes, List<MatCodeVo> utilMatCodes, String referReceiptCode,
			int pageIndex, int pageSize, int total, boolean paging) throws Exception {
		String[] manyBizTypes = null; // 业务类型数组
		if (UtilString.isNullOrEmpty(bizTypes) == false) {
			manyBizTypes = bizTypes.split(",");

		}
		Map<String, Object> map = new HashMap<String, Object>();

		SimpleDateFormat clsFormat = new SimpleDateFormat("yyyy-MM-dd");

		if (StringUtils.hasText(createTimeBegin) && StringUtils.hasText(createTimeEnd)) {
			map.put("createTimeBegin", clsFormat.parse(createTimeBegin));// 创建日期起
			map.put("createTimeEnd", UtilDateTime.getDate(clsFormat.parse(createTimeEnd), 1));// 创建日期止
		} else if (StringUtils.hasText(createTimeBegin) && !StringUtils.hasText(createTimeEnd)) {
			map.put("createTimeBegin", clsFormat.parse(createTimeBegin));
			map.put("createTimeEnd", UtilDateTime.getDate(clsFormat.parse(createTimeBegin), 1));
		} else if (!StringUtils.hasText(createTimeBegin) && StringUtils.hasText(createTimeEnd)) {
			map.put("createTimeBegin", clsFormat.parse(createTimeEnd));
			map.put("createTimeEnd", UtilDateTime.getDate(clsFormat.parse(createTimeEnd), 1));
		}
		map.put("createUser", createUser);// 创建人
		map.put("moveTypeCode", moveTypeCode);// 移动类型
		map.put("ftyId", ftyId);// 工厂id
		map.put("locationId", locationId);// 库存地点id
		map.put("specStockCode", specStockCode);// 特殊库存编码
		map.put("matName", matName);// 物料描述
		map.put("manyBizTypes", manyBizTypes); // 业务类型数组
		map.put("matCode", matCode);// 物料编码（判断用）
		map.put("utilMatCodes", utilMatCodes);// 物料编码集合
		map.put("referReceiptCode", referReceiptCode);// 单据编号
		map.put("pageSize", pageSize);
		map.put("pageIndex", pageIndex);
		map.put("paging", paging);
		return bizMaterialDocItemDao.selectOutAndInStockOnPaging(map);
	}

	@Override
	public List<Map<String, Object>> listStockDaysOnPaging(String dateBegin, String dateEnd, String checkDate,
			String ftyId, String locationId, int pageIndex, int pageSize, int total, boolean paging) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("dateBegin", dateBegin);// 积压天数开始
		map.put("dateEnd", dateEnd);// 积压天数结束
		map.put("checkDate", checkDate);// 考核日期
		map.put("ftyId", ftyId);// 工厂id
		map.put("locationId", locationId);// 库存地点id
		map.put("pageSize", pageSize);
		map.put("pageIndex", pageIndex);
		map.put("paging", paging);
		return stockBatchDao.selectStockDaysOnPaging(map);
	}

	@Override
	public List<Map<String, Object>> getNewMaps(JSONObject re, List<Map<String, Object>> stockDaysMaps)
			throws Exception {
		if (re.getString("RETURNVALUE").equals("S")) {
			if (re.get("map") != null) {

				@SuppressWarnings("unchecked")
				Map<String, BigDecimal> map = (Map<String, BigDecimal>) re.get("map");

				for (int i = 0; i < stockDaysMaps.size(); i++) {
					String key = stockDaysMaps.get(i).get("mat_code") + "-" + stockDaysMaps.get(i).get("fty_code");
					String qty = stockDaysMaps.get(i).get("qty") == null ? ""
							: stockDaysMaps.get(i).get("qty").toString();
					BigDecimal verpr = new BigDecimal(0);// 移动平均价
					if ("".equals(map.get(key)) || map.get(key) == null) {
						verpr = new BigDecimal(0);
					} else {
						verpr = map.get(key);
					}

					stockDaysMaps.get(i).put("move_average_sum", verpr);// 移动平均价
					stockDaysMaps.get(i).put("stock_sum", verpr.multiply(new BigDecimal(qty)).doubleValue());// 库存金额
				}
			}
		}
		return stockDaysMaps;
	}

	/**
	 * 查询库存积压图表
	 */
	@Override
	public JSONObject getStockDaysChart(Map<String, Object> param) {
		// 查询库存数据
		List<Map<String, Object>> list = stockBatchDao.getStockDaysList(param);
		// 返回‘工厂名-仓库名’ 对应的积压天数的集合 JSONObject Map<String, Map<String, BigDecimal>>
		return this.formatDataToChart(list); 
	}

	private JSONObject getCharInfo(String name) {
		JSONObject chartInfo = new JSONObject();
		chartInfo.put("name", name);
		chartInfo.put("type", "0");
		chartInfo.put("dependence", "0");
		return chartInfo;
	}

	/**
	 * 格式化图表返回数据
	 * 
	 * @param list
	 * @return
	 */
	private JSONObject formatDataToChart(List<Map<String, Object>> list) {
		// 存‘工厂的名-仓库名’ 字符串
		Map<String, Map<String, BigDecimal>> returnMap = new HashMap<String, Map<String, BigDecimal>>();
		BigDecimal total = new BigDecimal("0");
		BigDecimal wan = new BigDecimal(10000);
		for (int i = 0; i < list.size(); i++) {
			String keyName = list.get(i).get("fty_name") + "-" + list.get(i).get("wh_name")+"-"+list.get(i).get("fty_code");
			BigDecimal amount = UtilObject.getBigDecimalOrZero(list.get(i).get("amount")).divide(wan);
			int days = UtilObject.getIntOrZero(list.get(i).get("days"));
			if (returnMap.containsKey(keyName)) {
				Map<String, BigDecimal> temp = returnMap.get(keyName);
				if (days > 365) {
					BigDecimal amountTotal = UtilObject.getBigDecimalOrZero(temp.get("blue"));
					temp.put("blue", amountTotal.add(amount));

					if (temp.get("blue").compareTo(total) == 1) {
						total = temp.get("blue");
					}

					returnMap.put(keyName, temp);
				} else if (days <= 365 && days > 183) {
					BigDecimal amountTotal = UtilObject.getBigDecimalOrZero(temp.get("green"));
					temp.put("green", amountTotal.add(amount));

					if (temp.get("green").compareTo(total) == 1) {
						total = temp.get("green");
					}

					returnMap.put(keyName, temp);
				} else if (days <= 183 && days > 122) {
					BigDecimal amountTotal = UtilObject.getBigDecimalOrZero(temp.get("purple"));
					temp.put("purple", amountTotal.add(amount));

					if (temp.get("purple").compareTo(total) == 1) {
						total = temp.get("purple");
					}

					returnMap.put(keyName, temp);
				} else if (days <= 122) {
					BigDecimal amountTotal = UtilObject.getBigDecimalOrZero(temp.get("red"));
					temp.put("red", amountTotal.add(amount));

					if (temp.get("red").compareTo(total) == 1) {
						total = temp.get("red");
					}
					returnMap.put(keyName, temp);
				}

			} else {
				Map<String, BigDecimal> temp = new HashMap<String, BigDecimal>();
				temp.put("red", new BigDecimal("0"));
				temp.put("purple", new BigDecimal("0"));
				temp.put("green", new BigDecimal("0"));
				temp.put("blue", new BigDecimal("0"));

				if (days > 365) {
					temp.put("blue", amount);
					if (amount.compareTo(total) == 1) {
						total = amount;
					}

				} else if (days <= 365 && days > 183) {
					temp.put("green", amount);
					if (amount.compareTo(total) == 1) {
						total = amount;
					}
				} else if (days <= 183 && days > 122) {
					temp.put("purple", amount);
					if (amount.compareTo(total) == 1) {
						total = amount;
					}
				} else if (days <= 122) {
					temp.put("red", amount);
					if (amount.compareTo(total) == 1) {
						total = amount;
					}
				}

				returnMap.put(keyName, temp);
			}
		}

		JSONObject returnData = new JSONObject();

		returnData.put("leftAxisMaxValue", total.setScale(2, BigDecimal.ROUND_HALF_UP));
		returnData.put("rightAxisMaxValue", "");
		returnData.put("leftAxisUnit", "万元");
		returnData.put("rightAxisUnit", "");
		returnData.put("title", "库存积压分析");

		JSONArray chartInfoList = new JSONArray();
		chartInfoList.add(this.getCharInfo("1年以上"));
		chartInfoList.add(this.getCharInfo("6个月至1年"));
		chartInfoList.add(this.getCharInfo("4个月至6个月"));
		chartInfoList.add(this.getCharInfo("4个月以内"));
		returnData.put("chartInfoList", chartInfoList);

		JSONArray xAxisList = new JSONArray();
		Iterator<Map.Entry<String, Map<String, BigDecimal>>> it = returnMap.entrySet().iterator();
		while (it.hasNext()) {
			JSONObject xAxis = new JSONObject();
			Map.Entry<String, Map<String, BigDecimal>> entry = it.next();

			String xAxisName = entry.getKey().split("-")[2]+"-"+entry.getKey().split("-")[1];
			String xAxisWerks = entry.getKey().split("-")[0];
			xAxis.put("xAxisName", xAxisName);
			xAxis.put("xAxisWerks", xAxisWerks);

			Map<String, BigDecimal> tempMap = entry.getValue();

			JSONArray yAxisValueList = new JSONArray();
			JSONObject yAxisValue = new JSONObject();
			yAxisValue.put("yAxisValue", tempMap.get("blue").setScale(2, BigDecimal.ROUND_HALF_UP));
			yAxisValueList.add(yAxisValue);
			yAxisValue.put("yAxisValue", tempMap.get("green").setScale(2, BigDecimal.ROUND_HALF_UP));
			yAxisValueList.add(yAxisValue);
			yAxisValue.put("yAxisValue", tempMap.get("purple").setScale(2, BigDecimal.ROUND_HALF_UP));
			yAxisValueList.add(yAxisValue);
			yAxisValue.put("yAxisValue", tempMap.get("red").setScale(2, BigDecimal.ROUND_HALF_UP));
			yAxisValueList.add(yAxisValue);
			xAxis.put("yAxisValueList", yAxisValueList);

			xAxisList.add(xAxis);

		}
		returnData.put("xAxisList", xAxisList);
		JSONArray jsonArr = new JSONArray();
		returnData.put("xLimitLine", jsonArr);
		returnData.put("yLimitLine", jsonArr);

		return returnData;
	}

	@Override
	public List<Map<String, Object>> getStockDaysList(Map<String, Object> param) {
		// 查询库存数据
		List<Map<String, Object>> list = stockBatchDao.getStockDaysList(param);
		return list;
	}

	public List<String> getDataChangTitle() {
		List<String> list = new ArrayList<String>();

		list.add("matCode");
		list.add("batch");
		return list;
	}

	public Map<String, String> getTitleMapping1() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("仓库号", "whCode");
		map.put("仓储区", "areaCode");
		map.put("仓位", "positionCode");
		map.put("物料编号", "matCode");
		map.put("工厂", "ftyCode");
		map.put("库存地点", "locationCode");
		map.put("批号", "batch");
		map.put("冻结标识: 当前入库", "SKZSE");
		map.put("冻结标识: 当前库存出库", "SKZSA");
		map.put("冻结原因", "SPGRU");
		map.put("作业单编号", "BTANR");
		map.put("作业单编项目", "BTAPS");
		map.put("上架日期", "EDATU");
		map.put("收货日期", "takeDeliveryDate");
		map.put("计量单位", "unitCode");
		map.put("数量", "qty");
		map.put("物料重量", "MGEWI");
		map.put("重量单位", "unitWeightCode");
		map.put("托盘编号", "LENUM");
		map.put("质保到期日", "VFDAT");
		return map;
	}

	public Map<String, String> getTitleMapping2() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("物料编号", "matCode");
		map.put("工厂", "ftyCode");
		map.put("库存地点", "locationCode");
		map.put("批号", "batch");
		map.put("创建日期", "createTime");
		map.put("创建人", "createUser");
		map.put("非限制库存", "unRestricted");
		map.put("在途库存", "onTheWay");
		map.put("质量库存", "qualityInspection");
		map.put("冻结库存", "freeze");
		return map;
	}

	public Map<String, String> getTitleMapping3() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("物料编号", "matCode");
		map.put("工厂", "ftyCode");
		map.put("库存地点", "locationCode");
		map.put("批号", "batch");
		map.put("删除标记", "LVORM");
		map.put("创建日期", "createTime");
		map.put("创建人", "createUser");
		map.put("供应商代码", "supplierCode");
		map.put("供应商名称", "supplierName");
		map.put("特殊库存类型", "specStock");
		map.put("特殊库存代码", "specStockCode");
		map.put("特殊库存描述", "specStockName");
		map.put("需求部门", "demandDept");
		map.put("采购订单号", "purchaseOrderCode");

		return map;
	}

	public Map<String, String> getTitleMapping4() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("物料编号", "matCode");
		map.put("工厂", "ftyCode");
		map.put("批号", "batch");
		map.put("特性分类", "batchSpecCode");
		map.put("特性值", "batchSpecValue");
		return map;
	}

	public Map<String, List<String>> getMultValidation1() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = stockPositionDao.selectStockPositionForImport();
		list.add(UtilExcel.VALIDATION_DATA_NOTEXISTS);
		map.put("whCode-areaCode-positionCode", list);

		return map;
	}

	public Map<String, List<String>> getMultValidation2() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = stockPositionDao.selectStockBatchForImport();
		list.add(UtilExcel.VALIDATION_DATA_NOTEXISTS);
		map.put("ftyCode-matCode-batch", list);

		return map;
	}

	public Map<String, List<String>> getMultValidation3() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = stockPositionDao.selectBatchMaterialForImport();
		list.add(UtilExcel.VALIDATION_DATA_NOTEXISTS);
		map.put("ftyCode-matCode-batch", list);

		return map;
	}

	public Map<String, List<String>> getMultValidation4() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = stockPositionDao.selectBatchMaterialSpecForImport();
		list.add(UtilExcel.VALIDATION_DATA_NOTEXISTS);
		map.put("ftyCode-matCode-batch", list);

		return map;
	}

	public Map<String, ExcelCellType> getCheckData() {

		Map<String, ExcelCellType> checkData = new HashMap<String, ExcelCellType>();

		checkData.put("whCode", new ExcelCellType(true));
		checkData.put("areaCode", new ExcelCellType(true));
		checkData.put("positionCode", new ExcelCellType(true));
		checkData.put("matCode", new ExcelCellType(true));

		checkData.put("ftyCode", new ExcelCellType(true, 4, UtilExcel.CELL_TYPE_NUM));
		checkData.put("locationCode", new ExcelCellType(true, 4, UtilExcel.CELL_TYPE_NUM));

		checkData.put("batch", new ExcelCellType(true, 0, UtilExcel.CELL_TYPE_NUM));

		checkData.put("takeDeliveryDate", new ExcelCellType(true, 0, UtilExcel.CELL_TYPE_DATE));
		checkData.put("createTime", new ExcelCellType(true, 0, UtilExcel.CELL_TYPE_DATE));

		Map<String, List<String>> map1 = new HashMap<String, List<String>>();
		List<String> list1 = unitDao.selectAllCode();
		map1.put(UtilExcel.VALIDATION_DATA_EXISTS, list1);
		checkData.put("unitCode", new ExcelCellType(true, map1));

		checkData.put("qty", new ExcelCellType(true, 0, UtilExcel.CELL_TYPE_DOUBLE));

		checkData.put("purchaseOrderCode", new ExcelCellType(true, 0, UtilExcel.CELL_TYPE_NUM));

		Map<String, List<String>> map2 = new HashMap<String, List<String>>();
		List<String> list2 = stockPositionDao.selectBatchSpecForImport();
		map2.put(UtilExcel.VALIDATION_DATA_EXISTS, list2);
		checkData.put("batchSpecCode", new ExcelCellType(true, map2));
		checkData.put("batchSpecValue", new ExcelCellType(true));

		return checkData;
	}

	@Override
	public int getExcelData(String realPath, String fileName, String folder) throws Exception {

		// 文件全路径
		String path = realPath + folder + File.separator;

		List<Map<String, String>> titleList = new ArrayList<Map<String, String>>();

		titleList.add(getTitleMapping1());
		titleList.add(getTitleMapping2());
		titleList.add(getTitleMapping3());
		titleList.add(getTitleMapping4());

		List<Map<String, List<String>>> validationlist = new ArrayList<Map<String, List<String>>>();
		validationlist.add(getMultValidation1());
		validationlist.add(getMultValidation2());
		validationlist.add(getMultValidation3());
		validationlist.add(getMultValidation4());

		Map<String, List<Map<String, Object>>> map = UtilExcel.getMultExcelDataList(path + fileName,
				getDataChangTitle(), titleList, getCheckData(), 4, validationlist);

		List<Map<String, Object>> stockPositionList = map.get("0");
		List<Map<String, Object>> stockBatchList = map.get("1");
		List<Map<String, Object>> batchMaterialList = map.get("2");
		List<Map<String, Object>> batchMaterialSpecList = map.get("3");

		Map<String, Map<String, Object>> batchMaterialMap = new HashMap<String, Map<String, Object>>();
		List<String> matCodeList = new ArrayList<String>();
		for (int i = 0; i < stockPositionList.size(); i++) {
			String matCode = UtilString.getStringOrEmptyForObject(stockPositionList.get(i).get("matCode"));
			matCodeList.add(matCode);
		}
		Map<String, Integer> matCodeIdMap = dictionaryService.getMatMapByCodeList(matCodeList);

		for (int i = 0; i < batchMaterialList.size(); i++) {
			String key = "" + batchMaterialList.get(i).get("ftyCode") + batchMaterialList.get(i).get("matCode")
					+ batchMaterialList.get(i).get("batch");
			batchMaterialMap.put(key, batchMaterialList.get(i));

			String ftyCode = UtilString.getStringOrEmptyForObject(stockPositionList.get(i).get("ftyCode"));
			Integer ftyId = dictionaryService.getFtyIdByFtyCode(ftyCode);
			String matCode = UtilString.getStringOrEmptyForObject(stockPositionList.get(i).get("matCode"));
			Integer matId = matCodeIdMap.get(matCode);
			batchMaterialList.get(i).put("ftyId", ftyId);
			batchMaterialList.get(i).put("matId", matId);

		}

		for (int i = 0; i < stockPositionList.size(); i++) {
			String key = "" + stockPositionList.get(i).get("ftyCode") + stockPositionList.get(i).get("matCode")
					+ stockPositionList.get(i).get("batch");
			stockPositionList.get(i).put("specStock", batchMaterialMap.get(key).get("specStock"));
			stockPositionList.get(i).put("specStockCode", batchMaterialMap.get(key).get("specStockCode"));
			stockPositionList.get(i).put("specStockName", batchMaterialMap.get(key).get("specStockName"));
			String ftyCode = UtilString.getStringOrEmptyForObject(stockPositionList.get(i).get("ftyCode"));
			Integer ftyId = dictionaryService.getFtyIdByFtyCode(ftyCode);
			String locationCode = UtilString.getStringOrEmptyForObject(stockPositionList.get(i).get("locationCode"));
			Integer locationId = dictionaryService.getLocIdByFtyCodeAndLocCode(ftyCode, locationCode);
			String whCode = UtilString.getStringOrEmptyForObject(stockPositionList.get(i).get("whCode"));
			Integer whId = dictionaryService.getWhIdByWhCode(whCode);
			String areaCode = UtilString.getStringOrEmptyForObject(stockPositionList.get(i).get("areaCode"));
			Integer areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);
			String positionCode = UtilString.getStringOrEmptyForObject(stockPositionList.get(i).get("positionCode"));
			Integer positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode, areaCode,
					positionCode);
			String matCode = UtilString.getStringOrEmptyForObject(stockPositionList.get(i).get("matCode"));
			Integer matId = matCodeIdMap.get(matCode);
			String unitCode = UtilString.getStringOrEmptyForObject(stockPositionList.get(i).get("unitCode"));
			Integer unitId = dictionaryService.getUnitIdByUnitCode(unitCode);
			String unitWeightCode = UtilString
					.getStringOrEmptyForObject(stockPositionList.get(i).get("unitWeightCode"));
			Integer unitWeightId = dictionaryService.getUnitIdByUnitCode(unitWeightCode);
			stockPositionList.get(i).put("whId", whId);
			stockPositionList.get(i).put("ftyId", ftyId);
			stockPositionList.get(i).put("locationId", locationId);
			stockPositionList.get(i).put("areaId", areaId);
			stockPositionList.get(i).put("positionId", positionId);
			stockPositionList.get(i).put("matId", matId);
			stockPositionList.get(i).put("unitId", unitId);
			stockPositionList.get(i).put("unitWeight", unitWeightId);
			stockPositionList.get(i).put("palletId", 0);
			stockPositionList.get(i).put("packageId", 0);
			stockPositionList.get(i).put("inputDate", batchMaterialMap.get(key).get("createTime"));
		}

		for (int i = 0; i < stockBatchList.size(); i++) {
			String key = "" + stockBatchList.get(i).get("ftyCode") + stockBatchList.get(i).get("matCode")
					+ stockBatchList.get(i).get("batch");
			stockBatchList.get(i).put("specStock", batchMaterialMap.get(key).get("specStock"));
			stockBatchList.get(i).put("specStockCode", batchMaterialMap.get(key).get("specStockCode"));
			stockBatchList.get(i).put("specStockName", batchMaterialMap.get(key).get("specStockName"));
			String ftyCode = UtilString.getStringOrEmptyForObject(stockBatchList.get(i).get("ftyCode"));
			String locationCode = UtilString.getStringOrEmptyForObject(stockBatchList.get(i).get("locationCode"));
			Integer locationId = dictionaryService.getLocIdByFtyCodeAndLocCode(ftyCode, locationCode);
			String matCode = UtilString.getStringOrEmptyForObject(stockBatchList.get(i).get("matCode"));
			Integer matId = matCodeIdMap.get(matCode);
			stockBatchList.get(i).put("locationId", locationId);
			stockBatchList.get(i).put("matId", matId);
			String unRestricted = UtilObject.getStringOrEmpty(stockBatchList.get(i).get("unRestricted"));
			String onTheWay = UtilObject.getStringOrEmpty(stockBatchList.get(i).get("onTheWay"));
			String qualityInspection = UtilObject.getStringOrEmpty(stockBatchList.get(i).get("qualityInspection"));
			String freeze = UtilObject.getStringOrEmpty(stockBatchList.get(i).get("freeze"));
			if (StringUtils.hasText(unRestricted)) {
				stockBatchList.get(i).put("qty", UtilObject.getBigDecimalOrZero(unRestricted));
				stockBatchList.get(i).put("status", 1);
			} else if (StringUtils.hasText(onTheWay)) {
				stockBatchList.get(i).put("qty", UtilObject.getBigDecimalOrZero(onTheWay));
				stockBatchList.get(i).put("status", 2);
			} else if (StringUtils.hasText(qualityInspection)) {
				stockBatchList.get(i).put("qty", UtilObject.getBigDecimalOrZero(qualityInspection));
				stockBatchList.get(i).put("status", 3);
			} else if (StringUtils.hasText(freeze)) {
				stockBatchList.get(i).put("qty", UtilObject.getBigDecimalOrZero(freeze));
				stockBatchList.get(i).put("status", 4);
			}
		}

		for (int i = 0; i < batchMaterialSpecList.size(); i++) {
			String ftyCode = UtilString.getStringOrEmptyForObject(batchMaterialSpecList.get(i).get("ftyCode"));
			Integer ftyId = dictionaryService.getFtyIdByFtyCode(ftyCode);
			String matCode = UtilString.getStringOrEmptyForObject(batchMaterialSpecList.get(i).get("matCode"));
			Integer matId = matCodeIdMap.get(matCode);
			batchMaterialSpecList.get(i).put("ftyId", ftyId);
			batchMaterialSpecList.get(i).put("matId", matId);
		}

		int count = 0;

		if (stockPositionList.size() > 0) {
			count = count + stockPositionDao.insertStockPositionList(stockPositionList);
			List<Map<String, Object>> snList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> stockPosition : stockPositionList) {
				Map<String, Object> snMap = new HashMap<String, Object>();
				snMap.put("qty", stockPosition.get("qty"));
				snMap.put("snId", 0);
				snMap.put("matId", stockPosition.get("matId"));
				snMap.put("stockId", stockPosition.get("id"));
				snMap.put("freeze", 0);
				snList.add(snMap);
			}
			count = count + stockPositionDao.insertSNList(snList);
		}

		if (stockBatchList.size() > 0) {
			count = count + stockPositionDao.insertStockBatchList(stockBatchList);
		}
		if (batchMaterialList.size() > 0) {
			count = count + stockPositionDao.insertBatchMaterialList(batchMaterialList);
		}
		if (batchMaterialSpecList.size() > 0) {
			count = count + stockPositionDao.insertBatchMaterialSpecList(batchMaterialSpecList);
		}

		return count;

	}

	@Override
	public List<Map<String, Object>> listStockBatchOnPaging(Map<String, Object> map) throws Exception {
		return stockBatchDao.selectStockBatchOnPaging(map);
	}

	@Override
	public int updateStatus(Map<String, Object> map) throws Exception {
		int count = 0;
		count = stockBatchDao.updateStatus(map);
		stockBatchDao.updatePositionStatus(map);

		return count;
	}

	@Override
	public List<Map<String, Object>> listStockPositionOnPaging(Map<String, Object> map) throws Exception {// TODO
		/*
		 * List<Map<String, Object>> list =
		 * stockPositionDao.selectStockPositionOnPaging(map); List<Map<String, Object>>
		 * relist = new ArrayList<Map<String, Object>>(); Map<String, Object> reMap =
		 * null; List<Map<String,Object>> snList = null; String uniqueId = "";
		 * BigDecimal stockQty = null; for(int i=0; i<list.size() ; i++){
		 * Map<String,Object> m = list.get(i); String uniqueIdTmp =
		 * UtilObject.getStringOrEmpty(m.get("unique_id")); if(uniqueId.equals("")){
		 * uniqueId= uniqueIdTmp; reMap = m; snList = new ArrayList<Map<String,
		 * Object>>(); stockQty = new BigDecimal("0"); }
		 * 
		 * if(!uniqueId.equals(uniqueIdTmp)) { reMap.put("stock_qty", stockQty);
		 * reMap.put("pack_num", snList.size()); reMap.put("sn_list", snList);
		 * relist.add(reMap);
		 * 
		 * uniqueId= uniqueIdTmp; reMap = m; snList = new ArrayList<Map<String,
		 * Object>>(); stockQty = new BigDecimal("0"); }
		 * 
		 * 
		 * Map<String,Object> sn = new HashMap<String,Object>();
		 * sn.put("package_type_code", m.get("package_type_code"));
		 * sn.put("package_standard_ch", m.get("package_standard_ch"));
		 * sn.put("erp_batch", m.get("erp_batch")); sn.put("product_batch",
		 * m.get("product_batch")); sn.put("quality_batch", m.get("quality_batch"));
		 * sn.put("batch", m.get("batch")); sn.put("package_code",
		 * m.get("package_code")); sn.put("purchase_order_code",
		 * m.get("purchase_order_code")); sn.put("shelf_life", m.get("shelf_life"));
		 * sn.put("product_line_name", m.get("product_line_name"));
		 * sn.put("installation_name", m.get("installation_name"));
		 * sn.put("class_type_name", m.get("class_type_name"));
		 * sn.put("qty",m.get("stock_qty")); snList.add(sn);
		 * 
		 * stockQty=stockQty.add(UtilObject.getBigDecimalOrZero(m.get("stock_qty")));
		 * 
		 * if(list.size()-1 == i){ reMap.put("stock_qty", stockQty);
		 * reMap.put("pack_num", snList.size()); reMap.put("sn_list", snList);
		 * relist.add(reMap); }
		 * 
		 * } relist.get(0).put("totalCount", relist.size());
		 */
		return stockPositionDao.selectStockPositionGroupOnPaging(map);
	}

	@Override
	public List<Map<String, Object>> excelStockPositionOnPaging(Map<String, Object> map) throws Exception {
		return stockPositionDao.selectStockPositionOnPaging(map);
	}

	@Override
	public BatchMaterial getBatchMaterial(BatchMaterial bm) throws Exception {

		return batchMaterialDao.selectByUniqueKey(bm);
	}

	@Override
	public Map<String, Object> getBaseInfo(CurrentUser cUser) {

		DicBoard dicBoard = dicBoardMapper.selectByPrimaryKey(cUser.getBoardId());

		// List<DicBoard> boardList=dicBoardMapper.selectAllBoard();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// for (int i=0;i<boardList.size();i++) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("boardId", dicBoard.getBoardId());
		param.put("userId", cUser.getUserId());
		List<Map<String, Object>> listMap = dicFactoryMapper.selectFtyByBoardId(param);

		resultMap.put("fty_list", listMap);
		resultMap.put("board_id", dicBoard.getBoardId());
		resultMap.put("board_name", dicBoard.getBoardName());
		// }
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> selectWarrantyDateOnpaging(Map<String, Object> param) {
		List<Map<String, Object>> list = stockPositionDao.selectWarrantyDateOnpaging(param);
		// List<Map<String, Object>> relist = new ArrayList<Map<String, Object>>();
		// Map<String, Object> reMap = null;
		// List<Map<String,Object>> snList = null;
		// String uniqueId = "";
		// BigDecimal stockQty = null;
		// for(int i=0; i<list.size() ; i++){
		// Map<String,Object> m = list.get(i);
		// String uniqueIdTmp = UtilObject.getStringOrEmpty(m.get("unique_id"));
		// if(uniqueId.equals("")){
		// uniqueId= uniqueIdTmp;
		// reMap = m;
		// snList = new ArrayList<Map<String, Object>>();
		// stockQty = new BigDecimal("0");
		// }
		//
		// if(!uniqueId.equals(uniqueIdTmp))
		// {
		// reMap.put("stock_qty", stockQty);
		// reMap.put("pack_num", snList.size());
		// reMap.put("sn_list", snList);
		// relist.add(reMap);
		//
		// uniqueId= uniqueIdTmp;
		// reMap = m;
		// snList = new ArrayList<Map<String, Object>>();
		// stockQty = new BigDecimal("0");
		// }
		//
		//
		// Map<String,Object> sn = new HashMap<String,Object>();
		// sn.put("package_type_code", m.get("package_type_code"));
		// sn.put("package_standard_ch", m.get("package_standard_ch"));
		// sn.put("erp_batch", m.get("erp_batch"));
		// sn.put("product_batch", m.get("product_batch"));
		// sn.put("quality_batch", m.get("quality_batch"));
		// sn.put("batch", m.get("batch"));
		// sn.put("package_code", m.get("package_code"));
		// sn.put("purchase_order_code", m.get("purchase_order_code"));
		// sn.put("shelf_life", m.get("shelf_life"));
		// sn.put("product_line_name", m.get("product_line_name"));
		// sn.put("installation_name", m.get("installation_name"));
		// sn.put("class_type_name", m.get("class_type_name"));
		// sn.put("qty",m.get("stock_qty"));
		// snList.add(sn);
		//
		// stockQty=stockQty.add(UtilObject.getBigDecimalOrZero(m.get("stock_qty")));
		//
		// if(list.size()-1 == i){
		// reMap.put("stock_qty", stockQty);
		// reMap.put("pack_num", snList.size());
		// reMap.put("sn_list", snList);
		// relist.add(reMap);
		// }
		// }
		// relist.get(0).put("totalCount", relist.size());
		return list;

	}

	@Override
	public List<Map<String, Object>> selectWarrantyDateSnMassage(Map<String, Object> map) {
		return stockPositionDao.selectWarrantyDateSnMassage(map);
	}

	// TODO
	@Override
	public JSONObject pdaProductPicture() {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		JSONObject body = new JSONObject();
		try {
			List<Map<String, Object>> resultAry = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> dayList = stockPositionDao.selectWarringPdaPictrueDay();
			List<Map<String, Object>> dayAll = stockPositionDao.selectWarringPdaPictrueDayAll();
			for (int i = 0; i < dayAll.size(); i++) {
				Object key = dayAll.get(i).get("product_line_name");
				for (int j = 0; j < dayList.size(); j++) {
					Object keyInner = dayList.get(j).get("product_line_name");
					if (key.equals(keyInner)) {
						Map<String, Object> map = new HashMap<String, Object>();

						map.put("product_value",
								UtilObject.getBigDecimalOrZero(dayList.get(j).get("qty")).divide(
										UtilObject.getBigDecimalOrZero(dayAll.get(i).get("qty")), 2,
										BigDecimal.ROUND_HALF_EVEN));
						map.put("product_name", keyInner.toString());
						map.put("product_line_id", dayList.get(j).get("product_line_id"));
						resultAry.add(map);
					}

				}

			}

			JSONArray chartInfoAry = new JSONArray();
			
           if(dayList!=null&&dayList.size()>0 ) {
			JSONObject chartObject1 = new JSONObject();
			chartObject1.put("name", "即将过期占比");
			chartObject1.put("type", "1");
			chartObject1.put("dependence", "0");
			chartInfoAry.add(chartObject1);
           }
			JSONArray xAxisAry = new JSONArray();
			List<BigDecimal> leftList = new ArrayList<BigDecimal>();

			BigDecimal tenThousand = new BigDecimal(100);

			for (Map<String, Object> object : resultAry) {
				// JSONObject xAxisObj = (JSONObject) object;
				leftList.add(UtilObject.getBigDecimalOrZero(object.get("product_value")).multiply(tenThousand));

				JSONArray xAry = new JSONArray();
				JSONObject obj = new JSONObject();
				JSONObject innerObj1 = new JSONObject();

				innerObj1.put("yAxisValue",
						UtilObject.getBigDecimalOrZero(UtilObject.getBigDecimalOrZero(object.get("product_value")))
								.multiply(tenThousand).toString());
				innerObj1.put("primaryKey",
						UtilObject.getBigDecimalOrZero(UtilObject.getIntegerOrNull(object.get("product_line_id"))));
				
				xAry.add(innerObj1);				

				obj.put("xAxisName", object.get("product_name"));
				obj.put("yAxisValueList", xAry);
				xAxisAry.add(obj);
			}
			BigDecimal leftMax = BigDecimal.ZERO;

			for (int i = 0; i < leftList.size(); i++) {
				if (leftMax.compareTo(leftList.get(i)) == -1) {
					leftMax = leftList.get(i);
				}
			}

			body.put("leftAxisMaxValue", leftMax.toString());
			body.put("leftAxisUnit", "%");

			body.put("title", "即将过期占比");
			body.put("chartInfoList", chartInfoAry);
			body.put("xAxisList", xAxisAry);

		} catch (Exception e) {
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		return UtilResult.getResultToJson(status, error_code, body);
	}

	@Override
	public List<Map<String, Object>> selectPersontByProduct(Integer product_line_id) throws Exception {
		List<Map<String, Object>> dayListLq = stockPositionDao.selectWarringPdaByProductLineIdLq(product_line_id);
		List<Map<String, Object>> dayListGq = stockPositionDao.selectWarringPdaByProductLineIdGq(product_line_id);
		List<Map<String, Object>> dayListYj = stockPositionDao.selectWarringPdaByProductLineIdYj(product_line_id);
		List<Map<String, Object>> dayListAll = stockPositionDao.selectWarringPdaByProductLineIdAll(product_line_id);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		BigDecimal southend=new BigDecimal(1000);
		for (int i = 0; i < dayListAll.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("wh_code", dayListAll.get(i).get("wh_code"));
			map.put("wh_name", dayListAll.get(i).get("wh_code"));
			map.put("area_code", dayListAll.get(i).get("area_code"));
			map.put("area_name", dayListAll.get(i).get("area_name"));
			map.put("qty_lq", 0);
			map.put("qty_gq", 0);
			map.put("qty_yj", 0);
			map.put("qty_zs", UtilObject.getBigDecimalOrZero(dayListAll.get(i).get("qty")).divide(southend,2));
			map.put("lq_percent", 0);
			Object outKey = dayListAll.get(i).get("wh_id") + "-" + dayListAll.get(i).get("area_id");
			for (int j = 0; j < dayListLq.size(); j++) {
				Object innerKey = dayListLq.get(j).get("wh_id") + "-" + dayListLq.get(j).get("area_id");
				if (outKey.equals(innerKey)) {
					map.put("qty_lq", UtilObject.getBigDecimalOrZero(dayListLq.get(j).get("qty")).divide(southend,2));
					BigDecimal percent=	UtilObject.getBigDecimalOrZero( dayListLq.get(j).get("qty")).divide(UtilObject.getBigDecimalOrZero(dayListAll.get(i).get("qty")), 4);
					
					map.put("lq_percent",percent.multiply(new BigDecimal(100)));
				}
			}
			for (int j = 0; j < dayListGq.size(); j++) {
				Object innerKey = dayListGq.get(j).get("wh_id") + "-" + dayListGq.get(j).get("area_id");
				if (outKey.equals(innerKey)) {
					map.put("qty_gq", UtilObject.getBigDecimalOrZero(dayListGq.get(j).get("qty")).divide(southend, 2));
				}
			}

			for (int j = 0; j < dayListYj.size(); j++) {
				Object innerKey = dayListYj.get(j).get("wh_id") + "-" + dayListYj.get(j).get("area_id");
				if (outKey.equals(innerKey)) {
					map.put("qty_yj", UtilObject.getBigDecimalOrZero(dayListYj.get(j).get("qty")).divide(southend,2));
				}

			}
			map.put("lq_percent", map.get("lq_percent")+"%");
			result.add(map);
		}

		return result;
	}

	@Override
	public List<Map<String, Object>> selectWarehouseListByWhId(Integer whId) {
		
		return dicWarehouseMapper.selectWarehouseListByWhId(whId);
	}


	@Override
	public Map<String, Object> selectFirstView() {
		
		return stockPositionDao.selectFirstView();
	}

	@Override
	public Map<String, Object> selectFirstReport() {
		return stockPositionDao.selectFirstReport();
	}

	@Override
	public List<Map<String, Object>> selectDetail(Integer searchType) {
		return stockPositionDao.selectDetail(searchType);
	}

	@Override
	public List<Map<String, Object>> selectDetailMat(Map<String, Object> map) {
		return stockPositionDao.selectDetailMat(map);
	}

	@Override
	public List<Map<String, Object>> selectFirstViewStockDays(Map<String, Object> map) {
	
		return stockBatchDao.selectFirstViewStockDays(map);
	}

	@Override
	public List<Map<String, Object>> selectSecondViewStockDays(Map<String, Object> map) {
		return stockBatchDao.selectSecondViewStockDays(map);
	}

	@Override
	public List<Map<String, Object>> selectThirdViewStockDays(Map<String, Object> map) {
		return stockBatchDao.selectThirdViewStockDays(map);
	}
	

	


	@Override
	public BigDecimal selectALLPositionQty(Map<String, Object> param) {
		return stockPositionDao.selectAllQty(param);
	}
	
	@Override
	public BigDecimal selectALLBatchQty(Map<String, Object> param) {
		return stockBatchDao.selectAllQty(param);
	}

	@Override
	public List<Map<String, Object>> selectSecondViewTotalAmount(Map<String, Object> map) {
		return stockBatchDao.selectSecondViewTotalAmount(map);
	}

	@Override
	public Map<String, Object> listStockPositionOnPagingCw(Map<String, Object> map) throws Exception {
		return stockPositionDao.selectStockPositionGroupOnPagingCw(map);
	}

}
