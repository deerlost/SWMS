package com.inossem.wms.dao.wf;

import java.util.List;

import com.inossem.wms.model.wf.WfApproveGroupHead;

public interface WfApproveGroupHeadMapper {
	int deleteByPrimaryKey(Integer groupId);

	int insert(WfApproveGroupHead record);

	int insertSelective(WfApproveGroupHead record);

	WfApproveGroupHead selectByPrimaryKey(Integer groupId);

	int updateByPrimaryKeySelective(WfApproveGroupHead record);

	int updateByPrimaryKey(WfApproveGroupHead record);
	
	List<WfApproveGroupHead> selectByCondition(WfApproveGroupHead record);
	
}