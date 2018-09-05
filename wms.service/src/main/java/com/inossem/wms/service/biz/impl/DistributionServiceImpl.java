package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.biz.BizAllocateCargoHeadMapper;
import com.inossem.wms.dao.biz.BizAllocateCargoItemMapper;
import com.inossem.wms.dao.biz.BizAllocateCargoPositionMapper;
import com.inossem.wms.dao.biz.BizStockOutputHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqItemMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicDeliveryDriverMapper;
import com.inossem.wms.dao.dic.DicDeliveryVehicleMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicMaterialMapper;
import com.inossem.wms.dao.dic.DicProductLineMapper;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.dic.DicUnitMapper;
import com.inossem.wms.dao.stock.StockOccupancyMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.biz.BizAllocateCargoHead;
import com.inossem.wms.model.biz.BizAllocateCargoItem;
import com.inossem.wms.model.biz.BizAllocateCargoPosition;
import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.enums.EnumAllocateCargoOperationType;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.enums.EnumOperatorTypes;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumStockOutputReturnStatus;
import com.inossem.wms.model.enums.EnumTaskReqShippingType;
import com.inossem.wms.model.sap.SapDeliveryOrderHead;
import com.inossem.wms.model.sap.SapDeliveryOrderItem;
import com.inossem.wms.model.stock.StockOccupancy;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.model.vo.RelUserStockLocationVo;
import com.inossem.wms.service.biz.DistributionService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.ITaskService;
import com.inossem.wms.service.intfc.IStockAllocateCargo;
import com.inossem.wms.util.UtilObject;

import net.sf.json.JSONObject;
@Transactional
@Service("distributionService")
public class DistributionServiceImpl implements DistributionService {
	@Autowired
	private BizAllocateCargoHeadMapper bizAllocateCargoHeadMapper;
	@Autowired
	private BizAllocateCargoItemMapper bizAllocateCargoItemMapper;
	@Autowired
	private BizAllocateCargoPositionMapper bizAllocateCargoPositionMapper;
	@Autowired
	private SequenceDAO sequenceDAO;
	@Autowired
	private IStockAllocateCargo StockAllocateCargo;
	@Autowired
	private IDictionaryService dictionaryService;
	@Autowired
	private BizStockTaskReqHeadMapper stockTaskReqHeadMapper;
	@Autowired
	private BizStockTaskReqItemMapper stockTaskReqItemDao;
    @Autowired
    private DicStockLocationMapper stockLocationDao;
    @Autowired
    private DicMaterialMapper  dicMaterialDao;
    
    @Autowired
	private DicUnitMapper unitDao;
    @Autowired
    private DicFactoryMapper dicFactoryMapper;
    @Autowired
    private StockOccupancyMapper stockOccupancyMapper;
    
    @Autowired
    private ICommonService commonService;
    
    @Autowired 
    private  BizStockOutputHeadMapper bizStockOutputHeadMapper;
    
    @Autowired 
    private  DicDeliveryVehicleMapper dicDeliveryVehicleMapper;
    
    @Autowired 
    private  DicDeliveryDriverMapper dicDeliveryDriverMapper;
    
    @Autowired
    private   DicProductLineMapper dicProductLineMapper;
   
    @Autowired
   private   ITaskService taskService;
	@Override
	public List<Map<String, Object>> selectCargo(Map<String, Object> map) throws Exception{
		return bizAllocateCargoHeadMapper.selectCargoonpaging(map);
	}
    /**
     * 查询详细
     */
	@Override
	public Map<String, Object> selectCargoItem(Integer allocate_cargo_id) {
		// List<Map<String, Object>> headList=
		// bizAllocateCargoItemMapper.selectCargoItem1(allocate_cargo_id);
		// String referReceiptCode =(String) headList.get(0).get("refer_receipt_code");
		// List<Map<String,Object>> itemList =(List<Map<String, Object>>)
		// headList.get(0).get("item_list");
		// for (int i = 0; i < itemList.size(); i++) {
		// String matId = (String) itemList.get(i).get("mat_id");
		// Map<String,String> condition=new HashMap<String,String>();
		// condition.put("referReceiptCode", referReceiptCode);
		// condition.put("matId", matId);
		// int cargoCount=bizAllocateCargoItemMapper.getCountCargoed(condition);
		// itemList.get(i).put("output_qtyed", cargoCount+"");
		//
		// }
		Map<String, Object> bizAllocateCargoHead = bizAllocateCargoHeadMapper.getCargoHeadById(allocate_cargo_id);
		//交货单
		String referReceiptCode = (String) bizAllocateCargoHead.get("refer_receipt_code");
		//行项目
		List<Map<String, Object>> bizAllocateCargoItemList = bizAllocateCargoItemMapper
				.selectByCargoId(allocate_cargo_id);
	    
		for (int i = 0; i < bizAllocateCargoItemList.size(); i++) {
			Object allocate_cargo_rid = bizAllocateCargoItemList.get(i).get("allocate_cargo_rid");
			Object ftyId=bizAllocateCargoItemList.get(i).get("fty_id");
			Object matId = bizAllocateCargoItemList.get(i).get("mat_id");
			Object erpBatch = bizAllocateCargoItemList.get(i).get("erp_batch");
			Object locationId = bizAllocateCargoItemList.get(i).get("location_id");
			List<RelUserStockLocationVo> relUserStockLocationVoItem=  stockLocationDao.selectByFtyId(Integer.parseInt(ftyId.toString()));
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("referReceiptCode", referReceiptCode);
			condition.put("referReceiptRid", bizAllocateCargoItemList.get(i).get("refer_receipt_rid"));
//			condition.put("matId", matId);
//			condition.put("erpBatch", erpBatch);
//			condition.put("locationId", locationId);			
  			
		   Map<String,Object> result =	bizAllocateCargoItemMapper.getCountCargoed(condition);		    
		
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("allocate_cargo_id", allocate_cargo_id);
			parameter.put("allocate_cargo_rid", allocate_cargo_rid);
			
			//position数据
			List<Map<String, String>> bizAllocateCargoPositionList = bizAllocateCargoPositionMapper
					.selectPositionBySuperId(parameter);
			bizAllocateCargoItemList.get(i).put("position_list", bizAllocateCargoPositionList);
			if(result==null) {
				bizAllocateCargoItemList.get(i).put("output_qtyed", 0);
			}else {				
				bizAllocateCargoItemList.get(i).put("output_qtyed", result.get("output_qtyed"));
			}
			
			bizAllocateCargoItemList.get(i).put("location_list",relUserStockLocationVoItem);
						
			List<Map<String,Object>> erpList=commonService.getErpBatchList(UtilObject.getIntegerOrNull(matId), UtilObject.getIntegerOrNull(ftyId));
			bizAllocateCargoItemList.get(i).put("erp_list", erpList);
		}
		bizAllocateCargoHead.put("item_list", bizAllocateCargoItemList);
		return bizAllocateCargoHead;
	}

	@Override
	public List<Map<String, String>> selectByCondition(Map<String, String> map) {

		return bizAllocateCargoPositionMapper.selectByCondition(map);
	}

	@Override
	public int saveCargo(JSONObject json, User cUser) throws Exception {
        Integer locationId=-1;
        Integer locationIdNew=-1;
        int rId=1;
		BizAllocateCargoItem bizAllocateCargoItem = null;
		BizStockTaskReqHead bizStockTaskReqHead=new BizStockTaskReqHead();
		boolean isAdd = false; // 新增or修改
		List<BizAllocateCargoPosition> listBizAllocateCargoPosition = new ArrayList<BizAllocateCargoPosition>();
		List<BizAllocateCargoItem> listBizAllocateCargoItem = new ArrayList<BizAllocateCargoItem>();
//		Map<String, Object> map = new HashMap<String, Object>();
//		List<Object> packageTypeIdList = new ArrayList<Object>();
//		List<Object> erpBatchList = new ArrayList<Object>();
//		List<Object> productionBatchList = new ArrayList<Object>();
//		List<Object> qualityBatchList = new ArrayList<Object>();

		/******************************************************
		 * HEAD信息处理
		 ******************************************************/
		Map<String, Object> headData = (Map<String, Object>) json;

		BizAllocateCargoHead bizAllocateCargoHead = null;
		int allocateCargoId = 0;

		if (headData.containsKey("allocate_cargo_id")) {
			allocateCargoId = UtilObject.getIntOrZero(headData.get("allocate_cargo_id"));
		}
		// 根据id是否存在 判断新增还是修改
		if (allocateCargoId == 0) {
			isAdd = true;
		} else {
			bizAllocateCargoHead = bizAllocateCargoHeadMapper.selectByPrimaryKey(allocateCargoId);
			if (null == bizAllocateCargoHead || "".equals(bizAllocateCargoHead.getAllocateCargoCode())
					|| bizAllocateCargoHead.getIsDelete() == 1) {
				isAdd = true;
			}
		}
		// 如果不是新增，则根据配货单id删除item和position
		if (!isAdd) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("moditiUser", cUser.getUserId());
			paramMap.put("allocateCargoId", allocateCargoId);
			bizAllocateCargoItemMapper.updateByAllocateCargoId(paramMap);
			bizAllocateCargoPositionMapper.updateByAllocateCargoId(paramMap);
		}
		// 保存或修改配货单并返回配货单对象
		bizAllocateCargoHead = this.saveHeadData(headData, isAdd, bizAllocateCargoHead, cUser);
		int allocateCargoIdr = bizAllocateCargoHead.getAllocateCargoId();
		/******************************************************
		 * ITEM信息处理
		 ******************************************************/
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> itemList = (List<Map<String, Object>>) headData.get("item_list"); // 出库单明细list
		if (itemList != null) {
			for (int i = 0; i < itemList.size(); i++) {
				Map<String, Object> itemDataTemp = itemList.get(i);
				bizAllocateCargoItem = this.saveItemData(itemDataTemp, bizAllocateCargoHead, cUser);
				listBizAllocateCargoItem.add(bizAllocateCargoItem);
				/******************************************************
				 * position信息处理
				 ******************************************************/
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> positionList = (List<Map<String, Object>>) itemList.get(i)
						.get("position_list"); // 配货单明细list

				if (positionList != null) {
					for (int j = 0; j < positionList.size(); j++) {
						Map<String, Object> positionDataTemp = positionList.get(j);
						if (UtilObject.getBigDecimalOrZero(positionDataTemp.get("cargo_qty"))
								.compareTo(BigDecimal.ZERO) == 1) {
							Byte stockTypeId=UtilObject.getByteOrNull(positionDataTemp.get("stock_type_id"));
							BizAllocateCargoPosition bizAllocateCargoPosition = this.savePositionData(positionDataTemp,
									bizAllocateCargoHead, bizAllocateCargoItem, cUser);
							saveStockOccupancy( bizAllocateCargoPosition, bizAllocateCargoItem, stockTypeId);
							listBizAllocateCargoPosition.add(bizAllocateCargoPosition);
							
//							packageTypeIdList.add(positionDataTemp.get("package_type_id"));
//							erpBatchList.add(positionDataTemp.get("erp_batch"));
//							productionBatchList.add(positionDataTemp.get("production_batch"));
//							qualityBatchList.add(positionDataTemp.get("quality_batch"));
							Map<String,Object> paramMap=new HashMap<String,Object>();
							paramMap.put("package_type_id", positionDataTemp.get("package_type_id"));
							paramMap.put("erp_batch", positionDataTemp.get("erp_batch"));
							paramMap.put("production_batch", positionDataTemp.get("product_batch"));
							paramMap.put("quality_batch", positionDataTemp.get("quality_batch")); 
							paramMap.put("refer_receipt_rid",bizAllocateCargoItem.getReferReceiptRid());
							//在item的信息中得到location_id
						   locationId=	UtilObject.getIntegerOrNull(itemDataTemp.get("location_id"));						   
						   //同一张配货单配不同库存地点生成对应库存地点个下架请求    每次比较locationId和locationIdNew  如果值相等，则是同一库存地点
						   if(locationId!=locationIdNew) {
							  bizStockTaskReqHead=	saveTaskReqHead(bizAllocateCargoHead,bizAllocateCargoItem,bizAllocateCargoPosition,paramMap, cUser);								
							  rId=1;
						  }							  
						  paramMap.put("stock_task_req_rid",rId );
						  rId++;
						  locationIdNew=locationId;
					      saveTaskReq(bizAllocateCargoHead,bizAllocateCargoItem,bizAllocateCargoPosition,bizStockTaskReqHead,paramMap, cUser);
						
						}
					}
				}
			}
		}
//		map.put("packageTypeIdList", packageTypeIdList);
//		map.put("erpBatchList", erpBatchList);
//		map.put("productionBatchList", productionBatchList);
//		map.put("qualityBatchList", qualityBatchList);
	//	this.saveTaskReq(bizAllocateCargoHead, listBizAllocateCargoItem, listBizAllocateCargoPosition, map, cUser);
		return allocateCargoIdr;
	}
	
	private boolean saveStockOccupancy(BizAllocateCargoPosition bizAllocateCargoPosition,BizAllocateCargoItem bizAllocateCargoItem,Byte stockTypeId) {
		StockOccupancy stockOccupancy=new StockOccupancy();
		stockOccupancy.setBatch(bizAllocateCargoPosition.getBatch());
		stockOccupancy.setFtyId(bizAllocateCargoItem.getFtyId());
		stockOccupancy.setLocationId(bizAllocateCargoPosition.getLocationId());
		stockOccupancy.setMatId(bizAllocateCargoPosition.getMatId());
		stockOccupancy.setPackageTypeId(bizAllocateCargoPosition.getPackageTypeId());
		stockOccupancy.setQty(bizAllocateCargoPosition.getCargoQty());
		stockOccupancy.setStockTypeId(stockTypeId);
		//stockOccupancyMapper.insertSelective(stockOccupancy);
		commonService.modifyStockOccupancy(stockOccupancy, "S");
		return true;
	}
	
	private BizStockTaskReqHead saveTaskReqHead(BizAllocateCargoHead bizAllocateCargoHead,BizAllocateCargoItem bizAllocateCargoItem,BizAllocateCargoPosition bizAllocateCargoPosition,Map<String,Object> paramMap,User user) {
		Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
		DicStockLocationVo locationObj = locationMap.get(bizAllocateCargoItem.getLocationId());		
		BizStockTaskReqHead bizStockTaskReqHead = new BizStockTaskReqHead();
		bizStockTaskReqHead.setCreateTime(new Date());
		bizStockTaskReqHead.setCreateUser(user.getUserId());
		bizStockTaskReqHead.setFtyId(bizAllocateCargoItem.getFtyId());
		bizStockTaskReqHead.setIsDelete(EnumBoolean.FALSE.getValue());
		bizStockTaskReqHead.setLocationId(bizAllocateCargoItem.getLocationId());
		bizStockTaskReqHead.setMatDocId(0);
		bizStockTaskReqHead.setMatDocYear(0);
		bizStockTaskReqHead.setModifyTime(new Date());
		bizStockTaskReqHead.setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue());
		bizStockTaskReqHead.setReferReceiptCode(bizAllocateCargoHead.getAllocateCargoCode());
		bizStockTaskReqHead.setReferReceiptId(bizAllocateCargoHead.getAllocateCargoId());
		bizStockTaskReqHead.setReferReceiptType(EnumReceiptType.STOCK_OUTPUT_ALLOCATE_CARGO.getValue());
		bizStockTaskReqHead.setShippingType(EnumTaskReqShippingType.STOCK_OUTPUT.getValue());
		bizStockTaskReqHead.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue()); 
		bizStockTaskReqHead.setStockTaskReqCode(sequenceDAO.selectNextVal("stock_task_req") + "");
		bizStockTaskReqHead.setWhId(locationObj.getWhId());
		
		bizStockTaskReqHead.setDeliveryDriver(bizAllocateCargoHead.getDeliveryDriver());		
		bizStockTaskReqHead.setDeliveryVehicle(bizAllocateCargoHead.getDeliveryVehicle());
		bizStockTaskReqHead.setDeliveryDriverId(bizAllocateCargoHead.getDeliveryDriverId());
		bizStockTaskReqHead.setDeliveryVehicleId(bizAllocateCargoHead.getDeliveryVehicleId());
		
		bizStockTaskReqHead.setRemark(bizAllocateCargoHead.getRemark());
		stockTaskReqHeadMapper.insertSelective(bizStockTaskReqHead);
		return bizStockTaskReqHead;
	}

	private boolean saveTaskReq(BizAllocateCargoHead bizAllocateCargoHead,
			BizAllocateCargoItem bizAllocateCargoItem,
			BizAllocateCargoPosition bizAllocateCargoPosition,BizStockTaskReqHead bizStockTaskReqHead,Map<String,Object> map, User user) {
	
//		List<Object>  packageTypeIdList= (List<Object>) map.get("packageTypeIdList");
//		List<Object>  erpBatchList= (List<Object>) map.get("erpBatchList");
//		List<Object>  productionBatchList= (List<Object>) map.get("productionBatchList");
//		List<Object>  qualityBatchList=(List<Object>) map.get("qualityBatchList");		
////		Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
//		DicStockLocationVo locationObj = locationMap.get(bizAllocateCargoItemList.get(0).getLocationId());		
//		BizStockTaskReqHead bizStockTaskReqHead = new BizStockTaskReqHead();
//		bizStockTaskReqHead.setCreateTime(new Date());
//		bizStockTaskReqHead.setCreateUser(user.getUserId());
//		bizStockTaskReqHead.setFtyId(bizAllocateCargoItemList.get(0).getFtyId());
//		bizStockTaskReqHead.setIsDelete(EnumBoolean.FALSE.getValue());
//		bizStockTaskReqHead.setLocationId(bizAllocateCargoItemList.get(0).getLocationId());
//		bizStockTaskReqHead.setMatDocId(0);
//		bizStockTaskReqHead.setMatDocYear(0);
//		bizStockTaskReqHead.setModifyTime(new Date());
//		bizStockTaskReqHead.setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue());
//		bizStockTaskReqHead.setReferReceiptCode(bizAllocateCargoHead.getAllocateCargoCode());
//		bizStockTaskReqHead.setReferReceiptId(bizAllocateCargoHead.getAllocateCargoId());
//		bizStockTaskReqHead.setReferReceiptType(bizAllocateCargoHead.getDocumentType());
//		bizStockTaskReqHead.setShippingType(EnumTaskReqShippingType.STOCK_OUTPUT.getValue());
//		bizStockTaskReqHead.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
//		bizStockTaskReqHead.setStockTaskReqCode(sequenceDAO.selectNextVal("stock_task") + "");
//		bizStockTaskReqHead.setWhId(locationObj.getWhId());
//		//bizStockTaskReqHead.setWhId(0);
//		stockTaskReqHeadMapper.insertSelective(bizStockTaskReqHead);
		// 配货的head对应下架请求的head 配货的position对应下架请求的item 配货单只在一个库存地点出库
//		for (int i = 0; i < bizAllocateCargoPositionList.size(); i++) {
			BizStockTaskReqItem bizStockTaskReqItem = new BizStockTaskReqItem();
			bizStockTaskReqItem.setBatch(bizAllocateCargoPosition.getBatch());
			bizStockTaskReqItem.setCreateTime(new Date());
			bizStockTaskReqItem.setFtyId(bizAllocateCargoItem.getFtyId());
			bizStockTaskReqItem.setIsDelete(EnumBoolean.FALSE.getValue());
			bizStockTaskReqItem.setLocationId(bizAllocateCargoItem.getLocationId());
			bizStockTaskReqItem.setMatId(bizAllocateCargoItem.getMatId());
			bizStockTaskReqItem.setModifyTime(new Date());
			bizStockTaskReqItem.setQty(bizAllocateCargoPosition.getCargoQty());
			bizStockTaskReqItem.setStatus(EnumBoolean.FALSE.getValue());
			bizStockTaskReqItem.setStockTaskQty(new BigDecimal(0));
			bizStockTaskReqItem.setStockTaskReqId(bizStockTaskReqHead.getStockTaskReqId());
			bizStockTaskReqItem.setStockTaskReqRid(UtilObject.getIntegerOrNull(map.get("stock_task_req_rid")));
			bizStockTaskReqItem.setUnitId(bizAllocateCargoItem.getUnitId());
			bizStockTaskReqItem.setWhId(bizStockTaskReqHead.getWhId());
			bizStockTaskReqItem.setPackageTypeId(UtilObject.getIntegerOrNull(map.get("package_type_id")));
			bizStockTaskReqItem.setProductionBatch(UtilObject.getStringOrEmpty(map.get("production_batch")));
			bizStockTaskReqItem.setErpBatch(UtilObject.getStringOrEmpty(map.get("erp_batch")));
			bizStockTaskReqItem.setQualityBatch(UtilObject.getStringOrEmpty(map.get("quality_batch")));
			bizStockTaskReqItem.setReferReceiptId(bizStockTaskReqHead.getReferReceiptId());
			bizStockTaskReqItem.setReferReceiptCode(bizStockTaskReqHead.getReferReceiptCode());
			bizStockTaskReqItem.setReferReceiptRid(bizAllocateCargoItem.getAllocateCargoRid()); 
			bizStockTaskReqItem.setReferReceiptType(bizStockTaskReqHead.getReferReceiptType());
			bizStockTaskReqItem.setRemark(bizAllocateCargoItem.getRemark());			
			stockTaskReqItemDao.insertSelective(bizStockTaskReqItem);
//		}
		return false;

	}

	/**
	 * 保存head信息
	 */
	private BizAllocateCargoHead saveHeadData(Map<String, Object> headDataTemp, boolean isAdd,
			BizAllocateCargoHead bizAllocateCargoHead, User user) {
		if (isAdd) {
			bizAllocateCargoHead = new BizAllocateCargoHead();
			bizAllocateCargoHead.setAllocateCargoCode(sequenceDAO.selectNextVal("allocate_cargon") + "");
		} else {
			bizAllocateCargoHead.setAllocateCargoId(UtilObject.getIntOrZero(headDataTemp.get("allocate_cargo_id")));
			bizAllocateCargoHead
					.setAllocateCargoCode(UtilObject.getStringOrEmpty(headDataTemp.get("allocate_cargo_code")));
		}
		bizAllocateCargoHead.setStatus(UtilObject.getByteOrNull(headDataTemp.get("status")));
		bizAllocateCargoHead.setReceiveCode(UtilObject.getStringOrEmpty(headDataTemp.get("refer_receipt_code")));
		bizAllocateCargoHead.setSaleOrderCode(UtilObject.getStringOrEmpty(headDataTemp.get("sale_order_code")));
		bizAllocateCargoHead.setPreorderId(UtilObject.getStringOrEmpty(headDataTemp.get("preorder_id")));
		bizAllocateCargoHead.setReceiveCode(UtilObject.getStringOrEmpty(headDataTemp.get("receive_code")));
		bizAllocateCargoHead.setReceiveName(UtilObject.getStringOrEmpty(headDataTemp.get("receive_name")));
		//司机车辆存id
		bizAllocateCargoHead.setDeliveryVehicle(UtilObject.getStringOrEmpty(headDataTemp.get("delivery_vehicle")));
		bizAllocateCargoHead.setDeliveryDriver(UtilObject.getStringOrEmpty(headDataTemp.get("delivery_driver")));
		//bizAllocateCargoHead.setDeliveryDriverId(UtilObject.getIntOrZero(headDataTemp.get("delivery_driver_id")));
	//	bizAllocateCargoHead.setDeliveryVehicleId(UtilObject.getIntOrZero(headDataTemp.get("delivery_vehicle_id")));
		
		bizAllocateCargoHead.setClassTypeId(UtilObject.getIntOrZero(headDataTemp.get("class_type_id")));
		bizAllocateCargoHead.setRemark(UtilObject.getStringOrEmpty(headDataTemp.get("remark")));
		bizAllocateCargoHead.setErpRemark(UtilObject.getStringOrEmpty(headDataTemp.get("erp_remark")));
		bizAllocateCargoHead.setRequestSource(EnumBoolean.FALSE.getValue());
		bizAllocateCargoHead.setIsDelete(EnumBoolean.FALSE.getValue());
		bizAllocateCargoHead.setCreateTime(new Date());
		bizAllocateCargoHead.setModifyTime(new Date());
		bizAllocateCargoHead.setReferReceiptCode(UtilObject.getStringOrEmpty(headDataTemp.get("refer_receipt_code")));
		bizAllocateCargoHead.setStatus(EnumStockOutputReturnStatus.STOCK_OUTPUT_RETURN_SUBMIT.getValue());
		bizAllocateCargoHead.setCreateUser(user.getUserId());
		bizAllocateCargoHead.setModifyUser(user.getUserId());
		bizAllocateCargoHead.setOrderType(UtilObject.getStringOrEmpty(headDataTemp.get("order_type")));
		bizAllocateCargoHead.setOrderTypeName(UtilObject.getStringOrEmpty(headDataTemp.get("order_type_name")));
		bizAllocateCargoHead.setDocumentType(EnumReceiptType.STOCK_OUTPUT_ALLOCATE_CARGO.getValue());
		bizAllocateCargoHead.setOperationType(UtilObject.getByteOrNull(headDataTemp.get("operation_type")));
		if (isAdd) {

			bizAllocateCargoHeadMapper.insertSelective(bizAllocateCargoHead);

		} else {
			bizAllocateCargoHeadMapper.updateByPrimaryKeySelective(bizAllocateCargoHead);
		}
		return bizAllocateCargoHead;
	}

	/**
	 * 保存item信息
	 */

	private BizAllocateCargoItem saveItemData(Map<String, Object> itemDataTemp,
			BizAllocateCargoHead bizAllocateCargoHead, User user) {

		BizAllocateCargoItem bizAllocateCargoItem = new BizAllocateCargoItem();
		bizAllocateCargoItem.setAllocateCargoId(bizAllocateCargoHead.getAllocateCargoId());
		bizAllocateCargoItem.setAllocateCargoRid(UtilObject.getIntOrZero(itemDataTemp.get("allocate_cargo_rid")));
		bizAllocateCargoItem.setFtyId(UtilObject.getIntOrZero(itemDataTemp.get("fty_id")));
		bizAllocateCargoItem.setLocationId(UtilObject.getIntOrZero(itemDataTemp.get("location_id")));
		bizAllocateCargoItem.setMatId(UtilObject.getIntOrZero(itemDataTemp.get("mat_id")));
		bizAllocateCargoItem.setUnitId(UtilObject.getIntOrZero(itemDataTemp.get("unit_id")));
		bizAllocateCargoItem.setDecimalPlace(UtilObject.getByteOrNull(itemDataTemp.get("decimal_place")));
		bizAllocateCargoItem.setOrderQty(UtilObject.getBigDecimalOrZero(itemDataTemp.get("order_qty")));
		bizAllocateCargoItem.setOutputQty(UtilObject.getBigDecimalOrZero(itemDataTemp.get("output_qty")));
		bizAllocateCargoItem.setReferReceiptRid(UtilObject.getStringOrEmpty(itemDataTemp.get("refer_receipt_rid")));
		bizAllocateCargoItem.setRemark(UtilObject.getStringOrEmpty(itemDataTemp.get("item_remark")));
		bizAllocateCargoItem.setErpRemark(UtilObject.getStringOrEmpty(itemDataTemp.get("erp_remark")));
		bizAllocateCargoItem.setIsDelete(EnumBoolean.FALSE.getValue());
		bizAllocateCargoItem.setCreateTime(new Date());
		bizAllocateCargoItem.setModifyTime(new Date());
		bizAllocateCargoItem.setCreateUser(user.getUserId());
		bizAllocateCargoItem.setModifyUser(user.getUserId());
		bizAllocateCargoItem.setDecimalPlace(UtilObject.getByteOrNull(itemDataTemp.get("decimal_place")));
		bizAllocateCargoItem.setErpBatch(UtilObject.getStringOrEmpty(itemDataTemp.get("erp_batch")));
		bizAllocateCargoItemMapper.insertSelective(bizAllocateCargoItem);

		return bizAllocateCargoItem;
	}

	/**
	 * 保存position信息
	 */
	private BizAllocateCargoPosition savePositionData(Map<String, Object> postionData,
			BizAllocateCargoHead bizAllocateCargoHead, BizAllocateCargoItem allocateCargoItem, User user)
			throws Exception {
		BizAllocateCargoPosition bizAllocateCargoPosition = new BizAllocateCargoPosition();
		bizAllocateCargoPosition.setAllocateCargoId(bizAllocateCargoHead.getAllocateCargoId());
		bizAllocateCargoPosition.setAllocateCargoRid(allocateCargoItem.getAllocateCargoRid());
		bizAllocateCargoPosition
				.setAllocateCargoPositionId(UtilObject.getIntOrZero(postionData.get("allocate_cargo_rid")));
		bizAllocateCargoPosition.setStockId(UtilObject.getIntOrZero(postionData.get("stock_id")));
		bizAllocateCargoPosition.setBatch(UtilObject.getLongOrNull(postionData.get("batch")));
		bizAllocateCargoPosition.setCargoQty(UtilObject.getBigDecimalOrZero(postionData.get("cargo_qty")));
		bizAllocateCargoPosition.setRemark(UtilObject.getStringOrEmpty(postionData.get("remark")));
		bizAllocateCargoPosition.setIsDelete(EnumBoolean.FALSE.getValue());
		bizAllocateCargoPosition.setCreateTime(new Date());
		bizAllocateCargoPosition.setModifyTime(new Date());
		bizAllocateCargoPosition.setCreateUser(user.getUserId());
		bizAllocateCargoPosition.setModifyUser(user.getUserId());
		bizAllocateCargoPosition.setMatId(UtilObject.getIntOrZero(postionData.get("mat_id")));
		bizAllocateCargoPosition.setLocationId(UtilObject.getIntOrZero(postionData.get("location_id")));
		bizAllocateCargoPosition.setPackageTypeId(UtilObject.getIntOrZero(postionData.get("package_type_id")));
		bizAllocateCargoPosition.setStockQty(UtilObject.getBigDecimalOrZero(postionData.get("stock_qty")));
		bizAllocateCargoPosition.setInputDate(UtilObject.getStringOrEmpty(postionData.get("input_date")));
		bizAllocateCargoPosition.setStockTypeId(UtilObject.getIntOrZero(postionData.get("stock_type_id")));
		bizAllocateCargoPosition.setProductBatch(UtilObject.getStringOrEmpty(postionData.get("product_batch")));
		
		bizAllocateCargoPositionMapper.insertSelective(bizAllocateCargoPosition);
		return bizAllocateCargoPosition;
	
	}

	@Override
	// 根据交货单查询配货单信息 从sap
	public Map<String, Object> getSaleMasseg(SapDeliveryOrderHead sapDeliveryOrderHead, CurrentUser user) throws Exception {
		
		DicFactory dicFactory=null;
		Map<String, Object> headmap = new HashMap<String,Object>();
				//StockAllocateCargo.getSalesViewBySap(refer_receipt_code, user);
	   List<SapDeliveryOrderItem> itemList=sapDeliveryOrderHead.getItemList();
	   String referReceiptCode=  sapDeliveryOrderHead.getDeliveryOrderCode();
	   List<Map<String,Object>> resultItemList=new ArrayList<Map<String,Object>>();	   
	   
	   headmap.put("sale_order_code", sapDeliveryOrderHead.getSaleOrderCode());//销售订单号
	   headmap.put("refer_receipt_code", sapDeliveryOrderHead.getDeliveryOrderCode());// 前置单据号 交货单号
	   headmap.put("preorder_id", sapDeliveryOrderHead.getContractNumber());//合同编号
	   headmap.put("receive_code", sapDeliveryOrderHead.getReceiveCode());//客户编号
	   headmap.put("receive_name", sapDeliveryOrderHead.getReceiveName());//客户名称
	   headmap.put("order_type", sapDeliveryOrderHead.getOrderType());//订单类型
	   headmap.put("order_type_name", sapDeliveryOrderHead.getOrderTypeName());//订单类型名称
	   headmap.put("remark", "");//备注
	   List<Map<String,Object>> outList = bizAllocateCargoHeadMapper.checkOrderCode(sapDeliveryOrderHead.getDeliveryOrderCode(), EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue());
	   if(outList!=null && !outList.isEmpty()) {
		   headmap.put("operation_type", EnumAllocateCargoOperationType.NO_MENTION.getValue());
		   headmap.put("operation_type_name", EnumAllocateCargoOperationType.NO_MENTION.getName());
	   }else {
		   headmap.put("operation_type", EnumAllocateCargoOperationType.NORMAL.getValue());
		   headmap.put("operation_type_name", EnumAllocateCargoOperationType.NORMAL.getName());
	   }
	   //根据用户查询生产线
	   Map<String,Object> mapProduct=   dicProductLineMapper.getRelUserProductLine(user.getUserId());
	   //添加车辆list	   
	   headmap.put("delivery_vehicle_list", dicDeliveryVehicleMapper.selectVehicleByProductId(UtilObject.getIntegerOrNull(mapProduct.get("product_line_id"))));
	   
	  //添加司机list
	   headmap.put("delivery_driver_list", dicDeliveryDriverMapper.selectDriverByProductlineId(UtilObject.getIntegerOrNull(mapProduct.get("product_line_id"))));
	   
	   //遍历sap得行项目
	   for (SapDeliveryOrderItem sapDeliveryOrderItem : itemList) {
		  Map<String,Object> map = new HashMap<String,Object>();
		  Map<String,Object> mat = dicMaterialDao.getMatUnitByMatCode(sapDeliveryOrderItem.getMatCode());
		  if(sapDeliveryOrderItem.getErpRemark()==null) {
			  map.put("erp_remark",""); 
		  }else {			  
			  map.put("erp_remark",sapDeliveryOrderItem.getErpRemark());	  
		  }
		  map.put("mat_id",mat.get("mat_id"));
		  map.put("mat_code", mat.get("mat_code"));
		  map.put("mat_name",  mat.get("mat_name"));
		//  DicUnit unit = unitDao.getByCode(sapDeliveryOrderItem.getUnitCode());
		  map.put("unit_id", mat.get("unit_id"));
		  map.put("unit_code",  mat.get("unit_code"));
		  map.put("name_zh",  mat.get("name_zh"));
		  map.put("erp_batch", sapDeliveryOrderItem.getErpBatch()==null? "":sapDeliveryOrderItem.getErpBatch());	
		  //交货单创建配货单时，当交货单类型是ZR10，配货库存查询工厂固定为SWX1
		  if("ZR10".equals(headmap.get("order_type"))) {
			  dicFactory=  dicFactoryMapper.selectByCode("SWX1");			  
		  }else {
			  dicFactory=  dicFactoryMapper.selectByCode(sapDeliveryOrderItem.getFtyCode());			  
		  }
		  RelUserStockLocationVo location = stockLocationDao.selectByFtyCodeAndLocationCode(dicFactory.getFtyCode(), sapDeliveryOrderItem.getLocationCode());
	      List<RelUserStockLocationVo> relUserStockLocationVoItem=  stockLocationDao.selectByFtyId(dicFactory.getFtyId());
			map.put("fty_id", dicFactory.getFtyId());
			map.put("fty_code", dicFactory.getFtyCode());
			map.put("fty_name", dicFactory.getFtyName());
			List<Map<String,Object>> erpList=commonService.getErpBatchList(UtilObject.getIntegerOrNull(mat.get("mat_id")), dicFactory.getFtyId());
			map.put("erp_list", erpList);
			map.put("refer_receipt_rid", sapDeliveryOrderItem.getDeliveryOrderRid());
			if(location!=null) {
			map.put("location_id", location.getLocationId());
			}
			map.put("location_code", sapDeliveryOrderItem.getLocationCode());
			//map.put("location_name", location.getLocationName());
			map.put("order_qty",sapDeliveryOrderItem.getQty());
			//map.put("output_qty", "0");
			//把库存id和code放进配货行项目			
			map.put("location_list", relUserStockLocationVoItem);		
//			Object matId = map.get("mat_id");
//			Object erpBatch = map.get("erp_batch");
//			Object locationId = map.get("location_id");
			//查询条件
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("referReceiptCode", referReceiptCode);
			condition.put("referReceiptRid",sapDeliveryOrderItem.getDeliveryOrderRid());
//			condition.put("matId", matId);
//			condition.put("erpBatch", erpBatch);
//			condition.put("locationId", locationId);  referReceiptRid
	
			//获得已配货数量和erp_batch
			Map<String,Object> mapResult=bizAllocateCargoItemMapper.getCountCargoed(condition);
			
			//如果sap有erp_batch 就用sap得  没有就用已经配过得单子得erp批次
			if(mapResult!=null) {
				if(map.get("erp_batch")==null||"".equals(map.get("erp_batch"))) {			
					map.putAll(mapResult);
				}else {
					map.put("output_qtyed", mapResult.get("output_qtyed"));
				}
			}else {
				map.put("output_qtyed", "");
			}
			resultItemList.add(map);		
	   }
	   headmap.put("item_list", resultItemList);
	   return headmap;
	}
	
	public Integer getCountBySaleOrder(String refer_receipt_code) {
		  return bizStockOutputHeadMapper.getCountBySaleOrder(refer_receipt_code);
	}
	
	@Override
	public boolean deleteCargo(Integer allocate_cargo_id) throws Exception {
	
		//Integer referReceiptId, Byte referReceiptType   这里已经判断否可以删除
		taskService.deleteTaskReqAndTaskByReferIdAndType(allocate_cargo_id,EnumReceiptType.STOCK_OUTPUT_ALLOCATE_CARGO.getValue());
	    Map<String,Object> map=	selectCargoItem(allocate_cargo_id);		
	    List<Map<String, Object>> itemList = (List<Map<String, Object>>)map.get("item_list");
	  	for (int i = 0; i < itemList.size(); i++) {
	  		List<Map<String, Object>> positionList=	(List<Map<String, Object>>)itemList.get(i).get("position_list");
	  		for (int j = 0; j < positionList.size(); j++) {	  			
	  			StockOccupancy stockOccupancy=new StockOccupancy();
	  			stockOccupancy.setBatch(UtilObject.getLongOrNull(positionList.get(j).get("batch")));
	  			stockOccupancy.setFtyId(UtilObject.getIntegerOrNull(positionList.get(j).get("fty_id")));
	  			stockOccupancy.setLocationId(UtilObject.getIntegerOrNull(positionList.get(j).get("location_id")));
	  			stockOccupancy.setMatId(UtilObject.getIntegerOrNull(positionList.get(j).get("mat_id")));
	  			stockOccupancy.setPackageTypeId(UtilObject.getIntegerOrNull(positionList.get(j).get("package_type_id")));
	  			stockOccupancy.setQty(UtilObject.getBigDecimalOrZero(positionList.get(j).get("cargo_qty")));
	  			stockOccupancy.setStockTypeId(UtilObject.getByteOrNull(positionList.get(j).get("stock_type_id")));
	  			//stockOccupancyMapper.insertSelective(stockOccupancy);
	  			commonService.modifyStockOccupancy(stockOccupancy, "H");			
			}  		
	  		
		}	
		bizAllocateCargoHeadMapper.deleteAllocateHeadByHeadId(allocate_cargo_id);
		bizAllocateCargoItemMapper.deleteAllocateItemByHeadId(allocate_cargo_id);
		bizAllocateCargoPositionMapper.deleteAllocatePositionByHeadId(allocate_cargo_id);
		return true;
	}
	
	

}
