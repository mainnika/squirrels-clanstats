package ru.mainnika.squirrels.clanstats.core;

import org.apache.commons.collections4.map.MultiValueMap;

import java.util.ArrayList;
import java.util.Collection;

public class DeferredRequests
{
	interface DeferredRequirer
	{
		void deferredRequire(int waitingId);
	}

	interface DeferredObject
	{
		int getDeferredId();
	}

	interface DeferredWaiter<T>
	{
		void onRequest(int requestId, T response);
	}

	private DeferredRequirer requirer;
	private MultiValueMap<Integer, Integer> requests;
	private ArrayList<DeferredWaiter> waiters;
	private int nextId;

	DeferredRequests(DeferredRequirer requirer)
	{
		this.requirer = requirer;
		this.requests = new MultiValueMap<>();
		this.waiters = new ArrayList<>();
		this.nextId = 0;
	}

	void addWaiter(DeferredWaiter waiter)
	{
		this.waiters.add(waiter);
	}

	int addRequest(int waitingId)
	{
		int requestId = this.nextId++;
		this.requests.put(waitingId, requestId);
		this.requirer.deferredRequire(waitingId);

		return requestId;
	}

	void flow(DeferredObject element)
	{
		int waitingId = element.getDeferredId();

		Collection<Integer> ids = this.requests.getCollection(waitingId);

		if (ids == null)
			return;

		for (Integer id : ids)
		{
			for (DeferredWaiter waiter : this.waiters)
			{
				waiter.onRequest(id, element);
			}
		}

		this.requests.remove(waitingId);
	}
}
