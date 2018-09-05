package com.inossem.wms.model.wf;

import java.util.List;

import com.inossem.wms.model.vo.WfApproveGroupItemVo;

public class WfApproveGroupHead {
	
	private List<WfApproveGroupItemVo> groupItemList;
	
	
	public List<WfApproveGroupItemVo> getGroupItemList() {
		return groupItemList;
	}

	public void setGroupItemList(List<WfApproveGroupItemVo> groupItemList) {
		this.groupItemList = groupItemList;
	}

	private String typeName;
	
    public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	private Integer groupId;

    private Integer groupIndex;

    private String groupName;

    private Byte type;

    private String userId;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }
}