package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class HtlCreditAssureDate extends CEntity implements Serializable {

    private static final long serialVersionUID = -6200595310370417788L;

    // private Long ID;

    private long creditAssureDateID;

    private Date assureBeginDate;

    private Date assureEndDate;

    private HtlCreditAssure htlCreditAssure;

    public Date getAssureBeginDate() {
        return assureBeginDate;
    }

    public void setAssureBeginDate(Date assureBeginDate) {
        this.assureBeginDate = assureBeginDate;
    }

    public Date getAssureEndDate() {
        return assureEndDate;
    }

    public void setAssureEndDate(Date assureEndDate) {
        this.assureEndDate = assureEndDate;
    }

    public long getCreditAssureDateID() {
        return creditAssureDateID;
    }

    public void setCreditAssureDateID(long creditAssureDateID) {
        this.creditAssureDateID = creditAssureDateID;
    }

    public HtlCreditAssure getHtlCreditAssure() {
        return htlCreditAssure;
    }

    public void setHtlCreditAssure(HtlCreditAssure htlCreditAssure) {
        this.htlCreditAssure = htlCreditAssure;
    }
    /*
     * public Long getID() { return ID; } public void setID(Long id) { ID = id; }
     */

}
