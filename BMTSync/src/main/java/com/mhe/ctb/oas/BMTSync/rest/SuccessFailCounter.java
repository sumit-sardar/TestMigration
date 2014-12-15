package com.mhe.ctb.oas.BMTSync.rest;

import java.util.LinkedList;
import java.util.List;

public class SuccessFailCounter {
	private int _remainingItems;
	private int _failureCount;
	private int _successCount;
	private List<Object> _failures;

	public List<Object> getFailures() {
		return _failures;
	}

	public SuccessFailCounter(int totalItems) {
		if (totalItems < 0)
			throw new IllegalArgumentException("count must be 0 or more");

		_remainingItems = totalItems;
	}

	public void markFailed() {
		_failureCount += _remainingItems;
		_remainingItems = 0;
	}

	public void successful() throws IllegalAccessException {
		validateRemainingItems();
		_successCount++;
		_remainingItems--;
	}

	public void failure() throws IllegalAccessException {
		failure(null);
	}
	
	public void failure(Object o) throws IllegalAccessException {
		validateRemainingItems();
		
		if (o != null)
		{
			if (_failures == null) 
			{
				_failures = new LinkedList<Object>();
			}
		
			_failures.add(o);		
		}
		
		_failureCount++;
		_remainingItems--;
	}

	private void validateRemainingItems() throws IllegalAccessException {
		if (_remainingItems <= 0) {
			throw new IllegalAccessException("No remaining items expected");
		}
	}

	public int getRemainingItems() {
		return _remainingItems;
	}

	public int getFailureCount() {
		return _failureCount;
	}

	public int getSuccessCount() {
		return _successCount;
	}

}
