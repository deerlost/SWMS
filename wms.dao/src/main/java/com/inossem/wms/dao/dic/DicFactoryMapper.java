package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.vo.CheckLockFacLocVo;
import com.inossem.wms.model.vo.DicFactoryVo;

public interface DicFactoryMapper {
	int deleteByPrimaryKey(Integer ftyId);

	int insert(DicFactory record);

	int insertSelective(DicFactory record);

	DicFactory selectByPrimaryKey(Integer ftyId);

	DicFactory selectByCode(String ftyCode);

	List<DicFactory> selectByCorpCode(String corpCode);

	List<DicFactory> selectByCorpId(int corpId);

	int updateByPrimaryKeySelective(DicFactory record);

	int updateByPrimaryKey(DicFactory record);

	List<DicFactoryVo> selectAllFactory();

	List<DicFactoryVo> selectMatReqFactoryList();

	/**
	 * 查询所有工厂给审批部门
	 * 
	 * @author 刘宇 2017.01.24
	 */
	List<DicFactory> selectAllFactoryForDepartment();

	/**
	 * 通过ftyId查询工工厂
	 * 
	 * @author 刘宇 2017.01.24
	 */

	DicFactory selectFactoryByFtyIdForDepartment(int ftyId);

	List<CheckLockFacLocVo> selectAllFactoryAndLocationList();

	/**
	 * 通过fty_code查询工工厂
	 * 
	 * @author 刘宇 2018.02.27
	 * @param ftyCode
	 * @return
	 */
	DicFactory selectFactoryByFtyCode(String ftyCode);

	/**
	 * 分页查询系统管理下组织架构下工厂管理的所有工厂
	 * 
	 * @author 刘宇 2018.02.27
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectFactoryOnPaging(Map<String, Object> map);

	/**
	 * 根据工厂编码fty_code和公司idcorp_id查询工厂
	 * 
	 * @author 刘宇 2018.02.27
	 * @param record
	 * @return
	 */
	DicFactory selectFactoryByFtyCodeAndCorpId(DicFactory record);

	List<Map<String, Object>> listFtyIdAndName();
	
	List<Map<String,Object>> selectFtyByBoardId(Map<String,Object> param);

}