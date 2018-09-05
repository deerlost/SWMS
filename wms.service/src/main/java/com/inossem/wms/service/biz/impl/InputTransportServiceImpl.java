package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.biz.BizBusinessHistoryMapper;
import com.inossem.wms.dao.biz.BizFailMesMapper;
import com.inossem.wms.dao.biz.BizInputProduceHeadMapper;
import com.inossem.wms.dao.biz.BizStockInputHeadMapper;
import com.inossem.wms.dao.biz.BizStockInputTransportHeadMapper;
import com.inossem.wms.dao.biz.BizStockInputTransportItemMapper;
import com.inossem.wms.dao.biz.BizStockTaskHeadCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskItemCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskPositionCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqItemMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqPositionMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicClassTypeMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicInstallationMapper;
import com.inossem.wms.dao.dic.DicMoveTypeMapper;
import com.inossem.wms.dao.dic.DicPackageTypeMapper;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.dic.DicUnitMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizBusinessHistory;
import com.inossem.wms.model.biz.BizFailMes;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputItem;
import com.inossem.wms.model.biz.BizStockInputTransportHead;
import com.inossem.wms.model.biz.BizStockInputTransportItem;
import com.inossem.wms.model.biz.BizStockTaskHeadCw;
import com.inossem.wms.model.biz.BizStockTaskItemCw;
import com.inossem.wms.model.biz.BizStockTaskPositionCw;
import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.biz.BizStockTaskReqPosition;
import com.inossem.wms.model.biz.ErpReturn;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.enums.EnumDebitCredit;
import com.inossem.wms.model.enums.EnumFtyType;
import com.inossem.wms.model.enums.EnumMatStoreType;
import com.inossem.wms.model.enums.EnumRankLevel;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumSequence;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumStockType;
import com.inossem.wms.model.enums.EnumSynType;
import com.inossem.wms.model.enums.EnumTaskReqShippingType;
import com.inossem.wms.model.stock.StockOccupancy;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IInputProduceTransportService;
import com.inossem.wms.service.biz.IInputTransportService;
import com.inossem.wms.service.biz.ITaskService;
import com.inossem.wms.service.intfc.sap.CwErpWsImpl;
import com.inossem.wms.service.intfc.sap.CwMesWsImpl;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilString;
import com.inossem.wms.wsdl.mes.update.ArrayOfWmMvRecInteropeDto;
import com.inossem.wms.wsdl.mes.update.WmIopRetVal;
import com.inossem.wms.wsdl.mes.update.WmMvRecInteropeDto;
import com.inossem.wms.wsdl.sap.input.DTCWWMSMATDOCPOSTREQ;
import com.inossem.wms.wsdl.sap.input.MSGHEAD;
import com.inossem.wms.wsdl.sap.inputc.DTCWWMSCANCELMATDOCREQ;
import com.inossem.wms.wsdl.sap.inputc.DTCWWMSCANCELMATDOCREQ.ITMATDOC;
import com.lmax.disruptor.util.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("inputTransportService")
@Transactional
public class InputTransportServiceImpl implements IInputTransportService {


	@Autowired
	private ICommonService commonService;

	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private BizStockTaskItemCwMapper bizStocktakeItemDao;
	
	@Autowired
	private BizStockTaskHeadCwMapper bizStockTaskHeadCwDao;
	
	
	@Autowired
	private SequenceDAO sequenceDAO;

	@Autowired
	private BizStockInputHeadMapper stockInputHeadDao;
	

	@Autowired
	private BizInputProduceHeadMapper bizInputProduceHeadDao;

	@Autowired
	private BizStockTaskReqHeadMapper StockTaskReqHeadDao;

	@Autowired
	private BizStockTaskReqItemMapper StockTaskReqItemDao;

	
	@Autowired
	private BizStockInputTransportHeadMapper bizStockInputHeadDao;
	
	@Autowired
	private BizStockInputTransportItemMapper bizStockInputTransportItemDao;
	
	@Autowired
	private DicUnitMapper dicUnitMapper;
	
	
	@Autowired
	private DicStockLocationMapper dicLocationDao;
	
	@Autowired
	private DicClassTypeMapper dicClassTypeDao;

	@Autowired
	private DicMoveTypeMapper dicMoveTypeDao;
	
	@Autowired
	private DicStockLocationMapper dicStockLocationDao;
	

	@Autowired
	private DicClassTypeMapper dicClassTypeMapper;
	@Autowired
	private CwErpWsImpl cwErpWsImpl;
	
	@Autowired
	private CwMesWsImpl cwMesWsImpl;
	
	@Autowired
	private BizFailMesMapper failMesDao;

	@Autowired
	private DicFactoryMapper dicFactoryDao;
	@Autowired
	private BizStockTaskItemCwMapper stockTaskItemCwDao;
	
	@Autowired
	private BizStockTaskPositionCwMapper bizStockTaskPositionCwDao;
	
	@Autowired
	private BizBusinessHistoryMapper bizBusinessHistoryDao;
	
	@Autowired
	private ITaskService taskService;
	
	@Autowired
	private IInputProduceTransportService iInputProduceTransportService;
	
	@Autowired
	private BizStockTaskReqPositionMapper StockTaskReqPositionDao;

	@Autowired
	private DicInstallationMapper dicInstallationDao;
	
	
	@Autowired
	private DicPackageTypeMapper dicPackageTypeDao;
	

	@Override
	public BizStockInputHead getBizStockInputHeadByStockInputId(int stockInputId) throws Exception {
		return stockInputHeadDao.selectByPrimaryKey(stockInputId);
	}
	
	
	@Override
	public String selectNowTime() throws Exception {
		Date day=new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(day);
	}

	@Override
	public List<Map<String, Object>> getTransportInputList(Map<String, Object> map) {
		List<Map<String, Object>> shList = bizStockInputHeadDao.getTransportInputListOnPaging(map);
		return shList;
	}

	@Override
	public Map<String, Object> getTransportInputInfo2(Map<String, Object> map) {
		Map<String, Object> result=bizStockInputHeadDao.getTransportInputInfo(map);
		return  result;
	}

	@Override
	public List<Map<String, Object>> getInputTransportItemListByID(Integer stock_input_id) {
		List<Map<String, Object>> shList = bizStockInputHeadDao.getInputTransportItemListByID(stock_input_id);
		return shList;
	}

	@Override
	public List<Map<String, Object>> selectAllclassType() {
		return dicClassTypeDao.selectAll();
	}

	@Override
	public Integer selectNowClassType() {
		return  this.bizStockInputHeadDao.selectNowClassType();
	}

	@Override
	public List<Map<String, Object>> select_stock_task(Map<String, Object> map) {
		return bizStockInputHeadDao.select_stock_taskOnPaging(map);
	}

	@Override
	public List<Map<String, Object>> getStockMatListByTaskId(Map<String, Object> map) {
		return bizStockInputHeadDao.getStockMatListByTaskId(map);
	}

	@Override
	public BizStockInputTransportHead insertStock(List<Map<String,Object>> itemList,CurrentUser user,JSONObject json) {
		BizStockInputTransportHead bizStockInputTransportHead=new BizStockInputTransportHead();
		bizStockInputTransportHead.setClassTypeId(Integer.valueOf(json.getString("class_type")));
		bizStockInputTransportHead.setCreateTime(new Date());
		bizStockInputTransportHead.setCreateUser(user.getUserId());
//		bizStockInputTransportHead.setFtyId(Integer.valueOf(UtilString.getStringOrEmptyForObject(headMap.get("fty_id"))));
		bizStockInputTransportHead.setIsDelete((byte) 0);
//		bizStockInputTransportHead.setLocationId(Integer.valueOf(UtilString.getStringOrEmptyForObject(headMap.get("location_id"))));
		bizStockInputTransportHead.setModifyTime(new Date());
//		bizStockInputTransportHead.setMoveTypeId(Integer.valueOf(UtilString.getStringOrEmptyForObject(headMap.get("move_type_id"))));
//		int business_type_id=json.getInt("business_type_id");
		bizStockInputTransportHead.setReceiptType(EnumReceiptType.STOCK_INPUT_TRANSPORT_ERP.getValue());
		bizStockInputTransportHead.setRemark(json.getString("remark"));
//		bizStockInputTransportHead.setRequestSource(requestSource);
		bizStockInputTransportHead.setStatus((byte) 0);
	
		bizStockInputTransportHead.setStockTransportCode(String.valueOf(sequenceDAO.selectNextVal("stock_input")));
		bizStockInputTransportHead.setSynType(Integer.valueOf(json.getString("syn_type")));	
//		bizStockInputTransportHead.setSynType(1);
		 bizStockInputHeadDao.insertSelective(bizStockInputTransportHead);
		JSONArray myJsonArray =JSONArray.fromObject(json.get("item_list"));	
		
		for(int i=0;i<itemList.size();i++) {
			BizStockInputTransportItem bizStockInputTransportItem=new BizStockInputTransportItem();
//			bizStockInputTransportItem.setAreaId(areaId);
			bizStockInputTransportItem.setBatch(UtilObject.getLongOrNull(itemList.get(i).get("batch")));
			bizStockInputTransportItem.setBusinessType(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("refer_receipt_type"))));
			bizStockInputTransportItem.setCreateTime(new Date());
			bizStockInputTransportItem.setFtyId(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("fty_id"))));
			bizStockInputTransportItem.setFtyInput(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("tran_fty_id"))));
			bizStockInputTransportItem.setFtyOutputId(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("output_fty_id"))));
			bizStockInputTransportItem.setIsDelete((byte) 0);
//			bizStockInputTransportItem.setItemId(itemId);
			bizStockInputTransportItem.setLocationId(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("location_id"))));
			bizStockInputTransportItem.setLocationInput(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("tran_location_id"))));
			bizStockInputTransportItem.setLocationOutputId(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("output_location_id"))));
			bizStockInputTransportItem.setMatId(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("mat_id"))));
			bizStockInputTransportItem.setMatInput(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("tran_mat_id"))));
			bizStockInputTransportItem.setModifyTime(new Date());
//			bizStockInputTransportItem.setPositionId(positionId);
			bizStockInputTransportItem.setReferReceiptCode(UtilString.getStringOrEmptyForObject(itemList.get(i).get("stock_task_code")));
			bizStockInputTransportItem.setReferReceiptId(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("stock_task_id"))));
			bizStockInputTransportItem.setReferReceiptRid(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("stock_task_rid"))));
			bizStockInputTransportItem.setPackageTypeId(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("package_type_id"))));
			JSONObject jsonObject=JSONObject.fromObject(myJsonArray.get(i));		
			if(jsonObject.containsKey("remark")) {
				bizStockInputTransportItem.setRemark(jsonObject.getString("remark"));
			}
			
			
//			bizStockInputTransportItem.setSpecStockCodeInput(specStockCodeInput);
//			bizStockInputTransportItem.setSpecStockCodeOutput(specStockCodeOutput);
//			bizStockInputTransportItem.setStockPositionId(stockPositionId);
			bizStockInputTransportItem.setStockTransportId(bizStockInputTransportHead.getStockTransportId());
			bizStockInputTransportItem.setTransportQty(new BigDecimal(UtilString.getStringOrEmptyForObject(itemList.get(i).get("qty")).toString()));
			bizStockInputTransportItem.setStockTransportRid(i+1);
			bizStockInputTransportItem.setProductionBatch(UtilString.getStringOrEmptyForObject(itemList.get(i).get("production_batch")));
			bizStockInputTransportItem.setErpBatch(UtilString.getStringOrEmptyForObject(itemList.get(i).get("erp_batch")));
//			bizStockInputTransportItem.setUnitId(unitId);
			bizStockInputTransportItemDao.insertSelective(bizStockInputTransportItem);		
            BizStockTaskItemCw record=new BizStockTaskItemCw();
            record.setItemId((Integer) jsonObject.get("item_id"));
			record.setStatus(EnumReceiptStatus.RECEIPT_ISUSED.getValue());
			bizStocktakeItemDao.updateByPrimaryKeySelective(record);
		}
		
		return bizStockInputTransportHead;
	}

	@Override
	public void updateStockToWriteOff(Integer stock_input_id,CurrentUser cuser,List<Map<String,Object>> itemList,BizStockInputTransportHead head) throws Exception {
		BizStockInputTransportHead bizStockInputTransportHead=new BizStockInputTransportHead();
		bizStockInputTransportHead.setStockTransportId(stock_input_id);
		bizStockInputTransportHead.setStatus((byte) 20);
		bizStockInputHeadDao.updateByPrimaryKeySelective(bizStockInputTransportHead);
		
		BizStockInputTransportHead bizStock=bizStockInputHeadDao.selectByPrimaryKey(stock_input_id);
		String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());
		//保存作业请求头部信息
		
		
		
		Map<String,Object> locationIdMap = new HashMap<String,Object>();
		List<Map<String,Object>> list=bizStockInputTransportItemDao.selectByStockTransportId(stock_input_id);
		BizStockTaskReqHead taskReqHead = new BizStockTaskReqHead();
		for(int i=0;i<list.size();i++) {
			
			taskReqHead.setStockTaskReqCode(stockTaskReqCode);
			taskReqHead.setMatDocId(0);
			taskReqHead.setMatDocYear(0);
			taskReqHead.setShippingType(EnumTaskReqShippingType.STOCK_OUTPUT.getValue());
			taskReqHead.setStatus(EnumBoolean.FALSE.getValue());
			taskReqHead.setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue());
			taskReqHead.setReferReceiptId(bizStock.getStockTransportId());
			taskReqHead.setReferReceiptCode(bizStock.getStockTransportCode().toString());
			taskReqHead.setReferReceiptType(bizStock.getReceiptType());
//			if(bizStock.getReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_FACTORY.getValue())) {
//				taskReqHead.setReferReceiptType(EnumReceiptType.STOCK_TRANSPORT_FACTORY_WRITEOFF.getValue());
//			}
//			if(bizStock.getReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_STOCK.getValue())) {
//				taskReqHead.setReferReceiptType(EnumReceiptType.STOCK_TRANSPORT_STOCK_WRITEOFF.getValue());
//			}
//			if(bizStock.getReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue())) {
//				taskReqHead.setReferReceiptType(EnumReceiptType.STOCK_TRANSPORT_MATERIAL_WRITEOFF.getValue());
//			}
			
			
			
			taskReqHead.setCreateUser(cuser.getUserId());
			taskReqHead.setCreateTime(new Date());
			taskReqHead.setModifyTime(new Date());
			

			Map<String,Object> item = list.get(i);
			
			
			taskReqHead.setLocationId(UtilObject.getIntegerOrNull(item.get("location_input")));
			int wh=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(item.get("location_input").toString())).getWhId();
			taskReqHead.setFtyId(UtilObject.getIntegerOrNull(item.get("fty_input")));
			taskReqHead.setWhId(wh);
			if(!locationIdMap.containsKey(item.get("location_input").toString())) {
				taskReqHead.setStockTaskReqId(null);
				StockTaskReqHeadDao.insertSelective(taskReqHead);
				locationIdMap.put(item.get("location_input").toString(), taskReqHead.getStockTaskReqId());
			}
			
			
//			BizStockTaskReqItem taskReqItem = new BizStockTaskReqItem();
//			taskReqItem.setFtyId(UtilObject.getIntOrZero(item.get("output_fty_id")));
//			taskReqItem.setLocationId(UtilObject.getIntOrZero(item.get("output_location_id")));
//			taskReqItem.setMatId(UtilObject.getIntegerOrNull(item.get("mat_id")));
//			taskReqItem.setStockTaskQty(BigDecimal.ZERO);
//			taskReqItem.setQty(UtilObject.getBigDecimalOrZero(item.get("qty")));
//			taskReqItem.setBatch(UtilObject.getLongOrNull(item.get("batch")));
//			taskReqItem.setErpBatch(UtilObject.getStringOrEmpty(item.get("erp_batch")));
//			taskReqItem.setProductionBatch(UtilObject.getStringOrEmpty(item.get("production_batch")));
//			taskReqItem.setQualityBatch(UtilObject.getStringOrEmpty(item.get("production_batch")));
//			taskReqItem.setUnitId(UtilObject.getIntOrZero(item.get("unit_id")));
//			taskReqItem.setPackageTypeId(UtilObject.getIntOrZero(item.get("package_type_id")));
//			taskReqItem.setReferReceiptType(bizStock.getReceiptType());
//			taskReqItem.setReferReceiptId(bizStock.getStockTransportId());
//			taskReqItem.setReferReceiptCode(bizStock.getStockTransportCode());
//			taskReqItem.setReferReceiptRid(UtilObject.getIntOrZero(i+1));
//			taskReqItem.setStockTaskReqId(taskReqHead.getStockTaskReqId());
//			taskReqItem.setStockTaskReqRid(UtilObject.getIntOrZero(i));
//			taskReqItem.setIsDelete(EnumBoolean.FALSE.getValue());
//			taskReqItem.setStatus(EnumBoolean.FALSE.getValue());
//			int wh1=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(item.get("output_location_id").toString())).getWhId();
//			taskReqItem.setWhId(wh1);
//			taskReqItem.setCreateTime(new Date());
//			taskReqItem.setModifyTime(new Date());
//			StockTaskReqItemDao.insertSelective(taskReqItem);
			
			BizStockTaskReqItem taskReqItem = new BizStockTaskReqItem();
			taskReqItem.setFtyId(UtilObject.getIntOrZero(item.get("fty_input")));
			taskReqItem.setLocationId(UtilObject.getIntOrZero(item.get("location_input")));
			taskReqItem.setMatId(UtilObject.getIntegerOrNull(item.get("mat_input")));
			taskReqItem.setStockTaskQty(BigDecimal.ZERO);
			taskReqItem.setQty(UtilObject.getBigDecimalOrZero(item.get("qty")));
			taskReqItem.setBatch(UtilObject.getLongOrNull(item.get("tran_batch")));
			taskReqItem.setErpBatch(UtilObject.getStringOrEmpty(item.get("erp_batch")));
			taskReqItem.setProductionBatch(UtilObject.getStringOrEmpty(item.get("production_batch")));
			taskReqItem.setQualityBatch(UtilObject.getStringOrEmpty(item.get("production_batch")));
			taskReqItem.setUnitId(UtilObject.getIntOrZero(item.get("unit_id")));
			taskReqItem.setPackageTypeId(UtilObject.getIntOrZero(item.get("package_type_id")));
			taskReqItem.setReferReceiptType(bizStock.getReceiptType());
//			if(bizStock.getReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_FACTORY.getValue())) {
//				taskReqItem.setReferReceiptType(EnumReceiptType.STOCK_TRANSPORT_FACTORY_WRITEOFF.getValue());
//			}
//			if(bizStock.getReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_STOCK.getValue())) {
//				taskReqItem.setReferReceiptType(EnumReceiptType.STOCK_TRANSPORT_STOCK_WRITEOFF.getValue());
//			}
//			if(bizStock.getReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue())) {
//				taskReqItem.setReferReceiptType(EnumReceiptType.STOCK_TRANSPORT_MATERIAL_WRITEOFF.getValue());
//			}
			
			taskReqItem.setReferReceiptId(bizStock.getStockTransportId());
			taskReqItem.setReferReceiptCode(bizStock.getStockTransportCode());
			taskReqItem.setReferReceiptRid(UtilObject.getIntOrZero(item.get("stock_transport_rid")));
			taskReqItem.setStockTaskReqId(taskReqHead.getStockTaskReqId());
			taskReqItem.setStockTaskReqRid(UtilObject.getIntOrZero(i+1));
			taskReqItem.setIsDelete(EnumBoolean.FALSE.getValue());
			taskReqItem.setStatus(EnumBoolean.FALSE.getValue());
			taskReqItem.setCreateTime(new Date());
			taskReqItem.setModifyTime(new Date());
			taskReqItem.setWhId(wh);
			StockTaskReqItemDao.insertSelective(taskReqItem);
			BizStockTaskPositionCw tp = new BizStockTaskPositionCw();
//			tp.setStockTaskId((Integer) itemList.get(i).get("stock_task_id"));
//			tp.setItemId((Integer) itemList.get(i).get("item_id"));
			tp.setStockTaskId((Integer) list.get(i).get("refer_id"));
			tp.setStockTaskRid((Integer) list.get(i).get("refer_receipt_id"));
//			tp.setReferReceiptType((Byte) itemList.get(0).get("refer_receipt_type"));
			List<BizStockTaskPositionCw> positionList = bizStockTaskPositionCwDao.selectByReferReceiptIdAndType(tp);
			
			Byte matStoreType = UtilObject.getByteOrNull(item.get("sn_used"));
			if(matStoreType.equals(EnumMatStoreType.USEING.getValue())){
				// 后台启用sn 生成position
				if(positionList!=null&&positionList.size()>0){
					int ri = 1;
					for(BizStockTaskPositionCw p:positionList){
						BizStockTaskReqPosition reqPosition = new BizStockTaskReqPosition();
						reqPosition.setStockTaskReqId(taskReqHead.getStockTaskReqId());
						reqPosition.setStockTaskReqRid(taskReqItem.getStockTaskReqRid());
						reqPosition.setStockTaskReqPositionId(ri);
						ri++;
						reqPosition.setBatch(taskReqItem.getBatch());
						reqPosition.setPalletId(p.getPalletId());
						reqPosition.setPackageId(p.getPackageId());
						reqPosition.setQty(p.getQty());
						StockTaskReqPositionDao.insertSelective(reqPosition);
					}
				}
			}
			
			
			BizBusinessHistory record=new BizBusinessHistory();
			int whId=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(item.get("location_input").toString())).getWhId();			
			// 上架 组盘上架
			String whCode = dictionaryService.getWhCodeByWhId(whId);
			Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
					UtilProperties.getInstance().getDefaultCCQ());
			Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
					UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
			record.setAreaId(sourceAreaId);
			record.setBatch(new Long(item.get("tran_batch").toString()));
			record.setBusinessType((byte) 2);
			record.setCreateTime(new Date());
			record.setCreateUser(cuser.getUserId());
			record.setDebitCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			record.setFtyId(UtilObject.getIntOrZero(item.get("fty_input")));
			record.setLocationId(UtilObject.getIntOrZero(item.get("location_input")));
			record.setMatId(UtilObject.getIntegerOrNull(item.get("mat_input")));
			record.setQty(new BigDecimal(item.get("qty").toString()));
			record.setModifyTime(new Date());
			record.setModifyUser(cuser.getUserId());
//			record.setPackageId(Integer.valueOf(id));
//			record.setPalletId(Integer.valueOf(pkgPositionList.get(u).get("pallet_id").toString()));
			record.setPositionId(sourcePositionId);
			record.setReceiptType(bizStock.getReceiptType());
			record.setReferReceiptCode(bizStock.getStockTransportCode());
			record.setReferReceiptId(bizStock.getStockTransportId());
			record.setReferReceiptPid(whId);
			record.setReferReceiptRid(i+1);
			record.setSynMes(true);
			record.setSynSap(true);
			bizBusinessHistoryDao.insertSelective(record);	
			
			
			
			BizBusinessHistory record1=new BizBusinessHistory();
			int whId1=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(item.get("location_output_id").toString())).getWhId();			
			// 上架 组盘上架
			String whCode1 = dictionaryService.getWhCodeByWhId(whId1);
			Integer sourceAreaId1 = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode1,
					UtilProperties.getInstance().getDefaultCCQ());
			Integer sourcePositionId1 = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode1,
					UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
			record1.setAreaId(sourceAreaId1);
			record1.setBatch(new Long(item.get("batch").toString()));
			record1.setBusinessType((byte) 1);
			record1.setCreateTime(new Date());
			record1.setCreateUser(cuser.getUserId());
			record1.setDebitCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
			record1.setFtyId(UtilObject.getIntOrZero(item.get("fty_output_id")));
			record1.setLocationId(UtilObject.getIntOrZero(item.get("location_output_id")));
			record1.setMatId(UtilObject.getIntegerOrNull(item.get("mat_id")));
			record1.setQty(new BigDecimal(item.get("qty").toString()));
			record1.setModifyTime(new Date());
			record1.setModifyUser(cuser.getUserId());
//			record.setPackageId(Integer.valueOf(id));
//			record.setPalletId(Integer.valueOf(pkgPositionList.get(u).get("pallet_id").toString()));
			record1.setPositionId(sourcePositionId1);
			record1.setReceiptType(bizStock.getReceiptType());
			record1.setReferReceiptCode(bizStock.getStockTransportCode());
			record1.setReferReceiptId(bizStock.getStockTransportId());
			record1.setReferReceiptPid(whId1);
			record1.setReferReceiptRid(i+1);
			record1.setSynMes(true);
			record1.setSynSap(true);
			bizBusinessHistoryDao.insertSelective(record1);	
			
			
			
			
			Map<String, Object> map=new HashMap<String,Object>();
			BizStockTaskItemCw ti = new BizStockTaskItemCw();
			ti.setStockTaskId((Integer) list.get(i).get("refer_id"));
			ti.setStockTaskRid((Integer) list.get(i).get("refer_receipt_id"));
//			ti.setReferReceiptType((Byte) itemList.get(0).get("refer_receipt_type"));
			List<BizStockTaskItemCw> taskItemList = stockTaskItemCwDao.selectByReferReceiptIdAndType(ti);
			
			
			//发出

			map.put("matId", list.get(i).get("mat_id"));
			map.put("locationId", list.get(i).get("location_output_id"));
			map.put("batch", list.get(i).get("batch"));
			map.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			map.put("createUserId", cuser.getUserId());
			map.put("debitCredit",EnumDebitCredit.DEBIT_S_ADD.getName());
			map.put("qty", list.get(i).get("qty"));
			map.put("stockTypeId",EnumStockType.STOCK_TYPE_ERP.getValue());
			commonService.modifyStockBatch(map);			
		
			//接收
			map.put("matId", list.get(i).get("mat_input"));
			map.put("locationId", list.get(i).get("location_input"));
			map.put("batch", list.get(i).get("tran_batch"));
			map.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			map.put("createUserId", cuser.getUserId());
			map.put("debitCredit",EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			map.put("qty", list.get(i).get("qty"));
			commonService.modifyStockBatch(map);						
			
			
//			if(positionList!=null && positionList.size()>0) {
//				commonService.updateStockPositionByTask(taskItemList, positionList,EnumStockType.STOCK_TYPE_ERP.getValue(),false);
//			}
			Map<String,Object> up=new HashMap<String,Object>();//发出
			for(int p=0;p<taskItemList.size();p++) {
				up.put(taskItemList.get(p).getStockTaskId().toString()+"-"+taskItemList.get(p).getStockTaskRid().toString(), taskItemList.get(p)); 
				if(taskItemList.get(p).getReceiptType().equals(EnumReceiptType.STOCK_TASK_LISTING.getValue())) {
					taskItemList.get(p).setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL.getValue());
				}else if(taskItemList.get(p).getReceiptType().equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {				
					taskItemList.get(p).setReceiptType(EnumReceiptType.STOCK_TASK_LISTING.getValue());
				}
			}
			
			for(int k=0;k<positionList.size();k++) {
				BizStockTaskItemCw bizStockTaskItemCw=(BizStockTaskItemCw)up.get(positionList.get(k).getStockTaskId().toString()+"-"+positionList.get(k).getStockTaskRid().toString());			
				if(bizStockTaskItemCw.getReceiptType().equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
					positionList.get(k).setTargetAreaId(positionList.get(k).getSourceAreaId());
					positionList.get(k).setTargetPositionId(positionList.get(k).getSourcePositionId());
				}
				
				if(bizStockTaskItemCw.getReceiptType().equals(EnumReceiptType.STOCK_TASK_LISTING.getValue())) {
					positionList.get(k).setSourceAreaId(positionList.get(k).getTargetAreaId());
					positionList.get(k).setSourcePositionId(positionList.get(k).getTargetPositionId());
				}
			}
			if(positionList!=null && positionList.size()>0) {
				commonService.updateStockPositionByTask(taskItemList, positionList,EnumStockType.STOCK_TYPE_ERP.getValue(),false);
			}
			
			
			
			if(!taskItemList.get(0).getReferReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue())) {
			
				for(int p=0;p<taskItemList.size();p++) {					
					taskItemList.get(p).setMatId(Integer.valueOf(list.get(i).get("mat_id").toString()));
					taskItemList.get(p).setFtyId(Integer.valueOf(list.get(i).get("fty_output_id").toString()));
					taskItemList.get(p).setLocationId(Integer.valueOf(list.get(i).get("location_output_id").toString()));
					taskItemList.get(p).setReceiptType(EnumReceiptType.STOCK_TASK_LISTING.getValue());
				}
				
				for(int k=0;k<positionList.size();k++) {
					
					positionList.get(k).setWhId(whId1);
					positionList.get(k).setSourceAreaId(Integer.valueOf(sourceAreaId1));
					positionList.get(k).setSourcePositionId(Integer.valueOf(sourcePositionId1));
				}
				if(positionList!=null && positionList.size()>0) {
					commonService.updateStockPositionByTask(taskItemList, positionList,EnumStockType.STOCK_TYPE_ERP.getValue(),false);
				}
			}
			
			
			
			if(list.get(i).get("refer_receipt_type").toString().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue().toString())) {
				for(int p=0;p<taskItemList.size();p++) {
					Map<String,Object> param=new HashMap<String,Object>();
					param.put("taskId", taskItemList.get(p).getStockTaskId());
					param.put("taskIdRid", taskItemList.get(p).getStockTaskRid());
				}
			}
		}
		if(!locationIdMap.isEmpty()) {
			for(String key:locationIdMap.keySet()) {
				Integer stockTaskReqId = UtilObject.getIntegerOrNull(locationIdMap.get(key));
				taskService.transportByProduct(stockTaskReqId);
			}
		}
		if(!bizStock.getSynType().toString().equals(String.valueOf(EnumSynType.MES_SYN.getValue()))) {	
			ErpReturn er=this.takeSapWriteOff(head,itemList);	
			if("S".equals(er.getType())) {
				for(int i=0;i<list.size();i++) {
					BizStockInputTransportItem record=new BizStockInputTransportItem();
					record.setItemId(Integer.valueOf(list.get(i).get("item_id").toString()));
					record.setMatOffCode(er.getMatDocCode());
					bizStockInputTransportItemDao.updateByPrimaryKeySelective(record);		
				}
			}else {
				throw new WMSException(er.getMessage());
			}
		}
		
	}

	@Override
	public List<Map<String, Object>> getMatListByID(JSONArray myJsonArray) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String>  list=new ArrayList<String>();
		for(int i=0;i<myJsonArray.size();i++) {
			JSONObject jsonObject = JSONObject.fromObject(myJsonArray.get(i));	
			list.add(jsonObject.get("item_id").toString());
		}
		map.put("myJsonArray", list);
		return bizStockInputHeadDao.getMatListByID(map);
	}

	@Override
	public ErpReturn takeSap(BizStockInputTransportHead bizStockInputTransportHead,List<Map<String, Object>> itemList) throws Exception{
		DTCWWMSMATDOCPOSTREQ dtcwwmsmatdocpostreq=new DTCWWMSMATDOCPOSTREQ();
		String date=DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		MSGHEAD msghead=new MSGHEAD();
		msghead.setSYSTEMID("WMS");
		msghead.setGUID(date);
		String ivbukrs="";
		String ivtestrun="";
		dtcwwmsmatdocpostreq.setISMSGHEAD(msghead);
		dtcwwmsmatdocpostreq.setIVBUKRS(ivbukrs);
		dtcwwmsmatdocpostreq.setIVTESTRUN(ivtestrun);		
		List<DTCWWMSMATDOCPOSTREQ.ITDOCHEAD> itdochead=new ArrayList<DTCWWMSMATDOCPOSTREQ.ITDOCHEAD>();
		List<DTCWWMSMATDOCPOSTREQ.ITDOCITEM> itdocitem=new ArrayList<DTCWWMSMATDOCPOSTREQ.ITDOCITEM>();	
		DTCWWMSMATDOCPOSTREQ.ITDOCHEAD  itdocheadtest=new DTCWWMSMATDOCPOSTREQ.ITDOCHEAD();
		itdocheadtest.setDOCNUM(bizStockInputTransportHead.getStockTransportCode().toString());
		itdocheadtest.setGOODSMVTCODE("04");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		java.util.Date dat=new java.util.Date();  							
		itdocheadtest.setPSTNGDATE(sdf.format(dat));
		itdocheadtest.setDOCDATE(sdf.format(dat));
		itdochead.add(itdocheadtest);
		Map<String, Object> map = new HashMap<String, Object>();
		int j=1;
		BigDecimal bdm=BigDecimal.ZERO;
		
		
		for(int i=0;i<itemList.size();i++) {
			if(!map.containsKey(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString())) {
				map.put(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString(), new BigDecimal(itemList.get(i).get("qty").toString()));					
			}else {
				map.put(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString(), new BigDecimal(map.get(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString()).toString()).add(new BigDecimal(itemList.get(i).get("qty").toString())));								
			}	
		}
		Map<String, Object> m = new HashMap<String, Object>();
//		111
		for(int i=0;i<itemList.size();i++) {
			DTCWWMSMATDOCPOSTREQ.ITDOCITEM itdocitemtest=new DTCWWMSMATDOCPOSTREQ.ITDOCITEM();
			itdocitemtest.setDOCNUM(bizStockInputTransportHead.getStockTransportCode().toString());
			itdocitemtest.setDOCITEMNUM(itemList.get(i).get("stock_task_rid").toString());
			itdocitemtest.setPLANT(itemList.get(i).get("output_fty_code").toString());
			itdocitemtest.setSTGELOC(itemList.get(i).get("output_location_code").toString());
			itdocitemtest.setMATERIAL(itemList.get(i).get("mat_code").toString());
			itdocitemtest.setBATCH(itemList.get(i).get("tran_erp_batch").toString());
			itdocitemtest.setENTRYUOM("KG");
			itdocitemtest.setMOVETYPE(dicMoveTypeDao.selectByPrimaryKey(Integer.valueOf(itemList.get(i).get("move_type_id").toString())).getMoveTypeCode());
			itdocitemtest.setMOVEPLANT(itemList.get(i).get("fty_code").toString());
			itdocitemtest.setMOVESTLOC(itemList.get(i).get("location_code").toString());
			itdocitemtest.setMOVEMAT(itemList.get(i).get("tran_mat_code").toString());
			itdocitemtest.setMOVEBATCH(itemList.get(i).get("erp_batch").toString());			
			if(map.containsKey(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString())) {
				if(!m.containsKey(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString())) {
					itdocitemtest.setDOCITEMNUM(j+"");
					BigDecimal qty = UtilObject.getBigDecimalOrZero(map.get(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString()).toString());
					if(UtilObject.getStringOrEmpty(itemList.get(i).get("unit_code")).equals("KG")) {
						qty = qty.multiply(new BigDecimal("1"));
					}else {
						qty = qty.multiply(new BigDecimal("1000"));
					}
					itdocitemtest.setENTRYQNT(qty.toString());
					itdocitem.add(itdocitemtest);	
					j++;
					m.put(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString(), "1");
				}											
			}			
		}		
		dtcwwmsmatdocpostreq.setItdocitem(itdocitem);
		dtcwwmsmatdocpostreq.setItdochead(itdochead);
		ErpReturn er=cwErpWsImpl.inputPost(dtcwwmsmatdocpostreq);
		return er;
		
	}

	@Override
	public int modifyNum(List<Map<String,Object>> itemList,CurrentUser user,BizStockInputTransportHead bizStockInputTransportHead) throws Exception {
		
		int ret=0;
		for(int i=0;i<itemList.size();i++) {
			Map<String, Object> map=new HashMap<String,Object>();
			BizStockTaskItemCw ti = new BizStockTaskItemCw();
			ti.setStockTaskId((Integer) itemList.get(i).get("stock_task_id"));
			ti.setStockTaskRid((Integer) itemList.get(i).get("stock_task_rid"));
			List<BizStockTaskItemCw> taskItemList = stockTaskItemCwDao.selectByReferReceiptIdAndType(ti);
			
			BizStockTaskPositionCw tp = new BizStockTaskPositionCw();
			tp.setStockTaskId((Integer) itemList.get(i).get("stock_task_id"));
			tp.setStockTaskRid((Integer) itemList.get(i).get("stock_task_rid"));
			List<BizStockTaskPositionCw> positionList = bizStockTaskPositionCwDao.selectByReferReceiptIdAndType(tp);
			//发出
			map.put("matId", itemList.get(i).get("mat_id"));
			map.put("locationId", itemList.get(i).get("output_location_id"));
			map.put("batch", itemList.get(i).get("batch"));
			map.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			map.put("createUserId", user.getUserId());
			map.put("debitCredit", "H");
			map.put("qty", itemList.get(i).get("qty"));
			map.put("stockTypeId",EnumStockType.STOCK_TYPE_ERP.getValue());
			commonService.modifyStockBatch(map);
		
			//接收
			map.put("matId", itemList.get(i).get("tran_mat_id"));
			map.put("locationId", itemList.get(i).get("tran_location_id"));
			map.put("batch", itemList.get(i).get("tran_batch"));
			map.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			map.put("createUserId", user.getUserId());
			map.put("debitCredit", "S");
			map.put("qty", itemList.get(i).get("qty"));
			commonService.modifyStockBatch(map);			

			
			
			if(positionList!=null && positionList.size()>0) {
				commonService.updateStockPositionByTask(taskItemList, positionList,EnumStockType.STOCK_TYPE_ERP.getValue(),false);
			}
			
			
			if(!itemList.get(i).get("refer_receipt_type").toString().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue().toString())) {
				Map<String,Object> up=new HashMap<String,Object>();//发出
				for(int p=0;p<taskItemList.size();p++) {					
					taskItemList.get(p).setMatId(Integer.valueOf(itemList.get(i).get("mat_id").toString()));
					taskItemList.get(p).setFtyId(Integer.valueOf(itemList.get(i).get("output_fty_id").toString()));
					taskItemList.get(p).setLocationId(Integer.valueOf(itemList.get(i).get("output_location_id").toString()));
					taskItemList.get(p).setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL.getValue());
					up.put(taskItemList.get(p).getStockTaskId().toString()+taskItemList.get(p).getStockTaskRid().toString(), taskItemList.get(p));
				}
				
				for(int k=0;k<positionList.size();k++) {
					Integer locationId = Integer.valueOf(itemList.get(i).get("output_location_id").toString());
					int whId=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(locationId)).getWhId();
					String whCode = dictionaryService.getWhCodeByWhId(whId);
					Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
							UtilProperties.getInstance().getDefaultCCQ());
					Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
							UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
					positionList.get(k).setWhId(whId);
					positionList.get(k).setTargetAreaId(Integer.valueOf(sourceAreaId));
					positionList.get(k).setTargetPositionId(Integer.valueOf(sourcePositionId));
				}
				if(positionList!=null && positionList.size()>0) {
					commonService.updateStockPositionByTask(taskItemList, positionList,EnumStockType.STOCK_TYPE_ERP.getValue(),false);
				}
				
				
				
				Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
				DicStockLocationVo locationObj = locationMap.get(Integer.valueOf(itemList.get(i).get("output_location_id").toString()));

				if (locationObj.getFtyType().equals(EnumFtyType.PRODUCT.getValue())) {		
					StockOccupancy stockOccupancy = new StockOccupancy();
					stockOccupancy.setBatch(new Long(itemList.get(i).get("batch").toString()));
					stockOccupancy.setFtyId(Integer.valueOf(itemList.get(i).get("output_fty_id").toString()));
					stockOccupancy.setLocationId(Integer.valueOf(itemList.get(i).get("output_location_id").toString()));
					stockOccupancy.setMatId(Integer.valueOf(itemList.get(i).get("mat_id").toString()));
					stockOccupancy.setQty(new BigDecimal(itemList.get(i).get("qty").toString()));
					stockOccupancy.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
					commonService.modifyStockOccupancy(stockOccupancy, EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				}
				
				
				bizStockInputHeadDao.updateStatusByCode(itemList.get(i).get("refer_receipt_code").toString());
			}else {
				BizStockTaskItemCw bizStockTaskItemCw=new BizStockTaskItemCw();
				bizStockTaskItemCw.setItemId(Integer.valueOf(itemList.get(i).get("item_id").toString()));
				bizStockTaskItemCw.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
				bizStocktakeItemDao.updateByPrimaryKeySelective(bizStockTaskItemCw);
			}

			
			BizBusinessHistory record=new BizBusinessHistory();
			
			if(!itemList.get(i).get("refer_receipt_type").toString().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue().toString())) {
				
				int whId=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(i).get("output_location_id").toString())).getWhId();
				// 上架 组盘上架
				String whCode = dictionaryService.getWhCodeByWhId(whId);
				Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
						UtilProperties.getInstance().getDefaultCCQ());
				Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
						UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
				record.setAreaId(sourceAreaId);
				record.setBatch(new Long(itemList.get(i).get("batch").toString()));
				record.setBusinessType((byte) 1);
				record.setCreateTime(new Date());
				record.setCreateUser(user.getUserId());
				record.setDebitCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				record.setFtyId(Integer.valueOf(itemList.get(i).get("output_fty_id").toString()));
				record.setLocationId(Integer.valueOf(itemList.get(i).get("output_location_id").toString()));
				record.setMatId(Integer.valueOf(itemList.get(i).get("mat_id").toString()));
				record.setModifyTime(new Date());
				record.setModifyUser(user.getUserId());
				record.setQty(new BigDecimal(itemList.get(i).get("qty").toString()));
				record.setPackageId(0);
				record.setPalletId(0);
				record.setPositionId(sourcePositionId);
				record.setReceiptType(UtilObject.getByteOrNull(EnumReceiptType.STOCK_INPUT_TRANSPORT_ERP.getValue()));
				record.setReferReceiptCode(bizStockInputTransportHead.getStockTransportCode());
				record.setReferReceiptId(bizStockInputTransportHead.getStockTransportId());
				record.setReferReceiptPid(whId);
				record.setReferReceiptRid(i+1);
				record.setSynMes(true);
				record.setSynSap(true);
				bizBusinessHistoryDao.insertSelective(record);	
			}
			
			
			int whIdin=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(i).get("tran_location_id").toString())).getWhId();
			String whCodein = dictionaryService.getWhCodeByWhId(whIdin);
			Integer sourceAreaIdin = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCodein,
					UtilProperties.getInstance().getDefaultCCQ());
			Integer sourcePositionIdin = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCodein,
					UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
			record.setAreaId(sourceAreaIdin);
			record.setBatch(new Long(itemList.get(i).get("tran_batch").toString()));
			record.setBusinessType((byte) 1);
			record.setCreateTime(new Date());
			record.setCreateUser(user.getUserId());
			record.setDebitCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
			record.setFtyId(Integer.valueOf(itemList.get(i).get("tran_fty_id").toString()));
			record.setLocationId(Integer.valueOf(itemList.get(i).get("tran_location_id").toString()));
			record.setMatId(Integer.valueOf(itemList.get(i).get("tran_mat_id").toString()));
			record.setModifyTime(new Date());
			record.setModifyUser(user.getUserId());
			record.setQty(new BigDecimal(itemList.get(i).get("qty").toString()));
			record.setPackageId(0);
			record.setPalletId(0);
			record.setPositionId(sourcePositionIdin);
			record.setReceiptType(UtilObject.getByteOrNull(EnumReceiptType.STOCK_INPUT_TRANSPORT_ERP.getValue()));
			record.setReferReceiptCode(bizStockInputTransportHead.getStockTransportCode());
			record.setReferReceiptId(bizStockInputTransportHead.getStockTransportId());
			record.setReferReceiptPid(whIdin);
			record.setReferReceiptRid(i+1);
			record.setSynMes(true);
			record.setSynSap(true);
			bizBusinessHistoryDao.insertSelective(record);
			
			
		}
		
		BizStockInputTransportHead head=new BizStockInputTransportHead();
		head.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		java.util.Date dat=new java.util.Date();  							
		head.setStockTransportId(bizStockInputTransportHead.getStockTransportId());
		head.setDocTime(sdf.format(dat));
		head.setPostingDate(sdf.format(dat));
		bizStockInputHeadDao.updateByPrimaryKeySelective(head);
		
		
		for(int i=0;i<itemList.size();i++) {
			List<String> statuslist=bizStockInputHeadDao.selectItemStatus(itemList.get(i).get("stock_task_id").toString());
			
			BizStockTaskItemCw record=new BizStockTaskItemCw();
			record.setItemId(Integer.valueOf(itemList.get(i).get("item_id").toString()));
			record.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
			bizStocktakeItemDao.updateByPrimaryKeySelective(record);
			
			if(!statuslist.contains("0")) {
				BizStockTaskHeadCw bizStockTaskHeadCw=new BizStockTaskHeadCw();
				bizStockTaskHeadCw.setStockTaskId(Integer.valueOf(itemList.get(i).get("stock_task_id").toString()));
				bizStockTaskHeadCw.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
				bizStockTaskHeadCwDao.updateByPrimaryKeySelective(bizStockTaskHeadCw);

				Map<String, Object> map=new HashMap<String,Object>();
				map.put("refer_receipt_code", itemList.get(i).get("refer_receipt_code"));
				map.put("receipt_type", EnumReceiptType.STOCK_TASK_LISTING.getValue());
				//if(!itemList.get(i).get("refer_receipt_type").toString().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue().toString())) {
					String totalTransportNum=bizStockInputHeadDao.selectTotalTransportNum(map);
					String transportSum=bizStockInputHeadDao.getTransportSum(map);
					if(totalTransportNum!=null && transportSum!=null) {
						if(new BigDecimal(totalTransportNum).compareTo(new BigDecimal(transportSum))==0) {	
							Map<String, Object> param=new HashMap<String,Object>();
							param.put("status",EnumReceiptStatus.RECEIPT_FINISH.getValue());
							param.put("referReceiptId", itemList.get(i).get("refer_receipt_id"));
							param.put("referReceiptType",itemList.get(i).get("refer_receipt_type"));
							bizStockTaskHeadCwDao.updateStatusByReferReceiptIdAndType(param);
							bizStockInputHeadDao.updateDownStaskStatus(param);
							bizStockInputHeadDao.updateTransportStatus(map);						    
						}
					}			
				/*}else {				
					bizStockInputHeadDao.updateTransportStatus(map);
				}	*/
			}					
		}
		
		if(!bizStockInputTransportHead.getSynType().toString().equals(String.valueOf(EnumSynType.MES_SYN.getValue()))) {
			ErpReturn er=this.takeSap(bizStockInputTransportHead,itemList);
			
			if("S".equals(er.getType())) {
				List<Map<String,Object>> itemList1=bizStockInputHeadDao.selectItemList(bizStockInputTransportHead.getStockTransportId());
				for(int i=0;i<itemList1.size();i++) {
					BizStockInputTransportItem record=new BizStockInputTransportItem();
					record.setItemId(Integer.valueOf(itemList1.get(i).get("item_id").toString()));
					record.setMatDocCode(er.getMatDocCode());	
					record.setMatDocRid(i+1);
					record.setMatDocYear(er.getDocyear());
					bizStockInputTransportItemDao.updateByPrimaryKeySelective(record);
				}
			}else {
				throw new WMSException(er.getMessage());
			}
		}
		return ret;		
	}

	@Override
	public void updateStockHeandStatus(List<Map<String,Object>> itemList,Integer stock_input_id) {
		
		for(int i=0;i<itemList.size();i++) {
			BizStockTaskHeadCw record=new BizStockTaskHeadCw();
			record.setStockTaskId(Integer.valueOf(itemList.get(i).get("stock_task_id").toString()));
			record.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
			bizStockTaskHeadCwDao.updateByPrimaryKeySelective(record);
			
			BizStockTaskItemCw bizStockTaskItemCw=new BizStockTaskItemCw();
			bizStockTaskItemCw.setItemId(Integer.valueOf(itemList.get(i).get("item_id").toString()));
			bizStockTaskItemCw.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
			bizStocktakeItemDao.updateByPrimaryKeySelective(bizStockTaskItemCw);
			
			
			
			if(!String.valueOf(itemList.get(i).get("refer_receipt_type")).equals(String.valueOf(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue()))) {
				String stock_task_id=itemList.get(i).get("stock_task_id").toString();
				String other_id=bizStockInputHeadDao.getOtherId(stock_task_id);
				BizStockTaskHeadCw tranRecord=new BizStockTaskHeadCw();
				tranRecord.setStockTaskId(Integer.valueOf(other_id.toString()));
				tranRecord.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
				bizStockTaskHeadCwDao.updateByPrimaryKeySelective(tranRecord);				
			}
		} 
		BizStockInputTransportHead bizStockInputTransportHead=new BizStockInputTransportHead();
		bizStockInputTransportHead.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		java.util.Date dat=new java.util.Date();  							
		bizStockInputTransportHead.setStockTransportId(stock_input_id);
		bizStockInputTransportHead.setDocTime(sdf.format(dat));
		bizStockInputTransportHead.setPostingDate(sdf.format(dat));
		bizStockInputHeadDao.updateByPrimaryKeySelective(bizStockInputTransportHead);
		
	}

	@Override
	public void updateTransportStatus(List<Map<String, Object>> itemList, CurrentUser user) {

		
		for(int i=0;i<itemList.size();i++) {
			List<String> statuslist=bizStockInputHeadDao.selectItemStatus(itemList.get(i).get("stock_task_id").toString());
			
			BizStockTaskItemCw record=new BizStockTaskItemCw();
			record.setItemId(Integer.valueOf(itemList.get(i).get("item_id").toString()));
			record.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
			bizStocktakeItemDao.updateByPrimaryKeySelective(record);
			
			if(!statuslist.contains("0")) {
				BizStockTaskHeadCw bizStockTaskHeadCw=new BizStockTaskHeadCw();
				bizStockTaskHeadCw.setStockTaskId(Integer.valueOf(itemList.get(i).get("stock_task_id").toString()));
				bizStockTaskHeadCw.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
				bizStockTaskHeadCwDao.updateByPrimaryKeySelective(bizStockTaskHeadCw);

				Map<String, Object> map=new HashMap<String,Object>();
				map.put("refer_receipt_code", itemList.get(i).get("refer_receipt_code"));
				map.put("receipt_type", EnumReceiptType.STOCK_TASK_LISTING.getValue());
				if(!itemList.get(i).get("refer_receipt_type").toString().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue().toString())) {
					String totalTransportNum=bizStockInputHeadDao.selectTotalTransportNum(map);
					String transportSum=bizStockInputHeadDao.getTransportSum(map);
					if(totalTransportNum!=null && transportSum!=null) {
						if(new BigDecimal(totalTransportNum).compareTo(new BigDecimal(transportSum))==0) {	
							Map<String, Object> param=new HashMap<String,Object>();
							param.put("status",EnumReceiptStatus.RECEIPT_FINISH.getValue());
							param.put("refer_receipt_id", itemList.get(i).get("refer_receipt_id"));
							param.put("refer_receipt_type",itemList.get(i).get("refer_receipt_type"));
							bizStockTaskHeadCwDao.updateStatusByReferReceiptIdAndType(param);
							bizStockInputHeadDao.updateDownStaskStatus(param);
							bizStockInputHeadDao.updateTransportStatus(map);						    
						}
					}			
				}else {				
					bizStockInputHeadDao.updateTransportStatus(map);
				}	
			}					
		}

	}


	@Override
	public ErpReturn takeSapWriteOff(JSONArray item_id,JSONArray doc_yea) throws Exception {
		Map<String, Object> result = new HashMap<String,Object>();
		DTCWWMSCANCELMATDOCREQ  dtcwwmscancelmatdocreq = new DTCWWMSCANCELMATDOCREQ();
		
		com.inossem.wms.wsdl.sap.inputc.MSGHEAD iSMSGHEAD = new com.inossem.wms.wsdl.sap.inputc.MSGHEAD();
		iSMSGHEAD.setSYSTEMID("WMS");
		String date= DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		iSMSGHEAD.setGUID(date);
		dtcwwmscancelmatdocreq.setISMSGHEAD(iSMSGHEAD);
		List<ITMATDOC> IT_MATDOC = new ArrayList<ITMATDOC>();
		String date1 = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		for(int i=0;i< item_id.size(); i++){
			ITMATDOC ITMATDOC = new ITMATDOC();
			ITMATDOC.setMATERIALDOCUMENT(item_id.getString(i));
			ITMATDOC.setMATDOCUMENTYEAR(doc_yea.getString(i));
			ITMATDOC.setPSTNGDATE(date1);
			String rid = String.valueOf(i);
			while (rid.length() < 3) { // 不够三位数字，自动补0
				rid = "0" + rid;
			}
			ITMATDOC.setMATDOCITEM(rid);
			IT_MATDOC.add(ITMATDOC);
		}
		dtcwwmscancelmatdocreq.setItmatdoc(IT_MATDOC);
		ErpReturn erpReturn = cwErpWsImpl.inputWriteOff(dtcwwmscancelmatdocreq);
		result.put("mat_doc_code", erpReturn.getMatDocCode());
		result.put("message", erpReturn.getMessage());
		result.put("type", erpReturn.getType());
		return erpReturn;
		}


	@Override
	public ErpReturn takeSapWriteOff(BizStockInputTransportHead bizStockInputTransportHead,
			List<Map<String, Object>> itemList) throws Exception {
		DTCWWMSMATDOCPOSTREQ dtcwwmsmatdocpostreq=new DTCWWMSMATDOCPOSTREQ();
		String date=DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		MSGHEAD msghead=new MSGHEAD();
		msghead.setSYSTEMID("WMS");
		msghead.setGUID(date);
		String ivbukrs="";
		String ivtestrun="";
		dtcwwmsmatdocpostreq.setISMSGHEAD(msghead);
		dtcwwmsmatdocpostreq.setIVBUKRS(ivbukrs);
		dtcwwmsmatdocpostreq.setIVTESTRUN(ivtestrun);		
		List<DTCWWMSMATDOCPOSTREQ.ITDOCHEAD> itdochead=new ArrayList<DTCWWMSMATDOCPOSTREQ.ITDOCHEAD>();
		List<DTCWWMSMATDOCPOSTREQ.ITDOCITEM> itdocitem=new ArrayList<DTCWWMSMATDOCPOSTREQ.ITDOCITEM>();	
		DTCWWMSMATDOCPOSTREQ.ITDOCHEAD  itdocheadtest=new DTCWWMSMATDOCPOSTREQ.ITDOCHEAD();
		itdocheadtest.setDOCNUM(bizStockInputTransportHead.getStockTransportCode().toString());
		itdocheadtest.setGOODSMVTCODE("04");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		java.util.Date dat=new java.util.Date();  							
		itdocheadtest.setPSTNGDATE(sdf.format(dat));
		itdocheadtest.setDOCDATE(sdf.format(dat));
		itdochead.add(itdocheadtest);
		
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		int j=1;
		for(int i=0;i<itemList.size();i++) {
			if(!map.containsKey(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString())) {
				map.put(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString(), new BigDecimal(itemList.get(i).get("qty").toString()));					
			}else {
				map.put(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString(), new BigDecimal(map.get(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString()).toString()).add(new BigDecimal(itemList.get(i).get("qty").toString())));								
			}	
		}
		Map<String, Object> m = new HashMap<String, Object>();
		
		
		for(int i=0;i<itemList.size();i++) {			
			DTCWWMSMATDOCPOSTREQ.ITDOCITEM itdocitemtest=new DTCWWMSMATDOCPOSTREQ.ITDOCITEM();
			itdocitemtest.setDOCNUM(bizStockInputTransportHead.getStockTransportCode().toString());
			itdocitemtest.setDOCITEMNUM(i+"");
			itdocitemtest.setPLANT(itemList.get(i).get("fty_code").toString());
			itdocitemtest.setSTGELOC(itemList.get(i).get("location_code").toString());
			itdocitemtest.setMATERIAL(itemList.get(i).get("tran_mat_code").toString());
			itdocitemtest.setBATCH(itemList.get(i).get("tran_erp_batch").toString());
			itdocitemtest.setENTRYQNT(itemList.get(i).get("qty").toString());
			itdocitemtest.setMOVETYPE(dicMoveTypeDao.selectByPrimaryKey(Integer.valueOf(itemList.get(i).get("move_type_id").toString())).getMoveTypeCode());
			itdocitemtest.setMOVEPLANT(itemList.get(i).get("output_fty_code").toString());
			itdocitemtest.setMOVESTLOC(itemList.get(i).get("output_location_code").toString());
			itdocitemtest.setMOVEMAT(itemList.get(i).get("mat_code").toString());
			itdocitemtest.setMOVEBATCH(itemList.get(i).get("erp_batch").toString());
			if(map.containsKey(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString())) {
				if(!m.containsKey(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString())) {
					itdocitemtest.setDOCITEMNUM(j+"");
					itdocitemtest.setENTRYUOM("KG");
					BigDecimal qty = UtilObject.getBigDecimalOrZero(map.get(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString()).toString());
					if(UtilObject.getStringOrEmpty(itemList.get(i).get("unit_code")).equals("KG")) {
						qty = qty.multiply(new BigDecimal("1"));
					}else {
						qty = qty.multiply(new BigDecimal("1000"));
					}
					itdocitem.add(itdocitemtest);	
					j++;
					m.put(itemList.get(i).get("tran_mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString(), "1");
				}											
			}
		}		
		dtcwwmsmatdocpostreq.setItdocitem(itdocitem);
		dtcwwmsmatdocpostreq.setItdochead(itdochead);
		ErpReturn er=cwErpWsImpl.inputPost(dtcwwmsmatdocpostreq);
		return er;
	}
	
	
	
	
	
	
	
	/**
	 * mes同步
	 * @param head
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public WmIopRetVal takeMesAgain(BizStockInputTransportHead bizStockInputTransportHead,CurrentUser user) throws Exception{
		ArrayOfWmMvRecInteropeDto dtoSet = new ArrayOfWmMvRecInteropeDto();
		List<WmMvRecInteropeDto> wmMvRecInteropeDto = new ArrayList<WmMvRecInteropeDto>();
		List<Map<String, Object>> itemList = bizStockInputHeadDao.getViItemInfo(bizStockInputTransportHead.getStockTransportId());
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		int j=1;
		BigDecimal bdm=BigDecimal.ZERO;
		
		
		for(int i=0;i<itemList.size();i++) {
			Map<String, Object> map1=itemList.get(i);
			if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("location_input").toString())).getLocationCode().equals("6006")
					&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(map1.get("fty_id").toString())).getFtyCode().equals("SWX1")) {
				continue;
			}
			
			if(!map.containsKey(itemList.get(i).get("tran_mat_code").toString()+
					itemList.get(i).get("mat_code").toString()+
					itemList.get(i).get("output_location_code").toString()+
					itemList.get(i).get("location_code").toString()+
					itemList.get(i).get("tran_erp_batch").toString()+
					itemList.get(i).get("erp_batch").toString()+
					itemList.get(i).get("package_standard").toString()				
					)) {
				
					map.put(itemList.get(i).get("tran_mat_code").toString()+
							itemList.get(i).get("mat_code").toString()+
							itemList.get(i).get("output_location_code").toString()+
							itemList.get(i).get("location_code").toString()+
							itemList.get(i).get("tran_erp_batch").toString()+
							itemList.get(i).get("erp_batch").toString()+
							itemList.get(i).get("package_standard").toString(), new BigDecimal(itemList.get(i).get("qty").toString()));					
			}else {
				map.put(itemList.get(i).get("tran_mat_code").toString()+
						itemList.get(i).get("mat_code").toString()+
						itemList.get(i).get("output_location_code").toString()+
						itemList.get(i).get("location_code").toString()+
						itemList.get(i).get("tran_erp_batch").toString()+
						itemList.get(i).get("erp_batch").toString()+
						itemList.get(i).get("package_standard").toString(), 
						
						new BigDecimal(map.get(itemList.get(i).get("tran_mat_code").toString()+
								itemList.get(i).get("mat_code").toString()+
								itemList.get(i).get("output_location_code").toString()+
								itemList.get(i).get("location_code").toString()+
								itemList.get(i).get("tran_erp_batch").toString()+
								itemList.get(i).get("erp_batch").toString()+
								itemList.get(i).get("package_standard")).toString()).add(new BigDecimal(itemList.get(i).get("qty").toString())));								
			}	
		}
		Map<String, Object> m = new HashMap<String, Object>();
		
		
		for(int i=0;i<itemList.size();i++) {	
			Map<String, Object> map1=itemList.get(i);
			if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("location_input").toString())).getLocationCode().equals("6006")
					&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(map1.get("fty_id").toString())).getFtyCode().equals("SWX1")) {
				continue;
			}
			WmMvRecInteropeDto dto =  new WmMvRecInteropeDto();
			dto.setSrMtrlCode(map1.get("mat_code").toString());
			dto.setTgMtrlCode(map1.get("tran_mat_code").toString());
			Map<String,Object> matMap=iInputProduceTransportService.getMatInfoByCode(map1.get("tran_mat_code").toString());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("mes_unit_code", "吨");
			param.put("wms_unit_code", matMap.get("unit_code"));
			String rel=stockInputHeadDao.getUnitRelMesAndWms(param);
			dto.setWgtPerPack(Double.parseDouble(dicPackageTypeDao.selectByPrimaryKey(UtilObject.getIntegerOrNull(map1.get("package_type_id"))).getPackageStandard().toString()));
			dto.setAmnt(new BigDecimal(map1.get("qty").toString()).divide(new BigDecimal(map1.get("package_standard").toString()),0, RoundingMode.UP).intValue());
			dto.setWgt(new BigDecimal(map1.get("qty").toString()).divide(new BigDecimal(rel)).doubleValue());
			dto.setSrBch(map1.get("erp_batch").toString());
			dto.setTgBch(map1.get("tran_erp_batch").toString());
			dto.setSrRankCode(EnumRankLevel.getRankByValue(map1.get("erp_batch").toString()));
			dto.setTgRankCode(EnumRankLevel.getRankByValue(map1.get("tran_erp_batch").toString()));
			dto.setTgNodeCode(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("location_input").toString())).getNodeCode());

			dto.setCustomer("转储入库");
			dto.setTransTypeCode("1");
			dto.setDlvBillCode(bizStockInputTransportHead.getStockTransportCode());
			dto.setDes(bizStockInputTransportHead.getStockTransportCode());		
			dto.setWgtDim("TO");

			if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("location_output_id").toString())).getLocationCode().equals("6006")
					&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(map1.get("output_fty_id").toString())).getFtyCode().equals("SWX1")) {
				dto.setSrNodeCode("Z5SW042");
			}else {
				dto.setSrNodeCode(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("location_output_id").toString())).getNodeCode());
			}
			
			if(map.containsKey(itemList.get(i).get("tran_mat_code").toString()+
					itemList.get(i).get("mat_code").toString()+
					itemList.get(i).get("output_location_code").toString()+
					itemList.get(i).get("location_code").toString()+
					itemList.get(i).get("tran_erp_batch").toString()+
					itemList.get(i).get("erp_batch").toString()+
					itemList.get(i).get("package_standard").toString()				
					)) {
				if(!m.containsKey(itemList.get(i).get("tran_mat_code").toString()+
						itemList.get(i).get("mat_code").toString()+
						itemList.get(i).get("output_location_code").toString()+
						itemList.get(i).get("location_code").toString()+
						itemList.get(i).get("tran_erp_batch").toString()+
						itemList.get(i).get("erp_batch").toString()+
						itemList.get(i).get("package_standard").toString()				
						)) {
					dto.setRecNum(j);	
					dto.setAmnt(new BigDecimal((map.get(itemList.get(i).get("tran_mat_code").toString()+
							itemList.get(i).get("mat_code").toString()+
							itemList.get(i).get("output_location_code").toString()+
							itemList.get(i).get("location_code").toString()+
							itemList.get(i).get("tran_erp_batch").toString()+
							itemList.get(i).get("erp_batch").toString()+
							itemList.get(i).get("package_standard").toString()				
							).toString())).divide(new BigDecimal(map1.get("package_standard").toString()),0, RoundingMode.UP).intValue());				
					dto.setWgt(new BigDecimal((map.get(itemList.get(i).get("tran_mat_code").toString()+
							itemList.get(i).get("mat_code").toString()+
							itemList.get(i).get("output_location_code").toString()+
							itemList.get(i).get("location_code").toString()+
							itemList.get(i).get("tran_erp_batch").toString()+
							itemList.get(i).get("erp_batch").toString()+
							itemList.get(i).get("package_standard").toString()				
							).toString())).divide(new BigDecimal(rel)).doubleValue());
					wmMvRecInteropeDto.add(dto);	
					j++;
					m.put(itemList.get(i).get("tran_mat_code").toString()+
							itemList.get(i).get("mat_code").toString()+
							itemList.get(i).get("output_location_code").toString()+
							itemList.get(i).get("location_code").toString()+
							itemList.get(i).get("tran_erp_batch").toString()+
							itemList.get(i).get("erp_batch").toString()+
							itemList.get(i).get("package_standard").toString()				
							, "1");
				}											
			}				
		}
		dtoSet.setWmMvRecInteropeDto(wmMvRecInteropeDto);
		DateFormat format = new SimpleDateFormat("HH:mm:ss");  
		int classTypeId=commonService.selectNowClassType()==null?0:commonService.selectNowClassType();
		String beginTime=format.format(dicClassTypeMapper.selectByPrimaryKey(classTypeId).getStartTime()).toString();
		String endTime=format.format(dicClassTypeMapper.selectByPrimaryKey(classTypeId).getEndTime()).toString();
		WmIopRetVal val =null;
		String move_type_code=dicMoveTypeDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("move_type_id").toString())).getMoveTypeCode();
		if(move_type_code.equals("301") || move_type_code.equals("311")) {
			if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("location_output_id").toString())).getLocationCode().equals("6006") 
					&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("output_fty_id").toString())).getFtyCode().equals("SWX1") ) {
				val =cwMesWsImpl.updateMesStock(bizStockInputTransportHead.getStockTransportCode(), "101",beginTime ,endTime, dtoSet);				
			}else {
				val =cwMesWsImpl.updateMesStock(bizStockInputTransportHead.getStockTransportCode(), "301",beginTime ,endTime, dtoSet);
			}
		}
		
		if(move_type_code.equals("309") ) {
//			if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("location_output_id").toString())).getLocationCode().equals("6006")) {
//				val =cwMesWsImpl.updateMesStock(bizStockInputTransportHead.getStockTransportCode(), "301",beginTime ,endTime, dtoSet);
//			}else {
				val =cwMesWsImpl.updateMesStock(bizStockInputTransportHead.getStockTransportCode(), "1101",beginTime ,endTime, dtoSet);
//			}
		}
		
		if(val.getValue()==null) {
			BizFailMes mes = new BizFailMes();
			
			mes.setReferReceiptId(UtilObject.getIntOrZero(bizStockInputTransportHead.getStockTransportId()));
			mes.setReferReceiptCode(UtilObject.getStringOrEmpty(bizStockInputTransportHead.getStockTransportCode()));
			mes.setReferReceiptType(UtilObject.getByteOrNull(bizStockInputTransportHead.getReceiptType()));
			if(move_type_code.equals("301") || move_type_code.equals("311")) {
				if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("location_output_id").toString())).getLocationCode().equals("6006")) {
					mes.setBusinessType(UtilObject.getStringOrEmpty("301"));
				}else {
					mes.setBusinessType(UtilObject.getStringOrEmpty("101"));
				}
			}
			if(move_type_code.equals("309") ) {
				mes.setBusinessType(UtilObject.getStringOrEmpty("1101"));
			}
			mes.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
			mes.setCreateTime(new Date());
			mes.setModifyTime(new Date());
			mes.setCreateUser(user.getUserId());
			mes.setModifyUser(user.getUserId());
			mes.setErrorInfo(UtilObject.getStringOrEmpty(val.getErrmsg()));
			failMesDao.insert(mes);
		}else {
			List<Map<String,Object>> item=bizStockInputHeadDao.selectItemList(bizStockInputTransportHead.getStockTransportId());
			for(int i=0;i<item.size();i++) {
				BizStockInputTransportItem record=new BizStockInputTransportItem();
				record.setItemId(Integer.valueOf(item.get(i).get("item_id").toString()));
				record.setMesDocCode(val.getValue());
				bizStockInputTransportItemDao.updateByPrimaryKeySelective(record);
			}
		}
		return val;
	}
	
	
	public WmIopRetVal takeMes(BizStockInputTransportHead bizStockInputTransportHead, List<Map<String, Object>> itemList,CurrentUser user) throws Exception{
		ArrayOfWmMvRecInteropeDto dtoSet = new ArrayOfWmMvRecInteropeDto();
		List<WmMvRecInteropeDto> wmMvRecInteropeDto = new ArrayList<WmMvRecInteropeDto>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		int j=1;
		BigDecimal bdm=BigDecimal.ZERO;
		
		
		for(int i=0;i<itemList.size();i++) {
			
			Map<String, Object> map1=itemList.get(i);  
			if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("tran_location_id").toString())).getLocationCode().equals("6006") 
					&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(map1.get("tran_fty_id").toString())).getFtyCode().equals("SWX1")
					) {
				continue;
			}
			
			if(!map.containsKey(itemList.get(i).get("tran_mat_code").toString()+
					itemList.get(i).get("mat_code").toString()+
					itemList.get(i).get("output_location_code").toString()+
					itemList.get(i).get("location_code").toString()+
					itemList.get(i).get("tran_erp_batch").toString()+
					itemList.get(i).get("erp_batch").toString()+
					itemList.get(i).get("package_standard").toString()				
					)) {
				
					map.put(itemList.get(i).get("tran_mat_code").toString()+
							itemList.get(i).get("mat_code").toString()+
							itemList.get(i).get("output_location_code").toString()+
							itemList.get(i).get("location_code").toString()+
							itemList.get(i).get("tran_erp_batch").toString()+
							itemList.get(i).get("erp_batch").toString()+
							itemList.get(i).get("package_standard").toString(), new BigDecimal(itemList.get(i).get("qty").toString()));					
			}else {
				map.put(itemList.get(i).get("tran_mat_code").toString()+
						itemList.get(i).get("mat_code").toString()+
						itemList.get(i).get("output_location_code").toString()+
						itemList.get(i).get("location_code").toString()+
						itemList.get(i).get("tran_erp_batch").toString()+
						itemList.get(i).get("erp_batch").toString()+
						itemList.get(i).get("package_standard").toString(), 
						
						new BigDecimal(map.get(itemList.get(i).get("tran_mat_code").toString()+
								itemList.get(i).get("mat_code").toString()+
								itemList.get(i).get("output_location_code").toString()+
								itemList.get(i).get("location_code").toString()+
								itemList.get(i).get("tran_erp_batch").toString()+
								itemList.get(i).get("erp_batch").toString()+
								itemList.get(i).get("package_standard")).toString()).add(new BigDecimal(itemList.get(i).get("qty").toString())));								
			}	
		}
		Map<String, Object> m = new HashMap<String, Object>();
		
		
		for(int i=0;i<itemList.size();i++) {	
			
			Map<String, Object> map1=itemList.get(i);  
			if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("tran_location_id").toString())).getLocationCode().equals("6006") 
					&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(map1.get("tran_fty_id").toString())).getFtyCode().equals("SWX1") ) {
				continue;
			}
			WmMvRecInteropeDto dto =  new WmMvRecInteropeDto();
			dto.setSrMtrlCode(map1.get("mat_code").toString());
			dto.setTgMtrlCode(map1.get("tran_mat_code").toString());
			Map<String,Object> matMap=iInputProduceTransportService.getMatInfoByCode(map1.get("tran_mat_code").toString());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("mes_unit_code", "吨");
			param.put("wms_unit_code", matMap.get("unit_code"));
			String rel=stockInputHeadDao.getUnitRelMesAndWms(param);
			dto.setWgtPerPack(Double.parseDouble(dicPackageTypeDao.selectByPrimaryKey(UtilObject.getIntegerOrNull(map1.get("package_type_id"))).getPackageStandard().toString()));
			dto.setAmnt(new BigDecimal(map1.get("qty").toString()).divide(new BigDecimal(map1.get("package_standard").toString()),0, RoundingMode.UP).intValue());
			dto.setWgt(new BigDecimal(map1.get("qty").toString()).divide(new BigDecimal(rel)).doubleValue());
			dto.setSrBch(map1.get("erp_batch").toString());
			dto.setTgBch(map1.get("tran_erp_batch").toString());
			dto.setSrRankCode(EnumRankLevel.getRankByValue(map1.get("erp_batch").toString()));
			dto.setTgRankCode(EnumRankLevel.getRankByValue(map1.get("tran_erp_batch").toString()));
			dto.setTgNodeCode(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("tran_location_id").toString())).getNodeCode());

			dto.setCustomer("转储入库");
			dto.setTransTypeCode("1");
			dto.setDlvBillCode(bizStockInputTransportHead.getStockTransportCode());
			dto.setDes(bizStockInputTransportHead.getStockTransportCode());		
			dto.setWgtDim("TO");


			if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("location_output_id").toString())).getLocationCode().equals("6006")
					&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(map1.get("output_fty_id").toString())).getFtyCode().equals("SWX1")) {
				dto.setSrNodeCode("Z5SW042");
			}else {
				dto.setSrNodeCode(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("location_output_id").toString())).getNodeCode());
			}
			
			if(map.containsKey(itemList.get(i).get("tran_mat_code").toString()+
					itemList.get(i).get("mat_code").toString()+
					itemList.get(i).get("output_location_code").toString()+
					itemList.get(i).get("location_code").toString()+
					itemList.get(i).get("tran_erp_batch").toString()+
					itemList.get(i).get("erp_batch").toString()+
					itemList.get(i).get("package_standard").toString()				
					)) {
				if(!m.containsKey(itemList.get(i).get("tran_mat_code").toString()+
						itemList.get(i).get("mat_code").toString()+
						itemList.get(i).get("output_location_code").toString()+
						itemList.get(i).get("location_code").toString()+
						itemList.get(i).get("tran_erp_batch").toString()+
						itemList.get(i).get("erp_batch").toString()+
						itemList.get(i).get("package_standard").toString()				
						)) {
					dto.setRecNum(j);	
					dto.setAmnt(new BigDecimal((map.get(itemList.get(i).get("tran_mat_code").toString()+
							itemList.get(i).get("mat_code").toString()+
							itemList.get(i).get("output_location_code").toString()+
							itemList.get(i).get("location_code").toString()+
							itemList.get(i).get("tran_erp_batch").toString()+
							itemList.get(i).get("erp_batch").toString()+
							itemList.get(i).get("package_standard").toString()				
							).toString())).divide(new BigDecimal(map1.get("package_standard").toString()),0, RoundingMode.UP).intValue());				
					dto.setWgt(new BigDecimal((map.get(itemList.get(i).get("tran_mat_code").toString()+
							itemList.get(i).get("mat_code").toString()+
							itemList.get(i).get("output_location_code").toString()+
							itemList.get(i).get("location_code").toString()+
							itemList.get(i).get("tran_erp_batch").toString()+
							itemList.get(i).get("erp_batch").toString()+
							itemList.get(i).get("package_standard").toString()				
							).toString())).divide(new BigDecimal(rel)).doubleValue());
					wmMvRecInteropeDto.add(dto);	
					j++;
					m.put(itemList.get(i).get("tran_mat_code").toString()+
							itemList.get(i).get("mat_code").toString()+
							itemList.get(i).get("output_location_code").toString()+
							itemList.get(i).get("location_code").toString()+
							itemList.get(i).get("tran_erp_batch").toString()+
							itemList.get(i).get("erp_batch").toString()+
							itemList.get(i).get("package_standard").toString()				
							, "1");
				}											
			}				
		}
		
		
		dtoSet.setWmMvRecInteropeDto(wmMvRecInteropeDto);
		DateFormat format = new SimpleDateFormat("HH:mm:ss");  
		String beginTime=format.format(dicClassTypeMapper.selectByPrimaryKey(Integer.valueOf(bizStockInputTransportHead.getClassTypeId().toString())).getStartTime()).toString();
		String endTime=format.format(dicClassTypeMapper.selectByPrimaryKey(Integer.valueOf(bizStockInputTransportHead.getClassTypeId().toString())).getEndTime()).toString();
		
		WmIopRetVal val =new WmIopRetVal();
		String move_type_code=dicMoveTypeDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("move_type_id").toString())).getMoveTypeCode();
		if(move_type_code.equals("301") || move_type_code.equals("311")) {
			if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("output_location_id").toString())).getLocationCode().equals("6006") 
					&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("output_fty_id").toString())).getFtyCode().equals("SWX1") ) {
				val =cwMesWsImpl.updateMesStock(bizStockInputTransportHead.getStockTransportCode(), "101",beginTime ,endTime, dtoSet);				
			}else {
				val =cwMesWsImpl.updateMesStock(bizStockInputTransportHead.getStockTransportCode(), "301",beginTime ,endTime, dtoSet);
			}
		}
		
		if(move_type_code.equals("309") ) {
//			if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("output_location_id").toString())).getLocationCode().equals("6006")) {
//				val =cwMesWsImpl.updateMesStock(bizStockInputTransportHead.getStockTransportCode(), "301",beginTime ,endTime, dtoSet);
//			}else {
				val =cwMesWsImpl.updateMesStock(bizStockInputTransportHead.getStockTransportCode(), "1101",beginTime ,endTime, dtoSet);
//			}
		}
		
		
		if(val.getValue()==null) {
			BizFailMes mes = new BizFailMes();
			
			mes.setReferReceiptId(UtilObject.getIntOrZero(bizStockInputTransportHead.getStockTransportId()));
			mes.setReferReceiptCode(UtilObject.getStringOrEmpty(bizStockInputTransportHead.getStockTransportCode()));
			mes.setReferReceiptType(UtilObject.getByteOrNull(bizStockInputTransportHead.getReceiptType()));
			if(move_type_code.equals("301") || move_type_code.equals("311")) {
				if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("location_output_id").toString())).getLocationCode().equals("6006")) {
					mes.setBusinessType(UtilObject.getStringOrEmpty("301"));
				}else {
					mes.setBusinessType(UtilObject.getStringOrEmpty("101"));
				}
			}
			if(move_type_code.equals("309") ) {
				mes.setBusinessType(UtilObject.getStringOrEmpty("1101"));
			}
			mes.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
			mes.setCreateTime(new Date());
			mes.setModifyTime(new Date());
			mes.setCreateUser(user.getUserId());
			mes.setModifyUser(user.getUserId());
			mes.setErrorInfo(UtilObject.getStringOrEmpty(val.getErrmsg()));
			failMesDao.insert(mes);
		}else {
			List<Map<String,Object>> item=bizStockInputHeadDao.selectItemList(bizStockInputTransportHead.getStockTransportId());
			for(int i=0;i<item.size();i++) {
				BizStockInputTransportItem record=new BizStockInputTransportItem();
				record.setItemId(Integer.valueOf(item.get(i).get("item_id").toString()));
				record.setMesDocCode(val.getValue());
				bizStockInputTransportItemDao.updateByPrimaryKeySelective(record);
			}
		}
		return val;
	}
	
	private String getBeginDate(String classTypeName) {
		return classTypeName.replace("(", "").replace(")", "").split("-")[0];
	}
	
	private String getEndDate(String classTypeName) {
		return classTypeName.replace("(", "").replace(")", "").split("-")[1];
	}


	@Override
	public void setHistory(BizStockInputTransportHead bizStockInputTransportHead, CurrentUser user, JSONObject json,List<Map<String,Object>> itemList) {
		
		
		for(int i=0;i<itemList.size();i++) {
			BizBusinessHistory record=new BizBusinessHistory();
			
			if(!itemList.get(i).get("refer_receipt_type").toString().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue().toString())) {
				
				int whId=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(i).get("output_location_id").toString())).getWhId();
				// 上架 组盘上架
				String whCode = dictionaryService.getWhCodeByWhId(whId);
				Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
						UtilProperties.getInstance().getDefaultCCQ());
				Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
						UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
				record.setAreaId(sourceAreaId);
				record.setBatch(new Long(itemList.get(i).get("batch").toString()));
				record.setBusinessType((byte) 1);
				record.setCreateTime(new Date());
				record.setCreateUser(user.getUserId());
				record.setDebitCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				record.setFtyId(Integer.valueOf(itemList.get(i).get("output_fty_id").toString()));
				record.setLocationId(Integer.valueOf(itemList.get(i).get("output_location_id").toString()));
				record.setMatId(Integer.valueOf(itemList.get(i).get("mat_id").toString()));
				record.setModifyTime(new Date());
				record.setModifyUser(user.getUserId());
				record.setQty(new BigDecimal(itemList.get(i).get("qty").toString()));
				record.setPackageId(0);
				record.setPalletId(0);
				record.setPositionId(sourcePositionId);
				record.setReceiptType(bizStockInputTransportHead.getReceiptType());
				record.setReferReceiptCode(bizStockInputTransportHead.getStockTransportCode());
				record.setReferReceiptId(bizStockInputTransportHead.getStockTransportId());
				record.setReferReceiptPid(whId);
				record.setReferReceiptRid(i+1);
				record.setSynMes(true);
				record.setSynSap(true);
				bizBusinessHistoryDao.insertSelective(record);	
			}
			
			
			int whIdin=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(i).get("tran_location_id").toString())).getWhId();
			
			// 上架 组盘上架
			String whCodein = dictionaryService.getWhCodeByWhId(whIdin);
			Integer sourceAreaIdin = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCodein,
					UtilProperties.getInstance().getDefaultCCQ());
			Integer sourcePositionIdin = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCodein,
					UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
			record.setAreaId(sourceAreaIdin);
			record.setBatch(new Long(itemList.get(i).get("tran_batch").toString()));
			record.setBusinessType((byte) 1);
			record.setCreateTime(new Date());
			record.setCreateUser(user.getUserId());
			record.setDebitCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
			record.setFtyId(Integer.valueOf(itemList.get(i).get("tran_fty_id").toString()));
			record.setLocationId(Integer.valueOf(itemList.get(i).get("tran_location_id").toString()));
			record.setMatId(Integer.valueOf(itemList.get(i).get("tran_mat_id").toString()));
			record.setModifyTime(new Date());
			record.setModifyUser(user.getUserId());
			record.setQty(new BigDecimal(itemList.get(i).get("qty").toString()));
			record.setPackageId(0);
			record.setPalletId(0);
			record.setPositionId(sourcePositionIdin);
			record.setReceiptType(bizStockInputTransportHead.getReceiptType());
			record.setReferReceiptCode(bizStockInputTransportHead.getStockTransportCode());
			record.setReferReceiptId(bizStockInputTransportHead.getStockTransportId());
			record.setReferReceiptPid(whIdin);
			record.setReferReceiptRid(i+1);
			record.setSynMes(true);
			record.setSynSap(true);
			bizBusinessHistoryDao.insertSelective(record);
		}
		
	}


	@Override
	public BizStockInputTransportHead selectEntity(BizStockInputTransportHead bizStockInputTransportHead) {
		return bizStockInputHeadDao.selectByPrimaryKey(bizStockInputTransportHead.getStockTransportId());
	}


	@Override
	public void updateMatDocCode(ErpReturn er, BizStockInputTransportHead bizStockInputTransportHead) {
		List<Map<String,Object>> itemList=bizStockInputHeadDao.selectItemList(bizStockInputTransportHead.getStockTransportId());
		for(int i=0;i<itemList.size();i++) {
			BizStockInputTransportItem record=new BizStockInputTransportItem();
			record.setItemId(Integer.valueOf(itemList.get(i).get("item_id").toString()));
			record.setMatDocCode(er.getMatDocCode());	
			record.setMatDocRid(i+1);
			record.setMatDocYear(er.getDocyear());
			bizStockInputTransportItemDao.updateByPrimaryKeySelective(record);
		}		
	}
	
	@Override
	public void updateMesMatDocCode(WmIopRetVal er, BizStockInputTransportHead bizStockInputTransportHead) {
		List<Map<String,Object>> itemList=bizStockInputHeadDao.selectItemList(bizStockInputTransportHead.getStockTransportId());
		for(int i=0;i<itemList.size();i++) {
			BizStockInputTransportItem record=new BizStockInputTransportItem();
			record.setItemId(Integer.valueOf(itemList.get(i).get("item_id").toString()));
			record.setMesDocCode(er.getValue());
			bizStockInputTransportItemDao.updateByPrimaryKeySelective(record);
		}		
	}


	@Override
	public void delete(String stock_input_id) {
		BizStockInputTransportHead record=new BizStockInputTransportHead();
		record.setStockTransportId(Integer.valueOf(stock_input_id));
		record.setIsDelete((byte) 1);
		bizStockInputHeadDao.updateByPrimaryKeySelective(record);	
		
		List<Map<String,Object>> list=bizStockInputHeadDao.selectItemIdAndRid(Integer.valueOf(stock_input_id));
		for(int i=0;i<list.size();i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", list.get(i).get("refer_receipt_id"));
			map.put("rid", list.get(i).get("refer_receipt_rid"));
			bizStockInputHeadDao.setBeforeStatus(map);
		}
	}


	@Override
	public List<Map<String, Object>> selectItemList(Integer stock_transport_id) {
		
		return bizStockInputHeadDao.selectItemListRefer(stock_transport_id);
	}


	@Override
	public List<Map<String, Object>> getMatListByStockID(List<Map<String, Object>> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String>  list1=new ArrayList<String>();
		for(int i=0;i<list.size();i++) {
			JSONObject jsonObject = JSONObject.fromObject(list.get(i));	
			list1.add(jsonObject.get("item_id").toString());
		}
		map.put("myJsonArray", list1);
		return bizStockInputHeadDao.getMatListByID(map);
	}

	@Override
	public List<Map<String, Object>> getViItemInfo(Integer stock_input_id) {
		
		return bizStockInputHeadDao.getViItemInfo(stock_input_id);
	}


	@Override
	public void dealBeforeStatus(String stock_input_id) {
		List<Map<String,Object>> list=bizStockInputHeadDao.selectItemIdAndRid(Integer.valueOf(stock_input_id));
		for(int i=0;i<list.size();i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", list.get(i).get("refer_receipt_id"));
			map.put("rid", list.get(i).get("refer_receipt_rid"));
			bizStockInputHeadDao.setBeforeStatus(map);
		}
		
	}


	@Override
	public String takeMesFail(BizStockInputTransportHead bizStockInputTransportHead, List<Map<String, Object>> itemList,
			CurrentUser cuser) throws Exception {
		String result ="";
		List<Map<String, Object>> list=bizStockInputHeadDao.getViItemInfo(bizStockInputTransportHead.getStockTransportId());
		if(bizStockInputTransportItemDao.selectByPrimaryKey(Integer.valueOf(list.get(0).get("item_id").toString())).getMesDocCode()!=null) {
			result = cwMesWsImpl.CancelBookedBillByBillCode(bizStockInputTransportItemDao.selectByPrimaryKey(Integer.valueOf(list.get(0).get("item_id").toString())).getMesDocCode());
			if(!"".equals(result) && result!=null) {
				for(int i=0;i< itemList.size(); i++){
					BizStockInputTransportItem record=new BizStockInputTransportItem();
					record.setItemId(Integer.valueOf(list.get(i).get("item_id").toString()));
					record.setMesFailCode(result);
					bizStockInputTransportItemDao.updateByPrimaryKeySelective(record);
				}			
			}else {
				BizFailMes mes = new BizFailMes();
				mes.setReferReceiptId(UtilObject.getIntOrZero(bizStockInputTransportHead.getStockTransportId()));			
				mes.setReferReceiptCode(UtilObject.getStringOrEmpty(bizStockInputTransportHead.getStockTransportCode()));
				mes.setReferReceiptType(UtilObject.getByteOrNull(bizStockInputTransportHead.getReceiptType()));
				mes.setBusinessType(UtilObject.getStringOrEmpty("301"));
				mes.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
				mes.setCreateTime(new Date());
				mes.setModifyTime(new Date());
				mes.setCreateUser(cuser.getUserId());
				mes.setModifyUser(cuser.getUserId());
				mes.setErrorInfo(UtilObject.getStringOrEmpty(result));
				failMesDao.insert(mes);	
			}
			
		}
		
		return result;
	}


	@Override
	public String takeMesFailAgain(BizStockInputTransportHead bizStockInputTransportHead, CurrentUser user)
			throws Exception {
		
		String result ="";
		List<Map<String, Object>> list=bizStockInputHeadDao.getViItemInfo(bizStockInputTransportHead.getStockTransportId());
		if(bizStockInputTransportItemDao.selectByPrimaryKey(Integer.valueOf(list.get(0).get("item_id").toString())).getMesDocCode()!=null) {
			if(!"".equals(result) && result!=null) {
				for(int i=0;i< list.size(); i++){
					BizStockInputTransportItem record=new BizStockInputTransportItem();
					record.setItemId(Integer.valueOf(list.get(i).get("item_id").toString()));
					record.setMesFailCode(result);
					bizStockInputTransportItemDao.updateByPrimaryKeySelective(record);
				}			
			}else {
				BizFailMes mes = new BizFailMes();
				mes.setReferReceiptId(UtilObject.getIntOrZero(bizStockInputTransportHead.getStockTransportId()));			
				mes.setReferReceiptCode(UtilObject.getStringOrEmpty(bizStockInputTransportHead.getStockTransportCode()));
				mes.setReferReceiptType(UtilObject.getByteOrNull(bizStockInputTransportHead.getReceiptType()));
				mes.setBusinessType(UtilObject.getStringOrEmpty("301"));
				mes.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
				mes.setCreateTime(new Date());
				mes.setModifyTime(new Date());
				mes.setCreateUser(user.getUserId());
				mes.setModifyUser(user.getUserId());
				mes.setErrorInfo(UtilObject.getStringOrEmpty(result));
				failMesDao.insert(mes);	
			}
		}
		return result;
	}


	@Override
	public String selectIsWriteOff(Integer valueOf) {
		String flag="0";
		List<Map<String, Object>> list=bizStockInputHeadDao.getViItemInfo(valueOf);
		for(Map<String,Object> map:list) {
			BigDecimal qty=new BigDecimal(UtilObject.getBigDecimalOrZero(map.get("qty")).toString());
			Map<String, Object> param = new HashMap<String,Object>();
			param.put("batch", UtilObject.getStringOrEmpty(map.get("batch")));
			param.put("location_id", UtilObject.getStringOrEmpty(map.get("location_input")));
			param.put("mat_id", UtilObject.getStringOrEmpty(map.get("mat_input")));	
			param.put("stock_type_id",EnumStockType.STOCK_TYPE_ERP.getValue());
			String batchQty=bizInputProduceHeadDao.selectbatchMaterial(param);			
			if(batchQty!=null) {
				if(new BigDecimal(batchQty).compareTo(qty)==-1) {
					flag="1";
					break;
				}
			}else {
				flag="1";
				break;
			}
		}
		return flag;
	}
	
	
	
}
