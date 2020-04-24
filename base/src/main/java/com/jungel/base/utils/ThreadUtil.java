package com.jungel.base.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 线程管理类
 */
public class ThreadUtil {
	private static final String TAG = "ThreadUtil";


	private Map<String, ThreadBean> threadMap;
	private ThreadUtil() {
		threadMap = new HashMap<String, ThreadBean>();
	}

	private static ThreadUtil threadManager;
	public static synchronized ThreadUtil getInstance() {
		if (threadManager == null) {
			threadManager = new ThreadUtil();
		}

		return threadManager;
	}


	/**运行线程
	 * @param name
	 * @param runnable
	 * @return
	 */
	public Handler runThread(String name, Runnable runnable) {
		try {
			if (TextUtils.isEmpty(name) || runnable == null) {
				LogUtils.e("runThread  StringUtil.isNotEmpty(name, true) == false || runnable == null >> return");
				return null;
			}
			name = name.trim() + System.currentTimeMillis();
			LogUtils.d("\n runThread  name = " + name);

			Handler handler = getHandler(name);
			if (handler != null) {
				LogUtils.d("handler != null >>  destroyThread(name);");
				destroyThread(name);
			}

			HandlerThread thread = new HandlerThread(name);
			thread.start();//创建一个HandlerThread并启动它
			handler = new Handler(thread.getLooper());//使用HandlerThread的looper对象创建Handler
			KillRunnable killRunnable = new KillRunnable(runnable, name);
			handler.post(killRunnable);//将线程post到Handler中
			threadMap.put(name, new ThreadBean(name, thread, killRunnable, handler));

			LogUtils.d("runThread  added name = " + name + "; threadMap.size() = " + threadMap.size() + "\n");
			return handler;
		}catch (Exception e){
			return null;
		}
	}

	/**获取线程Handler
	 * @param name
	 * @return
	 */
	private Handler getHandler(String name) {
		ThreadBean tb = getThread(name);
		return tb == null ? null : tb.getHandler();
	}

	/**获取线程
	 * @param name
	 * @return
	 */
	private ThreadBean getThread(String name) {
		if (threadMap==null){
			return null;
		}
		return name == null ? null : threadMap.get(name);
	}


	/**销毁线程
	 * @param nameList
	 * @return
	 */
	public void destroyThread(List<String> nameList) {
		if (nameList != null) {
			for (String name : nameList) {
				destroyThread(name);
			}
		}
	}
	/**销毁线程
	 * @param name
	 * @return
	 */
	public void destroyThread(String name) {
		try {
			destroyThread(getThread(name));
		}catch (Exception e){
		}
	}
	/**销毁线程
	 * @param tb
	 * @return
	 */
	private void destroyThread(ThreadBean tb) {
		if (tb == null) {
			LogUtils.e("destroyThread  tb == null >> return;");
			return;
		}

		destroyThread(tb.getHandler(), tb.getRunnable());
		if (tb.getName() != null) { // StringUtil.isNotEmpty(tb.getName(), true)) {
			threadMap.remove(tb.getName());
		}
	}
	/**销毁线程
	 * @param handler
	 * @param runnable
	 * @return
	 */
	public void destroyThread(Handler handler, Runnable runnable) {
		if (handler == null || runnable == null) {
			LogUtils.e("destroyThread  handler == null || runnable == null >> return;");
			return;
		}

		try {
			handler.removeCallbacks(runnable);
		} catch (Exception e) {
			LogUtils.e("onDestroy try { handler.removeCallbacks(runnable);...  >> catch  : " + e.getMessage());
		}
	}


	/**结束ThreadManager所有进程
	 */
	public void finish() {
		threadManager = null;
		if (threadMap == null || threadMap.keySet() == null) {
			LogUtils.d("finish  threadMap == null || threadMap.keySet() == null >> threadMap = null; >> return;");
			threadMap = null;
			return;
		}
		List<String> nameList = new ArrayList<String>(threadMap.keySet());//直接用Set在系统杀掉应用时崩溃
		if (nameList != null) {
			for (String name : nameList) {
				destroyThread(name);
			}
		}
		threadMap = null;
		LogUtils.d("\n finish  finished \n");
	}


	/**线程类
	 */
	private static class ThreadBean {

		private String name;
		@SuppressWarnings("unused")
		private HandlerThread thread;
		private Runnable runnable;
		private Handler handler;

		public ThreadBean(String name, HandlerThread thread, Runnable runnable, Handler handler) {
			this.name = name;
			this.thread = thread;
			this.runnable = runnable;
			this.handler = handler;
		}

		public String getName() {
			return name;
		}

		public Runnable getRunnable() {
			return runnable;
		}

		public Handler getHandler() {
			return handler;
		}
	}


	class KillRunnable implements Runnable{

		private Runnable innerRunnable;
		private String threadName;

		public KillRunnable(Runnable runnable,String name){
			innerRunnable = runnable;
			threadName = name;
		}

		@Override
		public void run() {
			innerRunnable.run();
			destroyThread(threadName);
		}
	}
	
}
