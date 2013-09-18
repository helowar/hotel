package com.mangocity.hotel.base.resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.springframework.core.io.Resource;

import com.mangocity.hotel.base.resource.impl.BaseDataResourceDescriptor;
import com.mangocity.hotel.base.resource.impl.DbResourceDescriptor;
import com.mangocity.hotel.base.resource.impl.XmlResourceDescriptor;
import com.mangocity.util.ClassUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.XMLUtils;
import com.mangocity.util.collections.OrderedMap;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.exception.ConfigException;
import com.mangocity.util.log.MyLog;

/**
 * 资源描述器的工厂类
 */
public class ResourceDescriptorFactory implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8240632131386225746L;
    private static final MyLog log = MyLog.getLogger(ResourceDescriptorFactory.class);

    private Map globalConfigs = new HashMap();

    // private String configFileName = "resourceDescr.xml";

    private Resource configLocation;

    public static String RESOURCE_DATABASE = "database";

    public static String RESOURCE_REMOTE = "remote";

    private DAOIbatisImpl queryDao;// parasoft-suppress SERIAL.NSFSC "暂不修改" 

    public ResourceDescriptorFactory() {
    }

    public void init() {
        loadConfig();
    }

    public IResourceDescriptor getResouceDescriptor(String name) {

        IResourceDescriptor descriptor = (IResourceDescriptor) globalConfigs.get(name);

        if (null == descriptor) {

            try {
                loadConfig();
            } catch (Exception e) {
                log.error(e);

                throw new ConfigException(e);

            }

            descriptor = (IResourceDescriptor) globalConfigs.get(name);

        }

        if (null != descriptor)
            descriptor.setQueryDao(queryDao);

        return descriptor;

    }

    /**
     * 读取资源文件配置
     * 
     */
    public void loadConfig() {

        globalConfigs.clear();

        // InputStream in =
        // this.getClass().getClassLoader().getResourceAsStream(
        // configFileName);

        try {
            Element root = XMLUtils.getRootByName(configLocation.getInputStream());

            List elements = root.getChildren("resource");

            for (int m = 0; m < elements.size(); m++) {
                Element child = (Element) elements.get(m);

                loadResourceDescriptor(child, globalConfigs);
            }
        } catch (Exception e) {
            throw new ConfigException(e);
        }
    }

    private IResourceDescriptor getResourceDescriptor(Element root) {
        String resourceName = root.getAttributeValue("name");
        IResourceDescriptor resource = (IResourceDescriptor) globalConfigs.get(resourceName);

        if (null == resource) {

            String className = root.getAttributeValue("class");
            String type = root.getAttributeValue("type");

            /**
             * 如果在配置文件中指定class名称，就加载，否则起用默认的描述器
             */
            if (StringUtil.isValidStr(className)) {
                resource = (IResourceDescriptor) ClassUtil.newInstance(className);
            } else if (RESOURCE_DATABASE.equals(type)) {
                resource = new DbResourceDescriptor();
            } else if (RESOURCE_REMOTE.equalsIgnoreCase(type)) {
                resource = new BaseDataResourceDescriptor();
            } else {

                resource = new XmlResourceDescriptor();
            }

            resource.setName(resourceName);

            resource.setQueryDao(queryDao);

            globalConfigs.put(resourceName, resource);
        }

        return resource;
    }

    private void loadResourceDescriptor(Element root, Map global) {

        IResourceDescriptor resource = this.getResourceDescriptor(root);

        String type = root.getAttributeValue("type");
        String queryID = root.getAttributeValue("queryID");
        resource.setQueryID(queryID);

        resource.setFactory(this);

        if (RESOURCE_DATABASE.equals(type) || RESOURCE_REMOTE.equalsIgnoreCase(type)) {
            resource.setType(type);

            boolean cache = StringUtil.getBooleanValue(root.getAttributeValue("cache"));

            resource.setCacheAllowed(cache);

            if (!StringUtil.isValidStr(queryID))
                throw new ConfigException("QUERYID不能为空!");

        }

        Map description = new OrderedMap();

        List children = root.getChildren();

        for (int m = 0; m < children.size(); m++) {
            Element child = (Element) children.get(m);

            String name = child.getAttributeValue("name");
            String descr = child.getAttributeValue("descr");

            if (null == descr) {
                descr = child.getText();
            }

            description.put(name, descr);
        }

        resource.setDescription(description);

    }

    public DAOIbatisImpl getQueryDao() {
        return queryDao;
    }

    public void setQueryDao(DAOIbatisImpl queryDao) {
        this.queryDao = queryDao;
    }

    /**
     * /WEB-INF/classes/resourceDescr.xml.
     */
    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }
}
