package hanwha.neo.branch.ss.datacat_agent;


import java.sql.SQLException;

public class TEST2 {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		MysqlConnector mysqlConnector = new MysqlConnector();
		String result = mysqlConnector.executeMysql("select CBP_GEN_ID, TRADE_DAY, COLCT_ITEM_CD,\n"
									+ "HH_00, HH_01, HH_02, HH_03, HH_04, HH_05, HH_06,\n"
									+ "HH_07, HH_08, HH_09, HH_10, HH_11, HH_12,\n"
									+ "HH_13, HH_14, HH_15, HH_16, HH_17, HH_18,\n"
									+ "HH_19, HH_20, HH_21, HH_22, HH_23,\n"
									+ "CURRENT_TIMESTAMP, '200', 'Y' \n"
									+ "from vpp.if_kpx_forecast_group_tmcl_geneqty_dtl where  STR_TO_DATE(TRADE_DAY ,'%Y%m%d%H%i%s') >=curdate()");
		System.out.println(result);

	}
}
