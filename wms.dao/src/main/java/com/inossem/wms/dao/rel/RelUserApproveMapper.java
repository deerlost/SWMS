package com.inossem.wms.dao.rel;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.rel.RelUserApprove;

public interface RelUserApproveMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(RelUserApprove record);

	int insertSelective(RelUserApprove record);

	RelUserApprove selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(RelUserApprove record);

	int updateByPrimaryKey(RelUserApprove record);

	List<RelUserApprove> selectByTypeGroupNode(Map<String, Object> map);

	List<RelUserApprove> selectByTypeNodeForCoal(Map<String, Object> map);

	/**
	 * 关键字分页查询审批管理
	 * 
	 * @author 刘宇 2018.03.12
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectApproveOnPaging(Map<String, Object> map);

	/**
	 * 通过用户id和类型id和组id和节点名称查询审批管理的id
	 * 
	 * @author 刘宇 2018.03.12
	 * @param record
	 * @return
	 */
	RelUserApprove selectIdAndUserIdOfApproveByExceptId(RelUserApprove record);

	/**
	 * 根据用户id查询查询审批管理
	 * 
	 * @author 刘宇 2018.03.13
	 * @param userId
	 * @return
	 */
	Map<String, Object> selectApproveByUserId(Integer id);

}