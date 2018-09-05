package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
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
import com.inossem.wms.dao.biz.BizInputProduceHeadMapper;
import com.inossem.wms.dao.biz.BizPkgOperationHeadMapper;
import com.inossem.wms.dao.biz.BizPkgOperationItemMapper;
import com.inossem.wms.dao.biz.BizPkgOperationPositionMapper;
import com.inossem.wms.dao.biz.BizStockInputHeadMapper;
import com.inossem.wms.dao.biz.BizStockInputItemMapper;
import com.inossem.wms.dao.biz.BizStockInputPackageItemMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqItemMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicPackageTypeMapper;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.serial.SerialPackageMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.biz.BizBusinessHistory;
import com.inossem.wms.model.biz.BizPkgOperationHead;
import com.inossem.wms.model.biz.BizPkgOperationItem;
import com.inossem.wms.model.biz.BizPkgOperationPosition;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputItem;
import com.inossem.wms.model.biz.BizStockInputPackageItem;
import com.inossem.wms.model.biz.BizStockInputTransportHead;
import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.biz.ErpReturn;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.enums.EnumDebitCredit;
import com.inossem.wms.model.enums.EnumMatStoreType;
import com.inossem.wms.model.enums.EnumPkgOperationStatus;
import com.inossem.wms.model.enums.EnumProductOrderType;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumSequence;
import com.inossem.wms.model.enums.EnumStockOutputReturnStatus;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumStockType;
import com.inossem.wms.model.enums.EnumSynType;
import com.inossem.wms.model.enums.EnumTaskReqShippingType;
import com.inossem.wms.model.serial.SerialPackage;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IInputProduceInterfaceService;
import com.inossem.wms.service.biz.IInputProduceTransportService;
import com.inossem.wms.service.intfc.sap.CwErpWsImpl;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.wsdl.sap.input.DTCWWMSMATDOCPOSTREQ;
import com.inossem.wms.wsdl.sap.input.MSGHEAD;
import com.inossem.wms.wsdl.sap.inputc.DTCWWMSCANCELMATDOCREQ;
import com.inossem.wms.wsdl.sap.inputc.DTCWWMSCANCELMATDOCREQ.ITMATDOC;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("inputProduceInterfaceService")
@Transactional
public class InputProduceInterfaceServiceImpl implements IInputProduceInterfaceService {



	@Autowired
	private ICommonService commonService;

	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private SerialPackageMapper serialPackageDao;
	@Autowired
	private DicFactoryMapper dicFactoryDao;
	
	@Autowired
	private BizPkgOperationHeadMapper bizPkgOperationHeadDao;
	
	@Autowired
    private SequenceDAO sequenceDao;
	@Autowired
	private DicStockLocationMapper dicLocationDao;
	
    @Autowired
    private BizPkgOperationHeadMapper pkgHeadDao;

	@Autowired
	private BatchMaterialMapper batchMaterialDao;
	
	
	@Autowired
	private DicPackageTypeMapper dicPackageTypeDao;
	
	
	@Autowired
	private BizPkgOperationItemMapper pkgItemDao;
	@Autowired
	private BizPkgOperationPositionMapper pkgPositionDao;
	
	@Autowired
	private SequenceDAO sequenceDAO;

	@Autowired
	private BizStockInputHeadMapper stockInputHeadDao;

	@Autowired
	private BizStockInputItemMapper stockInputItemDao;
	
	@Autowired
	private BizStockInputPackageItemMapper bizStockInputPackageItemDao;
	
	
	@Autowired
	private DicStockLocationMapper dicStockLocationDao;
	
	
	@Autowired
	private CwErpWsImpl cwErpWsImpl;
	

	@Autowired
	private BizInputProduceHeadMapper bizInputProduceHeadDao;
	
	@Autowired
	private BizStockInputHeadMapper bizStockInputHeadMapper;
	

	@Autowired
	private BizStockTaskReqItemMapper stockTaskReqItemDao;
	
	
	@Autowired
	private BizStockTaskReqHeadMapper stockTaskReqHeadDao;
	
	@Autowired
	private BizBusinessHistoryMapper bizBusinessHistoryDao;
	




	@Override
	public BizStockInputHead insertProduction(CurrentUser user, JSONObject json) throws Exception {
		Map<String, Object> result=new HashMap<String,Object>();


		BizStockInputHead record=new BizStockInputHead();
		record.setStockInputCode(String.valueOf(sequenceDAO.selectNextVal("stock_input")));
		record.setFtyId(Integer.valueOf(json.get("fty_id").toString()));
		record.setClassTypeId(json.get("class_type_id").toString());
		record.setSysStatus(json.get("syn_type_id").toString());
		record.setCreateTime(new Date());
		record.setCreateUser(user.getUserId());
		record.setInstallationId(json.get("installation_id").toString());
		record.setProductLineId(json.get("product_line_id").toString());
		record.setPurchaseOrderCode(json.get("produce_order_code").toString());
		record.setReceiptType(EnumReceiptType.STOCK_INPUT_PRODUCE.getValue());
		record.setStatus((byte) 0);
		record.setPurchaseOrderType(json.get("business_type_id").toString());
		record.setStckType(UtilObject.getByteOrNull(json.get("stck_type")));
		record.setPurchaseOrderTypeName(EnumProductOrderType.getNameByValue(json.get("business_type_id").toString()));
		if(json.get("remark")!=null) {
			record.setRemark(json.get("remark").toString());
		}	
		if(json.containsKey("afpos") && json.get("afpos")!=null) {
			record.setAfpos(json.getString("afpos").toString());
		}
		stockInputHeadDao.insertSelective(record);
		
			
        
		JSONArray myJsonArray =JSONArray.fromObject(json.get("item_list"));	
		for(int i=0;i<myJsonArray.size();i++) {
			JSONObject obj =JSONObject.fromObject(myJsonArray.get(i));
			
			BatchMaterial batchMaterial=new BatchMaterial();
			long batch=sequenceDAO.selectNextVal("batch");
			batchMaterial.setBatch(batch);
			batchMaterial.setMatId(UtilObject.getIntOrZero(json.get("mat_id").toString()));
			batchMaterial.setFtyId(UtilObject.getIntOrZero(json.get("fty_id").toString()));
			batchMaterial.setProductionBatch(obj.getString("production_batch"));
			batchMaterial.setErpBatch(obj.getString("erp_batch"));
			batchMaterial.setQualityBatch(obj.getString("quality_batch"));
			batchMaterial.setIsDelete((byte) 0);
			batchMaterial.setCreateUser(user.getUserId());
			batchMaterial.setCreateTime(new Date());
			batchMaterial.setPackageTypeId(UtilObject.getIntOrZero(obj.getString("package_type_id").toString()));
			batchMaterial.setPurchaseOrderCode(json.get("produce_order_code").toString());
			batchMaterial.setProductLineId(UtilObject.getIntOrZero(json.get("product_line_id").toString()));
			batchMaterial.setInstallationId(UtilObject.getIntOrZero(json.get("installation_id").toString()));
			batchMaterial.setClassTypeId(Integer.valueOf(json.get("class_type_id").toString()));
			batchMaterialDao.insertSelective(batchMaterial);
			
			
			BizStockInputItem bizStockInputItem=new BizStockInputItem();
			bizStockInputItem.setMatCode(json.get("mat_code").toString());
			bizStockInputItem.setStockInputId(record.getStockInputId());
			bizStockInputItem.setStockInputRid(i+1);
			bizStockInputItem.setMatId(Integer.valueOf(json.get("mat_id").toString()));
			bizStockInputItem.setFtyId(Integer.valueOf(json.get("fty_id").toString()));
			bizStockInputItem.setOrderQty(new BigDecimal(json.get("input_stock_num").toString()));
			bizStockInputItem.setLocationId(Integer.valueOf(obj.get("location_id").toString()));
			bizStockInputItem.setUnitId(Integer.valueOf(json.get("unit_id").toString()));
			bizStockInputItem.setQty(new BigDecimal(obj.get("input_stock_num").toString()));
			bizStockInputItem.setBatch(batch);
			bizStockInputItem.setReferReceiptCode(json.get("produce_order_code").toString());			
			bizStockInputItem.setReferReceiptRid(String.valueOf(i+1));
			bizStockInputItem.setMesLocationId(UtilObject.getStringOrEmpty(obj.get("mes_location_id")));
			if(obj.get("remark")!=null) {
				bizStockInputItem.setRemark(obj.get("remark").toString());
			}
			stockInputItemDao.insertSelective(bizStockInputItem);

			
			
			BizStockInputPackageItem bizStockInputPackageItem=new BizStockInputPackageItem();
			bizStockInputPackageItem.setStockInputItemId(bizStockInputItem.getItemId());
			bizStockInputPackageItem.setErpBatch(obj.get("erp_batch").toString());
			bizStockInputPackageItem.setProductionBatch(obj.get("quality_batch").toString());
			bizStockInputPackageItem.setPackageNum(UtilObject.getIntOrZero(obj.get("package_num")));
			bizStockInputPackageItem.setPackageStandard(UtilObject.getBigDecimalOrZero(obj.get("package_standard")));
			bizStockInputPackageItem.setPackageTypeId(UtilObject.getIntOrZero(obj.get("package_type_id")));
			bizStockInputPackageItem.setQualityBatch(obj.get("quality_batch").toString());
			bizStockInputPackageItem.setQty(new BigDecimal(obj.get("input_stock_num").toString()));
			bizStockInputPackageItem.setBatch(batch);			
			if(obj.get("remark")!=null) {
				bizStockInputPackageItem.setRemark(obj.get("remark").toString());
			}				
			bizStockInputPackageItemDao.insertSelective(bizStockInputPackageItem);
			
			
		
			
			JSONArray pkgArray =JSONArray.fromObject(obj.get("ret_pkg_operation"));
			for(int j=0;j<pkgArray.size();j++) {
				JSONObject jsonObject =JSONObject.fromObject(pkgArray.get(j));
				BizPkgOperationHead  head = new BizPkgOperationHead();
		        long pkgOperationCode = sequenceDao.selectNextVal("pkg_operation");
		        head.setPkgOperationCode(String.valueOf(pkgOperationCode));
		        head.setCreateTime(new Date());     
		        head.setClassTypeId(UtilObject.getIntegerOrNull(json.get("class_type_id")));
		        head.setProductLine(UtilObject.getIntegerOrNull(json.get("product_line_id")));
		        head.setInstallation(UtilObject.getIntegerOrNull(json.get("installation_id")));
		        head.setReferReceiptCode(UtilObject.getStringOrNull(json.get("produce_order_code").toString()));
		        head.setReferReceiptId(record.getStockInputId());
		        head.setReferReceiptType(EnumReceiptType.STOCK_INPUT_PKG_OPERATION.getValue());
		        head.setPackageTeam(UtilObject.getStringOrNull(jsonObject.get("package_team")));
		        head.setStatus(EnumPkgOperationStatus.ISUSED.getValue());
		        head.setIsPallet(UtilObject.getByteOrNull(jsonObject.get("is_pallet")));
		        head.setRemark(UtilObject.getStringOrNull(jsonObject.get("remark")));
		        head.setProductBatch(UtilObject.getStringOrNull(jsonObject.get("production_batch")));
		        head.setDocumentType(EnumProductOrderType.getNameByValue(json.get("business_type_id").toString()));
		        head.setCreateUser(user.getUserId());
		        head.setModifyTime(new Date());
		        head.setIsDelete(EnumBoolean.FALSE.getValue());
		        head.setSynType(EnumSynType.MES_SAP_SYN.getValue());
		        head.setBeforeOrderCode(record.getStockInputCode());
		        head.setBeforeOrderId(record.getStockInputId());
		        head.setBeforeOrderRid(i);
		        head.setBeforeOrderType(null);       
		        this.pkgHeadDao.insertSelective(head);
		        
		        BizPkgOperationItem item = new BizPkgOperationItem();
		        item.setPkgOperationId(head.getPkgOperationId());
		        item.setPkgOperationRid(UtilObject.getIntegerOrNull(j+1));
		        item.setFtyId(UtilObject.getIntegerOrNull(json.get("fty_id")));
		        item.setBatch(batch);
		        item.setMatId(UtilObject.getIntegerOrNull(json.get("mat_id")));
		        item.setOrderQty(UtilObject.getBigDecimalOrZero(jsonObject.get("order_qty")));
		        item.setPkgQty(UtilObject.getBigDecimalOrZero(jsonObject.get("pkg_qty")));
		        item.setLocationId(UtilObject.getIntOrZero(obj.get("location_id")));
		        item.setCreateTime(new Date());
		        item.setModifyTime(new Date());
		        item.setPackageTypeId(UtilObject.getIntegerOrNull(obj.get("package_type_id")));
		        item.setIsDelete(Byte.valueOf(EnumBoolean.FALSE.getValue()));
		        pkgItemDao.insertSelective(item);  
			
//		        List<Map<String,Object>> positionList = (List<Map<String,Object>>)jsonObject.get("position_list");
//		        for(int p=0;p<positionList.size();p++) {
		        	 Map<String,Object> positionMap=new HashMap<String,Object>();
		        	 BizPkgOperationPosition position = new BizPkgOperationPosition();
		        	 position.setPkgOperationId(item.getPkgOperationId());
		        	 position.setPkgOperationRid(item.getPkgOperationRid());
//			         position.setPkgOperationPositionId(UtilObject.getIntOrZero(jsonObject.get("pkg_operation_position_id")));
		        	 position.setPkgOperationPositionId(j);
			         position.setPalletId(UtilObject.getIntOrZero(jsonObject.get("pallet_id")));
//			         int packageId=UtilObject.getIntOrZero(jsonObject.get("package_id"));
//			         position.setPackageId(packageId);
			         position.setPackageCode(UtilObject.getStringOrEmpty(jsonObject.get("package_code")));
			         position.setPackageTypeId(item.getPackageTypeId());  
			         //如果包裹id是0就是新增包裹(只有订单完成时保存到数据库)
			            
			         SerialPackage pkg = new SerialPackage();
			         pkg.setPackageCode(UtilObject.getStringOrEmpty(jsonObject.get("package_code")));
			         pkg.setModifyTime(new Date());
			         pkg.setCreateTime(new Date());
			         pkg.setPackageTypeId(item.getPackageTypeId());
			         String sup = position.getPackageCode().substring(2, 4);
			         if(sup.substring(0,1).equals("0")) {
		            	pkg.setSupplierId(UtilObject.getIntegerOrNull(sup.replaceAll("0", "")));
			         }else {
		            	pkg.setSupplierId(UtilObject.getIntegerOrNull(sup));
			         }	
			         pkg.setMatId(item.getMatId());
			         pkg.setCreateUser(head.getCreateUser());
			         pkg.setModifyUser(head.getCreateUser());
			         serialPackageDao.insertSelective(pkg);  	
			        	
			         position.setPackageId(pkg.getPackageId());	           
			         position.setCreateTime(new Date());
			         position.setModifyTime(new Date());
			         position.setIsDelete(EnumBoolean.FALSE.getValue());
			         this.pkgPositionDao.insertSelective(position); 		            	 	
//		        }		       
			}	
		}
		return record;
	}




	

	
}
