package com.inossem.wms.annotation;

import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target; 


/** 
* ClassName: AuditLog<br/> 
* Function: AOP日志记录，自定义注解 <br/> 
*/  
@Target({ElementType.PARAMETER, ElementType.METHOD})      
@Retention(RetentionPolicy.RUNTIME)      
@Documented
public  @interface AuditLog {
	/** 
	* 日志描述 
	*/  
	String op()  default "";
}
