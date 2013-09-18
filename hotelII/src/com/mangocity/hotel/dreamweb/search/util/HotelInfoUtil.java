package com.mangocity.hotel.dreamweb.search.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import com.mangocity.util.log.MyLog;

/**
 * 
 * @author alfred
 *
 */
public class HotelInfoUtil {
	
	public static String getCityNameByIP(String ip) throws IOException {
		GetMethod method =null;
		try {
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);  //设置连接超时
			client.getHttpConnectionManager().getParams().setSoTimeout(5000);
			String url = "http://club.mangocity.com/interface/IGetIPAddress.aspx?IP=" + ip;
			method = new GetMethod(url);
			int statusCode = client.executeMethod(method);
			if(statusCode == HttpStatus.SC_OK) {
				Reader reader = new InputStreamReader(method
						.getResponseBodyAsStream(), method
						.getResponseCharSet());
				int readBit;
				StringBuilder result = new StringBuilder();
				while ((readBit = reader.read()) != -1) {
					result.append((char) readBit);
				}
				String message = result.toString();
				if(message != null && !"".equals(message)) {
					return message.split("\\|")[0]; // 转义 add by diandian.hou 
				}else {
					log.error("............get cityName by IP from http://club.mangocity.com/interface/IGetIPAddress.aspx is null where IP =" + ip + "...........");
				}
			}else {
				log.error("............get cityName by IP from http://club.mangocity.com/interface/IGetIPAddress.aspx return code is" + statusCode + "..........");
			}
		} catch (HttpException e) {
			log.error("............connect to http://club.mangocity.com/interface/IGetIPAddress.aspx has occur a error!" ,e);
			throw new HttpException(e.getMessage());
		} catch (IOException e) {
			log.error("............" , e);
			throw new IOException(e.getMessage());
		}catch (Exception e){
			log.error("HotelInfoUtil getCityNameByIP has a wrong" , e);
		}
		finally{
			method.releaseConnection();
		}
		return null;
	}
	
	
	/**
	 * 
	 */
	public static MyLog log = MyLog.getLogger(HotelInfoUtil.class);

}
