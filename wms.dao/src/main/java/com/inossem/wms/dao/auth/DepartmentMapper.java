package com.inossem.wms.dao.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.Department;

public interface DepartmentMapper {
	int deleteByPrimaryKey(Integer departmentId);

	int insert(Department record);

	/**
	 * 添加审批部门
	 * 
	 * @author 刘宇 2018.01.24
	 * @param record
	 * @return
	 */
	int insertDepartment(Department record);

	int insertSelective(Department record);

	Department selectByPrimaryKey(Integer departmentId);

	/**
	 * 根据工厂id查询其下部门
	 * 
	 * @author 刘宇 2018.01.24
	 * @param factory
	 * @return
	 */
	ArrayList<Department> selectDepartmentByFactory(String factory);

	int updateByPrimaryKeySelective(Department record);

	/**
	 * 修改审批部门
	 * 
	 * @author 刘宇 2018.01.24
	 * @param record
	 * @return
	 */
	int updateDepartmentByPrimaryKey(Department record);

	/**
	 * 查询所有部门
	 * 
	 * @author 刘宇 2018.01.24
	 * @return
	 */
	ArrayList<Department> selectAllDepartment();

	/**
	 * 查询所有审批部门
	 * 
	 * @author 刘宇 2018.01.24
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectAllDepartmentOnPaging(Map<String, Object> map);

	/**
	 * 查询工厂下的部门
	 * 
	 * @return
	 */
	List<Map<String, Object>> selectDepartmentByFtyId(int ftyId);

}