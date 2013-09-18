package com.mangocity.hagtb2b.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import com.mangocity.hagtb2b.persistence.HtlB2bTempComminfo;
import com.mangocity.hagtb2b.persistence.HtlB2bTempComminfoItem;
import com.mangocity.hotel.base.persistence.CommisionAdjust;
import com.mangocity.hotel.base.persistence.CommisionSet;
import com.mangocity.hotel.order.persistence.B2bModifyOrderInfo;

public interface IB2bService {
	 public void updateOrder(B2bModifyOrderInfo b2border);
	 
	 
	 
		/**
		 * 批量更新佣金设置公式
		 * add by yong.zeng
		 * 2010.3.16
		 * @param commLst
		 */
		public void batchUpdate(List commLst);
		   /**
	     * 根据类名和ID来删除对象
	     */
	    public void remove(Class clazz, Long id);
		/**
		 * 
		 * 增加或更新佣金设置公式
		 * add by yong.zeng 2010-3-16
		 * @param commSet
		 */

		public void updateCommSet(CommisionSet commSet);
		/**
		 * 调整佣金
		 * add by yong.zeng 2010-3-16
		 * @param commAdjust
		 */
		public void updateCommAdjust(CommisionAdjust commAdjust);
		
	/**
	 * 
	 * @param ID
	 * @return
	 */
		public CommisionSet getCommSet(Long ID);
	
		/**
		 * 
		 * @param ID
		 * @return
		 */
		public CommisionAdjust getCommAdjust(Long ID);
		
		/**
		 * 根据B2bID得到佣金设置列表
		 * @param B2bCD
		 * @return
		 */
		public List<CommisionSet> getCommSetByB2B(String B2bCD);
		
		/**
		 * 根据B2bID得到Adjust列表
		 * @param B2bCD
		 * @return
		 */
			
		public List<CommisionAdjust> getCommAdjustByB2B(String B2bCD);
		
		
		/**
		 * 根据B2bCd和hotelId得到Adjust列表
		 * add by yong.zeng
		 * @param B2bCD
		 * @param hotelId
		 * @return
		 */
		public List<CommisionAdjust> getCommAdjustByB2BHotel(String B2bCD,Long hotelId);
		/**
		 * 根据hsql得到CommisionAdjust列表
		 * @param hsql
		 * @return
		 */
		public List<CommisionAdjust> getCommAdjustLst(String hsql);
		
		/**
		 * 支持sql语句更新,返回更新记录数
		 * add by yong.zeng
		 */
		public int sqlUpdate(final String sql);
		public boolean confirmToMid(long orderId, String operaterId,String state);
		
		public boolean cancelOrderForB2BMinPricePay(long orderId, String operaterId);
		
		/**
		 * 保存模板对象
		 * add by zhijie.gu
		 * @return
		 */
		public void saveOrUpdateComTemp(HtlB2bTempComminfo htlB2bTempComminfo);
		
		/**
		 * 根据hotelId得到HtlB2bTempComminfoItem列表
		 * add by zhijie.gu
		 * @param hotelId
		 * @return
		 */
		public List<HtlB2bTempComminfoItem> getCommTempByHotel(Long hotelId,Long tempId);
		
		/**
		 * 调整佣金
		 * add by zhijie.gu 2010-3-16
		 * @param commTempItem
		 */
		public void updateCommAdjust(HtlB2bTempComminfoItem commTempItem);
		
		
		public void adjustUtil(HtlB2bTempComminfoItem addComm,List<HtlB2bTempComminfoItem> oldComms)throws IllegalAccessException, InvocationTargetException;
		
		/**
	     * 得到DB中已存在的日期区间
	     * @param ca
	     * @return
	     */
		public List<HtlB2bTempComminfoItem> getDateCops(HtlB2bTempComminfoItem ca);
		
		/**
	     * 根据模板id获取数据
	     * @param tempId
	     * @return List<HtlB2bTempComminfoItem>
		 * @throws Exception 
	     */
		public List<HtlB2bTempComminfoItem> getcommTempItemByTempId(Long tempId) throws Exception;
		
		/**
	     * 根据模板id获取数据
	     * @param tempId
	     * @return List<HtlB2bTempComminfo>
	     */
		public HtlB2bTempComminfo getcommTempTempId(Long tempId);
		
		/**
	     * 根据模板id获取数据
	     * @param tempId
	     */
		public void deleteCommTempByTempId(Long tempId,String loginChnName,String loginId);
		
		/**
		 * 校验模板名字是否存在
		 * @param temName
		 * @return
		 */
		public boolean checkTempNameIsExist(String temName);
		
		/**
		 * 填充酒店名称和价格类型名称
		 * @param hotelComTempList
		 */
		public  List fillHotelNameAndPriceTypeName(List<HtlB2bTempComminfoItem> hotelComTempList);



		public int getPolicyScope(String memberCd);



		public boolean isSetCommission(String memberCd, int intStar,
				Date night, Long hotelId, Long roomTypeId,
				Long childRoomTypeId, String payMo);


		/**
		 * 根据hotelId查找佣金调整list
		 * add by luoguangming
		 * @param hotelId
		 * @return
		 */
		public List<CommisionAdjust> getAllCommAdjustByHotelId(Long hotelId);
		/**
		 * 根据hotelId查找佣金调整list，去重复，只去每个价格类型的最新调整
		 * add by luoguangming
		 * @param hotelId
		 * @return
		 */
		public List<CommisionAdjust> getDistinctCommAdjustByHotelId(Long hotelId);
}
