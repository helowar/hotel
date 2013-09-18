package com.mangocity.hotel.ejb.search.ejbRmiFactory;

import org.apache.log4j.Logger;
import org.mangocube.corenut.scm.stub.JndiContext;
import org.mangocube.corenut.scm.stub.JndiContextProperty;
import org.mangocube.corenut.scm.stub.JndiMapBind;
import org.mangocube.corenut.scm.stub.ServiceClientRmiFactory;

import com.ctol.mango.pge.common.ParamServiceImpl;

public class DBServiceClientRmiFactory extends ServiceClientRmiFactory {
  private Logger log = Logger.getLogger(DBServiceClientRmiFactory.class);
  private static final String PROVIDE_URL = "java.naming.provider.url";
  private static final String EJB_CONFIG_KEY_JAVA_NAMING_PROVIDER_URL = "{hotelII_ejb.java.naming.provider.url}";
  private static final String EJB_CONFIG_KEY_JAVA_NAMING_JNDI_BIND = "{hotelII_ejb.java.naming.jndi.bind.name}";

  public DBServiceClientRmiFactory() {
	  initializePropertiesFromDB();
  }

  public DBServiceClientRmiFactory(String url) {
    super(url);
    initializePropertiesFromDB();
  }

  private void initializePropertiesFromDB() {
    boolean reset = false;
    String contextId = "";
    for (JndiContext context : this.serviceSkeletonFactory.getJndiContexts()) {
      String value = ParamServiceImpl.getInstance().getConfValue(EJB_CONFIG_KEY_JAVA_NAMING_PROVIDER_URL);
      log.info(EJB_CONFIG_KEY_JAVA_NAMING_PROVIDER_URL+":" + value);
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
        String jndiBindName = ParamServiceImpl.getInstance().getConfValue(EJB_CONFIG_KEY_JAVA_NAMING_JNDI_BIND);
        log.info(EJB_CONFIG_KEY_JAVA_NAMING_JNDI_BIND+": " + jndiBindName);
        jmb.setJndiName(jndiBindName);
        break;
      }
    }
  }
}