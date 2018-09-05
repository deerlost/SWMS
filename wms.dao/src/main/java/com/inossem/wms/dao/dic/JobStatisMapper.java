package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.User;

/**
 * 作业量统计
 * @author 666
 *
 */
public interface JobStatisMapper {

	
	List<Map<String, Object>> selectJobStatis(Map<String, Object> map);
	
	List<User> selectUserListByWhId(Integer whId);
}
