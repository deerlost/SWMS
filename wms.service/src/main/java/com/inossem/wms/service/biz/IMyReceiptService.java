package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicReceiptType;
import com.inossem.wms.model.vo.ApproveUserMatReqVo;

import net.sf.json.JSONArray;

public interface IMyReceiptService {
	/**
	 * 根据userId获取非领料单的所有单据类型
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<DicReceiptType> getApproveTaskType(String userId) throws Exception;

	/**
	 * 非领料单未审批任务
	 * 
	 * @param map
	 * @return
	 */
	List<ApproveUserMatReqVo> getApproveTasks(Map<String, Object> map);

	/**
	 * 非领料单已审批任务
	 * 
	 * @param map
	 * @return
	 */
	List<ApproveUserMatReqVo> getApprovedTasks(Map<String, Object> map);

	/**
	 * 领料单未审批任务
	 * 
	 * @param map
	 * @return
	 */
	List<ApproveUserMatReqVo> getMatReqApproveTasks(Map<String, Object> map);

	/**
	 * 领料单已审批任务
	 * 
	 * @param map
	 * @return
	 */
	List<ApproveUserMatReqVo> getMatReqApprovedTasks(Map<String, Object> map);

	/**
	 * 审批
	 * 
	 * @param receiptId
	 * @param processInstanceId
	 * @param approvePerson
	 * @param approve
	 * @param comment
	 * @param url
	 * @return
	 */
	int taskApprove(int receiptId, String processInstanceId, String approvePerson, boolean approve, String comment,
			String url);

	/**
	 * 最后审批人审批
	 * 
	 * @param receiptId
	 * @param processInstanceId
	 * @param approvePerson
	 * @param deptCode
	 * @param deptName
	 * @param itemList
	 * @param comment
	 * @param url
	 * @return
	 * @throws Exception
	 */
	String lastTaskApprove(int receiptId, String processInstanceId, String approvePerson, String deptCode,
			String deptName, JSONArray itemList, String comment, String url) throws Exception;

}
