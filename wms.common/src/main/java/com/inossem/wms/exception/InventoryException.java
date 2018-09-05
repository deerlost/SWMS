package com.inossem.wms.exception;

import com.inossem.wms.model.enums.EnumErrorCode;

public class InventoryException extends WMSException {
	public InventoryException() {
		super(EnumErrorCode.ERROR_CODE_STOCK_EXCEPTION.getName());
		errorCode = EnumErrorCode.ERROR_CODE_STOCK_EXCEPTION.getValue();
	}

	public InventoryException(String msg) {
		super(msg);
		errorCode = EnumErrorCode.ERROR_CODE_STOCK_EXCEPTION.getValue();
	}
}
