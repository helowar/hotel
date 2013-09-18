package com.mangocity.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("hotelPage")
public interface MonitorGWTPageService extends RemoteService{

	public String pageMethod();
}
