package com.inossem.wms.service.intfc;

import com.inossem.wms.model.biz.ErpReturn;
import com.inossem.wms.model.sap.SapDeliveryOrderHead;
import com.inossem.wms.wsdl.sap.input.DTCWWMSMATDOCPOSTREQ;
import com.inossem.wms.wsdl.sap.inputc.DTCWWMSCANCELMATDOCREQ;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTREQ;
import com.inossem.wms.wsdl.sap.outputc.DTCWWMSDOREVERSEREQ;

public interface ICwErpWs {

	ErpReturn inputPost(DTCWWMSMATDOCPOSTREQ DTCWWMSMATDOCPOSTREQ)throws Exception;
	
	ErpReturn inputWriteOff(DTCWWMSCANCELMATDOCREQ dtcwwmscancelmatdocreq)throws Exception;
	
	ErpReturn outputPost(DTCWWMSDOPOSTREQ mtCWWMSDOPOSTREQ)throws Exception;
	
	ErpReturn outputWriteOff(DTCWWMSDOREVERSEREQ mtCWWMSDOREVERSERE)throws Exception;

	SapDeliveryOrderHead getDoOrder(String doCode)throws Exception;
}
