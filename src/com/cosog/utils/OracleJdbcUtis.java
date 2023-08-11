package com.cosog.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;

import com.cosog.model.DataSourceConfig;
import com.cosog.model.DataWriteBackConfig;

import oracle.sql.CLOB;

@SuppressWarnings("deprecation")
public class OracleJdbcUtis {

	public static BasicDataSource outerDiagramDataSource=null;
	
	public static BasicDataSource outerProductionDataSource=null;
	
	public static BasicDataSource outerDataWriteBackDataSource=null;
	
	private static void initDiagramDataSource(){
		
		DataSourceConfig dataSourceConfig=MemoryDataUtils.getDataSourceConfig();
		
		if(dataSourceConfig!=null && dataSourceConfig.getDiagramTable()!=null && dataSourceConfig.getDiagramTable().getConnectInfo()!=null && dataSourceConfig.getDiagramTable().getEnable()){
			
			outerDiagramDataSource = new BasicDataSource();
			
			outerDiagramDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");

			outerDiagramDataSource.setUrl("jdbc:oracle:thin:@"+dataSourceConfig.getDiagramTable().getConnectInfo().getIP()+":"+dataSourceConfig.getDiagramTable().getConnectInfo().getPort()+(dataSourceConfig.getDiagramTable().getConnectInfo().getVersion()>=12?"/":":")+dataSourceConfig.getDiagramTable().getConnectInfo().getInstanceName()+"");

			outerDiagramDataSource.setUsername(dataSourceConfig.getDiagramTable().getConnectInfo().getUser());

			outerDiagramDataSource.setPassword(dataSourceConfig.getDiagramTable().getConnectInfo().getPassword());

			outerDiagramDataSource.setInitialSize(5); // 初始化连接数

			outerDiagramDataSource.setMaxIdle(10); // 最大空闲连接数

			outerDiagramDataSource.setMinIdle(5); // 最小空闲连接数

			outerDiagramDataSource.setMaxIdle(100); // 最大连接数
		}
	}
	
	private static void initProductionDataSource(){
		
		DataSourceConfig dataSourceConfig=MemoryDataUtils.getDataSourceConfig();
		
		if(dataSourceConfig!=null && dataSourceConfig.getProductionDataTable()!=null && dataSourceConfig.getProductionDataTable().getConnectInfo()!=null && dataSourceConfig.getProductionDataTable().getEnable()){
			
			outerProductionDataSource = new BasicDataSource();
			
			outerProductionDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");

			outerProductionDataSource.setUrl("jdbc:oracle:thin:@"+dataSourceConfig.getProductionDataTable().getConnectInfo().getIP()+":"+dataSourceConfig.getProductionDataTable().getConnectInfo().getPort()+(dataSourceConfig.getProductionDataTable().getConnectInfo().getVersion()>=12?"/":":")+dataSourceConfig.getProductionDataTable().getConnectInfo().getInstanceName()+"");

			outerProductionDataSource.setUsername(dataSourceConfig.getProductionDataTable().getConnectInfo().getUser());

			outerProductionDataSource.setPassword(dataSourceConfig.getProductionDataTable().getConnectInfo().getPassword());

			outerProductionDataSource.setInitialSize(5); // 初始化连接数

			outerProductionDataSource.setMaxIdle(10); // 最大空闲连接数

			outerProductionDataSource.setMinIdle(5); // 最小空闲连接数

			outerProductionDataSource.setMaxIdle(100); // 最大连接数
		}
	}
	
	private static void initDataWriteBackDataSource(){
		
		DataWriteBackConfig dataWriteBackConfig=MemoryDataUtils.getDataWriteBackConfig();
		
		if(dataWriteBackConfig!=null && dataWriteBackConfig.getConnectInfo()!=null && dataWriteBackConfig.isEnable()){
			
			outerDataWriteBackDataSource = new BasicDataSource();
			
			outerDataWriteBackDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");

			outerDataWriteBackDataSource.setUrl("jdbc:oracle:thin:@"+dataWriteBackConfig.getConnectInfo().getIP()+":"+dataWriteBackConfig.getConnectInfo().getPort()+(dataWriteBackConfig.getConnectInfo().getVersion()>=12?"/":":")+dataWriteBackConfig.getConnectInfo().getInstanceName()+"");

			outerDataWriteBackDataSource.setUsername(dataWriteBackConfig.getConnectInfo().getUser());

			outerDataWriteBackDataSource.setPassword(dataWriteBackConfig.getConnectInfo().getPassword());

			outerDataWriteBackDataSource.setInitialSize(5); // 初始化连接数

			outerDataWriteBackDataSource.setMaxIdle(10); // 最大空闲连接数

			outerDataWriteBackDataSource.setMinIdle(5); // 最小空闲连接数

			outerDataWriteBackDataSource.setMaxIdle(100); // 最大连接数
		}
	}
	
	public static Connection getDiagramConnection() throws SQLException{
		Connection conn=null;
		if(outerDiagramDataSource==null){
			initDiagramDataSource();
		}
		if(outerDiagramDataSource!=null){
			conn=outerDiagramDataSource.getConnection();
		}
		return conn;
	}
	
	public static Connection getProductionDataConnection() throws SQLException{
		Connection conn=null;
		if(outerProductionDataSource==null){
			initProductionDataSource();
		}
		if(outerProductionDataSource!=null){
			conn=outerProductionDataSource.getConnection();
		}
		return conn;
	}
	
	public static Connection getDataWriteBackConnection() throws SQLException{
		Connection conn=null;
		if(outerDataWriteBackDataSource==null){
			initDataWriteBackDataSource();
		}
		if(outerDataWriteBackDataSource!=null){
			conn=outerDataWriteBackDataSource.getConnection();
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
            System.out.println(e.getMessage());  
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
            }finally{  
                try{
                	if(pstmt!=null)
                		pstmt.close();  
                	if(rs!=null)
                		rs.close();
                }catch(SQLException e){  
                    e.printStackTrace();  
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
