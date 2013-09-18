package com.mangocity.hotel.dreamweb.comment.ejb;

import com.ctol.mango.pge.common.ParamServiceImpl;
import org.mangocube.corenut.scm.stub.JndiContext;
import org.mangocube.corenut.scm.stub.JndiContextProperty;
import org.mangocube.corenut.scm.stub.JndiMapBind;
import org.mangocube.corenut.scm.stub.ServiceClientRmiFactory;



public class ExtServiceClientRmiFactory extends ServiceClientRmiFactory
{
  
  private static final String PROVIDE_URL = "java.naming.provider.url";
  private static final String EJB_CONFIG_KEY_JAVA_NAMING_PROVIDER_URL = "{htlComment_ejb.java.naming.provider.url}";
  private static final String EJB_CONFIG_KEY_JAVA_NAMING_JNDI_BIND = "{htlComment_ejb.java.naming.jndi.bind.name}";

  public ExtServiceClientRmiFactory()
  {
  }

  public ExtServiceClientRmiFactory(String url)
  {
    super(url);
    initializePropertiesFromDB();
  }

  private void initializePropertiesFromDB() {
    boolean reset = false;
    String contextId = "";
    for (JndiContext context : this.serviceSkeletonFactory.getJndiContexts()) {
      String value = ParamServiceImpl.getInstance().getConfValue("{htlComment_ejb.java.naming.provider.url}");

      if ((value == null) || (value.trim().length() == 0)) {
        continue;
      }
      for (JndiContextProperty property : context.getProperties()) {
        if ("java.naming.provider.url".equals(property.getName())) {
          property.setValue(value);
          reset = true;
          contextId = context.getId();

          break;
        }
      }
      if (!reset) {
        JndiContextProperty property = new JndiContextProperty();
        property.setName("java.naming.provider.url");
        property.setValue(value);
        context.addProperty(property);
      }
    }

    for (JndiMapBind jmb : this.serviceSkeletonFactory.getBindingMaps()) {

      if (contextId.equals(jmb.getRefContext())) {

        String jndiBindName = ParamServiceImpl.getInstance().getConfValue("{htlComment_ejb.java.naming.jndi.bind.name}");

        jmb.setJndiName(jndiBindName);
        break;
      }
    }
  }
}