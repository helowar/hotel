package com.mangocity.hotel.base.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mangocity.hotel.base.persistence.QueryCashBackControl;
import com.mangocity.hotel.base.service.ChannelCashBackManagerService;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;

/**
 * 
 * 渠道返利管理类
 * 
 * @author hushunqiang
 * @since v1.0
 */

public class ChannelCashBackManagerServiceImpl implements ChannelCashBackManagerService{
	private static final MyLog log = MyLog.getLogger(ChannelCashBackManagerServiceImpl.class);
    /**
     * ibatis查询dao
     */
	private DAOIbatisImpl queryDao;
    
    /**
     * 根据传入的查询条件，查询满足条件的记录并处理
     * 
     * @param projectcode
     *            渠道编码
     * @return Double
     * 			  通过渠道设定的返现比例处理后的返利比例
     * @author hushunqiang
     * @date 2013-04-20 15:15:00
     */
    public double getChannelCashBacktRate(String projectcode) {
    	
    	double cashbackrate = 1;
    	QueryCashBackControl queryCashBackControl = new QueryCashBackControl();
    	//1,判断输入条件，如果输入渠道编码为空，则返利金额不做处理，如果返现金额为0，则不做处理。
    	if("".equals(projectcode)||projectcode == null || projectcode ==""||"null".equals(projectcode)){
    		log.info("操作渠道来源：芒果网站");
    		return cashbackrate;
    	}
    	log.info("-----渠道号："+projectcode+",获取渠道返利率-----start");
    	//2,组装渠道返利控制输入条件，projectcode表示渠道编号，groupstatus表示渠道返利控制是否生效
    	Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("projectcode", projectcode);
		paramMap.put("groupstatus", 1);
		List list = null;
		//3,开始查询
		try{
			list= queryDao.queryForList("selectCannelcashControl", paramMap);
			if(list == null || list.size()==0 )
			{
				log.info("渠道编号为"+projectcode+"的渠道未查到相关渠道返现记录");
				return cashbackrate;
			}else{
				queryCashBackControl = (QueryCashBackControl) list.get(0);
				log.info("渠道编号:"+paramMap.get("projectcode")+",返利渠道组："+queryCashBackControl.getGroupname()+",返利率："+queryCashBackControl.getCashbackratevalue());
			}
			cashbackrate = queryCashBackControl.getCashbackratevalue();
			log.info("-----获取渠道预订返现百分比例结束-----end");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return cashbackrate;
    }
    
    /**
     * 根据传入的查询条件，查询满足条件的记录并处理
     * 
     * @param projectcode
     *            渠道编码
     * @param roomid
     *            房型id号
     * @param prefixreturnAmount
     *            原返利金额
     * @param cashbackratevalue
     * 			  返现比例
     * @return Double
     * 			  通过渠道设定的返现比例处理后的返利金额
     * @author hushunqiang
     * @date 2013-04-20 15:15:00
     */
	public Double getlastCashBackAmount(double cashbackratevalue,Double prefixreturnAmount) {
		double lastReturnAmount = 0;
		if(cashbackratevalue == 0){
			return lastReturnAmount;
		}
		BigDecimal scalelast = new BigDecimal(prefixreturnAmount);
		lastReturnAmount = scalelast.multiply(new BigDecimal(cashbackratevalue)).doubleValue();
		return lastReturnAmount;
	}
    
    
	
	public DAOIbatisImpl getQueryDao() {
		return queryDao;
	}
	public void setQueryDao(DAOIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}

}
