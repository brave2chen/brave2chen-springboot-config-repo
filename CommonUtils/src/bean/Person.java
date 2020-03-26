package bean;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * PersonÀà
 */
public class Person {
	public static final String VERSION = "1.0";
	private String name;
	private int sex;
	private int age;
	private Date birth = new Date();
	private List<String> hands;
	private Map<Integer, Float> ageWeigthMap;

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * @return the sex
	 */
	public int getSex() {
		return this.sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex( int sex ) {
		this.sex = sex;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return this.age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge( int age ) {
		this.age = age;
	}

	/**
	 * @return the birth
	 */
	public Date getBirth() {
		return this.birth;
	}

	/**
	 * @param birth the birth to set
	 */
	public void setBirth( Date birth ) {
		this.birth = birth;
	}

	/**
	 * @return the hands
	 */
	public List<String> getHands() {
		return this.hands;
	}

	/**
	 * @param hands the hands to set
	 */
	public void setHands( List<String> hands ) {
		this.hands = hands;
	}

	/**
	 * @return the ageWeigthMap
	 */
	public Map<Integer, Float> getAgeWeigthMap() {
		return this.ageWeigthMap;
	}

	/**
	 * @param ageWeigthMap the ageWeigthMap to set
	 */
	public void setAgeWeigthMap( Map<Integer, Float> ageWeigthMap ) {
		this.ageWeigthMap = ageWeigthMap;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "name:" + this.getName() + "\nsex:" + this.getSex() + "\nage:" + this.getAge() + "\n"
				+ DateFormatUtils.format( this.getBirth(), "yyyy-MM-dd HH:mm:ss" ) + "\nhands:" + this.getHands()
				+ "\nageWegthMap:" + this.getAgeWeigthMap();
	}

	public static void saySomething( Object something ) {
		System.out.println( something.toString() );
	}

}
