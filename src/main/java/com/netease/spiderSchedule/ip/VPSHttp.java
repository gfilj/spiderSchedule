package com.netease.spiderSchedule.ip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class VPSHttp {

	private RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(1000)
            .setConnectTimeout(1000)
            .setConnectionRequestTimeout(1000)
            .build();
	
	private static VPSHttp instance = new VPSHttp();
	private VPSHttp(){}
	public static VPSHttp getInstance(){
		return instance;
	}
	
	/**
	 * 发送 post请求
	 * @param httpUrl 地址
	 * @param maps 参数
	 */
	public String sendHttpPost(String httpUrl, Map<String, String> maps) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost  
		// 创建参数队列  
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}
	
	private String sendHttpPost(HttpPost httpPost) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpPost.setConfig(requestConfig);
			httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			System.out.println(" Exception"+e.toString());  
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
	
	/**
	 * 发送get请求
	 * @param url
	 * @param proxyip
	 * @param port
	 * @param referer
	 * @return
	 * @throws Exception
	 */
	public String sendHttpGet(String url,String proxyip,String port,String referer) throws Exception{
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			HttpHost proxy = new HttpHost(proxyip, Integer.parseInt(port));
			httpClient = HttpClientBuilder.create().setProxy(proxy).build(); //默认重试3次
			// 依次是代理地址，代理端口号，协议类型  
			httpGet.setConfig(requestConfig);
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
			httpGet.setHeader("Host", "weixin.sogou.com");
			if(referer!=null){
				httpGet.setHeader("Referer", referer);
			}
			response = httpClient.execute(httpGet);
			if(response.getStatusLine().getStatusCode() != 200){
				return "error";
			}else{
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			httpGet.abort();  
			throw e;
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
				if(httpGet!=null){
					httpGet.abort();
				}
				
			} catch (IOException e) {
				throw e;
			}
		}
	}
	
	public String sendHttpGet(String url,String referer) throws Exception{
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
//			HttpHost proxy = new HttpHost(proxyip, Integer.parseInt(port));
			httpClient = HttpClientBuilder.create().build(); //默认重试3次
			// 依次是代理地址，代理端口号，协议类型  
			httpGet.setConfig(requestConfig);
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
			httpGet.setHeader("Host", "weixin.sogou.com");
			if(referer!=null){
				httpGet.setHeader("Referer", referer);
			}
			response = httpClient.execute(httpGet);
			if(response.getStatusLine().getStatusCode() != 200){
				return "error";
			}else{
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			httpGet.abort();  
			throw e;
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
				if(httpGet!=null){
					httpGet.abort();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		
	}
	
}
