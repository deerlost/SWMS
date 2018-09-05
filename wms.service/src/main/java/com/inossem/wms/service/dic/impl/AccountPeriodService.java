package com.inossem.wms.service.dic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicAccountPeriodMapper;
import com.inossem.wms.dao.dic.DicCorpMapper;
import com.inossem.wms.model.dic.DicAccountPeriod;
import com.inossem.wms.model.dic.DicCorp;
import com.inossem.wms.model.enums.EnumBoard;
import com.inossem.wms.model.vo.DicAccountPeriodVo;
import com.inossem.wms.service.dic.IAccountPeriodService;
import com.inossem.wms.util.UtilObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service
public class AccountPeriodService implements IAccountPeriodService {

	@Autowired
	private DicAccountPeriodMapper accountPeriodDao;
	
	@Autowired
	private DicCorpMapper corpDao;
	
	@Override
	public Map<String, Object> addAccountPeriods(List<DicAccountPeriod> apList) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		accountPeriodDao.insertAccountPeriods(apList);
		return result;
	}

	@Override
	public Map<String, Object> updateAccountPeriods(List<DicAccountPeriod> apList) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		accountPeriodDao.updateAccountPeriods(apList);
		return result;
	}

	@Override
	public void deleteAccountPeriod(Integer periodId) throws Exception {
		DicAccountPeriod updateAccountPeriod = new DicAccountPeriod();
		updateAccountPeriod.setPeriodId(periodId);
		updateAccountPeriod.setIsDelete((byte)1);
		accountPeriodDao.updateByPrimaryKeySelective(updateAccountPeriod);
	}

	@Override
	public List<DicAccountPeriodVo> getAccountPeriods(Map<String, Object> map) throws Exception {
		return accountPeriodDao.selectByConditionOnPaging(map);
	}

	@Override
	public DicAccountPeriod validateAccountDate(List<DicAccountPeriod> apList) throws Exception {
		int count = 0;
		DicAccountPeriod apReturn = null;
		for(DicAccountPeriod ap:apList){
			HashMap<String, Object> map = new HashMap<String, Object>();
			if(ap.getPeriodId()!=null){
				map.put("periodId", ap.getPeriodId());
			}
			map.put("corpId", ap.getCorpId());
			map.put("boardId", ap.getBoardId());
			map.put("accountYear", ap.getAccountYear());
			
			map.put("accountBeginDate", ap.getAccountBeginDate());
			map.put("accountEndDate", ap.getAccountEndDate());
			count = accountPeriodDao.countAccountPeriods(map);
			if(count>0){
				apReturn = ap;
			}
		}
		
		return apReturn;
	}

	@Override
	public DicAccountPeriod validateAccountMonth(List<DicAccountPeriod> apList) throws Exception {
		int count = 0;
		DicAccountPeriod apReturn = null;
		for(DicAccountPeriod ap:apList){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("corpId", ap.getCorpId());
			map.put("boardId", ap.getBoardId());
			map.put("accountYear", ap.getAccountYear());
			map.put("accountMonth", ap.getAccountMonth());
			count = accountPeriodDao.countAccountPeriods(map);
			if(count>0){
				apReturn = ap;
			}
		}
		
		return apReturn;
	}

	@Override
	public JSONArray getAllBoardAndCorp() throws Exception {
		List<DicCorp> corpList = null; 
				//corpDao.selectAllCorpList();
		List<Map<String, Object>> boardList = EnumBoard.toBoardList();
		JSONArray body = new JSONArray();
		
		if(boardList!=null){
			for(Map<String, Object> map:boardList){
				JSONObject boardJson = new JSONObject();
				Integer boardId = UtilObject.getIntegerOrNull(map.get("boardId"));
				boardJson.put("boardId", boardId);
				boardJson.put("boardName", map.get("boardName"));
				List<DicCorp> innerCorpList = new ArrayList<DicCorp>();
				if(corpList!=null){
					for(DicCorp dCorp:corpList){
						if(dCorp.getBoardId().equals(boardId)){
							innerCorpList.add(dCorp);
						}
					}
				}
				boardJson.put("corpList", innerCorpList);
				body.add(boardJson);
			}
		}
		return body;
	}

	@Override
	public void deleteByIds(List<Integer> ids) throws Exception {
		
		accountPeriodDao.deleteByIds(ids);
	}

}
