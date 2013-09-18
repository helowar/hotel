package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlIncreasePrice;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 */
public class IncreasePriceAction extends PersistenceAction {

    /**
     * 加幅记录
     */
    private List<HtlIncreasePrice> increasePriceList = new ArrayList<HtlIncreasePrice>();

    /**
     * 酒店管理类
     */
    private HotelManage hotelManage;

    /**
     * 记录数
     */
    private int rowNum;

    /**
     * 酒店id hotel 2.9.2 add by chenjiajie 2009-08-03
     */
    private Long hotelIdForWeb;

    /**
     * 渠道类型 hotel 2.9.2 add by chenjiajie 2009-08-03
     */
    private String channelTypeForWeb;

    /**
     * 批量保存的酒店id 用,分隔 hotel 2.9.2 add by chenjiajie 2009-08-03
     */
    private String hotelIds;

    /**
     * 进入设置中旅房型的类别 1：单个，2：批量 hotel 2.9.2 add by chenjiajie 2009-08-03
     */
    private int editType;

    /**
     * hotel 2.9.2 查看某酒店的加幅设置 modify by chenjiajie 2009-08-03
     */
    public String view() {
        // 如果界面传入的酒店id和渠道号为空，则跳转到错误页面
        if (null == hotelIdForWeb || !StringUtil.isValidStr(channelTypeForWeb)) {
            return super.forwardError("无法获取酒店Id，请重新进入");
        }
        // 根据酒店ID和渠道取出该酒店的加幅记录
        increasePriceList = hotelManage.queryHtlIncreasePrice(hotelIdForWeb, channelTypeForWeb);
        rowNum = increasePriceList.size();
        return VIEW;
    }

    /**
     * 保存或更新加幅记录 modify by chenjiajie 2009-08-03
     */
    public String save() {
        Map params = super.getParams();
        increasePriceList = MyBeanUtil.getBatchObjectFromParam(params, HtlIncreasePrice.class,
            rowNum);
        List<HtlIncreasePrice> tempList = new ArrayList<HtlIncreasePrice>();
        for (HtlIncreasePrice item : increasePriceList) {
            if (null != item.getChannelType()) {
                if (null != super.getOnlineRoleUser()) {
                    if (null == item.getID() || 0 == item.getID()) {
                        item.setCreateBy(super.getOnlineRoleUser().getName());
                        item.setCreateById(super.getOnlineRoleUser().getLoginName());
                        item.setCreateTime(new Date());
                    }
                    item.setModifyBy(super.getOnlineRoleUser().getName());
                    item.setModifyById(super.getOnlineRoleUser().getLoginName());
                }
                item.setModifyTime(new Date());
                item.setHotelId(hotelIdForWeb);
                tempList.add(item);
            }
        }
        // 保存更新加幅记录 modify by chenjiajie 2009-08-03
        hotelManage.saveOrUpdateHtlIncreasePrice(tempList, hotelIdForWeb, channelTypeForWeb);
        return view();
    }

    /**
     * 进入批量设置页面 add by chenjiajie 2009-08-03
     * 
     * @return
     */
    public String viewBatch() {
        // 如果界面传入的酒店id和渠道号为空，则跳转到错误页面
        if (null == hotelIds || !StringUtil.isValidStr(channelTypeForWeb)) {
            return super.forwardError("无法获取酒店Id，请重新进入");
        }
        // 批量查询酒店的基本信息
        List hotelInfoList = hotelManage.queryHotelInfoBatch(hotelIds);
        request.setAttribute("hotelInfoList", hotelInfoList);
        return "editIncreasePriceBatch";
    }

    /**
     * 批量保存中旅加幅 add by chenjiajie 2009-08-03
     * 
     * @return
     */
    public String batchSave() {
        Map params = super.getParams();
        increasePriceList = MyBeanUtil.getBatchObjectFromParam(params, HtlIncreasePrice.class,
            rowNum);
        try {
            // 批量更新加幅
            if (StringUtil.isValidStr(hotelIds)) {
                // 先删除酒店存在的加幅记录
                hotelManage.deleteHtlIncreasePriceBatch(hotelIds);
                String[] hotelIdStr = hotelIds.split(",");
                for (int i = 0; i < hotelIdStr.length; i++) {
                    if (StringUtil.isValidStr(hotelIdStr[i])) {
                        // 单个酒店保存的List
                        List<HtlIncreasePrice> tempList = new ArrayList<HtlIncreasePrice>();
                        for (HtlIncreasePrice item : increasePriceList) {
                            if (null != item.getChannelType()) {
                                if (null != super.getOnlineRoleUser()) {
                                    if (null == item.getID() || 0 == item.getID()) {
                                        item.setCreateBy(super.getOnlineRoleUser().getName());
                                        item.setCreateById(
                                            super.getOnlineRoleUser().getLoginName());
                                        item.setCreateTime(new Date());
                                    }
                                    item.setModifyBy(super.getOnlineRoleUser().getName());
                                    item.setModifyById(super.getOnlineRoleUser().getLoginName());
                                }
                                item.setModifyTime(new Date());
                                item.setHotelId(Long.parseLong(hotelIdStr[i]));
                                tempList.add(item);
                            }
                        }
                        // 保存更新加幅记录 modify by chenjiajie 2009-08-03
                        hotelManage.saveOrUpdateHtlIncreasePrice(tempList, Long
                            .parseLong(hotelIdStr[i]), channelTypeForWeb);
                    }
                }
            }
            request.setAttribute("msg", "批量录入成功");
        } catch (Exception e) {
            log.error("2.9.2批量录入中旅加幅失败", e);
            return super.forwardError("批量录入失败");
        }
        return viewBatch();
    }

    /** getter and setter **/
    public List<HtlIncreasePrice> getIncreasePriceList() {
        return increasePriceList;
    }

    public void setIncreasePriceList(List<HtlIncreasePrice> increasePriceList) {
        this.increasePriceList = increasePriceList;
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

    public Long getHotelIdForWeb() {
        return hotelIdForWeb;
    }

    public void setHotelIdForWeb(Long hotelIdForWeb) {
        this.hotelIdForWeb = hotelIdForWeb;
    }

    public String getChannelTypeForWeb() {
        return channelTypeForWeb;
    }

    public void setChannelTypeForWeb(String channelTypeForWeb) {
        this.channelTypeForWeb = channelTypeForWeb;
    }

    public String getHotelIds() {
        return hotelIds;
    }

    public void setHotelIds(String hotelIds) {
        this.hotelIds = hotelIds;
    }

    public int getEditType() {
        return editType;
    }

    public void setEditType(int editType) {
        this.editType = editType;
    }

}
