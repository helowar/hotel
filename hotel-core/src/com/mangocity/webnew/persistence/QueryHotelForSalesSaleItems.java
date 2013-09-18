package com.mangocity.webnew.persistence;

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
public class QueryHotelForSalesSaleItems implements Serializable {
	
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
     * 销售价
     */
    private int saleNum;
    
    /**
     * 即时确认标记
     */
    private int confrimFlag;

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

    public String getRoomStatus() {
        return roomStatus;
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

	public int getConfrimFlag() {
		return confrimFlag;
	}

	public void setConfrimFlag(int confrimFlag) {
		this.confrimFlag = confrimFlag;
	}

	public int getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}

	public void setFellowDate(Date fellowDate) {
		this.fellowDate = fellowDate;
	}

	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}

	
}
