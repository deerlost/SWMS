package com.inossem.wms.auth;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import com.inossem.wms.dao.auth.UserMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.User;

public class CurrentUserArgumentResolver  implements WebArgumentResolver{
	@Resource  
    private  UserMapper userDao; 

	@Override
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
		if(methodParameter.getParameterType() != null
                && methodParameter.getParameterType().equals(CurrentUser.class)){
			// 判断controller方法参数有没有写当前用户，如果有，这里返回即可，通常我们从session里面取出来
            //HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            Object cu = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            CurrentUser cuser=(CurrentUser)cu;
            //Object currentUser = request.getSession().getAttribute("CURRENT_USER");
            //User user=userDao.selectByPrimaryKey("a123456");
            //CurrentUser cuser =new CurrentUser(user);
//            cuser.getLGORT().add("1000-0001");
//           cuser.getLGORT().add("2000-0001");
//            cuser.getLGORT().add("2000-0002");
//            cuser.getLGORT().add("2000-0004");
//            cuser.setBukrs("1000");
//            cuser.getLGORT().add("100R-0001");
//            cuser.getLGORT().add("100R-0003");
//            cuser.getLGORT().add("100R-S001");
//            //cuser.setBukrs("100R");
            return cuser;
        }
        return UNRESOLVED;
	}
	
}
