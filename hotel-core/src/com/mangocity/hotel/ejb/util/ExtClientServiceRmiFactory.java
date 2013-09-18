package com.mangocity.hotel.ejb.util;

import org.apache.log4j.Logger;
import org.mangocube.corenut.scm.stub.JndiContext;
import org.mangocube.corenut.scm.stub.JndiContextProperty;
import org.mangocube.corenut.scm.stub.JndiMapBind;
import org.mangocube.corenut.scm.stub.ServiceClientRmiFactory;

import com.ctol.mango.pge.common.ParamServiceImpl;

public class ExtClientServiceRmiFactory extends ServiceClientRmiFactory {
	private Logger log = Logger.getLogger(ExtClientServiceRmiFactory.class);
	private static final String PROVIDE_URL = "java.naming.provider.url";
	private String javaNameProviderUrl;
	private String jndiBind;

	public ExtClientServiceRmiFactory() {
		initializePropertiesFromDB();
	}

	public ExtClientServiceRmiFactory(String url) {
		super(url);
		initializePropertiesFromDB();
	}

	public ExtClientServiceRmiFactory(String url, String javaNameProviderUrl, String jndiBind) {
		super(url);
		this.javaNameProviderUrl = javaNameProviderUrl;
		this.jndiBind = jndiBind;
		initializePropertiesFromDB();
	}

	private void initializePropertiesFromDB() {
		if (javaNameProviderUrl == null || jndiBind == null) {
			throw new AssertionError("javaNameProviderUrl is null or jndiBind is null");
		}

		boolean reset = false;
		String contextId = "";
		for (JndiContext context : this.serviceSkeletonFactory.getJndiContexts()) {
			String value = ParamServiceImpl.getInstance().getConfValue(javaNameProviderUrl);
			log.info(javaNameProviderUrl + ":" + value);
			if ((value == null) || (value.trim().length() == 0)) {
				continue;
			}
			for (JndiContextProperty property : context.getProperties()) {
				if (PROVIDE_URL.equals(property.getName())) {
					property.setValue(value);
					reset = true;
					contextId = context.getId();
					log.info("contextId : " + contextId);
					break;
				}
			}
			if (!reset) {
				JndiContextProperty property = new JndiContextProperty();
				property.setName(PROVIDE_URL);
				property.setValue(value);
				context.addProperty(property);
			}
		}

		for (JndiMapBind jmb : this.serviceSkeletonFactory.getBindingMaps()) {
			log.info("jmb.getRefContext() : " + jmb.getRefContext());
			if (contextId.equals(jmb.getRefContext())) {
				log.info("jndi-bind name : " + jmb.getJndiName());
				String jndiBindName = ParamServiceImpl.getInstance().getConfValue(jndiBind);
				log.info(jndiBind + ": " + jndiBindName);
				jmb.setJndiName(jndiBindName);
				break;
			}
		}
	}


}