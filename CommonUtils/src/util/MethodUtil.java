package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang3.time.DateUtils;

import bean.Person;

/**
 * org.apache.commons.beanutils.MethodUtils 腔妗蚚Demo
 * org.apache.commons.lang3.reflect.MethodUtils 腔妗蚚Demo
 *
 * @author bravechen
 * @date 2015-11-26
 */
public class MethodUtil {
	public static void main( String[] args ) throws Exception {
		Person person = new Person();
		person.setName( "bravechen" );
		person.setAge( 25 );
		person.setSex( 1 );
		person.setBirth( DateUtils.parseDate( "19900705", "yyyyMMdd" ) );
		List<String> hands = new ArrayList<String>();
		hands.add( "THIS IS THE LEFT HAND" );
		hands.add( "THIS IS THE RIGTH HAND" );
		person.setHands( hands );
		Map<Integer, Float> ageWeightMap = new HashMap<Integer, Float>();
		ageWeightMap.put( 24, 43.5F );
		ageWeightMap.put( 25, 55.2F );
		person.setAgeWeigthMap( ageWeightMap );
		Object obj = person;
		
		System.out.println( MethodUtils.invokeMethod( obj, "getName", new Object[]{} ) );
		MethodUtils.invokeMethod( obj, "setName", new String[]{ "newName" } );
		System.out.println( MethodUtils.invokeMethod( obj, "toString", new Object[]{} ) );
		MethodUtils.invokeStaticMethod( obj.getClass(), "saySomething", new Object[]{ obj } );
		System.out.println( org.apache.commons.lang3.reflect.MethodUtils.invokeMethod( obj, "getName", new Object[]{} ) );
		org.apache.commons.lang3.reflect.MethodUtils.invokeMethod( obj, "setName", new Object[]{ "bravechen" } );
		System.out.println( org.apache.commons.lang3.reflect.MethodUtils.invokeMethod( obj, "toString", new Object[]{} ) );
		org.apache.commons.lang3.reflect.MethodUtils.invokeStaticMethod( obj.getClass(), "saySomething", new Object[]{ obj } );

		
		
		
	}
}
