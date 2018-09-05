package com.inossem.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
* @author 高海涛 
* @version 2017年11月1日
* 配置文件
*/
public class PropertiesUtil {
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	
	private static final PropertiesUtil single = new PropertiesUtil();

	// 静态工厂方法
	public static PropertiesUtil getInstance() {
		return single;
	}
	
	private PropertiesUtil() {
		InputStream in = null;
		try {
			Properties pro = new Properties();

			in = this.getClass().getResourceAsStream("/config.properties");
			pro.load(in);

			this.path = pro.getProperty("path");
			this.info_url = pro.getProperty("info_url");
			this.separator=pro.getProperty("separator");
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}
	}
	
	private String separator;
	
	public String getSeparator() {
		return this.separator;
	}

	private String path;

	public String getPath() {
		return this.path;
	}
	
	public String info_url;

	public String getInfo_url() {
		return this.info_url;
	}
}
