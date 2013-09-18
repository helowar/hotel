package com.mangocity.hotel.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mangocity.client.MonitorGWTPageService;

public class MonitorGWTPageServiceImpl extends RemoteServiceServlet implements MonitorGWTPageService{

	public String pageMethod() {
		return "OK";
	}

	
}
