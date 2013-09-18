package com.mangocity.client.hotel.search.serviceImpl;

public class HotelQueryConditonConstant {
	public enum HotelStar {
		NORESTRICTION("", "不限"), FiveSTAR("19,29,", "5星/豪华型"), FOURSTAR(
				"39,49,", "4星/高档型"), THREESTAR("59,64,", "3星/舒适型"), TWOSTAR(
				"66,69,", "2星/经济型"), UNDERTWOSTAR("79,", "2星级以下/公寓");
		private String key;
		private String value;

		HotelStar(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public String getKey() {
			return key;
		}
	}

	public enum HotelPrice {
		NORESTRICTION("", "不限"), UNDERTWOHUNDRED("0-200", "200元以下"), UNDERFIVEHUNDRED(
				"200-500", "200-500元"), UNDEREIGHTHUNDRED("500-800",
				"500-800元"), ABOVEIGHTHUNDRED("800-", "800元以上");
		private String key;
		private String value;

		HotelPrice(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public String getKey() {
			return key;
		}
	}

	public enum HotelFacility {
		NORESTRICTION("", "不限"), FREEBROADBAND("3,", "免费宽带"), COMPTER("1,",
				"房间配电脑"), FREEPARKING("6,", "免费停车场"), FREEGYM("8,", "游泳池"), FREESWINGPOOL(
				"9,", "健身房"), CONFERENCEHALL("7,", "会议室"), NEWOPENEDANDFIT(
				"4,", "新开业/装修");
		private String key;
		private String value;

		HotelFacility(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public String getKey() {
			return key;
		}
	}

	public enum HotelBrand {
		NORESTRICTION("", "不限"), WEIJING("1719", "维景"), RUJIA("1583", "如家"), HANTING(
				"1715", "汉庭"), ZHOUJI("1585", "洲际"), HUAJIAOCHENG("1561", "华侨城"), MAGEBOLUO(
				"1768", "马哥孛罗"), XIDAWU("1683", "喜达屋"), XIERDUN("1682", "希尔顿"), FUHAO(
				"1709", "富豪"), GELIHAOTAI("1584", "格林豪泰"), XIANGGELILA("1720",
				"香格里拉"), SHIJIJINYUAN("1624", "世纪金源"), WANHAO("1661", "万豪"), KAIBINSIJI(
				"1643", "凯宾斯基"), KAIYUAN("1861", "开元"), HAIYI("1714", "海逸"), SIJI(
				"2001", "四季"), YAGAO("1701", "雅高"), ZUIJIAXIFANG("1732", "最佳西方"), KAIYUE(
				"1762", "凯悦"), YILAI("2061", "怡莱"), WENHUADONGFANG("2361", "文华东方");
		private String key;
		private String value;

		HotelBrand(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public String getKey() {
			return key;
		}
	}
}
