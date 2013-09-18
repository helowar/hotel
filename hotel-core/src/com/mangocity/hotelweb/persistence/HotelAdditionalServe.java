package com.mangocity.hotelweb.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class HotelAdditionalServe {
    private List bedServes = new ArrayList();

    private List buffetServes = new ArrayList();

    public List getBedServes() {
        return bedServes;
    }

    public void setBedServes(List bedServes) {
        this.bedServes = bedServes;
    }

    public List getBuffetServes() {
        return buffetServes;
    }

    public void setBuffetServes(List buffetServes) {
        this.buffetServes = buffetServes;
    }

}
