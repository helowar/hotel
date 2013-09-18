package com.mangocity.hotel.base.persistence;

import java.util.Date;

/**艺龙担保规则表
 * AbstractHtlElAssureRule entity provides the base persistence definition of
 * the HtlElAssureRule entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class HtlElAssureRule implements java.io.Serializable {

	// Fields

	private Long id;
	//艺龙酒店ID
	private String elhotelid;
	//艺龙价格类型ID
	private String rateplanid;
	//担保规则类型:1信用卡-日期担保,2信用卡-房量担保
	private Long garanteerulestypecode;
	//艺龙规则ID
	private Long ruleid;
	//担保开始日期
	private Date startdate;
	//担保结束日期
	private Date enddate;
	//担保日期判断基准:1-入住日期,2-在店日期
	private Long datetype;
	//周有效天数
	private String weekset;
	//担保规则条数
	private Long changerule;
	private Long gage;
	//是否校验到店时间:0-否，1-是
	private Long isarrivetimevouch;
	//到店担保开始时间
	private String arrivestattime;
	//到店担保结束时间
	private String arriveendtime;
	//到店担保结束时间是否是第二天
	private Long istomorrow;
	//是否校验房量:0-否，1-是
	private Long isroomcountvouch;
	//担保校验房量,预定几间房以上要担保
	private Long roomcount;
	//担保金额规则：1-首晚房费，2-全额房费
	private Long vouchmoneytype;
	//几日
	private Date daynum;
	//几小时
	private Long hournum;
	//是否允许变更:0-否，1-是
	private Long ischange;
	//几时
	private String timenum;
	//状态 0:无效 1：有效
	private Long status;
	//担保规则创建时间
	private Date createTime;
	//担保规则修改时间
	private Date updateTime;

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getElhotelid() {
		return this.elhotelid;
	}

	public void setElhotelid(String elhotelid) {
		this.elhotelid = elhotelid;
	}

	public String getRateplanid() {
		return this.rateplanid;
	}

	public void setRateplanid(String rateplanid) {
		this.rateplanid = rateplanid;
	}

	public Long getGaranteerulestypecode() {
		return this.garanteerulestypecode;
	}

	public void setGaranteerulestypecode(Long garanteerulestypecode) {
		this.garanteerulestypecode = garanteerulestypecode;
	}

	public Long getRuleid() {
		return this.ruleid;
	}

	public void setRuleid(Long ruleid) {
		this.ruleid = ruleid;
	}

	public Date getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Long getDatetype() {
		return this.datetype;
	}

	public void setDatetype(Long datetype) {
		this.datetype = datetype;
	}

	public String getWeekset() {
		return this.weekset;
	}

	public void setWeekset(String weekset) {
		this.weekset = weekset;
	}

	public Long getChangerule() {
		return this.changerule;
	}

	public void setChangerule(Long changerule) {
		this.changerule = changerule;
	}

	public Long getGage() {
		return this.gage;
	}

	public void setGage(Long gage) {
		this.gage = gage;
	}

	public Long getIsarrivetimevouch() {
		return this.isarrivetimevouch;
	}

	public void setIsarrivetimevouch(Long isarrivetimevouch) {
		this.isarrivetimevouch = isarrivetimevouch;
	}

	public String getArrivestattime() {
		return this.arrivestattime;
	}

	public void setArrivestattime(String arrivestattime) {
		this.arrivestattime = arrivestattime;
	}

	public String getArriveendtime() {
		return this.arriveendtime;
	}

	public void setArriveendtime(String arriveendtime) {
		this.arriveendtime = arriveendtime;
	}

	public Long getIstomorrow() {
		return this.istomorrow;
	}

	public void setIstomorrow(Long istomorrow) {
		this.istomorrow = istomorrow;
	}

	public Long getIsroomcountvouch() {
		return this.isroomcountvouch;
	}

	public void setIsroomcountvouch(Long isroomcountvouch) {
		this.isroomcountvouch = isroomcountvouch;
	}

	public Long getRoomcount() {
		return this.roomcount;
	}

	public void setRoomcount(Long roomcount) {
		this.roomcount = roomcount;
	}

	public Long getVouchmoneytype() {
		return this.vouchmoneytype;
	}

	public void setVouchmoneytype(Long vouchmoneytype) {
		this.vouchmoneytype = vouchmoneytype;
	}

	public Date getDaynum() {
		return this.daynum;
	}

	public void setDaynum(Date daynum) {
		this.daynum = daynum;
	}

	public Long getHournum() {
		return this.hournum;
	}

	public void setHournum(Long hournum) {
		this.hournum = hournum;
	}

	public Long getIschange() {
		return this.ischange;
	}

	public void setIschange(Long ischange) {
		this.ischange = ischange;
	}

	public String getTimenum() {
		return this.timenum;
	}

	public void setTimenum(String timenum) {
		this.timenum = timenum;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}