package com.mangocity.hotel.base.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mangocity.hotel.base.dao.GeoDistanceDao;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlGeoDistance;
import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.service.GeoDistanceService;
import com.mangocity.hotel.base.service.IGeograpPositionService;
import com.mangocity.hotel.base.service.assistant.CalculateDistance;
import com.mangocity.hotel.constant.HotelDistance;

public class GeoDistanceServiceImpl implements GeoDistanceService{
	private static final int MAX_CONSUMER = 50;
	private static final Integer[] types =new Integer[]{21,23,24,26,27,28,29,30,31,32,33};
	private static Log log = LogFactory.getLog(GeoDistanceServiceImpl.class);
	private GeoDistanceDao geoDistanceDao;
	private HotelManage hotelManage;
	private IGeograpPositionService geograpPositionService;
	private BlockingQueue<HtlHotel> queue;
	public boolean finishFlag=false;
	@SuppressWarnings("unchecked")
	public List<HtlHotel> getHotels(){
		return hotelManage.findActiveHotel();
	}
	public void buildGeoInfo() throws InterruptedException{
		List<HtlHotel> hotels=getHotels();
		//System.out.println(hotels.size());
		if(null!=hotels&&!hotels.isEmpty()){
			log.info("初始化队列");
			queue = new SynchronousQueue<HtlHotel>();
			log.info("初始化队列完成");
			log.info("初始化消息处理器");
			startConsumer();
			log.info("初始化消息处理器完成");
			for(HtlHotel hotel:hotels) {
				queue.put(hotel);
			}
			finishFlag = true;
		}
	}
	private void startConsumer() {
		ExecutorService service = Executors.newFixedThreadPool(MAX_CONSUMER);
		for (int i=0;i<MAX_CONSUMER;i++) {
			log.info("启动 CONSUMER " + i);
			service.execute(new Consumer("Consumer"+i));
		}
		service.shutdown();
		
	}
	private double convert(double distance){
		return Math.round(distance/10)/100.0;
	}
	
	private HtlGeoDistance constructGeoInfo(HtlHotel hotel, HtlGeographicalposition geo,
			double distance) {
		HtlGeoDistance geoDistance=new HtlGeoDistance();
		geoDistance.setCityCode(hotel.getCity());
		geoDistance.setCreateTime(new Date());
		geoDistance.setDistance(distance);
		geoDistance.setGeoType(geo.getGptypeId());
		geoDistance.setHotelId(hotel.getID());
		geoDistance.setGeoId(geo.getID());
		geoDistance.setName(geo.getName());
		return geoDistance;
	}
	public List<HtlGeoDistance> getDistanceList(String cityCode, Long type) {
		return geoDistanceDao.getDistanceList(cityCode,type);
	}
	
	public GeoDistanceDao getGeoDistanceDao() {
		return geoDistanceDao;
	}
	public void setGeoDistanceDao(GeoDistanceDao geoDistanceDao) {
		this.geoDistanceDao = geoDistanceDao;
	}
	public HotelManage getHotelManage() {
		return hotelManage;
	}
	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}
	
	public IGeograpPositionService getGeograpPositionService() {
		return geograpPositionService;
	}
	public void setGeograpPositionService(
			IGeograpPositionService geograpPositionService) {
		this.geograpPositionService = geograpPositionService;
	}
	public void generateGoeInfoByHotel(Long hotelId) {
		log.info("GeoDistanceServiceImpl.generateGoeInfoByHotel hotelId="+hotelId);
		//geoDistanceDao.deleteGeoDistanceByHotelId(hotelId);
		HtlHotel hotel=hotelManage.findHotel(hotelId);
		updateDistance(hotel);
	}
	
	public void generateGoeInfoByGeoId(Long geoId) {
		HtlGeographicalposition geo = geograpPositionService.queryGeopositionById(geoId);
		log.info("GeoDistanceServiceImpl.generateGoeInfoByHotel geoId="+geoId);
		List<HtlHotel> hotelList=hotelManage.findActiveHotelCityCode(geo.getCityCode());
		log.info("GeoDistanceServiceImpl.generateGoeInfoByHotel hote size="+hotelList.size());
		try{
			for(HtlHotel hotel:hotelList){
				log.info("GeoDistanceServiceImpl.generateGoeInfoByHotel hote Latitude="+hotel.getLatitude()+"Longitude="+hotel.getLongitude());
				if(null!=hotel.getLatitude()&&null!=hotel.getLongitude()&&null!=geo.getLatitude()&&null!=geo.getLongitude()){
					double distance=CalculateDistance.getDistance(hotel.getLatitude(),hotel.getLongitude(),geo.getLatitude(), geo.getLongitude(), 'K');
					log.info("GeoDistanceServiceImpl.generateGoeInfoByHotel  distance="+distance);
					if(HotelDistance.CONSTANTS_SET_DISTANCE*1000>=distance){
						List<HtlGeoDistance> list = geoDistanceDao.queryGeoDistanceList(hotel.getID(),geoId);
						if(null!=list&&!list.isEmpty()){
							log.info("GeoDistanceServiceImpl.generateGoeInfoByHotel  update hotelId="+hotel.getID());
							geoDistanceDao.updateByGeoIdAndHotelId(hotel.getID(),geo.getID(),distance);
						}else{
							log.info("GeoDistanceServiceImpl.generateGoeInfoByHotel  save hotelId="+hotel.getID());
							geoDistanceDao.save(constructGeoInfo(hotel,geo,convert(distance)));
						}
					}
				}
			}
		}catch(Exception e){
			log.error("GeoDistanceServiceImpl.generateGoeInfoByHotel error:"+e);
		}
	}
	
	private void updateDistance(HtlHotel hotel){
		if(null!=hotel.getLatitude()&&null!=hotel.getLongitude()){
			List<HtlGeographicalposition> list=geoDistanceDao.getGeoList(hotel.getID(), types, hotel.getCity());
			try{
				for(HtlGeographicalposition geo:list){
					if(null!=hotel.getLatitude()&&null!=hotel.getLongitude()&&null!=geo.getLatitude()&&null!=geo.getLongitude()){
						double distance=CalculateDistance.getDistance(hotel.getLatitude(),hotel.getLongitude(),geo.getLatitude(), geo.getLongitude(), 'K');
						if(HotelDistance.CONSTANTS_SET_DISTANCE*1000>=distance){
							List<HtlGeoDistance> list1 = geoDistanceDao.queryGeoDistanceList(hotel.getID(),geo.getID());
							if(null!=list1&&!list1.isEmpty()){
								geoDistanceDao.updateByGeoIdAndHotelId(hotel.getID(),geo.getID(),distance);
							}else{
								geoDistanceDao.save(constructGeoInfo(hotel,geo,convert(distance)));
							}
						}
					}
				}
			}catch(Exception e){
				log.error("GeoDistanceServiceImpl.generateGoeInfoByHotel error:"+e);
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void process(HtlHotel hotel){
		if(null!=hotel.getLatitude()&&null!=hotel.getLongitude()){
			List<HtlGeographicalposition> list=geoDistanceDao.getGeoList(hotel.getID(), types, hotel.getCity());
			for(HtlGeographicalposition geo:list){
				if(null!=hotel.getLatitude()&&null!=hotel.getLongitude()&&null!=geo.getLatitude()&&null!=geo.getLongitude()){
					double distance=CalculateDistance.getDistance(hotel.getLatitude(),hotel.getLongitude(),geo.getLatitude(), geo.getLongitude(), 'K');
					//log.info("hotel info:"+hotel.getChnName()+"   distance="+distance);
					if(HotelDistance.CONSTANTS_SET_DISTANCE*1000>=distance){
						try {
							if(geoDistanceDao.queryGeoDistanceList(hotel.getID(),geo.getID()).isEmpty()){
								geoDistanceDao.save(constructGeoInfo(hotel,geo,convert(distance)));
								log.info("save HtlGeoDistance info: hotelName="+hotel.getChnName()+"; geoName="+geo.getName()+" ; distance="+convert(distance));
							}
						} catch (Exception e) {
							log.error("save HtlGeoDistance error: hotelId="+hotel.getID()+"; geoId="+geo.getID(), e);
						}
					}
				}
			}
		}
	}
	
	private class Consumer extends Thread {
			public Consumer(String name) {
				super(name);
			}
			@Override
			public void run() {
				//log.info(getName() + "正在处理消息...");
				while(true) {
					HtlHotel hotel = queue.poll();
					if (null != hotel) {
						process(hotel);
						log.info(getName() + " 消息处理 ；酒店名称：" + hotel.getChnName());
					} else if (!finishFlag) {
						sleep();
					} else {
						System.out.println(getName() + " OVER.");
						return;
					}
				}
			}
			private void sleep() {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

}

