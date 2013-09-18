package com.mangocity.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.collections.OrderedMap;
import com.mangocity.util.dao.DAO;

/**
 */
public class QueryBuilder implements Serializable {

    private DAO entityManager;

    public List query(String queryID, OrderedMap params) {

        List ls = entityManager.queryByNamedQuery(queryID, params.toArray());

        List results = new ArrayList(ls.size());

        for (int m = 0; m < ls.size(); m++) {
            Object obj = ls.get(m);

            results.add(MyBeanUtil.describe(obj));

        }

        return results;
    }

    public DAO getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(DAO entityManager) {
        this.entityManager = entityManager;
    }

    public void delete() {

    }

}
