package com.mangocity.hagtb2b.service.impl;

import com.mangocity.hagtb2b.service.IB2bInterfaceService;
import com.mangocity.hagtb2b.service.IHtlB2bService;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.util.hotel.constant.FromChannelType;


/**
 * 调用hessian接口service 
 * add by zhiejie.gu 2010-8-20
 */
public class B2bInterfaceServiceImpl  implements IB2bInterfaceService {
	
	private IHtlB2bService htlB2bService;
	
	private HotelManageWeb hotelManageWeb;
	
	/**
	 * b2b酒店列表查询
	 * @param queryHotelForWebBean
	 * @return
	 * @throws Exception
	 */
	public HotelPageForWebBean queryHotelsForWeb(QueryHotelForWebBean queryHotelForWebBean) throws Exception{
		
		HotelPageForWebBean hotelPageForWebBean = hotelManageWeb.queryHotelsForWeb(queryHotelForWebBean);
	    
		if(hotelPageForWebBean != null) {
		    //b2b取消优惠立减，按合同价格在b2b销售。add by zhijie.gu 2010-9-15
		    hotelPageForWebBean = htlB2bService.setBenefitFlagAllFlase(hotelPageForWebBean);
		    
		    //如果是预付+底价的，按预付底价+加幅在B2B销售 add by xuyiwen 2010-12-2
		    hotelPageForWebBean = htlB2bService.judgeB2BIncrease(hotelPageForWebBean, queryHotelForWebBean.getInDate(), 
		    		queryHotelForWebBean.getOutDate());
		}
		return hotelPageForWebBean;
	}
	 
	/**
	 * b2b酒店详情页面调用查询
	 * @param queryHotelForWebBean
	 * @return
	 * @throws Exception
	 */
	public QueryHotelForWebResult queryHotelInfoBeanForWebs(QueryHotelForWebBean queryBean)throws Exception{
		QueryHotelForWebResult queryHotelForWebResult = new QueryHotelForWebResult();
        // 设置查询渠道来源
        queryBean.setFromChannel(FromChannelType.WEB);
        HotelPageForWebBean hotelPageForWebBean = hotelManageWeb.queryHotelsForWeb(queryBean);
        
        if(hotelPageForWebBean != null) {
	        //b2b取消优惠立减，按合同价格在b2b销售。add by zhijie.gu 2010-9-15
		    hotelPageForWebBean = htlB2bService.setBenefitFlagAllFlase(hotelPageForWebBean);
		    
		  //如果是预付+底价的，按预付底价+加幅在B2B销售 add by xuyiwen 2010-12-2
		    hotelPageForWebBean = htlB2bService.judgeB2BIncrease(hotelPageForWebBean, 
		    		queryBean.getInDate(), queryBean.getOutDate());
		    
	        // 根据酒店ID查询，因此只会返回一家酒店 add by CMB
	        if (null != hotelPageForWebBean.getList() && 0 < hotelPageForWebBean.getList().size()) {
	            queryHotelForWebResult = hotelPageForWebBean.getList().get(0);
	        }
        }
        
        return queryHotelForWebResult;
		
	}
	/**
	 * b2b单独查询分页信息页数，页码，总记录数
	 * @param queryHotelForWebBean
	 * @return
	 * @throws Exception
	 */
	public HotelPageForWebBean queryHotelPagesForWeb(QueryHotelForWebBean queryHotelForWebBean)throws Exception{
		HotelPageForWebBean hotelPageForWebBean = null;
        //参数非空判断
        if(null != queryHotelForWebBean){
        	hotelPageForWebBean = hotelManageWeb.queryHotelPagesForWeb(queryHotelForWebBean);	    
	        if(hotelPageForWebBean != null) {
			    //b2b取消优惠立减，按合同价格在b2b销售。add by zhijie.gu 2010-9-15
			    hotelPageForWebBean = htlB2bService.setBenefitFlagAllFlase(hotelPageForWebBean);
			    
			  //如果是预付+底价的，按预付底价+加幅在B2B销售 add by xuyiwen 2010-12-2
			    hotelPageForWebBean = htlB2bService.judgeB2BIncrease(hotelPageForWebBean, 
			    		queryHotelForWebBean.getInDate(), queryHotelForWebBean.getOutDate());
	        }
        }
        
		return hotelPageForWebBean;
	}

	public IHtlB2bService getHtlB2bService() {
		return htlB2bService;
	}

	public void setHtlB2bService(IHtlB2bService htlB2bService) {
		this.htlB2bService = htlB2bService;
	}

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}
	
}
