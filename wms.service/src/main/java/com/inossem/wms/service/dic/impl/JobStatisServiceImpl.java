package com.inossem.wms.service.dic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicBoardMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.JobStatisMapper;
import com.inossem.wms.dao.dic.JobStatisMapperForPortal;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.dic.DicBoard;
import com.inossem.wms.service.dic.IJobStatisService;

@Transactional
@Service("jobStatisService")
public class JobStatisServiceImpl implements IJobStatisService {

	@Autowired
	private JobStatisMapper vsDao;
	
	@Autowired
	private JobStatisMapperForPortal vsDaoForPortable;
	
	@Autowired
	private DicBoardMapper dicBoardMapper;
	
	@Autowired
	private DicFactoryMapper dicFactoryMapper;
	
	@Override
	public List<Map<String, Object>> getJobStatis(Map<String, Object> map) throws Exception {
		
		return vsDao.selectJobStatis(map);
	}

	@Override
	public List<User> getUserListByWhId(Integer whId) throws Exception {
		return vsDao.selectUserListByWhId(whId);
	}
	
	
	@Override
	public List<Map<String,Object>> getBaseInfo(CurrentUser cUser) {	   
		
		List<DicBoard> boardList=dicBoardMapper.selectAllBoard();
		List<Map<String,Object>> resultMap=new ArrayList<Map<String,Object>>();
		for (int i=0;i<boardList.size();i++) {	
			
			Map<String,Object> param=new HashMap<String,Object>(); 
			param.put("boardId", boardList.get(i).getBoardId());
			param.put("userId", cUser.getUserId());
	     	List<Map<String,Object>> listMap=	dicFactoryMapper.selectFtyByBoardId(param);			
	     	param.put("fty_list", listMap);	     	
	     	param.put("board_id",  boardList.get(i).getBoardId());
	     	param.put("board_name",  boardList.get(i).getBoardName());
	     	resultMap.add(param);
		}		
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getJobStatisForPortable(Map<String, Object> map) {
		return vsDaoForPortable.selectJobStatis(map);
	}

}
