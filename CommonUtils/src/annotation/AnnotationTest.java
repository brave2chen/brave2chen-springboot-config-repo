package annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * message
 *
 * @author brave
 * @date 2017井6冥23耒
 */
public class AnnotationTest {
	public static void main(String[] args) throws Exception {
		AnnotationBean bean = new AnnotationBean();
		Class<?> clazz = bean.getClass();
		if (clazz.isAnnotationPresent(MyAnnotation.class)) {
			System.out.println(clazz.getName());
		}
		Annotation[] annotations = clazz.getAnnotations();
		for (Annotation an : annotations) {
			System.out.println(an.annotationType() == MyAnnotation.class);
		}
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			if(field.isAnnotationPresent(MyAnnotation.class)){
				System.out.println(field.getName());
				field.setAccessible(true);
				field.set(bean, "brave");
			}
		}
		
		Method[] declaredMethods = clazz.getDeclaredMethods();
		for (Method method : declaredMethods) {
			if(method.isAnnotationPresent(MyAnnotation.class)){
				Object invoke = method.invoke(bean, new Object[]{});
				System.out.println(invoke);
			}
		}
	}
}
