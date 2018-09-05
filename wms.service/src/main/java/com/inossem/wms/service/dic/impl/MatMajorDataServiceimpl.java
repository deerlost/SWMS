package com.inossem.wms.service.dic.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.inossem.wms.dao.dic.DicMaterialGroupMapper;
import com.inossem.wms.dao.dic.DicMaterialMapper;
import com.inossem.wms.dao.dic.DicWarehouseMatMapper;
import com.inossem.wms.exception.ExcelCellTypeException;
import com.inossem.wms.model.ExcelCellType;
import com.inossem.wms.model.dic.DicBatchSpecClassify;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.dic.DicMaterialGroup;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.dic.IMatMajorDataService;
import com.inossem.wms.service.intfc.IMaterial;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilString;
import com.inossem.wms.util.UtilTimestamp;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 物料主数据
 * 
 * @author 刘宇 2018.02.27
 *
 */
@Transactional
@Service("matMajorDataService")
public class MatMajorDataServiceimpl implements IMatMajorDataService {
	@Autowired
	private DicMaterialMapper dicMaterialDao;

	@Autowired
	private DicWarehouseMatMapper warehouseMatDao;
	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private ICommonService commonService;

	@Autowired
	private IMaterial materialSap;
	
	@Autowired
	private DicMaterialGroupMapper materialGroupMapper;

	private static Logger logger = LoggerFactory.getLogger(MatMajorDataServiceimpl.class);

	@Override
	public List<Map<String, Object>> listMatMajorDataOnPaging(String condition, int pageIndex, int pageSize, int total,
			boolean sortAscend, String sortColumn) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("condition", condition);// 查询条件
		map.put("pageSize", pageSize);
		map.put("pageIndex", pageIndex);
		map.put("paging", true);
		map.put("sortAscend", sortAscend);
		map.put("sortColumn", sortColumn);
		return dicMaterialDao.selectMatMajorDataOnPaging(map);
	}

	@Override
	public int syncSapMaterial(String matCode) throws Exception {
		List<DicMaterial> matList = materialSap.syncMaterial(matCode);
		int count = 0;
		if (matList != null && matList.size() > 0) {
			for (DicMaterial mat : matList) {
				count = count + dicMaterialDao.syncMatFromSap(mat);
				if (mat.getMatId() != null) {
					warehouseMatDao.insertDataForSapMatSync(mat.getMatId());
				}

			}
		}
		return count;
	}
	
	@Override
	public int syncSapMaterialFromOracle(String matCode) throws Exception {
		int count = 0;
		
			List<DicMaterial> matList = this.getMatListFromOracle(matCode.trim());
			if (matList != null && matList.size() > 0) {
				for (DicMaterial mat : matList) {
					count = count + dicMaterialDao.syncMatFromSap(mat);
					if (mat.getMatId() != null) {
						warehouseMatDao.insertDataForSapMatSync(mat.getMatId());
					}

				}
			}
					
		return count;
	}

	List<DicMaterial> getMatListFromOracle(String matCodeStr) throws Exception {
		List<DicMaterial> matList = new ArrayList<DicMaterial>();
		int strLength = matCodeStr.length();
		String matCodeTemp = matCodeStr;
		if(strLength<18){
			while(strLength<18){
				StringBuffer sb = new StringBuffer(); 
				sb.append("0").append(matCodeStr);
				matCodeStr = sb.toString();
				strLength = matCodeStr.length();
			}
		}
		
		String sqlStr = "SELECT\r\n" 
				
				+ " SUBSTR(MARA.MATNR, 3, 18) MATNR,\r\n" 
				+ " MAKT.MAKTX,\r\n" 
				+ " MARA.LVORM,\r\n"
				+ " MARA.MEINS,\r\n" 
				+ " MARA.MATKL,\r\n" 
				+ "	T023T.WGBEZ,\r\n" 
				+ "	MARA.MTART,\r\n"
				+ "	MARA.LAENG,\r\n" 
				+ "	MARA.BREIT,\r\n" 
				+ "	MARA.HOEHE,\r\n" 
				+ "	MARA.MEABM,\r\n"
				+ "	MARA.BRGEW,\r\n" 
				+ "	MARA.NTGEW,\r\n" 
				+ "	MARA.GEWEI,\r\n" 
				+ "	MARA.VOLUM,\r\n" 
				+ "	MARA.VOLEH\r\n" 
				+ " FROM\r\n" 
				+ "	MARA\r\n"
				+ "	inner join T023T on T023T.MATKL = MARA.MATKL AND T023T.SPRAS = '1'"
				+ "	inner join MAKT on MAKT.MATNR = MARA.MATNR And MAKT.SPRAS = '1' AND ";
			if(StringUtils.hasText(matCodeTemp)){
				sqlStr = sqlStr + "MARA.MATNR='" + matCodeStr + "'";
			}else{
				// 同步当天物料
				String dateStr = UtilTimestamp.formartDate(new Date(), "yyyyMMdd");
				sqlStr = sqlStr + "MARA.ERSDA='" + dateStr + "'";
			}
				
				
		List<Map<String, Object>> matMapList = commonService.getDataFromOracle(sqlStr);
		if(matMapList!=null&&matMapList.size()>0){
			for(Map<String, Object> matMap:matMapList){

				DicMaterial material = new DicMaterial();
				String matCode = UtilString.getStringOrEmptyForObject(matMap.get("MATNR"));
				material.setMatCode(matCode);
				Integer matId = dictionaryService.getMatIdByMatCode(matCode);
				if (matId != null && matId.compareTo(0) == 1) {
					material.setMatId(matId);
				}
				// matJson.getString("LVORM")删除标记
				String matName = UtilString.getStringOrEmptyForObject(matMap.get("MAKTX"));
				material.setMatName(matName);
				String unitCode = UtilString.getStringOrEmptyForObject(matMap.get("MEINS"));
				material.setUnitCode(unitCode);
				Integer unitId = dictionaryService.getUnitIdByUnitCode(unitCode);
				material.setUnitId(unitId);
				String matGroupCode = UtilString.getStringOrEmptyForObject(matMap.get("MATKL"));
				Integer matGroupId = dictionaryService.getMatGroupIdByCode(matGroupCode);
				String matGroupName = UtilString.getStringOrEmptyForObject(matMap.get("WGBEZ"));
				if(matGroupId==null||matGroupId==0){
					// 生成物料组
					DicMaterialGroup group = new DicMaterialGroup();
					group.setMatGroupCode(matGroupCode);
					group.setMatGroupName(matGroupName);
					materialGroupMapper.insertSelective(group);
					matGroupId = group.getMatGroupId();
				}
				material.setMatGroupId(matGroupId);
				
				String matType = UtilString.getStringOrEmptyForObject(matMap.get("MTART"));
				material.setMatType(matType);
				String length = UtilString.getStringOrEmptyForObject(matMap.get("LAENG"));
				material.setLength(UtilObject.getBigDecimalOrZero(length));
				String width = UtilString.getStringOrEmptyForObject(matMap.get("BREIT"));
				material.setWidth(UtilObject.getBigDecimalOrZero(width));
				String height = UtilString.getStringOrEmptyForObject(matMap.get("HOEHE"));
				material.setHeight(UtilObject.getBigDecimalOrZero(height));
				String unitHeightStr = UtilString.getStringOrEmptyForObject(matMap.get("MEABM"));
				int unitHeight = dictionaryService.getUnitIdByUnitCode(unitHeightStr);
				material.setUnitHeight(UtilObject.getIntOrZero(unitHeight));
				String grossWeight = UtilString.getStringOrEmptyForObject(matMap.get("BRGEW"));
				material.setGrossWeight(UtilObject.getBigDecimalOrZero(grossWeight));
				String netWeight = UtilString.getStringOrEmptyForObject(matMap.get("NTGEW"));
				material.setNetWeight(UtilObject.getBigDecimalOrZero(netWeight));
				String unitWeightStr = UtilString.getStringOrEmptyForObject(matMap.get("GEWEI"));
				int unitWeight = dictionaryService.getUnitIdByUnitCode(unitWeightStr);
				material.setUnitWeight(UtilObject.getIntOrZero(unitWeight));
				String volume = UtilString.getStringOrEmptyForObject(matMap.get("VOLUM"));
				material.setVolume(UtilObject.getBigDecimalOrZero(volume));
				String unitVolumeStr = UtilString.getStringOrEmptyForObject(matMap.get("VOLEH"));
				int unitVolume = dictionaryService.getUnitIdByUnitCode(unitVolumeStr);
				material.setUnitVolume(UtilObject.getIntOrZero(unitVolume));
				
				matList.add(material);
			
			}
		}
		
		return matList;
	}
	
	@Override
	public int getExcelData(String url, String fileName, String folder) throws Exception {

		// 文件全路径
		String path = url + folder + File.separator;

		List<Map<String, Object>> dataList = UtilExcel.getExcelDataList(path + fileName, getDataChangTitle(),
				getTitleMapping(), getCheckData());

		List<DicMaterial> matList = new ArrayList<DicMaterial>();
		for (int i = 0; i < dataList.size(); i++) {
			matList.add(this.saveMaterialByMapReturn(dataList.get(i)));
		}

		int count = 0;

		if (matList.size() > 0) {
			List<DicMaterial> distinctList = new ArrayList<DicMaterial>();
			this.distinctMaterial(matList, distinctList);
			if (distinctList.size() > 0) {
				count = dicMaterialDao.insertList(distinctList);
			}

		} else {
			throw new ExcelCellTypeException("Excel文档中没有找到数据!");
		}

		return count;

	}

	public List<String> getDataChangTitle() {
		List<String> list = new ArrayList<String>();

		list.add("matCode");

		return list;
	}

	public Map<String, String> getTitleMapping() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("物料编码", "matCode");
		map.put("物料描述", "matName");
		map.put("物料类型", "matType");
		map.put("删除标示", "isDelete");
		map.put("单位", "unitCode");
		map.put("物料组", "matGroupCode");
		map.put("物料组描述", "matGroupName");
		map.put("长度", "length");
		map.put("宽度", "width");
		map.put("高度", "height");
		map.put("长度/宽度/高度的单位", "unitHeightCode");
		map.put("毛重", "grossWeight");
		map.put("净重", "netWeight");
		map.put("重量的单位", "unitWeightCode");
		map.put("体积", "volume");
		map.put("体积的单位", "unitVolumeCode");
		map.put("保质期", "shelfLife");
		map.put("危险物料标示", "isDanger");
		map.put("批次特性分类", "batchSpecClassifyCode");
		map.put("资产属性", "assetAttr");

		return map;
	}

	public Map<String, ExcelCellType> getCheckData() {

		Map<String, ExcelCellType> checkData = new HashMap<String, ExcelCellType>();

		checkData.put("matCode", new ExcelCellType(true, 0, ""));
		checkData.put("matName", new ExcelCellType(true));
		checkData.put("matType", new ExcelCellType(true));

		Map<String, List<String>> map1 = new HashMap<String, List<String>>();
		List<String> list1 = new ArrayList<String>();
		Map<String, DicUnit> unitMap = dictionaryService.getUnitCodeMap();
		for (String code : unitMap.keySet()) {
			list1.add(code);
		}
		map1.put(UtilExcel.VALIDATION_DATA_EXISTS, list1);

		checkData.put("unitCode", new ExcelCellType(true, map1));

		Map<String, List<String>> map2 = new HashMap<String, List<String>>();
		List<String> list2 = new ArrayList<String>();
		Map<String, DicBatchSpecClassify> classifyCodeMap = dictionaryService.getBatchSpecClassifyCodeMap();
		for (String code : classifyCodeMap.keySet()) {
			list2.add(code);
		}
		map2.put(UtilExcel.VALIDATION_DATA_EXISTS, list2);
		checkData.put("batchSpecClassifyCode", new ExcelCellType(true, 0, "", true, map2));

		return checkData;
	}

	private DicMaterial saveMaterialByMapReturn(Map<String, Object> data) {
		DicMaterial material = new DicMaterial();

		String matCode = UtilString.getStringOrEmptyForObject(data.get("matCode"));

		material.setMatCode(matCode);
		String matName = UtilString.getStringOrEmptyForObject(data.get("matName"));
		material.setMatName(matName);
		String isDelete = UtilString.getStringOrEmptyForObject(data.get("isDelete"));
		material.setIsDelete((byte) UtilObject.getIntOrZero(isDelete));
		String unitCode = UtilString.getStringOrEmptyForObject(data.get("unitCode"));
		Integer unitId = dictionaryService.getUnitIdByUnitCode(unitCode);
		material.setUnitId(unitId);
		String matGroupCode = UtilString.getStringOrEmptyForObject(data.get("matGroupCode"));
		Integer matGroupId = dictionaryService.getMatGroupIdByCode(matGroupCode);
		if (matGroupId != null) {
			material.setMatGroupId(matGroupId);
		} else {
			material.setMatGroupId(0);
		}

		String matType = UtilString.getStringOrEmptyForObject(data.get("matType"));
		material.setMatType(matType);
		String length = UtilString.getStringOrEmptyForObject(data.get("length"));
		material.setLength(UtilObject.getBigDecimalOrZero(length));
		String width = UtilString.getStringOrEmptyForObject(data.get("width"));
		material.setWidth(UtilObject.getBigDecimalOrZero(width));
		String height = UtilString.getStringOrEmptyForObject(data.get("height"));
		material.setHeight(UtilObject.getBigDecimalOrZero(height));
		String unitHeightCode = UtilString.getStringOrEmptyForObject(data.get("unitHeightCode"));
		int unitHeight = dictionaryService.getUnitIdByUnitCode(unitHeightCode);
		material.setUnitHeight(UtilObject.getIntOrZero(unitHeight));
		String grossWeight = UtilString.getStringOrEmptyForObject(data.get("grossWeight"));
		material.setGrossWeight(UtilObject.getBigDecimalOrZero(grossWeight));
		String netWeight = UtilString.getStringOrEmptyForObject(data.get("netWeight"));
		material.setNetWeight(UtilObject.getBigDecimalOrZero(netWeight));
		String unitWeightCode = UtilString.getStringOrEmptyForObject(data.get("unitWeightCode"));
		int unitWeight = dictionaryService.getUnitIdByUnitCode(unitWeightCode);
		material.setUnitWeight(UtilObject.getIntOrZero(unitWeight));
		String volume = UtilString.getStringOrEmptyForObject(data.get("volume"));
		material.setVolume(UtilObject.getBigDecimalOrZero(volume));
		String unitVolumeCode = UtilString.getStringOrEmptyForObject(data.get("unitVolumeCode"));
		int unitVolume = dictionaryService.getUnitIdByUnitCode(unitVolumeCode);
		material.setUnitVolume(UtilObject.getIntOrZero(unitVolume));
		String shelfLife = UtilString.getStringOrEmptyForObject(data.get("shelfLife"));
		material.setShelfLife(UtilObject.getIntOrZero(shelfLife));
		String isDanger = UtilString.getStringOrEmptyForObject(data.get("isDanger"));
		material.setIsDanger((byte) UtilObject.getIntOrZero(isDanger));
		String batchSpecClassifyCode = UtilString.getStringOrEmptyForObject(data.get("batchSpecClassifyCode"));
		Integer batchSpecClassifyId = dictionaryService.getBatchSpecClassifyIdByCode(batchSpecClassifyCode);
		if (batchSpecClassifyId != null) {
			material.setBatchSpecClassifyId(batchSpecClassifyId);
		} else {
			material.setBatchSpecClassifyId(0);
		}

		String assetAttr = UtilString.getStringOrEmptyForObject(data.get("assetAttr"));
		material.setAssetAttr((byte) UtilObject.getIntOrZero(assetAttr));

		return material;
	}

	private void distinctMaterial(List<DicMaterial> oList, List<DicMaterial> newList) {

		List<String> codeList = new ArrayList<String>();
		for (DicMaterial inner : oList) {
			codeList.add(inner.getMatCode());
		}
		Map<String, Integer> codeIdMap = dictionaryService.getMatMapByCodeList(codeList);
		Map<String, String> codeMap = new HashMap<String, String>();
		if (codeIdMap != null && !codeIdMap.isEmpty()) {
			for (DicMaterial inner : oList) {

				if (!codeIdMap.containsKey(inner.getMatCode()) && !codeMap.containsKey(inner.getMatCode())) {
					codeMap.put(inner.getMatCode(), inner.getMatCode());
					newList.add(inner);
				}
			}
		} else {
			newList.addAll(oList);
		}

	}

	@Override
	public String upLoadExcel(MultipartFile fileInClient, String realPath, String folder) {
		// 文件全路径
		String path = realPath + folder + File.separator;
		// 文件名
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = sdf.format(new Date()) + ".xls";

		File fileInServer = new File(path, fileName);

		if (fileInServer != null) {
			if (!fileInServer.exists()) {
				fileInServer.mkdirs();
			}

			// MultipartFile自带的解析方法
			try {
				fileInClient.transferTo(fileInServer);
			} catch (IllegalStateException e) {
				logger.error("", e);
			} catch (IOException e) {
				logger.error("", e);
			}
		}

		return fileName;
	}

	@Override
	public Integer updateShelfByMatId(Map<String, Object> map) {
		return dicMaterialDao.updateShelfByMatId(map);
	}

	@Override
	public Integer addimg(JSONObject json) throws Exception {
		JSONArray fileAry = json.getJSONArray("file_list");
		commonService.saveFileAry(json.getInt("mat_id"), (byte)1, null,
				fileAry);
		return 1;
	}

	@Override
	public Map<String, Object> getMatUnitByMatCode(String matCode) {
		return dicMaterialDao.getMatUnitByMatCode(matCode);
	}
}
