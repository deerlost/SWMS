package com.inossem.wms.dao.wf;

import java.util.ArrayList;
import java.util.List;

import com.inossem.wms.model.vo.WfApproveGroupItemVo;
import com.inossem.wms.model.wf.WfApproveGroupItem;

public interface WfApproveGroupItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(WfApproveGroupItem record);

    int insertSelective(WfApproveGroupItem record);

    WfApproveGroupItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(WfApproveGroupItem record);

    int updateByPrimaryKey(WfApproveGroupItem record);
    
    int insertList(List<WfApproveGroupItemVo> recordList);
    
    int deleteByGroupId(Integer groupId);
    
    ArrayList<WfApproveGroupItemVo> selectByGroupId(Integer groupId);
}