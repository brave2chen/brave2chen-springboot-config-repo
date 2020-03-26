package util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * HttpClientUtil
 */
public class HttpClientUtil {
	/**
	 * get请求
	 * @param url 地址
	 * @return 返回数据
	 * @throws Exception 
	 */
	public static String requestGet( String url ) throws Exception {
		return requestGet( url, 30000, 30000 );
	}

	/**
	 * post请求
	 * @param url 地址
	 * @param data 参数
	 * @return 返回数据
	 * @throws Exception 
	 */
	public static String requestPost( String url, String data ) throws Exception {
		return requestPost( url, data, 30000, 30000 );
	}

	/**
	 * get请求
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String requestGet( String url, Integer connectionTimeout, Integer readTimeout ) throws Exception {
		String responseBody = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet( url );
			httpget.setConfig( RequestConfig.custom().setConnectionRequestTimeout( connectionTimeout )
					.setConnectTimeout( readTimeout ).setSocketTimeout( readTimeout ).build() );
			responseBody = httpclient.execute( httpget, new ResponseHandler<String>() {
				public String handleResponse( HttpResponse httpresponse ) throws ClientProtocolException, IOException {
					int status = httpresponse.getStatusLine().getStatusCode();
					if ( status >= 200 && status < 300 ) {
						HttpEntity entity = httpresponse.getEntity();
						return entity != null ? EntityUtils.toString( entity ) : null;
					} else {
						throw new ClientProtocolException( "Unexpected response status: " + status );
					}
				}
			} );
		} finally {
			httpclient.close();
		}
		return responseBody;
	}

	/**
	 * post请求
	 * @throws Exception 
	 */
	public static String requestPost( String url, String data, Integer connectionTimeout, Integer readTimeout )
			throws Exception {
		String responseBody = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost( url );
			//参数处理
			if ( data != null && !"".equals( data.trim() ) ) {
				StringEntity entity = new StringEntity( data, "UTF-8" );
				entity.setContentType( "application/x-www-form-urlencoded" );
				httppost.setEntity( entity );
			}
			CloseableHttpResponse response = httpclient.execute( httppost );
			try {
				int status = response.getStatusLine().getStatusCode();
				if ( status >= 200 && status < 300 ) {
					HttpEntity entity = response.getEntity();
					responseBody = entity != null ? EntityUtils.toString( entity ) : null;
				} else {
					throw new ClientProtocolException( "Unexpected response status: " + status );
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return responseBody;
	}
}
