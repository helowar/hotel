package com.mangocity.hotel.pricelowest.action;


import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mangocity.util.bean.CurrencyBean;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * 算出酒店详情页面上的各个床型的最低价
 * @author panjianping
 *
 */
public class MinPriceOfRoomType extends ActionSupport{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String priceArray;
    private String recashArray;
    private String indexOfMin;
    private String minPrice;
    private String recash="0.0";
    private double[] prices;
    private double[] recashs;
	@Override
	public String execute() throws Exception {
		if(priceArray!=null&&!"".equals(priceArray)){
			prices = convertToRMBPrices(priceArray);
			this.minPrice = new Double(getMinPrice(prices)).toString();
		}
		if(recashArray!=null&&!"".equals(recashArray)){
			recashs = convertToRMBPrices(recashArray);
			this.recash =new Double(recashs[Integer.valueOf(this.indexOfMin)]).toString();
		}		
		return SUCCESS;
	}
	public double getMinPrice(double[]prices){
		double min =prices[0];
		 int indexOfMin=0;
		for(int i=1;i<prices.length;i++){
		 if(min>prices[i]){
			min=prices[i]; 
			indexOfMin=i;
		 }
		}
		this.indexOfMin=new Integer(indexOfMin).toString();
		return min;
	}
    public double[]convertToRMBPrices(String priceArray){
    	String [] priceStrs=priceArray.split(";");
		double[] prices = new double[priceStrs.length];
		int i=0;
		for(String priceStr:priceStrs){
			if(-1!=priceStr.indexOf("RMB")){
				int beginIndex=priceStr.indexOf("RMB");
				priceStr=priceStr.substring(beginIndex+3);
				if(checkFloat(priceStr.trim(),"0+")){
					prices[i]=Double.parseDouble(priceStr);
				}		
			}else if(-1!=priceStr.indexOf("HKD")){
				int beginIndex=priceStr.indexOf("HKD");
				priceStr=priceStr.substring(beginIndex+3);
				if(checkFloat(priceStr.trim(),"0+")){
					prices[i]=Double.parseDouble(priceStr);
					double rateHKDToRMB = Double.parseDouble(CurrencyBean.rateMap.get(
							CurrencyBean.HKD).toString());
					prices[i]=prices[i]*rateHKDToRMB;
				}	
				
			}else if(-1!=priceStr.indexOf("MOP")){
				int beginIndex=priceStr.indexOf("MOP");
				priceStr=priceStr.substring(beginIndex+3);
				if(checkFloat(priceStr.trim(),"0+")){
					prices[i]=Double.parseDouble(priceStr);
					double rateMOPToRMB = Double.parseDouble(CurrencyBean.rateMap.get(
	    					CurrencyBean.MOP).toString());
			       prices[i]=prices[i]*rateMOPToRMB;
				}	
			    
			}
			prices[i]=Double.parseDouble(new DecimalFormat("#.00").format(prices[i]));
			i++;
		}
			return prices;
    }
    
    public static boolean checkFloat(String num,String type){   
        String eL = "";   
        if(type.equals("0+"))eL = "^\\d+(\\.\\d+)?$";//非负浮点数   
        else if(type.equals("+"))eL = "^((\\d+\\.\\d*[1-9]\\d*)|(\\d*[1-9]\\d*\\.\\d+)|(\\d*[1-9]\\d*))$";//正浮点数   
        else if(type.equals("-0"))eL = "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$";//非正浮点数   
        else if(type.equals("-"))eL = "^(-((\\d+\\.\\d*[1-9]\\d*)|(\\d*[1-9]\\d*\\.\\d+)|(\\d*[1-9]\\d*)))$";//负浮点数   
        else eL = "^(-?\\d+)(\\.\\d+)?$";//浮点数   
        Pattern p = Pattern.compile(eL);   
        Matcher m = p.matcher(num);   
        boolean b = m.matches();   
        return b;   
    } 
	public String getPriceArray() {
		return priceArray;
	}

	public void setPriceArray(String priceArray) {
		this.priceArray = priceArray;
	}
	public String getIndexOfMin() {
		return indexOfMin;
	}
	public void setIndexOfMin(String indexOfMin) {
		this.indexOfMin = indexOfMin;
	}
	public String getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}
	public String getRecashArray() {
		return recashArray;
	}
	public void setRecashArray(String recashArray) {
		this.recashArray = recashArray;
	}
	public String getRecash() {
		return recash;
	}
	public void setRecash(String recash) {
		this.recash = recash;
	}
	public double[] getPrices() {
		return prices;
	}
	public void setPrices(double[] prices) {
		this.prices = prices;
	}
	public double[] getRecashs() {
		return recashs;
	}
	public void setRecashs(double[] recashs) {
		this.recashs = recashs;
	}
	
    
}
