package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.biz.BizAllocateDeliveryHeadMapper;
import com.inossem.wms.dao.biz.BizAllocateDeliveryItemMapper;
import com.inossem.wms.dao.biz.BizAllocateHeadMapper;
import com.inossem.wms.dao.biz.BizAllocateItemMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.stock.StockBatchMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizAllocateDeliveryHead;
import com.inossem.wms.model.biz.BizAllocateDeliveryItem;
import com.inossem.wms.model.biz.BizAllocateHead;
import com.inossem.wms.model.biz.BizAllocateItem;
import com.inossem.wms.model.enums.EnumAllocateStatus;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.vo.BizAllocateHeadVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.model.vo.StockBatchVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IAllocService;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service("allocService")
public class AllocServiceImpl implements IAllocService {

	@Autowired
	private BizAllocateHeadMapper allocateHeadDao;

	@Autowired
	private BizAllocateItemMapper allocateItemDao;

	@Autowired
	private DicStockLocationMapper stockLocationDao;

	@Autowired
	private StockBatchMapper stockBatchDao;

	@Autowired
	private SequenceDAO sequenceDao;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private BizAllocateDeliveryHeadMapper allocateDeliveryHeadDao;

	@Autowired
	private BizAllocateDeliveryItemMapper allocateDeliveryItemDao;

	@Override
	public List<BizAllocateHeadVo> getAllocList(Map<String, Object> conditionMap) {
		return allocateHeadDao.selectlistByConditionOnPaging(conditionMap);
	}

	/**
	 * 根据用户id获取库存地点
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public List<DicStockLocationVo> getLocationsByCorpId(Integer corpId) {
		return stockLocationDao.selectLocationsByCorpId(corpId);
	}

	@Override
	public List<StockBatchVo> getMaterials(Map<String, Object> map) throws Exception {

		return stockBatchDao.getMatNumGroupByMatOnPaging(map);
	}

	@Override
	public Map<String, Object> createAllocateParams(CurrentUser cuser, JSONObject json) throws Exception {

		Map<String, Object> mapAllocate = new HashMap<String, Object>();
		BizAllocateHead head = new BizAllocateHead();
		// 保存提交 0 保存 1 提交
		if (json.containsKey("is_save_commit")) {
			mapAllocate.put("isSaveCommit", json.getString("is_save_commit"));
		}

		if (json.containsKey("allocate_id") && !UtilString.isNullOrEmpty(json.getString("allocate_id"))) {
			head.setAllocateId(json.getInt("allocate_id"));
		}
		if (json.containsKey("allocate_code") && !UtilString.isNullOrEmpty(json.getString("allocate_code"))) {
			head.setAllocateCode(json.getString("allocate_code"));
		}
		if (json.containsKey("demand_date")) {
			head.setDemandDate(UtilString.getDateForString(json.getString("demand_date"), null));
		}
		if (json.containsKey("fty_input")) {
			head.setFtyInput(json.getInt("fty_input"));
		}
		if (json.containsKey("location_input")) {
			head.setLocationInput(json.getInt("location_input"));
		}
		if (json.containsKey("remark")) {
			head.setRemark(json.getString("remark"));
		}
		if (json.containsKey("distributed")) {
			Integer distributed = json.getInt("distributed");
			head.setDistributed(distributed.byteValue());
		}
		// 申请用户相关信息
		head.setCreateUser(cuser.getUserId());
		head.setCorpId(cuser.getCorpId());
		head.setOrgId(cuser.getOrgId());

		mapAllocate.put("head", head);
		// 经办人
		List<Map<String, Object>> jbrList = new ArrayList<Map<String, Object>>();
		if (json.containsKey("staffids")) {
			JSONArray staffids = json.getJSONArray("staffids");
			if (staffids != null && staffids.size() > 0) {
				for (int i = 0; i < staffids.size(); i++) {
					JSONObject user = staffids.getJSONObject(i);

					HashMap<String, Object> jbrMap = new HashMap<String, Object>();
					jbrMap.put("userId", user.getString("user_id"));
					jbrMap.put("roleId", user.getInt("role_id"));
					jbrList.add(jbrMap);
				}
			}
			mapAllocate.put("staffids", jbrList);
		}

		// List<Map<String, Object>> allotItemList = new ArrayList<Map<String,
		// Object>>();
		List<BizAllocateItem> itemList = new ArrayList<BizAllocateItem>();
		if (json.containsKey("materials")) {
			JSONArray materials = json.getJSONArray("materials");
			if (materials != null && materials.size() > 0) {
				for (int i = 0; i < materials.size(); i++) {
					JSONObject material = materials.getJSONObject(i);
					BizAllocateItem item = new BizAllocateItem();
					item.setAllocateId(i + 1);
					item.setStockQty(new BigDecimal(0));
					item.setLocationOutput(material.getInt("location_output"));
					item.setMatId(material.getInt("mat_id"));
					item.setUnitId(material.getInt("unit_id"));
					item.setFtyOutput(material.getInt("fty_output"));

					BigDecimal menge = new BigDecimal(material.getString("allocate_qty"));
					// 只有大于0的行项目才保存
					if (menge.compareTo(new BigDecimal(0)) == 1) {
						item.setAllocateQty(menge);
						itemList.add(item);

					}
				}
				mapAllocate.put("itemList", itemList);
			}

		}
		return mapAllocate;

	}

	@Override
	public Map<String, Object> save(Map<String, Object> param) throws Exception {
		Map<String, Object> returnValue = new HashMap<String, Object>();

		BizAllocateHead head = (BizAllocateHead) param.get("head");
		// 当存在ID时代表是0草稿状态保存
		long allotCode = 0;
		if (head.getAllocateId() != null) {
			int allocateId = 0;
			allocateId = head.getAllocateId();
			BizAllocateHead headUpdateDelete = new BizAllocateHead();
			headUpdateDelete.setAllocateId(head.getAllocateId());
			headUpdateDelete.setIsDelete((byte) 1);
			// 逻辑删除原单据
			allocateHeadDao.deleteByPrimaryKey(allocateId);
			allocateItemDao.deleteByAllotId(allocateId);
			commonService.removeReceiptUser(allocateId, EnumReceiptType.ALLOCATE.getValue());

		} else {
			allotCode = sequenceDao.selectNextVal("allocate");
			head.setAllocateCode(allotCode + "");
		}
		String isSaveCommit = "" + param.get("isSaveCommit");
		byte status = 0;
		if ("1".equals(isSaveCommit)) {
			status = 10;
		}
		head.setStatus(status);
		head.setAllocateId(null);
		allocateHeadDao.insertSelective(head);

		// 处理行项目
		List<BizAllocateItem> itemList = (List<BizAllocateItem>) param.get("itemList");
		if (itemList != null && itemList.size() > 0) {
			for (int i = 0; i < itemList.size(); i++) {
				BizAllocateItem allotItem = itemList.get(i);
				allotItem.setAllocateId(head.getAllocateId());

				allotItem.setAllocateRid(i + 1);
				allotItem.setStatus(status);

				allotItem.setStockQty(new BigDecimal(0));
				if (allotItem.getAllocateQty() != null
						&& allotItem.getAllocateQty().compareTo(new BigDecimal(0)) == 1) {
					allotItem.setItemId(null);
					allocateItemDao.insertSelective(allotItem);
				} else {
					throw new RuntimeException("数量为0");
				}
			}
		}

		// 经办人处理
		List<Map<String, Object>> jbrList = (List<Map<String, Object>>) param.get("staffids");
		if (jbrList != null && jbrList.size() > 0) {
			for (int i = 0; i < jbrList.size(); i++) {
				Map<String, Object> user = jbrList.get(i);
				String userId = (String) user.get("userId");
				Integer roleId = (Integer) user.get("roleId");
				commonService.addReceiptUser(head.getAllocateId(), EnumReceiptType.ALLOCATE.getValue(), userId, roleId);
			}
		}
		if (head.getStatus() == 10) {
			// 提交后发起审批
			// 暂时没有该共通函数
			commonService.initRoleWf(head.getAllocateId(), EnumReceiptType.ALLOCATE.getValue(), head.getAllocateCode(),
					head.getCreateUser());
		}
		// returnValue.put("success", "1");
		// returnValue.put("msg", "操作成功");
		returnValue.put("allocate_id", head.getAllocateId());
		return returnValue;
	}

	@Override
	public boolean allocateDel(Integer allocateId) throws Exception {

		BizAllocateHead head = allocateHeadDao.selectByPrimaryKey(allocateId);

		if (head != null && head.getStatus() < 15) {
			deleteDBDByAllotId(allocateId);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int deleteDBDByAllotId(Integer allocateId) throws Exception {

		allocateHeadDao.deleteByPrimaryKey(allocateId);
		allocateItemDao.updateIsDeleteByHeadId(allocateId);
		commonService.removeReceiptUser(allocateId, EnumReceiptType.ALLOCATE.getValue());

		return 0;
	}

	/**
	 * 取得调拨单抬头详细信息（详细页面用）
	 */
	@Override
	public List<BizAllocateHeadVo> getAllotListByCondition(Map<String, Object> map) throws Exception {
		return allocateHeadDao.selectlistByConditionOnPaging(map);
	}

	/**
	 * 取得调拨单明细详细信息（详细页面用）
	 */
	@Override
	public List<Map<String, Object>> getAllotItemsByCondition(Map<String, Object> map) throws Exception {
		return allocateItemDao.selectAllotItemsByCondition(map);
	}

	@Override
	public List<Map<String, Object>> getAllotItemsOnCreateDelivery(Map<String, Object> map) throws Exception {
		return allocateItemDao.selectAllotItemsOnCreateDeliveryOnPaging(map);
	}

	@Override
	public Map<String, Object> saveDeliveryByAllotItemIds(BizAllocateDeliveryHead delivery, List<Integer> allotItemIds)
			throws Exception {

		Map<String, Object> returnValue = new HashMap<String, Object>();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("distributed", 10); // 配送
		map.put("status", EnumAllocateStatus.POSTED.getValue()); // 提交
		map.put("itemIds", allotItemIds);

		List<Map<String, Object>> findList = this.getAllotItemsOnCreateDelivery(map);

		if (findList != null && findList.size() > 0) {

			List<Object> allotIdList = new ArrayList<Object>();

			String werksString = "";
			String lgortString = "";
			// 判断发出工厂库存地点是否一致，不一致创建配送单
			for (int i = 0; i < findList.size(); i++) {
				Map<String, Object> allotItem = findList.get(i);
				if ("".equals(werksString) && "".equals(lgortString)) {
					werksString = allotItem.get("fty_output").toString();
					lgortString = allotItem.get("location_output").toString();
				} else if (!(werksString.equals(allotItem.get("fty_output").toString())
						&& lgortString.equals(allotItem.get("location_output").toString()))) {
					// throw new RuntimeException("发出工厂库存地点不一致");
					returnValue.put("errorCode", EnumErrorCode.ERROR_CODE_OUT_NOT_EQUAL_IN_LOCATION.getValue());
					returnValue.put("errorString", EnumErrorCode.ERROR_CODE_OUT_NOT_EQUAL_IN_LOCATION.getName());
					return returnValue;
				}
			}
			String deliveryCode = commonService.getNextReceiptCode("allocate_delivery");
			delivery.setAllocateDeliveryCode(deliveryCode);

			// 创建配送单抬头
			delivery.setFtyOutput(Integer.parseInt(werksString));
			delivery.setLocationOutput(Integer.parseInt(lgortString));
			delivery.setStatus((byte) 10);
			allocateDeliveryHeadDao.insertSelective(delivery);
			int allocateDeliveryId = delivery.getAllocateDeliveryId();
			for (int i = 0; i < findList.size(); i++) {
				Map<String, Object> allotItem = findList.get(i);
				BizAllocateDeliveryItem deliveryItem = new BizAllocateDeliveryItem();
				// 配送单ID()
				deliveryItem.setAllocateDeliveryId(allocateDeliveryId);
				deliveryItem.setAllocateDeliveryRid(i + 1);
				deliveryItem.setStatus((byte) 10);

				this.createDeliveryItemByAllotItem(allotItem, deliveryItem);
				allocateDeliveryItemDao.insertSelective(deliveryItem);

				BizAllocateItem allotItemUpdate = new BizAllocateItem();
				allotItemUpdate.setItemId(Integer.parseInt(allotItem.get("item_id").toString()));
				allotItemUpdate.setDeliveryItemId(deliveryItem.getItemId());
				// 调拨单待配送
				allotItemUpdate.setStatus((byte) 15);
				allocateItemDao.updateByPrimaryKeySelective(allotItemUpdate);

				Object objAllocateId = allotItem.get("allocate_id");
				if (!allotIdList.contains(objAllocateId)) {
					allotIdList.add(objAllocateId);

					BizAllocateHead allotUpdate = new BizAllocateHead();
					allotUpdate.setAllocateId(Integer.parseInt(objAllocateId.toString()));
					allotUpdate.setStatus((byte) 15);
					allocateHeadDao.updateByPrimaryKeySelective(allotUpdate);
				}

			}

			returnValue.put("errorCode", EnumErrorCode.ERROR_CODE_SUCESS.getValue());
			returnValue.put("errorString", EnumErrorCode.ERROR_CODE_SUCESS.getName());
			returnValue.put("allocateDeliveryId", allocateDeliveryId);
		} else {
			returnValue.put("errorCode", EnumErrorCode.ERROR_CODE_EMPTY_ITEM.getValue());
			returnValue.put("errorString", EnumErrorCode.ERROR_CODE_EMPTY_ITEM.getName());
		}

		return returnValue;
	}

	private void createDeliveryItemByAllotItem(Map<String, Object> allotItem, BizAllocateDeliveryItem deliveryItem) {

		deliveryItem.setAllocateItemId(Integer.parseInt(allotItem.get("item_id").toString()));
		deliveryItem.setMatId(Integer.parseInt(allotItem.get("mat_id").toString()));
		deliveryItem.setUnitId(Integer.parseInt(allotItem.get("unit_id").toString()));
		deliveryItem.setFtyInput(Integer.parseInt(allotItem.get("fty_input").toString()));
		deliveryItem.setLocationInput(Integer.parseInt(allotItem.get("location_input").toString()));
		deliveryItem.setAllocateQty(new BigDecimal(allotItem.get("allocate_qty").toString()));

	}

	@Override
	public BizAllocateDeliveryHead getDeliveryById(Integer allocateDeliveryId) throws Exception {
		return allocateDeliveryHeadDao.selectByPrimaryKey(allocateDeliveryId);
	}

	@Override
	public boolean deleteDeliveryById(Integer allocateDeliveryId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("allocateDeliveryId", allocateDeliveryId);

		List<Map<String, Object>> deliveryItems = allocateDeliveryItemDao.selectByCondition(map);
		// 修改调拨单
		Set<Object> allotIdSet = new HashSet<Object>();
		for (Map<String, Object> deliveryItem : deliveryItems) {

			int allot_item_id = Integer.parseInt(deliveryItem.get("allocate_item_id").toString());
			allotIdSet.add(deliveryItem.get("allocate_id"));

			BizAllocateItem allotItemUpdate = new BizAllocateItem();
			allotItemUpdate.setItemId(allot_item_id);
			allotItemUpdate.setDeliveryItemId(0);
			allotItemUpdate.setStatus((byte) 10);
			allocateItemDao.updateByPrimaryKeySelective(allotItemUpdate);
		}
		Iterator<Object> allotIdIterator = allotIdSet.iterator();
		while (allotIdIterator.hasNext()) {
			Object allotIdLong = allotIdIterator.next();
			Map<String, Object> mapAllotItems = new HashMap<String, Object>();
			mapAllotItems.put("status", 15);
			mapAllotItems.put("allocateId", allotIdLong);

			List<Map<String, Object>> allotItems = allocateItemDao.selectAllotItemsByCondition(mapAllotItems);

			if (allotItems == null || allotItems.size() == 0) {
				BizAllocateHead allotUpdate = new BizAllocateHead();
				allotUpdate.setAllocateId(Integer.parseInt(allotIdLong.toString()));
				allotUpdate.setStatus((byte) 10);
				allocateHeadDao.updateByPrimaryKeySelective(allotUpdate);

			}
		}

		// 逻辑删除
		BizAllocateDeliveryHead upDeliveryHead = new BizAllocateDeliveryHead();
		upDeliveryHead.setAllocateDeliveryId(allocateDeliveryId);
		upDeliveryHead.setIsDelete((byte) 1);
		allocateDeliveryHeadDao.updateByPrimaryKeySelective(upDeliveryHead);
		BizAllocateDeliveryItem upDeliveryItem = new BizAllocateDeliveryItem();
		upDeliveryItem.setAllocateDeliveryId(allocateDeliveryId);
		upDeliveryItem.setIsDelete((byte) 1);
		allocateDeliveryItemDao.updateByPrimaryKeySelective(upDeliveryItem);

		return true;
	}

	@Override
	public List<Map<String, Object>> getDeliverieListByCondition(Map<String, Object> map) throws Exception {
		return allocateDeliveryHeadDao.selectByConditionOnPagIng(map);
	}

	@Override
	public List<Map<String, Object>> getDeliveryItemsByConditions(Map<String, Object> map) throws Exception {
		return allocateDeliveryItemDao.selectByCondition(map);
	}

	@Override
	public List<String> getDeliveryCodeByAllocateId(Integer allocateId) throws Exception {

		return allocateHeadDao.getAllocateDeliveryCodeById(allocateId);
	}
}
