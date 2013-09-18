package com.mango.hotel.ebooking.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlEbooking;
import com.mangocity.hotel.base.persistence.HtlEbookingFunctionMaster;
import com.mangocity.hotel.base.persistence.HtlEbookingPersonnelInfo;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.StringUtil;

/**
 */

@SuppressWarnings("serial")
public class HotelEbookingAction extends PersistenceAction {
    private long hotelId;

    private List htlEbookingList = new ArrayList();

    private HtlHotel htlHotel = new HtlHotel();

    private HotelManage hotelManage;

    private String whetherEbooking;

    private String whetherPrice;

    private String whetherRoomType;

    private String productManager;
    
    private String loginName;
    
    private Long personnelInfoId;
    
    private List personnelInfos;
    
    private List<HtlEbookingFunctionMaster> queryEbookingMaster;
    
    private HtlEbookingPersonnelInfo epInfo;
    
    private HtlEbooking htlEbooking = new HtlEbooking();

    protected Class getEntityClass() {
        return HtlEbooking.class;

    }

    public String getWhetherEbooking() {
        return whetherEbooking;
    }

    public void setWhetherEbooking(String whetherEbooking) {
        this.whetherEbooking = whetherEbooking;
    }

    public String getWhetherPrice() {
        return whetherPrice;
    }

    public void setWhetherPrice(String whetherPrice) {
        this.whetherPrice = whetherPrice;
    }

    public String getWhetherRoomType() {
        return whetherRoomType;
    }

    public void setWhetherRoomType(String whetherRoomType) {
        this.whetherRoomType = whetherRoomType;
    }
    /**
     * 删除用户
     * @return
     */
    public String deleteEBookingUserById(){
    	boolean suc = hotelManage.deleteEBookingUserbyId(personnelInfoId);
    	return "view";
    }
    /**
     * 查看酒店ebooking设置 add by haibo.li 酒店2.9
     * 
     * @return
     */
    public String ebookingView() {
        super.setEntityID(hotelId);
        super.setEntity(super.populateEntity());
        this.setHtlHotel((HtlHotel) this.getEntity());
        try {
            htlHotel = hotelManage.findHotel(hotelId);
            htlEbookingList = hotelManage.findHtlEbooking(hotelId);
            personnelInfos = hotelManage.getEBookingUsersByHotelId(String.valueOf(hotelId));
            for(int i=0;i<personnelInfos.size();i++){
            	HtlEbookingPersonnelInfo ep = (HtlEbookingPersonnelInfo)personnelInfos.get(i);
            	if("1".equals(ep.getSuperper().toString())){
            		epInfo = ep;
            	}
            }
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }

        return "view";
    }
    /**
     * 查看E-Booking用户信息
     * @return
     */
    public String ebookingUserView(){
    	try {
			queryEbookingMaster = hotelManage.queryEbookingMaster(String.valueOf(hotelId), loginName);
		} catch (RuntimeException e) {
			log.error(e.getMessage(),e);
		}
    	return "viewmaster";
    }

    /**
     * 保存酒店ebooking设置 add by haibo.li 酒店2.9
     */
    public String ebookingSave() {

        super.setEntity(super.populateEntity());
        this.setHtlEbooking((HtlEbooking) this.getEntity());
        
        try {
        	Map params = super.getParams();
            htlHotel = hotelManage.findHotel(hotelId);
            String name = (String)params.get("superLoginName");
            if("1".equals(whetherEbooking) && StringUtil.isValidStr(name)){
            	//设置Ebooking超级用户
            	String pass = (String)params.get("superLoginPwd");
            	if(!StringUtil.isValidStr(pass))//密码为空，则默认为登陆名
            		pass = name;
            	roleUser = getOnlineRoleUser();
            	if(null == roleUser)
            		return forwardMsg("登陆超时，请重新登陆！");
            	hotelManage.setEbookingSupderUser(htlHotel,roleUser.getLoginName(),roleUser.getName(),name,pass);
            }

            htlEbookingList = hotelManage.findHtlEbooking(hotelId);
            String whetherContack = (String)params.get("whetherContack");
            String whetherPayIncrease = (String)params.get("whetherPayIncrease");
            if (0 < htlEbookingList.size()) {
                htlEbooking = (HtlEbooking) htlEbookingList.get(0);
                htlEbooking.setWhetherEbooking(whetherEbooking);
                htlEbooking.setWhetherPrice(whetherPrice);
                htlEbooking.setWhetherRoomType(whetherRoomType);
                htlEbooking.setWhetherContack(whetherContack);
                htlEbooking.setWhetherPayIncrease(whetherPayIncrease);
                htlEbooking.setHtlHotel(htlHotel);
                hotelManage.saveHtlEbooking(htlEbooking);
            } else {
                htlEbooking.setWhetherEbooking(whetherEbooking);
                htlEbooking.setWhetherPrice(whetherPrice);
                htlEbooking.setWhetherRoomType(whetherRoomType);
                htlEbooking.setWhetherContack(whetherContack);
                htlEbooking.setWhetherPayIncrease(whetherPayIncrease);
                htlEbooking.setHtlHotel(htlHotel);
                hotelManage.saveHtlEbooking(htlEbooking);
            }

        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }

        return "review";
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

    public String getProductManager() {
        return productManager;
    }

    public void setProductManager(String productManager) {
        this.productManager = productManager;
    }

    public List getHtlEbookingList() {
        return htlEbookingList;
    }

    public void setHtlEbookingList(List htlEbookingList) {
        this.htlEbookingList = htlEbookingList;
    }

    public HtlEbooking getHtlEbooking() {
        return htlEbooking;
    }

    public void setHtlEbooking(HtlEbooking htlEbooking) {
        this.htlEbooking = htlEbooking;
    }

	public Long getPersonnelInfoId() {
		return personnelInfoId;
	}

	public void setPersonnelInfoId(Long personnelInfoId) {
		this.personnelInfoId = personnelInfoId;
	}

	public List getPersonnelInfos() {
		return personnelInfos;
	}

	public void setPersonnelInfos(List personnelInfos) {
		this.personnelInfos = personnelInfos;
	}

	public HtlEbookingPersonnelInfo getEpInfo() {
		return epInfo;
	}

	public void setEpInfo(HtlEbookingPersonnelInfo epInfo) {
		this.epInfo = epInfo;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public List<HtlEbookingFunctionMaster> getQueryEbookingMaster() {
		return queryEbookingMaster;
	}

	public void setQueryEbookingMaster(
			List<HtlEbookingFunctionMaster> queryEbookingMaster) {
		this.queryEbookingMaster = queryEbookingMaster;
	}

}
