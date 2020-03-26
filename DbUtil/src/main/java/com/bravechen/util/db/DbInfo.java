package com.bravechen.util.db;

import java.sql.Connection;
import java.sql.SQLException;

public class DbInfo implements Comparable<DbInfo>{
	private String id;
	private String url;
	private String user;
	private String pwd;
	private DbType dbType;
	private boolean enable = false;

	/**
	 * @param id
	 * @param url
	 */
	public DbInfo(String id, String url) {
		if (url.indexOf(":oracle:") != -1) {
			this.dbType = DbType.ORACLE;
			int i = url.indexOf("/");
			this.user = url.substring(url.lastIndexOf(":", i) + 1, i);
			this.pwd = url.substring(i + 1, url.indexOf("@", i));
		} else {
			this.dbType = DbType.MYSQL;
			int i = url.indexOf("user=");
			this.user = url.substring(i+5, url.indexOf("&", i+5));
			i = url.indexOf("password=");
			this.pwd = url.substring(i+9, url.indexOf("&", i+9));
			
		}
		this.id = id;
		this.url = url;
	}

	/**
	 * @return the dbType
	 */
	public DbType getDbType() {
		return this.dbType;
	}

	/**
	 * @param dbType
	 *            the dbType to set
	 */
	public void setDbType(DbType dbType) {
		this.dbType = dbType;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return this.pwd;
	}

	/**
	 * @param pwd
	 *            the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @return the enable
	 */
	public boolean isEnable() {
		return this.enable;
	}

	
	/* 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DbInfo o) {
		return this.getId().compareTo(o.getId());
	}

	/**
	 * 获取连接
	 * @return
	 */
	public Connection getConnection() {
		try {
			if(DbType.ORACLE.equals(this.getDbType())) {
				return DbUtil.getOracleConnection(this.url, this.user, this.pwd);
			}else {
				return DbUtil.getMysqlConnection(this.url, this.user, this.pwd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}