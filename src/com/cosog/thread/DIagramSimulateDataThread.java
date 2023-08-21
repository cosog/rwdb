package com.cosog.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cosog.model.DataRequestConfig;
import com.cosog.model.DataResponseConfig;
import com.cosog.utils.MemoryDataUtils;
import com.cosog.utils.OracleJdbcUtis;
import com.cosog.utils.StringManagerUtils;

public class DIagramSimulateDataThread extends Thread{
	public void run(){
		DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
		DataResponseConfig dataResponseConfig=MemoryDataUtils.getDataResponseConfig();
		if(dataRequestConfig!=null 
				&& dataRequestConfig.getDiagramTable()!=null 
				&& dataRequestConfig.getDiagramTable().getEnable() 
				&& dataRequestConfig.getDiagramTable().getTableInfo()!=null 
				&& dataRequestConfig.getDiagramTable().getTableInfo().getColumns()!=null 
				&& DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getDiagramTable().getConnectInfo())
				&& dataResponseConfig!=null && dataResponseConfig.isEnable()){
			String time="";
			String writeDataInsertSql="";
			String diagramDataInsertSql="";
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Connection writeBackConn = null;
			PreparedStatement writeBackPstmt = null;
			ResultSet writeBackRs = null;
			do{
				try {
					conn=OracleJdbcUtis.getDiagramConnection();
					writeBackConn=OracleJdbcUtis.getDataWriteBackConnection();
					if(conn!=null && writeBackConn!=null){
						time=StringManagerUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss");
						String wellName="";
						int iNum=0;
						System.out.println(time+"-生成模拟数据");
						try{  
							for(int i=1;i<=100;i++){
								if(i<10){
									wellName="rpc00"+i;
								}else if(i>=10&& i<100){
									wellName="rpc0"+i;
								}else{
									wellName="rpc"+i;
								}
								writeDataInsertSql="insert into "+dataResponseConfig.getDiagramResult().getTableName()
										+"("+dataResponseConfig.getDiagramResult().getColumns().getWellName().getColumn()+","+dataResponseConfig.getDiagramResult().getColumns().getAcqTime().getColumn()+") "
										+ " values('"+wellName+"',to_date('"+time+"','yyyy-mm-dd hh24:mi:ss'))";
								writeBackPstmt=writeBackConn.prepareStatement(writeDataInsertSql);
					            iNum=writeBackPstmt.executeUpdate();
							}
							for(int i=1;i<=100;i++){
								if(i<10){
									wellName="rpc00"+i;
								}else if(i>=10&& i<100){
									wellName="rpc0"+i;
								}else{
									wellName="rpc"+i;
								}
								diagramDataInsertSql="insert into PC_FD_PUMPJACK_FDYNA_DIA_T "
										+ "(well_common_name,"
										+ "check_date,"
										+ "dyna_create_time,"
										+ "stroke,"
										+ "frequency,"
										+ "dyna_points,"
										+ "displacement,"
										+ "disp_load,"
										+ "disp_current,"
										+ "active_power) "
										+ "values ('"+wellName+"',to_date('"+time+"','yyyy-mm-dd hh24:mi:ss'),to_date('"+time+"','yyyy-mm-dd hh24:mi:ss'),"
										+ "4.71,2.38,200,"
										+ "'0.00;0.01;0.01;0.01;0.01;0.04;0.04;0.04;0.04;0.08;0.08;0.08;0.08;0.13;0.13;0.13;0.13;0.19;0.19;0.19;0.19;0.27;0.27;0.27;0.27;0.38;0.38;0.38;0.38;0.50;0.50;0.50;0.50;0.63;0.63;0.63;0.63;0.77;0.77;0.77;0.77;0.91;0.91;0.91;0.91;1.07;1.07;1.07;1.07;1.24;1.24;1.24;1.24;1.42;1.42;1.42;1.42;1.60;1.60;1.60;1.60;1.78;1.78;1.78;1.95;1.95;2.12;2.12;2.27;2.27;2.44;2.44;2.60;2.60;2.76;2.76;2.93;2.93;3.10;3.10;3.26;3.26;3.43;3.43;3.59;3.59;3.75;3.75;3.89;3.89;4.04;4.04;4.18;4.18;4.31;4.31;4.43;4.43;4.54;4.54;4.62;4.62;4.68;4.68;4.73;4.73;4.75;4.75;4.76;4.76;4.75;4.75;4.73;4.73;4.68;4.68;4.62;4.62;4.52;4.52;4.41;4.41;4.29;4.29;4.15;4.15;3.99;3.99;3.83;3.83;3.65;3.65;3.44;3.44;3.23;3.23;3.00;3.00;2.78;2.78;2.56;2.56;2.35;2.35;2.14;2.14;1.95;1.95;1.77;1.77;1.60;1.60;1.44;1.44;1.28;1.28;1.15;1.15;1.02;1.02;0.92;0.92;0.84;0.84;0.77;0.77;0.71;0.71;0.66;0.66;0.61;0.61;0.57;0.57;0.54;0.54;0.53;0.53;0.54;0.54;0.56;0.56;0.55;0.55;0.52;0.52;0.45;0.45;0.35;0.35;0.24;0.24;0.13;0.13;0.05;0.05;0.01;0.01;0.00;0.00',"
										+ "'58.94;58.94;58.94;58.94;58.94;59.64;59.64;59.64;59.64;61.05;61.05;61.05;61.05;61.05;61.05;61.05;61.05;62.47;62.47;62.47;62.47;63.17;63.17;63.17;63.17;66.00;66.00;66.00;66.00;69.52;69.52;69.52;69.52;73.76;73.76;73.76;73.76;77.29;77.29;77.29;77.29;79.41;79.41;79.41;79.41;85.05;85.05;85.05;85.05;89.29;89.29;89.29;89.29;92.82;92.82;92.82;92.82;92.11;92.11;92.11;92.11;91.41;91.41;91.41;90.00;90.00;90.00;90.00;90.70;90.70;91.41;91.41;91.41;91.41;91.41;91.41;91.41;91.41;90.70;90.70;90.70;90.70;91.41;91.41;90.70;90.70;91.41;91.41;90.70;90.70;90.70;90.70;90.00;90.00;90.00;90.00;90.00;90.00;90.00;90.00;90.00;90.00;90.00;90.00;89.29;89.29;89.29;89.29;88.58;88.58;88.58;88.58;87.17;87.17;85.76;85.76;84.35;84.35;83.64;83.64;82.23;82.23;78.70;78.70;75.88;75.88;73.05;73.05;69.52;69.52;65.29;65.29;61.05;61.05;56.82;56.82;57.52;57.52;58.94;58.94;59.64;59.64;58.94;58.94;58.23;58.23;58.23;58.23;58.23;58.23;58.23;58.23;56.82;56.82;56.82;56.82;56.82;56.82;57.52;57.52;56.82;56.82;57.52;57.52;58.23;58.23;58.23;58.23;58.23;58.23;58.94;58.94;60.35;60.35;61.05;61.05;61.05;61.05;61.05;61.05;61.76;61.76;61.76;61.76;58.94;58.94;58.23;58.23;58.23;58.23;58.23;58.23;58.23;58.23;57.52;57.52;58.23;58.23;58.94;58.94',"
										+ "'62.77;63.23;63.69;64.15;64.61;64.91;65.05;65.03;65.01;64.99;64.97;64.94;64.81;64.56;64.20;63.84;63.48;63.12;62.74;62.32;61.87;61.40;60.93;60.46;59.99;59.59;59.27;59.02;58.77;58.52;58.27;58.01;57.80;57.64;57.53;57.42;57.31;57.20;57.08;56.98;56.90;56.83;56.76;56.69;56.62;56.61;56.66;56.77;56.88;56.99;57.10;57.22;57.35;57.48;57.60;57.72;57.84;57.96;58.08;58.20;58.32;58.44;58.52;58.57;58.58;58.59;58.60;58.61;58.62;58.63;58.64;58.66;58.63;58.54;58.37;58.20;58.03;57.86;57.69;57.52;57.35;57.16;57.02;56.91;56.85;56.79;56.73;56.67;56.61;56.55;56.49;56.43;56.38;56.35;56.33;56.31;56.29;56.27;56.25;56.23;56.21;56.24;56.33;56.47;56.61;56.75;56.89;57.04;57.20;57.37;57.53;57.69;57.85;58.01;58.18;58.41;58.69;59.00;59.31;59.62;59.93;60.16;60.33;60.42;60.51;60.60;60.69;60.79;60.82;60.78;60.66;60.54;60.42;60.30;60.16;59.95;59.67;59.34;59.01;58.68;58.35;58.11;57.96;57.90;57.84;57.78;57.72;57.65;57.65;57.74;57.91;58.08;58.25;58.42;58.59;58.77;58.88;58.92;58.86;58.80;58.74;58.68;58.62;58.56;58.50;58.41;58.30;58.17;58.05;57.93;57.81;57.69;57.57;57.45;57.33;57.20;57.15;57.19;57.32;57.45;57.58;57.71;57.84;57.97;58.10;58.25;58.47;58.76;59.08;59.40;59.72;60.04;60.36;60.68;61.00;61.32;61.61;61.87;62.24;62.69',"
										+ "'31.91;31.38;30.85;30.32;29.78;29.43;29.26;29.28;29.30;29.32;29.34;29.36;29.54;29.86;30.32;30.78;31.24;31.70;32.18;32.74;33.40;34.12;34.84;35.56;36.28;36.95;37.57;38.12;38.67;39.22;39.77;40.32;40.80;41.21;41.55;41.89;42.23;42.57;42.91;43.38;43.98;44.71;45.44;46.17;46.91;47.61;48.26;48.85;49.44;50.03;50.62;51.22;50.00;46.95;42.05;37.15;32.25;27.35;22.45;17.55;12.65;7.73;4.48;2.89;2.97;3.05;3.13;3.21;3.29;3.37;3.45;3.54;5.21;8.47;13.29;18.11;22.93;27.75;32.57;37.39;42.21;47.05;50.04;51.16;50.39;49.62;48.85;48.08;47.31;46.54;45.77;44.97;44.48;44.29;44.43;44.57;44.71;44.85;44.99;45.13;45.28;45.18;44.82;44.18;43.54;42.90;42.26;41.61;41.07;40.65;40.34;40.03;39.72;39.41;39.08;38.69;38.25;37.76;37.27;36.78;36.28;35.91;35.65;35.51;35.37;35.23;35.09;34.95;34.94;35.08;35.35;35.62;35.89;36.16;36.43;36.89;37.56;38.42;39.28;40.14;41.00;41.91;42.89;43.92;44.95;45.98;47.01;48.04;46.77;43.22;37.37;31.52;25.67;19.82;13.97;8.09;4.15;2.14;2.08;2.02;1.96;1.90;1.84;1.78;1.72;1.65;3.05;5.90;10.21;14.52;18.83;23.14;27.45;31.76;36.07;40.39;43.08;44.16;43.60;43.04;42.48;41.92;41.36;40.80;40.24;39.66;39.08;38.51;37.95;37.39;36.83;36.27;35.71;35.15;34.59;34.01;33.51;33.10;32.59;32.02')";
								pstmt=conn.prepareStatement(diagramDataInsertSql);
					            iNum=pstmt.executeUpdate();
							}
				        }catch(RuntimeException re){  
				        	re.printStackTrace();
				        }
						
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					OracleJdbcUtis.closeDBConnection(conn, pstmt, rs);
					OracleJdbcUtis.closeDBConnection(writeBackConn, writeBackPstmt, writeBackRs);
				}
				try {
					Thread.sleep(60*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}while(true);
			
		}
	}
}
