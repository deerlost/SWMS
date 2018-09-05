package com.inossem.print;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.core.Constant;
import com.inossem.core.QRcodeUtil;
import com.inossem.service.PrintService;
import com.inossem.service.impl.CWPrintLabelZpl;
import com.inossem.service.impl.JLPrintImports;
import com.inossem.service.impl.JLPrintLabelZpl;
import com.inossem.service.impl.JLPrintOutOrder;
import com.inossem.service.impl.JLPrintQuality;
import com.inossem.service.impl.YTPrintInventory;
import com.inossem.service.impl.YTPrintLabel;
import com.inossem.template.CopyTemplate;
import com.inossem.template.impl.CWPrintErpStockImpl;
import com.inossem.template.impl.CWPrintLimsImpl;
import com.inossem.template.impl.CWPrintNewOutputImpl;
import com.inossem.template.impl.CWPrintNewProTransImpl;
import com.inossem.template.impl.CWPrintNewTaskImpl;
import com.inossem.template.impl.CWPrintOutputImpl;
import com.inossem.template.impl.CWPrintProTransImpl;
import com.inossem.template.impl.CWPrintTaskImpl;
import com.inossem.template.impl.CWPrintTransInputImpl;
import com.inossem.template.impl.YTLabelImpl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author 高海涛
 * @version 2017年9月21日 打印Controller
 */

@Controller
public class Print {

	/**
	 * 标签打印接口
	 * 
	 * @date 2017年9月22日
	 * @author 高海涛
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/ytLabel", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> ytLabel(@RequestBody JSONObject json) {

		PrintService printReport = new YTPrintLabel();

		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<Map> printData = (List<Map>) json.get("print_data");
		String labelType = printData.get(0).get("type").toString(); // 标签类型
																	// L1-批次
																	// L2-物料
																	// L3-仓位

		Map<String, Object> reMap = new HashMap<String, Object>();

		reMap.put("fileName", printReport.mdPrint(printData, labelType));
		return reMap;
	}

	/**
	 * 盘点报表打印
	 * 
	 * @date 2017年9月22日
	 * @author 高海涛
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/ytInventory", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> ytInventory(@RequestBody JSONObject json) {

		PrintService printReport = new YTPrintInventory();

		@SuppressWarnings("rawtypes")
		List<Map> data = new ArrayList<Map>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", json.get("print_data"));
		map.put("header", json.get("header_data"));

		data.add(map);

		Map<String, Object> reMap = new HashMap<String, Object>();

		reMap.put("fileName", printReport.mdPrint(data, ""));
		return reMap;
	}

	/**
	 * zpl标签打印
	 * 
	 * @date 2017年10月31日
	 * @author 高海涛
	 * @return
	 */
	@RequestMapping(value = "/jlLabelZpl", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> jlLabelZpl(@RequestBody JSONObject json) {
		PrintService printReport = new JLPrintLabelZpl();

		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<Map> printData = (List<Map>) json.get("printdata");
		String labelType = printData.get(0).get("type").toString(); // 标签类型
																	// L1-批次
																	// L2-物料
																	// L3-仓位

		printReport.mdPrint(printData, labelType);

		return null;
	}

	@RequestMapping(value = "/jlImports", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> ytImports(@RequestBody JSONObject json) {

		PrintService printReport = new JLPrintImports();

		@SuppressWarnings("rawtypes")
		List<Map> data = new ArrayList<Map>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", json.get("printdata"));
		map.put("header", json.get("headerdata"));

		data.add(map);

		Map<String, Object> reMap = new HashMap<String, Object>();

		reMap.put("fileName", printReport.mdPrint(data, ""));
		return reMap;
	}

	/**
	 * 质检单
	 * 
	 * @date 2017年11月10日
	 * @author 高海涛
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/jlQuality", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> ytQuality(@RequestBody JSONObject json) {

		PrintService printReport = new JLPrintQuality();

		@SuppressWarnings("rawtypes")
		List<Map> data = new ArrayList<Map>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", json.get("printdata"));

		data.add(map);

		Map<String, Object> reMap = new HashMap<String, Object>();

		reMap.put("fileName", printReport.mdPrint(data, ""));
		return reMap;
	}

	/**
	 * 出库单
	 * 
	 * @date 2017年11月10日
	 * @author 高海涛
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/jlOutOrder", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> ytOutOrder(@RequestBody JSONObject json) {

		PrintService printReport = new JLPrintOutOrder();

		@SuppressWarnings("rawtypes")
		List<Map> data = new ArrayList<Map>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", json.get("printdata"));
		map.put("header", json.get("headerdata"));

		data.add(map);

		Map<String, Object> reMap = new HashMap<String, Object>();

		reMap.put("fileName", printReport.mdPrint(data, ""));
		return reMap;
	}

	/**
	 * 领料单二维码
	 * 
	 * @date 2017年11月30日
	 * @author 高海涛
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/ytLldQR", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> ytLldQR(@RequestBody JSONObject json) {
		CopyTemplate report = new YTLabelImpl();

		String imagePath = report.getQRPath() + Constant.OUTPUTFILE_TYPE_PNG;
		String zlldh = json.getString("mat_req_code");
		JSONArray array = json.getJSONArray("list");
		String list = "";
		for (int i = 0; i < array.size(); i++) {
			list = list + ",[{" + array.getJSONObject(i).getString("mat_req_rid") + ","
					+ array.getJSONObject(i).getString("qty") + "}]";
		}
		QRcodeUtil.zxingCodeCreate("\"" + zlldh + "\"" + list, 400, 400, imagePath, "png");

		Map<String, Object> reMap = new HashMap<String, Object>();

		reMap.put("fileName", imagePath);
		return reMap;
	}

	/**
	 * @Description: TODO(打印川维生产转运单)</br>
	 * @Title: cwProTrans </br>
	 * @param @param
	 *            json
	 * @param @return
	 *            设定文件</br>
	 * @return Map<String,Object> 返回类型</br>
	 * @throws Exception
	 * @throws</br>
	 */
	@RequestMapping(value = "/cwProTrans", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> cwProTrans(@RequestBody JSONObject json) throws Exception {

		CopyTemplate printReport = new CWPrintNewProTransImpl();
		Map<String, Object> reMap = new HashMap<String, Object>();

		@SuppressWarnings("rawtypes")
		List<Map> data = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", json.get("print_item"));
		map.put("header", json.get("print_head"));

		data.add(map);

		reMap.put("fileName", printReport.mdPrint(data, ""));
		return reMap;
	}

	/**
	 * @Description: TODO(打印川维Erp库存盘点)</br>
	 * @Title: cwERPStock </br>
	 * @param @param
	 *            json
	 * @param @return
	 * @param @throws
	 *            Exception 设定文件</br>
	 * @return Map<String,Object> 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = "/cwErpStock", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> cwERPStock(@RequestBody JSONObject json) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		@SuppressWarnings("rawtypes")
		List<Map> data = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();

		CopyTemplate printReport = new CWPrintErpStockImpl();

		map.put("list", json.get("print_item"));
		map.put("header", json.get("print_head"));
		data.add(map);
		reMap.put("fileName", printReport.mdPrint(data, ""));

		return reMap;
	}

	/**
	 * @Description: TODO(打印川维发货单)</br>
	 * @Title: cwOutput </br>
	 * @param @param
	 *            json
	 * @param @return
	 * @param @throws
	 *            Exception 设定文件</br>
	 * @return Map<String,Object> 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = "/cwOutputs", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> cwOutput(@RequestBody JSONObject json) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		@SuppressWarnings("rawtypes")
		List<Map> data = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();

		CopyTemplate printReport = new CWPrintNewOutputImpl();

		map.put("list", json.get("print_item"));
		map.put("header", json.get("print_head"));
		data.add(map);
		reMap.put("fileName", printReport.mdPrint(data, ""));

		return reMap;
	}

	/**
	 * @Description: TODO(打印质检单)</br>
	 * @Title: cwLims </br>
	 * @param @param
	 *            json
	 * @param @return
	 * @param @throws
	 *            Exception 设定文件</br>
	 * @return Map<String,Object> 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = "/cwLims", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> cwLims(@RequestBody JSONObject json) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		@SuppressWarnings("rawtypes")
		List<Map> data = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();

		CopyTemplate printReport = new CWPrintLimsImpl();

		map.put("list", json.get("print_item"));
		map.put("header", json.get("print_head"));
		data.add(map);
		reMap.put("fileName", printReport.mdPrint(data, ""));

		return reMap;
	}

	/**
	 * @Description: TODO(d打印川维上下架)</br>
	 * @Title: cwTask </br>
	 * @param @param
	 *            json
	 * @param @return
	 * @param @throws
	 *            Exception 设定文件</br>
	 * @return Map<String,Object> 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = "/cwTask", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> cwTask(@RequestBody JSONObject json) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		@SuppressWarnings("rawtypes")
		List<Map> data = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();

		CopyTemplate printReport = new CWPrintNewTaskImpl();

		map.put("list", json.get("print_item"));
		map.put("header", json.get("print_head"));
		data.add(map);
		reMap.put("fileName", printReport.mdPrint(data, ""));

		return reMap;
	}
	
	/**
	* @Description: TODO(打印转储单)</br>
	   @Title: cwTrans </br>
	* @param @param json
	* @param @return
	* @param @throws Exception    设定文件</br>
	* @return Map<String,Object>    返回类型</br>
	* @throws</br>
	*/
	@RequestMapping(value = "/cwTrans", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> cwTrans(@RequestBody JSONObject json) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		@SuppressWarnings("rawtypes")
		List<Map> data = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();

		CopyTemplate printReport = new CWPrintTransInputImpl();

		map.put("list", json.get("print_item"));
		map.put("header", json.get("print_head"));
		data.add(map);
		reMap.put("fileName", printReport.mdPrint(data, ""));

		return reMap;
	}

	/**
	 * 川维标签打印
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/cwLabelZpl", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> cwLabelZpl(@RequestBody JSONObject json) {
		PrintService printReport = new CWPrintLabelZpl();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<Map> printData = (List<Map>) json.get("print_data");
		String labelType = printData.get(0).get("type").toString(); 
		Map<String, Object> reMap = new HashMap<String, Object>();
		reMap.put("fileName", printReport.mdPrint(printData, labelType));
		reMap.put("type", labelType);
		return reMap;
	}
}
