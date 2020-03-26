package util;

import java.io.File;
import java.io.FileInputStream;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * CacheUtil
 */
public class CacheUtil {

	private static CacheManager manager = null;//CacheManager.create();

	private Cache cache = null;

	/**
	 * 默认缓存器
	 */
	public static CacheUtil getCache() {
		return new CacheUtil( "ehcache0" );
	}

	/**
	 * 单独配置一个缓存块，在ehcache.xml里需要配置
	 */
	public static CacheUtil getCache( String cacheName ) {
		return new CacheUtil( cacheName );
	}

	/**
	 * 缓存key-value
	 *
	 * @param key
	 * @param value
	 */
	public void put( String key, Object value ) {
		cache.put( new Element( key, value ) );
	}

	/**
	 * 获取缓存
	 *
	 * @param key
	 * @return
	 */
	public Object get( String key ) {
		Object resultVal = null;
		Element ele = cache.get( key );
		if ( ele != null ) {
			Object val = ele.getObjectValue();
			if ( val != null ) {
				resultVal = val;
			}
		}
		return resultVal;
	}

	/**
	 * 判断是否已有缓存
	 *
	 * @param key
	 * @return
	 */
	public boolean containsKey( final String key ) {
		return cache.isKeyInCache( key );
	}

	/**
	 * 清除缓存
	 *
	 * @param key
	 */
	public void remove( String key ) {
		if ( cache.isKeyInCache( key ) ) cache.remove( key );
	}

	/**
	 * 清除缓存器所有缓存
	 */
	public void removeAll() {
		cache.removeAll();
	}

	/**
	 * 新建一个缓存器
	 *
	 * @param cacheName
	 */
	private CacheUtil( String cacheName ) {
		initManager();
		cache = manager.getCache( cacheName );
		if ( cache == null ) {
			cache = new Cache( cacheName, 1000, false, false, 0, 0 );
			manager.addCache( cache );
		}
	}

	/**
	 * 初始化管理器
	 */
	private void initManager() {
		if ( manager == null ) {
			try {
				FileInputStream istream = new FileInputStream( System.getProperty( "user.dir" ) + File.separator
						+ "conf" + File.separator + "ehcache.xml" );
				manager = new CacheManager( istream );
			} catch ( Exception ex ) {
				ex.printStackTrace();
			}
		}
	}
}
