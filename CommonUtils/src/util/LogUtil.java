package util;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;

/**
 * LogUtil
 *
 * @author 陈庆勇
 * @date 2016-2-26
 */
public class LogUtil {
	/**日志输出操作对象*/
	private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LogUtil.class);

	/**日志输出操作对象*/
	public static void loadLog4jConfig( String path ) {
		PropertyConfigurator.configure( path );
	}

	public static void main( String[] args ) {
		loadLog4jConfig( System.getProperty( "user.dir" ) + File.separator + "conf" + File.separator + "log4j.properties" );
		logger.info( "日志测试" );
	}

}
