package com.mangocity.hotel.dreamweb.search.action;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.mangocity.hotel.comment.dto.CommentCountDTO;
import com.mangocity.hotel.comment.dto.CommentQueryDTO;
import com.mangocity.hotel.comment.dto.CommentSessionDTO;
import com.mangocity.hotel.comment.dto.CommentdetailsDTO;
import com.mangocity.hotel.comment.dto.HtlEvaluationDTO;
import com.mangocity.hotel.comment.dto.PaginationSupport;
import com.mangocity.hotel.dreamweb.comment.AccessCommentDetail;
import com.mangocity.hotel.dreamweb.comment.model.CommentStatistics;
import com.mangocity.hotel.dreamweb.comment.model.ImpressionStatistics;
import com.mangocity.hotel.dreamweb.comment.service.CommentStatisticsService;
import com.mangocity.hotel.dreamweb.comment.service.ImpressionStatisticsService;
import com.mangocity.hotel.dreamweb.search.service.HotelInfoService;
import com.mangocity.hotel.dreamweb.search.util.HotelInfoUtil;
import com.mangocity.hotel.search.handler.impl.HotelQueryHandler;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;
import com.mangocity.webnew.persistence.HtlChannelClickLog;
import com.mangocity.webnew.util.action.GenericWebAction;
import com.opensymphony.xwork2.ActionContext;

public class HotelInfoPAAction  extends GenericWebAction{

	private static final long serialVersionUID = -342438239192151788L;
	/**
	 * 判断默认Ip城市需要使用的文件路径
	 */
	private final String IP_CITY_FILE_PATH = "/ipcity";
	private HotelInfoService hotelInfoService;
	private long logId;
	private String hotelId;
	private String inDate;
	private String outDate;
	private HotelBasicInfo hotelInfo;
	private HotelManageWeb hotelManageWeb;
	private int day=1;
	private String roomType;
	private String channel;
	private String label ; //是否从海外酒店进
	private List hotelNameAndIdLis = new ArrayList();
	private String phone; //传入的电话号码
	
	
	//获取酒店点评统计信息
	private HtlEvaluationDTO htlEvaluationDTO;
	private List<CommentdetailsDTO> itemsDTO;
	private String localPath;
	private Integer commentThete = 1;
	private Integer productType = 1;
	private Long recordId;
	private Integer effective;
	private PaginationSupport paginationSupport;
	private AccessCommentDetail accessCommentDetail;
	
	private CommentStatisticsService commentStatisticsService;
	private ImpressionStatisticsService impressionStatisticsService;
	private CommentStatistics commnentStatistics;
	private List<ImpressionStatistics> impressionStatisticses;
	
	
	private static final MyLog log = MyLog.getLogger(HotelInfoPAAction.class);
	public String execute() throws UnsupportedEncodingException{
		String ipAddress = getRequest().getRemoteAddr();
		HotelBasicInfoSearchParam hotelBasicInfoSearchParam=new HotelBasicInfoSearchParam();
		hotelBasicInfoSearchParam.setHotelId(hotelId);
		
		if(null==inDate||"".equals(inDate)){
			inDate=DateUtil.dateToString(DateUtil
					.getDate(DateUtil.getSystemDate(), day));		
		}
		if(null==outDate||"".equals(outDate)){
			outDate=DateUtil.dateToString(DateUtil
					.getDate(DateUtil.getSystemDate(), day+1));
		}
		
		hotelBasicInfoSearchParam.setInDate(DateUtil.getDate(inDate));
		hotelBasicInfoSearchParam.setOutDate(DateUtil.getDate(outDate));
		try{
		hotelInfo=hotelInfoService.queryHotelInfo(hotelBasicInfoSearchParam,ipAddress);
				
		}catch(Exception e){
			log.error("hotelIService error ",e);
		}
		hotelInfo.setCommendType(HotelQueryHandler.CommendTypeConvert.getCssStyleName(hotelInfo.getCommendType()));//这里保存的是css样式
		
		getHotelCommentInfo();
		getCommentDetails();
		return SUCCESS;
	}
	
			
	/**
	 * 获取酒店点评统计信息,by liting
	 */
	private void getHotelCommentInfo(){		
		try{	
		commnentStatistics=commentStatisticsService.queryCommentStatistics(Long.valueOf(hotelId));		
		impressionStatisticses=impressionStatisticsService.queryImpressionStatistics(Long.valueOf(hotelId));
		}catch(Exception e){
			log.error("HotelInfoAction getHotelCommentInfo has a wrong", e);
		}
		
		
	}
		
	/**
	 * 获取点评的信息列表，酒店详情页中的点评信息列表，by ting.li
	 * TODO Add comments here.
	 */
	@SuppressWarnings("unchecked")
	private void getCommentDetails() {
		try{
		if(accessCommentDetail!=null){
		String uploadPicsURL=accessCommentDetail.queryUploadPicsURL();		
		ActionContext.getContext().getSession().put("uploadPicsURL",uploadPicsURL);
		this.localPath = ServletActionContext.getRequest().getContextPath();
		ActionContext.getContext().getSession().put("basePath",uploadPicsURL);		
		ActionContext.getContext().getSession().put("commentIndex",accessCommentDetail.getCommentIndex(Long.valueOf(hotelId)));
		queryItemsDTO();
		}
		}catch(Exception e){
			log.error("HotelInfoAction getCommentDetails has a error", e);
		}
		
	}
    
	/**
	 * 酒店详情页中，点评信息列表中，评价点评信息有用或无用中用到，异步处理
	 * @param out
	 * @param date
	 * @throws ParseException
	 */
	private void outPrintCount(){
		
		PrintWriter out=null;
		try{
		//点击有用或无用的时间
		String date=queryDate();
		out=ServletActionContext.getResponse().getWriter();
		if (date != null && date.trim().length() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Long date1 = sdf.parse(date).getTime();
			Long date2 = System.currentTimeMillis();
			if ((date2 - date1) >= 5 * 60 * 1000L) {
				saveCommentCount();
				out.print("1");
			} else {
				out.print("2");
			}
		} else {
			saveCommentCount();
			out.print("1");
		}
		}catch(Exception e){
			log.info("HotelInfoAction out print the commentCount has a wrong");
		}
		finally{
		out.flush();
		out.close();
		}
	}

	/**
	 * 
	 * 查询点击日期，by ting.li，对点击”有用”，“无用” 时有用
	 */
	private String queryDate() {
		String date=null;
		try{
		CommentCountDTO commentCountDTO = new CommentCountDTO();
		commentCountDTO.setCommentCountIp(getIpAddr());
		commentCountDTO.setRecordId(recordId);
		date=accessCommentDetail.queryDate(commentCountDTO);}
		catch(Exception e){
			log.error("HotelInfoAction comment queryDate() has a wrong", e);
		}
		return date;
	}

	/**
	 * 保存有用和无用的点击数，by ting.li
	 */
	private void saveCommentCount() {
		try{
		CommentCountDTO commentCountDTO = new CommentCountDTO();
		commentCountDTO.setId(accessCommentDetail.getCountId());
		commentCountDTO.setCommentCountIp(getIpAddr());
		commentCountDTO.setEffective(effective);
		commentCountDTO.setRecordId(recordId);
		commentCountDTO.setCommentCountTime(new Date());
		accessCommentDetail.saveCommentCount(commentCountDTO);
		}catch(Exception e){
			log.error("HotelInfoAction comment saveCommentCount() has a wrong", e);
		}
	}
	
	/**
	 * 处理点评用户的IP地址,by ting.li
	 * @return 返回处理好的Ip地址
	 */
	private String getIpAddr() { 
		HttpServletRequest request=ServletActionContext.getRequest();
	    String ip = request.getHeader("x-forwarded-for"); 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	        ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	        ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	        ip = request.getRemoteAddr(); 
	    } 
	    return ip; 
	}
	
	/**
	 * 获取点评前15条记录,by ting.li
	 */
	@SuppressWarnings("unchecked")
	private void queryItemsDTO() {
		try{
		CommentQueryDTO commentQueryDTO = new CommentQueryDTO();
		commentQueryDTO.setHotelId(Long.valueOf(hotelId));
		commentQueryDTO.setSumRow(querySumRow());
		commentQueryDTO.setPageNo(1);
		commentQueryDTO.setCommentTheme(commentThete);
		commentQueryDTO.setImpressionMap((HashMap<Long, String>) accessCommentDetail.queryImpressionConstantInfo());
		commentQueryDTO.setGradedMap((HashMap<Long, String>) accessCommentDetail.queryGradedConstantInfo());
		commentQueryDTO.setPageSize(15);
		
		paginationSupport = accessCommentDetail.getPaginationSupport(commentQueryDTO);
		itemsDTO=(ArrayList<CommentdetailsDTO>)paginationSupport.getItemsDTO();
		handleIp(itemsDTO);
		}catch(Exception e){
			log.error("HotelInfoAction comment queryItemsDTO() has a wrong", e);
		}
		
	}
	
	
	/**
	 * 获取点评记录总条数，by ting.li
	 * @return
	 */
	private Long querySumRow() {
		
		Long sumRow=null;
		try{
		CommentSessionDTO commentSession = new CommentSessionDTO();
		commentSession.setProductId(Long.valueOf(hotelId));
		commentSession.setProductType(productType);
		commentSession.setCommentTheme(commentThete);
		sumRow=accessCommentDetail.querySumRow(commentSession);
		}catch(Exception e){
			log.error("HotelInfoAction comment querySumRow() has a wrong", e);
		}
		return sumRow;
	}

	 /*
	    * 对ip进行处理，如127.0.0.1 变为 127.0.**.**
	    */	   
	  private void handleIp(List<CommentdetailsDTO> itemsDTO){
		  String[] ipSplit=new String[10];
		  if(itemsDTO!=null){
		  for(CommentdetailsDTO obj : itemsDTO){
			  ipSplit=obj.getRecordip().split("\\.");
			  if(ipSplit.length==4){
			  obj.setRecordip(ipSplit[0]+"."+ipSplit[1]+".**.**");
			  }
		  }
		  }
	  }

	//对cookie处理
	@SuppressWarnings("unchecked")
	private List addCookies(String  hotelId) {
		List hotelIdCookies = new ArrayList();
		List hotelNameAndIdLis = new ArrayList();
		Cookie[] cookies = getRequest().getCookies();
		if(null != cookies && cookies.length>0){
			for (int j = 0; j < cookies.length; j++) {
				if (cookies[j].getName().indexOf("hotelId") > -1) {
					hotelIdCookies.add(cookies[j]);
	
				}
			}
	
			// 对cookie进行排序
			for (int ii = 0; ii < hotelIdCookies.size(); ii++) {
	
				Cookie cookieItems = (Cookie) hotelIdCookies.get(ii);
	
				for (int jj = 1; jj < hotelIdCookies.size(); jj++) {
					Cookie cookieItem = (Cookie) hotelIdCookies.get(jj);
					if (cookieItems.getMaxAge() < cookieItem.getMaxAge()) {
						hotelIdCookies.set(ii, cookieItem);
						hotelIdCookies.set(jj, cookieItems);
					}
	
				}
			}
			boolean cookieIsExist = false;
	
			// 判断是否有重复
			for (int k = 0; k < hotelIdCookies.size(); k++) {
				Cookie cook = (Cookie) hotelIdCookies.get(k);
				if (cook.getValue().equals(hotelId + "")) {
					cookieIsExist = true;
				}
	
			}
	
			// 如果存放酒店名称的cookie已经有5个了。再添加的时候要删除第一个cookie里的酒店名称，再添加新的酒店名称到cookie
			if (hotelIdCookies.size() >= 5) {
	
				if (cookieIsExist) {
					String cookieKey = "hotelId" + hotelId;
					Cookie cc = new Cookie(cookieKey, (hotelId + ""));
					cc.setMaxAge(0);
					cc.setPath("/");
					getResponse().addCookie(cc);
	
					Cookie c = new Cookie(cookieKey, (hotelId + ""));
					// cookie寿命为1个月
					c.setMaxAge(60 * 24 * 3600);
					c.setPath("/");
					getResponse().addCookie(c);
	
				} else {
					// 删除一个cookie
					Cookie cookieIte = (Cookie) hotelIdCookies.get(0);
					String cKey = cookieIte.getName();
					String cValue = cookieIte.getValue();
					Cookie c = new Cookie(cKey, cValue);
					c.setMaxAge(0);
					c.setPath("/");
					getResponse().addCookie(c);
	
					// 新增一个cookie
					String ccKey = "hotelId" + hotelId;
					Cookie cc = new Cookie(ccKey, (hotelId + ""));
					// cookie寿命为1个月
					cc.setMaxAge(60 * 24 * 3600);
					cc.setPath("/");
					getResponse().addCookie(cc);
	
				}
	
			} else {
	
				// 重复先删后曾
				if (cookieIsExist) {
					String cookieKey = "hotelId" + hotelId;
					Cookie cc = new Cookie(cookieKey, (hotelId + ""));
					cc.setMaxAge(0);
					cc.setPath("/");
					getResponse().addCookie(cc);
	
					Cookie c = new Cookie(cookieKey, (hotelId + ""));
					// cookie寿命为1个月
					c.setMaxAge(60 * 24 * 3600);
					c.setPath("/");
					getResponse().addCookie(c);
	
				} else {// hotelI的的cookie数量少于5个，直接增加。
					String cookieKey = "hotelId" + hotelId;
					Cookie cc = new Cookie(cookieKey, (hotelId + ""));
					// cookie寿命为1个月
					cc.setMaxAge(60 * 24 * 3600);
					cc.setPath("/");
					getResponse().addCookie(cc);
				}
	
			}
	
			// 排序之后封装cookie中的酒店id、酒店中文名，用于在页面显示
			for (int kk = hotelIdCookies.size() - 1; kk >= 0; kk--) {
				String[] forHotel = new String[2];
				Cookie cookieIte = (Cookie) hotelIdCookies.get(kk);
				if (cookieIte.getMaxAge() != 0) {
					String hotelIdd = cookieIte.getValue();
					HotelBasicInfo hotel = hotelInfoService.queryHotelInfoByHotelId(hotelIdd);
					if(null!=hotel){
						String hotelName = hotel.getChnName();
						forHotel[0] = hotelIdd;
						forHotel[1] = hotelName;
						hotelNameAndIdLis.add(forHotel);
					}
				} else {
					hotelIdCookies.remove(kk);
				}
	
			}

		}

		return hotelNameAndIdLis;
	}
	/**
	 * 获取Ip的城市信息
	 * @return 城市中文名
	 */
	private String getIpCity(){
		//默认城市为北京
		String city = "";
        try {
            String ip = hotelManageWeb.getIpAddr(getRequest());
            if(judgeIP(ip)){
            	return HotelInfoUtil.getCityNameByIP(ip)==null?"":HotelInfoUtil.getCityNameByIP(ip);
            }else{
            	log.info("this is clientIP illegal : "+ip);
            } 
		} catch (Exception e) {
			log.error("Get city information errors :", e);
		}
		log.info("========clientIP cityName:"+city+"=====");
	    return city;
	}
	
	/**
	 * 用于判断IP的合法性
	 * @return
	 */
	private boolean judgeIP(String ip){
		boolean flag = false;
		Pattern p=Pattern.compile(
                "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
                "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$");
        Matcher m = p.matcher(ip);
        flag = m.matches();
		return flag;
	}
	
	/**
	 * 记录别的渠道（去哪儿，百度）是否点击预订按钮
	 */
	public String updateBookingLog(){
		if(logId!=0){
			hotelManageWeb.updateChannelLog(logId);
		}
		return null;
	}
	
	public HttpServletRequest getRequest(){
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		return request;
	}

	private HttpServletResponse getResponse(){
		ActionContext ctx = ActionContext.getContext();
		HttpServletResponse response = (HttpServletResponse)ctx.get(ServletActionContext.HTTP_RESPONSE);
		return response;
	}
	public List getHotelNameAndIdLis() {
		return hotelNameAndIdLis;
	}

	public void setHotelNameAndIdLis(List hotelNameAndIdLis) {
		this.hotelNameAndIdLis = hotelNameAndIdLis;
	}

	public HotelInfoService getHotelInfoService() {
		return hotelInfoService;
	}

	public void setHotelInfoService(HotelInfoService hotelInfoService) {
		this.hotelInfoService = hotelInfoService;
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public HotelBasicInfo getHotelInfo() {
		return hotelInfo;
	}

	public void setHotelInfo(HotelBasicInfo hotelInfo) {
		this.hotelInfo = hotelInfo;
	}


	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public long getLogId() {
		return logId;
	}

	public void setLogId(long logId) {
		this.logId = logId;
	}

	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}



	public HtlEvaluationDTO getHtlEvaluationDTO() {
		return htlEvaluationDTO;
	}

	public void setHtlEvaluationDTO(HtlEvaluationDTO htlEvaluationDTO) {
		this.htlEvaluationDTO = htlEvaluationDTO;
	}

	public List<CommentdetailsDTO> getItemsDTO() {
		return itemsDTO;
	}

	public void setItemsDTO(List<CommentdetailsDTO> itemsDTO) {
		this.itemsDTO = itemsDTO;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public Integer getCommentThete() {
		return commentThete;
	}

	public void setCommentThete(Integer commentThete) {
		this.commentThete = commentThete;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Integer getEffective() {
		return effective;
	}

	public void setEffective(Integer effective) {
		this.effective = effective;
	}

	public AccessCommentDetail getAccessCommentDetail() {
		return accessCommentDetail;
	}

	public void setAccessCommentDetail(AccessCommentDetail accessCommentDetail) {
		this.accessCommentDetail = accessCommentDetail;
	}

	public PaginationSupport getPaginationSupport() {
		return paginationSupport;
	}

	public void setPaginationSupport(PaginationSupport paginationSupport) {
		this.paginationSupport = paginationSupport;
	}



	public CommentStatisticsService getCommentStatisticsService() {
		return commentStatisticsService;
	}

	public void setCommentStatisticsService(CommentStatisticsService commentStatisticsService) {
		this.commentStatisticsService = commentStatisticsService;
	}

	public ImpressionStatisticsService getImpressionStatisticsService() {
		return impressionStatisticsService;
	}

	public void setImpressionStatisticsService(ImpressionStatisticsService impressionStatisticsService) {
		this.impressionStatisticsService = impressionStatisticsService;
	}

	public CommentStatistics getCommnentStatistics() {
		return commnentStatistics;
	}

	public void setCommnentStatistics(CommentStatistics commnentStatistics) {
		this.commnentStatistics = commnentStatistics;
	}

	public List<ImpressionStatistics> getImpressionStatisticses() {
		return impressionStatisticses;
	}

	public void setImpressionStatisticses(List<ImpressionStatistics> impressionStatisticses) {
		this.impressionStatisticses = impressionStatisticses;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getPhone() {
		return phone;
	}
	
}
