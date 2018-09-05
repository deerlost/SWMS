package com.inossem.wms.service.intfc;

import java.util.List;

import com.inossem.wms.model.dic.DicMaterial;

public interface IMaterial {

	/**
	 * 同步物料
	 * @param matCode
	 * @return
	 * @throws Exception
	 */
	List<DicMaterial> syncMaterial(String matCode) throws Exception;
	
}
