package com.inossem.wms.exception;

import com.inossem.wms.model.enums.EnumErrorCode;

public class InterfaceCallException extends WMSException {

	// private final static String format = "[sap]%s";

	public InterfaceCallException() {
		// super(String.format(Const.SAP_API_MSG_PREFIX,
		// EnumErrorCode.ERROR_CODE_INTERFACE_CALL_FAILURE.getName()));
		super(EnumErrorCode.ERROR_CODE_INTERFACE_CALL_FAILURE.getName());
		errorCode = EnumErrorCode.ERROR_CODE_INTERFACE_CALL_FAILURE.getValue();
	}

	public InterfaceCallException(String msg) {
		// super(String.format(Const.SAP_API_MSG_PREFIX, msg));
		super(msg);
		errorCode = EnumErrorCode.ERROR_CODE_INTERFACE_CALL_FAILURE.getValue();
	}
}
