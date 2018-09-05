package com.inossem.wms.service.intfc.sap;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.inossem.wms.constant.Const;
import com.inossem.wms.dao.biz.BizAllocateHeadMapper;
import com.inossem.wms.dao.biz.BizMatReqHeadMapper;
import com.inossem.wms.dao.biz.BizMatReqItemMapper;
import com.inossem.wms.dao.biz.BizMaterialDocHeadMapper;
import com.inossem.wms.dao.biz.BizMaterialDocItemMapper;
import com.inossem.wms.dao.biz.BizStockOutputHeadMapper;
import com.inossem.wms.dao.biz.BizStockOutputItemMapper;
import com.inossem.wms.dao.biz.BizStockOutputPositionMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.stock.StockPositionMapper;
import com.inossem.wms.dao.syn.SynMaterialDocHeadMapper;
import com.inossem.wms.dao.syn.SynMaterialDocItemMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizAllocateHead;
import com.inossem.wms.model.biz.BizMatReqHead;
import com.inossem.wms.model.biz.BizMatReqItem;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizMaterialDocItem;
import com.inossem.wms.model.biz.BizStockOutputHead;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.syn.SynMaterialDocHead;
import com.inossem.wms.model.vo.BizStockOutputItemVo;
import com.inossem.wms.model.vo.BizStockOutputPositionVo;
import com.inossem.wms.model.vo.DicFactoryVo;
import com.inossem.wms.model.vo.DicStockLocationVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.intfc.IStockOutput;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilREST;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 出库SAP接口
 * 
 * @author 高海涛
 * @date 2018年2月7日
 */
@Service("stockOutputImpl")
public class StockOutputImpl implements IStockOutput {

	@Resource
	private BizStockOutputHeadMapper outPutHeadDao;
	@Resource
	private BizAllocateHeadMapper allocateHeadDao;
	@Resource
	private BizStockOutputItemMapper outPutItemDao;
	@Resource
	private BizStockOutputPositionMapper outPutPositionDao;
	@Resource
	private BizMatReqHeadMapper matReqHeadDao;
	@Resource
	private BizMatReqItemMapper matReqItemDao;
	@Resource
	private IDictionaryService dictionaryService;
	@Resource
	private SequenceDAO sequenceDao;
	@Resource
	private ICommonService commonService;
	@Resource
	private SynMaterialDocHeadMapper synMDocHeadDao;
	@Resource
	private SynMaterialDocItemMapper synMDocItemDao;
	@Resource
	private StockPositionMapper stockPositionDao;
	@Resource
	private DicStockLocationMapper stockLocationDao;
	@Resource
	private BizMaterialDocHeadMapper mDocHeadDao;
	@Resource
	private BizMaterialDocItemMapper mDocItemDao;

	/**
	 * 领料出库过账
	 */
	@Override
	public Map<String, Object> postingForMatReqOrder(int stockOutputId, String userId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stockOutputId);

		BizStockOutputHead outputHead = outPutHeadDao.selectByPrimaryKey(param);
		List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);
		List<BizStockOutputPositionVo> positionList = outPutPositionDao.selectPostionByOrderId(param);

		BizMatReqHead matReqHead = matReqHeadDao.selectByPrimaryKey(Integer.parseInt(outputHead.getReferReceiptCode()));

		/***************************************
		 * IMPORT
		 ***************************************/
		JSONObject I_IMPORT = this.setImport("09", userId);

		I_IMPORT.put("ZDJBH", outputHead.getStockOutputCode());

		/***************************************
		 * HEAD
		 ***************************************/
		JSONArray I_HEAD = new JSONArray();

		String postDate = commonService.getPostDate(matReqHead.getMatReqFtyId());

		JSONObject itemHeadMap = new JSONObject();
		itemHeadMap.put("ZDJBH", outputHead.getStockOutputCode());
		itemHeadMap.put("PSTNG_DATE", postDate); // 凭证日期
		itemHeadMap.put("DOC_DATE", postDate); // 过账日期
		itemHeadMap.put("WERKS", dictionaryService.getFtyCodeByFtyId(matReqHead.getMatReqFtyId())); // 物料发出工厂
		itemHeadMap.put("ZJSGC", dictionaryService.getFtyCodeByFtyId(matReqHead.getReceiveFtyId())); // 物料接受工厂
		itemHeadMap.put("ZNBDD", matReqHead.getInternalOrderCode()); // 内部订单号
		I_HEAD.add(itemHeadMap);

		/***************************************
		 * ITEM
		 ***************************************/
		JSONArray I_ITEM = new JSONArray();

		for (BizStockOutputItemVo item : itemList) {
			for (BizStockOutputPositionVo postion : positionList) {
				if (item.getStockOutputRid().equals(postion.getStockOutputRid())) {
					BizMatReqItem matReqItem = new BizMatReqItem();
					matReqItem.setMatReqId(Integer.parseInt(item.getReferReceiptCode()));
					matReqItem.setMatReqRid(Integer.parseInt(item.getReferReceiptRid()));
					matReqItem = matReqItemDao.selectByIdAndRid(matReqItem);

					JSONObject itemMap = new JSONObject();
					itemMap.put("ZDJBH", outputHead.getStockOutputCode());// 出库单号
					itemMap.put("ZDJXM", postion.getStockOutputRid());// 行项目号
					itemMap.put("ZNON", postion.getStockOutputPositionId());// 行项目号
					itemMap.put("ZLLDH", matReqHead.getMatReqCode()); // 领料单号
					itemMap.put("ZLLDXH", item.getReferReceiptRid()); // 领料单行号
					itemMap.put("RSNUM", ""); // 预留号
					itemMap.put("RSPOS", ""); // 预留行项目
					itemMap.put("LGORT", item.getLocationCode()); // 库存地点
					itemMap.put("ZBCNUM", postion.getQty()); // 出库数量
					itemMap.put("SOBKZ", postion.getSpecStock()); // 特殊库存标识
					itemMap.put("ZPOSID", postion.getSpecStockCode()); // 特殊库存代码
					itemMap.put("MATNR", item.getMatCode()); // 物料编码
					itemMap.put("SAKNR", matReqItem.getGeneralLedgerSubject()); // 总账科目
					itemMap.put("AUFNR", matReqItem.getWorkReceiptCode()); // 工单号
					itemMap.put("EQUNR", matReqItem.getDeviceCode()); // 设备号
					itemMap.put("ANLKL", matReqItem.getAssetAttr()); // 资产管控分类
					itemMap.put("BWART", item.getMoveTypeCode()); // 移动类型
					itemMap.put("KOSTL", matReqItem.getCostObjCode()); // 成本对象

					I_ITEM.add(itemMap);

				}
			}
		}

		JSONObject params = new JSONObject();
		params.put("I_IMPORT", I_IMPORT);
		params.put("I_HEAD", I_HEAD);
		params.put("I_ITEM", sortJsonArrayForMatReq(I_ITEM));

		JSONObject result = new JSONObject();

		if (I_HEAD.getJSONObject(0).get("ZJSGC").equals(I_HEAD.getJSONObject(0).get("WERKS"))) {
			result = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_09_1?sap-client=" + Const.SAP_API_CLIENT,
					params, 3);
		} else {
			result = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_09_2?sap-client=" + Const.SAP_API_CLIENT,
					params, 3);
		}

		JSONArray e_mseg = null;
		JSONArray e_anlaAry = null;

		if ("S".equals(result.getString("RETURNVALUE"))) {
			e_mseg = sortJsonArray(result.getJSONArray("E_MSEG"));
			if (result.has("E_ANLA")) {
				e_anlaAry = result.getJSONArray("E_ANLA");
			}
		}
		return formatSapData(result.getString("RETURNVALUE"), result.getJSONObject("RETURN").getString("MESSAGE"),
				postDate, e_mseg, e_anlaAry);
	}

	/**
	 * 销售出库过账
	 */
	@Override
	public Map<String, Object> postingForSalesOrder(int stockOutputId, String userId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stockOutputId);

		BizStockOutputHead outputHead = outPutHeadDao.selectByPrimaryKey(param);
		List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);

		/***************************************
		 * IMPORT
		 ***************************************/
		JSONObject I_IMPORT = this.setImport("11", userId);
		String postDate = commonService.getPostDate(itemList.get(0).getFtyId());
		I_IMPORT.put("I_DATBI", postDate);
		I_IMPORT.put("ZDJBH", outputHead.getStockOutputCode());

		/***************************************
		 * VBELN
		 ***************************************/
		List<Map<String, Object>> I_VBELN = new ArrayList<Map<String, Object>>();
		Map<String, Object> vbelnMap = new HashMap<String, Object>();
		vbelnMap.put("SIGN", "I");
		vbelnMap.put("OPTION", "EQ");
		vbelnMap.put("LOW", outputHead.getReferReceiptCode()); // 订单编号
		vbelnMap.put("HIGH", "");

		I_VBELN.add(vbelnMap);

		/***************************************
		 * ITEM
		 ***************************************/
		JSONArray I_ITEM = new JSONArray();
		for (BizStockOutputItemVo itemData : itemList) {
			JSONObject itemMap = new JSONObject();

			itemMap.put("ZDJBH", itemData.getStockOutputCode());
			itemMap.put("ZDJXM", itemData.getStockOutputRid());
			itemMap.put("VBELN", itemData.getReferReceiptCode());
			itemMap.put("POSNR", itemData.getReferReceiptRid());
			itemMap.put("MATNR", itemData.getMatCode());
			itemMap.put("LGORT", itemData.getLocationCode());
			itemMap.put("DLV_QTY", itemData.getOutputQty());

			I_ITEM.add(itemMap);
		}

		JSONObject params = new JSONObject();
		params.put("I_IMPORT", I_IMPORT);
		params.put("I_VBELN", I_VBELN);
		params.put("I_ITEM", I_ITEM);

		JSONObject result = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_11?sap-client=" + Const.SAP_API_CLIENT, params, 3);

		JSONArray e_mseg = null;

		if ("S".equals(result.getString("RETURNVALUE"))) {
			e_mseg = sortJsonArray(result.getJSONArray("E_MSEG"));
		}

		return formatSapData(result.getString("RETURNVALUE"), result.getJSONObject("RETURN").getString("MESSAGE"),
				postDate, e_mseg, null);
	}

	/**
	 * 采购退货过账
	 * 
	 * @param stockOutputId
	 * @param userId
	 * @return
	 */
	@Override
	public Map<String, Object> postingForPurchaseOrder(int stockOutputId, String userId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stockOutputId);

		BizStockOutputHead outputHead = outPutHeadDao.selectByPrimaryKey(param);
		List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);
		/***************************************
		 * IMPORT
		 ***************************************/
		JSONObject I_IMPORT = this.setImport("04", userId);
		I_IMPORT.put("ZDJBH", outputHead.getStockOutputCode());

		/***************************************
		 * ITEM
		 ***************************************/
		JSONArray I_LIST = new JSONArray();
		String postDate = "";
		for (BizStockOutputItemVo itemData : itemList) {
			JSONObject itemMap = new JSONObject();

			itemMap.put("ZDJBH", itemData.getStockOutputCode());
			itemMap.put("ZDJXM", itemData.getStockOutputRid());
			itemMap.put("EBELN", itemData.getReferReceiptCode());
			itemMap.put("EBELP", itemData.getReferReceiptRid());
			itemMap.put("BCNUM", itemData.getOutputQty());
			itemMap.put("STGE_LOC", itemData.getLocationCode());

			postDate = commonService.getPostDate(itemData.getFtyId());

			itemMap.put("PSTNG_DATE", postDate); // 凭证日期
			itemMap.put("DOC_DATE", postDate); // 过账日期

			I_LIST.add(itemMap);
		}

		JSONObject params = new JSONObject();

		params.put("I_IMPORT", I_IMPORT);
		params.put("I_ZDJBH", outputHead.getStockOutputCode());
		params.put("I_LIST", I_LIST);

		JSONObject result = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_04?sap-client=" + Const.SAP_API_CLIENT, params, 3);

		JSONArray e_mseg = new JSONArray();

		if ("S".equals(result.getString("RETURNVALUE"))) {
			e_mseg = sortJsonArray(result.getJSONArray("E_MSEG"));
		}

		return formatSapData(result.getString("RETURNVALUE"), result.getString("RETURN"), postDate, e_mseg, null);
	}

	/**
	 * 其他出库过账
	 */
	@Override
	public Map<String, Object> postingForOtherOrder(int stockOutputId, String userId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stockOutputId);

		BizStockOutputHead outputHead = outPutHeadDao.selectByPrimaryKey(param);
		List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);
		List<BizStockOutputPositionVo> positionList = outPutPositionDao.selectPostionByOrderId(param);
		BizStockOutputPositionVo position = positionList.get(0);
		/***************************************
		 * IMPORT
		 ***************************************/
		JSONObject I_IMPORT = this.setImport("06", userId);
		I_IMPORT.put("ZDJBH", outputHead.getStockOutputCode());

		/***************************************
		 * ITEM
		 ***************************************/
		JSONArray I_ITEM = new JSONArray();
		for (BizStockOutputItemVo itemData : itemList) {
			JSONObject itemMap = new JSONObject();

			itemMap.put("ZDJBH", itemData.getStockOutputCode());
			itemMap.put("ZDJXM", itemData.getStockOutputRid());
			itemMap.put("MATERIAL", itemData.getMatCode());
			itemMap.put("PLANT", itemData.getFtyCode());/// 工厂 "2000"
			itemMap.put("STGE_LOC", itemData.getLocationCode());// 库存地点"0004"
			itemMap.put("MOVE_TYPE", itemData.getMoveTypeCode());// 移动类型"511"
			itemMap.put("SPEC_STOCK", position.getSpecStock() == null ? "" : position.getSpecStock());// 特殊库存标识
			itemMap.put("VENDOR", "");// 供应商代码
			itemMap.put("ENTRY_QNT", itemData.getOutputQty());// 数量"30"
			itemMap.put("ENTRY_UOM", itemData.getUnitCode()); // 计量单位"KG"
			itemMap.put("EXTRA2", outputHead.getReceiveCode());// 接收方
			itemMap.put("MOVE_MAT", "");// 目标物料
			itemMap.put("MOVE_PLANT", "");// 目标工厂
			itemMap.put("MOVE_STLOC", "");// 目标库存地点
			itemMap.put("WBS_ELEM", "");// 特殊库存代码""
			itemMap.put("VAL_WBS_ELEM", "");// 目标特殊库存代码

			I_ITEM.add(itemMap);
		}
		/***************************************
		 * HEAD
		 ***************************************/
		JSONArray i_headAry = new JSONArray();
		JSONObject item = I_ITEM.getJSONObject(0);
		JSONObject i_head = new JSONObject();
		i_head.put("ZNO", outputHead.getStockOutputCode());// 出库单号
		i_head.put("PLANT", item.get("PLANT"));// 工厂"2000"
		i_head.put("STGE_LOC", item.get("STGE_LOC"));// 库存地点 "0004"
		i_head.put("MOVE_TYPE", item.get("MOVE_TYPE"));// 移动类型 "511"
		i_head.put("SPEC_STOCK", item.get("SPEC_STOCK"));// 特殊库存标识
		i_head.put("VENDOR", item.get("VENDOR"));// 供应商代码 "0020003377"
		i_head.put("VAL_WBS_ELEM", item.get("WBS_ELEM"));// 特殊库存代码 ""

		String postDate = commonService.getPostDate(itemList.get(0).getFtyId());
		i_head.put("BUDAT", postDate);// 过账日期"2017-07-08"
		i_head.put("BLDAT", postDate);// 凭证日期"2017-07-08"
		i_headAry.add(i_head);

		JSONObject params = new JSONObject();
		params.put("I_IMPORT", I_IMPORT);
		params.put("I_HEAD", i_headAry);
		params.put("I_ITEM", I_ITEM);

		JSONObject result = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_06?sap-client=" + Const.SAP_API_CLIENT, params, 10);

		JSONArray e_mseg = new JSONArray();

		if ("S".equals(result.getString("RETURNVALUE"))) {
			e_mseg = sortJsonArray(result.getJSONArray("E_MSEG"));

		}

		return formatSapData(result.getString("RETURNVALUE"), result.getString("RETURN"), postDate, e_mseg, null);
	}

	/**
	 * 核对账目过账
	 * 
	 * @param stockOutputId
	 * @param matDocId
	 * @param ridList
	 * @return
	 */
	@Override
	public Map<String, Object> postingForHdzm(int stockOutputId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stockOutputId);

		List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);

		SynMaterialDocHead head = synMDocHeadDao.selectByPrimaryKey(itemList.get(0).getMatDocId());

		Map<String, Object> key = new HashMap<String, Object>();
		key.put("matDocId", head.getMatDocId());
		List<Map<String, Object>> msegList = synMDocItemDao.selectListById(key);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String postDate = sdf.format(head.getPostingTime());

		if (msegList.size() > 0) {
			return formatSapData("S", "凭证生成成功", postDate, JSONArray.fromObject(msegList), null);

		} else {
			return formatSapData("E", "凭证生成失败", postDate, null, null);

		}

	}

	/**
	 * 调拨出库过账
	 */
	@Override
	public Map<String, Object> postingForAllocateOrder(int stockOutputId, String userId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stockOutputId);

		BizStockOutputHead outputHead = outPutHeadDao.selectByPrimaryKey(param);
		List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);

		BizAllocateHead allocateHead = allocateHeadDao
				.selectByPrimaryKey(Integer.parseInt(outputHead.getReferReceiptCode()));

		/***************************************
		 * IMPORT
		 ***************************************/
		JSONObject I_IMPORT = this.setImport("15", userId);
		I_IMPORT.put("ZDJBH", outputHead.getStockOutputCode());

		/***************************************
		 * ITEM
		 ***************************************/
		JSONArray I_ITEM = new JSONArray();
		for (BizStockOutputItemVo itemData : itemList) {
			JSONObject itemMap = new JSONObject();
			itemMap.put("ZDJBH", itemData.getStockOutputCode());
			itemMap.put("ZDJXM", itemData.getStockOutputRid());
			itemMap.put("MATERIAL", itemData.getMatCode());
			itemMap.put("PLANT", itemData.getFtyCode());
			itemMap.put("STGE_LOC", itemData.getLocationCode());
			itemMap.put("MOVE_TYPE", "313");
			itemMap.put("SPEC_STOCK", "");// 原特殊库存标识
			itemMap.put("ZTSKC1", "");// 原特殊库存编号
			itemMap.put("ENTRY_QNT", itemData.getOutputQty());

			itemMap.put("MOVE_MAT", itemData.getMatCode());
			itemMap.put("MOVE_PLANT", dictionaryService.getFtyCodeByFtyId(allocateHead.getFtyInput()));
			itemMap.put("MOVE_STLOC", dictionaryService.getLocCodeByLocId(allocateHead.getLocationInput()));
			itemMap.put("ZKCBS", "");
			itemMap.put("ZTSKC2", "");

			I_ITEM.add(itemMap);
		}

		/***************************************
		 * HEAD
		 ***************************************/
		JSONArray i_headAry = new JSONArray();
		JSONObject i_head = new JSONObject();
		String postDate = commonService.getPostDate(itemList.get(0).getFtyId());
		i_head.put("PSTNG_DATE", postDate);// 过账日期"2017-07-08"
		i_head.put("DOC_DATE", postDate);// 凭证日期"2017-07-08"
		i_head.put("ZDJBH", outputHead.getStockOutputCode());
		i_headAry.add(i_head);

		JSONObject params = new JSONObject();
		params.put("I_IMPORT", I_IMPORT);
		params.put("HEAD", i_headAry);
		params.put("ITEM", I_ITEM);

		JSONObject result = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_15?sap-client=" + Const.SAP_API_CLIENT, params, 3);

		JSONArray e_mseg = null;

		if ("S".equals(result.getString("RETURNVALUE"))) {
			e_mseg = sortJsonArray(result.getJSONArray("E_MSEG"));
		}

		return formatSapData(result.getString("RETURNVALUE"), result.getJSONObject("RETURN").getString("MESSAGE"),
				postDate, e_mseg, null);
	}

	/**
	 * 获取销售订单
	 */
	@Override
	public List<Map<String, Object>> getSalesOrderList(String salesOrderCode, String userId) throws Exception {
		JSONObject params = new JSONObject();

		JSONObject I_IMPORT = this.setImport("10", userId);

		JSONArray I_EBELN = new JSONArray();

		JSONObject ebelnObj = new JSONObject();
		ebelnObj.put("SIGN", "I");
		ebelnObj.put("OPTION", "EQ");
		ebelnObj.put("LOW", salesOrderCode);

		I_EBELN.add(ebelnObj);

		params.put("I_IMPORT", I_IMPORT);
		params.put("I_VBELN", I_EBELN);

		JSONObject result = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_10?sap-client=" + Const.SAP_API_CLIENT, params, 30);

		JSONArray returnList = result.getJSONArray("I_HEAD");

		List<Map<String, Object>> orderHeadList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < returnList.size(); i++) {
			JSONObject sapObj = returnList.getJSONObject(i);
			orderHeadList.add(this.getSalesOrderHead(sapObj));
		}

		return orderHeadList;

	}

	/**
	 * 获取销售订单详细
	 */
	@Override
	public Map<String, Object> getSalesViewBySap(String salesOrderCode, CurrentUser user) throws Exception {
		JSONObject params = new JSONObject();

		JSONObject I_IMPORT = this.setImport("10", user.getUserId());

		JSONArray I_EBELN = new JSONArray();

		JSONObject ebelnObj = new JSONObject();
		ebelnObj.put("SIGN", "I");
		ebelnObj.put("OPTION", "EQ");
		ebelnObj.put("LOW", salesOrderCode);

		I_EBELN.add(ebelnObj);

		params.put("I_IMPORT", I_IMPORT);
		params.put("I_VBELN", I_EBELN);

		JSONObject result = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_10?sap-client=" + Const.SAP_API_CLIENT, params, 30);

		JSONArray headSAP = result.getJSONArray("I_HEAD");

		Map<String, Object> head = null;

		if (headSAP.size() > 0) {
			JSONObject sapObj = headSAP.getJSONObject(0);
			head = this.getSalesOrderHead(sapObj);

			JSONArray itemAry = result.getJSONArray("I_ITEM"); // 销售订单子表数据
			head.put("item_list", this.getSalesOrderItem(itemAry, user.getLocationList()));

		}

		return head;

	}

	/**
	 * 获取采购订单
	 */
	@Override
	public List<Map<String, Object>> getPurchaseOrderList(Map<String, Object> searchMap) throws Exception {
		JSONObject params = new JSONObject();

		JSONObject I_IMPORT = this.setImport("03", UtilObject.getStringOrEmpty(searchMap.get("userId")));

		JSONArray I_EBELN = new JSONArray();

		JSONObject ebelnObj = new JSONObject();
		ebelnObj.put("SIGN", "I");
		ebelnObj.put("OPTION", "EQ");
		ebelnObj.put("LOW", searchMap.get("purchaseOrderCode"));

		I_EBELN.add(ebelnObj);

		params.put("I_IMPORT", I_IMPORT);
		params.put("I_EBELN", I_EBELN);
		params.put("I_LIFNR", UtilObject.getStringOrEmpty(searchMap.get("supplierCode")));
		params.put("I_ZHTBH", UtilObject.getStringOrEmpty(searchMap.get("contractCode")));
		params.put("i_state", "3");

		JSONObject result = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_03?sap-client=" + Const.SAP_API_CLIENT, params, 30);

		JSONArray returnList = result.getJSONArray("ET_HEAD");

		List<Map<String, Object>> orderHeadList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < returnList.size(); i++) {
			JSONObject sapObj = returnList.getJSONObject(i);
			orderHeadList.add(this.getPurchaseOrderHead(sapObj));
		}

		return orderHeadList;
	}

	/**
	 * 获取采购订单明细
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPurchaseViewBySap(Map<String, Object> searchMap) throws Exception {
		JSONObject params = new JSONObject();

		JSONObject I_IMPORT = this.setImport("03", UtilObject.getStringOrEmpty(searchMap.get("userId")));

		JSONArray I_EBELN = new JSONArray();

		JSONObject ebelnObj = new JSONObject();
		ebelnObj.put("SIGN", "I");
		ebelnObj.put("OPTION", "EQ");
		ebelnObj.put("LOW", searchMap.get("purchaseOrderCode"));

		I_EBELN.add(ebelnObj);

		params.put("I_IMPORT", I_IMPORT);
		params.put("I_EBELN", I_EBELN);
		params.put("I_LIFNR", UtilObject.getStringOrEmpty(searchMap.get("supplierCode")));
		params.put("I_ZHTBH", UtilObject.getStringOrEmpty(searchMap.get("contractCode")));
		params.put("i_state", "3");

		JSONObject result = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_03?sap-client=" + Const.SAP_API_CLIENT, params, 30);

		JSONArray headSAP = result.getJSONArray("ET_HEAD");

		Map<String, Object> head = null;

		if (headSAP.size() > 0) {
			JSONObject sapObj = headSAP.getJSONObject(0);
			head = this.getPurchaseOrderHead(sapObj);

			JSONArray itemAry = result.getJSONArray("ET_ITEM"); // 销售订单子表数据
			head.put("item_list", this.getPurchaseOrderItem(itemAry, (List<String>) searchMap.get("locationList")));
		}

		return head;
	}

	/**
	 * 冲销
	 */
	@Override
	public Map<String, Object> writeOff(Map<String, Object> param, List<Integer> ridList, CurrentUser user)
			throws Exception {

		BizStockOutputHead outputHead = outPutHeadDao.selectByPrimaryKey(param);

		/***************************************
		 * IMPORT
		 ***************************************/
		JSONObject I_IMPORT = new JSONObject();
		String url = "";
		if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_SALE.getValue()) { // 销售出库
			I_IMPORT = this.setImport("27", user.getUserId());
			url = Const.SAP_API_URL + "int_27?sap-client=" + Const.SAP_API_CLIENT;
		} else { // 其他
			I_IMPORT = this.setImport("16", user.getUserId());
			url = Const.SAP_API_URL + "int_16?sap-client=" + Const.SAP_API_CLIENT;
		}
		I_IMPORT.put("ZDJBH", outputHead.getStockOutputCode());

		/***************************************
		 * ITEM
		 ***************************************/
		List<Map<String, Object>> I_ITEM = new ArrayList<Map<String, Object>>();// SAP参数(ITEM)
		int matDocYear = 0;
		String matDocCode = "";
		for (int i = 0; i < ridList.size(); i++) {
			// 查询出库明细数据
			param.put("stockOutputRid", ridList.get(i));
			List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);
			BizStockOutputItemVo item = itemList.get(0);

			// 查询物料凭证明细数据
			Map<String, Object> key = new HashMap<String, Object>();
			key.put("matDocId", item.getMatDocId());
			key.put("referReceiptId", item.getStockOutputId());
			key.put("referReceiptRid", item.getStockOutputRid());
			if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_ALLCOTE.getValue()) {
				key.put("debitCredit", "S");
			}
			BizMaterialDocHead mDocHead = mDocHeadDao.selectByPrimaryKey(item.getMatDocId());
			matDocCode = mDocHead.getMatDocCode();

			List<BizMaterialDocItem> mDocItemList = mDocItemDao.selectByReferenceOrder(key);

			for (BizMaterialDocItem mDocItem : mDocItemList) {
				matDocYear = mDocItem.getMatDocYear();
				Map<String, Object> itemMap = new HashMap<String, Object>();
				itemMap.put("MBLNR", matDocCode); // 物料凭证
				itemMap.put("MBLPO", mDocItem.getMatDocRid().toString()); // SAP返回过的行项目号
				itemMap.put("ZDJBH", mDocItem.getReferReceiptCode());
				itemMap.put("ZDJXM", mDocItem.getReferReceiptRid().toString());
				if (outputHead.getReceiptType() == EnumReceiptType.STOCK_OUTPUT_MAT_REQ.getValue()) {
					BizMatReqHead matReqHead = matReqHeadDao
							.selectByPrimaryKey(Integer.parseInt(outputHead.getReferReceiptCode()));

					itemMap.put("ZLLDH", matReqHead.getMatReqCode());
					itemMap.put("ZLLDXH", item.getReferReceiptRid());
				}

				I_ITEM.add(itemMap);
			}

		}

		JSONObject params = new JSONObject();

		params.put("I_IMPORT", I_IMPORT);
		params.put("I_ITEM", I_ITEM);
		params.put("I_MBLNR", matDocCode);
		params.put("I_MJAHR", "" + matDocYear);
		params.put("I_ZDJBH", outputHead.getStockOutputCode());

		JSONObject result = UtilREST.executePostJSONTimeOut(url, params, 3);

		JSONArray e_mseg = new JSONArray();
		String postDate = "";
		if ("S".equals(result.getString("RETURNVALUE"))) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			e_mseg = sortJsonArray(result.getJSONArray("E_MSEG"));
			postDate = format.format(new Date());
		}

		return formatSapData(result.getString("RETURNVALUE"), result.getJSONObject("RETURN").getString("MESSAGE"),
				postDate, e_mseg, null);

	}

	/**
	 * 预留出库过账
	 */
	@Override
	public Map<String, Object> postingForReserveOrder(int stockOutputId, String userId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockOutputId", stockOutputId);

		BizStockOutputHead outputHead = outPutHeadDao.selectByPrimaryKey(param);
		List<BizStockOutputItemVo> itemList = outPutItemDao.selectItemByOrderId(param);
		List<BizStockOutputPositionVo> positionList = outPutPositionDao.selectPostionByOrderId(param);

		//BizMatReqHead matReqHead = matReqHeadDao.selectByPrimaryKey(Integer.parseInt(outputHead.getReferReceiptCode()));

		/***************************************
		 * IMPORT
		 ***************************************/
		JSONObject I_IMPORT = this.setImport("09", userId);

		I_IMPORT.put("ZDJBH", outputHead.getStockOutputCode());

		/***************************************
		 * HEAD
		 ***************************************/
		JSONArray I_HEAD = new JSONArray();

		String postDate = commonService.getPostDate(itemList.get(0).getFtyId());

		JSONObject itemHeadMap = new JSONObject();
		itemHeadMap.put("ZDJBH", outputHead.getStockOutputCode());
		itemHeadMap.put("PSTNG_DATE", postDate); // 凭证日期
		itemHeadMap.put("DOC_DATE", postDate); // 过账日期
		itemHeadMap.put("WERKS", dictionaryService.getFtyCodeByFtyId(itemList.get(0).getFtyId())); // 物料发出工厂
		itemHeadMap.put("ZJSGC", dictionaryService.getFtyCodeByFtyId(itemList.get(0).getFtyId())); // 物料接受工厂
		itemHeadMap.put("ZNBDD", ""); // 内部订单号
		I_HEAD.add(itemHeadMap);

		/***************************************
		 * ITEM
		 ***************************************/
		JSONArray I_ITEM = new JSONArray();

		for (BizStockOutputItemVo item : itemList) {
			for (BizStockOutputPositionVo postion : positionList) {
				if (item.getStockOutputRid().equals(postion.getStockOutputRid())) {
//					BizMatReqItem matReqItem = new BizMatReqItem();
//					matReqItem.setMatReqId(Integer.parseInt(item.getReferReceiptCode()));
//					matReqItem.setMatReqRid(Integer.parseInt(item.getReferReceiptRid()));
//					matReqItem = matReqItemDao.selectByIdAndRid(matReqItem);

					JSONObject itemMap = new JSONObject();
					itemMap.put("ZDJBH", outputHead.getStockOutputCode());// 出库单号
					itemMap.put("ZDJXM", postion.getStockOutputRid());// 行项目号
					itemMap.put("ZNON", postion.getStockOutputPositionId());// 行项目号
					itemMap.put("ZLLDH", ""); // 领料单号
					itemMap.put("ZLLDXH", ""); // 领料单行号
					itemMap.put("RSNUM", item.getReferReceiptCode()); // 预留号
					itemMap.put("RSPOS", item.getReferReceiptRid()); // 预留行项目
					itemMap.put("LGORT", item.getLocationCode()); // 库存地点
					itemMap.put("ZBCNUM", postion.getQty()); // 出库数量
					itemMap.put("SOBKZ", postion.getSpecStock()); // 特殊库存标识
					itemMap.put("ZPOSID", postion.getSpecStockCode()); // 特殊库存代码
					itemMap.put("MATNR", item.getMatCode()); // 物料编码
					itemMap.put("SAKNR", ""); // 总账科目
					itemMap.put("AUFNR", ""); // 工单号
					itemMap.put("EQUNR", ""); // 设备号
					itemMap.put("ANLKL", ""); // 资产管控分类
					itemMap.put("BWART", item.getMoveTypeCode()); // 移动类型
					itemMap.put("KOSTL", ""); // 成本对象

					I_ITEM.add(itemMap);

				}
			}
		}

		JSONObject params = new JSONObject();
		params.put("I_IMPORT", I_IMPORT);
		params.put("I_HEAD", I_HEAD);
		params.put("I_ITEM", sortJsonArrayForMatReq(I_ITEM));

		JSONObject result = new JSONObject();

		if (I_HEAD.getJSONObject(0).get("ZJSGC").equals(I_HEAD.getJSONObject(0).get("WERKS"))) {
			result = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_09_1?sap-client=" + Const.SAP_API_CLIENT,
					params, 3);
		} else {
			result = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_09_2?sap-client=" + Const.SAP_API_CLIENT,
					params, 3);
		}

		JSONArray e_mseg = null;
		JSONArray e_anlaAry = null;

		if ("S".equals(result.getString("RETURNVALUE"))) {
			e_mseg = sortJsonArray(result.getJSONArray("E_MSEG"));
			if (result.has("E_ANLA")) {
				e_anlaAry = result.getJSONArray("E_ANLA");
			}
		}
		return formatSapData(result.getString("RETURNVALUE"), result.getJSONObject("RETURN").getString("MESSAGE"),
				postDate, e_mseg, e_anlaAry);
	}

	/**
	 * 获取预留订单详细
	 */
	@Override
	public List<Map<String, Object>> getReserveViewBySap(String refer_reserve_code, CurrentUser user) throws Exception {
		JSONObject params = new JSONObject();

		JSONObject I_IMPORT = this.setImport("预留单接口", user.getUserId());

		params.put("I_IMPORT", I_IMPORT);
		if (!UtilString.isNullOrEmpty(refer_reserve_code)) {
			params.put("I_RSNUM", refer_reserve_code);
		}
		JSONObject result = JSONObject.fromObject(
				"{\"RETURNVALUE\":\"S\",\"RETURN\":\"成功\",\"E_LIST\":[{\"E_HEAD\":{\"RSNUM\":\"30000002\",\"DEPART_NAME\":\"领用部门\",\"SQR\":\"申请人\",\"BDTER\":\"申请日期\",\"HEA1\":\"预留1\",\"HEA2\":\"预留2\",\"HEA3\":\"预留3\"},\"E_ITEM\":[{\"ZSQDH_ITEM\":\"00010\",\"BWART\":\"241\",\"MATNR\":\"60000651\",\"MAKTX\":\"物料描述\",\"MEINS\":\"KG\",\"MSEHT\":\"公斤\",\"MENGE\":\"50\",\"WERKS\":\"2000\",\"LGORT\":\"0048\",\"LGOBE\":\"塔拉壕\",\"LABST\":\"100\",\"RES1\":\"预留1\",\"RES2\":\"预留2\",\"RES3\":\"预留3\"},{\"ZSQDH_ITEM\":\"00020\",\"BWART\":\"241\",\"MATNR\":\"60000649\",\"MAKTX\":\"物料描述\",\"MEINS\":\"KG\",\"MSEHT\":\"公斤\",\"MENGE\":\"50\",\"WERKS\":\"2000\",\"LGORT\":\"0048\",\"LGOBE\":\"塔拉壕\",\"LABST\":\"100\",\"RES1\":\"预留1\",\"RES2\":\"预留2\",\"RES3\":\"预留3\"},{\"ZSQDH_ITEM\":\"00030\",\"BWART\":\"241\",\"MATNR\":\"60161673\",\"MAKTX\":\"物料描述\",\"MEINS\":\"KG\",\"MSEHT\":\"公斤\",\"MENGE\":\"50\",\"WERKS\":\"2000\",\"LGORT\":\"0048\",\"LGOBE\":\"塔拉壕\",\"LABST\":\"100\",\"RES1\":\"预留1\",\"RES2\":\"预留2\",\"RES3\":\"预留3\"}]}]}");
		// UtilREST.executePostJSONTimeOut(Const.SAP_API_URL +
		// "int_10?sap-client=" + Const.SAP_API_CLIENT,params, 30);

		JSONArray headSAP = result.getJSONArray("E_LIST");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if (headSAP.size() > 0) {
			for (int i = 0; i < headSAP.size(); i++) {
				Map<String, Object> re = null;
				JSONObject sapObj = headSAP.getJSONObject(i);
				re = this.getReceiptHead(sapObj.getJSONObject("E_HEAD"));

				JSONArray itemAry = sapObj.getJSONArray("E_ITEM"); // 销售订单子表数据
				re = this.getReceiptItem(re, itemAry, user.getLocationList());

				list.add(re);
			}
		}

		return list;

	}

	// MyTask 通用私有方法

	/**
	 * SAP预留订单头信息转换
	 * 
	 * @param sapObj
	 * @return
	 */
	private Map<String, Object> getReceiptHead(JSONObject sapObj) {
		Map<String, Object> orderHead = new HashMap<String, Object>();

		orderHead.put("refer_receipt_code", sapObj.getString("RSNUM")); // 销售订单编号
		orderHead.put("apply_depart_name", sapObj.getString("DEPART_NAME"));// 领料部门
		orderHead.put("apply_user_name", sapObj.getString("SQR"));// 申请人
		orderHead.put("apply_time", sapObj.getString("BDTER"));// 申请日期

		return orderHead;
	}

	/**
	 * 预留订单明细信息转换
	 * 
	 * @param itemAry
	 * @return
	 */
	private Map<String, Object> getReceiptItem(Map<String, Object> re, JSONArray itemAry, List<String> locationList) {

		List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
		// String isFocusBatch = "0"; // 批次信息是否强控
		Map<String, DicFactoryVo> ftyMap = dictionaryService.getFtyCodeMap();
		Map<String, DicStockLocationVo> locationMap = dictionaryService.getLocationCodeMap();

		BigDecimal sumQty = new BigDecimal("0");

		for (int i = 0; i < itemAry.size(); i++) {
			JSONObject sapItemObj = itemAry.getJSONObject(i);

			sumQty = sumQty.add(UtilObject.getBigDecimalOrZero(sapItemObj.getString("MENGE")));

			DicFactoryVo ftyVo = ftyMap.get(sapItemObj.getString("WERKS"));
			DicStockLocationVo locationVo = locationMap.get(dictionaryService
					.getFtyCodeAndLocCodeForLocMap(sapItemObj.getString("WERKS"), sapItemObj.getString("LGORT")));

			String sapLgort = ftyVo.getFtyId() + "-" + locationVo.getLocationId();

			if (locationList.contains(sapLgort)) {
				Map<String, Object> orderItem = new HashMap<String, Object>();
				orderItem.put("stock_output_rid", i + 1); // 序号
				orderItem.put("fty_id", ftyVo.getFtyId()); // 工厂
				orderItem.put("fty_code", sapItemObj.getString("WERKS")); // 工厂
				orderItem.put("fty_name", ftyVo.getFtyName()); // 工厂
				orderItem.put("location_id", locationVo.getLocationId());// 库存地点
				orderItem.put("location_code", sapItemObj.getString("LGORT"));// 库存地点
				orderItem.put("location_name", locationVo.getLocationName()); // 库存地点
				orderItem.put("mat_id", dictionaryService.getMatIdByMatCode(sapItemObj.getString("MATNR"))); // 物料编码
				orderItem.put("mat_code", sapItemObj.getString("MATNR")); // 物料编码
				orderItem.put("mat_name", sapItemObj.getString("MAKTX")); // 物料名称
				orderItem.put("unit_id", dictionaryService.getUnitIdByUnitCode(sapItemObj.getString("MEINS"))); // 计量单位
				orderItem.put("unit_code", sapItemObj.getString("MEINS")); // 计量单位
				orderItem.put("unit_name", sapItemObj.getString("MSEHT")); // 计量单位描述
				sapItemObj.put("ANDEC", "3");
				sapItemObj.put("ZFHSL", "0");
				sapItemObj.put("TYPE-X", "2");
				orderItem.put("decimal_place", sapItemObj.getString("ANDEC"));// 浮点型小数
				orderItem.put("order_qty", sapItemObj.getString("MENGE"));// 订单数量
				orderItem.put("stock_qty", sapItemObj.getString("LABST"));// 库存数量
				orderItem.put("send_qty", sapItemObj.getString("ZFHSL"));// 已发货数量
				orderItem.put("output_qty", UtilObject.getBigDecimalOrZero(sapItemObj.getString("MENGE"))
						.subtract(UtilObject.getBigDecimalOrZero(sapItemObj.getString("ZFHSL"))));// 可发数量
				orderItem.put("refer_receipt_rid", sapItemObj.getString("ZSQDH_ITEM"));// 参考单据序号
				orderItem.put("mat_store_type", sapItemObj.getString("TYPE-X"));// 已发货数量
				// isFocusBatch =
				// stockLocationDao.selectPlanByLocation(locationVo.getLocationId());
				// orderItem.put("is_focus_batch", isFocusBatch == null ? "0" :
				// isFocusBatch);// 批次信息是否强控

				BigDecimal orderQty = UtilObject.getBigDecimalOrZero(orderItem.get("order_qty"));
				BigDecimal sendQty = UtilObject.getBigDecimalOrZero(orderItem.get("send_qty"));

				if (orderQty.compareTo(sendQty) == 1) {
					itemList.add(orderItem);
				}

			}
		}
		re.put("order_qty", sumQty);
		re.put("item_list", itemList);

		return re;
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

	/**
	 * SAP销售订单头信息转换
	 * 
	 * @param sapObj
	 * @return
	 */
	private Map<String, Object> getSalesOrderHead(JSONObject sapObj) {
		Map<String, Object> orderHead = new HashMap<String, Object>();
		orderHead.put("refer_receipt_code", sapObj.getString("VBELN")); // 销售订单编号
		orderHead.put("receive_name", sapObj.getString("NAME1"));// 客户名称
		orderHead.put("org_name", sapObj.getString("VTEXT"));// 销售组织名称
		orderHead.put("corp_id", dictionaryService.getCorpIdByCorpCode(sapObj.getString("BUKRS")));
		orderHead.put("corp_code", sapObj.getString("BUKRS")); // 公司代码
		orderHead.put("corp_name", sapObj.getString("BUTXT")); // 公司名称
		orderHead.put("preorder_id", sapObj.getString("BSTNK"));// 合同号
		orderHead.put("receive_code", sapObj.getString("KUNNR"));// 客户编码
		orderHead.put("order_type", sapObj.getString("AUART"));// 订单类型
		orderHead.put("order_type_name", sapObj.getString("BEZEI"));// 订单类型名称
		orderHead.put("org_code", sapObj.getString("VKORG"));// 销售组织
		orderHead.put("group_code", sapObj.getString("VKGRP"));// 销售组
		orderHead.put("create_time", sapObj.getString("ERDAT"));// 创建日期

		return orderHead;
	}

	/**
	 * SAP销售订单明细信息转换
	 * 
	 * @param itemAry
	 * @return
	 */
	private List<Map<String, Object>> getSalesOrderItem(JSONArray itemAry, List<String> locationList) {

		List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
		String isFocusBatch = "0"; // 批次信息是否强控
		Map<String, DicFactoryVo> ftyMap = dictionaryService.getFtyCodeMap();
		Map<String, DicStockLocationVo> locationMap = dictionaryService.getLocationCodeMap();

		for (int i = 0; i < itemAry.size(); i++) {
			JSONObject sapItemObj = itemAry.getJSONObject(i);
			DicFactoryVo ftyVo = ftyMap.get(sapItemObj.getString("WERKS"));
			DicStockLocationVo locationVo = locationMap.get(dictionaryService
					.getFtyCodeAndLocCodeForLocMap(sapItemObj.getString("WERKS"), sapItemObj.getString("LGORT")));

			String sapLgort = ftyVo.getFtyId() + "-" + locationVo.getLocationId();

			if (locationList.contains(sapLgort)) {
				Map<String, Object> orderItem = new HashMap<String, Object>();
				orderItem.put("stock_output_rid", i + 1); // 序号
				orderItem.put("fty_id", ftyVo.getFtyId()); // 工厂
				orderItem.put("fty_code", sapItemObj.getString("WERKS")); // 工厂
				orderItem.put("fty_name", ftyVo.getFtyName()); // 工厂
				orderItem.put("location_id", locationVo.getLocationId());// 库存地点
				orderItem.put("location_code", sapItemObj.getString("LGORT"));// 库存地点
				orderItem.put("location_name", locationVo.getLocationName()); // 库存地点
				orderItem.put("mat_id", dictionaryService.getMatIdByMatCode(sapItemObj.getString("MATNR"))); // 物料编码
				orderItem.put("mat_code", sapItemObj.getString("MATNR")); // 物料编码
				orderItem.put("mat_name", sapItemObj.getString("ARKTX")); // 物料名称
				orderItem.put("unit_id", dictionaryService.getUnitIdByUnitCode(sapItemObj.getString("MEINS"))); // 计量单位
				orderItem.put("unit_code", sapItemObj.getString("MEINS")); // 计量单位
				orderItem.put("unit_name", sapItemObj.getString("MSEHL")); // 计量单位描述
				orderItem.put("decimal_place", sapItemObj.getString("ANDEC"));// 浮点型小数
				orderItem.put("stock_qty", sapItemObj.getString("ZMENG"));// 销售数量
				orderItem.put("send_qty", sapItemObj.getString("ZFHSL"));// 已发货数量
				orderItem.put("last_qty", UtilObject.getBigDecimalOrZero(sapItemObj.getString("ZMENG"))
						.subtract(UtilObject.getBigDecimalOrZero(sapItemObj.getString("ZFHSL"))));// 可发数量
				orderItem.put("spec_stock", sapItemObj.getString("SOBKZ"));// 特殊库存类型
				orderItem.put("spec_stock_code", sapItemObj.getString("ZTSKC"));// 特殊库存编号
				orderItem.put("spec_stock_name", sapItemObj.getString("ZTSMS"));// 特殊库存描述
				orderItem.put("refer_receipt_code", sapItemObj.getString("VBELN"));// 参考单据编号(销售订单编号)
				orderItem.put("refer_receipt_rid", sapItemObj.getString("POSNR"));// 参考单据序号

				isFocusBatch = stockLocationDao.selectPlanByLocation(locationVo.getLocationId());
				orderItem.put("is_focus_batch", isFocusBatch == null ? "0" : isFocusBatch);// 批次信息是否强控

				BigDecimal stockQty = UtilObject.getBigDecimalOrZero(orderItem.get("stock_qty"));
				BigDecimal sendQty = UtilObject.getBigDecimalOrZero(orderItem.get("send_qty"));

				if (stockQty.compareTo(sendQty) == 1) {
					itemList.add(orderItem);
				}

			}
		}

		return itemList;
	}

	/**
	 * 获取采购订单头信息
	 * 
	 * @param sapObj
	 * @return
	 */
	private Map<String, Object> getPurchaseOrderHead(JSONObject sapObj) {
		Map<String, Object> orderHead = new HashMap<String, Object>();

		orderHead.put("corp_id", dictionaryService.getCorpIdByCorpCode(sapObj.getString("BUKRS")));
		orderHead.put("corp_code", sapObj.getString("BUKRS")); // 公司代码
		orderHead.put("corp_name", sapObj.getString("BUTXT")); // 公司名称
		orderHead.put("refer_receipt_code", sapObj.getString("EBELN")); // 采购订单号
		orderHead.put("preorder_id", sapObj.getString("ZHTBH"));// 合同号
		orderHead.put("preorder_name", sapObj.getString("ZHTMC"));// 合同描述
		orderHead.put("order_type", sapObj.getString("BSART"));// 订单类型
		orderHead.put("order_type_name", sapObj.getString("BATXT"));// 订单类型名称
		orderHead.put("org_code", sapObj.getString("EKORG"));// 采购组织
		orderHead.put("org_name", sapObj.getString("EKOTX"));// 采购组织描述
		orderHead.put("group_code", sapObj.getString("EKGRP"));// 采购组
		orderHead.put("group_name", sapObj.getString("EKNAM"));// 采购组描述
		orderHead.put("create_time", sapObj.getString("AEDAT"));// 创建日期
		orderHead.put("receive_code", sapObj.getString("LIFNR"));// 供应商名称
		orderHead.put("receive_name", sapObj.getString("NAME1"));// 供应商名称

		return orderHead;
	}

	/**
	 * SAP采购订单明细信息转换
	 * 
	 * @param itemAry
	 * @return
	 */
	private List<Map<String, Object>> getPurchaseOrderItem(JSONArray itemAry, List<String> locationList) {

		List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
		String isFocusBatch = "0"; // 批次信息是否强控

		Map<String, DicFactoryVo> ftyMap = dictionaryService.getFtyCodeMap();
		Map<String, DicStockLocationVo> locationMap = dictionaryService.getLocationCodeMap();

		for (int i = 0; i < itemAry.size(); i++) {
			JSONObject sapItemObj = itemAry.getJSONObject(i);
			if (!"".equals(sapItemObj.getString("RETPO")) && "X".equals(sapItemObj.getString("RETPO"))) {
				DicFactoryVo ftyVo = ftyMap.get(sapItemObj.getString("WERKS"));
				DicStockLocationVo locationVo = locationMap.get(dictionaryService
						.getFtyCodeAndLocCodeForLocMap(sapItemObj.getString("WERKS"), sapItemObj.getString("LGORT")));

				String sapLgort = ftyVo.getFtyId() + "-" + locationVo.getLocationId();

				if (locationList.contains(sapLgort)) {
					Map<String, Object> orderItem = new HashMap<String, Object>();
					orderItem.put("stock_output_rid", i + 1); // 工厂
					orderItem.put("fty_id", ftyVo.getFtyId()); // 工厂
					orderItem.put("fty_code", sapItemObj.getString("WERKS")); // 工厂
					orderItem.put("fty_name", ftyVo.getFtyName()); // 工厂
					orderItem.put("location_id", locationVo.getLocationId());// 库存地点
					orderItem.put("location_code", sapItemObj.getString("LGORT"));// 库存地点
					orderItem.put("location_name", locationVo.getLocationName());// 库存地点描述
					orderItem.put("mat_id", dictionaryService.getMatIdByMatCode(sapItemObj.getString("MATNR"))); // 物料编码
					orderItem.put("mat_code", sapItemObj.getString("MATNR")); // 物料编码
					orderItem.put("mat_name", sapItemObj.getString("TXZ01")); // 物料名称
					orderItem.put("unit_id", dictionaryService.getUnitIdByUnitCode(sapItemObj.getString("MEINS"))); // 计量单位
					orderItem.put("unit_code", sapItemObj.getString("MEINS")); // 计量单位
					orderItem.put("unit_name", sapItemObj.getString("MSEHL")); // 计量单位描述
					orderItem.put("decimal_place", sapItemObj.getString("ANDEC"));// 浮点型小数
					orderItem.put("stock_qty", sapItemObj.getString("MENGE"));// 订单数量
					orderItem.put("send_qty", sapItemObj.getString("SJNUM"));// 已退货数量
					orderItem.put("last_qty", UtilObject.getBigDecimalOrZero(sapItemObj.getString("MENGE"))
							.subtract(UtilObject.getBigDecimalOrZero(sapItemObj.getString("SJNUM"))));// 可发数量
					orderItem.put("spec_stock", sapItemObj.getString("SOBKZ"));// 特殊库存类型
					orderItem.put("spec_stock_code", sapItemObj.getString("ZTSKC"));// 特殊库存编号
					orderItem.put("spec_stock_name", sapItemObj.getString("ZTSMS"));// 特殊库存描述
					orderItem.put("refer_receipt_code", sapItemObj.getString("EBELN"));// 参考单据编号(销售订单编号)
					orderItem.put("refer_receipt_rid", sapItemObj.getString("EBELP"));// 参考单据序号

					isFocusBatch = stockLocationDao.selectPlanByLocation(locationVo.getLocationId());
					orderItem.put("is_focus_batch", isFocusBatch == null ? "0" : isFocusBatch);// 批次信息是否强控
					itemList.add(orderItem);
				}
			}
		}

		return itemList;
	}

	private Map<String, Object> formatSapData(String isSuccess, String msg, String postDate, JSONArray msegList,
			JSONArray anlaList) {
		Map<String, Object> wmsData = new HashMap<String, Object>();
		wmsData.put("isSuccess", isSuccess);
		wmsData.put("message", msg);
		if ("S".equals(isSuccess)) {
			wmsData.put("postDate", postDate);
			List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

			for (int i = 0; i < msegList.size(); i++) {
				JSONObject mseg = msegList.getJSONObject(i);
				Map<String, String> data = new HashMap<String, String>();
				data.put("matDocCode", mseg.getString("MBLNR"));
				data.put("matDocRid", mseg.getString("ZEILE"));
				data.put("matDocYear", mseg.getString("MJAHR"));
				if (mseg.has("CHARG")) {
					data.put("batch", mseg.getString("CHARG"));
				}

				data.put("moveTypeCode", mseg.getString("BWART"));
				data.put("specStock", mseg.getString("SOBKZ"));
				data.put("specStockCode", mseg.getString("ZTSKC"));
				data.put("specStockName", mseg.getString("ZTSMS"));
				data.put("matCode", mseg.getString("MATNR"));
				data.put("ftyCode", mseg.getString("WERKS"));
				data.put("locationCode", mseg.getString("LGORT"));
				data.put("supplierCode", mseg.getString("LIFNR"));
				data.put("supplierName", mseg.getString("VNAME1"));
				data.put("customerCode", mseg.getString("KUNNR"));
				data.put("customerName", mseg.getString("CNAME1"));
				data.put("saleOrderCode", mseg.getString("VBELN"));
				data.put("saleOrderProjectCode", mseg.getString("KDPOS"));
				data.put("saleOrderDeliveredPlan", mseg.getString("KDEIN"));
				data.put("standardCurrencyMoney", mseg.getString("DMBTR"));
				data.put("qty", mseg.getString("MENGE"));
				data.put("unitCode", mseg.getString("MEINS"));
				data.put("debitCredit", mseg.getString("SHKZG"));
				data.put("stockStatus", mseg.getString("INSMK"));
				data.put("stockOutputCode", mseg.getString("ZWMSBH"));
				data.put("stockOutputRid", mseg.getString("ZWMSXM"));
				data.put("stockOutputPid", mseg.getString("ZNON"));

				dataList.add(data);
			}

			wmsData.put("sapList", dataList);

			if (anlaList != null) {
				List<Map<String, String>> assetList = new ArrayList<Map<String, String>>();

				for (int i = 0; i < anlaList.size(); i++) {
					JSONObject anla = anlaList.getJSONObject(i);
					Map<String, String> data = new HashMap<String, String>();
					data.put("assetCardCode", anla.getString("ANLN1"));
					data.put("assetCardName", anla.getString("TXT50"));
					data.put("stockOutputRid", anla.getString("ZDJXM"));
					data.put("stockOutputPid", anla.getString("ZNON"));
					data.put("matDocYear", anla.getString("MJAHR"));
					data.put("matDocRid", anla.getString("ZEILE"));
					data.put("matReqCode", anla.getString("ZLLDH"));
					data.put("matDocRid", anla.getString("ZLLDXH"));
					data.put("isSend", anla.getString("ZFLAG"));

					assetList.add(data);

				}
				if(assetList.size()>0){
					wmsData.put("assetList", assetList);
				}
				
			}
		}
		return wmsData;
	}

	/**
	 * 领料过账排序
	 * 
	 * @date 2017年11月13日
	 * @author 高海涛
	 * @param array
	 * @return
	 */
	private JSONArray sortJsonArrayForMatReq(JSONArray array) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		JSONObject jsonObj = null;
		for (int i = 0; i < array.size(); i++) {
			jsonObj = (JSONObject) array.get(i);
			list.add(jsonObj);
		}

		// 排序操作
		Collections.sort(list, new Comparator<JSONObject>() {
			/*
			 * int compare(JSONObject o1, JSONObject o2) 返回一个基本类型的整型， 返回负数表示：o1
			 * 小于o2， 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
			 */
			public int compare(JSONObject o1, JSONObject o2) {
				Long zdjxmO1 = Long.parseLong(o1.getString("ZDJXM"));
				Long zdjxmO2 = Long.parseLong(o2.getString("ZDJXM"));
				if (zdjxmO1 > zdjxmO2) {
					return 1;
				}
				if (zdjxmO1 < zdjxmO2) {
					return -1;
				}

				return 0;
			}
		});

		return JSONArray.fromObject(list);
	}

	/**
	 * 对返回值排序
	 * 
	 * @date 2017年10月9日
	 * @author 高海涛
	 * @param array
	 * @param dateName
	 */
	private JSONArray sortJsonArray(JSONArray array) {

		List<JSONObject> list = new ArrayList<JSONObject>();
		JSONObject jsonObj = null;
		for (int i = 0; i < array.size(); i++) {
			jsonObj = (JSONObject) array.get(i);
			list.add(jsonObj);
		}
		// 排序操作
		Collections.sort(list, new Comparator<JSONObject>() {
			/*
			 * int compare(JSONObject o1, JSONObject o2) 返回一个基本类型的整型， 返回负数表示：o1
			 * 小于o2， 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
			 */
			public int compare(JSONObject o1, JSONObject o2) {
				// 物料凭证号
				Long mblnrO1 = Long.parseLong(o1.getString("MBLNR"));
				Long mblnrO2 = Long.parseLong(o2.getString("MBLNR"));
				// 借贷标示
				String shkzgO1 = o1.getString("SHKZG");

				// 1. 按照凭证号排序
				if (mblnrO1 > mblnrO2) {
					return 1;
				}
				if (mblnrO1 < mblnrO2) {
					return -1;
				}
				if (mblnrO1 == mblnrO2) {
					// 返回值中存在ABLAD
					if (!"".equals(o1.getString("ABLAD"))) {
						String[] ablad1 = o1.getString("ABLAD").split("-");
						String[] ablad2 = o2.getString("ABLAD").split("-");
						// 2. 按照ABLAD 第二位 排序
						if (Long.parseLong(ablad1[1]) > Long.parseLong(ablad2[1])) {
							return 1;
						}
						if (Long.parseLong(ablad1[1]) < Long.parseLong(ablad2[1])) {
							return -1;
						}
						if (Long.parseLong(ablad1[1]) == Long.parseLong(ablad2[1])) {
							// 3. 按照ABLAD 第三位 排序
							if (Long.parseLong(ablad1[2]) > Long.parseLong(ablad2[2])) {
								return 1;
							}
							if (Long.parseLong(ablad1[2]) < Long.parseLong(ablad2[2])) {
								return -1;
							}
							if (Long.parseLong(ablad1[2]) == Long.parseLong(ablad2[2])) {
								// 4. 按照H,S排序
								if ("S".equals(shkzgO1)) {
									return 1;
								}
								if ("H".equals(shkzgO1)) {
									return -1;
								} // 4-end
							} // 3-end
						} // 2-end
					} else {// 返回值中不存在ABLAD

						if (!"".equals(o1.getString("ZWMSXM"))) {
							Long zwmsxmO1 = Long.parseLong(o1.getString("ZWMSXM"));
							Long zwmsxmO2 = Long.parseLong(o2.getString("ZWMSXM"));
							// 2. 按照ZWMSXM 排序
							if (zwmsxmO1 > zwmsxmO2) {
								return 1;
							}
							if (zwmsxmO1 < zwmsxmO2) {
								return -1;
							}
							if (zwmsxmO1 == zwmsxmO2) {
								if (!"".equals(o1.getString("ZNON"))) {
									Long znonO1 = Long.parseLong(o1.getString("ZNON"));
									Long znonO2 = Long.parseLong(o2.getString("ZNON"));
									// 3. 按照ZNON 排序
									if (znonO1 > znonO2) {
										return 1;
									}
									if (znonO1 < znonO2) {
										return -1;
									}
									if (znonO1 == znonO2) {
										// 4. 按照H,S排序
										if ("S".equals(shkzgO1)) {
											return 1;
										}
										if ("H".equals(shkzgO1)) {
											return -1;
										} // 4-end
									} // 3-end
								}
							} // 2-end

						}

					}

				} // 1-end
				return 0;
			}
		});

		return JSONArray.fromObject(list);
	}
}
