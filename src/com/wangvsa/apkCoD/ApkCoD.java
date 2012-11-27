package com.wangvsa.apkCoD;


/**
 * apk capacity of downloading
 * @author wangchen
 *
 */

public class ApkCoD {
	public static void main(String[] args){
		// 读配置文件获得网站名和url
		ReadConfig rc = new ReadConfig("urls.conf");
		
		// 根据获得的网站数来设置线程数
		ThreadPoolManager threads = new ThreadPoolManager(rc.getSize());
		threads.init();
		
		for(int i=0;i<rc.getSize();i++) { 
			String webName = rc.getName(i); 
			String webUrl = rc.getUrl(i); 
			Task newTask = new Task(webName, webUrl);
			threads.addTask(newTask);
		}
	}
}