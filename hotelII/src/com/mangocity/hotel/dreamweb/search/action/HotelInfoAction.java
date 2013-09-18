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

import com.mangocity.util.StringUtil;
import com.mangocity.hotel.base.service.ChannelCashBackManagerService;
import com.mangocity.hotel.base.web.InitServletImpl;
import com.mangocity.hotel.comment.dto.CommentCountDTO;
import com.mangocity.hotel.comment.dto.CommentQueryDTO;
import com.mangocity.hotel.comment.dto.CommentSessionDTO;
import com.mangocity.hotel.comment.dto.CommentdetailsDTO;
import com.mangocity.hotel.comment.dto.HtlEvaluationDTO;
import com.mangocity.hotel.comment.dto.PaginationSupport;
import com.mangocity.hotel.dreamweb.comment.AccessCommentDetail;
import com.mangocity.hotel.dreamweb.comment.model.CommentStatistics;
import com.mangocity.hotel.dreamweb.comment.model.DaoDaoBasicComment;
import com.mangocity.hotel.dreamweb.comment.model.ImpressionStatistics;
import com.mangocity.hotel.dreamweb.comment.service.CommentStatisticsService;
import com.mangocity.hotel.dreamweb.comment.service.IDaoDaoCommentService;
import com.mangocity.hotel.dreamweb.comment.service.ImpressionStatisticsService;
import com.mangocity.hotel.dreamweb.displayvo.HtlAlbumVO;
import com.mangocity.hotel.dreamweb.displayvo.HtlPictureSizeVO;
import com.mangocity.hotel.dreamweb.displayvo.HtlPictureVO;
import com.mangocity.hotel.dreamweb.orderrecord.service.QueryOrderRecordService;
import com.mangocity.hotel.dreamweb.search.service.HotelInfoService;
import com.mangocity.hotel.dreamweb.search.util.HotelInfoUtil;
import com.mangocity.hotel.search.handler.impl.HotelQueryHandler;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;
import com.mangocity.hotel.search.model.HotelPictureInfoJson;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;
import com.mangocity.util.web.CookieUtils;
import com.mangocity.webnew.persistence.HtlChannelClickLog;
import com.mangocity.webnew.util.action.GenericWebAction;
import com.opensymphony.xwork2.ActionContext;

public class HotelInfoAction  extends GenericWebAction{

	private static final long serialVersionUID = -342438239192151788L;
	/**
	 * 判断默认Ip城市需要使用的文件路径
	 */
	private final String IP_CITY_FILE_PATH = "/ipcity";
	private HotelInfoService hotelInfoService;
	private IDaoDaoCommentService daoDaoCommentService;
	private long logId;
	private String hotelId;
	private String inDate;
	private String outDate;
	private HotelBasicInfo hotelInfo;
	private DaoDaoBasicComment daoDaoBasicComment;
	private HotelManageWeb hotelManageWeb;
	private int day=1;
	private String roomType;
	private String channel;
	private String label ; //是否从海外酒店进
	private List hotelNameAndIdLis = new ArrayList();
	private String promoteType;//醒狮活动类型
	
	private double roomprice; //去哪儿渠道传递价格
	
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
	private QueryOrderRecordService queryOrderRecordService;
	private CommentStatistics commnentStatistics;
	private List<ImpressionStatistics> impressionStatisticses;
	/**
	 * hotel-info.jsp图片展示
	 */
	private String moreList;
	
	/**
	 * hotel-info.jsp图片展示
	 */
	private String dataList;
	
	/**
	 * 图片数量
	 */
	private long pictureCount = 0;
	
	/**
	 * 外观封面图
	 */
	private String appearanceAlbumPicture;
	
	private String projectcode;
	
	//针对渠道控制百分点返利比例 add by hushunqiang
	private ChannelCashBackManagerService channelCashBackService;
	private double cashbackrate;
	
	private static final MyLog log = MyLog.getLogger(HotelInfoAction.class);
	public String execute() throws UnsupportedEncodingException{
		hotelId=request.getParameter("hotelId");
		setHotelId(hotelId);
		if(validateParam()){
			log.error("输入了非法的酒店Id"+hotelId);
			super.setErrorCode("H00");
			return super.forwardError("很抱歉，您输入的信息存在非法字符，查询不到该酒店！如需要，请致电芒果网客服电话40066-40066了解具体情况！");
		}
		validateBookDate();
		
		
		label = getRequest().getParameter("label");
		String ipAddress = getRequest().getRemoteAddr();
		HotelBasicInfoSearchParam hotelBasicInfoSearchParam=new HotelBasicInfoSearchParam();
		hotelBasicInfoSearchParam.setHotelId(hotelId);
		
		Cookie[] cookies = getRequest().getCookies();
		if(null==inDate || null==outDate){
		if(null!=cookies&&cookies.length>0) {
			for(int i=0;i<cookies.length;i++) {
				if(cookies[i].getName().equals("inDate")&&(null==inDate||"".equals(inDate))) {
					inDate = cookies[i].getValue();
				}else if(cookies[i].getName().equals("outDate")&&(null==outDate||"".equals(outDate))) {
					outDate = cookies[i].getValue();
				}
			}
		}
		}
		
		if (!StringUtil.isValidStr(inDate)) {
			inDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), day));
		}
		if (!StringUtil.isValidStr(outDate)) {
			outDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), day + 1));
		}
		
		//支付的查询天为14天 add by diandian.hou 2011-12-13
		if("nest".equals(label)){
			inDate=DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 14));
			outDate=DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), 15));
			
		} 
						
		//取projectcode,已经把逻辑迁移到queryOrderRecordService的方法中
		//projectcode = hotelManageWeb.getProjectCodeForWeb(getRequest()); 
		//request.setAttribute("projectcode",projectcode);
		
		saveOrderRecord(hotelId);
		
		hotelBasicInfoSearchParam.setInDate(DateUtil.getDate(inDate));
		hotelBasicInfoSearchParam.setOutDate(DateUtil.getDate(outDate));
		try{
		hotelInfo=hotelInfoService.queryHotelInfo(hotelBasicInfoSearchParam,ipAddress);
				
		if(hotelInfo!=null){
		hotelInfo.setCommendType(HotelQueryHandler.CommendTypeConvert.getCssStyleName(hotelInfo.getCommendType()));//这里保存的是css样式
		hotelNameAndIdLis = addCookies(hotelId);	    
		//测试用：
		//projectcode="0027001";
		
		/* 已经不用了，更改为：记录到l_htl_order_flow表中，change by ting.li
		if(StringUtil.isValidStr(projectcode)){ //目前没有判断是否重复刷新
			request.setAttribute("projectcode",projectcode);
			//String city = getIpCity();
			String city=null;
			HtlChannelClickLog clickLog = new HtlChannelClickLog();
			clickLog.setHotelId(Long.valueOf(hotelId));
			clickLog.setCheckInDate(DateUtil.getDate(inDate));
			clickLog.setCheckOutDate(DateUtil.getDate(outDate));
			clickLog.setClick("0");
			clickLog.setClickDate(new Date());
			clickLog.setClickTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
			clickLog.setProjectCode(projectcode);
			clickLog.setCity(city);
			clickLog.setRoomPrice(roomprice);
			//clickLog.setChannel(channel==null?"WEB":channel);
			clickLog.setRoomType(URLDecoder.decode(roomType==null?"":roomType, "utf8"));
			try{
			hotelManageWeb.addChannelLog(clickLog);
			}catch(Exception e){
				log.error("HotelInfoAction hotelManageWeb.addChannelLog has a wrong", e);
			}
			setLogId(clickLog.getID().longValue());
		}
		*/
		
		
		getHotelCommentInfo();
		getCommentDetails();
		convertHotelPicture(hotelInfo.getHtlAlbumVOList());
		if(!daoDaoCommentService.hasDaoDaoCommentTBBeenInitted()){
			daoDaoCommentService.updateHtlCommentDaoDaoTable();
		}
		//从数据库中获取到到网点评条数信息和评分图片URL                 by panjianping
		daoDaoBasicComment=daoDaoCommentService.getBasicCommentByHotelId(hotelInfo.getHotelId());
		//如果没有该酒店没有对应的到到网点评信息，则将点评条数置为0      by panjianping
		if(daoDaoBasicComment==null){
			daoDaoBasicComment = new DaoDaoBasicComment();
			daoDaoBasicComment.setTotalNumber(0);
			daoDaoBasicComment.setRatingUrl("");
			daoDaoBasicComment.setDaodaoId(0L);
		  }
		}
		else{
			log.info("传入的酒店Id 查询不到就酒店信息或者为空  "+hotelId);
			super.setErrorCode("H03");
			return super.forwardError("很抱歉，您输入的信息，查询不到该酒店的信息！如需要，请致电芒果网客服电话40066-40066了解具体情况");
		}
		}catch(Exception e){
			log.error("HotelInfoAction execute has a wrong", e);
			super.setErrorCode("H04");
			return super.forwardError("很抱歉，你输入的信息，查询不到该酒店！如需要，请致电芒果网客服电话40066-40066了解具体情况");
		}
		projectcode = CookieUtils.getCookieValue(request, "projectcode");
		cashbackrate = channelCashBackService.getChannelCashBacktRate(projectcode);
		return SUCCESS;
	}
	
	private void saveOrderRecord(String hotelId){
		try {
			if (StringUtil.isValidStr(hotelId) && hotelId.matches("\\d+")) {
				queryOrderRecordService.saveOrderRecord(request, super.getHttpResponse(), DateUtil.getDate(inDate), DateUtil.getDate(outDate), Long
						.valueOf(hotelId), 4);
			} else {
				queryOrderRecordService.saveOrderRecord(request, super.getHttpResponse(), DateUtil.getDate(inDate), DateUtil.getDate(outDate), 0L, 4);
			}
		} catch (Exception e) {
			log.error(" update order record has a wrong", e);
		}
	}
	
	/**
	 * 转换为json数据
	 * @param hotelPictureInfoMap
	 */
	private void convertHotelPicture(List<HtlAlbumVO> htlAlbumVOList) {
		List<HotelPictureInfoJson> moreListJson = new ArrayList<HotelPictureInfoJson>();
		String pictureUrl = InitServletImpl.PICTUREURL;
		
		List<HotelPictureInfoJson> dataListJson = new ArrayList<HotelPictureInfoJson>();
		/*详情页五张图片展示规则 (classify 1外观 2大堂 3logo 4服务设施 0房型 6其它)
		 * 第一张图片取外观相册的封面图 classify=1
		 * 第二张图片取大堂相册的封面图 classify=2
		 * 其余图片取房型相册图片
		 * 
		 * */
		//外观封面图片ID 
		long appearanceAlbumPictureId = -1;
		HotelPictureInfoJson  appearancePictureInfoJson = null;
		//大堂封面图片ID
		long lobbyAlbumPictureId = -1;
		HotelPictureInfoJson  lobbyPictureInfoJson = null;
		//房型封面图片ID
		long roomTypeAlbumPictureId = -1;
		HotelPictureInfoJson  roomTypePictureInfoJson = null;
		//logo封面图片ID
		long logoAlbumPictureId = -1;
		HotelPictureInfoJson  logoPictureInfoJson = null;
		//服务设施封面ID
		long serviceAlbumPictureId = -1;
		HotelPictureInfoJson  servicePictureInfoJson = null;
		//其它封面ID
		long otherAlbumPictureId = -1;
		HotelPictureInfoJson  otherPictureInfoJson = null;
		
		for(HtlAlbumVO htlAlbumVO : htlAlbumVOList){
			int  albumClassify = htlAlbumVO.getAlbumClassify();
			long albumCoverId = htlAlbumVO.getAlbumCoverId();
			switch(albumClassify){
				case 0: roomTypeAlbumPictureId = albumCoverId; roomTypePictureInfoJson = getAlbumPicture(htlAlbumVO); break; //房型
				case 1: appearanceAlbumPictureId = albumCoverId; appearancePictureInfoJson = getAlbumPicture(htlAlbumVO); break; //外观
				case 2: lobbyAlbumPictureId = albumCoverId; lobbyPictureInfoJson = getAlbumPicture(htlAlbumVO); break;  //大堂
				case 3: logoAlbumPictureId = albumCoverId; logoPictureInfoJson = getAlbumPicture(htlAlbumVO); break; //logo
				case 4: serviceAlbumPictureId = albumCoverId; servicePictureInfoJson = getAlbumPicture(htlAlbumVO); break; //服务设施
				case 6: otherAlbumPictureId = albumCoverId; otherPictureInfoJson = getAlbumPicture(htlAlbumVO); break; //其它
			}
		}
		if(-1 != appearanceAlbumPictureId && null != appearancePictureInfoJson) moreListJson.add(appearancePictureInfoJson);
		if(-1 != lobbyAlbumPictureId && null != lobbyPictureInfoJson) moreListJson.add(lobbyPictureInfoJson);
		if(-1 != roomTypeAlbumPictureId && null != roomTypePictureInfoJson) moreListJson.add(roomTypePictureInfoJson);
		if(-1 != logoAlbumPictureId && null != logoPictureInfoJson) moreListJson.add(appearancePictureInfoJson);
		if(-1 != serviceAlbumPictureId && null != servicePictureInfoJson) moreListJson.add(servicePictureInfoJson);
		if(5 > moreListJson.size() && -1 != otherAlbumPictureId && null != otherPictureInfoJson) moreListJson.add(otherPictureInfoJson);
		
		
		
		
		for(HtlAlbumVO htlAlbumVO : htlAlbumVOList){
			for(HtlPictureVO htlPictureVO :htlAlbumVO.getHtlPictureVOList()){
				long pictureId = htlPictureVO.getPictureId();

				int	moreListSize = moreListJson.size();
				
				String pictureName = htlPictureVO.getPictureName();
				HotelPictureInfoJson dataHotelPictureInfoJson = new HotelPictureInfoJson();
				boolean moreListFlag = false;
				HotelPictureInfoJson moreHotelPictureInfoJson = null;
				//图片数量小于5、不是外观封面、大堂封面、房型封面、logo封面、服务设施封面、其它相册封面
				if(moreListSize<5 && appearanceAlbumPictureId != pictureId && lobbyAlbumPictureId != pictureId && roomTypeAlbumPictureId != pictureId && logoAlbumPictureId != pictureId && serviceAlbumPictureId != pictureId && otherAlbumPictureId != pictureId){ 
					moreListFlag = true;
					moreHotelPictureInfoJson = new HotelPictureInfoJson();
				}
				
				
				for(HtlPictureSizeVO htlPictureSizeVO :htlPictureVO.getHtlPictureSizeVOList()){
					int pictureType = htlPictureSizeVO.getPictureType();
					switch(pictureType){
						case 2:{
							dataHotelPictureInfoJson.setBigInfo(pictureName);
							dataHotelPictureInfoJson.setBigSrc(pictureUrl+htlPictureSizeVO.getPictureUrl());
							break;
						}
						case 3:{
							if(moreListFlag){
								moreHotelPictureInfoJson.setBigInfo(pictureName);
								moreHotelPictureInfoJson.setBigSrc(pictureUrl+htlPictureSizeVO.getPictureUrl());
							}
							break;
						}
						case 4:{
							dataHotelPictureInfoJson.setSmallInfo(pictureName);
							dataHotelPictureInfoJson.setSmallSrc(pictureUrl+htlPictureSizeVO.getPictureUrl());
							break;
						}
						case 5:{
							if(moreListFlag){
								moreHotelPictureInfoJson.setSmallInfo(pictureName);
								moreHotelPictureInfoJson.setSmallSrc(pictureUrl+htlPictureSizeVO.getPictureUrl());
							}
							break;
						}
					}
				}
				
				dataListJson.add(dataHotelPictureInfoJson);
				if(moreListFlag) moreListJson.add(moreHotelPictureInfoJson);
			}
		}
		
		pictureCount = dataListJson.size(); 
		
		moreList = net.sf.json.JSONArray.fromObject(moreListJson).toString();
		
		dataList = net.sf.json.JSONArray.fromObject(dataListJson).toString();
	}
	
	private HotelPictureInfoJson getAlbumPicture(HtlAlbumVO htlAlbumVO){
		HotelPictureInfoJson moreHotelPictureInfoJson = null;
		long albumCoverId = htlAlbumVO.getAlbumCoverId(); //封面图片ID
		for(HtlPictureVO htlPictureVO :htlAlbumVO.getHtlPictureVOList()){
			if(albumCoverId == htlPictureVO.getPictureId()){
				moreHotelPictureInfoJson = new HotelPictureInfoJson();
				for(HtlPictureSizeVO htlPictureSizeVO :htlPictureVO.getHtlPictureSizeVOList()){
					int pictureType = htlPictureSizeVO.getPictureType();
					if(3 == pictureType){
						if(1 == htlAlbumVO.getAlbumClassify()) appearanceAlbumPicture = InitServletImpl.PICTUREURL+htlPictureSizeVO.getPictureUrl();
						moreHotelPictureInfoJson.setBigSrc(InitServletImpl.PICTUREURL+htlPictureSizeVO.getPictureUrl());
						moreHotelPictureInfoJson.setBigInfo(htlPictureVO.getPictureName());
					}else if(5 == pictureType){
						moreHotelPictureInfoJson.setSmallSrc(InitServletImpl.PICTUREURL+htlPictureSizeVO.getPictureUrl());
						moreHotelPictureInfoJson.setSmallInfo(htlPictureVO.getPictureName());
					} 
				}
				break;
			}
		}
		return moreHotelPictureInfoJson;
	}

	public String computerCount(){
		try{
		ActionContext.getContext().getActionInvocation().getProxy().setExecuteResult(false);					
		outPrintCount();
		}catch(Exception e){
			log.error("HotelInfoAction computerCount() has a wrong ",e);
		}
		return null;
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
	private void outPrintCount() throws Exception{
		
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
		if(sumRow==null){
			sumRow=0L;
		}
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
					HotelBasicInfo hotel=null;
					try{
					if(StringUtil.isValidStr(hotelIdd)){	
					hotel = hotelInfoService.queryHotelInfoByHotelId(hotelIdd);
					}
					}catch(Exception e){
						super.setErrorCode("H05");
						log.error("HotelInfoAction addCookie has a wrong", e);
					}
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
	
	public String updateBookingLog(){
		if(logId!=0){
			hotelManageWeb.updateChannelLog(logId);
		}
		ActionContext.getContext().getActionInvocation().getProxy().setExecuteResult(false);
		return null;
	}
	 */
	
	/**
	 * 校验输入的酒店Id，防止非法写入
	 * @return
	 */
	private boolean validateParam(){
		if(hotelId != null){
			if(!hotelId.matches("\\d+")){
				return true;
			}
		}
		
		return false;
	}
	/**
	 * 校验输入的日期
	 */
	private void validateBookDate(){
		if(inDate!=null){
			if(!inDate.matches("\\d{4}-\\d{1,2}-\\d{1,2}")){
				inDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), day));
				outDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), day+1));
			}
			
		}
		if(outDate!=null){
			if(!outDate.matches("\\d{4}-\\d{1,2}-\\d{1,2}")){
				inDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), day));
				outDate = DateUtil.dateToString(DateUtil.getDate(DateUtil.getSystemDate(), day+1));
			}
			
		}
		
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

	public IDaoDaoCommentService getDaoDaoCommentService() {
		return daoDaoCommentService;
	}

	public void setDaoDaoCommentService(IDaoDaoCommentService daoDaoCommentService) {
		this.daoDaoCommentService = daoDaoCommentService;
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
    
	public DaoDaoBasicComment getDaoDaoBasicComment() {
		return daoDaoBasicComment;
	}

	public void setDaoDaoBasicComment(DaoDaoBasicComment daoDaoBasicComment) {
		this.daoDaoBasicComment = daoDaoBasicComment;
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

	public String getPromoteType() {
		return promoteType;
	}

	public void setPromoteType(String promoteType) {
		this.promoteType = promoteType;
	}

	public String getMoreList() {
		return moreList;
	}

	public void setMoreList(String moreList) {
		this.moreList = moreList;
	}

	public String getDataList() {
		return dataList;
	}

	public void setDataList(String dataList) {
		this.dataList = dataList;
	}

	public void setRoomprice(double roomprice) {
		this.roomprice = roomprice;
	}

	public long getPictureCount() {
		return pictureCount;
	}

	public void setPictureCount(long pictureCount) {
		this.pictureCount = pictureCount;
	}

	public String getAppearanceAlbumPicture() {
		if(!StringUtil.isValidStr(this.appearanceAlbumPicture)){
			this.appearanceAlbumPicture = "image/big.jpg";
		}
		return appearanceAlbumPicture;
	}

	public String getProjectcode() {
		return projectcode;
	}

	public void setProjectcode(String projectcode) {
		this.projectcode = projectcode;
	}

	public void setQueryOrderRecordService(QueryOrderRecordService queryOrderRecordService) {
		this.queryOrderRecordService = queryOrderRecordService;
	}

	public ChannelCashBackManagerService getChannelCashBackService() {
		return channelCashBackService;
	}

	public void setChannelCashBackService(
			ChannelCashBackManagerService channelCashBackService) {
		this.channelCashBackService = channelCashBackService;
	}

	public double getCashbackrate() {
		return cashbackrate;
	}

	public void setCashbackrate(double cashbackrate) {
		this.cashbackrate = cashbackrate;
	}
}
