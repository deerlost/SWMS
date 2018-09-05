package com.inossem.wms.web.biz;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inossem.wms.service.biz.IDemoService;

import net.sf.json.JSONObject;

@Controller  
@RequestMapping("/demo")
public class DemoController {

	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
 	@Resource  
    private IDemoService demoService;  
	
	
	//通过body传递参数：http://127.0.0.1:8080/bizservice/demo/process
    //header: Content-Type:application/json;charset=utf-8
    //body: {"id":1,"loginName":"ccc"}
    @RequestMapping(value="/process",method=RequestMethod.POST, produces="application/json;charset=UTF-8")
    public @ResponseBody Object initProcess(@RequestBody   JSONObject obj){
    	logger.debug("Receive json : {}",obj.toString());
    	ObjectMapper mapper = new ObjectMapper();    
    	 Map<String, Object>vmap = new HashMap<String, Object>();  
    	String name=obj.get("name").toString();
    	String variables=obj.get("variables").toString();
    	try{  
            vmap = mapper.readValue(variables, new TypeReference<HashMap<String,Object>>(){});  
            System.out.println(vmap); 
            String pid=demoService.initProcess(name, vmap);
            logger.debug("Start  process : {}",pid);
           
        }catch(Exception e){  
            e.printStackTrace();  
        }
    	
        obj.put("flag", true);
        return obj;
  
    }
}
