package com.inossem.wms.service.dic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.Department;
import com.inossem.wms.model.dic.DicFactory;

/**
 * 审批部门功能接口
 * 
 * @author 刘宇 2018.01.24
 *
 */
public interface IDepartmentService {

	/**
	 * 审批部门列表and关键字查询
	 * 
	 * @author 刘宇 2018.01.24
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @param condition
	 * @param name
	 * @param ftyId
	 * @param departmentId
	 * @return
	 */
	public List<Map<String, Object>> listDepartment(int pageIndex, int pageSize, int total, String condition,
			String name, Integer ftyId, int departmentId, boolean sortAscend, String sortColumn);

	public List<Map<String, Object>> listDepartmentByFtyId(int ftyId);

	/**
	 * 添加审批部门
	 * 
	 * @author 刘宇 2018.01.24
	 * @param errorString
	 * @param errorStatus
	 * @param code
	 * @param name
	 * @param ftyId
	 * @return
	 */
	public Map<String, Object> addDepartment(int errorCode, String errorString, boolean errorStatus, String code,
			String name, Integer ftyId);

	/**
	 * 修改审批部门
	 * 
	 * @author 刘宇 2018.01.24
	 * @param errorString
	 * @param errorStatus
	 * @param departmentId
	 * @param code
	 * @param name
	 * @param ftyId
	 * @return
	 */
	public Map<String, Object> updateDepartment(int errorCode, String errorString, boolean errorStatus,
			int departmentId, String code, String name, Integer ftyId);

	/**
	 * 查询所有工厂给审批部门
	 * 
	 * @author 刘宇 2018.01.24
	 * @return
	 */
	public List<DicFactory> listFactory();

	/**
	 * 通过ftyId（工厂编号）查询工厂
	 * 
	 * @author 刘宇 2018.01.24
	 * @param ftyId
	 * @return
	 */
	public DicFactory getFactory(Integer ftyId);

	/**
	 * 查询所有审批部门
	 * 
	 * @author 刘宇 2018.03.12
	 * @return
	 */
	public ArrayList<Department> listAlldepartment();

}
