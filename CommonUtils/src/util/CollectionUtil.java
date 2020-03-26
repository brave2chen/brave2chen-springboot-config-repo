package util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.collections.CollectionUtils;

/**
 * CollectionUtil
 */
public class CollectionUtil {
	public static void main( String[] args ) {
		List list1 = new ArrayList();
		List list2 = new ArrayList();
		list1.add( "a" );
		list1.add( "b" );
		list1.add( 3 );

		list2.add( 4 );
		list2.add( "c" );
		list2.add( "b" );
		list2.add( 5 );

		System.out.println( CollectionUtils.union( list2, list1 ) );

		arrayStack();
	}

	public static void arrayStack() {
		ArrayStack stack = new ArrayStack( 10 );//初始大小
		for ( int i = 0; i < 10; i++ ) {
			stack.push( i + 1 );
		}
		System.out.println( stack.peek() );
		System.out.println( stack.peek( 1 ) );
		System.out.println( stack.pop() );
		System.out.println( stack.remove() );
		System.out.println( stack );
	}
}
