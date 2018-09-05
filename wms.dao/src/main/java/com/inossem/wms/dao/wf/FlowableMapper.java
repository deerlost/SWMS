package com.inossem.wms.dao.wf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicReceiptType;
import com.inossem.wms.model.vo.ApproveUserMatReqVo;
import com.inossem.wms.model.vo.ApproveUserVo;

public interface FlowableMapper {
	/**
	 * 根据userId查询非领料单审批的单据类型
	 * 
	 * @param userID
	 * @return
	 */
	List<DicReceiptType> selectApproveTaskTypeByUserID(String userID);

	/**
	 * 领料单审批人查询
	 * 
	 * @param map
	 * @return
	 */
	List<ApproveUserMatReqVo> selectCandidateUsersByReceiptIDAndType(HashMap<String, Object> map);

	/**
	 * 一般单据审批人查询
	 * 
	 * @param map
	 * @return
	 */
	List<ApproveUserVo> selectByReceiptIDAndReceiptType(HashMap<String, Object> map);

	/**
	 * 查询piid对应的当前审批人
	 * 
	 * @param userID
	 * @return
	 */
	List<String> selectEmployerNumberForApproveUserByPiid(String piid);

	/**
	 * 当前人未审批任务
	 * 
	 * @param map
	 * @return
	 */
	List<ApproveUserMatReqVo> selectApproveTasksByUserIDOnPaging(Map<String, Object> map);

	/**
	 * 当前人已审批任务
	 * 
	 * @param map
	 * @return
	 */
	List<ApproveUserMatReqVo> selectApprovedTasksByUserIDOnPaging(Map<String, Object> map);

	/**
	 * 当前人领料单未审批任务
	 * 
	 * @param map
	 * @return
	 */
	List<ApproveUserMatReqVo> selectMatReqApproveTasksByUserIDOnPaging(Map<String, Object> map);

	/**
	 * 当前人领料单已审批任务
	 * 
	 * @param map
	 * @return
	 */
	List<ApproveUserMatReqVo> selectMatReqApprovedTasksByUserIDOnPaging(Map<String, Object> map);

	/**
	 * 查询piid对应的当前审批人
	 * 
	 * @param userID
	 * @return
	 */
	List<String> selectApproveUserByPiid(String piid);
}
