package com.inossem.wms.portable.auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.auth.RoleService;
import com.inossem.wms.dao.auth.UserMapper;
import com.inossem.wms.dao.auth.VersionMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.MenuADK;
import com.inossem.wms.model.auth.Resources;
import com.inossem.wms.model.auth.Version;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 手持端基础信息获取
 * 
 * @author 高海涛
 * @date 2018年2月6日
 */
@Controller
@RequestMapping("/auth")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Resource
	private UserMapper userDao;
	@Resource
	private VersionMapper versionDao;
	@Resource
	private RoleService roleService;
	@Resource
	private IUserService userService;
	@Resource
	private ICommonService commonService;
	
	
	/**
	 * 获取菜单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/function", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getMenu(HttpServletRequest request) {
		String authToken = request.getHeader("X-Auth-Token");
		String[] parts = authToken.split(":");
		List<Integer> role = userDao.selectRoleForUser(parts[0]);
		// 获取资源
		ArrayList<Resources> resources = roleService.getResources(role);
		ArrayList<MenuADK> ma = this.userService.settingADKMenu(resources);

		JSONObject body = new JSONObject();
		body.put("function_list", JSONArray.fromObject(ma));

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), 1, 1, 1, body);
	}

	/**
	 * 获取MD5值
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String getMd5ByFile(File file) throws FileNotFoundException {
		String value = null;
		FileInputStream in = new FileInputStream(file);
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}
		return value;
	}

	/**
	 * 校验版本
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getVersions", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getVersions(HttpServletRequest request) {
		int versionCd = Integer.parseInt(request.getParameter("versionCode"));
		Version version = versionDao.selectForMaxVersion();

		if (version.getVersionCode() > versionCd) {
			JSONObject body = new JSONObject();

			JSONObject dataJson = new JSONObject();
			dataJson.put("versionCode", version.getVersionCode().toString());
			dataJson.put("versionName", version.getVersionName());
			dataJson.put("versionDescription", version.getDescription());
			dataJson.put("md5", version.getMd5());
			String url = UtilProperties.getInstance().getAdk_down_url() + version.getUrl() + version.getFileName();
			dataJson.put("url", url);
			dataJson.put("focus", "1");
			dataJson.put("size", version.getSize() == null ? "0" : version.getSize());

			body.put("version", dataJson);

			return UtilResult.getResultToMap(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), 1, 1, 1, body);
		} else {
			JSONObject body = new JSONObject();
			body.put("version", new JSONObject());

			return UtilResult.getResultToMap(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(), 1, 1, 1, body);
		}
	}

	// 显示当前登录用户同板块的库存地点
	@RequestMapping(value = "/get_fty_location_for_board", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getFtyLocationForBoard(CurrentUser cUser,String fty_id,String receipt_type) {
		JSONArray ftyAry = new JSONArray();
		try {
			ftyAry = commonService.listFtyLocationForBoardId(cUser.getBoardId());

			if(EnumReceiptType.STOCK_TRANSPORT_MATERIAL.getValue() == Byte.valueOf(receipt_type) ||
					   EnumReceiptType.STOCK_TRANSPORT_STOCK.getValue()==Byte.valueOf(receipt_type)){
				if(!"".equals(fty_id)&& fty_id!=null){
					for(int i=0;i< ftyAry.size();i++ ){
						JSONObject ftyJson =  ftyAry.getJSONObject(i);
						if(fty_id.equals(UtilObject.getStringOrEmpty(ftyJson.get("fty_id")))){
							JSONArray ftyAry2 = new JSONArray();
							ftyAry2.add(ftyJson);
							return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
									EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), ftyAry2);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("显示当前登录用户同板块的库存地点", e);
		}

		return UtilResult.getResultToJson(true, EnumErrorCode.ERROR_CODE_SUCESS.getValue(),
				EnumPage.PAGE_INDEX.getValue(), EnumPage.PAGE_SIZE.getValue(), EnumPage.TOTAL_COUNT.getValue(), ftyAry);
	}
	
	@RequestMapping(value = "/get_printer_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object get_printer_list(CurrentUser cUser) {

		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		Map<String,Object> reuslt = new HashMap<String,Object>();
		try {
			reuslt = userService.getPrinterList(null);
		} catch (Exception e) {
			logger.error("查询打印机列表", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		JSONObject obj = UtilResult.getResultToJson(status, errorCode, reuslt);
		return obj;
	}
	
}
