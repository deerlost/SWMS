package com.inossem.wms.service.intfc.sap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inossem.wms.dao.dic.DicMaterialMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.vo.DicFactoryVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.intfc.IStockAllocateCargo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service("stockAllocateCargoImpl")
public class StockAllocateCargoImpl implements IStockAllocateCargo {
    @Autowired
    private IDictionaryService dictionaryService;

    @Autowired
    private DicMaterialMapper dicMaterialMapper;
    
//	@Override
//	public List<Map<String, Object>> getSaleMasseg(String refer_receipt_code, String userId) throws Exception {
//		JSONObject params = new JSONObject();
//
//		JSONObject I_IMPORT = this.setImport("10", userId);
//
//		JSONArray I_EBELN = new JSONArray();
//
//		JSONObject ebelnObj = new JSONObject();
//		ebelnObj.put("SIGN", "I");
//		ebelnObj.put("OPTION", "EQ");
//		ebelnObj.put("LOW", refer_receipt_code);
//
//		I_EBELN.add(ebelnObj);
//
//		params.put("I_IMPORT", I_IMPORT);
//		params.put("I_VBELN", I_EBELN);
//
//		JSONObject result = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_10?sap-client=" + Const.SAP_API_CLIENT, params, 30);
//
//		JSONArray returnList = result.getJSONArray("I_HEAD");
//
//		List<Map<String, Object>> orderHeadList = new ArrayList<Map<String, Object>>();
//
//		for (int i = 0; i < returnList.size(); i++) {
//			JSONObject sapObj = returnList.getJSONObject(i);
//			orderHeadList.add(this.getSalesViewBySap(sapObj));
//			
//		}
//		return orderHeadList;
//	
//	}
	
	
	/**
	 * 获取销售订单详细
	 */
//	@Override
//	public Map<String, Object> getSalesViewBySap(String deliveryCode, CurrentUser user) throws Exception {
//		JSONObject params = new JSONObject();
//
//		JSONObject I_IMPORT = this.setImport("10", user.getUserId());
//
//		JSONArray I_EBELN = new JSONArray();
//
//		JSONObject ebelnObj = new JSONObject();
//		ebelnObj.put("SIGN", "I");
//		ebelnObj.put("OPTION", "EQ");
//		ebelnObj.put("LOW", deliveryCode);
//
//		I_EBELN.add(ebelnObj);
//
//		params.put("I_IMPORT", I_IMPORT);
//		params.put("I_VBELN", I_EBELN);
//
//		//JSONObject result =new JSONObject();
////				UtilREST
////				.executePostJSONTimeOut(Const.SAP_API_URL + "int_10?sap-client=" + Const.SAP_API_CLIENT, params, 30);
//
////		JSONArray headSAP = result.getJSONArray("I_HEAD");
////
////		Map<String, Object> head = null;
////
////		if (headSAP.size() > 0) {
////			JSONObject sapObj = headSAP.getJSONObject(0);
////			head = this.getSalesOrderHead(sapObj);
////
////			JSONArray itemAry = result.getJSONArray("I_ITEM"); // 销售订单子表数据
////			head.put("item_list", this.getSalesOrderItem(itemAry, user.getLocationList()));
////
////		}
//		//getSalesOrderHead(result);
//		//Map<String, Object> result=new HashMap<String, Object>();
//	   Map<String, Object> head=getDeliveryHead(eLIKPList);
//	   List<Map<String, Object>> item=getDeliveryItem(eLIPSList);
//	   head.put("item_list", item);
//	    return head;
//
//	}
	
	
	
	/**
	 * SAP交货订单头信息转换
	 * 
	 * @param sapObj
	 * @return
	 */
	private Map<String, Object> getDeliveryHead(List<Map<String,Object>> head) {
		Map<String, Object> orderHead = head.get(0);
		Map<String , Object> result=new HashMap<String,Object>();
		result.put("refer_receipt_code", orderHead.get("VBELN")); // 交货 1
		result.put("document_type", orderHead.get("LFART"));//交货单类型 2
		result.put("sale_order_code", orderHead.get("VBELN_s"));//销售凭证 3
		result.put("create_time", orderHead.get("ERDAT"));// 创建日期 4
		result.put("create_user", orderHead.get("ERNAM")); //创建人 5
		result.put("order_type", orderHead.get("AUART"));// 订单类型 6
		result.put("order_type_name", orderHead.get("BEZEI"));// 订单类型名称 7
		result.put("org_code", orderHead.get("VKORG"));// 销售组织 8
		
//		result.put("VTWEG", orderHead.get("VTWEG")); // 分销渠道 9
//		result.put("SPART", orderHead.get("SPART"));// 10 产品组
		
		result.put("receive_code", orderHead.get("KUNNR"));// 客户编码 11
		result.put("receive_name", orderHead.get("NAME1"));// 客户名称 12
		result.put("preorder_id", orderHead.get("BSTNK"));// 合同号  13  
		result.put("remark", "");
		
//		orderHead.put("refer_receipt_code", "000001");
//		orderHead.put("allocate_cargo_id", "111222");
//		orderHead.put("sale_order_code", "内部销售");
//		orderHead.put("preorder_id","3333");
//		orderHead.put("receive_name", "销售公司"); 
//		orderHead.put("receive_code", "10000002"); 
//		orderHead.put("create_user", "管理员");
//		orderHead.put("create_time", "2017-10-10");
//		orderHead.put("remark", "00");
//		orderHead.put("delivery_vehicle", "老五");
//		orderHead.put("delivery_driver", "辽A5555");
//		orderHead.put("class_type_id", "0");
//		orderHead.put("status", "3");
//		orderHead.put("status_name", "已提交");
//		List<Map<String, Object>> item = new ArrayList<Map<String, Object>>();  
//		Map<String, Object> itemOne=new HashMap<String, Object>();
//		itemOne.put("mat_id", "55773");
//		itemOne.put("mat_code", "41000000001");
//		itemOne.put("mat_name", "转向拉杆03069391-0020");
//		itemOne.put("unit_id", "149");
//		itemOne.put("name_zh", "件");
//		itemOne.put("order_qty", "10");
//		itemOne.put("unit_code", "PC");
//		itemOne.put("output_qty", "2");
//		itemOne.put("location_name", "大地中心供应站");
//		itemOne.put("location_code", "0004");
//		itemOne.put("location_id", "23");
//		itemOne.put("remark", "pp");
//		itemOne.put("fty_id", "23");
//		itemOne.put("erp_batch", "");
//		itemOne.put("erp_remark", "请发00005批次");
//		itemOne.put("refer_receipt_rid", "32");
//		itemOne.put("allocate_cargo_rid", "1");
//		item.add(itemOne);
//		orderHead.put("item_list", item);
		return result;
	}
	/**
	 * SAP交货订单明细信息转换
	 * 
	 * @param itemAry
	 * @return
	 */
	private List<Map<String, Object>> getDeliveryItem(List<Map<String,Object>> item) {

//		List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
		String isFocusBatch = "0"; // 批次信息是否强控
		Map<String, DicFactoryVo> ftyMap = dictionaryService.getFtyCodeMap();
		Map<String, DicStockLocationVo> locationMap = dictionaryService.getLocationCodeMap();
		List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
		for (int i = 0; i < item.size(); i++) {
			//JSONObject sapItemObj = item.getJSONObject(i);
			Map<String, Object> deliveryItem =	item.get(i);
			DicFactoryVo ftyVo = ftyMap.get(deliveryItem.get("WERKS"));
			DicStockLocationVo locationVo = locationMap.get(dictionaryService
					.getFtyCodeAndLocCodeForLocMap(deliveryItem.get("WERKS").toString(), deliveryItem.get("LGORT").toString()));

			//String sapLgort = ftyVo.getFtyId() + "-" + locationVo.getLocationId();
			
			
				Map<String, Object> orderItem = new HashMap<String, Object>(); 
				orderItem.put("allocate_cargo_rid", i + 1); // 序号
				orderItem.put("fty_id", ftyVo.getFtyId()); // 工厂
				orderItem.put("fty_code", deliveryItem.get("WERKS")); // 工厂 4
				orderItem.put("fty_name", ftyVo.getFtyName()); // 工厂
								
				orderItem.put("location_id",locationVo.getLocationId());
				orderItem.put("location_code",deliveryItem.get("LGORT"));// 库存地点 5
				orderItem.put("location_name", locationVo.getLocationName()); // 库存地点
				orderItem.put("mat_id", dictionaryService.getMatIdByMatCode(deliveryItem.get("KDMAT").toString())); // 物料编码
				orderItem.put("mat_code", deliveryItem.get("KDMAT")); // 物料编码 10
			    Map<String,Object> map=dicMaterialMapper.getMatUnitByMatCode(deliveryItem.get("KDMAT").toString());
				
			    orderItem.put("unit_id", map.get("unit_id"));
			    orderItem.put("unit_code", map.get("unit_code"));
			    orderItem.put("name_zh", map.get("name_zh"));
			    
				orderItem.put("mat_name", deliveryItem.get("MATNR")); // 物料名称 3
				orderItem.put("refer_receipt_code", deliveryItem.get("VBELN")); // 交货单号 1
				//orderItem.put("erp_remark", deliveryItem.get("CHARG")); //erp 批号  6
				orderItem.put("erp_remark", "");
			//	orderItem.put("refer_receipt_code", deliveryItem.get("VGBEL"));// 参考单据编号(销售订单编号) 8
				orderItem.put("erp_batch", deliveryItem.get("CHARG"));// 批号 2
				orderItem.put("refer_receipt_rid", deliveryItem.get("POSNR"));// 交货行项目 2
				orderItem.put("order_qty", deliveryItem.get("LFIMG")); //交货数量 7
				
              //CHARG   批号  6     LFIMG 交货数量 7  VGBEL 参考单据的单据编号  8 VGPOS  参考项目的项目号  9  KDMAT 客户物料编号 10			
				result.add(orderItem);
//				orderItem.put("remark", "");// 备注
//				orderItem.put("erp_remark", "");// erp备注
			//	isFocusBatch = stockLocationDao.selectPlanByLocation(locationVo.getLocationId());

			
		}

		return result;
	}
	
	/**
	 * 生成IMPORT
	 * 
	 * @param typeNum
	 * @param userId
	 * @return
	 */
	private JSONObject setImport(String typeNum, String userId) {
		JSONObject I_IMPORT = new JSONObject();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dateStr = sdf.format(new Date());

		I_IMPORT.put("ZTYPE", typeNum); // 接口类型
		I_IMPORT.put("ZERNAM", userId); // 用户id
		I_IMPORT.put("ZIMENO", ""); // 设备IMENO
		I_IMPORT.put("ZDATE", dateStr.substring(0, 10)); // 传输日期
		I_IMPORT.put("ZTIME", dateStr.substring(11, 19)); // 传输时间

		return I_IMPORT;
	}

	

}
