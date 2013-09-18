package com.mangocity.hotel.search.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.base.service.GeographicalPositionService;
import com.mangocity.hotel.search.service.HotelMgisSearchService;
import com.mangocity.hotel.search.service.HotelSearcher;
import com.mangocity.util.log.MyLog;

public class HotelMgisSearchServiceImpl implements HotelMgisSearchService{
	private static final MyLog log = MyLog.getLogger(HotelMgisSearchServiceImpl.class);
	private static final Long CONSTANT_GOE_TYPE_SUBWAY = 24L;
	private static final Long CONSTANT_GOE_TYPE_UNIVERSITY = 26L;
	private static final Long CONSTANT_GOE_TYPE_SCENIC = 23L;
	private static final Long CONSTANT_GOE_TYPE_HOSPITAL = 27L;
	private static final Long CONSTANT_GOE_TYPE_TRAINANDAIRD = 21L;
	public HotelSearcher hotelSearcher;	
	public GeographicalPositionService geographicalPositionService;
	public Map getIndexMgisInfo(String cityCode) {
		Map map=geographicalPositionService.queryBusinessForCityCode(cityCode);
		districtInit(map, cityCode);
		log.info("HotelMgisSearchServiceImpl getIndexMgisInfo city"+map.size());
		//business,district
		HtlGeographicalposition geo=new HtlGeographicalposition();
		geo.setCityCode(cityCode);
		geo.setGptypeId(CONSTANT_GOE_TYPE_SUBWAY);
		//地铁
		List<HtlGeographicalposition>  subway=hotelSearcher.searchHotelGeoInfo(geo);
		Collections.sort(subway);

		map.put("subway", subway);
		geo.setGptypeId(CONSTANT_GOE_TYPE_UNIVERSITY);
		//大学
		List<HtlGeographicalposition> university=hotelSearcher.searchHotelGeoInfo(geo);
		Collections.sort(university);

		map.put("university",university);
		geo.setGptypeId(CONSTANT_GOE_TYPE_SCENIC);
		//景区
		List<HtlGeographicalposition> scenic =hotelSearcher.searchHotelGeoInfo(geo);
		Collections.sort(scenic);

		map.put("scenic", scenic);
		geo.setGptypeId(CONSTANT_GOE_TYPE_HOSPITAL);
		//医院
		List<HtlGeographicalposition> hospital=hotelSearcher.searchHotelGeoInfo(geo);
		Collections.sort(hospital);

		map.put("hospital", hospital);
		geo.setGptypeId(CONSTANT_GOE_TYPE_TRAINANDAIRD);
		geo.setName("火车");
		//火车站
		List train =hotelSearcher.searchHotelGeoInfo(geo);
		Collections.sort(train);

		map.put("train",train);
		geo.setGptypeId(CONSTANT_GOE_TYPE_TRAINANDAIRD);
		geo.setName("机场");
		//机场
		List<HtlGeographicalposition> airdrome=hotelSearcher.searchHotelGeoInfo(geo);
		log.info("HotelMgisSearchServiceImpl airdrome size="+airdrome.size());
		Collections.sort(airdrome);

		map.put("airdrome", airdrome);
		map.put("brand",initCityBrand().get(cityCode));
		return map;
	}
	private Map initCityBrand(){
		Map brandMap=new HashMap();
		Object [][] pek={{1719,"港中旅维景"},{1583,"如家"},{1321,"锦江之星"},{1601,"莫泰"},{1623,"速8"},{1061,"格林豪泰"},{1623,"华美达"},{1701,"雅高"},{1683,"喜达屋"},{1720,"香格里拉"},{1661,"万豪"},{1585,"洲际"},{1715,"汉庭"},{1622,"首旅建国"},{2281,"玉渊潭"}};
		brandMap.put("PEK",pek);
		Object [][] sha={{1719,"港中旅维景"},{1583,"如家"},{1321,"锦江之星"},{1061,"格林豪泰"},{1601,"莫泰"},{1623,"华美达"},{1701,"雅高"},{1683,"喜达屋"},{1661,"万豪"},{1585,"洲际"},{1715,"汉庭"},{1682,"希尔顿"}};
		brandMap.put("SHA",sha);
		Object [][] can={{1583,"如家"},{1730,"山水时尚"},{1720,"香格里拉 "},{1762,"凯悦"},{1683,"喜达屋"},{1765,"岭南花园"},{1585,"洲际"},{1662,"维也纳"},{1715,"汉庭"}};
		brandMap.put("CAN",can);
		Object [][] szx={{1719,"港中旅维景"},{1583,"如家"},{1321,"锦江之星"},{1601,"莫泰"},{1623,"速8"},{1061,"格林豪泰"},{1701,"雅高"},{1623,"戴斯"},{1683,"喜达屋"},{1661,"万豪"},{1585,"洲际"},{1715,"汉庭"},{1720,"香格里拉 "},{1561,"华侨城"},{1662,"维也纳"},{1727,"粤海"},{1561,"城市客栈"},{1581,"锦江"}};
		brandMap.put("SZX",szx);
		Object [][] ctu={{1720,"香格里拉 "},{1585,"洲际"},{1683,"喜达屋"},{1701,"雅高"},{1561,"华侨城"},{1583,"如家"},{1715,"汉庭"}};
		brandMap.put("CTU",ctu);
		Object [][] hgh={{1719,"港中旅维景"},{1701,"雅高"},{1585,"洲际"},{1601,"莫泰"},{1583,"如家"},{1623,"温德姆"},{1861,"开元集团"},{1683,"喜达屋"},{1715,"汉庭"},{1720,"香格里拉 "},{1623,"华美达"}};
		brandMap.put("HGH",hgh);
		Object [][] nkg={{1719,"港中旅维景"},{1641,"金陵集团"},{1683,"喜达屋"},{1701,"雅高"},{1585,"洲际"},{1601,"莫泰"},{1583,"如家"},{1584,"格林豪泰"},{1715,"汉庭"}};
		brandMap.put("NKG",nkg);
		Object [][] syx={{1719,"港中旅维景"},{1701,"雅高"},{1643,"凯宾斯基"},{1661,"万豪"},{1623,"温德姆"},{1682,"希尔顿"},{1683,"喜达屋"},{1585,"洲际"},{1583,"如家"},{1663,"凯莱"}};
		brandMap.put("SYX",syx);
		return brandMap;
	}
	
	
	private void districtInit(Map map,String cityCode){
		List list = new ArrayList<Object[]>();
		List<Object[]> tempList = (ArrayList<Object[]>) map.get("district");
		String[] districtName = null;
		// 北京
		if ("PEK".equals(cityCode)) {
			String[] tempVal = { "西城区", "东城区", "朝阳区", "宣武区", "崇文区", "丰台区",
					"石景山区", "海淀区", "密云区", "顺义区", "延庆区", "通州区", "昌平区", "怀柔区",
					"平谷区", "门头沟区", "房山区", "大兴区" };
			districtName = tempVal;
		}
		// 上海
		if ("SHA".equals(cityCode)) {
			String[] tempVal = { "浦东新区", "徐汇区", "长宁区", "静安区", "闸北区", "卢湾区",
					"普陀区", "杨浦区", "宝山区", "虹口区", "黄浦区", "松江区", "嘉定区", "奉贤区",
					"闵行区", "青浦区", "崇明区", "金山区", "南汇区" };
			districtName = tempVal;
		}
		// 广州
		if ("CAN".equals(cityCode)) {
			String[] tempVal = { "天河区", "荔湾区", "越秀区", "海珠区", "白云区", "番禺区",
					"花都区", "增城区", "黄埔区", "南沙区", "萝岗区", "从化区" };
			districtName = tempVal;
		}
		//杭州
		if ("HGH".equals(cityCode)) {
			String[] tempVal = { "上城区", "下城区", "西湖区", "拱墅区", "江干区", "滨江区",
					"萧山区", "余杭区", "文教区", "下沙经济开发区" };
			districtName = tempVal;
		}
		//成都
		if ("CTU".equals(cityCode)) {
			String[] tempVal = { "锦江区", "青羊区", "武侯区", "金牛区", "成华区", "高新区",
					"龙泉驿区", "双流", "温江", "蒲江", "新都", "新津", "青白江区", "大邑", "邛崃",
					"郫县", "彭州", "崇州", "金堂" };
			districtName = tempVal;

		}

		if (districtName != null) {
			for (String val : districtName) {
				for (Object[] obj : tempList) {
					if (val.equals(obj[1].toString())) {
						list.add(obj);
						break;
					}
				}
			}
			map.remove("district");
			map.put("district", list);
		}				
	}
	
	
	public HotelSearcher getHotelSearcher() {
		return hotelSearcher;
	}
	public void setHotelSearcher(HotelSearcher hotelSearcher) {
		this.hotelSearcher = hotelSearcher;
	}
	public GeographicalPositionService getGeographicalPositionService() {
		return geographicalPositionService;
	}
	public void setGeographicalPositionService(
			GeographicalPositionService geographicalPositionService) {
		this.geographicalPositionService = geographicalPositionService;
	}
	
}
