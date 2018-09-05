package com.inossem.wms.service.auth.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.rel.RelUserApproveMapper;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.rel.RelUserApprove;
import com.inossem.wms.service.auth.IApproveService;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.util.UtilString;

/**
 * 审批管理
 * 
 * @author 刘宇 2018.03.12
 *
 */
@Transactional
@Service("approveService")
public class ApproveServiceImpl implements IApproveService {
	@Autowired
	private RelUserApproveMapper approveDao;

	@Resource
	private IUserService userService;

	@Override
	public List<Map<String, Object>> listApproveOnPaging(String condition, int pageIndex, int pageSize, int total,
			boolean sortAscend, String sortColumn) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("condition", condition);// 查询条件
		map.put("pageSize", pageSize);
		map.put("pageIndex", pageIndex);
		map.put("paging", true);
		map.put("sortAscend", sortAscend);
		map.put("sortColumn", sortColumn);
		return approveDao.selectApproveOnPaging(map);
	}

	@Override
	public Map<String, Object> addOrUpdateApprove(String errorString, boolean errorStatus, String id, String userId,
			String receiptType, String group, String node) throws Exception {
		RelUserApprove isUpdateOrAddObj = null;// 判断信息重复
		User userobj = null;// 判断用户存在否
		if (UtilString.isNullOrEmpty(id) || UtilString.isNullOrEmpty(userId) || UtilString.isNullOrEmpty(receiptType)
				|| UtilString.isNullOrEmpty(group) || UtilString.isNullOrEmpty(node)) {
			errorStatus = false;
			errorString = "不可以有空值";
		} else {
			userobj = userService.getUserByIdForApprove(userId);
			isUpdateOrAddObj = getIdAndUserIdOfApproveByExceptId(userId, receiptType, group, node);
			if (isUpdateOrAddObj == null && userobj != null) {
				if (UtilString.getIntForString(id) == 0) {
					RelUserApprove obj = new RelUserApprove();
					obj.setUserId(userId);
					obj.setReceiptType(UtilString.getIntForString(receiptType));
					obj.setGroup(group);
					obj.setNode(node);
					approveDao.insertSelective(obj);
				} else {
					RelUserApprove obj = new RelUserApprove();
					obj.setId(UtilString.getIntForString(id));// 表id
					obj.setUserId(userId);// 用户id
					obj.setReceiptType(UtilString.getIntForString(receiptType));// 类型id
					obj.setGroup(group);// 组id
					obj.setNode(node);// 节点名称
					approveDao.updateByPrimaryKeySelective(obj);
				}
			} else if (userobj == null) {
				errorStatus = false;
				errorString = "该用户不存在";
			} else {
				if (UtilString.getIntForString(id) == 0) {
					if (isUpdateOrAddObj != null) {
						errorStatus = false;
						errorString = "该条信息已存在";
					}
				} else {
					if (isUpdateOrAddObj.getId().intValue() == UtilString.getIntForString(id) && userobj != null) {
						RelUserApprove obj = new RelUserApprove();
						obj.setId(UtilString.getIntForString(id));// 表id
						obj.setUserId(userId);// 用户id
						obj.setReceiptType(UtilString.getIntForString(receiptType));// 类型id
						obj.setGroup(group);// 组id
						obj.setNode(node);// 节点名称
						approveDao.updateByPrimaryKeySelective(obj);
					} else {
						if (isUpdateOrAddObj.getId().intValue() != UtilString.getIntForString(id)) {
							errorStatus = false;
							errorString = "该条信息已存在";
						}
						if (userobj == null) {
							errorStatus = false;
							errorString = "该用户不存在";
						}
					}
				}
			}
		}
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("errorStatus", errorStatus);
		errorMap.put("errorString", errorString);
		return errorMap;
	}

	@Override
	public RelUserApprove getIdAndUserIdOfApproveByExceptId(String userId, String receiptType, String group,
			String node) throws Exception {
		RelUserApprove obj = new RelUserApprove();
		obj.setUserId(userId);
		obj.setReceiptType(UtilString.getIntForString(receiptType));
		obj.setGroup(group);
		obj.setNode(node);
		return approveDao.selectIdAndUserIdOfApproveByExceptId(obj);
	}

	@Override
	public int deleteApprove(String id) throws Exception {
		// TODO Auto-generated method stub
		return approveDao.deleteByPrimaryKey(UtilString.getIntForString(id));
	}

	@Override
	public Map<String, Object> getApproveByUserId(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return approveDao.selectApproveByUserId(id);
	}
}
