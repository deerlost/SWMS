package com.inossem.wms.auth;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.inossem.wms.dao.auth.UserMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.vo.RelUserStockLocationVo;

public class WmsUserDetailsService implements UserDetailsService {

	@Resource
	private UserMapper userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.selectCurrentUserByPrimaryKey(username);
		CurrentUser cuser = null;

		// 如果用户ID或密码错误则抛出403异常
		if (user == null || "".equals(user)) {
			return cuser;
		} else {
			cuser = new CurrentUser(user);
			List<Integer> role = userDao.selectRoleForUser(user.getUserId());
			// 如果当前用户ID没有查到对应角色则赋予默认角色0
			if (role.size() == 0) {
				role.add(0);
			}
			cuser.setRoleCode(role);
			List<String> locationCode = userDao.selectLocationForUser(user.getUserId());
			if (locationCode.size() == 0) {
				cuser.setLocationList(userDao.selectLocationForUser("default"));
			} else {
				cuser.setLocationList(locationCode);
			}
			List<RelUserStockLocationVo> relUserStockLocationVoList = userDao
					.selectInventoryLocationForUser(user.getUserId());
			if (relUserStockLocationVoList.size() == 0) {
				cuser.setInventoryLocation(userDao.selectInventoryLocationForUser("default"));
			} else {
				cuser.setInventoryLocation(relUserStockLocationVoList);
			}

		}
		return cuser;
	}

}
