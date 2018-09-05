package com.inossem.wms.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class UtilREST {
	private static final Logger logger = LoggerFactory.getLogger(UtilREST.class);

	public enum MethodType {
		POST, PUT, GET, DELETE
	}

	private static class UTF8PostMethod extends PostMethod {
		public UTF8PostMethod(String url) {
			super(url);
		}

		@Override
		public String getRequestCharSet() {
			return "UTF-8";
		}
	}

	private static class UTF8PutMethod extends PutMethod {
		public UTF8PutMethod(String url) {
			super(url);
		}

		@Override
		public String getRequestCharSet() {
			return "UTF-8";
		}
	}

	private static class UTF8GetMethod extends GetMethod {
		public UTF8GetMethod(String url) {
			super(url);
		}

		@Override
		public String getRequestCharSet() {
			return "UTF-8";
		}
	}

	private static class UTF8DeleteMethod extends DeleteMethod {
		public UTF8DeleteMethod(String url) {
			super(url);
		}

		@Override
		public String getRequestCharSet() {
			return "UTF-8";
		}
	}

	public static JSONObject executePostJSONTimeOut(String url, JSONObject params, int timeout) throws Exception {
		logger.info(String.format("post url:%s\nparams:%s\ntimeout:%d", url, params.toString(), timeout));
		PostMethod method = null;
		HttpClient client = null;
		try {
			method = new UTF8PostMethod(url);
			RequestEntity requestEntity = new StringRequestEntity(params.toString(), "application/json", "UTF-8");
			method.setRequestEntity(requestEntity);
			client = new HttpClient();

			if (timeout > 0) {
				// 设置超时的时间
				client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout * 1000);
			}
			client.executeMethod(method);
			String str = method.getResponseBodyAsString();
			logger.info(String.format("responese-str:%s", str));
			return JSONObject.fromObject(str);
		} catch (Exception e) {
			throw e;
		} finally {
			if (method != null) {
				method.releaseConnection();
				method = null;
			}
			if (client != null) {
				((SimpleHttpConnectionManager) (client.getHttpConnectionManager())).shutdown();
				client = null;
			}
		}
	}

	public static JSONObject executeGetJSONTimeOut(String url, int timeout) throws Exception {
		logger.info(String.format("sap-api:get url:%s\ntimeout:%d", url, timeout));
		GetMethod method = null;
		HttpClient client = null;
		try {
			method = new UTF8GetMethod(url);
			client = new HttpClient();
			if (timeout > 0) {
				client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout * 1000);
			}
			client.executeMethod(method);
			String str = method.getResponseBodyAsString();
			logger.info(String.format("sap-api-responese-str:%s", str));
			return JSONObject.fromObject(str);
			// return new JSONObject(str);
		} catch (Exception e) {
			throw e;
		} finally {
			if (method != null) {
				method.releaseConnection();
				method = null;
			}
			if (client != null) {
				((SimpleHttpConnectionManager) (client.getHttpConnectionManager())).shutdown();
				client = null;
			}
		}
	}

	public static JSONObject executePutJSONTimeOut(String url, String params, int timeout) throws Exception {
		logger.info(String.format("sap-api:put url:%s\nparams:%s\ntimeout:%d", url, params.toString(), timeout));
		PutMethod method = null;
		HttpClient client = null;
		try {
			method = new UTF8PutMethod(url);
			RequestEntity requestEntity = new StringRequestEntity(params, "application/json", "UTF-8");
			method.setRequestEntity(requestEntity);
			client = new HttpClient();
			if (timeout > 0) {
				// client.setTimeout(timeout * 1000);
				client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout * 1000);
			}
			client.executeMethod(method);
			String str = method.getResponseBodyAsString();
			logger.info(String.format("sap-api-responese-str:%s", str));
			return JSONObject.fromObject(str);
		} catch (Exception e) {
			throw e;
		} finally {
			if (method != null) {
				method.releaseConnection();
				method = null;
			}
			if (client != null) {
				((SimpleHttpConnectionManager) (client.getHttpConnectionManager())).shutdown();
				client = null;
			}
		}
	}

	public static JSONObject executeDeleteJSONTimeOut(String url, int timeout) throws Exception {
		logger.info(String.format("sap-api:delete url:%s\ntimeout:%d", url, timeout));
		DeleteMethod method = null;
		HttpClient client = null;
		try {
			method = new UTF8DeleteMethod(url);
			client = new HttpClient();
			if (timeout > 0) {
				client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout * 1000);
			}
			client.executeMethod(method);
			String str = method.getResponseBodyAsString();
			logger.info(String.format("sap-api-responese-str:%s", str));
			return JSONObject.fromObject(str);
		} catch (Exception e) {
			throw e;
		} finally {
			if (method != null) {
				method.releaseConnection();
				method = null;
			}
			if (client != null) {
				((SimpleHttpConnectionManager) (client.getHttpConnectionManager())).shutdown();
				client = null;
			}
		}
	}

	public static String executePostStringTimeOut(String url, JSONObject params, int timeout) throws Exception {
		logger.info(String.format("sap-api:post url:%s\nparams:%s\ntimeout:%d", url, params.toString(), timeout));
		PostMethod method = null;
		HttpClient client = null;
		try {
			method = new UTF8PostMethod(url);
			RequestEntity requestEntity = new StringRequestEntity(params.toString(), "application/json", "UTF-8");
			method.setRequestEntity(requestEntity);
			client = new HttpClient();

			if (timeout > 0) {
				// 设置超时的时间
				client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout * 1000);
			}
			client.executeMethod(method);
			String str = method.getResponseBodyAsString();
			logger.info(String.format("sap-api-responese-str:%s", str));
			return str;
		} catch (Exception e) {
			throw e;
		} finally {
			if (method != null) {
				method.releaseConnection();
				method = null;
			}
			if (client != null) {
				((SimpleHttpConnectionManager) (client.getHttpConnectionManager())).shutdown();
				client = null;
			}
		}
	}

	public static String executeGetStringTimeOut(String url, int timeout) throws Exception {
		logger.info(String.format("sap-api:get url:%s\ntimeout:%d", url, timeout));
		GetMethod method = null;
		HttpClient client = null;
		try {
			method = new UTF8GetMethod(url);
			client = new HttpClient();
			if (timeout > 0) {
				client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout * 1000);
			}
			client.executeMethod(method);
			String str = method.getResponseBodyAsString();
			logger.info(String.format("sap-api-responese-str:%s", str));
			return str;
			// return new JSONObject(str);
		} catch (Exception e) {
			throw e;
		} finally {
			if (method != null) {
				method.releaseConnection();
				method = null;
			}
			if (client != null) {
				((SimpleHttpConnectionManager) (client.getHttpConnectionManager())).shutdown();
				client = null;
			}
		}
	}

	public static String executePutStringTimeOut(String url, String params, int timeout) throws Exception {
		logger.info(String.format("sap-api:put url:%s\nparams:%s\ntimeout:%d", url, params.toString(), timeout));
		PutMethod method = null;
		HttpClient client = null;
		try {
			method = new UTF8PutMethod(url);
			RequestEntity requestEntity = new StringRequestEntity(params, "application/json", "UTF-8");
			method.setRequestEntity(requestEntity);
			client = new HttpClient();
			if (timeout > 0) {
				// client.setTimeout(timeout * 1000);
				client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout * 1000);
			}
			client.executeMethod(method);
			String str = method.getResponseBodyAsString();
			logger.info(String.format("sap-api-responese-str:%s", str));
			return str;
		} catch (Exception e) {
			throw e;
		} finally {
			if (method != null) {
				method.releaseConnection();
				method = null;
			}
			if (client != null) {
				((SimpleHttpConnectionManager) (client.getHttpConnectionManager())).shutdown();
				client = null;
			}
		}
	}

	public static String executeDeleteStringTimeOut(String url, int timeout) throws Exception {
		logger.info(String.format("sap-api:delete url:%s\ntimeout:%d", url, timeout));
		DeleteMethod method = null;
		HttpClient client = null;
		try {
			method = new UTF8DeleteMethod(url);
			client = new HttpClient();
			if (timeout > 0) {
				client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout * 1000);
			}
			client.executeMethod(method);
			String str = method.getResponseBodyAsString();
			logger.info(String.format("sap-api-responese-str:%s", str));
			return str;
		} catch (Exception e) {
			throw e;
		} finally {
			if (method != null) {
				method.releaseConnection();
				method = null;
			}
			if (client != null) {
				((SimpleHttpConnectionManager) (client.getHttpConnectionManager())).shutdown();
				client = null;
			}
		}
	}

}
