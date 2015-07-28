package ru.mainnika.squirrels.clanstats.storage;

import ru.mainnika.squirrels.clanstats.utils.Timers;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public abstract class Savable
{
	private static final Logger log;

	private static class Saver implements Timers.Task
	{
		private Saver()
		{
			Timers.subscribe(this, 5, 5, TimeUnit.MINUTES);
		}

		@Override
		public void onTimer()
		{
			for (Savable savable : savables)
			{
				log.info("Saving " + savable.name);
				savable.onSave();
			}
		}
	}

	static ConcurrentLinkedQueue<Savable> savables;
	static Saver saver;

	static
	{
		log = Logger.getLogger(Savable.class.getName());
		savables = new ConcurrentLinkedQueue<>();
		saver = new Saver();
	}

	private String name;

	public Savable(String name)
	{
		this.name = name;
		savables.add(this);
	}

	public abstract void onSave();
}
