package com.mangocity.webnew.dao.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mangocity.hotel.base.persistence.HtlHotelSortByArea;
import com.mangocity.hweb.persistence.HotelBookingInfoForHkSale;
import com.mangocity.hweb.persistence.QHotelInfo;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.dao.QueryAllHkHotelDao;
import com.mangocity.webnew.util.ParserQmangoHotel;

/**
 * 查询指定香港酒店的最低价和酒店总体是否可订
 * @author guzhijie
 *
 */
@SuppressWarnings("serial")
public class QueryAllHkHotelDaoImpl extends DAOIbatisImpl implements  QueryAllHkHotelDao {
	
	@SuppressWarnings("unchecked")
	public List<HotelBookingInfoForHkSale> queryAllHotelInfoByHotelIdAndAbleSaleDate(String cityCode,String ableSaleDate){
		Map paramMap = new HashMap();
		paramMap.put("ableSaleDate", ableSaleDate);
		paramMap.put("cityCode", cityCode);
		List<HotelBookingInfoForHkSale> hotelBookingInfoForHkSaleList = super.queryForList("queryAllHkHotelInfo", paramMap);
		return null != hotelBookingInfoForHkSaleList ? hotelBookingInfoForHkSaleList:Collections.EMPTY_LIST;
	}
	
	@SuppressWarnings("unchecked")
	public List<HtlHotelSortByArea> queryHtlHotelSortByArea(String cityCode){
		Map paramMap = new HashMap();
		paramMap.put("cityCode", cityCode);
		List<HtlHotelSortByArea> htlHotelSortByAreaList = super.queryForList("queryHtlHotelSortByArea", paramMap);
		return null != htlHotelSortByAreaList ? htlHotelSortByAreaList:Collections.EMPTY_LIST;
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<QHotelInfo> queryQMangocityHotelInfo(String cityCode, String ableSaleDate) {
		GetMethod method=null;
		try {
			HttpClient httpClient = new HttpClient();
			
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);  //设置连接超时
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(3000); //设置读取数据超时
			
			String url = "http://www.qmango.com/hkmango?city=" + cityCode + "&sdate=" + ableSaleDate;
			log.info(".....url=" + url + ".............");
			method = new GetMethod(url);
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			int stateCode = httpClient.executeMethod(method);
			if(stateCode == HttpStatus.SC_OK) {
				List<QHotelInfo> qhotels = ParserQmangoHotel.getQhotels(method.getResponseBodyAsStream());
				return qhotels;
			}else {
				log.error("..........the error code is" + stateCode + " while connect to QMango............");
			}
		} catch (HttpException e) {
			log.error(".................connect to QMango failed!.......................",e);
		} catch (IOException e) {
			log.error("...........occur a IOException whiel transtract with QMango!...........",e);
		} catch (ParserConfigurationException e) {
			log.error("..........SAX config error!............",e);
		} catch (SAXException e) {
			log.error("..........parse xml error...........",e);
		}catch (Exception e){
			log.error("......other error ",e);
		}
		
		finally{
			if(method!=null){
				method.releaseConnection();
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		QueryAllHkHotelDaoImpl queryAllHkHotelDaoImpl = new QueryAllHkHotelDaoImpl();
		List list = queryAllHkHotelDaoImpl.queryQMangocityHotelInfo("HKG", "2011-7-27");
		
	}
	
	
	private MyLog log = MyLog.getLogger(QueryAllHkHotelDaoImpl.class);
    
}
