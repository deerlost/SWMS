package com.inossem.wms.web.biz;


import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller  
@RequestMapping("/demo")
public class DemoController {

	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
 
    @RequestMapping(value = "/auth/ping.action", method = RequestMethod.GET)
	public @ResponseBody String ping(HttpServletResponse response) {
		String msg="{\"code\":\"200\",\"msg\":\"\",\"data\":{}}";
		return msg; 
	}
}
