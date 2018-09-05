package com.inossem.wms.exception;

import com.inossem.wms.model.enums.EnumErrorCode;

public class ExcelCellTypeException extends WMSException {

	private static final long serialVersionUID = 1L;

	public ExcelCellTypeException() {
		super(EnumErrorCode.ERROR_CODE_EXCEL_CELL_TYPE.getName());
		errorCode = EnumErrorCode.ERROR_CODE_EXCEL_CELL_TYPE.getValue();
	}

	public ExcelCellTypeException(String msg) {
		super(msg);
		errorCode = EnumErrorCode.ERROR_CODE_EXCEL_CELL_TYPE.getValue();
	}
}
