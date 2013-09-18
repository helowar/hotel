package com.mangocity.hotel.quotationdisplay.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;

import com.googlecode.jsonplugin.annotations.JSON;

import com.mangocity.hotel.quotationdisplay.model.QueryParam;
import com.mangocity.hotel.quotationdisplay.service.IAveragePriceService;
import com.mangocity.hotel.quotationdisplay.vo.AveragePricesVO;
import com.mangocity.hotel.quotationdisplay.vo.HotelAveragePriceVO;
import com.mangocity.hotel.search.util.DateUtil;
import com.opensymphony.xwork2.ActionSupport;
/***
 * 
 * @author panjianping
 *
 */
public class AveragePriceAction extends ActionSupport{
private static final Log log= LogFactory.getLog(AveragePriceAction.class);
  private IAveragePriceService averagePriceService;
  private String content;
  private QueryParam  param = new QueryParam();
  
  /***
   * 以json的形式将酒店未来30天平均价格的数据返回给客户端
   */
  public String execute(){
	  //log.info(param.getZone());
	  AveragePricesVO avgPrices = new AveragePricesVO();
	   param.setLevel(true);//请求中高档酒店
	  List<HotelAveragePriceVO> advancedAvgPrices=averagePriceService.getAveragePrice(param);
	  avgPrices.setMaxAvgPrice(averagePriceService.getMaxAveragePrice());
	  param.setLevel(false);//请求经济型酒店
	  List<HotelAveragePriceVO> economyAvgPrices = averagePriceService.getAveragePrice(param);
	  
	  avgPrices.setAdvanced(advancedAvgPrices);
	  avgPrices.setEconomy(economyAvgPrices);
     
	  if(!advancedAvgPrices.isEmpty()&&!economyAvgPrices.isEmpty()){
			JSONArray jsonArray = JSONArray.fromObject(avgPrices);
			content = jsonArray.toString();
			//log.info(content);
			//log.info("平均价格返回成功");
			return SUCCESS; 
		}		
		return null;
	} 
  @JSON(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	@JSON(serialize = false)
	public IAveragePriceService getAveragePriceService() {
		return averagePriceService;
	}
	public void setAveragePriceService(IAveragePriceService averagePriceService) {
		this.averagePriceService = averagePriceService;
	}
	@JSON(serialize = false)
	public QueryParam getParam() {
		return param;
	}
	public void setParam(QueryParam param) {
		this.param = param;
	}
	
	
}
