package com.inossem.wms.service.dic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.inossem.wms.dao.dic.DicReceiverMapper;
import com.inossem.wms.model.dic.DicReceiver;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.MatCodeVo;
import com.inossem.wms.service.dic.IDicReceiverService;
import com.inossem.wms.util.UtilString;

/**
 * 接收方功能实现类
 * 
 * @author 刘宇 2018.01.23
 *
 */
@Service("dicReceiverService")
public class DicReceiverServiceImpl implements IDicReceiverService {
	@Resource
	private DicReceiverMapper dicReceiverDao;

	/**
	 * 删除接收方 刘宇
	 */
	@Override
	public Map<String, Object> deleteDicReceiver(String errorString, boolean errorStatus, int id) {

		List<DicReceiver> receivers = listDicReceiver(UtilString.STRING_EMPTY, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), UtilString.STRING_EMPTY, id, true,
				UtilString.STRING_EMPTY);
		if (receivers.size() == 0 || id <= 0) {
			errorStatus = false;
			errorString = "此id不存在";
		} else if (receivers.size() != 0 && id > 0) {
			DicReceiver obj = new DicReceiver();

			obj.setId(id);
			dicReceiverDao.deleteDicReceiverByPrimaryKey(obj);
		}
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("errorStatus", errorStatus);
		errorMap.put("errorString", errorString);
		return errorMap;
	}

	/**
	 * 分页查询接收方列表and条件查询 刘宇
	 * 
	 */
	@Override
	public List<DicReceiver> listDicReceiver(String condition, int pageIndex, int pageSize, int total,
			String receiveCode, int id, boolean sortAscend, String sortColumn) {
		DicReceiver obj = new DicReceiver();
		obj.setPaging(true);
		obj.setPageSize(pageSize);
		obj.setPageIndex(pageIndex);
		obj.setCondition(condition);
		obj.setTotalCount(total);
		obj.setReceiveCode(receiveCode);
		obj.setId(id);
		obj.setSortAscend(sortAscend);
		obj.setSortColumn(sortColumn);
		return dicReceiverDao.selectReceiverOnPaging(obj);
	}

	/**
	 * 添加接收方 刘宇
	 */
	@Override
	public Map<String, Object> addDicReceiver(int errorCode, String errorString, boolean errorStatus,
			String receiveCode, String receiveName) {
		List<DicReceiver> objreceiversErr1 = null;

		objreceiversErr1 = listDicReceiver(UtilString.STRING_EMPTY, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), receiveCode, 0, true,
				UtilString.STRING_EMPTY);

		if (UtilString.isNullOrEmpty(receiveCode) == true || UtilString.isNullOrEmpty(receiveName) == true) {
			errorStatus = false;
			errorString = "不可有空值";

		} else if (objreceiversErr1.size() != 0 && UtilString.isNullOrEmpty(receiveCode) == false) {
			errorString = "接收方代码重复无法添加";
			errorStatus = false;

		} else if (objreceiversErr1.size() == 0 && UtilString.isNullOrEmpty(receiveCode) == false
				&& UtilString.isNullOrEmpty(receiveName) == false) {
			DicReceiver receiverObj = new DicReceiver();
			receiverObj.setReceiveCode(receiveCode);// 接收方代码
			receiverObj.setReceiveName(receiveName);// 接收方描述
			dicReceiverDao.insertDicReceiver(receiverObj);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		}
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("errorStatus", errorStatus);
		errorMap.put("errorString", errorString);
		errorMap.put("errorCode", errorString);
		return errorMap;

	}

	/**
	 * 修改接收方 刘宇
	 */
	@Override
	public Map<String, Object> updateDicReceiver(int errorCode, String errorString, boolean errorStatus, int id,
			String receiveCode, String receiveName) {
		DicReceiver receiver = new DicReceiver();
		receiver.setId(id);
		List<DicReceiver> receivers = listDicReceiver(UtilString.STRING_EMPTY, EnumPage.PAGE_INDEX.getValue(),
				EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), UtilString.STRING_EMPTY, id, true,
				UtilString.STRING_EMPTY);
		if (UtilString.isNullOrEmpty(receiveCode) == true || UtilString.isNullOrEmpty(receiveName) == true) {
			errorStatus = false;
			errorString = "不可有空值";

		} else if (receivers.size() == 0) {
			errorStatus = false;
			errorString = "此id不存在无法修改";

		} else if (receivers.size() != 0) {
			List<DicReceiver> receivers3 = listDicReceiver(UtilString.STRING_EMPTY, EnumPage.PAGE_INDEX.getValue(),
					EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), receiveCode, 0, true,
					UtilString.STRING_EMPTY);
			if (receivers3.size() != 0 && receivers3.get(0).getId() != id) {
				errorStatus = false;
				errorString = "此编号与编号重复无法修改";
			} else if (UtilString.isNullOrEmpty(receiveCode) == false
					&& UtilString.isNullOrEmpty(receiveName) == false) {
				DicReceiver receiverObj = new DicReceiver();
				receiverObj.setId(id);// 接收方表主键
				receiverObj.setReceiveCode(receiveCode);// 接收方代码
				receiverObj.setReceiveName(receiveName);// 接收方描述
				dicReceiverDao.updateDicReceiverByPrimaryKey(receiverObj);
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			}
		}
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("errorStatus", errorStatus);
		errorMap.put("errorString", errorString);
		errorMap.put("errorCode", errorCode);
		return errorMap;

	}

	/**
	 * 查询接收方数量 刘宇
	 */
	@Override
	public int selectDicReceiverCount() {
		// TODO Auto-generated method stub
		int a = dicReceiverDao.selectDicReceiverCount();
		return a;
	}

	/**
	 * 查找全部接收方
	 */
	@Override
	public List<DicReceiver> listAllDicReceiver() {
		// TODO Auto-generated method stub
		return dicReceiverDao.selectReceiver();
	}

	@Override
	public List<DicReceiver> selectByManyOfPrimaryKey(List<MatCodeVo> utilMatCodes) {
		// TODO Auto-generated method stub
		return dicReceiverDao.selectByManyOfPrimaryKey(utilMatCodes);
	}

}
