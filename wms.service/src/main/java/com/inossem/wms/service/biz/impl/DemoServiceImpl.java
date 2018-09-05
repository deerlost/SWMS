package com.inossem.wms.service.biz.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.inossem.wms.service.biz.IDemoService;

@Service("demoService")
public class DemoServiceImpl implements  IDemoService{
	
	/*@Autowired
    private RuntimeService runtimeService;*/

	@Override
	public String initProcess(String name, Map<String, Object> variables) {
		/*runtimeService.startProcessInstanceByKey("holidayRequest", variables);
		ProcessInstance processInstance =runtimeService.startProcessInstanceByKey("holidayRequest", variables);
		return processInstance.getId();*/
		return "";
	}
		
}
