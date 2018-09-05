package com.inossem.wms.exception;

import com.inossem.wms.model.enums.EnumErrorCode;

public class WMSException extends RuntimeException {
	protected int errorCode;

	public WMSException() {
		super(EnumErrorCode.ERROR_CODE_EXCEPTION.getName());
		errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();

	}

	public WMSException(String msg) {
		super(msg);
		errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
	}

	public int getErrorCode() {
		return errorCode;
	}

}
