package com.inossem.wms.web.biz;

import java.sql.SQLException;

public class ConnOracle {

	public static void main(String[] args) {	
	        OracleJdbcTest test = new OracleJdbcTest();
	        try {
				test.query("SELECT\r\n" + 
						"	AUFK.AUFNR,\r\n" + 
						"	AUFK.AUART,\r\n" + 
						"	AUFK.KTEXT,\r\n" + 
						"	AUFK.WERKS,\r\n" + 
						"	AUFK.BUKRS,\r\n" + 
						"	AFKO.AUFNR,\r\n" + 
						"	AFKO.GAMNG,\r\n" + 
						"	AFKO.GMEIN,\r\n" + 
						"	AFKO.PLNBEZ,\r\n" + 
						"	AFKO.PLGRP,\r\n" + 
						"	AFKO.DISPO,\r\n" + 
						"	AFKO.FEVOR\r\n" + 
						"FROM\r\n" + 
						"	AUFK,\r\n" + 
						"	AFKO\r\n" + 
						"WHERE\r\n" + 
						"	AUFK.AUFNR = AFKO.AUFNR\r\n" + 
						"and AUFK.AUFNR='001500008329'", true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	       

	        test.close();
	    }
}
