package com.androidtemplate.engine.commons.threading.base;

import java.util.concurrent.atomic.AtomicBoolean;

import com.androidtemplate.engine.commons.threading.ICancelableRunnable;

/**
 * Cancelable runnable abstract class implementation.
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public abstract class CancelableRunnable implements ICancelableRunnable {
	protected volatile AtomicBoolean canceled = new AtomicBoolean(false);
	
	@Override
	public boolean isCancelled() {
		return canceled.get();
	}
	
	@Override
	public boolean canContinue() {
		return (canceled.get() == false);
	}
	
	@Override
	public void cancel() {			
		canceled.set(true);
	}
	
	@Override
	public boolean workInProgress() {
		return true;
	}
}
