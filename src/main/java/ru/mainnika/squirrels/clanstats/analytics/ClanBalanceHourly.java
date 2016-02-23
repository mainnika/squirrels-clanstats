package ru.mainnika.squirrels.clanstats.analytics;

import ru.mainnika.squirrels.clanstats.core.Clan;
import ru.mainnika.squirrels.clanstats.utils.DateTime;

import java.util.*;

public class ClanBalanceHourly extends AnalyticSnapshot
{
	public static final int COINS_TYPE = 0;
	public static final int NUTS_TYPE = 1;

	private TreeMap<Integer, HashSet<Snapshot>> snapshots;
	private HashSet<Snapshot> unsaved;

	public ClanBalanceHourly()
	{
		super("ClanBalanceHourly");

		this.snapshots = new TreeMap<>();
		this.unsaved = new HashSet<>();
	}

	@Override
	public void add(Object object)
	{
		if (!(object instanceof Clan))
		{
			return;
		}

		Clan clan = (Clan) object;

		Integer nuts = clan.nuts();
		Integer coins = clan.coins();
		Integer hour = this.getHash();
		HashSet<Snapshot> storage = this.snapshots.get(hour);

		if (storage == null)
		{
			storage = new HashSet<Snapshot>();
			this.snapshots.put(hour, storage);
		}

		int types[] = {COINS_TYPE, NUTS_TYPE};
		for (int type : types)
		{
			Snapshot snapshot = new Snapshot();
			snapshot.hash = this.getHash();
			snapshot.type = Analytics.CLAN_BALANCE_HOURLY.type();
			snapshot.id = clan.id();
			snapshot.data = type;
			snapshot.value = type == COINS_TYPE ? coins : nuts;

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
			if (snapshot.type != Analytics.CLAN_BALANCE_HOURLY.type() || snapshot.id != id)
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
