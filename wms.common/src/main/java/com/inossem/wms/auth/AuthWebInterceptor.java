package com.inossem.wms.auth;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.inossem.wms.dao.auth.UserMapper;
import com.inossem.wms.model.auth.User;;

public class AuthWebInterceptor  extends HandlerInterceptorAdapter{
	
	@Resource  
    private  UserMapper userDao; 
	
	@Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
//		Class<?> a =IUserAware.class;
//		Class<?> b =handler.getClass();
//        if (a.isAssignableFrom(b)){
//	        //User user = (User)httpServletRequest.getSession().getAttribute("currentUser");
//        	User user=userDao.selectByPrimaryKey("a123456");
//	        IUserAware userAware = (IUserAware) handler;
//	        userAware.setUser(user);
//        }
        if(HandlerMethod.class.equals(handler.getClass())){  
            //获取controller，判断是不是实现登录接口的控制器  
            HandlerMethod method = (HandlerMethod) handler;  
            Object controller = method.getBean();  
              
            //判断是否为登录接口实现类  
            if(controller instanceof IUserAware){  
                //Object user = request.getSession().getAttribute("user");  
//            	User user=userDao.selectByPrimaryKey("a123456");
//    	        IUserAware userAware = (IUserAware) controller;
//    	        userAware.setUser(user);
            }  
        } 
        return super.preHandle(httpServletRequest, httpServletResponse, handler);
    }
}
