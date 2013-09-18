package com.mangocity.hotel.base.web;

import com.mangocity.hotel.base.persistence.HtlFavourableHotel;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

public class LimitFavourableHotelAction extends PersistenceAction{
	protected Class getEntityClass() {
        return HtlFavourableHotel.class;
    }
}
