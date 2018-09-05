package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicAccountPeriod;
import com.inossem.wms.model.vo.DicAccountPeriodVo;

import net.sf.json.JSONArray;

/**
 * 账期管理service
 * @author 666
 *
 */
public interface IAccountPeriodService {

	public abstract Map<String, Object> addAccountPeriods(List<DicAccountPeriod> apList) throws Exception;
	
	public abstract Map<String, Object> updateAccountPeriods(List<DicAccountPeriod> apList) throws Exception;

	public abstract void deleteAccountPeriod(Integer periodId) throws Exception;
	
	public abstract List<DicAccountPeriodVo> getAccountPeriods(Map<String, Object> map)throws Exception;

	public DicAccountPeriod validateAccountDate(List<DicAccountPeriod> apList)throws Exception;
	
	public DicAccountPeriod validateAccountMonth(List<DicAccountPeriod> apList)throws Exception;

	public JSONArray getAllBoardAndCorp() throws Exception;
	
	public void deleteByIds(List<Integer> ids) throws Exception;
	
}
