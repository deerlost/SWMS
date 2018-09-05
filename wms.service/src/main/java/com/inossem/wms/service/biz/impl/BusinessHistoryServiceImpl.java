package com.inossem.wms.service.biz.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.inossem.wms.dao.biz.BizBusinessHistoryMapper;
import com.inossem.wms.dao.buf.BufUserLocationMatStockMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.vo.BizBusinessHistoryVo;
import com.inossem.wms.service.biz.IBusinessHistoryService;
import com.inossem.wms.util.UtilDateTime;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilString;

@Service
@Transactional
public class BusinessHistoryServiceImpl implements IBusinessHistoryService {

	@Autowired
	private BizBusinessHistoryMapper HistoryDao;

	@Autowired
	private BufUserLocationMatStockMapper BufUserLocationMatStockDao;

	@Override
	public List<BizBusinessHistoryVo> selectHistoryList(Map<String, Object> param) {
		return HistoryDao.selectHistoryListByDispatcherOnPaging(param);
	}

	@Override
	public List<Map<String, Object>> selectInAndOut(Map<String, Object> map) {

		String dateStart = UtilObject.getStringOrEmpty(map.get("createTimeStart"));
		String dateEnd = UtilObject.getStringOrEmpty(map.get("createTimeEnd"));
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		int refresh = UtilObject.getIntOrZero(map.get("refresh"));
		if (refresh == 1) {
			String userId = UtilObject.getStringOrEmpty(map.get("userId"));
			BufUserLocationMatStockDao.deleteByUserId(userId);

			if (StringUtils.hasText(dateStart) && StringUtils.hasText(dateEnd)
					&& UtilString.getDateForString(dateEnd).compareTo(UtilString.getDateForString(dateStart)) >= 0) {

				String date[] = dateStart.split("-");
				int year = UtilObject.getIntOrZero(date[0]);
				int month = UtilObject.getIntOrZero(date[1]);
				Calendar cal_1 = Calendar.getInstance();// 获取当前日期

				cal_1.set(year, month - 1, 1);
				// 月初
				String startDay = UtilString.getShortStringForDate(cal_1.getTime());
				map.put("createTime", startDay);
				// 期初库存
				BufUserLocationMatStockDao.insertStartQty(map);

				String endDate = UtilString
						.getShortStringForDate(UtilDateTime.getDate(UtilString.getDateForString(dateEnd), 1));

				map.put("timeFr", startDay);
				map.put("timeTo", endDate);

				// end_date_input_qty
				map.put("end_date_input_qty", true);
				BufUserLocationMatStockDao.insertInputQty(map);
				map.remove("end_date_input_qty");

				// end_date_output_qty
				map.put("end_date_output_qty", true);
				BufUserLocationMatStockDao.insertOutputQty(map);
				map.remove("end_date_output_qty");

				map.remove("timeFr");
				map.remove("timeTo");

				map.put("timeFr", dateStart);

				map.put("timeTo", endDate);

				// between_date_input_qty
				map.put("between_date_input_qty", true);
				BufUserLocationMatStockDao.insertInputQty(map);
				map.remove("between_date_input_qty");

				// between_date_output_qty
				map.put("between_date_output_qty", true);
				BufUserLocationMatStockDao.insertOutputQty(map);
				map.remove("between_date_output_qty");

			} else {
				throw new WMSException("请选择正确日期");
			}
		}
		returnList = BufUserLocationMatStockDao.selectByParamOnPaging(map);
		return returnList;
	}

}
