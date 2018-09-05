package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.vo.DicFactoryVo;

public interface IDicFactoryService {

	/**
	 * 获取单个工厂
	 * 
	 * @return
	 * @throws Exception
	 */
	DicFactory getFactoryByCode(String ftyCode);

	/**
	 * 保存
	 * 
	 * @param isAdd
	 * @param factory
	 * @return
	 */
	int saveFactory(boolean isAdd, DicFactory factory);

	// 查询所有工厂
	List<DicFactoryVo> getAllFactory();

	// 查询所有工厂
	List<DicFactory> getFactoryByCorpCode(String corpCode);

	List<DicFactory> getFactoryByCorpId(int corpId);

	// 根据公司id查工厂
	DicFactory getFactoryById(int ftyId);

	/**
	 * 根据工厂ftyCode查工厂
	 * 
	 * @author 刘宇 2018.02.27
	 * @param ftyCode
	 * @return
	 */
	DicFactory getFactoryByFtyCode(String ftyCode);

	/**
	 * 分页查询系统管理下组织架构下工厂管理的所有工厂
	 * 
	 * @author 刘宇 2018.02.27
	 * @param condition
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> listFactoryOnPaging(String condition, int pageIndex, int pageSize, int total,
			boolean sortAscend, String sortColumn) throws Exception;

	/**
	 * 添加或者修改工厂
	 * 
	 * @param isAdd
	 * @param obj
	 * @return
	 */
	Map<String, Object> addOrUpdateFactory(int errorCode, String errorString, boolean errorStatus, boolean isAdd,
			Integer ftyId, String ftyCode, String ftyName, String address, Integer corpId) throws Exception;

	/**
	 * 根据工厂fty_code and crop_id查工厂
	 * 
	 * @param record
	 * @return
	 */
	DicFactory selectFactoryByFtyCodeAndCorpId(String ftyCode, Integer corpId) throws Exception;

	List<Map<String, Object>> listFtyIdAndName() throws Exception;

	Map<String, Object> getFtyById(Integer ftyId, CurrentUser user);

}
