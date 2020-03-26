package util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DoubleConverter;

/**
 * ConverterUtil
 */
public class ConverterUtil {
	@SuppressWarnings("deprecation")
	public static void main( String[] args ) {
		Map map = new HashMap();
		map.put( "a", "a" );
		map.put( "b", "1.2aa" );
		map.put( "c", 8 );
		map.put( "d", test.Color.RED );

		System.out.println( ConvertUtils.convert( map.get( "a" ) ) );
		System.out.println( ConvertUtils.convert( map.get( "b" ) ) );
		System.out.println( ConvertUtils.convert( map.get( "c" ) ) );
		System.out.println( ConvertUtils.convert( map.get( "d" ) ) );
		
		ConvertUtils.setDefaultDouble( -1D );
		System.out.println( ConvertUtils.convert( map.get( "b" ), Double.class ) );

		System.out.println( "-------------------------------------------------" );
		System.out.println( new DoubleConverter( null ).convert( Double.class, (Double)null ) );
		System.out.println( new DoubleConverter().convert( Double.class, "2.1" ) );
		System.out.println( new DoubleConverter( null ).convert( Double.class, "aa" ) );
		System.out.println( new DoubleConverter().convert( Double.class, 2 ) );
		System.out.println( new DoubleConverter().convert( Double.class, 3.1 ) );
	}
}
