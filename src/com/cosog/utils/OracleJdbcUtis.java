package com.cosog.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

			outerDiagramDataSource.setInitialSize(5);  // 初始化连接数

			outerDiagramDataSource.setMaxIdle(10); // 最大空闲连接数

			outerDiagramDataSource.setMinIdle(5); // 最小空闲连接数

			outerDiagramDataSource.setMaxIdle(100); // 最大连接数
		}
	}
	
	private static void initProductionDataSource(){
		
		DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
		
		if(dataRequestConfig!=null && dataRequestConfig.getProductionTable()!=null && DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getProductionTable().getConnectInfo()) ){
			
			outerProductionDataSource = new BasicDataSource();
			
			outerProductionDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");

			outerProductionDataSource.setUrl("jdbc:oracle:thin:@"+dataRequestConfig.getProductionTable().getConnectInfo().getIP()+":"+dataRequestConfig.getProductionTable().getConnectInfo().getPort()+(dataRequestConfig.getProductionTable().getConnectInfo().getVersion()>=12?"/":":")+dataRequestConfig.getProductionTable().getConnectInfo().getInstanceName()+"");

			outerProductionDataSource.setUsername(dataRequestConfig.getProductionTable().getConnectInfo().getUser());

			outerProductionDataSource.setPassword(dataRequestConfig.getProductionTable().getConnectInfo().getPassword());

			outerProductionDataSource.setInitialSize(5); // 初始化连接数

			outerProductionDataSource.setMaxIdle(10); // 最大空闲连接数

			outerProductionDataSource.setMinIdle(5); // 最小空闲连接数

			outerProductionDataSource.setMaxIdle(100); // 最大连接数
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

			outerDataWriteBackDataSource.setInitialSize(5); // 初始化连接数

			outerDataWriteBackDataSource.setMaxIdle(10); // 最大空闲连接数

			outerDataWriteBackDataSource.setMinIdle(5); // 最小空闲连接数

			outerDataWriteBackDataSource.setMaxIdle(100); // 最大连接数
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
                System.out.println("closeDBConnectionError!");  
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
                System.out.println("closeDBConnectionError!");  
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
}
