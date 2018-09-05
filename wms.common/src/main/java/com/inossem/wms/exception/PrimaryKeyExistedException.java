package com.inossem.wms.exception;

import com.inossem.wms.model.enums.EnumErrorCode;

public class PrimaryKeyExistedException extends WMSException {
	public PrimaryKeyExistedException() {
		super(EnumErrorCode.ERROR_CODE_PRIMARY_KEY_EXISTED.getName());
		errorCode = EnumErrorCode.ERROR_CODE_PRIMARY_KEY_EXISTED.getValue();
	}

	public PrimaryKeyExistedException(String msg) {
		super(msg);
		errorCode = EnumErrorCode.ERROR_CODE_PRIMARY_KEY_EXISTED.getValue();
	}
}
