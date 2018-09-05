package com.inossem.wms.service.intfc.sap;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.inossem.wms.constant.Const;
import com.inossem.wms.dao.stock.StockBatchMapper;
import com.inossem.wms.dao.stock.StockPositionMapper;
import com.inossem.wms.dao.syn.SynMaterialDocHeadMapper;
import com.inossem.wms.dao.syn.SynMaterialDocItemMapper;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.syn.SynMaterialDocHead;
import com.inossem.wms.model.syn.SynMaterialDocItem;
import com.inossem.wms.model.vo.DicFactoryVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.intfc.IStockCheck;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilREST;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("stockCheckImpl")
public class StockCheckImpl implements IStockCheck {

	@Resource
	private IDictionaryService dictionaryService;
	@Resource
	private SynMaterialDocHeadMapper synMDocHeadDao;
	@Resource
	private SynMaterialDocItemMapper synMDocItemDao;
	@Resource
	private StockBatchMapper stockBatchDao;
	@Resource
	private ICommonService commonService;
	
	/**
	 * 生成IMPORT
	 * @param typeNum
	 * @param userId
	 * @return
	 */
	private JSONObject setImport(String typeNum,String userId){
		JSONObject I_IMPORT = new JSONObject();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dateStr = sdf.format(new Date());
		
		I_IMPORT.put("ZTYPE",  typeNum);	// 接口类型
		I_IMPORT.put("ZERNAM", userId);		// 用户id
		I_IMPORT.put("ZIMENO", "");			// 设备IMENO
		I_IMPORT.put("ZDATE",  dateStr.substring(0, 10));	// 传输日期
		I_IMPORT.put("ZTIME",  dateStr.substring(11, 19));	// 传输时间
		
		return I_IMPORT;
	}
	
	/**
	 * 保存head
	 * @return
	 * @throws ParseException 
	 */
	private SynMaterialDocHead saveMatDocHead(JSONObject sapObj,SimpleDateFormat sdf0) throws ParseException{
		SynMaterialDocHead synMatDocHead = new SynMaterialDocHead();
		synMatDocHead.setMatDocCode(UtilObject.getStringOrEmpty(sapObj.get("MBLNR")));
		synMatDocHead.setMatDocYear(UtilObject.getIntegerOrNull(sapObj.get("MJAHR")));
		
		String docTime = UtilObject.getStringOrEmpty(sapObj.get("BLDAT"));
		if (docTime != null && !"".equals(docTime)) {
			synMatDocHead.setMatDocTime(sdf0.parse(docTime));
		}
		String postTime = UtilObject.getStringOrEmpty(sapObj.getString("BUDAT"));
		if (postTime != null && !"".equals(postTime)) {
			synMatDocHead.setPostingTime(sdf0.parse(postTime));
		}
		synMDocHeadDao.insertSelective(synMatDocHead);
		
		return synMatDocHead;
	}
	
	/**
	 * 保存item
	 * @param sapObj
	 */
	private void saveMatDocItem(JSONObject sapObj,Map<String,Integer> headMap){
		SynMaterialDocItem synMatDocItem = new SynMaterialDocItem();
		synMatDocItem.setMatDocId(headMap.get(UtilObject.getStringOrEmpty(sapObj.get("MBLNR"))));
		synMatDocItem.setMatDocRid(UtilObject.getIntegerOrNull(sapObj.get("ZEILE")));
		synMatDocItem.setMoveTypeId(dictionaryService.getMoveTypeIdByMoveTypeCodeAndSpecStock(UtilObject.getStringOrEmpty(sapObj.get("BWART")), UtilObject.getStringOrEmpty(sapObj.get("SOBKZ"))));
		
		synMatDocItem.setMatId(dictionaryService.getMatIdByMatCode(UtilObject.getStringOrEmpty(sapObj.get("MATNR"))));
		synMatDocItem.setMatCode(UtilObject.getStringOrEmpty(sapObj.get("MATNR")));
		synMatDocItem.setMatName(UtilObject.getStringOrEmpty(sapObj.get("MAKTX")));
		
		synMatDocItem.setStockStatus(UtilObject.getStringOrEmpty(sapObj.get("INSMK")));
		
		synMatDocItem.setSpecStock(UtilObject.getStringOrEmpty(sapObj.get("SOBKZ")));
		synMatDocItem.setSpecStockCode(UtilObject.getStringOrEmpty(sapObj.get("ZSONUM")));
		synMatDocItem.setSpecStockName(UtilObject.getStringOrEmpty(sapObj.get("ZSODIS")));
		
		synMatDocItem.setQty(UtilObject.getBigDecimalOrZero(sapObj.get("MENGE")));
		
		synMatDocItem.setUnitId(dictionaryService.getUnitIdByUnitCode(UtilObject.getStringOrEmpty(sapObj.get("MEINS"))));
		
		synMatDocItem.setFtyId(dictionaryService.getFtyIdByFtyCode(UtilObject.getStringOrEmpty(sapObj.get("WERKS"))));
		synMatDocItem.setLocationId(dictionaryService.getLocIdByFtyCodeAndLocCode(UtilObject.getStringOrEmpty(sapObj.get("WERKS")), 
																				  UtilObject.getStringOrEmpty(sapObj.get("LGORT"))));
		synMatDocItem.setReserveId(UtilObject.getStringOrEmpty(sapObj.get("RSNUM")));
		synMatDocItem.setReserveRid(UtilObject.getStringOrEmpty(sapObj.get("RSPOS")));
		synMatDocItem.setCreateUser(UtilObject.getStringOrEmpty(sapObj.get("USNAM")));
		synMatDocItem.setSupplierCode(UtilObject.getStringOrEmpty(sapObj.get("LIFNR")));
		synMatDocItem.setSupplierName(UtilObject.getStringOrEmpty(sapObj.get("VNAME1")));
		synMatDocItem.setCustomerCode(UtilObject.getStringOrEmpty(sapObj.get("KUNNR")));
		synMatDocItem.setCustomerName(UtilObject.getStringOrEmpty(sapObj.get("CNAME1")));
		synMatDocItem.setSaleOrderCode(UtilObject.getStringOrEmpty(sapObj.get("VBELN")));
		synMatDocItem.setSaleOrderPrjectCode(UtilObject.getStringOrEmpty(sapObj.get("KDPOS")));
		synMatDocItem.setSaleOrderDeliveredPlan(UtilObject.getStringOrEmpty(sapObj.get("KDEIN")));
		synMatDocItem.setDebitCredit(UtilObject.getStringOrEmpty(sapObj.get("SHKZG")));
		
		synMDocItemDao.insertSelective(synMatDocItem);
	}
	
	@Override
	public boolean syncSapMatDoc(String ftyId,String locationId,String sDate,String eDate,String userId) throws Exception {
		
		/***************************************
		 * IMPORT
		 ***************************************/
		JSONObject I_IMPORT = this.setImport("13", userId);
		
		/***************************************
		 * I_BLDAT
		 ***************************************/
		SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");

		if("".equals(sDate)||sDate==null){
			sDate = sdf0.format(new Date());
		}
		if("".equals(eDate)||eDate==null){
			eDate = sdf0.format(new Date());
		}
		
		JSONArray bldats = new JSONArray();
		JSONObject bldat = new JSONObject();
		bldat.put("SIGN", "I");
		bldat.put("OPTION", "BT");
		bldat.put("LOW",  sDate);
		bldat.put("HIGH", eDate);
		bldats.add(bldat);
		
		JSONObject params = new JSONObject();
		params.put("I_IMPORT", I_IMPORT);
		params.put("I_BLDAT", bldats);
		
		/***************************************
		 * I_WERKS I_LGORT
		 ***************************************/
		if(!"".equals(ftyId)&&ftyId!=null){
			params.put("I_WERKS", dictionaryService.getFtyCodeByFtyId(Integer.parseInt(ftyId)));
		}
		if(!"".equals(locationId)&&locationId!=null){
			params.put("I_LGORT", dictionaryService.getLocCodeByLocId(Integer.parseInt(locationId)));
		}
		
		JSONObject result = UtilREST.executePostJSONTimeOut( Const.SAP_API_URL + "int_13?sap-client=" + Const.SAP_API_CLIENT, params,30);
		
		if (result != null && result.getString("RETURNVALUE").equalsIgnoreCase("S")) {
			Map<String,Integer> headMap = new HashMap<String,Integer>();
			
			JSONArray headAry = result.getJSONArray("HEAD");
			for (Object object : headAry) {
				SynMaterialDocHead synMatDocHead = this.saveMatDocHead((JSONObject) object, sdf0);
				headMap.put(synMatDocHead.getMatDocCode(), synMatDocHead.getMatDocId());
			}

			JSONArray itemAry = result.getJSONArray("ITEM");
			for (Object object : itemAry) {
				this.saveMatDocItem((JSONObject) object,headMap);
			}
		}
		
		synMDocItemDao.updateMatDocSapToPolymeric();//更新WMS中没有的凭证号的状态
		Map<String,Object> searchKey = new HashMap<String,Object>();
		searchKey.put("isNew", "1");
		searchKey.put("debitCredit", "S");
		//查询wms中不存在的sap凭证S
		List<Map<String,Object>> matDocSList= synMDocItemDao.selectMatDocInfoByParams(searchKey);
		
		//查询wms中不存在的sap凭证H
		searchKey.put("debitCredit", "H");
		List<Map<String,Object>> matDocHList= synMDocItemDao.selectMatDocInfoByParams(searchKey);
	
		List<Map<String,Object>> polymericList = new ArrayList<Map<String,Object>>();
		
		String ftyIdTemp = "";//工厂
		String locationIdTemp = "";//库存地点
		String specStockTemp = "";//特殊库存
		String matIdTemp = "";//物料
		BigDecimal qtyTemp = new BigDecimal("0.0");
		
		//循环以工厂 库存地点 特殊库存 数量来对 凭证配对
		for(Map<String,Object> matDocS : matDocSList){
			
			ftyIdTemp = UtilObject.getStringOrEmpty(matDocS.get("fty_id"));
			locationIdTemp = UtilObject.getStringOrEmpty(matDocS.get("location_id"));
			specStockTemp = UtilObject.getStringOrEmpty(matDocS.get("spec_stock"));
			qtyTemp = UtilObject.getBigDecimalOrZero(matDocS.get("qty"));
			matIdTemp = UtilObject.getStringOrEmpty(matDocS.get("mat_id"));
			for(int j=0; j<matDocHList.size(); j++){
				Map<String,Object> matDocH = matDocHList.get(j);
				
				if(matIdTemp.equals(UtilObject.getStringOrEmpty(matDocH.get("mat_id")))
				 &&ftyIdTemp.equals(UtilObject.getStringOrEmpty(matDocH.get("fty_id")))
				 &&locationIdTemp.equals(UtilObject.getStringOrEmpty(matDocH.get("location_id")))
				 &&specStockTemp.equals(UtilObject.getStringOrEmpty(matDocH.get("spec_stock")))){
					if(qtyTemp.compareTo(UtilObject.getBigDecimalOrZero(matDocH.get("qty")))==0){
						polymericList.add(matDocH);
						polymericList.add(matDocS);
						matDocHList.remove(j);
						break;
					}
				}
				
			}
		}
		
		//更新状态为已配对
		for(Map<String,Object> matDoc: polymericList){
			SynMaterialDocItem record = new SynMaterialDocItem();
			record.setIsNeutralize((byte)1);
			record.setMatDocId(UtilObject.getIntegerOrNull(matDoc.get("mat_doc_id")));
			record.setMatDocRid(UtilObject.getIntegerOrNull(matDoc.get("mat_doc_rid")));
			
			synMDocItemDao.updateByPrimaryKeySelective(record);
		}
		
		return true;
	}
	
	@Override
	public List<Map<String,Object>> getStorageList(String ftyId,String locationId,String userId) throws Exception{
		int ftyIdtemp = 0;
		int locationIdtemp = 0 ;
		if (ftyId != null && ftyId.length()>0) {
			ftyIdtemp = Integer.parseInt(ftyId);
		}
		if (locationId != null && locationId.length()>0) {
			locationIdtemp = Integer.parseInt(locationId);
		}
		
		List<Map<String,Object>> sapList = commonService.getSynStockFromOracle(ftyIdtemp, locationIdtemp);

		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		if(sapList != null && sapList.size()>0 ){
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("ftyId", ftyId);
			param.put("locationId", locationId);
			List<Map<String,Object>> wmsList = stockBatchDao.selectDataForHdzmStorageNum(param);
			Map<String,Map<String,Object>> wmsListMap = new HashMap<String,Map<String,Object>>();
			
			for(Map<String,Object> map:wmsList){
				String wmsFtyCode = UtilString.getStringOrEmptyForObject(map.get("fty_code"));
				String wmsLocationCode = UtilString.getStringOrEmptyForObject(map.get("location_code"));
				String wmsMatCode = UtilString.getStringOrEmptyForObject(map.get("mat_code"));
				String wmsErpBatch = UtilString.getStringOrEmptyForObject(map.get("erp_batch"));
				String key = wmsFtyCode.trim() + "-" + 
							 wmsLocationCode.trim() + "-" +
							 wmsMatCode.trim() + "-" +
							 wmsErpBatch.trim() ;
				wmsListMap.put(key, map);
			}
	
			for(int i =0;i<sapList.size();i++){
				Map<String,Object> sap = sapList.get(i);
				String sapFtyCode= UtilString.getStringOrEmptyForObject(sap.get("WERKS"));
				String sapLocationCode= UtilString.getStringOrEmptyForObject(sap.get("LGORT"));
				String sapMatCode= UtilString.getStringOrEmptyForObject(sap.get("MATNR"));
				String sapErpBatch= UtilString.getStringOrEmptyForObject(sap.get("CHARG"))==null?"":sap.get("CHARG").toString();				
				BigDecimal sapQty = UtilObject.getBigDecimalOrZero(sap.get("CLABS"));
				
				String key = sapFtyCode.trim() + "-" + 
							 sapLocationCode.trim() + "-" +
							 sapMatCode.trim() + "-" +
							 sapErpBatch.trim() ;
				
				Map<String,Object> wmsStock = wmsListMap.get(key);
				
				List<String> matList = new ArrayList<String>();
				matList.add(sapMatCode);
				Map<String, DicMaterial> matMap = dictionaryService.getMatObjectMapByCodeList(matList);
				DicMaterial matObj = matMap.get(sapMatCode);
				
				Map<String, DicFactoryVo> ftyMap = dictionaryService.getFtyCodeMap();
				DicFactoryVo ftyObj = ftyMap.get(sapFtyCode);
				
				Map<String, DicStockLocationVo> locationMap = dictionaryService.getLocationCodeMap();
				DicStockLocationVo loactionObj = locationMap.get(sapFtyCode+ "-" +sapLocationCode);
				
				if(wmsStock==null){
					if (sapQty.compareTo(BigDecimal.ZERO)!=0) {
						Map<String,Object> data = new HashMap<String,Object>();
						if(ftyObj!=null){
							data.put("fty_id",ftyObj.getFtyId());
							data.put("fty_code",ftyObj.getFtyCode());
							data.put("fty_name",ftyObj.getFtyName());
						}else{
							data.put("fty_id","");
							data.put("fty_code",sapFtyCode);
							data.put("fty_name","");
						}
						if(loactionObj!=null){
							data.put("location_id",loactionObj.getLocationId());
							data.put("location_code",loactionObj.getLocationCode());
							data.put("location_name",loactionObj.getLocationName());
						}else{
							data.put("location_id","");
							data.put("location_code",sapLocationCode);
							data.put("location_name","");
						}
						if(matObj!=null){
							data.put("mat_id",matObj.getMatId());
							data.put("mat_code",matObj.getMatCode());
							data.put("mat_name",matObj.getMatName());
							data.put("unit_code",dictionaryService.getUnitCodeByUnitId(matObj.getUnitId()));
							data.put("unit_name",dictionaryService.getUnitIdMap().get(matObj.getUnitId()).getNameZh());
						}else{
							data.put("mat_id","");
							data.put("mat_code",sapMatCode);
							data.put("mat_name","");
							data.put("unit_code","");
							data.put("unit_name","");
						}
						
						data.put("price",sap.get("VERPR"));
						data.put("erp_batch",sapErpBatch);
						data.put("spec_stock","");
						data.put("spec_stock_code","");
						data.put("spec_stock_name","");
						
						data.put("wms_qty","0");
						data.put("sap_qty",sapQty);
						data.put("num",sapQty);
						
						list.add(data);
					}
					
				}else{
					BigDecimal wmsQty = UtilObject.getBigDecimalOrZero(wmsStock.get("qty"));
					BigDecimal num = wmsQty.subtract(sapQty).setScale(3, BigDecimal.ROUND_HALF_UP);
					
					if(num.compareTo(BigDecimal.ZERO)!=0){
						Map<String,Object> data = new HashMap<String,Object>();
						if(ftyObj!=null){
							data.put("fty_id",ftyObj.getFtyId());
							data.put("fty_code",ftyObj.getFtyCode());
							data.put("fty_name",ftyObj.getFtyName());
						}else{
							data.put("fty_id","");
							data.put("fty_code",sapFtyCode);
							data.put("fty_name","");
						}
						if(loactionObj!=null){
							data.put("location_id",loactionObj.getLocationId());
							data.put("location_code",loactionObj.getLocationCode());
							data.put("location_name",loactionObj.getLocationName());
						}else{
							data.put("location_id","");
							data.put("location_code",sapLocationCode);
							data.put("location_name","");
						}
						if(matObj!=null){
							data.put("mat_id",matObj.getMatId());
							data.put("mat_code",matObj.getMatCode());
							data.put("mat_name",matObj.getMatName());
							data.put("unit_code",dictionaryService.getUnitCodeByUnitId(matObj.getUnitId()));
							data.put("unit_name",dictionaryService.getUnitIdMap().get(matObj.getUnitId()).getNameZh());
						}else{
							data.put("mat_id","");
							data.put("mat_code",sapMatCode);
							data.put("mat_name","");
							data.put("unit_code","");
							data.put("unit_name","");
						}
						
						data.put("price",sap.get("VERPR"));
						data.put("erp_batch",sapErpBatch);
						data.put("spec_stock",UtilString.getStringOrEmptyForObject(wmsStock.get("spec_stock")));
						data.put("spec_stock_code",UtilString.getStringOrEmptyForObject(wmsStock.get("spec_stock_code")));
						data.put("spec_stock_name",UtilString.getStringOrEmptyForObject(wmsStock.get("spec_stock_name")));
						
						data.put("wms_qty",wmsQty);
						data.put("sap_qty",sapQty);
						data.put("num",num);
						
						list.add(data);
					}
				}
					
				
			}
			
		}
		return list;
	}
}
