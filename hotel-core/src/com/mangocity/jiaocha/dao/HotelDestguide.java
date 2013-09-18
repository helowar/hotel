package com.mangocity.jiaocha.dao;

import java.util.Map;

public class HotelDestguide
{
    private String cityName;

    private String cityCode;

    private String cityBrief;

    private Map<String, String> citySigns;

    public String getCityBrief()
    {
        return cityBrief;
    }

    public void setCityBrief(String cityBrief)
    {
        this.cityBrief = cityBrief;
    }

    public String getCityCode()
    {
        return cityCode;
    }

    public void setCityCode(String cityCode)
    {
        this.cityCode = cityCode;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public Map<String, String> getCitySigns()
    {
        return citySigns;
    }

    public void setCitySigns(Map<String, String> citySigns)
    {
        this.citySigns = citySigns;
    }

    public HotelDestguide()
    {

    }

}
