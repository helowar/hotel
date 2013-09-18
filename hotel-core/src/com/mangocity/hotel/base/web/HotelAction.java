package com.mangocity.hotel.base.web;

import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 */
public class HotelAction extends PersistenceAction {

    protected Class getEntityClass() {
        return HtlHotel.class;
    }

}
