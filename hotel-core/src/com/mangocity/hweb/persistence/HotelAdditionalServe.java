package com.mangocity.hweb.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 附加服务信息
 * 
 * @author chenkeming
 * 
 */
public class HotelAdditionalServe implements Serializable {
    /**
     * 加床服务信息
     */
    private List bedServes = new ArrayList();

    /**
     * 自助早信息
     */
    private List buffetServes = new ArrayList();

    /**
     * 中早信息
     */
    private List chineseServes = new ArrayList();

    /**
     * 西早信息
     */
    private List westernServes = new ArrayList();
    
    /**
     * 接送信息
     */
    private List welcomeServers = new ArrayList();

    public List getWelcomeServers() {
		return welcomeServers;
	}

	public void setWelcomeServers(List welcomeServers) {
		this.welcomeServers = welcomeServers;
	}

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

    public List getChineseServes() {
        return chineseServes;
    }

    public void setChineseServes(List chineseServes) {
        this.chineseServes = chineseServes;
    }

    public List getWesternServes() {
        return westernServes;
    }

    public void setWesternServes(List westernServes) {
        this.westernServes = westernServes;
    }

}
