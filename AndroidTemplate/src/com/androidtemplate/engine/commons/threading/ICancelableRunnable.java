package com.androidtemplate.engine.commons.threading;

/**
 * Interface representing a runnable that can be cancelled.
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public interface ICancelableRunnable extends Runnable {
	
	public boolean isCancelled();
		
	public boolean canContinue();
		
	public void cancel();
	
	public boolean workInProgress();
}
