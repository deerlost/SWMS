package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizMatReqHead;
import com.inossem.wms.model.vo.BizMatReqHeadVo;

public interface BizMatReqHeadMapper {
	int deleteByPrimaryKey(Integer matReqId);

	int insert(BizMatReqHead record);

	int insertSelective(BizMatReqHead record);

	BizMatReqHead selectByPrimaryKey(Integer matReqId);

	int updateByPrimaryKeySelective(BizMatReqHead record);

	int updateByPrimaryKey(BizMatReqHead record);

	List<BizMatReqHeadVo> selectMatReqListOnPaging(Map<String, Object> map);

	BizMatReqHeadVo selectBizMatReqHeadVo(Map<String, Object> map);

	int updateStatus(BizMatReqHead record);

	int updatePiid(BizMatReqHead record);
	
	int deleteLogicalByPrimaryKey(int matReqId);

	/**
	 * 领料单分页查询
	 * 
	 * @author 刘宇 2018.02.01
	 */
	List<Map<String, Object>> selectBizMatReqHeadOnPaging(Map<String, Object> map);

	/**
	 * 最后一个人审批[财务审批]接口
	 * 
	 * @param record
	 * @return
	 */
	int updateForLastTask(BizMatReqHead record);
}