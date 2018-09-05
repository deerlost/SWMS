package com.inossem.wms.web.biz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OracleJdbcTest {
    //数据库连接对象
    private static Connection conn = null;
     
    private static String driver = "oracle.jdbc.driver.OracleDriver"; //驱动
     
    private static String url = "jdbc:oracle:thin:@//10.122.1.175:1521/cwedw"; //连接字符串
     
    private static String username = "CWDBA"; //用户名
     
    private static String password = "cwedw"; //密码
     
     
    // 获得连接对象
    private static synchronized Connection getConn(){
        if(conn == null){
            try {
                Class.forName(driver);
                conn = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
     
    //执行查询语句
    public List<Map<String,Object>> query(String sql, boolean isSelect) throws SQLException{
        PreparedStatement pstmt;
        List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();  
        try {
            pstmt = getConn().prepareStatement(sql);
            //建立一个结果集，用来保存查询出来的结果
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();  
            int fieldCount = rsmd.getColumnCount();  
          
            while (rs.next()) {
//                String name = rs.getString("AUFK");
                Map<String, Object> valueMap = new LinkedHashMap<String, Object>();  
                for (int i = 1; i <= fieldCount; i++)  
                {  
//                    String fieldClassName = rsmd.getColumnClassName(i);  
                    String fieldName = rsmd.getColumnName(i);  
                    valueMap.put(fieldName, rs.getObject(i));
                }  
                records.add(valueMap);  
//                System.out.println(name);
            }
            rs.close();
            pstmt.close();
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
     
    public void query(String sql) throws SQLException{
        PreparedStatement pstmt;
        pstmt = getConn().prepareStatement(sql);
        pstmt.execute();
        pstmt.close();
    }
     
     
    //关闭连接
    public void close(){
        try {
            getConn().close();
             
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     
}
