package com.inossem.wms.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.UUID;

@Aspect
@Component
public class ControllerInterceptor {
	private static final Logger log = LoggerFactory.getLogger(ControllerInterceptor.class);
	private static ThreadLocal<Long> startTime = new ThreadLocal<Long>();
	private static ThreadLocal<String> key = new ThreadLocal<String>();
	private static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 定义拦截规则：拦截..*(..))包下面的所有类中，有@RequestMapping注解的方法
	 */
	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	// @Pointcut("execution(* com.inossem.wms.web.biz.*(..))")
	public void controllerMethodPointcut() {
	}

	/**
	 * 请求方法前打印内容
	 *
	 * @param joinPoint
	 * @throws UnsupportedEncodingException
	 */
	@Before("controllerMethodPointcut()")
	public void doBefore(JoinPoint joinPoint) throws IOException {
	//	System.out.println("=============================进入调用方法之后========================");
		// 请求开始时间
		startTime.set(System.currentTimeMillis());
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		// 如果有session则返回session如果没有则返回null(避免创建过多的session浪费内存)
		HttpSession session = request.getSession(false);
		// 获取请求头
		Enumeration<String> enumeration = request.getHeaderNames();
		StringBuffer headers = new StringBuffer();
		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement();
			String value = request.getHeader(name);
			headers.append(name + ":" + value).append(",");
		}
		String uri = UUID.randomUUID() + "|" + request.getRequestURI();

		String method = request.getMethod();
		StringBuffer params = new StringBuffer();
		if (HttpMethod.GET.toString().equals(method)) {// get请求
			String queryString = request.getQueryString();
			if (StringUtils.isNotBlank(queryString)) {
				params.append(URLEncoder.encode(queryString, "UTF-8"));
			}
		} else {// 其他请求
			Object[] paramsArray = joinPoint.getArgs();
			if (paramsArray != null && paramsArray.length > 0) {
				for (int i = 0; i < paramsArray.length; i++) {
					if (paramsArray[i] instanceof Serializable) {
						params.append(paramsArray[i].toString()).append(",");
					} else {
						// 使用json系列化 反射等等方法 反系列化会影响请求性能建议重写tostring方法实现系列化接口
						try {
							String param = objectMapper.writeValueAsString(paramsArray[i]);
							if (StringUtils.isNotBlank(param))
								params.append(param).append(",");
						} catch (JsonProcessingException e) {
							log.error("doBefore obj to json exception obj={},msg={}", paramsArray[i], e);
						}
					}
				}
			}
		}
		key.set(uri);
		log.info("========================request params>>>>>>uri={},method={},params={},headers={}==========", uri, method, params, headers);
	}

	/**
	 * 在方法执行后打印返回内容
	 *
	 * @param obj
	 */
	@AfterReturning(returning = "obj", pointcut = "controllerMethodPointcut()")
	public void doAfterReturing(Object obj) {
		//System.out.println("=============================进入调用方法之后========================");
		long costTime = System.currentTimeMillis() - startTime.get();
		String uri = key.get();
		startTime.remove();
		key.remove();
		String result = null;
		if (obj instanceof Serializable) {
			result = obj.toString();
		} else {
			if (obj != null) {
				try {
					result = objectMapper.writeValueAsString(obj);
				} catch (JsonProcessingException e) {
					log.error("doAfterReturing obj to json exception obj={},msg={}", obj, e);
				}
			}
		}
		log.info("========================response result<<<<<<uri={},result={},costTime={}ms", uri, result, costTime);
	}
}
