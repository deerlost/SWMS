package com.inossem.wms.service.dic;

import java.util.List;

import com.inossem.wms.model.dic.DicWarehouseArea;

/**
 * 储存区
 * 
 * @author 刘宇 2018.03.01
 *
 */
public interface IAreaService {
	/**
	 * 查询全部储存区id，编码，描述
	 * 
	 * @author 刘宇 2018.03.01
	 * @return
	 */
	List<DicWarehouseArea> listAreaIdAndAreaCodeAndAreaName() throws Exception;
}
