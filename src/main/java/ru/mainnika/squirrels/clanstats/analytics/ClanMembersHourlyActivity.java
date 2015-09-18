package ru.mainnika.squirrels.clanstats.analytics;

import ru.mainnika.squirrels.clanstats.core.Clan;
import ru.mainnika.squirrels.clanstats.storage.Database;
import ru.mainnika.squirrels.clanstats.utils.DateTime;

import java.util.*;

public class ClanMembersHourlyActivity extends AnalyticSnapshot
{
	private TreeMap<Integer, HashSet<Snapshot>> snapshots;
	private HashSet<Snapshot> unsaved;

	public ClanMembersHourlyActivity()
	{
		super("ClanMembersHourlyActivity");

		this.snapshots = new TreeMap<>();
		this.unsaved = new HashSet<>();

		Database.Guard guard = null;
		Database base = null;
		try
		{
			guard = new Database.Guard();
			base = guard.get();

			base.loadSnapshot(this);

		} catch (Exception ignored)
		{
		} finally
		{
			if (guard != null)
				guard.close();
		}

	}

	@Override
	public void add(Object object)
	{
		if (!(object instanceof Clan))
		{
			return;
		}

		Clan clan = (Clan) object;

		HashMap stats = clan.stats();

		if (stats == null)
		{
			return;
		}

		Set elements = stats.entrySet();

		for (Object element : elements)
		{
			Map.Entry<Integer, Map.Entry<Integer, Integer>> pair = (Map.Entry) element;
			Snapshot snapshot = new Snapshot();

			snapshot.hash = this.getHash();
			snapshot.type = Analytics.CLAN_MEMBERS_HOURLY.type();
			snapshot.id = clan.id();
			snapshot.data = pair.getKey();
			snapshot.value = pair.getValue().getKey();

			Integer hour = snapshot.hash;
			HashSet<Snapshot> storage = this.snapshots.get(hour);

			if (storage == null)
			{
				storage = new HashSet<Snapshot>();
				this.snapshots.put(hour, storage);
			}

			storage.remove(snapshot);
			storage.add(snapshot);

			unsaved.remove(snapshot);
			unsaved.add(snapshot);
		}
	}

	@Override
	public List<Snapshot> getSnapshots()
	{
//		TODO:
		return null;
	}

	@Override
	public List<Snapshot> getUnsaved()
	{
		return new ArrayList<>(this.unsaved);
	}

	@Override
	public List<Snapshot> getLast(int id)
	{
		Map.Entry<Integer, HashSet<Snapshot>> last = this.snapshots.lastEntry();
		ArrayList<Snapshot> result = new ArrayList<>();

		if (last == null)
			return result;

		Integer hour = last.getKey();
		HashSet<Snapshot> storage = last.getValue();

		for (Snapshot snapshot : storage)
		{
			if (snapshot.type != Analytics.CLAN_MEMBERS_HOURLY.type() || snapshot.id != id)
				continue;

			result.add(snapshot);
		}

		return result;
	}

	@Override
	public synchronized void setSnapshots(List<Snapshot> snapshots)
	{
		this.snapshots.clear();

		for (Snapshot snapshot : snapshots)
		{
			Integer hour = snapshot.hash;
			HashSet<Snapshot> storage = this.snapshots.get(hour);

			if (storage == null)
			{
				storage = new HashSet<Snapshot>();
				this.snapshots.put(hour, storage);
			}

			storage.add(snapshot);
		}
	}

	@Override
	public void clearUnsaved()
	{
		this.unsaved.clear();
	}

	@Override
	public int getHash()
	{
		return DateTime.getUnixhour();
	}
}
