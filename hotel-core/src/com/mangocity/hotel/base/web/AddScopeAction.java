package com.mangocity.hotel.base.web;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.IPriceManage;
import com.mangocity.hotel.base.persistence.HtlHdlAddscope;
import com.mangocity.hotel.base.service.HotelRoomTypeService;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.DateSegment;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * @author zhuangzhineng
 * 
 */
public class AddScopeAction extends PersistenceAction {

    /*
     * 房型信息
     */
    private List lstRoomType;

    private HotelManage hotelManage;
    
    private HotelRoomTypeService hotelRoomTypeService;

    private IPriceManage priceManage;

    /*
     * 酒店ID
     */
    private long hotelId;

    /*
     * 行数
     */
    private int rowNum;

    protected Class getEntityClass() {
        return HtlHdlAddscope.class;
    }

    /**
     * 跳转到加幅页面
     * 
     * @return
     */
    public String forward() {
        super.setEntity(super.populateEntity());
        lstRoomType = hotelRoomTypeService.getHtlRoomTypeListByHotelId(hotelId);
        return "toAddScope";
    }

    /**
     * 保存新增修改加幅信息
     * 
     * @return
     */
    public String saveOrUpdate() {
        super.setEntity(super.populateEntity());
        HtlHdlAddscope htlHdlAddscope = (HtlHdlAddscope) super.getEntity();
        if (null != super.getOnlineRoleUser()) {
            htlHdlAddscope.setModify_by(super.getOnlineRoleUser().getName());
            htlHdlAddscope.setModify_by_id(super.getOnlineRoleUser().getLoginName());
            htlHdlAddscope.setModify_time(DateUtil.getSystemDate());
        }
        
        String isSave = "";
        if (null == htlHdlAddscope || 0 == htlHdlAddscope.getID()) {
        	Map paramsDate = super.getParams();
            List dateLst = MyBeanUtil.getBatchObjectFromParam(paramsDate, DateSegment.class, rowNum);
        	DateSegment ds = new DateSegment();
            for (int i = 0; i < dateLst.size(); i++) {
                ds = (DateSegment) dateLst.get(i);
                htlHdlAddscope.setBeginDate(ds.getStart());
                htlHdlAddscope.setEndDate(ds.getEnd());
                isSave = priceManage.saveOrUpdateAddScope(htlHdlAddscope);
            }
        } else {
            isSave = priceManage.saveOrUpdateAddScope(htlHdlAddscope);
        }
        if (isSave.equals("1")) {
            return SAVE_SUCCESS;
        } else {
            return "forwardToError";
        }
    }

    public List getLstRoomType() {
        return lstRoomType;
    }

    public void setLstRoomType(List lstRoomType) {
        this.lstRoomType = lstRoomType;
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

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public IPriceManage getPriceManage() {
        return priceManage;
    }

    public void setPriceManage(IPriceManage priceManage) {
        this.priceManage = priceManage;
    }

	public void setHotelRoomTypeService(HotelRoomTypeService hotelRoomTypeService) {
		this.hotelRoomTypeService = hotelRoomTypeService;
	}

}
