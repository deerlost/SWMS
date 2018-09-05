package com.inossem.wms.dao.dic;

import java.util.List;

import com.inossem.wms.model.dic.DicReceiver;
import com.inossem.wms.model.vo.MatCodeVo;

public interface DicReceiverMapper {
	int deleteByPrimaryKey(Integer id);

	DicReceiver selectByPrimaryKey(Integer id);

	/**
	 * 删除收方
	 * 
	 * @author 刘宇 2018.01.23
	 * @param record
	 * @return
	 */
	int deleteDicReceiverByPrimaryKey(DicReceiver record);

	/**
	 * 添加接收方
	 * 
	 * @author 刘宇 2018.01.23
	 * @param record
	 * @return
	 */
	int insertDicReceiver(DicReceiver record);

	int insertSelective(DicReceiver record);

	int updateByPrimaryKeySelective(DicReceiver record);

	/**
	 * 修改单个接收方
	 * 
	 * @author 刘宇 2018.01.23
	 * @param record
	 * @return
	 */
	int updateDicReceiverByPrimaryKey(DicReceiver record);

	/**
	 * 分页查询所有接收方
	 * 
	 * @author 刘宇 2018.01.23
	 * @param record
	 * @return
	 */
	List<DicReceiver> selectReceiverOnPaging(DicReceiver record);

	/**
	 * 查询所有接收方
	 * 
	 * @author 刘宇 2018.02.01
	 * @return
	 */
	List<DicReceiver> selectReceiver();

	/**
	 * 查询接收方数量
	 * 
	 * @author 刘宇 2018.01.23
	 * @return
	 */
	int selectDicReceiverCount();

	/**
	 * 查询好多好多接收方（测试用）
	 * 
	 * @author 刘宇 2018.02.05
	 * @param utilMatCodes
	 * @return
	 */
	List<DicReceiver> selectByManyOfPrimaryKey(List<MatCodeVo> utilMatCodes);

}