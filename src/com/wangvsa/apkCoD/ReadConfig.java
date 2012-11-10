package com.wangvsa.apkCoD;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadConfig {
	private WebInfo webInfos;
	public ReadConfig(String configFileName){
		webInfos = new WebInfo();
		try {
			InputStream is = new FileInputStream(configFileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String content = null;
			while((content=br.readLine())!=null){
				if(content.contains("/*"))	// 注释
					continue;
				// 分隔成两段，一段是网站名字，一段是url
				String[] webSite = content.split(" ");
				webInfos.addWebInfo(webSite[0], webSite[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getSize(){
		if(webInfos!=null)
			return webInfos.size;
		else
			return 0;
	}
	public String getName(int index) {
		if(webInfos!=null)
			return webInfos.names.get(index);
		else 
			return null;
	}
	public String getUrl(int index) {
		if(webInfos!=null)
			return webInfos.urls.get(index);
		else 
			return null;
	}
	
	private class WebInfo {
		int size;
		ArrayList<String> names;
		ArrayList<String> urls;
		public WebInfo(){
			size = 0;
			names = new ArrayList<String>();
			urls = new ArrayList<String>();
		}
		public void addWebInfo(String name,String url) {
			names.add(name);
			urls.add(url);
			size++;
		}
	};
}