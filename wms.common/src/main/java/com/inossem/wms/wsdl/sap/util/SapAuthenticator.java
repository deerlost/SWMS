package com.inossem.wms.wsdl.sap.util;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import com.inossem.wms.util.UtilProperties;

public class SapAuthenticator extends Authenticator {
	
	private static PasswordAuthentication pa = null;
	
	//private final static String username = "RFCIWMSCW";
	//private final static char[] password = new char[]{'S', 'I', 'N', 'O', 'P', 'E', 'C', '1'};
	//private final static char[] password = new char[]{'S', '$', 'N', 'O', 'P', 'E', 'C', '2','0','1','8'};
	private final static String username = UtilProperties.getInstance().getSapUser();
	private final static char[] password = UtilProperties.getInstance().getSapPass().toCharArray();
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
	    return getInstance();
	}
	
	static {  
		pa = new PasswordAuthentication(username, password);  
	}
	
	//静态工厂方法   
	private static PasswordAuthentication getInstance() {
        return pa;
    }
	
}