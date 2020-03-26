package util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;

import bean.Person;

/**
 * BeanUtil
 */
public class BeanUtil {
	public static void main( String[] args ) {
		LogUtil.loadLog4jConfig( System.getProperty( "user.dir" ) + File.separator + "conf" + File.separator + "log4j.properties" );
		Person person = new Person();
		Object obj = person;
		try {
			BeanUtils.setProperty( obj, "name", "BeanUtils" );
			System.out.println( BeanUtils.getProperty( obj, "name" ) );
			System.out.println( BeanUtils.describe( obj ) );

			Map map = new HashMap();
			map.put( "name", "populateTest" );
			map.put( "age", "25" );
			map.put( "birth", "2015-11-25 12:30:45" );
			ConvertUtils.register( new DateLocaleConverter() {
				public Object convert( Class type, Object value ) {
					return this.convert( type, value, "yyyy-MM-dd HH:mm:ss" );
				}
			}, Date.class );
			BeanUtils.populate( person, map );

			System.out.println( person );
			System.out.println( BeanUtils.getProperty( person, "birth" ) );
		} catch ( IllegalAccessException e ) {
			e.printStackTrace();
		} catch ( InvocationTargetException e ) {
			e.printStackTrace();
		} catch ( NoSuchMethodException e ) {
			e.printStackTrace();
		}
	}
}
