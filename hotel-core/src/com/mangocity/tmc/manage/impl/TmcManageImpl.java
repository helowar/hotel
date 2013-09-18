package com.mangocity.tmc.manage.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.hweb.persistence.QueryHotelForWebRoomType;
import com.mangocity.tmc.manage.TmcManage;
import com.mangocity.tmc.parameter.SqlMappContractParameter;
import com.mangocity.tmc.persistence.TbhotelContract;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;

/**
 * tmc应用实现类
 * @author:shizhongwen
 * 创建日期:Sep 24, 2009,5:57:46 PM
 * 描述：
 */
public class TmcManageImpl implements TmcManage {
	private static final MyLog log = MyLog.getLogger(TmcManageImpl.class);

    private DAOIbatisImpl queryDao;
    
    /**
     * manage类
     */
    private HotelManageWeb hotelManageWeb;
    
    /**
     * 封装了页信息的结果类
     */
    private HotelPageForWebBean hotelPageForWebBean;
    
    
    /**
     * 查询结果类
     */
    private List<QueryHotelForWebResult> webHotelResultLis = 
                                                new ArrayList<QueryHotelForWebResult>();

    
    /**
     * 根据商旅公司ID，酒店ID，入住日期，离店日期查询商旅公司与酒店签订的三方协义合同
     * add by shizhongwen
     * 时间:Sep 24, 2009  5:32:27 PM
     * @param companyid //商旅公司ID 
     * @param hotelid //酒店ID
     * @param startDate //入住日期
     * @param endDate //离店日期
     * @return
     */
    public TbhotelContract getTbhotelContract(Long companyid, Long hotelid, Date startDate, Date endDate) {
        TbhotelContract contrat=null;
        SqlMappContractParameter parameter=new SqlMappContractParameter();
        parameter.setCompanyid(companyid);
        parameter.setHotelid(hotelid);        
        parameter.setStartDate(DateUtil.toStringByFormat(startDate, "yyyy-MM-dd"));
        parameter.setEndDate(DateUtil.toStringByFormat(endDate, "yyyy-MM-dd"));
        try{
            contrat=(TbhotelContract)queryDao.queryForObject("qryTMCHotelContract",parameter);
        }catch(Exception e){
            log.error("根据商旅公司ID，酒店ID，入住日期，离店日期查询商旅公司与酒店签订的三方协义合同 出错"+e);
            
        }
        return contrat;
    }

   /**
    * 判断此酒店是否为此商旅公司的三方协议酒店
    * add by shizhongwen
    * 时间:Sep 24, 2009  5:45:32 PM
    * @param companyid
    * @param hotelid
    * @param startDate
    * @param endDate
    * @return
    */
    public boolean isAgreementHotel(Long companyid, Long hotelid, Date startDate, Date endDate) {
        TbhotelContract contrat=null;
            contrat=this.getTbhotelContract(companyid,hotelid,startDate,endDate);
            if(null!=contrat){
                return true;
            }else{
                return false;
            }
    }
    
    
    /**
     * 根据酒店网站查询条件类查询此酒店是否在散客里也有
     * add by shizhongwen
     * 时间:Sep 27, 2009  4:09:05 PM
     * @param queryHotelForWebBean
     * @return
     */
    public boolean isPriceTypeOfHotel(QueryHotelForWebBean queryHotelForWebBean){
        boolean pricetag=true;
        hotelPageForWebBean = hotelManageWeb.queryHotelsForWeb(queryHotelForWebBean);
        webHotelResultLis = hotelPageForWebBean.getList();
        if(null==webHotelResultLis||webHotelResultLis.size()<=0){
            return false;
        }
        for(QueryHotelForWebResult webHotelInfo:webHotelResultLis){
            List roomTypes = new ArrayList();
            roomTypes=webHotelInfo.getRoomTypes();
            if(null==roomTypes||roomTypes.size()<=0){//房型不存在
                pricetag=false;
            }else{//房型存在
              for(Object roomtypeobject:roomTypes){
                  QueryHotelForWebRoomType roomtype=(QueryHotelForWebRoomType)roomtypeobject;
                  if((null!=roomtype.getSaleItems()&&roomtype.getSaleItems().size()>0)
                      ||(null!=roomtype.getFaceItems()&&roomtype.getFaceItems().size()>0)){//价格类型存在
                      pricetag=true;
                      return pricetag;
                  }else{//价格类型不存在
                      pricetag=false;
                  }
              }
            }
        }
        return true;
    }

    public DAOIbatisImpl getQueryDao() {
        return queryDao;
    }

    public void setQueryDao(DAOIbatisImpl queryDao) {
        this.queryDao = queryDao;
    }

    public HotelManageWeb getHotelManageWeb() {
        return hotelManageWeb;
    }

    public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
        this.hotelManageWeb = hotelManageWeb;
    }
}
