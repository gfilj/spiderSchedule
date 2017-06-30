package com.netease.spiderSchedule.timer;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.spiderSchedule.util.MD5Util;

/**
 * 二进制文件上传
 * 
 * @author handongming
 *
 */
public class CrawlUtil {
	
    private static final Logger logger = LoggerFactory.getLogger(CrawlUtil.class);
    
    Pattern SUFFIX_PATTERN = Pattern.compile("", Pattern.CASE_INSENSITIVE);
    
    public static String makeDataTag(String url) {
        return "##spider_data#" + MD5Util.calcMD5(url) + "###";
    }
    
    public static String makeSrcTag(String url) {
        return "##spider_src#" + MD5Util.calcMD5(url) + "###";
    }
    
    /**
     * 获取uri的文件名后缀
     * @param url
     * @return
     */
    public static String getSuffixFromUri(String url) {
        if(StringUtils.isBlank(url)) {
            return null;
        }
        try {
            UURI uri = UURIFactory.getInstance(url);
            String name = StringUtils.trimToEmpty(uri.getEscapedName());
            int index = name.lastIndexOf('.');
            return index != -1 ? name.substring(index + 1).toLowerCase() : null;
        } catch (Exception e) {
            logger.error("url= " + url , e);
        }
        return null;
    }
    public static String getWeiXinSuffixFromUri(String url) {
    	if(StringUtils.isBlank(url)) {
    		return null;
    	}
    	try {
    		String name = StringUtils.trimToEmpty(url);
    		int index = name.lastIndexOf("wx_fmt=");
    		return index != -1 ? name.substring(index + 7).toLowerCase() : null;
    	} catch (Exception e) {
    		logger.error("url= " + url , e);
    	}
    	return null;
    }
    
    /**
     * 根据二进制资源的uri生成唯一标识
     * @param url
     * @return
     */
    public static String makeResourceKey(String url) {
        url = StringUtils.trimToNull(url);
        if(url == null) {
            return null;
        }
        UURI uri;
        try {
            uri = UURIFactory.getInstance(url);
            url = uri.toCustomString();
        } catch (URIException e) {
            logger.error("url= " + url , e);
        }      
        String suffix = CrawlUtil.getSuffixFromUri(url);
        String key = MD5Util.calcMD5(url);
        if(suffix != null) {
            key = key + "." + suffix;
        }
        return key;
    }
    public static String makeWeiXinResourceKey(String url,String suffix) {
    	url = StringUtils.trimToNull(url);
    	if(url == null) {
    		return null;
    	}
    	UURI uri;
    	try {
    		uri = UURIFactory.getInstance(url);
    		url = uri.toCustomString();
    	} catch (URIException e) {
    		logger.error("url= " + url , e);
    	}      
    	String key = MD5Util.calcMD5(url);
    	if(suffix != null) {
    		key = key + "." + suffix;
    	}
    	return key;
    }
    
    //根据Content-Type 获取图片类型
    public static String getFileType(String url){
    	BufferedInputStream bis = null;  
		HttpURLConnection urlconnection = null;  
		URL urlinstance = null;          
		try {
			urlinstance = new URL(url);  
			urlconnection = (HttpURLConnection) urlinstance.openConnection();  
			urlconnection.connect();  
			bis = new BufferedInputStream(urlconnection.getInputStream());  
			String fileType=HttpURLConnection.guessContentTypeFromStream(bis);
			if(!StringUtils.isEmpty(fileType)){
				int index = fileType.lastIndexOf("/");
	    		return index != -1 ? fileType.substring(index + 1).toLowerCase() : null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
}
