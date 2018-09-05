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

import com.inossem.wms.dao.batch.BatchMaterialMapper;
import com.inossem.wms.dao.biz.BizBusinessHistoryMapper;
import com.inossem.wms.dao.biz.BizFailMesMapper;
import com.inossem.wms.dao.biz.BizInputProduceHeadMapper;
import com.inossem.wms.dao.biz.BizPkgOperationHeadMapper;
import com.inossem.wms.dao.biz.BizStockInputHeadMapper;
import com.inossem.wms.dao.biz.BizStockInputItemMapper;
import com.inossem.wms.dao.biz.BizStockInputMesItemMapper;
import com.inossem.wms.dao.biz.BizStockInputPackageItemMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqItemMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicClassTypeMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicInstallationMapper;
import com.inossem.wms.dao.dic.DicPackageTypeMapper;
import com.inossem.wms.dao.dic.DicProductLineMapper;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.dic.DicUnitMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.biz.BizBusinessHistory;
import com.inossem.wms.model.biz.BizFailMes;
import com.inossem.wms.model.biz.BizPkgOperationHead;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputItem;
import com.inossem.wms.model.biz.BizStockInputMesItem;
import com.inossem.wms.model.biz.BizStockInputPackageItem;
import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.biz.ErpReturn;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.enums.EnumDebitCredit;
import com.inossem.wms.model.enums.EnumMatStoreType;
import com.inossem.wms.model.enums.EnumPkgOperationStatus;
import com.inossem.wms.model.enums.EnumProductOrderType;
import com.inossem.wms.model.enums.EnumRankLevel;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumSequence;
import com.inossem.wms.model.enums.EnumStckType;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumStockType;
import com.inossem.wms.model.enums.EnumTaskReqShippingType;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IInputProduceTransportService;
import com.inossem.wms.service.biz.IInputStraightProduceService;
import com.inossem.wms.service.biz.IInputVirtualProduceService;
import com.inossem.wms.service.intfc.sap.CwErpWsImpl;
import com.inossem.wms.service.intfc.sap.CwMesWsImpl;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.wsdl.mes.update.ArrayOfWmMvRecInteropeDto;
import com.inossem.wms.wsdl.mes.update.WmIopRetVal;
import com.inossem.wms.wsdl.mes.update.WmMvRecInteropeDto;
import com.inossem.wms.wsdl.sap.input.DTCWWMSMATDOCPOSTREQ;
import com.inossem.wms.wsdl.sap.input.MSGHEAD;
import com.inossem.wms.wsdl.sap.inputc.DTCWWMSCANCELMATDOCREQ;
import com.inossem.wms.wsdl.sap.inputc.DTCWWMSCANCELMATDOCREQ.ITMATDOC;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("inputStraightProduceTransportService")
@Transactional
public class InputStraightProduceServiceImpl implements IInputStraightProduceService {



	@Autowired
	private ICommonService commonService;

	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private DicProductLineMapper dicProductLineDao;
	@Autowired
	private DicFactoryMapper dicFactoryDao;
	
	@Autowired
	private BizPkgOperationHeadMapper bizPkgOperationHeadDao;
	
	@Autowired
	private BizBusinessHistoryMapper bizBusinessHistoryDao;
	
	@Autowired
	private DicStockLocationMapper dicLocationDao;
	
	@Autowired
	private DicUnitMapper unitDao;
	
	@Autowired
	private BatchMaterialMapper batchMaterialDao;
	
	@Autowired
	private BizStockInputPackageItemMapper bizStockInputPackageItemDao;
	
	@Autowired
	private DicPackageTypeMapper dicPackageTypeDao;
	
	@Autowired
	private SequenceDAO sequenceDAO;

	@Autowired
	private BizStockInputHeadMapper stockInputHeadDao;

	@Autowired
	private BizStockInputItemMapper stockInputItemDao;
	
	@Autowired
	private BizStockInputMesItemMapper bizStockInputMesItemDao;
	
	@Autowired
	private DicStockLocationMapper dicStockLocationDao;
	
	
	@Autowired
	private CwErpWsImpl cwErpWsImpl;
	
	@Autowired
	private DicClassTypeMapper dicClassTypeMapper;
	@Autowired
	private BizInputProduceHeadMapper bizInputProduceHeadDao;
	
	@Autowired
	private BizStockInputHeadMapper bizStockInputHeadMapper;
	

	@Autowired
	private BizStockTaskReqItemMapper stockTaskReqItemDao;
	
	
	@Autowired
	private BizStockTaskReqHeadMapper stockTaskReqHeadDao;
	@Autowired
	private CwMesWsImpl cwMesWsImpl;
	@Autowired
	private DicInstallationMapper dicInstallationDao;
	@Autowired
	private DicUnitMapper dicUnitMapper;
	@Autowired
	private BizFailMesMapper failMesDao;

	@Override
	public List<Map<String, Object>> production_input_list(Map<String, Object> map) {
		return bizInputProduceHeadDao.production_input_listOnPagIng(map);
	}



	@Override
	public Map<String, Object> production_input_info(String stock_input_id, String userId) {
		return bizInputProduceHeadDao.production_input_info(stock_input_id);
	}



	@Override
	public List<Map<String, Object>> selectItemList(String stock_input_id, String userId) {
		return bizInputProduceHeadDao.selectItemList(stock_input_id);
	}



//	@Override
//	public List<Map<String, Object>> selectErpList(String mat_id) {
//		return bizInputProduceHeadDao.selectErpList(mat_id);
//	}



	@Override
	public List<Map<String, Object>> selectPackageTypeList(String mat_id) {
		return bizInputProduceHeadDao.selectPackageTypeListNoSn(mat_id);
	}



//	@Override
//	public List<Map<String, Object>> selectLocationList(int fty_id) {
//		Map<String, Object> param=new HashMap<String,Object>();
//		return bizInputProduceHeadDao.selectLocationList(param);
//	}



	@Override
	public List<Map<String,Object>> selectPkgList(Map<String, Object> param) {
		return bizInputProduceHeadDao.selectPkgList(param);
	}



	@Override
	public List<Map<String, Object>> selectProduceLineList() {
		return bizInputProduceHeadDao.selectProduceLineList();
	}



	@Override
	public Map<String,Object> insertProduction(CurrentUser user, JSONObject json) throws Exception {
		Map<String, Object> result=new HashMap<String,Object>();
		JSONArray resultBatch =new JSONArray();
		JSONArray itemArray =new JSONArray(); 
		BizStockInputHead record=new BizStockInputHead();
		record.setStockInputCode(String.valueOf(sequenceDAO.selectNextVal("stock_input")));
		record.setFtyId(Integer.valueOf(json.get("fty_id").toString()));
		record.setClassTypeId(json.get("class_type_id").toString());
		record.setSysStatus(json.get("syn_type_id").toString());
		record.setCreateTime(new Date());
		record.setCreateUser(user.getUserId());
		record.setInstallationId(json.get("device_id").toString());
		record.setProductLineId(json.get("product_line_id").toString());
		record.setPurchaseOrderCode(json.get("produce_order_code").toString());
		record.setPurchaseOrderType(json.get("business_type_id").toString());
		record.setStckType(UtilObject.getByteOrNull(json.get("stck_type")));
		record.setPurchaseOrderTypeName(EnumProductOrderType.getNameByValue(UtilObject.getStringOrEmpty(json.get("business_type_id"))));
		record.setReceiptType(EnumReceiptType.STOCK_INPUT_STRAIGTH.getValue());
		record.setStatus((byte) 0);
		if(json.get("remark")!=null) {
			record.setRemark(json.get("remark").toString());
		}		
		stockInputHeadDao.insertSelective(record);
		
		
		Map<String, Object> locationMap = new HashMap<String, Object>();		
		JSONArray myJsonArray =JSONArray.fromObject(json.get("item_list"));	
		for(int i=0;i<myJsonArray.size();i++) {
			JSONObject obj =JSONObject.fromObject(myJsonArray.get(i));
			
			
	
			BatchMaterial batchMaterial=new BatchMaterial();
			long batch=sequenceDAO.selectNextVal("batch");
			batchMaterial.setBatch(batch);
			batchMaterial.setMatId(Integer.valueOf(json.get("mat_id").toString()));
			batchMaterial.setFtyId(Integer.valueOf(json.get("fty_id").toString()));
			batchMaterial.setProductionBatch(obj.getString("production_batch"));
			batchMaterial.setErpBatch(obj.getString("erp_batch"));
			batchMaterial.setQualityBatch(obj.getString("quality_batch"));
			batchMaterial.setIsDelete((byte) 0);
			batchMaterial.setCreateUser(user.getUserId());
			batchMaterial.setCreateTime(new Date());
			batchMaterial.setPackageTypeId(Integer.valueOf(obj.getString("package_type_id").toString()));
			batchMaterial.setProductLineId(Integer.valueOf(json.get("product_line_id").toString()));
			batchMaterial.setInstallationId(Integer.valueOf(json.get("device_id").toString()));
			batchMaterial.setClassTypeId(Integer.valueOf(json.get("class_type_id").toString()));
			batchMaterial.setPurchaseOrderCode(json.get("produce_order_code").toString());
			
			batchMaterialDao.insertSelective(batchMaterial);
			resultBatch.add(batch);
			
			
			BizStockInputItem bizStockInputItem=new BizStockInputItem();
			bizStockInputItem.setMatCode(json.get("mat_code").toString());
			bizStockInputItem.setStockInputId(record.getStockInputId());
			bizStockInputItem.setStockInputRid(i+1);
			bizStockInputItem.setMatId(Integer.valueOf(json.get("mat_id").toString()));
			bizStockInputItem.setOrderQty(new BigDecimal(json.get("input_stock_num").toString()));
			bizStockInputItem.setQty(new BigDecimal(obj.get("input_stock_num").toString()));
			bizStockInputItem.setFtyId(Integer.valueOf(json.get("fty_id").toString()));
			bizStockInputItem.setLocationId(Integer.valueOf(obj.get("location_id").toString()));
			bizStockInputItem.setUnitId(Integer.valueOf(json.get("unit_id").toString()));
			bizStockInputItem.setBatch(batch);
			bizStockInputItem.setReferReceiptCode(json.get("produce_order_code").toString());			
			bizStockInputItem.setReferReceiptRid(String.valueOf(i+1));
			bizStockInputItem.setMesLocationId(UtilObject.getStringOrEmpty(obj.get("mes_location_id")));
			if(obj.get("remark")!=null) {
				bizStockInputItem.setRemark(obj.get("remark").toString());
			}
//			if(!locationMap.containsKey(obj.get("location_id"))) {
				stockInputItemDao.insertSelective(bizStockInputItem);
				locationMap.put(obj.get("location_id").toString(), "1");
//			}
			itemArray.add(bizStockInputItem.getItemId());
			
			
			BizStockInputPackageItem bizStockInputPackageItem=new BizStockInputPackageItem();
			bizStockInputPackageItem.setStockInputItemId(bizStockInputItem.getItemId());
			bizStockInputPackageItem.setErpBatch(obj.get("erp_batch").toString());
			bizStockInputPackageItem.setProductionBatch(obj.get("quality_batch").toString());
			if(!"".equals(obj.get("package_num"))) {
				bizStockInputPackageItem.setPackageNum(Integer.valueOf(obj.get("package_num").toString()));
			}
			bizStockInputPackageItem.setPackageStandard(UtilObject.getBigDecimalOrZero(obj.get("package_standard")));
			bizStockInputPackageItem.setPackageTypeId(Integer.valueOf(obj.get("package_type_id").toString()));
			bizStockInputPackageItem.setQualityBatch(obj.get("quality_batch").toString());
			bizStockInputPackageItem.setQty(new BigDecimal(obj.get("input_stock_num").toString()));
			bizStockInputPackageItem.setBatch(batch);			
			if(obj.get("remark")!=null) {
				bizStockInputPackageItem.setRemark(obj.get("remark").toString());
			}				
			bizStockInputPackageItemDao.insertSelective(bizStockInputPackageItem);
			
			
		
			
			JSONArray pkgArray =JSONArray.fromObject(obj.get("ret_pkg_operation_id"));
			for(int j=0;j<pkgArray.size();j++) {
				BizPkgOperationHead head=new BizPkgOperationHead();
				head.setBeforeOrderId(bizStockInputPackageItem.getPackageTypeItemId());
				head.setBeforeOrderType(EnumReceiptType.STOCK_INPUT_STRAIGTH.getValue());
				head.setPkgOperationId(Integer.valueOf(pkgArray.get(j).toString()));
				head.setStatus(EnumPkgOperationStatus.ISUSED.getValue());
				bizPkgOperationHeadDao.updateByPrimaryKeySelective(head);
			}
			JSONArray mes_mat_List =JSONArray.fromObject(obj.get("mes_mat_list"));
			for(int j=0;j<mes_mat_List.size();j++) {
				JSONObject mesobj =JSONObject.fromObject(mes_mat_List.get(j));
				BizStockInputMesItem bizStockInputMesItem=new BizStockInputMesItem();
				bizStockInputMesItem.setMatCode(mesobj.get("mes_code").toString());
				bizStockInputMesItem.setStockInputItemId(bizStockInputItem.getItemId());
				bizStockInputMesItem.setMesBch(UtilObject.getStringOrEmpty(mesobj.get("mes_bch")));
				bizStockInputMesItem.setMesRank(UtilObject.getStringOrEmpty(mesobj.get("mes_rank")));
				bizStockInputMesItem.setMesPackageStandard(UtilObject.getBigDecimalOrZero(mesobj.get("mes_package_standard")));
				BigDecimal qty = UtilObject.getBigDecimalOrZero(mesobj.get("input_stock_num").toString());
				if(qty.compareTo(BigDecimal.ZERO) == 0) {
					continue;
				}
				bizStockInputMesItem.setQty(qty);
				bizStockInputMesItem.setMesLocationId(UtilObject.getStringOrEmpty(mesobj.get("mes_location_id")));
				bizStockInputMesItem.setMesLocationName(UtilObject.getStringOrEmpty(mesobj.get("mes_location_name")));
				if(!"".equals(obj.get("package_num"))) {
					bizStockInputMesItem.setPackageNum(mesobj.get("package_num").toString());
				}
				bizStockInputMesItemDao.insertSelective(bizStockInputMesItem);
			}
		}
		result.put("itemArray", itemArray);
		result.put("bizStockInputHead", record);
		result.put("batchArray", resultBatch);
		return result;
	}



	@Override
	public Map<String, Object> getMatInfoByCode(String mat_code) {		
		return bizInputProduceHeadDao.getMatInfoByCode(mat_code);
	}



	@Override
	public DicFactory selectFtyNameById(String ftyCode) {	
		return dicFactoryDao.selectFactoryByFtyCode(ftyCode);
	}
	
	
	
	@Override
	public Map<String, Object> getInputHeadMap(Map<String, Object> map) {
		return bizInputProduceHeadDao.getInputHeadMap(map);
	}

	@Override
	public List<Map<String, Object>> getItemMap(Map<String, Object> map) {
		return bizInputProduceHeadDao.getItemMap(map);
	}

	@Override
	public List<Map<String, Object>> getPackageList(List<String> itemlist) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("itemlist", itemlist);
		List<Map<String, Object>> list= bizInputProduceHeadDao.getPackageList(map);
		for(int i=0;i<list.size();i++) {
//			List<Map<String, Object>> packList=bizInputProduceHeadDao.getPackageOperationlist(list.get(i).get("stock_input_id").toString());
			List<Map<String, Object>> mes_mat_List=bizInputProduceHeadDao.getMesMatList(list.get(i).get("item_id").toString());
//			list.get(i).put("ret_package_list", packList);
			list.get(i).put("mes_mat_list", mes_mat_List);
		}
		return list;
	}



	@Override
	public ErpReturn takeSap(BizStockInputHead bizStockInputHead, JSONObject json) throws Exception{
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
		itdocheadtest.setDOCNUM(bizStockInputHead.getStockInputCode().toString());
		itdocheadtest.setGOODSMVTCODE("02");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		java.util.Date dat=new java.util.Date();  							
		itdocheadtest.setPSTNGDATE(sdf.format(dat));
		itdocheadtest.setDOCDATE(sdf.format(dat));
		
//		itdocheadtest.setPSTNGDATE("2018-04-28");
//		itdocheadtest.setDOCDATE("2018-04-28");
		itdochead.add(itdocheadtest);	
		JSONArray array =JSONArray.fromObject(json.get("item_list"));
		for(int i=0;i<array.size();i++) {		
			JSONObject itemList =JSONObject.fromObject(array.get(i));	
				
			 
			DTCWWMSMATDOCPOSTREQ.ITDOCITEM itdocitemtest=new DTCWWMSMATDOCPOSTREQ.ITDOCITEM();
			itdocitemtest.setDOCNUM(bizStockInputHead.getStockInputCode().toString());
			itdocitemtest.setDOCITEMNUM(i+"");
			itdocitemtest.setMATERIAL(json.get("mat_code").toString().trim());
			itdocitemtest.setPLANT(dicFactoryDao.selectByPrimaryKey(Integer.valueOf(json.get("fty_id").toString())).getFtyCode());
//			itdocitemtest.setSTGELOC("2007");
			itdocitemtest.setSTGELOC(dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getLocationCode());
//			itdocitemtest.setBATCH("P0000");
			itdocitemtest.setMOVETYPE("101");
			if(bizStockInputHead.getStckType() == EnumStckType.CHECK.getValue()) {
				itdocitemtest.setSTCKTYPE("X");
				itdocitemtest.setBATCH("X"+itemList.get("erp_batch").toString().substring(1));
			} else {
				itdocitemtest.setSTCKTYPE("");
				itdocitemtest.setBATCH(itemList.get("erp_batch").toString());
			}
			BigDecimal qty = UtilObject.getBigDecimalOrZero(UtilObject.getStringOrNull(itemList.get("input_stock_num")));
			String unitCode = unitDao.selectByPrimaryKey(UtilObject.getIntegerOrNull(json.get("unit_id"))).getUnitCode();
			if(unitCode.equals("KG")) {
				qty = qty.multiply(new BigDecimal("1"));
			}else {
				qty = qty.multiply(new BigDecimal("1000"));
			}
			itdocitemtest.setENTRYQNT(qty.toString());
			itdocitemtest.setENTRYUOM("KG");
			itdocitemtest.setITEMTEXT("订单收货");
			itdocitemtest.setMVTIND("F");
			itdocitemtest.setORDERID(bizStockInputHead.getPurchaseOrderCode());					
			itdocitem.add(itdocitemtest);
		}		
		dtcwwmsmatdocpostreq.setItdocitem(itdocitem);
		dtcwwmsmatdocpostreq.setItdochead(itdochead);
		ErpReturn er=cwErpWsImpl.inputPost(dtcwwmsmatdocpostreq);
		return er;
	}



	@Override
	public Map<String,Object> getUnitIdByCode(String string) {
		return bizInputProduceHeadDao.getUnitIdByCode(string);
	}
	
	
	public WmIopRetVal takeMes(BizStockInputHead bih, JSONObject json,CurrentUser user,JSONArray itemArray) throws Exception{
		ArrayOfWmMvRecInteropeDto dtoSet = new ArrayOfWmMvRecInteropeDto();
		List<WmMvRecInteropeDto> wmMvRecInteropeDto = new ArrayList<WmMvRecInteropeDto>();
		Map<String,Object> matMap=this.getMatInfoByCode(json.get("mat_code").toString().trim());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mes_unit_code", "吨");
		param.put("wms_unit_code", matMap.get("unit_code"));
		String rel=stockInputHeadDao.getUnitRelMesAndWms(param);
		
		int sum=1;
		JSONArray array =JSONArray.fromObject(json.get("item_list"));
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pkNumMap = new HashMap<String, Object>();
		for(int z=0;z<array.size();z++) {
			JSONObject itemList =JSONObject.fromObject(array.get(z));
			if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getLocationCode().equals("6006") ) {
				JSONArray mescodeList =JSONArray.fromObject(itemList.get("mes_mat_list"));	
				for(int k=0;k<mescodeList.size();k++) {
					JSONObject mesObj =JSONObject.fromObject(mescodeList.get(k));
					BigDecimal qty = UtilObject.getBigDecimalOrZero(mesObj.get("input_stock_num").toString());
					if(qty.compareTo(BigDecimal.ZERO) == 0) {
						continue;
					}
					if(!map.containsKey(itemList.get("package_standard").toString()+
							mesObj.get("mes_code").toString().trim()+
							itemList.get("erp_batch").toString()+
							itemList.get("location_id").toString()								
							)) {				
							map.put(itemList.get("package_standard").toString()+
									mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()
									, new BigDecimal(mesObj.get("input_stock_num").toString()).doubleValue());					
					}else {
						map.put(itemList.get("package_standard").toString()+
								mesObj.get("mes_code").toString().trim()+
								itemList.get("erp_batch").toString()+
								itemList.get("location_id").toString(), 								
								new BigDecimal(map.get(itemList.get("package_standard").toString()+
										mesObj.get("mes_code").toString().trim()+
										itemList.get("erp_batch").toString()+
										itemList.get("location_id").toString()
										).toString()).add(new BigDecimal(mesObj.get("input_stock_num").toString())).doubleValue());								
					}	
					
					if(!pkNumMap.containsKey(itemList.get("package_standard").toString()+
							mesObj.get("mes_code").toString().trim()+
							itemList.get("erp_batch").toString()+
							itemList.get("location_id").toString()								
							)) {				
						pkNumMap.put(itemList.get("package_standard").toString()+
								mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()
									, new BigDecimal(mesObj.get("package_num").toString()).doubleValue());					
					}else {
						pkNumMap.put(itemList.get("package_standard").toString()+
								mesObj.get("mes_code").toString().trim()+
								itemList.get("erp_batch").toString()+
								itemList.get("location_id").toString(), 								
								new BigDecimal(pkNumMap.get(itemList.get("package_standard").toString()+
										mesObj.get("mes_code").toString().trim()+
										itemList.get("erp_batch").toString()+
										itemList.get("location_id").toString()
										).toString()).add(new BigDecimal(mesObj.get("package_num").toString())).doubleValue());								
					}
				}
			}
		}
		Map<String, Object> m = new HashMap<String, Object>();
		
		
		for(int i=0;i<array.size();i++) {	
			
			JSONObject itemList =JSONObject.fromObject(array.get(i));
			
			if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getLocationCode().equals("6006")) {
				JSONArray mescodeList =JSONArray.fromObject(itemList.get("mes_mat_list"));			
				for(int j=0;j<mescodeList.size();j++) {	
					JSONObject mesObj =JSONObject.fromObject(mescodeList.get(j));
					BigDecimal qty = UtilObject.getBigDecimalOrZero(mesObj.get("input_stock_num").toString());
					if(qty.compareTo(BigDecimal.ZERO) == 0) {
						continue;
					}
					WmMvRecInteropeDto dto =  new WmMvRecInteropeDto();
					dto.setRecNum(sum);
					dto.setSrMtrlCode(mesObj.get("mes_code").toString().trim());
					dto.setTgMtrlCode(json.get("mat_code").toString().trim());
					dto.setWgtPerPack(Double.parseDouble(dicPackageTypeDao.selectByPrimaryKey(UtilObject.getIntegerOrNull(itemList.get("package_type_id"))).getPackageStandard().toString()));
					dto.setAmnt(new BigDecimal(mesObj.get("input_stock_num").toString()).divide(new BigDecimal(itemList.get("package_standard").toString()),0, RoundingMode.UP).intValue());
					dto.setWgt(new BigDecimal(mesObj.get("input_stock_num").toString()).divide(new BigDecimal(rel)).doubleValue());
					dto.setSrBch(UtilObject.getStringOrEmpty(mesObj.get("mes_bch")).equals("")?"00000":UtilObject.getStringOrEmpty(mesObj.get("mes_bch")));
					dto.setTgBch(itemList.get("erp_batch").toString());
					dto.setSrRankCode(UtilObject.getStringOrEmpty(mesObj.get("mes_rank")).equals("")?"-1":UtilObject.getStringOrEmpty(mesObj.get("mes_rank")));
					dto.setTgRankCode(EnumRankLevel.getRankByValue(itemList.get("erp_batch").toString()));
					dto.setSrNodeCode(bizInputProduceHeadDao.getMesCodeByMesLocationId(mesObj.get("mes_location_id").toString()));
					dto.setTgNodeCode(dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getNodeCode());
					dto.setDlvBillCode("1001");
					dto.setCustomer("生产入库");
					dto.setTransTypeCode("1");
					dto.setVehiCode(bih.getPurchaseOrderCode());
					if(UtilObject.getBigDecimalOrZero(mesObj.get("mes_package_standard")).compareTo(BigDecimal.ZERO) == 0
							&& !dicProductLineDao.selectByPrimaryKey(UtilObject.getIntOrZero(bih.getProductLineId())).getProductLineName().contains("纤维产品线")) {
						dto.setDes("bzw0000");		
					}else {
						dto.setDes(bih.getStockInputCode());	
					}		
					dto.setWgtDim("TO");
					if(map.containsKey(itemList.get("package_standard").toString()+
							mesObj.get("mes_code").toString().trim()+
							itemList.get("erp_batch").toString()+
							itemList.get("location_id").toString()											
							)) {
						if(!m.containsKey(itemList.get("package_standard").toString()+
								mesObj.get("mes_code").toString().trim()+
								itemList.get("erp_batch").toString()+
								itemList.get("location_id").toString())) {
							dto.setRecNum(sum);
							dto.setAmnt(new BigDecimal((pkNumMap.get(itemList.get("package_standard").toString()+
									mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()							
									).toString()))
									.intValue());
							dto.setWgt(new BigDecimal((map.get(itemList.get("package_standard").toString()+
									mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()							
									).toString())).divide(new BigDecimal(rel)).doubleValue());
							wmMvRecInteropeDto.add(dto);	
							sum++;
							m.put(itemList.get("package_standard").toString()+
									mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()				
									, "1");
						}											
					}	
				}
			}
//			else {
//				WmMvRecInteropeDto dto =  new WmMvRecInteropeDto();
//				dto.setRecNum(sum);
//				dto.setSrMtrlCode(json.get("mat_code").toString().trim());
//				dto.setTgMtrlCode(json.get("mat_code").toString().trim());
//				dto.setWgtPerPack(Double.parseDouble(itemList.get("package_standard").toString()));
//				dto.setAmnt(new BigDecimal(itemList.get("input_stock_num").toString()).divide(new BigDecimal(itemList.get("package_standard").toString()),0, RoundingMode.UP).intValue());
//				dto.setWgt(new BigDecimal(itemList.get("input_stock_num").toString()).divide(new BigDecimal("1000")).doubleValue());
//				dto.setSrBch(itemList.get("erp_batch").toString());
//				dto.setTgBch(itemList.get("erp_batch").toString());
//				dto.setSrRankCode(EnumRankLevel.getRankByValue(itemList.get("erp_batch").toString()));
//				dto.setTgRankCode(EnumRankLevel.getRankByValue(itemList.get("erp_batch").toString()));
//				dto.setSrNodeCode(dicInstallationDao.selectByPrimaryKey(Integer.valueOf(bih.getInstallationId())).getMesInstallationCode());
//				dto.setTgNodeCode(dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getNodeCode());
//				dto.setDlvBillCode("1001");
//				dto.setCustomer("生产入库");
//				dto.setTransTypeCode("1");
//				dto.setVehiCode(bih.getPurchaseOrderCode());
//				dto.setDes(bih.getStockInputCode());		
//				dto.setWgtDim(dicUnitMapper.selectByPrimaryKey(Integer.valueOf(json.get("unit_id").toString())).getUnitCode());
//				dto.setWgtDim("TO");
//				wmMvRecInteropeDto.add(dto);
//				sum++;
//			}
		}
		dtoSet.setWmMvRecInteropeDto(wmMvRecInteropeDto);
		
		DateFormat format = new SimpleDateFormat("HH:mm:ss");  
		int classTypeId=commonService.selectNowClassType()==null?0:commonService.selectNowClassType();
		String beginTime=format.format(dicClassTypeMapper.selectByPrimaryKey(classTypeId).getStartTime()).toString();
		String endTime=format.format(dicClassTypeMapper.selectByPrimaryKey(classTypeId).getEndTime()).toString();
		WmIopRetVal val=null;
		JSONObject itemList =JSONObject.fromObject(array.get(0));
		if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getLocationCode().equals("6006")) {
			val =cwMesWsImpl.updateMesStock(bih.getStockInputCode(), "1101", beginTime, endTime, dtoSet);
			if(val.getValue()==null) {
				BizFailMes mes = new BizFailMes();			
				mes.setReferReceiptId(UtilObject.getIntOrZero(bih.getStockInputId()));
				mes.setReferReceiptCode(UtilObject.getStringOrEmpty(bih.getStockInputCode()));
				mes.setReferReceiptType(UtilObject.getByteOrNull(bih.getReceiptType()));
				if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getLocationCode().equals("6006")) {
					mes.setBusinessType(UtilObject.getStringOrEmpty("1101"));
				}
//				else {
//					mes.setBusinessType(UtilObject.getStringOrEmpty("101"));
//				}
				mes.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
				mes.setCreateTime(new Date());
				mes.setModifyTime(new Date());
				mes.setCreateUser(user.getUserId());
				mes.setModifyUser(user.getUserId());
				mes.setErrorInfo(UtilObject.getStringOrEmpty(val.getErrmsg()));
				failMesDao.insert(mes);	
			}else {
				for(int i=0;i<itemArray.size();i++) {
					BizStockInputItem record=new BizStockInputItem();
					record.setItemId(itemArray.getInt(i));
					record.setMesDocCode(val.getValue());		
					stockInputItemDao.updateByPrimaryKeySelective(record);
				}
			}
		}
//		else {
//			val =cwMesWsImpl.updateMesStock(bih.getStockInputCode(), "101", beginTime, endTime, dtoSet);
//		}
//		
		
		
		return val;
	}
	
	
	public WmIopRetVal takeMes2(BizStockInputHead bih, JSONObject json,CurrentUser user,JSONArray itemArray) throws Exception{
		ArrayOfWmMvRecInteropeDto dtoSet = new ArrayOfWmMvRecInteropeDto();
		List<WmMvRecInteropeDto> wmMvRecInteropeDto = new ArrayList<WmMvRecInteropeDto>();
		Map<String,Object> matMap=this.getMatInfoByCode(json.get("mat_code").toString().trim());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mes_unit_code", "吨");
		param.put("wms_unit_code", matMap.get("unit_code"));
		String rel=stockInputHeadDao.getUnitRelMesAndWms(param);
		int sum=1;
		JSONArray array =JSONArray.fromObject(json.get("item_list"));
		List<String> packageIdList=bizInputProduceHeadDao.getPackageId(bih.getStockInputId().toString());
		List<String> packageStanderdList=bizInputProduceHeadDao.getPackageStanderd(bih.getStockInputId().toString());
		
		Map<String, Object> map = new HashMap<String, Object>();	
		Map<String, Object> pkNumMap = new HashMap<String, Object>();
		for(int z=0;z<array.size();z++) {
			JSONObject itemList =JSONObject.fromObject(array.get(z));
			if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getLocationCode().equals("6006")) {
				List<Map<String, Object>> mes_mat_List=bizInputProduceHeadDao.getMesMatList(itemArray.get(z).toString());
				JSONArray mescodeList =JSONArray.fromObject(mes_mat_List);	
				for(int k=0;k<mescodeList.size();k++) {
					JSONObject mesObj =JSONObject.fromObject(mescodeList.get(k));
					BigDecimal qty = UtilObject.getBigDecimalOrZero(mesObj.get("input_stock_num").toString());
					if(qty.compareTo(BigDecimal.ZERO) == 0) {
						continue;
					}
					String id=packageStanderdList.get(z);
					if(!map.containsKey(id+
							mesObj.get("mes_code").toString().trim()+
							itemList.get("erp_batch").toString()+
							itemList.get("location_id").toString()								
							)) {				
							map.put(id+
									mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()
									, new BigDecimal(mesObj.get("input_stock_num").toString()).doubleValue());					
					}else {
						map.put(id+
								mesObj.get("mes_code").toString().trim()+
								itemList.get("erp_batch").toString()+
								itemList.get("location_id").toString(), 								
								new BigDecimal(map.get(id+
										mesObj.get("mes_code").toString().trim()+
										itemList.get("erp_batch").toString()+
										itemList.get("location_id").toString()
										).toString()).add(new BigDecimal(mesObj.get("input_stock_num").toString())).doubleValue());								
					}
					
					if(!pkNumMap.containsKey(id+
							mesObj.get("mes_code").toString().trim()+
							itemList.get("erp_batch").toString()+
							itemList.get("location_id").toString()								
							)) {				
						pkNumMap.put(id+
								mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()
									, new BigDecimal(mesObj.get("package_num").toString()).doubleValue());					
					}else {
						pkNumMap.put(id+
								mesObj.get("mes_code").toString().trim()+
								itemList.get("erp_batch").toString()+
								itemList.get("location_id").toString(), 								
								new BigDecimal(pkNumMap.get(id+
										mesObj.get("mes_code").toString().trim()+
										itemList.get("erp_batch").toString()+
										itemList.get("location_id").toString()
										).toString()).add(new BigDecimal(mesObj.get("package_num").toString())).doubleValue());								
					}
				}
			}
		}
		Map<String, Object> m = new HashMap<String, Object>();
		
		for(int i=0;i<array.size();i++) {	
			WmMvRecInteropeDto dto =  new WmMvRecInteropeDto();
			JSONObject itemList =JSONObject.fromObject(array.get(i));
			if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getLocationCode().equals("6006")) {
				List<Map<String, Object>> mes_mat_List=bizInputProduceHeadDao.getMesMatList(itemArray.get(i).toString());
				JSONArray mescodeList =JSONArray.fromObject(mes_mat_List);			
				for(int j=0;j<mescodeList.size();j++) {	
					JSONObject mesObj =JSONObject.fromObject(mescodeList.get(j));
					dto.setRecNum(sum);
					dto.setSrMtrlCode(mesObj.get("mes_code").toString().trim());
					dto.setTgMtrlCode(json.get("mat_code").toString().trim());
					String id=packageStanderdList.get(i);
					dto.setWgtPerPack(Double.parseDouble(dicPackageTypeDao.selectByPrimaryKey(Integer.valueOf(packageIdList.get(i))).getPackageStandard().toString()));
					dto.setAmnt(new BigDecimal(mesObj.get("input_stock_num").toString()).divide(new BigDecimal(itemList.get("package_standard").toString()),0, RoundingMode.UP).intValue());
					dto.setWgt(new BigDecimal(mesObj.get("input_stock_num").toString()).divide(new BigDecimal(rel)).doubleValue());
					dto.setSrBch(UtilObject.getStringOrEmpty(mesObj.get("mes_bch")).equals("")?"00000":UtilObject.getStringOrEmpty(mesObj.get("mes_bch")));
					dto.setTgBch(itemList.get("erp_batch").toString());
					dto.setSrRankCode(UtilObject.getStringOrEmpty(mesObj.get("mes_rank")).equals("")?"-1":UtilObject.getStringOrEmpty(mesObj.get("mes_rank")));
					dto.setTgRankCode(EnumRankLevel.getRankByValue(itemList.get("erp_batch").toString()));
					dto.setSrNodeCode(bizInputProduceHeadDao.getMesCodeByMesLocationId(mesObj.get("mes_location_id").toString()));
					dto.setTgNodeCode(dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getNodeCode());
					dto.setDlvBillCode("1001");
					dto.setCustomer("生产入库");
					dto.setTransTypeCode("1");
					dto.setVehiCode(bih.getPurchaseOrderCode());
					if(UtilObject.getBigDecimalOrZero(mesObj.get("mes_package_standard")).compareTo(BigDecimal.ZERO) == 0
							&& !dicProductLineDao.selectByPrimaryKey(UtilObject.getIntOrZero(bih.getProductLineId())).getProductLineName().contains("纤维产品线")) {
						dto.setDes("bzw0000");		
					}else {
						dto.setDes(bih.getStockInputCode());	
					}	
					dto.setWgtDim("TO");
					if(map.containsKey(id+
							mesObj.get("mes_code").toString().trim()+
							itemList.get("erp_batch").toString()+
							itemList.get("location_id").toString()											
							)) {
						if(!m.containsKey(id+
								mesObj.get("mes_code").toString().trim()+
								itemList.get("erp_batch").toString()+
								itemList.get("location_id").toString())) {
							dto.setRecNum(sum);
							dto.setAmnt(new BigDecimal((pkNumMap.get(id+
									mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()							
									).toString()))
									.intValue());
							dto.setWgt(new BigDecimal((map.get(id+
									mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()							
									).toString())).divide(new BigDecimal(rel)).doubleValue());
							wmMvRecInteropeDto.add(dto);	
							sum++;
							m.put(id+
									mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()				
									, "1");
						}											
					}
				}
			}
//			else {
//				dto.setRecNum(sum);
//				dto.setSrMtrlCode(json.get("mat_code").toString().trim());
//				dto.setTgMtrlCode(json.get("mat_code").toString().trim());
//				String id=selectPackageId(itemList.getString("package_type_id").toString());
//				dto.setWgtPerPack(Double.parseDouble(dicPackageTypeDao.selectByPrimaryKey(Integer.valueOf(id)).getPackageStandard().toString()));
//				dto.setAmnt(new BigDecimal(itemList.get("input_stock_num").toString()).divide(new BigDecimal(dicPackageTypeDao.selectByPrimaryKey(Integer.valueOf(id)).getPackageStandard().toString()),0, RoundingMode.UP).intValue());
//				dto.setWgt(new BigDecimal(itemList.get("input_stock_num").toString()).divide(new BigDecimal("1000")).doubleValue());
//				dto.setSrBch(itemList.get("erp_batch").toString());
//				dto.setTgBch(itemList.get("erp_batch").toString());
//				dto.setSrRankCode(EnumRankLevel.getRankByValue(itemList.get("erp_batch").toString()));
//				dto.setTgRankCode(EnumRankLevel.getRankByValue(itemList.get("erp_batch").toString()));
//				dto.setSrNodeCode(dicInstallationDao.selectByPrimaryKey(Integer.valueOf(bih.getInstallationId())).getMesInstallationCode());
//				dto.setTgNodeCode(dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getNodeCode());
//				dto.setDlvBillCode("1001");
//				dto.setCustomer("生产入库");
//				dto.setTransTypeCode("1");
//				dto.setVehiCode(bih.getPurchaseOrderCode());
//				dto.setDes(bih.getStockInputCode());		
////				dto.setWgtDim(dicUnitMapper.selectByPrimaryKey(Integer.valueOf(json.get("unit_id").toString())).getUnitCode());
//				dto.setWgtDim("TO");
//				wmMvRecInteropeDto.add(dto);
//				sum++;
//			}
		}
		dtoSet.setWmMvRecInteropeDto(wmMvRecInteropeDto);
		DateFormat format = new SimpleDateFormat("HH:mm:ss");  
		int classTypeId=commonService.selectNowClassType()==null?0:commonService.selectNowClassType();
		String beginTime=format.format(dicClassTypeMapper.selectByPrimaryKey(classTypeId).getStartTime()).toString();
		String endTime=format.format(dicClassTypeMapper.selectByPrimaryKey(classTypeId).getEndTime()).toString();
		WmIopRetVal val=null;
		JSONObject itemList =JSONObject.fromObject(array.get(0));
		if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getLocationCode().equals("6006")) {
			val =cwMesWsImpl.updateMesStock(bih.getStockInputCode(), "1101", beginTime, endTime, dtoSet);
			if(val.getValue()==null) {
				BizFailMes mes = new BizFailMes();
				mes.setReferReceiptId(UtilObject.getIntOrZero(bih.getStockInputId()));
				mes.setReferReceiptCode(UtilObject.getStringOrEmpty(bih.getStockInputCode()));
				mes.setReferReceiptType(UtilObject.getByteOrNull(bih.getReceiptType()));
				if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getLocationCode().equals("6006")) {
					mes.setBusinessType(UtilObject.getStringOrEmpty("1101"));
				}
//				else {
//					mes.setBusinessType(UtilObject.getStringOrEmpty("101"));
//				}
				mes.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
				mes.setCreateTime(new Date());
				mes.setModifyTime(new Date());
				mes.setCreateUser(user.getUserId());
				mes.setModifyUser(user.getUserId());
				mes.setErrorInfo(UtilObject.getStringOrEmpty(val.getErrmsg()));
				failMesDao.insert(mes);	
			}else {
				for(int i=0;i<itemArray.size();i++) {
					BizStockInputItem record=new BizStockInputItem();
					record.setItemId(itemArray.getInt(i));
					record.setMesDocCode(val.getValue());		
					stockInputItemDao.updateByPrimaryKeySelective(record);
				}
			}
		}
//		else {
//			val =cwMesWsImpl.updateMesStock(bih.getStockInputCode(), "101", beginTime, endTime, dtoSet);
//		}
		
		
		
		return val;
	}
	
	
	
	public WmIopRetVal takeMesAgain(BizStockInputHead bih, JSONObject json,CurrentUser user,JSONArray itemArray) throws Exception{
		ArrayOfWmMvRecInteropeDto dtoSet = new ArrayOfWmMvRecInteropeDto();
		List<WmMvRecInteropeDto> wmMvRecInteropeDto = new ArrayList<WmMvRecInteropeDto>();
		List<Map<String,Object>> list=bizInputProduceHeadDao.selectByStockTransportId(bih.getStockInputId());
		List<String> packageIdList=bizInputProduceHeadDao.getPackageId(bih.getStockInputId().toString());
		List<String> packageStanderdList=bizInputProduceHeadDao.getPackageStanderd(bih.getStockInputId().toString());
		int sum=1;
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pkNumMap = new HashMap<String, Object>();
		for(int z=0;z<list.size();z++) {
			JSONObject itemList =JSONObject.fromObject(list.get(z));
			if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getLocationCode().equals("6006") ) {
				List<Map<String, Object>> mes_mat_List=bizInputProduceHeadDao.getMesMatList(itemArray.get(z).toString());	
				JSONArray mescodeList =JSONArray.fromObject(mes_mat_List);
				for(int k=0;k<mescodeList.size();k++) {
					JSONObject mesObj =JSONObject.fromObject(mescodeList.get(k));
					BigDecimal qty = UtilObject.getBigDecimalOrZero(mesObj.get("input_stock_num").toString());
					if(qty.compareTo(BigDecimal.ZERO) == 0) {
						continue;
					}
					Map<String,Object> matMap=this.getMatInfoByCode(itemList.get("mat_code").toString().trim());
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("mes_unit_code", "吨");
					param.put("wms_unit_code", matMap.get("unit_code"));
					String rel=this.getUnitRelMesAndWms(param);
					String id=packageStanderdList.get(z);
					if(!map.containsKey(id+
							mesObj.get("mes_code").toString().trim()+
							itemList.get("erp_batch").toString()+
							itemList.get("location_id").toString()								
							)) {				
							map.put(id+
									mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()
									, new BigDecimal(mesObj.get("input_stock_num").toString()).doubleValue());					
					}else {
						map.put(id+
								mesObj.get("mes_code").toString().trim()+
								itemList.get("erp_batch").toString()+
								itemList.get("location_id").toString(), 								
								new BigDecimal(map.get(id+
										mesObj.get("mes_code").toString().trim()+
										itemList.get("erp_batch").toString()+
										itemList.get("location_id").toString()
										).toString()).add(new BigDecimal(mesObj.get("input_stock_num").toString())).doubleValue());								
					}	
					
					
					if(!pkNumMap.containsKey(id+
							mesObj.get("mes_code").toString().trim()+
							itemList.get("erp_batch").toString()+
							itemList.get("location_id").toString()								
							)) {				
						pkNumMap.put(id+
								mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()
									, new BigDecimal(mesObj.get("package_num").toString()).doubleValue());					
					}else {
						pkNumMap.put(id+
								mesObj.get("mes_code").toString().trim()+
								itemList.get("erp_batch").toString()+
								itemList.get("location_id").toString(), 								
								new BigDecimal(pkNumMap.get(id+
										mesObj.get("mes_code").toString().trim()+
										itemList.get("erp_batch").toString()+
										itemList.get("location_id").toString()
										).toString()).add(new BigDecimal(mesObj.get("package_num").toString())).doubleValue());								
					}
				}
			}
		}
		Map<String, Object> m = new HashMap<String, Object>();
		
		for(int i=0;i<list.size();i++) {	
			
			JSONObject itemList =JSONObject.fromObject(list.get(i));
			if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getLocationCode().equals("6006")) {
				List<Map<String, Object>> mes_mat_List=bizInputProduceHeadDao.getMesMatList(itemArray.get(i).toString());
				JSONArray mescodeList =JSONArray.fromObject(mes_mat_List);			
				for(int j=0;j<mescodeList.size();j++) {	
					JSONObject mesObj =JSONObject.fromObject(mescodeList.get(j));
					BigDecimal qty = UtilObject.getBigDecimalOrZero(mesObj.get("input_stock_num").toString());
					if(qty.compareTo(BigDecimal.ZERO) == 0) {
						continue;
					}
					WmMvRecInteropeDto dto =  new WmMvRecInteropeDto();
					dto.setRecNum(sum);
					dto.setSrMtrlCode(mesObj.get("mes_code").toString().trim());
					dto.setTgMtrlCode(itemList.get("mat_code").toString().trim());
					Map<String,Object> matMap=this.getMatInfoByCode(itemList.get("mat_code").toString().trim());
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("mes_unit_code", "吨");
					param.put("wms_unit_code", matMap.get("unit_code"));
					String rel=stockInputHeadDao.getUnitRelMesAndWms(param);
					String id=packageStanderdList.get(i);
					dto.setWgtPerPack(Double.parseDouble(dicPackageTypeDao.selectByPrimaryKey(Integer.valueOf(packageIdList.get(i))).getPackageStandard().toString()));
					dto.setAmnt(new BigDecimal(mesObj.get("input_stock_num").toString()).divide(new BigDecimal(itemList.get("package_standard").toString()),0, RoundingMode.UP).intValue());
					dto.setWgt(new BigDecimal(mesObj.get("input_stock_num").toString()).divide(new BigDecimal(rel)).doubleValue());
					dto.setSrBch(UtilObject.getStringOrEmpty(mesObj.get("mes_bch")).equals("")?"00000":UtilObject.getStringOrEmpty(mesObj.get("mes_bch")));
					dto.setTgBch(itemList.get("erp_batch").toString());
					dto.setSrRankCode(UtilObject.getStringOrEmpty(mesObj.get("mes_rank")).equals("")?"-1":UtilObject.getStringOrEmpty(mesObj.get("mes_rank")));
					dto.setTgRankCode(EnumRankLevel.getRankByValue(itemList.get("erp_batch").toString()));
					dto.setSrNodeCode(bizInputProduceHeadDao.getMesCodeByMesLocationId(mesObj.get("mes_location_id").toString()));
					dto.setTgNodeCode(dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getNodeCode());
					dto.setDlvBillCode("1001");
					dto.setCustomer("生产入库");
					dto.setTransTypeCode("1");
					dto.setVehiCode(bih.getPurchaseOrderCode());
					if(UtilObject.getBigDecimalOrZero(mesObj.get("mes_package_standard")).compareTo(BigDecimal.ZERO) == 0
							&& !dicProductLineDao.selectByPrimaryKey(UtilObject.getIntOrZero(bih.getProductLineId())).getProductLineName().contains("纤维产品线")) {
						dto.setDes("bzw0000");		
					}else {
						dto.setDes(bih.getStockInputCode());	
					}	
					dto.setWgtDim("TO");
					if(map.containsKey(id+
							mesObj.get("mes_code").toString().trim()+
							itemList.get("erp_batch").toString()+
							itemList.get("location_id").toString()											
							)) {
						if(!m.containsKey(id+
								mesObj.get("mes_code").toString().trim()+
								itemList.get("erp_batch").toString()+
								itemList.get("location_id").toString())) {
							dto.setRecNum(sum);
							dto.setAmnt(new BigDecimal((pkNumMap.get(id+
									mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()							
									).toString()))
									.intValue());
							dto.setWgt(new BigDecimal((map.get(id+
									mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()							
									).toString())).divide(new BigDecimal(rel)).doubleValue());
							wmMvRecInteropeDto.add(dto);	
							sum++;
							m.put(id+
									mesObj.get("mes_code").toString().trim()+
									itemList.get("erp_batch").toString()+
									itemList.get("location_id").toString()				
									, "1");
						}											
					}
				}
			}
//			else {
//				WmMvRecInteropeDto dto =  new WmMvRecInteropeDto();
//				dto.setRecNum(sum);
//				dto.setSrMtrlCode(itemList.get("mat_code").toString().trim());
//				dto.setTgMtrlCode(itemList.get("mat_code").toString().trim());
//				String id=itemList.getString("package_type_id").toString();
//				dto.setWgtPerPack(Double.parseDouble(dicPackageTypeDao.selectByPrimaryKey(Integer.valueOf(id)).getPackageStandard().toString()));
//				dto.setAmnt(new BigDecimal(itemList.get("qty").toString()).divide(new BigDecimal(dicPackageTypeDao.selectByPrimaryKey(Integer.valueOf(id)).getPackageStandard().toString()),0, RoundingMode.UP).intValue());
//				dto.setWgt(new BigDecimal(itemList.get("qty").toString()).divide(new BigDecimal("1000")).doubleValue());
//				dto.setSrBch(itemList.get("erp_batch").toString());
//				dto.setTgBch(itemList.get("erp_batch").toString());
//				dto.setSrRankCode(EnumRankLevel.getRankByValue(itemList.get("erp_batch").toString()));
//				dto.setTgRankCode(EnumRankLevel.getRankByValue(itemList.get("erp_batch").toString()));
//				dto.setSrNodeCode(dicInstallationDao.selectByPrimaryKey(Integer.valueOf(bih.getInstallationId())).getMesInstallationCode());
//				dto.setTgNodeCode(dicLocationDao.selectByPrimaryKey(Integer.valueOf(itemList.get("location_id").toString())).getNodeCode());
//				dto.setDlvBillCode("1001");
//				dto.setCustomer("生产入库");
//				dto.setTransTypeCode("1");
//				dto.setVehiCode(bih.getPurchaseOrderCode());
//				dto.setDes(bih.getStockInputCode());		
////				dto.setWgtDim(dicUnitMapper.selectByPrimaryKey(Integer.valueOf(json.get("unit_id").toString())).getUnitCode());
//				dto.setWgtDim("TO");
//				wmMvRecInteropeDto.add(dto);
//				sum++;
//			}
		}
		dtoSet.setWmMvRecInteropeDto(wmMvRecInteropeDto);
		DateFormat format = new SimpleDateFormat("HH:mm:ss");  
		int classTypeId=commonService.selectNowClassType()==null?0:commonService.selectNowClassType();
		String beginTime=format.format(dicClassTypeMapper.selectByPrimaryKey(classTypeId).getStartTime()).toString();
		String endTime=format.format(dicClassTypeMapper.selectByPrimaryKey(classTypeId).getEndTime()).toString();
		WmIopRetVal val=null;
		if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(list.get(0).get("location_id").toString())).getLocationCode().equals("6006")) {
			val =cwMesWsImpl.updateMesStock(bih.getStockInputCode(), "1101", beginTime, endTime, dtoSet);

			if(val.getValue()==null) {
				BizFailMes mes = new BizFailMes();
				mes.setReferReceiptId(UtilObject.getIntOrZero(bih.getStockInputId()));
				mes.setReferReceiptCode(UtilObject.getStringOrEmpty(bih.getStockInputCode()));
				mes.setReferReceiptType(UtilObject.getByteOrNull(bih.getReceiptType()));
				if(!dicLocationDao.selectByPrimaryKey(Integer.valueOf(list.get(0).get("location_id").toString())).getLocationCode().equals("6006")) {
					mes.setBusinessType(UtilObject.getStringOrEmpty("1101"));
				}
//				else {
//					mes.setBusinessType(UtilObject.getStringOrEmpty("101"));
//				}
				mes.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
				mes.setCreateTime(new Date());
				mes.setModifyTime(new Date());
				mes.setCreateUser(user.getUserId());
				mes.setModifyUser(user.getUserId());
				mes.setErrorInfo(UtilObject.getStringOrEmpty(val.getErrmsg()));
				failMesDao.insert(mes);	
			}else {
				for(int i=0;i<itemArray.size();i++) {
					BizStockInputItem record=new BizStockInputItem();
					record.setItemId(itemArray.getInt(i));
					record.setMesDocCode(val.getValue());		
					stockInputItemDao.updateByPrimaryKeySelective(record);
				}
			}
		}
//		else {
//			val =cwMesWsImpl.updateMesStock(bih.getStockInputCode(), "101", beginTime, endTime, dtoSet);
//		}
		
		
		return val;
	}


	public String getUnitRelMesAndWms(Map<String, Object> param) {
		return stockInputHeadDao.getUnitRelMesAndWms(param);
	}

	@Override
	public void modifyNum(BizStockInputHead bizStockInputHead,CurrentUser user, JSONObject json,JSONArray batchArray,JSONArray itemArray) throws Exception {


		JSONArray myJsonArray =JSONArray.fromObject(json.get("item_list"));	
		for(int i=0;i<myJsonArray.size();i++) {						
			JSONObject obj =JSONObject.fromObject(myJsonArray.get(i));			
			Map<String, Object> stockBatchMap=new HashMap<String,Object>();
			stockBatchMap.put("matId", json.get("mat_id"));
			stockBatchMap.put("locationId",obj.get("location_id"));
			stockBatchMap.put("batch", batchArray.get(i));
			stockBatchMap.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			stockBatchMap.put("createUserId", user.getUserId());
			stockBatchMap.put("debitCredit", "S");
			stockBatchMap.put("qty", obj.get("input_stock_num"));
			stockBatchMap.put("stockTypeId",EnumStockType.STOCK_TYPE_DIRECT.getValue());
			commonService.modifyStockBatch(stockBatchMap);
			StockPosition stockPosition=new StockPosition();
//			if (dicPackageTypeDao.selectByPrimaryKey(Integer.valueOf(obj.getString("package_type_id"))).getSnUsed().equals(EnumMatStoreType.USEING.getValue())) {
//				JSONArray pkgArray =JSONArray.fromObject(obj.get("ret_pkg_operation_id"));
//				for(int j=0;j<pkgArray.size();j++) {				
//					List<Map<String,Object>> pkgPositionList=bizInputProduceHeadDao.getPkgPosition(pkgArray.get(j).toString());			
//					for(int u=0;u<pkgPositionList.size();u++) {
//						StockPosition stockPositionIn=new StockPosition();
//						int whId=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(obj.get("location_id").toString())).getWhId();
//						stockPositionIn.setWhId(whId);
//						// 上架 组盘上架
//						String whCode = dictionaryService.getWhCodeByWhId(whId);
//						Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
//								UtilProperties.getInstance().getDefaultCCQ());
//						Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
//								UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
//		
//						stockPositionIn.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
//						stockPositionIn.setAreaId(sourceAreaId);
//						stockPositionIn.setPositionId(sourcePositionId);
//						stockPositionIn.setMatId(Integer.valueOf(json.get("mat_id").toString()));
//						stockPositionIn.setFtyId(Integer.valueOf(json.get("fty_id").toString()));
//						stockPositionIn.setLocationId(Integer.valueOf(obj.get("location_id").toString()));
//						stockPositionIn.setBatch(new Long(batchArray.get(i).toString()));
//						stockPositionIn.setPackageTypeId(Integer.valueOf(obj.get("package_type_id").toString()));
//						stockPositionIn.setInputDate(new Date());
//						stockPositionIn.setQty(new BigDecimal(pkgPositionList.get(u).get("package_standard").toString()));
//						stockPositionIn.setUnitId(Integer.valueOf(json.get("unit_id").toString()));
//						stockPositionIn.setInputDate(new Date());
//						stockPositionIn.setStockTypeId(EnumStockType.STOCK_TYPE_DIRECT.getValue());
//						stockPositionIn.setPalletId(Integer.valueOf(pkgPositionList.get(u).get("pallet_id").toString()));					
//						stockPositionIn.setPackageId(Integer.valueOf(pkgPositionList.get(u).get("package_id").toString()));
//						commonService.modifyStockPosition(stockPositionIn);	
//						
//						
//						BizBusinessHistory record=new BizBusinessHistory();
//				
//						record.setAreaId(sourceAreaId);
//						record.setBatch(new Long(batchArray.get(i).toString()));
//						record.setBusinessType((byte) 1);
//						record.setCreateTime(new Date());
//						record.setCreateUser(user.getUserId());
//						record.setDebitCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
//						record.setFtyId(bizStockInputHead.getFtyId());
//						record.setLocationId(bizStockInputHead.getLocationId());
//						record.setMatId(Integer.valueOf(json.get("mat_id").toString()));
//						record.setModifyTime(new Date());
//						record.setModifyUser(user.getUserId());
//						record.setQty(new BigDecimal(pkgPositionList.get(u).get("package_standard").toString()));
//						record.setPackageId(Integer.valueOf(pkgPositionList.get(u).get("package_id").toString()));
//						record.setPalletId(Integer.valueOf(pkgPositionList.get(u).get("pallet_id").toString()));
//						record.setPositionId(sourcePositionId);
//						record.setReceiptType(bizStockInputHead.getReceiptType());
//						record.setReferReceiptCode(bizStockInputHead.getStockInputCode());
//						record.setReferReceiptId(bizStockInputHead.getStockInputId());
//						record.setReferReceiptPid(whId);
//						record.setReferReceiptRid(i+1);
//						record.setSynMes(true);
//						record.setSynSap(true);
//						bizBusinessHistoryDao.insertSelective(record);	
//				}				
//			}			
//		}else {
			// 不启用包
			int whId=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(obj.get("location_id").toString())).getWhId();
			stockPosition.setWhId(whId);
			// 上架 组盘上架
			String whCode = dictionaryService.getWhCodeByWhId(whId);
			Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
					UtilProperties.getInstance().getDefaultCCQ());
			Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
					UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());

			stockPosition.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
			stockPosition.setAreaId(sourceAreaId);
			stockPosition.setPositionId(sourcePositionId);
			stockPosition.setMatId(Integer.valueOf(json.get("mat_id").toString()));
			stockPosition.setFtyId(Integer.valueOf(json.get("fty_id").toString()));
			stockPosition.setLocationId(Integer.valueOf(obj.get("location_id").toString()));
			stockPosition.setBatch(new Long(batchArray.get(i).toString()));
			stockPosition.setPackageTypeId(Integer.valueOf(obj.get("package_type_id").toString()));
			stockPosition.setInputDate(new Date());
			stockPosition.setQty(new BigDecimal(obj.get("input_stock_num").toString()));
			stockPosition.setUnitId(Integer.valueOf(json.get("unit_id").toString()));
			stockPosition.setInputDate(new Date());
			stockPosition.setPackageId(0);
			stockPosition.setPalletId(0);
			stockPosition.setStockTypeId(EnumStockType.STOCK_TYPE_DIRECT.getValue());
			stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			commonService.modifyStockPosition(stockPosition);	
			
			
		
			BizBusinessHistory record=new BizBusinessHistory();
			record.setAreaId(sourceAreaId);
			record.setBatch(new Long(batchArray.get(i).toString()));
			record.setBusinessType((byte) 1);
			record.setCreateTime(new Date());
			record.setQty(new BigDecimal(obj.get("input_stock_num").toString()));				
			record.setDebitCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
			record.setFtyId(bizStockInputHead.getFtyId());
			record.setLocationId(Integer.valueOf(obj.get("location_id").toString()));
			record.setMatId(Integer.valueOf(json.get("mat_id").toString()));
			record.setModifyTime(new Date());
			record.setModifyUser(user.getUserId());
			record.setCreateUser(user.getUserId());
			record.setPackageId(0);
			record.setPalletId(0);
			record.setPositionId(sourcePositionId);
			record.setReceiptType(bizStockInputHead.getReceiptType());
			record.setReferReceiptCode(bizStockInputHead.getStockInputCode());
			record.setReferReceiptId(bizStockInputHead.getStockInputId());
			record.setReferReceiptPid(whId);
			record.setReferReceiptRid(i+1);
			record.setSynMes(true);
			record.setSynSap(true);
			bizBusinessHistoryDao.insertSelective(record);	
//		}
		}
		BizStockInputHead bih=new BizStockInputHead();
		bih.setStockInputId(bizStockInputHead.getStockInputId());
		bih.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
		bih.setDocTime(new Date());
		bih.setPostingDate(new Date());
		stockInputHeadDao.updateByPrimaryKeySelective(bih);
		
		ErpReturn er=this.takeSap(bizStockInputHead,json);	
		if("S".equals(er.getType())) {
			for(int i=0;i<itemArray.size();i++) {
				BizStockInputItem record=new BizStockInputItem();
				record.setItemId(itemArray.getInt(i));
				record.setMatDocCode(er.getMatDocCode());			
				record.setMatDocRid(i+1);
				record.setMacDocYear(er.getDocyear());
				stockInputItemDao.updateByPrimaryKeySelective(record);
			}
		}else {
			 throw new WMSException(er.getMessage());
		}
		
	}
	
	
	
	
	
	@Override
	public void modifyNum2(BizStockInputHead bizStockInputHead,CurrentUser user, JSONObject json,JSONArray batchArray,JSONArray itemArray) throws Exception {


		JSONArray myJsonArray =JSONArray.fromObject(json.get("item_list"));	
		List<String> packageIdList=bizInputProduceHeadDao.getPackageId(bizStockInputHead.getStockInputId().toString());
		for(int i=0;i<myJsonArray.size();i++) {						
			JSONObject obj =JSONObject.fromObject(myJsonArray.get(i));			
			Map<String, Object> stockBatchMap=new HashMap<String,Object>();
			stockBatchMap.put("matId", json.get("mat_id"));
			stockBatchMap.put("locationId",obj.get("location_id"));
			stockBatchMap.put("batch", batchArray.get(i));
			stockBatchMap.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			stockBatchMap.put("createUserId", user.getUserId());
			stockBatchMap.put("debitCredit", "S");
			stockBatchMap.put("qty", obj.get("input_stock_num"));
			stockBatchMap.put("stockTypeId",EnumStockType.STOCK_TYPE_DIRECT.getValue());
			commonService.modifyStockBatch(stockBatchMap);
			StockPosition stockPosition=new StockPosition();
			String id=packageIdList.get(i);
			if (dicPackageTypeDao.selectByPrimaryKey(Integer.valueOf(id)).getSnUsed().equals(EnumMatStoreType.USEING.getValue())) {
				JSONArray pkgArray =JSONArray.fromObject(obj.get("ret_pkg_operation_id"));
				for(int j=0;j<pkgArray.size();j++) {				
					List<Map<String,Object>> pkgPositionList=bizInputProduceHeadDao.getPkgPosition(pkgArray.get(j).toString());			
					for(int u=0;u<pkgPositionList.size();u++) {
						StockPosition stockPositionIn=new StockPosition();
						int whId=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(obj.get("location_id").toString())).getWhId();
						stockPositionIn.setWhId(whId);
						// 上架 组盘上架
						String whCode = dictionaryService.getWhCodeByWhId(whId);
						Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
								UtilProperties.getInstance().getDefaultCCQ());
						Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
								UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
		
						stockPositionIn.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
						stockPositionIn.setAreaId(sourceAreaId);
						stockPositionIn.setPositionId(sourcePositionId);
						stockPositionIn.setMatId(Integer.valueOf(json.get("mat_id").toString()));
						stockPositionIn.setFtyId(Integer.valueOf(json.get("fty_id").toString()));
						stockPositionIn.setLocationId(Integer.valueOf(obj.get("location_id").toString()));
						stockPositionIn.setBatch(new Long(batchArray.get(i).toString()));
						stockPositionIn.setPackageTypeId(Integer.valueOf(id.toString()));
						stockPositionIn.setInputDate(new Date());
						stockPositionIn.setQty(new BigDecimal(pkgPositionList.get(u).get("package_standard").toString()));
						stockPositionIn.setUnitId(Integer.valueOf(json.get("unit_id").toString()));
						stockPositionIn.setInputDate(new Date());
						stockPositionIn.setStockTypeId(EnumStockType.STOCK_TYPE_DIRECT.getValue());
						stockPositionIn.setPalletId(Integer.valueOf(pkgPositionList.get(u).get("pallet_id").toString()));					
						stockPositionIn.setPackageId(Integer.valueOf(pkgPositionList.get(u).get("package_id").toString()));
						commonService.modifyStockPosition(stockPositionIn);		
						
						BizBusinessHistory record=new BizBusinessHistory();
						
						record.setAreaId(sourceAreaId);
						record.setBatch(new Long(batchArray.get(i).toString()));
						record.setBusinessType((byte) 1);
						record.setCreateTime(new Date());
						record.setCreateUser(user.getUserId());
						record.setDebitCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
						record.setFtyId(bizStockInputHead.getFtyId());
						record.setLocationId(Integer.valueOf(obj.get("location_id").toString()));
						record.setMatId(Integer.valueOf(json.get("mat_id").toString()));
						record.setQty(new BigDecimal(pkgPositionList.get(u).get("package_standard").toString()));
						record.setModifyTime(new Date());
						record.setModifyUser(user.getUserId());
						record.setPackageId(Integer.valueOf(id));
						record.setPalletId(Integer.valueOf(pkgPositionList.get(u).get("pallet_id").toString()));
						record.setPositionId(sourcePositionId);
						record.setReceiptType(bizStockInputHead.getReceiptType());
						record.setReferReceiptCode(bizStockInputHead.getStockInputCode());
						record.setReferReceiptId(bizStockInputHead.getStockInputId());
						record.setReferReceiptPid(whId);
						record.setReferReceiptRid(i+1);
						record.setSynMes(true);
						record.setSynSap(true);
						bizBusinessHistoryDao.insertSelective(record);	
				}				
			}			
		}else {
			// 不启用包
			int whId=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(obj.get("location_id").toString())).getWhId();
			stockPosition.setWhId(whId);
			// 上架 组盘上架
			String whCode = dictionaryService.getWhCodeByWhId(whId);
			Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
					UtilProperties.getInstance().getDefaultCCQ());
			Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
					UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());

			stockPosition.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
			stockPosition.setAreaId(sourceAreaId);
			stockPosition.setPositionId(sourcePositionId);
			stockPosition.setMatId(Integer.valueOf(json.get("mat_id").toString()));
			stockPosition.setFtyId(Integer.valueOf(json.get("fty_id").toString()));
			stockPosition.setLocationId(Integer.valueOf(obj.get("location_id").toString()));
			stockPosition.setBatch(new Long(batchArray.get(i).toString()));
			stockPosition.setPackageTypeId(Integer.valueOf(id.toString()));
			stockPosition.setInputDate(new Date());
			stockPosition.setQty(new BigDecimal(obj.get("input_stock_num").toString()));
			stockPosition.setUnitId(Integer.valueOf(json.get("unit_id").toString()));
			stockPosition.setInputDate(new Date());
			stockPosition.setPackageId(0);
			stockPosition.setPalletId(0);
			stockPosition.setStockTypeId(EnumStockType.STOCK_TYPE_DIRECT.getValue());
			stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			commonService.modifyStockPosition(stockPosition);	
			
			
			BizBusinessHistory record=new BizBusinessHistory();
			record.setAreaId(sourceAreaId);
			record.setBatch(new Long(batchArray.get(i).toString()));
			record.setBusinessType((byte) 1);
			record.setCreateTime(new Date());
			record.setCreateUser(user.getUserId());
			record.setDebitCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
			record.setQty(new BigDecimal(obj.get("input_stock_num").toString()));
			record.setFtyId(bizStockInputHead.getFtyId());
			record.setLocationId(Integer.valueOf(obj.get("location_id").toString()));
			record.setMatId(Integer.valueOf(json.get("mat_id").toString()));
			record.setModifyTime(new Date());
			record.setModifyUser(user.getUserId());
			record.setPackageId(0);
			record.setPalletId(0);
			record.setPositionId(sourcePositionId);
			record.setReceiptType(bizStockInputHead.getReceiptType());
			record.setReferReceiptCode(bizStockInputHead.getStockInputCode());
			record.setReferReceiptId(bizStockInputHead.getStockInputId());
			record.setReferReceiptPid(whId);
			record.setReferReceiptRid(i+1);
			record.setSynMes(true);
			record.setSynSap(true);
			bizBusinessHistoryDao.insertSelective(record);	
		}
	
		}
		
		BizStockInputHead bih=new BizStockInputHead();
		bih.setStockInputId(bizStockInputHead.getStockInputId());
		bih.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
		bih.setDocTime(new Date());
		bih.setPostingDate(new Date());
		stockInputHeadDao.updateByPrimaryKeySelective(bih);
		ErpReturn er=this.takeSap(bizStockInputHead,json);	
		if("S".equals(er.getType())) {
			for(int i=0;i<itemArray.size();i++) {
				BizStockInputItem record=new BizStockInputItem();
				record.setItemId(itemArray.getInt(i));
				record.setMatDocCode(er.getMatDocCode());			
				record.setMatDocRid(i+1);
				record.setMacDocYear(er.getDocyear());
				stockInputItemDao.updateByPrimaryKeySelective(record);
			}
		}else {
			 throw new WMSException(er.getMessage());
		}
		
	}



	@Override
	public Map<String,Object>  getstockInputHeadAndBatch(String string) {
		Map<String,Object> map=new HashMap<String,Object>();
		BizStockInputHead bizStockInputHead= stockInputHeadDao.selectByPrimaryKey(Integer.valueOf(string));
		List<String> list=bizInputProduceHeadDao.selectItemByInputId(bizStockInputHead.getStockInputId());
		List<String> itemArray=bizInputProduceHeadDao.selectItemIdByInputId(bizStockInputHead.getStockInputId());
		map.put("bizStockInputHead", bizStockInputHead);
		map.put("list", list);
		map.put("itemArray", JSONArray.fromObject(itemArray));
	
		return map;
		
	}



	@Override
	public void updateMatDocCode(ErpReturn er, JSONArray itemArray) {
		for(int i=0;i<itemArray.size();i++) {
			BizStockInputItem record=new BizStockInputItem();
			record.setItemId(itemArray.getInt(i));
			record.setMatDocCode(er.getMatDocCode());			
			record.setMatDocRid(i+1);
			record.setMacDocYear(er.getDocyear());
			stockInputItemDao.updateByPrimaryKeySelective(record);
		}		
	}



	@Override
	public ErpReturn takeSapWriteOff(JSONArray item_id, JSONArray doc_year) throws Exception {
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
			
			ITMATDOC.setMATERIALDOCUMENT(stockInputItemDao.selectByPrimaryKey(Integer.valueOf(item_id.getString(i))).getMatDocCode());
			ITMATDOC.setMATDOCUMENTYEAR(doc_year.getString(i));
			ITMATDOC.setPSTNGDATE(date1);
			String rid = String.valueOf(i+1);
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
	public void updateStockToWriteOff(int stock_input_id, CurrentUser cuser) throws Exception {
		BizStockInputHead bizStockInputTransportHead=new BizStockInputHead();
		bizStockInputTransportHead.setStockInputId(stock_input_id);
		bizStockInputTransportHead.setStatus((byte) 20);
		bizStockInputHeadMapper.updateByPrimaryKeySelective(bizStockInputTransportHead);
		
		BizStockInputHead bizStockInputHead=bizStockInputHeadMapper.selectByPrimaryKey(stock_input_id);
		String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());
		//保存作业请求头部信息
		BizStockTaskReqHead taskReqHead = new BizStockTaskReqHead();
		taskReqHead.setStockTaskReqCode(stockTaskReqCode);
		taskReqHead.setWhId(0);
		taskReqHead.setFtyId(UtilObject.getIntegerOrNull(bizStockInputHead.getFtyId()));
		taskReqHead.setLocationId(UtilObject.getIntegerOrNull(bizStockInputHead.getLocationId()));
		taskReqHead.setMatDocId(0);
		taskReqHead.setMatDocYear(0);
		taskReqHead.setShippingType(EnumTaskReqShippingType.STOCK_INPUT.getValue());
		taskReqHead.setStatus(EnumBoolean.FALSE.getValue());
		taskReqHead.setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue());
		taskReqHead.setReferReceiptId(bizStockInputHead.getStockInputId());
		taskReqHead.setReferReceiptCode(bizStockInputHead.getStockInputCode());
		taskReqHead.setReferReceiptType(UtilObject.getByteOrNull(bizStockInputHead.getReceiptType()));
		taskReqHead.setCreateUser(cuser.getUserId());
		taskReqHead.setCreateTime(new Date());
		taskReqHead.setModifyTime(new Date());
		stockTaskReqHeadDao.insertSelective(taskReqHead);
		
		
		
		
		List<Map<String,Object>> list=bizInputProduceHeadDao.selectByStockTransportId(stock_input_id);
		
		for(int i=0;i<list.size();i++) {
			Map<String,Object> item = list.get(i);
			BizStockTaskReqItem taskReqItem = new BizStockTaskReqItem();
			taskReqItem.setFtyId(UtilObject.getIntOrZero(item.get("fty_id")));
			taskReqItem.setLocationId(UtilObject.getIntOrZero(item.get("location_id")));
			taskReqItem.setMatId(UtilObject.getIntegerOrNull(item.get("mat_id")));
			taskReqItem.setStockTaskQty(BigDecimal.ZERO);
			taskReqItem.setQty(UtilObject.getBigDecimalOrZero(item.get("qty")));
			taskReqItem.setBatch(UtilObject.getLongOrNull(item.get("batch")));
			Map<String, Object> param = new HashMap<String,Object>();
			param.put("batch", item.get("batch").toString());
			param.put("code", bizStockInputHead.getPurchaseOrderCode());
			Map<String,Object> map= bizInputProduceHeadDao.getErpBatchByBatch(param);
			taskReqItem.setErpBatch(UtilObject.getStringOrEmpty(map.get("erp_batch")));
//			taskReqItem.setProductionBatch(UtilObject.getStringOrEmpty(item.get("production_batch")));
//			taskReqItem.setQualityBatch(UtilObject.getStringOrEmpty(item.get("quality_batch")));			
			taskReqItem.setUnitId(UtilObject.getIntOrZero(item.get("unit_id")));
			
//			taskReqItem.setPackageTypeId(UtilObject.getIntOrZero(item.get("package_type_id")));
			taskReqItem.setReferReceiptType(bizStockInputHead.getReceiptType());
//			taskReqItem.setReferReceiptId(bizStockInputTransportHead.getStockInputId());
			taskReqItem.setReferReceiptCode(item.get("refer_receipt_code").toString());
			taskReqItem.setReferReceiptRid(UtilObject.getIntOrZero(item.get("refer_receipt_rid")));
			
			taskReqItem.setStockTaskReqId(taskReqHead.getStockTaskReqId());
			taskReqItem.setStockTaskReqRid(UtilObject.getIntOrZero(i));
			taskReqItem.setIsDelete(EnumBoolean.FALSE.getValue());
			taskReqItem.setStatus(EnumBoolean.FALSE.getValue());
			taskReqItem.setWhId(0);
			taskReqItem.setCreateTime(new Date());
			taskReqItem.setModifyTime(new Date());
			stockTaskReqItemDao.insertSelective(taskReqItem);
		}
		
	}



	@Override
	public void updateWriteOffStatus(JSONArray item_id, int stock_input_id) {
		BizStockInputHead bizStockInputHead=new BizStockInputHead();
		bizStockInputHead.setStockInputId(stock_input_id);
		bizStockInputHead.setStatus(EnumReceiptStatus.RECEIPT_WRITEOFF.getValue());
		stockInputHeadDao.updateByPrimaryKeySelective(bizStockInputHead);
		
		for(int i=0;i<item_id.size();i++) {
			BizStockInputItem record=new BizStockInputItem();
			record.setItemId(Integer.valueOf(item_id.get(i).toString()));
			record.setStatus(EnumReceiptStatus.RECEIPT_WRITEOFF.getValue());
			stockInputItemDao.updateByPrimaryKeySelective(record);		
		}
		
	}



	@Override
	public String selectPackageId(String name) {
		return bizInputProduceHeadDao.selectPackageId(name);
	}
	@Override
	public void updateStockToWriteOff(int stock_input_id, CurrentUser cuser,JSONObject json) throws Exception {
		
		
		
		JSONArray myJsonArray =JSONArray.fromObject(json.get("item_list"));	
		
		List<Map<String,Object>> list=bizInputProduceHeadDao.selectByStockTransportId(stock_input_id);
		
		
		Map<String,Object> locationIdMap = new HashMap<String,Object>();
		BizStockTaskReqHead taskReqHead = new BizStockTaskReqHead();
		for(int i=0;i<list.size();i++) {
			Map<String,Object> item = list.get(i);
			
			
			BizStockInputHead bizStockInputTransportHead=new BizStockInputHead();
			bizStockInputTransportHead.setStockInputId(stock_input_id);
			bizStockInputTransportHead.setStatus(EnumReceiptStatus.RECEIPT_WRITEOFF.getValue());
			bizStockInputHeadMapper.updateByPrimaryKeySelective(bizStockInputTransportHead);
			
			BizStockInputHead bizStockInputHead=bizStockInputHeadMapper.selectByPrimaryKey(stock_input_id);
			String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());
			//保存作业请求头部信息
			
			taskReqHead.setStockTaskReqCode(stockTaskReqCode);
		
			taskReqHead.setFtyId(UtilObject.getIntegerOrNull(bizStockInputHead.getFtyId()));
			int wh=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(item.get("location_id").toString())).getWhId();
			taskReqHead.setWhId(wh);
			taskReqHead.setLocationId(Integer.valueOf(item.get("location_id").toString()));
			taskReqHead.setMatDocId(0);
			taskReqHead.setMatDocYear(0);
			taskReqHead.setShippingType(EnumTaskReqShippingType.STOCK_OUTPUT.getValue());
			taskReqHead.setStatus(EnumBoolean.FALSE.getValue());
			taskReqHead.setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue());
			taskReqHead.setReferReceiptId(bizStockInputHead.getStockInputId());
			taskReqHead.setReferReceiptCode(bizStockInputHead.getStockInputCode());
			taskReqHead.setReferReceiptType(UtilObject.getByteOrNull(bizStockInputHead.getReceiptType()));
			taskReqHead.setCreateUser(cuser.getUserId());
			taskReqHead.setCreateTime(new Date());
			taskReqHead.setModifyTime(new Date());
			
			if(!locationIdMap.containsKey(item.get("location_id"))) {
				taskReqHead.setStockTaskReqId(null);
				stockTaskReqHeadDao.insertSelective(taskReqHead);
				locationIdMap.put(item.get("location_id").toString(), "1");
			}
			
			
			
			
			
			BizStockTaskReqItem taskReqItem = new BizStockTaskReqItem();
			taskReqItem.setFtyId(UtilObject.getIntOrZero(bizStockInputHead.getFtyId()));
			taskReqItem.setLocationId(UtilObject.getIntOrZero(item.get("location_id")));
			taskReqItem.setMatId(UtilObject.getIntegerOrNull(item.get("mat_id")));
			taskReqItem.setStockTaskQty(BigDecimal.ZERO);
			taskReqItem.setQty(UtilObject.getBigDecimalOrZero(item.get("qty")));
			taskReqItem.setBatch(UtilObject.getLongOrNull(item.get("batch")));
			
			Map<String, Object> param = new HashMap<String,Object>();
			param.put("batch", item.get("batch").toString());
			param.put("code", bizStockInputHead.getPurchaseOrderCode());
			Map<String,Object> map= bizInputProduceHeadDao.getErpBatchByBatch(param);
			
			taskReqItem.setErpBatch(UtilObject.getStringOrEmpty(map.get("erp_batch")));			
			taskReqItem.setProductionBatch(UtilObject.getStringOrEmpty(item.get("production_batch")));
			taskReqItem.setQualityBatch(UtilObject.getStringOrEmpty(item.get("quality_batch")));			
			taskReqItem.setUnitId(UtilObject.getIntOrZero(item.get("unit_id")));
			
			taskReqItem.setPackageTypeId(UtilObject.getIntOrZero(item.get("package_type_id")));
			taskReqItem.setReferReceiptType(bizStockInputHead.getReceiptType());
			taskReqItem.setReferReceiptId(bizStockInputTransportHead.getStockInputId());
			taskReqItem.setReferReceiptCode(item.get("refer_receipt_code").toString());
			taskReqItem.setReferReceiptRid(UtilObject.getIntOrZero(item.get("refer_receipt_rid")));
			
			taskReqItem.setStockTaskReqId(taskReqHead.getStockTaskReqId());
			taskReqItem.setStockTaskReqRid(UtilObject.getIntOrZero(i+1));
			taskReqItem.setIsDelete(EnumBoolean.FALSE.getValue());
			taskReqItem.setStatus(EnumBoolean.FALSE.getValue());
			taskReqItem.setWhId(dicStockLocationDao.selectByPrimaryKey(UtilObject.getIntOrZero(item.get("location_id"))).getWhId());
			taskReqItem.setCreateTime(new Date());
			taskReqItem.setModifyTime(new Date());
			stockTaskReqItemDao.insertSelective(taskReqItem);
			
			
			
				
			Map<String, Object> stockBatchMap=new HashMap<String,Object>();
			stockBatchMap.put("matId", item.get("mat_id"));
			stockBatchMap.put("locationId",item.get("location_id"));
			stockBatchMap.put("batch", item.get("batch"));
			stockBatchMap.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			stockBatchMap.put("createUserId", cuser.getUserId());
			stockBatchMap.put("debitCredit", EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			stockBatchMap.put("qty", item.get("qty"));
			stockBatchMap.put("stockTypeId",EnumStockType.STOCK_TYPE_DIRECT.getValue());
			commonService.modifyStockBatch(stockBatchMap);
			StockPosition stockPosition=new StockPosition();
			JSONObject obj =JSONObject.fromObject(myJsonArray.get(i));		
			if (dicPackageTypeDao.selectByPrimaryKey(Integer.valueOf(item.get("package_type_id").toString())).getSnUsed().equals(EnumMatStoreType.USEING.getValue())) {
				JSONArray pkgArray =JSONArray.fromObject(obj.get("ret_package_list"));
				for(int j=0;j<pkgArray.size();j++) {
					
					bizInputProduceHeadDao.updateStatusById(Integer.valueOf(pkgArray.get(j).toString()));
					
					List<Map<String,Object>> pkgPositionList=bizInputProduceHeadDao.getPkgPosition(pkgArray.get(j).toString());			
					for(int u=0;u<pkgPositionList.size();u++) {
						StockPosition stockPositionIn=new StockPosition();
						int whId=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(item.get("location_id").toString())).getWhId();
						stockPositionIn.setWhId(whId);
						// 上架 组盘上架
						String whCode = dictionaryService.getWhCodeByWhId(whId);
						Integer sourceAreaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode,
								UtilProperties.getInstance().getDefaultCCQ());
						Integer sourcePositionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode,
								UtilProperties.getInstance().getDefaultCCQ(), UtilProperties.getInstance().getDefaultCW());
		
						stockPositionIn.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
						stockPositionIn.setAreaId(sourceAreaId);
						stockPositionIn.setPositionId(sourcePositionId);
						stockPositionIn.setMatId(Integer.valueOf(item.get("mat_id").toString()));
						stockPositionIn.setFtyId(bizStockInputHead.getFtyId());
						stockPositionIn.setLocationId(Integer.valueOf(item.get("location_id").toString()));
						stockPositionIn.setBatch(new Long(item.get("batch").toString()));
						stockPositionIn.setPackageTypeId(Integer.valueOf(item.get("package_type_id").toString()));
						stockPositionIn.setInputDate(new Date());
						stockPositionIn.setQty(new BigDecimal(pkgPositionList.get(u).get("package_standard").toString()));
						stockPositionIn.setUnitId(Integer.valueOf(item.get("unit_id").toString()));
						stockPositionIn.setInputDate(new Date());
						stockPositionIn.setStockTypeId(EnumStockType.STOCK_TYPE_DIRECT.getValue());
						stockPositionIn.setPalletId(Integer.valueOf(pkgPositionList.get(u).get("pallet_id").toString()));					
						stockPositionIn.setPackageId(Integer.valueOf(pkgPositionList.get(u).get("package_id").toString()));
						stockPositionIn.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
						commonService.modifyStockPosition(stockPositionIn);	
						
						
						
						BizBusinessHistory record=new BizBusinessHistory();
						record.setAreaId(sourceAreaId);
						record.setBatch(new Long(item.get("batch").toString()));
						record.setBusinessType((byte) 1);
						record.setCreateTime(new Date());
						record.setCreateUser(cuser.getUserId());
						record.setDebitCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
						record.setFtyId(UtilObject.getIntOrZero(bizStockInputHead.getFtyId()));
						record.setLocationId(UtilObject.getIntOrZero(item.get("location_id")));
						record.setMatId(UtilObject.getIntegerOrNull(item.get("mat_id")));
						record.setQty(new BigDecimal(pkgPositionList.get(u).get("package_standard").toString()));
						record.setModifyTime(new Date());
						record.setModifyUser(cuser.getUserId());
//						record.setPackageId(Integer.valueOf(id));
//						record.setPalletId(Integer.valueOf(pkgPositionList.get(u).get("pallet_id").toString()));
						
						record.setPositionId(sourcePositionId);
						record.setReceiptType(bizStockInputHead.getReceiptType());
						record.setReferReceiptCode(bizStockInputHead.getStockInputCode());
						record.setReferReceiptId(bizStockInputHead.getStockInputId());
						record.setReferReceiptPid(whId);
						record.setReferReceiptRid(i+1);
						record.setSynMes(true);
						record.setSynSap(true);
						bizBusinessHistoryDao.insertSelective(record);	
				}				
			}			
		}else {
			// 不启用包
			int whId=dicStockLocationDao.selectByPrimaryKey(Integer.valueOf(item.get("location_id").toString())).getWhId();
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
			stockPosition.setMatId(Integer.valueOf(item.get("mat_id").toString()));
			stockPosition.setFtyId(bizStockInputHead.getFtyId());
			stockPosition.setLocationId(Integer.valueOf(item.get("location_id").toString()));
			stockPosition.setBatch(new Long(item.get("batch").toString()));
			stockPosition.setPackageTypeId(Integer.valueOf(item.get("package_type_id").toString()));
			stockPosition.setInputDate(new Date());
			stockPosition.setQty(new BigDecimal(item.get("qty").toString()));
			stockPosition.setUnitId(Integer.valueOf(item.get("unit_id").toString()));
			stockPosition.setInputDate(new Date());
			stockPosition.setPackageId(0);
			stockPosition.setPalletId(0);
			stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			stockPosition.setStockTypeId(EnumStockType.STOCK_TYPE_DIRECT.getValue());
			commonService.modifyStockPosition(stockPosition);
			
			
			
			
			BizBusinessHistory record=new BizBusinessHistory();
			record.setAreaId(sourceAreaId);
			record.setBatch(new Long(item.get("batch").toString()));
			record.setBusinessType((byte) 1);
			record.setCreateTime(new Date());
			record.setCreateUser(cuser.getUserId());
			record.setDebitCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			record.setFtyId(UtilObject.getIntOrZero(item.get("fty_id")));
			record.setLocationId(UtilObject.getIntOrZero(item.get("location_id")));
			record.setMatId(UtilObject.getIntegerOrNull(item.get("mat_id")));
			record.setQty(new BigDecimal(UtilObject.getBigDecimalOrZero(item.get("qty")).toString()));
			record.setModifyTime(new Date());
			record.setModifyUser(cuser.getUserId());
//			record.setPackageId(Integer.valueOf(id));
//			record.setPalletId(Integer.valueOf(pkgPositionList.get(u).get("pallet_id").toString()));
			record.setPositionId(sourcePositionId);
			record.setReceiptType(bizStockInputHead.getReceiptType());
			record.setReferReceiptCode(bizStockInputHead.getStockInputCode());
			record.setReferReceiptId(bizStockInputHead.getStockInputId());
			record.setReferReceiptPid(whId);
			record.setReferReceiptRid(i+1);
			record.setSynMes(true);
			record.setSynSap(true);
			bizBusinessHistoryDao.insertSelective(record);	
			}	
		}
	}

	
}
