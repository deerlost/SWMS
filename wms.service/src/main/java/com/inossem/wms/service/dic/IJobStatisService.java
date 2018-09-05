package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.User;

/**
 * 作业量统计
 * @author 666
 *
 */
public interface IJobStatisService {

	
	List<Map<String, Object>> getJobStatis(Map<String, Object> map) throws Exception;

	List<User> getUserListByWhId(Integer whId) throws Exception;
	public List<Map<String,Object>> getBaseInfo(CurrentUser cUser);

	List<Map<String, Object>> getJobStatisForPortable(Map<String, Object> map);
}
