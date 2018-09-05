package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.inossem.wms.dao.batch.BatchMaterialMapper;
import com.inossem.wms.dao.biz.BizAllocateCargoHeadMapper;
import com.inossem.wms.dao.biz.BizBusinessHistoryMapper;
import com.inossem.wms.dao.biz.BizPkgOperationPositionMapper;
import com.inossem.wms.dao.biz.BizStockInputHeadMapper;
import com.inossem.wms.dao.biz.BizStockOutputHeadMapper;
import com.inossem.wms.dao.biz.BizStockOutputReturnHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskHeadCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskItemCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskItemMapper;
import com.inossem.wms.dao.biz.BizStockTaskPositionCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqItemMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqPositionMapper;
import com.inossem.wms.dao.biz.BizStockTransportItemCwMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicWarehouseAreaMapper;
import com.inossem.wms.dao.dic.DicWarehousePalletMapper;
import com.inossem.wms.dao.dic.DicWarehousePositionMapper;
import com.inossem.wms.dao.rel.RelTaskUserWarehouseMapper;
import com.inossem.wms.dao.stock.StockPositionHistoryMapper;
import com.inossem.wms.dao.stock.StockPositionMapper;
import com.inossem.wms.dao.stock.StockSnHistoryMapper;
import com.inossem.wms.dao.stock.StockSnMapper;
import com.inossem.wms.exception.InventoryException;
import com.inossem.wms.exception.SAPDebitCreditException;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.FreezeCheck;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.biz.BizAllocateCargoHead;
import com.inossem.wms.model.biz.BizBusinessHistory;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockOutputHead;
import com.inossem.wms.model.biz.BizStockOutputReturnHead;
import com.inossem.wms.model.biz.BizStockTaskHead;
import com.inossem.wms.model.biz.BizStockTaskHeadCw;
import com.inossem.wms.model.biz.BizStockTaskItem;
import com.inossem.wms.model.biz.BizStockTaskItemCw;
import com.inossem.wms.model.biz.BizStockTaskPositionCw;
import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.biz.BizStockTaskReqPosition;
import com.inossem.wms.model.biz.BizStockTransportItemCw;
import com.inossem.wms.model.dic.DicWarehouseArea;
import com.inossem.wms.model.dic.DicWarehousePallet;
import com.inossem.wms.model.dic.DicWarehousePosition;
import com.inossem.wms.model.enums.EnumAllocateCargoOperationType;
import com.inossem.wms.model.enums.EnumBusinessType;
import com.inossem.wms.model.enums.EnumDebitCredit;
import com.inossem.wms.model.enums.EnumFtyType;
import com.inossem.wms.model.enums.EnumIsUrgent;
import com.inossem.wms.model.enums.EnumMatStoreType;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumRequestSource;
import com.inossem.wms.model.enums.EnumSequence;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumStockType;
import com.inossem.wms.model.enums.EnumTaskReqShippingType;
import com.inossem.wms.model.enums.EnumUrgentSynStatus;
import com.inossem.wms.model.enums.EnumWorkModel;
import com.inossem.wms.model.key.DicWarehousePositionKey;
import com.inossem.wms.model.key.StockPositionKey;
import com.inossem.wms.model.rel.RelTaskUserWarehouse;
import com.inossem.wms.model.stock.StockOccupancy;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.stock.StockPositionHistory;
import com.inossem.wms.model.stock.StockSn;
import com.inossem.wms.model.stock.StockSnHistory;
import com.inossem.wms.model.vo.BizPkgOperationPositionVo;
import com.inossem.wms.model.vo.BizReceiptAttachmentVo;
import com.inossem.wms.model.vo.BizStockTaskHeadVo;
import com.inossem.wms.model.vo.BizStockTaskItemVo;
import com.inossem.wms.model.vo.BizStockTaskReqHeadVo;
import com.inossem.wms.model.vo.BizStockTaskReqItemVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.model.vo.StockPositionExpandVo;
import com.inossem.wms.model.vo.StockPositionVo;
import com.inossem.wms.model.vo.StockSnExpandVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IFileService;
import com.inossem.wms.service.biz.ITaskService;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilNumber;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilString;
import com.inossem.wms.util.UtilTwoDimensionCode;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service("taskServiceImpl")
public class TaskServiceImpl implements ITaskService {
	private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
	@Autowired
	private ICommonService commonService;

	@Autowired
	private BizStockTaskReqItemMapper bizStockTaskReqItemDao;

	@Autowired
	private BizStockTaskReqHeadMapper bizStockTaskReqHeadDao;

	@Autowired
	private BizStockTaskItemMapper bizStockTaskItemMapper;

	@Autowired
	private BizStockTaskHeadMapper bizStockTaskHeadDao;

	@Autowired
	private DicWarehouseAreaMapper dicWarehouseAreaDao;

	@Autowired
	private DicWarehousePositionMapper dicWarehousePositionDao;

	@Autowired
	private StockPositionMapper stockPositionMapper;

	@Autowired
	private StockPositionHistoryMapper stockPositionHistoryDao;

	@Autowired
	private StockSnMapper stockSnDao;

	@Autowired
	private StockSnHistoryMapper stockSnHistoryDao;

	@Autowired
	private SequenceDAO sequenceDao;

	@Autowired
	private IDictionaryService dictionaryService;

	@Autowired
	private BizStockTaskReqPositionMapper stockTaskReqPositionDao;

	@Autowired
	private BizStockTaskHeadCwMapper bizStockTaskHeadCwDao;

	@Autowired
	private BizStockTaskItemCwMapper bizStockTaskItemCwDao;

	@Autowired
	private BizStockTaskPositionCwMapper bizStockTaskPositionCwDao;

	@Autowired
	private BizStockInputHeadMapper stockInputHeadDao;

	@Autowired
	private BizPkgOperationPositionMapper pkgOperationPositionDao;

	@Autowired
	private DicWarehousePositionMapper warehousePositionDao;

	@Autowired
	private DicWarehousePalletMapper warehousePalletDao;

	@Autowired
	private BizStockTransportItemCwMapper stockTransportItemCwDao;

	@Autowired
	private BizStockTaskReqHeadMapper StockTaskReqHeadDao;

	@Autowired
	private BizStockTaskReqItemMapper StockTaskReqItemDao;

	@Autowired
	private BizStockTaskReqPositionMapper StockTaskReqPositionDao;

	@Autowired
	private BizStockOutputHeadMapper stockOutputHeadDao;

	@Autowired
	private BizAllocateCargoHeadMapper allocateCargoHeadDao;

	@Autowired
	private BizStockOutputReturnHeadMapper StockOutputReturnHeadDao;

	@Autowired
	private BizBusinessHistoryMapper businessHistoryDao;

	@Autowired
	private RelTaskUserWarehouseMapper RelTaskUserWarehouseDao;

	@Autowired
	private IFileService fileService;
	
	@Autowired
	private StockPositionMapper stockPositionDao;
	
	@Autowired
	private BizAllocateCargoHeadMapper AllocateCargoHeadDao;
	
	@Autowired
	private BatchMaterialMapper batchMaterialDao;

	@Override
	public List<BizStockTaskHeadVo> getBizStockTaskHeadList(BizStockTaskHeadVo paramVo) {

		return bizStockTaskHeadCwDao.getBizStockTaskHeadListOnPaging(paramVo);
	}

	@Override
	public List<DicWarehouseArea> selectByKey(Integer whId) {

		return dicWarehouseAreaDao.selectByKey(whId);
	}

	@Override
	public DicWarehousePosition selectDicWarehousePositionByKey(DicWarehousePositionKey key) {

		return dicWarehousePositionDao.selectDicWarehousePositionByKey(key);
	}

	@Override
	public String getHavenMaterial(StockPosition record) {

		return stockPositionMapper.getHavenMaterial(record);
	}

	/*
	 * @Override public int positionCleanupOld(BizStockTaskHead
	 * bizStockTaskHead, int whId, int sourceAreaId,int sourcePositionId,int
	 * targetAreaId,int targetPositionId) { Map<String, Object> map = new
	 * HashMap<String, Object>(); map.put("wh_id", whId); map.put("s_area_id",
	 * sourceAreaId); map.put("s_position_id", sourcePositionId);
	 * map.put("t_area_id", targetAreaId); map.put("t_position_id",
	 * targetPositionId);
	 * 
	 * long stockTaskCode = sequenceDao.selectNextVal("stock_task");
	 * 
	 * // 新建作业单 bizStockTaskHead.setStockTaskCode(stockTaskCode + "");
	 * bizStockTaskHeadDao.insertSelective(bizStockTaskHead);
	 * 
	 * int stockTaskId = bizStockTaskHead.getStockTaskId();// 自增的id
	 * 
	 * // 新建作业单明细 map.put("stockTaskId", String.valueOf(stockTaskId));
	 * bizStockTaskItemMapper.insertItemForPosition(map);
	 * 
	 * // 仓位调整 stockPositionMapper.updateStockPosition(map);
	 * stockPositionMapper.insertIgnoreStockPosition(map);
	 * stockPositionMapper.deleteStockPosition(map);
	 * 
	 * //添加审批人,先获得当前作业单 BizStockTaskHead currentBizStockTaskHead =
	 * bizStockTaskHeadDao.selectByPrimaryKey(stockTaskId); try {
	 * commonService.initDefaultWf(stockTaskId,
	 * currentBizStockTaskHead.getReceiptType(),
	 * currentBizStockTaskHead.getStockTaskCode(),
	 * currentBizStockTaskHead.getCreateUser()); } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * return 0; }
	 */

	@Override
	public List<StockPositionVo> getStockPositionList(int whId, String condition, String defaultArea, int pageIndex,
			int pageSize, int totalCount) {
		Map<String, Object> parapMap = new HashMap<String, Object>();
		parapMap.put("whId", whId);
		parapMap.put("condition", condition);
		parapMap.put("defaultArea", defaultArea);
		parapMap.put("paging", true);
		parapMap.put("pageIndex", pageIndex);
		parapMap.put("pageSize", pageSize);
		parapMap.put("totalCount", totalCount);

		return stockPositionMapper.getStockPositionListOnPaging(parapMap);
	}

	// @Override
	// public int materialCleanupOld(BizStockTaskHead bizStockTaskHead,
	// List<BizStockTaskItem> list) {
	// StockPositionVo stockPositionVo = new StockPositionVo();
	//
	// StringBuffer sqlSB = new StringBuffer();
	// StringBuffer conditionSB = new StringBuffer();
	// StringBuffer conditionGroupSB = new StringBuffer();
	// // 判断库存是否足够，即调整的物料数量不大于库存数量，并且该物料存在
	// if (list == null || list.size() == 0) {
	// throw new NullPointerException();
	// } else {
	// BizStockTaskItem first = list.get(0);
	// conditionSB.append(" SELECT
	// ").append(first.getStockPositionId()).append(" stockPositionId0,")
	// .append(first.getQty().toString()).append("
	// qty0,'").append(first.getWhId()).append("' whId0,'")
	// .append(first.getTargetAreaId()).append("'
	// targetAreaId0,'").append(first.getTargetPositionId())
	// .append("' targetPositionId0");
	//
	// for (int i = 1; i < list.size(); i++) {
	// BizStockTaskItem tmp = list.get(i);
	// conditionSB.append(" UNION ALL SELECT
	// ").append(tmp.getStockPositionId()).append(",")
	// .append(tmp.getQty().toString()).append(",'").append(tmp.getWhId()).append("','")
	// .append(tmp.getTargetAreaId()).append("','").append(tmp.getTargetPositionId()).append("'");
	// }
	//
	// conditionGroupSB.append("SELECT stockPositionId0,SUM(qty0) qty0,count(*)
	// cnt FROM (").append(conditionSB)
	// .append(") x GROUP BY stockPositionId0");
	//
	// sqlSB.append("SELECT SUM(cnt) cnt FROM stock_position l INNER JOIN (");
	// sqlSB.append(conditionGroupSB);
	// sqlSB.append(") t ON l.id = t.stockPositionId0 AND l.qty >= t.qty0");
	//
	// }
	//
	// stockPositionVo.setSql(sqlSB.toString());
	// int cnt = stockPositionMapper.selectCntForSLZG(stockPositionVo);
	// // 判断是否足够
	// if (cnt < list.size()) {
	// throw new InventoryException();
	// }
	//
	// // 清空sql
	// sqlSB.setLength(0);
	// // 调整目标库存数量
	// sqlSB.append(
	// " INSERT INTO stock_position(pallet_id,package_id, wh_id, area_id,
	// position_id, mat_id, fty_id, location_id, batch, spec_stock,
	// spec_stock_code, spec_stock_name, input_date, take_delivery_date,
	// unit_id, qty, unit_weight)");
	// sqlSB.append(
	// " SELECT pallet_id,package_id, wh_id, targetAreaId0, targetPositionId0,
	// mat_id, fty_id, location_id, batch,spec_stock, spec_stock_code,
	// spec_stock_name, input_date, take_delivery_date, unit_id, qty0,
	// unit_weight");
	//
	// sqlSB.append(
	// " FROM (SELECT l.pallet_id,l.package_id, l.wh_id, t.targetAreaId0,
	// t.targetPositionId0, l.mat_id, l.fty_id, l.location_id, l.batch,
	// l.spec_stock, l.spec_stock_code, l.spec_stock_name, l.input_date,
	// l.take_delivery_date, l.unit_id, t.qty0, l.unit_weight");
	// sqlSB.append(" FROM stock_position l INNER JOIN (");
	// sqlSB.append(conditionSB);
	// sqlSB.append(") t ON l.id = t.stockPositionId0");
	// sqlSB.append(") a ON DUPLICATE KEY UPDATE qty = qty + VALUES(qty)");
	// stockPositionVo.setSql(sqlSB.toString());
	// stockPositionMapper.dml(stockPositionVo);
	//
	// // 清空sql
	// sqlSB.setLength(0);
	// // 调整源仓位库存数量
	// sqlSB.append("UPDATE stock_position l INNER JOIN (");
	// sqlSB.append(conditionGroupSB);
	// sqlSB.append(") t ON l.id = t.stockPositionId0 SET l.qty = l.qty -
	// t.qty0");
	//
	// stockPositionVo.setSql(sqlSB.toString());
	// stockPositionMapper.dml(stockPositionVo);
	//
	// // 清空sql
	// sqlSB.setLength(0);
	// // 如果源仓位库存数量为0，则删除
	// sqlSB.append("DELETE l FROM stock_position l INNER JOIN (");
	// sqlSB.append(conditionGroupSB);
	// sqlSB.append(") t ON l.id = t.stockPositionId0 AND l.qty = 0 ");
	// stockPositionVo.setSql(sqlSB.toString());
	// stockPositionMapper.dml(stockPositionVo);
	//
	// long stockTaskCode = sequenceDao.selectNextVal("stock_task");
	// // 创建作业单抬头
	// bizStockTaskHead.setStockTaskCode(stockTaskCode + "");
	// bizStockTaskHeadDao.insertSelective(bizStockTaskHead);
	//
	// int stockTaskId = bizStockTaskHead.getStockTaskId();// 自增的id
	//
	// int i = 1;
	// for (BizStockTaskItem bizStockTaskItem : list) {
	// // 创建作业单明细
	// bizStockTaskItem.setStockTaskId(stockTaskId);
	// bizStockTaskItem.setStockTaskRid(i);
	// i++;
	// bizStockTaskItemMapper.insertSelective(bizStockTaskItem);
	// }
	//
	// BizStockTaskHead currentBizStockTaskHead =
	// bizStockTaskHeadDao.selectByPrimaryKey(stockTaskId);
	// try {
	// commonService.initRoleWf(stockTaskId,
	// currentBizStockTaskHead.getReceiptType(),
	// currentBizStockTaskHead.getStockTaskCode(),
	// currentBizStockTaskHead.getCreateUser());
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return 0;
	// }

	@Override
	public BizStockTaskHeadCw getBizStockTaskHeadByPrimaryKey(Integer stockTaskId) {

		return bizStockTaskHeadCwDao.selectByPrimaryKey(stockTaskId);
	}

	@Override
	public List<BizStockTaskItemVo> getItemsByStockTaskId(BizStockTaskItemCw bizStockTaskItem) {

		return bizStockTaskItemCwDao.getItemsByStockTaskId(bizStockTaskItem);
	}

	@Override
	public List<BizStockTaskReqHeadVo> getBizStockTaskReqHeadList(BizStockTaskReqHeadVo paramVo) {

		return bizStockTaskReqHeadDao.getBizStockTaskReqHeadListOnPaging(paramVo);
	}

	@Override
	public List<BizStockTaskReqItemVo> getReqItemsByTaskReqId(Integer stockTaskReqId) {

		return bizStockTaskReqItemDao.getReqItemsByTaskReqId(stockTaskReqId);
	}

	// @Override
	// public List<BizStockTaskHeadVo>
	// getBizStockTaskHeadListInReq(BizStockTaskHeadVo paramVo) {
	//
	// return bizStockTaskHeadDao.getBizStockTaskHeadListInReq(paramVo);
	// }

	// 川维
	@Override
	public List<BizStockTaskHeadVo> getBizStockTaskHeadListInReq(BizStockTaskHeadVo paramVo) {

		return bizStockTaskHeadCwDao.getBizStockTaskHeadListInReq(paramVo);
	}

	@Override
	public HashMap<String, Object> addBizStockTaskReqHead(JSONObject bizStockTaskReqHead) {
		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		String userId = bizStockTaskReqHead.getString("user_id");
		int stockTaskReqId = bizStockTaskReqHead.getInt("stock_task_req_id");// 作业申请号
		JSONArray bizStockTaskReqItemAry = bizStockTaskReqHead.getJSONArray("item_list");

		// BizStockTaskHead bizStockTaskHead = new BizStockTaskHead();
		List<BizStockTaskItem> bizStockTaskItemList = new ArrayList<BizStockTaskItem>();

		// 得到作业申请单
		BizStockTaskReqHead findBizStockTaskReqHead = bizStockTaskReqHeadDao.selectByPrimaryKey(stockTaskReqId);

		// 得到作业申请单明细
		// BizStockTaskReqItem findBizStockTaskReqItem = new
		// BizStockTaskReqItem();
		// findBizStockTaskReqItem.setStockTaskReqId(stockTaskReqId);
		List<BizStockTaskReqItemVo> bizStockTaskReqItemList = bizStockTaskReqItemDao
				.getReqItemsByTaskReqId(stockTaskReqId);
		ArrayList<BizStockTaskReqItem> updateBizStockTaskReqItemList = new ArrayList<BizStockTaskReqItem>();

		try {

			if (findBizStockTaskReqHead != null && bizStockTaskReqItemAry != null
					&& !bizStockTaskReqItemAry.isEmpty()) {

				List<Map<String, String>> checkMaps = new ArrayList<Map<String, String>>();

				for (int i = 0; i < bizStockTaskReqItemAry.size(); i++) {
					JSONObject bizStockTaskReqItem = bizStockTaskReqItemAry.getJSONObject(i);
					JSONArray positionAry = bizStockTaskReqItem.getJSONArray("position_list");
					if (positionAry != null && !positionAry.isEmpty()) {
						for (int j = 0; j < positionAry.size(); j++) {
							// position:-- cunchuqu: "001" mubiaocangwei:
							// "01-0005" shangjiashuliang: "1"
							JSONObject position = positionAry.getJSONObject(j);
							// DicWarehousePosition findPosition = new
							// DicWarehousePosition();
							// DicWarehousePosition paramPosition = new
							// DicWarehousePosition();
							// paramPosition.setAreaId(position.getInt("area_id"));
							// paramPosition.setPositionCode(position.getString("position_code"));
							// findPosition =
							// dicWarehousePositionDao.selectDicWarehousePositionByPositionIndex(paramPosition);

							HashMap<String, String> checkMap = new HashMap<String, String>();
							checkMap.put("position_id", position.get("position_id") + "");
							checkMaps.add(checkMap);
						}

					}

				}
				FreezeCheck fCheck = commonService.checkWhPosAndStockLoc(checkMaps);

				// 新建作业单抬头 BizStockTaskHead
				BizStockTaskHead bizStockTaskHead = new BizStockTaskHead();
				if (fCheck != null && !fCheck.isLockedInput()) {
					long stockTaskCode = sequenceDao.selectNextVal("stock_task");
					bizStockTaskHead.setStockTaskCode(stockTaskCode + "");
					bizStockTaskHead.setCreateUser(userId);
					bizStockTaskHead.setStatus((byte) 1);

					// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd
					// hh:mm:ss");
					// String dateStr = sdf.format(new Date());
					// String submitDate = dateStr.substring(0, 10);
					// java.util.Date date=sdf.parse(dateStr);

					int whId = findBizStockTaskReqHead.getWhId();
					bizStockTaskHead.setSubmitDate(new Date());
					bizStockTaskHead.setStockTaskReqId(stockTaskReqId);
					bizStockTaskHead.setFtyId(findBizStockTaskReqHead.getFtyId());
					bizStockTaskHead.setLocationId(findBizStockTaskReqHead.getLocationId());
					bizStockTaskHead.setWhId(whId);
					bizStockTaskHead.setShippingType("E");
					bizStockTaskHead.setReceiptType(EnumReceiptType.STOCK_TASK_LISTING.getValue());// 上架
					bizStockTaskHead.setMoveTypeId(0);

					// if (bizStockTaskReqHead.containsKey("request_source")) {
					// Integer request_sourceInteger =
					// bizStockTaskReqHead.getInt("request_source");
					// bizStockTaskHead.setRequestSource(request_sourceInteger.byteValue());//0
					// web 1 pda
					// }

					bizStockTaskHead.setRequestSource((byte) 1);// 0 web 1 pda
					bizStockTaskHeadDao.insertSelective(bizStockTaskHead);
					int stockTaskId = bizStockTaskHead.getStockTaskId();// 自增的id

					// 新建作业单明细 BizStockTaskItem
					int bizStockTaskItemCount = 1;
					for (int i = 0; i < bizStockTaskReqItemAry.size(); i++) {
						JSONObject bizStockTaskReqItemObj = bizStockTaskReqItemAry.getJSONObject(i);
						String stockTaskReqRid = bizStockTaskReqItemObj.getString("stock_task_req_rid");// 作业申请单行号
						BizStockTaskReqItem innerBizStockTaskReqItem = bizStockTaskReqItemList.stream()
								.filter(mseg -> Integer.parseInt(stockTaskReqRid) == mseg.getStockTaskReqRid())
								.findFirst().get();
						BigDecimal menge = new BigDecimal(0);// 上架数量
						JSONArray positionAry = bizStockTaskReqItemObj.getJSONArray("position_list");
						if (positionAry != null && !positionAry.isEmpty()) {
							for (int j = 0; j < positionAry.size(); j++) {
								JSONObject position = positionAry.getJSONObject(j);
								BizStockTaskItem bizStockTaskItem = new BizStockTaskItem();
								int areaId = position.getInt("area_id");

								Integer positioId = Integer.parseInt(position.getString("position_id"));
								DicWarehousePosition WarehousePosition = dicWarehousePositionDao
										.selectByPrimaryKey(positioId);

								String positionCode = WarehousePosition.getPositionCode();
								String submitMenge = position.getString("submit_menge");

								DicWarehousePositionKey key = new DicWarehousePositionKey();
								key.setAreaId(areaId);
								key.setWhId(whId);
								key.setPositionCode(positionCode);

								if (StringUtils.hasText(submitMenge)
										&& new BigDecimal(submitMenge).compareTo(BigDecimal.ZERO) == 1) {
									DicWarehousePosition dicWarehousePosition = dicWarehousePositionDao
											.selectDicWarehousePositionByKey(key);
									if (dicWarehousePosition == null) {
										throw new WMSException(positionCode + "仓位不存在");
									}
									menge = menge.add(new BigDecimal(submitMenge));

									bizStockTaskItem.setWhId(whId);
									bizStockTaskItem.setBatch(innerBizStockTaskReqItem.getBatch());
									bizStockTaskItem.setLocationId(innerBizStockTaskReqItem.getLocationId());
									bizStockTaskItem.setMatId(innerBizStockTaskReqItem.getMatId());
									bizStockTaskItem.setMatDocId(innerBizStockTaskReqItem.getMatDocId());
									bizStockTaskItem.setMatDocRid(innerBizStockTaskReqItem.getMatDocRid());
									bizStockTaskItem.setUnitId(innerBizStockTaskReqItem.getUnitId());
									bizStockTaskItem.setTargetAreaId(areaId);
									bizStockTaskItem.setTargetPositionId(dicWarehousePosition.getPositionId());

									bizStockTaskItem.setSpecStock(innerBizStockTaskReqItem.getSpecStock());
									bizStockTaskItem.setSpecStockCode(innerBizStockTaskReqItem.getSpecStockCode());
									bizStockTaskItem.setStockTaskId(stockTaskId);
									bizStockTaskItem.setStockTaskRid(bizStockTaskItemCount);
									bizStockTaskItemCount++;
									bizStockTaskItem.setStockTaskReqId(innerBizStockTaskReqItem.getStockTaskReqId());
									bizStockTaskItem
											.setStockListingReqRid(innerBizStockTaskReqItem.getStockTaskReqRid());

									String whCode = dictionaryService.getWhCodeByWhId(whId);
									Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
											UtilProperties.getInstance().getDefaultCCQ());
									Integer sourcePositionId = dictionaryService
											.getPositionIdByWhCodeAreaCodePositionCode(whCode,
													UtilProperties.getInstance().getDefaultCCQ(),
													UtilProperties.getInstance().getDefaultCW());
									bizStockTaskItem.setSourceAreaId(sourceAreaId); // 源发地仓储区
									bizStockTaskItem.setSourcePositionId(sourcePositionId); // 源发地仓储位
									bizStockTaskItem.setQty(new BigDecimal(submitMenge));
									bizStockTaskItem.setFtyId(innerBizStockTaskReqItem.getFtyId());
									bizStockTaskItemList.add(bizStockTaskItem);
								}
							}
						}
						BigDecimal stockTaskQty = innerBizStockTaskReqItem.getStockTaskQty();
						if (stockTaskQty == null) {
							stockTaskQty = new BigDecimal(0);
						}

						if (innerBizStockTaskReqItem.getQty().compareTo(menge.add(stockTaskQty)) == -1) {
							// 上架数量+已上架数量》总数量
							throw new WMSException("上架数量+已上架数量>总数量 ");
						} else {
							// ------------更新BizStockTaskReqItem 上架数量
							BigDecimal allStockTaskQty = menge.add(stockTaskQty);
							BizStockTaskReqItem updateBizStockTaskReqItem = new BizStockTaskReqItem();
							updateBizStockTaskReqItem.setStockTaskReqId(stockTaskReqId);
							updateBizStockTaskReqItem.setStockTaskReqRid(Integer.parseInt(stockTaskReqRid));
							updateBizStockTaskReqItem.setStockTaskQty(allStockTaskQty);

							if (allStockTaskQty.compareTo(innerBizStockTaskReqItem.getQty()) == 0) {
								updateBizStockTaskReqItem.setStatus((byte) 1);// 已经完成
							}
							updateBizStockTaskReqItemList.add(updateBizStockTaskReqItem);
						}

					}
					List<StockSnExpandVo> snList = new ArrayList<StockSnExpandVo>();
					for (BizStockTaskItem item : bizStockTaskItemList) {
						StockSnExpandVo sn = new StockSnExpandVo();
						sn.setTargetAreaId(item.getTargetAreaId());
						sn.setTargetPositionId(item.getTargetPositionId());
						sn.setTargetPackageId(0);
						sn.setTargetPalletId(0);
						StockPosition record = new StockPosition();
						record.setPositionId(item.getSourcePositionId());
						record.setAreaId(item.getSourceAreaId());
						record.setBatch(item.getBatch());
						record.setMatId(item.getMatId());
						record.setLocationId(item.getLocationId());
						record.setFtyId(item.getFtyId());
						StockPosition find = stockPositionMapper.selectStockObjectByParam(record);
						sn.setStockId(find.getId());
						sn.setQty(item.getQty());
						sn.setSnId(0);
						sn.setMatId(item.getMatId());
						snList.add(sn);
					}
					JSONObject result = this.stockMoveForSn(snList);
					if (result.getInt("is_success") != 0) {
						throw new WMSException(result.getString("message"));
					}
					for (BizStockTaskItem bizStockTaskItem : bizStockTaskItemList) {
						// 创建作业单明细
						bizStockTaskItemMapper.insertSelective(bizStockTaskItem);
					}
					// shelve(bizStockTaskHead, bizStockTaskItemList);
					// 判断是否全部完成

					for (BizStockTaskReqItem updateBizStockTaskReqItem : updateBizStockTaskReqItemList) {
						bizStockTaskReqItemDao.updateItemQty(updateBizStockTaskReqItem);
					}
					Byte isComplete = bizStockTaskReqItemDao.selectMinStatus(stockTaskReqId);
					if (isComplete != null && isComplete.compareTo(EnumReceiptStatus.RECEIPT_FINISH.getValue()) == 0) {
						// 作业请求单已完成
						BizStockTaskReqHead updateBizStockTaskReqHead = new BizStockTaskReqHead();
						updateBizStockTaskReqHead.setStockTaskReqId(stockTaskReqId);
						updateBizStockTaskReqHead.setStatus((byte) 1);
						bizStockTaskReqHeadDao.updateByPrimaryKeySelective(updateBizStockTaskReqHead);
					}
					returnMap.put("code", "1");
					returnMap.put("msg", "");

					commonService.initRoleWf(stockTaskId, bizStockTaskHead.getReceiptType(),
							bizStockTaskHead.getStockTaskCode(), bizStockTaskHead.getCreateUser());

				} else {
					returnMap.put("code", "0");
					returnMap.put("msg", fCheck.getResultMsgInput());
				}

			} else {
				returnMap.put("code", "0");
				returnMap.put("msg", "数据异常");
			}
		} catch (InventoryException e) {
			logger.error("上架", e);
			returnMap.put("code", "0");
			returnMap.put("msg", "库存数量不足");
		} catch (WMSException e) {
			logger.error("上架", e);
			returnMap.put("code", "0");
			returnMap.put("msg", e.getMessage());
		} catch (Exception e) {
			logger.error("上架", e);
			returnMap.put("code", "0");
			returnMap.put("msg", "程序异常");
		}
		return returnMap;
	}

	@Override
	public int shelve(BizStockTaskHead bizStockTaskHead, List<BizStockTaskItem> bizStockTaskItemList) {

		StockPositionVo stockPositionVo = new StockPositionVo();

		StringBuffer sqlSB = new StringBuffer();
		StringBuffer conditionSB = new StringBuffer();
		StringBuffer conditionGroupSB = new StringBuffer();
		int defaultAreaId;
		int defaultPositionId;
		// 判断库存是否足够，即调整的物料数量不大于库存数量，并且该物料存在
		if (bizStockTaskItemList == null || bizStockTaskItemList.size() == 0) {
			throw new NullPointerException();
		} else {
			BizStockTaskItem first = bizStockTaskItemList.get(0);
			conditionSB.append(" SELECT '").append(first.getTargetAreaId()).append("' targetAreaId0,'")
					.append(first.getTargetPositionId()).append("' targetPositionId0,'").append(first.getMatId())
					.append("' mat_id,'").append(first.getBatch()).append("' batch,").append(first.getQty().toString())
					.append(" qty0");

			for (int i = 1; i < bizStockTaskItemList.size(); i++) {
				BizStockTaskItem tmp = bizStockTaskItemList.get(i);
				conditionSB.append(" UNION ALL SELECT '").append(tmp.getTargetAreaId()).append("','")
						.append(tmp.getTargetPositionId()).append("','").append(tmp.getMatId()).append("','")
						.append(tmp.getBatch()).append("',").append(tmp.getQty().toString());
			}

			conditionGroupSB.append("SELECT mat_id,batch,SUM(qty0) qty,count(*) cnt FROM (").append(conditionSB)
					.append(") x GROUP BY mat_id,batch");
			String defaultAreaCode = UtilProperties.getInstance().getDefaultCCQ();
			String whCode = dictionaryService.getWhCodeByWhId(bizStockTaskHead.getWhId());
			defaultAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, defaultAreaCode);
			String defaultPositionCode = UtilProperties.getInstance().getDefaultCW();
			defaultPositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode, defaultAreaCode,
					defaultPositionCode);
			sqlSB.append(" SELECT IFNULL(SUM(cnt),0) cnt").append(" FROM (").append(conditionGroupSB)
					.append(") t INNER JOIN stock_position l").append(" ON l.wh_id = '")
					.append(bizStockTaskHead.getWhId()).append("' AND l.area_id = '").append(defaultAreaId)
					.append("' AND l.position_id = '").append(defaultPositionId).append("' AND t.mat_id = l.mat_id ")
					.append("AND l.fty_id = '").append(bizStockTaskHead.getFtyId()).append("' AND l.location_id = '")
					.append(bizStockTaskHead.getLocationId()).append("' AND t.batch = l.batch AND l.qty >= t.qty;");
		}
		stockPositionVo.setSql(sqlSB.toString());
		int cnt = stockPositionMapper.selectCntForSLZG(stockPositionVo);
		// 判断是否足够
		if (cnt < bizStockTaskItemList.size()) {
			throw new InventoryException();
		}

		// 清空sql
		sqlSB.setLength(0);
		// 调整目标库存数量
		sqlSB.append(
				" INSERT INTO stock_position(pallet_id,package_id,wh_id, area_id, position_id, mat_id, fty_id, location_id, batch, spec_stock, spec_stock_code, spec_stock_name, input_date, take_delivery_date, unit_id, qty, unit_weight)");
		sqlSB.append(
				" SELECT pallet_id,package_id,wh_id, targetAreaId0, targetPositionId0, mat_id, fty_id, location_id, batch,spec_stock, spec_stock_code, spec_stock_name, input_date, take_delivery_date, unit_id, qty0,  unit_weight");

		sqlSB.append(
				" FROM (SELECT l.pallet_id,l.package_id,l.wh_id, t.targetAreaId0, t.targetPositionId0, l.mat_id, l.fty_id, l.location_id, l.batch, l.spec_stock, l.spec_stock_code, l.spec_stock_name, l.input_date, l.take_delivery_date, l.unit_id, t.qty0, l.unit_weight");
		sqlSB.append(" FROM stock_position l INNER JOIN (");
		sqlSB.append(conditionSB);
		sqlSB.append(") t ON l.wh_id = '").append(bizStockTaskHead.getWhId()).append("' AND l.area_id = '")
				.append(defaultAreaId).append("' AND l.position_id = '").append(defaultPositionId)
				.append("' AND t.mat_id = l.mat_id ").append("AND l.fty_id = '").append(bizStockTaskHead.getFtyId())
				.append("' AND l.location_id = '").append(bizStockTaskHead.getLocationId())
				.append("' AND t.batch = l.batch");
		sqlSB.append(") a ON DUPLICATE KEY UPDATE qty = qty + VALUES(qty)");

		stockPositionVo.setSql(sqlSB.toString());
		stockPositionMapper.dml(stockPositionVo);

		// 清空sql
		sqlSB.setLength(0);
		// 调整源仓位库存数量
		sqlSB.append("UPDATE stock_position l INNER JOIN (");
		sqlSB.append(conditionGroupSB);
		sqlSB.append(") t ON l.wh_id = '").append(bizStockTaskHead.getWhId()).append("' AND l.area_id = '")
				.append(defaultAreaId).append("' AND l.position_id = '").append(defaultPositionId)
				.append("' AND t.mat_id = l.mat_id ").append("AND l.fty_id = '").append(bizStockTaskHead.getFtyId())
				.append("' AND l.location_id = '").append(bizStockTaskHead.getLocationId())
				.append("' AND t.batch = l.batch SET l.qty = l.qty - t.qty");

		stockPositionVo.setSql(sqlSB.toString());
		stockPositionMapper.dml(stockPositionVo);
		// 清空sql
		sqlSB.setLength(0);
		// 如果源仓位库存数量为0，则删除
		sqlSB.append("DELETE l FROM stock_position l INNER JOIN (");
		sqlSB.append(conditionGroupSB);
		sqlSB.append(") t ON l.wh_id = '").append(bizStockTaskHead.getWhId()).append("' AND l.area_id = '")
				.append(defaultAreaId).append("' AND l.position_id = '").append(defaultPositionId)
				.append("' AND t.mat_id = l.mat_id ").append("AND l.fty_id = '").append(bizStockTaskHead.getFtyId())
				.append("' AND l.location_id = '").append(bizStockTaskHead.getLocationId())
				.append("' AND t.batch = l.batch AND l.qty = 0 ");
		stockPositionVo.setSql(sqlSB.toString());
		stockPositionMapper.dml(stockPositionVo);

		for (BizStockTaskItem bizStockTaskItem : bizStockTaskItemList) {
			// 创建作业单明细
			bizStockTaskItemMapper.insertSelective(bizStockTaskItem);
		}

		return 0;

	}

	@Override
	public List<StockPositionVo> getMaterialInPosition(StockPosition record) {

		return stockPositionMapper.selectMaterialInPosition(record);
	}

	@Override
	public String getReferReceiptCodeInReq(Integer stockTaskId) {

		return bizStockTaskHeadDao.getReferReceiptCodeInReq(stockTaskId);
	}

	@Override
	public BizStockTaskReqHead getReqHeadByPrimaryKey(Integer stockTaskReqId) {
		// TODO Auto-generated method stub
		return bizStockTaskReqHeadDao.selectByPrimaryKey(stockTaskReqId);
	}

	// 仓位移动
	// 仓位库存移动时,移动这个仓位库存的所有数量
	@Override
	public JSONObject stockMoveForPosition(List<StockPositionExpandVo> spList) throws Exception {
		JSONObject body = new JSONObject();
		if (spList == null || spList.size() < 1) {
			body.put("is_success", 1);
			body.put("message", "传入值为空");
			return body;
		}
		for (StockPositionExpandVo evo : spList) {
			// 判断传进来的值有没有areaId
			if (evo.getTargetAreaId() <= 0) {
				// DicWarehousePosition dwp =
				// dicWarehousePositionMapper.selectByPrimaryKey(evo.getTargetPositionId());
				DicWarehousePosition dwp = dictionaryService.getPositionByPositionId(evo.getTargetPositionId());
				evo.setTargetAreaId(dwp.getAreaId());
			}

			// 源仓位库存
			StockPosition sourceSp = stockPositionMapper.selectBySourceUniqueKey(evo);
			// 源sn库存
			List<StockSn> sourceSnList = stockSnDao.queryStockSnListByStockId(sourceSp.getId());

			// 仓位库存历史
			StockPositionHistory history = new StockPositionHistory(sourceSp);
			stockPositionHistoryDao.insertSelective(history);
			if (sourceSnList != null && sourceSnList.size() > 0) {
				for (StockSn sn : sourceSnList) {
					// sn库存历史
					StockSnHistory snHistory = new StockSnHistory(sn);
					snHistory.setId(0);
					stockSnHistoryDao.insertSelective(snHistory);
				}
			}

			// 获取目标仓位中是否有数据
			StockPosition targetSp = stockPositionMapper.selectByTargetUniqueKey(evo);
			if (targetSp == null) {
				// 目标没有数据则直接插入
				stockPositionMapper.insertTargetSelective(evo);

				// 同时更新sn库存
				if (sourceSnList != null && sourceSnList.size() > 0) {
					for (StockSn sn : sourceSnList) {
						sn.setStockId(evo.getId());
					}
				}
				stockSnDao.batchUpdateSnByPrimaryKey(sourceSnList);

			} else {
				// 目标仓位有数据 则更新数量
				BigDecimal sum = targetSp.getQty().add(evo.getQty());
				targetSp.setQty(sum);
				stockPositionMapper.updateByStockQty(targetSp);

				Map<String, StockSn> sourceMap = new HashMap<String, StockSn>();
				if (sourceSnList == null || sourceSnList.size() < 1) {
					// body.put("is_success", 1);
					// body.put("message", "源仓位数据为空");
					// return body;
					throw new InventoryException("源仓位sn库存为空");
				}

				for (StockSn ssn : sourceSnList) {
					sourceMap.put(ssn.getSnId() + "", ssn);
				}

				List<StockSn> targetSnList = stockSnDao.queryStockSnListByStockId(targetSp.getId());

				Map<String, StockSn> targetMap = new HashMap<String, StockSn>();
				if (targetSnList == null || targetSnList.size() < 1) {
					// body.put("is_success", 1);
					// body.put("message", "目标仓位数据为空");
					// return body;
					throw new InventoryException("目标仓位sn库存为空");
				}
				for (StockSn tsn : targetSnList) {
					targetMap.put(tsn.getSnId() + "", tsn);
				}

				List<StockSn> updateQtyList = new ArrayList<StockSn>();
				List<StockSn> updateStockIdList = new ArrayList<StockSn>();
				// List<StockSn> deleteSnList = new ArrayList<StockSn>();
				for (String key : sourceMap.keySet()) {
					if (targetMap.containsKey(key)) {
						BigDecimal targetSum = targetMap.get(key).getQty().add(sourceMap.get(key).getQty());
						targetMap.get(key).setQty(targetSum);
						updateQtyList.add(targetMap.get(key));
						// deleteSnList.add(sourceMap.get(key));
					} else {
						sourceMap.get(key).setStockId(targetSp.getId());
						updateStockIdList.add(sourceMap.get(key));
					}
				}

				if (updateStockIdList.size() > 0) {
					stockSnDao.batchUpdateSnByPrimaryKey(updateStockIdList);
				}

				// 目标仓位已经存在的话，sn库存 把行号和物料相同的 更新最新的数量
				if (updateQtyList.size() > 0) {
					stockSnDao.batchUpdateSnQtyByPrimaryKey(updateQtyList);
				}

				stockSnDao.deleteBySourcePrimaryKey(sourceSp.getId());
			}
			stockPositionMapper.deleteBySourceUniqueKey(evo);

		}

		body.put("is_success", 0);
		body.put("message", "操作成功");

		return body;
	}

	// SN移动
	// 移动SN库存,可以选择移动数量
	@Override
	public JSONObject stockMoveForSn(List<StockSnExpandVo> snList) throws Exception {
		JSONObject body = new JSONObject();
		if (snList == null || snList.size() < 1) {
			body.put("is_success", 1);
			body.put("message", "传入值为空");
			return body;
		}

		// 封装 需要新增的 StockPosition，更新的StockPosition ，新增的SN，更新的SN
		Set<StockPosition> insertStockPositionList = new HashSet<StockPosition>();
		Set<StockPosition> updateStockPositionList = new HashSet<StockPosition>();
		List<StockSnExpandVo> insertStockSnList = new ArrayList<StockSnExpandVo>();
		List<StockSnExpandVo> updateStockSnList = new ArrayList<StockSnExpandVo>();

		for (StockSnExpandVo sn : snList) {
			// 如果目标areaId小于等于0 则根据positionid获取areaid
			if (sn.getTargetAreaId() <= 0) {
				// DicWarehousePosition dwp =
				// dicWarehousePositionMapper.selectByPrimaryKey(sn.getTargetPositionId());
				DicWarehousePosition dwp = dictionaryService.getPositionByPositionId(sn.getTargetPositionId());
				if (dwp == null) {
					// body.put("is_success", 1);
					// body.put("message", "传入的仓位不存在");
					// return body;
					throw new InventoryException("传入的仓位不存在");
				}
				sn.setTargetAreaId(dwp.getAreaId());
			}

			// 查询stockPosition的源数据
			StockPosition sourceStockPosition = stockPositionMapper.selectByPrimaryKey(sn.getStockId());

			if (sourceStockPosition == null) {
				// body.put("is_success", 1);
				// body.put("message", "原StockPosition为空");
				// return body;
				throw new InventoryException("原仓位库存不存在");
			}

			// 仓位库存历史
			StockPositionHistory history = new StockPositionHistory(sourceStockPosition);
			history.setQty(sn.getQty());
			stockPositionHistoryDao.insertSelective(history);
			// sn库存历史
			StockSnHistory snHistory = new StockSnHistory(sn);
			snHistory.setId(0);
			stockSnHistoryDao.insertSelective(snHistory);

			sourceStockPosition.setPositionId(sn.getTargetPositionId());
			sourceStockPosition.setPalletId(sn.getTargetPalletId());
			sourceStockPosition.setPackageId(sn.getTargetPackageId());
			sourceStockPosition.setAreaId(sn.getTargetAreaId());
			// 根据源仓位的三个主键 再加上 传进来的 三个目标 仓位 托盘 和 包 来查询目标仓位 是否存在
			StockPosition targetSp = stockPositionMapper.selectBySourceStockUniqueKey(sourceStockPosition);

			if (targetSp == null) {
				// 如果目标仓位为空，封装插入的 对象到 插入的 list
				insertStockPositionList.add(sourceStockPosition);
				insertStockSnList.add(sn);
			} else {
				// 如果目标仓位不为空 封装更新数据到 更新的list
				updateStockPositionList.add(targetSp);
				updateStockSnList.add(sn);
			}

		}

		// 处理新增数据
		if (insertStockPositionList.size() > 0) {
			for (StockPosition sp : insertStockPositionList) {
				// 封装要插入的SNlist
				List<StockSnExpandVo> snTempList = new ArrayList<StockSnExpandVo>();
				BigDecimal spQty = BigDecimal.ZERO;
				for (StockSnExpandVo sn : insertStockSnList) {
					StockPosition sourceStockPosition = stockPositionMapper.selectByPrimaryKey(sn.getStockId());
					// 累加sn的数量到对应的position里面的数量
					if (sourceStockPosition != null
							&& sp.getMatId().intValue() == sourceStockPosition.getMatId().intValue()
							&& sp.getLocationId().intValue() == sourceStockPosition.getLocationId().intValue()
							&& sp.getBatch().equals(sourceStockPosition.getBatch())
							&& sp.getStatus().equals(sourceStockPosition.getStatus())
							&& sp.getPositionId().intValue() == sn.getTargetPositionId().intValue()
							&& sp.getPalletId().intValue() == sn.getTargetPalletId().intValue()
							&& sp.getPackageId().intValue() == sn.getTargetPackageId().intValue()) {
						snTempList.add(sn);
						// 累加物料的数量
						spQty = spQty.add(sn.getQty());

						// 还要判断原来的position里面的数据的递减

						BigDecimal subQty = sourceStockPosition.getQty().subtract(sn.getQty());
						int r = subQty.compareTo(BigDecimal.ZERO);
						if (r <= 0) {
							stockPositionMapper.deleteByPrimaryKey(sn.getStockId());
						} else {
							sourceStockPosition.setQty(subQty);
							stockPositionMapper.updateByStockQty(sourceStockPosition);
						}

						// 判断原来的sn的数据的减少
						StockSn sourceSn = stockSnDao.selectByThreeKey(sn);
						if (sourceSn == null) {
							// body.put("is_success", 1);
							// body.put("message", "源sn为空");
							// return body;
							throw new InventoryException("源仓位sn库存为空");
						}

						// 判断原来的SN里面的数据的递减
						BigDecimal subSourceQty = sourceSn.getQty().subtract(sn.getQty());
						int subr = subSourceQty.compareTo(BigDecimal.ZERO);

						if (subr <= 0) {
							stockSnDao.deleteByPrimaryKey(sourceSn.getId());
						} else {
							sourceSn.setQty(subSourceQty);
							stockSnDao.updateByStockQty(sourceSn);
						}

					}
				}
				sp.setQty(spQty);
				sp.setId(null);
				// 新增的position
				stockPositionMapper.insertSelective(sp);
				// 新增的SN
				if (snTempList.size() > 0) {
					for (StockSnExpandVo snvo : snTempList) {
						snvo.setStockId(sp.getId());
						stockSnDao.insertSnSelective(snvo);
					}
				}
			}
		}

		// 处理更新数据
		if (updateStockPositionList.size() > 0) {

			for (StockPosition sp : updateStockPositionList) {

				// 封装更新的SNlist
				List<StockSnExpandVo> snTempList = new ArrayList<StockSnExpandVo>();
				BigDecimal spQty = sp.getQty();

				for (StockSnExpandVo sn : updateStockSnList) {
					StockPosition sourceStockPosition = stockPositionMapper.selectByPrimaryKey(sn.getStockId());
					// 累加sn的数量到对应的position里面的数量
					// 获取对应的仓位 更新数量
					if (sourceStockPosition != null
							&& sp.getMatId().intValue() == sourceStockPosition.getMatId().intValue()
							&& sp.getLocationId().intValue() == sourceStockPosition.getLocationId().intValue()
							&& sp.getBatch().equals(sourceStockPosition.getBatch())
							&& sp.getPositionId().intValue() == sn.getTargetPositionId().intValue()
							&& sp.getPalletId().intValue() == sn.getTargetPalletId().intValue()
							&& sp.getPackageId().intValue() == sn.getTargetPackageId().intValue()) {
						snTempList.add(sn);
						spQty = spQty.add(sn.getQty());
						// 处理源仓位的数量
						// 如果为0删除，否则数量递减
						BigDecimal subQty = sourceStockPosition.getQty().subtract(sn.getQty());
						int r = subQty.compareTo(BigDecimal.ZERO);
						if (r <= 0) {
							stockPositionMapper.deleteByPrimaryKey(sn.getStockId());
						} else {
							sourceStockPosition.setQty(subQty);
							stockPositionMapper.updateByStockQty(sourceStockPosition);
						}

						// 处理源SN和目标SN的数量

						StockSn sourceSn = stockSnDao.selectByThreeKey(sn);
						if (sourceSn == null) {
							// body.put("is_success", 1);
							// body.put("message", "源sn为空");
							// return body;
							throw new InventoryException("源仓位sn库存为空");
						}
						StockSnExpandVo snvo = new StockSnExpandVo();
						snvo.setMatId(sn.getMatId());
						snvo.setSnId(sn.getSnId());
						snvo.setStockId(sp.getId());
						StockSn targerSn = stockSnDao.selectByThreeKey(snvo);
						if (targerSn == null) {
							// body.put("is_success", 1);
							// body.put("message", "目标sn为空");
							// return body;
							throw new InventoryException("目标仓位sn库存为空");
						}

						BigDecimal targetSnQty = targerSn.getQty().add(sn.getQty());
						targerSn.setQty(targetSnQty);
						stockSnDao.updateByStockQty(targerSn);

						BigDecimal subSourceQty = sourceSn.getQty().subtract(sn.getQty());
						int subr = subSourceQty.compareTo(BigDecimal.ZERO);

						if (subr <= 0) {
							stockSnDao.deleteByPrimaryKey(sourceSn.getId());
						} else {
							sourceSn.setQty(subSourceQty);
							stockSnDao.updateByStockQty(sourceSn);
						}

					}
				}
				sp.setQty(spQty);
				stockPositionMapper.updateByStockQty(sp);

			}
		}

		body.put("is_success", 0);
		body.put("message", "操作成功");

		return body;
	}

	@Override
	public int positionCleanup(BizStockTaskHead bizStockTaskHead, BizStockTaskItem bizStockTaskItem) throws Exception {
		try {

			// 新建作业单抬头
			long stockTaskCode = sequenceDao.selectNextVal("stock_task");
			bizStockTaskHead.setStockTaskCode(stockTaskCode + "");
			bizStockTaskHeadDao.insertSelective(bizStockTaskHead);
			int stockTaskId = bizStockTaskHead.getStockTaskId();// 自增的id

			// 新建作业单明细
			bizStockTaskItem.setStockTaskId(stockTaskId);
			bizStockTaskItemMapper.insertItemForPositionNew(bizStockTaskItem);

			// 仓位调整
			List<StockPosition> list = stockPositionMapper
					.selectStockPositionListByPositionId(bizStockTaskItem.getSourcePositionId());
			List<StockPositionExpandVo> expandVoList = new ArrayList<StockPositionExpandVo>();
			if (list != null && list.size() > 0) {
				for (StockPosition stockPosition : list) {
					StockPositionExpandVo expandVo = new StockPositionExpandVo();
					expandVo.setWhId(bizStockTaskItem.getWhId());
					expandVo.setTargetAreaId(bizStockTaskItem.getTargetAreaId());
					expandVo.setTargetPositionId(bizStockTaskItem.getTargetPositionId());
					expandVo.setTargetPackageId(0);
					expandVo.setTargetPalletId(0);

					expandVo.setAreaId(stockPosition.getAreaId());
					expandVo.setPositionId(stockPosition.getPositionId());
					expandVo.setPackageId(stockPosition.getPackageId());
					expandVo.setPalletId(stockPosition.getPalletId());

					expandVo.setMatId(stockPosition.getMatId());
					expandVo.setFtyId(stockPosition.getFtyId());
					expandVo.setLocationId(stockPosition.getLocationId());
					expandVo.setBatch(stockPosition.getBatch());
					expandVo.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
					expandVo.setQty(stockPosition.getQty());
					expandVo.setSpecStock(stockPosition.getSpecStock());
					expandVo.setSpecStockCode(stockPosition.getSpecStockCode());
					expandVo.setSpecStockName(stockPosition.getSpecStockName());
					expandVo.setInputDate(new Date());
					expandVo.setUnitId(stockPosition.getUnitId());
					expandVo.setUnitWeight(stockPosition.getUnitWeight());
					expandVoList.add(expandVo);
				}

			}
			this.stockMoveForPosition(expandVoList);

			// 添加审批人,先获得当前作业单
			BizStockTaskHead currentBizStockTaskHead = bizStockTaskHeadDao.selectByPrimaryKey(stockTaskId);
			commonService.initDefaultWf(stockTaskId, currentBizStockTaskHead.getReceiptType(),
					currentBizStockTaskHead.getStockTaskCode(), currentBizStockTaskHead.getCreateUser());

		} catch (Exception e) {
			throw e;
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int materialCleanupNew(BizStockTaskHead bizStockTaskHead, List<BizStockTaskItem> list) throws Exception {
		try {
			// 创建作业单抬头
			long stockTaskCode = sequenceDao.selectNextVal("stock_task");
			bizStockTaskHead.setStockTaskCode(stockTaskCode + "");
			bizStockTaskHeadDao.insertSelective(bizStockTaskHead);
			int stockTaskId = bizStockTaskHead.getStockTaskId();// 自增的id

			// 创建作业单明细
			int i = 1;
			for (BizStockTaskItem bizStockTaskItem : list) {
				bizStockTaskItem.setStockTaskId(stockTaskId);
				bizStockTaskItem.setStockTaskRid(i);
				i++;
				bizStockTaskItemMapper.insertSelective(bizStockTaskItem);
			}
			// 处理仓位库存
			List<StockSnExpandVo> snList = new ArrayList<StockSnExpandVo>();
			for (BizStockTaskItem item : list) {
				StockSnExpandVo snVo = new StockSnExpandVo();
				snVo.setDebitOrCredit("");
				snVo.setFreeze((byte) 0);
				snVo.setMatId(item.getMatId());
				snVo.setQty(item.getQty());
				snVo.setSnId(item.getSnId());
				snVo.setStockId(item.getStockPositionId());
				snVo.setTargetAreaId(item.getTargetAreaId());
				snVo.setTargetPackageId(item.getTargetPackageId());
				snVo.setTargetPalletId(item.getTargetPalletId());
				snVo.setTargetPositionId(item.getTargetPositionId());
				snList.add(snVo);
			}
			this.stockMoveForSn(snList);

			BizStockTaskHead currentBizStockTaskHead = bizStockTaskHeadDao.selectByPrimaryKey(stockTaskId);

			commonService.initRoleWf(stockTaskId, currentBizStockTaskHead.getReceiptType(),
					currentBizStockTaskHead.getStockTaskCode(), currentBizStockTaskHead.getCreateUser());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw e;
		}

		return 0;
	}

	@Override
	public StockPosition selectStockPositionByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return stockPositionMapper.selectByPrimaryKey(id);
	}

	boolean isWriteOff(BizStockTaskHeadCw head) throws Exception {
		boolean b = false;
		Byte referReceiptType = head.getReferReceiptType();
		Byte receiptType = head.getReceiptType();

		if ((referReceiptType.equals(EnumReceiptType.STOCK_RETURN_SALE.getValue())
				&& receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue()))
				|| (referReceiptType.equals(EnumReceiptType.STOCK_INPUT_TRANSPORT_ERP.getValue()))
				|| ((referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_OTHER.getValue())
						|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue())
						|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue())
						|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())
						|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue())
						|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()))
						&& receiptType.equals(EnumReceiptType.STOCK_TASK_LISTING.getValue()))
				|| (referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_ALLOCATE_CARGO.getValue())
						&& receiptType.equals(EnumReceiptType.STOCK_TASK_LISTING.getValue()))

		) {
			b = true;
		}

		return b;
	}

	@Override
	public StockSn queryStockSnByStockId(Integer stockId) {
		// TODO Auto-generated method stub
		return stockSnDao.queryStockSnListByStockId(stockId).get(0);
	}

	void initStockTask(BizStockTaskHeadCw stockTaskHead) throws Exception {
		if (stockTaskHead != null) {
			BizStockTaskReqHead stockTaskReqHead = bizStockTaskReqHeadDao
					.selectByPrimaryKey(stockTaskHead.getStockTaskReqId());
			if (stockTaskReqHead != null) {
				List<BizStockTaskReqItem> stockTaskReqItemList = bizStockTaskReqItemDao
						.selectItemById(stockTaskHead.getStockTaskReqId());

				if (stockTaskReqItemList != null && stockTaskReqItemList.size() > 0) {
					Map<Integer, BizStockTaskReqItem> reqItemMap = new HashMap<Integer, BizStockTaskReqItem>();
					Map<Integer, Integer> palletIdMap = new HashMap<Integer, Integer>();
					for (BizStockTaskReqItem item : stockTaskReqItemList) {
						reqItemMap.put(item.getItemId(), item);
					}
					// 未上架 包装物
					List<BizStockTaskReqPosition> reqPositionList = stockTaskReqPositionDao
							.selectByReqId(stockTaskHead.getStockTaskReqId());
					Map<Integer, BizStockTaskReqPosition> reqUnFinishPositionMap = new HashMap<Integer, BizStockTaskReqPosition>();
					Map<Integer, List<BizStockTaskReqPosition>> reqPositionListMap = new HashMap<Integer, List<BizStockTaskReqPosition>>();
					if (reqPositionList != null && reqPositionList.size() > 0) {
						for (BizStockTaskReqPosition poItem : reqPositionList) {
							if (poItem.getStatus().equals(EnumReceiptStatus.RECEIPT_UNFINISH.getValue())) {
								reqUnFinishPositionMap.put(poItem.getPackageId(), poItem);
							}
							List<BizStockTaskReqPosition> itemPList = reqPositionListMap
									.get(poItem.getStockTaskReqRid());
							if (itemPList == null) {
								itemPList = new ArrayList<BizStockTaskReqPosition>();
								itemPList.add(poItem);
							} else {
								itemPList.add(poItem);
							}
							reqPositionListMap.put(poItem.getStockTaskReqRid(), itemPList);
						}
					}
					String stockTaskCode = commonService.getNextReceiptCode("stock_task");
					stockTaskHead.setStockTaskCode(stockTaskCode);
					// 未过账
					stockTaskHead.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());

					int whId = stockTaskReqHead.getWhId();
					stockTaskHead.setSubmitDate(new Date());
					stockTaskHead.setFtyId(stockTaskReqHead.getFtyId());
					stockTaskHead.setLocationId(stockTaskReqHead.getLocationId());
					stockTaskHead.setWhId(whId);
					stockTaskHead.setShippingType(stockTaskReqHead.getShippingType());
					stockTaskHead.setReferReceiptType(stockTaskReqHead.getReferReceiptType());
					stockTaskHead.setReferReceiptId(stockTaskReqHead.getReferReceiptId());
					stockTaskHead.setReferReceiptCode(stockTaskReqHead.getReferReceiptCode());
					stockTaskHead.setMoveTypeId(0);
					boolean isWriteOff = this.isWriteOff(stockTaskHead);
					if (isWriteOff) {
						stockTaskHead.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
					}

					String whCode = dictionaryService.getWhCodeByWhId(whId);
					Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
							UtilProperties.getInstance().getDefaultCCQ());
					Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
							UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());

					bizStockTaskHeadCwDao.insertSelective(stockTaskHead);

					int taskItemRid = 1;
					for (BizStockTaskItemCw item : stockTaskHead.getItemList()) {
						BizStockTaskReqItem reqItem = reqItemMap.get(item.getItemId());
						item.setItemId(null);
						item.setStockTaskRid(taskItemRid);
						taskItemRid++;
						item.setStockTaskId(stockTaskHead.getStockTaskId());
						item.setStockTaskReqId(reqItem.getStockTaskReqId());
						item.setStockTaskReqRid(reqItem.getStockTaskReqRid());
						// 冲销单据 状态为完成

						item.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
						if (isWriteOff) {
							item.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
						}

						item.setWhId(stockTaskHead.getWhId());
						item.setFtyId(stockTaskHead.getFtyId());
						item.setLocationId(stockTaskHead.getLocationId());
						item.setMatId(reqItem.getMatId());
						item.setUnitId(reqItem.getUnitId());
						item.setBatch(reqItem.getBatch());
						item.setStockTaskQty(reqItem.getStockTaskQty());
						item.setMatStoreType(reqItem.getMatStoreType());
						item.setPackageTypeId(reqItem.getPackageTypeId());
						item.setReferReceiptType(reqItem.getReferReceiptType());
						item.setReferReceiptId(reqItem.getReferReceiptId());
						item.setReferReceiptCode(reqItem.getReferReceiptCode());
						item.setReferReceiptRid(reqItem.getReferReceiptRid());
						item.setProductionBatch(reqItem.getProductionBatch());
						item.setErpBatch(reqItem.getErpBatch());
						item.setQualityBatch(reqItem.getQualityBatch());
						bizStockTaskItemCwDao.insertSelective(item);
						// 校验数量
						if (reqItem.getStockTaskQty().add(item.getQty()).compareTo(reqItem.getQty()) == 1) {
							// 上架数量+已上架数量> 请求数量
							String msg = "上架数量+已上架数量>总数量 ";
							if(stockTaskHead.getReceiptType().equals(EnumReceiptType.STOCK_TASK_LISTING.getValue())){
								msg = "上架数量+已上架数量>总数量 ";
							}
							if(stockTaskHead.getReceiptType().equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())){
								msg = "下架数量+已下架数量>总数量 ";
							}
							throw new WMSException(msg);
						}

						BigDecimal pItemQty = new BigDecimal(0);

						// 前续单据有包
						for (BizStockTaskPositionCw poItem : item.getPalletPackageList()) {
							
							List<BizStockTaskReqPosition> itemPList = reqPositionListMap
									.get(reqItem.getStockTaskReqRid());
							if (itemPList != null && itemPList.size() > 0 && poItem.getPackageId() != null) {
								BizStockTaskReqPosition reqPoItem = reqUnFinishPositionMap.get(poItem.getPackageId());
								if (reqPoItem == null) {
									throw new WMSException("包装物错误");
								}
								poItem.setStockTaskReqPositionId(reqPoItem.getStockTaskReqPositionId());
								poItem.setStockId(reqPoItem.getStockId());
								poItem.setStockSnId(reqPoItem.getStockSnId());
								poItem.setBatch(reqPoItem.getBatch());
								poItem.setSnId(reqPoItem.getSnId());
								poItem.setStockQty(reqPoItem.getStockQty());

								// 更新前续单据
								// 将托盘设置0
								if (poItem.getPalletId() != null && !palletIdMap.containsKey(poItem.getPalletId())) {
									stockTaskReqPositionDao.updatePalletIdTOZeroByPalletId(poItem.getPalletId());
									palletIdMap.put(poItem.getPalletId(), poItem.getPalletId());
								}
								// 将包 更新完成状态
								BizStockTaskReqPosition updateRecord = new BizStockTaskReqPosition();
								updateRecord.setItemPositionId(reqPoItem.getItemPositionId());
								updateRecord.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
								stockTaskReqPositionDao.updateByPrimaryKeySelective(updateRecord);
							}

							poItem.setStockTaskId(item.getStockTaskId());
							poItem.setStockTaskRid(item.getStockTaskRid());
							poItem.setStockTaskReqId(item.getStockTaskReqId());
							poItem.setStockTaskReqRid(item.getStockTaskReqRid());
							poItem.setBatch(item.getBatch());
							poItem.setCreateUser(stockTaskHead.getCreateUser());

							// 从默认仓位发出
							// 上架 组盘上架
							Byte receiptType = stockTaskHead.getReceiptType();
							if (receiptType.equals(EnumReceiptType.STOCK_TASK_LISTING.getValue())
									|| receiptType.equals(EnumReceiptType.STOCK_TASK_GROUP.getValue())) {
								poItem.setSourceAreaId(sourceAreaId);
								poItem.setSourcePositionId(sourcePositionId);
							} else if (receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
								poItem.setTargetAreaId(sourceAreaId);
								poItem.setTargetPositionId(sourcePositionId);
							} else {
								throw new WMSException("单据类型出错");
							}

							bizStockTaskPositionCwDao.insertSelective(poItem);

							// 插入历史表
							this.addHistory(stockTaskHead, item, poItem);

							pItemQty = pItemQty.add(poItem.getQty());
						}

						// 更新请求 行项目状态

						if (item.getQty().compareTo(pItemQty) == 0) {

							BigDecimal allStockTaskQty = item.getQty().add(reqItem.getStockTaskQty());
							BizStockTaskReqItem updateBizStockTaskReqItem = new BizStockTaskReqItem();
							updateBizStockTaskReqItem.setItemId(reqItem.getItemId());
							updateBizStockTaskReqItem.setStockTaskQty(allStockTaskQty);
							updateBizStockTaskReqItem.setRemark(item.getRemark());
							if (allStockTaskQty.compareTo(reqItem.getQty()) == 0) {
								updateBizStockTaskReqItem.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());// 已经完成
							}
							bizStockTaskReqItemDao.updateByPrimaryKeySelective(updateBizStockTaskReqItem);

						} else {
							throw new WMSException("上架数量错误");
						}

					}
					// 更新请求 抬头状态
					Byte isComplete = bizStockTaskReqItemDao.selectMinStatus(stockTaskHead.getStockTaskReqId());
					// 作业请求单已完成
					BizStockTaskReqHead updateBizStockTaskReqHead = new BizStockTaskReqHead();
					updateBizStockTaskReqHead.setStockTaskReqId(stockTaskHead.getStockTaskReqId());
					if (isComplete != null && isComplete.compareTo(EnumReceiptStatus.RECEIPT_FINISH.getValue()) == 0) {
						updateBizStockTaskReqHead.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());

					}
					updateBizStockTaskReqHead.setDeliveryDriver(stockTaskHead.getDeliveryDriver());
					updateBizStockTaskReqHead.setDeliveryVehicle(stockTaskHead.getDeliveryVehicle());
					updateBizStockTaskReqHead.setRemark(stockTaskHead.getRemark());
					bizStockTaskReqHeadDao.updateByPrimaryKeySelective(updateBizStockTaskReqHead);
				} else {
					throw new WMSException("缺少请求行项目信息");
				}
			} else {
				throw new WMSException("缺少请求抬头信息");
			}
		} else {
			throw new WMSException("参数异常");
		}
	}

	void addHistory(BizStockTaskHeadCw stockTaskHead, BizStockTaskItemCw item, BizStockTaskPositionCw poItem)
			throws Exception {
		BizBusinessHistory bh = new BizBusinessHistory();
		bh.setReferReceiptId(stockTaskHead.getStockTaskId());
		bh.setReferReceiptRid(item.getStockTaskReqRid());
		bh.setReferReceiptCode(stockTaskHead.getStockTaskCode());
		bh.setReferReceiptPid(poItem.getItemPositionId());
		Byte businessType = EnumBusinessType.STOCK_TASK.getValue();
		String debitCredit = null;
		if (stockTaskHead.getReceiptType().equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {

			debitCredit = EnumDebitCredit.CREDIT_H_SUBTRACT.getName();
		} else {
			debitCredit = EnumDebitCredit.DEBIT_S_ADD.getName();
		}
		bh.setBusinessType(businessType);
		bh.setReceiptType(stockTaskHead.getReceiptType());
		bh.setMatId(item.getMatId());
		bh.setBatch(item.getBatch());
		bh.setFtyId(item.getFtyId());
		bh.setLocationId(item.getLocationId());
		bh.setAreaId(poItem.getTargetAreaId());
		bh.setPositionId(poItem.getTargetPositionId());
		bh.setPalletId(poItem.getPalletId());
		bh.setPackageId(poItem.getPackageId());
		bh.setQty(poItem.getQty());
		bh.setDebitCredit(debitCredit);
		bh.setCreateUser(stockTaskHead.getCreateUser());
		businessHistoryDao.insertSelective(bh);
	}

	void addHistoryByDelete(BizStockTaskHeadCw stockTaskHead, BizStockTaskItemCw item, BizStockTaskPositionCw poItem)
			throws Exception {
		BizBusinessHistory bh = new BizBusinessHistory();
		bh.setReferReceiptId(stockTaskHead.getStockTaskId());
		bh.setReferReceiptRid(item.getStockTaskReqRid());
		bh.setReferReceiptCode(stockTaskHead.getStockTaskCode());
		bh.setReferReceiptPid(poItem.getItemPositionId());
		Byte businessType = EnumBusinessType.STOCK_TASK.getValue();
		String debitCredit = null;
		if (stockTaskHead.getReceiptType().equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {

			debitCredit = EnumDebitCredit.DEBIT_S_ADD.getName();
		} else {
			debitCredit = EnumDebitCredit.CREDIT_H_SUBTRACT.getName();
		}
		bh.setBusinessType(businessType);
		bh.setReceiptType(stockTaskHead.getReceiptType());
		bh.setMatId(item.getMatId());
		bh.setBatch(item.getBatch());
		bh.setFtyId(item.getFtyId());
		bh.setLocationId(item.getLocationId());
		bh.setAreaId(poItem.getSourceAreaId());
		bh.setPositionId(poItem.getSourcePositionId());
		bh.setPalletId(poItem.getPalletId());
		bh.setPackageId(poItem.getPackageId());
		bh.setQty(poItem.getQty());
		bh.setDebitCredit(debitCredit);
		bh.setCreateUser(stockTaskHead.getCreateUser());
		businessHistoryDao.insertSelective(bh);
	}

	void initStockTaskByGroup(BizStockTaskHeadCw stockTaskHead) throws Exception {
		if (stockTaskHead != null) {
			List<Integer> itemIds = new ArrayList<Integer>();
			for (BizStockTaskItemCw item : stockTaskHead.getItemList()) {
				itemIds.add(item.getItemId());
			}

			List<BizStockTaskReqItem> stockTaskReqItemList = bizStockTaskReqItemDao.selectItemByItemIds(itemIds);

			if (stockTaskReqItemList != null && stockTaskReqItemList.size() > 0) {
				Map<Integer, BizStockTaskReqItem> reqItemMap = new HashMap<Integer, BizStockTaskReqItem>();
				Map<Integer, Integer> palletIdMap = new HashMap<Integer, Integer>();
				for (BizStockTaskReqItem item : stockTaskReqItemList) {
					reqItemMap.put(item.getItemId(), item);
				}
				// 未上架 包装物
				Map<String, Object> reqPositionMap = new HashMap<String, Object>();
				reqPositionMap.put("itemIds", itemIds);
				List<BizStockTaskReqPosition> reqPositionList = stockTaskReqPositionDao
						.selectByCondition(reqPositionMap);
				Map<Integer, BizStockTaskReqPosition> reqUnFinishPositionMap = new HashMap<Integer, BizStockTaskReqPosition>();
				Map<String, List<BizStockTaskReqPosition>> reqPositionListMap = new HashMap<String, List<BizStockTaskReqPosition>>();
				if (reqPositionList != null && reqPositionList.size() > 0) {
					for (BizStockTaskReqPosition poItem : reqPositionList) {
						if (poItem.getStatus().equals(EnumReceiptStatus.RECEIPT_UNFINISH.getValue())) {
							reqUnFinishPositionMap.put(poItem.getPackageId(), poItem);
						}
						String key = poItem.getStockTaskReqId() +"-"+poItem.getStockTaskReqRid();
						List<BizStockTaskReqPosition> itemPList = reqPositionListMap
								.get(key);
						if (itemPList == null) {
							itemPList = new ArrayList<BizStockTaskReqPosition>();
							itemPList.add(poItem);
						} else {
							itemPList.add(poItem);
						}
						reqPositionListMap.put(key, itemPList);

					}
				}
				String stockTaskCode = commonService.getNextReceiptCode("stock_task");
				stockTaskHead.setStockTaskCode(stockTaskCode);
				// 未过账
				stockTaskHead.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
				stockTaskHead.setMoveTypeId(0);
				stockTaskHead.setStockTaskReqId(0);
				stockTaskHead.setSubmitDate(new Date());

				String whCode = dictionaryService.getWhCodeByWhId(stockTaskHead.getWhId());
				Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
						UtilProperties.getInstance().getDefaultCCQ());
				Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
						UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());

				bizStockTaskHeadCwDao.insertSelective(stockTaskHead);
				int taskItemRid = 1;
				Map<Integer, Integer> reqIdMap = new HashMap<Integer, Integer>();
				for (BizStockTaskItemCw item : stockTaskHead.getItemList()) {
					BizStockTaskReqItem reqItem = reqItemMap.get(item.getItemId());
					item.setItemId(null);
					item.setStockTaskRid(taskItemRid);
					taskItemRid++;
					item.setStockTaskId(stockTaskHead.getStockTaskId());
					item.setStockTaskReqId(reqItem.getStockTaskReqId());
					reqIdMap.put(reqItem.getStockTaskReqId(), reqItem.getStockTaskReqId());
					item.setStockTaskReqRid(reqItem.getStockTaskReqRid());
					item.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
					item.setWhId(stockTaskHead.getWhId());
					item.setFtyId(stockTaskHead.getFtyId());
					item.setLocationId(stockTaskHead.getLocationId());
					item.setMatId(reqItem.getMatId());
					item.setUnitId(reqItem.getUnitId());
					item.setBatch(reqItem.getBatch());
					item.setStockTaskQty(reqItem.getStockTaskQty());
					item.setMatStoreType(reqItem.getMatStoreType());
					item.setPackageTypeId(reqItem.getPackageTypeId());
					item.setReferReceiptType(reqItem.getReferReceiptType());
					item.setReferReceiptId(reqItem.getReferReceiptId());
					item.setReferReceiptCode(reqItem.getReferReceiptCode());
					item.setReferReceiptRid(reqItem.getReferReceiptRid());
					item.setProductionBatch(reqItem.getProductionBatch());
					item.setErpBatch(reqItem.getErpBatch());
					item.setQualityBatch(reqItem.getQualityBatch());
					bizStockTaskItemCwDao.insertSelective(item);
					// 校验数量
					if (reqItem.getStockTaskQty().add(item.getQty()).compareTo(reqItem.getQty()) == 1) {
						// 上架数量+已上架数量> 请求数量
						throw new WMSException("上架数量+已上架数量>总数量 ");
					}

					BigDecimal pItemQty = new BigDecimal(0);

					// 前续单据有包
					for (BizStockTaskPositionCw poItem : item.getPalletPackageList()) {
						String key = reqItem.getStockTaskReqId() +"-"+reqItem.getStockTaskReqRid();
						List<BizStockTaskReqPosition> itemPList = reqPositionListMap
								.get(key);
						if (itemPList != null && itemPList.size() > 0 && poItem.getPackageId() != null) {
							BizStockTaskReqPosition reqPoItem = reqUnFinishPositionMap.get(poItem.getPackageId());
							if (reqPoItem == null) {
								throw new WMSException("包装物错误");
							}
							poItem.setStockTaskReqPositionId(reqPoItem.getStockTaskReqPositionId());
							poItem.setStockId(reqPoItem.getStockId());
							poItem.setStockSnId(reqPoItem.getStockSnId());
							poItem.setBatch(reqPoItem.getBatch());
							poItem.setSnId(reqPoItem.getSnId());
							poItem.setStockQty(reqPoItem.getStockQty());

							// 更新前续单据
							// 将托盘设置0
							if (poItem.getPalletId() != null && !palletIdMap.containsKey(poItem.getPalletId())) {
								stockTaskReqPositionDao.updatePalletIdTOZeroByPalletId(poItem.getPalletId());
								palletIdMap.put(poItem.getPalletId(), poItem.getPalletId());
							}
							// 将包 更新完成状态
							BizStockTaskReqPosition updateRecord = new BizStockTaskReqPosition();
							updateRecord.setItemPositionId(reqPoItem.getItemPositionId());
							updateRecord.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
							stockTaskReqPositionDao.updateByPrimaryKeySelective(updateRecord);
						}

						poItem.setStockTaskId(item.getStockTaskId());
						poItem.setStockTaskRid(item.getStockTaskRid());
						poItem.setStockTaskReqId(item.getStockTaskReqId());
						poItem.setStockTaskReqRid(item.getStockTaskReqRid());
						poItem.setBatch(item.getBatch());
						poItem.setCreateUser(stockTaskHead.getCreateUser());

						// 从默认仓位发出
						// 上架 组盘上架
						Byte receiptType = stockTaskHead.getReceiptType();
						if (receiptType.equals(EnumReceiptType.STOCK_TASK_LISTING.getValue())
								|| receiptType.equals(EnumReceiptType.STOCK_TASK_GROUP.getValue())) {
							poItem.setSourceAreaId(sourceAreaId);
							poItem.setSourcePositionId(sourcePositionId);
						} else if (receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
							poItem.setTargetAreaId(sourceAreaId);
							poItem.setTargetPositionId(sourcePositionId);
						} else {
							throw new WMSException("单据类型出错");
						}

						bizStockTaskPositionCwDao.insertSelective(poItem);

						pItemQty = pItemQty.add(poItem.getQty());
					}

					// 更新请求 行项目状态

					if (item.getQty().compareTo(pItemQty) == 0) {

						BigDecimal allStockTaskQty = item.getQty().add(reqItem.getStockTaskQty());
						BizStockTaskReqItem updateBizStockTaskReqItem = new BizStockTaskReqItem();
						updateBizStockTaskReqItem.setItemId(reqItem.getItemId());
						updateBizStockTaskReqItem.setStockTaskQty(allStockTaskQty);

						if (allStockTaskQty.compareTo(reqItem.getQty()) == 0) {
							updateBizStockTaskReqItem.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());// 已经完成
						}
						bizStockTaskReqItemDao.updateByPrimaryKeySelective(updateBizStockTaskReqItem);

					} else {
						throw new WMSException("上架数量错误");
					}

				}
				if (!reqIdMap.isEmpty()) {
					for (Integer reqId : reqIdMap.keySet()) {
						// 更新请求 抬头状态
						Byte isComplete = bizStockTaskReqItemDao.selectMinStatus(reqId);
						if (isComplete != null
								&& isComplete.compareTo(EnumReceiptStatus.RECEIPT_FINISH.getValue()) == 0) {
							// 作业请求单已完成
							BizStockTaskReqHead updateBizStockTaskReqHead = new BizStockTaskReqHead();
							updateBizStockTaskReqHead.setStockTaskReqId(reqId);
							updateBizStockTaskReqHead.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
							bizStockTaskReqHeadDao.updateByPrimaryKeySelective(updateBizStockTaskReqHead);

						}
					}
				}

			} else {
				throw new WMSException("缺少请求行项目信息");
			}

		} else {
			throw new WMSException("参数异常");
		}
	}

	void updateStockPosition(BizStockTaskHeadCw stockTaskHead) throws Exception {
		for (BizStockTaskItemCw item : stockTaskHead.getItemList()) {
			Boolean isSale = false;
			BizAllocateCargoHead ahead = new BizAllocateCargoHead();
			String debitCredit = null;
			if (stockTaskHead.getReferReceiptType() != null && stockTaskHead.getReferReceiptType()
					.equals(EnumReceiptType.STOCK_OUTPUT_ALLOCATE_CARGO.getValue())
					) {
				// 判断是否是特殊配货单
				ahead = AllocateCargoHeadDao.selectByPrimaryKey(stockTaskHead.getReferReceiptId());
				if (ahead != null && EnumAllocateCargoOperationType.NO_MENTION.getValue() == ahead.getOperationType()) {
					// 已售未提 的交货单
					isSale = true;
					if(stockTaskHead.getReceiptType().equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())){
						debitCredit = EnumDebitCredit.DEBIT_S_ADD.getName();
					}else{
						debitCredit = EnumDebitCredit.CREDIT_H_SUBTRACT.getName();
					}
				}

			}
			updateStockPosition(item,isSale,ahead,debitCredit);
		}
	}
	
	void updateStockPositionForWarehouse(BizStockTaskHeadCw stockTaskHead) throws Exception {
		for (BizStockTaskItemCw item : stockTaskHead.getItemList()) {
			updateStockPositionForWarehouse(item);
		}
	}
	
	void updateStockPositionForWarehouse(BizStockTaskItemCw stockTaskItem) throws Exception {

		Map<Integer, Integer> palletIdMap = new HashMap<Integer, Integer>();
		for (BizStockTaskPositionCw pItem : stockTaskItem.getPalletPackageList()) {
			// 原仓位
			StockPosition stockPosition = new StockPosition();

			stockPosition.setWhId(stockTaskItem.getWhId());
			stockPosition.setAreaId(pItem.getSourceAreaId());
			stockPosition.setPositionId(pItem.getSourcePositionId());
			stockPosition.setMatId(stockTaskItem.getMatId());
			stockPosition.setFtyId(stockTaskItem.getFtyId());
			stockPosition.setLocationId(stockTaskItem.getLocationId());
			stockPosition.setBatch(stockTaskItem.getBatch());

			stockPosition.setInputDate(new Date());
			stockPosition.setQty(pItem.getQty());
			stockPosition.setUnitId(stockTaskItem.getUnitId());
			stockPosition.setInputDate(new Date());
			
			//添加托盘2018-7-26
			stockPosition.setPalletId(pItem.getSourcePalletId());
			
			if (stockTaskItem.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
				// 启用包
				if (pItem.getSourcePalletId() == null) {
					stockPosition.setPalletId(0);
				} else {
					stockPosition.setPalletId(pItem.getSourcePalletId());
//					if (!palletIdMap.containsKey(pItem.getPalletId())) {
//						palletIdMap.put(pItem.getPalletId(), pItem.getPalletId());
//
//						stockPositionMapper.updatePalletIdToZeroByPalletId(pItem.getPalletId());
//					}

				}

				// 启用包
				if (pItem.getPackageId() == null) {
					throw new WMSException("包装类型出错");
				}
				stockPosition.setPackageId(pItem.getPackageId());
			} else {
				// 不启用包
				stockPosition.setPalletId(0);
				stockPosition.setPackageId(0);
			}

			stockPosition.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			stockPosition.setPackageTypeId(stockTaskItem.getPackageTypeId());
			if (stockTaskItem.getReferReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())
					|| stockTaskItem.getReferReceiptType()
							.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())) {
				// 紧急 作业生产转运 紧急作业销售出库 假库存
				stockPosition.setIsUrgent(EnumIsUrgent.URGENT.getValue());
			}
			this.modifyStockPositionForWarehouse(stockPosition);

			// 目标仓位
			StockPosition stockPositionT = new StockPosition();

			stockPositionT.setWhId(stockTaskItem.getWhId());
			stockPositionT.setAreaId(pItem.getTargetAreaId());
			stockPositionT.setPositionId(pItem.getTargetPositionId());
			stockPositionT.setMatId(stockTaskItem.getMatId());
			stockPositionT.setFtyId(stockTaskItem.getFtyId());
			stockPositionT.setLocationId(stockTaskItem.getLocationId());
			stockPositionT.setBatch(stockTaskItem.getBatch());

			stockPositionT.setInputDate(new Date());
			stockPositionT.setQty(pItem.getQty());
			stockPositionT.setUnitId(stockTaskItem.getUnitId());
			stockPositionT.setInputDate(new Date());
			//添加托盘2018-7-26
			stockPosition.setPalletId(pItem.getPalletId());
			
			if (stockTaskItem.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
				// 启用包
				if (pItem.getPalletId() == null) {
					stockPositionT.setPalletId(0);
				} else {
					stockPositionT.setPalletId(pItem.getPalletId());
				}
				// 启用包
				if (pItem.getPackageId() == null) {
					throw new WMSException("包装类型出错");
				}
				stockPositionT.setPackageId(pItem.getPackageId());
			} else {
				// 不启用包
				stockPositionT.setPalletId(0);
				stockPositionT.setPackageId(0);
			}

			stockPositionT.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
			stockPositionT.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			stockPositionT.setPackageTypeId(stockTaskItem.getPackageTypeId());

			if (stockTaskItem.getReferReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())
					|| stockTaskItem.getReferReceiptType()
							.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())) {
				// 紧急 作业生产转运 紧急作业销售出库 假库存
				stockPositionT.setIsUrgent(EnumIsUrgent.URGENT.getValue());
			}
			this.modifyStockPositionForWarehouse(stockPositionT);
		}
	}
	
	//与commonService.modifyStockPosition不同之处是把key.setPalletId(param.getPalletId());的注释放出
	//否则可能查出两条，在其他功能未上架的情况下，默认仓位可能有负库存
	private boolean modifyStockPositionForWarehouse(StockPosition param) throws Exception {

		boolean ret = false;
		Byte stockTypeId = param.getStockTypeId();
		if (stockTypeId == null) {
			stockTypeId = 1;
		}
		StockPosition stockPosition;
		if (param.getId() != null && param.getId() > 0) {
			if (param.getIsUrgent() != null) {
				// 紧急查询
				stockPosition = stockPositionDao.selectByPrimaryKeyOnUrgent(param.getId());
			} else {
				stockPosition = stockPositionDao.selectByPrimaryKey(param.getId());
			}

		} else {
			// 根据唯一键查询仓位库存是否存在
			StockPositionKey key = new StockPositionKey();
			key.setPositionId(param.getPositionId());// 仓位
			key.setPalletId(param.getPalletId());// 托盘
			key.setPackageId(param.getPackageId());// 包
			key.setMatId(param.getMatId()); // 物料id,
			key.setLocationId(param.getLocationId());// 库存地点id
			key.setBatch(param.getBatch()); // 批号id
			key.setStatus(param.getStatus());
			// 紧急查询
			key.setIsUrgent(param.getIsUrgent());
			if (param.getIsUrgent() != null) {
				key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
			}
			key.setStockTypeId(stockTypeId);
			stockPosition = stockPositionDao.selectByUniqueKey(key);
		}
		// 存储仓位库存id,以备sn库存使用
		int stockId;
		if (stockPosition == null) {
			// 如果不存在,直接插入
			if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				param.setQty(param.getQty().negate());
			} else if (EnumDebitCredit.DEBIT_S_ADD.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				param.setQty(param.getQty());
			} else {
				throw new SAPDebitCreditException();
			}

			// 插入仓位库存并取出新添加的仓位库存id
			stockPositionDao.insertSelective(param);
			stockId = param.getId();

		} else {
			// 如果存在,直接取出仓位库存id
			stockId = stockPosition.getId();

			// 创建个临时变量,设置id和修改后数量
			StockPosition stockPositionTmp = new StockPosition();
			stockPositionTmp.setId(stockId);
			stockPositionTmp.setIsUrgent(param.getIsUrgent());
			if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				// 仓位库存历史
				StockPositionHistory history = new StockPositionHistory(stockPosition);
				history.setQty(param.getQty());

				stockPositionHistoryDao.insertSelective(history);

				// 贷 H 减
				stockPositionTmp.setQty(stockPosition.getQty().subtract(param.getQty()));
			} else if (EnumDebitCredit.DEBIT_S_ADD.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				// 借 S 加
				stockPositionTmp.setQty(stockPosition.getQty().add(param.getQty()));
			} else {
				throw new SAPDebitCreditException();
			}

			if (BigDecimal.ZERO.compareTo(stockPositionTmp.getQty()) == 0) {
				// 如果是0直接删除
				if (param.getIsUrgent() != null) {
					// 紧急
					stockPositionDao.deleteByPrimaryKeyUrgent(stockId);
				} else {
					stockPositionDao.deleteByPrimaryKey(stockId);
				}

			} else {
				// 如果不是0,直接修改,修改的数量是已经经过计算的
				stockPositionDao.updateByStockQty(stockPositionTmp);

			}
		}

		return ret;

	}

	void updateStockPosition(BizStockTaskItemCw stockTaskItem , boolean isSale ,BizAllocateCargoHead achead ,String debitCredit) throws Exception {

		if(isSale){
			// 添加已售未提库存
			this.updateSaleStock(stockTaskItem, debitCredit, achead);
			
		}
		this.updatePostionBase(stockTaskItem);
		
		
		
	}
	
	void updatePostionBase(BizStockTaskItemCw stockTaskItem) throws Exception{
		Map<String, Integer> palletIdMap = new HashMap<String, Integer>();
		for (BizStockTaskPositionCw pItem : stockTaskItem.getPalletPackageList()) {
			// 原仓位
			StockPosition stockPosition = new StockPosition();

			stockPosition.setWhId(stockTaskItem.getWhId());
			stockPosition.setAreaId(pItem.getSourceAreaId());
			stockPosition.setPositionId(pItem.getSourcePositionId());
			stockPosition.setMatId(stockTaskItem.getMatId());
			stockPosition.setFtyId(stockTaskItem.getFtyId());
			stockPosition.setLocationId(stockTaskItem.getLocationId());
			stockPosition.setBatch(stockTaskItem.getBatch());

			stockPosition.setInputDate(new Date());
			stockPosition.setQty(pItem.getQty());
			stockPosition.setUnitId(stockTaskItem.getUnitId());
			stockPosition.setInputDate(new Date());

			if (stockTaskItem.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
				// 启用包
				if (pItem.getPalletId() == null) {
					stockPosition.setPalletId(0);
				} else {
					stockPosition.setPalletId(pItem.getPalletId());
					String palletKey = pItem.getPalletId() + "-" +pItem.getSourcePositionId();
					if (!palletIdMap.containsKey(palletKey)) {
						palletIdMap.put(palletKey, pItem.getPalletId());

						Map<String, Object> udpateMap = new HashMap<String, Object>();
						udpateMap.put("palletId", pItem.getPalletId());
						udpateMap.put("positionId", pItem.getSourcePositionId());
						stockPositionMapper.updatePalletIdToZeroByPalletIdAndPositionId(udpateMap);
					}

				}

				// 启用包
				if (pItem.getPackageId() == null) {
					throw new WMSException("包装类型出错");
				}
				stockPosition.setPackageId(pItem.getPackageId());
			} else {
				// 不启用包
				stockPosition.setPalletId(0);
				stockPosition.setPackageId(0);
			}

			stockPosition.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			stockPosition.setPackageTypeId(stockTaskItem.getPackageTypeId());
			if (stockTaskItem.getReferReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())
					|| stockTaskItem.getReferReceiptType()
							.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())) {
				// 紧急 作业生产转运 紧急作业销售出库 假库存
				stockPosition.setIsUrgent(EnumIsUrgent.URGENT.getValue());
			}
			commonService.modifyStockPosition(stockPosition);

			// 目标仓位
			StockPosition stockPositionT = new StockPosition();

			stockPositionT.setWhId(stockTaskItem.getWhId());
			stockPositionT.setAreaId(pItem.getTargetAreaId());
			stockPositionT.setPositionId(pItem.getTargetPositionId());
			stockPositionT.setMatId(stockTaskItem.getMatId());
			stockPositionT.setFtyId(stockTaskItem.getFtyId());
			stockPositionT.setLocationId(stockTaskItem.getLocationId());
			stockPositionT.setBatch(stockTaskItem.getBatch());

			stockPositionT.setInputDate(new Date());
			stockPositionT.setQty(pItem.getQty());
			stockPositionT.setUnitId(stockTaskItem.getUnitId());
			stockPositionT.setInputDate(new Date());

			if (stockTaskItem.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
				// 启用包
				if (pItem.getPalletId() == null) {
					stockPositionT.setPalletId(0);
				} else {
					stockPositionT.setPalletId(pItem.getPalletId());
				}
				// 启用包
				if (pItem.getPackageId() == null) {
					throw new WMSException("包装类型出错");
				}
				stockPositionT.setPackageId(pItem.getPackageId());
			} else {
				// 不启用包
				stockPositionT.setPalletId(0);
				stockPositionT.setPackageId(0);
			}

			stockPositionT.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
			
			stockPositionT.setPackageTypeId(stockTaskItem.getPackageTypeId());

			if (stockTaskItem.getReferReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())
					|| stockTaskItem.getReferReceiptType()
							.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())) {
				// 紧急 作业生产转运 紧急作业销售出库 假库存
				stockPositionT.setIsUrgent(EnumIsUrgent.URGENT.getValue());
			}
			
			stockPositionT.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			
			commonService.modifyStockPosition(stockPositionT);
		}
	
	}
	
	void updatePostionSale(BizStockTaskItemCw stockTaskItem , boolean isSale,StockPosition salePosition) throws Exception{
		Map<String, Integer> palletIdMap = new HashMap<String, Integer>();
		for (BizStockTaskPositionCw pItem : stockTaskItem.getPalletPackageList()) {
			// 原仓位
			StockPosition stockPosition = new StockPosition();

			stockPosition.setWhId(stockTaskItem.getWhId());
			stockPosition.setAreaId(pItem.getSourceAreaId());
			stockPosition.setPositionId(pItem.getSourcePositionId());
			stockPosition.setMatId(stockTaskItem.getMatId());
			stockPosition.setFtyId(stockTaskItem.getFtyId());
			stockPosition.setLocationId(stockTaskItem.getLocationId());
			stockPosition.setBatch(stockTaskItem.getBatch());

			stockPosition.setInputDate(new Date());
			stockPosition.setQty(pItem.getQty());
			stockPosition.setUnitId(stockTaskItem.getUnitId());
			stockPosition.setInputDate(new Date());

			if (stockTaskItem.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
				// 启用包
				if (pItem.getPalletId() == null) {
					stockPosition.setPalletId(0);
				} else {
					stockPosition.setPalletId(pItem.getPalletId());
					String palletKey = pItem.getPalletId() + "-" +pItem.getSourcePositionId();
					if (!palletIdMap.containsKey(palletKey)) {
						palletIdMap.put(palletKey, pItem.getPalletId());

						Map<String, Object> udpateMap = new HashMap<String, Object>();
						udpateMap.put("palletId", pItem.getPalletId());
						udpateMap.put("positionId", pItem.getSourcePositionId());
						stockPositionMapper.updatePalletIdToZeroByPalletIdAndPositionId(udpateMap);
					}

				}

				// 启用包
				if (pItem.getPackageId() == null) {
					throw new WMSException("包装类型出错");
				}
				stockPosition.setPackageId(pItem.getPackageId());
			} else {
				// 不启用包
				stockPosition.setPalletId(0);
				stockPosition.setPackageId(0);
			}

			stockPosition.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			stockPosition.setPackageTypeId(stockTaskItem.getPackageTypeId());
			if (stockTaskItem.getReferReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())
					|| stockTaskItem.getReferReceiptType()
							.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())) {
				// 紧急 作业生产转运 紧急作业销售出库 假库存
				stockPosition.setIsUrgent(EnumIsUrgent.URGENT.getValue());
			}
			commonService.modifyStockPosition(stockPosition);

			// 目标仓位
			
			StockPosition stockPositionT = new StockPosition();

			stockPositionT.setWhId(salePosition.getWhId());
			stockPositionT.setAreaId(salePosition.getAreaId());
			stockPositionT.setPositionId(salePosition.getPositionId());
			stockPositionT.setMatId(stockTaskItem.getMatId());
			stockPositionT.setFtyId(stockTaskItem.getFtyId());
			stockPositionT.setLocationId(stockTaskItem.getLocationId());
			stockPositionT.setBatch(salePosition.getBatch());

			stockPositionT.setInputDate(new Date());
			stockPositionT.setQty(pItem.getQty());
			stockPositionT.setUnitId(stockTaskItem.getUnitId());
			stockPositionT.setInputDate(new Date());

			if (stockTaskItem.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
				// 启用包
				if (pItem.getPalletId() == null) {
					stockPositionT.setPalletId(0);
				} else {
					stockPositionT.setPalletId(pItem.getPalletId());
				}
				// 启用包
				if (pItem.getPackageId() == null) {
					throw new WMSException("包装类型出错");
				}
				stockPositionT.setPackageId(pItem.getPackageId());
			} else {
				// 不启用包
				stockPositionT.setPalletId(0);
				stockPositionT.setPackageId(0);
			}

			stockPositionT.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
			
			stockPositionT.setPackageTypeId(stockTaskItem.getPackageTypeId());

			if (stockTaskItem.getReferReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())
					|| stockTaskItem.getReferReceiptType()
							.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())) {
				// 紧急 作业生产转运 紧急作业销售出库 假库存
				stockPositionT.setIsUrgent(EnumIsUrgent.URGENT.getValue());
			}
			stockPositionT.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			
			commonService.modifyStockPosition(stockPositionT);
			
			
			// 扣减发出批次库存
			Map<String, Object> outMap = new HashMap<String, Object>();
			outMap.put("matId", stockPosition.getMatId());
			outMap.put("locationId", stockPosition.getLocationId());
			outMap.put("batch", stockPosition.getBatch());
			outMap.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			outMap.put("createUserId", "admin");
			outMap.put("debitCredit", "H");
			outMap.put("qty", stockPosition.getQty());
			outMap.put("stockTypeId",EnumStockType.STOCK_TYPE_ERP.getValue());
			commonService.modifyStockBatch(outMap);
			
			// 增加已售未提库存
			Map<String, Object> inMap = new HashMap<String, Object>();
			inMap.put("matId", stockPositionT.getMatId());
			inMap.put("locationId", stockPositionT.getLocationId());
			inMap.put("batch", stockPositionT.getBatch());
			inMap.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			inMap.put("createUserId", "admin");
			inMap.put("debitCredit", "S");
			inMap.put("qty", stockPositionT.getQty());
			inMap.put("stockTypeId",EnumStockType.STOCK_TYPE_ERP.getValue());
			commonService.modifyStockBatch(inMap);
			
		}
	
	}

	/**
	 * 删除作业单时修改库存
	 * 
	 * @param stockTaskItem
	 * @throws Exception
	 */
	void updateStockPositionByDelete(BizStockTaskItemCw stockTaskItem, BizStockTaskPositionCw pItem) throws Exception {

		if (stockTaskItem == null || pItem == null) {
			throw new WMSException("作业单行项目异常");
		}

		// 原仓位
		StockPosition stockPosition = new StockPosition();

		stockPosition.setWhId(stockTaskItem.getWhId());
		stockPosition.setAreaId(pItem.getSourceAreaId());
		stockPosition.setPositionId(pItem.getSourcePositionId());
		stockPosition.setMatId(stockTaskItem.getMatId());
		stockPosition.setFtyId(stockTaskItem.getFtyId());
		stockPosition.setLocationId(stockTaskItem.getLocationId());
		stockPosition.setBatch(stockTaskItem.getBatch());

		stockPosition.setInputDate(new Date());
		stockPosition.setQty(pItem.getQty());
		stockPosition.setUnitId(stockTaskItem.getUnitId());
		stockPosition.setInputDate(new Date());

		if (stockTaskItem.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
			// 启用包
			if (pItem.getPalletId() == null) {
				stockPosition.setPalletId(0);
			} else {
				stockPosition.setPalletId(pItem.getPalletId());

			}

			// 启用包
			if (pItem.getPackageId() == null) {
				throw new WMSException("包装类型出错");
			}
			stockPosition.setPackageId(pItem.getPackageId());
		} else {
			// 不启用包
			stockPosition.setPalletId(0);
			stockPosition.setPackageId(0);
		}

		stockPosition.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
		stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
		stockPosition.setPackageTypeId(stockTaskItem.getPackageTypeId());
		if (stockTaskItem.getReferReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())
				|| stockTaskItem.getReferReceiptType()
						.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())) {
			// 紧急 作业生产转运 紧急作业销售出库 假库存
			stockPosition.setIsUrgent(EnumIsUrgent.URGENT.getValue());
			
		}
		commonService.modifyStockPositionNotNull(stockPosition);
		
		

		// 目标仓位
		StockPosition stockPositionT = new StockPosition();

		stockPositionT.setWhId(stockTaskItem.getWhId());
		stockPositionT.setAreaId(pItem.getTargetAreaId());
		stockPositionT.setPositionId(pItem.getTargetPositionId());
		stockPositionT.setMatId(stockTaskItem.getMatId());
		stockPositionT.setFtyId(stockTaskItem.getFtyId());
		stockPositionT.setLocationId(stockTaskItem.getLocationId());
		stockPositionT.setBatch(stockTaskItem.getBatch());

		stockPositionT.setInputDate(new Date());
		stockPositionT.setQty(pItem.getQty());
		stockPositionT.setUnitId(stockTaskItem.getUnitId());
		stockPositionT.setInputDate(new Date());

		if (stockTaskItem.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
			// 启用包
			if (pItem.getPalletId() == null) {
				stockPositionT.setPalletId(0);
			} else {
				stockPositionT.setPalletId(pItem.getPalletId());
			}
			// 启用包
			if (pItem.getPackageId() == null) {
				throw new WMSException("包装类型出错");
			}
			stockPositionT.setPackageId(pItem.getPackageId());
		} else {
			// 不启用包
			stockPositionT.setPalletId(0);
			stockPositionT.setPackageId(0);
		}

		stockPositionT.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
		stockPositionT.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
		stockPositionT.setPackageTypeId(stockTaskItem.getPackageTypeId());

		if (stockTaskItem.getReferReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())
				|| stockTaskItem.getReferReceiptType()
						.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())) {
			// 紧急 作业生产转运 紧急作业销售出库 假库存
			stockPositionT.setIsUrgent(EnumIsUrgent.URGENT.getValue());
		}
		
			
		commonService.modifyStockPositionNotNull(stockPositionT);
			
		

	}

	void unfreezeStock(BizStockTaskHeadCw stockTaskHead) throws Exception {
		Byte referReceiptType = stockTaskHead.getReferReceiptType();
		Byte receiptType = stockTaskHead.getReceiptType();

		if ((referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_OTHER.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_ALLOCATE_CARGO.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_FACTORY.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_STOCK.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue()))

				&& receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
			for (BizStockTaskItemCw stockTaskItem : stockTaskHead.getItemList()) {
				BigDecimal qty = new BigDecimal(0);
				for (BizStockTaskPositionCw pItem : stockTaskItem.getPalletPackageList()) {
					qty = qty.add(pItem.getQty());
				}
				StockOccupancy stockOccupancy = new StockOccupancy();
				stockOccupancy.setBatch(stockTaskItem.getBatch());

				stockOccupancy.setFtyId(stockTaskItem.getFtyId());
				stockOccupancy.setLocationId(stockTaskItem.getLocationId());
				stockOccupancy.setMatId(stockTaskItem.getMatId());
				stockOccupancy.setQty(qty);
				if (referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())) {
					stockOccupancy.setIsUrgent(EnumIsUrgent.URGENT.getValue());
				}
				stockOccupancy.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());

				commonService.modifyStockOccupancy(stockOccupancy, EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			}
		}
	}

	void freezeStock(BizStockTaskHeadCw stockTaskHead, BizStockTaskItemCw stockTaskItem, BizStockTaskPositionCw pItem)
			throws Exception {
		Byte referReceiptType = stockTaskHead.getReferReceiptType();
		Byte receiptType = stockTaskHead.getReceiptType();
		Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
		DicStockLocationVo locationObj = locationMap.get(stockTaskHead.getLocationId());

		
		if ((referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_OTHER.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_ALLOCATE_CARGO.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_FACTORY.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_STOCK.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue()))

				&& receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {

			StockOccupancy stockOccupancy = new StockOccupancy();
			stockOccupancy.setBatch(stockTaskItem.getBatch());

			stockOccupancy.setFtyId(stockTaskItem.getFtyId());
			stockOccupancy.setLocationId(stockTaskItem.getLocationId());
			stockOccupancy.setMatId(stockTaskItem.getMatId());
			stockOccupancy.setQty(pItem.getQty());
			if (referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())) {
				stockOccupancy.setIsUrgent(EnumIsUrgent.URGENT.getValue());
			}
			stockOccupancy.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
			stockOccupancy.setPackageTypeId(stockTaskItem.getPackageTypeId());
			if (locationObj.getFtyType().equals(EnumFtyType.PRODUCT.getValue()) &&(referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_FACTORY.getValue())
					|| referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_STOCK.getValue())
					|| referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue()
					
					))) {
				
			}else {
				commonService.modifyStockOccupancy(stockOccupancy, EnumDebitCredit.DEBIT_S_ADD.getName());
			}
			
		}

	}

	// 更改前续单据状态
	void updateReferDoc(BizStockTaskHeadCw stockTaskHead) throws Exception {
		Byte referReceiptType = stockTaskHead.getReferReceiptType();
		Byte receiptType = stockTaskHead.getReceiptType();

		if (receiptType.equals(EnumReceiptType.STOCK_TASK_GROUP.getValue())) {
			// 组盘上架
			Map<Integer, Byte> reqIdMap = new HashMap<Integer, Byte>();
			for (BizStockTaskItemCw item : stockTaskHead.getItemList()) {
				if (item.getReferReceiptType().equals(EnumReceiptType.STOCK_INPUT_OTHER.getValue())) {
					reqIdMap.put(item.getReferReceiptId(), item.getReferReceiptType());
				}
			}
			if (!reqIdMap.isEmpty()) {
				for (Integer key : reqIdMap.keySet()) {
					if (reqIdMap.get(key).equals(EnumReceiptType.STOCK_INPUT_OTHER.getValue())) {
						// 其他入库
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("referReceiptId", key);
						map.put("referReceiptType", reqIdMap.get(key));
						Byte minStatus = bizStockTaskReqHeadDao.selectMinStatusByReferIdAndType(map);
						if (minStatus != null && minStatus.equals(EnumReceiptStatus.RECEIPT_FINISH.getValue())) {
							// 所有请求已完成 更新入库单状态 已作业
							BizStockInputHead stockInputHead = new BizStockInputHead();
							stockInputHead.setStockInputId(key);
							stockInputHead.setStatus(EnumReceiptStatus.RECEIPT_WORK.getValue());
							stockInputHeadDao.updateByPrimaryKeySelective(stockInputHead);
						}
					} else if (reqIdMap.get(key).equals(EnumReceiptType.STOCK_RETURN_SALE.getValue())) {
						// 销售退库
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("referReceiptId", key);
						map.put("referReceiptType", reqIdMap.get(key));
						Byte minStatus = bizStockTaskReqHeadDao.selectMinStatusByReferIdAndType(map);
						if (minStatus != null && minStatus.equals(EnumReceiptStatus.RECEIPT_FINISH.getValue())) {
							// 所有请求已完成 更新入库单状态 已作业
							BizStockOutputReturnHead head = new BizStockOutputReturnHead();
							head.setReturnId(key);
							head.setStatus(EnumReceiptStatus.RECEIPT_WORK.getValue());
							StockOutputReturnHeadDao.updateByPrimaryKeySelective(head);
						}

					}

				}
			}

		} else if (referReceiptType.equals(EnumReceiptType.STOCK_INPUT_OTHER.getValue())
				&& receiptType.equals(EnumReceiptType.STOCK_TASK_LISTING.getValue())) {
			// 其他入库
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("referReceiptId", stockTaskHead.getReferReceiptId());
			map.put("referReceiptType", stockTaskHead.getReferReceiptType());
			Byte minStatus = bizStockTaskReqHeadDao.selectMinStatusByReferIdAndType(map);
			if (minStatus != null && minStatus.equals(EnumReceiptStatus.RECEIPT_FINISH.getValue())) {
				// 所有请求已完成 更新入库单状态 已作业
				BizStockInputHead stockInputHead = new BizStockInputHead();
				stockInputHead.setStockInputId(stockTaskHead.getReferReceiptId());
				stockInputHead.setStatus(EnumReceiptStatus.RECEIPT_WORK.getValue());
				stockInputHeadDao.updateByPrimaryKeySelective(stockInputHead);
			}

		} else if (referReceiptType.equals(EnumReceiptType.STOCK_RETURN_SALE.getValue())
				&& receiptType.equals(EnumReceiptType.STOCK_TASK_LISTING.getValue())) {
			// 销售退库
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("referReceiptId", stockTaskHead.getReferReceiptId());
			map.put("referReceiptType", stockTaskHead.getReferReceiptType());
			Byte minStatus = bizStockTaskReqHeadDao.selectMinStatusByReferIdAndType(map);
			if (minStatus != null && minStatus.equals(EnumReceiptStatus.RECEIPT_FINISH.getValue())) {
				// 所有请求已完成 更新入库单状态 已作业
				BizStockOutputReturnHead head = new BizStockOutputReturnHead();
				head.setReturnId(stockTaskHead.getReferReceiptId());
				head.setStatus(EnumReceiptStatus.RECEIPT_WORK.getValue());
				StockOutputReturnHeadDao.updateByPrimaryKeySelective(head);
			}

		} else if ((referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_FACTORY.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_STOCK.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue()))
				&& receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
			// 转储单的下架作业单 生成上架请求
			this.addStockTaskReqByTransport(stockTaskHead);
		} else if ((referReceiptType.equals(EnumReceiptType.STOCK_INPUT_TRANSPORT_ERP.getValue()))
				&& receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
			// 转储入库冲销
			this.addStockTaskReqByTransportWriteOff(stockTaskHead);
		} else if ((referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_OTHER.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue()))

				&& receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
			// 其它出库,报废出库,紧急作业销售出库,紧急记账销售出库

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("referReceiptId", stockTaskHead.getReferReceiptId());
			map.put("referReceiptType", stockTaskHead.getReferReceiptType());
			Byte minStatus = bizStockTaskReqHeadDao.selectMinStatusByReferIdAndType(map);
			if (minStatus != null && minStatus.equals(EnumReceiptStatus.RECEIPT_FINISH.getValue())) {
				// 所有请求已完成 其它出库状态 已作业
				BizStockOutputHead stockOutput = new BizStockOutputHead();
				stockOutput.setStockOutputId(stockTaskHead.getReferReceiptId());
				stockOutput.setStatus(EnumReceiptStatus.RECEIPT_WORK.getValue());
				stockOutputHeadDao.updateByPrimaryKeySelective(stockOutput);
			}

		} else if (referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue())
				&& receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
			// 报废出库

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("referReceiptId", stockTaskHead.getReferReceiptId());
			map.put("referReceiptType", stockTaskHead.getReferReceiptType());
			Byte minStatus = bizStockTaskReqHeadDao.selectMinStatusByReferIdAndType(map);
			if (minStatus != null && minStatus.equals(EnumReceiptStatus.RECEIPT_FINISH.getValue())) {
				// 所有请求已完成 其它出库状态 已作业
				BizStockOutputHead stockOutput = new BizStockOutputHead();
				stockOutput.setStockOutputId(stockTaskHead.getReferReceiptId());
				stockOutput.setStatus(EnumReceiptStatus.RECEIPT_WORK.getValue());
				stockOutputHeadDao.updateByPrimaryKeySelective(stockOutput);
			}

		}

		else if (referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_ALLOCATE_CARGO.getValue())
				&& receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
			// 配货 单

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("referReceiptId", stockTaskHead.getReferReceiptId());
			map.put("referReceiptType", stockTaskHead.getReferReceiptType());
			Byte minStatus = bizStockTaskReqHeadDao.selectMinStatusByReferIdAndType(map);
			if (minStatus != null && minStatus.equals(EnumReceiptStatus.RECEIPT_FINISH.getValue())) {
				// 所有请求已完成 更新入库单状态 已作业
				BizAllocateCargoHead cargoHead = new BizAllocateCargoHead();

				cargoHead.setAllocateCargoId(stockTaskHead.getReferReceiptId());
				cargoHead.setStatus(EnumReceiptStatus.RECEIPT_WORK.getValue());
				allocateCargoHeadDao.updateByPrimaryKeySelective(cargoHead);
			}

		}

	}

	// 更改前续单据状态
	void updateReferDocByDelete(BizStockTaskHeadCw stockTaskHead) throws Exception {
		Byte referReceiptType = stockTaskHead.getReferReceiptType();
		Byte receiptType = stockTaskHead.getReceiptType();

		if (receiptType.equals(EnumReceiptType.STOCK_TASK_GROUP.getValue())) {

		} else if (referReceiptType.equals(EnumReceiptType.STOCK_INPUT_OTHER.getValue())
				&& receiptType.equals(EnumReceiptType.STOCK_TASK_LISTING.getValue())) {
			// 其他入库

			// 所有请求已完成 更新入库单状态 已作业
			BizStockInputHead stockInputHead = new BizStockInputHead();
			stockInputHead.setStockInputId(stockTaskHead.getReferReceiptId());
			stockInputHead.setStatus(EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
			stockInputHeadDao.updateByPrimaryKeySelective(stockInputHead);

		} else if (referReceiptType.equals(EnumReceiptType.STOCK_RETURN_SALE.getValue())
				&& receiptType.equals(EnumReceiptType.STOCK_TASK_LISTING.getValue())) {
			// 销售退库
			// 所有请求已完成 更新入库单状态 已作业
			BizStockOutputReturnHead head = new BizStockOutputReturnHead();
			head.setReturnId(stockTaskHead.getReferReceiptId());
			head.setStatus(EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
			StockOutputReturnHeadDao.updateByPrimaryKeySelective(head);

		} else if ((referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_OTHER.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue()))

				&& receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
			// 其它出库,报废出库,紧急作业销售出库,紧急记账销售出库

			// 所有请求已完成 其它出库状态 已作业
			BizStockOutputHead stockOutput = new BizStockOutputHead();
			stockOutput.setStockOutputId(stockTaskHead.getReferReceiptId());
			stockOutput.setStatus(EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
			stockOutputHeadDao.updateByPrimaryKeySelective(stockOutput);

		} else if (referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue())
				&& receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
			// 报废出库

			// 所有请求已完成 其它出库状态 已作业
			BizStockOutputHead stockOutput = new BizStockOutputHead();
			stockOutput.setStockOutputId(stockTaskHead.getReferReceiptId());
			stockOutput.setStatus(EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
			stockOutputHeadDao.updateByPrimaryKeySelective(stockOutput);

		}

		else if (referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_ALLOCATE_CARGO.getValue())
				&& receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
			// 配货 单

			// 所有请求已完成 更新入库单状态 已作业
			BizAllocateCargoHead cargoHead = new BizAllocateCargoHead();
			cargoHead.setAllocateCargoId(stockTaskHead.getReferReceiptId());
			cargoHead.setStatus(EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
			allocateCargoHeadDao.updateByPrimaryKeySelective(cargoHead);

		}

	}

	@SuppressWarnings("unchecked")
	void addStockTaskReqByTransportWriteOff(BizStockTaskHeadCw stockTaskHead) throws Exception {

		Map<Integer, Object> locationIdMap = new HashMap<Integer, Object>();
		List<BizStockTaskReqItem> addItemList = new ArrayList<BizStockTaskReqItem>();

		List<BizStockTransportItemCw> transportItemList = stockTransportItemCwDao
				.selectByStockTaskIdWriteOff(stockTaskHead.getStockTaskId());
		Map<String, BizStockTransportItemCw> transportItemMap = new HashMap<String, BizStockTransportItemCw>();
		if (transportItemList != null && transportItemList.size() > 0) {
			for (BizStockTransportItemCw item : transportItemList) {
				String key = item.getStockInputId()+ "-" + item.getStockInputRid();
				transportItemMap.put(key, item);
			}
		} else {
			throw new WMSException("转储单数据异常");
		}

		if (stockTaskHead.getItemList() != null && stockTaskHead.getItemList().size() > 0) {
			for (BizStockTaskItemCw item : stockTaskHead.getItemList()) {
				String key = item.getReferReceiptId() + "-" + item.getReferReceiptRid();
				BizStockTransportItemCw transportItem = transportItemMap.get(key);
				Integer locationId = transportItem.getLocationOutput();

				// -------生成作业请求单-----相同的库存地点生成一张作业单----------------

				if (locationIdMap.containsKey(locationId)) {
					// 含有相同的库存地
					Map<String, Object> headInfo = new HashMap<String, Object>();

					headInfo = (Map<String, Object>) locationIdMap.get(locationId);

					int stockTaskReqRid = (int) headInfo.get("stockTaskReqRid");
					stockTaskReqRid = stockTaskReqRid + 1;
					headInfo.put("stockTaskReqRid", stockTaskReqRid);

					BizStockTaskReqItem reqItem = new BizStockTaskReqItem();

					this.addStockTaskReqItemWriteOff(headInfo, transportItem, item, reqItem);

					addItemList.add(reqItem);

				} else {
					// --------------------- 插入抬头

					Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
					DicStockLocationVo locationObj = locationMap.get(locationId);

					Integer whId = locationObj.getWhId();

					BizStockTaskReqHead reqhead = new BizStockTaskReqHead();
					String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());
					reqhead.setStockTaskReqCode(stockTaskReqCode);
					reqhead.setFtyId(locationObj.getFtyId());
					reqhead.setLocationId(locationId);
					reqhead.setWhId(whId);
					reqhead.setCreateUser(stockTaskHead.getCreateUser());
					reqhead.setShippingType(EnumTaskReqShippingType.STOCK_INPUT.getValue());
					reqhead.setReceiptType(EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue());
					reqhead.setReferReceiptCode(stockTaskHead.getReferReceiptCode());
					reqhead.setReferReceiptId(stockTaskHead.getReferReceiptId());
					reqhead.setReferReceiptType(stockTaskHead.getReferReceiptType());
					reqhead.setRemoveTaskId(stockTaskHead.getStockTaskId());
					StockTaskReqHeadDao.insertSelective(reqhead);

					Map<String, Object> headInfo = new HashMap<String, Object>();

					if (locationObj.getFtyType().equals(EnumFtyType.PRODUCT.getValue())) {
						// 生产工厂 生成上架作业
						BizStockTaskHeadCw stockTaskHeadS = new BizStockTaskHeadCw();
						stockTaskHeadS.setReceiptType(EnumReceiptType.STOCK_TASK_LISTING.getValue());
						this.addStockTaskHeadByReq(stockTaskHeadS, reqhead);

						headInfo.put("stockTaskHead", stockTaskHeadS);// 作业申请序号

					}

					// --------------end
					int stockTaskReqRid = 1;
					int stockTaskReqId = reqhead.getStockTaskReqId();

					headInfo.put("stockTaskReqId", stockTaskReqId);// 仓库号
					headInfo.put("whId", whId);// 作业申请号
					headInfo.put("stockTaskReqRid", stockTaskReqRid);// 作业申请序号
					locationIdMap.put(locationId, headInfo);

					BizStockTaskReqItem reqItem = new BizStockTaskReqItem();

					this.addStockTaskReqItemWriteOff(headInfo, transportItem, item, reqItem);

					addItemList.add(reqItem);

				}
			}
			StockTaskReqItemDao.insertReqItems(addItemList);
		} else {
			throw new WMSException("缺少行项目");
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void addStockTaskReqByTransport(BizStockTaskHeadCw stockTaskHead) throws Exception {

		List<BizReceiptAttachmentVo> fileList = fileService.getReceiptAttachments(stockTaskHead.getStockTaskReqId(),
				stockTaskHead.getReceiptType());

		BizStockTaskReqHead removeReqHead = StockTaskReqHeadDao.selectByPrimaryKey(stockTaskHead.getStockTaskReqId());
		
		Map<Integer, Object> locationIdMap = new HashMap<Integer, Object>();
		List<BizStockTaskReqItem> addItemList = new ArrayList<BizStockTaskReqItem>();

		List<BizStockTransportItemCw> transportItemList = stockTransportItemCwDao
				.selectByStockTaskId(stockTaskHead.getStockTaskId());
		Map<String, BizStockTransportItemCw> transportItemMap = new HashMap<String, BizStockTransportItemCw>();
		if (transportItemList != null && transportItemList.size() > 0) {
			for (BizStockTransportItemCw item : transportItemList) {
				String key = item.getStockTransportId() + "-" + item.getStockTransportRid();
				transportItemMap.put(key, item);
			}
		} else {
			throw new WMSException("转储单数据异常");
		}

		if (stockTaskHead.getItemList() != null && stockTaskHead.getItemList().size() > 0) {
			for (BizStockTaskItemCw item : stockTaskHead.getItemList()) {
				String key = item.getReferReceiptId() + "-" + item.getReferReceiptRid();
				BizStockTransportItemCw transportItem = transportItemMap.get(key);
				Integer locationId = transportItem.getLocationInput();

				// -------生成作业请求单-----相同的库存地点生成一张作业单----------------

				if (locationIdMap.containsKey(locationId)) {
					// 含有相同的库存地
					Map<String, Object> headInfo = new HashMap<String, Object>();

					headInfo = (Map<String, Object>) locationIdMap.get(locationId);

					int stockTaskReqRid = (int) headInfo.get("stockTaskReqRid");
					stockTaskReqRid = stockTaskReqRid + 1;
					headInfo.put("stockTaskReqRid", stockTaskReqRid);

					BizStockTaskReqItem reqItem = new BizStockTaskReqItem();

					this.addStockTaskReqItem(headInfo, transportItem, item, reqItem);

					addItemList.add(reqItem);

				} else {
					// --------------------- 插入抬头

					Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
					DicStockLocationVo locationObj = locationMap.get(locationId);

					Integer whId = locationObj.getWhId();

					BizStockTaskReqHead reqhead = new BizStockTaskReqHead();
					String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());
					reqhead.setStockTaskReqCode(stockTaskReqCode);
					reqhead.setFtyId(locationObj.getFtyId());
					reqhead.setLocationId(locationId);
					reqhead.setWhId(whId);
					reqhead.setCreateUser(stockTaskHead.getCreateUser());
					reqhead.setShippingType(EnumTaskReqShippingType.STOCK_INPUT.getValue());
					reqhead.setReceiptType(EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue());
					reqhead.setReferReceiptCode(stockTaskHead.getReferReceiptCode());
					reqhead.setReferReceiptId(stockTaskHead.getReferReceiptId());
					reqhead.setReferReceiptType(stockTaskHead.getReferReceiptType());
					reqhead.setRemoveTaskId(stockTaskHead.getStockTaskId());
					reqhead.setDeliveryDriver(removeReqHead.getDeliveryDriver());
					reqhead.setDeliveryVehicle(removeReqHead.getDeliveryVehicle());
					StockTaskReqHeadDao.insertSelective(reqhead);
					
					
					commonService.saveFileVoList(reqhead.getStockTaskReqId(), reqhead.getReceiptType(),
							reqhead.getCreateUser(), fileList);

					Map<String, Object> headInfo = new HashMap<String, Object>();
					if (locationObj.getFtyType().equals(EnumFtyType.PRODUCT.getValue())) {
						// 生产工厂 生成上架作业
						BizStockTaskHeadCw stockTaskHeadS = new BizStockTaskHeadCw();
						stockTaskHeadS.setReceiptType(EnumReceiptType.STOCK_TASK_LISTING.getValue());
						this.addStockTaskHeadByReq(stockTaskHeadS, reqhead);

						headInfo.put("stockTaskHead", stockTaskHeadS);// 作业申请序号

					}

					// --------------end
					int stockTaskReqRid = 1;
					int stockTaskReqId = reqhead.getStockTaskReqId();

					headInfo.put("stockTaskReqId", stockTaskReqId);// 仓库号
					headInfo.put("whId", whId);// 作业申请号
					headInfo.put("stockTaskReqRid", stockTaskReqRid);// 作业申请序号

					locationIdMap.put(locationId, headInfo);

					BizStockTaskReqItem reqItem = new BizStockTaskReqItem();

					this.addStockTaskReqItem(headInfo, transportItem, item, reqItem);

					addItemList.add(reqItem);

				}
			}
			StockTaskReqItemDao.insertReqItems(addItemList);
		} else {
			throw new WMSException("缺少行项目");
		}

	}

	@Override
	public void addStockTaskHeadByReq(BizStockTaskHeadCw taskHead, BizStockTaskReqHead reqHead) throws Exception {

		// 生产工厂 生成上架作业
		String stockTaskCode = commonService.getNextReceiptCode("stock_task");
		taskHead.setStockTaskCode(stockTaskCode);
		// 未过账
		taskHead.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());

		taskHead.setCreateUser(reqHead.getCreateUser());

		taskHead.setRequestSource(reqHead.getRequestSource());
		taskHead.setSubmitDate(new Date());
		taskHead.setFtyId(reqHead.getFtyId());
		taskHead.setLocationId(reqHead.getLocationId());
		taskHead.setWhId(reqHead.getWhId());
		taskHead.setShippingType(reqHead.getShippingType());
		taskHead.setReferReceiptType(reqHead.getReferReceiptType());
		taskHead.setReferReceiptId(reqHead.getReferReceiptId());
		taskHead.setReferReceiptCode(reqHead.getReferReceiptCode());
		taskHead.setStockTaskReqId(reqHead.getStockTaskReqId());
		taskHead.setMoveTypeId(0);
		
		boolean isWriteOff = this.isWriteOff(taskHead);
		if (isWriteOff) {
			taskHead.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
		}
		
		bizStockTaskHeadCwDao.insertSelective(taskHead);

		// 更新请求状态
		BizStockTaskReqHead record = new BizStockTaskReqHead();
		record.setStockTaskReqId(reqHead.getStockTaskReqId());
		record.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
		StockTaskReqHeadDao.updateByPrimaryKeySelective(record);

	}

	@Override
	public void addStockTaskItemByReq(BizStockTaskHeadCw taskHead, BizStockTaskItemCw taskitem,
			BizStockTaskReqItem reqItem) throws Exception {
		taskitem.setItemId(null);

		taskitem.setStockTaskId(taskHead.getStockTaskId());
		taskitem.setStockTaskReqId(reqItem.getStockTaskReqId());
		taskitem.setStockTaskReqRid(reqItem.getStockTaskReqRid());
		taskitem.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
		taskitem.setWhId(taskHead.getWhId());
		taskitem.setFtyId(taskHead.getFtyId());
		taskitem.setLocationId(taskHead.getLocationId());
		taskitem.setMatId(reqItem.getMatId());
		taskitem.setUnitId(reqItem.getUnitId());
		taskitem.setBatch(reqItem.getBatch());
		taskitem.setQty(reqItem.getQty());
		taskitem.setStockTaskQty(reqItem.getStockTaskQty());

		taskitem.setPackageTypeId(reqItem.getPackageTypeId());
		taskitem.setReferReceiptType(reqItem.getReferReceiptType());
		taskitem.setReferReceiptId(reqItem.getReferReceiptId());
		taskitem.setReferReceiptCode(reqItem.getReferReceiptCode());
		taskitem.setReferReceiptRid(reqItem.getReferReceiptRid());
		taskitem.setProductionBatch(reqItem.getProductionBatch());
		taskitem.setErpBatch(reqItem.getErpBatch());
		taskitem.setQualityBatch(reqItem.getQualityBatch());
		taskitem.setPackageTypeId(reqItem.getPackageTypeId());
		
		boolean isWriteOff = this.isWriteOff(taskHead);
		if (isWriteOff) {
			taskitem.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
		}
		
		bizStockTaskItemCwDao.insertSelective(taskitem);
	}

	void addStockTaskReqItem(Map<String, Object> headMap, BizStockTransportItemCw transportItem,
			BizStockTaskItemCw item, BizStockTaskReqItem reqItem) throws Exception {

		int stockTaskReqId = (int) headMap.get("stockTaskReqId");
		int stockTaskReqRid = (int) headMap.get("stockTaskReqRid");
		int whId = (int) headMap.get("whId");
		reqItem.setBatch(item.getBatch());
		reqItem.setWhId(whId);
		reqItem.setFtyId(transportItem.getFtyInput());
		reqItem.setLocationId(transportItem.getLocationInput());
		reqItem.setMatId(transportItem.getMatInput());

		reqItem.setUnitId(item.getUnitId());
		reqItem.setQty(item.getQty());
		reqItem.setPackageTypeId(item.getPackageTypeId());
		reqItem.setStockTaskReqId(stockTaskReqId);
		reqItem.setStockTaskReqRid(stockTaskReqRid);
		reqItem.setProductionBatch(transportItem.getProductionBatchInput());
		reqItem.setErpBatch(transportItem.getErpBatchInput());
		reqItem.setQualityBatch(transportItem.getQualityBatchInput());

		reqItem.setReferReceiptCode(item.getReferReceiptCode());
		reqItem.setReferReceiptId(item.getReferReceiptId());
		reqItem.setReferReceiptType(item.getReferReceiptType());
		reqItem.setReferReceiptRid(item.getReferReceiptRid());
		BizStockTaskItemCw taskitem = new BizStockTaskItemCw();
		BizStockTaskHeadCw taskHead = null;
		if (headMap.containsKey("stockTaskHead")) {
			// 请求数量 状态 完成
			reqItem.setStockTaskQty(reqItem.getQty());
			reqItem.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
			taskHead = (BizStockTaskHeadCw) headMap.get("stockTaskHead");
			taskitem.setWorkModel(item.getWorkModel());
			taskitem.setMatStoreType(item.getMatStoreType());

			int stockTaskRid = UtilObject.getIntOrZero(headMap.get("stockTaskRid"));
			stockTaskRid = stockTaskRid + 1;
			headMap.put("stockTaskRid", stockTaskRid);

			taskitem.setStockTaskRid(stockTaskRid);
			this.addStockTaskItemByReq(taskHead, taskitem, reqItem);

		}
		String whCode = dictionaryService.getWhCodeByWhId(whId);
		Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
				UtilProperties.getInstance().getDefaultCCQ());
		Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
				UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());

		if (item.getPalletPackageList() != null && item.getPalletPackageList().size() > 0) {
			// 启用包
			for (BizStockTaskPositionCw tp : item.getPalletPackageList()) {

				int i = 1;
				if (tp.getPackageId() != null && tp.getPackageId() > 0) {
					BizStockTaskReqPosition reqPosition = new BizStockTaskReqPosition();
					reqPosition.setStockTaskReqId(stockTaskReqId);
					reqPosition.setStockTaskReqRid(stockTaskReqRid);
					reqPosition.setStockTaskReqPositionId(i);
					i++;
					reqPosition.setBatch(item.getBatch());
					reqPosition.setPalletId(tp.getPalletId());
					reqPosition.setPackageId(tp.getPackageId());
					reqPosition.setQty(tp.getQty());
					StockTaskReqPositionDao.insertSelective(reqPosition);
				}
				if (headMap.containsKey("stockTaskHead")) {
					BizStockTaskPositionCw poItem = new BizStockTaskPositionCw();
					poItem.setBatch(item.getBatch());

					poItem.setStockTaskId(taskitem.getStockTaskId());
					poItem.setStockTaskRid(taskitem.getStockTaskRid());
					poItem.setStockTaskReqId(taskitem.getStockTaskReqId());
					poItem.setStockTaskReqRid(taskitem.getStockTaskReqRid());
					poItem.setBatch(taskitem.getBatch());
					poItem.setQty(tp.getQty());
					poItem.setPalletId(tp.getPalletId());
					poItem.setPackageId(tp.getPackageId());
					poItem.setSourceAreaId(sourceAreaId);
					poItem.setSourcePositionId(sourcePositionId);
					poItem.setTargetAreaId(sourceAreaId);
					poItem.setTargetPositionId(sourcePositionId);
					poItem.setWhId(taskitem.getWhId());
					bizStockTaskPositionCwDao.insertSelective(poItem);

					// 插入历史表
					this.addHistory(taskHead, taskitem, poItem);
				}

			}
		}

	}

	void addStockTaskReqItemWriteOff(Map<String, Object> headMap, BizStockTransportItemCw transportItem,
			BizStockTaskItemCw item, BizStockTaskReqItem reqItem) throws Exception {

		int stockTaskReqId = (int) headMap.get("stockTaskReqId");
		int stockTaskReqRid = (int) headMap.get("stockTaskReqRid");
		int whId = (int) headMap.get("whId");
		reqItem.setBatch(item.getBatch());
		reqItem.setWhId(whId);
		reqItem.setFtyId(transportItem.getFtyOutput());
		reqItem.setLocationId(transportItem.getLocationOutput());
		reqItem.setMatId(transportItem.getMatId());

		reqItem.setUnitId(item.getUnitId());
		reqItem.setQty(item.getQty());
		reqItem.setPackageTypeId(item.getPackageTypeId());
		reqItem.setStockTaskReqId(stockTaskReqId);
		reqItem.setStockTaskReqRid(stockTaskReqRid);
		reqItem.setProductionBatch(transportItem.getProductionBatch());
		reqItem.setErpBatch(transportItem.getErpBatch());
		reqItem.setQualityBatch(transportItem.getQualityBatch());

		reqItem.setReferReceiptCode(item.getReferReceiptCode());
		reqItem.setReferReceiptId(item.getReferReceiptId());
		reqItem.setReferReceiptType(item.getReferReceiptType());
		reqItem.setReferReceiptRid(item.getReferReceiptRid());

		BizStockTaskItemCw taskitem = new BizStockTaskItemCw();
		BizStockTaskHeadCw taskHead = null;
		if (headMap.containsKey("stockTaskHead")) {
			// 请求数量 状态 完成
			reqItem.setStockTaskQty(reqItem.getQty());
			reqItem.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
			taskHead = (BizStockTaskHeadCw) headMap.get("stockTaskHead");
			taskitem.setWorkModel(item.getWorkModel());
			taskitem.setMatStoreType(item.getMatStoreType());

			int stockTaskRid = UtilObject.getIntOrZero(headMap.get("stockTaskRid"));
			stockTaskRid = stockTaskRid + 1;
			headMap.put("stockTaskRid", stockTaskRid);

			taskitem.setStockTaskRid(stockTaskRid);
			this.addStockTaskItemByReq(taskHead, taskitem, reqItem);

		}
		String whCode = dictionaryService.getWhCodeByWhId(whId);
		Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
				UtilProperties.getInstance().getDefaultCCQ());
		Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
				UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());

		if (item.getPalletPackageList() != null && item.getPalletPackageList().size() > 0) {
			// 启用包
			for (BizStockTaskPositionCw tp : item.getPalletPackageList()) {

				int i = 1;
				if (tp.getPackageId() != null && tp.getPackageId() > 0) {
					BizStockTaskReqPosition reqPosition = new BizStockTaskReqPosition();
					reqPosition.setStockTaskReqId(stockTaskReqId);
					reqPosition.setStockTaskReqRid(stockTaskReqRid);
					reqPosition.setStockTaskReqPositionId(i);
					i++;
					reqPosition.setBatch(item.getBatch());
					reqPosition.setPalletId(tp.getPalletId());
					reqPosition.setPackageId(tp.getPackageId());
					reqPosition.setQty(tp.getQty());
					StockTaskReqPositionDao.insertSelective(reqPosition);
				}
				if (headMap.containsKey("stockTaskHead")) {
					BizStockTaskPositionCw poItem = new BizStockTaskPositionCw();
					poItem.setBatch(item.getBatch());

					poItem.setStockTaskId(taskitem.getStockTaskId());
					poItem.setStockTaskRid(taskitem.getStockTaskRid());
					poItem.setStockTaskReqId(taskitem.getStockTaskReqId());
					poItem.setStockTaskReqRid(taskitem.getStockTaskReqRid());
					poItem.setBatch(taskitem.getBatch());
					poItem.setQty(tp.getQty());
					poItem.setPalletId(tp.getPalletId());
					poItem.setPackageId(tp.getPackageId());
					poItem.setSourceAreaId(sourceAreaId);
					poItem.setSourcePositionId(sourcePositionId);
					poItem.setTargetAreaId(sourceAreaId);
					poItem.setTargetPositionId(sourcePositionId);
					poItem.setWhId(taskitem.getWhId());
					bizStockTaskPositionCwDao.insertSelective(poItem);

					// 插入历史表
					this.addHistory(taskHead, taskitem, poItem);
				}
			}
		}

	}

	@Override
	public HashMap<String, Object> addBizStockTaskHead(BizStockTaskHeadCw stockTaskHead) throws Exception {

		initStockTask(stockTaskHead);

		commonService.saveFileList(stockTaskHead.getStockTaskId(), stockTaskHead.getReceiptType(),
				stockTaskHead.getCreateUser(), stockTaskHead.getFileList());
		updateStockPosition(stockTaskHead);
		updateReferDoc(stockTaskHead);
		this.unfreezeStock(stockTaskHead);
		return null;
	}
	
	@Override
	public Map<String, Object> addStockTaskByGroup(BizStockTaskHeadCw head) throws Exception {

		initStockTaskByGroup(head);
		
		commonService.saveFileList(head.getStockTaskId(), head.getReceiptType(),
				head.getCreateUser(), head.getFileList());
		updateStockPosition(head);
		updateReferDoc(head);
		return null;
	}

	@Override
	public List<Map<String, Object>> getBizStockTaskHeadListForCW(Map<String, Object> map) {
		return bizStockTaskHeadCwDao.getBizStockTaskHeadListForCWOnPaging(map);
	}

	@Override
	public List<Map<String, Object>> getBizStockTaskReqHeadListForCW(Map<String, Object> map) {
		return bizStockTaskReqHeadDao.getBizStockTaskReqHeadListForCWOnPaging(map);
	}

	// 前续单据
	boolean checkPalletCodeOnReq(JSONObject json, JSONObject returnObj, boolean flag) throws Exception {

		if (!flag) {
			// 1-检验前续单据
			Integer itemId = UtilJSON.getIntFromJSON("item_id", json);
			String condition = UtilJSON.getStringOrEmptyFromJSON("condition", json);
			condition = condition.trim();
			Map<String, Object> reqMap = new HashMap<String, Object>();
			// reqMap.put("status",
			// EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
			reqMap.put("itemId", itemId);
			// reqMap.put("palletCode", condition);
			List<BizStockTaskReqPosition> reqPositionList = stockTaskReqPositionDao.selectByCondition(reqMap);

			if (reqPositionList != null && reqPositionList.size() > 0) {
				// 有前续单据

				BizStockTaskReqPosition trp = reqPositionList.get(0);
				returnObj.put("type", 1);
				returnObj.put("pallet_id", trp.getPalletId());
				returnObj.put("pallet_code", trp.getPalletCode());
				returnObj.put("max_payload", trp.getMaxWeight());
				BigDecimal have_payload = new BigDecimal(0);
				int package_num = 0;
				JSONArray package_list = new JSONArray();
				for (BizStockTaskReqPosition item : reqPositionList) {
					if (item.getPalletCode() != null && item.getPalletCode().equals(condition)
							&& item.getStatus().equals(EnumReceiptStatus.RECEIPT_UNFINISH.getValue())) {
						have_payload = have_payload.add(item.getQty());
						package_num++;
						JSONObject obj = new JSONObject();
						obj.put("package_id", item.getPackageId());
						obj.put("package_code", item.getPackageCode());
						obj.put("qty", item.getQty());
						obj.put("package_type_id", item.getPackageTypeId());
						obj.put("package_type_code", item.getPackageTypeCode());
						obj.put("unit_zh", item.getUnitZh());
						package_list.add(obj);
					}

				}
				returnObj.put("have_payload", 0);
				returnObj.put("package_num", package_num);
				returnObj.put("package_list", package_list);
				if (package_list.size() > 0) {
					flag = true;
				} 
//				else {
//					throw new WMSException("无效托盘");
//				}

			} else {
				// 无前续单据
			}
		}
		return flag;
	}

	// 仓位库存
	boolean checkPalletCodeOnPosition(Map<String, Object> palletMap, JSONObject returnObj, boolean flag)
			throws Exception {

		if (!flag) {
			// 2-校验仓位库存

			boolean havePackageList = false;
			if (palletMap.containsKey("havePackageList")) {
				havePackageList = (boolean) palletMap.get("havePackageList");
			}

			List<StockPosition> spList = stockPositionMapper.selectByPalletCodeOrPackageCode(palletMap);
			if (spList != null && spList.size() > 0) {
				StockPosition sp = spList.get(0);
				int locationId = UtilObject.getIntOrZero(palletMap.get("locationId"));
				int sLocationId = sp.getLocationId();
				if (locationId != sLocationId) {
					throw new WMSException("目标托盘不在当前库存地点下");
				}
				returnObj.put("type", 1);
				returnObj.put("pallet_id", sp.getPalletId());
				returnObj.put("pallet_code", sp.getPalletCode());
				returnObj.put("max_payload", sp.getMaxWeight());
				BigDecimal have_payload = new BigDecimal(0);
				int package_num = 0;
				JSONArray package_list = new JSONArray();
				for (StockPosition item : spList) {
					have_payload = have_payload.add(item.getQty());
					package_num++;
					if (havePackageList) {

						JSONObject obj = new JSONObject();
						obj.put("package_id", item.getPackageId());
						obj.put("package_code", item.getPackageCode());

						obj.put("qty", item.getQty());
						obj.put("package_type_id", item.getPackageTypeId());
						obj.put("package_type_code", item.getPackageTypeCode());
						obj.put("unit_zh", item.getUnitZh());
						package_list.add(obj);
					}

				}
				if (package_list.size() > 0) {
					returnObj.put("package_list", package_list);
				}
				returnObj.put("have_payload", have_payload);
				returnObj.put("package_num", package_num);
				returnObj.put("wh_id", sp.getWhId());
				returnObj.put("area_id", sp.getAreaId());
				returnObj.put("area_code", sp.getAreaCode());
				returnObj.put("position_id", sp.getPositionId());
				returnObj.put("position_code", sp.getPositionCode());
				flag = true;
			}

		}
		return flag;
	}

	// 关联单
	boolean checkPalletCodeOnOperation(JSONObject json, JSONObject returnObj, boolean flag) throws Exception {

		if (!flag) {
			// 校验关联单

			String condition = UtilJSON.getStringOrEmptyFromJSON("condition", json);
			Integer matId = UtilJSON.getIntFromJSON("mat_id", json);
			Map<String, Object> pkgOperationMap = new HashMap<String, Object>();
			pkgOperationMap.put("palletCode", condition);
			pkgOperationMap.put("matId", matId);
			if (json.containsKey("package_type_id")) {
				Integer packageTypeId = json.getInt("package_type_id");
				pkgOperationMap.put("packageTypeId", packageTypeId);
			}
			if (json.containsKey("production_batch")) {
				String productionBatch = json.getString("production_batch");
				pkgOperationMap.put("productionBatch", productionBatch);
			}

			List<BizPkgOperationPositionVo> pList = pkgOperationPositionDao
					.selectByPalletCodeOrPackageCode(pkgOperationMap);
			if (pList != null && pList.size() > 0) {
				BizPkgOperationPositionVo sp = pList.get(0);
				returnObj.put("type", 1);
				returnObj.put("pallet_id", sp.getPalletId());
				returnObj.put("pallet_code", sp.getPalletCode());
				returnObj.put("max_payload", sp.getMaxWeight());
				BigDecimal have_payload = new BigDecimal(0);
				int package_num = 0;
				JSONArray package_list = new JSONArray();
				List<Integer> packageIdList = new ArrayList<Integer>();
				for (BizPkgOperationPositionVo item : pList) {
					have_payload = have_payload.add(item.getPackageStandard());
					package_num++;
					JSONObject obj = new JSONObject();
					obj.put("package_id", item.getPackageId());
					obj.put("package_code", item.getPackageCode());
					packageIdList.add(item.getPackageId());
					obj.put("qty", item.getPackageStandard());
					obj.put("package_type_id", item.getPackageTypeId());
					obj.put("package_type_code", item.getPackageTypeCode());
					obj.put("unit_zh", item.getUnitZh());
					package_list.add(obj);
				}
				int count = businessHistoryDao.countByPackageIds(packageIdList);
				if (count == 0) {
					// 历史记录里无使用 包返回 托盘下的包
					returnObj.put("package_list", package_list);
				} else {
					// 历史记录里存在包 返回托盘
					returnObj.put("package_list", new JSONArray());
				}
				returnObj.put("have_payload", 0);
				returnObj.put("package_num", package_num);

				flag = true;
			}

		}
		return flag;
	}

	// 托盘主数据
	boolean checkPalletCodeOnMajor(JSONObject json, JSONObject returnObj, boolean flag) throws Exception {

		if (!flag) {
			// 校验托盘主数据
			String condition = UtilJSON.getStringOrEmptyFromJSON("condition", json);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("keyword", condition);
			List<DicWarehousePallet> palletList = warehousePalletDao.selectDicWarehousePalletList(param);
			if (palletList != null && palletList.size() == 1) {
				DicWarehousePallet pallet = palletList.get(0);
				returnObj.put("type", 1);
				returnObj.put("pallet_id", pallet.getPalletId());
				returnObj.put("pallet_code", pallet.getPalletCode());
				returnObj.put("max_payload", pallet.getMaxWeight());

				returnObj.put("have_payload", new BigDecimal(0));
				returnObj.put("package_num", 0);
				flag = true;
			}

		}
		return flag;
	}

	// 包 前续单据
	boolean checkPackageCodeOnReq(JSONObject json, JSONObject returnObj, boolean flag) throws Exception {

		if (!flag) {
			Integer itemId = UtilJSON.getIntFromJSON("item_id", json);
			String condition = UtilJSON.getStringOrEmptyFromJSON("condition", json);
			Map<String, Object> reqPackageMap = new HashMap<String, Object>();
			reqPackageMap.put("itemId", itemId);
			List<BizStockTaskReqPosition> reqPackagePositionList = stockTaskReqPositionDao
					.selectByCondition(reqPackageMap);
			if (reqPackagePositionList != null && reqPackagePositionList.size() > 0) {
				for (BizStockTaskReqPosition item : reqPackagePositionList) {
					if (item.getPackageCode() != null && item.getPackageCode().equals(condition)
							&& item.getStatus().equals(EnumReceiptStatus.RECEIPT_UNFINISH.getValue())) {
						returnObj.put("type", 2);
						returnObj.put("package_id", item.getPackageId());
						returnObj.put("package_code", item.getPackageCode());
						returnObj.put("qty", item.getQty());
						returnObj.put("package_type_id", item.getPackageTypeId());
						returnObj.put("package_type_code", item.getPackageTypeCode());
						returnObj.put("unit_zh", item.getUnitZh());
						flag = true;
					}
				}
				if (!flag) {
					throw new WMSException("无效包");
				}

			}
		}
		return flag;
	}

	// 包 仓位库存
	boolean checkPackageCodeOnPosition(Map<String, Object> packgeMap, JSONObject returnObj, boolean flag)
			throws Exception {

		if (!flag) {
			// 2-校验仓位库存

			List<StockPosition> packageList = stockPositionMapper.selectByPalletCodeOrPackageCode(packgeMap);
			if (packageList != null && packageList.size() > 0) {
				StockPosition sp = packageList.get(0);
				returnObj.put("type", 2);
				returnObj.put("package_id", sp.getPackageId());
				returnObj.put("package_code", sp.getPackageCode());
				returnObj.put("qty", sp.getQty());
				returnObj.put("package_type_id", sp.getPackageTypeId());
				returnObj.put("package_type_code", sp.getPackageTypeCode());
				returnObj.put("unit_zh", sp.getUnitZh());

				returnObj.put("wh_id", sp.getWhId());
				returnObj.put("area_id", sp.getAreaId());
				returnObj.put("area_code", sp.getAreaCode());
				returnObj.put("position_id", sp.getPositionId());
				returnObj.put("position_code", sp.getPositionCode());
				flag = true;
			}

		}
		return flag;
	}

	// 包 关联单
	boolean checkPackageCodeOnOperation(JSONObject json, JSONObject returnObj, boolean flag) throws Exception {

		if (!flag) {
			// 校验关联单
			String condition = UtilJSON.getStringOrEmptyFromJSON("condition", json);
			Integer matId = UtilJSON.getIntFromJSON("mat_id", json);
			Integer locationId = UtilJSON.getIntFromJSON("location_id", json);
			String debitCredit = UtilJSON.getStringOrEmptyFromJSON("debitCredit", json);
			Map<String, Object> pkgOperationpackageMap = new HashMap<String, Object>();
			pkgOperationpackageMap.put("packageCode", condition);
			pkgOperationpackageMap.put("matId", matId);
			if (json.containsKey("package_type_id")) {
				Integer packageTypeId = json.getInt("package_type_id");
				pkgOperationpackageMap.put("packageTypeId", packageTypeId);
			}
			if (json.containsKey("production_batch")) {
				String productionBatch = json.getString("production_batch");
				pkgOperationpackageMap.put("productionBatch", productionBatch);
			}
			List<BizPkgOperationPositionVo> packagePList = pkgOperationPositionDao
					.selectByPalletCodeOrPackageCode(pkgOperationpackageMap);
			if (packagePList != null && packagePList.size() > 0) {
				// 检验历史
				BizPkgOperationPositionVo sp = packagePList.get(0);
				Map<String, Object> hMap = new HashMap<String, Object>();
				hMap.put("businessType", EnumBusinessType.STOCK_TASK.getValue());
				//hMap.put("locationId", locationId);
				//hMap.put("matId", matId);
				hMap.put("packageId", sp.getPackageId());
				BizBusinessHistory bh = businessHistoryDao.selectNewestByParams(hMap);
				if (bh != null && bh.getDebitCredit().equalsIgnoreCase(debitCredit)) {
					// 不能做相同处理 上过架 不能再上架 下架不能再下架
					throw new WMSException("无效数据");
				}
				returnObj.put("type", 2);
				returnObj.put("package_id", sp.getPackageId());
				returnObj.put("package_code", sp.getPackageCode());

				returnObj.put("qty", sp.getPackageStandard());
				returnObj.put("package_type_id", sp.getPackageTypeId());
				returnObj.put("package_type_code", sp.getPackageTypeCode());
				returnObj.put("unit_zh", sp.getUnitZh());
				flag = true;
			}

		}
		return flag;
	}

	// 仓位
	boolean checkPositionCode(JSONObject json, JSONObject returnObj, boolean flag) throws Exception {

		if (!flag) {

			String condition = UtilJSON.getStringOrEmptyFromJSON("condition", json);
			// 校验仓位
			Map<Integer, DicStockLocationVo> locationIdMap = dictionaryService.getLocationIdMap();
			Integer locationId = UtilJSON.getIntFromJSON("location_id", json);
			Integer whId = locationIdMap.get(locationId).getWhId();
			Map<String, Object> areaPositionMap = UtilTwoDimensionCode.getLGTYPandLGPLA(condition);
			String areaCode = (String) areaPositionMap.get("lgtyp");
			String positionCode = (String) areaPositionMap.get("lgpla");

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("whId", whId);
			map.put("areaCode", areaCode);
			map.put("positionCode", positionCode);
			
			if (locationIdMap.get(UtilObject.getIntOrZero(locationId)).getFtyType()
					.equals(EnumFtyType.PRODUCT.getValue())) {
				map.put("isDefault", 1); // 默认存储区
			} else {
				map.put("isDefault", 0); // 非默认存储区
			}
			List<Map<String, Object>> positionList = warehousePositionDao.selectWarehousePositionOnPaging(map);
			if (positionList != null && positionList.size() == 1) {
				Map<String, Object> position = positionList.get(0);
				returnObj.put("type", 3);
				returnObj.put("wh_id", whId);
				returnObj.put("area_id", position.get("area_id"));
				returnObj.put("position_id", position.get("position_id"));
				returnObj.put("area_code", position.get("area_code"));
				returnObj.put("area_name", position.get("area_name"));
				returnObj.put("position_code", position.get("position_code"));
				flag = true;
			}
		}
		return flag;
	}

	// 仓位(下架仓位校验)
	boolean checkPositionCodeByRemove(Map<String, Object> map, JSONObject returnObj, boolean flag) throws Exception {

		if (!flag) {

			String condition = UtilString.getStringOrEmptyForObject(map.get("condition"));
			// 校验仓位
			Map<Integer, DicStockLocationVo> locationIdMap = dictionaryService.getLocationIdMap();
			Integer locationId = UtilObject.getIntegerOrNull(map.get("locationId"));
			Integer whId = locationIdMap.get(locationId).getWhId();
			String whCode = dictionaryService.getWhCodeByWhId(whId);
			Map<String, Object> areaPositionMap = UtilTwoDimensionCode.getLGTYPandLGPLA(condition);
			String areaCode = (String) areaPositionMap.get("lgtyp");
			String positionCode = (String) areaPositionMap.get("lgpla");

			Integer positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode, areaCode,
					positionCode);
			map.put("positionId", positionId);
			List<StockPosition> packageList = stockPositionMapper.selectByPalletCodeOrPackageCode(map);
			if (packageList != null && packageList.size() > 0) {
				returnObj.put("type", 3);
				returnObj.put("wh_id", packageList.get(0).getWhId());
				returnObj.put("area_id", packageList.get(0).getAreaId());
				returnObj.put("position_id", packageList.get(0).getPositionId());
				returnObj.put("area_code", packageList.get(0).getAreaCode());
				returnObj.put("area_name", packageList.get(0).getAreaName());
				returnObj.put("position_code", packageList.get(0).getPositionCode());
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public JSONArray getAreaListByLocationId(Integer locationId) throws Exception {
		Map<Integer, DicStockLocationVo> locationIdMap = dictionaryService.getLocationIdMap();
		Integer whId = locationIdMap.get(locationId).getWhId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("whId", whId);
		if (locationIdMap.get(locationId).getFtyType().equals(EnumFtyType.PRODUCT.getValue())) {
			map.put("isDefault", 1); // 默认存储区
		} else {
			map.put("isDefault", 0); // 非默认存储区
		}

		List<Map<String, Object>> positionList = warehousePositionDao.selectWarehousePositionOnPaging(map);
		return getAreaList(positionList);
	}

	@Override
	public JSONArray getAreaListByRemoval(Map<String, Object> map) throws Exception {

		if (!(map.containsKey("locationId") && map.containsKey("matId") && map.containsKey("batch"))) {
			throw new WMSException("参数异常");
		}
		Map<Integer, DicStockLocationVo> locationIdMap = dictionaryService.getLocationIdMap();
		if (locationIdMap.get(map.get("locationId")).getFtyType().equals(EnumFtyType.PRODUCT.getValue())) {
			map.put("isDefault", 1); // 默认存储区
		} else {
			map.put("isDefault", 0); // 非默认存储区
		}

		List<Map<String, Object>> positionList = stockPositionMapper.selectAreaPositionByParam(map);

		return getAreaList(positionList);
	}

	@Override
	public JSONArray getAdviseAreaListByRemoval(Map<String, Object> map) throws Exception {

		if (!(map.containsKey("locationId") && map.containsKey("matId") && map.containsKey("batch")
				&& map.containsKey("qty"))) {
			throw new WMSException("参数异常");
		}
		Map<Integer, DicStockLocationVo> locationIdMap = dictionaryService.getLocationIdMap();
		if (locationIdMap.get(UtilObject.getIntOrZero(map.get("locationId"))).getFtyType()
				.equals(EnumFtyType.PRODUCT.getValue())) {
			map.put("isDefault", 1); // 默认存储区
		} else {
			map.put("isDefault", 0); // 非默认存储区
		}
		List<Map<String, Object>> positionList = stockPositionMapper.selectAreaPositionByParam(map);

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		BigDecimal qty = UtilObject.getBigDecimalOrZero(map.get("qty"));
		for (Map<String, Object> innerMap : positionList) {
			returnList.add(innerMap);
			BigDecimal stockQty = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
			qty = qty.subtract(stockQty);
			if (qty.compareTo(BigDecimal.ZERO) <= 0) {
				break;
			}
		}

		return JSONArray.fromObject(returnList);
	}

	@Override
	public JSONArray getAdviseAreaListByShelves(Map<String, Object> map) throws Exception {

		if (!(map.containsKey("locationId") && map.containsKey("productionBatch") && map.containsKey("qty"))) {
			throw new WMSException("参数异常");
		}
		Map<Integer, DicStockLocationVo> locationIdMap = dictionaryService.getLocationIdMap();
		if (locationIdMap.get(UtilObject.getIntOrZero(map.get("locationId"))).getFtyType()
				.equals(EnumFtyType.PRODUCT.getValue())) {
			map.put("isDefault", 1); // 默认存储区
		} else {
			map.put("isDefault", 0); // 非默认存储区
		}
		List<Map<String, Object>> positionList = stockPositionMapper.selectAdviseAreaPositionByParam(map);

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		BigDecimal qty = UtilObject.getBigDecimalOrZero(map.get("qty"));
		for (Map<String, Object> innerMap : positionList) {
			returnList.add(innerMap);
			BigDecimal stockQty = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
			qty = qty.subtract(stockQty);
			if (qty.compareTo(BigDecimal.ZERO) <= 0) {
				break;
			}
		}

		return JSONArray.fromObject(returnList);
	}

	JSONArray getAreaList(List<Map<String, Object>> positionList) throws Exception {
		Map<Integer, JSONObject> areaMap = new HashMap<Integer, JSONObject>();
		if (positionList != null && positionList.size() > 0) {
			for (Map<String, Object> innerMap : positionList) {
				Integer whId = UtilObject.getIntOrZero(innerMap.get("wh_id"));
				String areaCode = UtilObject.getStringOrEmpty(innerMap.get("area_code"));
				Integer areaId = UtilObject.getIntOrZero(innerMap.get("area_id"));
				String areaName = UtilObject.getStringOrEmpty(innerMap.get("area_name"));
				String positionCode = UtilObject.getStringOrEmpty(innerMap.get("position_code"));
				Integer positionId = UtilObject.getIntOrZero(innerMap.get("position_id"));
				String positionName = UtilObject.getStringOrEmpty(innerMap.get("position_code"));
				BigDecimal qty = UtilObject.getBigDecimalOrZero(innerMap.get("qty"));
				if (areaMap.containsKey(areaId)) {
					JSONObject areaObj = areaMap.get(areaId);
					JSONArray positionAry = areaObj.getJSONArray("position_list");
					JSONObject positionObj = new JSONObject();
					positionObj.put("positionCode", positionCode);
					positionObj.put("positionId", positionId);
					positionObj.put("positionName", positionName);
					positionObj.put("qty", qty);
					positionAry.add(positionObj);
					areaObj.put("position_list", positionAry);
				} else {
					JSONObject areaObj = new JSONObject();
					areaObj.put("whId", whId);
					areaObj.put("areaCode", areaCode);
					areaObj.put("areaId", areaId);
					areaObj.put("areaName", areaName);
					JSONArray positionAry = new JSONArray();
					JSONObject positionObj = new JSONObject();
					positionObj.put("positionCode", positionCode);
					positionObj.put("positionId", positionId);
					positionObj.put("positionName", positionName);
					positionObj.put("qty", qty);
					positionAry.add(positionObj);
					areaObj.put("position_list", positionAry);
					areaMap.put(areaId, areaObj);
				}
			}
		}
		JSONArray areaAry = new JSONArray();
		if (!areaMap.isEmpty()) {
			for (Integer areaId : areaMap.keySet()) {
				areaAry.add(areaMap.get(areaId));
			}
		} else {
			throw new WMSException("仓位查询结果为空");
		}

		return areaAry;
	}

	@Override
	public List<BizStockTaskReqPosition> getStockTaskReqPositionListByReqItemId(Integer itemId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("itemId", itemId);
		return stockTaskReqPositionDao.selectByCondition(map);
	}

	@Override
	public List<BizStockTaskPositionCw> getStockTaskPositionListByReqItemId(BizStockTaskPositionCw record)
			throws Exception {

		return bizStockTaskPositionCwDao.selectByReferReceiptIdAndType(record);
	}

	@Override
	public JSONObject checkCodeTypeByShelves(JSONObject json) throws Exception {

		// 校验托盘
		JSONObject returnObj = new JSONObject();
		returnObj.put("type", "");
		returnObj.put("pallet_id", "");
		returnObj.put("pallet_code", "");
		returnObj.put("max_payload", "");
		returnObj.put("have_payload", "");
		returnObj.put("package_num", "");

		returnObj.put("package_id", "");
		returnObj.put("package_code", "");
		returnObj.put("qty", "");
		returnObj.put("package_type_id", "");
		returnObj.put("package_type_code", "");
		returnObj.put("unit_zh", "");

		returnObj.put("wh_id", "");
		returnObj.put("area_id", "");
		returnObj.put("position_id", "");
		returnObj.put("package_list", new JSONArray());
		boolean flag = false;
		json.put("havePackageList", false);
		flag = this.checkPalletCodeOnReq(json, returnObj, flag);

		Map<String, Object> palletMap = new HashMap<String, Object>();
		palletMap.put("locationId", json.get("location_id"));
		palletMap.put("palletCode", json.get("condition"));
		palletMap.put("isDefault", 0);// 非默认仓位库存
		flag = this.checkPalletCodeOnPosition(palletMap, returnObj, flag);
		flag = this.checkPalletCodeOnOperation(json, returnObj, flag);
		flag = this.checkPalletCodeOnMajor(json, returnObj, flag);
		flag = this.checkPackageCodeOnReq(json, returnObj, flag);
		Map<String, Object> packageMap = new HashMap<String, Object>();
		packageMap.put("matId", json.get("mat_id"));
		packageMap.put("locationId", json.get("location_id"));
		packageMap.put("batch", json.get("batch"));
		packageMap.put("packageCode", json.get("condition"));
		packageMap.put("isDefault", 1);// 默认仓位库存
		flag = this.checkPackageCodeOnPosition(packageMap, returnObj, flag);
		flag = this.checkPackageCodeOnOperation(json, returnObj, flag);
		flag = this.checkPositionCode(json, returnObj, flag);

		if (!flag) {
			throw new WMSException("数据不合法");
		}
		return returnObj;
	}

	@Override
	public JSONObject checkCodeTypeByShelvesNew(JSONObject json) throws Exception {

		// 校验托盘
		JSONObject returnObj = new JSONObject();
		returnObj.put("type", "");
		returnObj.put("pallet_id", "");
		returnObj.put("pallet_code", "");
		returnObj.put("max_payload", "");
		returnObj.put("have_payload", "");
		returnObj.put("package_num", "");

		returnObj.put("package_id", "");
		returnObj.put("package_code", "");
		returnObj.put("qty", "");
		returnObj.put("package_type_id", "");
		returnObj.put("package_type_code", "");
		returnObj.put("unit_zh", "");

		returnObj.put("wh_id", "");
		returnObj.put("area_id", "");
		returnObj.put("position_id", "");
		returnObj.put("package_list", new JSONArray());
		boolean flag = false;

		String condition = json.getString("condition");
		Byte requestSource = UtilObject.getByteOrNull(json.get("requestSource"));
		if (StringUtils.hasText(condition)) {

			String firstChar = condition.substring(0, 1);
			String otherStr = condition.substring(1, condition.length());
			if (condition.length() == 12) {
				// 托盘
				json.put("havePackageList", false);
				flag = this.checkPalletCodeOnReq(json, returnObj, flag);
				Map<String, Object> palletMap = new HashMap<String, Object>();
				palletMap.put("locationId", json.get("location_id"));
				palletMap.put("palletCode", json.get("condition"));
				Map<Integer, DicStockLocationVo> locationIdMap = dictionaryService.getLocationIdMap();
				if (locationIdMap.get(UtilObject.getIntOrZero(palletMap.get("locationId"))).getFtyType()
						.equals(EnumFtyType.PRODUCT.getValue())) {
					palletMap.put("isDefault", 1); // 默认存储区
				} else {
					palletMap.put("isDefault", 0); // 非默认存储区
				}
				
				flag = this.checkPalletCodeOnPosition(palletMap, returnObj, flag);
				flag = this.checkPalletCodeOnOperation(json, returnObj, flag);
				flag = this.checkPalletCodeOnMajor(json, returnObj, flag);
			} else if ("T".equalsIgnoreCase(firstChar)) {
				throw new WMSException("托盘条码错误");
			} else if ( condition.length() == 18&&!condition.contains("-")) {
				// 包
				flag = this.checkPackageCodeOnReq(json, returnObj, flag);
				// Map<String, Object> packageMap = new HashMap<String,
				// Object>();
				// packageMap.put("matId", json.get("mat_id"));
				// packageMap.put("locationId", json.get("location_id"));
				// packageMap.put("batch", json.get("batch"));
				// packageMap.put("packageCode", json.get("condition"));
				// packageMap.put("isDefault", 1);// 默认仓位库存
				// flag = this.checkPackageCodeOnPosition(packageMap, returnObj,
				// flag);
				json.put("debitCredit", EnumDebitCredit.DEBIT_S_ADD.getName());
				flag = this.checkPackageCodeOnOperation(json, returnObj, flag);
			}   
			if (EnumRequestSource.PDA.getValue().equals(requestSource)) {
				// PDA 查询 仓位
				flag = this.checkPositionCode(json, returnObj, flag);
			}

			if (!flag) {
				throw new WMSException("数据不合法");
			}
		} else {
			throw new WMSException("查询条件为空");
		}

		return returnObj;
	}

	@Override
	public JSONObject checkCodeTypeByReomove(JSONObject json) throws Exception {
		// 校验托盘
		JSONObject returnObj = new JSONObject();
		returnObj.put("type", "");
		returnObj.put("pallet_id", "");
		returnObj.put("pallet_code", "");
		returnObj.put("max_payload", "");
		returnObj.put("have_payload", "");
		returnObj.put("package_num", "");

		returnObj.put("package_id", "");
		returnObj.put("package_code", "");
		returnObj.put("qty", "");
		returnObj.put("package_type_id", "");
		returnObj.put("package_type_code", "");
		returnObj.put("unit_zh", "");

		returnObj.put("wh_id", "");
		returnObj.put("area_id", "");
		returnObj.put("position_id", "");
		returnObj.put("package_list", new JSONArray());
		boolean flag = false;
		// 后台启用包装类型
		Byte matStoreType = UtilObject.getByteOrNull(json.getInt("mat_store_type"));
		if (matStoreType == 1) {
			Map<String, Object> palletMap = new HashMap<String, Object>();
			palletMap.put("matId", json.get("mat_id"));
			palletMap.put("locationId", json.get("location_id"));
			palletMap.put("batch", json.get("batch"));
			palletMap.put("palletCode", json.get("condition"));
			palletMap.put("isDefault", 0);// 非默认仓位
			palletMap.put("havePackageList", true);
			flag = this.checkPalletCodeOnPosition(palletMap, returnObj, flag);
			Map<String, Object> packageMap = new HashMap<String, Object>();
			packageMap.put("matId", json.get("mat_id"));
			packageMap.put("locationId", json.get("location_id"));
			packageMap.put("batch", json.get("batch"));
			packageMap.put("packageCode", json.get("condition"));
			packageMap.put("isDefault", 0);// 非默认仓位
			flag = this.checkPackageCodeOnPosition(packageMap, returnObj, flag);
		} else {
			flag = this.checkPalletCodeOnOperation(json, returnObj, flag);
			json.put("debitCredit", EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			flag = this.checkPackageCodeOnOperation(json, returnObj, flag);
		}
		if (!flag) {
			throw new WMSException("数据不合法");
		}
		return returnObj;
	}

	@Override
	public JSONObject checkCodeTypeByReomovePortable(JSONObject json) throws Exception {
		// 校验托盘
		JSONObject returnObj = new JSONObject();
		returnObj.put("type", "");
		returnObj.put("pallet_id", "");
		returnObj.put("pallet_code", "");
		returnObj.put("max_payload", "");
		returnObj.put("have_payload", "");
		returnObj.put("package_num", "");

		returnObj.put("package_id", "");
		returnObj.put("package_code", "");
		returnObj.put("qty", "");
		returnObj.put("package_type_id", "");
		returnObj.put("package_type_code", "");
		returnObj.put("unit_zh", "");

		returnObj.put("wh_id", "");
		returnObj.put("area_id", "");
		returnObj.put("position_id", "");
		returnObj.put("package_list", new JSONArray());
		boolean flag = false;
		// 后台启用包装类型
		Byte matStoreType = UtilObject.getByteOrNull(json.getInt("mat_store_type"));
		if (matStoreType.equals(EnumMatStoreType.USEING.getValue())) {
			Map<String, Object> palletMap = new HashMap<String, Object>();
			palletMap.put("matId", json.get("mat_id"));
			palletMap.put("locationId", json.get("location_id"));
			palletMap.put("batch", json.get("batch"));
			palletMap.put("palletCode", json.get("condition"));
			palletMap.put("havePackageList", true);
			palletMap.put("isDefault", 0);// 非默认仓位
			flag = this.checkPalletCodeOnPosition(palletMap, returnObj, flag);
			Map<String, Object> packageMap = new HashMap<String, Object>();
			packageMap.put("matId", json.get("mat_id"));
			packageMap.put("locationId", json.get("location_id"));
			packageMap.put("batch", json.get("batch"));
			packageMap.put("packageCode", json.get("condition"));
			packageMap.put("isDefault", 0);// 非默认仓位
			flag = this.checkPackageCodeOnPosition(packageMap, returnObj, flag);
		} else {
			flag = this.checkPalletCodeOnOperation(json, returnObj, flag);
			json.put("debitCredit", EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			flag = this.checkPackageCodeOnOperation(json, returnObj, flag);
		}
		Map<String, Object> positionCode = new HashMap<String, Object>();
		positionCode.put("matId", json.get("mat_id"));
		positionCode.put("locationId", json.get("location_id"));
		positionCode.put("batch", json.get("batch"));
		positionCode.put("condition", json.get("condition"));
		positionCode.put("isDefault", 0);// 非默认仓位
		flag = this.checkPositionCodeByRemove(positionCode, returnObj, flag);
		if (!flag) {
			throw new WMSException("数据不合法");
		}
		return returnObj;
	}

	@Override
	public JSONObject checkCodeTypeByReomoveNew(JSONObject json) throws Exception {
		// 校验托盘
		JSONObject returnObj = new JSONObject();
		returnObj.put("type", "");
		returnObj.put("pallet_id", "");
		returnObj.put("pallet_code", "");
		returnObj.put("max_payload", "");
		returnObj.put("have_payload", "");
		returnObj.put("package_num", "");

		returnObj.put("package_id", "");
		returnObj.put("package_code", "");
		returnObj.put("qty", "");
		returnObj.put("package_type_id", "");
		returnObj.put("package_type_code", "");
		returnObj.put("unit_zh", "");

		returnObj.put("wh_id", "");
		returnObj.put("area_id", "");
		returnObj.put("position_id", "");
		returnObj.put("package_list", new JSONArray());
		boolean flag = false;

		String condition = json.getString("condition");
		Byte requestSource = UtilObject.getByteOrNull(json.get("requestSource"));
		Map<Integer, DicStockLocationVo> locationIdMap = dictionaryService.getLocationIdMap();
	
		
		if (StringUtils.hasText(condition)) {
			// 后台启用包装类型
			Byte matStoreType = UtilObject.getByteOrNull(json.getInt("mat_store_type"));
			String firstChar = condition.substring(0, 1);
			String otherStr = condition.substring(1, condition.length());
			if (condition.length() == 12) {
				// 托盘
				if (matStoreType == 1) {
					Map<String, Object> palletMap = new HashMap<String, Object>();
					palletMap.put("matId", json.get("mat_id"));
					palletMap.put("locationId", json.get("location_id"));
					palletMap.put("batch", json.get("batch"));
					palletMap.put("palletCode", json.get("condition"));
					
					if (locationIdMap.get(UtilObject.getIntOrZero(palletMap.get("locationId"))).getFtyType()
							.equals(EnumFtyType.PRODUCT.getValue())) {
						palletMap.put("isDefault", 1); // 默认存储区
					} else {
						palletMap.put("isDefault", 0); // 非默认存储区
					}
					
					// palletMap.put("isDefault", 0);// 非默认仓位
					palletMap.put("havePackageList", true);
					if (json.containsKey("isUrgent")) {
						palletMap.put("isUrgent", json.get("isUrgent"));
					}
					// palletMap.put("positionId", json.get("position_id"));
					flag = this.checkPalletCodeOnPosition(palletMap, returnObj, flag);
					if (flag) {
						Integer positionId = json.getInt("position_id");
						Integer returnId = returnObj.getInt("position_id");
						if (!positionId.equals(returnId)) {
							throw new WMSException("托盘不在该仓位下");
						}
					}
				} else {
					flag = this.checkPalletCodeOnOperation(json, returnObj, flag);

				}
				// 查询 主数据
				flag = this.checkPalletCodeOnMajor(json, returnObj, flag);
			} else if ("T".equalsIgnoreCase(firstChar)) {
				throw new WMSException("托盘条码错误");
			} else if (condition.length() == 18&&!condition.contains("-")) {
				// 包装物
				if (matStoreType == 1) {

					Map<String, Object> packageMap = new HashMap<String, Object>();
					packageMap.put("matId", json.get("mat_id"));
					packageMap.put("locationId", json.get("location_id"));
					packageMap.put("batch", json.get("batch"));
					packageMap.put("packageCode", json.get("condition"));
					if (locationIdMap.get(UtilObject.getIntOrZero(packageMap.get("locationId"))).getFtyType()
							.equals(EnumFtyType.PRODUCT.getValue())) {
						packageMap.put("isDefault", 1); // 默认存储区
					} else {
						packageMap.put("isDefault", 0); // 非默认存储区
					}
					// packageMap.put("isDefault", 0);// 非默认仓位
					packageMap.put("positionId", json.get("position_id"));
					if (json.containsKey("isUrgent")) {
						packageMap.put("isUrgent", json.get("isUrgent"));
					}
					flag = this.checkPackageCodeOnPosition(packageMap, returnObj, flag);
					if (flag) {
						Integer positionId = json.getInt("position_id");
						Integer returnId = returnObj.getInt("position_id");
						if (!positionId.equals(returnId)) {
							throw new WMSException("托盘不在该仓位下");
						}
					}
				} else {
					json.put("debitCredit", EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
					flag = this.checkPackageCodeOnOperation(json, returnObj, flag);
				}
			} 

			if (requestSource.equals(EnumRequestSource.PDA.getValue())) {
				// PDA 校验仓位
				Map<String, Object> positionCode = new HashMap<String, Object>();
				positionCode.put("matId", json.get("mat_id"));
				positionCode.put("locationId", json.get("location_id"));
				positionCode.put("batch", json.get("batch"));
				positionCode.put("condition", json.get("condition"));
				if (locationIdMap.get(UtilObject.getIntOrZero(positionCode.get("locationId"))).getFtyType()
						.equals(EnumFtyType.PRODUCT.getValue())) {
					positionCode.put("isDefault", 1); // 默认存储区
				} else {
					positionCode.put("isDefault", 0); // 非默认存储区
				}
				
			
				flag = this.checkPositionCodeByRemove(positionCode, returnObj, flag);
			}
			if (!flag) {
				throw new WMSException("数据不合法");
			}
		} else {
			throw new WMSException("查询条件为空");
		}

		return returnObj;

	}

	@Override
	public JSONObject checkCodeTypeByProductionTransport(JSONObject json) throws Exception {

		if (!(json.containsKey("mat_id") && json.containsKey("batch") && json.containsKey("location_id"))) {
			throw new WMSException("参数异常");
		}

		// 校验托盘
		JSONObject returnObj = new JSONObject();
		returnObj.put("type", "");
		returnObj.put("pallet_id", "");
		returnObj.put("pallet_code", "");
		returnObj.put("max_payload", "");
		returnObj.put("have_payload", "");
		returnObj.put("package_num", "");

		returnObj.put("package_id", "");
		returnObj.put("package_code", "");
		returnObj.put("qty", "");
		returnObj.put("package_type_id", "");
		returnObj.put("package_type_code", "");
		returnObj.put("unit_zh", "");

		returnObj.put("wh_id", "");
		returnObj.put("area_id", "");
		returnObj.put("position_id", "");
		returnObj.put("package_list", new JSONArray());
		boolean flag = false;

		Byte matStoreType = UtilObject.getByteOrNull(json.getInt("mat_store_type"));
		if (matStoreType == 1) {
			// 后台启用
			Map<String, Object> palletMap = new HashMap<String, Object>();
			palletMap.put("matId", json.get("mat_id"));
			palletMap.put("locationId", json.get("location_id"));
			palletMap.put("batch", json.get("batch"));
			palletMap.put("palletCode", json.get("condition"));
			palletMap.put("isDefault", 1);// 默认仓位库存
			palletMap.put("havePackageList", true);
			if (json.containsKey("isUrgent")) {
				palletMap.put("isUrgent", json.get("isUrgent"));
			}
			flag = this.checkPalletCodeOnPosition(palletMap, returnObj, flag);

			Map<String, Object> packageMap = new HashMap<String, Object>();
			packageMap.put("matId", json.get("mat_id"));
			packageMap.put("locationId", json.get("location_id"));
			packageMap.put("batch", json.get("batch"));
			packageMap.put("isDefault", 1);// 默认仓位库存
			packageMap.put("packageCode", json.get("condition"));
			if (json.containsKey("isUrgent")) {
				packageMap.put("isUrgent", json.get("isUrgent"));
			}
			flag = this.checkPackageCodeOnPosition(packageMap, returnObj, flag);
		} else {
			flag = this.checkPalletCodeOnOperation(json, returnObj, flag);
			flag = this.checkPalletCodeOnMajor(json, returnObj, flag);
			json.put("debitCredit", EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			flag = this.checkPackageCodeOnOperation(json, returnObj, flag);
		}

		if (!flag) {
			throw new WMSException("数据不合法");
		}
		return returnObj;
	}

	@Override
	public JSONObject checkCodeTypeByProductionTransportNew(JSONObject json) throws Exception {

		if (!(json.containsKey("mat_id") && json.containsKey("batch") && json.containsKey("location_id"))) {
			throw new WMSException("参数异常");
		}

		// 校验托盘
		JSONObject returnObj = new JSONObject();
		returnObj.put("type", "");
		returnObj.put("pallet_id", "");
		returnObj.put("pallet_code", "");
		returnObj.put("max_payload", "");
		returnObj.put("have_payload", "");
		returnObj.put("package_num", "");

		returnObj.put("package_id", "");
		returnObj.put("package_code", "");
		returnObj.put("qty", "");
		returnObj.put("package_type_id", "");
		returnObj.put("package_type_code", "");
		returnObj.put("unit_zh", "");

		returnObj.put("wh_id", "");
		returnObj.put("area_id", "");
		returnObj.put("position_id", "");
		returnObj.put("package_list", new JSONArray());
		boolean flag = false;
		String condition = json.getString("condition");
		Byte matStoreType = UtilObject.getByteOrNull(json.getInt("mat_store_type"));
		if (StringUtils.hasText(condition)) {
			String firstChar = condition.substring(0, 1);
			String otherStr = condition.substring(1, condition.length());
			if (condition.length() == 12) {
				if (matStoreType == 1) {
					// 后台启用
					Map<String, Object> palletMap = new HashMap<String, Object>();
					palletMap.put("matId", json.get("mat_id"));
					palletMap.put("locationId", json.get("location_id"));
					palletMap.put("batch", json.get("batch"));
					palletMap.put("palletCode", json.get("condition"));
					palletMap.put("isDefault", 1);// 默认仓位库存
					palletMap.put("havePackageList", true);
					if (json.containsKey("isUrgent")) {
						palletMap.put("isUrgent", json.get("isUrgent"));
					}
					flag = this.checkPalletCodeOnPosition(palletMap, returnObj, flag);

				} else {
					flag = this.checkPalletCodeOnOperation(json, returnObj, flag);

				}
				flag = this.checkPalletCodeOnMajor(json, returnObj, flag);
			} else if ("T".equalsIgnoreCase(firstChar)) {
				throw new WMSException("托盘条码错误");
			} else if (condition.length() == 18&&!condition.contains("-")) {
				if (matStoreType == 1) {
					// 后台启用

					Map<String, Object> packageMap = new HashMap<String, Object>();
					packageMap.put("matId", json.get("mat_id"));
					packageMap.put("locationId", json.get("location_id"));
					packageMap.put("batch", json.get("batch"));
					packageMap.put("isDefault", 1);// 默认仓位库存
					packageMap.put("packageCode", json.get("condition"));
					if (json.containsKey("isUrgent")) {
						packageMap.put("isUrgent", json.get("isUrgent"));
					}
					flag = this.checkPackageCodeOnPosition(packageMap, returnObj, flag);
				} else {
					json.put("debitCredit", EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
					flag = this.checkPackageCodeOnOperation(json, returnObj, flag);
				}
			}

			if (!flag) {
				throw new WMSException("数据不合法");
			}
		} else {
			throw new WMSException("查询条件为空");
		}

		return returnObj;
	}

	@Override
	public List<Map<String, Object>> getStockPosition(Map<String, Object> map) throws Exception {
		if (!(map.containsKey("locationId") && map.containsKey("matId") && map.containsKey("batch"))) {
			throw new WMSException("参数异常");
		}
		Map<Integer, DicStockLocationVo> locationIdMap = dictionaryService.getLocationIdMap();
		if (locationIdMap.get(UtilObject.getIntOrZero(map.get("locationId"))).getFtyType()
				.equals(EnumFtyType.PRODUCT.getValue())) {
			map.put("isDefault", 1); // 默认存储区
		} else {
			map.put("isDefault", 0); // 非默认存储区
		}
		return stockPositionMapper.selectStockPositionByParam(map);
	}

	@Override
	public List<BizStockTaskReqItemVo> getStockTaskReqItemByParams(Map<String, Object> map) throws Exception {

		return StockTaskReqItemDao.selectItemsByParams(map);
	}

	

	@Override
	public Map<String, Object> saveTaskDataForPositionPallet(JSONObject json, boolean isPDA) throws Exception {
		Map<String, Object> result = new HashMap<>();
		int sourceAreaId = 0;
		int sourcePositionId = 0;
		byte receiptType = (byte) json.getInt("receiptType");
		if (receiptType == EnumReceiptType.STOCK_TASK_POSITION_CLEANUP.getValue()) {
			sourceAreaId = json.getInt("s_area_id");
			sourcePositionId = json.getInt("s_position_id");
			int targetAreaId = json.getInt("t_area_id");
			int targetPositionId = json.getInt("t_position_id");
			if (sourceAreaId == targetAreaId && sourcePositionId == targetPositionId) {
				throw new WMSException("发出仓位与接受仓位不能相同");
			}
		}
		
		Map<String, Object> param = new HashMap<>();
		param.put("ftyId", json.getInt("fty_id"));
		param.put("locationId", json.getInt("location_id"));
		param.put("whId", json.getInt("wh_id"));
		param.put("isDefault", 0);
		if (receiptType == EnumReceiptType.STOCK_TASK_POSITION_CLEANUP.getValue()) {
			param.put("areaId", sourceAreaId);
			param.put("positionId", sourcePositionId);// 基于仓位 发出存储区 发出仓位
		} else {
			param.put("palletCode", json.get("s_pallet_code"));// 基于托盘 发出托盘号
		}

		List<Map<String, Object>> materialList = commonService.getTaskMatListByPosition(param);
		if (receiptType == EnumReceiptType.STOCK_TASK_PALLET_CLEANUP.getValue()) {
			if (materialList != null && materialList.size() > 0) {
				int areaId = UtilObject.getIntOrZero(materialList.get(0).get("area_id"));     
				int positionId = UtilObject.getIntOrZero(materialList.get(0).get("position_id"));
				int palletId = UtilObject.getIntOrZero(materialList.get(0).get("pallet_id"));
				
				int tAreaId = json.getInt("t_area_id"); 
				int tPositionId = json.getInt("t_position_id");
				int tPalletId = json.getInt("t_pallet_id");
				
				if (areaId==tAreaId&&positionId==tPositionId&&palletId==tPalletId) {
					throw new WMSException("源和目标存储区、仓位、托盘一致，不能提交数据，请检查");
				}
			}
		}
		Boolean submit = json.getBoolean("submit");// false 保存 true 提交
		int stockTaskId = json.getInt("stock_task_id");// stockTaskId>0修改
														// stockTaskId=0新增
		boolean isAdd = true;

		// 判断是否新增
		if (stockTaskId > 0) {
			isAdd = false;
			bizStockTaskItemCwDao.deleteByStockTaskId(stockTaskId);
			bizStockTaskPositionCwDao.deleteByStockTaskId(stockTaskId);
		} else {
			BizStockTaskHeadCw head = bizStockTaskHeadCwDao.selectByPrimaryKey(stockTaskId);
			if (null == head || "".equals(head.getStockTaskCode()) || head.getIsDelete() == 1) {
				isAdd = true;
			}
		}
		// 保存head
		BizStockTaskHeadCw taskHeadCw = new BizStockTaskHeadCw();
		taskHeadCw = this.saveHeadData(isAdd, isPDA, json, taskHeadCw);
		stockTaskId = taskHeadCw.getStockTaskId();
		// 保存item
		
		BizStockTaskItemCw itemCw;
		int i = 1;
		List<BizStockTaskItemCw> itemList = new ArrayList<>();
		if (materialList != null && materialList.size() > 0) {
			for (Map<String, Object> map : materialList) {
				itemCw = new BizStockTaskItemCw();
				itemCw.setStockTaskRid(i++);
				itemCw = this.saveItemData(taskHeadCw, itemCw, map);
				// `position_id`,`pallet_id`,`package_id`,`mat_id`,`location_id`,`batch`,`status`,`stock_type_id`唯一键
				StockPosition sPosition = new StockPosition();
				sPosition.setPositionId(UtilObject.getIntOrZero(map.get("position_id")));
				sPosition.setPalletId(UtilObject.getIntOrZero(map.get("pallet_id")));
				sPosition.setMatId(itemCw.getMatId());
				sPosition.setLocationId(itemCw.getLocationId());
				sPosition.setBatch(itemCw.getBatch());
				sPosition.setStatus((byte) UtilObject.getIntOrZero(map.get("status")));
				sPosition.setStockTypeId((byte) UtilObject.getIntOrZero(map.get("stock_type_id")));
				List<StockPosition> list = stockPositionMapper.selectUniqueExceptPackageId(sPosition);
				List<BizStockTaskPositionCw> palletPackageList = new ArrayList<>();
				for (StockPosition stockPosition : list) {
					BizStockTaskPositionCw positionCw = new BizStockTaskPositionCw();
					positionCw = this.savePositionDataForPositionPallet(taskHeadCw, itemCw, positionCw, stockPosition);
					palletPackageList.add(positionCw);
				}
				// itemCw.setMatStoreType((byte)UtilObject.getIntOrZero(map.get("sn_used")));
				itemCw.setPalletPackageList(palletPackageList);

				itemList.add(itemCw);
			}
		} else {
			throw new WMSException("物料不存在");
		}
		taskHeadCw.setItemList(itemList);

		// 修改仓位库存
		if (submit) {
			updateStockPositionForWarehouse(taskHeadCw);
			// 更改单据状态
			taskHeadCw.setStatus(EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
			bizStockTaskHeadCwDao.updateByPrimaryKeySelective(taskHeadCw);
			BizStockTaskItemCw updateItemCw = new BizStockTaskItemCw();
			updateItemCw.setStatus(EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
			updateItemCw.setStockTaskId(stockTaskId);
			bizStockTaskItemCwDao.updateByStockTaskId(updateItemCw);
		}

		result.put("stock_task_id", stockTaskId);
		return result;
	}

	private BizStockTaskPositionCw savePositionDataForPositionPallet(BizStockTaskHeadCw taskHeadCw,
			BizStockTaskItemCw itemCw, BizStockTaskPositionCw positionCw, StockPosition stockPosition)
			throws Exception {
		positionCw.setStockTaskId(itemCw.getStockTaskId());
		positionCw.setStockTaskRid(itemCw.getStockTaskRid());
		positionCw.setStockId(stockPosition.getId());
		positionCw.setStockSnId(0);
		positionCw.setBatch(stockPosition.getBatch());
		positionCw.setSnId(0);
		if (taskHeadCw.getReceiptType() == EnumReceiptType.STOCK_TASK_PALLET_CLEANUP.getValue()) {
			positionCw.setPalletId(taskHeadCw.getPalletId());// 基于托盘抬头中存的是目标托盘
		} else {
			positionCw.setPalletId(stockPosition.getPalletId());// 基于仓位目标托盘与原托盘一致
			positionCw.setSourcePalletId(stockPosition.getPalletId());
		}
		positionCw.setPackageId(stockPosition.getPackageId());
		positionCw.setQty(stockPosition.getQty());
		positionCw.setStockQty(stockPosition.getQty());
		positionCw.setWhId(stockPosition.getWhId());
		positionCw.setSourceAreaId(stockPosition.getAreaId());
		positionCw.setSourcePositionId(stockPosition.getPositionId());
		positionCw.setCreateUser(taskHeadCw.getCreateUser());
		positionCw.setModifyUser(taskHeadCw.getCreateUser());
		positionCw.setTargetAreaId(taskHeadCw.getAreaId());
		positionCw.setTargetPositionId(taskHeadCw.getPositionId());
		positionCw.setSourcePalletId(stockPosition.getPalletId());

		positionCw.setStockTaskReqId(0);
		positionCw.setStockTaskReqRid(0);
		bizStockTaskPositionCwDao.insertSelective(positionCw);
		return positionCw;
	}

	private BizStockTaskItemCw saveItemData(BizStockTaskHeadCw taskHeadCw, BizStockTaskItemCw itemCw,
			Map<String, Object> map) throws Exception {
		itemCw.setStockTaskId(taskHeadCw.getStockTaskId());
		itemCw.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
		itemCw.setWhId(taskHeadCw.getWhId());
		itemCw.setFtyId(taskHeadCw.getFtyId());
		itemCw.setLocationId(taskHeadCw.getLocationId());
		itemCw.setMatId(UtilObject.getIntOrZero(map.get("mat_id")));
		itemCw.setUnitId(UtilObject.getIntOrZero(map.get("unit_id")));
		itemCw.setBatch(Long.valueOf(UtilString.getStringOrEmptyForObject(map.get("batch"))));
		if (taskHeadCw.getReceiptType() != EnumReceiptType.STOCK_TASK_MATERIAL_CLEANUP.getValue()) {
			itemCw.setQty(new BigDecimal(UtilString.getStringOrEmptyForObject(map.get("stock_qty"))));
		}
		itemCw.setSpecStock(UtilString.getStringOrEmptyForObject(map.get("spec_stock")));
		itemCw.setSpecStockCode(UtilString.getStringOrEmptyForObject(map.get("spec_stock_code")));
		itemCw.setSpecStockName(UtilString.getStringOrEmptyForObject(map.get("spec_stock_name")));
		itemCw.setPackageTypeId(UtilObject.getIntOrZero(map.get("package_type_id")));
		itemCw.setProductionBatch(UtilString.getStringOrEmptyForObject(map.get("product_batch")));
		itemCw.setErpBatch(UtilString.getStringOrEmptyForObject(map.get("erp_batch")));
		itemCw.setQualityBatch(UtilString.getStringOrEmptyForObject(map.get("quality_batch")));
		itemCw.setMatStoreType(UtilObject.getByteOrNull(map.get("sn_used")));// 是否启用包

		itemCw.setReferReceiptType((byte) 0);
		itemCw.setReceiptType((byte) 0);
		itemCw.setReferReceiptId(0);
		itemCw.setReferReceiptCode("");
		itemCw.setStockTaskReqId(0);
		itemCw.setStockTaskReqRid(0);
		bizStockTaskItemCwDao.insertSelective(itemCw);
		return itemCw;
	}

	private BizStockTaskHeadCw saveHeadData(boolean isAdd, boolean isPDA, JSONObject json,
			BizStockTaskHeadCw taskHeadCw) throws Exception {
		taskHeadCw.setRemover(json.getString("remover"));// 搬运工
		taskHeadCw.setForkliftWorker(json.getString("forklift_worker"));// 叉车工
		taskHeadCw.setTallyClerk(json.getString("tally_clerk"));// 理货员
		taskHeadCw.setRemoverId(json.getInt("remover_id"));
		taskHeadCw.setForkliftWorkerId(json.getInt("forklift_worker_id"));
		taskHeadCw.setTallyClerkId(json.getInt("tally_clerk_id"));

		taskHeadCw.setCreateUser(json.getString("cUserId"));
		taskHeadCw.setFtyId(json.getInt("fty_id"));
		taskHeadCw.setLocationId(json.getInt("location_id"));
		taskHeadCw.setWhId(json.getInt("wh_id"));
		taskHeadCw.setReceiptType((byte) json.getInt("receiptType"));
		taskHeadCw.setRemark(json.getString("remark"));
		if (taskHeadCw.getReceiptType() == EnumReceiptType.STOCK_TASK_PALLET_CLEANUP.getValue()
				|| taskHeadCw.getReceiptType() == EnumReceiptType.STOCK_TASK_POSITION_CLEANUP.getValue()) {
			taskHeadCw.setAreaId(json.getInt("t_area_id"));// 抬头中存的是目标存储区
			taskHeadCw.setPositionId(json.getInt("t_position_id"));// 抬头中存的是目标仓位
		}

		if (taskHeadCw.getReceiptType() == EnumReceiptType.STOCK_TASK_PALLET_CLEANUP.getValue()) {
			taskHeadCw.setPalletId(json.getInt("t_pallet_id"));// 抬头中存的是目标托盘
		}
		if (isPDA) {
			taskHeadCw.setRequestSource(EnumRequestSource.PDA.getValue());
		} else {
			taskHeadCw.setRequestSource(EnumRequestSource.WEB.getValue());
		}
		taskHeadCw.setShippingType(EnumTaskReqShippingType.WAREHOUSE_MONITORING.getValue());
		taskHeadCw.setStatus(EnumReceiptStatus.RECEIPT_DRAFT.getValue());

		taskHeadCw.setMoveTypeId(0);
		taskHeadCw.setStockTaskReqId(0);

		if (isAdd) {
			taskHeadCw.setStockTaskCode(commonService.getNextReceiptCode("stock_task"));
			bizStockTaskHeadCwDao.insertSelective(taskHeadCw);
		} else {
			taskHeadCw.setStockTaskId(json.getInt("stock_task_id"));
			bizStockTaskHeadCwDao.updateByPrimaryKeySelective(taskHeadCw);
		}

		return taskHeadCw;
	}

	@Override
	public Map<String, Object> saveTaskDataForPackageMaterial(JSONObject json, boolean isPDA) throws Exception {
		Map<String, Object> result = new HashMap<>();
		// int sourceAreaId = 0;
		// int sourcePositionId = 0;
		byte receiptType = (byte) json.getInt("receiptType");

		Boolean submit = json.getBoolean("submit");// false 保存 true 提交
		int stockTaskId = json.getInt("stock_task_id");// stockTaskId>0修改
														// stockTaskId=0新增
		boolean isAdd = true;

		// 判断是否新增
		if (stockTaskId > 0) {
			isAdd = false;
			bizStockTaskItemCwDao.deleteByStockTaskId(stockTaskId);
			bizStockTaskPositionCwDao.deleteByStockTaskId(stockTaskId);
		} else {
			BizStockTaskHeadCw head = bizStockTaskHeadCwDao.selectByPrimaryKey(stockTaskId);
			if (null == head || "".equals(head.getStockTaskCode()) || head.getIsDelete() == 1) {
				isAdd = true;
			}
		}
		// 保存head
		BizStockTaskHeadCw taskHeadCw = new BizStockTaskHeadCw();
		taskHeadCw = this.saveHeadData(isAdd, isPDA, json, taskHeadCw);
		stockTaskId = taskHeadCw.getStockTaskId();
		// 保存item
		JSONArray itemListAry = json.getJSONArray("item_list");
		List<BizStockTaskItemCw> itemList = new ArrayList<>();
		BizStockTaskItemCw itemCw;
		int i = 1;
		for (Object object : itemListAry) {
			JSONObject jsonObject = (JSONObject) object;
			String stockIdList = jsonObject.getString("stock_id");// 仓位库存id集合
			String[] stockIdAry = stockIdList.split(",");
			Map<String, Object> param = new HashMap<>();
			param.put("stockId", stockIdAry[0]);
			param.put("isDefault", 0);
			param.put("includePackage", 0);
			List<Map<String, Object>> materialList = commonService.getTaskMatListByPosition(param);
			if (materialList == null || materialList.size()==0) {
				throw new WMSException("物料不存在");
			}
			Map<String, Object> map = materialList.get(0);
			itemCw = new BizStockTaskItemCw();
			itemCw.setStockTaskRid(i++);
			itemCw.setQty(new BigDecimal(UtilString.getStringOrEmptyForObject(jsonObject.get("qty"))));// 调整数量
																										// 基于物料
			itemCw = this.saveItemData(taskHeadCw, itemCw, map);
			// `position_id`,`pallet_id`,`package_id`,`mat_id`,`location_id`,`batch`,`status`,`stock_type_id`唯一键
			List<BizStockTaskPositionCw> palletPackageList = new ArrayList<>();
			for (String stockIdStr : stockIdAry) {
				int stockId = Integer.parseInt(stockIdStr);
				StockPosition stockPosition = stockPositionMapper.selectByPrimaryKey(stockId);
				BizStockTaskPositionCw positionCw = new BizStockTaskPositionCw();
				positionCw = this.savePositionDataForPackageMaterial(taskHeadCw, itemCw, positionCw, stockPosition,
						jsonObject);
				palletPackageList.add(positionCw);
			}
			// itemCw.setMatStoreType((byte)UtilObject.getIntOrZero(map.get("sn_used")));
			itemCw.setPalletPackageList(palletPackageList);

			itemList.add(itemCw);

		}
		taskHeadCw.setItemList(itemList);
		if (submit) {
			updateStockPositionForWarehouse(taskHeadCw);
			// 更改单据状态
			taskHeadCw.setStatus(EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
			bizStockTaskHeadCwDao.updateByPrimaryKeySelective(taskHeadCw);
			BizStockTaskItemCw updateItemCw = new BizStockTaskItemCw();
			updateItemCw.setStatus(EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
			updateItemCw.setStockTaskId(stockTaskId);
			bizStockTaskItemCwDao.updateByStockTaskId(updateItemCw);
		}

		result.put("stock_task_id", stockTaskId);
		return result;
	}

	private BizStockTaskPositionCw savePositionDataForPackageMaterial(BizStockTaskHeadCw taskHeadCw,
			BizStockTaskItemCw itemCw, BizStockTaskPositionCw positionCw, StockPosition stockPosition,
			JSONObject jsonObject) throws Exception {
		positionCw.setStockTaskId(itemCw.getStockTaskId());
		positionCw.setStockTaskRid(itemCw.getStockTaskRid());
		positionCw.setStockId(stockPosition.getId());
		positionCw.setStockSnId(0);
		positionCw.setBatch(stockPosition.getBatch());
		positionCw.setSnId(0);
		positionCw.setTargetAreaId(jsonObject.getInt("t_area_id"));
		positionCw.setTargetPositionId(jsonObject.getInt("t_position_id"));
		positionCw.setPalletId(jsonObject.getInt("t_pallet_id"));

		positionCw.setPackageId(stockPosition.getPackageId());
		positionCw.setQty(new BigDecimal(UtilString.getStringOrEmptyForObject(jsonObject.get("qty"))));
		positionCw.setStockQty(stockPosition.getQty());
		positionCw.setWhId(stockPosition.getWhId());
		positionCw.setSourceAreaId(stockPosition.getAreaId());
		positionCw.setSourcePositionId(stockPosition.getPositionId());
		positionCw.setSourcePalletId(stockPosition.getPalletId());
		positionCw.setCreateUser(taskHeadCw.getCreateUser());
		positionCw.setModifyUser(taskHeadCw.getCreateUser());

		if (taskHeadCw.getReceiptType() == EnumReceiptType.STOCK_TASK_PALLET_CLEANUP.getValue()) {
			positionCw.setSourcePalletId(stockPosition.getPalletId());
		}
		positionCw.setStockTaskReqId(0);
		positionCw.setStockTaskReqRid(0);
		bizStockTaskPositionCwDao.insertSelective(positionCw);
		return positionCw;
	}

	@Override
	public List<Map<String, Object>> getItemsByStockTaskIdForWarehouse(BizStockTaskItemCw bizStockTaskItem) {

		return bizStockTaskItemCwDao.getItemsByStockTaskIdForWarehouse(bizStockTaskItem);
	}

	@Override
	public void transportByProduct(int stockTaskReqId) throws Exception {

		BizStockTaskReqHead reqHead = new BizStockTaskReqHead();
		reqHead = StockTaskReqHeadDao.selectByPrimaryKey(stockTaskReqId);
		Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
		DicStockLocationVo locationObj = locationMap.get(reqHead.getLocationId());

		if (locationObj.getFtyType().equals(EnumFtyType.PRODUCT.getValue())) {
			String whCode = dictionaryService.getWhCodeByWhId(reqHead.getWhId());
			Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
					UtilProperties.getInstance().getDefaultCCQ());
			Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
					UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());

			List<BizStockTaskReqItemVo> reqItemList = new ArrayList<BizStockTaskReqItemVo>();

			reqItemList = StockTaskReqItemDao.getReqItemsByTaskReqId(stockTaskReqId);

			
			
			List<BizStockTaskItemCw> taskItemList = new ArrayList<BizStockTaskItemCw>();
			List<BizStockTaskReqItemVo> reqItemListAdd = new ArrayList<BizStockTaskReqItemVo>();
			for (BizStockTaskReqItemVo item : reqItemList) {
				if (item.getMatStoreType().equals(EnumMatStoreType.UN_USEING.getValue())){
					// 后台不启用
					reqItemListAdd.add(item);
				}else {
					// 后台启用
					if(reqHead.getReceiptType().equals(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue())&&reqHead.getReferReceiptType().equals(EnumReceiptType.STOCK_INPUT_TRANSPORT_ERP.getValue())){
						// 转储入库冲销
						reqItemListAdd.add(item);
					}else if(reqHead.getReferReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue())||reqHead.getReferReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue())){
						// 生产转运
						reqItemListAdd.add(item);
					}
				}
			}
			if (reqItemListAdd.size() > 0) {
				BizStockTaskHeadCw taskHead = new BizStockTaskHeadCw();
				if(reqHead.getReceiptType().equals(EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue())){
					taskHead.setReceiptType(EnumReceiptType.STOCK_TASK_LISTING.getValue());
				}else if(reqHead.getReceiptType().equals(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue())){
					taskHead.setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL.getValue());
				}
				
				this.addStockTaskHeadByReq(taskHead, reqHead);
				int i = 0;
				for (BizStockTaskReqItemVo item : reqItemListAdd) {
					Byte workModel = EnumWorkModel.NOTUSED.getValue();
					BizStockTaskItemCw taskitem = new BizStockTaskItemCw();
					taskitem.setStockTaskRid(i + 1);
					i++;
					
					taskitem.setMatStoreType(item.getMatStoreType());
					this.addStockTaskItemByReq(taskHead, taskitem, item);
					List<BizStockTaskPositionCw> pList = new ArrayList<BizStockTaskPositionCw>();
					
					if(item.getMatStoreType().equals(EnumMatStoreType.UN_USEING.getValue())){
						BizStockTaskPositionCw poItem = new BizStockTaskPositionCw();
						poItem.setBatch(item.getBatch());

						poItem.setStockTaskId(taskitem.getStockTaskId());
						poItem.setStockTaskRid(taskitem.getStockTaskRid());
						poItem.setStockTaskReqId(taskitem.getStockTaskReqId());
						poItem.setStockTaskReqRid(taskitem.getStockTaskReqRid());
						poItem.setBatch(taskitem.getBatch());
						poItem.setQty(taskitem.getQty());
						poItem.setPalletId(0);
						poItem.setPackageId(0);
						poItem.setSourceAreaId(sourceAreaId);
						poItem.setSourcePositionId(sourcePositionId);
						poItem.setTargetAreaId(sourceAreaId);
						poItem.setTargetPositionId(sourcePositionId);
						poItem.setWhId(taskitem.getWhId());
						bizStockTaskPositionCwDao.insertSelective(poItem);
						pList.add(poItem);
						// 插入历史表
						
						this.addHistory(taskHead, taskitem, poItem);
					}else{
						
						List<BizStockTaskReqPosition> reqPList = new ArrayList<BizStockTaskReqPosition>();
							// 生产转运
						Map<String, Object> reqMap = new HashMap<String, Object>();
						reqMap.put("stockTaskReqId", taskitem.getStockTaskReqId());
						reqMap.put("stockTaskReqRid", taskitem.getStockTaskReqRid());
						
						reqPList = StockTaskReqPositionDao.selectByCondition(reqMap);
						if (reqPList != null && reqPList.size() > 0) {
							for(BizStockTaskReqPosition rp:reqPList){
								BizStockTaskPositionCw poItem = new BizStockTaskPositionCw();
								poItem.setBatch(item.getBatch());

								poItem.setStockTaskId(taskitem.getStockTaskId());
								poItem.setStockTaskRid(taskitem.getStockTaskRid());
								poItem.setStockTaskReqId(taskitem.getStockTaskReqId());
								poItem.setStockTaskReqRid(taskitem.getStockTaskReqRid());
								poItem.setBatch(taskitem.getBatch());
								poItem.setQty(rp.getQty());
								
								poItem.setPalletId(rp.getPalletId());
								poItem.setPackageId(rp.getPackageId());
								if(rp.getPackageId()!=null&&rp.getPackageId()>0){
									workModel = EnumWorkModel.ONLYPACKAGE.getValue();
								}
								if(rp.getPalletId()!=null&&rp.getPalletId()>0){
									workModel = EnumWorkModel.PALLETANDPACKAGE.getValue();
								}
								
								poItem.setSourceAreaId(sourceAreaId);
								poItem.setSourcePositionId(sourcePositionId);
								poItem.setTargetAreaId(sourceAreaId);
								poItem.setTargetPositionId(sourcePositionId);
								poItem.setWhId(taskitem.getWhId());
								bizStockTaskPositionCwDao.insertSelective(poItem);
								pList.add(poItem);
								// 插入历史表
								
								this.addHistory(taskHead, taskitem, poItem);
							}
						}
						
						
						
					}
					// 更新 工作模式
					BizStockTaskItemCw itemRecord = new BizStockTaskItemCw();
					itemRecord.setWorkModel(workModel);
					itemRecord.setItemId(taskitem.getItemId());
					bizStockTaskItemCwDao.updateByPrimaryKeySelective(itemRecord);
					taskitem.setWorkModel(workModel);
					taskitem.setPalletPackageList(pList);

					taskItemList.add(taskitem);

					BizStockTaskReqItem record = new BizStockTaskReqItem();
					record.setItemId(item.getItemId());
					record.setStockTaskQty(item.getQty());

					record.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());

					StockTaskReqItemDao.updateByPrimaryKeySelective(record);

				}
				taskHead.setItemList(taskItemList);
				if (reqItemListAdd.size() < reqItemList.size()) {
					// 更新请求状态
					BizStockTaskReqHead record = new BizStockTaskReqHead();
					record.setStockTaskReqId(reqHead.getStockTaskReqId());
					record.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
					StockTaskReqHeadDao.updateByPrimaryKeySelective(record);
				}
				if (reqHead.getReferReceiptType().equals(EnumReceiptType.STOCK_INPUT_TRANSPORT_ERP.getValue())) {
					// 转储入库冲销
					this.addStockTaskReqByTransportWriteOff(taskHead);
				} else if(reqHead.getReferReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_FACTORY.getValue())
						|| reqHead.getReferReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_STOCK.getValue())
						|| reqHead.getReferReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue())){
					this.addStockTaskReqByTransport(taskHead);
					//this.unfreezeStock(taskHead);
				}else if(reqHead.getReferReceiptType().equals(EnumReceiptType.STOCK_OUTPUT_OTHER.getValue())) {
					this.unfreezeStock(taskHead);
				}

			}
		}
	}

	@Override
	public Map<String, Object> updateBizStockTaskRemark(BizStockTaskHeadCw stockTaskHead) throws Exception {
		bizStockTaskHeadCwDao.updateByPrimaryKeySelective(stockTaskHead);
		if (stockTaskHead.getItemList() != null) {
			for (BizStockTaskItemCw item : stockTaskHead.getItemList()) {
				bizStockTaskItemCwDao.updateByPrimaryKeySelective(item);
			}
		}
		return null;
	}

	@Override
	public List<RelTaskUserWarehouse> getWarehouseUserList(Map<String, Object> map) {
		return RelTaskUserWarehouseDao.selectByWhIdAndType(map);
	}

	@Override
	public JSONObject checkCodeTypeByWarehouse(JSONObject json) throws Exception {
		// 校验托盘
		JSONObject returnObj = new JSONObject();
		returnObj.put("type", "");
		returnObj.put("pallet_id", "");
		returnObj.put("pallet_code", "");
		returnObj.put("max_payload", "");
		returnObj.put("have_payload", "");
		returnObj.put("package_num", "");

		returnObj.put("package_id", "");
		returnObj.put("package_code", "");
		returnObj.put("qty", "");
		returnObj.put("package_type_id", "");
		returnObj.put("package_type_code", "");
		returnObj.put("unit_zh", "");

		returnObj.put("wh_id", "");
		returnObj.put("area_id", "");
		returnObj.put("position_id", "");
		returnObj.put("package_list", new JSONArray());
		boolean flag = false;

		String condition = json.getString("condition");
		Byte requestSource = UtilObject.getByteOrNull(json.get("requestSource"));
		if (StringUtils.hasText(condition)) {

			String firstChar = condition.substring(0, 1);
			String otherStr = condition.substring(1, condition.length());
			if (condition.length() == 12) {
				// 托盘
				json.put("havePackageList", false);
				//flag = this.checkPalletCodeOnReq(json, returnObj, flag);
				Map<String, Object> palletMap = new HashMap<String, Object>();
				palletMap.put("locationId", json.get("location_id"));
				palletMap.put("palletCode", json.get("condition"));
				palletMap.put("isDefault", 0);// 非默认仓位库存
				flag = this.checkPalletCodeOnPosition(palletMap, returnObj, flag);
				
				// flag = this.checkPalletCodeOnOperation(json, returnObj,
				// flag);//上架时查关联单/仓库整理不查
				flag = this.checkPalletCodeOnMajor(json, returnObj, flag);
			} else if ("T".equalsIgnoreCase(firstChar)) {
				throw new WMSException("托盘条码错误");
			} else if (condition.length() == 18) {
				// 包
				flag = this.checkPackageCodeOnReq(json, returnObj, flag);
				// Map<String, Object> packageMap = new HashMap<String,
				// Object>();
				// packageMap.put("matId", json.get("mat_id"));
				// packageMap.put("locationId", json.get("location_id"));
				// packageMap.put("batch", json.get("batch"));
				// packageMap.put("packageCode", json.get("condition"));
				// packageMap.put("isDefault", 1);// 默认仓位库存
				// flag = this.checkPackageCodeOnPosition(packageMap, returnObj,
				// flag);
				json.put("debitCredit", EnumDebitCredit.DEBIT_S_ADD.getName());
				flag = this.checkPackageCodeOnOperation(json, returnObj, flag);
			} else if (requestSource.equals(EnumRequestSource.PDA.getValue())) {
				// PDA 查询 仓位
				flag = this.checkPositionCode(json, returnObj, flag);
			}

			if (!flag) {
				throw new WMSException("数据不合法");
			}
		} else {
			throw new WMSException("查询条件为空");
		}

		return returnObj;
	}

	void updateSaleStock(BizStockTaskItemCw stockTaskItem , String debitCredit ,BizAllocateCargoHead achead) throws Exception{

		BatchMaterial record = new BatchMaterial();
		record.setErpBatch(stockTaskItem.getErpBatch());
		record.setProductionBatch(achead.getReceiveCode());
		record.setFtyId(stockTaskItem.getFtyId());
		record.setMatId(stockTaskItem.getMatId());
		record.setPackageTypeId(888888);
		BatchMaterial find = batchMaterialDao.selectByUniqueKey(record);
		Long batch= null;
		if(find!=null){
			batch = find.getBatch();
		}else{
			batch = commonService.getBatch();
			BatchMaterial batchMaterial = new BatchMaterial();
			batchMaterial.setBatch(batch);
			batchMaterial.setMatId(stockTaskItem.getMatId());
			batchMaterial.setFtyId(stockTaskItem.getFtyId());
			batchMaterial.setCreateUser(achead.getCreateUser());
			batchMaterial.setPackageTypeId(888888);
			batchMaterial.setErpBatch(stockTaskItem.getErpBatch());
			batchMaterial.setQualityBatch(achead.getReceiveCode());
			batchMaterial.setProductionBatch(achead.getReceiveCode());
			batchMaterial.setProductLineId(0);
			batchMaterial.setInstallationId(0);
			batchMaterial.setClassTypeId(0);
			batchMaterial.setSupplierName("");
			batchMaterial.setSpecStockName(achead.getReceiveName());
			batchMaterialDao.insertSelective(batchMaterial);
		}
		
		// 添加已售未提库存  
		// 批次库存
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("qty", stockTaskItem.getQty());// 需要下架的总数量
		param.put("batch", batch);// 批次
		param.put("locationId", stockTaskItem.getLocationId());// 库存id
		param.put("matId", stockTaskItem.getMatId());// 物料id
		param.put("createUserId", achead.getCreateUser());
		param.put("debitCredit", debitCredit);
		param.put("status", EnumStockStatus.STOCK_BATCH_STATUS_NO_MENTION.getValue());
		param.put("stockTypeId",EnumStockType.STOCK_TYPE_ERP.getValue());
		commonService.modifyStockBatchCanMinus(param);
		
		Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
		DicStockLocationVo locationObj = locationMap.get(stockTaskItem.getLocationId());
		Integer whId = locationObj.getWhId();
		String whCode = dictionaryService.getWhCodeByWhId(whId);
		String areaCode = UtilProperties.getInstance().getDefaultCCQProduct();
		Integer areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);
		String positionCode = UtilProperties.getInstance().getDefaultCWProduct();
		Integer positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode, areaCode,
				positionCode);
		
		
		//仓位库存
		StockPosition position = new StockPosition();
		position.setDebitOrCredit(debitCredit);
		position.setWhId(stockTaskItem.getWhId());
		position.setAreaId(areaId);
		position.setPositionId(positionId);
		position.setBatch(batch);
		position.setMatId(stockTaskItem.getMatId());
		position.setFtyId(stockTaskItem.getFtyId());
		position.setLocationId(stockTaskItem.getLocationId());
		position.setInputDate(new Date());
		position.setPackageTypeId(888888);
		position.setQty(stockTaskItem.getQty());
		position.setUnitId(stockTaskItem.getUnitId());
		position.setPackageId(0);
		position.setPalletId(0);
		position.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_NO_MENTION.getValue());
		position.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
		commonService.modifyStockPosition(position);
		
		
		
	
	
	}
	
	@Override
	public void deleteTaskById(Integer taskId) throws Exception {

		// 判断是否过账 完成状态不可删除

		BizStockTaskItemCw statusFind = new BizStockTaskItemCw();
		statusFind.setStockTaskId(taskId);

		Byte isComplete = bizStockTaskItemCwDao.selectMaxStatus(statusFind);
		if (isComplete != null && isComplete.compareTo(EnumReceiptStatus.RECEIPT_UNFINISH.getValue()) == 0) {

		} else {
			throw new WMSException("存在已关联的作业单行项目，无法删除");
		}
		
		
		
		
		// 更改前续单据 状态
		BizStockTaskHeadCw stockTaskHead = new BizStockTaskHeadCw();
		stockTaskHead  = bizStockTaskHeadCwDao.selectByPrimaryKey(taskId);
		// 判断是否为转储单下架作业 或者转储入库下架作业 如果生成上架作业 不删除
		Byte referReceiptType = stockTaskHead.getReferReceiptType();
		Byte receiptType = stockTaskHead.getReceiptType();
		
		boolean isSale = false;
		BizAllocateCargoHead ahead = null;
		if(stockTaskHead.getReferReceiptType().equals(EnumReceiptType.STOCK_OUTPUT_ALLOCATE_CARGO.getValue())){
			// 判断是否是特殊配货单
			ahead =  AllocateCargoHeadDao.selectByPrimaryKey(stockTaskHead.getReferReceiptId());
			if(ahead!=null&&EnumAllocateCargoOperationType.NO_MENTION.getValue()==ahead.getOperationType()){
				//已售未提 的交货单
				isSale = true;
			}
			
		}
		if((referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_FACTORY.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_STOCK.getValue())
				|| referReceiptType.equals(EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue())
				||referReceiptType.equals(EnumReceiptType.STOCK_INPUT_TRANSPORT_ERP.getValue()))
				&& receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())){
			// 判断是否有未删除的上架作业单
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("removeTaskId", taskId);
			Integer count =  bizStockTaskReqHeadDao.countTaskByParam(map);
			if(count!=null&&count>0){
				throw new WMSException("该下架作业单生成的上架作业单未删除");
			}else{
				// 删除上架请求
				// 删除请求

				Map<String, Object> recordHead = new HashMap<String, Object>();
				recordHead.put("referReceiptId", stockTaskHead.getReferReceiptId());
				recordHead.put("referReceiptType", stockTaskHead.getReferReceiptType());
				recordHead.put("receiptType", EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue());
				recordHead.put("isDelete", (byte) 1);
				recordHead.put("removeTaskId", taskId);
				StockTaskReqHeadDao.updateDeleteByReferReceiptIdAndType(recordHead);
				
				Map<String, Object> recordItem = new HashMap<String, Object>();
				recordItem.put("referReceiptId", stockTaskHead.getReferReceiptId());
				recordItem.put("referReceiptType", stockTaskHead.getReferReceiptType());
				recordItem.put("receiptType", EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue());
				recordItem.put("isDelete", (byte) 1);
				recordItem.put("removeTaskId", taskId);
				recordItem.put("isDelete", (byte) 1);

				StockTaskReqItemDao.updateDeleteByReferReceiptIdAndType(recordItem);

				Map<String, Object> recordPosition = new HashMap<String, Object>();
				recordPosition.put("referReceiptId", stockTaskHead.getReferReceiptId());
				recordPosition.put("referReceiptType", stockTaskHead.getReferReceiptType());
				recordPosition.put("receiptType", EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue());
				recordPosition.put("isDelete", (byte) 1);
				recordPosition.put("removeTaskId", taskId);
				recordPosition.put("isDelete", (byte) 1);
				stockTaskReqPositionDao.updateDeleteByReferReceiptIdAndType(recordPosition);
			}
		}
		
		
		this.updateReferDocByDelete(stockTaskHead);
		
		
		List<BizStockTaskItemCw> itemList = new ArrayList<BizStockTaskItemCw>();
		List<BizStockTaskPositionCw> pList = new ArrayList<BizStockTaskPositionCw>();

		BizStockTaskPositionCw find = new BizStockTaskPositionCw();
		find.setStockTaskId(taskId);
		pList = bizStockTaskPositionCwDao.selectByReferReceiptIdAndType(find);
		BizStockTaskItemCw itemFind = new BizStockTaskItemCw();
		itemFind.setStockTaskId(taskId);
		itemList = bizStockTaskItemCwDao.selectByReferReceiptIdAndType(itemFind);
		Map<String, BizStockTaskItemCw> itemMap = new HashMap<String, BizStockTaskItemCw>();
		for (BizStockTaskItemCw item : itemList) {
			String key = item.getStockTaskId() + "-" + item.getStockTaskRid();
			itemMap.put(key, item);
			
			if(isSale){
				// 返回已售未提库存
				String debitCredit = null;
				if(stockTaskHead.getReceiptType().equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())){
					debitCredit = EnumDebitCredit.CREDIT_H_SUBTRACT.getName();
				}else{
					debitCredit = EnumDebitCredit.DEBIT_S_ADD.getName();
				}
				
				updateSaleStock(item, debitCredit, ahead);
				
			}
			
		}
		// 修改库存

		BizStockTaskHeadCw head = new BizStockTaskHeadCw();
		head.setStockTaskId(taskId);
		head = bizStockTaskHeadCwDao.selectByPrimaryKey(taskId);

		if (pList != null && pList.size() > 0) {
			for (BizStockTaskPositionCw p : pList) {
				String key = p.getStockTaskId() + "-" + p.getStockTaskRid();
				BizStockTaskItemCw item = itemMap.get(key);
				
				
				
				this.updateStockPositionByDelete(item, p);
				
				
				
				//补回 前续单据 req_position 
				BizStockTaskReqPosition rp = new BizStockTaskReqPosition();
			//	rp.setPalletId(p.getPalletId());
				rp.setPackageId(p.getPackageId());
				rp.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
				StockTaskReqPositionDao.updateByPackageId(rp);
				this.addHistoryByDelete(head, item, p);
				this.freezeStock(head, item, p);
			}
		}

		// 请求 数据修改
		bizStockTaskReqItemDao.updateItemQtyByDelete(taskId);

		// 删除单据
		BizStockTaskHeadCw recordHead = new BizStockTaskHeadCw();
		recordHead.setStockTaskId(taskId);
		recordHead.setIsDelete((byte) 1);
		bizStockTaskHeadCwDao.updateByPrimaryKeySelective(recordHead);
		BizStockTaskItemCw recordItem = new BizStockTaskItemCw();
		recordItem.setStockTaskId(taskId);
		recordItem.setIsDelete((byte) 1);
		bizStockTaskItemCwDao.updateByStockTaskId(recordItem);

		BizStockTaskPositionCw recordPosition = new BizStockTaskPositionCw();
		recordPosition.setStockTaskId(taskId);
		recordPosition.setIsDelete((byte) 1);
		bizStockTaskPositionCwDao.updateByStockTaskId(recordPosition);

		
		
		

	}

	@Override
	public void deleteTaskReqAndTaskByReferIdAndType(Integer referReceiptId, Byte referReceiptType) throws Exception {

		Byte type = null;
		Byte reqType = null;
		if (referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_ALLOCATE_CARGO.getValue())) {
			type = EnumReceiptType.STOCK_TASK_REMOVAL.getValue();
		}
		if (referReceiptType.equals(EnumReceiptType.STOCK_OUTPUT_ALLOCATE_CARGO.getValue())) {
			reqType = EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue();
		}

		BizStockTaskItemCw statusFind = new BizStockTaskItemCw();
		statusFind.setReferReceiptType(referReceiptType);
		statusFind.setReferReceiptId(referReceiptId);
		statusFind.setReceiptType(type);
		Byte isComplete = bizStockTaskItemCwDao.selectMaxStatus(statusFind);
		if (isComplete == null) {

		} /*else if (isComplete != null && isComplete.compareTo(EnumReceiptStatus.RECEIPT_UNFINISH.getValue()) == 0) {
			// 删除作业单 更新库存

			List<BizStockTaskItemCw> itemList = new ArrayList<BizStockTaskItemCw>();
			List<BizStockTaskPositionCw> pList = new ArrayList<BizStockTaskPositionCw>();

			BizStockTaskPositionCw find = new BizStockTaskPositionCw();
			find.setReferReceiptId(referReceiptId);
			find.setReferReceiptType(referReceiptType);
			find.setReceiptType(type);
			pList = bizStockTaskPositionCwDao.selectByReferReceiptIdAndType(find);
			BizStockTaskItemCw itemFind = new BizStockTaskItemCw();
			itemFind.setReferReceiptId(referReceiptId);
			itemFind.setReferReceiptType(referReceiptType);
			itemFind.setReceiptType(type);
			itemList = bizStockTaskItemCwDao.selectByReferReceiptIdAndType(itemFind);
			Map<String, BizStockTaskItemCw> itemMap = new HashMap<String, BizStockTaskItemCw>();
			for (BizStockTaskItemCw item : itemList) {
				String key = item.getStockTaskId() + "-" + item.getStockTaskRid();
				itemMap.put(key, item);
			}
			// 修改库存
			Map<Integer, BizStockTaskHeadCw> headMap = new HashMap<Integer, BizStockTaskHeadCw>();
			if (pList != null && pList.size() > 0) {
				for (BizStockTaskPositionCw p : pList) {
					String key = p.getStockTaskId() + "-" + p.getStockTaskRid();
					BizStockTaskItemCw item = itemMap.get(key);
					this.updateStockPositionByDelete(item, p);
					BizStockTaskHeadCw head = new BizStockTaskHeadCw();
					if (headMap.containsKey(p.getStockTaskId())) {
						head = headMap.get(p.getStockTaskId());

					} else {
						head = bizStockTaskHeadCwDao.selectByPrimaryKey(p.getStockTaskId());
						headMap.put(p.getStockTaskId(), head);
					}
					this.addHistoryByDelete(head, item, p);
					this.freezeStock(head, item, p);
				}
			}
			// 删除单据
			Map<String, Object> recordHead = new HashMap<String, Object>();
			recordHead.put("referReceiptId", referReceiptId);
			recordHead.put("referReceiptType", referReceiptType);
			recordHead.put("receiptType", type);
			recordHead.put("isDelete", (byte) 1);
			bizStockTaskHeadCwDao.updateDeleteByReferReceiptIdAndType(recordHead);

			Map<String, Object> recordItem = new HashMap<String, Object>();
			recordItem.put("referReceiptId", referReceiptId);
			recordItem.put("referReceiptType", referReceiptType);
			recordItem.put("receiptType", type);
			recordItem.put("isDelete", (byte) 1);

			bizStockTaskItemCwDao.updateDeleteByReferReceiptIdAndType(recordItem);

			Map<String, Object> recordPosition = new HashMap<String, Object>();
			recordPosition.put("referReceiptId", referReceiptId);
			recordPosition.put("referReceiptType", referReceiptType);
			recordPosition.put("receiptType", type);
			recordPosition.put("isDelete", (byte) 1);
			bizStockTaskPositionCwDao.updateDeleteByReferReceiptIdAndType(recordPosition);*/

		else {
			throw new WMSException("还有未删除作业单无法删除");
		}

		// 删除请求

		Map<String, Object> recordHead = new HashMap<String, Object>();
		recordHead.put("referReceiptId", referReceiptId);
		recordHead.put("referReceiptType", referReceiptType);
		recordHead.put("receiptType", reqType);
		recordHead.put("isDelete", (byte) 1);
		StockTaskReqHeadDao.updateDeleteByReferReceiptIdAndType(recordHead);

		Map<String, Object> recordItem = new HashMap<String, Object>();
		recordItem.put("referReceiptId", referReceiptId);
		recordItem.put("referReceiptType", referReceiptType);
		recordItem.put("receiptType", reqType);
		recordItem.put("isDelete", (byte) 1);

		StockTaskReqItemDao.updateDeleteByReferReceiptIdAndType(recordItem);

		Map<String, Object> recordPosition = new HashMap<String, Object>();
		recordPosition.put("referReceiptId", referReceiptId);
		recordPosition.put("referReceiptType", referReceiptType);
		recordPosition.put("receiptType", reqType);
		recordPosition.put("isDelete", (byte) 1);
		stockTaskReqPositionDao.updateDeleteByReferReceiptIdAndType(recordPosition);

	}

	@Override
	public JSONArray getAreaListByLocationIdForWarehouse(Integer locationId) throws Exception {
		Map<Integer, DicStockLocationVo> locationIdMap = dictionaryService.getLocationIdMap();
		Integer whId = locationIdMap.get(locationId).getWhId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("whId", whId);
		map.put("isDefaultWarehouse", "(0)"); // 下拉查询非默认存储区
//		if (locationIdMap.get(locationId).getFtyType().equals(EnumFtyType.PRODUCT.getValue())) {
//			map.put("isDefaultWarehouse", "(0,1)"); // 默认存储区
//		} else {
//			map.put("isDefaultWarehouse", "(0)"); // 非默认存储区
//		}

		List<Map<String, Object>> positionList = warehousePositionDao.selectWarehousePositionOnPaging(map);
		return getAreaList(positionList);
	}

	@Override
	public void updateStockPostionByDate() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Date date = UtilString.getDateForString("2018-08-03");
		map.put("createTime", date);
		List<BizStockTaskItemCw> taskList = bizStockTaskItemCwDao.selectByDate(map);
		if(taskList!=null&&taskList.size()>0){
			for(BizStockTaskItemCw task:taskList){
				BizStockTaskPositionCw record = new BizStockTaskPositionCw();
				record.setStockTaskId(task.getStockTaskId());
				record.setStockTaskRid(task.getStockTaskRid());
				List<BizStockTaskPositionCw> positionList = this
						.getStockTaskPositionListByReqItemId(record);
				task.setPalletPackageList(positionList);
				this.updatePostionBase(task);
			}
		}
	}

}
