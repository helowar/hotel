package com.mangocity.hotel.base.resource;

import java.util.Map;

import com.mangocity.util.dao.DAOIbatisImpl;

/**
 */
public interface IResourceDescriptor {

    Map getDescription();

    Map getDescription(Map params);

    boolean isCacheAllowed();

    void setCacheAllowed(boolean cache);

    void setName(String name);

    void setQueryID(String queryID);

    void setType(String type);

    void setDescription(Map description);

    void setQueryDao(DAOIbatisImpl queryDao);

    void setFactory(ResourceDescriptorFactory factory);

}