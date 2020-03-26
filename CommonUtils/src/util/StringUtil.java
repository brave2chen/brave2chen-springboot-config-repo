package util;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

/**
 * org.apache.commons.lang3.StringUtils 腔妗蚚Demo
 *
 * @author bravechen
 * @date 2015-11-24
 */
public class StringUtil {

	public static void main( String[] args ) {
		System.out.println( StringUtils.isEmpty( "" ) );
		System.out.println( StringUtils.isNotEmpty( " " ) );
		System.out.println( StringUtils.isBlank( " " ) );
		System.out.println( StringUtils.isNotBlank( "\b \t \n \f \r" ) );

		System.out.println( StringUtils.trim( "\b \t \n \f \r" ) );
		System.out.println( StringUtils.trimToNull( "\b \t \n \f \r" ) );
		System.out.println( StringUtils.trimToEmpty( "\b \t \n \f \r" ) );

		System.out.println( StringUtils.strip( "a \t \n \f \r" ) );
		System.out.println( StringUtils.stripToNull( "\t b \n \f \r" ) );
		System.out.println( StringUtils.stripToEmpty( "\b \t \n \f \r" ) );

		System.out.println( StringUtils.contains( "cccacc", "a" ) );
		System.out.println( StringUtils.containsOnly( "abcba", new char[]{ 'a', 'b', 'c' } ) );
		System.out.println( StringUtils.containsNone( "efg", new char[]{ 'a', 'b', 'c' } ) );
		System.out.println( StringUtils.containsAny( "efag", new char[]{ 'a', 'b', 'c' } ) );

		System.out.println( StringUtils.join( new String[]{ "a", "b", "c" }, "," ) );

		System.out.println( StringUtils.countMatches( "abababababa", "a" ) );

		System.out.println( Arrays.toString( StringUtils.split( "abababababa", 'b' ) ) );
	}
}
