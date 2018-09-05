package com.inossem.wms.service.intfc.sap;

//import org.junit.Test;
import org.springframework.stereotype.Service;

import com.inossem.wms.service.intfc.ICwLimsWs;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.wsdl.lims.report.Service1;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
public class CwLimsWsImpl implements ICwLimsWs {

	//private static final Logger logger = LoggerFactory.getLogger(CwLimsWsImpl.class);

	@Override
	public JSONArray searchReport(String sampName,String batchName,String specification) throws Exception {
		//logger.info(batchName);
		Service1 service = new Service1();
		String result = service.getService1Soap().limsQADATA3(sampName, batchName, specification);
		JSONObject sys = JSONObject.fromObject(result);
		UtilJSONConvert.deleteNull(sys);
	//	logger.info(sys.toString());
		JSONArray list = sys.getJSONArray("results");
		System.out.println( list.toString());
		return list;
	}

//	@Test
//	public void test() {
//		try {
//			searchReport("聚乙烯醇材料", "180717", "JC-Ⅱ");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
}
