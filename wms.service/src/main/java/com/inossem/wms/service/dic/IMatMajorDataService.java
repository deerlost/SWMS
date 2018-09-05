package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;

/**
 * 物料主数据
 * 
 * @author 刘宇 2018.02.27
 *
 */
public interface IMatMajorDataService {
	/**
	 * 分页查询物料主数据
	 * 
	 * @author 刘宇 2018.02.27
	 * @param condition
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> listMatMajorDataOnPaging(String condition, int pageIndex, int pageSize, int total,
			boolean sortAscend, String sortColumn) throws Exception;

	int syncSapMaterial(String matCode) throws Exception;
	
	int syncSapMaterialFromOracle(String matCode) throws Exception;

	int getExcelData(String url, String fileName, String folder) throws Exception;

	String upLoadExcel(MultipartFile fileInClient, String realPath, String folder);
	
	Integer updateShelfByMatId(Map<String,Object> map);

	Integer addimg(JSONObject json) throws Exception;
	
	/**
	 * 查询物料信息
	 * @author wangfz
	 * @param matCode
	 * @return
	 */
	Map<String ,Object> getMatUnitByMatCode(String matCode);
}
