package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicCorp;

public interface DicCorpMapper {
	int deleteByPrimaryKey(Integer corpId);

	int insert(DicCorp record);

	int insertSelective(DicCorp record);

	DicCorp selectByPrimaryKey(Integer corpId);

	DicCorp selectByCode(String corpCode);

	List<DicCorp> selectAllCorpList();

	int updateByPrimaryKeySelective(DicCorp record);

	int updateByPrimaryKey(DicCorp record);

	/**
	 * 查询所有公司id和公司描述
	 * 
	 * @return
	 */
	List<DicCorp> selectAllCorpIdAndCorpNameOfCorpList();

	/**
	 * 公司分页查询
	 * 
	 * @author 刘宇 2018.03.02
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectCorpOnPaging(Map<String, Object> map);

}