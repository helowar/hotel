package com.mangocity.hotel.order.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.mangocity.hotel.base.manage.IQuotaManage;
import com.mangocity.hotel.base.web.webwork.PaginateAction;

public class QueryBuyQuotaExcelAction  extends PaginateAction{
	
	private static final long serialVersionUID = 1238234073584240463L;
	
	private String beginDate;
	private String endDate;
	private String hotelId;
	private IQuotaManage quotaManage;
	
	public void exportExcel(){		
		List<Map<String, String>> buyQuotaList = quotaManage.queryBuyQuotaReport(beginDate, endDate, hotelId);
		
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(super.getHttpResponse().getOutputStream());
			 WritableSheet sheet = workbook.createSheet("1", 0);
			 int row=0; 
			 Label labelCF = new Label(0, row, "日期"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(1, row, "酒店名称"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(2, row, "房型"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(3, row, "普通总数"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(4, row, "普通已用数"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(5, row, "普通过期数"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(6, row, "包房总数"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(7, row, "包房已用数"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(8, row, "包房过期数"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(9, row, "呼出配额数"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(10, row, "临时总数"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(11, row, "临时已用数"); 
			 sheet.addCell(labelCF);
			 labelCF = new Label(12, row, "临时过期数"); 
			 sheet.addCell(labelCF);
			 for(Map<String, String> recordMap : buyQuotaList){
				 row++;
				 if(null!=recordMap.get("ABLE_SALE_DATE"))
					 labelCF = new Label(0, row, recordMap.get("ABLE_SALE_DATE")); 
				 	 sheet.addCell(labelCF);
			 	if(null!=recordMap.get("CHN_NAME"))
					 labelCF = new Label(1, row,  recordMap.get("CHN_NAME")); 
				 	 sheet.addCell(labelCF);
			 	if(null!=recordMap.get("ROOM_NAME"))
					 labelCF = new Label(2, row,  recordMap.get("ROOM_NAME")); 
				 	 sheet.addCell(labelCF);
			 	if(null!=recordMap.get("COMMON_QUOTA_SUM"))
				 	 sheet.addCell(new jxl.write.Number(3, row, Double.parseDouble(recordMap.get("COMMON_QUOTA_SUM"))));
			 	if(null!=recordMap.get("COMMON_QUOTA_USED_NUM"))
				 	 sheet.addCell(new jxl.write.Number(4, row, Double.parseDouble(recordMap.get("COMMON_QUOTA_USED_NUM"))));
			 	if(null!=recordMap.get("COMMON_QUOTA_OUTOFDATE_NUM"))
				 	 sheet.addCell(new jxl.write.Number(5, row, Double.parseDouble(recordMap.get("COMMON_QUOTA_OUTOFDATE_NUM"))));
			 	if(null!=recordMap.get("BUY_QUOTA_SUM"))
				 	 sheet.addCell(new jxl.write.Number(6, row, Double.parseDouble(recordMap.get("BUY_QUOTA_SUM"))));
			 	if(null!=recordMap.get("BUY_QUOTA_USED_NUM"))
				 	 sheet.addCell(new jxl.write.Number(7, row, Double.parseDouble(recordMap.get("BUY_QUOTA_USED_NUM"))));
			 	if(null!=recordMap.get("BUY_QUOTA_OUTOFDATE_NUM"))
				 	 sheet.addCell(new jxl.write.Number(8, row, Double.parseDouble(recordMap.get("BUY_QUOTA_OUTOFDATE_NUM"))));
			 	if(null!=recordMap.get("OUTSIDE_QUOTA_SUM"))
				 	 sheet.addCell(new jxl.write.Number(9, row, Double.parseDouble(recordMap.get("OUTSIDE_QUOTA_SUM"))));
			 	if(null!=recordMap.get("CASUAL_QUOTA_SUM"))
				 	 sheet.addCell(new jxl.write.Number(10, row, Double.parseDouble(recordMap.get("CASUAL_QUOTA_SUM"))));
			 	if(null!=recordMap.get("CASUAL_QUOTA_USED_NUM"))
				 	 sheet.addCell(new jxl.write.Number(11, row, Double.parseDouble(recordMap.get("CASUAL_QUOTA_USED_NUM"))));
			 	if(null!=recordMap.get("CASUAL_QUOTA_OUTOFDATE_NUM"))
				 	 sheet.addCell(new jxl.write.Number(12, row, Double.parseDouble(recordMap.get("CASUAL_QUOTA_OUTOFDATE_NUM"))));
				 	 
			 }
			 httpResponse.setContentType( "application/nvd.ms-excel "); 
			 httpResponse.setHeader("Content-disposition", "attachment; filename=ReportConfirm.xls");
			 workbook.write();
			 workbook.close();
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		} catch (RowsExceededException e) {
			log.error(e.getMessage(),e);
		} catch (WriteException e) {
			log.error(e.getMessage(),e);
		}
	}

	public IQuotaManage getQuotaManage() {
		return quotaManage;
	}

	public void setQuotaManage(IQuotaManage quotaManage) {
		this.quotaManage = quotaManage;
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

	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	
}
