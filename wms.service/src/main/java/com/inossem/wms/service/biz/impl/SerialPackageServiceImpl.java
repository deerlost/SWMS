package com.inossem.wms.service.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicPackClassificationMapper;
import com.inossem.wms.dao.dic.DicPackageTypeMapper;
import com.inossem.wms.dao.serial.SerialPackageMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.dic.DicPackClassification;
import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.serial.SerialPackage;
import com.inossem.wms.model.vo.SerialPackageVo;
import com.inossem.wms.service.biz.ISerialPackageService;
import com.inossem.wms.util.UtilObject;

import net.sf.json.JSONObject;

@Service
@Transactional
public class SerialPackageServiceImpl implements ISerialPackageService {

	@Autowired
	private SerialPackageMapper serialPackageDao;
	@Autowired
	private DicPackClassificationMapper packClassDao;
	@Autowired
	private DicPackageTypeMapper packageTypeDao;
	@Autowired
	private SequenceDAO sequenceDao;
	
	/**
	 * 查询包装物sn集合
	 */
	@Override
	public List<SerialPackageVo> selectSerialPackageList(Map<String, Object> param) {
		return serialPackageDao.selectListOnPaging(param);
	}
	
	/**
	 * 查询包装分类  包装类型 供应商
	 */
	@Override
	public List<DicPackClassification> selectClassList() {
		return packClassDao.selectAll();
	}
	
	/**
	 * 获取流水号
	 * @param supplierId
	 * @param packageTypeId
	 * @return
	 */
	public String getNextCode(Integer supplierId,Integer packageTypeId) {
		String serial_package = sequenceDao.selectNextVal("serial_package")+"";
		while (serial_package.length() < 10) { // 不够十位数字，自动补0
			serial_package = "0" + serial_package;
		}
		String date = DateUtil.formatDate(new Date(), "yyMM");
		serial_package = date+serial_package;
		String sup = supplierId.toString();
		while(sup.length()<2) {
			sup = "0"+sup;
		}
		serial_package = sup+serial_package;
		DicPackageType packageType = packageTypeDao.selectByPrimaryKey(packageTypeId);
		return packageType.getSnSerialKey()+serial_package;
	}
	
	/**
	 * 批量删除
	 */
	@Override
	public int deleteSerialPackageByIds(List<Integer> ids,String userId) throws Exception {
		for(Integer id : ids) {
			SerialPackage pack = new SerialPackage();
			pack.setPackageId(id);
			pack.setIsDelete(EnumBoolean.TRUE.getValue());
			pack.setModifyTime(new Date());
			pack.setModifyUser(userId);
			serialPackageDao.updateByPrimaryKeySelective(pack);
		}
		return 1;
	}

	/**
	 * 创建包装物sn
	 */
	@Override
	public List<Integer> saveSerialPackage(JSONObject json,String userId) throws Exception {
		List<Integer> result = new ArrayList<Integer>();
		Integer packageNum = UtilObject.getIntegerOrNull(json.get("package_num"));
		if(packageNum == null) {
			throw new WMSException("包装数量为空");
		}
		Integer packageTypeId = UtilObject.getIntegerOrNull(json.get("package_type_id"));
		if(packageTypeId == null) {
			throw new WMSException("包装物类型为空");
		}
		Integer supplierId = UtilObject.getIntegerOrNull(json.get("supplier_id"));
		if(supplierId == null) {
			throw new WMSException("供应商信息为空");
		}
		for(int i = 0; i < packageNum; i++) {
			SerialPackage serialPackage = new SerialPackage();
			serialPackage.setPackageTypeId(packageTypeId);
			serialPackage.setSupplierId(supplierId);
			serialPackage.setStatus(EnumBoolean.FALSE.getValue());
			serialPackage.setIsDelete(EnumBoolean.FALSE.getValue());
			serialPackage.setPackageCode(this.getNextCode(supplierId,packageTypeId));
			serialPackage.setCreateUser(userId);
			serialPackage.setModifyUser(userId);
			serialPackage.setCreateTime(new Date());
			serialPackage.setModifyTime(new Date());
			serialPackageDao.insertSelective(serialPackage);
			result.add(serialPackage.getPackageId());
		}
		return result;
	}

}
