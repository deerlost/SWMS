package com.inossem.wms.dao.wf;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.wf.WfReceiptUser;

public interface WfReceiptUserMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(WfReceiptUser record);

	int insertSelective(WfReceiptUser record);

	WfReceiptUser selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(WfReceiptUser record);

	int updateByPrimaryKey(WfReceiptUser record);

	WfReceiptUser selectByReceiptIdAndReceiptTypeAndUserId(Map<String, Object> map);

	List<WfReceiptUser> selectByReceiptIdAndReceiptType(Map<String, Object> map);
	
	int updateApproveMsg(WfReceiptUser record);

	int deleteByReceiptIdAndReceiptType(Map<String, Object> map);
}