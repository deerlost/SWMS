package com.inossem.wms.service.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.Department;
import com.inossem.wms.model.auth.MenuADK;
import com.inossem.wms.model.auth.ORG;
import com.inossem.wms.model.auth.Resources;
import com.inossem.wms.model.auth.Role;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.auth.UserRole;
import com.inossem.wms.model.dic.DicProductLine;
import com.inossem.wms.model.rel.RelUserStockLocation;
import com.inossem.wms.model.vo.RelUserStockLocationVo;
import com.inossem.wms.model.vo.WfApproveGroupItemVo;
import com.inossem.wms.model.wf.WfApproveGroupHead;
import com.inossem.wms.model.wf.WfApproveGroupItem;

public interface IUserService {
	// 根据工厂id查询其下部门
	ArrayList<Department> listDepartmentByFactory(String factory);

	public User getUserById(String userId) throws Exception;

	public int addUser(User user) throws Exception;

	public int modifyUser(User user) throws Exception;

	/**
	 * 根据条件查询用户
	 * 
	 * @param user
	 * @return
	 */
	public ArrayList<User> listUser(User user) throws Exception;

	ORG getOrgByPrimaryKey(String orgId);

	ArrayList<ORG> listAllOrg();

	int selectChildrenOrgCount(String parentOrgId);

	ArrayList<ORG> listChildrenOrg(String parentOrgId);

	// 获取所有用户

	List<Map<String, Object>> listAll(HashMap<String, Object> paramMap) throws Exception;

	int addUserRole(UserRole userRole) throws Exception;

	ArrayList<Role> getRoleByUserId(String userId) throws Exception;

	int deleteByUserId(String userId) throws Exception;

	List<RelUserStockLocationVo> listLocForUser(String userId);

	List<RelUserStockLocationVo> listLocForBoardId(int boardId);

	int deleteLocationByUserId(String userId);

	int addRelUserStockLocation(RelUserStockLocation record);

	ArrayList<User> listByUserName(String userName);

	int updateUser(User user);

	public ArrayList<Resources> settingMenu(ArrayList<Resources> resources);

	public ArrayList<MenuADK> settingADKMenu(ArrayList<Resources> resources);

	// User getFirstApproveUserByReceiptAndType(long receipt_id, int type);

	/**
	 * 添加常用经办人组
	 * 
	 * @param group
	 * @return
	 * @throws Exception
	 */
	public int addApproveGroup(WfApproveGroupHead group) throws Exception;

	/**
	 * 删除常用经办人组
	 * 
	 * @param group
	 * @return
	 * @throws Exception
	 */
	public int deleteApproveGroup(WfApproveGroupHead group) throws Exception;

	/**
	 * 修改常用经办人组
	 * 
	 * @param group
	 * @return
	 * @throws Exception
	 */
	public void updateApproveGroup(WfApproveGroupHead group) throws Exception;

	/**
	 * 查询常用经办人组详情
	 * 
	 * @param group
	 * @return
	 * @throws Exception
	 */
	public ArrayList<WfApproveGroupItemVo> listApproveGroupItem(WfApproveGroupItem groupDetail) throws Exception;

	/**
	 * 查询经办人组
	 * 
	 * @param group
	 * @return
	 * @throws Exception
	 */
	public List<WfApproveGroupHead> getApproveGroups(WfApproveGroupHead group) throws Exception;

	/**
	 * 通过user_id查询用户用于判断审批管理保存
	 * 
	 * @author 刘宇 2018.03.12
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public User getUserByIdForApprove(String userId) throws Exception;

	/**
	 * 获取当前登录人仓库
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCurrentUserWarehouse(String userId) throws Exception;
	
	public List<DicProductLine> getCurrentUserProductLine(String userId) throws Exception;
	
	public int insertRelProductUser(Map<String,Object> param)throws Exception;
	
	public int updateRelProductUser(Map<String,Object> param)throws Exception;
	
    List<DicProductLine> selectAllProductLine();
    
    Map<String,Object> getRelUserProductLine(String userId);
   
    int changePassword(Map<String,Object> map);
    
    /**
     * 查询打印机列表
     * @param userId
     * @return
     */
    Map<String,Object> getPrinterList(String userId);
    
    /**
     * 插入打印机与用户关联关系
     * @param userId
     * @param printerId
     * @param type
     * @return
     */
    int insertPrinterRelUser(String userId,Integer printerId,Byte type)throws Exception;
}
