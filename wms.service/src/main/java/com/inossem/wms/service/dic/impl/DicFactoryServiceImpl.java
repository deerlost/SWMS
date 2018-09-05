package com.inossem.wms.service.dic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.vo.DicFactoryVo;
import com.inossem.wms.service.dic.IDicFactoryService;
import com.inossem.wms.util.UtilString;

@Transactional
@Service("dicFactoryServiceImpl")
public class DicFactoryServiceImpl implements IDicFactoryService {

	private static Logger logger = LoggerFactory.getLogger(DicFactoryServiceImpl.class);
	@Autowired
	private DicFactoryMapper ftyMapper;

	/**
	 * 获取单个工厂
	 */
	@Override
	public DicFactory getFactoryByCode(String ftyCode) {
		if (!UtilString.isNullOrEmpty(ftyCode)) {
			return ftyMapper.selectByCode(ftyCode);
		}
		return null;
	}

	@Override
	public List<DicFactory> getFactoryByCorpCode(String corpCode) {
		return ftyMapper.selectByCorpCode(corpCode);
	}

	@Override
	public List<DicFactory> getFactoryByCorpId(int corpId) {
		return ftyMapper.selectByCorpId(corpId);
	}

	/**
	 * 保存
	 */
	@Override
	public int saveFactory(boolean isAdd, DicFactory fty) {
		if (isAdd) {
			return ftyMapper.insertSelective(fty);
		} else {
			return ftyMapper.updateByPrimaryKey(fty);
		}
	}

	@Override
	public DicFactory getFactoryById(int ftyId) {
		// TODO Auto-generated method stub
		return ftyMapper.selectByPrimaryKey(ftyId);
	}

	@Override
	public List<DicFactoryVo> getAllFactory() {
		// TODO Auto-generated method stub
		return ftyMapper.selectAllFactory();
	}

	@Override
	public List<Map<String, Object>> listFactoryOnPaging(String condition, int pageIndex, int pageSize, int total,
			boolean sortAscend, String sortColumn) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("condition", condition);// 查询条件
		map.put("pageSize", pageSize);
		map.put("pageIndex", pageIndex);
		map.put("paging", true);
		map.put("sortAscend", sortAscend);
		map.put("sortColumn", sortColumn);
		return ftyMapper.selectFactoryOnPaging(map);
	}

	@Override
	public Map<String, Object> addOrUpdateFactory(int errorCode, String errorString, boolean errorStatus, boolean isAdd,
			Integer ftyId, String ftyCode, String ftyName, String address, Integer corpId) throws Exception {
		DicFactory isUpdateObj = null;
		if (isAdd) {
			DicFactory isAddObj = getFactoryByFtyCode(ftyCode);
			isUpdateObj = selectFactoryByFtyCodeAndCorpId(ftyCode, corpId);
			if (isAddObj != null) {
				errorStatus = false;
				errorString = "该工厂编号已经存在";
			} else if (isUpdateObj != null) {
				errorStatus = false;
				errorString = "该工厂已经归属于该公司";
			} else if (UtilString.isNullOrEmpty(ftyCode) || UtilString.isNullOrEmpty(ftyName)
					|| UtilString.isNullOrEmpty(address) || corpId == null || corpId < 0) {
				errorStatus = false;
				errorString = "不可以有空值和负值";
			} else {
				DicFactory obj = new DicFactory();
				obj.setFtyCode(ftyCode);
				obj.setFtyName(ftyName);
				obj.setAddress(address);
				obj.setCorpId(corpId);
				ftyMapper.insertSelective(obj);
				errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			}
		} else {
			isUpdateObj = selectFactoryByFtyCodeAndCorpId(ftyCode, corpId);

			if (isUpdateObj != null) {
				if (isUpdateObj.getFtyId().intValue() != ftyId) {
					errorStatus = false;
					errorString = "该工厂已经归属于该公司";
				} else {
					if (UtilString.isNullOrEmpty(ftyCode) || UtilString.isNullOrEmpty(ftyName)
							|| UtilString.isNullOrEmpty(address) || corpId == null || corpId < 0) {
						errorStatus = false;
						errorString = "不可以有空值和负值";
					} else {
						DicFactory obj = new DicFactory();
						obj.setFtyId(ftyId);
						obj.setFtyCode(ftyCode);
						obj.setFtyName(ftyName);
						obj.setAddress(address);
						obj.setCorpId(corpId);
						ftyMapper.updateByPrimaryKeySelective(obj);
						errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
					}
				}
			} else {
				if (UtilString.isNullOrEmpty(ftyCode) || UtilString.isNullOrEmpty(ftyName)
						|| UtilString.isNullOrEmpty(address) || corpId == null || corpId < 0) {
					errorStatus = false;
					errorString = "不可以有空值和负值";
				} else {
					DicFactory obj = new DicFactory();
					obj.setFtyId(ftyId);
					obj.setFtyCode(ftyCode);
					obj.setFtyName(ftyName);
					obj.setAddress(address);
					obj.setCorpId(corpId);
					ftyMapper.updateByPrimaryKeySelective(obj);
					errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				}
			}
		}
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("errorCode", errorCode);
		errorMap.put("errorStatus", errorStatus);
		errorMap.put("errorString", errorString);
		return errorMap;
	}

	@Override
	public DicFactory getFactoryByFtyCode(String ftyCode) {

		return ftyMapper.selectFactoryByFtyCode(ftyCode);
	}

	@Override
	public DicFactory selectFactoryByFtyCodeAndCorpId(String ftyCode, Integer corpId) throws Exception {
		DicFactory obj = new DicFactory();
		obj.setFtyCode(ftyCode);
		obj.setCorpId(corpId);
		return ftyMapper.selectFactoryByFtyCodeAndCorpId(obj);
	}

	@Override
	public Map<String, Object> getFtyById(Integer ftyId, CurrentUser user) {

		Map<String, Object> body = new HashMap<String, Object>();
		try {
			DicFactory dw = null;

			dw = ftyMapper.selectByPrimaryKey(ftyId);
			if (null == dw) {
				dw = new DicFactory();
			}

			body.put("fty_id", dw.getFtyId());
			body.put("fty_code", dw.getFtyCode());
			body.put("fty_name", dw.getFtyName());
			body.put("address", dw.getAddress());
			body.put("corp_id", dw.getCorpId());

		} catch (Exception e) {

		}

		return body;

	}

	@Override
	public List<Map<String, Object>> listFtyIdAndName() throws Exception {

		return ftyMapper.listFtyIdAndName();
	}

}
