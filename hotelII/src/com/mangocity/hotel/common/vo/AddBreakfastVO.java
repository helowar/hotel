package com.mangocity.hotel.common.vo;

import java.util.Date;

import com.mangocity.hotel.search.vo.SerializableVO;

public class AddBreakfastVO implements SerializableVO {

	 public AddBreakfastVO(){}
	 
	    // 加床价格id
	    private long ID;

	    // 起始日期
	    private String beginDate;

	    // 终止时期
	    private String endDate;

	    // 中餐
	    private String chineseFood;

	    private String chineseFoodType;

	    // 西餐
	    private String westernFood;

	    private String westernFoodType;

	    // 自助餐
	    private String buffetDinner;

	    private String buffetDinneTyper;

	    /**
	     * 支付类型
	     */
	    private String payMethod;

	    /**
	     * 备注
	     */
	    private String remark;

		public long getID() {
			return ID;
		}

		public void setID(long id) {
			ID = id;
		}

		public String getBeginDate() {
			return beginDate;
		}

		public void setBeginDate(String beginDate) {
			this.beginDate = beginDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getChineseFood() {
			return chineseFood;
		}

		public void setChineseFood(String chineseFood) {
			this.chineseFood = chineseFood;
		}

		public String getChineseFoodType() {
			return chineseFoodType;
		}

		public void setChineseFoodType(String chineseFoodType) {
			this.chineseFoodType = chineseFoodType;
		}

		public String getWesternFood() {
			return westernFood;
		}

		public void setWesternFood(String westernFood) {
			this.westernFood = westernFood;
		}

		public String getWesternFoodType() {
			return westernFoodType;
		}

		public void setWesternFoodType(String westernFoodType) {
			this.westernFoodType = westernFoodType;
		}

		public String getBuffetDinner() {
			return buffetDinner;
		}

		public void setBuffetDinner(String buffetDinner) {
			this.buffetDinner = buffetDinner;
		}

		public String getBuffetDinneTyper() {
			return buffetDinneTyper;
		}

		public void setBuffetDinneTyper(String buffetDinneTyper) {
			this.buffetDinneTyper = buffetDinneTyper;
		}

		public String getPayMethod() {
			return payMethod;
		}

		public void setPayMethod(String payMethod) {
			this.payMethod = payMethod;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
}
