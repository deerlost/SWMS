package com.inossem.wms.service.intfc.sap;

import java.math.BigDecimal;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.inossem.wms.model.biz.ErpReturn;
import com.inossem.wms.model.sap.SapDeliveryOrderHead;
import com.inossem.wms.model.sap.SapDeliveryOrderItem;
import com.inossem.wms.service.intfc.ICwErpWs;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.wsdl.sap.getdo.DTCWWMSGETDOREQ;
import com.inossem.wms.wsdl.sap.getdo.DTCWWMSGETDORESP;
import com.inossem.wms.wsdl.sap.getdo.DTCWWMSGETDORESP.ETLIKP;
import com.inossem.wms.wsdl.sap.getdo.DTCWWMSGETDORESP.ETLIPS;
import com.inossem.wms.wsdl.sap.getdo.SICWWMSGETDOSOUTService;
import com.inossem.wms.wsdl.sap.input.DTCWWMSMATDOCPOSTREQ;
import com.inossem.wms.wsdl.sap.input.DTCWWMSMATDOCPOSTRESP;
import com.inossem.wms.wsdl.sap.input.DTCWWMSMATDOCPOSTRESP.ETLOG;
import com.inossem.wms.wsdl.sap.input.SICWWMSMATDOCPOSTSOUTService;
import com.inossem.wms.wsdl.sap.inputc.DTCWWMSCANCELMATDOCREQ;
import com.inossem.wms.wsdl.sap.inputc.DTCWWMSCANCELMATDOCRESP;
import com.inossem.wms.wsdl.sap.inputc.SICWWMSCANCELMATDOCSOUTService;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTREQ;
import com.inossem.wms.wsdl.sap.output.DTCWWMSDOPOSTRESP;
import com.inossem.wms.wsdl.sap.output.SICWWMSDOPOSTSOUTService;
import com.inossem.wms.wsdl.sap.outputc.DTCWWMSDOREVERSEREQ;
import com.inossem.wms.wsdl.sap.outputc.DTCWWMSDOREVERSERESP;
import com.inossem.wms.wsdl.sap.outputc.SICWWMSDOREVERSESOUTService;
import com.inossem.wms.wsdl.sap.util.SapAuthenticator;

import net.sf.json.JSONObject;

/**
 * SAP接口
 * 
 * @author 高海涛
 * @date 2018年2月7日
 */
@Service("cwErpWsImpl")
public class CwErpWsImpl implements ICwErpWs{
	
	private static final Logger logger = LoggerFactory.getLogger(CwErpWsImpl.class);
	/**
	 * 入库,转储 过账
	 */
	@Override
	public ErpReturn inputPost(DTCWWMSMATDOCPOSTREQ dtcwwmsmatdocpostreq)throws Exception{
		Authenticator.setDefault(new  SapAuthenticator()); 
		
		SICWWMSMATDOCPOSTSOUTService service = new SICWWMSMATDOCPOSTSOUTService();
		JSONObject sys = JSONObject.fromObject(dtcwwmsmatdocpostreq);
		UtilJSONConvert.deleteNull(sys);
		logger.info(sys.toString());
		DTCWWMSMATDOCPOSTRESP result = service.getHTTPPort().siCWWMSMATDOCPOSTSOUT(dtcwwmsmatdocpostreq);
		sys = JSONObject.fromObject(result);
		UtilJSONConvert.deleteNull(sys);
		logger.info(sys.toString());
		ErpReturn re = new ErpReturn();
		List<ETLOG> list =  result.getETLOG();
		re.setMatDocCode(list.get(0).getMATDOC());
		re.setMatDocItem(list.get(0).getMATDOCITEM());
		re.setDocyear(list.get(0).getDOCYEAR());
		re.setMessage(list.get(0).getMESSAGE());
		re.setType(list.get(0).getTYPE());
		re.setDocyear(list.get(0).getDOCYEAR());
		re.setMatDocItem(list.get(0).getDOCITEMNUM());
		return re;
	}
	
	/**
	 * 入库冲销
	 */
	@Override
	public ErpReturn inputWriteOff(DTCWWMSCANCELMATDOCREQ dtcwwmscancelmatdocreq)throws Exception{
		Authenticator.setDefault(new  SapAuthenticator()); 
		
		SICWWMSCANCELMATDOCSOUTService service = new SICWWMSCANCELMATDOCSOUTService();
		JSONObject sys = JSONObject.fromObject(dtcwwmscancelmatdocreq);
		UtilJSONConvert.deleteNull(sys);
		
		logger.info(sys.toString());
		DTCWWMSCANCELMATDOCRESP result = service.getHTTPPort().siCWWMSCANCELMATDOCSOUT(dtcwwmscancelmatdocreq);
		sys = JSONObject.fromObject(result);
		UtilJSONConvert.deleteNull(sys);
		logger.info(sys.toString());
		ErpReturn re = new ErpReturn();
		List<DTCWWMSCANCELMATDOCRESP.ETLOG> list =  result.getETLOG();
		String docyear = "";
		String docCode = "";
		for(DTCWWMSCANCELMATDOCRESP.ETLOG etd : list) {
			if(etd.getDOCYEAR()!=null) {
				docyear = etd.getDOCYEAR();
			}
			if(etd.getMATDOC()!=null) {
				docCode = etd.getMATDOC();
			}
		}
		re.setDocyear(docyear);
		re.setMatDocCode(docCode);
		re.setMessage(list.get(0).getMESSAGE());
		re.setType(list.get(0).getTYPE());
		
		return re;
	}
	
	/**
	 * 出库过账
	 */
	@Override
	public ErpReturn outputPost(DTCWWMSDOPOSTREQ mtCWWMSDOPOSTREQ)throws Exception{
		Authenticator.setDefault(new  SapAuthenticator()); 
		
		SICWWMSDOPOSTSOUTService service = new SICWWMSDOPOSTSOUTService();
		
		JSONObject sys = JSONObject.fromObject(mtCWWMSDOPOSTREQ);
		UtilJSONConvert.deleteNull(sys);
		
		logger.info(sys.toString());
		DTCWWMSDOPOSTRESP result =  service.getHTTPPort().siCWWMSDOPOSTSOUT(mtCWWMSDOPOSTREQ);
		
		sys = JSONObject.fromObject(result);
		UtilJSONConvert.deleteNull(sys);
		
		logger.info(sys.toString());
		
		ErpReturn re = new ErpReturn();
		List<DTCWWMSDOPOSTRESP.ETBAPIRET2> list =  result.getETBAPIRET2();
		re.setMatDocCode(list.get(0).getMBLNR());
		re.setMessage(list.get(0).getMESSAGE());
		re.setType(list.get(0).getTYPE());
		
		return re;
	}
	
	/**
	 * 出库冲销
	 */
	@Override
	public ErpReturn outputWriteOff(DTCWWMSDOREVERSEREQ mtCWWMSDOREVERSEREQ)throws Exception{
		Authenticator.setDefault(new  SapAuthenticator()); 
		
		SICWWMSDOREVERSESOUTService service = new SICWWMSDOREVERSESOUTService();
		JSONObject sys = JSONObject.fromObject(mtCWWMSDOREVERSEREQ);
		UtilJSONConvert.deleteNull(sys);
		
		logger.info(sys.toString());
		DTCWWMSDOREVERSERESP result = service.getHTTPPort().siCWWMSDOREVERSESOUT(mtCWWMSDOREVERSEREQ);
		sys = JSONObject.fromObject(result);
		UtilJSONConvert.deleteNull(sys);
		logger.info(sys.toString());
		ErpReturn re = new ErpReturn();
		List<DTCWWMSDOREVERSERESP.ETDATA> list =  result.getETDATA();
		String docyear = "";
		String docCode = "";
		for(DTCWWMSDOREVERSERESP.ETDATA etd : list) {
			if(etd.getDOCYEAR()!=null) {
				docyear = etd.getDOCYEAR();
			}
			if(etd.getMATDOC()!=null) {
				docCode = etd.getMATDOC();
			}
		}
		re.setDocyear(docyear);
		re.setMatDocCode(docCode);
		re.setMessage(list.get(0).getMESSAGE());
		re.setType(list.get(0).getTYPE());
		
		return re;
		
	}
	
	/**
	 * 获取交货单
	 *
	 */
	@Override
	public SapDeliveryOrderHead getDoOrder(String doCode)throws Exception{
		
		Authenticator.setDefault(new  SapAuthenticator()); 
		
		SICWWMSGETDOSOUTService service = new SICWWMSGETDOSOUTService();
		
		DTCWWMSGETDOREQ mtCWWMSGETDOREQ = new DTCWWMSGETDOREQ();
		
		mtCWWMSGETDOREQ.setIVVBELN(doCode);
		
		JSONObject sys = JSONObject.fromObject(mtCWWMSGETDOREQ);
		UtilJSONConvert.deleteNull(sys);
		
		logger.info(sys.toString());
		
		DTCWWMSGETDORESP result = service.getHTTPPort().siCWWMSGETDOSOUT(mtCWWMSGETDOREQ);
		
		sys = JSONObject.fromObject(result);
		UtilJSONConvert.deleteNull(sys);
		logger.info(sys.toString());
		//TODO 对结果判断
		
		ETLIKP etlikp = result.getETLIKP().get(0);
		List<ETLIPS> etlips = result.getETLIPS();
		SapDeliveryOrderHead head = new SapDeliveryOrderHead();
		head.setDeliveryOrderCode(etlikp.getVBELN());
		head.setSaleOrderCode(etlips.get(0).getVGBEL());
		head.setReceiveCode(etlikp.getKUNWE());
		head.setReceiveName(etlikp.getNAME1());
		head.setOrderType(etlikp.getLFART());
		head.setOrderTypeName(etlips.get(0).getBEZEI());
		head.setReferSaleOrderCode(etlips.get(0).getBSTNK());
		head.setOrgCode(etlikp.getVKORG());
		head.setContractNumber(etlips.get(0).getBSTNK());
		List<SapDeliveryOrderItem> itemList = new ArrayList<SapDeliveryOrderItem>();
		for(ETLIPS etlip:etlips){
			//排除掉交货数量不正确的
			if(new BigDecimal(etlip.getLFIMG()).compareTo(BigDecimal.ZERO)==1) {
				SapDeliveryOrderItem item = new SapDeliveryOrderItem();
				item.setDeliveryOrderRid(etlip.getPOSNR());
				item.setErpBatch(etlip.getCHARG());
				item.setFtyCode(etlip.getWERKS());
				item.setLocationCode(etlip.getLGORT());
				item.setMatCode(etlip.getMATNR());
				item.setQty(new BigDecimal(etlip.getLFIMG()).multiply(new BigDecimal(etlip.getUMVKZ())));
				item.setUnitCode(etlip.getMEINS());
				item.setErpRemark(etlip.getKDMAT());
				itemList.add(item);
			}
		}
		head.setItemList(itemList);
		return head;
	}
}
