package com.cosog.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.cosog.main.AgileCalculate;
import com.cosog.model.DataRequestConfig;
import com.cosog.model.DataResponseConfig;

import oracle.sql.CLOB;

@SuppressWarnings("deprecation")
public class OracleJdbcUtis {
	private static final Logger logger = Logger.getLogger(OracleJdbcUtis.class.getName());
	
	public static BasicDataSource outerDiagramDataSource=null;
	
	public static BasicDataSource outerProductionDataSource=null;
	
	public static BasicDataSource outerDataWriteBackDataSource=null;
	
	private static void initDiagramDataSource(){
		
		DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
		
		if(dataRequestConfig!=null && dataRequestConfig.getDiagramTable()!=null && DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getDiagramTable().getConnectInfo()) ){
			
			outerDiagramDataSource = new BasicDataSource();
			
			outerDiagramDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");

			outerDiagramDataSource.setUrl("jdbc:oracle:thin:@"+dataRequestConfig.getDiagramTable().getConnectInfo().getIP()+":"+dataRequestConfig.getDiagramTable().getConnectInfo().getPort()+(dataRequestConfig.getDiagramTable().getConnectInfo().getVersion()>=12?"/":":")+dataRequestConfig.getDiagramTable().getConnectInfo().getInstanceName()+"");

			outerDiagramDataSource.setUsername(dataRequestConfig.getDiagramTable().getConnectInfo().getUser());

			outerDiagramDataSource.setPassword(dataRequestConfig.getDiagramTable().getConnectInfo().getPassword());

			outerDiagramDataSource.setInitialSize(10);  // 初始化连接数

			outerDiagramDataSource.setMaxIdle(50); // 最大空闲连接数

			outerDiagramDataSource.setMinIdle(10); // 最小空闲连接数

//			outerDiagramDataSource.setMaxIdle(100); // 最大连接数
			
			outerDiagramDataSource.setMaxActive(100);
		}
	}
	
	private static void initProductionDataSource(){
		
		DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
		
		if(dataRequestConfig!=null && dataRequestConfig.getAuxTable()!=null && DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getAuxTable().getConnectInfo()) ){
			
			outerProductionDataSource = new BasicDataSource();
			
			outerProductionDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");

			outerProductionDataSource.setUrl("jdbc:oracle:thin:@"+dataRequestConfig.getAuxTable().getConnectInfo().getIP()+":"+dataRequestConfig.getAuxTable().getConnectInfo().getPort()+(dataRequestConfig.getAuxTable().getConnectInfo().getVersion()>=12?"/":":")+dataRequestConfig.getAuxTable().getConnectInfo().getInstanceName()+"");

			outerProductionDataSource.setUsername(dataRequestConfig.getAuxTable().getConnectInfo().getUser());

			outerProductionDataSource.setPassword(dataRequestConfig.getAuxTable().getConnectInfo().getPassword());

			outerProductionDataSource.setInitialSize(10); // 初始化连接数

			outerProductionDataSource.setMaxIdle(50); // 最大空闲连接数

			outerProductionDataSource.setMinIdle(10); // 最小空闲连接数

			outerProductionDataSource.setMaxActive(100); // 最大连接数
		}
	}
	
	private static void initDataWriteBackDataSource(){
		
		DataResponseConfig dataResponseConfig=MemoryDataUtils.getDataResponseConfig();
		
		if(dataResponseConfig!=null 
				&& dataResponseConfig.getDiagramTable()!=null
				&& DataResponseConfig.ConnectInfoEffective(dataResponseConfig.getDiagramTable().getConnectInfo())){
			
			outerDataWriteBackDataSource = new BasicDataSource();
			
			outerDataWriteBackDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");

			outerDataWriteBackDataSource.setUrl("jdbc:oracle:thin:@"+dataResponseConfig.getDiagramTable().getConnectInfo().getIP()+":"+dataResponseConfig.getDiagramTable().getConnectInfo().getPort()+(dataResponseConfig.getDiagramTable().getConnectInfo().getVersion()>=12?"/":":")+dataResponseConfig.getDiagramTable().getConnectInfo().getInstanceName()+"");

			outerDataWriteBackDataSource.setUsername(dataResponseConfig.getDiagramTable().getConnectInfo().getUser());

			outerDataWriteBackDataSource.setPassword(dataResponseConfig.getDiagramTable().getConnectInfo().getPassword());

			outerDataWriteBackDataSource.setInitialSize(10); // 初始化连接数

			outerDataWriteBackDataSource.setMaxIdle(50); // 最大空闲连接数

			outerDataWriteBackDataSource.setMinIdle(10); // 最小空闲连接数

			outerDataWriteBackDataSource.setMaxActive(100); // 最大连接数
		}
	}
	
	public static Connection getDiagramConnection(){
		Connection conn=null;
		if(outerDiagramDataSource==null){
			initDiagramDataSource();
		}
		if(outerDiagramDataSource!=null){
			try {
				conn=outerDiagramDataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				StringManagerUtils.printLogFile(logger, "error", e, "error");;
			}
		}
		return conn;
	}
	
	public static Connection getProductionDataConnection(){
		Connection conn=null;
		if(outerProductionDataSource==null){
			initProductionDataSource();
		}
		if(outerProductionDataSource!=null){
			try {
				conn=outerProductionDataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				StringManagerUtils.printLogFile(logger, "error", e, "error");;
			}
		}
		return conn;
	}
	
	public static Connection getDataWriteBackConnection(){
		Connection conn=null;
		if(outerDataWriteBackDataSource==null){
			initDataWriteBackDataSource();
		}
		if(outerDataWriteBackDataSource!=null){
			try {
				conn=outerDataWriteBackDataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				StringManagerUtils.printLogFile(logger, "error", e, "error");;
			}
		}
		return conn;
	}
	
	
	public static Connection getConnection(){  
        try{
        	String driver="";
            String url = ""; 
            String username = "";
            String password = "";  
            Class.forName(driver).newInstance();  
            Connection conn = DriverManager.getConnection(url, username, password);  
            return conn;  
        }  
        catch (Exception e){  
        	e.printStackTrace();
			StringManagerUtils.printLogFile(logger, "error", e, "error");;
            return null;  
        }  
    }
	
	public static void closeDBConnection(Connection conn,PreparedStatement pstmt,ResultSet rs){  
        if(conn != null){  
            try{           
            	if(pstmt!=null)
            		pstmt.close();  
            	if(rs!=null)
            		rs.close();
                conn.close();  
            }catch(SQLException e){ 
                e.printStackTrace();  
                StringManagerUtils.printLogFile(logger, "closeDBConnectionError!","info");
                StringManagerUtils.printLogFile(logger, "error", e, "error");;
            }finally{  
                try{
                	if(pstmt!=null)
                		pstmt.close();  
                	if(rs!=null)
                		rs.close();
                }catch(SQLException e){  
                    e.printStackTrace();  
                    StringManagerUtils.printLogFile(logger, "error", e, "error");;
                }  
                conn = null;  
            }  
        }  
    }
	
	public static void closeDBConnection(Connection conn,Statement stmt,PreparedStatement pstmt,ResultSet rs){  
        if(conn != null){  
            try{
            	if(stmt!=null){
            		stmt.close();
            	}
            	if(pstmt!=null)
            		pstmt.close();  
            	if(rs!=null)
            		rs.close();
                conn.close();  
            }catch(SQLException e){
                e.printStackTrace();  
                StringManagerUtils.printLogFile(logger, "closeDBConnectionError!","info");
                StringManagerUtils.printLogFile(logger, "error", e, "error");;
            }finally{  
                try{
                	if(stmt!=null){
                		stmt.close();
                	}
                	if(pstmt!=null)
                		pstmt.close();  
                	if(rs!=null)
                		rs.close();
                }catch(SQLException e){  
                    e.printStackTrace();  
                    StringManagerUtils.printLogFile(logger, "error", e, "error");;
                }  
                conn = null;  
            }  
        }  
    }
	
	public static int executeSqlUpdateClob(Connection conn,PreparedStatement ps,String sql,List<String> values) throws SQLException {
		int n = 0;
		for(int i=0;i<values.size();i++){
			CLOB clob   = oracle.sql.CLOB.createTemporary(conn, false,oracle.sql.CLOB.DURATION_SESSION);  
			clob.putString(1,  values.get(i)); 
			ps.setClob(i+1, clob);  
		}
		n=ps.executeUpdate();  
//		ps.close();  
//		conn.commit(); 
		return n;
	}
	
	public static List<List<Object>> queryProductionData(String sql){
		DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<List<Object>> list=new ArrayList<List<Object>>();
		try{
			if( (!DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getAuxTable().getConnectInfo())) || DataRequestConfig.ConnectInfoEquals(dataRequestConfig.getAuxTable().getConnectInfo(), dataRequestConfig.getDiagramTable().getConnectInfo())  ){
				conn=OracleJdbcUtis.getDiagramConnection();//配置无效或者和功图数据表连接配置相同，获取功图数据表连接
			}else{
				conn=OracleJdbcUtis.getProductionDataConnection();
			}
			if(conn!=null){
				pstmt = conn.prepareStatement(sql);
				rs=pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				while(rs.next()){
					List<Object> prodList=new ArrayList<Object>();
			        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			            String columnName = rsmd.getColumnName(i);
			            Object value = rs.getObject(i);
			            prodList.add(value);
			        }
			        list.add(prodList);
				}
			}else{
				StringManagerUtils.printLog("Production data database connection failure");
				StringManagerUtils.printLogFile(logger, "Production data database connection failure","info");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			StringManagerUtils.printLogFile(logger, "error", e1, "error");
			StringManagerUtils.printLog("Failed to query production data,sql:"+sql);
			StringManagerUtils.printLogFile(logger, "Failed to query production data,sql:"+sql,"error");
		} catch (Exception e1) {
			e1.printStackTrace();
			StringManagerUtils.printLogFile(logger, "error", e1, "error");
		}finally{
			closeDBConnection(conn, pstmt, rs);
		}
		return list;
	}
	
	public static List<List<Object>> queryFESDiagramData(String sql){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<List<Object>> list=new ArrayList<List<Object>>();
		try{
			conn=OracleJdbcUtis.getDiagramConnection();
			if(conn!=null){
				pstmt = conn.prepareStatement(sql);
				rs=pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				while(rs.next()){
					List<Object> prodList=new ArrayList<Object>();
			        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			            String columnName = rsmd.getColumnName(i);
			            Object value = rs.getObject(i);
			            prodList.add(value);
			        }
			        list.add(prodList);
				}
			}else{
				StringManagerUtils.printLog("Diagram data database connection failure");
				StringManagerUtils.printLogFile(logger, "Diagram data database connection failure","info");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			StringManagerUtils.printLogFile(logger, "error", e, "error");
			StringManagerUtils.printLog("sql:"+sql);
			StringManagerUtils.printLogFile(logger, "sql:"+sql, "error");
		} catch (Exception e) {
			e.printStackTrace();
			StringManagerUtils.printLogFile(logger, "error", e, "error");
		}finally{
			closeDBConnection(conn, pstmt, rs);
		}
		return list;
	}
	
	public static int writeBackDiagramCalculateData(String writeBackSql){
		Connection writeBackConn = null;
		PreparedStatement writeBackPstmt = null;
		ResultSet writeBackRs = null;
		int iNum=0;
		try {
			writeBackConn=OracleJdbcUtis.getDataWriteBackConnection();
			if(writeBackConn!=null){
				writeBackPstmt=writeBackConn.prepareStatement(writeBackSql);
				iNum=writeBackPstmt.executeUpdate();
			}else{
				StringManagerUtils.printLog("Write back database connection failure");
				StringManagerUtils.printLogFile(logger, "Write back database connection failure","info");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			StringManagerUtils.printLogFile(logger, "error", e, "error");
			StringManagerUtils.printLog("sql:"+writeBackSql);
			StringManagerUtils.printLogFile(logger, "sql:"+writeBackSql, "error");
		} catch (Exception e) {
			e.printStackTrace();
			StringManagerUtils.printLogFile(logger, "error", e, "error");
		}finally{
			closeDBConnection(writeBackConn, writeBackPstmt, writeBackRs);
		}
		return iNum;
	}
}
