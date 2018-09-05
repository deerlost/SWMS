package com.inossem.wms.service.dic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.auth.DepartmentMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.model.auth.Department;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.dic.IDepartmentService;
import com.inossem.wms.util.UtilString;

/**
 * 审批部门功能实现类
 * 
 * @author 刘宇 2018.01.24
 *
 */
@Transactional
@Service("departmentService")
public class DepartmentServiceImpl implements IDepartmentService {

	@Resource
	private DicFactoryMapper dicFactoryDao;

	@Resource
	private DepartmentMapper departmentDao;

	/**
	 * 审批部门列表and关键字查询
	 */
	@Override
	public List<Map<String, Object>> listDepartment(int pageIndex, int pageSize, int total, String condition,
			String name, Integer ftyId, int departmentId, boolean sortAscend, String sortColumn) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageSize", pageSize);
		map.put("pageIndex", pageIndex);
		map.put("condition", condition);
		map.put("paging", true);
		map.put("name", name);// 审批部门编描述
		map.put("ftyId", ftyId);// 工厂id
		map.put("department_id", departmentId);// 审批部门表主键id
		map.put("sortAscend", sortAscend);
		map.put("sortColumn", sortColumn);
		return departmentDao.selectAllDepartmentOnPaging(map);
	}

	@Override
	public List<Map<String, Object>> listDepartmentByFtyId(int ftyId) {
		return departmentDao.selectDepartmentByFtyId(ftyId);
	}

	/**
	 * 添加审批部门
	 */
	@Override
	public Map<String, Object> addDepartment(int errorCode, String errorString, boolean errorStatus, String code,
			String name, Integer ftyId) {
		DicFactory t001w = getFactory(ftyId);
		List<Map<String, Object>> objs = listDepartment(EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(),
				EnumPage.TOTAL_COUNT.getValue(), UtilString.STRING_EMPTY, name, ftyId, 0, true,
				UtilString.STRING_EMPTY);
		// 判断部门编号是否重复所用的查询 Map<String, Object> map1 = new HashMap<String,
		// Object>();
		// map1.put("code", code);// 审批部门描述
		// List<Map<String, Object>> objs1 =
		// departmentService.listDepartment(map1);
		if (UtilString.isNullOrEmpty(name) == true) {
			errorStatus = false;
			errorString = "部门不可以是空值";

		} else if (t001w == null) {
			errorStatus = false;
			errorString = "该工厂不存在";
		}
		// 判断部门编号是否重复 else if (objs1.size() != 0) {
		// errorStatus = false;
		// errorString = "该部门编号已存在";
		// }
		else if (objs.size() != 0) {
			errorStatus = false;
			errorString = "该部门已经绑定该工厂";
		} else {
			Department department = new Department();
			department.setCode(code); // 审批部门编号
			department.setFtyId(ftyId); // 工厂编号
			department.setName(name); // 审批部门描述
			departmentDao.insertDepartment(department);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		}
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("errorCode", errorCode);
		errorMap.put("errorStatus", errorStatus);
		errorMap.put("errorString", errorString);
		return errorMap;
	}

	/**
	 * 修改审批部门
	 */
	@Override
	public Map<String, Object> updateDepartment(int errorCode, String errorString, boolean errorStatus,
			int departmentId, String code, String name, Integer ftyId) {
		DicFactory t001w = getFactory(ftyId);

		List<Map<String, Object>> objs = listDepartment(EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(),
				EnumPage.TOTAL_COUNT.getValue(), UtilString.STRING_EMPTY, UtilString.STRING_EMPTY, 0, departmentId,
				true, UtilString.STRING_EMPTY);
		// 判断部门编号是否重复用的查询 String codeString = (String)
		// objs.get(0).get("code");
		// Map<String, Object> map1 = new HashMap<String, Object>();
		// map1.put("code", code);// 审批部门编号
		// List<Map<String, Object>> objs1 =
		// departmentService.listDepartment(map1);
		List<Map<String, Object>> objs2 = listDepartment(EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(),
				EnumPage.TOTAL_COUNT.getValue(), UtilString.STRING_EMPTY, name, ftyId, 0, true,
				UtilString.STRING_EMPTY);
		int departmentIdErr = departmentId;
		if (objs2.size() != 0) {
			departmentIdErr = Integer.parseInt(objs2.get(0).get("department_id").toString());
		}

		if (UtilString.isNullOrEmpty(name) == true) {
			errorStatus = false;
			errorString = "部门不可以是空值";
		} else if (objs.size() == 0) {
			errorStatus = false;
			errorString = "该部门不存在";
		}
		// 判断部门编号是否重复 else if (objs.size() != 0 && codeString.equals(code)
		// == false && objs1.size() != 0) {
		// errorStatus = false;
		// errorString = "已存在该部门编号";
		// }
		else if (t001w == null) {
			errorStatus = false;
			errorString = "该工厂不存在";
		} else if (departmentIdErr != departmentId && departmentIdErr != 0) {
			errorStatus = false;
			errorString = "该部门已经绑定该工厂";
		} else {
			Department department2 = new Department();
			department2.setDepartmentId(departmentId); // 审批部门表主键id
			department2.setCode(code); // 审批部门编号
			department2.setName(name); // 审批部门描述
			department2.setFtyId(ftyId); // 工厂id

			departmentDao.updateDepartmentByPrimaryKey(department2);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		}
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("errorCode", errorCode);
		errorMap.put("errorStatus", errorStatus);
		errorMap.put("errorString", errorString);
		return errorMap;
	}

	/**
	 * 查询所有工厂的工厂编号和工厂名称
	 */
	@Override
	public List<DicFactory> listFactory() {
		// TODO Auto-generated method stub
		return dicFactoryDao.selectAllFactoryForDepartment();
	}

	/**
	 * 按照fty_id查询工厂
	 */
	@Override
	public DicFactory getFactory(Integer ftyId) {
		// TODO Auto-generated method stub
		return dicFactoryDao.selectFactoryByFtyIdForDepartment(ftyId);
	}

	@Override
	public ArrayList<Department> listAlldepartment() {
		// TODO Auto-generated method stub
		return departmentDao.selectAllDepartment();
	}

}
