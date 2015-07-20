package ru.mainnika.squirrels.clanstats.utils;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Timers
{
	private static ScheduledExecutorService service;
	private static HashMap<Task, ScheduledFuture> tasks;

	public interface Task
	{
		void onTimer();
	}

	private static class InternalTask implements Runnable
	{
		private final Task task;

		public InternalTask(Task task)
		{
			this.task = task;
		}

		@Override
		public void run()
		{
			this.task.onTimer();
		}
	}

	public static void subscribe(Task task, int delay, int repeat, TimeUnit unit)
	{
		if (service == null)
			return;

		ScheduledFuture future = service.scheduleAtFixedRate(new InternalTask(task), delay, repeat, unit);

		tasks.put(task, future);
	}

	public static void unsubscribe(Task task)
	{
		if (tasks == null)
			return;

		ScheduledFuture future = tasks.remove(task);

		if (future == null)
			return;

		future.cancel(false);
	}

	public static void create()
	{
		service = Executors.newScheduledThreadPool(4);
		tasks = new HashMap<>();
	}

	public static void destroy()
	{
		service.shutdownNow();
		tasks.clear();
	}

}
