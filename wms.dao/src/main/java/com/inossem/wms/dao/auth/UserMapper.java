package com.inossem.wms.dao.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.vo.RelUserStockLocationVo;

public interface UserMapper {
	int deleteByPrimaryKey(String userId);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(String userId);

	ArrayList<User> selectByUserName(String userName);

	// 查询所有用户
	// Map<String, Object>改成了User
	List<Map<String, Object>> selectListOnPaging(HashMap<String, Object> paramMap);

	// 查询所有用户总共行数

	int selectListCount(HashMap<String, Object> paramMap);

	/**
	 * 根据主键查询用户，仅用于CurrentUser查询
	 * 
	 * @param record
	 * @return
	 */
	User selectCurrentUserByPrimaryKey(String userId);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	/**
	 * 根据条件查询用户
	 * 
	 * @param record
	 * @return
	 */
	ArrayList<User> selectUser(User record);

	/**
	 * 根据条件查询用户 库存盘点
	 * 
	 * @param record
	 * @return
	 */
	ArrayList<User> selectUserForKCPD(User record);

	/**
	 * 查询用户所关联库存地(格式: 工厂编码-库存地点编码)
	 * 
	 * @author ght
	 */
	List<String> selectLocationForUser(String userId);

	/**
	 * 查询用户角色
	 * 
	 * @author ght
	 */
	List<Integer> selectRoleForUser(String userId);

	/**
	 * 根据角色和部门查询用户列表，目前针对领料单审批专门添加的方法
	 * 
	 * @author ght
	 */
	List<User> selectLLDSPUserList(HashMap<String, Object> map);

	User selectFirstApproveUserByReceiptIDAndType(HashMap<String, Object> map);

	User selectLastApproveUserByReceiptIDAndType(HashMap<String, Object> map);

	ArrayList<User> selectJBRUser(User user);

	List<RelUserStockLocationVo> selectInventoryLocationForUser(String userId);

	/**
	 * 通过user_id查询用户用于判断审批管理保存
	 * 
	 * @author 刘宇 2018.03.12
	 * @param userId
	 * @return
	 */
	User selectByPrimaryKeyForApprove(String userId);
	
	List<Map<String, Object>> selectCurrentUserWarehouse(String userId);
	  
	int changePassword(Map<String,Object> map);

	List<Map<String, Object>> getPrinterList();

	List<Map<String, Object>> getPrinterListByUserId(@Param("userId") String userId);
}