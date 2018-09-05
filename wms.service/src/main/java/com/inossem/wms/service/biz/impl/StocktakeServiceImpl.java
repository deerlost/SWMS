package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.biz.BizDeliveryNoticeHeadMapper;
import com.inossem.wms.dao.biz.BizStockTakeHeadMapper;
import com.inossem.wms.dao.biz.BizStockTakeItemMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizStockTakeHead;
import com.inossem.wms.model.biz.BizStockTakeItem;
import com.inossem.wms.model.enums.EnumSequence;
import com.inossem.wms.model.enums.EnumSpecStock;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumStocktakeMode;
import com.inossem.wms.model.enums.EnumStocktakeStatus;
import com.inossem.wms.model.enums.EnumStocktakeStockStatus;
import com.inossem.wms.model.enums.EnumStocktakeStocktakeType;
import com.inossem.wms.model.enums.EnumWorkShiftStatus;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IStocktakeService;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilMap;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONObject;

@Transactional
@Service("stocktakeService")
public class StocktakeServiceImpl implements IStocktakeService {

	@Resource
	private ICommonService commonService;
	@Resource
	private BizDeliveryNoticeHeadMapper deliveryNoticeHeadDao;
	@Resource
	private BizStockTakeHeadMapper headDao;
	@Resource
	private BizStockTakeItemMapper itemDao;

	/**
	 * 2.2 新增时查询盘点行项目信息列表
	 */
	@Override
	public List<Map<String, Object>> queryItemListOnPaging(JSONObject json) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("fty_id", json.getString("fty_id"));
		paramMap.put("location_id", json.getString("location_id"));
		if (EnumStocktakeStockStatus.ONLY_LIMIT_STOCK.getValue()
				.equals(json.getString("stock_take_status").toString())) {
			// 只盘点非限制库存
			paramMap.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			paramMap.put("spec_stock", EnumSpecStock.STOCK_BATCH_STATUS_NORMAL.getValue());

		}
//		else if (EnumStocktakeStockStatus.ONLY_LOCKED_STOCK.getValue()
//				.equals(json.getString("stock_take_status").toString())) {
//			// 只盘点冻结库存
//			paramMap.put("status", EnumStockStatus.STOCK_BATCH_STATUS_FREEZE.getValue());
//
//		} else if (EnumStocktakeStockStatus.ONLY_CONSIGNMENT.getValue()
//				.equals(json.getString("stock_take_status").toString())) {
//			// 只盘点寄售库存
//			paramMap.put("spec_stock", EnumSpecStock.STOCK_BATCH_STATUS_CONSIGNMENT.getValue());
//
//		} else if (EnumStocktakeStockStatus.ONLY_PROJECT_STOCK.getValue()
//				.equals(json.getString("stock_take_status").toString())) {
//			// 只盘点项目库存
//			paramMap.put("spec_stock", EnumSpecStock.STOCK_BATCH_STATUS_PROJECT.getValue());
//
//		} else if (EnumStocktakeStockStatus.ONLY_CUSTOMER_STOCK.getValue()
//				.equals(json.getString("stock_take_status").toString())) {
//			// 只盘点客户库存
//			paramMap.put("spec_stock", EnumSpecStock.STOCK_BATCH_STATUS_SALE.getValue());
//
//		}
		paramMap.put("stock_take_mode", json.getInt("stock_take_mode"));
		paramMap.put("code", json.getString("code"));
		return itemDao.queryItemListOnPaging(paramMap);
	}

	/**
	 * 2.3 盘点信息暂存/提交
	 */
	@Override
	public JSONObject save(CurrentUser cuser, JSONObject json) throws Exception {
		// 头信息
		BizStockTakeHead head = new BizStockTakeHead();
		head.setStockTakeType(Byte.parseByte(json.getString("stock_take_type")));
		head.setStockTakeMode(Byte.parseByte(json.getString("stock_take_mode")));
		head.setStockTakeTime(UtilString.getDateForString(json.getString("stock_take_time")));
		head.setWorkShift(Byte.parseByte(json.getString("work_shift")));
		head.setFtyId(json.getInt("fty_id"));
		head.setLocationId(json.getInt("location_id"));
		head.setStockTakeStatus(Byte.parseByte(json.getString("stock_take_status")));
		head.setRemark(json.getString("remark"));
		head.setStatus(Byte.parseByte(json.getString("status")));
		head.setCreateUser(cuser.getUserId());
		if (UtilString.isNullOrEmpty(json.getString("stock_take_id"))) {
			String code = commonService.getNextReceiptCode(EnumSequence.STOCKTAKE.getValue());
			// 盘点凭证号
			head.setStockTakeCode(code);
			// 新增
			headDao.insertSelective(head);

		} else {
			head.setStockTakeId(json.getInt("stock_take_id"));
			// 修改
			headDao.updateByPrimaryKeySelective(head);

		}
		json.put("stock_take_id", head.getStockTakeId());
		json.put("stock_take_code", head.getStockTakeCode());

		// 行项目信息
		itemDao.deleteByStockTakeId(head.getStockTakeId());
		List<Map<String, Object>> itemList = (List<Map<String, Object>>) json.get("item_list");
		List<Map<String, Object>> insertItemList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : itemList) {
			map.put("stock_take_id", head.getStockTakeId());
			map.put("request_source", json.get("request_source"));
			insertItemList.add(map);
			if (insertItemList.size() > 98) {
				itemDao.insertItemList(insertItemList);
				insertItemList.clear();
			}
		}
		if (insertItemList.size() > 0) {
			itemDao.insertItemList(insertItemList);
		}
		return json;
	}

	/**
	 * 2.4 查询盘点列表
	 */
	@Override
	public List<Map<String, Object>> listOnPaging(Map<String, Object> paramMap) {
		List<Map<String, Object>> list = headDao.listOnPaging(paramMap);
		for (Map<String, Object> map : list) {
			map.put("create_time",
					UtilString.getLongStringForDate((Date)map.get("create_time")));
			map.put("status_name", EnumStocktakeStatus.getNameByValue(map.get("status").toString()));
			map.put("stock_take_type_name",
					EnumStocktakeStocktakeType.getNameByValue(map.get("stock_take_type").toString()));
			map.put("stock_take_mode_name", EnumStocktakeMode.getNameByValue(map.get("stock_take_mode").toString()));
		}
		return list;
	}

	/**
	 * 2.5 查询盘点详情
	 */
	@Override
	public JSONObject info(Integer stockTakeId) {
		// 头信息
		BizStockTakeHead head = headDao.selectInfoByPrimaryKey(stockTakeId);
		// 行项目信息
		head.setItemList(itemDao.getListByStockTakeId(stockTakeId));
		UtilJSONConvert.setNullToEmpty(head);
		JSONObject json = JSONObject.fromObject(head);
		json.remove("modifyTime");
		json.put("createTime", UtilString.getLongStringForDate(head.getCreateTime()));
		json.put("stockTakeTime", UtilString.getShortStringForDate(head.getStockTakeTime()));
		json.put("stock_take_status_name",
				EnumStocktakeStockStatus.getNameByValue(head.getStockTakeStatus().toString()));
		json.put("status_name", EnumStocktakeStatus.getNameByValue(head.getStatus().toString()));
		json.put("stock_take_type_name", EnumStocktakeStocktakeType.getNameByValue(head.getStockTakeType().toString()));
		json.put("stock_take_mode_name", EnumStocktakeMode.getNameByValue(head.getStockTakeMode().toString()));
//		json.put("work_shift_name", EnumWorkShiftStatus.getNameByValue(head.getWorkShift()));
		return json;
	}

	/**
	 * 2.5 查询盘点详情(手持端)
	 */
	@Override
	public JSONObject infoForPDA(Integer stockTakeId) {
		// 头信息
		BizStockTakeHead head = headDao.selectInfoByPrimaryKey(stockTakeId);
		UtilJSONConvert.setNullToEmpty(head);
		JSONObject json = JSONObject.fromObject(head);
		json.remove("modifyTime");
		json.put("createTime", UtilString.getLongStringForDate(head.getCreateTime()));
		json.put("stockTakeTime", UtilString.getShortStringForDate(head.getStockTakeTime()));
		json.put("stock_take_status_name",
				EnumStocktakeStockStatus.getNameByValue(head.getStockTakeStatus().toString()));
		json.put("status_name", EnumStocktakeStatus.getNameByValue(head.getStatus().toString()));
		json.put("stock_take_type_name", EnumStocktakeStocktakeType.getNameByValue(head.getStockTakeType().toString()));
		json.put("stock_take_mode_name", EnumStocktakeMode.getNameByValue(head.getStockTakeMode().toString()));
//		json.put("work_shift_name", EnumWorkShiftStatus.getNameByValue(head.getWorkShift()));
		// 行项目信息
		List<Map<String, Object>> itemList = itemDao.getListByStockTakeId(stockTakeId);
		if (EnumStocktakeMode.POSITION.getValue().equals(head.getStockTakeMode().toString())) {
			// 0-仓位
			Map<String, Object> positionItemMap = new HashMap<String, Object>();
			for (Map<String, Object> item : itemList) {
				Map<String, Object> mat = new HashMap<String, Object>();
				mat.put("item_id", item.get("item_id"));
				mat.put("mat_id", item.get("mat_id"));
				mat.put("mat_code", item.get("mat_code"));
				mat.put("mat_name", item.get("mat_name"));
				mat.put("unit_id", item.get("unit_id"));
				mat.put("name_zh", item.get("name_zh"));
				mat.put("stock_qty", item.get("stock_qty"));
				mat.put("decimal_place", item.get("decimal_place"));
				mat.put("qty", item.get("qty"));
				mat.put("remark", item.get("remark"));
				if (positionItemMap.containsKey(item.get("position_id").toString())) {
					Map<String, Object> positionItem = (Map<String, Object>) positionItemMap
							.get(item.get("position_id").toString());
					List<Map<String, Object>> matList = (List<Map<String, Object>>) positionItem.get("mat_list");
					matList.add(mat);

				} else {
					Map<String, Object> positionItem = new HashMap<String, Object>();
					positionItem.put("area_id", item.get("area_id"));
					positionItem.put("area_code", item.get("area_code"));
					positionItem.put("position_id", item.get("position_id"));
					positionItem.put("position_code", item.get("position_code"));
					List<Map<String, Object>> matList = new ArrayList<Map<String, Object>>();
					matList.add(mat);
					positionItem.put("mat_list", matList);
					positionItemMap.put(item.get("position_id").toString(), positionItem);
				}
			}
			json.put("position_item_list", UtilMap.mapToList(positionItemMap));

		} else if (EnumStocktakeMode.MAT.getValue().equals(head.getStockTakeMode().toString())) {
			// 10-物料
			Map<String, Object> matItemMap = new HashMap<String, Object>();
			for (Map<String, Object> item : itemList) {
				Map<String, Object> position = new HashMap<String, Object>();
				position.put("item_id", item.get("item_id"));
				position.put("area_code", item.get("area_code"));
				position.put("position_code", item.get("position_code"));
				position.put("stock_qty", item.get("stock_qty"));
				position.put("decimal_place", item.get("decimal_place"));
				position.put("qty", item.get("qty"));
				position.put("remark", item.get("remark"));
				if (matItemMap.containsKey(item.get("mat_id").toString())) {
					Map<String, Object> matItem = (Map<String, Object>) matItemMap.get(item.get("mat_id").toString());
					List<Map<String, Object>> positionList = (List<Map<String, Object>>) matItem.get("position_list");
					positionList.add(position);

				} else {
					Map<String, Object> matItem = new HashMap<String, Object>();
					matItem.put("mat_id", item.get("mat_id"));
					matItem.put("mat_code", item.get("mat_code"));
					matItem.put("mat_name", item.get("mat_name"));
					matItem.put("unit_id", item.get("unit_id"));
					matItem.put("name_zh", item.get("name_zh"));
					List<Map<String, Object>> positionList = new ArrayList<Map<String, Object>>();
					positionList.add(position);
					matItem.put("position_list", positionList);
					matItemMap.put(item.get("mat_id").toString(), matItem);
				}
			}
			json.put("mat_item_list", UtilMap.mapToList(matItemMap));

		}
		UtilJSONConvert.setNullToEmpty(json);
		return json;
	}

	/**
	 * 2.6 删除盘点单
	 */
	@Override
	public void delete(Integer stockTakeId) {
		// 行项目信息
		itemDao.deleteByStockTakeId(stockTakeId);
		// 头信息
		headDao.deleteByPrimaryKey(stockTakeId);
	}

	/**
	 * 2.7 盘点数量暂存/完成
	 */
	@Override
	public void take(CurrentUser cuser, JSONObject json) {
		// 头信息
		if (EnumStocktakeStatus.COMPLETED.getValue().equals(json.getString("status"))) {
			BizStockTakeHead head = new BizStockTakeHead();
			head.setStatus(Byte.parseByte(json.getString("status")));
			head.setStockTakeId(json.getInt("stock_take_id"));
			headDao.updateByPrimaryKeySelective(head);
		}
		// 行项目信息
		List<Map<String, Object>> itemList = (List<Map<String, Object>>) json.get("item_list");
		for (Map<String, Object> itemMap : itemList) {
			BizStockTakeItem item = new BizStockTakeItem();
			item.setItemId(Integer.parseInt(itemMap.get("item_id").toString()));
			item.setQty(new BigDecimal(itemMap.get("qty").toString()));
			item.setRemark(itemMap.get("remark").toString());
			item.setStockTakeUser(cuser.getUserId());
			item.setStockTakeTime(new Date());
			item.setRequestSource(Byte.parseByte(json.get("request_source").toString()));
			itemDao.updateByPrimaryKeySelective(item);
		}
	}

	/**
	 * 2.8 重盘
	 */
	@Override
	public void reset(Integer stock_take_id) {
		BizStockTakeHead head = new BizStockTakeHead();
		head.setStockTakeId(stock_take_id);
		head.setStatus(Byte.parseByte(EnumStocktakeStatus.INCOMPLETE.getValue()));
		headDao.updateByPrimaryKeySelective(head);
	}

}
