package com.mangocity.outservices.hotelquery.impl;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.service.assistant.HotelInfo;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelInfoForWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.outservices.hotelquery.IHotelQueryCcService;
import com.mangocity.outservices.hotelquery.IHotelQueryWebService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.log.MyLog;

/**
 * 酒店对外查询接口基础接口实现
 * @author chenjiajie
 *
 */
public class HotelQueryOutServiceImpl implements IHotelQueryCcService,IHotelQueryWebService {

	private static final MyLog log = MyLog.getLogger(HotelQueryOutServiceImpl.class);
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6495227825252806291L;
    
    /**
     * 酒店网站查询manager
     */
    private HotelManageWeb hotelManageWeb;

    /**
     * 网站根据酒店ID查询某家酒店的信息
     * @param hotelId 酒店ID
     * @param params 参数列表(key,value)见下方
     * <ul>
     *  <li>哪个渠道调用查询(KEY_QUERY_CHANNEL,CHANNEL_*)</li>
     * </ul>
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public HotelInfoForWeb queryHotelInfoForWeb(Long hotelId, Map params)
            throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * cc查询酒店
     * @param condition CC查询条件
     * @param params 参数列表(key,value)见下方
     * <ul>
     *  <li>哪个渠道调用查询(KEY_QUERY_CHANNEL,CHANNEL_*)</li>
     * </ul>
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<HotelInfo> queryHotelsForCc(
            HotelQueryCondition condition, Map params) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * cc单独查询分页信息页数，页码，总记录数
     * @param condition CC查询条件
     * @param params 参数列表(key,value)见下方
     * <ul>
     *  <li>哪个渠道调用查询(KEY_QUERY_CHANNEL,CHANNEL_*)</li>
     * </ul>
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<HotelInfo> queryHotelPagesForCc(
            HotelQueryCondition condition,Map params) throws Exception{
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 查询散客网站酒店
     * @param queryBean 网站查询条件
     * @param params 参数列表(key,value)见下方
     * <ul>
     *  <li>哪个渠道调用查询(KEY_QUERY_CHANNEL,CHANNEL_*)</li>
     * </ul>
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public HotelPageForWebBean queryHotelsForWeb(
            QueryHotelForWebBean queryBean, Map params) throws Exception {
        HotelPageForWebBean hotelPageForWebBean = new HotelPageForWebBean();
        //参数非空判断
        if(null != queryBean && null != params){
            //查询接口调用方
            String queryChannel = (String) params.get(KEY_QUERY_CHANNEL);
            log.info("Channel:" + queryChannel + ",网站开始查询酒店:" + queryBean.getCityId() + "," + 
                    DateUtil.dateToString(queryBean.getInDate()) + "," + 
                    DateUtil.dateToString(queryBean.getOutDate()));
            hotelPageForWebBean = hotelManageWeb.queryHotelsForWeb(queryBean);
            log.info("Channel:" + queryChannel + ",本次查询结果酒店数:" + hotelPageForWebBean.getList().size());
        }
        return hotelPageForWebBean;
    }
    
    /**
     * 网站单独查询分页信息页数，页码，总记录数
     * @param queryBean 网站查询条件
     * @param params 参数列表(key,value)见下方
     * <ul>
     *  <li>哪个渠道调用查询(KEY_QUERY_CHANNEL,CHANNEL_*)</li>
     * </ul>
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public HotelPageForWebBean queryHotelPagesForWeb(
            QueryHotelForWebBean queryBean,Map params) throws Exception{
        HotelPageForWebBean hotelPageForWebBean = new HotelPageForWebBean();
        //参数非空判断
        if(null != queryBean && null != params){
            //查询接口调用方
            String queryChannel = (String) params.get(KEY_QUERY_CHANNEL);
            log.info("Channel:" + queryChannel + ",网站开始查询酒店分页信息:" + queryBean.getCityId() + "," + 
                    DateUtil.dateToString(queryBean.getInDate()) + "," + 
                    DateUtil.dateToString(queryBean.getOutDate()));
            hotelPageForWebBean = hotelManageWeb.queryHotelPagesForWeb(queryBean);
        }
        return hotelPageForWebBean;
    }

    /**
     * 网站查询主题酒店
     * @param qureyBean
     * @param params 参数列表(key,value)见下方
     * <ul>
     *  <li>哪个渠道调用查询(KEY_QUERY_CHANNEL,CHANNEL_*)</li>
     * </ul>
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public HotelPageForWebBean queryThemeHotelsForWeb(
            QueryHotelForWebBean qureyBean, Map params) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 测试方法,对qureyBean的cityId操作并返回
     * @param qureyBean
     * @return
     */
    public String testHessian(QueryHotelForWebBean qureyBean) {
        return "HessionRemot:" + qureyBean.getCityId();
    }

    /* getter and setter */
    public HotelManageWeb getHotelManageWeb() {
        return hotelManageWeb;
    }

    public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
        this.hotelManageWeb = hotelManageWeb;
    }

}
