package com.androidtemplate.engine.application;

import java.util.List;

import android.app.Service;

import com.factory.android.InfuseApplication;

/**
 * Base class for all applications using this template.
 * 
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class TApplication extends InfuseApplication {
	private List<Service> mServices;
	
	@Override
	public void onCreate() {
		
	}
	
	
}
