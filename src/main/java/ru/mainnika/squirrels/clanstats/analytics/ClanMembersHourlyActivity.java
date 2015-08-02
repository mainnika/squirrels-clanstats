package ru.mainnika.squirrels.clanstats.analytics;

import ru.mainnika.squirrels.clanstats.core.Clan;
import ru.mainnika.squirrels.clanstats.utils.DateTime;

import java.util.*;

public class ClanMembersHourlyActivity extends AnalyticSnapshot
{
	private HashSet<Snapshot> set;
	private HashSet<Snapshot> unsaved;

	public ClanMembersHourlyActivity()
	{
		super("ClanMembersHourlyActivity");

		this.set = new HashSet<>();
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

			set.remove(snapshot);
			set.add(snapshot);

			unsaved.remove(snapshot);
			unsaved.add(snapshot);
		}
	}

	@Override
	public List<Snapshot> getSnapshots()
	{
		return new ArrayList<>(this.set);
	}

	@Override
	public List<Snapshot> getUnsaved()
	{
		return new ArrayList<>(this.unsaved);
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
