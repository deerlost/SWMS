package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Resource;
import com.inossem.wms.dao.batch.BatchMaterialMapper;
import com.inossem.wms.dao.biz.*;
import com.inossem.wms.dao.dic.DicClassTypeMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.biz.*;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.enums.*;
import com.inossem.wms.model.stock.StockBatch;
import com.inossem.wms.model.stock.StockOccupancy;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.vo.*;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.util.UtilProperties;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.inossem.wms.dao.dic.DicMaterialMapper;
import com.inossem.wms.dao.dic.DicPackageTypeMapper;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.dic.DicUnitMapper;
import com.inossem.wms.dao.stock.StockBatchMapper;
import com.inossem.wms.dao.stock.StockPositionMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.model.sap.SapDeliveryOrderHead;
import com.inossem.wms.model.sap.SapDeliveryOrderItem;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IOutPutCWService;
import com.inossem.wms.service.biz.ITaskService;
import com.inossem.wms.service.intfc.ICwErpWs;
import com.inossem.wms.service.intfc.ICwMesWs;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.wsdl.mes.update.ArrayOfWmMvRecInteropeDto;
import com.inossem.wms.wsdl.mes.update.WmIopRetVal;
import com.inossem.wms.wsdl.mes.update.WmMvRecInteropeDto;
import com.inossem.wms.wsdl.sap.input.DTCWWMSMATDOCPOSTREQ;
import com.inossem.wms.wsdl.sap.input.DTCWWMSMATDOCPOSTREQ.ITDOCHEAD;
import com.inossem.wms.wsdl.sap.input.DTCWWMSMATDOCPOSTREQ.ITDOCITEM;
import com.inossem.wms.wsdl.sap.inputc.DTCWWMSCANCELMATDOCREQ;
import com.inossem.wms.wsdl.sap.inputc.DTCWWMSCANCELMATDOCREQ.ITMATDOC;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTREQ;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTREQ.ISMSGHEAD;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTREQ.ISSPLITBATCH;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTREQ.ITLIKP;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTREQ.ITLIPS;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTREQ.ISSPLITBATCH.ZLTSDBATCHCONTENT;
import com.inossem.wms.wsdl.sap.outputc.DTCWWMSDOREVERSEREQ;
import com.inossem.wms.wsdl.sap.outputc.DTCWWMSDOREVERSEREQ.ITDATA;
import com.inossem.wms.wsdl.sap.outputc.MSGHEAD;
import net.sf.json.JSONObject;

/**
 * 出库
 * 
 * @author sw
 *
 */
@Service
@Transactional
public class OutPutCWServiceImpl implements IOutPutCWService {

	@Resource
	private BizStockOutputHeadMapper outPutHeadDao;
	@Resource
	private BizStockOutputItemMapper outPutItemDao;
	@Resource
	private BizStockOutputPositionMapper outPutPositionDao;
	@Resource
	private SequenceDAO sequenceDao;
	@Resource
	private ICommonService commonService;
	@Resource
	private BizStockTaskReqHeadMapper StockTaskReqHeadDao;
	@Autowired
	private BizStockTaskReqItemMapper StockTaskReqItemDao;
	@Autowired
	private BizStockTaskReqPositionMapper StockTaskReqPositionDao;
	@Autowired
	private BizStockTaskPositionCwMapper StockTaskPositionDao;
	@Autowired
	private StockPositionMapper stockPositionDao;
	@Autowired
	private StockBatchMapper stockBatchDao;
	@Autowired
	private DicMaterialMapper dicMaterialDao;
	@Autowired
	private BatchMaterialMapper batchMaterialDao;
	@Autowired
	private BizStockTaskHeadCwMapper bizStockTaskHeadCwDao;
	@Autowired
	private BizStockTaskItemCwMapper bizStockTaskItemCwDao;
	@Autowired
	private BizStockTaskPositionCwMapper bizStockTaskPositionCwDao;
	@Autowired
	private BizAllocateCargoHeadMapper allocateCargoHeadDao;
	@Autowired
	private BizAllocateCargoItemMapper allocateCargoItemDao;
	@Autowired
	private BizAllocateCargoPositionMapper allocateCargoPositionDao;
	@Autowired
	private DicStockLocationMapper stockLocationDao;
	@Autowired
	private BizStockInputTransportHeadMapper bizStockInputTransportHeadDao;
	@Autowired
	private DicFactoryMapper dicFactoryDao;
	@Autowired
	private DicUnitMapper unitDao;
	@Autowired
	private IDictionaryService dictionaryService;
	@Autowired
	private BizBusinessHistoryMapper businessHistoryDao;
	@Autowired
	private ICwErpWs CwErpWsImpl;
	@Autowired
	private ICwMesWs CwMesWsImpl;
	@Autowired
	private BizFailMesMapper failMesDao;
	@Autowired
	private DicClassTypeMapper classTypeDao;
	@Autowired
	private ITaskService taskService;
	@Autowired
	private BizStockInputHeadMapper stockInputHeadDao;
	@Autowired
	private DicPackageTypeMapper packageTypeDao;
	
	/**
	 * 查询出库列表
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getOrderList(Map<String, Object> param) {
		return outPutHeadDao.selectOrderListOnPaging(param);
	}
	
	@Override
	public List<Map<String,Object>> getCargoOrderList(String code,String userId){
		return allocateCargoHeadDao.getCargoOrderList(code,userId);
	}
	
	@Override
	public List<Map<String, Object>> getOrderListAll(Map<String, Object> param) {
		return outPutHeadDao.selectOrderListAllOnPaging(param);
	}

	/**
	 * 查询sap交货单详情 并格式化本地需要格式
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getOrderViewBySap(String code, Byte type) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		SapDeliveryOrderHead head = CwErpWsImpl.getDoOrder(code);

		if (head == null) {
			throw new WMSException("获取交货单信息异常");
		}

		if ("ZR10".equals(head.getOrderType())) {
			if (type != EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) {
				throw new WMSException("交货单" + code + "不适用于" + EnumReceiptType.getNameByValue(type));
			}
		}
		// sap获取的抬头信息
		result.put("sale_order_code", head.getSaleOrderCode());// 销售订单号
		result.put("refer_receipt_code", head.getDeliveryOrderCode());// 前置单据号 交货单号
		result.put("preorder_id", head.getContractNumber());// 合同编号
		result.put("receive_code", head.getReceiveCode());// 客户编号
		result.put("receive_name", head.getReceiveName());// 客户名称
		result.put("order_type", head.getOrderType());// 订单类型
		result.put("order_type_name", head.getOrderTypeName());// 订单类型名称
		result.put("class_type_id", bizStockInputTransportHeadDao.selectNowClassType());// 班次id
		result.put("class_type_list", this.selectClassTypeList());
		//result.put("syn_type", EnumSynType.MES_SAP_SYN.getValue());// 同步类型
		result.put("remark", "");// 备注
		// sap获取行项目
		List<SapDeliveryOrderItem> itemSapList = head.getItemList();
		DicFactory factory = null;
		// 交货单创建配货单时，当交货单类型是ZR10，配货库存查询工厂固定为SWX1
		if ("ZR10".equals(head.getOrderType())) {
			factory = dicFactoryDao.selectByCode("SWX1");
		} else {
			factory = dicFactoryDao.selectByCode(itemSapList.get(0).getFtyCode());
		}
		result.put("fty_id", factory.getFtyId());// 工厂id
		result.put("fty_code", factory.getFtyCode());// 工厂编码
		result.put("fty_name", factory.getFtyName());// 工厂名字
		// 返回值行项目
		List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
		for (SapDeliveryOrderItem sapItem : itemSapList) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("refer_receipt_rid", sapItem.getDeliveryOrderRid());// sap行号
			item.put("refer_receipt_code", code);// 交货单号
			item.put("order_qty", sapItem.getQty());// 交货数量
			item.put("write_off", EnumBoolean.FALSE.getValue());// 冲销状态
			item.put("remark", "");// 备注
			Map<String, Object> mat = dicMaterialDao.getMatUnitByMatCode(sapItem.getMatCode());
			item.put("mat_id", mat.get("mat_id"));// 物料id
			item.put("mat_code", mat.get("mat_code"));// 物料编码
			item.put("mat_name", mat.get("mat_name"));// 物料描述
			DicUnit unit = unitDao.getByCode(sapItem.getUnitCode());
			item.put("unit_id", unit.getUnitId());// 单位id
			item.put("unit_code", unit.getUnitCode());// 单位编码
			item.put("unit_name", unit.getNameZh());// 单位描述
			item.put("erp_batch", sapItem.getErpBatch());// erp批次
			item.put("erp_remark", sapItem.getErpRemark());// erp备注
			item.put("fty_id", factory.getFtyId());// 工厂id
			item.put("fty_code", factory.getFtyCode());// 工厂编码
			item.put("fty_name", factory.getFtyName());// 工厂名字
			if (sapItem.getLocationCode() != null && !sapItem.getLocationCode().equals("")) {
				RelUserStockLocationVo location = stockLocationDao.selectByFtyCodeAndLocationCode(factory.getFtyCode(),
						sapItem.getLocationCode());
				item.put("location_id", location.getLocationId());
				item.put("location_code", location.getLocationCode());
				item.put("location_name", location.getLocationName());
			} else {
				item.put("location_id", 0);
				item.put("location_code", "");
				item.put("location_name", "");
			}
			item.put("output_qty", BigDecimal.ZERO);// 已完成数量
			itemList.add(item);
		}
		result.put("item_list", itemList);
		return result;
	}

	/**
	 * 查询交货单详情
	 */
	@Override
	public Map<String, Object> getDeliverOrderView(String code, Byte type) throws Exception {
		// 查询参数
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("referReceiptCode", code);
		// 需要排除的状态
		param.put("status", EnumReceiptStatus.RECEIPT_WRITEOFF.getValue());
		param.put("receiptType", type);
		param.put("synType", EnumSynType.MES_SYN.getValue());
		BizStockOutputHeadVo outputHead = outPutHeadDao.selectHeadByOrderId(param);
		if (outputHead != null) {
			throw new WMSException("交货单" + code + "已用于" + EnumReceiptType.getNameByValue(outputHead.getReceiptType()));
		}
//		if (type != EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) {
//			Map<String, Object> aparam = new HashMap<String, Object>();
//			aparam.put("referReceiptCode", code);
//			List<Byte> list = new ArrayList<Byte>();
//			list.add(EnumReceiptStatus.RECEIPT_WORK.getValue());
//			list.add(EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
//			aparam.put("status", list);
//			List<BizAllocateCargoHead> allocateCargoHeads = allocateCargoHeadDao.selectByReferReceiptCode(aparam);
//			if (allocateCargoHeads != null && !allocateCargoHeads.isEmpty()) {
//				throw new WMSException("交货单" + code + "已经进行配货,无法进行" + EnumReceiptType.getNameByValue(type));
//			}
//		}
		// 返回值
		Map<String, Object> result = new HashMap<String, Object>();
		// sap交货单
		result = this.getOrderViewBySap(code, type);
		// 销售出库处理
		if (type == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) {
			// 配货单行项目集合
			Map<String, List<BizAllocateCargoItemVo>> resultCargoItem = new HashMap<String, List<BizAllocateCargoItemVo>>();
			param = new HashMap<String, Object>();
			param.put("referReceiptCode", code);
			List<BizAllocateCargoItemVo> allItemList = this.allocateCargoItemDao.selectByReferReceiptCode(param);
			for (BizAllocateCargoItemVo cargoItem : allItemList) {
				List<BizAllocateCargoItemVo> cargoListTemp = new ArrayList<BizAllocateCargoItemVo>();
				if (resultCargoItem.containsKey(cargoItem.getReferReceiptRid())) {
					cargoListTemp = resultCargoItem.get(cargoItem.getReferReceiptRid());
					cargoListTemp.add(cargoItem);
					resultCargoItem.put(cargoItem.getReferReceiptRid(), cargoListTemp);
				} else {
					cargoListTemp.add(cargoItem);
					resultCargoItem.put(cargoItem.getReferReceiptRid(), cargoListTemp);
				}
			}
			// 配货单状态已作业
			param.put("astatus", EnumReceiptStatus.RECEIPT_WORK.getValue());
			// 作业单状态未完成
			param.put("status", EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
			// 下架作业单集合
			Map<String, List<Map<String, Object>>> listMap = this.getTaskListBySale(param);
			// sap获取的行项目集合
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> itemSapList = (List<Map<String, Object>>) result.get("item_list");
			// 行项目返回值
			List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
			int index = 0;
			for (int i = 0; i < itemSapList.size(); i++) {
				Map<String, Object> sapItem = itemSapList.get(i);
				List<BizAllocateCargoItemVo> cargoItemList = resultCargoItem.get(sapItem.get("refer_receipt_rid"));
				if (cargoItemList != null) {
					for (int j = 0; j < cargoItemList.size(); j++) {
						List<Map<String, Object>> task = new ArrayList<Map<String, Object>>();
						BizAllocateCargoItemVo cargoItem = cargoItemList.get(j);
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("stock_output_rid", index++);
						item.put("refer_receipt_rid", sapItem.get("refer_receipt_rid"));
						item.put("refer_receipt_code", code);
						item.put("order_qty", sapItem.get("order_qty"));
						item.put("write_off", sapItem.get("write_off"));
						item.put("remark", "");
						item.put("mat_id", cargoItem.getMatId());
						item.put("mat_code", cargoItem.getMatCode());
						item.put("mat_name", cargoItem.getMatName());
						item.put("unit_id", cargoItem.getUnitId());
						item.put("unit_code", cargoItem.getUnitCode());
						item.put("unit_name", cargoItem.getUnitName());
						item.put("erp_batch", cargoItem.getErpBatch());
						item.put("erp_remark", cargoItem.getErpRemark());
						item.put("fty_id", cargoItem.getFtyId());
						item.put("fty_code", cargoItem.getFtyCode());
						item.put("fty_name", cargoItem.getFtyName());
						item.put("location_id", cargoItem.getLocationId());
						item.put("location_code", cargoItem.getLocationCode());
						item.put("location_name", cargoItem.getLocationName());
						BigDecimal outputQty = BigDecimal.ZERO;
						item.put("output_qty", outputQty);
						// TODO 为了方便查询 插入position信息
						Map<String, Object> paramCar = new HashMap<String, Object>();
						paramCar.put("allocate_cargo_id", cargoItem.getAllocateCargoId());
						paramCar.put("allocate_cargo_rid", cargoItem.getAllocateCargoRid());
						List<Map<String, String>> cargoListposition = allocateCargoPositionDao
								.selectPositionBySuperId(paramCar);
						for (int p = 0; p < cargoListposition.size(); p++) {
							cargoListposition.get(p).put("stock_output_rid",
									UtilObject.getStringOrEmpty(item.get("stock_output_rid")));
							cargoListposition.get(p).put("stock_output_position_id", p + 1 + "");
						}
						item.put("position_list", cargoListposition);
						String key = sapItem.get("refer_receipt_rid") + "-" + cargoItem.getLocationId();
						task = listMap.get(key);
						if (task != null && task.size() > 0) {
							for (Map<String, Object> map : task) {
								outputQty = outputQty.add(UtilObject.getBigDecimalOrZero(map.get("qty")));
							}
							item.put("output_qty", outputQty);
						}
						item.put("task_list", task);
						itemList.add(item);
					}
				} else {
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("stock_output_rid", index++);
					item.put("refer_receipt_rid", sapItem.get("refer_receipt_rid"));
					item.put("refer_receipt_code", code);
					item.put("order_qty", sapItem.get("order_qty"));
					item.put("write_off", EnumBoolean.FALSE.getValue());
					item.put("remark", "");
					item.put("mat_id", sapItem.get("mat_id"));
					item.put("mat_code", sapItem.get("mat_code"));
					item.put("mat_name", sapItem.get("mat_name"));
					item.put("unit_id", sapItem.get("unit_id"));
					item.put("unit_code", sapItem.get("unit_code"));
					item.put("unit_name", sapItem.get("unit_name"));
					item.put("erp_batch", sapItem.get("erp_batch"));
					item.put("erp_remark", sapItem.get("erp_remark"));
					item.put("fty_id", sapItem.get("fty_id"));
					item.put("fty_code", sapItem.get("fty_code"));
					item.put("fty_name", sapItem.get("fty_name"));
					item.put("location_id", sapItem.get("location_id"));
					item.put("location_code", sapItem.get("location_code"));
					item.put("location_name", sapItem.get("location_name"));
					item.put("output_qty", sapItem.get("output_qty"));
					itemList.add(item);
				}
			}
			result.put("item_list", itemList);
			// 直发出库 紧急作业销售出库 紧急记账销售出库 处理
		} else if (type == EnumReceiptType.STOCK_OUTPUT_STRAIGTH.getValue()
				|| type == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()
				|| type == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue()
				|| type == EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue()) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> itemList = (List<Map<String, Object>>) result.get("item_list");
			List<RelUserStockLocationVo> locationVos = stockLocationDao
					.selectByFtyId(UtilObject.getIntOrZero(itemList.get(0).get("fty_id")));
			List<Map<String, Object>> locationList = new ArrayList<Map<String, Object>>();
			for (RelUserStockLocationVo locationVo : locationVos) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("location_id", locationVo.getLocationId());
				map.put("location_code", locationVo.getLocationCode());
				map.put("location_name", locationVo.getLocationName());
				locationList.add(map);
			}
			for (Map<String, Object> item : itemList) {
				item.put("location_list", locationList);
				item.put("erpbatch_list", commonService.getErpBatchList(UtilObject.getIntOrZero(item.get("mat_id")),
						UtilObject.getIntOrZero(item.get("fty_id"))));
				item.remove("output_qty");
			}
			result.put("item_list", itemList);
		}
		return result;
	}

	/**
	 * 查询销售出库下架作业单集合
	 * 
	 * @param param
	 * @return
	 */
	public Map<String, List<Map<String, Object>>> getTaskListBySale(Map<String, Object> param) {
		// 下架作业单集合
		List<Map<String, Object>> taskList = bizStockTaskHeadCwDao.selectTaskListBySale(param);
		Map<String, List<Map<String, Object>>> listMap = new HashMap<String, List<Map<String, Object>>>();
		for (Map<String, Object> map : taskList) {
			String key = map.get("refer_receipt_rid") + "-" + map.get("location_id");
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			if (listMap.containsKey(key)) {
				mapList = listMap.get(key);
				mapList.add(map);
				listMap.put(key, mapList);
			} else {
				mapList.add(map);
				listMap.put(key, mapList);
			}
		}
		return listMap;
	}

	/**
	 * 获取出库视图
	 *
	 * @param stock_output_id
	 * @return
	 * @throws Exception
	 */
	@Override
	public BizStockOutputHeadVo getOrderView(Integer stock_output_id) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stock_output_id);
		BizStockOutputHeadVo head = outPutHeadDao.selectHeadByOrderId(param);

		List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);

		if (itemList != null && itemList.size() > 0) {

			// 库存地点集合
			List<RelUserStockLocationVo> locationVos = stockLocationDao
					.selectByFtyId(UtilObject.getIntOrZero(itemList.get(0).getFtyId()));
			List<Map<String, Object>> locationList = new ArrayList<Map<String, Object>>();
			for (RelUserStockLocationVo locationVo : locationVos) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("location_id", locationVo.getLocationId());
				map.put("location_code", locationVo.getLocationCode());
				map.put("location_name", locationVo.getLocationName());
				locationList.add(map);
			}
			// 根据单据类型不同控制返回值不同
			for (BizStockOutputItemVo itemVo : itemList) {
				BigDecimal outputQty = BigDecimal.ZERO;// 下架数量
				List<Map<String, Object>> task = new ArrayList<Map<String, Object>>();
				// 销售出库
				if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) {
					Map<String, Object> paramnew = new HashMap<String, Object>();
					paramnew.put("referReceiptCode", head.getReferReceiptCode());
					// 出库单号
					paramnew.put("beforeOrderId", head.getStockOutputId());
					paramnew.put("beforeOrderType", head.getReceiptType());
					Byte status = EnumReceiptStatus.RECEIPT_WORK.getValue();
					if(head.getStatus() == EnumReceiptStatus.RECEIPT_UNFINISH.getValue()) {
						 status = EnumReceiptStatus.RECEIPT_WORK.getValue();
					}else if(head.getStatus() == EnumReceiptStatus.RECEIPT_INVOICED.getValue()
							||head.getStatus() == EnumReceiptStatus.RECEIPT_FINISH.getValue()) {
						 status = EnumReceiptStatus.RECEIPT_FINISH.getValue();
					}else {
						 status = head.getStatus() ;
					}
					// 配货单状态
					paramnew.put("astatus", status);
					Map<String, List<Map<String, Object>>> taskMap = this.getTaskListBySale(paramnew);
					String key = itemVo.getReferReceiptRid() + "-" + itemVo.getLocationId();
					task = taskMap.get(key);
					if (task != null && task.size() > 0) {
						for (Map<String, Object> map : task) {
							outputQty = outputQty.add(UtilObject.getBigDecimalOrZero(map.get("qty")));
						}
					}
					itemVo.setTaskList(task);
					itemVo.setOutputQty(outputQty);
					param = new HashMap<String, Object>();
					param.put("stockOutputId", itemVo.getStockOutputId());
					param.put("stockOutputRid", itemVo.getStockOutputRid());
					itemVo.setPositionList(outPutPositionDao.selectPostionByOrderId(param));
					// 报废出库 其他出库
				} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()
						|| head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_OTHER.getValue()) {
					Map<String, Object> paramnew = new HashMap<String, Object>();
					paramnew.put("referReceiptCode", itemVo.getStockOutputCode());
					paramnew.put("referReceiptRid", itemVo.getStockOutputRid());
					paramnew.put("referReceiptType", head.getReceiptType());
					paramnew.put("referReceiptId", itemVo.getStockOutputId());
					Byte status = EnumReceiptStatus.RECEIPT_UNFINISH.getValue();
					// 已冲销 已完成
					if (head.getStatus() == EnumReceiptStatus.RECEIPT_FINISH.getValue()
							|| head.getStatus() == EnumReceiptStatus.RECEIPT_WRITEOFF.getValue()) {
						status = EnumReceiptStatus.RECEIPT_FINISH.getValue();
						// 未完成 已作业 已拣货 已提交
					} else if (head.getStatus() == EnumReceiptStatus.RECEIPT_UNFINISH.getValue()
							|| head.getStatus() == EnumReceiptStatus.RECEIPT_WORK.getValue()
							|| head.getStatus() == EnumReceiptStatus.RECEIPT_SORTING.getValue()
							|| head.getStatus() == EnumReceiptStatus.RECEIPT_SUBMIT.getValue()) {
						status = EnumReceiptStatus.RECEIPT_UNFINISH.getValue();
					}
					paramnew.put("status", status);
					task = bizStockTaskHeadCwDao.selectTaskListByOutput(paramnew);
					if (task != null && task.size() > 0) {
						for(Map<String,Object> tt:task) {
							outputQty = outputQty.add(UtilObject.getBigDecimalOrZero(tt.get("qty")));
						}
					}
					itemVo.setTaskList(task);
					itemVo.setOutputQty(outputQty);
					param = new HashMap<String, Object>();
					param.put("stockOutputId", itemVo.getStockOutputId());
					param.put("stockOutputRid", itemVo.getStockOutputRid());
					itemVo.setPositionList(outPutPositionDao.selectPostionByOrderId(param));
					// 直发出库 紧急记账销售出库
				} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_STRAIGTH.getValue()
						|| head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue()) {
					param = new HashMap<String, Object>();
					param.put("stockOutputId", itemVo.getStockOutputId());
					param.put("stockOutputRid", itemVo.getStockOutputRid());
					itemVo.setPositionList(outPutPositionDao.selectPostionByOrderId(param));
					itemVo.setLocationList(locationList);
					itemVo.setErpbatchList(commonService.getErpBatchList(itemVo.getMatId(), itemVo.getFtyId()));
					param = new HashMap<String, Object>();
					param.put("stockOutputId", itemVo.getStockOutputId());
					param.put("stockOutputRid", itemVo.getStockOutputRid());
					itemVo.setPositionList(outPutPositionDao.selectPostionByOrderId(param));
					// 紧急作业销售出库
				} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue()
						|| head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()) {
					BigDecimal sentqty = BigDecimal.ZERO;// 下架数量
					Map<String, Object> paramnew = new HashMap<String, Object>();
					paramnew.put("referReceiptCode", itemVo.getStockOutputCode());
					paramnew.put("referReceiptRid", itemVo.getStockOutputRid());
					paramnew.put("referReceiptType", head.getReceiptType());
					paramnew.put("referReceiptId", itemVo.getStockOutputId());
					Byte status = EnumReceiptStatus.RECEIPT_UNFINISH.getValue();
					// 已冲销 已完成
					if (head.getStatus() == EnumReceiptStatus.RECEIPT_FINISH.getValue()
							|| head.getStatus() == EnumReceiptStatus.RECEIPT_WRITEOFF.getValue()
							|| head.getStatus() == EnumReceiptStatus.RECEIPT_SORTING.getValue()) {
						status = EnumReceiptStatus.RECEIPT_FINISH.getValue();
						// 未完成 已作业 已提交
					} else {
						status = EnumReceiptStatus.RECEIPT_UNFINISH.getValue();
					}
					paramnew.put("status", status);
					task = bizStockTaskHeadCwDao.selectTaskListByOutput(paramnew);
					if (task != null && task.size() > 0) {
						for(Map<String,Object> tt:task) {
							sentqty = sentqty.add(UtilObject.getBigDecimalOrZero(tt.get("qty")));
						}
					}
					itemVo.setTaskList(task);
					itemVo.setSendQty(sentqty);
					param = new HashMap<String, Object>();
					param.put("stockOutputId", itemVo.getStockOutputId());
					param.put("stockOutputRid", itemVo.getStockOutputRid());
					param.put("receiptType", head.getReceiptType());
					itemVo.setPositionList(outPutPositionDao.selectPostionByOrderId(param));
					itemVo.setLocationList(locationList);
					itemVo.setErpbatchList(commonService.getErpBatchList(itemVo.getMatId(), itemVo.getFtyId()));
				}
			}
		}
		head.setItemList(itemList);
		return head;
	}

	/**
	 * 出库单详情格式化
	 * 
	 * @param head
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> getOrderViewFormat(BizStockOutputHeadVo head) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		// 销售出库
		if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) {
			result.put("stock_output_id", head.getStockOutputId());// 出库单id
			result.put("stock_output_code", head.getStockOutputCode());// 出库单code
			result.put("refer_receipt_code", head.getReferReceiptCode());// 交货单号
			result.put("sale_order_code", head.getSaleOrderCode());// 销售单号
			result.put("receipt_type_name", head.getReceiptTypeName());// 销售单类型
			result.put("preorder_id", head.getPreorderId());// 合同号
			result.put("receive_code", head.getReceiveCode());// 客户编号
			result.put("receive_name", head.getReceiveName());// 客户名字
			result.put("class_type_id", head.getClassTypeId());// 班次id
			result.put("class_type_name", head.getClassTypeName());// 班次name
			result.put("syn_type", head.getSynType());// 同步类型
			result.put("remark", head.getRemark());// 备注
			result.put("status", head.getStatus());// 状态码
			result.put("status_name", head.getStatusName());// 状态中文
			result.put("create_user", head.getUserName());// 创建人
			result.put("create_time", head.getCreateTime());// 创建时间
			List<BizStockOutputItemVo> itemVos = head.getItemList();
			result.put("mat_doc_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMatDocCode()));// sap凭证
			result.put("mes_doc_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMesDocCode()));// mes凭证
			result.put("mat_write_off_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMatWriteOffCode()));//sap冲销凭证
			result.put("mes_write_off_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMesWriteOffCode()));//mes冲销凭证
			result.put("class_type_list", this.selectClassTypeList());
			List<Map<String, Object>> itemVosMap = new ArrayList<Map<String, Object>>();
			if (itemVos != null && itemVos.size() > 0) {
				for (BizStockOutputItemVo itemVo : itemVos) {
					Map<String, Object> detail = new HashMap<String, Object>();
					detail.put("item_id", UtilObject.getIntOrZero(itemVo.getItemId()));// 主键
					detail.put("fty_id", UtilObject.getStringOrEmpty(itemVo.getFtyId()));// 工厂id
					detail.put("mat_id", UtilObject.getIntOrZero(itemVo.getMatId()));// 物料id
					detail.put("mat_code", UtilObject.getStringOrEmpty(itemVo.getMatCode()));// 物料code
					detail.put("mat_name", UtilObject.getStringOrEmpty(itemVo.getMatName()));// 物料name
					detail.put("unit_id", UtilObject.getIntOrZero(itemVo.getUnitId()));// 单位id
					detail.put("unit_code", UtilObject.getStringOrEmpty(itemVo.getUnitCode()));// 单位code
					detail.put("unit_name", UtilObject.getStringOrEmpty(itemVo.getUnitName()));// 单位name
					detail.put("order_qty", UtilObject.getBigDecimalOrZero(itemVo.getStockQty()));// 订单数量
					detail.put("output_qty", UtilObject.getBigDecimalOrZero(itemVo.getOutputQty()));// 已下架数量
					detail.put("location_id", UtilObject.getIntOrZero(itemVo.getLocationId()));// 库存地点id
					detail.put("location_code", UtilObject.getStringOrEmpty(itemVo.getLocationCode()));// 库存地点code
					detail.put("location_name", UtilObject.getStringOrEmpty(itemVo.getLocationName()));// 库存地点name
					detail.put("write_off", UtilObject.getByteOrNull(itemVo.getWriteOff()));// 冲销码
					detail.put("write_off_name", UtilObject.getStringOrEmpty(itemVo.getWriteOffName()));// 冲销状态
					detail.put("stock_output_id", UtilObject.getIntOrZero(itemVo.getStockOutputId()));// 出库单
					detail.put("stock_output_rid", UtilObject.getIntOrZero(itemVo.getStockOutputRid()));// 行序号
					detail.put("refer_receipt_rid", UtilObject.getStringOrEmpty(itemVo.getReferReceiptRid()));// sap行序号
					detail.put("mat_doc_code", UtilObject.getStringOrEmpty(itemVo.getMatDocCode()));// sap凭证
					detail.put("mes_doc_code", UtilObject.getStringOrEmpty(itemVo.getMesDocCode()));// mes凭证
					detail.put("remark", UtilObject.getStringOrEmpty(itemVo.getRemark()));// 行备注
					detail.put("erp_batch", itemVo.getErpBatch());// erp批次
					detail.put("mes_write_off_code", UtilObject.getStringOrEmpty(itemVo.getMesWriteOffCode()));//mes冲销凭证
					detail.put("mat_write_off_code", UtilObject.getStringOrEmpty(itemVo.getMatWriteOffCode()));//sap冲销凭证
					detail.put("samp_name", UtilObject.getStringOrEmpty(itemVo.getSampName()));//样品名称
					detail.put("specification", UtilObject.getStringOrEmpty(itemVo.getSpecification()));//样品规格
					detail.put("standard", UtilObject.getStringOrEmpty(itemVo.getStandard()));//执行标准
					detail.put("package_standard_ch", UtilObject.getStringOrEmpty(itemVo.getPackageStandardCh()));
					detail.put("package_standard", UtilObject.getBigDecimalOrZero(itemVo.getPackageStandard()));
					detail.put("package_type_id", UtilObject.getStringOrEmpty(itemVo.getPackageTypeId()));
					detail.put("package_type_code", UtilObject.getStringOrEmpty(itemVo.getPackageTypeCode()));
					detail.put("position_list", itemVo.getPositionList());
					if (itemVo.getTaskList() != null) {
						detail.put("task_list", itemVo.getTaskList());// 下架作业单集合
					}
					itemVosMap.add(detail);
				}
			}
			result.put("item_list", itemVosMap);
			// 直发出库
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_STRAIGTH.getValue()) {
			result.put("stock_output_id", head.getStockOutputId());// 出库单id
			result.put("stock_output_code", head.getStockOutputCode());// 出库单code
			result.put("refer_receipt_code", head.getReferReceiptCode());// 交货单号
			result.put("sale_order_code", head.getSaleOrderCode());// 销售单号
			result.put("receipt_type_name", head.getReceiptTypeName());// 销售单类型
			result.put("preorder_id", head.getPreorderId());// 合同号
			result.put("receive_code", head.getReceiveCode());// 客户编号
			result.put("receive_name", head.getReceiveName());// 客户名字
			result.put("class_type_id", head.getClassTypeId());// 班次id
			result.put("class_type_name", head.getClassTypeName());// 班次name
			result.put("syn_type", head.getSynType());// 同步类型
			result.put("remark", head.getRemark());// 备注
			result.put("status", head.getStatus());// 状态码
			result.put("status_name", head.getStatusName());// 状态中文
			result.put("create_user", head.getUserName());// 创建人
			result.put("create_time", head.getCreateTime());// 创建时间
			result.put("class_type_list", this.selectClassTypeList());
			List<BizStockOutputItemVo> itemVos = head.getItemList();
			result.put("mat_doc_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMatDocCode()));// sap凭证
			result.put("mes_doc_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMesDocCode()));// mes凭证
			result.put("mat_doc_code_transport",
					UtilObject.getStringOrEmpty(itemVos.get(0).getPositionList().get(0).getMatDocCode()));
			result.put("mes_doc_code_transport",
					UtilObject.getStringOrEmpty(itemVos.get(0).getPositionList().get(0).getMesDocCode()));
			List<Map<String, Object>> itemVosMap = new ArrayList<Map<String, Object>>();
			if (itemVos != null && itemVos.size() > 0) {
				for (BizStockOutputItemVo itemVo : itemVos) {
					Map<String, Object> detail = new HashMap<String, Object>();
					detail.put("item_id", UtilObject.getIntOrZero(itemVo.getItemId()));// 主键
					detail.put("fty_id", UtilObject.getStringOrEmpty(itemVo.getFtyId()));// 工厂id
					detail.put("mat_id", UtilObject.getIntOrZero(itemVo.getMatId()));// 物料id
					detail.put("mat_code", UtilObject.getStringOrEmpty(itemVo.getMatCode()));// 物料code
					detail.put("mat_name", UtilObject.getStringOrEmpty(itemVo.getMatName()));// 物料name
					detail.put("unit_id", UtilObject.getIntOrZero(itemVo.getUnitId()));// 单位id
					detail.put("unit_code", UtilObject.getStringOrEmpty(itemVo.getUnitCode()));// 单位code
					detail.put("unit_name", UtilObject.getStringOrEmpty(itemVo.getUnitName()));// 单位name
					detail.put("order_qty", UtilObject.getBigDecimalOrZero(itemVo.getStockQty()));// 订单数量
					detail.put("output_qty", UtilObject.getBigDecimalOrZero(itemVo.getOutputQty()));// 已下架数量
					detail.put("location_id", UtilObject.getIntOrZero(itemVo.getLocationId()));// 库存地点id
					detail.put("location_code", UtilObject.getStringOrEmpty(itemVo.getLocationCode()));// 库存地点code
					detail.put("location_name", UtilObject.getStringOrEmpty(itemVo.getLocationName()));// 库存地点name
					detail.put("write_off", UtilObject.getByteOrNull(itemVo.getWriteOff()));// 冲销码
					detail.put("write_off_name", UtilObject.getStringOrEmpty(itemVo.getWriteOffName()));// 冲销状态
					detail.put("stock_output_id", UtilObject.getIntOrZero(itemVo.getStockOutputId()));// 出库单
					detail.put("stock_output_rid", UtilObject.getIntOrZero(itemVo.getStockOutputRid()));// 行序号
					detail.put("refer_receipt_rid", UtilObject.getStringOrEmpty(itemVo.getReferReceiptRid()));// sap行序号
					detail.put("mat_doc_code", UtilObject.getStringOrEmpty(itemVo.getMatDocCode()));// sap凭证
					detail.put("mes_doc_code", UtilObject.getStringOrEmpty(itemVo.getMesDocCode()));// mes凭证
					detail.put("remark", UtilObject.getStringOrEmpty(itemVo.getRemark()));// 行备注
					detail.put("erp_batch", itemVo.getErpBatch());// erp批次
					detail.put("package_standard_ch", UtilObject.getStringOrEmpty(itemVo.getPackageStandardCh()));
					detail.put("package_standard", UtilObject.getBigDecimalOrZero(itemVo.getPackageStandard()));
					detail.put("package_type_id", UtilObject.getStringOrEmpty(itemVo.getPackageTypeId()));
					detail.put("package_type_code", UtilObject.getStringOrEmpty(itemVo.getPackageTypeCode()));
					if (itemVo.getPositionList() != null) {
						detail.put("position_list", itemVo.getPositionList());// 行项目详情集合
					}
					if (itemVo.getLocationList() != null) {
						detail.put("location_list", itemVo.getLocationList());// 库存地点集合
					}
					if (itemVo.getErpbatchList() != null) {
						detail.put("erpbatch_list", itemVo.getErpbatchList());// erp批次集合
					}
					itemVosMap.add(detail);
				}
			}
			result.put("item_list", itemVosMap);
			// 报废出库 其它出库
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()
				|| head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_OTHER.getValue()) {
			result.put("stock_output_id", head.getStockOutputId());// 出库单id
			result.put("stock_output_code", head.getStockOutputCode());// 出库单code
			result.put("class_type_id", head.getClassTypeId());// 班次id
			result.put("class_type_name", head.getClassTypeName());// 班次name
			result.put("syn_type", head.getSynType());// 同步类型
			result.put("remark", head.getRemark());// 备注
			result.put("status", head.getStatus());// 状态码
			result.put("status_name", head.getStatusName());// 状态中文
			result.put("create_user", head.getUserName());// 创建人
			result.put("create_time", head.getCreateTime());// 创建时间
			List<BizStockOutputItemVo> itemVos = head.getItemList();
			result.put("mat_doc_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMatDocCode()));// sap凭证
			result.put("mes_doc_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMesDocCode()));// mes凭证
			result.put("mat_write_off_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMatWriteOffCode()));//sap冲销凭证
			result.put("mes_write_off_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMesWriteOffCode()));//mes冲销凭证
			result.put("move_type_id", UtilObject.getIntOrZero(head.getMoveTypeId()));//移动类型id
			result.put("move_type_code", UtilObject.getStringOrEmpty(head.getMoveTypeCode()));//移动类型code
			result.put("move_type_name", UtilObject.getStringOrEmpty(head.getMoveTypeName()));//移动类型name
			List<Map<String, Object>> itemVosMap = new ArrayList<Map<String, Object>>();
			if (itemVos != null && itemVos.size() > 0) {
				BizStockOutputItemVo item = itemVos.get(0);
				result.put("fty_id", UtilObject.getStringOrEmpty(item.getFtyId()));// 工厂id
				result.put("fty_code", UtilObject.getStringOrEmpty(item.getFtyCode()));// 工厂code
				result.put("fty_name", UtilObject.getStringOrEmpty(item.getFtyName()));// 工厂name
				result.put("location_id", UtilObject.getIntOrZero(item.getLocationId()));// 库存地点id
				result.put("location_code", UtilObject.getStringOrEmpty(item.getLocationCode()));// 库存地点code
				result.put("location_name", UtilObject.getStringOrEmpty(item.getLocationName()));// 库存地点name
				for (BizStockOutputItemVo itemVo : itemVos) {
					Map<String, Object> detail = new HashMap<String, Object>();
					detail.put("item_id", UtilObject.getIntOrZero(itemVo.getItemId()));// 主键
					detail.put("fty_id", UtilObject.getStringOrEmpty(itemVo.getFtyId()));// 工厂id
					detail.put("mat_id", UtilObject.getIntOrZero(itemVo.getMatId()));// 物料id
					detail.put("mat_code", UtilObject.getStringOrEmpty(itemVo.getMatCode()));// 物料code
					detail.put("mat_name", UtilObject.getStringOrEmpty(itemVo.getMatName()));// 物料name
					detail.put("unit_id", UtilObject.getIntOrZero(itemVo.getUnitId()));// 单位id
					detail.put("unit_code", UtilObject.getStringOrEmpty(itemVo.getUnitCode()));// 单位code
					detail.put("unit_name", UtilObject.getStringOrEmpty(itemVo.getUnitName()));// 单位name
					detail.put("order_qty", UtilObject.getBigDecimalOrZero(itemVo.getStockQty()));// 出库数量
					detail.put("output_qty", UtilObject.getBigDecimalOrZero(itemVo.getOutputQty()));// 已下架数量
					detail.put("qty", UtilObject.getBigDecimalOrZero(itemVo.getReturnQty()));// 库存数量
					detail.put("location_id", UtilObject.getIntOrZero(itemVo.getLocationId()));// 库存地点id
					detail.put("location_code", UtilObject.getStringOrEmpty(itemVo.getLocationCode()));// 库存地点code
					detail.put("location_name", UtilObject.getStringOrEmpty(itemVo.getLocationName()));// 库存地点name
					detail.put("write_off", UtilObject.getByteOrNull(itemVo.getWriteOff()));// 冲销码
					detail.put("write_off_name", UtilObject.getStringOrEmpty(itemVo.getWriteOffName()));// 冲销状态
					detail.put("stock_output_id", UtilObject.getIntOrZero(itemVo.getStockOutputId()));// 出库单
					detail.put("stock_output_rid", UtilObject.getIntOrZero(itemVo.getStockOutputRid()));// 行序号
					detail.put("remark", UtilObject.getStringOrEmpty(itemVo.getRemark()));// 行备注
					detail.put("package_type_id", itemVo.getPackageTypeId());// 包类型id
					detail.put("package_type_code", itemVo.getPackageTypeCode());// 包类型code
					detail.put("package_type_name", itemVo.getPackageTypeName());// 包类型name
					detail.put("erp_batch", itemVo.getErpBatch());// erp批次
					detail.put("product_batch", itemVo.getProductBatch());// 产品批次
					detail.put("quality_batch", itemVo.getQualityBatch());// 质检批次
					detail.put("batch", itemVo.getBatch());// 批次
					detail.put("wh_id", itemVo.getWhId());// 仓库号
					detail.put("mat_doc_code", UtilObject.getStringOrEmpty(itemVo.getMatDocCode()));// sap凭证
					detail.put("mes_doc_code", UtilObject.getStringOrEmpty(itemVo.getMesDocCode()));// mes凭证
					detail.put("mat_write_off_code", UtilObject.getStringOrEmpty(itemVo.getMatWriteOffCode()));//sap冲销凭证
					detail.put("mes_write_off_code", UtilObject.getStringOrEmpty(itemVo.getMesWriteOffCode()));//mes冲销凭证
					detail.put("package_standard_ch", UtilObject.getStringOrEmpty(itemVo.getPackageStandardCh()));
					detail.put("package_standard", UtilObject.getBigDecimalOrZero(itemVo.getPackageStandard()));
					detail.put("package_type_id", UtilObject.getStringOrEmpty(itemVo.getPackageTypeId()));
					detail.put("package_type_code", UtilObject.getStringOrEmpty(itemVo.getPackageTypeCode()));
					if (itemVo.getTaskList() != null) {
						detail.put("task_list", itemVo.getTaskList());// 下架作业单集合
					}
					itemVosMap.add(detail);
				}
			}
			result.put("item_list", itemVosMap);
			// 紧急作业销售出库
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue()
				|| head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()) {
			result.put("stock_output_id", head.getStockOutputId());// 出库单id
			result.put("stock_output_code", head.getStockOutputCode());// 出库单code
			result.put("refer_receipt_code", head.getReferReceiptCode());// 交货单号
			result.put("sale_order_code", head.getSaleOrderCode());// 销售单号
			result.put("receipt_type_name", head.getReceiptTypeName());// 销售单类型
			result.put("preorder_id", head.getPreorderId());// 合同号
			result.put("receive_code", head.getReceiveCode());// 客户编号
			result.put("receive_name", head.getReceiveName());// 客户名字
			result.put("class_type_id", head.getClassTypeId());// 班次id
			result.put("class_type_name", head.getClassTypeName());// 班次name
			result.put("syn_type", head.getSynType());// 同步类型
			result.put("remark", head.getRemark());// 备注
			result.put("status", head.getStatus());// 状态码
			result.put("status_name", head.getStatusName());// 状态中文
			result.put("create_user", head.getUserName());// 创建人
			result.put("create_time", head.getCreateTime());// 创建时间
			result.put("class_type_list", this.selectClassTypeList());
			result.put("delivery_vehicle_id", UtilObject.getIntOrZero(head.getDeliveryVehicleId()));
			result.put("delivery_driver_id", UtilObject.getIntOrZero(head.getDeliveryDriverId()));
			result.put("delivery_vehicle_name", head.getDeliveryVehicle());
			result.put("delivery_driver_name", head.getDeliveryDriver());
			if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()) {
				result.put("is_urgent", false);
			} else {
				result.put("is_urgent", true);
			}
			List<Map<String, Object>> itemVosMap = new ArrayList<Map<String, Object>>();
			List<BizStockOutputItemVo> itemVos = head.getItemList();
			result.put("mat_doc_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMatDocCode()));// sap凭证
			result.put("mes_doc_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMesDocCode()));// mes凭证
			if (itemVos != null && itemVos.size() > 0) {
				BizStockOutputItemVo item = itemVos.get(0);
				result.put("fty_id", UtilObject.getStringOrEmpty(item.getFtyId()));// 工厂id
				result.put("fty_code", UtilObject.getStringOrEmpty(item.getFtyCode()));// 工厂code
				result.put("fty_name", UtilObject.getStringOrEmpty(item.getFtyName()));// 工厂name
				for (BizStockOutputItemVo itemVo : itemVos) {
					Map<String, Object> detail = new HashMap<String, Object>();
					detail.put("item_id", UtilObject.getIntOrZero(itemVo.getItemId()));// 主键
					detail.put("fty_id", UtilObject.getStringOrEmpty(itemVo.getFtyId()));// 工厂id
					detail.put("mat_id", UtilObject.getIntOrZero(itemVo.getMatId()));// 物料id
					detail.put("mat_code", UtilObject.getStringOrEmpty(itemVo.getMatCode()));// 物料code
					detail.put("mat_name", UtilObject.getStringOrEmpty(itemVo.getMatName()));// 物料name
					detail.put("unit_id", UtilObject.getIntOrZero(itemVo.getUnitId()));// 单位id
					detail.put("unit_code", UtilObject.getStringOrEmpty(itemVo.getUnitCode()));// 单位code
					detail.put("unit_name", UtilObject.getStringOrEmpty(itemVo.getUnitName()));// 单位name
					detail.put("order_qty", UtilObject.getBigDecimalOrZero(itemVo.getStockQty()));// 订单数量
					detail.put("output_qty", UtilObject.getBigDecimalOrZero(itemVo.getOutputQty()));// 出库数量
					detail.put("send_qty", UtilObject.getBigDecimalOrZero(itemVo.getSendQty()));// 已下架数量
					detail.put("location_id", UtilObject.getIntOrZero(itemVo.getLocationId()));// 库存地点id
					detail.put("location_code", UtilObject.getStringOrEmpty(itemVo.getLocationCode()));// 库存地点code
					detail.put("location_name", UtilObject.getStringOrEmpty(itemVo.getLocationName()));// 库存地点name
					detail.put("write_off", UtilObject.getByteOrNull(itemVo.getWriteOff()));// 冲销码
					detail.put("write_off_name", UtilObject.getStringOrEmpty(itemVo.getWriteOffName()));// 冲销状态
					detail.put("stock_output_id", UtilObject.getIntOrZero(itemVo.getStockOutputId()));// 出库单
					detail.put("stock_output_rid", UtilObject.getIntOrZero(itemVo.getStockOutputRid()));// 行序号
					detail.put("refer_receipt_rid", UtilObject.getStringOrEmpty(itemVo.getReferReceiptRid()));// sap行序号
					detail.put("remark", UtilObject.getStringOrEmpty(itemVo.getRemark()));// 行备注
					detail.put("erp_batch", itemVo.getErpBatch());// erp批次
					detail.put("mat_doc_code", UtilObject.getStringOrEmpty(itemVo.getMatDocCode()));// sap凭证
					detail.put("mes_doc_code", UtilObject.getStringOrEmpty(itemVo.getMesDocCode()));// mes凭证
					detail.put("package_standard_ch", UtilObject.getStringOrEmpty(itemVo.getPackageStandardCh()));
					detail.put("package_standard", UtilObject.getBigDecimalOrZero(itemVo.getPackageStandard()));
					detail.put("package_type_id", UtilObject.getStringOrEmpty(itemVo.getPackageTypeId()));
					detail.put("package_type_code", UtilObject.getStringOrEmpty(itemVo.getPackageTypeCode()));
					if (itemVo.getPositionList() != null) {
						detail.put("position_list", itemVo.getPositionList());// 行项目详情集合
					}
					if (itemVo.getLocationList() != null) {
						detail.put("location_list", itemVo.getLocationList());// 库存地点集合
					}
					if (itemVo.getErpbatchList() != null) {
						detail.put("erpbatch_list", itemVo.getErpbatchList());// erp批次集合
					}
					if (itemVo.getTaskList() != null) {
						detail.put("task_list", itemVo.getTaskList());// 下架作业单集合
					}
					itemVosMap.add(detail);
				}
			}
			result.put("item_list", itemVosMap);
			// 紧急记账销售出库
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue()) {
			result.put("stock_output_id", head.getStockOutputId());// 出库单id
			result.put("stock_output_code", head.getStockOutputCode());// 出库单code
			result.put("refer_receipt_code", head.getReferReceiptCode());// 交货单号
			result.put("sale_order_code", head.getSaleOrderCode());// 销售单号
			result.put("receipt_type_name", head.getReceiptTypeName());// 销售单类型
			result.put("preorder_id", head.getPreorderId());// 合同号
			result.put("receive_code", head.getReceiveCode());// 客户编号
			result.put("receive_name", head.getReceiveName());// 客户名字
			result.put("class_type_id", head.getClassTypeId());// 班次id
			result.put("class_type_name", head.getClassTypeName());// 班次name
			result.put("syn_type", head.getSynType());// 同步类型
			result.put("remark", head.getRemark());// 备注
			result.put("status", head.getStatus());// 状态码
			result.put("status_name", head.getStatusName());// 状态中文
			result.put("create_user", head.getUserName());// 创建人
			result.put("create_time", head.getCreateTime());// 创建时间
			result.put("class_type_list", this.selectClassTypeList());
			List<BizStockOutputItemVo> itemVos = head.getItemList();
			result.put("mat_doc_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMatDocCode()));// sap凭证
			result.put("mes_doc_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMesDocCode()));// mes凭证
			result.put("mat_write_off_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMatWriteOffCode()));//sap冲销凭证
			result.put("mes_write_off_code", UtilObject.getStringOrEmpty(itemVos.get(0).getMesWriteOffCode()));//mes冲销凭证
			List<Map<String, Object>> itemVosMap = new ArrayList<Map<String, Object>>();
			result.put("stock_type_id", itemVos.get(0).getPositionList().get(0).getStockTypeId());
			if (itemVos != null && itemVos.size() > 0) {
				for (BizStockOutputItemVo itemVo : itemVos) {
					Map<String, Object> detail = new HashMap<String, Object>();
					detail.put("item_id", UtilObject.getIntOrZero(itemVo.getItemId()));// 主键
					detail.put("fty_id", UtilObject.getStringOrEmpty(itemVo.getFtyId()));// 工厂id
					detail.put("mat_id", UtilObject.getIntOrZero(itemVo.getMatId()));// 物料id
					detail.put("mat_code", UtilObject.getStringOrEmpty(itemVo.getMatCode()));// 物料code
					detail.put("mat_name", UtilObject.getStringOrEmpty(itemVo.getMatName()));// 物料name
					detail.put("unit_id", UtilObject.getIntOrZero(itemVo.getUnitId()));// 单位id
					detail.put("unit_code", UtilObject.getStringOrEmpty(itemVo.getUnitCode()));// 单位code
					detail.put("unit_name", UtilObject.getStringOrEmpty(itemVo.getUnitName()));// 单位name
					detail.put("order_qty", UtilObject.getBigDecimalOrZero(itemVo.getStockQty()));// 订单数量
					detail.put("output_qty", UtilObject.getBigDecimalOrZero(itemVo.getOutputQty()));// 已下架数量
					detail.put("location_id", UtilObject.getIntOrZero(itemVo.getLocationId()));// 库存地点id
					detail.put("location_code", UtilObject.getStringOrEmpty(itemVo.getLocationCode()));// 库存地点code
					detail.put("location_name", UtilObject.getStringOrEmpty(itemVo.getLocationName()));// 库存地点name
					detail.put("write_off", UtilObject.getByteOrNull(itemVo.getWriteOff()));// 冲销码
					detail.put("write_off_name", UtilObject.getStringOrEmpty(itemVo.getWriteOffName()));// 冲销状态
					detail.put("stock_output_id", UtilObject.getIntOrZero(itemVo.getStockOutputId()));// 出库单
					detail.put("stock_output_rid", UtilObject.getIntOrZero(itemVo.getStockOutputRid()));// 行序号
					detail.put("refer_receipt_rid", UtilObject.getStringOrEmpty(itemVo.getReferReceiptRid()));// sap行序号
					detail.put("remark", UtilObject.getStringOrEmpty(itemVo.getRemark()));// 行备注
					detail.put("erp_batch", itemVo.getErpBatch());// erp批次
					detail.put("mat_doc_code", UtilObject.getStringOrEmpty(itemVo.getMatDocCode()));// sap凭证
					detail.put("mes_doc_code", UtilObject.getStringOrEmpty(itemVo.getMesDocCode()));// mes凭证
					detail.put("mat_write_off_code", UtilObject.getStringOrEmpty(itemVo.getMatWriteOffCode()));//sap冲销凭证
					detail.put("mes_write_off_code", UtilObject.getStringOrEmpty(itemVo.getMesWriteOffCode()));//mes冲销凭证
					detail.put("package_standard_ch", UtilObject.getStringOrEmpty(itemVo.getPackageStandardCh()));
					detail.put("package_standard", UtilObject.getBigDecimalOrZero(itemVo.getPackageStandard()));
					detail.put("package_type_id", UtilObject.getStringOrEmpty(itemVo.getPackageTypeId()));
					detail.put("package_type_code", UtilObject.getStringOrEmpty(itemVo.getPackageTypeCode()));
					if (itemVo.getPositionList() != null) {
						detail.put("position_list", itemVo.getPositionList());// 行项目详情集合
					}
					if (itemVo.getLocationList() != null) {
						detail.put("location_list", itemVo.getLocationList());// 库存地点集合
					}
					if (itemVo.getErpbatchList() != null) {
						detail.put("erpbatch_list", itemVo.getErpbatchList());// erp批次集合
					}
					itemVosMap.add(detail);
				}
			}
			result.put("item_list", itemVosMap);
		}
		return result;
	}

	/**
	 * 删除出库单
	 * 
	 * @param stockOutputId
	 * @param userId
	 * @return
	 */
	@Override
	@Transactional
	public String deleteOutputOrder(int stockOutputId, String userId) throws Exception {
		BizStockOutputHeadVo outputHead = this.getOrderView(stockOutputId);
		if (outputHead == null) {
			throw new WMSException("无法找到出库单");
		}
		outputHead.setIsDelete(EnumBoolean.TRUE.getValue());
		outputHead.setModifyUser(userId);
		outPutHeadDao.updateByPrimaryKeySelective(outputHead);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stockOutputId);
		param.put("modifyUser", userId);
		outPutItemDao.deleteItemByOrderId(param);
		outPutPositionDao.deletePostionByOrderId(param);
		// 销售出库
		if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) {
			param = new HashMap<String, Object>();
			param.put("referReceiptCode", outputHead.getReferReceiptCode());
			List<Byte> list = new ArrayList<Byte>();
			list.add(EnumReceiptStatus.RECEIPT_WORK.getValue());
			param.put("status", list);
			List<Map<String, Object>> orders = this.allocateCargoHeadDao.selectOrderCodeAndId(param);
			for (Map<String, Object> map : orders) {
				map.put("status", EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
				// 下架作业单状态
				this.updateTaskStatus(map);
			}
			// 配货单 删除出库单信息
			param = new HashMap<String, Object>();
			param.put("beforeOrderId", outputHead.getStockOutputId());
			param.put("beforeOrderCode", outputHead.getStockOutputCode());
			param.put("beforeOrderType", outputHead.getReceiptType());
			allocateCargoHeadDao.deleteBeforeInfo(param);
			// 报废出库
		} else if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()) {
			this.taskService.deleteTaskReqAndTaskByReferIdAndType(outputHead.getStockOutputId(),
					outputHead.getReceiptType());
			// 解除冻结
			List<BizStockOutputItemVo> itemVos = outputHead.getItemList();
			for (BizStockOutputItemVo item : itemVos) {
				StockOccupancy stockOccupancy = new StockOccupancy();
				stockOccupancy.setBatch(item.getBatch());
				stockOccupancy.setFtyId(item.getFtyId());
				stockOccupancy.setLocationId(item.getLocationId());
				stockOccupancy.setMatId(item.getMatId());
				stockOccupancy.setPackageTypeId(item.getPackageTypeId());
				stockOccupancy.setQty(item.getStockQty());
				stockOccupancy.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
				this.commonService.modifyStockOccupancy(stockOccupancy, "H");
			}
			// 其他出库
		} else if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_OTHER.getValue()) {
			this.taskService.deleteTaskReqAndTaskByReferIdAndType(outputHead.getStockOutputId(),
					outputHead.getReceiptType());
			List<BizStockOutputItemVo> itemVos = outputHead.getItemList();
		    //Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
			//DicStockLocationVo locationObj = locationMap.get(itemVos.get(0).getLocationId());
			// 销售工厂
			//if (locationObj.getFtyType().equals(EnumFtyType.SALE.getValue())) {
				// 解除冻结
				for (BizStockOutputItemVo item : itemVos) {
					StockOccupancy stockOccupancy = new StockOccupancy();
					stockOccupancy.setBatch(item.getBatch());
					stockOccupancy.setFtyId(item.getFtyId());
					stockOccupancy.setLocationId(item.getLocationId());
					stockOccupancy.setMatId(item.getMatId());
					stockOccupancy.setPackageTypeId(item.getPackageTypeId());
					stockOccupancy.setQty(item.getStockQty());
					stockOccupancy.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
					this.commonService.modifyStockOccupancy(stockOccupancy, "H");
				}
			//}
			// 临时作业销售出库
		} else if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue()
				|| outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()) {
			this.taskService.deleteTaskReqAndTaskByReferIdAndType(outputHead.getStockOutputId(),
					outputHead.getReceiptType());
			List<BizStockOutputItemVo> itemVos = outputHead.getItemList();
			for (BizStockOutputItemVo item : itemVos) {
				List<BizStockOutputPositionVo> positionVos = item.getPositionList();
				for (BizStockOutputPositionVo position : positionVos) {
					// 解除冻结
					StockOccupancy stockOccupancy = new StockOccupancy();
					stockOccupancy.setBatch(position.getBatch());
					stockOccupancy.setFtyId(position.getFtyId());
					stockOccupancy.setLocationId(position.getLocationId());
					stockOccupancy.setMatId(position.getMatId());
					stockOccupancy.setPackageTypeId(position.getPackageTypeId());
					stockOccupancy.setQty(position.getStockQty());
					stockOccupancy.setStockTypeId(position.getStockTypeId());
					if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue()) {
						stockOccupancy.setIsUrgent(EnumBoolean.TRUE.getValue());
					} else {
						stockOccupancy.setIsUrgent(null);
					}
					this.commonService.modifyStockOccupancy(stockOccupancy, "H");
				}
			}
		}
		return "删除成功";
	}

	/**
	 * 保存出库表信息
	 * 
	 * @return
	 */
	@Override
	@Transactional
	public JSONObject saveOutputOrderData(JSONObject jsonData, CurrentUser user, boolean isPDA, byte orderType) throws Exception {
		boolean isAdd = false; // 新增or修改
		/******************************************************
		 * HEAD信息处理
		 ******************************************************/
		@SuppressWarnings("unchecked")
		Map<String, Object> headData = (Map<String, Object>) jsonData;

		BizStockOutputHead outputHead = null; // 领料出库单对象
		int stockOutputId = 0;

		if (headData.containsKey("stock_output_id")) {
			stockOutputId = UtilObject.getIntOrZero(headData.get("stock_output_id"));
		}
		// 根据出库单号是否存在 判断新增还是修改
		if (stockOutputId == 0) {
			isAdd = true;
		} else {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("stockOutputId", stockOutputId);

			outputHead = outPutHeadDao.selectByPrimaryKey(param);
			if (null == outputHead || "".equals(outputHead.getStockOutputCode()) || outputHead.getIsDelete() == 1) {
				isAdd = true;
			}
		}

		// 保存或修改出库单并返回出库单对象
		outputHead = this.saveHeadData(isAdd, headData, outputHead, user, orderType, isPDA);

		// 如果当前操作为修改,则删除经办人与出库单详细信息
		if (!isAdd) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("modifyUser", user.getUserId());
			param.put("stockOutputId", outputHead.getStockOutputId());
			outPutItemDao.deleteItemByOrderId(param);
			outPutPositionDao.deletePostionByOrderId(param);
		}

		/******************************************************
		 * ITEM信息处理
		 ******************************************************/
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> itemList = (List<Map<String, Object>>) headData.get("item_list"); // 出库单明细list
		if(itemList == null || itemList.isEmpty()) {
			throw new WMSException("缺少行项目");
		}
		for (int i = 0; i < itemList.size(); i++) {
			Map<String, Object> itemDataTemp = itemList.get(i);
			if (UtilObject.getBigDecimalOrZero(itemDataTemp.get("order_qty")).compareTo(BigDecimal.ZERO) == 1) {
				BizStockOutputItem item = this.saveItemData(itemDataTemp, outputHead, user.getUserId());

				/******************************************************
				 * position信息处理
				 ******************************************************/
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> positionList = (List<Map<String, Object>>) itemDataTemp.get("position_list"); // 出库单明细list
				if (positionList != null) {
					for (int j = 0; j < positionList.size(); j++) {
						Map<String, Object> positionDataTemp = positionList.get(j);
						if (UtilObject.getBigDecimalOrZero(positionDataTemp.get("output_qty"))
								.compareTo(BigDecimal.ZERO) == 1) {
							this.savePostionData(positionDataTemp, outputHead, item.getStockOutputRid(),
									user.getUserId());
						}
					}
				}
			}
		}

		JSONObject result = new JSONObject();
		result.put("flag", true);
		result.put("stock_output_id", outputHead.getStockOutputId());// 出库单id
		result.put("stock_output_code", outputHead.getStockOutputCode());// 出库单code
		result.put("refer_receipt_code", outputHead.getReferReceiptCode());// 前置单据交货单号
		result.put("receipt_type", outputHead.getReceiptType());// 出库单类型

		// 报废出库 紧急作业销售出库
		if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()
				|| outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue()
				|| outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()) {
			this.saveStockTaskReqDown(result, jsonData, user.getUserId());
		}
		// 销售出库
		if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) {
			// 配货单插入出库单号
			this.insertBeforInfo(outputHead);
			// 下架作业单改为已关联
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("referReceiptCode", outputHead.getReferReceiptCode());
			param.put("beforeOrderId", outputHead.getStockOutputId());
			List<Byte> list = new ArrayList<Byte>();
			list.add(EnumReceiptStatus.RECEIPT_WORK.getValue());
			param.put("status", list);
			List<Map<String, Object>> orders = allocateCargoHeadDao.selectOrderCodeAndId(param);
			for (Map<String, Object> map : orders) {
				map.put("status", EnumReceiptStatus.RECEIPT_ISUSED.getValue());
				// 下架作业单状态
				this.updateTaskStatus(map);
			}
		}
		// 其它出库 发起下架请求并完成下架作业单
		if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_OTHER.getValue()) {
			List<Integer> ids = this.saveStockTaskReqDown(result, jsonData, user.getUserId());
			if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_OTHER.getValue()) {
				Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
				DicStockLocationVo locationObj = locationMap
						.get(UtilObject.getIntegerOrNull(itemList.get(0).get("location_id")));
				// 生产工厂
				if (locationObj.getFtyType().equals(EnumFtyType.PRODUCT.getValue())) {
					this.addStockTaskByReq(ids);
					// 单据状态 已作业
					this.updateOutPutHead(outputHead.getStockOutputId(), user.getUserId(),
							EnumReceiptStatus.RECEIPT_WORK.getValue());
				}
			}
		}
		return result;
	}

	/**
	 * 保存出库主表对象
	 * 
	 * @param isAdd
	 * @param headData
	 * @param head
	 * @param user
	 * @param receiptType
	 * @param isPDA
	 * @return
	 */
	private BizStockOutputHead saveHeadData(boolean isAdd, Map<String, Object> headData, BizStockOutputHead head,
			CurrentUser user, byte receiptType, boolean isPDA) {
		if (isAdd) {
			head = new BizStockOutputHead();
			head.setStockOutputCode(String.valueOf(this.getNewOrderID()));// 新出库单号
			head.setReceiptType(receiptType); // 出库单类型

			// 报废出库 其它出库 紧急作业销售出库 默认状态为已提交
			if (receiptType == EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()
					|| receiptType == EnumReceiptType.STOCK_OUTPUT_OTHER.getValue()
					|| receiptType == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()
					|| receiptType == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue()) {
				head.setStatus(EnumReceiptStatus.RECEIPT_SUBMIT.getValue());
				// 直发出库 紧急记账销售出库 销售出库 默认状态未完成
			} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_STRAIGTH.getValue()
					|| head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue()
					|| head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) {
				head.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
			}

			head.setIsDelete(EnumBoolean.FALSE.getValue());// 是否删除
			head.setCheckAccount(EnumBoolean.FALSE.getValue());
			head.setCreateUser(user.getUserId());// 创建者id
		}

		head.setCorpId(user.getCorpId()); // 公司ID
		head.setReferReceiptCode(UtilObject.getStringOrEmpty(headData.get("refer_receipt_code"))); // 前置单据ID
		head.setPreorderId(UtilObject.getStringOrEmpty(headData.get("preorder_id"))); // 前置关联号
		head.setReceiveCode(UtilObject.getStringOrEmpty(headData.get("receive_code"))); // 接收人编码
		head.setReceiveName(UtilObject.getStringOrEmpty(headData.get("receive_name"))); // 接收人名称
		head.setOrderType(UtilObject.getStringOrEmpty(headData.get("order_type"))); // 订单类型
		head.setOrderTypeName(UtilObject.getStringOrEmpty(headData.get("order_type_name"))); // 订单类型名称
		head.setOrgCode(UtilObject.getStringOrEmpty(headData.get("org_code")));// 销售组织
		head.setOrgName(UtilObject.getStringOrEmpty(headData.get("org_name")));// 销售组织名称
		head.setGroupCode(UtilObject.getStringOrEmpty(headData.get("group_code")));// 销售组
		head.setRemark(UtilObject.getStringOrEmpty(headData.get("remark")));// 备注
		head.setClassTypeId(UtilObject.getIntOrZero(headData.get("class_type_id")));// 班次
		head.setSynType(UtilObject.getByteOrNull(headData.get("syn_type")));// 同步类型
		head.setSaleOrderCode(UtilObject.getStringOrEmpty(headData.get("sale_order_code")));// 销售订单号
		head.setRemark(UtilObject.getStringOrEmpty(headData.get("remark")));// 备注
		head.setModifyUser(user.getUserId()); // 最后修改人
		head.setDeliveryVehicleId(UtilObject.getIntOrZero(headData.get("delivery_vehicle_id")));// 车辆id
		head.setDeliveryVehicle(UtilObject.getStringOrEmpty(headData.get("delivery_vehicle_name")));// 车辆
		head.setDeliveryDriverId(UtilObject.getIntOrZero(headData.get("delivery_driver_id")));// 司机id
		head.setDeliveryDriver(UtilObject.getStringOrEmpty(headData.get("delivery_driver_name")));// 司机
		head.setMoveTypeId(UtilObject.getIntOrZero(headData.get("move_type_id")));//移动类型
		// pad or web
		if (isPDA) {
			head.setRequestSource(EnumRequestSource.PDA.getValue());
		} else {
			head.setRequestSource(EnumRequestSource.WEB.getValue());
		}

		this.saveOrderInfo(isAdd, head);

		return head;
	}

	/**
	 * 保存出库单详细信息
	 * 
	 * @param itemData
	 * @param head
	 * @param userId
	 * @return
	 */
	private BizStockOutputItem saveItemData(Map<String, Object> itemData, BizStockOutputHead head, String userId) {

		BizStockOutputItem item = new BizStockOutputItem();

		item.setStockOutputId(head.getStockOutputId());
		item.setStockOutputRid(UtilObject.getIntOrZero(itemData.get("stock_output_rid")));
		item.setFtyId(UtilObject.getIntOrZero(itemData.get("fty_id")));// 工厂
		item.setLocationId(UtilObject.getIntOrZero(itemData.get("location_id"))); // 库存地点
		item.setMatId(UtilObject.getIntOrZero(itemData.get("mat_id"))); // 物料编码
		item.setUnitId(UtilObject.getIntOrZero(itemData.get("unit_id"))); // 计量单位
		item.setDecimalPlace(UtilObject.getByteOrNull(itemData.get("decimal_place"))); // 浮点型
		item.setSendQty(UtilObject.getBigDecimalOrZero(itemData.get("send_qty"))); // 已发货数量
		item.setOutputQty(UtilObject.getBigDecimalOrZero(itemData.get("output_qty")));// 出库数量
		item.setReturnQty(UtilObject.getBigDecimalOrZero(itemData.get("qty")));// 库存数量
		item.setStockQty(UtilObject.getBigDecimalOrZero(itemData.get("order_qty"))); // 销售出库交货数量/报废出库数量/其它出库
		if(UtilObject.getBigDecimalOrZero(itemData.get("output_qty")).compareTo(BigDecimal.ZERO)==0) {
			item.setOutputQty(UtilObject.getBigDecimalOrZero(itemData.get("order_qty")));// 出库数量
		}
		item.setMoveTypeId(UtilObject.getIntOrZero(itemData.get("move_type_id")));// 移动类型
		item.setReferReceiptCode(head.getReferReceiptCode());// 领料单号/参考单据编号(表头销售订单编号)
		item.setReferReceiptRid(UtilObject.getStringOrEmpty(itemData.get("refer_receipt_rid"))); // 领料单行项目号/参考单据序号(销售订单项目)
		item.setReferPrice(UtilObject.getBigDecimalOrZero(itemData.get("refer_price")));// 参考价
		item.setRemark(UtilObject.getStringOrEmpty(itemData.get("remark")));// 备注
		item.setIsDelete(EnumBoolean.FALSE.getValue());
		item.setWriteOff(EnumBoolean.FALSE.getValue());
		item.setPackageTypeId(UtilObject.getIntOrZero(itemData.get("package_type_id")));// 包装类型
		Long batch = UtilObject.getLongOrNull(itemData.get("batch"));
		item.setBatch(batch == null ? 0L : batch);// 批次
		item.setErpBatch(UtilObject.getStringOrEmpty(itemData.get("erp_batch")));// erp批次
		item.setProductBatch(UtilObject.getStringOrEmpty(itemData.get("product_batch")));// 生产批次
		item.setQualityBatch(UtilObject.getStringOrEmpty(itemData.get("quality_batch")));// 质检批次
		item.setWhId(UtilObject.getIntOrZero(itemData.get("wh_id")));// 仓位id
		item.setCreateUser(userId);
		item.setModifyUser(userId);
		outPutItemDao.insertSelective(item);

		/**
		 * 进行冻结库存
		 */
		// 报废出库
		if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()) {
			StockOccupancy stockOccupancy = new StockOccupancy();
			stockOccupancy.setBatch(batch);
			stockOccupancy.setFtyId(item.getFtyId());
			stockOccupancy.setLocationId(item.getLocationId());
			stockOccupancy.setMatId(item.getMatId());
			stockOccupancy.setPackageTypeId(item.getPackageTypeId());
			stockOccupancy.setQty(item.getStockQty());
			stockOccupancy.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
			this.commonService.modifyStockOccupancy(stockOccupancy, "S");
		}
		// 其他出库
		if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_OTHER.getValue()) {

				StockOccupancy stockOccupancy = new StockOccupancy();
				stockOccupancy.setBatch(batch);
				stockOccupancy.setFtyId(item.getFtyId());
				stockOccupancy.setLocationId(item.getLocationId());
				stockOccupancy.setMatId(item.getMatId());
				stockOccupancy.setPackageTypeId(item.getPackageTypeId());
				stockOccupancy.setQty(item.getStockQty());
				stockOccupancy.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
				this.commonService.modifyStockOccupancy(stockOccupancy, "S");

		}

		// TODO 为了方便查询 插入position信息
		// 报废出库 其他出库
		if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()
				|| head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_OTHER.getValue()) {
			BizStockOutputPosition position = new BizStockOutputPosition();
			position.setStockOutputId(head.getStockOutputId());
			position.setStockOutputRid(UtilObject.getIntOrZero(itemData.get("stock_output_rid")));
			position.setStockOutputPositionId(1);

			position.setQty(BigDecimal.ZERO);// 当前库存数量
			position.setBatch(UtilObject.getLongOrNull(itemData.get("batch")));// 批次
			position.setStockQty(UtilObject.getBigDecimalOrZero(itemData.get("order_qty")));// 发出总数
			position.setWhId(UtilObject.getIntOrZero(itemData.get("wh_id")));
			position.setAreaId(UtilObject.getIntOrZero(itemData.get("area_id")));
			position.setPositionId(UtilObject.getIntOrZero(itemData.get("position_id")));
			position.setRemark(UtilObject.getStringOrEmpty(itemData.get("remark")));
			position.setFtyId(UtilObject.getIntOrZero(itemData.get("fty_id")));
			position.setMatId(UtilObject.getIntOrZero(itemData.get("mat_id")));
			position.setErpBatch(UtilObject.getStringOrEmpty(itemData.get("erp_batch")));// erp批次
			position.setPackageTypeId(UtilObject.getIntOrZero(itemData.get("package_type_id")));// 包装类型
			position.setProductBatch(UtilObject.getStringOrEmpty(itemData.get("product_batch")));// 生产批次
			position.setQualityBatch(UtilObject.getStringOrEmpty(itemData.get("quality_batch")));// 质检批次
			position.setStockTypeId(UtilObject.getByteOrNull(itemData.get("stock_type_id")));// 库存类型
			position.setInputDate(UtilObject.getStringOrEmpty(itemData.get("input_date")));// 入库时间
			position.setUnitId(UtilObject.getIntOrZero(itemData.get("unit_id")));
			position.setLocationId(UtilObject.getIntOrZero(itemData.get("location_id")));
			position.setIsDelete(EnumBoolean.FALSE.getValue());

			position.setStockId(UtilObject.getIntOrZero(itemData.get("stock_id")));// 仓位库存ID
			position.setStockSnId(UtilObject.getIntOrZero(itemData.get("stock_sn_id")));
			position.setSnId(UtilObject.getIntOrZero(itemData.get("sn_id")));
			position.setPalletId(UtilObject.getIntOrZero(itemData.get("pallet_id")));
			position.setPackageId(UtilObject.getIntOrZero(itemData.get("package_id")));
			position.setReturnQty(BigDecimal.ZERO);
			position.setSpecStock(UtilObject.getStringOrEmpty(itemData.get("spec_stock")));
			position.setSpecStockCode(UtilObject.getStringOrEmpty(itemData.get("spec_stock_code")));
			position.setSpecStockName(UtilObject.getStringOrEmpty(itemData.get("spec_stock_name")));

			position.setModifyTime(new Date());
			position.setModifyUser(userId);
			position.setCreateUser(userId);
			position.setCreateTime(new Date());
			outPutPositionDao.insertSelective(position);
		}
		return item;
	}

	/**
	 * 保存出库单行项目详情
	 * 
	 * @param postionData
	 * @param head
	 * @param userId
	 * @param rid
	 * @return
	 * @throws Exception
	 */
	private BizStockOutputPosition savePostionData(Map<String, Object> postionData, BizStockOutputHead head, int rid,
			String userId) throws Exception {

		BizStockOutputPosition position = new BizStockOutputPosition();

		position.setStockOutputId(head.getStockOutputId());
		position.setStockOutputRid(rid);
		position.setStockOutputPositionId(UtilObject.getIntOrZero(postionData.get("stock_output_position_id")));

		position.setQty(UtilObject.getBigDecimalOrZero(postionData.get("qty")));// 当前库存数量
		position.setBatch(UtilObject.getLongOrNull(postionData.get("batch")));// 批次
		position.setStockQty(UtilObject.getBigDecimalOrZero(postionData.get("output_qty")));// 发出总数
		position.setWhId(UtilObject.getIntOrZero(postionData.get("wh_id")));
		position.setAreaId(UtilObject.getIntOrZero(postionData.get("area_id")));
		position.setPositionId(UtilObject.getIntOrZero(postionData.get("position_id")));
		position.setRemark(UtilObject.getStringOrEmpty(postionData.get("remark")));
		position.setFtyId(UtilObject.getIntOrZero(postionData.get("fty_id")));
		position.setMatId(UtilObject.getIntOrZero(postionData.get("mat_id")));
		position.setErpBatch(UtilObject.getStringOrEmpty(postionData.get("erp_batch")));// erp批次
		position.setPackageTypeId(UtilObject.getIntOrZero(postionData.get("package_type_id")));// 包装类型
		position.setProductBatch(UtilObject.getStringOrEmpty(postionData.get("product_batch")));// 生产批次
		position.setQualityBatch(UtilObject.getStringOrEmpty(postionData.get("quality_batch")));// 质检批次
		position.setStockTypeId(UtilObject.getByteOrNull(postionData.get("stock_type_id")));// 库存类型
		position.setInputDate(UtilObject.getStringOrEmpty(postionData.get("input_date")));// 入库时间
		position.setUnitId(UtilObject.getIntOrZero(postionData.get("unit_id")));
		position.setLocationId(UtilObject.getIntOrZero(postionData.get("location_id")));
		position.setIsDelete(EnumBoolean.FALSE.getValue());

		position.setStockId(UtilObject.getIntOrZero(postionData.get("stock_id")));// 仓位库存ID
		position.setStockSnId(UtilObject.getIntOrZero(postionData.get("stock_sn_id")));
		position.setSnId(UtilObject.getIntOrZero(postionData.get("sn_id")));
		position.setPalletId(UtilObject.getIntOrZero(postionData.get("pallet_id")));
		position.setPackageId(UtilObject.getIntOrZero(postionData.get("package_id")));
		position.setReturnQty(BigDecimal.ZERO);
		position.setSpecStock(UtilObject.getStringOrEmpty(postionData.get("spec_stock")));
		position.setSpecStockCode(UtilObject.getStringOrEmpty(postionData.get("spec_stock_code")));
		position.setSpecStockName(UtilObject.getStringOrEmpty(postionData.get("spec_stock_name")));

		position.setModifyTime(new Date());
		position.setModifyUser(userId);
		position.setCreateUser(userId);
		position.setCreateTime(new Date());

		/**
		 * 进行冻结库存
		 */
		// 紧急作业销售出库(正常库存) 紧急记账销售出库
		if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()
				||head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue()) {
			StockOccupancy stockOccupancy = new StockOccupancy();
			stockOccupancy.setBatch(position.getBatch());
			stockOccupancy.setFtyId(position.getFtyId());
			stockOccupancy.setLocationId(position.getLocationId());
			stockOccupancy.setMatId(position.getMatId());
			stockOccupancy.setPackageTypeId(position.getPackageTypeId());
			stockOccupancy.setQty(position.getStockQty());
			stockOccupancy.setStockTypeId(position.getStockTypeId());
			this.commonService.modifyStockOccupancy(stockOccupancy, "S");
			// 紧急作业销售出库(紧急库存) 
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue()) {
			StockOccupancy stockOccupancy = new StockOccupancy();
			stockOccupancy.setBatch(position.getBatch());
			stockOccupancy.setFtyId(position.getFtyId());
			stockOccupancy.setLocationId(position.getLocationId());
			stockOccupancy.setMatId(position.getMatId());
			stockOccupancy.setPackageTypeId(position.getPackageTypeId());
			stockOccupancy.setQty(position.getStockQty());
			stockOccupancy.setStockTypeId(position.getStockTypeId());
			stockOccupancy.setIsUrgent(EnumBoolean.TRUE.getValue());
			this.commonService.modifyStockOccupancy(stockOccupancy, "S");
		}

		outPutPositionDao.insertSelective(position);
		return position;
	}

	/**
	 * 获取出库新主键
	 */
	private long getNewOrderID() {
		return sequenceDao.selectNextVal("stock_output");
	}

	/**
	 * 保存出库单数据
	 */
	private int saveOrderInfo(boolean isAdd, BizStockOutputHead head) {
		if (isAdd) {
			return outPutHeadDao.insertSelective(head);
		} else {
			return outPutHeadDao.updateByPrimaryKeySelective(head);
		}
	}

	/**
	 * 拼接作业请求的行项目
	 * 
	 * @param headMap
	 * @param map
	 * @param reqItem
	 * @throws Exception
	 */
	public BizStockTaskReqItem addStockTaskReqItemFromMapItem(Map<String, Object> headMap, Map<String, Object> map)
			throws Exception {
		BizStockTaskReqItem reqItem = new BizStockTaskReqItem();
		reqItem.setBatch(UtilObject.getLongOrNull(map.get("batch")));
		reqItem.setWhId(UtilObject.getIntOrZero(map.get("wh_id")));
		reqItem.setFtyId(UtilObject.getIntOrZero(map.get("fty_id")));
		reqItem.setLocationId(UtilObject.getIntOrZero(map.get("location_id")));
		reqItem.setMatId(UtilObject.getIntOrZero(map.get("mat_id")));
		reqItem.setMatDocId(UtilObject.getIntOrZero(map.get("mat_doc_id")));
		reqItem.setMatDocRid(UtilObject.getIntOrZero(map.get("mat_doc_rid")));
		reqItem.setUnitId(UtilObject.getIntOrZero(map.get("unit_id")));
		reqItem.setQty(UtilObject.getBigDecimalOrZero(map.get("order_qty")));
		if (UtilObject.getBigDecimalOrZero(map.get("output_qty")).compareTo(BigDecimal.ZERO) == 1) {
			reqItem.setQty(UtilObject.getBigDecimalOrZero(map.get("output_qty")));
		}
		reqItem.setStockTaskQty(BigDecimal.ZERO);
		reqItem.setSpecStock(UtilObject.getStringOrEmpty(map.get("spec_stock")));
		reqItem.setSpecStockCode(UtilObject.getStringOrEmpty(map.get("spec_stock_code")));
		reqItem.setSpecStockName(UtilObject.getStringOrEmpty(map.get("spec_stock_name")));
		reqItem.setPackageTypeId(UtilObject.getIntOrZero(map.get("package_type_id")));
		reqItem.setErpBatch(UtilObject.getStringOrEmpty(map.get("erp_batch")));
		reqItem.setProductionBatch(UtilObject.getStringOrEmpty(map.get("product_batch")));
		reqItem.setQualityBatch(UtilObject.getStringOrEmpty(map.get("quality_batch")));

		reqItem.setStockTaskReqId(UtilObject.getIntOrZero(headMap.get("stock_task_req_id")));
		reqItem.setStockTaskReqRid(UtilObject.getIntOrZero(headMap.get("stock_task_req_rid")));
		reqItem.setReferReceiptCode(UtilObject.getStringOrEmpty(headMap.get("refer_receipt_code")));
		reqItem.setReferReceiptId(UtilObject.getIntOrZero(headMap.get("refer_receipt_id")));
		reqItem.setReferReceiptType(UtilObject.getByteOrNull(headMap.get("refer_receipt_type")));
		reqItem.setReferReceiptRid(UtilObject.getIntOrZero(map.get("stock_output_rid")));
		return reqItem;
	}

	/**
	 * 保存下架作业请求
	 * 
	 * @param result
	 * @param jsonData
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> saveStockTaskReqDown(JSONObject result, JSONObject jsonData, String userId) throws Exception {
		List<Integer> ids = new ArrayList<Integer>();
		Map<String, Object> headData = (Map<String, Object>) jsonData;
		Byte receiptType = UtilObject.getByteOrNull(result.get("receipt_type"));
		// 出库单明细list
		List<Map<String, Object>> itemList = (List<Map<String, Object>>) headData.get("item_list");
		// 作业请求 库存地点 map
		Map<Integer, Object> locationIdMap = new HashMap<Integer, Object>();
		List<BizStockTaskReqItem> addItemList = new ArrayList<BizStockTaskReqItem>();
		for (Map<String, Object> map : itemList) {
			Integer locationId = UtilObject.getIntOrZero(map.get("location_id"));
			if (locationIdMap.containsKey(locationId)) {
				// 含有相同的库存地
				Map<String, Object> headInfo = new HashMap<String, Object>();
				headInfo = (Map<String, Object>) locationIdMap.get(locationId);
				int stockTaskReqRid = (int) headInfo.get("stock_task_req_rid");
			
				// 紧急作业销售出库
				if (receiptType == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue()
						|| receiptType == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()) {
					@SuppressWarnings("unused")
					List<Map<String, Object>> positionList = (List<Map<String, Object>>) map.get("position_list");
					for (Map<String, Object> postion : positionList) {
						stockTaskReqRid = stockTaskReqRid + 1;
						headInfo.put("stock_task_req_rid", stockTaskReqRid);
						postion.put("stock_output_rid", map.get("stock_output_rid"));
						BizStockTaskReqItem item = new BizStockTaskReqItem();
						item = this.addStockTaskReqItemFromMapItem(headInfo, postion);
						addItemList.add(item);
					}
				} else {
					stockTaskReqRid = stockTaskReqRid + 1;
					headInfo.put("stock_task_req_rid", stockTaskReqRid);
					BizStockTaskReqItem item = new BizStockTaskReqItem();
					item = this.addStockTaskReqItemFromMapItem(headInfo, map);
					addItemList.add(item);
				}
			} else {
				BizStockTaskReqHead reqhead = new BizStockTaskReqHead();
				String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());
				reqhead.setStockTaskReqCode(stockTaskReqCode);
				reqhead.setFtyId(UtilObject.getIntOrZero(map.get("fty_id")));
				Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
				DicStockLocationVo locationObj = locationMap.get(locationId);
				reqhead.setLocationId(locationId);
				reqhead.setWhId(locationObj.getWhId());
				reqhead.setMatDocId(UtilObject.getIntOrZero(map.get("mat_doc_id")));
				reqhead.setMatDocYear(UtilObject.getIntOrZero(map.get("mat_doc_year")));
				reqhead.setCreateUser(userId);
				reqhead.setShippingType(EnumTaskReqShippingType.STOCK_OUTPUT.getValue());
				reqhead.setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue());
				reqhead.setReferReceiptCode(UtilObject.getStringOrEmpty(result.get("stock_output_code")));
				reqhead.setReferReceiptId(UtilObject.getIntOrZero(result.get("stock_output_id")));
				reqhead.setReferReceiptType(UtilObject.getByteOrNull(result.get("receipt_type")));
				reqhead.setDeliveryDriverId(UtilObject.getIntOrZero(headData.get("delivery_driver_id")));
				reqhead.setDeliveryDriver(UtilObject.getStringOrEmpty(headData.get("delivery_driver_name")));
				reqhead.setDeliveryDriverId(UtilObject.getIntOrZero(headData.get("delivery_vehicle_id")));
				reqhead.setDeliveryVehicle(UtilObject.getStringOrEmpty(headData.get("delivery_vehicle_name")));
				StockTaskReqHeadDao.insertSelective(reqhead);
				int stockTaskReqRid = 1;
				int stockTaskReqId = reqhead.getStockTaskReqId();
				ids.add(stockTaskReqId);
				Map<String, Object> headInfo = new HashMap<String, Object>();
				headInfo.put("stock_task_req_id", stockTaskReqId);// 作业申请号
				headInfo.put("stock_task_req_rid", stockTaskReqRid);// 作业申请序号
				headInfo.put("refer_receipt_code", UtilObject.getStringOrEmpty(result.get("stock_output_code")));// 前置单据号
				headInfo.put("refer_receipt_id", UtilObject.getIntOrZero(result.get("stock_output_id")));// 前置单据id
				headInfo.put("refer_receipt_type", receiptType);// 前置单据类型
				locationIdMap.put(locationId, headInfo);
				// 紧急作业销售出库
				if (receiptType == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue()
						|| receiptType == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()) {
					@SuppressWarnings("unused")
					List<Map<String, Object>> positionList = (List<Map<String, Object>>) map.get("position_list");
					for (Map<String, Object> postion : positionList) {
						stockTaskReqRid = stockTaskReqRid+1;
						headInfo.put("stock_task_req_rid", stockTaskReqRid);
						postion.put("stock_output_rid", map.get("stock_output_rid"));
						BizStockTaskReqItem item = new BizStockTaskReqItem();
						item = this.addStockTaskReqItemFromMapItem(headInfo, postion);
						addItemList.add(item);
					}
				} else {
					BizStockTaskReqItem item = new BizStockTaskReqItem();
					item = this.addStockTaskReqItemFromMapItem(headInfo, map);
					addItemList.add(item);
				}
			}
		}
		StockTaskReqItemDao.insertReqItems(addItemList);
		return ids;
	}

	/**
	 * 拼接作业请求的行项目
	 * 
	 * @param headMap
	 * @param map
	 * @param reqItem
	 * @throws Exception
	 */
	public BizStockTaskReqItem addStockTaskReqItemFromMapItem(Map<String, Object> headMap,
			BizStockOutputPositionVo positionVo) throws Exception {
		BizStockTaskReqItem reqItem = new BizStockTaskReqItem();
		reqItem.setBatch(positionVo.getBatch());
		reqItem.setWhId(positionVo.getWhId());
		reqItem.setFtyId(positionVo.getFtyId());
		reqItem.setLocationId(positionVo.getLocationId());
		reqItem.setMatId(positionVo.getMatId());
		reqItem.setMatDocId(0);
		reqItem.setMatDocRid(0);
		reqItem.setUnitId(positionVo.getUnitId());
		reqItem.setQty(positionVo.getStockQty());
		reqItem.setStockTaskQty(BigDecimal.ZERO);
		reqItem.setSpecStock("");
		reqItem.setSpecStockCode("");
		reqItem.setSpecStockName("");
		reqItem.setPackageTypeId(positionVo.getPackageTypeId());
		reqItem.setErpBatch(positionVo.getErpBatch());
		reqItem.setProductionBatch(positionVo.getProductBatch());
		reqItem.setQualityBatch(positionVo.getQualityBatch());

		reqItem.setStockTaskReqId(UtilObject.getIntOrZero(headMap.get("stock_task_req_id")));
		reqItem.setStockTaskReqRid(UtilObject.getIntOrZero(headMap.get("stock_task_req_rid")));
		reqItem.setReferReceiptCode(UtilObject.getStringOrEmpty(headMap.get("refer_receipt_code")));
		reqItem.setReferReceiptId(UtilObject.getIntOrZero(headMap.get("refer_receipt_id")));
		reqItem.setReferReceiptType(UtilObject.getByteOrNull(headMap.get("refer_receipt_type")));
		reqItem.setReferReceiptRid(positionVo.getStockOutputRid());
		return reqItem;
	}

	/**
	 * 保存下架作业请求
	 * 
	 * @param result
	 * @param jsonData
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveStockTaskReqDown(BizStockOutputHeadVo head) throws Exception {
		List<BizStockOutputItemVo> itemVos = head.getItemList();
		// 作业请求 库存地点 map
		Map<Integer, Object> locationIdMap = new HashMap<Integer, Object>();
		List<BizStockTaskReqItem> addItemList = new ArrayList<BizStockTaskReqItem>();
		for (BizStockOutputItemVo itemvo : itemVos) {
			Integer locationId = itemvo.getLocationId();
			if (locationIdMap.containsKey(locationId)) {
				// 含有相同的库存地
				Map<String, Object> headInfo = new HashMap<String, Object>();
				headInfo = (Map<String, Object>) locationIdMap.get(locationId);
				int stockTaskReqRid = (int) headInfo.get("stock_task_req_rid");
				stockTaskReqRid = stockTaskReqRid + 1;
				headInfo.put("stock_task_req_rid", stockTaskReqRid);
				List<BizStockOutputPositionVo> positionVos = itemvo.getPositionList();
				for (BizStockOutputPositionVo postion : positionVos) {
					BizStockTaskReqItem item = new BizStockTaskReqItem();
					item = this.addStockTaskReqItemFromMapItem(headInfo, postion);
					addItemList.add(item);
				}
			} else {
				BizStockTaskReqHead reqhead = new BizStockTaskReqHead();
				String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());
				reqhead.setStockTaskReqCode(stockTaskReqCode);
				reqhead.setFtyId(itemvo.getFtyId());
				Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
				DicStockLocationVo locationObj = locationMap.get(locationId);
				reqhead.setLocationId(locationId);
				reqhead.setWhId(locationObj.getWhId());
				reqhead.setMatDocId(0);
				reqhead.setMatDocYear(0);
				reqhead.setCreateUser(head.getCreateUser());
				reqhead.setShippingType(EnumTaskReqShippingType.STOCK_OUTPUT.getValue());
				reqhead.setReceiptType(EnumReceiptType.STOCK_TASK_REMOVAL_REQ.getValue());
				reqhead.setReferReceiptCode(head.getStockOutputCode());
				reqhead.setReferReceiptId(head.getStockOutputId());
				reqhead.setReferReceiptType(head.getReceiptType());
				StockTaskReqHeadDao.insertSelective(reqhead);
				int stockTaskReqRid = 1;
				int stockTaskReqId = reqhead.getStockTaskReqId();
				Map<String, Object> headInfo = new HashMap<String, Object>();
				headInfo.put("stock_task_req_id", stockTaskReqId);// 作业申请号
				headInfo.put("stock_task_req_rid", stockTaskReqRid);// 作业申请序号
				headInfo.put("refer_receipt_code", head.getStockOutputCode());// 前置单据号
				headInfo.put("refer_receipt_id", head.getStockOutputId());// 前置单据id
				headInfo.put("refer_receipt_type", head.getReceiptType());// 前置单据类型
				locationIdMap.put(locationId, headInfo);
				List<BizStockOutputPositionVo> positionVos = itemvo.getPositionList();
				for (BizStockOutputPositionVo postion : positionVos) {
					BizStockTaskReqItem item = new BizStockTaskReqItem();
					item = this.addStockTaskReqItemFromMapItem(headInfo, postion);
					addItemList.add(item);
				}
			}
		}
		StockTaskReqItemDao.insertReqItems(addItemList);
	}

	/**
	 * 进行转储操作
	 * 
	 * @param sapRetrun
	 * @param userId
	 * @throws Exception
	 */
	public String saveToTranSport(Map<String, Object> sapRetrun, String userId) throws Exception {
		String msg = "转储成功";
		if ("S".equals(sapRetrun.get("type"))) {

			BizStockOutputHeadVo head = this.getOrderView(UtilObject.getIntOrZero(sapRetrun.get("stock_output_id")));

			// 出库单明细list
			List<BizStockOutputItemVo> itemList = head.getItemList();
			for (BizStockOutputItemVo item : itemList) {

				List<BizStockOutputPositionVo> positionList = item.getPositionList(); // 出库单行明细list

				for (BizStockOutputPositionVo position : positionList) {

					// 修改接收工厂批次库存
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("qty", position.getStockQty());// 需要下架的总数量
					param.put("batch", position.getBatch());// 批次
					param.put("locationId", item.getLocationId());// 库存id
					param.put("matId", item.getMatId());// 物料id
					param.put("createUserId", userId);
					param.put("debitCredit", EnumDebitCredit.DEBIT_S_ADD.getName());
					param.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
					param.put("stockTypeId", position.getStockTypeId());
					commonService.modifyStockBatch(param);

					// 验证批次信息
					BatchMaterial material = new BatchMaterial();
					material.setBatch(position.getBatch());
					material.setMatId(item.getMatId());
					material.setFtyId(position.getFtyId());
					BatchMaterial find = batchMaterialDao.selectByUniqueKey(material);
					if (find == null) {
						throw new WMSException("无生产批次信息");
					}
					// 添加批次信息
					BatchMaterial batchMaterial = new BatchMaterial();
					batchMaterial.setBatch(position.getBatch());
					batchMaterial.setMatId(item.getMatId());
					batchMaterial.setFtyId(item.getFtyId());
					batchMaterial.setCreateUser(userId);
					batchMaterial.setPackageTypeId(position.getPackageTypeId());
					batchMaterial.setErpBatch(position.getErpBatch());
					batchMaterial.setQualityBatch(position.getQualityBatch());
					batchMaterial.setProductionBatch(position.getProductBatch());
					batchMaterial.setPurchaseOrderCode(find.getPurchaseOrderCode());
					batchMaterial.setProductLineId(find.getProductLineId());
					batchMaterial.setInstallationId(find.getInstallationId());
					batchMaterial.setClassTypeId(head.getClassTypeId());
					batchMaterialDao.insertSelective(batchMaterial);

					// 添加历史记录
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("debit_credit", EnumDebitCredit.DEBIT_S_ADD.getName());
					map.put("business_type", (byte) 1);
					map.put("qty", position.getStockQty());
					map.put("batch", position.getBatch());
					map.put("location_id", item.getLocationId());
					map.put("mat_id", item.getMatId());
					map.put("create_user", userId);
					map.put("modify_user", userId);
					map.put("refer_receipt_id", head.getStockOutputId());
					map.put("refer_receipt_code", head.getStockOutputCode());
					map.put("refer_receipt_rid", item.getStockOutputRid());
					map.put("refer_receipt_type", head.getReceiptType());
					map.put("erp_batch", position.getErpBatch());
					map.put("package_type_id", position.getPackageTypeId());
					this.insertBusinessHistory(map);

					Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
					DicStockLocationVo locationObj = locationMap.get(item.getLocationId());
					Integer whId = locationObj.getWhId();
					String whCode = dictionaryService.getWhCodeByWhId(whId);
					String areaCode = UtilProperties.getInstance().getDefaultCCQ();
					Integer areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);
					String positionCode = UtilProperties.getInstance().getDefaultCW();
					Integer positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode, areaCode,
							positionCode);
					// 修改接收工厂仓位库存
					StockPosition stockPosition = new StockPosition();
					stockPosition.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
					stockPosition.setWhId(whId);
					stockPosition.setAreaId(areaId);
					stockPosition.setPositionId(positionId);
					stockPosition.setBatch(position.getBatch());
					stockPosition.setMatId(item.getMatId());
					stockPosition.setFtyId(item.getFtyId());
					stockPosition.setLocationId(item.getLocationId());
					stockPosition.setInputDate(new Date());
					stockPosition.setPackageTypeId(position.getPackageTypeId());
					stockPosition.setQty(position.getStockQty());
					stockPosition.setUnitId(position.getUnitId());
					stockPosition.setPackageId(position.getPackageId());
					stockPosition.setPalletId(position.getPalletId());
					stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
					stockPosition.setStockTypeId(position.getStockTypeId());
					commonService.modifyStockPosition(stockPosition);

					// 修改发出工厂批次库存
					param = new HashMap<String, Object>();
					param.put("qty", position.getStockQty());// 需要下架的总数量
					param.put("batch", position.getBatch());// 批次
					param.put("locationId", position.getLocationId());// 库存id
					param.put("matId", position.getMatId());// 物料id
					param.put("createUserId", userId);
					param.put("debitCredit", EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
					param.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
					param.put("stockTypeId", position.getStockTypeId());
					commonService.modifyStockBatch(param);

					map = new HashMap<String, Object>();
					map.put("debit_credit", EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
					map.put("business_type", (byte) 2);
					map.put("qty", position.getStockQty());
					map.put("batch", position.getBatch());
					map.put("location_id", item.getLocationId());
					map.put("mat_id", item.getMatId());
					map.put("create_user", userId);
					map.put("modify_user", userId);
					map.put("refer_receipt_id", head.getStockOutputId());
					map.put("refer_receipt_code", head.getStockOutputCode());
					map.put("refer_receipt_rid", item.getStockOutputRid());
					map.put("refer_receipt_type", head.getReceiptType());
					map.put("erp_batch", position.getErpBatch());
					map.put("package_type_id", position.getPackageTypeId());
					this.insertBusinessHistory(map);
					// 修改发出工厂仓位库存
					stockPosition = new StockPosition();
					stockPosition.setWhId(position.getWhId());
					stockPosition.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
					stockPosition.setAreaId(position.getAreaId());
					stockPosition.setPositionId(position.getPositionId());
					stockPosition.setMatId(position.getMatId());
					stockPosition.setFtyId(position.getFtyId());
					stockPosition.setLocationId(position.getLocationId());
					stockPosition.setBatch(position.getBatch());
					stockPosition.setInputDate(new Date());
					stockPosition.setQty(position.getStockQty());
					stockPosition.setUnitId(position.getUnitId());
					stockPosition.setPackageId(position.getPackageId());
					stockPosition.setPalletId(position.getPalletId());
					stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
					stockPosition.setStockTypeId(position.getStockTypeId());
					commonService.modifyStockPosition(stockPosition);
				}
			}
			// 修改出库单状态为已转储
			this.updateOutPutHead(head.getStockOutputId(), userId, EnumReceiptStatus.RECEIPT_TRANSPORT.getValue());
			if (head.getSynType() == EnumSynType.MES_SAP_SYN.getValue()) {
				WmIopRetVal val = this.saveMesTranSport(head, "301");
				sapRetrun.put("mes_doc_code", UtilObject.getStringOrEmpty(val.getValue()));
			}
			// 保存物料转储过账凭证
			sapRetrun.put("stock_output_id", head.getStockOutputId());
			this.outPutPositionDao.saveMatDocInfo(sapRetrun);
			// 保存过账日期 凭证日期
			this.outPutHeadDao.insertPostingDateAndDocTime(sapRetrun);
		} else {
			msg = UtilObject.getStringOrEmpty(sapRetrun.get("message"));
			throw new WMSException(msg);
		}
		return msg;
	}

	/**
	 * 保存上架作业请求格式化
	 * 
	 * @param orderId
	 * @param orderCode
	 * @param orderType
	 * @param userId
	 * @throws Exception
	 */
	public void saveStockTaskReqUpFomat(Integer orderId, String orderCode, Byte orderType, String userId)
			throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("referReceiptId", orderId);
		param.put("referReceiptCode", orderCode);
		param.put("referReceiptType", orderType);
		this.saveStockTaskReqUp(param, userId);
	}

	/**
	 * 保存上架作业请求
	 * 
	 * @param param
	 * @param userId
	 * @throws Exception
	 */
	public void saveStockTaskReqUp(Map<String, Object> param, String userId) throws Exception {
		List<BizStockTaskReqHead> taskReqs = StockTaskReqHeadDao.getBizStockTaskReqHeadListByReferReipt(param);

		for (BizStockTaskReqHead reqHead : taskReqs) {
			Integer oldReqId = reqHead.getStockTaskReqId();
			List<BizStockTaskReqItem> items = StockTaskReqItemDao.selectItemById(oldReqId);
			reqHead.setShippingType(EnumTaskReqShippingType.STOCK_INPUT.getValue());
			reqHead.setCreateTime(new Date());
			reqHead.setModifyTime(new Date());
			reqHead.setCreateUser(userId);
			reqHead.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
			reqHead.setReceiptType(EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue());
			String stockTaskReqCode = commonService.getNextReceiptCode(EnumSequence.STOCK_TASK_REQ.getValue());
			reqHead.setStockTaskReqCode(stockTaskReqCode);
			reqHead.setStockTaskReqId(null);
			StockTaskReqHeadDao.insertSelective(reqHead);
			for (BizStockTaskReqItem item : items) {
				item.setStockTaskReqId(reqHead.getStockTaskReqId());
				item.setItemId(null);
				item.setStockTaskQty(BigDecimal.ZERO);
				item.setCreateTime(new Date());
				item.setModifyTime(new Date());

				BizStockTaskPositionCw taskPositionCw = new BizStockTaskPositionCw();
				taskPositionCw.setReferReceiptId(UtilObject.getIntOrZero(param.get("referReceiptId")));
				taskPositionCw.setReferReceiptCode(UtilObject.getStringOrEmpty(param.get("referReceiptCode")));
				taskPositionCw.setReferReceiptType(UtilObject.getByteOrNull(param.get("referReceiptType")));
				taskPositionCw.setStockTaskReqRid(item.getStockTaskReqRid());
				List<BizStockTaskPositionCw> positions = StockTaskPositionDao
						.selectByReferReceiptIdAndType(taskPositionCw);

				for (BizStockTaskPositionCw position : positions) {
					int i = 1;
					if (position.getPackageId() != null && position.getPackageId() > 0) {
						BizStockTaskReqPosition reqPosition = new BizStockTaskReqPosition();
						reqPosition.setStockTaskReqId(reqHead.getStockTaskReqId());
						reqPosition.setStockTaskReqRid(item.getStockTaskReqRid());
						reqPosition.setStockTaskReqPositionId(i);
						i++;
						reqPosition.setBatch(item.getBatch());
						reqPosition.setPalletId(position.getPalletId());
						reqPosition.setPackageId(position.getPackageId());
						reqPosition.setQty(position.getQty());
						StockTaskReqPositionDao.insertSelective(reqPosition);
					}
				}
			}
			StockTaskReqItemDao.insertReqItems(items);
		}
	}

	/**
	 * 销售出库冲销
	 * 
	 * @param head
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> writeOffSale(BizStockOutputHeadVo head) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		DTCWWMSDOREVERSEREQ mtCWWMSDOREVERSERE = new DTCWWMSDOREVERSEREQ();

		/***************************************************************
		 * IS_MSG_HEAD
		 **************************************************************/
		MSGHEAD iSMSGHEAD = new MSGHEAD();
		iSMSGHEAD.setSYSTEMID("WMS");
		String date = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		iSMSGHEAD.setGUID(head.getStockOutputCode() + date);
		mtCWWMSDOREVERSERE.setISMSGHEAD(iSMSGHEAD);

		/***************************************************************
		 * IT_DATA
		 **************************************************************/
		List<ITDATA> IT_DATA = new ArrayList<ITDATA>();
		ITDATA iTDATA = new ITDATA();
		iTDATA.setVBELN(head.getReferReceiptCode());
		iTDATA.setIBUDAT(head.getPostingDate());
		IT_DATA.add(iTDATA);
		mtCWWMSDOREVERSERE.setItdata(IT_DATA);
		ErpReturn erpReturn = CwErpWsImpl.outputWriteOff(mtCWWMSDOREVERSERE);
		result.put("mat_write_off_code", erpReturn.getMatDocCode());
		result.put("mat_doc_year", erpReturn.getDocyear());
		result.put("message", erpReturn.getMessage());
		result.put("type", erpReturn.getType());
		return result;
	}

	/**
	 * 报废出库冲销
	 * 
	 * @param head
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> writeOffScrap(BizStockOutputHeadVo head) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		DTCWWMSCANCELMATDOCREQ dtcwwmscancelmatdocreq = new DTCWWMSCANCELMATDOCREQ();
		/***************************************************************
		 * IS_MSG_HEAD
		 **************************************************************/
		com.inossem.wms.wsdl.sap.inputc.MSGHEAD iSMSGHEAD = new com.inossem.wms.wsdl.sap.inputc.MSGHEAD();
		iSMSGHEAD.setSYSTEMID("WMS");
		String date = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		iSMSGHEAD.setGUID(date);
		dtcwwmscancelmatdocreq.setISMSGHEAD(iSMSGHEAD);
		/***************************************************************
		 * IT_MATDOC
		 **************************************************************/
		List<BizStockOutputItemVo> itemVoList = head.getItemList();
		List<ITMATDOC> IT_MATDOC = new ArrayList<ITMATDOC>();
		for (BizStockOutputItemVo itemVo : itemVoList) {
			ITMATDOC ITMATDOC = new ITMATDOC();
			ITMATDOC.setMATERIALDOCUMENT(itemVo.getMatDocCode());
			ITMATDOC.setMATDOCUMENTYEAR(itemVo.getMatDocYear().toString());
			ITMATDOC.setPSTNGDATE(head.getPostingDate());
			String rid = itemVo.getStockOutputRid().toString();
			while (rid.length() < 3) { // 不够三位数字，自动补0
				rid = "0" + rid;
			}
			ITMATDOC.setMATDOCITEM(rid);
			IT_MATDOC.add(ITMATDOC);
		}
		dtcwwmscancelmatdocreq.setItmatdoc(IT_MATDOC);
		ErpReturn erpReturn = CwErpWsImpl.inputWriteOff(dtcwwmscancelmatdocreq);
		result.put("mat_write_off_code", erpReturn.getMatDocCode());
		result.put("mat_doc_year", erpReturn.getDocyear());
		result.put("message", erpReturn.getMessage());
		result.put("type", erpReturn.getType());
		return result;
	}

	/**
	 * 获取班次集合
	 */
	@Override
	public Map<String, Object> selectClassTypeList() throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("class_type_list", this.commonService.getClassTypeList());
		result.put("class_type_id", this.commonService.selectNowClassType());
		return result;
	}

	/**
	 * 查询出库物料集合
	 * 
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> selectMatListPastion(Map<String, Object> param) throws Exception {
		return commonService.getMatListByPosition(param);
	}

	/**
	 * 修改仓位库存
	 * 
	 * @param referReceiptId
	 * @param referReceiptCode
	 * @param referReceiptType
	 * @throws Exception
	 */
	private void updateStockPosition(Integer referReceiptId, String referReceiptCode, Byte referReceiptType)
			throws Exception {
		BizStockTaskItemCw item = new BizStockTaskItemCw();
		item.setReferReceiptId(referReceiptId);
		item.setReferReceiptType(referReceiptType);
		item.setReferReceiptCode(referReceiptCode);
		BizStockTaskPositionCw positon = new BizStockTaskPositionCw();
		positon.setReferReceiptId(referReceiptId);
		positon.setReferReceiptType(referReceiptType);
		positon.setReferReceiptCode(referReceiptCode);
		List<BizStockTaskItemCw> itemcw = this.bizStockTaskItemCwDao.selectByReferReceiptIdAndType(item);
		List<BizStockTaskPositionCw> positoncw = this.bizStockTaskPositionCwDao.selectByReferReceiptIdAndType(positon);
		Boolean isUrgent = false;
		Byte stockTypeId = EnumStockType.STOCK_TYPE_ERP.getValue();
		// 紧急作业销售出库(紧急库存)
		if (referReceiptType == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()) {
			isUrgent = true;
		}
		this.commonService.updateStockPositionByTask(itemcw, positoncw, stockTypeId, isUrgent);
	}

	/**
	 * 修改紧急作业出库 库存
	 * 
	 * @param referReceiptId
	 * @param referReceiptCode
	 * @param referReceiptType
	 * @throws Exception
	 */
	private void updateStockPositionOnUrgent(Integer referReceiptId, String referReceiptCode, Byte referReceiptType,
			String createUserId) throws Exception {
		BizStockTaskItemCw item = new BizStockTaskItemCw();
		item.setReferReceiptId(referReceiptId);
		item.setReferReceiptType(referReceiptType);
		item.setReferReceiptCode(referReceiptCode);
		BizStockTaskPositionCw positon = new BizStockTaskPositionCw();
		positon.setReferReceiptId(referReceiptId);
		positon.setReferReceiptType(referReceiptType);
		positon.setReferReceiptCode(referReceiptCode);
		List<BizStockTaskItemCw> itemcw = this.bizStockTaskItemCwDao.selectByReferReceiptIdAndType(item);
		List<BizStockTaskPositionCw> positoncw = this.bizStockTaskPositionCwDao.selectByReferReceiptIdAndType(positon);
		Boolean isUrgent = true;
		Byte stockTypeId = EnumStockType.STOCK_TYPE_ERP.getValue();
		// 仓位库存
		this.commonService.updateStockPositionByTask(itemcw, positoncw, stockTypeId, isUrgent);
		// 批次库存
		for (BizStockTaskItemCw taskitem : itemcw) {
			// 修改紧急批次库存
			Map<String, Object> param1 = new HashMap<String, Object>();
			param1.put("qty", taskitem.getQty());// 需要下架的总数量
			param1.put("batch", taskitem.getBatch());// 批次
			param1.put("locationId", taskitem.getLocationId());// 库存id
			param1.put("matId", taskitem.getMatId());// 物料id
			param1.put("createUserId", createUserId);
			param1.put("debitCredit", EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			param1.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
			param1.put("stockTypeId", stockTypeId);
			param1.put("isUrgent", EnumIsUrgent.URGENT.getValue());
			commonService.modifyStockBatch(param1);
		}
	}

	/**
	 * 修改仓位库存与批次库存
	 * 
	 * @param itemList
	 * @param userId
	 * @param debitCredit
	 * @param isUrgent
	 * @throws Exception
	 */
	private void updateStock(BizStockOutputHeadVo head, String userId, String debitCredit, Byte isUrgent)
			throws Exception {
		for (BizStockOutputItemVo item : head.getItemList()) {
			
			List<BizStockOutputPositionVo> positionList = item.getPositionList(); // 出库单行明细list
			for (BizStockOutputPositionVo position : positionList) {

				// 修改批次库存
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("qty", position.getStockQty());// 需要下架的总数量
				param.put("batch", position.getBatch());// 批次
				param.put("locationId", item.getLocationId());// 库存id
				param.put("matId", item.getMatId());// 物料id
				param.put("createUserId", userId);
				param.put("debitCredit", debitCredit);
				param.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				param.put("stockTypeId", position.getStockTypeId());
				if (isUrgent == EnumBoolean.TRUE.getValue()) {
					param.put("isUrgent", isUrgent);
					param.put("debitCredit", EnumDebitCredit.DEBIT_S_ADD.getName());
					commonService.modifyStockBatchOnSyn(param);
				}

				param.remove("isUrgent");
				param.put("debitCredit", debitCredit);
				commonService.modifyStockBatch(param);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("debit_credit", debitCredit);
				if ("H".equalsIgnoreCase(debitCredit)) {
					map.put("business_type", (byte) 2);
				} else {
					map.put("business_type", (byte) 1);
				}
				map.put("qty", item.getStockQty());
				map.put("batch", position.getBatch());
				map.put("location_id", item.getLocationId());
				map.put("mat_id", item.getMatId());
				map.put("create_user", userId);
				map.put("modify_user", userId);
				map.put("refer_receipt_id", head.getStockOutputId());
				map.put("refer_receipt_code", head.getStockOutputCode());
				map.put("refer_receipt_rid", item.getStockOutputRid());
				map.put("refer_receipt_type", head.getReceiptType());
				map.put("erp_batch", position.getErpBatch());
				map.put("package_type_id", position.getPackageTypeId());
				this.insertBusinessHistory(map);

				// 修改仓位库存
				Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
				DicStockLocationVo locationObj = locationMap.get(item.getLocationId());
				Integer whId = locationObj.getWhId();
				String whCode = dictionaryService.getWhCodeByWhId(whId);
				String areaCode = UtilProperties.getInstance().getDefaultCCQ();
				String positionCode = UtilProperties.getInstance().getDefaultCW();
				
			
				Integer areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);

				// TODO 没有仓库号的库存地点
				Integer positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode, areaCode,
						positionCode);
				StockPosition stockPosition = new StockPosition();
				stockPosition.setDebitOrCredit(debitCredit);
				stockPosition.setWhId(whId);
				stockPosition.setAreaId(areaId);
				stockPosition.setPositionId(positionId);
				stockPosition.setBatch(position.getBatch());
				stockPosition.setMatId(item.getMatId());
				stockPosition.setFtyId(item.getFtyId());
				stockPosition.setLocationId(item.getLocationId());
				stockPosition.setInputDate(new Date());
				stockPosition.setPackageTypeId(position.getPackageTypeId());
				stockPosition.setQty(position.getStockQty());
				stockPosition.setUnitId(position.getUnitId());
				stockPosition.setPackageId(position.getPackageId());
				stockPosition.setPalletId(position.getPalletId());
				stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				
				stockPosition.setStockTypeId(position.getStockTypeId());
				if (isUrgent == EnumBoolean.TRUE.getValue()) {
					stockPosition.setIsUrgent(isUrgent);
					stockPosition.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
					commonService.modifyStockPositionOnSyn(stockPosition);
				}
				stockPosition.setUrgentSynStatus(null);
				stockPosition.setId(null);
				stockPosition.setIsUrgent(null);
				stockPosition.setQty(position.getStockQty());
				stockPosition.setDebitOrCredit(debitCredit);
				commonService.modifyStockPosition(stockPosition);
			}
		}
	}

	/**
	 * 临时作业 临时库存修改
	 * @param head
	 * @param userId
	 * @param debitCredit
	 * @throws Exception
	 */
	private void updateStockOnUrgent(BizStockOutputHeadVo head, String userId, String debitCredit)throws Exception {
		for (BizStockOutputItemVo item : head.getItemList()) {
			List<BizStockOutputPositionVo> positionList = item.getPositionList(); // 出库单行明细list
			for (BizStockOutputPositionVo position : positionList) {
				// 修改批次库存
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("qty", position.getStockQty());// 需要下架的总数量
				param.put("batch", position.getBatch());// 批次
				param.put("locationId", item.getLocationId());// 库存id
				param.put("matId", item.getMatId());// 物料id
				param.put("createUserId", userId);
				param.put("debitCredit", debitCredit);
				param.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				param.put("stockTypeId", position.getStockTypeId());
				param.put("isUrgent", (byte) 1);
				param.put("debitCredit", EnumDebitCredit.DEBIT_S_ADD.getName());
				commonService.modifyStockBatchOnSyn(param);

				param.remove("isUrgent");
				param.put("debitCredit", debitCredit);
				commonService.modifyStockBatch(param);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("debit_credit", debitCredit);
				if ("H".equalsIgnoreCase(debitCredit)) {
					map.put("business_type", (byte) 2);
				} else {
					map.put("business_type", (byte) 1);
				}
				map.put("qty", item.getStockQty());
				map.put("batch", position.getBatch());
				map.put("location_id", item.getLocationId());
				map.put("mat_id", item.getMatId());
				map.put("create_user", userId);
				map.put("modify_user", userId);
				map.put("refer_receipt_id", head.getStockOutputId());
				map.put("refer_receipt_code", head.getStockOutputCode());
				map.put("refer_receipt_rid", item.getStockOutputRid());
				map.put("refer_receipt_type", head.getReceiptType());
				map.put("erp_batch", position.getErpBatch());
				map.put("package_type_id", position.getPackageTypeId());
				this.insertBusinessHistory(map);
			}
		}
		BizStockTaskItemCw item = new BizStockTaskItemCw();
		item.setReferReceiptId(head.getStockOutputId());
		item.setReferReceiptType(head.getReceiptType());
		item.setReferReceiptCode(head.getStockOutputCode());
		BizStockTaskPositionCw positon = new BizStockTaskPositionCw();
		positon.setReferReceiptId(head.getStockOutputId());
		positon.setReferReceiptType(head.getReceiptType());
		positon.setReferReceiptCode(head.getStockOutputCode());
		List<BizStockTaskItemCw> itemcw = this.bizStockTaskItemCwDao.selectByReferReceiptIdAndType(item);
		List<BizStockTaskPositionCw> positoncw = this.bizStockTaskPositionCwDao.selectByReferReceiptIdAndType(positon);
		commonService.synStockPositionOnUrgent(itemcw,positoncw,(byte)1);
	}
	
	/**
	 * 修改批次库存
	 * 
	 * @param head
	 * @param createUserId
	 * @param debitCredit
	 *            (H减 S加)
	 * @throws Exception
	 */
	private void updateStockBatch(BizStockOutputHeadVo head, String createUserId, String debitCredit) throws Exception {
		if (head != null) {
			// 销售出库
			if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("referReceiptCode", head.getReferReceiptCode());
				param.put("beforeOrderId", head.getStockOutputId());
				param.put("beforeOrderType", head.getReceiptType());
				// 下架作业单集合
				List<Map<String, Object>> taskList = bizStockTaskHeadCwDao.selectTaskListBySaleNoGroup(param);
				for (Map<String, Object> map : taskList) {
					param = new HashMap<String, Object>();
					param.put("qty", map.get("qty"));// 下架数
					param.put("batch", map.get("batch"));// 批次
					param.put("locationId", map.get("location_id"));// 库存id
					param.put("matId", map.get("mat_id"));// 物料id
					param.put("createUserId", createUserId);
					param.put("debitCredit", debitCredit);
					param.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
					param.put("stockTypeId", EnumStockType.STOCK_TYPE_ERP.getValue());
					// 修改批次库存
					commonService.modifyStockBatch(param);
					map.put("debit_credit", debitCredit);
					if ("H".equalsIgnoreCase(debitCredit)) {
						map.put("business_type", (byte) 2);
					} else {
						map.put("business_type", (byte) 1);
					}
					map.put("create_user", createUserId);
					map.put("modify_user", createUserId);
					this.insertBusinessHistory(map);
				}
				// 报废出库 其它出库
			} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_OTHER.getValue()
					|| head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()) {
				List<BizStockOutputItemVo> itemVos = head.getItemList();
				if (itemVos != null && itemVos.size() > 0) {
					for (int i = 0; i < itemVos.size(); i++) {
						BizStockOutputItemVo itemVo = itemVos.get(i);
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("qty", itemVo.getStockQty());// 需要下架的总数量
						param.put("batch", itemVo.getBatch());// 批次
						param.put("locationId", itemVo.getLocationId());// 库存id
						param.put("matId", itemVo.getMatId());// 物料id
						param.put("createUserId", createUserId);
						param.put("debitCredit", debitCredit);
						param.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
						param.put("stockTypeId", EnumStockType.STOCK_TYPE_ERP.getValue());
						// 修改批次库存
						commonService.modifyStockBatch(param);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("debit_credit", debitCredit);
						if ("H".equalsIgnoreCase(debitCredit)) {
							map.put("business_type", (byte) 2);
						} else {
							map.put("business_type", (byte) 1);
						}
						map.put("qty", itemVo.getStockQty());
						map.put("batch", itemVo.getBatch());
						map.put("location_id", itemVo.getLocationId());
						map.put("mat_id", itemVo.getMatId());
						map.put("create_user", createUserId);
						map.put("modify_user", createUserId);
						map.put("refer_receipt_id", head.getStockOutputId());
						map.put("refer_receipt_code", head.getStockOutputCode());
						map.put("refer_receipt_rid", itemVo.getStockOutputRid());
						map.put("refer_receipt_type", head.getReceiptType());
						map.put("erp_batch", itemVo.getErpBatch());
						map.put("package_type_id", itemVo.getPackageTypeId());
						this.insertBusinessHistory(map);
					}
				}
			}
		}
	}

	/**
	 * sap 销售出库过账 单库存地点
	 * 
	 * @param headVo
	 * @param postingDate
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> saveSapSaleByOneLocation(BizStockOutputHeadVo headVo, String postingDate)
			throws Exception {
		if(headVo.getOrderType().equals("ZR10")) {
			headVo.setFtyCode("SW0Z");
		}
		Map<String, Object> sapReturn = new HashMap<String, Object>();
		List<BizStockOutputItemVo> itemVos = headVo.getItemList();
		DTCWWMSDOPOSTREQ mtCWWMSDOPOSTREQ = new DTCWWMSDOPOSTREQ();

		/****************************************
		 * IS_MSG_HEAD
		 ************************************/
		ISMSGHEAD iSMSGHEAD = new ISMSGHEAD();
		String date = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		iSMSGHEAD.setGUID(headVo.getStockOutputCode() + date);
		iSMSGHEAD.setSystemid("WMS");
		mtCWWMSDOPOSTREQ.setISMSGHEAD(iSMSGHEAD);

		/****************************************
		 * IT_LIKP
		 ************************************/
		List<ITLIKP> IT_LIKP = new ArrayList<ITLIKP>();
		ITLIKP iTLIKP = new ITLIKP();
		iTLIKP.setZFLAG("UP");
		iTLIKP.setVBELN(headVo.getReferReceiptCode());
		iTLIKP.setLFDAT(postingDate);
		iTLIKP.setKODAT(postingDate);
		iTLIKP.setTDDAT(postingDate);
		iTLIKP.setLDDAT(postingDate);
		iTLIKP.setWADAT(postingDate);
		IT_LIKP.add(iTLIKP);
		mtCWWMSDOPOSTREQ.setItlikp(IT_LIKP);

		/****************************************
		 * IT_LIPS
		 ************************************/
		List<ITLIPS> IT_LIPS = new ArrayList<ITLIPS>();
		for (int i = 0; i < itemVos.size(); i++) {
			BizStockOutputItemVo item = itemVos.get(i);
			ITLIPS iTLIPS = new ITLIPS();
			iTLIPS.setZFLAG("UP");
			iTLIPS.setVBELN(item.getReferReceiptCode());
			iTLIPS.setPOSNR(item.getReferReceiptRid());
			iTLIPS.setWERKS(headVo.getFtyCode());
			iTLIPS.setLGORT(item.getLocationCode());
			iTLIPS.setCHARG(item.getErpBatch());
			IT_LIPS.add(iTLIPS);
		}
		mtCWWMSDOPOSTREQ.setItlips(IT_LIPS);

		ErpReturn erpReturn = CwErpWsImpl.outputPost(mtCWWMSDOPOSTREQ);
		sapReturn.put("mat_doc_code", erpReturn.getMatDocCode());
		sapReturn.put("message", erpReturn.getMessage());
		sapReturn.put("type", erpReturn.getType());
		return sapReturn;
	}

	/**
	 * sap 销售出库过账 多库存地点或多erp批次
	 * 
	 * @param headVo
	 * @param postingDate
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> saveSapSaleByMoreLocation(BizStockOutputHeadVo headVo, String postingDate)
			throws Exception {
		if(headVo.getOrderType().equals("ZR10")) {
			headVo.setFtyCode("SW0Z");
		}
		Map<String, Object> sapReturn = new HashMap<String, Object>();
		List<BizStockOutputItemVo> itemVos = headVo.getItemList();
		DTCWWMSDOPOSTREQ mtCWWMSDOPOSTREQ = new DTCWWMSDOPOSTREQ();

		/****************************************
		 * IS_MSG_HEAD
		 ************************************/
		ISMSGHEAD iSMSGHEAD = new ISMSGHEAD();
		String date = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		iSMSGHEAD.setGUID(headVo.getStockOutputCode() + date);
		iSMSGHEAD.setSystemid("WMS");
		mtCWWMSDOPOSTREQ.setISMSGHEAD(iSMSGHEAD);

		/****************************************
		 * IS_SPLIT_BATCH
		 ************************************/
		Map<String, List<BizStockOutputItemVo>> itemMap = new HashMap<String, List<BizStockOutputItemVo>>();
		if (itemVos != null && itemVos.size() > 0) {
			for (BizStockOutputItemVo item : itemVos) {
				String key = item.getReferReceiptRid();
				List<BizStockOutputItemVo> itemList = new ArrayList<BizStockOutputItemVo>();
				if (itemMap.containsKey(key)) {
					itemList = itemMap.get(key);
					itemList.add(item);
					itemMap.put(key, itemList);
				} else {
					itemList.add(item);
					itemMap.put(key, itemList);
				}
			}
		}
		Set<String> keys = itemMap.keySet();
		List<ISSPLITBATCH> IS_SPLIT_BATCH = new ArrayList<ISSPLITBATCH>();
		for (String rid : keys) {
			ISSPLITBATCH iSSPLITBATCH = new ISSPLITBATCH();
			iSSPLITBATCH.setVBELN(headVo.getReferReceiptCode());
			iSSPLITBATCH.setPOSNR(rid);
			/****************************************
			 * ZLT_SD_BATCH_CONTENT
			 ************************************/
			List<BizStockOutputItemVo> itemVoList = itemMap.get(rid);
			List<ZLTSDBATCHCONTENT> ZLT_SD_BATCH_CONTENT = new ArrayList<ZLTSDBATCHCONTENT>();
			if (itemVoList != null && itemVoList.size() > 0) {
				for (BizStockOutputItemVo item : itemVoList) {
					ZLTSDBATCHCONTENT zLTSDBATCHCONTENT = new ZLTSDBATCHCONTENT();
					zLTSDBATCHCONTENT.setDOCNUM(item.getStockOutputCode());
					zLTSDBATCHCONTENT.setDOCITEMNUM(item.getStockOutputRid().toString());
					zLTSDBATCHCONTENT.setLFIMG(item.getOutputQty().toString());
					zLTSDBATCHCONTENT.setLGORT(item.getLocationCode());
					zLTSDBATCHCONTENT.setCHARG(item.getErpBatch());
					ZLT_SD_BATCH_CONTENT.add(zLTSDBATCHCONTENT);
				}
			}
			iSSPLITBATCH.setZltsdbatchcontent(ZLT_SD_BATCH_CONTENT);
			IS_SPLIT_BATCH.add(iSSPLITBATCH);
		}
		mtCWWMSDOPOSTREQ.setIssplitbatch(IS_SPLIT_BATCH);

		/****************************************
		 * IT_LIKP
		 ************************************/
		List<ITLIKP> IT_LIKP = new ArrayList<ITLIKP>();
		ITLIKP iTLIKP = new ITLIKP();
		iTLIKP.setZFLAG("UP");
		iTLIKP.setVBELN(headVo.getReferReceiptCode());
		iTLIKP.setLFDAT(postingDate);
		iTLIKP.setKODAT(postingDate);
		iTLIKP.setTDDAT(postingDate);
		iTLIKP.setLDDAT(postingDate);
		iTLIKP.setWADAT(postingDate);
		IT_LIKP.add(iTLIKP);
		mtCWWMSDOPOSTREQ.setItlikp(IT_LIKP);

		/****************************************
		 * IT_LIPS
		 ************************************/
		List<ITLIPS> IT_LIPS = new ArrayList<ITLIPS>();
		for (String rid : keys) {
			ITLIPS iTLIPS = new ITLIPS();
			iTLIPS.setZFLAG("UP");
			iTLIPS.setVBELN(headVo.getReferReceiptCode());
			iTLIPS.setPOSNR(rid);
			// iTLIPS.setWERKS(item.getFtyCode());
			// iTLIPS.setLGORT(item.getLocationCode());
			// iTLIPS.setCHARG(item.getErpBatch());
			IT_LIPS.add(iTLIPS);
		}
		mtCWWMSDOPOSTREQ.setItlips(IT_LIPS);

		ErpReturn erpReturn = CwErpWsImpl.outputPost(mtCWWMSDOPOSTREQ);
		sapReturn.put("mat_doc_code", erpReturn.getMatDocCode());
		sapReturn.put("message", erpReturn.getMessage());
		sapReturn.put("type", erpReturn.getType());
		return sapReturn;
	}

	/**
	 * sap 报废出库过账
	 * 
	 * @param headVo
	 * @param postingDate
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> saveSapScrap(BizStockOutputHeadVo headVo, String postingDate) throws Exception {
		Map<String, Object> sapReturn = new HashMap<String, Object>();
		List<BizStockOutputItemVo> itemVos = headVo.getItemList();
		DTCWWMSMATDOCPOSTREQ dtcwwmsmatdocpostreq = new DTCWWMSMATDOCPOSTREQ();

		/****************************************
		 * IS_MSG_HEAD
		 ************************************/
		com.inossem.wms.wsdl.sap.input.MSGHEAD msghead = new com.inossem.wms.wsdl.sap.input.MSGHEAD();
		String date = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		msghead.setSYSTEMID("WMS");
		msghead.setGUID(date);
		dtcwwmsmatdocpostreq.setISMSGHEAD(msghead);

		/****************************************
		 * IT_DOC_HEAD
		 ************************************/
		List<ITDOCHEAD> itdochead = new ArrayList<ITDOCHEAD>();
		ITDOCHEAD itdocheadtest = new ITDOCHEAD();
		itdocheadtest.setDOCNUM(headVo.getStockOutputCode());
		itdocheadtest.setGOODSMVTCODE("03");
		itdocheadtest.setPSTNGDATE(postingDate);
		itdocheadtest.setDOCDATE(postingDate);
		itdochead.add(itdocheadtest);

		/****************************************
		 * IT_DOC_ITEM
		 ************************************/
		List<ITDOCITEM> itdocitem = new ArrayList<ITDOCITEM>();
		for (BizStockOutputItemVo itemVo : itemVos) {
			ITDOCITEM itdocitemtest = new ITDOCITEM();
			itdocitemtest.setDOCNUM(itemVo.getStockOutputCode());
			itdocitemtest.setDOCITEMNUM(itemVo.getStockOutputId().toString());
			itdocitemtest.setPLANT(itemVo.getFtyCode());
			itdocitemtest.setSTGELOC(itemVo.getLocationCode());
			itdocitemtest.setMATERIAL(itemVo.getMatCode());
			itdocitemtest.setBATCH(itemVo.getErpBatch());
			itdocitemtest.setENTRYUOM("KG");
			BigDecimal qty = itemVo.getStockQty();
			if(itemVo.getUnitCode().equals("KG")) {
				qty = qty.multiply(new BigDecimal("1"));
			}else {
				qty = qty.multiply(new BigDecimal("1000"));
			}
			itdocitemtest.setENTRYQNT(qty.toString());
			itdocitemtest.setMOVETYPE("551");
			itdocitemtest.setCOSTCENTER("8SW4110002");
			itdocitemtest.setTRPARTBA("3000");
			itdocitemtest.setITEMTEXT("报废出库");
			itdocitem.add(itdocitemtest);
		}
		dtcwwmsmatdocpostreq.setItdochead(itdochead);
		dtcwwmsmatdocpostreq.setItdocitem(itdocitem);
		ErpReturn erpReturn = CwErpWsImpl.inputPost(dtcwwmsmatdocpostreq);
		sapReturn.put("mat_doc_code", erpReturn.getMatDocCode());
		sapReturn.put("message", erpReturn.getMessage());
		sapReturn.put("type", erpReturn.getType());
		sapReturn.put("mat_doc_year", erpReturn.getDocyear());
		sapReturn.put("mat_doc_item", erpReturn.getMatDocItem());
		return sapReturn;
	}

	/**
	 * 进行sap转储过账
	 */
	@Override
	public Map<String, Object> saveSapTranSport(Integer stockOutputId, String postingDate, String userId)
			throws Exception {
		BizStockOutputHeadVo head = this.getOrderView(stockOutputId);
		this.checkQtyByOutPut(head);
		List<BizStockOutputItemVo> itemVos = head.getItemList();

		DTCWWMSMATDOCPOSTREQ dtcwwmsmatdocpostreq = new DTCWWMSMATDOCPOSTREQ();
		String date = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");

		/********************************************************************
		 * ISMSGHEAD
		 *******************************************************************/
		com.inossem.wms.wsdl.sap.input.MSGHEAD msghead = new com.inossem.wms.wsdl.sap.input.MSGHEAD();
		msghead.setSYSTEMID("WMS");
		msghead.setGUID(date);
		String ivbukrs = "";
		String ivtestrun = "";
		dtcwwmsmatdocpostreq.setISMSGHEAD(msghead);
		dtcwwmsmatdocpostreq.setIVBUKRS(ivbukrs);
		dtcwwmsmatdocpostreq.setIVTESTRUN(ivtestrun);
		List<DTCWWMSMATDOCPOSTREQ.ITDOCHEAD> itdochead = new ArrayList<DTCWWMSMATDOCPOSTREQ.ITDOCHEAD>();
		List<DTCWWMSMATDOCPOSTREQ.ITDOCITEM> itdocitem = new ArrayList<DTCWWMSMATDOCPOSTREQ.ITDOCITEM>();
		for (int i = 0; i < itemVos.size(); i++) {
			BizStockOutputItemVo itemVo = itemVos.get(i);
			List<BizStockOutputPositionVo> positionList = itemVo.getPositionList();
			for (int j = 0; j < positionList.size(); j++) {

				BizStockOutputPositionVo positionVo = positionList.get(j);

				/********************************************************************
				 * ITDOCHEAD
				 *******************************************************************/
				DTCWWMSMATDOCPOSTREQ.ITDOCHEAD itdocheadtest = new DTCWWMSMATDOCPOSTREQ.ITDOCHEAD();
				itdocheadtest.setDOCNUM(head.getStockOutputCode());
				itdocheadtest.setGOODSMVTCODE("04");
				itdocheadtest.setPSTNGDATE(postingDate);
				itdocheadtest.setDOCDATE(postingDate);
				itdochead.add(itdocheadtest);

				/********************************************************************
				 * ITDOCITEM
				 *******************************************************************/
				DTCWWMSMATDOCPOSTREQ.ITDOCITEM itdocitemtest = new DTCWWMSMATDOCPOSTREQ.ITDOCITEM();
				// 发出
				itdocitemtest.setDOCNUM(head.getStockOutputCode());
				String rid = positionVo.getStockOutputPositionId().toString();
				while (rid.length() < 3) { // 不够三位数字，自动补0
					rid = "0" + rid;
				}
				itdocitemtest.setDOCITEMNUM(rid);

				itdocitemtest.setPLANT(positionVo.getFtyCode());
				itdocitemtest.setSTGELOC(positionVo.getLocationCode());
				itdocitemtest.setMATERIAL(positionVo.getMatCode());
				itdocitemtest.setBATCH(positionVo.getErpBatch());
				itdocitemtest.setENTRYUOM("KG");
				BigDecimal qty = positionVo.getStockQty();
				if(positionVo.getUnitCode().equals("KG")) {
					qty = qty.multiply(new BigDecimal("1"));
				}else {
					qty = qty.multiply(new BigDecimal("1000"));
				}
				itdocitemtest.setENTRYQNT(qty.toString());
				itdocitemtest.setMOVETYPE("301");

				// 接收
				itdocitemtest.setMOVEPLANT(itemVo.getFtyCode());
				itdocitemtest.setMOVESTLOC(itemVo.getLocationCode());
				itdocitemtest.setMOVEMAT(itemVo.getMatCode());
				itdocitemtest.setMOVEBATCH(itemVo.getErpBatch());
				itdocitem.add(itdocitemtest);
			}
		}
		dtcwwmsmatdocpostreq.setItdocitem(itdocitem);
		dtcwwmsmatdocpostreq.setItdochead(itdochead);
		ErpReturn erpReturn = CwErpWsImpl.inputPost(dtcwwmsmatdocpostreq);
		Map<String, Object> sapReturn = new HashMap<String, Object>();
		sapReturn.put("mat_doc_code", erpReturn.getMatDocCode());
		sapReturn.put("message", erpReturn.getMessage());
		sapReturn.put("type", erpReturn.getType());
		sapReturn.put("mat_doc_year", erpReturn.getDocyear());
		sapReturn.put("mat_doc_item", erpReturn.getMatDocItem());
		sapReturn.put("posting_date", postingDate);
		sapReturn.put("doc_time", postingDate);
		sapReturn.put("stock_output_id", head.getStockOutputId());
		sapReturn.put("syn_type", head.getSynType());
		return sapReturn;
	}

	/**
	 * 转储mes同步
	 * 
	 * @param head
	 * @param opeTypeCode
	 * @return
	 * @throws Exception
	 */
	private WmIopRetVal saveMesTranSport(BizStockOutputHeadVo head, String opeTypeCode) throws Exception {
		ArrayOfWmMvRecInteropeDto dtoSet = new ArrayOfWmMvRecInteropeDto();
		List<WmMvRecInteropeDto> wmMvRecInteropeDto = new ArrayList<WmMvRecInteropeDto>();
		List<BizStockOutputItemVo> itemVos = head.getItemList();
		itemVos = this.dataFormatByItem(itemVos);
		for (int i = 0; i < itemVos.size(); i++) {
			BizStockOutputItemVo itemVo = itemVos.get(i);
			List<BizStockOutputPositionVo> positionList = itemVo.getPositionList();
			positionList = this.dataFormatByPosition(positionList);
			for (int j = 0; j < positionList.size(); j++) {
				BizStockOutputPositionVo positionVo = positionList.get(j);
				WmMvRecInteropeDto dto = new WmMvRecInteropeDto();
				dto.setRecNum(positionVo.getStockOutputPositionId());
				dto.setSrMtrlCode(positionVo.getMatCode());
				dto.setTgMtrlCode(itemVo.getMatCode());
				BigDecimal wgtperpack = positionVo.getPackageStandard();
				dto.setWgtPerPack(packageTypeDao.selectByPrimaryKey(positionVo.getPackageTypeId()).getPackageStandard().doubleValue());
				dto.setAmnt(positionVo.getOutputQty().divide(wgtperpack,0, RoundingMode.UP).intValue());
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("mes_unit_code", "吨");
				param.put("wms_unit_code", positionVo.getUnitCode());
				String rel=this.getUnitRelMesAndWms(param);
				dto.setWgt(positionVo.getOutputQty().divide(new BigDecimal(rel)).doubleValue());
				dto.setSrBch(positionVo.getErpBatch());
				dto.setTgBch(itemVo.getErpBatch());
				dto.setSrRankCode(EnumRankLevel.getRankByValue(positionVo.getErpBatch()));
				dto.setTgRankCode(EnumRankLevel.getRankByValue(itemVo.getErpBatch()));
				dto.setSrNodeCode(positionVo.getMesLocationCode());
				dto.setTgNodeCode(itemVo.getMesLocationCode());
				dto.setDlvBillCode("1001");
				dto.setCustomer(head.getReceiveName());
				dto.setTransTypeCode("1");
				dto.setVehiCode("1001");
				dto.setDes(head.getStockOutputCode());
				dto.setWgtDim("TO");
				wmMvRecInteropeDto.add(dto);
			}
		}
		dtoSet.setWmMvRecInteropeDto(wmMvRecInteropeDto);
		WmIopRetVal val = CwMesWsImpl.updateMesStock(head.getStockOutputCode(), opeTypeCode,
				getBeginDate(head.getClassTypeId()), getEndDate(head.getClassTypeId()), dtoSet);
		return val;
	}

	/**
	 * 出库mes同步
	 * 
	 * @param head
	 * @param opeTypeCode
	 * @return
	 * @throws Exception
	 */
	private WmIopRetVal saveMesOutPut(BizStockOutputHeadVo head, String opeTypeCode) throws Exception {
		String credentialCode = "".equals(head.getReferReceiptCode()) ? head.getStockOutputCode()
				: head.getReferReceiptCode();
		ArrayOfWmMvRecInteropeDto dtoSet = new ArrayOfWmMvRecInteropeDto();
		List<WmMvRecInteropeDto> wmMvRecInteropeDto = new ArrayList<WmMvRecInteropeDto>();
		List<BizStockOutputItemVo> itemVos = head.getItemList();
		itemVos = this.dataFormatByItem(itemVos);
		for (BizStockOutputItemVo itemVo : itemVos) {
			List<BizStockOutputPositionVo> positionList = itemVo.getPositionList();
			positionList = this.dataFormatByPosition(positionList);
			for (int j = 0; j < positionList.size(); j++) {
				BizStockOutputPositionVo positionVo = positionList.get(j);
				WmMvRecInteropeDto dto = new WmMvRecInteropeDto();
				dto.setRecNum(positionVo.getStockOutputPositionId());
				dto.setSrMtrlCode(positionVo.getMatCode());
				dto.setTgMtrlCode(positionVo.getMatCode());
				BigDecimal wgtperpack = positionVo.getPackageStandard();
				dto.setWgtPerPack(packageTypeDao.selectByPrimaryKey(positionVo.getPackageTypeId()).getPackageStandard().doubleValue());
				dto.setAmnt(positionVo.getOutputQty().divide(wgtperpack,0, RoundingMode.UP).intValue());
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("mes_unit_code", "吨");
				param.put("wms_unit_code", positionVo.getUnitCode());
				String rel=this.getUnitRelMesAndWms(param);
				dto.setWgt(positionVo.getOutputQty().divide(new BigDecimal(rel)).doubleValue());
				dto.setSrBch(positionVo.getErpBatch());
				dto.setTgBch(positionVo.getErpBatch());
				dto.setSrRankCode(EnumRankLevel.getRankByValue(positionVo.getErpBatch()));
				dto.setTgRankCode(EnumRankLevel.getRankByValue(positionVo.getErpBatch()));
				//直发取销售工厂库存地点
				if(head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_STRAIGTH.getValue()) {
					dto.setSrNodeCode(itemVo.getMesLocationCode());
					dto.setTgNodeCode(itemVo.getMesLocationCode());
				} else {
					dto.setSrNodeCode(positionVo.getMesLocationCode());
					dto.setTgNodeCode(positionVo.getMesLocationCode());
				}
				dto.setDlvBillCode(credentialCode);
				dto.setCustomer("".equals(head.getReceiveName()) ? "报废出库" : head.getReceiveName());
				dto.setTransTypeCode("1");
				dto.setVehiCode("1001");
				dto.setDes(head.getStockOutputCode());
				dto.setWgtDim("TO");
				wmMvRecInteropeDto.add(dto);
			}
		}
		dtoSet.setWmMvRecInteropeDto(wmMvRecInteropeDto);
		WmIopRetVal val = CwMesWsImpl.updateMesStock(credentialCode, opeTypeCode, getBeginDate(head.getClassTypeId()),
				getEndDate(head.getClassTypeId()), dtoSet);
		return val;
	}
	
	/**
	 * 按物料、包装规格、ERP批次、库存地点汇总详情
	 * @param positionList
	 * @return
	 */
	private List<BizStockOutputPositionVo> dataFormatByPosition(List<BizStockOutputPositionVo> positionList){
		List<BizStockOutputPositionVo> result = new ArrayList<BizStockOutputPositionVo>();
		Map<String,BizStockOutputPositionVo> data = new HashMap<String,BizStockOutputPositionVo>();
		for(BizStockOutputPositionVo positionVo : positionList) {
			String key = positionVo.getMatId()+"-"+positionVo.getPackageStandard()+"-"+positionVo.getErpBatch()+"-"+positionVo.getLocationId();
			if(data.containsKey(key)) {
				BizStockOutputPositionVo position = data.get(key);
				position.setOutputQty(position.getOutputQty().add(positionVo.getOutputQty()));
				data.put(key, position);
			}else {
				data.put(key, positionVo);
			}
		}
		Iterator<Entry<String, BizStockOutputPositionVo>> it = data.entrySet().iterator();
		while(it.hasNext()) {
			result.add(it.next().getValue());
		}
		return result;
	}
	
	/**
	 * 按物料、包装规格、ERP批次、库存地点汇总行项目
	 * @param itemVos
	 * @return
	 */
	private List<BizStockOutputItemVo> dataFormatByItem(List<BizStockOutputItemVo> itemVos){
		List<BizStockOutputItemVo> result = new ArrayList<BizStockOutputItemVo>();
		Map<String,BizStockOutputItemVo> data = new HashMap<String,BizStockOutputItemVo>();
		for(BizStockOutputItemVo itemVo : itemVos) {
			String key = itemVo.getMatId()+"-"+itemVo.getPackageStandard()+"-"+itemVo.getErpBatch()+"-"+itemVo.getLocationId();
			if(data.containsKey(key)) {
				BizStockOutputItemVo item = data.get(key);
				List<BizStockOutputPositionVo> positionList = item.getPositionList();
				List<BizStockOutputPositionVo> positionListVo = itemVo.getPositionList();
				positionList.addAll(positionListVo);
				item.setPositionList(positionList);
				item.setOutputQty(item.getOutputQty().add(itemVo.getOutputQty()));
				data.put(key, item);
			}else {
				data.put(key, itemVo);
			}
		}
		Iterator<Entry<String, BizStockOutputItemVo>> it = data.entrySet().iterator();
		while(it.hasNext()) {
			result.add(it.next().getValue());
		}
		return result;
	}
	
	private String getUnitRelMesAndWms(Map<String, Object> param) {
		return stockInputHeadDao.getUnitRelMesAndWms(param);
	}
	
	private String getBeginDate(Integer classTypeId) {
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(classTypeDao.selectByPrimaryKey(classTypeId).getStartTime()).toString();
	}

	private String getEndDate(Integer classTypeId) {
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(classTypeDao.selectByPrimaryKey(classTypeId).getEndTime()).toString();
	}

	/**
	 * sap 过账
	 * 
	 * @param stockOutputId
	 * @param postingDate
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> saveOrderToSap(Integer stockOutputId, String postingDate, String userId)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		BizStockOutputHeadVo headVo = this.getOrderView(stockOutputId);
		// 销售出库 紧急作业销售出库 紧急记账销售出库
		if (headVo.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()
				|| headVo.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue()
				|| headVo.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()
				|| headVo.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue()) {
			// 验证库存地点是否多个
			if (this.outPutHeadDao.selectCountByLocation(stockOutputId) > 1) {
				result = this.saveSapSaleByMoreLocation(headVo, postingDate);
			} else {
				result = this.saveSapSaleByOneLocation(headVo, postingDate);
			}
			// 报废出库
		} else if (headVo.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()) {
			result = this.saveSapScrap(headVo, postingDate);
			// 直发出库
		} else if (headVo.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_STRAIGTH.getValue()) {
			this.checkQtyByOutPut(headVo);
			// 验证库存地点是否多个
			if (this.outPutHeadDao.selectCountByLocation(stockOutputId) > 1) {
				result = this.saveSapSaleByMoreLocation(headVo, headVo.getPostingDate());
			} else {
				result = this.saveSapSaleByOneLocation(headVo, headVo.getPostingDate());
			}
		}
		result.put("stock_output_id", stockOutputId);
		result.put("stock_output_code", headVo.getStockOutputCode());
		result.put("stock_output_type", headVo.getReceiptType());
		result.put("refer_receipt_code", headVo.getReferReceiptCode());
		result.put("syn_type", headVo.getSynType());
		result.put("posting_date", postingDate);
		result.put("doc_time", postingDate);
		return result;
	}

	/**
	 * 修改出库单抬头单据状态
	 * 
	 * @param stockOutputId
	 * @param userId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateOutPutHead(Integer stockOutputId, String userId, Byte status) throws Exception {
		BizStockOutputHead head = new BizStockOutputHead();
		head.setStockOutputId(stockOutputId);
		head.setStatus(status);
		head.setModifyUser(userId);
		head.setModifyTime(new Date());
		return outPutHeadDao.updateByPrimaryKeySelective(head);
	}

	/**
	 * 修改出库单行项目冲销状态
	 * 
	 * @param itemId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public int updateOutPutItem(Integer itemId, Byte status) throws Exception {
		BizStockOutputItem outputItem = new BizStockOutputItem();
		outputItem.setItemId(itemId);
		outputItem.setWriteOff(status);
		return outPutItemDao.updateByPrimaryKeySelective(outputItem);
	}

	/**
	 * 修改下架作业单
	 * 
	 * @param map
	 * @throws Exception
	 */
	private void updateTaskStatus(Map<String, Object> map) throws Exception {
		this.bizStockTaskHeadCwDao.updateStatusByReferReceiptIdAndType(map);
	}

	/**
	 * 关联交货单
	 * 
	 * @param head
	 * @throws Exception
	 */
	private void insertBeforInfo(BizStockOutputHead head) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("referReceiptCode", head.getReferReceiptCode());
		map.put("beforeOrderId", head.getStockOutputId());
		map.put("beforeOrderCode", head.getStockOutputCode());
		map.put("beforeOrderType", head.getReceiptType());
		this.allocateCargoHeadDao.insertBeforeInfo(map);
	}

	/**
	 * (通用)出库sap成功后修改库存与单据状态
	 * 
	 * @param sapReturn
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public String updateOutPutOrder(Map<String, Object> sapReturn, String userId) throws Exception {
		String msg = "成功";
		if (("S").equals(sapReturn.get("type"))) {
			BizStockOutputHeadVo head = this.getOrderView(UtilObject.getIntOrZero(sapReturn.get("stock_output_id")));
			// 销售出库
			if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("referReceiptCode", UtilObject.getStringOrEmpty(sapReturn.get("refer_receipt_code")));
				List<Byte> list = new ArrayList<Byte>();
				list.add(EnumReceiptStatus.RECEIPT_WORK.getValue());
				param.put("status", list);
				List<Map<String, Object>> orders = allocateCargoHeadDao.selectOrderCodeAndId(param);
				for (Map<String, Object> map : orders) {
					// 仓位库存
					this.updateStockPosition(UtilObject.getIntOrZero(map.get("referReceiptId")),
							UtilObject.getStringOrEmpty(map.get("referReceiptCode")),
							UtilObject.getByteOrNull(map.get("referReceiptType")));

					map.put("status", EnumReceiptStatus.RECEIPT_FINISH.getValue());
					// 下架作业单状态
					this.updateTaskStatus(map);
				}

				// 批次库存
				this.updateStockBatch(head, userId, EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				// 单据状态 已完成
				this.updateOutPutHead(UtilObject.getIntOrZero(sapReturn.get("stock_output_id")), userId,
						EnumReceiptStatus.RECEIPT_FINISH.getValue());

				// 报废出库
			} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()) {
				// 下架作业单状态
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("referReceiptId", sapReturn.get("stock_output_id"));
				map.put("referReceiptType", sapReturn.get("stock_output_type"));
				map.put("status", EnumReceiptStatus.RECEIPT_FINISH.getValue());
				this.updateTaskStatus(map);
				// 仓位库存
				this.updateStockPosition(head.getStockOutputId(), head.getStockOutputCode(), head.getReceiptType());
				// 批次库存
				this.updateStockBatch(head, userId, EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				// 单据状态 已完成
				this.updateOutPutHead(UtilObject.getIntOrZero(sapReturn.get("stock_output_id")), userId,
						EnumReceiptStatus.RECEIPT_FINISH.getValue());

				// 直发出库
			} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_STRAIGTH.getValue()) {
				// 修改库存
				this.updateStock(head, userId, EnumDebitCredit.CREDIT_H_SUBTRACT.getName(),
						EnumBoolean.FALSE.getValue());
				// 单据状态 已完成
				this.updateOutPutHead(UtilObject.getIntOrZero(sapReturn.get("stock_output_id")), userId,
						EnumReceiptStatus.RECEIPT_FINISH.getValue());
				// 紧急记账销售出库
			} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue()) {
				// 单据状态 待出库
				this.updateOutPutHead(UtilObject.getIntOrZero(sapReturn.get("stock_output_id")), userId,
						EnumReceiptStatus.RECEIPT_BEOUTPUT.getValue());
				// 紧急作业销售出库(紧急库存)
			} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue()) {
				// 修改库存
				this.updateStock(head, userId, EnumDebitCredit.CREDIT_H_SUBTRACT.getName(),
						EnumBoolean.TRUE.getValue());
				// 单据状态 已完成
				this.updateOutPutHead(UtilObject.getIntOrZero(sapReturn.get("stock_output_id")), userId,
						EnumReceiptStatus.RECEIPT_FINISH.getValue());
				// 紧急作业销售出库(正常库存)
			} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()) {
				// 单据状态 已完成
				this.updateOutPutHead(UtilObject.getIntOrZero(sapReturn.get("stock_output_id")), userId,
						EnumReceiptStatus.RECEIPT_FINISH.getValue());
			}
			// 保存出库过账物料凭证
			this.outPutItemDao.updateByOutPutId(sapReturn);
			// 保存过账日期 凭证日期
			this.outPutHeadDao.insertPostingDateAndDocTime(sapReturn);
		} else {
			// TODO 过账失败单据状态修改为未完成状态 因需要抛出异常会回滚故无法实现
			// this.updateOutPutHead(UtilObject.getIntOrZero(sapReturn.get("stock_output_id")),
			// EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
			msg = sapReturn.get("message").toString();
			throw new WMSException(msg);
		}
		return msg;
	}

	/**
	 * sap冲销后修改仓位库存
	 * 
	 * @param stockOutputId
	 * @param stockOutputCode
	 * @param stockOutputType
	 * @throws Exception
	 */
	public void updateStockPostionByTaskReq(Integer stockOutputId, String stockOutputCode, Byte stockOutputType)
			throws Exception {
		BizStockTaskPositionCw positon = new BizStockTaskPositionCw();
		positon.setReferReceiptId(stockOutputId);
		positon.setReferReceiptType(stockOutputType);
		positon.setReferReceiptCode(stockOutputCode);
		List<BizStockTaskPositionCw> positionCws = bizStockTaskPositionCwDao.selectByReferReceiptIdAndType(positon);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("referReceiptId", stockOutputId);
		param.put("referReceiptCode", stockOutputCode);
		param.put("referReceiptType", stockOutputType);
		param.put("receiptType", EnumReceiptType.STOCK_TASK_LISTING_REQ.getValue());
		List<BizStockTaskReqItem> reqItems = StockTaskReqItemDao.getBizStockTaskReqItemListByReferReipt(param);
		commonService.updateStockPositionByTaskReq(EnumReceiptType.STOCK_TASK_LISTING.getValue(), reqItems, positionCws,
				EnumStockType.STOCK_TYPE_ERP.getValue(), false);
	}

	/**
	 * 冲销修改单据
	 * @param head
	 * @param userId
	 * @throws Exception
	 */
	private void updateWriteOffToQty(BizStockOutputHeadVo head, String userId)throws Exception {
		
		// 报废出库冲销后
		if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()) {
			// 发起上架请求
			this.saveStockTaskReqUpFomat(head.getStockOutputId(), head.getStockOutputCode(), head.getReceiptType(),
					userId);
			// 仓位库存修改
			this.updateStockPostionByTaskReq(head.getStockOutputId(), head.getStockOutputCode(),
					head.getReceiptType());
			// 批次库存修改
			this.updateStockBatch(head, userId, EnumDebitCredit.DEBIT_S_ADD.getName());

			// 销售出库冲销后
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("referReceiptCode", head.getReferReceiptCode());
			param.put("beforeOrderId",head.getStockOutputId());
			param.put("beforeOrderCode",head.getStockOutputCode());
			param.put("beforeOrderType",head.getReceiptType());
			List<Map<String, Object>> orders = allocateCargoHeadDao.selectOrderCodeAndId(param);
			if (orders != null && orders.size() > 0) {
				for (Map<String, Object> map : orders) {
					// 修改配货单状态
					this.allocateCargoHeadDao.updateOrderStatus(UtilObject.getIntOrZero(map.get("referReceiptId")),
							EnumReceiptStatus.RECEIPT_WRITEOFF.getValue());
					// 发起上架请求
					this.saveStockTaskReqUpFomat(UtilObject.getIntOrZero(map.get("referReceiptId")),
							UtilObject.getStringOrEmpty(map.get("referReceiptCode")),
							UtilObject.getByteOrNull(map.get("referReceiptType")), userId);

					// 仓位库存
					this.updateStockPostionByTaskReq(UtilObject.getIntOrZero(map.get("referReceiptId")),
							UtilObject.getStringOrEmpty(map.get("referReceiptCode")),
							UtilObject.getByteOrNull(map.get("referReceiptType")));
				}
			}

			// 批次库存
			this.updateStockBatch(head, userId, EnumDebitCredit.DEBIT_S_ADD.getName());

			// 紧急记账销售出库冲销后
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue()) {
			// 待出库状态下冲销
			if (head.getStatus() == EnumReceiptStatus.RECEIPT_BEOUTPUT.getValue()) {
				// 解除冻结
				List<BizStockOutputItemVo> itemVos = head.getItemList();
				for(BizStockOutputItemVo itemVo : itemVos) {
					List<BizStockOutputPositionVo> positionVos = itemVo.getPositionList();
					for(BizStockOutputPositionVo position : positionVos) {
						StockOccupancy stockOccupancy = new StockOccupancy();
						stockOccupancy.setBatch(position.getBatch());
						stockOccupancy.setFtyId(position.getFtyId());
						stockOccupancy.setLocationId(position.getLocationId());
						stockOccupancy.setMatId(position.getMatId());
						stockOccupancy.setPackageTypeId(position.getPackageTypeId());
						stockOccupancy.setQty(position.getStockQty());
						stockOccupancy.setStockTypeId(position.getStockTypeId());
						this.commonService.modifyStockOccupancy(stockOccupancy, "H");
					}
				}
				// 已完成状态下冲销
			} else if (head.getStatus() == EnumReceiptStatus.RECEIPT_FINISH.getValue()) {
				//库存类型
				Byte StockTypeId = head.getItemList().get(0).getPositionList().get(0).getStockTypeId();
				//记账库存特殊处理
				if(StockTypeId == EnumStockType.STOCK_TYPE_ACCOUNT.getValue() ) {
					// 修改库存
					this.updateStock(head, userId, EnumDebitCredit.DEBIT_S_ADD.getName(), EnumBoolean.FALSE.getValue());
					
				//正常库存已售未提
				} else {
					List<BizStockOutputItemVo> itemVos = head.getItemList();
					for(BizStockOutputItemVo itemVo : itemVos) {
						this.bookSaleUpdateQty(head,itemVo,userId,EnumDebitCredit.DEBIT_S_ADD.getName());
					}
				}
				
			}
		}
		// 修改出库单状态
		this.updateOutPutHead(head.getStockOutputId(), userId, EnumReceiptStatus.RECEIPT_WRITEOFF.getValue());
		// 修改出库单行项目冲销状态
		List<BizStockOutputItemVo> itemList = head.getItemList();
		if (itemList != null && itemList.size() > 0) {
			for (BizStockOutputItemVo item : itemList) {
				this.updateOutPutItem(item.getItemId(), EnumBoolean.TRUE.getValue());
			}
		}
	}
	
	/**
	 * sap冲销完成后修改状态与库存信息
	 * 
	 * @param sapReturn
	 * @param head
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String updateToQty(Map<String,Object> sapReturn, BizStockOutputHeadVo head, String userId)
			throws Exception {
		String msg = "冲销成功";
		if (("S").equals(sapReturn.get("type"))) {
			// 报废出库冲销后
			if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()) {
				// 发起上架请求
				this.saveStockTaskReqUpFomat(head.getStockOutputId(), head.getStockOutputCode(), head.getReceiptType(),
						userId);
				// 仓位库存修改
				this.updateStockPostionByTaskReq(head.getStockOutputId(), head.getStockOutputCode(),
						head.getReceiptType());
				// 批次库存修改
				this.updateStockBatch(head, userId, EnumDebitCredit.DEBIT_S_ADD.getName());

				// 销售出库冲销后
			} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("referReceiptCode", head.getReferReceiptCode());
				param.put("beforeOrderId",head.getStockOutputId());
				param.put("beforeOrderCode",head.getStockOutputCode());
				param.put("beforeOrderType",head.getReceiptType());
				List<Map<String, Object>> orders = allocateCargoHeadDao.selectOrderCodeAndId(param);
				if (orders != null && orders.size() > 0) {
					for (Map<String, Object> map : orders) {
						// 修改配货单状态
						this.allocateCargoHeadDao.updateOrderStatus(UtilObject.getIntOrZero(map.get("referReceiptId")),
								EnumReceiptStatus.RECEIPT_WRITEOFF.getValue());
						// 发起上架请求
						this.saveStockTaskReqUpFomat(UtilObject.getIntOrZero(map.get("referReceiptId")),
								UtilObject.getStringOrEmpty(map.get("referReceiptCode")),
								UtilObject.getByteOrNull(map.get("referReceiptType")), userId);
						// 仓位库存
						this.updateStockPostionByTaskReq(UtilObject.getIntOrZero(map.get("referReceiptId")),
								UtilObject.getStringOrEmpty(map.get("referReceiptCode")),
								UtilObject.getByteOrNull(map.get("referReceiptType")));
					}
				}
				// 批次库存
				this.updateStockBatch(head, userId, EnumDebitCredit.DEBIT_S_ADD.getName());
				// 紧急记账销售出库冲销后
			} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue()) {
				// 待出库状态下冲销
				if (head.getStatus() == EnumReceiptStatus.RECEIPT_BEOUTPUT.getValue()) {

					// 已完成状态下冲销
				} else if (head.getStatus() == EnumReceiptStatus.RECEIPT_FINISH.getValue()) {
					// 修改库存
					this.updateStock(head, userId, EnumDebitCredit.DEBIT_S_ADD.getName(), EnumBoolean.FALSE.getValue());
				}
			}
			// 修改出库单状态
			this.updateOutPutHead(head.getStockOutputId(), userId, EnumReceiptStatus.RECEIPT_WRITEOFF.getValue());
			// 保存冲销物料凭证
			this.outPutItemDao.updateByOutPutId(sapReturn);
			// 修改出库单行项目冲销状态
			List<BizStockOutputItemVo> itemList = head.getItemList();
			if (itemList != null && itemList.size() > 0) {
				for (BizStockOutputItemVo item : itemList) {
					this.updateOutPutItem(item.getItemId(), EnumBoolean.TRUE.getValue());
				}
			}
		} else {
			msg = sapReturn.get("message").toString();
			throw new WMSException(msg);
		}
		return msg;
	}

	/**
	 * 完成出库
	 * 
	 * @param stockOutputId
	 * @param postingDate
	 * @param createUserId
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject saveOrderOutput(Integer stockOutputId, String postingDate, String createUserId) throws Exception {
		JSONObject result = new JSONObject();
		result.put("stock_output_id", stockOutputId);
		BizStockOutputHeadVo head = this.getOrderView(stockOutputId);
		// 紧急作业销售出库(紧急库存)
		if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue()) {
			// 下架作业单状态
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("referReceiptId", head.getStockOutputId());
			map.put("referReceiptCode", head.getStockOutputCode());
			map.put("referReceiptType", head.getReceiptType());
			map.put("status", EnumReceiptStatus.RECEIPT_FINISH.getValue());
			this.updateTaskStatus(map);
			// 修改库存
			this.updateStockPositionOnUrgent(head.getStockOutputId(), head.getStockOutputCode(), head.getReceiptType(),
					createUserId);
			// 单据状态 已捡货
			this.updateOutPutHead(stockOutputId, createUserId, EnumReceiptStatus.RECEIPT_SORTING.getValue());
			// 紧急作业销售出库(正常库存)
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()) {
			// 下架作业单状态
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("referReceiptId", head.getStockOutputId());
			map.put("referReceiptCode", head.getStockOutputCode());
			map.put("referReceiptType", head.getReceiptType());
			map.put("status", EnumReceiptStatus.RECEIPT_FINISH.getValue());
			this.updateTaskStatus(map);
			// 修改库存
			this.updateStock(head, createUserId, EnumDebitCredit.CREDIT_H_SUBTRACT.getName(),
					EnumBoolean.FALSE.getValue());
			// 单据状态 已捡货
			this.updateOutPutHead(stockOutputId, createUserId, EnumReceiptStatus.RECEIPT_SORTING.getValue());
			// 其他出库
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_OTHER.getValue()) {
			// 仓位库存
			this.updateStockPosition(head.getStockOutputId(), head.getStockOutputCode(), head.getReceiptType());
			// 批次库存
			this.updateStockBatch(head, createUserId, EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
			// 下架作业单状态
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("referReceiptId", head.getStockOutputId());
			map.put("referReceiptCode", head.getStockOutputCode());
			map.put("referReceiptType", head.getReceiptType());
			map.put("status", EnumReceiptStatus.RECEIPT_FINISH.getValue());
			this.updateTaskStatus(map);
			// 单据状态 已完成
			this.updateOutPutHead(stockOutputId, createUserId, EnumReceiptStatus.RECEIPT_FINISH.getValue());
			// 保存过账日期 凭证日期
			map.put("stock_output_id", stockOutputId);
			map.put("posting_date", postingDate);
			map.put("doc_time", postingDate);
			this.outPutHeadDao.insertPostingDateAndDocTime(map);
			// 紧急记账销售出库
		} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue()) {
			//库存类型
			Byte StockTypeId = head.getItemList().get(0).getPositionList().get(0).getStockTypeId();
			//记账库存特殊处理
			if(StockTypeId == EnumStockType.STOCK_TYPE_ACCOUNT.getValue() ) {
				// 修改库存
				this.updateStock(head, createUserId, EnumDebitCredit.CREDIT_H_SUBTRACT.getName(),
						EnumBoolean.FALSE.getValue());
			//正常库存已售未提
			} else {
				List<BizStockOutputItemVo> itemVos = head.getItemList();
				for(BizStockOutputItemVo itemVo : itemVos) {
					bookSaleUpdateQty(head,itemVo,createUserId,EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				}
			}
			
			// 解除冻结
			List<BizStockOutputItemVo> itemVos = head.getItemList();
			for(BizStockOutputItemVo itemVo : itemVos) {
				List<BizStockOutputPositionVo> positionVos = itemVo.getPositionList();
				for(BizStockOutputPositionVo position : positionVos) {
					StockOccupancy stockOccupancy = new StockOccupancy();
					stockOccupancy.setBatch(position.getBatch());
					stockOccupancy.setFtyId(position.getFtyId());
					stockOccupancy.setLocationId(position.getLocationId());
					stockOccupancy.setMatId(position.getMatId());
					stockOccupancy.setPackageTypeId(position.getPackageTypeId());
					stockOccupancy.setQty(position.getStockQty());
					stockOccupancy.setStockTypeId(position.getStockTypeId());
					this.commonService.modifyStockOccupancy(stockOccupancy, "H");
				}
			}
			// 单据状态 已完成
			this.updateOutPutHead(stockOutputId, createUserId, EnumReceiptStatus.RECEIPT_FINISH.getValue());
		}
		return result;
	}

	/**
	 * 获取物料
	 */
	@Override
	public List<Map<String, Object>> selectMaterialList(Map<String, Object> param) {
		List<Map<String, Object>> mapList = this.dicMaterialDao.selectMaterialList(param);
		List<RelUserStockLocationVo> locationVos = this.stockLocationDao
				.selectByFtyId(UtilObject.getIntOrZero(param.get("ftyId")));
		List<Map<String, Object>> locationList = new ArrayList<Map<String, Object>>();
		for (RelUserStockLocationVo locationVo : locationVos) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("location_id", locationVo.getLocationId());
			map.put("location_code", locationVo.getLocationCode());
			map.put("location_name", locationVo.getLocationName());
			locationList.add(map);
		}
		for (Map<String, Object> item : mapList) {
			item.put("location_list", locationList);
			item.put("erpbatch_list", commonService.getErpBatchList(UtilObject.getIntOrZero(item.get("mat_id")),
					UtilObject.getIntOrZero(param.get("ftyId"))));
			item.remove("output_qty");
		}
		return mapList;
	}

	/**
	 * 校验交货单与出库单是否一致
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Boolean checkOrderBySapOrder(String refer_receipt_code, Integer stockOutputId) throws Exception {
		Boolean flag = true;
		Map<String, Object> order = this.getOrderViewBySap(refer_receipt_code, null);
		BizStockOutputHeadVo head = this.getOrderView(stockOutputId);
		head.setReferReceiptCode(UtilObject.getStringOrEmpty(order.get("refer_receipt_code")));
		head.setSaleOrderCode(UtilObject.getStringOrEmpty(order.get("sale_order_code")));
		head.setPreorderId(UtilObject.getStringOrEmpty(order.get("preorder_id")));
		head.setReceiveCode(UtilObject.getStringOrEmpty(order.get("receive_code")));
		head.setReceiveName(UtilObject.getStringOrEmpty(order.get("receive_name")));
		head.setOrderType(UtilObject.getStringOrEmpty(order.get("order_type")));
		head.setOrderTypeName(UtilObject.getStringOrEmpty(order.get("order_type_name")));
		this.outPutHeadDao.updateByPrimaryKeySelective(head);
		List<Map<String, Object>> itemSapList = (List<Map<String, Object>>) order.get("item_list");
		List<BizStockOutputItemVo> itemVos = head.getItemList();
		if (itemSapList.size() != itemVos.size()) {
			flag = false;
			throw new WMSException("交货单与出库单行项目行项目数不一致: 交货单" + itemSapList.size() + "行,出库单" + itemVos.size() + "行。");
		}
		for (int i = 0; i < itemSapList.size(); i++) {
			Map<String, Object> itemSap = itemSapList.get(i);
			BizStockOutputItemVo item = itemVos.get(i);
			if (item.getStockQty().compareTo(UtilObject.getBigDecimalOrZero(itemSap.get("order_qty"))) != 0) {
				flag = false;
				throw new WMSException("交货单与出库单交货数量不一致: 行数" + (i + 1) + ",交货单数量:"
						+ UtilObject.getBigDecimalOrZero(itemSap.get("order_qty")) + ",出货单数量:" + item.getStockQty()
						+ "。");
			}
			if (!item.getMatCode().equals(UtilObject.getStringOrEmpty(itemSap.get("mat_code")))) {
				flag = false;
				throw new WMSException("交货单与出库单物料信息不一致: 行数" + (i + 1) + ",交货单物料信息:"
						+ UtilObject.getBigDecimalOrZero(itemSap.get("mat_code")) + ",出货物料信息:" + item.getMatCode()
						+ "。");
			}
			if (!item.getErpBatch().equals(UtilObject.getStringOrEmpty(itemSap.get("erp_batch")))) {
				flag = false;
				throw new WMSException("交货单erp批次与出库单erp批次不一致: 行数" + (i + 1) + ",交货单erp批次信息:"
						+ UtilObject.getBigDecimalOrZero(itemSap.get("erp_batch")) + ",出货单erp批次信息:" + item.getErpBatch()
						+ "。");
			}
			item.setReferReceiptRid(UtilObject.getStringOrEmpty(itemSap.get("refer_receipt_rid")));
			item.setReferReceiptCode(UtilObject.getStringOrEmpty(itemSap.get("refer_receipt_code")));
			this.outPutItemDao.updateByPrimaryKeySelective(item);
		}
		return flag;
	}

	/**
	 * 插入业务历史纪录
	 * 
	 * @param data
	 * @throws Exception
	 */
	private void insertBusinessHistory(Map<String, Object> data) throws Exception {
		BizBusinessHistory businessHistory = new BizBusinessHistory();
		Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
		DicStockLocationVo locationObj = locationMap.get(data.get("location_id"));
		Integer whId = locationObj.getWhId();
		String whCode = dictionaryService.getWhCodeByWhId(whId);
		String areaCode = UtilProperties.getInstance().getDefaultCCQ();
		Integer areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);
		Integer positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode, areaCode,
				UtilProperties.getInstance().getDefaultCW());
		businessHistory.setAreaId(areaId);
		businessHistory.setBatch(UtilObject.getLongOrNull(data.get("batch")));
		businessHistory.setBusinessType(UtilObject.getByteOrNull(data.get("business_type")));
		businessHistory.setCreateTime(new Date());
		businessHistory.setDebitCredit(UtilObject.getStringOrEmpty(data.get("debit_credit")));
		businessHistory.setFtyId(locationObj.getFtyId());
		businessHistory.setLocationId(locationObj.getLocationId());
		businessHistory.setMatId(UtilObject.getIntOrZero(data.get("mat_id")));
		businessHistory.setModifyTime(new Date());
		businessHistory.setCreateUser(UtilObject.getStringOrEmpty(data.get("create_user")));
		businessHistory.setModifyUser(UtilObject.getStringOrEmpty(data.get("modify_user")));
		businessHistory.setPackageId(0);
		businessHistory.setPalletId(0);
		businessHistory.setPositionId(positionId);
		businessHistory.setQty(UtilObject.getBigDecimalOrZero(data.get("qty")));
		businessHistory.setReceiptType(UtilObject.getByteOrNull(data.get("refer_receipt_type")));
		businessHistory.setReferReceiptCode(UtilObject.getStringOrEmpty(data.get("refer_receipt_code")));
		businessHistory.setReferReceiptId(UtilObject.getIntOrZero(data.get("refer_receipt_id")));
		businessHistory.setReferReceiptRid(UtilObject.getIntOrZero(data.get("refer_receipt_rid")));
		businessHistory.setReferReceiptPid(whId);
		businessHistory.setSynMes(Boolean.TRUE);
		businessHistory.setSynSap(Boolean.TRUE);
		this.businessHistoryDao.insertSelective(businessHistory);
	}

	/**
	 * 销售数量统计
	 */
	@Override
	public Map<String, Object> selectChartData(String startDate, String endDate) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<String> dateList = this.getDateList(startDate, endDate);
		param.put("dateList", dateList);
		List<Map<String, Object>> data = outPutHeadDao.selectChartData(param);
		List<Map<String, Object>> dataCopy = new ArrayList<Map<String, Object>>();
		dataCopy.addAll(data);
		if (dataCopy != null && data != null && !dataCopy.isEmpty() && !data.isEmpty()) {
			List<Map<String, Object>> lineList = this.selectChartDataline(param);
			List<String> keys = this.getDateProductLineList(lineList, dateList);
			List<String> dbkeys = new ArrayList<String>();
			for (Map<String, Object> map : data) {
				String createDate = UtilObject.getStringOrEmpty(map.get("create_time"));
				Integer productLineId = UtilObject.getIntOrZero(map.get("product_line_id"));
				String key = createDate + "#" + productLineId;
				dbkeys.add(key);
			}
			for (String line : keys) {
				if (!(dbkeys).contains(line)) {
					Map<String, Object> jdata = new HashMap<String, Object>();
					jdata.put("product_line_id", UtilObject.getIntOrZero(line.split("#")[1]));
					jdata.put("qty", 0);
					jdata.put("create_time", UtilObject.getStringOrEmpty(line.split("#")[0]));
					dataCopy.add(jdata);
				}
			}
			Map<String, List<Map<String, Object>>> dataList = new HashMap<String, List<Map<String, Object>>>();
			for (Map<String, Object> map : dataCopy) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				String key = UtilObject.getStringOrEmpty(map.get("create_time"));
				if (dataList.containsKey(key)) {
					list = dataList.get(key);
					list.add(map);
					dataList.put(key, list);
				} else {
					list.add(map);
					dataList.put(key, list);
				}
			}
			List<Map<String, Object>> chartInfoList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : lineList) {
				Map<String, Object> smap = new HashMap<String, Object>();
				smap.put("name", map.get("product_line_name"));
				smap.put("dependence", 0);
				smap.put("type", 1);
				chartInfoList.add(smap);
			}
			result.put("chart_info_list", chartInfoList);
			List<Map<String, Object>> xAxisList = new ArrayList<Map<String, Object>>();
			for (String date : dateList) {
				Map<String, Object> map = new HashMap<String, Object>();
				List<Map<String, Object>> listmap = dataList.get(date);
				if (listmap != null && !listmap.isEmpty()) {
					List<Map<String, Object>> yvalue = new ArrayList<Map<String, Object>>();
					for (Map<String, Object> datamap : listmap) {
						Map<String, Object> value = new HashMap<String, Object>();
						value.put("y_axis_id", datamap.get("product_line_id"));
						value.put("y_axis_value", datamap.get("qty"));
						yvalue.add(value);
					}
					// 进行排序
					Collections.sort(yvalue, new MapComparatorByidAsc());

					map.put("x_axis_name", date);
					map.put("y_axis_value_list", yvalue);
					xAxisList.add(map);
				}
			}
			result.put("x_axis_list", xAxisList);
			result.put("left_axis_unit", "");
			result.put("right_axis_unit", "");
			BigDecimal left_axis_max_value = this.selectChartYmax(param);
			result.put("left_axis_max_value", left_axis_max_value == null ? "" : left_axis_max_value);
		} else {
			List<Map<String, Object>> xAxisList = new ArrayList<Map<String, Object>>();
			for (String date : dateList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x_axis_name", date);
				map.put("y_axis_value_list", data);
				xAxisList.add(map);
			}
			result.put("x_axis_list", xAxisList);
			result.put("chart_info_list", data);
			result.put("left_axis_max_value", "");
			result.put("left_axis_unit", "");
			result.put("right_axis_unit", "");
		}
		return result;
	}

	/**
	 * 根据时间产品线拼接key
	 * 
	 * @param lineList
	 * @param dateList
	 * @return
	 * @throws Exception
	 */
	private List<String> getDateProductLineList(List<Map<String, Object>> lineList, List<String> dateList)
			throws Exception {
		List<String> set = new ArrayList<String>();
		for (String date : dateList) {
			for (Map<String, Object> map : lineList) {
				String key = date + "#" + map.get("product_line_id");
				set.add(key);
			}
		}
		return set;
	}

	/**
	 * 获取日期集合
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	private List<String> getDateList(String startDate, String endDate) throws Exception {
		List<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar min = Calendar.getInstance();
		Calendar max = Calendar.getInstance();
		min.setTime(sdf.parse(startDate));
		min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
		max.setTime(sdf.parse(endDate));
		max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
		Calendar curr = min;
		while (curr.before(max)) {
			result.add(sdf.format(curr.getTime()));
			curr.add(Calendar.MONTH, 1);
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> selectChartDataline(Map<String, Object> param) {
		return outPutHeadDao.selectChartDataline(param);
	}

	/**
	 * 销售数量统计excel导出数据
	 */
	@Override
	public List<Map<String, Object>> selectSaleDataListForExcel(Map<String, Object> param) {
		return outPutHeadDao.selectSaleDataListForExcel(param);
	}

	@Override
	public BigDecimal selectChartYmax(Map<String, Object> param) {
		return outPutHeadDao.selectChartYmax(param);
	}

	/**
	 * 保存mes出库
	 */
	@Override
	public void saveOutPutToMes(Integer stockOutputId, String userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		try {
			
			BizStockOutputHeadVo head = this.getOrderView(stockOutputId);
			//同步判断
			if(head.getSynType() == EnumSynType.MES_SAP_SYN.getValue()
					|| head.getSynType() == EnumSynType.MES_SYN.getValue()) {
				param.put("refer_receipt_id", head.getStockOutputId());
				param.put("refer_receipt_code", head.getStockOutputCode());
				param.put("refer_receipt_type", head.getReceiptType());
				param.put("business_type", "202");
				WmIopRetVal val = this.saveMesOutPut(head, "202");
				if (val != null && val.getValue() != null && !val.getValue().equals("")) {
					param = new HashMap<String, Object>();
					param.put("stock_output_id", stockOutputId);
					param.put("mes_doc_code", UtilObject.getStringOrEmpty(val.getValue()));
					// 保存mes过账物料凭证
					this.outPutItemDao.updateByOutPutId(param);
				} else {
					param.put("error_info", val.getErrmsg());
					this.saveFailMes(param, userId);
				}
			}
		} catch (Exception e) {
			param.put("error_info", e.getMessage());
			e.printStackTrace();
			try {
				this.saveFailMes(param, userId);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 保存mes转储
	 */
	@Override
	public void saveTranSportToMes(Integer stockOutputId, String userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		try {
			BizStockOutputHeadVo head = this.getOrderView(stockOutputId);
			if(head.getSynType() == EnumSynType.MES_SAP_SYN.getValue()
					|| head.getSynType() == EnumSynType.MES_SYN.getValue()) {
				param.put("refer_receipt_id", head.getStockOutputId());
				param.put("refer_receipt_code", head.getStockOutputCode());
				param.put("refer_receipt_type", head.getReceiptType());
				param.put("business_type", "301");
				WmIopRetVal val = this.saveMesTranSport(head, "301");
				if (val != null && val.getValue() != null && !val.getValue().equals("")) {
					param = new HashMap<String, Object>();
					param.put("stock_output_id", head.getStockOutputId());
					param.put("mes_doc_code", UtilObject.getStringOrEmpty(val.getValue()));
					// 保存mes过账物料凭证
					this.outPutPositionDao.saveMatDocInfo(param);
				} else {
					param.put("error_info", val.getErrmsg());
					this.saveFailMes(param, userId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			param.put("error_info", e.getMessage());
			try {
				this.saveFailMes(param, userId);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 保存mes同步失败记录
	 * 
	 * @param param
	 * @param userId
	 */
	private void saveFailMes(Map<String, Object> param, String userId) throws Exception {
		BizFailMes mes = new BizFailMes();
		mes.setReferReceiptId(UtilObject.getIntOrZero(param.get("refer_receipt_id")));
		mes.setReferReceiptCode(UtilObject.getStringOrEmpty(param.get("refer_receipt_code")));
		mes.setReferReceiptType(UtilObject.getByteOrNull(param.get("refer_receipt_type")));
		mes.setBusinessType(UtilObject.getStringOrEmpty(param.get("business_type")));
		mes.setStatus(EnumReceiptStatus.RECEIPT_UNFINISH.getValue());
		mes.setCreateTime(new Date());
		mes.setModifyTime(new Date());
		mes.setCreateUser(userId);
		mes.setModifyUser(userId);
		mes.setErrorInfo(UtilObject.getStringOrEmpty(param.get("error_info")));
		failMesDao.insert(mes);
	}
	
	/**
	 * 自动完成作业请求
	 * @param ids
	 * @throws Exception
	 */
	private void addStockTaskByReq(List<Integer> ids) throws Exception {
		for (Integer id : ids) {
			taskService.transportByProduct(id);
		}
	}
	
	/**
	 * 查询司机 车辆
	 */
	@Override
	public Map<String, Object> selectDriverInfo(String userId) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("delivery_vehicle_list", this.commonService.selectVehicleByProductId(userId));
		result.put("delivery_driver_list", this.commonService.selectDriverByProductlineId(userId));
		return result;
	}
	

	public String testWriteOff(String code) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("referReceiptCode", code);
		BizStockOutputHead head = this.outPutHeadDao.selectByPrimaryKey(param);
		DTCWWMSDOREVERSEREQ mtCWWMSDOREVERSERE = new DTCWWMSDOREVERSEREQ();

		/***************************************************************
		 * IS_MSG_HEAD
		 **************************************************************/
		MSGHEAD iSMSGHEAD = new MSGHEAD();
		iSMSGHEAD.setSYSTEMID("WMS");
		String date = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		iSMSGHEAD.setGUID(head.getStockOutputCode() + date);
		mtCWWMSDOREVERSERE.setISMSGHEAD(iSMSGHEAD);

		/***************************************************************
		 * IT_DATA
		 **************************************************************/
		List<ITDATA> IT_DATA = new ArrayList<ITDATA>();
		ITDATA iTDATA = new ITDATA();
		iTDATA.setVBELN(head.getReferReceiptCode());
		iTDATA.setIBUDAT(head.getPostingDate());
		IT_DATA.add(iTDATA);
		mtCWWMSDOREVERSERE.setItdata(IT_DATA);
		ErpReturn erpReturn = CwErpWsImpl.outputWriteOff(mtCWWMSDOREVERSERE);
		if ("S".equals(erpReturn.getType())) {
			this.updateOutPutHead(head.getStockOutputId(), "admin", EnumReceiptStatus.RECEIPT_WRITEOFF.getValue());
		}
		return erpReturn.getMessage();
	}
	
	/**
	 * 再次同步mes
	 * 
	 */
	@Override
	public JSONObject saveMesAgain(Integer stockOutputId, String businessType, String userId) throws Exception {
		JSONObject result = new JSONObject();
		BizStockOutputHeadVo head = this.getOrderView(stockOutputId);
		head.setClassTypeId(this.commonService.selectNowClassType());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("refer_receipt_id", head.getStockOutputId());
		param.put("refer_receipt_code", head.getStockOutputCode());
		param.put("refer_receipt_type", head.getReceiptType());
		param.put("business_type", businessType);
		if ("202".equals(businessType)) {
			List<BizStockOutputItemVo> itemVos = head.getItemList();
			if(UtilObject.getStringOrNull(itemVos.get(0).getMesDocCode())!=null) {
				String mesWriteOffCode = this.mesWriteOff(head.getStockOutputId(),userId);
				result.put("stock_output_id", stockOutputId);
				result.put("mes_write_off_code", mesWriteOffCode);
			}else if(head.getStatus() != EnumReceiptStatus.RECEIPT_WRITEOFF.getValue()){
				WmIopRetVal val = this.saveMesOutPut(head, businessType);
				if (val != null && val.getValue() != null && !val.getValue().equals("")) {
					param = new HashMap<String, Object>();
					param.put("stock_output_id", stockOutputId);
					param.put("mes_doc_code", UtilObject.getStringOrEmpty(val.getValue()));
					// 保存mes过账物料凭证
					this.outPutItemDao.updateByOutPutId(param);
					result.put("mes_doc_code", val.getValue());
					result.put("msg", "");
				} else {
					param.put("error_info", val.getErrmsg());
					this.saveFailMes(param, userId);
					result.put("msg", val.getErrmsg());
				}
			}else {
				throw new WMSException("MES还未过账无法冲销");
			}
		} else if ("301".equals(businessType)) {
			WmIopRetVal val = this.saveMesTranSport(head, businessType);
			if (val != null && val.getValue() != null && !val.getValue().equals("")) {
				param = new HashMap<String, Object>();
				param.put("stock_output_id", stockOutputId);
				param.put("mes_doc_code", UtilObject.getStringOrEmpty(val.getValue()));
				// 保存mes过账物料凭证
				this.outPutPositionDao.saveMatDocInfo(param);
				result.put("mes_doc_code_transport", val.getValue());
				result.put("msg", "");
			} else {
				param.put("error_info", val.getErrmsg());
				this.saveFailMes(param, userId);
				result.put("msg", val.getErrmsg());
			}
		}
		return result;
	}

	/**
	 * 根据产品线排序
	 * 
	 * @author sw
	 *
	 */
	private static class MapComparatorByidAsc implements Comparator<Map<String, Object>> {

		@Override
		public int compare(Map<String, Object> o1, Map<String, Object> o2) {

			Integer s1 = UtilObject.getIntOrZero(o1.get("y_axis_id"));
			Integer s2 = UtilObject.getIntOrZero(o2.get("y_axis_id"));

			return s1.compareTo(s2);
		}

	}
	
	/**
	 * 批量修改出库单状态
	 */
	@Override
	public int updateOutPutHead(List<Integer> stockOutputIds, String userId, Byte status) throws Exception {
		for (Integer stockOutputId : stockOutputIds) {
			this.updateOutPutHead(stockOutputId, userId, status);
		}
		return 1;
	}
	
	/**
	 * mes冲销
	 */
	@Override
	public String mesWriteOff(Integer stockOutputId, String userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		String result = null;
		BizStockOutputHeadVo head;
		try {
			head = this.getOrderView(stockOutputId);
			if (head.getSynType() == EnumSynType.MES_SAP_SYN.getValue()
					|| head.getSynType() == EnumSynType.MES_SYN.getValue()) {
				try {
					param.put("refer_receipt_id", head.getStockOutputId());
					param.put("refer_receipt_code", head.getStockOutputCode());
					param.put("refer_receipt_type", head.getReceiptType());
					param.put("business_type", "202");
					List<BizStockOutputItemVo> itemVos = head.getItemList();
					if(itemVos.get(0).getMesDocCode()!=null && !itemVos.get(0).getMesDocCode().equals("")) {
						result =UtilObject.getStringOrNull(CwMesWsImpl.CancelBookedBillByBillCode(itemVos.get(0).getMesDocCode()));
						if (result != null && result.length() == 16) {
							param = new HashMap<String, Object>();
							param.put("stock_output_id", head.getStockOutputId());
							param.put("mes_write_off_code",result);
							// 保存mes过账物料凭证
							this.outPutItemDao.updateByOutPutId(param);
						} else {
							param.put("error_info", result);
							this.saveFailMes(param, userId);
						}
					}
				} catch (Exception e) {
					param.put("error_info", e.getMessage());
					e.printStackTrace();
					try {
						this.saveFailMes(param, userId);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 校验出库库存
	 * @param head
	 * @throws Exception
	 */
    private void checkQtyByOutPut(BizStockOutputHeadVo head)throws Exception{
    	if(head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_STRAIGTH.getValue()) {
    		//转储
    		if(head.getStatus() == EnumReceiptStatus.RECEIPT_UNFINISH.getValue()) {
    			List<BizStockOutputItemVo> itemVos = head.getItemList();
    			List<BigDecimal> qtyList = new ArrayList<BigDecimal>();
    			List<String> uniquePositionList = new ArrayList<String>();
       			List<String> uniqueBatchList = new ArrayList<String>();
    			for(BizStockOutputItemVo itemVo : itemVos) {
    				List<BizStockOutputPositionVo> positionVos = itemVo.getPositionList();
    				for(BizStockOutputPositionVo positionVo : positionVos) {
    					String uniquePosition = positionVo.getPositionId()+"-"+positionVo.getMatId()+"-"+
    							positionVo.getPalletId()+"-"+positionVo.getLocationId()+"-"+positionVo.getBatch()+"-"+"1"
    							+"-"+positionVo.getStockTypeId()+"-"+positionVo.getPackageId();
    					String uniqueBatch = positionVo.getMatId()+"-"+positionVo.getLocationId()+"-"+"1"+
    					"-"+positionVo.getBatch()+"-"+positionVo.getStockTypeId();
    					uniqueBatchList.add(uniqueBatch);
    					uniquePositionList.add(uniquePosition);
    					qtyList.add(positionVo.getStockQty());
    				}
    			}
    			//仓位库存校验
    			List<StockPosition> stockPositions = stockPositionDao.selectByUniqueList(uniquePositionList);
    			Map<String,StockPosition> data = new HashMap<String,StockPosition>();
    			if(stockPositions!=null&&!stockPositions.isEmpty()) {
    				for(StockPosition postion : stockPositions) {
    					String unique = postion.getPositionId()+"-"+postion.getMatId()+"-"+
    							postion.getPalletId()+"-"+postion.getLocationId()+"-"+postion.getBatch()+"-"+"1"
    							+"-"+postion.getStockTypeId()+"-"+postion.getPackageId();
    					data.put(unique, postion);
    				}
    			}else {
    				throw new WMSException("仓位库存不足");
    			}
    			for(int i=0;i<uniquePositionList.size();i++) {
    				StockPosition stock = data.get(uniquePositionList.get(i));
    				if(stock != null) {
    					if(stock.getQty().compareTo(qtyList.get(i))<0) {
        					throw new WMSException("仓位库存不足");
        				}
    				}else {
    					throw new WMSException("仓位库存不足");
    				}
    			}
    			//批次库存校验
    			List<StockBatch> stockBatchs = stockBatchDao.selectByUniqueList(uniqueBatchList);
    			Map<String,StockBatch> data1 = new HashMap<String,StockBatch>();
    			if(stockPositions!=null&&!stockPositions.isEmpty()) {
    				for(StockBatch batch : stockBatchs) {
    					String unique = batch.getMatId()+"-"+batch.getLocationId()+"-"+"1"+
    	    					"-"+batch.getBatch()+"-"+batch.getStockTypeId();
    					data1.put(unique, batch);
    				}
    			}else {
    				throw new WMSException("批次库存不足");
    			}
    			for(int i=0;i<uniqueBatchList.size();i++) {
    				StockBatch batch = data1.get(uniqueBatchList.get(i));
    				if(batch != null) {
    					if(batch.getQty().compareTo(qtyList.get(i))<0) {
        					throw new WMSException("批次库存不足");
        				}
    				}else {
    					throw new WMSException("批次库存不足");
    				}
    			}
    		//出库	
    		}else if(head.getStatus() == EnumReceiptStatus.RECEIPT_TRANSPORT.getValue()) {
    			List<BizStockOutputItemVo> itemVos = head.getItemList();
    			List<BigDecimal> qtyList = new ArrayList<BigDecimal>();
    			List<String> uniquePositionList = new ArrayList<String>();
       			List<String> uniqueBatchList = new ArrayList<String>();
    			for(BizStockOutputItemVo itemVo : itemVos) {
    				List<BizStockOutputPositionVo> positionVos = itemVo.getPositionList();
    				for(BizStockOutputPositionVo positionVo : positionVos) {
    					Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
    					DicStockLocationVo locationObj = locationMap.get(itemVo.getLocationId());
    					Integer whId = locationObj.getWhId();
    					String whCode = dictionaryService.getWhCodeByWhId(whId);
    					String areaCode = UtilProperties.getInstance().getDefaultCCQ();
    					String positionCode = UtilProperties.getInstance().getDefaultCW();
    					Integer positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode, areaCode,
    							positionCode);
    					String unique = positionId+"-"+itemVo.getMatId()+"-"+
    							positionVo.getPalletId()+"-"+itemVo.getLocationId()+"-"+positionVo.getBatch()+"-"+"1"
    							+"-"+positionVo.getStockTypeId()+"-"+positionVo.getPackageId();
    					String uniqueBatch = itemVo.getMatId()+"-"+itemVo.getLocationId()+"-"+"1"+
    	    					"-"+positionVo.getBatch()+"-"+positionVo.getStockTypeId();
    					uniqueBatchList.add(uniqueBatch);
    					uniquePositionList.add(unique);
    					qtyList.add(positionVo.getStockQty());
    				}
    			}
    			//仓位库存校验
    			List<StockPosition> stockPositions = stockPositionDao.selectByUniqueList(uniquePositionList);
    			Map<String,StockPosition> data = new HashMap<String,StockPosition>();
    			if(stockPositions!=null&&!stockPositions.isEmpty()) {
    				for(StockPosition postion : stockPositions) {
    					String unique = postion.getPositionId()+"-"+postion.getMatId()+"-"+
    							postion.getPalletId()+"-"+postion.getLocationId()+"-"+postion.getBatch()+"-"+"1"
    							+"-"+postion.getStockTypeId()+"-"+postion.getPackageId();
    					data.put(unique, postion);
    				}
    			}
    			for(int i=0;i<uniquePositionList.size();i++) {
    				StockPosition stock = data.get(uniquePositionList.get(i));
    				if(stock != null) {
    					if(stock.getQty().compareTo(qtyList.get(i))<0) {
        					throw new WMSException("仓位库存不足");
        				}
    				}else {
    					throw new WMSException("仓位库存不足");
    				}
    			}
    			//批次库存校验
    			List<StockBatch> stockBatchs = stockBatchDao.selectByUniqueList(uniqueBatchList);
    			Map<String,StockBatch> data1 = new HashMap<String,StockBatch>();
    			if(stockPositions!=null&&!stockPositions.isEmpty()) {
    				for(StockBatch batch : stockBatchs) {
    					String unique = batch.getMatId()+"-"+batch.getLocationId()+"-"+"1"+
    	    					"-"+batch.getBatch()+"-"+batch.getStockTypeId();
    					data1.put(unique, batch);
    				}
    			}else {
    				throw new WMSException("批次库存不足");
    			}
    			for(int i=0;i<uniqueBatchList.size();i++) {
    				StockBatch batch = data1.get(uniqueBatchList.get(i));
    				if(batch != null) {
    					if(batch.getQty().compareTo(qtyList.get(i))<0) {
        					throw new WMSException("批次库存不足");
        				}
    				}else {
    					throw new WMSException("批次库存不足");
    				}
    			}
    		}
    	}
    }

	@Override
	public List<DicMoveType> selectMoveTypeList(Byte type) throws Exception{
		return commonService.getMoveTypeByReceiptType(type);
	}

	@Override
	public String udateWmsAndSapData(Integer stockOutputId, String postingDate, String userId) throws Exception {
		String msg = "成功";
		BizStockOutputHeadVo head = this.getOrderView(stockOutputId);
		Map<String, Object> sapReturn = new HashMap<String,Object>();
		if(head != null) {
			// 销售出库
			if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) {
				//获取配货单信息
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("referReceiptCode", head.getReferReceiptCode());
				List<Byte> list = new ArrayList<Byte>();
				list.add(EnumReceiptStatus.RECEIPT_WORK.getValue());
				param.put("status", list);
				param.put("beforeOrderId",head.getStockOutputId());
				List<Map<String, Object>> orders = allocateCargoHeadDao.selectOrderCodeAndId(param);
				//遍历配货单
				for (Map<String, Object> map : orders) {
					// 仓位库存
					this.updateStockPosition(UtilObject.getIntOrZero(map.get("referReceiptId")),
							UtilObject.getStringOrEmpty(map.get("referReceiptCode")),
							UtilObject.getByteOrNull(map.get("referReceiptType")));
					map.put("status", EnumReceiptStatus.RECEIPT_FINISH.getValue());
					// 下架作业单状态
					this.updateTaskStatus(map);
				}
				// 批次库存
				this.updateStockBatch(head, userId, EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				
				// 单据状态 已完成
				this.updateOutPutHead(head.getStockOutputId(), userId,EnumReceiptStatus.RECEIPT_FINISH.getValue());
				// 配货单 已完成
				param = new HashMap<String, Object>();
				param.put("beforeOrderId", head.getStockOutputId());
				param.put("beforeOrderCode", head.getStockOutputCode());
				param.put("beforeOrderType", head.getReceiptType());
				param.put("status", EnumReceiptStatus.RECEIPT_FINISH.getValue());
				allocateCargoHeadDao.updateOrderBySale(param);
				
				//判断是否同步sap
				if(head.getSynType() == EnumSynType.MES_SAP_SYN.getValue()
						|| head.getSynType() == EnumSynType.SAP_SYN.getValue()) {
					// sap过账
					sapReturn = this.saveOrderToSap(stockOutputId, postingDate, userId);
				}
				// 报废出库
			} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()) {
				
				// 下架作业单状态
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("referReceiptId", head.getStockOutputId());
				map.put("referReceiptType",head.getReceiptType());
				map.put("status", EnumReceiptStatus.RECEIPT_FINISH.getValue());
				this.updateTaskStatus(map);
				// 仓位库存
				this.updateStockPosition(head.getStockOutputId(), head.getStockOutputCode(), head.getReceiptType());
				// 批次库存
				this.updateStockBatch(head, userId, EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				// 单据状态 已完成
				this.updateOutPutHead(head.getStockOutputId(), userId,EnumReceiptStatus.RECEIPT_FINISH.getValue());
				//判断是否同步sap
				if(head.getSynType() == EnumSynType.MES_SAP_SYN.getValue()
						|| head.getSynType() == EnumSynType.SAP_SYN.getValue()) {
					// sap过账
					sapReturn = this.saveOrderToSap(head.getStockOutputId(), postingDate, userId);
				}
				// 直发出库
			} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_STRAIGTH.getValue()) {
			
				//转储
				if(head.getStatus() == EnumReceiptStatus.RECEIPT_UNFINISH.getValue()) {
					
					// 出库单明细list
					List<BizStockOutputItemVo> itemList = head.getItemList();
					for (BizStockOutputItemVo item : itemList) {

						List<BizStockOutputPositionVo> positionList = item.getPositionList(); // 出库单行明细list

						for (BizStockOutputPositionVo position : positionList) {

							// 修改接收工厂批次库存
							Map<String, Object> param = new HashMap<String, Object>();
							param.put("qty", position.getStockQty());// 需要下架的总数量
							param.put("batch", position.getBatch());// 批次
							param.put("locationId", item.getLocationId());// 库存id
							param.put("matId", item.getMatId());// 物料id
							param.put("createUserId", userId);
							param.put("debitCredit", EnumDebitCredit.DEBIT_S_ADD.getName());
							param.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
							param.put("stockTypeId", position.getStockTypeId());
							commonService.modifyStockBatch(param);

							// 验证批次信息
							BatchMaterial material = new BatchMaterial();
							material.setBatch(position.getBatch());
							material.setMatId(item.getMatId());
							material.setFtyId(position.getFtyId());
							BatchMaterial find = batchMaterialDao.selectByUniqueKey(material);
							if (find == null) {
								throw new WMSException("无生产批次信息");
							}
							// 添加批次信息
							BatchMaterial batchMaterial = new BatchMaterial();
							batchMaterial.setBatch(position.getBatch());
							batchMaterial.setMatId(item.getMatId());
							batchMaterial.setFtyId(item.getFtyId());
							batchMaterial.setCreateUser(userId);
							batchMaterial.setPackageTypeId(position.getPackageTypeId());
							batchMaterial.setErpBatch(position.getErpBatch());
							batchMaterial.setQualityBatch(position.getQualityBatch());
							batchMaterial.setProductionBatch(position.getProductBatch());
							batchMaterial.setPurchaseOrderCode(find.getPurchaseOrderCode());
							batchMaterial.setProductLineId(find.getProductLineId());
							batchMaterial.setInstallationId(find.getInstallationId());
							batchMaterial.setClassTypeId(head.getClassTypeId());
							batchMaterialDao.insertSelective(batchMaterial);

							// 添加历史记录
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("debit_credit", EnumDebitCredit.DEBIT_S_ADD.getName());
							map.put("business_type", (byte) 1);
							map.put("qty", position.getStockQty());
							map.put("batch", position.getBatch());
							map.put("location_id", item.getLocationId());
							map.put("mat_id", item.getMatId());
							map.put("create_user", userId);
							map.put("modify_user", userId);
							map.put("refer_receipt_id", head.getStockOutputId());
							map.put("refer_receipt_code", head.getStockOutputCode());
							map.put("refer_receipt_rid", item.getStockOutputRid());
							map.put("refer_receipt_type", head.getReceiptType());
							map.put("erp_batch", position.getErpBatch());
							map.put("package_type_id", position.getPackageTypeId());
							this.insertBusinessHistory(map);

							Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
							DicStockLocationVo locationObj = locationMap.get(item.getLocationId());
							Integer whId = locationObj.getWhId();
							String whCode = dictionaryService.getWhCodeByWhId(whId);
							String areaCode = UtilProperties.getInstance().getDefaultCCQ();
							Integer areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);
							String positionCode = UtilProperties.getInstance().getDefaultCW();
							Integer positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode, areaCode,
									positionCode);
							// 修改接收工厂仓位库存
							StockPosition stockPosition = new StockPosition();
							stockPosition.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
							stockPosition.setWhId(whId);
							stockPosition.setAreaId(areaId);
							stockPosition.setPositionId(positionId);
							stockPosition.setBatch(position.getBatch());
							stockPosition.setMatId(item.getMatId());
							stockPosition.setFtyId(item.getFtyId());
							stockPosition.setLocationId(item.getLocationId());
							stockPosition.setInputDate(new Date());
							stockPosition.setPackageTypeId(position.getPackageTypeId());
							stockPosition.setQty(position.getStockQty());
							stockPosition.setUnitId(position.getUnitId());
							stockPosition.setPackageId(position.getPackageId());
							stockPosition.setPalletId(position.getPalletId());
							stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
							stockPosition.setStockTypeId(position.getStockTypeId());
							commonService.modifyStockPosition(stockPosition);

							// 修改发出工厂批次库存
							param = new HashMap<String, Object>();
							param.put("qty", position.getStockQty());// 需要下架的总数量
							param.put("batch", position.getBatch());// 批次
							param.put("locationId", position.getLocationId());// 库存id
							param.put("matId", position.getMatId());// 物料id
							param.put("createUserId", userId);
							param.put("debitCredit", EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
							param.put("status", EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
							param.put("stockTypeId", position.getStockTypeId());
							commonService.modifyStockBatch(param);

							map = new HashMap<String, Object>();
							map.put("debit_credit", EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
							map.put("business_type", (byte) 2);
							map.put("qty", position.getStockQty());
							map.put("batch", position.getBatch());
							map.put("location_id", item.getLocationId());
							map.put("mat_id", item.getMatId());
							map.put("create_user", userId);
							map.put("modify_user", userId);
							map.put("refer_receipt_id", head.getStockOutputId());
							map.put("refer_receipt_code", head.getStockOutputCode());
							map.put("refer_receipt_rid", item.getStockOutputRid());
							map.put("refer_receipt_type", head.getReceiptType());
							map.put("erp_batch", position.getErpBatch());
							map.put("package_type_id", position.getPackageTypeId());
							this.insertBusinessHistory(map);
							// 修改发出工厂仓位库存
							stockPosition = new StockPosition();
							stockPosition.setWhId(position.getWhId());
							stockPosition.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
							stockPosition.setAreaId(position.getAreaId());
							stockPosition.setPositionId(position.getPositionId());
							stockPosition.setMatId(position.getMatId());
							stockPosition.setFtyId(position.getFtyId());
							stockPosition.setLocationId(position.getLocationId());
							stockPosition.setBatch(position.getBatch());
							stockPosition.setInputDate(new Date());
							stockPosition.setQty(position.getStockQty());
							stockPosition.setUnitId(position.getUnitId());
							stockPosition.setPackageId(position.getPackageId());
							stockPosition.setPalletId(position.getPalletId());
							stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
							stockPosition.setStockTypeId(position.getStockTypeId());
							commonService.modifyStockPosition(stockPosition);
						}
					}
					// 单据状态 已转储
					this.updateOutPutHead(head.getStockOutputId(), userId,EnumReceiptStatus.RECEIPT_TRANSPORT.getValue());
					
					//判断是否同步sap
					if(head.getSynType() == EnumSynType.MES_SAP_SYN.getValue()
							|| head.getSynType() == EnumSynType.SAP_SYN.getValue()) {
						//sap转储过账
						Map<String, Object> sapTran = this.saveSapTranSport(head.getStockOutputId(), postingDate, userId);
						//过账成功后插入物料凭证
						if("S".equals(UtilObject.getStringOrEmpty(sapTran.get("type")))) {
							// 保存物料转储过账凭证
							this.outPutPositionDao.saveMatDocInfo(sapTran);
							// 保存过账日期 凭证日期
							this.outPutHeadDao.insertPostingDateAndDocTime(sapTran);
						} else {
							msg = sapTran.get("message").toString();
							throw new WMSException(msg);
						}
						return msg;
					}
					
					//销售
				} else if(head.getStatus() == EnumReceiptStatus.RECEIPT_TRANSPORT.getValue()){
					
					// 修改库存
					this.updateStock(head, userId, EnumDebitCredit.CREDIT_H_SUBTRACT.getName(),EnumBoolean.FALSE.getValue());
					// 单据状态 已完成
					this.updateOutPutHead(stockOutputId, userId,EnumReceiptStatus.RECEIPT_FINISH.getValue());
					//判断是否同步sap
					if(head.getSynType() == EnumSynType.MES_SAP_SYN.getValue()
							|| head.getSynType() == EnumSynType.SAP_SYN.getValue()) {
						// sap出库过账
						sapReturn = this.saveOrderToSap(head.getStockOutputId(), postingDate, userId);
					}
				}
				
				// 紧急记账销售出库
			} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue()) {
				
				// 单据状态 待出库
				this.updateOutPutHead(stockOutputId, userId,EnumReceiptStatus.RECEIPT_BEOUTPUT.getValue());
				
				//判断是否同步sap
				if(head.getSynType() == EnumSynType.MES_SAP_SYN.getValue()
						|| head.getSynType() == EnumSynType.SAP_SYN.getValue()) {
					// sap过账
					sapReturn = this.saveOrderToSap(head.getStockOutputId(), postingDate, userId);
				}
				
				// 紧急作业销售出库(紧急库存)
			} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_FALSE.getValue()) {
				
				// 修改库存
				this.updateStockOnUrgent(head, userId, EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				// 单据状态 已完成
				this.updateOutPutHead(stockOutputId, userId,EnumReceiptStatus.RECEIPT_FINISH.getValue());
				//判断是否同步sap
				if(head.getSynType() == EnumSynType.MES_SAP_SYN.getValue()
						|| head.getSynType() == EnumSynType.SAP_SYN.getValue()) {
					// sap过账
					sapReturn = this.saveOrderToSap(head.getStockOutputId(), postingDate, userId);
				}
				// 紧急作业销售出库(正常库存)
			} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_PRODUCTION_URGENT_TRUE.getValue()) {
				
				// 单据状态 已完成
				this.updateOutPutHead(stockOutputId, userId,EnumReceiptStatus.RECEIPT_FINISH.getValue());
				//判断是否同步sap
				if(head.getSynType() == EnumSynType.MES_SAP_SYN.getValue()
						|| head.getSynType() == EnumSynType.SAP_SYN.getValue()) {
					// sap过账
					sapReturn = this.saveOrderToSap(head.getStockOutputId(), postingDate, userId);
				}
				
			}
			
			if(sapReturn!=null && !sapReturn.isEmpty()) {
				//过账成功后插入物料凭证
				if("S".equals(UtilObject.getStringOrEmpty(sapReturn.get("type")))) {
					// 保存出库过账物料凭证
					this.outPutItemDao.updateByOutPutId(sapReturn);
					// 保存过账日期 凭证日期
					this.outPutHeadDao.insertPostingDateAndDocTime(sapReturn);
				} else {
					msg = sapReturn.get("message").toString();
					throw new WMSException(msg);
				}
			}
		} else {
			msg = "系统异常";
		}
		return msg;
	}

	@Override
	public String writeOffAndUpdate(Integer stockOutputId,String userId) throws Exception {
		String msg = "成功";
		BizStockOutputHeadVo head = this.getOrderView(stockOutputId);
		if(head != null) {
			//修改库存
			this.updateWriteOffToQty(head,userId);
			//判断是否同步sap
			if(head.getSynType() == EnumSynType.MES_SAP_SYN.getValue()
					|| head.getSynType() == EnumSynType.SAP_SYN.getValue()) {
				Map<String, Object> sapReturn = new HashMap<String, Object>();
				// 销售出库 紧急记账销售出库
				if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()
						|| head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_BOOK_URGENT.getValue()) {
					sapReturn = this.writeOffSale(head);
					// 报废出库
				} else if (head.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SCRAP.getValue()) {
					sapReturn = this.writeOffScrap(head);
				}
				sapReturn.put("stock_output_id", head.getStockOutputId());
				//冲销成功后插入物料凭证
				if("S".equals(UtilObject.getStringOrEmpty(sapReturn.get("type")))) {
					// 保存冲销物料凭证
					this.outPutItemDao.updateByOutPutId(sapReturn);
				} else {
					msg = sapReturn.get("message").toString();
					throw new WMSException(msg);
				}
			}
		}else {
			msg = "系统异常";
		}
		return msg;
	}
	
	/**
	 * 已售未提库存修改
	 * @param head
	 * @param itemVo
	 * @param createUserId
	 * @param debitCredit
	 * @throws Exception
	 */
	private void bookSaleUpdateQty(BizStockOutputHeadVo head,BizStockOutputItemVo itemVo,String createUserId,String debitCredit) throws Exception{
		Long batch = null;
		BatchMaterial record = new BatchMaterial();
		record.setErpBatch(itemVo.getErpBatch());
		record.setProductionBatch(head.getReceiveCode());
		record.setFtyId(itemVo.getFtyId());
		record.setMatId(itemVo.getMatId());
		record.setPackageTypeId(888888);
		BatchMaterial find = batchMaterialDao.selectByUniqueKey(record);
		//验证批次
		if(find != null) {
			batch = find.getBatch();
		} else {
			batch = commonService.getBatch();
			// 添加批次信息
			BatchMaterial batchMaterial = new BatchMaterial();
			batchMaterial.setBatch(batch);
			batchMaterial.setMatId(itemVo.getMatId());
			batchMaterial.setFtyId(itemVo.getFtyId());
			batchMaterial.setCreateUser(createUserId);
			batchMaterial.setPackageTypeId(888888);
			batchMaterial.setErpBatch(itemVo.getErpBatch());
			batchMaterial.setQualityBatch(head.getReceiveCode());
			batchMaterial.setProductionBatch(head.getReceiveCode());
			batchMaterial.setProductLineId(0);
			batchMaterial.setInstallationId(0);
			batchMaterial.setClassTypeId(0);
			batchMaterial.setSupplierName("");
			batchMaterial.setPurchaseOrderCode(head.getReferReceiptCode());
			batchMaterial.setPurchaseOrderRid(itemVo.getStockOutputRid().toString());
			batchMaterial.setSpecStockName(head.getReceiveName());
			batchMaterialDao.insertSelective(batchMaterial);
		}
		
		// 批次库存
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("qty", itemVo.getOutputQty());// 需要下架的总数量
		param.put("batch", batch);// 批次
		param.put("locationId", itemVo.getLocationId());// 库存id
		param.put("matId", itemVo.getMatId());// 物料id
		param.put("createUserId", createUserId);
		param.put("debitCredit", debitCredit);
		param.put("status", EnumStockStatus.STOCK_BATCH_STATUS_NO_MENTION.getValue());
		param.put("stockTypeId",EnumStockType.STOCK_TYPE_ERP.getValue());
		commonService.modifyStockBatchCanMinus(param);
		
		//仓位库存
		StockPosition position = new StockPosition();
		Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
		DicStockLocationVo locationObj = locationMap.get(itemVo.getLocationId());
		Integer whId = locationObj.getWhId();
		String whCode = dictionaryService.getWhCodeByWhId(whId);
		String areaCode = UtilProperties.getInstance().getDefaultCCQProduct();
		Integer areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);
		String positionCode = UtilProperties.getInstance().getDefaultCWProduct();
		Integer positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode, areaCode,
				positionCode);
		position.setDebitOrCredit(debitCredit);
		position.setWhId(whId);
		position.setAreaId(areaId);
		position.setPositionId(positionId);
		position.setBatch(batch);
		position.setMatId(itemVo.getMatId());
		position.setFtyId(itemVo.getFtyId());
		position.setLocationId(itemVo.getLocationId());
		position.setInputDate(new Date());
		position.setPackageTypeId(888888);
		position.setQty(itemVo.getOutputQty());
		position.setUnitId(itemVo.getUnitId());
		position.setPackageId(0);
		position.setPalletId(0);
		position.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_NO_MENTION.getValue());
		position.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
		commonService.modifyStockPosition(position);
	}
	

	public void test()throws Exception {
		Integer[] ids = {84,107,283,284,285,287,288,289,290,291,292,293,294,295,296};
		for(Integer id : ids) {
			BizStockOutputHeadVo head = this.getOrderView(id);
			List<BizStockOutputItemVo> itemList = head.getItemList();
			for(BizStockOutputItemVo itemVo : itemList) {
				bookSaleUpdateQty(head, itemVo, "admin", "H");
			}
		}
	}
	
	@Override
	public void test1() throws Exception{
		List<Map<String,Object>> data = outPutHeadDao.getTmpSaleList();
		if(data!=null && !data.isEmpty()) {
			for(Map<String,Object> map : data) {
				Long batch = null;
				BatchMaterial record = new BatchMaterial();
				record.setErpBatch(UtilObject.getStringOrEmpty(map.get("erp_batch")));
				record.setProductionBatch("00"+UtilObject.getStringOrEmpty(map.get("receive_code")));
				record.setFtyId(UtilObject.getIntOrZero(map.get("fty_id")));
				record.setMatId(UtilObject.getIntOrZero(map.get("mat_id")));
				record.setPackageTypeId(888888);
				BatchMaterial find = batchMaterialDao.selectByUniqueKey(record);
				//验证批次
				if(find != null) {
					batch = find.getBatch();
				} else {
					batch = commonService.getBatch();
					// 添加批次信息
					BatchMaterial batchMaterial = new BatchMaterial();
					batchMaterial.setBatch(batch);
					batchMaterial.setMatId(UtilObject.getIntOrZero(map.get("mat_id")));
					batchMaterial.setFtyId(UtilObject.getIntOrZero(map.get("fty_id")));
					batchMaterial.setCreateUser("admin");
					batchMaterial.setPackageTypeId(888888);
					batchMaterial.setErpBatch(UtilObject.getStringOrEmpty(map.get("erp_batch")));
					batchMaterial.setQualityBatch("00"+UtilObject.getStringOrEmpty(map.get("receive_code")));
					batchMaterial.setProductionBatch("00"+UtilObject.getStringOrEmpty(map.get("receive_code")));
					batchMaterial.setProductLineId(0);
					batchMaterial.setInstallationId(0);
					batchMaterial.setClassTypeId(0);
					batchMaterial.setSupplierName("");
					batchMaterial.setPurchaseOrderCode("");
					batchMaterial.setPurchaseOrderRid("");
					batchMaterial.setSpecStockName(UtilObject.getStringOrEmpty(map.get("receive_name")));
					batchMaterialDao.insertSelective(batchMaterial);
				}
				
				// 批次库存
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("qty", UtilObject.getBigDecimalOrZero(map.get("qty")));// 需要下架的总数量
				param.put("batch", batch);// 批次
				param.put("locationId", UtilObject.getIntOrZero(map.get("location_id")));// 库存id
				param.put("matId", UtilObject.getIntOrZero(map.get("mat_id")));// 物料id
				param.put("createUserId", "admin");
				param.put("debitCredit", EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				param.put("status", EnumStockStatus.STOCK_BATCH_STATUS_NO_MENTION.getValue());
				param.put("stockTypeId",EnumStockType.STOCK_TYPE_ERP.getValue());
				commonService.modifyStockBatchCanMinus(param);
				
				//仓位库存
				StockPosition position = new StockPosition();
				Map<Integer, DicStockLocationVo> locationMap = dictionaryService.getLocationIdMap();
				DicStockLocationVo locationObj = locationMap.get( UtilObject.getIntOrZero(map.get("location_id")));
				Integer whId = locationObj.getWhId();
				String whCode = dictionaryService.getWhCodeByWhId(whId);
				String areaCode = UtilProperties.getInstance().getDefaultCCQProduct();
				Integer areaId = dictionaryService.getAreaIdByWhCodeAndAreaCode(whCode, areaCode);
				String positionCode = UtilProperties.getInstance().getDefaultCWProduct();
				Integer positionId = dictionaryService.getPositionIdByWhCodeAreaCodePositionCode(whCode, areaCode,
						positionCode);
				position.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				position.setWhId(whId);
				position.setAreaId(areaId);
				position.setPositionId(positionId);
				position.setBatch(batch);
				position.setMatId( UtilObject.getIntOrZero(map.get("mat_id")));
				position.setFtyId(UtilObject.getIntOrZero(map.get("fty_id")));
				position.setLocationId(UtilObject.getIntOrZero(map.get("location_id")));
				position.setInputDate(new Date());
				position.setPackageTypeId(888888);
				position.setQty(UtilObject.getBigDecimalOrZero(map.get("qty")));
				position.setUnitId(UtilObject.getIntOrZero(map.get("unit_id")));
				position.setPackageId(0);
				position.setPalletId(0);
				position.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_NO_MENTION.getValue());
				position.setStockTypeId(EnumStockType.STOCK_TYPE_ERP.getValue());
				commonService.modifyStockPosition(position);
			}
		}
	}

	@Override
	public List<Map<String, Object>> selectCargoInfoOnPaging(Map<String, Object> param) {
		return outPutHeadDao.selectCargoInfoOnPaging(param);
	}
}
