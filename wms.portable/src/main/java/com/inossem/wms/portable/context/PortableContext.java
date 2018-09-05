package com.inossem.wms.portable.context;

import javax.annotation.Resource;

import com.inossem.wms.dao.auth.UserMapper;
import com.inossem.wms.model.auth.User;

public class PortableContext {
	@Resource  
    private static UserMapper userDao; 
	
	public static User  getCurrentUser(){
			return  userDao.selectByPrimaryKey("a123456");
		
	}
}
