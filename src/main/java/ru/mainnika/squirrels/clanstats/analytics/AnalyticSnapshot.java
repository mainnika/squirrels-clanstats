package ru.mainnika.squirrels.clanstats.analytics;

import ru.mainnika.squirrels.clanstats.storage.Database;
import ru.mainnika.squirrels.clanstats.storage.Savable;

import java.util.List;

public abstract class AnalyticSnapshot extends Savable
{
	public static class Snapshot
	{
		public int hash;
		public int type;
		public int id;
		public int data;
		public int value;

		@Override
		public int hashCode()
		{
			return hash ^ id ^ data;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof Snapshot)
			{
				Snapshot other = (Snapshot) obj;

				return other.hash == this.hash && other.type == this.type && other.id == this.id && other.data == this.data;
			}

			return false;
		}
	}

	private String name;

	protected AnalyticSnapshot(String name)
	{
		super(name);
		this.name = name;
	}

	public abstract void add(Object object);

	public abstract int getHash();

	public abstract void clearUnsaved();

	public abstract void setSnapshots(List<Snapshot> snapshots);

	public abstract List<Snapshot> getUnsaved();

	public abstract List<Snapshot> getSnapshots();

	public String name()
	{
		return this.name;
	}

	@Override
	public void onSave()
	{
		Database.Guard guard = null;
		try
		{
			guard = new Database.Guard();

			Database base = guard.get();
			base.saveSnapshot(this);
		} catch (Exception ignored)
		{
		} finally
		{
			if (guard != null)
				guard.close();
		}
	}
}
