package genericity;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型
 *
 * @author 陈庆勇
 * @date 2016年10月8日
 */
public class Genericity<E> {
	
	public E get(E e){
		return e;
	}
	
	public <V> V getV(V v){
		return v;
	}
	
	public <T> T getT(ArrayList<T> v){
		return v.get( 0 );
	}
	
	public static void main( String[] args ) {
		Object object = new Object();
		Object object2 = new Genericity<>().get( object );
		System.out.println( object2 == object );
		Object object3 = new Genericity<>().getV( object );
		System.out.println( object3 == object );
		new Genericity<>().getT( new ArrayList<List>() );
		System.out.println( object3 == object );
	}
}
