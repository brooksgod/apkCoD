package com.wangvsa.apkCoD;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * apk capacity of downloading
 * @author wangchen
 *
 */
public class ApkCoD {
	public static void main(String[] args){
		// 读配置文件获得网站名和url
		ReadConfig rc = new ReadConfig("urls.conf");
		for(int i=0;i<rc.getSize();i++){
			String webName = rc.getName(i);
			String webUrl = rc.getUrl(i);
			System.out.print(i+1+"."+webName+",");
			/* 正则表达式
			 * regx1对机锋、应用汇、安智、多多、91、木蚂蚁、安桌有效
			 * regx2对N多有效 
			 */
			String regx1 = "(下载(次数|量)?(:|：)?(小于|大于)?(<[^>]*>)*\\d+)";	
			String regx2 = "(\\d+次下载)";								
			// (?:expression) 匹配expression不给此组分号
			Pattern pattern = Pattern.compile("(?:.*"+regx1+".*)|(.*"+regx2+".*)");
			HttpClient client = new HttpClient();
			GetMethod getMethod = new GetMethod(webUrl);
			// 解决cookie reject问题,不大明白。。。
			getMethod.getParams().setParameter("http.protocol.cookie-policy",CookiePolicy.BROWSER_COMPATIBILITY);
			try {
				client.executeMethod(getMethod);
				InputStream is = getMethod.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
			    String content = null;
			    while((content=br.readLine())!=null){
			    	Matcher matcher = pattern.matcher(content);
			    	if(matcher.matches()){
			    		String sub = matcher.group(1);
			    		if(sub==null)
			    			sub = matcher.group();
			    		// 处理并输出
			    		System.out.println(parseContent(sub));
				    	break;
			    	}// 爱桌网特别处理
			    	else if(content.trim().matches("<p>\\d+</p>")){
			    		System.out.println("下载量:"+parseContent(content));
			    	}
			    }
			} catch (Exception e) {
				e.printStackTrace();
			}
			getMethod.releaseConnection();
		}
	}
	
	public static String parseContent(String content){
		// 清除所有标签
		String info = content.replaceAll("<[^>]*>", "");
		return info.trim();
	}
}