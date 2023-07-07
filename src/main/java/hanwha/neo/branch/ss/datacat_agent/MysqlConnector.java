package hanwha.neo.branch.ss.datacat_agent;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlConnector {

	
	public String executeMysql(String q) throws SQLException, ClassNotFoundException {
		Connection mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost:33060/vpp?characterEncoding=UTF-8&enabledTLSProtocols=TLSv1.2&clusterInstanceHostPattern=?.0.0.1&serverTimezone=UTC","sithome","sit0911!");	
		
		Statement mysqlSt = mysqlConn.createStatement();
        
		try {
			
			ResultSet rs = mysqlSt.executeQuery(q); // ResultSet은 쿼리문을 보낸후 나온 결과를 가져올 때 사용한다.
			String result = "";
			while(rs.next()){
				result = rs.getString("TIMESTAMPDIFF");
			}
			return result;
			
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