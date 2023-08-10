package com.cosog.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cosog.model.DataSourceConfig;
import com.cosog.utils.Config;
import com.cosog.utils.MemoryDataUtils;
import com.cosog.utils.OracleJdbcUtis;
import com.cosog.utils.StringManagerUtils;
import com.google.gson.Gson;

public class RPCWellDataSyncThread  extends Thread{
	private String wellName;
	private String acqtime;
	private String fesdiagramacqtime;
	public RPCWellDataSyncThread(String wellName, String acqtime, String fesdiagramacqtime) {
		super();
		this.wellName = wellName;
		this.acqtime = acqtime;
		this.fesdiagramacqtime = fesdiagramacqtime;
	}
	
	public void run(){
		DataSourceConfig dataSourceConfig=MemoryDataUtils.getDataSourceConfig();
		if(dataSourceConfig!=null && dataSourceConfig.isEnable()){
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				conn=OracleJdbcUtis.getOuterConnection();
				if(conn!=null){
					if(dataSourceConfig.getDiagramTable().getEnable()){
						String wellNameColumn=dataSourceConfig.getDiagramTable().getColumns().getWellName().getColumn();
						String acqTimeColumn=dataSourceConfig.getDiagramTable().getColumns().getAcqTime().getColumn();
						String strokeColumn=dataSourceConfig.getDiagramTable().getColumns().getStroke().getColumn();
						String spmColumn=dataSourceConfig.getDiagramTable().getColumns().getSPM().getColumn();
						String pointCountColumn=dataSourceConfig.getDiagramTable().getColumns().getPointCount().getColumn();
						String sColumn=dataSourceConfig.getDiagramTable().getColumns().getS().getColumn();
						String fColumn=dataSourceConfig.getDiagramTable().getColumns().getF().getColumn();
						String iColumn=dataSourceConfig.getDiagramTable().getColumns().getI().getColumn();
						String KWattColumn=dataSourceConfig.getDiagramTable().getColumns().getKWatt().getColumn();
						String sql="";
						String finalSql="";
						Gson gson = new Gson();

						sql="select "+wellNameColumn+","
								+ "to_char("+acqTimeColumn+",'yyyy-mm-dd hh24:mi:ss') as "+acqTimeColumn+", "
								+ strokeColumn+", "
								+ spmColumn+", "
								+ pointCountColumn+", "
								+ sColumn+", "
								+ fColumn+", "
								+ iColumn+", "
								+ KWattColumn+" "
								+ " from "+dataSourceConfig.getDiagramTable().getName()+" t "
								+ " where t."+dataSourceConfig.getDiagramTable().getColumns().getWellName().getColumn()+"='"+this.wellName+"'";
						if(StringManagerUtils.isNotNull(this.fesdiagramacqtime)){
							sql+=" and "+acqTimeColumn+" > to_date('"+fesdiagramacqtime+"','yyyy-mm-dd hh24:mi:ss') order by "+acqTimeColumn;
						}else{
							sql+="order by "+acqTimeColumn+" desc";
						}
						
						finalSql="select "+wellNameColumn+","
								+ acqTimeColumn+", "
								+ strokeColumn+", "
								+ spmColumn+", "
								+ pointCountColumn+", "
								+ sColumn+", "
								+ fColumn+", "
								+ iColumn+", "
								+ KWattColumn+" "
								+ " from ("+sql+") v";
						if(StringManagerUtils.isNotNull(this.fesdiagramacqtime)){
							finalSql+=" where rownum<=100";
						}else{
							finalSql+=" where rownum<=1";//取最新数据
						}
						
						pstmt = conn.prepareStatement(finalSql);
						rs=pstmt.executeQuery();
						while(rs.next()){
							String fesdiagramAcqtimeStr=rs.getString(2);
							float stroke=rs.getFloat(3);
							float spm=rs.getFloat(4);
							int pointCount=rs.getInt(5);
							String sStr=rs.getString(6);
							String fStr=rs.getString(7);
							String iStr=rs.getString(8);
							String KWattStr=rs.getString(9);
							this.fesdiagramacqtime=fesdiagramAcqtimeStr;
							
						}
					}
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				OracleJdbcUtis.closeDBConnection(conn, pstmt, rs);
			}
		}
	}
	
	public String getWellName() {
		return wellName;
	}
	public void setWellName(String wellName) {
		this.wellName = wellName;
	}
	public String getAcqtime() {
		return acqtime;
	}
	public void setAcqtime(String acqtime) {
		this.acqtime = acqtime;
	}
	public String getFesdiagramacqtime() {
		return fesdiagramacqtime;
	}
	public void setFesdiagramacqtime(String fesdiagramacqtime) {
		this.fesdiagramacqtime = fesdiagramacqtime;
	}
}
