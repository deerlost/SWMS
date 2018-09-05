package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.dic.DicMaterial;

public interface DicMaterialMapper {
	
	List<String> selectMatCode();
	
	int deleteByPrimaryKey(Integer matId);

	int insert(DicMaterial record);
	
	int insertList(List<DicMaterial> list);

	int insertSelective(DicMaterial record);

	DicMaterial selectByPrimaryKey(Integer matId);

	int updateByPrimaryKeySelective(DicMaterial record);

	int updateByPrimaryKey(DicMaterial record);

	List<DicMaterial> selectIdAndCodeBySql(String value);

	List<DicMaterial> selectByCondition(DicMaterial record);

	int syncMatFromSap(DicMaterial record);
	
	Map<String ,Object> getMatUnitByMatCode(@Param("matCode") String matCode);
	
	/**
	 * 分页查询物料主数据
	 * 
	 * @author 刘宇 2018.02.27
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectMatMajorDataOnPaging(Map<String, Object> map);
	
	/**
	 * @Description: 库存盘点-根据物料编号列表查询物料信息，新增物资使用
	 * @param matIdArray 物料编号的数组
	 */
	public List<Map<String, Object>> findMaterielByMatnrList4KCPD(@Param("array") Integer[] matIdArray) throws Exception;
	
	
	List<Map<String, Object>> selectMaterialList(Map<String, Object> param);
	
	Integer updateShelfByMatId(Map<String,Object> map);

}