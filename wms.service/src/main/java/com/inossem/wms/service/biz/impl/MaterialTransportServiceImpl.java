package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.batch.BatchMaterialMapper;
import com.inossem.wms.dao.biz.BizInputProduceHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqItemMapper;
import com.inossem.wms.dao.biz.BizStockTransportHeadCwMapper;
import com.inossem.wms.dao.biz.BizStockTransportItemCwMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicDeliveryDriverMapper;
import com.inossem.wms.dao.dic.DicDeliveryVehicleMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicMaterialMapper;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.stock.StockBatchMapper;
import com.inossem.wms.dao.stock.StockOccupancyMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.biz.BizStockTransportHeadCw;
import com.inossem.wms.model.biz.BizStockTransportItemCw;
import com.inossem.wms.model.dic.DicDeliveryDriver;
import com.inossem.wms.model.dic.DicDeliveryVehicle;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.enums.EnumDebitCredit;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumStockType;
import com.inossem.wms.model.enums.EnumTaskReqShippingType;
import com.inossem.wms.model.key.StockPositionKey;
import com.inossem.wms.model.stock.StockOccupancy;
import com.inossem.wms.model.vo.BizReceiptAttachmentVo;
import com.inossem.wms.model.vo.DicFactoryVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IFileService;
import com.inossem.wms.service.biz.ITaskService;
import com.inossem.wms.service.biz.MaterialTransportService;
import com.inossem.wms.util.UtilObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service
public class MaterialTransportServiceImpl implements MaterialTransportService {

	@Resource
	private BizStockTransportHeadCwMapper materialTransportHeadDao;

	@Autowired
	private DicStockLocationMapper locationDao;

	@Resource
	private BizStockTransportItemCwMapper materialTransportItemDao;

	@Resource
	private DicFactoryMapper factoryDao;

	@Resource
	private SequenceDAO sequenceDao;

	@Resource
	private BizStockTaskReqHeadMapper biztaskHeadDao;

	@Resource
	private BizStockTaskReqItemMapper biztaskItemDao;

	@Resource
	private DicMaterialMapper materialDao;

	@Autowired
	private IDictionaryService dictionaryService;

	@Autowired
	private StockBatchMapper stockBatchDao;

	@Autowired
	private StockOccupancyMapper stockOccupancyDao;

	@Autowired
	private BatchMaterialMapper batchMaterialDao;

	@Autowired
	private ICommonService commonService;

	@Autowired
	ITaskService taskService;

	@Autowired
	private IFileService fileService;

	@Autowired
	private DicDeliveryDriverMapper DeliveryDriverDao;

	@Autowired
	private DicDeliveryVehicleMapper DeliveryVehicleDao;

	@Autowired
	private BizInputProduceHeadMapper bizInputProduceHeadDao;

	@Override
	public List<Map<String, Object>> getDumpList(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return materialTransportHeadDao.selectDumpListOnPaging(param);
	}

	@Override
	public List<DicMoveType> getMoveList() throws Exception {
		// TODO Auto-generated method stub
		return commonService.getMoveTypeByReceiptType(EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue());
	}

	@Override
	public List<DicStockLocationVo> locListByFty(Integer ftyid) {
		// TODO Auto-generated method stub
		return locationDao.selectLocationsByFtyId(ftyid);
	}

	@Override
	public List<DicFactoryVo> selectFtylist() {
		// TODO Auto-generated method stub
		return factoryDao.selectAllFactory();
	}

	@Override
	public List<Map<String, Object>> getMaterial(String matcode) {
		// TODO Auto-generated method stub
		return materialTransportItemDao.getMaterial(matcode);
	}

	@Override
	public BatchMaterial getMaterialbyBatchOrID(Long batch, Integer matid) {
		// TODO Auto-generated method stub
		return materialTransportItemDao.getMaterialbyBatchOrID(batch, matid);
	}

	@Override
	public String getNowTime() {
		// TODO Auto-generated method stub
		Date now = new Date();
		SimpleDateFormat sdate = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdate.format(now);
		return date;
	}

	@Override
	public List<Map<String, Object>> getClassMap() {
		// TODO Auto-generated method stub
		return materialTransportHeadDao.getClassMap();
	}

	@Override
	public StockPositionKey getStockPositionBymatid(Integer matid) {
		// TODO Auto-generated method stub
		return materialTransportItemDao.getStockPositionBymatid(matid);
	}

	@Override
	public DicPackageType getPackageTypeByID(Integer package_type_id) {
		// TODO Auto-generated method stub
		return materialTransportItemDao.getPackageTypeByID(package_type_id);
	}

	/*
	 * @Override public DicStockType getStockTypeByID(Integer stock_type_id) { //
	 * TODO Auto-generated method stub return
	 * materialTransportItemDao.getStockTypeByID(stock_type_id); }
	 */

	@Override
	public Map<String, Object> getDumpByTransportID(Integer stock_transport_id) throws Exception {
		// TODO Auto-generated method stub

		Map<String, Object> head = materialTransportHeadDao.selectHeadById(stock_transport_id);
		int syn_type = UtilObject.getIntOrZero(head.get("syn_type"));
		String syn_type_name = null;
		if (syn_type == 0) {
			syn_type_name = "不同步";
		} else if (syn_type == 1) {
			syn_type_name = "同步SAP";
		} else {
			syn_type_name = "同步ERP&MES";
		}
		head.put("syn_type_name", syn_type_name);
		List<Map<String, Object>> itemlist = materialTransportItemDao.selectItemById(stock_transport_id);
		List<Map<String, Object>> item = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : itemlist) {
			DicMaterial mat = materialDao.selectByPrimaryKey(UtilObject.getIntOrZero(map.get("mat_id")));
			map.put("mat_input_code", mat.getMatCode());
			item.add(map);
		}

		head.put("Item_list", item);
		List<BizReceiptAttachmentVo> fileList = fileService.getReceiptAttachments(stock_transport_id,
				UtilObject.getByteOrNull(head.get("receipt_type")));
		head.put("fileList", fileList);

		return head;
	}

	@Override
	public void InsertDump(BizStockTransportHeadCw st) {
		// TODO Auto-generated method stub
		materialTransportHeadDao.InsertDump(st);
	}

	@Override
	public String getMaterialTransCode() {
		// TODO Auto-generated method stub

		return String.valueOf(sequenceDao.selectNextVal("material_transport"));
	}

	/*
	 * @Override public BizStockTransportHeadCw getDumpItemByTransportID(int
	 * stock_transport_id) throws Exception { // TODO Auto-generated method stub
	 * 
	 * Map<String, Object> param = new HashMap<String, Object>();
	 * param.put("stock_transport_id", stock_transport_id); BizStockTransportHeadCw
	 * head = materialTransportHeadDao.selectHeadById(param);
	 * List<BizStockTransportItemCw> itemList =
	 * materialTransportItemDao.selectItemById(param);
	 * 
	 * head.setList_item(itemList); return head; }
	 */

	@Override
	public DicUnit getUnitByID(int unitid) {
		// TODO Auto-generated method stub
		return materialTransportItemDao.getUnitByID(unitid);
	}

	@Override
	public List<Map<String, Object>> getMatListByParam(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return materialTransportItemDao.selectMatListByParam(map);
	}

	/*
	 * <p>Description: 插入物料转储HEAD</p> <p>Title: InsertStockTransportHeadCw</br></p>
	 * 
	 * @param json
	 * 
	 * @param userId
	 * 
	 * @return
	 * 
	 * @throws Exception</br>
	 * 
	 * @see com.inossem.wms.service.biz.MaterialTransportService#
	 * InsertStockTransportHeadCw(net.sf.json.JSONObject, java.lang.String)</br>
	 */
	@Override
	public int InsertStockTransportHeadCw(JSONObject json, String userId) throws Exception {
		int stock_task_req_id = 0;
		JSONArray jsonlist = json.getJSONArray("item_list");
		BizStockTransportHeadCw stHeadCW = new BizStockTransportHeadCw();
		stHeadCW.setStockTransportCode(this.getMaterialTransCode());
		stHeadCW.setMoveTypeId(UtilObject.getIntegerOrNull(json.get("move_type_id")));
		stHeadCW.setFtyId(UtilObject.getIntOrZero(json.get("fty_id")));
		stHeadCW.setLocationId(UtilObject.getIntOrZero(json.get("location_id")));

		stHeadCW.setStatus((byte) 2);
		stHeadCW.setCreateUser(userId);
		stHeadCW.setRemark(UtilObject.getStringOrEmpty(json.get("remark")));
		stHeadCW.setClassTypeId(UtilObject.getIntOrZero(json.get("schedules")));
		stHeadCW.setIsDelete((byte) 0);
		stHeadCW.setReceiptType(EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue());
		stHeadCW.setSynType(UtilObject.getIntegerOrNull(json.get("syn_type")));
		stHeadCW.setDeliveryDriverId(UtilObject.getIntegerOrNull(json.get("delivery_driver_id")));
		stHeadCW.setDeliveryVehicleId(UtilObject.getIntegerOrNull(json.get("delivery_vehicle_id")));

		materialTransportHeadDao.insertSelective(stHeadCW);
		stock_task_req_id = this.InsertBizStockTaskReqHead(stHeadCW);

		JSONArray fileAry = json.getJSONArray("file_list");
		commonService.saveFileAry(stHeadCW.getStockTransportId(), stHeadCW.getReceiptType(), stHeadCW.getCreateUser(),
				fileAry);

		for (int i = 0; i < jsonlist.size(); i++) {

			BizStockTransportItemCw st = new BizStockTransportItemCw();
			JSONObject object = jsonlist.getJSONObject(i);

			BatchMaterial param = new BatchMaterial();
			param.setBatch(UtilObject.getLongOrNull(object.get("batch")));
			param.setMatId(UtilObject.getIntegerOrNull(object.get("mat_id")));
			param.setFtyId(stHeadCW.getFtyId());
			BatchMaterial find = batchMaterialDao.selectByUniqueKey(param);
			if (find == null) {
				throw new WMSException("无生产批次信息");
			}

			st.setStockTransportId(UtilObject.getIntOrZero(stHeadCW.getStockTransportId()));
			st.setStockTransportRid(UtilObject.getIntegerOrNull(object.get("stock_transport_rid")));
			st.setMatId(UtilObject.getIntegerOrNull(object.get("mat_id")));
			st.setPackageTypeId(UtilObject.getIntOrZero(object.get("package_type_id")));
			st.setTransportQty(UtilObject.getBigDecimalOrZero(object.get("transport_qty")));
			st.setProductionBatch(UtilObject.getStringOrNull(object.get("product_batch")));
			st.setErpBatch(UtilObject.getStringOrNull(object.get("erp_batch")));
			st.setBatch(UtilObject.getLongOrNull(object.get("batch")));
			st.setStockQty(UtilObject.getBigDecimalOrZero(object.get("stock_qty")));
			st.setFtyInput(UtilObject.getIntOrZero(object.get("fty_input")));
			st.setLocationInput(UtilObject.getIntOrZero(object.get("location_input")));
			st.setFtyOutput(stHeadCW.getFtyId());
			st.setLocationOutput(stHeadCW.getLocationId());
			st.setUnitId(UtilObject.getIntOrZero(object.get("unit_id")));
			st.setQualityBatch(UtilObject.getStringOrNull(object.get("quality_batch")));
			st.setIsDelete((byte) 0);
			st.setRemark(UtilObject.getStringOrNull(object.get("remark")));
			String c = UtilObject.getStringOrNull(object.get("mat_input"));

			if (null == c || c.equals(null)) {

				StockOccupancy stockOccupancy = new StockOccupancy();
				stockOccupancy.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
				stockOccupancy.setMatId(UtilObject.getIntegerOrNull(object.get("mat_id")));
				stockOccupancy.setFtyId(UtilObject.getIntOrZero(stHeadCW.getFtyId()));
				stockOccupancy.setLocationId(UtilObject.getIntOrZero(stHeadCW.getLocationId()));
				stockOccupancy.setPackageTypeId(UtilObject.getIntOrZero(object.get("package_type_id")));
				stockOccupancy.setBatch(UtilObject.getLongOrNull(object.get("batch")));
				stockOccupancy.setQty(UtilObject.getBigDecimalOrZero(object.get("transport_qty")));

				commonService.modifyStockOccupancy(stockOccupancy, EnumDebitCredit.DEBIT_S_ADD.getName());

				if (!stHeadCW.getFtyId().equals(UtilObject.getIntOrZero(object.get("fty_input")))) {
					BatchMaterial batchMaterial = new BatchMaterial();

					batchMaterial.setBatch(UtilObject.getLongOrNull(object.get("batch")));
					batchMaterial.setMatId(UtilObject.getIntegerOrNull(object.get("mat_id")));
					batchMaterial.setFtyId(UtilObject.getIntOrZero(object.get("fty_input")));
					batchMaterial.setCreateUser(userId);
					batchMaterial.setPackageTypeId(UtilObject.getIntOrZero(object.get("package_type_id")));
					batchMaterial.setErpBatch(UtilObject.getStringOrNull(object.get("erp_batch")));
					batchMaterial.setQualityBatch(UtilObject.getStringOrNull(object.get("quality_batch")));
					batchMaterial.setProductionBatch(UtilObject.getStringOrNull(object.get("product_batch")));

					batchMaterial.setPurchaseOrderCode(find.getPurchaseOrderCode());
					batchMaterial.setProductLineId(find.getProductLineId());
					batchMaterial.setInstallationId(find.getInstallationId());
					batchMaterial.setClassTypeId(UtilObject.getIntOrZero(json.get("schedules")));
					batchMaterialDao.insertSelective(batchMaterial);

				}

				st.setMatInput(UtilObject.getIntOrZero(object.get("mat_id")));
				st.setProductionBatchInput(UtilObject.getStringOrNull(object.get("product_batch")));
				st.setErpBatchInput(UtilObject.getStringOrNull(object.get("erp_batch")));
				st.setQualityBatchInput(UtilObject.getStringOrNull(object.get("quality_batch")));

			} else {
				List<Map<String, Object>> mat = this
						.getMaterial(UtilObject.getStringOrNull(object.get("mat_input")).trim());
				Integer mat_id = 0;
				if (mat.size() > 0) {
					mat_id = UtilObject.getIntegerOrNull(mat.get(0).get("mat_id"));
				}

				StockOccupancy stockOccupancy = new StockOccupancy();
				stockOccupancy.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
				stockOccupancy.setMatId(UtilObject.getIntegerOrNull(object.get("mat_id")));
				stockOccupancy.setFtyId(UtilObject.getIntOrZero(stHeadCW.getFtyId()));
				stockOccupancy.setLocationId(UtilObject.getIntOrZero(stHeadCW.getLocationId()));
				stockOccupancy.setPackageTypeId(UtilObject.getIntOrZero(object.get("package_type_id")));
				stockOccupancy.setBatch(UtilObject.getLongOrNull(object.get("batch")));
				stockOccupancy.setQty(UtilObject.getBigDecimalOrZero(object.get("transport_qty")));

				commonService.modifyStockOccupancy(stockOccupancy, EnumDebitCredit.DEBIT_S_ADD.getName());

				if (UtilObject.getIntegerOrNull(json.get("move_type_id")) == 3 && mat != null && mat.size() > 0) {
					BatchMaterial batchMaterial = new BatchMaterial();

					batchMaterial.setBatch(UtilObject.getLongOrNull(object.get("batch")));
					batchMaterial.setMatId(mat_id);
					batchMaterial.setSupplierCode("");
					batchMaterial.setSupplierName("");
					batchMaterial.setFtyId(UtilObject.getIntOrZero(object.get("fty_input")));
					batchMaterial.setSpecStock("");
					batchMaterial.setSpecStockCode("");
					batchMaterial.setSpecStockName("");
					batchMaterial.setCreateUser(userId);
					batchMaterial.setPackageTypeId(UtilObject.getIntOrZero(object.get("package_type_id")));
					batchMaterial.setErpBatch(UtilObject.getStringOrNull(object.get("erp_batch_input")));
					batchMaterial.setQualityBatch(UtilObject.getStringOrNull(object.get("quality_batch")));
					batchMaterial.setProductionBatch(UtilObject.getStringOrNull(object.get("production_batch_input")));

					batchMaterial.setPurchaseOrderCode(find.getPurchaseOrderCode());
					batchMaterial.setProductLineId(find.getProductLineId());
					batchMaterial.setInstallationId(find.getInstallationId());
					batchMaterial.setClassTypeId(UtilObject.getIntOrZero(json.get("schedules")));
					batchMaterialDao.insertSelective(batchMaterial);

					st.setProductionBatchInput(UtilObject.getStringOrNull(object.get("production_batch_input")));
					st.setErpBatchInput(UtilObject.getStringOrNull(object.get("erp_batch_input")));
					st.setQualityBatchInput(UtilObject.getStringOrNull(object.get("production_batch_input")));
					st.setMatInput(UtilObject.getIntegerOrNull(mat_id));
				}
			}

			materialTransportItemDao.insertSelective(st);
			this.InsertBizStockTasReqkItem(st, stock_task_req_id, UtilObject.getIntOrZero(i + 1), stHeadCW);
		}

		taskService.transportByProduct(stock_task_req_id);
		return stHeadCW.getStockTransportId();
	}

	@SuppressWarnings("deprecation")
	@Override
	public int InsertBizStockTaskReqHead(BizStockTransportHeadCw record) {
		BizStockTaskReqHead strHead = new BizStockTaskReqHead();
		Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
		DicStockLocationVo locationObj = locationMap.get(record.getLocationId());
		strHead.setStockTaskReqCode(String.valueOf(sequenceDao.selectNextVal("stock_task_req")));
		strHead.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
		strHead.setWhId(locationObj.getWhId());
		strHead.setFtyId(record.getFtyId());
		strHead.setLocationId(record.getLocationId());
		strHead.setIsDelete(EnumBoolean.FALSE.getValue());
		strHead.setShippingType(EnumTaskReqShippingType.STOCK_INPUT.getValue());
		strHead.setCreateUser(record.getCreateUser());
		strHead.setReferReceiptType((record.getReceiptType()));
		strHead.setReferReceiptId(record.getStockTransportId());
		strHead.setReferReceiptCode(record.getStockTransportCode());
		strHead.setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue());
		strHead.setDeliveryDriverId(record.getDeliveryDriverId());
		DicDeliveryDriver driver = DeliveryDriverDao.selectByPrimaryKey(record.getDeliveryDriverId());
		strHead.setDeliveryVehicleId(record.getDeliveryVehicleId());
		DicDeliveryVehicle vehicle = DeliveryVehicleDao.selectByPrimaryKey(record.getDeliveryVehicleId());
		if (driver != null) {
			strHead.setDeliveryDriver(driver.getDeliveryDriverName());
		}
		if (vehicle != null) {
			strHead.setDeliveryVehicle(vehicle.getDeliveryVehicleName());
		}
		strHead.setRemark(record.getRemark());

		biztaskHeadDao.insertSelective(strHead);

		int stock_task_req_id = strHead.getStockTaskReqId();

		return stock_task_req_id;
	}

	@Override
	public void InsertBizStockTasReqkItem(BizStockTransportItemCw record, int stock_task_req_id, int i,
			BizStockTransportHeadCw rerecord) {
		BizStockTaskReqItem strItem = new BizStockTaskReqItem();

		Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
		DicStockLocationVo locationObj = locationMap.get(record.getLocationOutput());
		strItem.setStockTaskReqId(stock_task_req_id);
		strItem.setStockTaskReqRid(i);
		strItem.setReferReceiptCode(UtilObject.getStringOrNull(rerecord.getStockTransportCode()));
		strItem.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
		strItem.setWhId(locationObj.getWhId());
		strItem.setFtyId(record.getFtyOutput());
		strItem.setLocationId(record.getLocationOutput());
		strItem.setMatId(record.getMatId());
		strItem.setUnitId(record.getUnitId());
		strItem.setBatch(record.getBatch());
		strItem.setStockTaskQty(new BigDecimal(0));
		strItem.setQty(record.getTransportQty());
		strItem.setIsDelete(EnumBoolean.FALSE.getValue());
		strItem.setPackageTypeId(record.getPackageTypeId());
		strItem.setProductionBatch(record.getProductionBatch());
		strItem.setErpBatch(record.getErpBatch());
		strItem.setReferReceiptType(rerecord.getReceiptType());
		strItem.setReferReceiptId(record.getStockTransportId());
		strItem.setReferReceiptRid(record.getStockTransportRid());
		strItem.setQualityBatch(record.getQualityBatch());
		strItem.setRemark(record.getRemark());
		biztaskItemDao.insertSelective(strItem);
	}

	@Override
	public int selectReceipt(Integer moveTypeId) {
		// TODO Auto-generated method stub
		return materialTransportHeadDao.selectReceipt(moveTypeId);
	}

	@Override
	public void deleteDump(Integer stockTransportId) throws Exception {
		BizStockTransportHeadCw head = new BizStockTransportHeadCw();
		head.setStockTransportId(stockTransportId);
		head.setIsDelete((byte) 1);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("stock_transport_id", stockTransportId);

		// 解冻
		// Map<String, Object> headCW =
		// materialTransportHeadDao.selectHeadById(stockTransportId);
		List<Map<String, Object>> itemlist = materialTransportItemDao.selectItemById(stockTransportId);
		for (Map<String, Object> map : itemlist) {
			StockOccupancy stockOccupancy = new StockOccupancy();
			stockOccupancy.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
			stockOccupancy.setMatId(UtilObject.getIntegerOrNull(map.get("mat_id")));
			stockOccupancy.setFtyId(UtilObject.getIntOrZero(map.get("fty_output")));
			stockOccupancy.setLocationId(UtilObject.getIntOrZero(map.get("location_output")));
			stockOccupancy.setPackageTypeId(UtilObject.getIntOrZero(map.get("package_type_id")));
			stockOccupancy.setBatch(UtilObject.getLongOrNull(map.get("batch")));
			stockOccupancy.setQty(UtilObject.getBigDecimalOrZero(map.get("transport_qty")));

			commonService.modifyStockOccupancy(stockOccupancy, EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
		}
		taskService.deleteTaskReqAndTaskByReferIdAndType(stockTransportId,
				EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue());
		materialTransportHeadDao.updateByPrimaryKeySelective(head);
		materialTransportItemDao.deleteDumpItem(params);
	}

	@Override
	public List<Map<String, Object>> selectERP(String mat_id, int fty_id, int package_id) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("mat_id", mat_id);
		para.put("package_id", package_id);
		Map<String, Object> package_type_list = bizInputProduceHeadDao.selectPackageTypeListByP(para);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mat_id", mat_id);
		param.put("fty_id", fty_id);
		List<Map<String, Object>> list = bizInputProduceHeadDao.selectErpList(param);
		List<Map<String, Object>> erpList = new ArrayList<Map<String, Object>>();
		if (list != null && package_type_list != null) {
			for (int i = 0; i < list.size(); i++) {
				// if(package_id == UtilObject.getIntOrZero(map.get("package_type_id"))){
				String erp_batch_key = UtilObject.getStringOrEmpty(package_type_list.get("erp_batch_key"));
				String erp_batch_code = UtilObject.getStringOrEmpty(list.get(i).get("erp_batch_code"));
				if (erp_batch_code != null && erp_batch_code.contains(erp_batch_key)) {
					erpList.add(list.get(i));
					// }
				}
			}
		}

		return erpList;
		/*
		 * List<Map<String, Object>> l=new ArrayList<Map<String,Object>>();
		 * List<Map<String, Object>>
		 * package_type_list=bizInputProduceHeadDao.selectPackageTypeList(mat_id);
		 * Map<String, Object> param=new HashMap<String,Object>(); param.put("mat_id",
		 * mat_id); param.put("fty_id", fty_id); List<Map<String, Object>>
		 * list=bizInputProduceHeadDao.selectErpList(param); for(Map<String, Object>
		 * map:package_type_list) { List<Map<String, Object>> erpList = new
		 * ArrayList<Map<String,Object>>(); if(list!=null){ for(int
		 * i=0;i<list.size();i++) { String erp_batch_key =
		 * UtilObject.getStringOrEmpty(map.get("erp_batch_key")); String erp_batch_code
		 * = UtilObject.getStringOrEmpty(list.get(i).get("erp_batch_code"));
		 * if(erp_batch_code!=null&&erp_batch_code.contains(erp_batch_key)) {
		 * erpList.add(list.get(i)); } } } map.put("erp_batch_list", erpList);
		 * l.add(map); } return l;
		 */
	}
}
