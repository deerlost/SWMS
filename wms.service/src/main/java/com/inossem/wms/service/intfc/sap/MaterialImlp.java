package com.inossem.wms.service.intfc.sap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.inossem.wms.constant.Const;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.intfc.IMaterial;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilREST;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

@Service("materialSap")
public class MaterialImlp implements IMaterial {

	@Autowired
	private IDictionaryService dictionaryService;

	private void setMaterilaListFromSAP(JSONObject json, List<DicMaterial> materialList) {

		JSONArray dataList = json.getJSONArray("E_MARA");
		for (int i = 0; i < dataList.size(); i++) {
			DicMaterial material = new DicMaterial();
			JSONObject matJson = dataList.getJSONObject(i);
			String matCode = matJson.getString("MATNR");
			material.setMatCode(matCode);
			Integer matId = dictionaryService.getMatIdByMatCode(matCode);
			if (matId != null && matId.compareTo(0) == 1) {
				material.setMatId(matId);
			}
			// matJson.getString("LVORM")删除标记
			String matName = matJson.getString("MAKTX");
			material.setMatName(matName);
			String unitCode = matJson.getString("MEINS");
			material.setUnitCode(unitCode);
			Integer unitId = dictionaryService.getUnitIdByUnitCode(unitCode);
			material.setUnitId(unitId);
			String matGroupCode = matJson.getString("MATKL");
			Integer matGroupId = dictionaryService.getMatGroupIdByCode(matGroupCode);
			material.setMatGroupId(matGroupId);
			String matType = matJson.getString("MTART");
			material.setMatType(matType);
			String length = matJson.getString("LAENG");
			material.setLength(UtilObject.getBigDecimalOrZero(length));
			String width = matJson.getString("BREIT");
			material.setWidth(UtilObject.getBigDecimalOrZero(width));
			String height = matJson.getString("HOEHE");
			material.setHeight(UtilObject.getBigDecimalOrZero(height));
			String unitHeightStr = matJson.getString("MEABM");
			int unitHeight = dictionaryService.getUnitIdByUnitCode(unitHeightStr);
			material.setUnitHeight(UtilObject.getIntOrZero(unitHeight));
			String grossWeight = matJson.getString("BRGEW");
			material.setGrossWeight(UtilObject.getBigDecimalOrZero(grossWeight));
			String netWeight = matJson.getString("NTGEW");
			material.setNetWeight(UtilObject.getBigDecimalOrZero(netWeight));
			String unitWeightStr = matJson.getString("GEWEI");
			int unitWeight = dictionaryService.getUnitIdByUnitCode(unitWeightStr);
			material.setUnitWeight(UtilObject.getIntOrZero(unitWeight));
			String volume = matJson.getString("VOLUM");
			material.setVolume(UtilObject.getBigDecimalOrZero(volume));
			String unitVolumeStr = matJson.getString("VOLEH");
			int unitVolume = dictionaryService.getUnitIdByUnitCode(unitVolumeStr);
			material.setUnitVolume(UtilObject.getIntOrZero(unitVolume));
			if (matJson.containsKey("ZASSETA")) {
				String assetAttr = matJson.getString("ZASSETA");
				material.setAssetAttr(UtilObject.getByteOrNull(assetAttr));
			} else {
				material.setAssetAttr((byte) 80);
			}
			materialList.add(material);
		}

	}

	@Override
	public List<DicMaterial> syncMaterial(String matCode) throws Exception {

		JSONObject params = new JSONObject();
		JSONObject I_IMPORT = new JSONObject();

		String dateStr = UtilString.getLongStringForDate(new Date());
		String ZDATE = dateStr.substring(0, 10);
		String ZTIME = dateStr.substring(11, 19);
		I_IMPORT.put("ZTYPE", "23");
		I_IMPORT.put("ZERNAM", "");
		I_IMPORT.put("ZIMENO", "");
		I_IMPORT.put("ZDATE", ZDATE);
		I_IMPORT.put("ZTIME", ZTIME);

		params.put("I_IMPORT", I_IMPORT);

		String date = UtilString.getShortStringForDate(new Date());
		if (!StringUtils.hasText(matCode)) {
			JSONArray I_ERSDAArr = new JSONArray();
			JSONObject I_ERSDA = new JSONObject();
			I_ERSDA.put("SIGN", "I");
			I_ERSDA.put("OPTION", "EQ");
			I_ERSDA.put("LOW", date);
			I_ERSDAArr.add(I_ERSDA);

			JSONArray I_LAEDAArr = new JSONArray();
			JSONObject I_LAEDA = new JSONObject();
			I_LAEDA.put("SIGN", "I");
			I_LAEDA.put("OPTION", "EQ");
			I_LAEDA.put("LOW", date);
			I_LAEDAArr.add(I_LAEDA);

			params.put("I_ERSDA", I_ERSDAArr);
			params.put("I_LAEDA", I_LAEDAArr);
		} else {
			JSONArray I_MATNRArr = new JSONArray();

			JSONObject I_MATNR = new JSONObject();
			I_MATNR.put("SIGN", "I");
			I_MATNR.put("OPTION", "EQ");
			I_MATNR.put("LOW", matCode);

			I_MATNRArr.add(I_MATNR);

			params.put("I_MATNR", I_MATNRArr);
		}

		JSONObject returnObj = new JSONObject();

		returnObj = UtilREST.executePostJSONTimeOut(Const.SAP_API_URL + "int_23?sap-client=" + Const.SAP_API_CLIENT,
				params, 30);

		List<DicMaterial> matList = new ArrayList<DicMaterial>();
		if ("S".equals(returnObj.get("RETURNVALUE"))) {
			setMaterilaListFromSAP(returnObj, matList);
		}

		return matList;
	}

}
