package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

public interface ITurnoverService {
	
	JSONArray selectInfoData(int boardId);
	
	List<Map<String, Object>> selectMatGroup(Map<String, Object> paramMap);
	
	JSONArray getReportDetail(String boardId, String ftyId, String locationId, String matGroupId, String timeFr, String timeTo)throws Exception;
	
	 List<Map<String, Object>>   downloadTurnover(Map<String, Object> param);

	 Map<String, Object> getOverview(String boardId, String corpId, String whId ,String date , int type) throws Exception;
	 
	 Map<String, Object> getAvgview(String boardId, String corpId, String whId ,String date , int type,int month) throws Exception;
	 
	 List<Map<String, Object>> getWhAndMatGroupView(String boardId, String corpId, String whId ,String date , int type, String matGroupId , boolean groupMat) throws Exception;
	 
	 Map<String ,Object> getMatView(String boardId, String corpId, String whId ,String date , int type,int matNum , String matGroupId ,int showType) throws Exception;
	 
}
