package com.main.datacat_agent.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlConnector {

	
	public String executeMysql(String q) throws SQLException {
		Connection mysqlConn = DriverManager.getConnection("jdbc:mysql://dev-dp-db1-cluster-virginia-instance-1.c8vihicq2w3y.us-east-1.rds.amazonaws.com:33060/uep?characterEncoding=UTF-8&enabledTLSProtocols=TLSv1.2&clusterInstanceHostPattern=?.0.0.1&serverTimezone=UTC","sithome","sit0911!");	
		
		Statement mysqlSt = mysqlConn.createStatement();

		try {
			
			ResultSet rs = mysqlSt.executeQuery(q); // ResultSet은 쿼리문을 보낸후 나온 결과를 가져올 때 사용한다.
			
			return rs.getString(0);
		} catch (Exception e) {
			e.printStackTrace();
            return null;
		}finally {
			try {
				
				if(mysqlSt != null)
                    mysqlSt.close();
				
				if(mysqlConn != null)
                    mysqlConn.close();
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		
	}

}