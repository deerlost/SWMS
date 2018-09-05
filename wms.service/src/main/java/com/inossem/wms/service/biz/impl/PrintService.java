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
import com.inossem.wms.dao.biz.BizAllocateCargoHeadMapper;
import com.inossem.wms.dao.biz.BizAllocateCargoPositionMapper;
import com.inossem.wms.dao.biz.BizStockInputItemMapper;
import com.inossem.wms.dao.biz.BizStockInputTransportHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskHeadCwMapper;
import com.inossem.wms.dao.biz.BizStockTaskPositionCwMapper;
import com.inossem.wms.dao.biz.BizStockTransportHeadCwMapper;
import com.inossem.wms.dao.biz.BizStockTransportItemCwMapper;
import com.inossem.wms.dao.dic.DicPrinterMapper;
import com.inossem.wms.dao.dic.DicWarehousePositionMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.biz.BizStockInputItem;
import com.inossem.wms.model.biz.BizStockTransportHeadCw;
import com.inossem.wms.model.dic.DicPrinter;
import com.inossem.wms.model.dic.DicWarehousePosition;
import com.inossem.wms.model.enums.EnumPrinterType;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IPrintService;
import com.inossem.wms.service.intfc.ICwLimsWs;
import com.inossem.wms.service.intfc.IStockCheck;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilREST;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Transactional
@Service("printService")
public class PrintService implements IPrintService {

	@Resource
	private BizStockTransportHeadCwMapper materialTransportHeadDao;
	
	@Resource
	private BizStockTransportItemCwMapper materialTransportItemDao;
	
	@Autowired
	private BizAllocateCargoHeadMapper bizAllocateCargoHeadMapper;

	@Autowired
	private BizAllocateCargoPositionMapper bizAllocateCargoPositionMapper;

	@Autowired
	private BizStockInputItemMapper stockInputItemDao;

	@Autowired
	private BatchMaterialMapper batchMaterialDao;

	@Resource
	private IDictionaryService dictionaryService;

	@Autowired
	private DicWarehousePositionMapper positionMapper;

	@Autowired
	private BizStockTransportHeadCwMapper stockTransportHeadCwDao;

	@Autowired
	private BizStockTransportItemCwMapper stockTransportItemCwDao;

	@Resource
	private IStockCheck stockCheckImpl;

	@Resource
	private ICwLimsWs cwLimsService;

	@Autowired
	private DicPrinterMapper printerDao;

	@Autowired
	private BizStockTaskHeadCwMapper bizStockTaskHeadCwDao;

	@Autowired
	private BizStockTaskPositionCwMapper bizStockTaskPositionCwDao;

	@Autowired
	private BizStockInputTransportHeadMapper bizStockInputHeadDao;

	private String url = UtilProperties.getInstance().getPrintUrl() + "/wms/print/ytLabel";

	@Override
	public String printInputBatch(Integer stockInputId) throws Exception {

		List<BizStockInputItem> itemList = stockInputItemDao.selectByStockInputId(stockInputId);

		JSONObject params = new JSONObject();

		JSONArray printdata = new JSONArray();
		for (BizStockInputItem item : itemList) {
			BatchMaterial record = new BatchMaterial();
			record.setFtyId(item.getFtyId());
			record.setMatId(item.getMatId());
			record.setBatch(item.getBatch());
			BatchMaterial batchMaterial = batchMaterialDao.selectByUniqueKey(record);

			JSONObject json = new JSONObject();
			json.put("batch", UtilString.getStringOrEmptyForObject(item.getBatch()));// 批次号
			json.put("purchase_order_code", UtilString.getStringOrEmptyForObject(batchMaterial.getPurchaseOrderCode()));// 采购订单号
			json.put("purchase_order_rid", UtilString.getStringOrEmptyForObject(batchMaterial.getPurchaseOrderRid()));// 采购行项目号
			json.put("supplier_name", UtilString.getStringOrEmptyForObject(batchMaterial.getSupplierName()));// 供应商名称
			json.put("contract_code", UtilString.getStringOrEmptyForObject(batchMaterial.getContractCode()));// 合同号
			json.put("demand_dept", UtilString.getStringOrEmptyForObject(item.getDemandDept()));// 需求部门
			json.put("mat_code", UtilString.getStringOrEmptyForObject(item.getMatCode())); // 物料编码
			json.put("mat_name", UtilString.getStringOrEmptyForObject(item.getMatName()));// 物料描述
			json.put("spec_stock", UtilString.getStringOrEmptyForObject(batchMaterial.getSpecStock()));// 特殊库存标识
			json.put("spec_stock_code", UtilString.getStringOrEmptyForObject(batchMaterial.getSpecStockCode()));// 特殊库存代码
			json.put("input_date", UtilString.getShortStringForDate(item.getCreateTime())); // 入库时间
			json.put("type", "L1");
			printdata.add(json);
		}
		params.put("print_data", printdata);

		JSONObject jsonreturn = new JSONObject();
		if (printdata.size() > 0) {
			jsonreturn = UtilREST.executePostJSONTimeOut(url, params, 20);
		}
		String returnString = "";
		if (jsonreturn != null && !jsonreturn.isEmpty() && jsonreturn.containsKey("fileName")) {
			returnString = jsonreturn.getString("fileName");
		}
		return returnString;
	}

	@Override
	public String printStockBatchForBatch(JSONObject params) throws Exception {

		JSONArray batchList = params.getJSONArray("batch_list");
		@SuppressWarnings("unchecked")
		List<Integer> idsList = JSONArray.toList(batchList, new Integer(0), new JsonConfig());

		JSONArray printdata = new JSONArray();
		if (idsList != null && idsList.size() > 0) {
			List<BatchMaterial> batchMaterialList = batchMaterialDao.selectByStockBatchIds(idsList);
			for (BatchMaterial batchMaterial : batchMaterialList) {
				JSONObject json = new JSONObject();
				json.put("batch", UtilString.getStringOrEmptyForObject(batchMaterial.getBatch()));// 批次号
				json.put("purchase_order_code",
						UtilString.getStringOrEmptyForObject(batchMaterial.getPurchaseOrderCode()));// 采购订单号
				json.put("purchase_order_rid",
						UtilString.getStringOrEmptyForObject(batchMaterial.getPurchaseOrderRid()));// 采购行项目号
				json.put("supplier_name", UtilString.getStringOrEmptyForObject(batchMaterial.getSupplierName()));// 供应商名称
				json.put("contract_code", UtilString.getStringOrEmptyForObject(batchMaterial.getContractCode()));// 合同号
				json.put("demand_dept", UtilString.getStringOrEmptyForObject(batchMaterial.getDemandDept()));// 需求部门
				json.put("mat_code", UtilString.getStringOrEmptyForObject(batchMaterial.getMatCode())); // 物料编码
				json.put("mat_name", UtilString.getStringOrEmptyForObject(batchMaterial.getMatName()));// 物料描述
				json.put("spec_stock", UtilString.getStringOrEmptyForObject(batchMaterial.getSpecStock()));// 物料描述
				json.put("spec_stock_code", UtilString.getStringOrEmptyForObject(batchMaterial.getSpecStock()));// 物料描述
				json.put("input_date", UtilString.getShortStringForDate(batchMaterial.getCreateTime())); // 入库时间
				json.put("type", "L1");
				printdata.add(json);
			}
		}
		params.put("print_data", printdata);
		JSONObject jsonreturn = new JSONObject();
		if (printdata.size() > 0) {
			jsonreturn = UtilREST.executePostJSONTimeOut(url, params, 20);
		}
		String returnString = "";
		if (jsonreturn != null && !jsonreturn.isEmpty() && jsonreturn.containsKey("fileName")) {
			returnString = jsonreturn.getString("fileName");
		}
		return returnString;
	}

	@Override
	public String printForPosition(JSONObject params) throws Exception {
		JSONArray cwAry = params.getJSONArray("area_position_list");
		Map<String, String> area_position_HashMap = new HashMap<String, String>();
		JSONArray printdata = new JSONArray();
		if (cwAry != null && !cwAry.isEmpty()) {
			for (int i = 0; i < cwAry.size(); i++) {
				JSONObject cwObject = cwAry.getJSONObject(i);
				String wh_code = cwObject.getString("wh_code");
				String area_code = cwObject.getString("area_code");
				String position_code = cwObject.getString("position_code");
				if (!area_position_HashMap.containsKey(area_code + "-" + position_code)) {
					JSONObject json = new JSONObject();
					json.put("wh_code", wh_code);// 存储区
					json.put("area_code", area_code);// 存储区
					json.put("position_code", position_code);// 仓位
					json.put("type", "L3");
					printdata.add(json);
					area_position_HashMap.put("INS-10"+wh_code+"-"+area_code + "-" + position_code,"INS-10"+ wh_code+"-"+area_code + "-" + position_code);
				}
			}

		}
		params.put("print_data", printdata);
		JSONObject jsonreturn = new JSONObject();
		if (printdata.size() > 0) {
			jsonreturn = UtilREST.executePostJSONTimeOut(url, params, 200);
		}
		String returnString = "";
		if (jsonreturn != null && !jsonreturn.isEmpty() && jsonreturn.containsKey("fileName")) {
			returnString = jsonreturn.getString("fileName");
		}
		return returnString;
	}

	@Override
	public String printStockPosition(JSONObject params) throws Exception {

		JSONArray ids = params.getJSONArray("ids");
		@SuppressWarnings("unchecked")
		List<Integer> idsList = JSONArray.toList(ids, new Integer(0), new JsonConfig());
		JSONArray printdata = new JSONArray();
		if (idsList != null && idsList.size() > 0) {
			List<BatchMaterial> batchMaterialList = batchMaterialDao.selectByStockPositionIds(idsList);
			for (BatchMaterial batchMaterial : batchMaterialList) {
				JSONObject json = new JSONObject();
				json.put("batch", UtilString.getStringOrEmptyForObject(batchMaterial.getBatch()));// 批次号
				json.put("purchase_order_code",
						UtilString.getStringOrEmptyForObject(batchMaterial.getPurchaseOrderCode()));// 采购订单号
				json.put("purchase_order_rid",
						UtilString.getStringOrEmptyForObject(batchMaterial.getPurchaseOrderRid()));// 采购行项目号
				json.put("supplier_name", UtilString.getStringOrEmptyForObject(batchMaterial.getSupplierName()));// 供应商名称
				json.put("contract_code", UtilString.getStringOrEmptyForObject(batchMaterial.getContractCode()));// 合同号
				json.put("demand_dept", UtilString.getStringOrEmptyForObject(batchMaterial.getDemandDept()));// 需求部门
				json.put("mat_code", UtilString.getStringOrEmptyForObject(batchMaterial.getMatCode())); // 物料编码
				json.put("mat_name", UtilString.getStringOrEmptyForObject(batchMaterial.getMatName()));// 物料描述
				json.put("spec_stock", UtilString.getStringOrEmptyForObject(batchMaterial.getSpecStock()));// 物料描述
				json.put("spec_stock_code", UtilString.getStringOrEmptyForObject(batchMaterial.getSpecStock()));// 物料描述
				json.put("input_date", UtilString.getShortStringForDate(batchMaterial.getCreateTime())); // 入库时间
				json.put("type", "L1");
				printdata.add(json);
			}
		}
		params.put("print_data", printdata);
		JSONObject jsonreturn = new JSONObject();
		if (printdata.size() > 0) {
			jsonreturn = UtilREST.executePostJSONTimeOut(url, params, 20);
		}
		String returnString = "";
		if (jsonreturn != null && !jsonreturn.isEmpty() && jsonreturn.containsKey("fileName")) {
			returnString = jsonreturn.getString("fileName");
		}
		return returnString;
	}

	@Override
	public String printUrgence(JSONObject params) throws Exception {

		JSONArray printdata = new JSONArray();
		// String demand_dept = params.getString("demand_dept");//
		// temp.get("demand_dept")==null?"":temp.get("demand_dept");
		// String demand_person = params.getString("demand_person");
		// String create_time = params.getString("create_time");
		// String urgence_code = params.getString("urgence_code");
		// JSONObject json = new JSONObject();
		// json.put("demand_dept", demand_dept);// 存储区
		// json.put("demand_person", demand_person);// 仓位
		// json.put("create_time", create_time);// 存储区
		// json.put("urgence_code", urgence_code);// 仓位
		params.put("type", "L4");
		printdata.add(params);
		params.put("print_data", printdata);
		JSONObject jsonreturn = new JSONObject();
		if (printdata.size() > 0) {
			jsonreturn = UtilREST.executePostJSONTimeOut(url, params, 20);
		}
		String returnString = "";
		if (jsonreturn != null && !jsonreturn.isEmpty() && jsonreturn.containsKey("fileName")) {
			returnString = jsonreturn.getString("fileName");
		}
		return returnString;
	}

	@Override
	public String printCGDDForMatnr(JSONObject params) throws Exception {

		JSONObject jsonreturn = UtilREST.executePostJSONTimeOut(url, params, 20);
		String returnString = "";
		if (jsonreturn != null && !jsonreturn.isEmpty() && jsonreturn.containsKey("fileName")) {
			returnString = jsonreturn.getString("fileName");
		}
		return returnString;
	}

	@Override
	public List<DicWarehousePosition> selectPositionForPrint(Map<String, Object> map) throws Exception {

		return positionMapper.selectPositionForPrint(map);
	}

	/*
	 * <p>生产转运单: </p> <p>Title: printProductTrans</br></p>
	 * 
	 * @param stockTransportId
	 * 
	 * @param userId
	 * 
	 * @return
	 * 
	 * @throws Exception</br>
	 * 
	 * @see
	 * com.inossem.wms.service.biz.IPrintService#printProductTrans(java.lang.
	 * Integer, java.lang.String)</br>
	 */
	@Override
	public String printProductTrans(Integer stockTransportId, String userId) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> proTransHead = stockTransportHeadCwDao.queryStockTransForPrint(stockTransportId);
		List<Map<String, Object>> proTransItem = stockTransportItemCwDao.selectItemByIdForPrint(stockTransportId);
		JSONObject printHead = new JSONObject();
		JSONArray printItem = new JSONArray();
		JSONObject params = new JSONObject();
		JSONObject printreturn = new JSONObject();
		String fileName = "";

		if (proTransHead != null && proTransHead.size() > 0) {
	//		printHead.put("user_name", cUser.getUserName());
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			printHead.put("print_date", date.format(new Date()));
			/*
			 * printHead.put("isPDA", String.valueOf(0));
			 * printHead.put("printPath", "HP LaserJet MFP M129-M134 PCLm-S");
			 */
			printHead.put("print_name", "");
			if (null != userId && !userId.equals("")) {
				DicPrinter printer = printerDao.getPrinterByUserAndType(userId,
						EnumPrinterType.PAPER_PRINTER.getValue());
				printHead.put("print_name", printer.getPrinterName());
			}
			printHead.put("stock_transport_code", proTransHead.get("stock_transport_code"));
			printHead.put("fty_name", proTransHead.get("fty_name"));
			printHead.put("location_name", proTransHead.get("wh_code") + "-" + proTransHead.get("wh_name"));
			printHead.put("driver", UtilString.getStringOrEmptyForObject(proTransHead.get("driver")));
			printHead.put("vehicle", UtilString.getStringOrEmptyForObject(proTransHead.get("vehicle")));
			printHead.put("user_name", proTransHead.get("user_name"));
			printHead.put("create_time", proTransHead.get("create_time"));
			printHead.put("remark", proTransHead.get("remark"));
			printHead.put("create_user", proTransHead.get("create_user"));
		}

		if (null != proTransItem && proTransItem.size() > 0) {
			for (Map<String, Object> mapitem : proTransItem) {
				JSONObject item = new JSONObject();
				item.put("specs_model", String.valueOf(mapitem.get("mat_name")) + mapitem.get("mat_code"));
				item.put("package_type_name", mapitem.get("package_type_name"));
				double standard = Double.parseDouble(String.valueOf(mapitem.get("package_standard")));
				item.put("num", UtilObject.getBigDecimalOrZero(mapitem.get("transport_qty"))
						.divide(new BigDecimal(standard),0, BigDecimal.ROUND_UP).longValue());

				item.put("transport_qty", mapitem.get("transport_qty"));// 本次上架数量
				item.put("product_batch", mapitem.get("product_batch"));// 生产批次
				item.put("erp_batch", mapitem.get("erp_batch"));// erp批次
				item.put("fty_input", mapitem.get("fty_name"));// 目标地点
				item.put("location_input", mapitem.get("location_name"));// 目标
																			// 库存地点
				item.put("remark", mapitem.get("remark"));
				printItem.add(item);
			}
		}

		params.put("print_head", printHead);
		params.put("print_item", printItem);
		String proTransUrl = UtilProperties.getInstance().getPrintUrl() + "/wms/print/cwProTrans";
		if (null != printItem && printItem.size() > 0) {
			printreturn = UtilREST.executePostJSONTimeOut(proTransUrl, params, 30);
		}
		if (null != printreturn && printreturn.size() > 0 && printreturn.containsKey("fileName")) {
			fileName = printreturn.getString("fileName");
		}
		return fileName;
	}

	/*
	 * <p>打印erp盘点 </p> <p>Title: printErpStock</br></p>
	 * 
	 * @param fty_id
	 * 
	 * @param location_id
	 * 
	 * @param cUser
	 * 
	 * @return
	 * 
	 * @throws Exception</br>
	 * 
	 * @see
	 * com.inossem.wms.service.biz.IPrintService#printErpStock(java.lang.String,
	 * java.lang.String, com.inossem.wms.model.auth.CurrentUser)</br>
	 */
	@Override
	public String printErpStock(String fty_id, String location_id, CurrentUser cUser) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (null == location_id || location_id.equals("")) {
			list = stockCheckImpl.getStorageList(fty_id, String.valueOf(-1), cUser.getUserId());
		} else {
			list = stockCheckImpl.getStorageList(fty_id, location_id, cUser.getUserId());
		}
		JSONObject printHead = new JSONObject();
		JSONArray printItem = new JSONArray();
		JSONObject params = new JSONObject();
		JSONObject printreturn = new JSONObject();
		String fileName = "";

		printHead.put("user_name", cUser.getUserName());
		if (list.size() > 0) {
			printHead.put("fty_name", list.get(0).get("fty_name"));
			printHead.put("fty_code", list.get(0).get("fty_code"));
		}
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		printHead.put("print_date", date.format(new Date()));

		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> item = list.get(i);
			JSONObject jsonitem = new JSONObject();
			jsonitem.put("mat_code", item.get("mat_code"));
			jsonitem.put("mat_name", item.get("mat_name"));
			jsonitem.put("erp_batch", item.get("erp_batch"));
			jsonitem.put("location_code", item.get("location_code"));
			jsonitem.put("location_name", item.get("location_name"));
			jsonitem.put("sap_qty", item.get("sap_qty"));
			jsonitem.put("unit_name", item.get("unit_name"));
			jsonitem.put("price", item.get("price"));
			jsonitem.put("wms_qty", item.get("wms_qty"));
			jsonitem.put("num", item.get("num"));
			printItem.add(jsonitem);
		}
		params.put("print_head", printHead);
		params.put("print_item", printItem);
		String proTransUrl = UtilProperties.getInstance().getPrintUrl() + "/wms/print/cwErpStock";
		if (null != printItem && printItem.size() > 0) {
			printreturn = UtilREST.executePostJSONTimeOut(proTransUrl, params, 30);
		}
		if (null != printreturn && printreturn.size() > 0 && printreturn.containsKey("fileName")) {
			fileName = printreturn.getString("fileName");
		}
		return fileName;
	}

	/*
	 * <p>打印配货单: </p> <p>Title: printOutput</br></p>
	 * 
	 * @param param
	 * 
	 * @return
	 * 
	 * @throws Exception</br>
	 * 
	 * @see
	 * com.inossem.wms.service.biz.IPrintService#printOutput(java.util.Map)</br>
	 */
	@Override
	public String printOutput(Integer allocate_cargo_id) throws Exception {
		Map<String, Object> bizAllocateCargoHead = bizAllocateCargoHeadMapper.getCargoHeadById(allocate_cargo_id);
		JSONObject printHead = new JSONObject();
		JSONArray printItem = new JSONArray();
		JSONObject params = new JSONObject();
		JSONObject printreturn = new JSONObject();
		String fileName = "";

		printHead.put("refer_receipt_code", bizAllocateCargoHead.get("refer_receipt_code"));// 交货单号
		printHead.put("allocate_cargo_code", bizAllocateCargoHead.get("allocate_cargo_code"));// 配货单号
		printHead.put("vehicle", bizAllocateCargoHead.get("delivery_vehicle"));
		printHead.put("driver", bizAllocateCargoHead.get("delivery_driver"));
		printHead.put("receive_name", bizAllocateCargoHead.get("receive_name"));
		// SimpleDateFormat clsFormat = new SimpleDateFormat("yy-MM-dd");
		printHead.put("create_time", bizAllocateCargoHead.get("print_time"));
		printHead.put("head_remark", bizAllocateCargoHead.get("remark"));//
		printHead.put("create_user", bizAllocateCargoHead.get("create_user"));
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		printHead.put("print_date", date.format(new Date()));
		List<Map<String, Object>> list = bizAllocateCargoPositionMapper.selectPositionByCargoIdForPrint(allocate_cargo_id);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> item = list.get(i);
			printHead.put("fty_name", item.get("fty_name"));
			printHead.put("ware", item.get("wh_code") + "-" + item.get("wh_name"));
			JSONObject jsonitem = new JSONObject();
			jsonitem.put("specs_model", String.valueOf(item.get("mat_name")) + item.get("mat_code"));// 提货单号
			jsonitem.put("package_type_name", item.get("package_type_name"));
			double standard = Double.parseDouble(String.valueOf(item.get("package_standard")));
			jsonitem.put("reqnum", UtilObject.getBigDecimalOrZero(item.get("output_qty"))
					.divide(new BigDecimal(standard),0, BigDecimal.ROUND_UP).longValue());
			jsonitem.put("product_batch", item.get("product_batch"));
			jsonitem.put("erp_batch", item.get("erp_batch"));
			jsonitem.put("output_qty", item.get("output_qty"));// 交货数量
			if(UtilObject.getIntegerOrNull(bizAllocateCargoHead.get("status"))==EnumReceiptStatus.RECEIPT_SUBMIT.getValue()){
				jsonitem.put("num","");
			}else{
				jsonitem.put("num", UtilObject.getBigDecimalOrZero(item.get("cargo_qty")).divide(new BigDecimal(standard),0, BigDecimal.ROUND_UP)
						.longValue());
			}
			jsonitem.put("position", String.valueOf(item.get("wh_code")) + "-" + item.get("area_code") + "-"
					+ item.get("position_code"));// 仓位

			jsonitem.put("item_remark", item.get("remark"));// 备注
			printItem.add(jsonitem);
		}
		params.put("print_head", printHead);
		params.put("print_item", printItem);
		String proTransUrl = UtilProperties.getInstance().getPrintUrl() + "/wms/print/cwOutputs";
		if (null != printItem && printItem.size() > 0) {
			printreturn = UtilREST.executePostJSONTimeOut(proTransUrl, params, 30);
		}
		if (null != printreturn && printreturn.size() > 0 && printreturn.containsKey("fileName")) {
			fileName = printreturn.getString("fileName");
		}
		return fileName;
	}

	
	/* 
	* <p>Description:  打印Lims质检单</p>
	* <p>Title: printLims</br></p>
	* @param sampName 样品名称
	* @param batchName 生产批次
	* @param specification 规格
	* @return
	* @throws Exception</br>
	* @see com.inossem.wms.service.biz.IPrintService#printLims(java.lang.String, java.lang.String, java.lang.String)</br>
	*/
	@Override
	public String printLims(String sampName,String batchName,String specification) throws Exception {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONObject printHead = new JSONObject();
		JSONArray printItem = cwLimsService.searchReport(sampName, batchName, specification);
		JSONObject params = new JSONObject();
		JSONObject printreturn = new JSONObject();
		String fileName = "";

		printHead.put("sampName",sampName);//样品名称
		printHead.put("batchName",batchName);//生产批次
		printHead.put("specification",specification);//规格
		params.put("print_head", printHead);
		params.put("print_item", printItem);
		String proTransUrl = UtilProperties.getInstance().getPrintUrl() + "/wms/print/cwLims";
		if (null != printItem && printItem.size() > 0) {
			printreturn = UtilREST.executePostJSONTimeOut(proTransUrl, params, 30);
		}
		if (null != printreturn && printreturn.size() > 0 && printreturn.containsKey("fileName")) {
			fileName = printreturn.getString("fileName");
		}
		return fileName;
	}

	@Override
	public JSONObject printZpl(JSONObject params, String userId) throws Exception {
		String url = UtilProperties.getInstance().getPrintUrl() + "/wms/print/cwLabelZpl";
		JSONObject json = new JSONObject();
		DicPrinter printer = printerDao.getPrinterByUserAndType(userId, EnumPrinterType.LABEL_PRINTER.getValue());
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = (List<Map<String, Object>>) params.get("print_data");
		for (Map<String, Object> map : data) {
			map.put("print_name", UtilObject.getStringOrEmpty(printer!=null?printer.getPrinterName():""));
		}
		json.put("print_data", data);
		JSONObject jsonreturn = new JSONObject();
		if (params.size() > 0) {
			jsonreturn = UtilREST.executePostJSONTimeOut(url, params, 20);
		}
		String returnString = "";
		if (jsonreturn != null && !jsonreturn.isEmpty() && jsonreturn.containsKey("fileName")) {
			returnString = jsonreturn.getString("fileName");
			json.put("fileName", returnString);
			returnString = UtilProperties.getInstance().getPrintUrl() + "/wms/print/outputfiles/label/" + returnString+ ".xls";
			json.put("url", returnString);
		}
		return json;
	}

	/*
	 * <p>打印作业单: </p> <p>Title: printTask</br></p>
	 * 
	 * @param stockTaskId
	 * 
	 * @param receiptType 单据类型
	 * 
	 * @return
	 * 
	 * @throws Exception</br>
	 * 
	 * @see com.inossem.wms.service.biz.IPrintService#printTask(int, int)</br>
	 */
	@Override
	public String printTask(int stockTaskId, byte receiptType, String userId) throws Exception {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> headmap = new HashMap<String, Object>();

		JSONObject printHead = new JSONObject();
		JSONArray printItem = new JSONArray();
		JSONObject params = new JSONObject();
		JSONObject printreturn = new JSONObject();
		String fileName = "";

		headmap = bizStockTaskHeadCwDao.selectByTaskIdForPrint(stockTaskId);
		printHead.put("print_name", "");
		// if (receiptType == EnumReceiptType.STOCK_TASK_REMOVAL.getValue()) {
		if (null != userId && !userId.equals("")) {
			DicPrinter printer = printerDao.getPrinterByUserAndType(userId, EnumPrinterType.PAPER_PRINTER.getValue());
			printHead.put("print_name", printer.getPrinterName());
		}
		printHead.put("receiptType", receiptType);// 判断上下架
		printHead.put("fty_name", headmap.get("fty_name"));
		printHead.put("location_name", headmap.get("location_name"));
		printHead.put("receipt_code", headmap.get("receipt_code"));// 交货单号
		printHead.put("receive_name", headmap.get("receive_name"));// 客户名称
		printHead.put("driver", headmap.get("delivery_driver"));
		printHead.put("vehicle", headmap.get("delivery_vehicle"));
		printHead.put("stock_task_code", headmap.get("stock_task_code"));// 作业单号
		printHead.put("create_time", headmap.get("create_time"));// 创建日期
		printHead.put("receipt_type_name", headmap.get("receipt_type_name"));// 单据类型
		printHead.put("headremark", headmap.get("remark"));
		printHead.put("forklift_worker", headmap.get("forklift_worker"));// 叉车工
		printHead.put("tally_clerk", headmap.get("tally_clerk"));// 理货员
		printHead.put("remover", headmap.get("remover"));// 搬运工

		printHead.put("refer_receipt_code", headmap.get("refer_receipt_code"));// 上架
																				// ---业务单号
		// }

		list = bizStockTaskPositionCwDao.selectByTaskIdForPrint(stockTaskId);
		for (int i = 0; i < list.size(); i++) {
			int reqnum = 0;// 请求件数
			int num = 0;// 实际件数
			Map<String, Object> item = list.get(i);
			JSONObject jsonitem = new JSONObject();
			jsonitem.put("specs_model", String.valueOf(item.get("mat_name")) + item.get("mat_code"));
			jsonitem.put("package_type_name", item.get("package_type_name"));// 包装规格
			double standard = Double.parseDouble(String.valueOf(item.get("package_standard")));
			jsonitem.put("reqnum", UtilObject.getBigDecimalOrZero(item.get("reqqty")).divide(new BigDecimal(standard),0, BigDecimal.ROUND_UP)
					);// 请求件数
			jsonitem.put("num", UtilObject.getBigDecimalOrZero(item.get("qty")).divide(new BigDecimal(standard),0, BigDecimal.ROUND_UP)
					);// 实际件数

			jsonitem.put("qty", item.get("qty"));// 重量
			jsonitem.put("erp_batch", item.get("erp_batch"));
			jsonitem.put("production_batch", item.get("production_batch"));
			//上架时用目标仓位
			if( EnumReceiptType.STOCK_TASK_LISTING.getValue()==receiptType){
				jsonitem.put("position", String.valueOf(item.get("wh_code")) + "-" + item.get("target_area_code") + "-"
						+ item.get("target_position_code"));//目标 仓位
			}else{
				//下架时用发出仓位
				jsonitem.put("position", String.valueOf(item.get("source_wh_code")) + "-" + item.get("source_area_code") + "-"
						+ item.get("source_position_code"));//发出 仓位
				
			}
			jsonitem.put("itemremark", item.get("remark"));
			printItem.add(jsonitem);
		}
		params.put("print_head", printHead);
		params.put("print_item", printItem);
		String proTransUrl = UtilProperties.getInstance().getPrintUrl() + "/wms/print/cwTask";
		if (null != printItem && printItem.size() > 0) {
			printreturn = UtilREST.executePostJSONTimeOut(proTransUrl, params, 30);
		}
		if (null != printreturn && printreturn.size() > 0 && printreturn.containsKey("fileName")) {
			fileName = printreturn.getString("fileName");
		}
		return fileName;

	}

	@Override
	public String printTransport(Integer stockTransportId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> headmap =materialTransportHeadDao.selectHeadById(stockTransportId);
		List<Map<String, Object>> list =materialTransportItemDao.selectItemById(stockTransportId);
		JSONObject printHead = new JSONObject();
		JSONArray printItem = new JSONArray();
		JSONObject params = new JSONObject();
		JSONObject printreturn = new JSONObject();
		String fileName = "";

		printHead.put("fty_name", headmap.get("fty_name"));
	
		printHead.put("move_type", headmap.get("move_type_code") + "-" + headmap.get("move_type_name"));//
		printHead.put("driver", headmap.get("delivery_driver_name"));
		printHead.put("vehicle", headmap.get("delivery_vehicle_name"));
		printHead.put("stock_input_code", headmap.get("stock_transport_code"));
		printHead.put("create_time", String.valueOf(headmap.get("create_time")).split(" ")[0]);// 创建日期
		printHead.put("headremark", UtilObject.getStringOrEmpty(headmap.get("remark")));
		printHead.put("ware", headmap.get("wh_code") + "-" + headmap.get("wh_name"));

		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> item = list.get(i);
			JSONObject jsonitem = new JSONObject();
		//	printHead.put("fty_name", item.get("fty_output_name"));
			
		//	printHead.put("fty_input_name", item.get("fty_input_name"));
			jsonitem.put("specs_model", String.valueOf(item.get("mat_name")) + item.get("mat_code"));
			jsonitem.put("package_type_name", item.get("package_type_name"));
			double standard = Double.parseDouble(String.valueOf(item.get("package_standard")));
			jsonitem.put("reqnum", UtilObject.getBigDecimalOrZero(item.get("transport_qty")).divide(new BigDecimal(standard),10,BigDecimal.ROUND_UP)
					);// 请求件数
			jsonitem.put("num", UtilObject.getBigDecimalOrZero(item.get("transport_qty")).divide(new BigDecimal(standard),10,BigDecimal.ROUND_UP)
					);// 实际件数
			jsonitem.put("qty", item.get("transport_qty"));// 重量
			jsonitem.put("production_batch", item.get("product_batch"));
			jsonitem.put("erp_batch", item.get("erp_batch"));
			jsonitem.put("remark", item.get("remark"));
			jsonitem.put("location_input", item.get("fty_code")+"-"+item.get("location_code"));
			printItem.add(jsonitem);
		}
		params.put("print_head", printHead);
		params.put("print_item", printItem);

		String proTransUrl = UtilProperties.getInstance().getPrintUrl() + "/wms/print/cwTrans";
		if (null != printItem && printItem.size() > 0) {
			printreturn = UtilREST.executePostJSONTimeOut(proTransUrl, params, 30);
		}
		if (null != printreturn && printreturn.size() > 0 && printreturn.containsKey("fileName")) {
			fileName = printreturn.getString("fileName");
		}
		return fileName;
	}

}
