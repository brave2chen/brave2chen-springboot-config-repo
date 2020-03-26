package com.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigUtil {

	private static Properties properties;
	static {
		properties = new Properties();
		try {
			String path = ConfigUtil.class.getClassLoader().getResource( "" ).getPath();
			File file = new File( path.replace( "/bin", "" ), "conf.properties" );
			properties.load( new FileReader( file ) );
			System.out.println( properties );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	public static String get( String key ) {
		return properties.getProperty( key );
	}

	public static void main( String[] args ) {
		System.out.println( get( "isProxy" ) );
	}
}
