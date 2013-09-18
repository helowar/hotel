package com.mangocity.hotel.base.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts2.ServletActionContext;

import com.mangocity.hotel.base.persistence.BatchUpdateParam;
import com.mangocity.hotel.base.persistence.HtlSupplierInfo;
import com.mangocity.hotel.base.service.BatchUpdatePriceService;
import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.hotel.util.CdmUtil;
import com.opensymphony.xwork2.ActionContext;


public class BatchUpdatePriceAction extends GenericAction {

	private TreeMap<String, String> localProvinceObj;

	private String stateAjax;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
	private BatchUpdatePriceService batchUpdatePriceService;
	
	private List<HtlSupplierInfo> supplyList= null;
	
	private List<String []> modelExportList= null;
	private List<String []> modelImportList= new ArrayList<String []>();
	
	//页面参数
	
	private String hotelName;
	private String supplyId;
	private String productManager;
	private String province;
	private String city;
	private String payMethod;
	
	
	//上传文件所需要
	private File file ;
	private String fileFileName ;
	private String fileFileContentTypes;
	
	private Integer confirmFlag=0;
	@Override
	public String execute() throws Exception {

		localProvinceObj = (TreeMap<String, String>) mapSort(InitServlet.localProvinceObj);
        
		supplyList=batchUpdatePriceService.getSupplyId();
		
		return SUCCESS;
	}
    
	
	public String importExcel(){
		String returnStr="successBack";
        initParam();
		
		BatchUpdateParam bachUpdateParam = new BatchUpdateParam();	
		bachUpdateParam.setHotelName(hotelName);
		bachUpdateParam.setSupplyId(supplyId);
		bachUpdateParam.setProductManager(productManager);
		bachUpdateParam.setProvince(province);
		bachUpdateParam.setCity(city);	
		bachUpdateParam.setPayMethod(payMethod);
		roleUser = getOnlineRoleUser();
        try {
			Workbook book = Workbook.getWorkbook(file);
		    
			Sheet sheet = book.getSheet(0);
			
			Integer cols = sheet.getColumns();
			Integer rows = sheet.getRows();
			
			if((rows-1)>301){
				returnStr="failBack";
			}else{
				for(int m = 0 ; m<=rows-1 ; m++){
					String [] data = new String[cols];
					for(int n = 0 ; n<cols ; n++){					
						data[n] = sheet.getCell(n, m).getContents();					
					}
					modelImportList.add(data);
					data=null;
				}
				batchUpdatePriceService.batchUpdatePrice(modelImportList,roleUser,bachUpdateParam);
			}
        } catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnStr;
	}
	
	public String exportExcel() throws IOException{
		initParam();
		
		BatchUpdateParam bachUpdateParam = new BatchUpdateParam();		
		bachUpdateParam.setSupplyId(supplyId);
		if(productManager!=null){
			bachUpdateParam.setProductManager(URLDecoder.decode(productManager,"UTF-8"));				
		}
		
		bachUpdateParam.setProvince(province);
		bachUpdateParam.setCity(city);		
		modelExportList = batchUpdatePriceService.getExportData(bachUpdateParam);
		
		OutputStream os = null;
	    HttpServletResponse response=ServletActionContext.getResponse();
	    WritableWorkbook wbook = null;
		   try {
			   os = response.getOutputStream();
			   response.reset();// 清空输出流   
			   String fileName = new String("导出批量更新价格模板.xls".getBytes("GBK"),"ISO8859_1");       
			   response.setHeader("Content-disposition", "attachment; filename="+fileName);// 设定输出文件头   
			   response.setContentType("application/msexcel");// 定义输出类型 
	
			   
			   wbook = Workbook.createWorkbook(os); //建立excel文件
	
			   WritableSheet wsheet = wbook.createSheet("批量更新价格1", 0); //工作表名称
	
			   //设置Excel字体
	
			   WritableFont wfont = new WritableFont(WritableFont.TIMES, 8, WritableFont.BOLD, false,
			                          jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
	
			   WritableCellFormat titleFormat = new WritableCellFormat(wfont);
			   titleFormat.setWrap(false);
			   titleFormat.setBackground(jxl.format.Colour.LIGHT_TURQUOISE);
			   titleFormat.setAlignment(Alignment.CENTRE);
			   titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
			   titleFormat.setBorder(Border.ALL,   BorderLineStyle.THIN);
			   
			 //设置列的宽度
			   wsheet.setColumnView(0,10);
			   wsheet.setColumnView(1,30);
			   wsheet.setColumnView(2,10);
			   wsheet.setColumnView(3,30);
			   wsheet.setColumnView(4,10);
			   wsheet.setColumnView(5,30);
			   wsheet.setColumnView(6,10);
			   wsheet.setColumnView(7,30);
			   wsheet.setColumnView(8,10);
               for(int m=8 ; m<=37 ; m++){
            	   wsheet.setColumnView(m,15);
		       }
			   
              //设置行的高度
			   wsheet.setRowView(0, 400);
			   

			 //生成标题
			   String[] title = new String[39]; 
			   title[0]="供应商ID";
			   title[1]="供应商名称";
			   title[2]="酒店ID";
			   title[3]="酒店名称";
			   title[4]="房型ID";
			   title[5]="房型名称";
			   title[6]="价格类型ID";
			   title[7]="价格类型名称";
			   title[8]="佣金";
			   int n=0;
			   for(int m=9 ; m<=38 ; m++){
				   title[m]=getFutureDate(n++);
		       }
			   
			 //写入标题
			   for(int m=0 ; m<title.length ;m++){
				   wsheet.addCell(new Label(m, 0, title[m],titleFormat)); 
			   }
			  
			   
			 //写入内容
			   wfont = new WritableFont(WritableFont.TIMES, 8, WritableFont.NO_BOLD, false,
                       jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
			   WritableCellFormat contentFormat = new WritableCellFormat(wfont);
			   contentFormat.setWrap(false);
			   contentFormat.setAlignment(Alignment.CENTRE);
			   contentFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
			   contentFormat.setBorder(Border.ALL,   BorderLineStyle.THIN);
			   
			   
			   for(int i=1 ; i<=modelExportList.size() ; i++){
				   for(int j=0 ; j<9 ; j++){
					   wsheet.addCell(new Label(j, i, modelExportList.get(i-1)[j],contentFormat)); 
				   }
				   for(int j=9 ; j<39 ; j++){
					   wsheet.addCell(new Label(j, i, null,contentFormat)); 
				   }
				   
			   }
			   
			   wbook.write(); // 写入文件   			   			   
		} catch (Exception e) {
			// TODO Auto-generated catch block		
		}finally{
			wbook.close();  
			os.close();
		}
	    
		return null;
	}
	
	public String ajaxMessage() throws IOException{		
		HttpServletResponse response = ServletActionContext.getResponse();
		BatchUpdateParam bachUpdateParam = new BatchUpdateParam();		
		bachUpdateParam.setSupplyId(supplyId);
		bachUpdateParam.setProductManager(productManager);
		bachUpdateParam.setProvince(province);
		bachUpdateParam.setCity(city);		
		modelExportList = batchUpdatePriceService.getExportData(bachUpdateParam);
		response.getWriter().write("1");
		response.getWriter().flush();
		response.getWriter().close();
		return null;
	}
	
	public String ajaxCitys() throws Exception {
		String citys = CdmUtil.getCitys(stateAjax);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(citys);
		response.getWriter().flush();
		response.getWriter().close();
		return null;
	}
    
	
	
	public void initParam(){
		hotelName = (hotelName!=null && hotelName.trim().length()>0 ? hotelName : null);
		supplyId = (supplyId!=null && supplyId.trim().length()>0 ? supplyId : null);
		productManager = (productManager!=null && productManager.trim().length()>0 ? productManager : null);
		province = (province!=null && province.trim().length()>0 ? province : null);
		city = (city!=null && city.trim().length()>0 ? city : null);
		payMethod = (payMethod!=null && payMethod.trim().length()>0 ? payMethod : null);
		
	}
	
	
	
	public Map<String, String> mapSort(Map map) {
		Map<String, String> provinceMap = new TreeMap<String, String>();
		Set<String> keySet = map.keySet();

		List<String> keyList = new ArrayList<String>();

		if (keySet != null) {
			Iterator it = keySet.iterator();
			while (it.hasNext()) {
				keyList.add((String) it.next());
			}

			Collections.sort(keyList);

			Iterator it1 = keyList.iterator();
			while (it1.hasNext()) {
				String key = (String) it1.next();
				provinceMap.put(key, (String) map.get(key));
			}

		}
		map = null;
		return provinceMap;
	}
    
	/**
	 * 获取未来日期  
	 * @param num  
	 * @return 当前日期+num(天)
	 */
	public String getFutureDate(Integer num){
		    Date date=new Date();//取时间
	        Calendar calendar = new GregorianCalendar();
	        calendar.setTime(date);
	        calendar.add(calendar.DATE,num);//把日期往后增加一天.整数往后推,负数往前移动
	        date=calendar.getTime(); //这个时间就是日期往后推一天的结果 
	        return sdf.format(date);
	}
	
	public TreeMap<String, String> getLocalProvinceObj() {
		return localProvinceObj;
	}

	public void setLocalProvinceObj(TreeMap<String, String> localProvinceObj) {
		this.localProvinceObj = localProvinceObj;
	}

	public String getStateAjax() {
		return stateAjax;
	}

	public void setStateAjax(String stateAjax) {
		this.stateAjax = stateAjax;
	}

	public BatchUpdatePriceService getBatchUpdatePriceService() {
		return batchUpdatePriceService;
	}

	public void setBatchUpdatePriceService(
			BatchUpdatePriceService batchUpdatePriceService) {
		this.batchUpdatePriceService = batchUpdatePriceService;
	}

	public List<HtlSupplierInfo> getSupplyList() {
		return supplyList;
	}

	public void setSupplyList(List<HtlSupplierInfo> supplyList) {
		this.supplyList = supplyList;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}



	public String getSupplyId() {
		return supplyId;
	}


	public void setSupplyId(String supplyId) {
		this.supplyId = supplyId;
	}


	public String getProductManager() {
		return productManager;
	}

	public void setProductManager(String productManager) {
		try {
			productManager=URLDecoder.decode(productManager,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.productManager = productManager;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}


	public File getFile() {
		return file;
	}


	public void setFile(File file) {
		this.file = file;
	}


	public String getFileFileName() {
		return fileFileName;
	}


	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}


	public String getFileFileContentTypes() {
		return fileFileContentTypes;
	}


	public void setFileFileContentTypes(String fileFileContentTypes) {
		this.fileFileContentTypes = fileFileContentTypes;
	}


	public Integer getConfirmFlag() {
		return confirmFlag;
	}


	public void setConfirmFlag(Integer confirmFlag) {
		this.confirmFlag = confirmFlag;
	}
    
	
}
