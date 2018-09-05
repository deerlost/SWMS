package com.inossem.wms.exception;

import com.inossem.wms.model.enums.EnumErrorCode;

public class MaterialNotExistException extends WMSException {

	public MaterialNotExistException() {
		super(EnumErrorCode.ERROR_CODE_MATERIAL_NOT_EXIST.getName());
		errorCode = EnumErrorCode.ERROR_CODE_MATERIAL_NOT_EXIST.getValue();
	}
	
	public MaterialNotExistException(String msg) {
		super(msg);
		errorCode = EnumErrorCode.ERROR_CODE_MATERIAL_NOT_EXIST.getValue();
	}

}
