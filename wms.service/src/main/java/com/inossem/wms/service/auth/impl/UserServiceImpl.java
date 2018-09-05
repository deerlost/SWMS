package com.inossem.wms.service.auth.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.inossem.wms.dao.auth.DepartmentMapper;
import com.inossem.wms.dao.auth.ORGMapper;
import com.inossem.wms.dao.auth.ResourcesMapper;
import com.inossem.wms.dao.auth.RoleMapper;
import com.inossem.wms.dao.auth.UserMapper;
import com.inossem.wms.dao.auth.UserRoleMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicPrinterMapper;
import com.inossem.wms.dao.dic.DicProductLineMapper;
import com.inossem.wms.dao.rel.RelUserStockLocationMapper;
import com.inossem.wms.dao.wf.WfApproveGroupHeadMapper;
import com.inossem.wms.dao.wf.WfApproveGroupItemMapper;
import com.inossem.wms.model.auth.Department;
import com.inossem.wms.model.auth.MenuADK;
import com.inossem.wms.model.auth.ORG;
import com.inossem.wms.model.auth.Resources;
import com.inossem.wms.model.auth.Role;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.auth.UserRole;
import com.inossem.wms.model.dic.DicProductLine;
import com.inossem.wms.model.enums.EnumPrinterType;
import com.inossem.wms.model.rel.RelUserStockLocation;
import com.inossem.wms.model.vo.RelUserStockLocationVo;
import com.inossem.wms.model.vo.WfApproveGroupItemVo;
import com.inossem.wms.model.wf.WfApproveGroupHead;
import com.inossem.wms.model.wf.WfApproveGroupItem;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilString;

@Service("userService")
public class UserServiceImpl implements IUserService {

	@Resource
	private UserMapper userDao;

	@Resource
	private UserRoleMapper userRoleDao;

	@Resource
	private RoleMapper roleDao;

	@Resource
	private RelUserStockLocationMapper relUserLocDao;

	@Resource
	private ResourcesMapper resourcesDao;

	@Resource
	private ORGMapper orgDao;

	@Resource
	private DicFactoryMapper ftyDao;

	@Resource
	private DepartmentMapper departmentDao;

	@Autowired
	private WfApproveGroupHeadMapper wfApproveGroupHeadDao;

	@Autowired
	private WfApproveGroupItemMapper wfApproveGroupItemDao;
	
	@Autowired
	private DicProductLineMapper dicProductLineMapper;

    @Autowired
    private DicPrinterMapper printerDao;
    
	/**
	 * 配置菜单
	 * 
	 * @date 2017年10月11日
	 * @author 高海涛
	 */
	public ArrayList<Resources> settingMenu(ArrayList<Resources> resourcesList) {
		ArrayList<Resources> all = resourcesDao.selectAllResourcesToRoleSet();

		Map<Integer, Resources> parent = new HashMap<Integer, Resources>();
		for (Resources resources : all) {
			parent.put(resources.getResourcesId(), resources);
		}
		int i = 0;

		while (true) {
			Resources resources = resourcesList.get(i);
			if (resources.getParentId() != 0) {
				if (parent.get(resources.getParentId()) != null) {
					Resources parentResources = parent.get(resources.getParentId());
					parent.remove(resources.getParentId());
					resourcesList.add(parentResources);
				}
			}
			i++;

			if (i == resourcesList.size()) {
				break;
			}
		}

		return resourcesList;
	}

	/**
	 * 配置菜单
	 * 
	 * @date 2017年10月11日
	 * @author 高海涛
	 */
	public ArrayList<MenuADK> settingADKMenu(ArrayList<Resources> resourcesList) {
		ArrayList<Resources> all = resourcesDao.selectAllResourcesToRoleSet();

		Map<Integer, Resources> parent = new HashMap<Integer, Resources>();
		for (Resources resources : all) {
			parent.put(resources.getResourcesId(), resources);
		}
		int i = 0;

		while (true) {
			Resources resources = resourcesList.get(i);
			if (resources.getParentId() != 0) {
				if (parent.get(resources.getParentId()) != null) {
					Resources parentResources = parent.get(resources.getParentId());
					parent.remove(resources.getParentId());
					resourcesList.add(parentResources);
				}
			}
			i++;

			if (i == resourcesList.size()) {
				break;
			}
		}
		ArrayList<MenuADK> menuADKList = new ArrayList<MenuADK>();
		for (Resources resources : resourcesList) {
			if (StringUtils.hasText(resources.getPortableIndex())) {
				MenuADK menuADK = new MenuADK();
				menuADK.setId(resources.getResourcesId().toString());
				menuADK.setName(resources.getResourcesName());
				menuADK.setIndex(Integer.parseInt(resources.getPortableIndex()));
				menuADKList.add(menuADK);
			}
		}
		Collections.sort(menuADKList);
		return menuADKList;
	}

	@Override
	public User getUserById(String userId) {
		return this.userDao.selectByPrimaryKey(userId);
	}

	@Override
	public int addUser(User user) {
		return this.userDao.insert(user);
	}

	@Override
	public int modifyUser(User user) {
		return this.userDao.updateByPrimaryKey(user);
	}

	@Override
	public ArrayList<User> listUser(User user) {
		ArrayList<User> userList = new ArrayList<User>();
		userList = this.userDao.selectUser(user);
		return userList;
	}

	// 所有用户列表
	@Override
	public List<Map<String, Object>> listAll(HashMap<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return userDao.selectListOnPaging(paramMap);
	}

	@Override
	public int addUserRole(UserRole userRole) {
		return userRoleDao.insert(userRole);
	}

	@Override
	public ArrayList<Role> getRoleByUserId(String userId) {
		if (UtilString.isNullOrEmpty(userId)) {
			return roleDao.selectRole();
		} else {
			return roleDao.selectRoleByUserId(userId);
		}

	}

	@Override
	public int deleteByUserId(String userId) throws Exception {

		return userRoleDao.deleteByUserId(userId);
	}

	@Override
	public List<RelUserStockLocationVo> listLocForUser(String userId) {

		return relUserLocDao.selectStockLocationForUser(userId);
	}

	@Override
	public List<RelUserStockLocationVo> listLocForBoardId(int boardId) {

		return relUserLocDao.selectStockLocationForBoardId(boardId);
	}

	@Override
	public int deleteLocationByUserId(String userId) {
		return relUserLocDao.deleteLocationByUserId(userId);
	}

	@Override
	public int addRelUserStockLocation(RelUserStockLocation record) {

		return relUserLocDao.insert(record);
	}

	@Override
	public int updateUser(User user) {

		return userDao.updateByPrimaryKeySelective(user);
	}

	@Override
	public ArrayList<User> listByUserName(String userName) {
		// TODO Auto-generated method stub
		return userDao.selectByUserName(userName);
	}

	@Override
	public ORG getOrgByPrimaryKey(String orgId) {

		return orgDao.selectByPrimaryKey(orgId);
	}

	@Override
	public ArrayList<ORG> listAllOrg() {
		// TODO Auto-generated method stub
		return orgDao.selectAllOrg();
	}

	@Override
	public ArrayList<ORG> listChildrenOrg(String parentOrgId) {

		return orgDao.selectChildrenOrg(parentOrgId);
	}

	@Override
	public int selectChildrenOrgCount(String parentOrgId) {
		// TODO Auto-generated method stub
		return orgDao.selectChildrenOrgCount(parentOrgId);
	}

	@Override
	public ArrayList<Department> listDepartmentByFactory(String factory) {
		return departmentDao.selectDepartmentByFactory(factory);
	}

	@Override
	public int addApproveGroup(WfApproveGroupHead group) throws Exception {
		int record = wfApproveGroupHeadDao.insertSelective(group);
		if (group.getGroupItemList() != null) {
			for (WfApproveGroupItemVo groupItem : group.getGroupItemList()) {
				groupItem.setGroupId(group.getGroupId());
			}
			wfApproveGroupItemDao.insertList(group.getGroupItemList());
		}
		return record;
	}

	@Override
	public int deleteApproveGroup(WfApproveGroupHead group) throws Exception {
		int record = wfApproveGroupHeadDao.deleteByPrimaryKey(group.getGroupId());
		wfApproveGroupItemDao.deleteByGroupId(group.getGroupId());
		return record;
	}

	@Override
	public void updateApproveGroup(WfApproveGroupHead group) throws Exception {
		wfApproveGroupHeadDao.updateByPrimaryKeySelective(group);
		if (group.getGroupItemList() != null) {
			wfApproveGroupItemDao.deleteByGroupId(group.getGroupId());
			wfApproveGroupItemDao.insertList(group.getGroupItemList());
		}

	}

	@Override
	public ArrayList<WfApproveGroupItemVo> listApproveGroupItem(WfApproveGroupItem groupItem) throws Exception {

		return wfApproveGroupItemDao.selectByGroupId(groupItem.getGroupId());
	}

	@Override
	public List<WfApproveGroupHead> getApproveGroups(WfApproveGroupHead group) throws Exception {
		return wfApproveGroupHeadDao.selectByCondition(group);
	}

	@Override
	public User getUserByIdForApprove(String userId) throws Exception {
		// TODO Auto-generated method stub
		return userDao.selectByPrimaryKeyForApprove(userId);
	}

	@Override
	public List<Map<String, Object>> getCurrentUserWarehouse(String userId) throws Exception {
		
		return userDao.selectCurrentUserWarehouse(userId);
	}

	@Override
	public List<DicProductLine> getCurrentUserProductLine(String userId) throws Exception {
		
		return dicProductLineMapper.selectDicProductLineList(userId);
	}
	
	
	
	public int  insertRelProductUser(Map<String,Object> param) {		
		
	return dicProductLineMapper.insertRelUserProductLine(param);
	}

	@Override
	public int updateRelProductUser(Map<String, Object> param) throws Exception {
		return dicProductLineMapper.updateRelUserProductLine(param);
	}

	@Override
	public List<DicProductLine> selectAllProductLine() {
		
		return dicProductLineMapper.selectAllProductLine();
	}

	@Override
	public Map<String, Object> getRelUserProductLine(String userId) {	   
		
		return dicProductLineMapper.getRelUserProductLine(userId);
	}

	@Override
	public int changePassword(Map<String,Object> map) {
		
		return userDao.changePassword(map);
	}
	
	@Override
	public Map<String,Object> getPrinterList(String userId) {
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String, Object>> labelList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> paperList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = userDao.getPrinterList();
		for(Map<String, Object> map : list) {
			Map<String,Object> data = new HashMap<String,Object>();
			if(UtilObject.getByteOrNull(map.get("type")) == EnumPrinterType.LABEL_PRINTER.getValue()) {
				data.put("label_printer_id", UtilObject.getIntOrZero(map.get("printer_id")));
				data.put("label_printer_name", UtilObject.getStringOrEmpty(map.get("printer_name")));
				labelList.add(data);
			}else if(UtilObject.getByteOrNull(map.get("type")) == EnumPrinterType.PAPER_PRINTER.getValue()){
				data.put("paper_printer_id", UtilObject.getIntOrZero(map.get("printer_id")));
				data.put("paper_printer_name", UtilObject.getStringOrEmpty(map.get("printer_name")));
				paperList.add(data);
			}
		}
		result.put("label_list", labelList);
		result.put("paper_list", paperList);
		if(userId!=null) {
			List<Map<String, Object>> userPrinterList = userDao.getPrinterListByUserId(userId);
			for(Map<String, Object> map : userPrinterList) {
				if(UtilObject.getByteOrNull(map.get("type")) == EnumPrinterType.LABEL_PRINTER.getValue()) {
					result.put("label_printer_id", UtilObject.getIntOrZero(map.get("printer_id")));
					result.put("label_printer_name", UtilObject.getStringOrEmpty(map.get("printer_name")));
				}else if(UtilObject.getByteOrNull(map.get("type")) == EnumPrinterType.PAPER_PRINTER.getValue()){
					result.put("paper_printer_id", UtilObject.getIntOrZero(map.get("printer_id")));
					result.put("paper_printer_name", UtilObject.getStringOrEmpty(map.get("printer_name")));
				}
			}
		}
		return result;
	}

	@Override
	public int insertPrinterRelUser(String userId, Integer printerId, Byte type) throws Exception{
		printerDao.deletePrinterRelUser(userId, type);
		return printerDao.insertPrinterRelUser(userId, printerId, type);
	}
	
}
