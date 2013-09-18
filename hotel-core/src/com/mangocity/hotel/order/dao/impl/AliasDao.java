package com.mangocity.hotel.order.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SQLQuery;

import com.mangocity.hotel.order.dao.IaliasDao;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 */
public class AliasDao extends DAOHibernateImpl implements IaliasDao, Serializable {

    public List queryNamebyCode(String sql) {
        SQLQuery query = super.getSession().createSQLQuery(sql);
        return query.list();
    }

}
