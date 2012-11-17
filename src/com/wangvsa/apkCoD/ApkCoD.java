package com.wangvsa.apkCoD;


/**
 * apk capacity of downloading
 * @author wangchen
 *
 */

public class ApkCoD {
	public static void main(String[] args){
		ThreadPoolManager threads = new ThreadPoolManager("TEST");
		threads.init();
		
		// 读配置文件获得网站名和url
		ReadConfig rc = new ReadConfig("urls.conf");
		
		for(int i=0;i<rc.getSize();i++) { 
			String webName = rc.getName(i); 
			String webUrl = rc.getUrl(i); 
			Task newTask = new Task(webName, webUrl);
			threads.addTask(newTask);
		}
	}
}