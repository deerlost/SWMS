package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicReceiver;
import com.inossem.wms.model.vo.MatCodeVo;

/**
 * 接收方功能接口类
 * 
 * @author 刘宇 2018.01.23
 *
 */
public interface IDicReceiverService {
	/**
	 * 删除单个接收方
	 * 
	 * @author 刘宇 2018.01.23
	 * @param errorString
	 * @param errorStatus
	 * @param id
	 * @return
	 */
	Map<String, Object> deleteDicReceiver(String errorString, boolean errorStatus, int id);

	/**
	 * 分页查找全部接收方
	 * 
	 * @author 刘宇 2018.01.23
	 * @param condition
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @param receiveCode
	 * @return
	 */
	public List<DicReceiver> listDicReceiver(String condition, int pageIndex, int pageSize, int total,
			String receiveCode, int id, boolean sortAscend, String sortColumn);

	/**
	 * 查找全部接收方
	 * 
	 * @author 刘宇 2018.02.01
	 * @return
	 */
	public List<DicReceiver> listAllDicReceiver();

	/**
	 * 添加接收方
	 * 
	 * @author 刘宇 2018.01.23
	 * @param errorString
	 * @param errorStatus
	 * @param receiveCode
	 * @param receiveName
	 * @return
	 */
	public Map<String, Object> addDicReceiver(int errorCode, String errorString, boolean errorStatus,
			String receiveCode, String receiveName);

	/**
	 * 修改接收方信息
	 * 
	 * @author 刘宇 2018.01.23
	 * @param errorString
	 * @param errorStatus
	 * @param id
	 * @param receiveCode
	 * @param receiveName
	 * @return
	 */
	Map<String, Object> updateDicReceiver(int errorCode, String errorString, boolean errorStatus, int id,
			String receiveCode, String receiveName);

	/**
	 * 查询接收方数量
	 * 
	 * @author 刘宇 2018.01.23
	 * 
	 * @return
	 */
	public int selectDicReceiverCount();

	/**
	 * 查询好多好多接收方 （测试用）
	 * 
	 * @author 刘宇 2018.02.05
	 * @param utilMatCodes
	 * @return
	 */
	List<DicReceiver> selectByManyOfPrimaryKey(List<MatCodeVo> utilMatCodes);

}
