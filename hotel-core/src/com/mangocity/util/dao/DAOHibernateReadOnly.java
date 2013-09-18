package com.mangocity.util.dao;

import java.io.Serializable;

import org.hibernate.Session;

public interface DAOHibernateReadOnly extends DAO,Serializable {
    /**
     * 释放Session
     * @param session
     */
    public void releaseSessian(Session session);
}
