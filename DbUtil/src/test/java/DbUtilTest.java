import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bravechen.util.db.DbConfig;
import com.bravechen.util.db.DbUtil;

public class DbUtilTest {
	private Connection oracleConnection;
	private Connection mysqlConnection;
	

	@Before
	public void setUp() throws Exception {
		oracleConnection = DbConfig.getConnection("oracle");
		mysqlConnection = DbConfig.getConnection("mysql");
	}

	@After
	public void tearDown() throws Exception {
		DbUtil.closeQuietly(oracleConnection);
		DbUtil.closeQuietly(mysqlConnection);
	}

	@Test
	public void test() {
		try {
			DbUtil.query(oracleConnection, "SELECT 1 FROM DUAL");
			DbUtil.query(mysqlConnection, "SELECT 1");
		} catch (SQLException e) {
			fail(e.getMessage());
		}
	}
	
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		Connection sourceConn = DbConfig.getConnection("oracle");
		
		// get tables
		List<Map<String, String>> query = DbUtil.query(sourceConn, "SELECT TABLE_NAME FROM USER_TABLES");

		// get total count
		for (Map<String, String> table : query) {
			String name = table.get("TABLE_NAME");
			try {
				DbUtil.update(sourceConn, "UPDATE " + name + " SET I_DATA_DOMAIN_ID = 1010007 WHERE I_DATA_DOMAIN_ID <> 1010007");
			}catch(Exception e) {
				if(e.getMessage().indexOf("标识符无效") == -1) {
					System.out.println(e + " talbe:"+ name);
				}
				
				try {
					DbUtil.update(sourceConn, "UPDATE " + name + " SET DATA_DOMAIN_ID = 1010007 WHERE DATA_DOMAIN_ID = 1010001");
				}catch(Exception e2) {
					if(e.getMessage().indexOf("标识符无效") == -1) {
						System.out.println(e + " talbe:"+ name);
					}
					
					try {
						DbUtil.update(sourceConn, "UPDATE " + name + " SET IDOMAINID = 1010007 WHERE IDOMAINID = 1010001");
					}catch(Exception e3) {
						if(e.getMessage().indexOf("标识符无效") == -1) {
							System.out.println(e + " talbe:"+ name);
						}
					}
				}
			}
			try {
				DbUtil.update(sourceConn, "UPDATE " + name + " SET I_MANAGE_DOMAIN_ID = 1010007 WHERE I_MANAGE_DOMAIN_ID = 1010001");
			}catch(Exception e) {
				if(e.getMessage().indexOf("标识符无效") == -1) {
					System.out.println(e + " talbe:"+ name);
				}
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + ((end - start) / 1000 / 60) + "分 " + ((end - start) / 1000 % 60 + 1) + "秒 ");
	}

}
