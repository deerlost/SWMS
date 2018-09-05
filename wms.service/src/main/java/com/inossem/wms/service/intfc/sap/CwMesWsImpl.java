package com.inossem.wms.service.intfc.sap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.inossem.wms.service.intfc.ICwMesWs;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.wsdl.mes.update.ArrayOfString;
import com.inossem.wms.wsdl.mes.update.ArrayOfWmInvQueryNewResult;
import com.inossem.wms.wsdl.mes.update.ArrayOfWmMvRecInteropeDto;
import com.inossem.wms.wsdl.mes.update.WmIopRetVal;
import com.inossem.wms.wsdl.mes.update.WmSvcInterOpe;

import net.sf.json.JSONObject;
@Service
public class CwMesWsImpl implements ICwMesWs {
	
	private static final Logger logger = LoggerFactory.getLogger(CwMesWsImpl.class);

	@Override
	public String CancelBookedBillByBillCode(String code) throws Exception {
		WmSvcInterOpe service = new WmSvcInterOpe();
		ArrayOfString result = service.getWmSvcInterOpeSoap().cancelBookedBillByBillCode(UtilProperties.getInstance().getMes_user(),code);
		if(result!=null) {
			return result.getString().get(0);
		}else {
			return null;
		}
	}
	
	@Override
	public ArrayOfWmInvQueryNewResult searchMatStock(String matCode,String location_id) throws Exception {
		WmSvcInterOpe service = new WmSvcInterOpe();
		
		GregorianCalendar gcal =new GregorianCalendar();
		gcal.setTime(new Date());
		ArrayOfWmInvQueryNewResult result = service.getWmSvcInterOpeSoap().getCurrentStockInfosByMtrlOrlocalId("",matCode,location_id);
		
		return result;
	}

	@Override
	public WmIopRetVal updateMesStock(String credentialCode,String opeTypeCode,String shiftBeginTime,String shiftEndTime,ArrayOfWmMvRecInteropeDto dtoSet) throws Exception {
		WmSvcInterOpe service = new WmSvcInterOpe();
		
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd"); 
		String ymd = sdf.format(new Date());
		
		sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		GregorianCalendar gcal =new GregorianCalendar();
		gcal.setTime(sdf.parse(ymd + " " +shiftBeginTime));
		XMLGregorianCalendar begin= DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		gcal.setTime(sdf.parse(ymd + " " +shiftEndTime));
		XMLGregorianCalendar end= DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		JSONObject sys = JSONObject.fromObject(dtoSet);
		UtilJSONConvert.deleteNull(sys);
		
		logger.info(sys.toString());
		WmIopRetVal result = service.getWmSvcInterOpeSoap().createOpeBillAndBook(UtilProperties.getInstance().getMes_user(), 
																			     UtilProperties.getInstance().getMes_pass(), 
																			     credentialCode, 
																			     opeTypeCode, 
																			     begin, 
																			     end, 
																			     dtoSet);
		sys = JSONObject.fromObject(result);
		UtilJSONConvert.deleteNull(sys);
		
		logger.info(sys.toString());
		
		return result;
//		return null;
	}

}
