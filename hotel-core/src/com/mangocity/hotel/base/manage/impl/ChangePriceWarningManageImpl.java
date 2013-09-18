package com.mangocity.hotel.base.manage.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.manage.ChangePriceWarningManage;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 */
public class ChangePriceWarningManageImpl extends DAOHibernateImpl implements
    ChangePriceWarningManage {

    public List findPriceDate(long hotel_id) {

        Date tDate = DateUtil.getDate(DateUtil.getSystemDate());
        List changePriResult = super.queryByNamedQuery("getCpwData",
            new Object[] { hotel_id, tDate });
        return changePriResult;
    }

}
