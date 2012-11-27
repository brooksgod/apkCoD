package com.wangvsa.apkCoD;

import java.util.LinkedList;
import java.util.List;

/**
 * 线程池，管理线程，所有方法为同步方法
 * @author wangchen
 *
 */
public class ThreadPoolManager extends ThreadGroup{
	// 工作线程数
	private int _threadCount = 8;
	// 任务队列
	List<Task> tasks = new LinkedList<Task>();
	
	public ThreadPoolManager(int threadCount) {
		this("test");
		_threadCount = threadCount;
		// 设置为守护线程
		setDaemon(true);
	}
	
	public ThreadPoolManager(String name) {
		super(name);
	}

	/** 初始化线程池，创建所有工作线程并开始 */
	public synchronized void init(){
		int N = _threadCount;
		for (int i = 0;i<N;i++){ 
			new WorkThread(i).start();
       }
	}
	
	/** 结束线程池，关闭所有线程 */
	public synchronized void finish(){
		tasks.clear();
		interrupt();
	}
	
	public synchronized void addTask(Task newTask){
		tasks.add(newTask);
		notifyAll();
	}
	
	/** 在工作线程run方法中会调用此函数 
	 * 虽然run方法是由于addTask函数被唤醒
	 * 但仍然有可能进入这个方法后发现task.size=0
	 * 所以要用循环判断，不能直接remove(0)*/
	public synchronized Task getTask() {
		while(tasks.size()==0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return tasks.remove(0);
	}
	
	
	/** 
	 * 内部工作线程类
	 * @author wangchen
	 *
	 */
	private class WorkThread extends Thread {
		public WorkThread(int threadId) {
			super(ThreadPoolManager.this, ""+threadId);
		}
		public void run() {
			while(!isInterrupted()) {
				Task runTask = getTask();
				if(runTask!=null) 
					runTask.run();
			}
		}
	}
}