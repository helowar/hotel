package com.mangocity.hagtb2b.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.mangocity.hagtb2b.persistence.AgentOrder;
import com.mangocity.hagtb2b.persistence.AgentOrderItem;
import com.mangocity.hagtb2b.persistence.AgentOrg;
import com.mangocity.hagtb2b.persistence.StatisticsInfo;
import com.mangocity.hagtb2b.service.IFinaceService;
import com.mangocity.hotel.base.web.webwork.PaginateAction;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.collections.FormatMap;




/**
 * 操作Excel表
 * @author zengyong
 * May 29, 2010,3:14:10 PM
 *描述:
 */
public class OpExcelAction  extends PaginateAction {
  
	private IFinaceService finaceService;
	
	private IOrderService orderService;

	private String year;
	
	private String month;
	
	//代理商code
	private String  agentCode;
	
	//操作人ID
	private String operaterId ;
	private static final int statDate = 7;//每月6日后可以查询和统计

	public IFinaceService getFinaceService() {
		return finaceService;
	}

	public void setFinaceService(IFinaceService finaceService) {
		this.finaceService = finaceService;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getOperaterId() {
		return operaterId;
	}

	public void setOperaterId(String operaterId) {
		this.operaterId = operaterId;
	}

	public void exportExcel(){

		try{
			if(!super.checkLogin()){
				super.setErrorMessage("没有登录过。。。");
			}
			Map params = super.getParams();
			this.setParam(params,super.request);//设置分页查询参数
			StatisticsInfo agentStatiss = new StatisticsInfo();
			finaceService.getAgentOrderList(params, agentStatiss,agentCode);//从or_order和or_orderitem里查询数据并放到agentStatiss对象里
			int policyScope = orderService.getPolicyScope(agentCode);
			
			 WritableWorkbook workbook = Workbook.createWorkbook(super.getHttpResponse().getOutputStream());		 
			 WritableSheet sheet = workbook.createSheet("1", 0);
			 
			 //WritableFont wf = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD); 
			 //WritableCellFormat wcfF = new WritableCellFormat(wf);
			 int row=0;            // 行号
			 Label labelCF = new Label(0, row++, "月份统计信息:"); 
			 sheet.addCell(labelCF);
			 
			 //wf = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD); 
			 //wcfF = new WritableCellFormat(wf); 

			 
		
			 // 酒店销售明细
			 //labelCF = new Label(0, row++, "月份统计信息："); 
			 //sheet.addCell(labelCF);
			 
			 labelCF = new Label(0, row, "代理商名称"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(1, row, "统计年份"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(2, row, "统计月份"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(3, row, "订单总数"); 
			 sheet.addCell(labelCF);
/*			 labelCF = new Label(4, row, "订单间夜量"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(5, row, "订单总金额"); 
			 sheet.addCell(labelCF);
*/			 labelCF = new Label(4, row, "间夜量"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(5, row, "总金额"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(6, row, "税前佣金"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(7, row, "税后佣金");
			 sheet.addCell(labelCF);
			 labelCF = new Label(8, row++, "税金");
			 sheet.addCell(labelCF);
			 
			 labelCF = new Label(0, row, agentStatiss.getAgentName()); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(1, row, agentStatiss.getStatYear()); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(2, row, (agentStatiss.getStatMonth()+1)+"月"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(3, row, agentStatiss.getOrderNum()+""); 
			 sheet.addCell(labelCF);
/*			 labelCF = new Label(4, row, agentStatiss.getNightsNum()+""); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(5, row, agentStatiss.getSumAcount()+""); 
			 sheet.addCell(labelCF);
*/			 labelCF = new Label(4, row, agentStatiss.getActNightsNum()+""); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(5, row, agentStatiss.getActSumAcount()+""); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(6, row, agentStatiss.getCommsion()+""); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(7, row, agentStatiss.getFactcomm()+"");
			 sheet.addCell(labelCF);
			 labelCF = new Label(8, row++, agentStatiss.getCommrate()+"");
			 sheet.addCell(labelCF);
			 
			 labelCF = new Label(0, row++, "月份统计信息:"); 
			 sheet.addCell(labelCF);
			 
			 labelCF = new Label(0, row, "订单编号"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(1, row, "酒店名称"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(2, row, "入住房型(子房型)"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(3, row, "客人姓名"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(4, row, "预订账号"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(5, row, "入住日期"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(6, row, "离店日期"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(7, row, "房间数量"); 
			 sheet.addCell(labelCF);
			 if(policyScope==1){
				 labelCF = new Label(8, row, "房费单价"); 
				 sheet.addCell(labelCF);
				 labelCF = new Label(9, row, "佣金价"); 
				 sheet.addCell(labelCF);
				 labelCF = new Label(10, row, "佣金率");
				 sheet.addCell(labelCF);
				 labelCF = new Label(11, row++, "佣金");
				 sheet.addCell(labelCF);
			 }else if(policyScope==2){
				 labelCF = new Label(8, row, "佣金价"); 
				 sheet.addCell(labelCF);
				 labelCF = new Label(9, row++, "佣金金额");
				 sheet.addCell(labelCF);
			 }
			 
	          List<AgentOrder> detailList = agentStatiss.getOrderItems();
	          int col;
	          for (int i = 0; i < detailList.size(); i++) {
	        	  AgentOrder ago = detailList.get(i);
	        	  List<AgentOrderItem> itemLst = ago.getOrderItems();
	        	  for(int j=0;j<itemLst.size();j++,row++){
	        		AgentOrderItem aoi = itemLst.get(j);
		         labelCF = new Label(0, row, ago.getOrderCd());
		         sheet.addCell(labelCF);
		         labelCF = new Label(1, row, ago.getHotelName());
		         sheet.addCell(labelCF);
		         labelCF = new Label(2, row, ago.getRoomName()+"("+ago.getChildRoomName()+")");
		         sheet.addCell(labelCF);
		         //labelCF = new Label(3, row, getBedTypeName(ago.getBedType()));
		         labelCF = new Label(3, row, aoi.getFellowname());
		         sheet.addCell(labelCF);
		         labelCF = new Label(4, row, ago.getBookingname());
		         sheet.addCell(labelCF);
		         labelCF = new Label(5, row, DateUtil.dateToString(aoi.getNight()));
		         sheet.addCell(labelCF);
		         labelCF = new Label(6, row, DateUtil.dateToString(DateUtil.getDate(aoi.getNight(),1)));//加一天
		         sheet.addCell(labelCF);
		         labelCF = new Label(7, row, "1");
		         sheet.addCell(labelCF);
         
		         if(policyScope==1){
			         labelCF = new Label(8, row, aoi.getSaleprice()+"");
			         sheet.addCell(labelCF);
			         labelCF = new Label(9, row, aoi.getAgentReadComissionPrice()+"");
			         sheet.addCell(labelCF);
			         labelCF = new Label(10, row, aoi.getAgentReadComissionRate()+"");
			         sheet.addCell(labelCF);
			         labelCF = new Label(11, row, aoi.getAgentReadComission()+"");
			         sheet.addCell(labelCF);
			         
		         }else if(policyScope==2){
			         labelCF = new Label(8, row, aoi.getAgentReadComissionPrice()+"");
			         sheet.addCell(labelCF);
			         labelCF = new Label(9, row, aoi.getAgentReadComission()+"");
			         sheet.addCell(labelCF);
		        	 
		         }
	        	  }
	            }
	           
				FormatMap agentmap = new FormatMap();
				agentmap.put("agentcode", agentCode);
				AgentOrg agentOrg = finaceService.queryAgentOrg(agentmap);
	          labelCF = new Label(0, row, "您的开户银行："); 
			  sheet.addCell(labelCF);
	          labelCF = new Label(2, row++, agentOrg.getBankname()); 
			  sheet.addCell(labelCF);

			  
	          labelCF = new Label(0, row, "您的帐号/卡号："); 
			  sheet.addCell(labelCF);
	          labelCF = new Label(2, row++, agentOrg.getAccount()); 
			  sheet.addCell(labelCF);
			  
	          labelCF = new Label(0, row, "您的开户名称："); 
			  sheet.addCell(labelCF);
	          labelCF = new Label(2, row++, agentOrg.getBankusername()); 
			  sheet.addCell(labelCF);
	         //httpResponse.setContentType("application/txt");
	         httpResponse.setContentType( "application/nvd.ms-excel "); 
	         httpResponse.setHeader("Content-disposition", "attachment; filename=" + "ReportConfirm" + ".xls");
			 workbook.write();
			 workbook.close();
	         
	    } catch (Exception e) {
	    	log.error(e.getMessage(),e);
			super.forwardError("导出Excel异常"+e.getMessage());
	    }

	}
	

	
	/**
	 * 导出确认信
	 * add by yong.zeng
	 */
	public void exportCommisionMissive(){

		try{
			if(!super.checkLogin()){
				super.setErrorMessage("没有登录过。。。");
			}
			Map params = super.getParams();
			this.setParam(params,super.request);//设置分页查询参数
			StatisticsInfo agentStatiss = new StatisticsInfo();
			
			finaceService.getAgentOrderList(params, agentStatiss,agentCode);//从or_order和or_orderitem里查询数据并放到agentStatiss对象里
			int policyScope = orderService.getPolicyScope(agentCode);
			
			FormatMap agentmap = new FormatMap();
			agentmap.put("agentcode", agentCode);
			AgentOrg agentOrg = finaceService.queryAgentOrg(agentmap);
			
			int agenttype= agentOrg.getAgentkind();
			
			
			 WritableWorkbook workbook = Workbook.createWorkbook(super.getHttpResponse().getOutputStream());		 
			 WritableSheet sheet = workbook.createSheet("1", 0);
			 
			 WritableFont wf = new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD); 
			 WritableCellFormat wcfF = new WritableCellFormat(wf);
			 int row=0;            // 行号
			 Label labelCF = new Label(0, row++, "芒果网有限公司代理商佣金确认函"); 
			 sheet.addCell(labelCF);
			 
			 wf = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD); 
			 wcfF = new WritableCellFormat(wf); 
		
			 	String agentName ="";
				List<AgentOrg> orgList = getAgentOrgList(agentCode);
				if(null!=orgList && !orgList.isEmpty()){
					agentName= orgList.get(0).getOrg_name();	
				}else{
					agentName = "";
				}
				
				
				
				 labelCF = new Label(0, row++, "尊敬的代理商："+agentName); 
				 sheet.addCell(labelCF);
				
				if(agenttype==2){//个人
					 labelCF = new Label(0, row++, "     您好，感谢您一直以来对芒果网的支持，并希望您能一如既往的关注芒果网。"); 
					 sheet.addCell(labelCF);
					 labelCF = new Label(0, row++, "为了能尽快将佣金返还给您，现提供您"+agentStatiss.getStatYear()+"年"+(agentStatiss.getStatMonth()+1)+"月份业绩明细（如下表），请仔细核对，如确认无误，我司将直接把税后佣金汇入您指定的帐户。如日后发现所汇佣金有误，我们将在下个月作相应调整。"); 
					 sheet.addCell(labelCF);
					 labelCF = new Label(0, row++, "另外，请于收到款项后一周内向我司回传确认，否则将视同确认收款，请知晓。"); 
					 sheet.addCell(labelCF);			
				}else{//公司
					 labelCF = new Label(0, row++, "     您好，感谢您一直以来对芒果网的支持，并希望您能一如既往的关注芒果网。"); 
					 sheet.addCell(labelCF);
					 labelCF = new Label(0, row++, "为了能尽快将佣金返还给您，现提供您"+agentStatiss.getStatYear()+"年"+(agentStatiss.getStatMonth()+1)+"月份业绩明细（如下表），请仔细核对，如确认无误，请开具贵公司的发票：抬头为“芒果网有限公司”，内容为“佣金”或者“服务费”，并请尽快"); 
					 sheet.addCell(labelCF);
					 labelCF = new Label(0, row++, "寄回芒果网。收到您的发票后我们尽快将您的佣金汇入您的帐号，请注意查收。如日后发现所汇佣金有误，我们将在下个月作相应调整；但是如所寄发票金额小于确认函金额的，差异部分则"); 
					 sheet.addCell(labelCF);
					 labelCF = new Label(0, row++, "不在下个月多扣少补。另外，请于收到款项后一周内向我司回传确认，否则将视同确认收款,请知晓。"); 
					 sheet.addCell(labelCF);			
				}

			 
			 row = row+1;
			 labelCF = new Label(0, row, "代理商名称"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(1, row, "统计年份"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(2, row, "统计月份"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(3, row, "订单总数"); 
			 sheet.addCell(labelCF);
/*			 labelCF = new Label(4, row, "订单间夜量"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(5, row, "订单总金额"); 
			 sheet.addCell(labelCF);
*/			 labelCF = new Label(4, row, "间夜量"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(5, row, "总金额"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(6, row, "税前佣金"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(7, row, "税后佣金");
			 sheet.addCell(labelCF);
			 labelCF = new Label(8, row++, "税金");
			 sheet.addCell(labelCF);
			 
			 labelCF = new Label(0, row, agentStatiss.getAgentName()); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(1, row, agentStatiss.getStatYear()); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(2, row, (agentStatiss.getStatMonth()+1)+"月"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(3, row, agentStatiss.getOrderNum()+""); 
			 sheet.addCell(labelCF);
/*			 labelCF = new Label(4, row, agentStatiss.getNightsNum()+""); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(5, row, agentStatiss.getSumAcount()+""); 
			 sheet.addCell(labelCF);
*/			 labelCF = new Label(4, row, agentStatiss.getActNightsNum()+""); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(5, row, agentStatiss.getActSumAcount()+""); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(6, row, agentStatiss.getCommsion()+""); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(7, row, agentStatiss.getFactcomm()+"");
			 sheet.addCell(labelCF);
			 labelCF = new Label(8, row++, agentStatiss.getCommrate()+"");
			 sheet.addCell(labelCF);
			 
			 labelCF = new Label(0, row++, "酒店销售明细:"); 
			 sheet.addCell(labelCF);
			 
			 labelCF = new Label(0, row, "订单编号"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(1, row, "酒店名称"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(2, row, "入住房型(子房型)"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(3, row, "客人姓名"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(4, row, "预订账号"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(5, row, "入住日期"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(6, row, "离店日期"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(7, row, "间夜量"); 
			 sheet.addCell(labelCF);
			 if(policyScope==1){
				 labelCF = new Label(8, row, "房费单价"); 
				 sheet.addCell(labelCF);
				 labelCF = new Label(9, row, "佣金价"); 
				 sheet.addCell(labelCF);
				 labelCF = new Label(10, row, "佣金率");
				 sheet.addCell(labelCF);
				 labelCF = new Label(11, row++, "佣金");
				 sheet.addCell(labelCF);
			 }else if(policyScope==2){
				 labelCF = new Label(8, row, "佣金价"); 
				 sheet.addCell(labelCF);
				 labelCF = new Label(9, row++, "佣金金额");
				 sheet.addCell(labelCF);
			 }
			 
			 
	         List<AgentOrder> detailList = agentStatiss.getOrderItems();
	          int col;
	          for (int i = 0; i < detailList.size(); i++) {
	        	  AgentOrder ago = detailList.get(i);
	        	  List<AgentOrderItem> itemLst = ago.getOrderItems();
	        	  for(int j=0;j<itemLst.size();j++,row++){
	        		AgentOrderItem aoi = itemLst.get(j);
		         labelCF = new Label(0, row, ago.getOrderCd());
		         sheet.addCell(labelCF);
		         labelCF = new Label(1, row, ago.getHotelName());
		         sheet.addCell(labelCF);
		         labelCF = new Label(2, row, ago.getRoomName()+"("+ago.getChildRoomName()+")");
		         sheet.addCell(labelCF);
		         labelCF = new Label(3, row, aoi.getFellowname());
		         sheet.addCell(labelCF);
		         labelCF = new Label(4, row,ago.getBookingname());
		         sheet.addCell(labelCF);
		         labelCF = new Label(5, row, DateUtil.dateToString(aoi.getNight()));
		         sheet.addCell(labelCF);
		         labelCF = new Label(6, row,DateUtil.dateToString(DateUtil.getDate(aoi.getNight(),1)));
		         sheet.addCell(labelCF);
		         labelCF = new Label(7, row, "1");
		         sheet.addCell(labelCF);
		         
		         
		         if(policyScope==1){
			         labelCF = new Label(8, row, aoi.getSaleprice()+"");
			         sheet.addCell(labelCF);
			         labelCF = new Label(9, row, aoi.getAgentReadComissionPrice()+"");
			         sheet.addCell(labelCF);
			         labelCF = new Label(10, row, aoi.getAgentReadComissionRate()+"");
			         sheet.addCell(labelCF);
			         labelCF = new Label(11, row, aoi.getAgentReadComission()+"");
			         sheet.addCell(labelCF);
			         
		         }else if(policyScope==2){
			         labelCF = new Label(8, row, aoi.getAgentReadComissionPrice()+"");
			         sheet.addCell(labelCF);
			         labelCF = new Label(9, row, aoi.getAgentReadComission()+"");
			         sheet.addCell(labelCF);
		        	 
		         }
	        	}
	        }
	
	          labelCF = new Label(0, row, "合计:"); 
			  sheet.addCell(labelCF);
	          labelCF = new Label(1, row, ""); 
			  sheet.addCell(labelCF);
	          labelCF = new Label(2, row, "");
			  sheet.addCell(labelCF);
	          labelCF = new Label(3, row, "");
			  sheet.addCell(labelCF);
	          labelCF = new Label(4, row, "");
			  sheet.addCell(labelCF);
	          labelCF = new Label(5, row, ""); 
			  sheet.addCell(labelCF);
	          labelCF = new Label(6, row, ""); 
			  sheet.addCell(labelCF);
	          labelCF = new Label(7, row, agentStatiss.getActNightsNum()+"");
			  sheet.addCell(labelCF);
			  if(policyScope==1){
		          labelCF = new Label(8, row, agentStatiss.getSumAcount()+""); 
				  sheet.addCell(labelCF);
		          labelCF = new Label(9, row, agentStatiss.getActSumAcount()+""); 
				  sheet.addCell(labelCF);				  
		          labelCF = new Label(10, row, ""); 
				  sheet.addCell(labelCF);				  
		          labelCF = new Label(11, row, agentStatiss.getCommsion()+""); 
				  sheet.addCell(labelCF);				  
				  
			  }else if(policyScope==2){
		          labelCF = new Label(8, row, agentStatiss.getActSumAcount()+""); 
				  sheet.addCell(labelCF);
		          labelCF = new Label(9, row, agentStatiss.getCommsion()+""); 
				  sheet.addCell(labelCF);
				  
			  }
	          labelCF = new Label(0, row++, ""); 
			  sheet.addCell(labelCF);
			  
	          labelCF = new Label(0, row++, "另外，请核对以下内容："); 
			  sheet.addCell(labelCF);
			  
	          labelCF = new Label(0, row, "您的开户银行："); 
			  sheet.addCell(labelCF);
	          labelCF = new Label(2, row++, agentOrg.getBankname()); 
			  sheet.addCell(labelCF);

			  
	          labelCF = new Label(0, row, "您的帐号/卡号："); 
			  sheet.addCell(labelCF);
	          labelCF = new Label(2, row++, agentOrg.getAccount()); 
			  sheet.addCell(labelCF);
			  
	          labelCF = new Label(0, row, "您的开户名称："); 
			  sheet.addCell(labelCF);
	          labelCF = new Label(2, row++, agentOrg.getBankusername()); 
			  sheet.addCell(labelCF);
			  
			  row++;
	          labelCF = new Label(0, row++, "如果您对我们的服务有任何意见或者建议，请拨打代理商专属热线：800 9990 999 或 400 8848 888(手机)与我们联系！"); 
			  sheet.addCell(labelCF);
			 
	          labelCF = new Label(0, row++, "                                                                                         芒果网有限公司市场部"); 
			  sheet.addCell(labelCF);
			  
			  labelCF = new Label(0, row++, "                                                                                          "+DateUtil.dateToString(new Date())); 
			  sheet.addCell(labelCF);
			  
			  
										
			  WritableCellFormat mergeFormat = new WritableCellFormat();
			   //文字垂直居中对齐
			  mergeFormat.setVerticalAlignment(jxl.format.VerticalAlignment.TOP); 

			    
//			  sheet.mergeCells(0, row, 13, row+2);
	//          labelCF = new Label(0, row++, tempStr,mergeFormat); 
		//	  sheet.addCell(labelCF);
			  
	         //httpResponse.setContentType("application/txt");
	         httpResponse.setContentType( "application/nvd.ms-excel "); 
	         httpResponse.setHeader("Content-disposition", "attachment; filename=" + "ReportConfirm" + ".xls");
			 workbook.write();
			 workbook.close();
	         
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
			super.forwardError("导出Excel异常"+e.getMessage());
	    }

	}
	
	

	/**
	 * 得到床型名称
	 * @param bedtype
	 * @return
	 */
	public String getBedTypeName(int bedtype){
		String bedName = "";
		if(1==bedtype){
			bedName = "大床";
		}else if(2==bedtype){
			bedName = "双床";
		}else if(3==bedtype){
			bedName = "单床";
		}
		return bedName;
	}
	
	public void setParam(Map<String,Object> params,HttpServletRequest request){
		String beginDate = null;
		String endDate = null;
		String beginItemDate = null;
		String endItemDate = null;
		beginDate = DateUtil.getBeginDate(DateUtil.getDate(Integer.parseInt(this.getYear()), Integer.parseInt(month)));
		endDate   = DateUtil.getEndDate(DateUtil.getDate(Integer.parseInt(this.getYear()), Integer.parseInt(month)));
		params.put("beginQueryDate", beginDate);
		params.put("endQueryDate", endDate);
		beginItemDate = DateUtil.formatDateToSQLString(DateUtil.newDate(Integer.parseInt(month)+1, statDate,Integer.parseInt(this.getYear())));//每月5日财务回写数据
		endItemDate = DateUtil.formatDateToSQLString(DateUtil.newDate(Integer.parseInt(month)+2, statDate,Integer.parseInt(this.getYear())));//每月5日财务回写数据
		
		params.put("beginItemDate", beginItemDate);
		params.put("endItemDate", endItemDate);
		//params.put("agentCode", WebUtils.getAgentCode(super.request));
		params.put("agentCode", agentCode);
		List<AgentOrg> orgList = getAgentOrgList(agentCode);
		if(null!=orgList && !orgList.isEmpty()){
			params.put("agentName", orgList.get(0).getOrg_name());	
		}else{
			params.put("agentName", "");
		}
		
		//params.put("operaterId", WebUtils.getOperId(super.request));
		params.put("operaterId", operaterId);
		params.put("year", this.getYear());
	}
	public String getYear() {
		if(!StringUtil.isValidStr(year))year=String.valueOf(DateUtil.getYearOfSysTime());
		return year;
	}
	


	/**
	 * 得到agentorg列表
	 * @param agentCode
	 * @return
	 */
	public List<AgentOrg> getAgentOrgList(String agentCode){
		FormatMap newmap = new FormatMap();
		newmap.put("agentcode", agentCode);
		List<AgentOrg> orgList = getQueryDao().queryForList("queryAgentOrgObject", newmap);
		return orgList;

	}
	
	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}
}



