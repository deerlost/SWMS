package com.inossem.wms.service.auth;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.rel.RelUserApprove;

/**
 * 审批管理
 * 
 * @author 刘宇 2018.03.12
 *
 */
public interface IApproveService {
	/**
	 * 分页查询审批管理
	 * 
	 * @author 刘宇 2018.03.12
	 * @param condition
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> listApproveOnPaging(String condition, int pageIndex, int pageSize, int total,
			boolean sortAscend, String sortColumn) throws Exception;

	/**
	 * 添加或者修改审批管理
	 * 
	 * @param errorString
	 * @param errorStatus
	 * @param id
	 * @param userId
	 * @param receiptType
	 * @param departmentId
	 * @param node
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> addOrUpdateApprove(String errorString, boolean errorStatus, String id, String userId,
			String receiptType, String group, String node) throws Exception;

	/**
	 * 通过用户id和类型id和组id和节点名称查询审批管理的id
	 * 
	 * @author 刘宇 2018.03.12
	 * @param userId
	 * @param receiptType
	 * @param group
	 * @param node
	 * @return
	 * @throws Exception
	 */
	RelUserApprove getIdAndUserIdOfApproveByExceptId(String userId, String receiptType, String group, String node)
			throws Exception;

	/**
	 * 通过id删除审批管理
	 * 
	 * @author 刘宇 2018.03.12
	 * @param id
	 * @return
	 * @throws Exception
	 */
	int deleteApprove(String id) throws Exception;

	/**
	 * 根据用户id查询查询审批管理
	 * 
	 * @author 刘宇 2018.03.13
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getApproveByUserId(Integer id) throws Exception;
}
