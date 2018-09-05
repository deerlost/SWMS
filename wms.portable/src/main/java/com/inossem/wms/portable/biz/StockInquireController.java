package com.inossem.wms.portable.biz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.util.UtilJSONConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.biz.IStockInquireService;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONObject;

import javax.annotation.Resource;

/**
 * @Package com.inossem.wms.web.biz
 * @ClassName：StockInquireController
 * @Description : 查询
 * @anthor：王洋
 * @date：2018年6月11日 上午10:57:35 @版本：V1.0
 */
@Controller
public class StockInquireController {

    private final static Logger logger = LoggerFactory.getLogger(StockInquireController.class);

    @Resource
    private IStockInquireService stockInquireSerivce;

    /**
     * @Description: TODO(库存统计报表portable)</   br>
     * @param @return 设定文件</br>
     * @return JSONObject 返回类型</br>
     * @Title: queryLocPrice </br>
     * @throws</br>
     */
    @RequestMapping(value = "/biz/stquery/query_loc_price", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    JSONObject queryLocPricePor(@RequestBody JSONObject json) {
        int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
        boolean status = true;
        String msg ="";
        Map<String, Object> body = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();

        SimpleDateFormat sdate = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        //统计年度月份
        params.put("beginDate", String.format("%s-%s", sdate.format(date), "01"));
        params.put("endDate", String.format("%s-%s", sdate.format(date), "31"));
       /* if (json.containsKey("year")) {
            if (null != json.get("year") && !json.get("year").equals("")) {
                params.put("beginDate", String.format("%s-%s", json.getString("year"), "01-01"));



            } else {
            }
        }*/
        //查询全国的库存情况
        if (json.containsKey("countries")) {
        }
        //查询某个城市的库存情况
        if (json.containsKey("city_id")) {
            if (null == json.get("city_id") || json.get("city_id").equals("")) {
                params.put("cityId", "");
            } else {
                params.put("cityId", json.getInt("city_id"));
            }
        }
        if (json.containsKey("board_id")) {
            if (null == json.get("board_id") || json.get("board_id").equals("")) {
            } else {
                params.put("boardId", json.getInt("board_id"));
            }
        }
        if (json.containsKey("corp_id")) {
            if (null == json.get("corp_id") || json.get("corp_id").equals("")) {
            } else {
                params.put("corpId", json.getInt("corp_id"));
            }
        }
        if (json.containsKey("wh_id")) {
            if (null == json.get("wh_id") || json.get("wh_id").equals("")) {
            } else {
                params.put("whId", json.getInt("wh_id"));
            }
        }
        if (json.containsKey("type")) {
            if (null == json.get("type") || json.get("type").equals("")) {
                params.put("type", "order by wh_price desc");
            } else {
                params.put("type", String.format("order by %s desc", json.getString("type")));
            }
        } else {
            params.put("type", "order by wh_price desc");
        }
        params.put("pda", "PAD");
        try {
            body = stockInquireSerivce.queryLocPrice(params);
        }catch (WMSException e) {
            error_code = e.getErrorCode();
            msg = e.getMessage();
            status = false;
        } catch (Exception e) {
            error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
            status = false;
            logger.error("--------------=============-库存结构分析查询失败==============", e);
        }

        return UtilResult.getResultToJson(status, error_code,msg,body);
    }


    @RequestMapping(value = "/biz/stquery/get_base_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    JSONObject queryBaseInfo(CurrentUser cUser) {
        int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
        boolean status = true;

        JSONObject body = new JSONObject();

        try {
            body = stockInquireSerivce.queryBaseInfo(cUser);
        } catch (Exception e) {
            logger.error("============", e);
            error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
            status = false;
        }

        JSONObject  obj = UtilResult.getResultToJson(status, error_code, body);
        UtilJSONConvert.setNullToEmpty(obj);
        return  obj;
    }

}
