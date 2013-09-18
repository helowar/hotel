package com.mangocity.tmc.service.assistant;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 房型描述
 * @author bruce.yang
 * 
 */
public class RoomType {
	/**
	 * 房型ID
	 */
	private String roomTypeId;
	
	/**
	 * 子房型ID
	 */
	private String childRoomTypeId;
	
	/**
	 * 房型名称
	 */
	private String roomTypeName;
		
	/**
	 * 子房型名称
	 */
	private String childRoomTypeName;
	
	/**
	 * 门市价
	 */
	private String roomPrice;
	
	/**
	 * 推荐级别
	 */
	private String recommend;
	
	/**
	 * 销售明细 SaleItem类
	 */
	private List  saleItems = new ArrayList();
	
	/**
	 * 房型跨多少行 rowspan
	 */
	private int totalCount = 0;
	
	/**
	 * 首日价格
	 */
	private String firstDayPrice;
	/**
	 * 只有面付
	 */
	private boolean onlyPay;
    
	/**
	 * addby xulei.chen 2009.05.05
	 * 房间直联标志
	 */
	private String cooperate_channel;
    
    /**
     * 早餐数量<br>
     * 
     * @author chenkeming 
     */
    private int breakfastNum;
    
    /**
     * 宽带
     * 
     * @author chenkeming
     */
    private String net;    
    
    /** hotel2.9.2加了4个参数@author chenjiajie 2009-07-22 begin * */

    /**
     * 房间最多入住人数
     */
    private int maxPersons;

    /**
     * 加床数量
     */
    private int addBedQty;

    /**
     * 房间设施 结果是用,分隔
     */
    private String roomEquipment;

    /**
     * 房间其实设施 结果是用中文字符串
     */
    private String roomOtherEquipment;

    /** hotel2.9.2加了4个参数@author chenjiajie 2009-07-22 end * */
    
    /**
     * 关房标志
     */
    private String close_flag;
    
    /**
     * hotel2.6 面转预标记, 必须:1, 允许:2, 不许:3
     * 
     * @author shengwei.zuo 2009-03-30
     */
    private int payToPrepay;
    
    /**
     * 设置房型对应的床型列表，以逗号分隔;add by shengwei.zuo  2009-10-26
     */
    private String bedTypeStr;
	
	public String getFirstDayPrice() {
		if(this.saleItems.size() > 0){
			SaleItem saleItem = (SaleItem)this.saleItems.get(0);
			firstDayPrice = saleItem.getFirstDayPrice();
		}
		return firstDayPrice;
	}

	public String getChildRoomTypeId() {
		return childRoomTypeId;
	}

	public void setChildRoomTypeId(String childRoomTypeId) {
		this.childRoomTypeId = childRoomTypeId;
	}

	public String getChildRoomTypeName() {
		return childRoomTypeName;
	}

	public void setChildRoomTypeName(String childRoomTypeName) {
		this.childRoomTypeName = childRoomTypeName;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getRoomPrice() {
		return roomPrice;
	}

	public void setRoomPrice(String roomPrice) {
		this.roomPrice = roomPrice;
	}

	public String getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(String roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public String getRoomTypeName() {
		return roomTypeName;
	}

	public void setRoomTypeName(String roomTypeName) {
		this.roomTypeName = roomTypeName;
	}

	public List getSaleItems() {
		return saleItems;
	}

	public void setSaleItems(List saleItems) {
		this.saleItems = saleItems;
	}

	public int getTotalCount() {
		if(this.saleItems.size() > 0){
			SaleItem saleItem = (SaleItem)this.saleItems.get(0);
			totalCount = this.saleItems.size() * saleItem.getWeekNum();
		}
		return totalCount;
	}
	
	public void addSaleItem(Object o){
		this.saleItems.add(o);
	}
	
	public boolean getOnlyPay() {
		onlyPay = true;
		if(this.saleItems.size() > 0){
			Iterator itemIter = this.saleItems.iterator();
			while(itemIter.hasNext()){
				SaleItem saleItem = (SaleItem)itemIter.next();
				String payMethod = saleItem.getPayMethod();
				if(payMethod == null || "".equals(payMethod) || !"pay".equals(payMethod)){
					onlyPay = false;
					break;
				}
			}					
		}
		return onlyPay;
	}

	public String getCooperate_channel() {
		return cooperate_channel;
	}

	public void setCooperate_channel(String cooperate_channel) {
		this.cooperate_channel = cooperate_channel;
	}

    public int getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(int breakfastNum) {
        this.breakfastNum = breakfastNum;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public int getAddBedQty() {
        return addBedQty;
    }

    public void setAddBedQty(int addBedQty) {
        this.addBedQty = addBedQty;
    }

    public int getMaxPersons() {
        return maxPersons;
    }

    public void setMaxPersons(int maxPersons) {
        this.maxPersons = maxPersons;
    }

    public String getRoomEquipment() {
        return roomEquipment;
    }

    public void setRoomEquipment(String roomEquipment) {
        this.roomEquipment = roomEquipment;
    }

    public String getRoomOtherEquipment() {
        return roomOtherEquipment;
    }

    public void setRoomOtherEquipment(String roomOtherEquipment) {
        this.roomOtherEquipment = roomOtherEquipment;
    }

    public String getClose_flag() {
        return close_flag;
    }

    public void setClose_flag(String close_flag) {
        this.close_flag = close_flag;
    }

    public int getPayToPrepay() {
        return payToPrepay;
    }

    public void setPayToPrepay(int payToPrepay) {
        this.payToPrepay = payToPrepay;
    }

    public String getBedTypeStr() {
        return bedTypeStr;
    }

    public void setBedTypeStr(String bedTypeStr) {
        this.bedTypeStr = bedTypeStr;
    }
	
}
