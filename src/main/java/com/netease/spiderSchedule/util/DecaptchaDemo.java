package com.netease.spiderSchedule.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 验证码识别接口调用演示 Created by yangweiqiang on 17-1-4.
 * 
 * 
 */
public class DecaptchaDemo {
	private static Logger log = LoggerFactory.getLogger(DecaptchaDemo.class);
	private static final long MAX_DOWNLOAD_SIZE = 20L * 1024 * 1024;// 20M
	private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";

	/**
	 * 读取网络图像为byte的格式，后面设置下载时间
	 *
	 * @param webUrl
	 *            要下载的web图像的url
	 * @return byte数组
	 */
	private static Map<String, Object> download(String webUrl, int timeOut) {
		return download(webUrl, timeOut, null, null);
	}

	private static Map<String, Object> download(String webUrl, int timeOut, String proxyip, String port) {
		Proxy proxy = null;
		if (!StringUtils.isBlank(proxyip)) {
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyip, Integer.parseInt(port)));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		HttpURLConnection connection = null;
		long start = System.currentTimeMillis();
		try {
			URL url = new URL(webUrl);
			if (!StringUtils.isBlank(proxyip)) {
				connection = (HttpURLConnection) url.openConnection(proxy);
			} else {
				connection = (HttpURLConnection) url.openConnection();
			}
			connection.setConnectTimeout(timeOut);
			connection.setReadTimeout(timeOut);
			connection.setRequestProperty("User-Agent", USER_AGENT);
			int len = connection.getContentLength();
			if (len >= MAX_DOWNLOAD_SIZE) {
				log.error(String.format("文件太大跳过下载, size=%s, url=%s", len, webUrl));
				return null;
			}
			String cookieStr = connection.getHeaderField("Set-cookie");
			System.out.println(cookieStr);
			cookieStr = cookieStr.substring(0, cookieStr.indexOf(";"));
			System.out.println(cookieStr);
			map.put("cookie", cookieStr);
			if (len == -1) {// 长度为-1的情况特殊处理，http://chuantu.biz/t5/35/1474453734x3340469630.jpg
							// 返回文件长度为-1
				try (InputStream in = connection.getInputStream()) {// TODO:这里没法处理文件过大和超时的问题
					map.put("arr", IOUtils.toByteArray(connection.getInputStream()));
				}
			} else {
				byte[] data = new byte[len];
				byte[] buffer = new byte[4096 * 2];// 8k读取缓冲，读取nos文件测试发现比4k稍微快一点
				int count = 0, sum = 0;
				try (InputStream in = connection.getInputStream()) {
					while ((count = in.read(buffer)) > 0) {
						long elapse = System.currentTimeMillis() - start;
						if (elapse >= timeOut) {// 每读取一次数据，检测一次超时
							log.error(String.format("read file timeout, url=%s, elapse=%sms", webUrl, elapse));
							data = null;
							break;
						}
						System.arraycopy(buffer, 0, data, sum, count);
						sum += count;
					}
				}
				map.put("arr", data);
			}
		} catch (Exception e) {
			log.error("图片下载失败, url=" + webUrl);
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return map;
	}

	/**
	 * 发送post请求
	 *
	 * @param httpClient
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param encoding
	 *            编码
	 * @return
	 */
	public static String sendPost(HttpClient httpClient, String url, Map<String, String> params, Charset encoding) {
		String resp = "";
		HttpPost httpPost = new HttpPost(url);
		if (params != null && params.size() > 0) {
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			Iterator<Map.Entry<String, String>> itr = params.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, String> entry = itr.next();
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(formParams, encoding);
			httpPost.setEntity(postEntity);
		}
		CloseableHttpResponse response = null;
		try {
			response = (CloseableHttpResponse) httpClient.execute(httpPost);
			Map<String, Object> map = new HashMap<String, Object>();
			resp = EntityUtils.toString(response.getEntity(), encoding);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// log
					e.printStackTrace();
				}
			}
		}
		return resp;
	}

	public static void getResponseMap(Map<String, Object> map) throws IOException {

	}

	/**
	 * <dependency> <groupId>org.apache.httpcomponents</groupId>
	 * <artifactId>httpclient</artifactId> <version>4.5</version> </dependency>
	 * <p>
	 * <p>
	 * <dependency> <groupId>commons-io</groupId>
	 * <artifactId>commons-io</artifactId> <version>2.4</version> </dependency>
	 *
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		Map<String, Object> resp = getIdentifyCode("http://weixin.sogou.com/antispider/util/seccode.php?tc=1501212872");
		log.info("resp = " + resp);
	}
	public static Map<String, Object> getIdentifyCode(String url) {
		return getIdentifyCode(url, null, null);
	}
	public static Map<String, Object> getIdentifyCode(String url, String ip , String port) {
		HttpClient httpClient = HttpClients.createDefault();
		final String api = "http://decaptcha.nis.netease.com/api/decode";

		// 1. 将验证码图片下载成byte数组
		Map<String, Object> map = download(url, 5000,ip,port);
		byte[] imageBytes = (byte[]) map.get("arr");
//		writeImageToDisk(imageBytes, url.substring(url.lastIndexOf("cert=") + 5, url.length()) + ".jpg");
		// 2. 构造验证码识别请求
		Map<String, String> params = new HashMap<>();
		params.put("key", "gAV3WpvZZAGcMdRKnAedQyWeVQrS7UXa");
		params.put("source", "sogou4"); // 来源, tencent4表示腾讯4位验证码,
										// taobao6表示淘宝6位验证码
		params.put("type", "2"); // 2 表示是base64数据
		params.put("data", Base64.getEncoder().encodeToString(imageBytes)); // 将图片byte数组编码成base64用于http传输

		// 3. 发送识别请求获得识别结果
		map.put("resp", sendPost(httpClient, api, params, Charset.forName("utf8")));
		return map;
	}

	public static void writeImageToDisk(byte[] img, String fileName) {
		FileOutputStream fops = null;
		try {
			System.out.println(fileName);
			File file = new File(fileName);
			System.out.println(file.getAbsolutePath());
			// if (!file.getParentFile().exists()) {
			// boolean mkdirs = file.getParentFile().mkdirs();
			// }
			fops = new FileOutputStream(file);
			fops.write(img);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fops != null) {
				try {
					fops.flush();
					fops.close();
					System.out.println("dowm load the image into local");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
