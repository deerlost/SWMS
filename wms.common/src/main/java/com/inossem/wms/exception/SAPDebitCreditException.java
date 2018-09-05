package com.inossem.wms.exception;

import com.inossem.wms.model.enums.EnumErrorCode;

public class SAPDebitCreditException extends WMSException {
	public SAPDebitCreditException() {
		super(EnumErrorCode.ERROR_CODE_DEBIT_CREDIT.getName());
		errorCode = EnumErrorCode.ERROR_CODE_DEBIT_CREDIT.getValue();
	}

	public SAPDebitCreditException(String msg) {
		super(msg);
		errorCode = EnumErrorCode.ERROR_CODE_DEBIT_CREDIT.getValue();
	}
}
