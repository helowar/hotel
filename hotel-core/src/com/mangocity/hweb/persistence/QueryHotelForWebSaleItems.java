package com.mangocity.hweb.persistence;

import java.io.Serializable;
import java.util.Date;

import com.mangocity.hotel.base.util.WebStrUtil;
import com.mangocity.util.DateUtil;

/**
 * 
 * 酒店网站v2.2查询结果<br>
 * 价格明细类<br>
 * 
 * @author chenkeming
 * 
 */
public class QueryHotelForWebSaleItems implements Serializable {
	
	private static final long serialVersionUID = 4884058131631491381L;

    /**
     * 子房型ID
     */
    private long priceId;

    /**
     * 房间日期(YYYY-MM-DD)
     */
    private Date fellowDate;

    /**
     * 销售价
     */
    private double salePrice;
    /**
     * 净价
     */
    private double advicePrice;

    /**
     * 房态 -- 处理后
     */
    private String roomState;

    /**
     * 房态 -- 处理前
     */
    private String roomStatus;

    /**
     * quotaType
     */
    private String quotaType;

    /**
     * 床型
     */
    private String bedType;

    /**
     * 房间设施：宽带
     */
    private String roomEquipment;

    /**
     * 门市价
     */
    private double salesRoomPrice;

    /**
     * 显示页面的价格－－－如果是港币则为计算后的人民币价格
     */
    private double rmbPrice;

    /**
     * 开关房标志
     */
    private String close_flag;

    /**
     * 如果关房，根据关房原因判断是否显示价格<br>
     * 因变价原因关房时，不显示价格<br>
     * 1: 不显示<br>
     * 0: 显示
     */
    private int closeShowPrice = 0;

    /**
     * 配额数量 -- 用于检查房态不可超且配额为0的情况
     */
    private int availQty = 0;

    // 日期所对应的星期(yun.wu 2008-5-28)
    private int weekDay;

    // 早餐类型(yun.wu 2008-5-28)
    private String breakfastType;

    // 早餐数量(yun.wu 2008-5-28)
    private int breakfastNum;

    // 早餐价格(yun.wu 2008-5-29)
    private int breakfastPrice;

    // 能否预定
    private int canBook;

    /**
     * ***********************hotel2.6加了8个参数@author
     * zhuangzhineng@begin********************************
     */

    /**
     * hotel2.6 是否需要无条件担保, 是:1(true), 否:0(false)
     * 
     * @author chenkeming Feb 6, 2009 8:58:29 AM
     */
    private boolean needAssure;

    /**
     * hotel2.6 面付转预付, 必须:0, 允许:1, 不许:2
     * 
     * @author chenkeming Feb 6, 2009 8:59:16 AM
     */
    private int payToPrepay;

    /**
     * 最晚可预订日期 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private Date latestBookableDate;

    /**
     * 最晚可预订时间点 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private String latestBokableTime;
    
    /**hotel 2.9.3  
     * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 begin  
     */
    
    //最早可预订日期 
    private Date  firstBookableDate;
    
    //最早可预订时间
    private String firstBookableTime;

    /**hotel 2.9.3  
     * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 end  
     */

    /**
     * 必住最后日期 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private Date mustLastDate;

    /**
     * 必住最早日期 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private Date mustFirstDate;

    /**
     * 连住天数 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private long continueDay;

    /**
     * 最少限住天数 hotel2.9.2 add by xiaoyong.li 2009-07-28
     */
    private long minRestrictNights;

    /**
     * 最大限住天数 hotel2.9.2 add by xiaoyong.li 2009-07-28
     */
    private long maxRestrictNights;

    /**
     * 必住日期 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private String mustInDate;

    /**
     * 平均价 add by haibo.li 电子地图二期
     */
    private double avlPrice;

    /**
     * 首日价格 add by haibo.li 电子地图二期
     */
    private double oneDayPrice;

    /**
     * ***********************hotel2.6加了8个参数@author
     * zhuangzhineng@end********************************
     */
    // 香港中科酒店一单最多可订房间数 ADD BY WUYUN 2009-03-25
    private int roomNumCts;

    // 底价 ADD BY WUYUN 2009-04-21
    private double basePrice;

    /*
     * 必须连住日期的关系,或者 or 并且 V2.9.2 addby chenjuesu
     */
    private String mustInDatesRelation;
	
	
	    // Normand add by shizhongwen 2009-10-03
    private double tmcPirce;
    
    
    /**
     * 是否根据连住优惠更改了售价 add by shengwei.zuo 2009-11-13
     */
    private boolean isChgSalePri;
    
    /**
     * 计算公式 
     * add by haibo.li
     * @return
     */
    private String formulaId;
    
    /**
     * 佣金
     * add by haibo.li
     */
    private double commission;
    
    /**
     * add by haibo.li
     * @return
     */
    private double commissionRate;
    
    private double agentComissionRate;
    
    private double agentComissionPrice;
    
    //显示给代理看的返佣率、
   
    private double agentReadComissionRate;
    
    //佣金价
    private double agentReadComissionPrice;
    
    //佣金额
    private double agentReadComission;
    
    private boolean favourableFlag;
    
    private String currency;
    
    private String curryncySymbol;
    
    public String getCurryncySymbol() {
		return curryncySymbol;
	}

	public void setCurryncySymbol(String curryncySymbol) {
		this.curryncySymbol = curryncySymbol;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getFormulaId() {
		return formulaId;
	}

	public void setFormulaId(String formulaId) {
		this.formulaId = formulaId;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public double getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionrate(double commissionrate) {
		this.commissionRate = commissionrate;
	}

	public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public int getRoomNumCts() {
        return roomNumCts;
    }

    public void setRoomNumCts(int roomNumCts) {
        this.roomNumCts = roomNumCts;
    }

    public int getCanBook() {
        return canBook;
    }

    public void setCanBook(int canBook) {
        this.canBook = canBook;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public int getBreakfastPrice() {
        return breakfastPrice;
    }

    public void setBreakfastPrice(int breakfastPrice) {
        this.breakfastPrice = breakfastPrice;
    }

    public int getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(int breakfastNum) {
        this.breakfastNum = breakfastNum;
    }

    public String getBreakfastType() {
        return breakfastType;
    }

    public void setBreakfastType(String breakfastType) {
        this.breakfastType = breakfastType;
    }

    public String getBreakfastStr() {
        return WebStrUtil.getWebBreakfastStr(this.breakfastNum);
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public double getRmbPrice() {
        return rmbPrice;
    }

    public void setRmbPrice(double rmbPrice) {
        this.rmbPrice = rmbPrice;
    }

    public int getAvailQty() {
        return availQty;
    }

    public void setAvailQty(int availQty) {
        this.availQty = availQty;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public Date getFellowDate() {
        return fellowDate;
    }

    public void setFellowDate(Date fellowDate) {
        setWeekDay(DateUtil.getWeekOfDate(fellowDate));
        this.fellowDate = fellowDate;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getRoomState() {
        return roomState;
    }

    public void setRoomState(String roomState) {
        this.roomStatus = roomState;
        if (null != roomState && 0 < roomState.length()) {
            this.roomState = WebStrUtil.strRoomStatue(roomState);
        } else {
            this.roomState = roomState;
        }
    }

    public long getPriceId() {
        return priceId;
    }

    public void setPriceId(long priceId) {
        this.priceId = priceId;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public String getRoomEquipment() {
        return roomEquipment;
    }

    public void setRoomEquipment(String roomEquipment) {
        this.roomEquipment = roomEquipment;
    }

    public double getSalesRoomPrice() {
        return salesRoomPrice;
    }

    public void setSalesRoomPrice(double salesRoomPrice) {
        this.salesRoomPrice = salesRoomPrice;
    }

    public String getClose_flag() {
        return close_flag;
    }

    public void setClose_flag(String close_flag) {
        this.close_flag = close_flag;
    }

    public int getCloseShowPrice() {
        return closeShowPrice;
    }

    public void setCloseShowPriceInfo(String reason) {
        if (null == reason) {
            this.closeShowPrice = 0;
            return;
        }
        // 因变价、订单/投诉、CC关房、装修原因关房的(子)房型不能显示
        if (reason.equals("1") || reason.equals("3") || reason.equals("7") || reason.equals("2")) {
            this.closeShowPrice = 1;
        } else {
            this.closeShowPrice = 0;
        }
    }

    public long getContinueDay() {
        return continueDay;
    }

    public void setContinueDay(long continueDay) {
        this.continueDay = continueDay;
    }

    public String getLatestBokableTime() {
        return latestBokableTime;
    }

    public void setLatestBokableTime(String latestBokableTime) {
        this.latestBokableTime = latestBokableTime;
    }

    public Date getLatestBookableDate() {
        return latestBookableDate;
    }

    public void setLatestBookableDate(Date latestBookableDate) {
        this.latestBookableDate = latestBookableDate;
    }

    public Date getMustFirstDate() {
        return mustFirstDate;
    }

    public void setMustFirstDate(Date mustFirstDate) {
        this.mustFirstDate = mustFirstDate;
    }

    public String getMustInDate() {
        return mustInDate;
    }

    public void setMustInDate(String mustInDate) {
        this.mustInDate = mustInDate;
    }

    public Date getMustLastDate() {
        return mustLastDate;
    }

    public void setMustLastDate(Date mustLastDate) {
        this.mustLastDate = mustLastDate;
    }

    public boolean isNeedAssure() {
        return needAssure;
    }

    public void setNeedAssure(boolean needAssure) {
        this.needAssure = needAssure;
    }

    public int getPayToPrepay() {
        return payToPrepay;
    }

    public void setPayToPrepay(int payToPrepay) {
        this.payToPrepay = payToPrepay;
    }

    public void setCloseShowPrice(int closeShowPrice) {
        this.closeShowPrice = closeShowPrice;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public double getAvlPrice() {
        return avlPrice;
    }

    public void setAvlPrice(double avlPrice) {
        this.avlPrice = avlPrice;
    }

    public double getOneDayPrice() {
        return oneDayPrice;
    }

    public void setOneDayPrice(double oneDayPrice) {
        this.oneDayPrice = oneDayPrice;
    }

    public void setMustInDatesRelation(String mustInDatesRelation) {
        // TODO Auto-generated method stub
        this.mustInDatesRelation = mustInDatesRelation;
    }

    public String getMustInDatesRelation() {
        return mustInDatesRelation;
    }

    public long getMinRestrictNights() {
        return minRestrictNights;
    }

    public void setMinRestrictNights(long minRestrictNights) {
        this.minRestrictNights = minRestrictNights;
    }

    public long getMaxRestrictNights() {
        return maxRestrictNights;
    }

    public void setMaxRestrictNights(long maxRestrictNights) {
        this.maxRestrictNights = maxRestrictNights;
    }

	public Date getFirstBookableDate() {
		return firstBookableDate;
	}

	public void setFirstBookableDate(Date firstBookableDate) {
		this.firstBookableDate = firstBookableDate;
	}

	public String getFirstBookableTime() {
		return firstBookableTime;
	}

	public void setFirstBookableTime(String firstBookableTime) {
		this.firstBookableTime = firstBookableTime;
	}

    public double getTmcPirce() {
        return tmcPirce;
    }

    public void setTmcPirce(double tmcPirce) {
        this.tmcPirce = tmcPirce;
    }
	
	public boolean isChgSalePri() {
		return isChgSalePri;
	}

	public void setChgSalePri(boolean isChgSalePri) {
		this.isChgSalePri = isChgSalePri;
	}

	public double getAgentComissionRate() {
		return agentComissionRate;
	}

	public void setAgentComissionRate(double agentComissionRate) {
		this.agentComissionRate = agentComissionRate;
	}

	public double getAgentComissionPrice() {
		return agentComissionPrice;
	}

	public void setAgentComissionPrice(double agentComissionPrice) {
		this.agentComissionPrice = agentComissionPrice;
	}

	public void setCommissionRate(double commissionRate) {
		this.commissionRate = commissionRate;
	}

	public double getAgentReadComissionRate() {
		return agentReadComissionRate;
	}

	public void setAgentReadComissionRate(double agentReadComissionRate) {
		this.agentReadComissionRate = agentReadComissionRate;
	}

	public double getAgentReadComissionPrice() {
		return agentReadComissionPrice;
	}

	public void setAgentReadComissionPrice(double agentReadComissionPrice) {
		this.agentReadComissionPrice = agentReadComissionPrice;
	}

	public double getAgentReadComission() {
		return agentReadComission;
	}

	public void setAgentReadComission(double agentReadComission) {
		this.agentReadComission = agentReadComission;
	}

	public boolean getFavourableFlag() {
		return favourableFlag;
	}

	public void setFavourableFlag(boolean favourableFlag) {
		this.favourableFlag = favourableFlag;
	}

	public double getAdvicePrice() {
		return advicePrice;
	}

	public void setAdvicePrice(double advicePrice) {
		this.advicePrice = advicePrice;
	}

	
}
