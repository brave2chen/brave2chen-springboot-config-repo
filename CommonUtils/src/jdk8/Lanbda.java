/**
 * 
 */
package jdk8;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * message
 *
 * @author 陈庆勇
 * @date 2016年11月2日
 */
public class Lanbda {

	/**
	 * message
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> list = Arrays.asList("A","B","C","D","E");
		Collections.sort(list, (s1, s2) -> s2.compareTo(s1));
		System.out.println(list);
	}
}
