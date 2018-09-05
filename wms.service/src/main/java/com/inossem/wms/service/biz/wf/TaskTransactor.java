package com.inossem.wms.service.biz.wf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.inossem.wms.dao.rel.RelUserApproveMapper;
import com.inossem.wms.model.rel.RelUserApprove;
import com.inossem.wms.util.UtilProperties;

public class TaskTransactor {
	@Autowired
	private RelUserApproveMapper relUserApproveDao;

	/**
	 * flowable使用的类,多用户审批流程时设置审批人组
	 * @param receiptType
	 * @param group
	 * @param node
	 * @return
	 */
	public String getUsersForGroup(int receiptType, int group, String node) {
		Map<String, Object> uMap = new HashMap<String, Object>();
		uMap.put("receiptType", receiptType);
		uMap.put("group", group);
		uMap.put("node", node);
		List<RelUserApprove> list = relUserApproveDao.selectByTypeGroupNode(uMap);

		StringBuffer sBuffer = new StringBuffer();
		if (list != null && list.size() > 0) {
			list.stream().forEach((final RelUserApprove item) -> sBuffer.append(item.getUserId()).append(","));
		}
		if (sBuffer.length() > 0) {
			sBuffer.deleteCharAt(sBuffer.length() - 1);
		} else {
			// 默认领料单审批人,无论是否已经设置审批人,该审批人一定可以审批
			sBuffer.append(UtilProperties.getInstance().getStockRequisitionApproveUser());
		}
		return sBuffer.toString();
		//return "";
	}

	public String getUsersForFactory(int receiptType, int group, String node, String factory) {
		Map<String, Object> uMap = new HashMap<String, Object>();
		uMap.put("receiptType", receiptType);
		uMap.put("factory", factory);
		uMap.put("node", node);
		List<RelUserApprove> list = relUserApproveDao.selectByTypeNodeForCoal(uMap);

		StringBuffer sBuffer = new StringBuffer();
		if (list != null && list.size() > 0) {
			list.stream().forEach((final RelUserApprove item) -> sBuffer.append(item.getUserId()).append(","));
		}
		if (sBuffer.length() > 0) {
			sBuffer.deleteCharAt(sBuffer.length() - 1);
		} else {
			// 默认领料单审批人,无论是否已经设置审批人,该审批人一定可以审批
			sBuffer.append(UtilProperties.getInstance().getStockRequisitionApproveUser());
		}
		return sBuffer.toString();
		//return "";
	}
}
