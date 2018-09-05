package com.inossem.service;

import java.util.List;
import java.util.Map;

/** 
* @author 高海涛 
* @version 2017年9月27日
* 打印服务
*/
public interface PrintService {
	
	/**
	 * 生成打印文件
	 * @date 2017年9月27日
	 * @author 高海涛
	 * @param dataList
	 * @param type
	 * @return
	 */
	public String mdPrint(@SuppressWarnings("rawtypes") List<Map> dataList,String type);
	
}
