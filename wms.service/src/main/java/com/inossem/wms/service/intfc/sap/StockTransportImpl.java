package com.inossem.wms.service.intfc.sap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.inossem.wms.constant.Const;
import com.inossem.wms.dao.biz.BizStockTransportHeadMapper;
import com.inossem.wms.dao.biz.BizStockTransportItemMapper;
import com.inossem.wms.model.vo.BizStockTransportHeadVo;
import com.inossem.wms.model.vo.BizStockTransportItemVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.intfc.IStockTransport;
import com.inossem.wms.util.UtilREST;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 转储SAP接口
 * 
 * @author 高海涛
 * @date 2018年2月6日
 */
@Service("stockTransportImpl")
public class StockTransportImpl implements IStockTransport {

	@Resource
	private BizStockTransportHeadMapper transportHeadDao;
	@Resource
	private BizStockTransportItemMapper transportItemDao;
	@Resource
	private ICommonService commonService;

	public Map<String, Object> postingForTransport(int stockTransportId, String userId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stockTransportId", stockTransportId);

		BizStockTransportHeadVo head = transportHeadDao.selectHeadById(param);
		List<BizStockTransportItemVo> itemList = transportItemDao.selectItemById(param);

		/***************************************
		 * IMPORT
		 ***************************************/
		JSONObject I_IMPORT = this.setImport("15", userId);
		I_IMPORT.put("ZDJBH", head.getStockTransportCode());

		/***************************************
		 * HEAD
		 ***************************************/
		JSONArray i_headAry = new JSONArray();
		JSONObject i_head = new JSONObject();
		String postDate = commonService.getPostDate(head.getFtyId());

		i_head.put("DOC_DATE", postDate); // 凭证日期
		i_head.put("PSTNG_DATE", postDate); // 过帐日期
		i_head.put("ZDJBH", head.getStockTransportCode()); // 转储申请号
		i_headAry.add(i_head);

		/****************************************
		 * ITEM
		 ***************************************/
		JSONArray I_ITEM = new JSONArray();

		for (BizStockTransportItemVo item : itemList) {
			JSONObject itemMap = new JSONObject();
			itemMap.put("ZDJBH", head.getStockTransportCode());
			itemMap.put("ZDJXM", item.getStockTransportRid());
			itemMap.put("MATERIAL", item.getMatCode());
			itemMap.put("PLANT", item.getFtyCode());
			itemMap.put("STGE_LOC", item.getLocationCode());
			itemMap.put("MOVE_TYPE", head.getMoveTypeCode());
			itemMap.put("SPEC_STOCK", item.getSpecStockOutput());// 原特殊库存标识
			itemMap.put("ZTSKC1", item.getSpecStockCodeOutput());// 原特殊库存编号
			itemMap.put("VENDOR", item.getSpecStockCodeOutput());
			itemMap.put("ENTRY_QNT", item.getTransportQty());
			itemMap.put("MOVE_MAT", item.getInputMatCode());
			itemMap.put("MOVE_PLANT", item.getInputFtyCode());
			itemMap.put("MOVE_STLOC", item.getInputLocationCode());
			itemMap.put("ZKCBS", item.getSpecStockInput());
			itemMap.put("ZTSKC2", item.getSpecStockCodeInput());

			I_ITEM.add(itemMap);
		}

		JSONObject params = new JSONObject();
		params.put("I_IMPORT", I_IMPORT);
		params.put("HEAD", i_headAry);
		params.put("ITEM", I_ITEM);

		JSONObject result = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_15?sap-client=" + Const.SAP_API_CLIENT, params, 3);

		JSONObject RETURN = result.getJSONObject("RETURN");

		return formatSapData(RETURN.getString("TYPE"), RETURN.getString("MESSAGE"), postDate,
				result.getJSONArray("E_MSEG"));
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

	private Map<String, Object> formatSapData(String isSuccess, String msg, String postDate, JSONArray msegList) {
		Map<String, Object> wmsData = new HashMap<String, Object>();
		wmsData.put("isSuccess", isSuccess);
		wmsData.put("message", msg);
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
			data.put("stockTransportCode", mseg.getString("ZWMSBH"));
			data.put("stockTransportRid", mseg.getString("ZWMSXM"));

			dataList.add(data);
		}

		wmsData.put("sapList", dataList);

		return wmsData;
	}
}
