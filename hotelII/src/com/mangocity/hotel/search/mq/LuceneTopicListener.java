package com.mangocity.hotel.search.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.mangocity.hotel.search.index.HotelInfoIndexer;
import com.mangocity.mq.hotel.SimpleMessageConverter;
import com.mangocity.util.log.MyLog;


/**
 * 
 * 监听类实现MessageListener接口的OnMessage方法, 监听lucene topic的修改信息
 * 
 * @author chenkeming
 *
 */
public class LuceneTopicListener implements MessageListener {
	
	private static final MyLog log = MyLog.getLogger(LuceneTopicListener.class);
	
	private String name = "Lucene Topic Listener";
	
	private HotelInfoIndexer hotelInfoIndexer;

    public void onMessage(Message message) {
        try {
        	Object msg = SimpleMessageConverter.fromMessage(message);
        	log.info("[" + this.getName() + "] Received: '" + msg + "");
        	handleReceiveMessage(msg);

        } catch (JMSException e) {
            log.error("[" + this.getName() + "] Caught: " + e);
            e.printStackTrace();
        } 
    }

	/**
	 * 
	 * 处理接收消息类型
	 * 
	 * @param msg
	 */
	private void handleReceiveMessage(Object msg) {
		if(msg instanceof String) {
			String hotelMsg = (String)msg;
			if(hotelMsg.startsWith("hotelInfo#")) { // 酒店基本信息的修改
				String hotelId = hotelMsg.substring(10);
				
				try {
					// 更新该酒店document
					hotelInfoIndexer.updateHotelBasicInfoDoc(Long.valueOf(hotelId));	
				} catch(Exception e) {
					log.error(e);
				}
			} else if(hotelMsg.startsWith("reload")) { // 重新导入lucene文件
				try {
				//	hotelInfoIndexer.createHotelInfoIndex();
					log.info("重新生成盗梦网站的lucene文件成功!");
				} catch(Exception e) {
					e.printStackTrace();
					log.error("重新生成盗梦网站的lucene文件失败!",e);
				}				
			} else {
				//更新经纬度的document即（GeoDistance);
				log.info("LuceneTopicListener.handleReceiveMessage hotelMsg:"+hotelMsg);
				if(hotelMsg.startsWith("hotelmap")){
					//修改酒店经纬度
					if(hotelMsg.indexOf("hotelmap:hotel#")>=0){
						String hotelId = hotelMsg.substring(15);
						log.info("LuceneTopicListener.handleReceiveMessage hotelId:"+hotelId);
						hotelInfoIndexer.updateHotelBasicInfoDoc(Long.valueOf(hotelId));	
					}else{
						//修改地理位置信息经纬度
						if(hotelMsg.indexOf("hotelmap:mgis#")>=0){
							String geoId=hotelMsg.substring(14);
							log.info("LuceneTopicListener.handleReceiveMessage geoId:"+geoId);
							hotelInfoIndexer.updateMgisInfoDoc(Long.valueOf(geoId));
						}
					}
				}
			}
		} 
	}
	

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public void setHotelInfoIndexer(HotelInfoIndexer hotelInfoIndexer) {
		this.hotelInfoIndexer = hotelInfoIndexer;
	}

}

