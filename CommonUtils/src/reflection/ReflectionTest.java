package reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import bean.Person;

/**
 * message
 *
 * @author brave
 * @date 2017年6月23日
 */
public class ReflectionTest {

	public static void main(String[] args) throws Exception {
		Class<?> clazz = Person.class;
		
		Field[] declaredFields = clazz.getDeclaredFields();
		Field[] fields = clazz.getFields();
		System.out.println(Arrays.toString(declaredFields));
		System.out.println(Arrays.toString(fields));

		Method[] methods = clazz.getMethods();
		Method[] declaredMethods = clazz.getDeclaredMethods();
		System.out.println(Arrays.toString(declaredMethods));
		System.out.println(Arrays.toString(methods));
		
		
		ArrayList<Integer> list = new ArrayList<Integer>();
        Method method = list.getClass().getMethod("add", Object.class);
        method.invoke(list, "Java反射机制实例。");
        System.out.println(list.get(0));
		
	}

}
