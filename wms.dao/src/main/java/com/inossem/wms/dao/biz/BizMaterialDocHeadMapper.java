package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizMaterialDocHead;

public interface BizMaterialDocHeadMapper {
	int deleteByPrimaryKey(Integer matDocId);

	int insert(BizMaterialDocHead record);

	int insertSelective(BizMaterialDocHead record);

	BizMaterialDocHead selectByPrimaryKey(Integer matDocId);

	int updateByPrimaryKeySelective(BizMaterialDocHead record);

	int updateByPrimaryKey(BizMaterialDocHead record);

	/**
	 * 通过单据id和单据类型查找物料凭证 刘宇 2018.04.18
	 * 
	 * @author ly 2018.04.18
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectByRefereceiptIdAndMatDocType(Map<String, Object> param);

}