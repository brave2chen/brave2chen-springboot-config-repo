package util;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/** 
 * JsonUtil
 */
public class JsonUtil {
	/** 日期时间格式 */
	private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 对象转json串
	 * @param obj 任意对象对型
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static String getBasetJsonData( Object obj ) throws JsonGenerationException, JsonMappingException,
			IOException {
		StringWriter writer = new StringWriter();
		if ( obj != null ) {
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat sdf = new SimpleDateFormat( YYYY_MM_DD_HH_MM_SS );
			mapper.setDateFormat( sdf );
			mapper.writeValue( writer, obj );
		}
		return writer.toString();
	}

	/**
	 * json串转指定对象
	 * @param json
	 * @param c
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static Object getObjectByJson( String json, Class c ) throws JsonParseException, JsonMappingException,
			IOException {
		Object obj = null;
		ObjectMapper mapper = new ObjectMapper();
		SimpleDateFormat sdf = new SimpleDateFormat( YYYY_MM_DD_HH_MM_SS );
		mapper.setDateFormat( sdf );
		obj = mapper.readValue( json, c );
		return obj;
	}

	/**
	 * json数组文本串转集合
	 * @param json
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static List getListByJsonArray( String json ) throws JsonParseException, JsonMappingException, IOException {
		return (List)getObjectByJson( json, List.class );
	}

	/**
	 * 将JSON字符串转换为Map实现类对象
	 * @param json json串
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static Map getMapByJsonString( String json ) throws JsonParseException, JsonMappingException, IOException {
		return (HashMap)getObjectByJson( json, HashMap.class );
	}
}