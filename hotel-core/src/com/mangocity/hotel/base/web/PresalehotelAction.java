package com.mangocity.hotel.base.web;

import com.mangocity.hotel.base.persistence.HtlPresaleHotel;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class PresalehotelAction extends PersistenceAction {

    protected Class getEntityClass() {
        return HtlPresaleHotel.class;
    }

}
