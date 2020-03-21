/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.task 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午7:08:24 
 * 
 */
package com.fxz.fts.task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: TaskMgr
 * @Description: 运行任务管理
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午7:08:24
 */

public class TaskMgr {
	private static Map<String, FtsTask> runningTask = new ConcurrentHashMap<String, FtsTask>();

	public static FtsTask getTask(String uuid) {
		return runningTask.get(uuid);
	}

	public static void addTask(String uuid, FtsTask ftsTask) {
		runningTask.put(uuid, ftsTask);
	}

	public static void removeTask(String uuid) {
		runningTask.remove(uuid);
	}

	public static boolean isRunning(String uuid) {
		return runningTask.containsKey(uuid);
	}
}
