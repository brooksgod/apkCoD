package com.wangvsa.apkCoD;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			System.out.println(i+1+"."+webName+",");
			// 正则表达式
			// regx1对机锋、应用汇、安智、多多、91、木蚂蚁有效
			String regx1 = ".*下载(次数|量|)?(:|：)?(小于|大于)?(<[^>]*>)*\\d+.*";	
			String regx2 = ".*\\d+次下载.*";								// N多
			String regx3 = ".*<label id=\"ctl00_AndroidMaster_Content_Apk_Download\">\\d+.*"; // 安桌
			Pattern pattern = Pattern.compile("("+regx1+")|("+regx2+")");
			try {
				URL url = new URL(webUrl);
				HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			    String content = null;
			    while((content=br.readLine())!=null){
			    	Matcher matcher = pattern.matcher(content);
		    		//System.out.println(content);
			    	if(matcher.matches()){
			    		//String info = parseContent(content);
			    		String info = parseContentSpec(content);
				    	System.out.println(info);
				    	break;
			    	}// 安桌网
			    	else if(content.matches(regx3)){
			    		String info = parseContentSpec(content);
				    	System.out.println(info);
			    	}
			    }
			    conn.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static String parseContentSpec(String content){
		String info = content.replaceAll("<[^>]*>", "");
		return info.trim();
	}
	public static String parseContent(String content){
		/* 先找到“下载”左边的第一个tag叫做startTag
		 * 再找到和startTag对应的叫做endTag
		 * 截取startTag和endTag之间的内容再将其中的标签都去掉
		 */
		String info = "";
		
		int i = content.indexOf('下');
		int startTagLeft = i,startTagRight = i;
		for(;i>0;i--){
			char c = content.charAt(i);
			if(c=='>')
				startTagRight = i;
			else if(c=='<'){
				startTagLeft = i+1;
				break;
			}
		}
		// starTag长度，如<span class="xxx">标签长度为4
		int startTagLength = startTagRight - startTagLeft;
		for(i=startTagLeft;i<startTagRight;i++){
			if(content.charAt(i)==' '){
				startTagLength = i - startTagLeft;
				break;
			}
		}
		// 获得startTag内容,用来构成endTag
		String startTag = content.substring(startTagLeft, startTagLeft+startTagLength);
		String endTag = "</"+startTag+">";
		int endTagLeft = 0;
		// 得到endTagLeft
		for(int j=startTagRight+1;j<content.length();j++){
			if(content.substring(j, j+startTagLength+3).equals(endTag)){
				endTagLeft = j;
				break;
			}
		}
		// 截取startTag和endTag之间的内容再去除其中的所有标签
		info = content.substring(startTagRight+1,endTagLeft);
		info = info.replaceAll("<[^>]*>", "");
		return info.trim();
	}
}