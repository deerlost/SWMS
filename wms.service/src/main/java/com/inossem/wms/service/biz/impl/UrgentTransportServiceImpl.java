package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.batch.BatchMaterialMapper;
import com.inossem.wms.dao.biz.BizBusinessHistoryMapper;
import com.inossem.wms.dao.biz.BizStockInputHeadMapper;
import com.inossem.wms.dao.biz.BizStockInputItemMapper;
import com.inossem.wms.dao.biz.BizStockInputPackageItemMapper;
import com.inossem.wms.dao.biz.BizStockInputTransportHeadMapper;
import com.inossem.wms.dao.biz.BizStockInputTransportItemMapper;
import com.inossem.wms.dao.biz.BizStockTaskItemCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskPositionCwMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicMaterialMapper;
import com.inossem.wms.dao.dic.DicMoveTypeMapper;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.dic.DicUnitMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizBusinessHistory;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputTransportHead;
import com.inossem.wms.model.biz.BizStockInputTransportItem;
import com.inossem.wms.model.biz.ErpReturn;
import com.inossem.wms.model.enums.EnumDebitCredit;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumStockType;
import com.inossem.wms.model.enums.EnumSynType;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IUrgentTransportService;
import com.inossem.wms.service.intfc.sap.CwErpWsImpl;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.wsdl.sap.input.DTCWWMSMATDOCPOSTREQ;
import com.inossem.wms.wsdl.sap.input.MSGHEAD;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Transactional
@Service
public class UrgentTransportServiceImpl implements IUrgentTransportService{
	
	

	@Resource
	BizStockInputHeadMapper stockinputHeadDao;

	@Resource
	BizStockInputItemMapper stockinputItemDao;

	@Resource
	BizStockInputPackageItemMapper stockPackDao;
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private BizStockInputTransportHeadMapper bizStockInputHeadDao;
	
	@Autowired
	private BizStockInputTransportItemMapper bizStockInputTransportItemDao;
	
	@Autowired
	private DicUnitMapper unitDao;
	
	@Autowired
	private CwErpWsImpl cwErpWsImpl;
	
	@Autowired
	private DicMaterialMapper dicMaterialDao;
	
	@Autowired
	private DicMoveTypeMapper dicMoveTypeDao;
	
	@Autowired
	private DicFactoryMapper dicFactoryDao;
	
	
	@Autowired
	private ICommonService commonService;
	@Autowired
	private DicStockLocationMapper dicLocationDao;
	@Autowired
	private DicStockLocationMapper dicStockLocationDao;
	
	@Autowired
	private BatchMaterialMapper batchMaterialDao;
	
	@Autowired
	private BizStockTaskItemCwMapper stockTaskItemCwDao;
	
	@Autowired
	private BizStockTaskPositionCwMapper bizStockTaskPositionCwDao;
	
	@Autowired
	private BizBusinessHistoryMapper bizBusinessHistoryDao;

	@Resource
	BizStockInputTransportHeadMapper stockinputTranHeadDao;

	@Resource
	BizStockInputTransportItemMapper stockinputTranItemDao;
	
	
	public JSONArray getSynType() {
		// TODO Auto-generated method stub
		JSONArray myJsonArray = new JSONArray();

		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("syn_type_name", "同步ERP");
		jsonObject1.put("syn_type", EnumSynType.SAP_SYN.getValue());
		myJsonArray.add(jsonObject1);

		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("syn_type_name", "同步ERP&MES");
		jsonObject2.put("syn_type", EnumSynType.MES_SAP_SYN.getValue());
		myJsonArray.add(jsonObject2);

		return myJsonArray;
	}


	@Override
	public List<Map<String, Object>> getMoveList() {
		// TODO Auto-generated method stub
		return stockinputTranHeadDao.selectMoveType();
	}
	
	
	
	
	
	@Override
	public void delete(String stock_input_id) {
		BizStockInputTransportHead record=new BizStockInputTransportHead();
		record.setStockTransportId(Integer.valueOf(stock_input_id));
		record.setIsDelete((byte) 1);
		bizStockInputHeadDao.updateByPrimaryKeySelective(record);	
	}
	
	@Override
	public ErpReturn takeSap(BizStockInputTransportHead transHead, JSONObject json,
			JSONArray itemList) throws Exception {
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
		itdocheadtest.setDOCNUM(transHead.getStockTransportCode().toString());
		itdocheadtest.setGOODSMVTCODE("04");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		java.util.Date dat=new java.util.Date();  							
		itdocheadtest.setPSTNGDATE(sdf.format(dat));
		itdocheadtest.setDOCDATE(sdf.format(dat));
		itdochead.add(itdocheadtest);
		
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		int j=1;
		String loc_output_code=dicLocationDao.selectByPrimaryKey(Integer.valueOf(json.get("loc_output_id").toString())).getLocationCode();
		String loc_input_code=dicLocationDao.selectByPrimaryKey(Integer.valueOf(json.get("loc_input_id").toString())).getLocationCode();
		
		for(int i=0;i<itemList.size();i++) {
			JSONObject js = itemList.getJSONObject(i);
			String mat_code=dicMaterialDao.selectByPrimaryKey(Integer.valueOf(js.get("mat_id").toString())).getMatCode();
			if(!map.containsKey(mat_code+loc_output_code+loc_input_code+js.get("erp_batch").toString())) {
				map.put(mat_code+loc_output_code+loc_input_code+js.get("erp_batch").toString(), new BigDecimal(js.get("transport_qty").toString()));					
			}else {
				map.put(mat_code+loc_output_code+loc_input_code+js.get("erp_batch").toString(), new BigDecimal(map.get(mat_code+loc_output_code+loc_input_code+js.get("erp_batch").toString()).toString()).add(new BigDecimal(js.get("transport_qty").toString())));								
			}	
		}
		Map<String, Object> m = new HashMap<String, Object>();
		
		
		
		for(int i=0;i<itemList.size();i++) {
			DTCWWMSMATDOCPOSTREQ.ITDOCITEM itdocitemtest=new DTCWWMSMATDOCPOSTREQ.ITDOCITEM();
			itdocitemtest.setDOCNUM(transHead.getStockTransportCode().toString());
			itdocitemtest.setDOCITEMNUM(i+"");
			JSONObject js = itemList.getJSONObject(i);
			String mat_code=dicMaterialDao.selectByPrimaryKey(Integer.valueOf(js.get("mat_id").toString())).getMatCode();
			itdocitemtest.setPLANT(dicFactoryDao.selectByPrimaryKey(Integer.valueOf(json.get("fty_output_id").toString())).getFtyCode());
			itdocitemtest.setSTGELOC(dicLocationDao.selectByPrimaryKey(Integer.valueOf(json.get("loc_output_id").toString())).getLocationCode()); 
			itdocitemtest.setMATERIAL(dicMaterialDao.selectByPrimaryKey(Integer.valueOf(js.get("mat_id").toString())).getMatCode());
			itdocitemtest.setBATCH(js.get("erp_batch").toString());
			itdocitemtest.setENTRYQNT(js.get("transport_qty").toString());
		
			itdocitemtest.setMOVETYPE(dicMoveTypeDao.selectByPrimaryKey(Integer.valueOf(json.get("move_type_id").toString())).getMoveTypeCode());
			itdocitemtest.setMOVEPLANT(dicFactoryDao.selectByPrimaryKey(Integer.valueOf(json.get("fty_input_id").toString())).getFtyCode());			
			itdocitemtest.setMOVESTLOC(dicLocationDao.selectByPrimaryKey(Integer.valueOf(json.get("loc_input_id").toString())).getLocationCode());
			itdocitemtest.setMOVEMAT(dicMaterialDao.selectByPrimaryKey(Integer.valueOf(js.get("mat_id").toString())).getMatCode());
			itdocitemtest.setMOVEBATCH(js.get("erp_batch").toString());
			itdocitemtest.setENTRYUOM("KG");
			if(map.containsKey(mat_code+loc_output_code+loc_input_code+js.get("erp_batch").toString())) {
				if(!m.containsKey(mat_code+loc_output_code+loc_input_code+js.get("erp_batch").toString())) {
					itdocitemtest.setDOCITEMNUM(j+"");
					
					BigDecimal qty = UtilObject.getBigDecimalOrZero(map.get(mat_code+loc_output_code+loc_input_code+js.get("erp_batch").toString()).toString());
					String unitCode = unitDao.selectByPrimaryKey(UtilObject.getIntegerOrNull(json.get("unit_id"))).getUnitCode();
					if(unitCode.equals("KG")) {
						qty = qty.multiply(new BigDecimal("1"));
					}else {
						qty = qty.multiply(new BigDecimal("1000"));
					}
					itdocitemtest.setENTRYQNT(qty.toString());
					itdocitem.add(itdocitemtest);	
					j++;
					m.put(mat_code+loc_output_code+loc_input_code+js.get("erp_batch").toString(), "1");
				}											
			}
		}		
		dtcwwmsmatdocpostreq.setItdocitem(itdocitem);
		dtcwwmsmatdocpostreq.setItdochead(itdochead);
		ErpReturn er=cwErpWsImpl.inputPost(dtcwwmsmatdocpostreq);
		return er;
	}
	
	
	
	@Override
	public void updateStockToWriteOff(Integer stock_input_id,BizStockInputTransportHead head,
			List<Map<String, Object>> l,CurrentUser cuser) throws Exception {
		BizStockInputTransportHead bizStockInputTransportHead=new BizStockInputTransportHead();
		bizStockInputTransportHead.setStockTransportId(stock_input_id);
		bizStockInputTransportHead.setStatus((byte) 20);
		bizStockInputHeadDao.updateByPrimaryKeySelective(bizStockInputTransportHead);
		
		BizStockInputTransportHead bizStock=bizStockInputHeadDao.selectByPrimaryKey(stock_input_id);
		
		List<Map<String,Object>> itemList=bizStockInputHeadDao.selectItemList(stock_input_id);
		
		
//		String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());
		//保存作业请求头部信息
		
		
		
		Map<String,Object> locationIdMap = new HashMap<String,Object>();
		List<Map<String,Object>> list1=bizStockInputHeadDao.getViItemInfo(stock_input_id);
//		BizStockTaskReqHead taskReqHead = new BizStockTaskReqHead();
		for(int i=0;i<list1.size();i++) {
			
			
			
			
//			taskReqHead.setStockTaskReqCode(stockTaskReqCode);
//			taskReqHead.setMatDocId(0);
//			taskReqHead.setMatDocYear(0);
//			taskReqHead.setShippingType(EnumTaskReqShippingType.STOCK_OUTPUT.getValue());
//			taskReqHead.setStatus(EnumBoolean.FALSE.getValue());
//			taskReqHead.setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue());
//			taskReqHead.setReferReceiptId(bizStock.getStockTransportId());
//			taskReqHead.setReferReceiptCode(bizStock.getStockTransportCode().toString());
//			taskReqHead.setReferReceiptType(bizStock.getReceiptType());
//			if(bizStock.getReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_FACTORY.getValue())) {
//				taskReqHead.setReferReceiptType(EnumReceiptType.STOCK_TRANSPORT_FACTORY_WRITEOFF.getValue());
//			}
//			if(bizStock.getReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_STOCK.getValue())) {
//				taskReqHead.setReferReceiptType(EnumReceiptType.STOCK_TRANSPORT_STOCK_WRITEOFF.getValue());
//			}
//			if(bizStock.getReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue())) {
//				taskReqHead.setReferReceiptType(EnumReceiptType.STOCK_TRANSPORT_MATERIAL_WRITEOFF.getValue());
//			}
			
			
			
//			taskReqHead.setCreateUser(cuser.getUserId());
//			taskReqHead.setCreateTime(new Date());
//			taskReqHead.setModifyTime(new Date());
//			
//
			Map<String,Object> item = list1.get(i);
//			
//			
//			taskReqHead.setLocationId(UtilObject.getIntegerOrNull(item.get("location_input")));
//			int wh=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(item.get("location_input").toString())).getWhId();
//			taskReqHead.setFtyId(UtilObject.getIntegerOrNull(item.get("fty_input")));
//			taskReqHead.setWhId(wh);
//			if(!locationIdMap.containsKey(item.get("location_input").toString())) {
//				taskReqHead.setStockTaskReqId(null);
//				StockTaskReqHeadDao.insertSelective(taskReqHead);
//				locationIdMap.put(item.get("location_input").toString(), taskReqHead.getStockTaskReqId());
//			}
			
			
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
			
//			BizStockTaskReqItem taskReqItem = new BizStockTaskReqItem();
//			taskReqItem.setFtyId(UtilObject.getIntOrZero(item.get("fty_input")));
//			taskReqItem.setLocationId(UtilObject.getIntOrZero(item.get("location_input")));
//			taskReqItem.setMatId(UtilObject.getIntegerOrNull(item.get("mat_input")));
//			taskReqItem.setStockTaskQty(BigDecimal.ZERO);
//			taskReqItem.setQty(UtilObject.getBigDecimalOrZero(item.get("qty")));
//			taskReqItem.setBatch(UtilObject.getLongOrNull(item.get("tran_batch")));
//			taskReqItem.setErpBatch(UtilObject.getStringOrEmpty(item.get("erp_batch")));
//			taskReqItem.setProductionBatch(UtilObject.getStringOrEmpty(item.get("production_batch")));
//			taskReqItem.setQualityBatch(UtilObject.getStringOrEmpty(item.get("production_batch")));
//			taskReqItem.setUnitId(UtilObject.getIntOrZero(item.get("unit_id")));
//			taskReqItem.setPackageTypeId(UtilObject.getIntOrZero(item.get("package_type_id")));
//			taskReqItem.setReferReceiptType(bizStock.getReceiptType());
			
			
//			if(bizStock.getReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_FACTORY.getValue())) {
//				taskReqItem.setReferReceiptType(EnumReceiptType.STOCK_TRANSPORT_FACTORY_WRITEOFF.getValue());
//			}
//			if(bizStock.getReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_STOCK.getValue())) {
//				taskReqItem.setReferReceiptType(EnumReceiptType.STOCK_TRANSPORT_STOCK_WRITEOFF.getValue());
//			}
//			if(bizStock.getReceiptType().equals(EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue())) {
//				taskReqItem.setReferReceiptType(EnumReceiptType.STOCK_TRANSPORT_MATERIAL_WRITEOFF.getValue());
//			}
			
//			taskReqItem.setReferReceiptId(bizStock.getStockTransportId());
//			taskReqItem.setReferReceiptCode(bizStock.getStockTransportCode());
//			taskReqItem.setReferReceiptRid(UtilObject.getIntOrZero(item.get("stock_transport_rid")));
//			taskReqItem.setStockTaskReqId(taskReqHead.getStockTaskReqId());
//			taskReqItem.setStockTaskReqRid(UtilObject.getIntOrZero(i+1));
//			taskReqItem.setIsDelete(EnumBoolean.FALSE.getValue());
//			taskReqItem.setStatus(EnumBoolean.FALSE.getValue());
//			taskReqItem.setCreateTime(new Date());
//			taskReqItem.setModifyTime(new Date());
//			taskReqItem.setWhId(wh);
//			StockTaskReqItemDao.insertSelective(taskReqItem);
			
			
			

			
			
			
			
			Map<String, Object> map=new HashMap<String,Object>();
			//发出

			map.put("matId", list1.get(i).get("mat_id"));
			map.put("locationId", list1.get(i).get("location_output_id"));
			map.put("batch", list1.get(i).get("batch"));
			map.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			map.put("createUserId", cuser.getUserId());
			map.put("debitCredit",EnumDebitCredit.DEBIT_S_ADD.getName());
			map.put("qty", list1.get(i).get("qty"));
			map.put("stockTypeId",EnumStockType.STOCK_TYPE_ACCOUNT.getValue());
			commonService.modifyStockBatch(map);			
		
			//接收
			map.put("matId", list1.get(i).get("mat_id"));
			map.put("locationId", list1.get(i).get("location_input"));
			map.put("batch", list1.get(i).get("batch"));
			map.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			map.put("createUserId", cuser.getUserId());
			map.put("debitCredit",EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			map.put("qty", list1.get(i).get("qty"));
			map.put("stockTypeId",EnumStockType.STOCK_TYPE_ACCOUNT.getValue());
			commonService.modifyStockBatch(map);
			
			
			
			StockPosition stockPosition=new StockPosition();
			int whId=dicLocationDao.selectByPrimaryKey(Integer.valueOf(list1.get(i).get("location_input").toString())).getWhId();				
			stockPosition.setWhId(whId);
			// 上架 组盘上架
			String whCode = dictionaryService.getWhCodeByWhId(whId);
			Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
					UtilProperties.getInstance().getDefaultCCQ());
			Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
					UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());

			stockPosition.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			stockPosition.setAreaId(sourceAreaId);
			stockPosition.setPositionId(sourcePositionId);
			stockPosition.setMatId(Integer.valueOf(list1.get(i).get("mat_id").toString()));
			stockPosition.setFtyId(Integer.valueOf(list1.get(i).get("fty_id").toString()));
			stockPosition.setLocationId(Integer.valueOf(list1.get(i).get("location_input").toString()));
			stockPosition.setBatch(new Long(list1.get(i).get("batch").toString()));
			stockPosition.setPackageTypeId(Integer.valueOf(list1.get(i).get("package_type_id").toString()));
			stockPosition.setInputDate(new Date());
			stockPosition.setQty(new BigDecimal(list1.get(i).get("qty").toString()));
			stockPosition.setUnitId(Integer.valueOf(list1.get(i).get("unit_id").toString()));
			stockPosition.setInputDate(new Date());
			stockPosition.setPackageId(0);
			stockPosition.setPalletId(0);
			stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			stockPosition.setStockTypeId(EnumStockType.STOCK_TYPE_ACCOUNT.getValue());
			commonService.modifyStockPosition(stockPosition);	
			
			
			StockPosition stockPositionOut=new StockPosition();
			int whIdOut=dicLocationDao.selectByPrimaryKey(Integer.valueOf(list1.get(i).get("location_output_id").toString())).getWhId();				
			stockPositionOut.setWhId(whIdOut);
			// 上架 组盘上架
			String whCodeOut = dictionaryService.getWhCodeByWhId(whIdOut);
			Integer sourceAreaIdOut = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCodeOut,
					UtilProperties.getInstance().getDefaultCCQ());
			Integer sourcePositionIdOut = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCodeOut,
					UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());

			stockPositionOut.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
			stockPositionOut.setAreaId(sourceAreaIdOut);
			stockPositionOut.setPositionId(sourcePositionIdOut);
			stockPositionOut.setMatId(Integer.valueOf(list1.get(i).get("mat_id").toString()));
			stockPositionOut.setFtyId(Integer.valueOf(list1.get(i).get("output_fty_id").toString()));
			stockPositionOut.setLocationId(Integer.valueOf(list1.get(i).get("location_output_id").toString()));
			stockPositionOut.setBatch(new Long(list1.get(i).get("batch").toString()));
			stockPositionOut.setPackageTypeId(Integer.valueOf(list1.get(i).get("package_type_id").toString()));
			stockPositionOut.setInputDate(new Date());
			stockPositionOut.setQty(new BigDecimal(list1.get(i).get("qty").toString()));
			stockPosition.setUnitId(Integer.valueOf(list1.get(i).get("unit_id").toString()));
			stockPositionOut.setInputDate(new Date());
			stockPositionOut.setPackageId(0);
			stockPositionOut.setPalletId(0);
			stockPositionOut.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			stockPositionOut.setStockTypeId(EnumStockType.STOCK_TYPE_ACCOUNT.getValue());
			commonService.modifyStockPosition(stockPositionOut);
			
			
			
			BizBusinessHistory record=new BizBusinessHistory();		
			record.setAreaId(sourceAreaId);
			record.setBatch(new Long(item.get("batch").toString()));
			record.setBusinessType((byte) 1);
			record.setCreateTime(new Date());
			record.setCreateUser(cuser.getUserId());
			record.setDebitCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			record.setFtyId(UtilObject.getIntOrZero(item.get("fty_id")));
			record.setLocationId(UtilObject.getIntOrZero(item.get("location_input")));
			record.setMatId(UtilObject.getIntegerOrNull(item.get("mat_id")));
			record.setQty(new BigDecimal(item.get("qty").toString()));
			record.setModifyTime(new Date());
			record.setModifyUser(cuser.getUserId());
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
			record1.setDebitCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			record1.setFtyId(UtilObject.getIntOrZero(item.get("output_fty_id")));
			record1.setLocationId(UtilObject.getIntOrZero(item.get("location_output_id")));
			record1.setMatId(UtilObject.getIntegerOrNull(item.get("mat_id")));
			record1.setQty(new BigDecimal(item.get("qty").toString()));
			record1.setModifyTime(new Date());
			record1.setModifyUser(cuser.getUserId());
			record1.setPositionId(sourcePositionId1);
			record1.setReceiptType(bizStock.getReceiptType());
			record1.setReferReceiptCode(bizStock.getStockTransportCode());
			record1.setReferReceiptId(bizStock.getStockTransportId());
			record1.setReferReceiptPid(whId1);
			record1.setReferReceiptRid(i+1);
			record1.setSynMes(true);
			record1.setSynSap(true);
			bizBusinessHistoryDao.insertSelective(record1);	
			
		}	
		ErpReturn er = this.takeSapWriteOff(head, l);
		if ("S".equals(er.getType())) {
			for(int i=0;i<itemList.size();i++) {
				BizStockInputTransportItem record=new BizStockInputTransportItem();
				record.setItemId(Integer.valueOf(itemList.get(i).get("item_id").toString()));
				record.setMatOffCode(er.getMatDocCode());	
				bizStockInputTransportItemDao.updateByPrimaryKeySelective(record);
			}	
		}else {
			throw new WMSException(er.getMessage());
		}
	}

	

	@Override
	public void modifyNum(JSONObject json,JSONArray itemList, CurrentUser cUser,BizStockInputTransportHead transHead) throws Exception {
		for(int i=0;i<itemList.size();i++) {
			Map<String, Object> map=new HashMap<String,Object>();
			JSONObject js = itemList.getJSONObject(i);
			//发出
//			for(int j=0;j<itemList.size();j++) {
				map.put("matId", js.get("mat_id"));
				map.put("locationId", json.get("loc_output_id"));
				map.put("batch", js.get("batch"));
				map.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				map.put("createUserId", cUser.getUserId());
				map.put("debitCredit", "H");
				map.put("qty", js.get("transport_qty"));
				map.put("stockTypeId",EnumStockType.STOCK_TYPE_ACCOUNT.getValue());
				commonService.modifyStockBatch(map);
//			}
		
		//接收
//			for(int p=0;p<taskItemList.size();p++) {
				map.put("matId", js.get("mat_id"));
				map.put("locationId", json.get("loc_input_id"));
				map.put("batch", js.get("batch"));
				map.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				map.put("createUserId", cUser.getUserId());
				map.put("debitCredit", "S");
				map.put("qty", js.get("transport_qty"));
				map.put("stockTypeId",EnumStockType.STOCK_TYPE_ACCOUNT.getValue());
				commonService.modifyStockBatch(map);			
//			}
				
				StockPosition stockPosition=new StockPosition();
				int whId=dicLocationDao.selectByPrimaryKey(Integer.valueOf(json.get("loc_output_id").toString())).getWhId();				
				stockPosition.setWhId(whId);
				// 上架 组盘上架
				String whCode = dictionaryService.getWhCodeByWhId(whId);
				Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
						UtilProperties.getInstance().getDefaultCCQ());
				Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
						UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());

				stockPosition.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				stockPosition.setAreaId(sourceAreaId);
				stockPosition.setPositionId(sourcePositionId);
				stockPosition.setMatId(Integer.valueOf(js.get("mat_id").toString()));
				stockPosition.setFtyId(Integer.valueOf(json.get("fty_output_id").toString()));
				stockPosition.setLocationId(Integer.valueOf(json.get("loc_output_id").toString()));
				stockPosition.setBatch(new Long(js.get("batch").toString()));
				stockPosition.setPackageTypeId(Integer.valueOf(js.get("package_type_id").toString()));
				stockPosition.setInputDate(new Date());
				stockPosition.setQty(new BigDecimal(js.get("transport_qty").toString()));
//				stockPosition.setUnitId(Integer.valueOf(json.get("unit_id").toString()));
				stockPosition.setInputDate(new Date());
				stockPosition.setPackageId(0);
				stockPosition.setPalletId(0);
				stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				stockPosition.setStockTypeId(EnumStockType.STOCK_TYPE_ACCOUNT.getValue());
				commonService.modifyStockPosition(stockPosition);	
				
				
				StockPosition stockPositionOut=new StockPosition();
				int whIdOut=dicLocationDao.selectByPrimaryKey(Integer.valueOf(json.get("loc_input_id").toString())).getWhId();				
				stockPositionOut.setWhId(whIdOut);
				// 上架 组盘上架
				String whCodeOut = dictionaryService.getWhCodeByWhId(whIdOut);
				Integer sourceAreaIdOut = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCodeOut,
						UtilProperties.getInstance().getDefaultCCQ());
				Integer sourcePositionIdOut = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCodeOut,
						UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());

				stockPositionOut.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
				stockPositionOut.setAreaId(sourceAreaIdOut);
				stockPositionOut.setPositionId(sourcePositionIdOut);
				stockPositionOut.setMatId(Integer.valueOf(js.get("mat_id").toString()));
				stockPositionOut.setFtyId(Integer.valueOf(json.get("fty_input_id").toString()));
				stockPositionOut.setLocationId(Integer.valueOf(json.get("loc_input_id").toString()));
				stockPositionOut.setBatch(new Long(js.get("batch").toString()));
				stockPositionOut.setPackageTypeId(Integer.valueOf(js.get("package_type_id").toString()));
				stockPositionOut.setInputDate(new Date());
				stockPositionOut.setQty(new BigDecimal(js.get("transport_qty").toString()));
//				stockPosition.setUnitId(Integer.valueOf(json.get("unit_id").toString()));
				stockPositionOut.setInputDate(new Date());
				stockPositionOut.setPackageId(0);
				stockPositionOut.setPalletId(0);
				stockPositionOut.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				stockPositionOut.setStockTypeId(EnumStockType.STOCK_TYPE_ACCOUNT.getValue());
				commonService.modifyStockPosition(stockPositionOut);
				
				
				BizBusinessHistory record=new BizBusinessHistory();
				JSONObject js1 = itemList.getJSONObject(i);				
				record.setAreaId(sourceAreaId);
				record.setBatch(new Long(js1.get("batch").toString()));
				record.setBusinessType((byte) 2);
				record.setCreateTime(new Date());
				record.setCreateUser(cUser.getUserId());
				record.setDebitCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				record.setFtyId(Integer.valueOf(json.get("fty_output_id").toString()));
				record.setLocationId(Integer.valueOf(json.get("loc_output_id").toString()));
				record.setMatId(Integer.valueOf(js1.get("mat_id").toString()));
				record.setModifyTime(new Date());
				record.setModifyUser(cUser.getUserId());
				record.setQty(new BigDecimal(js1.get("transport_qty").toString()));
				record.setPackageId(0);
				record.setPalletId(0);
				record.setPositionId(sourcePositionId);
				record.setReceiptType(transHead.getReceiptType());
				record.setReferReceiptCode(transHead.getStockTransportCode());
				record.setReferReceiptId(transHead.getStockTransportId());
				record.setReferReceiptPid(whId);
				record.setReferReceiptRid(i+1);
				record.setSynMes(true);
				record.setSynSap(true);
				bizBusinessHistoryDao.insertSelective(record);		
			
				
				int whIdin=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(json.get("loc_input_id").toString())).getWhId();
				
				// 上架 组盘上架
				String whCodein = dictionaryService.getWhCodeByWhId(whIdin);
				Integer sourceAreaIdin = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCodein,
						UtilProperties.getInstance().getDefaultCCQ());
				Integer sourcePositionIdin = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCodein,
						UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
				record.setAreaId(sourceAreaIdin);
				record.setBatch(new Long(js1.get("batch").toString()));
				record.setBusinessType((byte) 1);
				record.setCreateTime(new Date());
				record.setCreateUser(cUser.getUserId());
				record.setDebitCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
				record.setFtyId(Integer.valueOf(json.get("fty_input_id").toString()));
				record.setLocationId(Integer.valueOf(json.get("loc_input_id").toString()));
				record.setMatId(Integer.valueOf(js1.get("mat_id").toString()));
				record.setModifyTime(new Date());
				record.setModifyUser(cUser.getUserId());
				record.setQty(new BigDecimal(js1.get("transport_qty").toString()));
				record.setPackageId(0);
				record.setPalletId(0);
				record.setPositionId(sourcePositionIdin);
				record.setReceiptType(transHead.getReceiptType());
				record.setReferReceiptCode(transHead.getStockTransportCode());
				record.setReferReceiptId(transHead.getStockTransportId());
				record.setReferReceiptPid(whIdin);
				record.setReferReceiptRid(i+1);
				record.setSynMes(true);
				record.setSynSap(true);
				bizBusinessHistoryDao.insertSelective(record);
		}
		
		ErpReturn er=this.takeSap(transHead,json,itemList);
		if("S".equals(er.getType())) {
			List<Map<String,Object>> itemList1=bizStockInputHeadDao.selectItemList(transHead.getStockTransportId());
			for(int i=0;i<itemList1.size();i++) {
				BizStockInputTransportItem record=new BizStockInputTransportItem();
				record.setItemId(Integer.valueOf(itemList1.get(i).get("item_id").toString()));
				record.setMatDocCode(er.getMatDocCode());	
				record.setMatDocRid(i+1);
				record.setMatDocYear(er.getDocyear());
				bizStockInputTransportItemDao.updateByPrimaryKeySelective(record);
			}		

			BizStockInputTransportHead record=new BizStockInputTransportHead();
			record.setStockTransportId(transHead.getStockTransportId());
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			java.util.Date dat=new java.util.Date();  							
			record.setDocTime(sdf.format(dat));
			record.setPostingDate(sdf.format(dat));
			record.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
			bizStockInputHeadDao.updateByPrimaryKeySelective(record);	
		}else {
			 throw new RuntimeException("过账失败！");
		}
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
			if(!map.containsKey(itemList.get(i).get("mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString())) {
				map.put(itemList.get(i).get("mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString(), new BigDecimal(itemList.get(i).get("qty").toString()));					
			}else {
				map.put(itemList.get(i).get("mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString(), new BigDecimal(map.get(itemList.get(i).get("mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString()).toString()).add(new BigDecimal(itemList.get(i).get("qty").toString())));								
			}	
		}
		Map<String, Object> m = new HashMap<String, Object>();
		
		
		for(int i=0;i<itemList.size();i++) {			
			DTCWWMSMATDOCPOSTREQ.ITDOCITEM itdocitemtest=new DTCWWMSMATDOCPOSTREQ.ITDOCITEM();
			itdocitemtest.setDOCNUM(bizStockInputTransportHead.getStockTransportCode().toString());
			itdocitemtest.setDOCITEMNUM(i+"");
			itdocitemtest.setPLANT(itemList.get(i).get("fty_code").toString());
			itdocitemtest.setSTGELOC(itemList.get(i).get("location_code").toString());
			itdocitemtest.setMATERIAL(itemList.get(i).get("mat_code").toString());
			itdocitemtest.setBATCH(itemList.get(i).get("tran_erp_batch").toString());
			itdocitemtest.setENTRYQNT(itemList.get(i).get("qty").toString());
//			itdocitemtest.setMOVETYPE(dicMoveTypeDao.selectByPrimaryKey(Integer.valueOf(itemList.get(i).get("move_type_id").toString())).getMoveTypeCode());
			itdocitemtest.setMOVETYPE("301");
			itdocitemtest.setMOVEPLANT(itemList.get(i).get("output_fty_code").toString());
			itdocitemtest.setMOVESTLOC(itemList.get(i).get("output_location_code").toString());
			itdocitemtest.setMOVEMAT(itemList.get(i).get("mat_code").toString());
			itdocitemtest.setMOVEBATCH(itemList.get(i).get("erp_batch").toString());
			if(map.containsKey(itemList.get(i).get("mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString())) {
				if(!m.containsKey(itemList.get(i).get("mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString())) {
					itdocitemtest.setDOCITEMNUM(j+"");
					itdocitemtest.setENTRYUOM("KG");
					BigDecimal qty = UtilObject.getBigDecimalOrZero(map.get(itemList.get(i).get("mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString()).toString());
					if(UtilObject.getStringOrEmpty(itemList.get(i).get("unit_code")).equals("KG")) {
						qty = qty.multiply(new BigDecimal("1"));
					}else {
						qty = qty.multiply(new BigDecimal("1000"));
					}
					itdocitem.add(itdocitemtest);	
					j++;
					m.put(itemList.get(i).get("mat_code").toString()+itemList.get(i).get("output_location_code").toString()+itemList.get(i).get("location_code").toString()+itemList.get(i).get("tran_erp_batch").toString(), "1");
				}											
			}
		}		
		dtcwwmsmatdocpostreq.setItdocitem(itdocitem);
		dtcwwmsmatdocpostreq.setItdochead(itdochead);
		ErpReturn er=cwErpWsImpl.inputPost(dtcwwmsmatdocpostreq);
		return er;
	}

	@Override
	public void updateMatDocCode(ErpReturn er, BizStockInputTransportHead transHead) {
		List<Map<String,Object>> itemList=bizStockInputHeadDao.selectItemList(transHead.getStockTransportId());
		for(int i=0;i<itemList.size();i++) {
			BizStockInputTransportItem record=new BizStockInputTransportItem();
			record.setItemId(Integer.valueOf(itemList.get(i).get("item_id").toString()));
			record.setMatDocCode(er.getMatDocCode());	
			record.setMatDocRid(i+1);
			record.setMatDocYear(er.getDocyear());
			bizStockInputTransportItemDao.updateByPrimaryKeySelective(record);
		}		


		BizStockInputTransportHead record=new BizStockInputTransportHead();
		record.setStockTransportId(transHead.getStockTransportId());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		java.util.Date dat=new java.util.Date();  							
		record.setDocTime(sdf.format(dat));
		record.setPostingDate(sdf.format(dat));
		record.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
		bizStockInputHeadDao.updateByPrimaryKeySelective(record);	
	}



	@Override
	public void setHistory(BizStockInputTransportHead bizStockInputTransportHead, CurrentUser cUser, JSONObject json,
			JSONArray itemList)  throws Exception {
		for(int i=0;i<itemList.size();i++) {
			BizBusinessHistory record=new BizBusinessHistory();
			JSONObject js = itemList.getJSONObject(i);
				int whId=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(json.get("loc_output_id").toString())).getWhId();

				// 上架 组盘上架
				String whCode = dictionaryService.getWhCodeByWhId(whId);
				Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
						UtilProperties.getInstance().getDefaultCCQ());
				Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
						UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
				record.setAreaId(sourceAreaId);
				record.setBatch(new Long(js.get("batch").toString()));
				record.setBusinessType((byte) 2);
				record.setCreateTime(new Date());
				record.setCreateUser(cUser.getUserId());
				record.setDebitCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				record.setFtyId(Integer.valueOf(json.get("fty_output_id").toString()));
				record.setLocationId(Integer.valueOf(json.get("loc_output_id").toString()));
				record.setMatId(Integer.valueOf(js.get("mat_id").toString()));
				record.setModifyTime(new Date());
				record.setModifyUser(cUser.getUserId());
				record.setQty(new BigDecimal(js.get("transport_qty").toString()));
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
		
			
			int whIdin=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(json.get("loc_input_id").toString())).getWhId();
			
			// 上架 组盘上架
			String whCodein = dictionaryService.getWhCodeByWhId(whIdin);
			Integer sourceAreaIdin = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCodein,
					UtilProperties.getInstance().getDefaultCCQ());
			Integer sourcePositionIdin = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCodein,
					UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
			record.setAreaId(sourceAreaIdin);
			record.setBatch(new Long(js.get("batch").toString()));
			record.setBusinessType((byte) 1);
			record.setCreateTime(new Date());
			record.setCreateUser(cUser.getUserId());
			record.setDebitCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
			record.setFtyId(Integer.valueOf(json.get("fty_input_id").toString()));
			record.setLocationId(Integer.valueOf(json.get("loc_input_id").toString()));
			record.setMatId(Integer.valueOf(js.get("mat_id").toString()));
			record.setModifyTime(new Date());
			record.setModifyUser(cUser.getUserId());
			record.setQty(new BigDecimal(js.get("transport_qty").toString()));
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

}
