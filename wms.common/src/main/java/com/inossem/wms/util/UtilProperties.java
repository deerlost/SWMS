package com.inossem.wms.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inossem.wms.config.BatchMaterialConfig;

public class UtilProperties {
	private static Logger logger = LoggerFactory.getLogger(UtilProperties.class);
	
	private String mes_user;
	
	private String mes_pass;
	
	public String getMes_user() {
		return mes_user;
	}

	public String getMes_pass() {
		return mes_pass;
	}

	private String mes_url;
	
	public String getMes_url() {
		return mes_url;
	}


	// 文件系统地址
	private String file_server_url;

	public String getFileServerUrl() {
		return file_server_url;
	}

	private String stock_requisition_approve_user;

	public String getStockRequisitionApproveUser() {
		return stock_requisition_approve_user;
	}

	// wms的地址
	private String wms_url;

	public String getWmsUrl() {
		return wms_url;
	}

	// 安卓版本更新地址
	private String adk_down_url;

	public String getAdk_down_url() {
		return adk_down_url;
	}

	// 推送OA代办接口地址
	private String oa_wsdl_url;

	public String getOa_wsdl_url() {
		return oa_wsdl_url;
	}

	private String default_ccq;

	public String getDefaultCCQ() {
		return default_ccq;
	}

	private String default_cw;

	public String getDefaultCW() {
		return default_cw;
	}
	
	private String default_ccq_product;

	public String getDefaultCCQProduct() {
		return default_ccq_product;
	}

	private String default_cw_product;

	public String getDefaultCWProduct() {
		return default_cw_product;
	}
	

	private String printUrl;

	public String getPrintUrl() {
		return printUrl;
	}

	private String sapApiUrl;

	public String getSapApiUrl() {
		return sapApiUrl;
	}

	private String sapUser;
	
	private String sapPass;
	
	public String getSapUser() {
		return sapUser;
	}

	public String getSapPass() {
		return sapPass;
	}
	
	
	private String infoUrl;
	
	public String getInfoUrl() {
		return infoUrl;
	}
	// public String getBatchMaterialProductionTime() {
	// return batchMaterialProductionTime;
	// }
	//
	// public String getBatchMaterialValidityTime() {
	// return batchMaterialValidityTime;
	// }
	//
	// public String getBatchMaterialContractCode() {
	// return batchMaterialContractCode;
	// }
	//
	// public String getBatchMaterialContractName() {
	// return batchMaterialContractName;
	// }
	//
	// public String getBatchMaterialPurchaseOrderCode() {
	// return batchMaterialPurchaseOrderCode;
	// }
	//
	// public String getBatchMaterialPurchaseOrderRow() {
	// return batchMaterialPurchaseOrderRow;
	// }
	//
	// public String getBatchMaterialDemandDept() {
	// return batchMaterialDemandDept;
	// }


	private String sapApiClient;

	public String getSapApiClient() {
		return sapApiClient;
	}

	private List<BatchMaterialConfig> batchMaterialConfigList;

	// private String batchMaterialProductionTime;
	// private String batchMaterialValidityTime;
	// private String batchMaterialContractCode;
	// private String batchMaterialContractName;
	// private String batchMaterialPurchaseOrderCode;
	// private String batchMaterialPurchaseOrderRow;
	// private String batchMaterialDemandDept;

	private String oracleDriver;
	private String oracleUrl;
	private String oracleUserName;
	private String oraclePassword;

	public String getOracleDriver() {
		return oracleDriver;
	}

	public String getOracleUrl() {
		return oracleUrl;
	}

	public String getOracleUserName() {
		return oracleUserName;
	}

	public String getOraclePassword() {
		return oraclePassword;
	}

	private UtilProperties() {
		InputStream in = null;
		try {
			Properties pro = new Properties();

			in = this.getClass().getResourceAsStream("/wms_config.properties");
			pro.load(in);

			// 默认存储区
			this.default_ccq = pro.getProperty("defaultccq");
			// 默认仓位
			this.default_cw = pro.getProperty("defaultcw");
			
			this.default_ccq_product = pro.getProperty("defaultccqProduct");
			
			this.default_cw_product = pro.getProperty("defaultcwProduct");
			
			// 打印服务器地址
			this.printUrl = pro.getProperty("print_url");
			// sap接口的url,调用接口时使用
			this.sapApiUrl = pro.getProperty("sap_api_url");
			// sap接口的client,调用接口时使用
			this.sapApiClient = pro.getProperty("sap_api_client");

			this.adk_down_url = pro.getProperty("adk_down_url");
			// oa系统地址,2017-12-15仅在领料单旧审批人删除新审批人添加时,推送到oa系统通知
			this.oa_wsdl_url = pro.getProperty("oa_wsdl_url");
			// wms的web服务地址
			this.wms_url = pro.getProperty("wms_url");
			
			this.mes_url = pro.getProperty("mes_url");
			// 文件系统url
			this.file_server_url = pro.getProperty("file_server_url");
			// 领料单默认审批人,如果没有设置,默认admin
			this.stock_requisition_approve_user = pro.getProperty("stock_requisition_approve_user");

			this.mes_pass = pro.getProperty("mes_pass");
			this.mes_user = pro.getProperty("mes_user");
			
			this.sapUser = pro.getProperty("sap_user");
			this.sapPass = pro.getProperty("sap_pass");
			
			this.oracleDriver = pro.getProperty("oracle_driver");
			this.oraclePassword = pro.getProperty("oracle_password");
			this.oracleUrl = pro.getProperty("oracle_url");
			this.oracleUserName = pro.getProperty("oracle_user_name");
			this.infoUrl = pro.getProperty("info_url");
			String columnName = "production_time";
			this.addBatchMaterialConfig(pro.getProperty(columnName), columnName);
			columnName = "validity_time";
			this.addBatchMaterialConfig(pro.getProperty(columnName), columnName);
			columnName = "contract_code";
			this.addBatchMaterialConfig(pro.getProperty(columnName), columnName);
			columnName = "contract_name";
			this.addBatchMaterialConfig(pro.getProperty(columnName), columnName);
			columnName = "purchase_order_code";
			this.addBatchMaterialConfig(pro.getProperty(columnName), columnName);
			columnName = "purchase_order_rid";
			this.addBatchMaterialConfig(pro.getProperty(columnName), columnName);
			columnName = "demand_dept";
			this.addBatchMaterialConfig(pro.getProperty(columnName), columnName);

			if (UtilString.isNullOrEmpty(this.stock_requisition_approve_user)) {
				this.stock_requisition_approve_user = "admin";
			}
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

	public List<BatchMaterialConfig> getBatchMaterialConfigList() {
		return batchMaterialConfigList;
	}

	private void addBatchMaterialConfig(String conf, String column) {
		if (conf == null) {
			return;
		}
		String[] confAry = conf.split(",");
		try {
			if (confAry != null && confAry.length >= 2 && confAry.length <= 3) {

				BatchMaterialConfig config = new BatchMaterialConfig();
				config.setColumnName(column);
				config.setIndex(Integer.parseInt(confAry[0]));
				config.setEdit(Boolean.parseBoolean(confAry[1]));
				if (confAry.length == 3 && confAry[2].length() > 0) {
					config.setBatchSpecCode(confAry[2]);
				}

				if (batchMaterialConfigList == null) {
					batchMaterialConfigList = new ArrayList<BatchMaterialConfig>();
				}

				batchMaterialConfigList.add(config);
			}
		} catch (Exception e) {
			logger.error("", e);
		}

	}

	private static final UtilProperties single = new UtilProperties();

	// 静态工厂方法
	public static UtilProperties getInstance() {
		return single;
	}

}
