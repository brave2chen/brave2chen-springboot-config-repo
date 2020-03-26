package util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * org.apache.commons.lang3.time.DateUtils 腔妗蚚Demo
 * org.apache.commons.lang3.time.DateFormatUtils 腔妗蚚Demo
 */
public class DateUtil {
	@SuppressWarnings( "deprecation" )
	public static void main( String[] args ) throws Exception {
		String sDate = "2015-11-16 12:29:30";

		Date date = DateUtils.parseDate( sDate, "yyyy-MM-dd HH:mm:ss" );
		System.out.println( date.toLocaleString() );
		System.out.println( DateUtils.truncate( date, Calendar.HOUR ).toLocaleString() );
		System.out.println( DateUtils.addDays( date, -6 ).toLocaleString() );
		System.out.println( DateUtils.setMonths( date, 10 ).toLocaleString() );
		System.out.println( DateUtils.round( date, Calendar.MONTH ).toLocaleString() );
		System.out.println( DateUtils.round( date, Calendar.DAY_OF_MONTH ).toLocaleString() );
		System.out.println( DateUtils.round( date, Calendar.MINUTE ).toLocaleString() );
		System.out.println( DateUtils.round( date, Calendar.HOUR ).toLocaleString() );
		System.out.println( DateUtils.ceiling( date, Calendar.HOUR ).toLocaleString() );
		System.out.println( DateUtils.isSameDay( date, DateUtils.addHours( date, 10 ) ) );
		System.out.println( DateUtils.getFragmentInDays( date, Calendar.YEAR ) );

		System.out.println( DateFormatUtils.ISO_DATETIME_FORMAT.format( date ) );
		System.out.println( DateFormatUtils.format( date, "y-M-d" ) );
		System.out.println( DateFormatUtils.format( date.getTime(), "HH:mm:ss" ) );

	}
}
