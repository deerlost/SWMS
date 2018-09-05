package com.inossem.wms.service.biz.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.inossem.wms.dao.biz.BizDefinedReportMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.model.biz.BizDefinedReport;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.vo.BizDefinedReportVo;
import com.inossem.wms.service.biz.IDefinedReportService;
import com.inossem.wms.util.UtilObject;
import net.sf.json.JSONObject;

@Service
@Transactional
public class DefinedReportServiceImpl implements IDefinedReportService {
	
	@Autowired
	private BizDefinedReportMapper definedReportDao;
	
	@Autowired
	private SequenceDAO sequenceDAO;
	
	@Override
	public List<BizDefinedReportVo> getDefinedReportList(Map<String, Object> param) {
		return definedReportDao.getDefinedReportListOnPaging(param);
	}
	
	@Override
	public BizDefinedReportVo getDetailById(Integer definedId) {
		BizDefinedReportVo definedReportVo = definedReportDao.selectByPrimaryKey(definedId);
		definedReportVo.setSearchField(searchField(definedReportVo.getDefinedType(),definedId));
		definedReportVo.setQueryField(queryField(definedReportVo.getDefinedType(),definedId));
		return definedReportVo;
	}
	
	private List<Map<String,Object>> searchField(Byte type,Integer definedId){
		return definedReportDao.getDefinedFieldBySearch(type, definedId);
	}
	
	private List<Map<String,Object>> queryField(Byte type,Integer definedId){
		return definedReportDao.getDefinedFieldByQuery(type, definedId);
	}
	
//	private Map<String,Object> getStockReport() {
//		Map<String,Object> stockReport = new HashMap<String,Object>();
//		Map<String,Object> searchField = new HashMap<String,Object>();
//		List<String> searchFieldCh = Arrays.asList("工厂","库存地点","库存类型","物料编码","物料描述","入库时间","物料组","存储区","仓位","托盘标签","生产批次");
//		List<String> searchFieldEn = Arrays.asList("fty_id","location_id","stock_type_id","mat_code","mat_name","input_date","mat_group_name","area_code","position_code","pallet_code","product_batch");
//		searchField.put("search_field_en", searchFieldEn);
//		searchField.put("search_field_ch", searchFieldCh);
//		Map<String,Object> queryField = new HashMap<String,Object>();
//		List<String> queryFieldCn = Arrays.asList("物料组","物料编码","物料描述","包装类型","包装规格","计量单位","库存数量","库存状态","工厂","库存地点","存储区","仓位","仓位库存","托盘标签","SN号码","生产批次","ERP批次","质检批次","批次","入库日期","库龄(天)");
//		List<String> queryFieldEn = Arrays.asList("mat_group_name","mat_code","mat_name","package_type_code","package_standard_ch","unit_name","stock_qty","status","fty_name","location_name","area_name","position_code","qty","pallet_code","package_code","product_batch","erp_batch","quality_batch","batch","input_date","stcok_days");
//		queryField.put("query_field_en", queryFieldEn);
//		queryField.put("query_field_cn", queryFieldCn);
//		stockReport.put("search_field", searchField);
//		stockReport.put("query_field", queryField);
//		return stockReport;
//	}
//	
//	private Map<String,Object> getInputReport() {
//		Map<String,Object> inputReport = new HashMap<String,Object>();
//		Map<String,Object> searchField = new HashMap<String,Object>();
//		List<String> searchFieldCh = Arrays.asList("生产工厂","生产订单编号","物料编码","物料描述","产品线","装置","班次","单据类型","创建人","创建日期","入库类型","入库单编号","入库单状态");
//		List<String> searchFieldEn = Arrays.asList("fty_id","stock_input_code","mat_code","mat_name","product_line_id","installation_id","class_type_id","purchase_order_type_name","create_user","create_date","receipt_type_name","stock_input_code","status_name");
//		searchField.put("search_field_en", searchFieldEn);
//		searchField.put("search_field_ch", searchFieldCh);
//		Map<String,Object> queryField = new HashMap<String,Object>();
//		List<String> queryFieldCn = Arrays.asList("交货单编号","交货单行项目序号","物料编码","物料描述","交货数量","过账日期","物料凭证","移动类型","出库类型","发出工厂","出库单编号","出库单行项目号","出库单状态","销售订单号","订单类型","客户名称","创建人","创建日期","合同编号");
//		List<String> queryFieldEn = Arrays.asList("refer_receipt_code","refer_receipt_rid","mat_code","mat_name","output_qty","posting_date","mat_doc_code","move_type_name","receipt_type_name","fty_name","stock_output_code","stock_output_rid","status_name","sale_order_code","order_type_name","receive_name","create_user","create_date","preorder_id");
//		queryField.put("query_field_en", queryFieldEn);
//		queryField.put("query_field_cn", queryFieldCn);
//		inputReport.put("search_field", searchField);
//		inputReport.put("query_field", queryField);
//		return inputReport;
//	}

	@Override
	public Map<String, Object> getDefinedField(Byte type) {
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("search_field",  definedReportDao.getDefinedField(type,"0"));
		result.put("query_field",  definedReportDao.getDefinedField(type,"1"));
		return result;
	}

	@Override
	public Integer saveData(JSONObject jsonData, String userId) throws Exception {
		Integer definedId = 0;
		if(jsonData.containsKey("defined_id")) {
			definedId = UtilObject.getIntOrZero(jsonData.get("defined_id"));
        }
		BizDefinedReport record = new BizDefinedReport();
		record.setDefinedId(definedId);
		record.setDefinedName(UtilObject.getStringOrEmpty(jsonData.get("defined_name")));
		record.setDefinedType(UtilObject.getByteOrNull(jsonData.get("defined_type")));
		record.setIsDelete(EnumBoolean.FALSE.getValue());
		if(definedId.equals(0)) {
			record.setCreateTime(new Date());
			record.setCreateUser(userId);
			record.setModifyTime(new Date());
			record.setModifyUser(userId);
			record.setDefinedCode(sequenceDAO.selectNextVal("defined_report")+"");
			definedReportDao.insertSelective(record);
		}else {
			record.setModifyTime(new Date());
			record.setModifyUser(userId);
			definedReportDao.updateByPrimaryKeySelective(record);
		}
		definedReportDao.deleteRelReportField(record.getDefinedId());
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("defined_id",record.getDefinedId());
		param.put("list",jsonData.getJSONArray("field_list"));
		definedReportDao.insertRelReportField(param);
		definedReportDao.deleteRelReportUser(definedId);
		param = new HashMap<String,Object>();
		param.put("defined_id",record.getDefinedId());
		param.put("list",jsonData.getJSONArray("user_list"));
		definedReportDao.insertRelReportUser(param);
		return definedId;
	}
	
	@Override
	public List<Map<String,Object>> getDataList(JSONObject jsonData){
		return definedReportDao.selectDataListBySql(drawDataSql(jsonData));
	}
	
	@Override
	public Integer getDataCount(JSONObject jsonData){
		return definedReportDao.selectCountBySql(drawCountSql(jsonData));
	}

	private String drawDataSql(JSONObject jsonData) {
		Byte type = UtilObject.getByteOrNull(jsonData.get("defined_type"));
		Integer definedId = UtilObject.getIntOrZero(jsonData.get("defined_id"));
		String sql = "select ";
		List<Map<String,Object>> queryFiledList = queryField(type,definedId);
		for(Map<String,Object> map : queryFiledList) {
			String field = UtilObject.getStringOrEmpty(map.get("field_en"));
			sql += field+",";
		}
		sql += sql.substring(0,sql.length()-1)+" ";
		String table = "";
		switch (type) {
			case (byte)1 : table = ""; break;
			case (byte)2 : table = "from v_stock_output"; break;
			case (byte)3 : table = ""; break;
			case (byte)4 : table = ""; break;
			case (byte)5 : table = ""; break;
		}
		sql += table;
		List<Map<String,Object>> searchFiledList = searchField(type, definedId);
		if(searchFiledList!=null && !searchFiledList.isEmpty()) {
			sql +=" where 1=1 ";
			for(Map<String,Object> map : searchFiledList) {
				String field = UtilObject.getStringOrEmpty(map.get("field_en"));
				sql += "and "+field+" = "+UtilObject.getStringOrEmpty(jsonData.get(field));;
			}
		}
		Integer pageIndex = UtilObject.getIntOrZero(jsonData.get("page_index"));
		Integer pageSize = UtilObject.getIntOrZero(jsonData.get("page_size"));
		sql += " limit "+ (pageIndex-1)*pageSize + "," +pageSize;
		sql += ";";
		return sql;
	}
	
	private String drawCountSql(JSONObject jsonData) {
		String sql = "select ";
		sql += "count(1) ";
		Integer definedId = UtilObject.getIntOrZero(jsonData.get("defined_id"));
		Byte type = UtilObject.getByteOrNull(jsonData.get("defined_type"));
		String table = "";
		switch (type) {
			case (byte)1 : table = ""; break;
			case (byte)2 : table = "from v_stock_output"; break;
			case (byte)3 : table = ""; break;
			case (byte)4 : table = ""; break;
			case (byte)5 : table = ""; break;
		}
		sql += table;
		List<Map<String,Object>> searchFiledList = searchField(type, definedId);
		if(searchFiledList!=null && !searchFiledList.isEmpty()) {
			sql +=" where 1=1 ";
			for(Map<String,Object> map : searchFiledList) {
				String field = UtilObject.getStringOrEmpty(map.get("field_en"));
				sql += "and "+field+" = "+UtilObject.getStringOrEmpty(jsonData.get(field));;
			}
		}
		sql += ";";
		return sql;
	}

	@Override
	public Integer deleteById(Integer definedId) throws Exception {
		return definedReportDao.deleteByPrimaryKey(definedId);
	}
}
