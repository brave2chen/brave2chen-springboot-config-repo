package com.bravechen.util.db;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DbConfig {
	private static Properties config = new Properties();
	
	private static Map<String, DbInfo> dbInfos = new HashMap<String, DbInfo>();
	
	static {
		try {
			FileReader in = new FileReader(new File("conf/dbConfig.properties"));
			config.load(in);
			in.close();
			
			for (Map.Entry<Object, Object> entry : config.entrySet()) {
				String id = entry.getKey().toString();
				String url = entry.getValue().toString();
				DbInfo info = new DbInfo(id, url);
				
				dbInfos.put(id, info);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection(String id) {
		return dbInfos.get(id).getConnection();
	}
}
