package com.inossem.wms.service.dic.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicInstallationMapper;
import com.inossem.wms.dao.dic.DicProductLineMapper;
import com.inossem.wms.dao.rel.RelProductInstallationMapper;
import com.inossem.wms.model.dic.DicInstallation;
import com.inossem.wms.model.dic.DicProductLine;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.rel.RelProductInstallation;
import com.inossem.wms.service.dic.IProductLineService;
import com.inossem.wms.util.UtilObject;

import net.sf.json.JSONObject;
@Transactional

@Service("productLineService")
public class ProductLineServiceImpl implements IProductLineService {
	@Autowired
	private DicProductLineMapper dicProductLineMapper;

	@Autowired
	private RelProductInstallationMapper relProductInstallationMapper;

	@Autowired
	private DicInstallationMapper dicInstallationMapper;

	@Override
	public List<DicProductLine> getDicProductLineList(Map<String, Object> parameter) {

		return dicProductLineMapper.selectProductLineOnpaging(parameter);
	}

	@Override
	public boolean saveOrUpdateProductLine(JSONObject json) {
		relProductInstallationMapper.updateIsDeleteAll();
		List<Map<String, Object>> listMap = (List<Map<String, Object>>) json.get("rel_product_line_installation");
		for (Map<String, Object> map : listMap) {
			if (map.containsKey("product_line_id")) {
				DicProductLine dicProductLine = new DicProductLine();
				dicProductLine.setProductLineId(UtilObject.getIntegerOrNull(map.get("product_line_id")));
				dicProductLine.setProductLineName(UtilObject.getStringOrEmpty(map.get("product_line_name")));
				dicProductLine.setIsDelete(EnumBoolean.FALSE.getValue());
				dicProductLine.setIsUsed(UtilObject.getByteOrNull(map.get("is_used")));
				dicProductLineMapper.updateByPrimaryKeySelective(dicProductLine);
				if (map.containsKey("installations")) {
					List<Map<String, Object>> installationList = (List<Map<String, Object>>) map.get("installations");
					for (Map<String, Object> installationMap : installationList) {
						if (installationMap.containsKey("installation_id")) {
							RelProductInstallation relProductInstallation = new RelProductInstallation();
							DicInstallation dicInstallation = new DicInstallation();
							dicInstallation.setInstallationId(
									UtilObject.getIntegerOrNull(installationMap.get("installation_id")));
							dicInstallation.setInstallationName(
									UtilObject.getStringOrEmpty(installationMap.get("installation_name")));
							dicInstallation.setIsDelete(EnumBoolean.FALSE.getValue());
							dicInstallation.setIsUsed(UtilObject.getByteOrNull(installationMap.get("is_used")));
							dicInstallationMapper.updateByPrimaryKeySelective(dicInstallation);
							relProductInstallation.setInstallationId(dicInstallation.getInstallationId());
							relProductInstallation.setProductLineId(dicProductLine.getProductLineId());
							relProductInstallationMapper.insertSelective(relProductInstallation);
						} else {
							RelProductInstallation relProductInstallation = new RelProductInstallation();
							DicInstallation dicInstallation = new DicInstallation();
							dicInstallation.setInstallationName(
									UtilObject.getStringOrEmpty(installationMap.get("installation_name")));
							dicInstallation.setIsDelete(EnumBoolean.FALSE.getValue());
							dicInstallation.setIsUsed(UtilObject.getByteOrNull(installationMap.get("is_used")));
							dicInstallationMapper.insertSelective(dicInstallation);
							relProductInstallation.setInstallationId(dicInstallation.getInstallationId());
							relProductInstallation.setProductLineId(dicProductLine.getProductLineId());
							relProductInstallationMapper.insertSelective(relProductInstallation);

						}
					}
				}

			} else {
				DicProductLine dicProductLine = new DicProductLine();
				dicProductLine.setProductLineName(UtilObject.getStringOrEmpty(map.get("product_line_name")));
				dicProductLine.setIsUsed(UtilObject.getByteOrNull(map.get("is_used")));
				dicProductLine.setIsDelete(EnumBoolean.FALSE.getValue());
				dicProductLineMapper.insertSelective(dicProductLine);
				if (map.containsKey("installations")) {
					List<Map<String, Object>> installationList = (List<Map<String, Object>>) map.get("installations");
					for (Map<String, Object> installationMap : installationList) {
						if (installationMap.containsKey("installation_id")) {
							RelProductInstallation relProductInstallation = new RelProductInstallation();
							DicInstallation dicInstallation = new DicInstallation();
							dicInstallation.setInstallationId(
									UtilObject.getIntegerOrNull(installationMap.get("installation_id")));
							dicInstallation.setInstallationName(
									UtilObject.getStringOrEmpty(installationMap.get("installation_name")));
							dicInstallation.setIsDelete(EnumBoolean.FALSE.getValue());
							dicInstallation.setIsUsed(UtilObject.getByteOrNull(installationMap.get("is_used")));
							dicInstallationMapper.updateByPrimaryKeySelective(dicInstallation);
							relProductInstallation.setInstallationId(dicInstallation.getInstallationId());
							relProductInstallation.setProductLineId(dicProductLine.getProductLineId());
							relProductInstallationMapper.insertSelective(relProductInstallation);
						} else {
							RelProductInstallation relProductInstallation = new RelProductInstallation();
							DicInstallation dicInstallation = new DicInstallation();
							dicInstallation.setInstallationName(
									UtilObject.getStringOrEmpty(installationMap.get("installation_name")));
							dicInstallation.setIsDelete(EnumBoolean.FALSE.getValue());
							dicInstallation.setIsUsed(UtilObject.getByteOrNull(installationMap.get("is_used")));
							dicInstallationMapper.insertSelective(dicInstallation);
							relProductInstallation.setInstallationId(dicInstallation.getInstallationId());
							relProductInstallation.setProductLineId(dicProductLine.getProductLineId());
							relProductInstallationMapper.insertSelective(relProductInstallation);

						}
					}
				}

			}
		}

		// boolean isAdd=true;
		// if(json.containsKey("product_line_id")) {
		// dicProductLine.setProductLineId(UtilObject.getIntegerOrNull(json.get("product_line_id")));
		// isAdd=false;
		// }
		// dicProductLine.setProductLineName(UtilObject.getStringOrEmpty(json.get("product_line_name")));
		// dicProductLine.setIsDelete(EnumBoolean.FALSE.getValue());
		// if(isAdd) {
		// dicProductLineMapper.insertSelective(dicProductLine);
		// }else {
		// dicProductLineMapper.updateByPrimaryKeySelective(dicProductLine);
		// }
		return true;
	}

	@Override
	public int deleteProductLine(Integer productLineId) {

		return dicProductLineMapper.updateIsDeleteByKey(productLineId);
	}

	@Override
	public List<DicProductLine> selectProductInstallationList() {

		return dicProductLineMapper.selectProductInstallationList();
	}

}
