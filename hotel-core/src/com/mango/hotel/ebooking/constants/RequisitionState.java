package com.mango.hotel.ebooking.constants;

/**
 * 审核状态
 * 
 * @author chenjiajie
 * 
 */
public class RequisitionState {

    /**
     * 待审核
     */
    public final static int UNAUDITED = 0;

    /**
     * 审核中
     */
    public final static int AUDITING = 1;

    /**
     * 审核通过
     */
    public final static int AUDITED = 2;

    /**
     * 审核未通过
     */
    public final static int REJECT = 3;
}
