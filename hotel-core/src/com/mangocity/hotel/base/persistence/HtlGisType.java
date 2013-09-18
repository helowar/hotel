package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author zuoshengwei
 * 
 */
public class HtlGisType implements Serializable {

    private String addressTypeName;

    private long addressTypeId;

    private String cityName;

    private List lstAddress;

    public String getAddressTypeName() {
        return addressTypeName;
    }

    public void setAddressTypeName(String addressTypeName) {
        this.addressTypeName = addressTypeName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List getLstAddress() {
        return lstAddress;
    }

    public void setLstAddress(List lstAddress) {
        this.lstAddress = lstAddress;
    }

    public long getAddressTypeId() {
        return addressTypeId;
    }

    public void setAddressTypeId(long addressTypeId) {
        this.addressTypeId = addressTypeId;
    }

}
