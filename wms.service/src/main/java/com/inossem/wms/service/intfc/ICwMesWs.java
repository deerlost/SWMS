package com.inossem.wms.service.intfc;

import com.inossem.wms.wsdl.mes.update.ArrayOfWmInvQueryNewResult;
import com.inossem.wms.wsdl.mes.update.ArrayOfWmMvRecInteropeDto;
import com.inossem.wms.wsdl.mes.update.WmIopRetVal;

public interface ICwMesWs {
	
	ArrayOfWmInvQueryNewResult searchMatStock(String matCode,String location_id) throws Exception;
	
	WmIopRetVal updateMesStock(String credentialCode,String opeTypeCode,String shiftBeginTime,String shiftEndTime,ArrayOfWmMvRecInteropeDto dtoSet) throws Exception;

	String CancelBookedBillByBillCode(String code) throws Exception;
}
