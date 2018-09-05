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
import com.inossem.wms.dao.biz.BizStockInputHeadMapper;
import com.inossem.wms.dao.biz.BizStockInputTransportHeadMapper;
import com.inossem.wms.dao.biz.BizStockInputTransportItemMapper;
import com.inossem.wms.dao.biz.BizStockTaskHeadCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskItemCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskPositionCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqItemMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicClassTypeMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicMoveTypeMapper;
import com.inossem.wms.dao.dic.DicPackageTypeMapper;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.dic.DicUnitMapper;
import com.inossem.wms.dao.stock.StockPositionMapper;
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
import com.inossem.wms.model.biz.ErpReturn;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.enums.EnumDebitCredit;
import com.inossem.wms.model.enums.EnumIsUrgent;
import com.inossem.wms.model.enums.EnumMatStoreType;
import com.inossem.wms.model.enums.EnumRankLevel;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumSequence;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumStockType;
import com.inossem.wms.model.enums.EnumTaskReqShippingType;
import com.inossem.wms.model.key.StockPositionKey;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IInputProduceTransportService;
import com.inossem.wms.service.biz.IInputTransportService;
import com.inossem.wms.service.biz.IInputUrgentTransportService;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("inputUrgentTransportService")
@Transactional
public class InputUrgentTransportServiceImpl implements IInputUrgentTransportService {

	@Autowired
	private StockPositionMapper stockPositionDao;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private DicFactoryMapper dicFactoryDao;
	@Autowired
	private IDictionaryService dictionaryService;
	@Autowired
	private BizFailMesMapper failMesDao;
	@Autowired
	private BizStockTaskItemCwMapper bizStocktakeItemDao;
	
	@Autowired
	private BizStockTaskHeadCwMapper bizStockTaskHeadCwDao;
	@Autowired
	private DicClassTypeMapper dicClassTypeMapper;
	@Autowired
	private DicStockLocationMapper dicLocationDao;
	@Autowired
	private SequenceDAO sequenceDAO;

	@Autowired
	private BizStockInputHeadMapper stockInputHeadDao;


	@Autowired
	private BizStockTaskReqHeadMapper StockTaskReqHeadDao;

	@Autowired
	private BizStockTaskReqItemMapper StockTaskReqItemDao;

	
	@Autowired
	private BizStockInputTransportHeadMapper bizStockInputHeadDao;
	
	@Autowired
	private BizStockInputTransportItemMapper bizStockInputTransportItemDao;
	
	
	@Autowired
	private DicClassTypeMapper dicClassTypeDao;

	@Autowired
	private DicMoveTypeMapper dicMoveTypeDao;
	
	@Autowired
	private DicStockLocationMapper dicStockLocationDao;
	
	@Autowired
	private DicUnitMapper dicUnitMapper;
	@Autowired
	private CwErpWsImpl cwErpWsImpl;

	
	@Autowired
	private CwMesWsImpl cwMesWsImpl;
	
	@Autowired
	private BizStockTaskItemCwMapper stockTaskItemCwDao;
	
	@Autowired
	private BizStockTaskPositionCwMapper bizStockTaskPositionCwDao;
	
	@Autowired
	private BizBusinessHistoryMapper bizBusinessHistoryDao;
	
	
	@Autowired
	private IInputProduceTransportService iInputProduceTransportService;

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
		return bizStockInputHeadDao.select_stock_taskOnPagingUrgent(map);
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
		bizStockInputTransportHead.setReceiptType(EnumReceiptType.STOCK_INPUT_TRANSPORT_PRODUCTION_URGENT.getValue());
		bizStockInputTransportHead.setRemark(json.getString("remark"));
//		bizStockInputTransportHead.setRequestSource(requestSource);
		bizStockInputTransportHead.setStatus(EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
	
		bizStockInputTransportHead.setStockTransportCode(String.valueOf(sequenceDAO.selectNextVal("stock_input")));
//		bizStockInputTransportHead.setSynType(Integer.valueOf(json.getString("syn_type")));	
		bizStockInputTransportHead.setSynType(2);
		 bizStockInputHeadDao.insertSelective(bizStockInputTransportHead);
		JSONArray myJsonArray =JSONArray.fromObject(json.get("item_list"));	
		
		for(int i=0;i<itemList.size();i++) {
			BizStockInputTransportItem bizStockInputTransportItem=new BizStockInputTransportItem();
//			bizStockInputTransportItem.setAreaId(areaId);
			bizStockInputTransportItem.setBatch(UtilObject.getLongOrNull(itemList.get(i).get("batch")));
			bizStockInputTransportItem.setBusinessType(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("refer_receipt_type"))));
			bizStockInputTransportItem.setCreateTime(new Date());
			bizStockInputTransportItem.setFtyId(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("fty_id"))));
			bizStockInputTransportItem.setFtyInput(Integer.valueOf(UtilString.getStringOrEmptyForObject(itemList.get(i).get("fty_id"))));
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
			record.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
			bizStocktakeItemDao.updateByPrimaryKeySelective(record);
		}
		
		return bizStockInputTransportHead;
	}

	@Override
	public void updateStockToWriteOff(Integer stock_input_id,CurrentUser cuser) throws Exception {
		BizStockInputTransportHead bizStockInputTransportHead=new BizStockInputTransportHead();
		bizStockInputTransportHead.setStockTransportId(stock_input_id);
		bizStockInputTransportHead.setStatus((byte) 20);
		bizStockInputHeadDao.updateByPrimaryKeySelective(bizStockInputTransportHead);
		
		BizStockInputTransportHead bizStock=bizStockInputHeadDao.selectByPrimaryKey(stock_input_id);
		String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());
		//保存作业请求头部信息
		BizStockTaskReqHead taskReqHead = new BizStockTaskReqHead();
		taskReqHead.setStockTaskReqCode(stockTaskReqCode);
		taskReqHead.setWhId(0);
		taskReqHead.setFtyId(UtilObject.getIntegerOrNull(bizStock.getFtyId()));
		taskReqHead.setLocationId(UtilObject.getIntegerOrNull(bizStock.getLocationId()));
		taskReqHead.setMatDocId(0);
		taskReqHead.setMatDocYear(0);
		taskReqHead.setShippingType(EnumTaskReqShippingType.STOCK_OUTPUT.getValue());
		taskReqHead.setStatus(EnumBoolean.FALSE.getValue());
		taskReqHead.setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue());
		taskReqHead.setReferReceiptId(bizStock.getStockTransportId());
		taskReqHead.setReferReceiptCode(bizStock.getStockTransportId().toString());
		taskReqHead.setReferReceiptType(UtilObject.getByteOrNull(bizStock.getReceiptType()));
		taskReqHead.setCreateUser(cuser.getUserId());
		taskReqHead.setCreateTime(new Date());
		taskReqHead.setModifyTime(new Date());
		StockTaskReqHeadDao.insertSelective(taskReqHead);
		
		
		
		
		List<Map<String,Object>> list=bizStockInputTransportItemDao.selectByStockTransportId(stock_input_id);
		
		for(int i=0;i<list.size();i++) {
			Map<String,Object> item = list.get(i);
			BizStockTaskReqItem taskReqItem = new BizStockTaskReqItem();
			taskReqItem.setFtyId(UtilObject.getIntOrZero(item.get("fty_id")));
			taskReqItem.setLocationId(UtilObject.getIntOrZero(item.get("location_id")));
			taskReqItem.setMatId(UtilObject.getIntegerOrNull(item.get("mat_id")));
			taskReqItem.setStockTaskQty(BigDecimal.ZERO);
			taskReqItem.setQty(UtilObject.getBigDecimalOrZero(item.get("qty")));
//			taskReqItem.setBatch(UtilObject.getLongOrNull(item.get("batch")));
			taskReqItem.setBatch(UtilObject.getLongOrNull("111"));
			taskReqItem.setErpBatch(UtilObject.getStringOrEmpty(item.get("erp_batch")));
			taskReqItem.setProductionBatch(UtilObject.getStringOrEmpty(item.get("production_batch")));
			taskReqItem.setQualityBatch(UtilObject.getStringOrEmpty(item.get("quality_batch")));
			taskReqItem.setUnitId(UtilObject.getIntOrZero(item.get("unit_id")));
			taskReqItem.setPackageTypeId(UtilObject.getIntOrZero(item.get("package_type_id")));
			taskReqItem.setReferReceiptType(bizStock.getReceiptType());
			taskReqItem.setReferReceiptId(bizStock.getStockTransportId());
			taskReqItem.setReferReceiptCode(bizStock.getStockTransportCode());
			taskReqItem.setReferReceiptRid(UtilObject.getIntOrZero(item.get("stock_output_rid")));
			taskReqItem.setStockTaskReqId(taskReqHead.getStockTaskReqId());
			taskReqItem.setStockTaskReqRid(UtilObject.getIntOrZero(i));
			taskReqItem.setIsDelete(EnumBoolean.FALSE.getValue());
			taskReqItem.setStatus(EnumBoolean.FALSE.getValue());
			taskReqItem.setWhId(0);
			taskReqItem.setCreateTime(new Date());
			taskReqItem.setModifyTime(new Date());
			StockTaskReqItemDao.insertSelective(taskReqItem);
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
		
		
		for(int i=0;i<itemList.size();i++) {
			DTCWWMSMATDOCPOSTREQ.ITDOCITEM itdocitemtest=new DTCWWMSMATDOCPOSTREQ.ITDOCITEM();
			itdocitemtest.setDOCNUM(bizStockInputTransportHead.getStockTransportCode().toString());
			itdocitemtest.setDOCITEMNUM(i+"");
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
			WmMvRecInteropeDto dto =  new WmMvRecInteropeDto();
			Map<String, Object> map1=itemList.get(i);
			if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("location_input").toString())).getLocationCode().equals("6006")
					&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(map1.get("fty_id").toString())).getFtyCode().equals("SWX1")) {
				continue;
			}
			dto.setRecNum(i+1);
			dto.setSrMtrlCode(map1.get("mat_code").toString());
			dto.setTgMtrlCode(map1.get("tran_mat_code").toString());
			Map<String,Object> matMap=iInputProduceTransportService.getMatInfoByCode(map1.get("tran_mat_code").toString());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("mes_unit_code", "吨");
			param.put("wms_unit_code", matMap.get("unit_code"));
			String rel=stockInputHeadDao.getUnitRelMesAndWms(param);
			dto.setWgtPerPack(Double.parseDouble(map1.get("package_standard").toString()));
			dto.setAmnt(new BigDecimal(map1.get("qty").toString()).divide(new BigDecimal(map1.get("package_standard").toString()),0, RoundingMode.UP).intValue());
			dto.setWgt(new BigDecimal(map1.get("qty").toString()).divide(new BigDecimal(rel)).doubleValue());
			dto.setSrBch(map1.get("erp_batch").toString());
			dto.setTgBch(map1.get("tran_erp_batch").toString());
			dto.setSrRankCode(EnumRankLevel.getRankByValue(map1.get("erp_batch").toString()));
			dto.setTgRankCode(EnumRankLevel.getRankByValue(map1.get("tran_erp_batch").toString()));

			if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("location_output_id").toString())).getLocationCode().equals("6006")
					&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(map1.get("output_fty_id").toString())).getFtyCode().equals("SWX1")) {
				dto.setSrNodeCode("Z5SW042");
			}else {
				dto.setSrNodeCode(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("location_output_id").toString())).getNodeCode());
			}
			dto.setTgNodeCode(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("location_input").toString())).getNodeCode());
			dto.setDlvBillCode(bizStockInputTransportHead.getStockTransportCode());
			dto.setCustomer("转储入库");
			dto.setTransTypeCode("1");
//			dto.setVehiCode("1001");
//			if(map.get("remark")!=null) {
				dto.setDes(bizStockInputTransportHead.getStockTransportCode());
//			}			
//			dto.setWgtDim(dicUnitMapper.selectByPrimaryKey(Integer.valueOf(map.get("unit_id").toString())).getUnitCode());
			dto.setWgtDim("TO");
			
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
//		String move_type_code=dicMoveTypeDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("move_type_id").toString())).getMoveTypeCode();
//		if(move_type_code.equals("301") || move_type_code.equals("311")) {
		if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("location_output_id").toString())).getLocationCode().equals("6006") 
				&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("output_fty_id").toString())).getFtyCode().equals("SWX1") ) {
			val =cwMesWsImpl.updateMesStock(bizStockInputTransportHead.getStockTransportCode(), "101",beginTime ,endTime, dtoSet);				
		}else {
			val =cwMesWsImpl.updateMesStock(bizStockInputTransportHead.getStockTransportCode(), "301",beginTime ,endTime, dtoSet);
		}
//		}
		
//		if(move_type_code.equals("309") ) {
//			val =cwMesWsImpl.updateMesStock(bizStockInputTransportHead.getStockTransportCode(), "1101",beginTime ,endTime, dtoSet);
//		}
		if(val.getValue()==null) {
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

	@Override
	public void modifyNum(List<Map<String,Object>> itemList,CurrentUser user,BizStockInputTransportHead bizStockInputTransportHead) throws Exception {
		
		
		for(int i=0;i<itemList.size();i++) {
			Map<String, Object> map=new HashMap<String,Object>();
			BizStockTaskItemCw ti = new BizStockTaskItemCw();
			ti.setStockTaskId((Integer) itemList.get(i).get("stock_task_id"));
			ti.setStockTaskRid((Integer) itemList.get(i).get("stock_task_rid"));
//			ti.setReferReceiptType((Byte) itemList.get(0).get("refer_receipt_type"));
			List<BizStockTaskItemCw> taskItemList = stockTaskItemCwDao.selectByReferReceiptIdAndType(ti);
			
			BizStockTaskPositionCw tp = new BizStockTaskPositionCw();
//			tp.setStockTaskId((Integer) itemList.get(i).get("stock_task_id"));
//			tp.setItemId((Integer) itemList.get(i).get("item_id"));
			tp.setStockTaskId((Integer) itemList.get(i).get("stock_task_id"));
			tp.setStockTaskRid((Integer) itemList.get(i).get("stock_task_rid"));
//			tp.setReferReceiptType((Byte) itemList.get(0).get("refer_receipt_type"));
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
			map.put("isUrgent", EnumIsUrgent.URGENT.getValue());
			
			commonService.modifyStockBatch(map);

	
			//接收
			map.put("matId", itemList.get(i).get("tran_mat_id"));
			map.put("locationId", itemList.get(i).get("tran_location_id"));
			map.put("batch", itemList.get(i).get("tran_batch"));
			map.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			map.put("createUserId", user.getUserId());
			map.put("debitCredit", "S");
			map.put("qty", itemList.get(i).get("qty"));
			map.put("stockTypeId",EnumStockType.STOCK_TYPE_ERP.getValue());
			map.put("isUrgent", EnumIsUrgent.URGENT.getValue());
			commonService.modifyStockBatch(map);
			
			

			
			
			if(positionList!=null && positionList.size()>0) {
				commonService.updateStockPositionByTask(taskItemList, positionList,EnumStockType.STOCK_TYPE_ERP.getValue(),true);
			}
			
			
			
//			BizBusinessHistory record=new BizBusinessHistory();
//			int whIdin=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(i).get("tran_location_id").toString())).getWhId();			
//			// 上架 组盘上架
//			String whCodein = dictionaryService.getWhCodeByWhId(whIdin);
//			Integer sourceAreaIdin = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCodein,
//					UtilProperties.getInstance().getDefaultCCQ());
//			Integer sourcePositionIdin = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCodein,
//					UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
//			record.setAreaId(sourceAreaIdin);
//			record.setBatch(new Long(itemList.get(i).get("tran_batch").toString()));
//			record.setBusinessType((byte) 1);
//			record.setCreateTime(new Date());
//			record.setCreateUser(user.getUserId());
//			record.setDebitCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
//			record.setFtyId(Integer.valueOf(itemList.get(i).get("tran_fty_id").toString()));
//			record.setLocationId(Integer.valueOf(itemList.get(i).get("tran_location_id").toString()));
//			record.setMatId(Integer.valueOf(itemList.get(i).get("tran_mat_id").toString()));
//			record.setModifyTime(new Date());
//			record.setModifyUser(user.getUserId());
//			record.setQty(new BigDecimal(itemList.get(i).get("qty").toString()));
//			record.setPackageId(0);
//			record.setPalletId(0);
//			record.setPositionId(sourcePositionIdin);
//			record.setReceiptType(bizStockInputTransportHead.getReceiptType());
//			record.setReferReceiptCode(bizStockInputTransportHead.getStockTransportCode());
//			record.setReferReceiptId(bizStockInputTransportHead.getStockTransportId());
//			record.setReferReceiptPid(whIdin);
//			record.setReferReceiptRid(i+1);
//			record.setSynMes(true);
//			record.setSynSap(true);
//			bizBusinessHistoryDao.insertSelective(record);
			
			
				
		}
		
	
	}
	
	
	
	@Override
	public void modifyNum2(List<Map<String,Object>> itemList,CurrentUser user,BizStockInputTransportHead bizStockInputTransportHead) throws Exception {
		
		
		for(int i=0;i<itemList.size();i++) {
			Map<String, Object> map=new HashMap<String,Object>();
			BizStockTaskItemCw ti = new BizStockTaskItemCw();
			ti.setStockTaskId((Integer) itemList.get(i).get("stock_task_id"));
			ti.setStockTaskRid((Integer) itemList.get(i).get("stock_task_rid"));
//			ti.setReferReceiptType((Byte) itemList.get(0).get("refer_receipt_type"));
			List<BizStockTaskItemCw> taskItemList = stockTaskItemCwDao.selectByReferReceiptIdAndType(ti);
			
			BizStockTaskPositionCw tp = new BizStockTaskPositionCw();
//			tp.setStockTaskId((Integer) itemList.get(i).get("stock_task_id"));
//			tp.setItemId((Integer) itemList.get(i).get("item_id"));
			tp.setStockTaskId((Integer) itemList.get(i).get("stock_task_id"));
			tp.setStockTaskRid((Integer) itemList.get(i).get("stock_task_rid"));
//			tp.setReferReceiptType((Byte) itemList.get(0).get("refer_receipt_type"));
			List<BizStockTaskPositionCw> positionList = bizStockTaskPositionCwDao.selectByReferReceiptIdAndType(tp);
			
			
			
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
			map.put("stockTypeId",EnumStockType.STOCK_TYPE_ERP.getValue());
			commonService.modifyStockBatch(map);	
			
			
			
			
			
			//发出
			map.put("matId", itemList.get(i).get("mat_id"));
			map.put("locationId", itemList.get(i).get("output_location_id"));
			map.put("batch", itemList.get(i).get("batch"));
			map.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			map.put("createUserId", user.getUserId());
			map.put("debitCredit", "S");
			map.put("qty", itemList.get(i).get("qty"));
			map.put("stockTypeId",EnumStockType.STOCK_TYPE_ERP.getValue());
			map.put("isUrgent", EnumIsUrgent.URGENT.getValue());
			
			commonService.modifyStockBatchOnSyn(map);

	
			//接收
			map.put("matId", itemList.get(i).get("tran_mat_id"));
			map.put("locationId", itemList.get(i).get("tran_location_id"));
			map.put("batch", itemList.get(i).get("tran_batch"));
			map.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			map.put("createUserId", user.getUserId());
			map.put("debitCredit", "H");
			map.put("qty", itemList.get(i).get("qty"));
			map.put("stockTypeId",EnumStockType.STOCK_TYPE_ERP.getValue());
			map.put("isUrgent", EnumIsUrgent.URGENT.getValue());
			commonService.modifyStockBatchOnSyn(map);
			
			
			
			if(positionList!=null && positionList.size()>0) {
				commonService.synStockPositionOnUrgent(taskItemList, positionList,EnumStockType.STOCK_TYPE_ERP.getValue());
			}
			
			
			for(int k=0;k<positionList.size();k++) {
				// 生产工厂  修改正式库存
				StockPosition stockPosition=new StockPosition();
				BizStockTaskItemCw  stockTaskItem = taskItemList.get(0);
				int whId=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(i).get("output_location_id").toString())).getWhId();
				stockPosition.setWhId(whId);
//				// 上架 组盘上架
				String whCode = dictionaryService.getWhCodeByWhId(whId);
				Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
						UtilProperties.getInstance().getDefaultCCQ());
				Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
						UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());

				stockPosition.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
				stockPosition.setAreaId(sourceAreaId);
				stockPosition.setPositionId(sourcePositionId);
				stockPosition.setMatId(Integer.valueOf(itemList.get(i).get("mat_id").toString()));
				stockPosition.setFtyId(Integer.valueOf(itemList.get(i).get("output_fty_id").toString()));
				stockPosition.setLocationId(Integer.valueOf(itemList.get(i).get("output_location_id").toString()));
				stockPosition.setBatch(new Long(itemList.get(i).get("batch").toString()));
				stockPosition.setPackageTypeId(stockTaskItem.getPackageTypeId());
				stockPosition.setInputDate(new Date());
			//	stockPosition.setQty(new BigDecimal(itemList.get(i).get("qty").toString()));
				stockPosition.setQty(positionList.get(k).getQty());
				stockPosition.setUnitId(stockTaskItem.getUnitId());
				stockPosition.setInputDate(new Date());
				stockPosition.setPackageId(0);
				stockPosition.setPalletId(0);
				stockPosition.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
				stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				stockPosition.setIsUrgent(EnumIsUrgent.URGENT.getValue());
				
				if (stockTaskItem.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
					// 启用包
					if (positionList.get(k).getPackageId() == null) {
						throw new WMSException("包装类型出错");
					}
					stockPosition.setPackageId(positionList.get(k).getPackageId());
					if (positionList.get(k).getPalletId() == null) {
						stockPosition.setPalletId(0);
					} else {
						stockPosition.setPalletId(positionList.get(k).getPalletId());
					}

				} else {
					// 不启用包
					stockPosition.setPackageId(0);
					stockPosition.setPalletId(0);
				}
				commonService.modifyStockPositionOnSyn(stockPosition);
				
				
				
				StockPosition stockPosition1=new StockPosition();
				
				stockPosition1.setWhId(whId);
				// 上架 组盘上架

				stockPosition1.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				stockPosition1.setAreaId(sourceAreaId);
				stockPosition1.setPositionId(sourcePositionId);
				stockPosition1.setMatId(Integer.valueOf(itemList.get(i).get("mat_id").toString()));
				stockPosition1.setFtyId(Integer.valueOf(itemList.get(i).get("output_fty_id").toString()));
				stockPosition1.setLocationId(Integer.valueOf(itemList.get(i).get("output_location_id").toString()));
				stockPosition1.setBatch(new Long(itemList.get(i).get("batch").toString()));
				stockPosition1.setPackageTypeId(stockTaskItem.getPackageTypeId());
				stockPosition1.setInputDate(new Date());
				stockPosition1.setQty(positionList.get(k).getQty());
				stockPosition1.setUnitId(stockTaskItem.getUnitId());
				stockPosition1.setInputDate(new Date());
				stockPosition1.setPackageId(0);
				stockPosition1.setPalletId(0);
				stockPosition1.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
				stockPosition1.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				
				if (stockTaskItem.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
					// 启用包
					if (positionList.get(k).getPackageId() == null) {
						throw new WMSException("包装类型出错");
					}
					stockPosition1.setPackageId(positionList.get(k).getPackageId());
					if (positionList.get(k).getPalletId() == null) {
						stockPosition1.setPalletId(0);
					} else {
						stockPosition1.setPalletId(positionList.get(k).getPalletId());
					}

				} else {
					// 不启用包
					stockPosition1.setPackageId(0);
					stockPosition1.setPalletId(0);
				}
				commonService.modifyStockPosition(stockPosition1);		
			}
			
			BizBusinessHistory record=new BizBusinessHistory();
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
			
			
			BizBusinessHistory record1=new BizBusinessHistory();
			int whIdin1=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(i).get("output_location_id").toString())).getWhId();			
			// 上架 组盘上架
			String whCodein1 = dictionaryService.getWhCodeByWhId(whIdin1);
			Integer sourceAreaIdin1 = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCodein1,
					UtilProperties.getInstance().getDefaultCCQ());
			Integer sourcePositionIdin1 = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCodein1,
					UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
			record1.setAreaId(sourceAreaIdin1);
			record1.setBatch(new Long(itemList.get(i).get("batch").toString()));
			record1.setBusinessType((byte) 2);
			record1.setCreateTime(new Date());
			record1.setCreateUser(user.getUserId());
			record1.setDebitCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			record1.setFtyId(Integer.valueOf(itemList.get(i).get("output_fty_id").toString()));
			record1.setLocationId(Integer.valueOf(itemList.get(i).get("output_location_id").toString()));
			record1.setMatId(Integer.valueOf(itemList.get(i).get("mat_id").toString()));
			record1.setModifyTime(new Date());
			record1.setModifyUser(user.getUserId());
			record1.setQty(new BigDecimal(itemList.get(i).get("qty").toString()));
			record1.setPackageId(0);
			record1.setPalletId(0);
			record1.setPositionId(sourcePositionIdin1);
			record1.setReceiptType(bizStockInputTransportHead.getReceiptType());
			record1.setReferReceiptCode(bizStockInputTransportHead.getStockTransportCode());
			record1.setReferReceiptId(bizStockInputTransportHead.getStockTransportId());
			record1.setReferReceiptPid(whIdin1);
			record1.setReferReceiptRid(i+1);
			record1.setSynMes(true);
			record1.setSynSap(true);
			bizBusinessHistoryDao.insertSelective(record1);
			
			BizStockTaskHeadCw bizStockTaskHeadCw=new BizStockTaskHeadCw();
			bizStockTaskHeadCw.setStockTaskId(Integer.valueOf(itemList.get(i).get("stock_task_id").toString()));
			bizStockTaskHeadCw.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
			bizStockTaskHeadCwDao.updateByPrimaryKeySelective(bizStockTaskHeadCw);
			
			BizStockTaskItemCw bizStockTaskItemCw=new BizStockTaskItemCw();
			bizStockTaskItemCw.setItemId(Integer.valueOf(itemList.get(i).get("item_id").toString()));
			bizStockTaskItemCw.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
			bizStocktakeItemDao.updateByPrimaryKeySelective(bizStockTaskItemCw);
			
			
			
			if(!String.valueOf(itemList.get(i).get("refer_receipt_type")).equals(String.valueOf(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue())) && !String.valueOf(itemList.get(i).get("refer_receipt_type")).equals(String.valueOf(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue()))) {
				String stock_task_id=itemList.get(i).get("stock_task_id").toString();
				String other_id=bizStockInputHeadDao.getOtherId(stock_task_id);
				BizStockTaskHeadCw tranRecord=new BizStockTaskHeadCw();
				tranRecord.setStockTaskId(Integer.valueOf(other_id.toString()));
				tranRecord.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
				bizStockTaskHeadCwDao.updateByPrimaryKeySelective(tranRecord);				
			}
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
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("refer_receipt_code", itemList.get(i).get("refer_receipt_code"));
			bizStockInputHeadDao.updateTransportStatus(map);			
		}
		
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
			
			
			
			if(!String.valueOf(itemList.get(i).get("refer_receipt_type")).equals(String.valueOf(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION.getValue())) && !String.valueOf(itemList.get(i).get("refer_receipt_type")).equals(String.valueOf(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue()))) {
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
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("refer_receipt_code", itemList.get(i).get("refer_receipt_code"));
			bizStockInputHeadDao.updateTransportStatus(map);			
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
		for(int i=0;i<itemList.size();i++) {
			
			DTCWWMSMATDOCPOSTREQ.ITDOCHEAD  itdocheadtest=new DTCWWMSMATDOCPOSTREQ.ITDOCHEAD();
			itdocheadtest.setDOCNUM(bizStockInputTransportHead.getStockTransportCode().toString());
			itdocheadtest.setGOODSMVTCODE("04");
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			java.util.Date dat=new java.util.Date();  							
			itdocheadtest.setPSTNGDATE(sdf.format(dat));
			itdocheadtest.setDOCDATE(sdf.format(dat));
//			itdocheadtest.setPSTNGDATE("2018-05-22");
//			itdocheadtest.setDOCDATE("2018-05-22");
			itdochead.add(itdocheadtest);
			
			DTCWWMSMATDOCPOSTREQ.ITDOCITEM itdocitemtest=new DTCWWMSMATDOCPOSTREQ.ITDOCITEM();
			itdocitemtest.setDOCNUM(bizStockInputTransportHead.getStockTransportCode().toString());
			itdocitemtest.setDOCITEMNUM(i+"");
			itdocitemtest.setPLANT(itemList.get(i).get("output_fty_code").toString());
//			itdocitemtest.setSTGELOC(itemList.get(i).get("output_location_code").toString());
			itdocitemtest.setSTGELOC("2007");
			itdocitemtest.setMATERIAL(itemList.get(i).get("tran_mat_code").toString());
			itdocitemtest.setBATCH("1SB15");
//			itdocitemtest.setBATCH(itemList.get(i).get("tran_erp_batch").toString());
			itdocitemtest.setENTRYQNT(itemList.get(i).get("qty").toString());
			itdocitemtest.setMOVETYPE("301");
			itdocitemtest.setMOVEPLANT(itemList.get(i).get("fty_code").toString());
//			itdocitemtest.setMOVESTLOC(itemList.get(i).get("location_code").toString());
			itdocitemtest.setMOVESTLOC("2006");
//			itdocitemtest.setMOVEPLANT("SWX1");
//			itdocitemtest.setMOVESTLOC("2001");
			itdocitemtest.setMOVEMAT(itemList.get(i).get("mat_code").toString());
//			itdocitemtest.setMOVEBATCH(itemList.get(i).get("erp_batch").toString());
			itdocitemtest.setMOVEBATCH("1SB15");
			itdocitem.add(itdocitemtest);
		}		
		dtcwwmsmatdocpostreq.setItdocitem(itdocitem);
		dtcwwmsmatdocpostreq.setItdochead(itdochead);
		ErpReturn er=cwErpWsImpl.inputPost(dtcwwmsmatdocpostreq);
		return er;
	}


	@Override
	public void setHistory(BizStockInputTransportHead bizStockInputTransportHead, CurrentUser user, JSONObject json,List<Map<String,Object>> itemList) {
		
		
		for(int i=0;i<itemList.size();i++) {
			BizBusinessHistory record=new BizBusinessHistory();
			int whId=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(i).get("output_location_id").toString())).getWhId();
			if(!itemList.get(i).get("refer_receipt_type").toString().equals(EnumReceiptType.STOCK_TRANSPORT_PRODUCTION_URGENT.getValue().toString())) {
				
			
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
	
	
	/**
	 * mes同步
	 * @param head
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public WmIopRetVal takeMes(BizStockInputTransportHead bizStockInputTransportHead, List<Map<String, Object>> itemList,CurrentUser user) throws Exception{
		ArrayOfWmMvRecInteropeDto dtoSet = new ArrayOfWmMvRecInteropeDto();
		List<WmMvRecInteropeDto> wmMvRecInteropeDto = new ArrayList<WmMvRecInteropeDto>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		int j=1;
		BigDecimal bdm=BigDecimal.ZERO;
		
		
		for(int i=0;i<itemList.size();i++) {
			Map<String, Object> map1=itemList.get(i);
			if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("tran_location_id").toString())).getLocationCode().equals("6006")
					&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(map1.get("tran_fty_id").toString())).getFtyCode().equals("SWX1")) {
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
			WmMvRecInteropeDto dto =  new WmMvRecInteropeDto();
			Map<String, Object> map1=itemList.get(i);
			if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("tran_location_id").toString())).getLocationCode().equals("6006")
					&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(map1.get("tran_fty_id").toString())).getFtyCode().equals("SWX1")) {
				continue;
			}
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

			if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("location_output_id").toString())).getLocationCode().equals("6006")
					&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(map1.get("output_fty_id").toString())).getFtyCode().equals("SWX1")) {
				dto.setSrNodeCode("Z5SW042");
			}else {
				dto.setSrNodeCode(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("location_output_id").toString())).getNodeCode());
			}
			dto.setTgNodeCode(dicLocationDao.selectByPrimaryKey(Integer.valueOf(map1.get("tran_location_id").toString())).getNodeCode());
			dto.setDlvBillCode(bizStockInputTransportHead.getStockTransportCode());
			dto.setCustomer("转储入库");
			dto.setTransTypeCode("1");
			dto.setDes(bizStockInputTransportHead.getStockTransportCode());			
//			dto.setWgtDim(dicUnitMapper.selectByPrimaryKey(Integer.valueOf(map.get("unit_id").toString())).getUnitCode());
			dto.setWgtDim("TO");
			
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
		WmIopRetVal val = null;
		if(dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("output_location_id").toString())).getLocationCode().equals("6006") 
				&& !dicFactoryDao.selectByPrimaryKey(Integer.valueOf(itemList.get(0).get("output_fty_id").toString())).getFtyCode().equals("SWX1") ) {
			val =cwMesWsImpl.updateMesStock(bizStockInputTransportHead.getStockTransportCode(), "101",beginTime ,endTime, dtoSet);				
		}else {
			val =cwMesWsImpl.updateMesStock(bizStockInputTransportHead.getStockTransportCode(), "301",beginTime ,endTime, dtoSet);
		}
		if(val.getValue()==null) {
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
	public void dealBeforeStatus(String stock_input_id) {
		List<Map<String,Object>> list=bizStockInputHeadDao.selectItemIdAndRid(Integer.valueOf(stock_input_id));
		for(int i=0;i<list.size();i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", map.get("refer_receipt_id"));
			map.put("rid", map.get("refer_receipt_rid"));
			bizStockInputHeadDao.setBeforeStatus(map);
		}
		
	}


	@Override
	public void selectIsWriteOff(Integer stock_input) {
		List<Map<String, Object>> list=bizStockInputHeadDao.getViItemInfo(stock_input);
		for(int i=0;i<list.size();i++) {
			StockPositionKey key = new StockPositionKey();
			Map<String, Object> map=list.get(i);
			int whId=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(map.get("location_output_id").toString())).getWhId();
				// 上架 组盘上架
			String whCode = dictionaryService.getWhCodeByWhId(whId);
			Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
					UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
			key.setPositionId(sourcePositionId);// 仓位
			// key.setPalletId(param.getPalletId());// 托盘
			key.setPackageId(UtilObject.getIntOrZero(map.get("package_type_id")));// 包
			key.setMatId(UtilObject.getIntOrZero(map.get("mat_id"))); // 物料id,
			key.setLocationId(UtilObject.getIntOrZero(map.get("location_output_id")));// 库存地点id
			key.setBatch(UtilObject.getLongOrNull(map.get("batch"))); // 批号id
			key.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			key.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
			StockPosition stockPosition = stockPositionDao.selectByUniqueKey(key);
			//不存在
			if(stockPosition == null) {
				throw new WMSException("库存地点"+map.get("output_location_code")+"不存在仓位库存");
			//存在校验数量是否可用
			}else {			
				if(stockPosition.getQty().compareTo(new BigDecimal(map.get("qty").toString()))==-1) {
					throw new WMSException("库存地点"+map.get("output_location_code")+"仓位库存不足");
				}
			}
		}

	}


	@Override
	public BizStockInputTransportHead  submitTransport(JSONObject json, CurrentUser cUser,BizStockInputTransportHead head) throws Exception {
		JSONArray myJsonArray =JSONArray.fromObject(json.get("item_list"));
		List<Map<String,Object>> itemList = this.getMatListByID(myJsonArray);		
		head=this.insertStock(itemList,cUser,json);
		this.modifyNum(itemList,cUser,head);
		return head;		
	}
}
