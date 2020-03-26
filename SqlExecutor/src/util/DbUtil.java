package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * org.apache.commons.DbUtils.DbUtils
 */
public class DbUtil {
	/**
	 * 获取数据库连接
	 *
	 * @return
	 * @throws SQLException
	 */
	public static Connection getOracleConnection( String url, String user, String passwd ) throws SQLException {
		Connection conn = null;
		try {
			Class.forName( "oracle.jdbc.driver.OracleDriver" );
			conn = DriverManager.getConnection( url, user, passwd );
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
			System.out.println( "加载数据库驱动失败！" );
		}
		return conn;
	}

	/**
	 * 获取数据库连接
	 *
	 * @return
	 * @throws SQLException
	 */
	public static Connection getMysqlConnection( String url, String user, String passwd ) throws SQLException {
		Connection conn = null;
		try {
			Class.forName( "com.mysql.jdbc.Driver" );
			conn = DriverManager.getConnection( url, user, passwd );
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
			System.out.println( "转载数据库驱动失败！" );
		}
		return conn;
	}

	/**
	 * SQL查询
	 *
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, String>> query( Connection conn, String sql ) throws SQLException {
		return query( conn, sql, null );
	}

	/**
	 * SQL查询
	 *
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, String>> query( Connection conn, String sql, List<Object> params ) throws SQLException {
		if ( conn == null ) {
			throw new SQLException( "Null connection" );
		}

		if ( sql == null ) {
			throw new SQLException( "Null SQL statement" );
		}

		List<Map<String, String>> result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement( sql );
			fillStatement( stmt, params );
			rs = stmt.executeQuery();

			result = new ArrayList<Map<String, String>>();

			ResultSetMetaData meta = rs.getMetaData();
			int columnCount = meta.getColumnCount();
			Map<String, String> data = null;
			Object colValue = null;
			String colName = null;
			while ( rs.next() ) {
				data = new HashMap<String, String>();
				for ( int i = 1; i <= columnCount; i++ ) {
					colName = meta.getColumnLabel( i );
					colValue = rs.getObject( i );
					data.put( colName, colValue == null ? null : colValue.toString() );
				}
				result.add( data );
			}
		} catch ( SQLException e ) {
			throw e;
		} finally {
			DbUtil.closeQuietly( conn, stmt, rs );
		}
		return result;
	}

	/**
	 * SQL更新
	 *
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static int update( Connection conn, String sql ) throws SQLException {
		return update( conn, sql, null );
	}

	/**
	 * SQL更新
	 *
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static int update( Connection conn, String sql, List<Object> params ) throws SQLException {
		if ( conn == null ) {
			throw new SQLException( "Null connection" );
		}
		if ( sql == null ) {
			throw new SQLException( "Null SQL statement" );
		}
		PreparedStatement stmt = null;
		int rows = 0;
		try {
			stmt = conn.prepareStatement( sql );
			fillStatement( stmt, params );
			 rows = stmt.executeUpdate();
		} catch ( SQLException e ) {
			throw e;
		} finally {
			DbUtil.closeQuietly( stmt );
		}
		return rows;
	}

	/**
	 * 批量更新
	 *
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static int batchUpdate( Connection conn, String sql, List<List<Object>> params ) throws SQLException {
		if ( conn == null ) {
			throw new SQLException( "Null connection" );
		}
		if ( sql == null ) {
			throw new SQLException( "Null SQL statement" );
		}
		boolean commitType = conn.getAutoCommit();
		conn.setAutoCommit( false );
		PreparedStatement stmt = null;
		int rows = 0;
		try {
			stmt = conn.prepareStatement( sql );
			for ( List<Object> param : params ) {
				fillStatement( stmt, param );
				stmt.addBatch();
			}
			 rows = stmt.executeBatch().length;
			conn.commit();
		} catch ( SQLException e ) {
			conn.rollback();
			throw e;
		} finally {
			DbUtil.closeQuietly( stmt );
			conn.setAutoCommit( commitType );
		}
		return rows;
	}

	/**
	 * 预处理参数替换
	 *
	 * @param stmt
	 * @param params
	 * @throws SQLException
	 */
	private static void fillStatement( PreparedStatement stmt, List<Object> params ) throws SQLException {
		if ( params == null ) {
			return;
		}
		Object param = null;
		for ( int i = 0, l = params.size(); i < l; i++ ) {
			param = params.get( i );
			if ( param != null ) {
				stmt.setObject( i + 1, param );
			} else {
				stmt.setNull( i + 1, Types.NULL );
			}
		}
	}

	/**
	 * Close a <code>Connection</code>, avoid closing if null.
	 *
	 * @param conn Connection to close.
	 * @throws SQLException if a database access error occurs
	 */
	public static void close( Connection conn ) throws SQLException {
		if ( conn != null ) {
			conn.close();
		}
	}

	/**
	 * Close a <code>ResultSet</code>, avoid closing if null.
	 *
	 * @param rs ResultSet to close.
	 * @throws SQLException if a database access error occurs
	 */
	public static void close( ResultSet rs ) throws SQLException {
		if ( rs != null ) {
			rs.close();
		}
	}

	/**
	 * Close a <code>Statement</code>, avoid closing if null.
	 *
	 * @param stmt Statement to close.
	 * @throws SQLException if a database access error occurs
	 */
	public static void close( Statement stmt ) throws SQLException {
		if ( stmt != null ) {
			stmt.close();
		}
	}

	/**
	 * Close a <code>Connection</code>, <code>Statement</code> and
	 * <code>ResultSet</code>.  Avoid closing if null and hide any
	 * SQLExceptions that occur.
	 *
	 * @param conn Connection to close.
	 * @param stmt Statement to close.
	 * @param rs ResultSet to close.
	 */
	public static void closeQuietly( Connection conn, Statement stmt, ResultSet rs ) {
		try {
			closeQuietly( rs );
		} finally {
			try {
				closeQuietly( stmt );
			} finally {
				closeQuietly( conn );
			}
		}
	}

	/**
	 * Close a <code>Connection</code>, avoid closing if null and hide
	 * any SQLExceptions that occur.
	 *
	 * @param conn Connection to close.
	 */
	public static void closeQuietly( Connection conn ) {
		try {
			close( conn );
		} catch ( SQLException e ) { // NOPMD
			// quiet
		}
	}

	/**
	 * Close a <code>ResultSet</code>, avoid closing if null and hide any
	 * SQLExceptions that occur.
	 *
	 * @param rs ResultSet to close.
	 */
	public static void closeQuietly( ResultSet rs ) {
		try {
			close( rs );
		} catch ( SQLException e ) { // NOPMD
			// quiet
		}
	}

	/**
	 * Close a <code>Statement</code>, avoid closing if null and hide
	 * any SQLExceptions that occur.
	 *
	 * @param stmt Statement to close.
	 */
	public static void closeQuietly( Statement stmt ) {
		try {
			close( stmt );
		} catch ( SQLException e ) { // NOPMD
			// quiet
		}
	}
}
