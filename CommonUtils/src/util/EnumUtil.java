package util;

import org.apache.commons.beanutils.MethodUtils;

/**
 * 枚举校验和获取工具类
 *
 * @author 陈庆勇
 * @date 2016年9月9日
 */
public class EnumUtil {

	/**
	 * 校验参数value是否为enumClass枚举的其中一个枚举值，该枚举必须提供一个getValue的方法
	 *
	 * @param enumClass
	 * @param value 
	 * @return
	 */
	public static <E extends Enum<E>> boolean isValidEnum( Class<E> enumClass, int value ) {
		return getEnum( enumClass, value ) != null;
	}

	/**
	 * 获取enumClass枚举里，枚举值等于参数value的枚举，该枚举必须提供一个getValue的方法
	 * getValue方法不存在或枚举值不存在，均返回null
	 * @param enumClass
	 * @param value
	 * @return
	 */
	public static <E extends Enum<E>> E getEnum( Class<E> enumClass, int value ) {
		return getEnum( enumClass, value, "getValue" );
	}

	/**
	 * 获取enumClass枚举里，枚举值equals参数value的枚举，该枚举必须提供一个参数method的方法，
	 * 参数method方法不存在或枚举值不存在，均返回null
	 * 
	 * @param enumClass
	 * @param value
	 * @param method
	 * @return
	 */
	public static <E extends Enum<E>> E getEnum( Class<E> enumClass, Object value, String method ) {
		E[] enumList = enumClass.getEnumConstants();
		for ( E e : enumList ) {
			try {
				if ( MethodUtils.invokeMethod( e, method, new Object[]{} ).equals( value ) ) {
					return e;
				}
			} catch ( Exception ex ) {
				ex.printStackTrace();
			}
		}
		return null;
	}

}
