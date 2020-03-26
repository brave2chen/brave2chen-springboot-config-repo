package annotation;

/**
 * message
 *
 * @author brave
 * @date 2017井6冥23耒
 */
@MyAnnotation
public class AnnotationBean {
	@MyAnnotation
	private String name;
	
	@MyAnnotation
	public void printName(){
		System.out.println(this.name);
	}
}
