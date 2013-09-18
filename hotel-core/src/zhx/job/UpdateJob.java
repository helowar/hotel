package zhx.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import zhx.dto.OTRequestHARQ;
import zhx.dto.OTResponseHARS;
import zhx.dto.RoomStay;
import zhx.dto.OTRequestHARQ.HotelAvailRQ;
import zhx.dto.OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria;
import zhx.dto.OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.HotelSearchCriteria;
import zhx.dto.OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.StayDateRange;
import zhx.dto.OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.HotelSearchCriteria.HotelRef;
import zhx.dto.RoomStay.RoomRates;
import zhx.dto.RoomStay.RoomRates.RoomRate;
import zhx.dto.RoomStay.RoomRates.RoomRate.Rates;
import zhx.dto.RoomStay.RoomRates.RoomRate.Rates.Rate;
import zhx.service.IZhxManage;
import zhx.service.IZhxMappingService;
import zhx.service.IZhxScheduleService;
import zhx.service.IZhxService;

import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;

/**
 * 
 * 中航信每天定时更新价格等信息的job
 * 
 * @author chenkeming
 *
 */
public class UpdateJob extends QuartzJobBean {
	
	private static final MyLog log = MyLog.getLogger(UpdateJob.class);
	
	/**
	 * 中航信提供的webservice接口
	 */
	private IZhxService zhxService;
	
	/**
	 * 中航信的映射service
	 */
	private IZhxMappingService zhxMappingService;
	
	/**
	 * 中航信定时器参数Service接口
	 */
	private IZhxScheduleService zhxScheduleService;	
	
	/**
	 * 中航信的业务层
	 */
	private IZhxManage zhxManage;
	
	private String checkInDate;
	
	private String checkOutDate;
	
	private Map<String, ExMapping> existPlan = new HashMap<String, ExMapping>();
	
	protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
		
		// 判断中航信更新价格定时器是否需要更新价格 
        boolean needUpdatePrice = zhxScheduleService.needUpdatePriceForZhx();        
        if(!needUpdatePrice) {
        	return;
        }
        
        // 查询所有处于激活状态下的中航信酒店
		List<ExMapping> list = zhxMappingService.getAllZhxHotel();
		
		// 刷新记录成功的次数
		int updateSuccess = 0;
		// 刷新记录失败的次数
		int updateFail = 0;
		// 刷新记录部分失败的次数
		int updatePartFail = 0;
		Date today = DateUtil.getDate(new Date());
		checkInDate = DateUtil.dateToString(today);
		checkOutDate = DateUtil.dateToString(DateUtil.getDate(today, 30));
		for(ExMapping exMapping: list) {
			String loginfo = "hotel:" + exMapping.getHotelname();
			int result = updateHotel(exMapping); 
			if(0 == result){
				updateSuccess ++;
				log.info(loginfo + "价格更新成功");
			} else if(1 == result){
				updateFail ++;
				log.info(loginfo + "价格更新失败");
			} else {
				updatePartFail ++;
				log.info(loginfo + "价格更新部分失败");
			}
		}
		
		// 显示成功更新中航信酒店多少，失败更新多少
		log.info("Zhx update hotel : updateSuccess = " + updateSuccess 
				+ ", updateFail = " + updateFail
				+ ", updatePartFail = " + updatePartFail);
		
	}
	
	/**
	 * 
	 * 更新指定的某个中航信酒店的价格等信息
	 * 
	 * @param exMapping
	 * @return 0:成功, 1:失败, 2:部分失败(指更新其中部分价格计划失败)
	 */
	private int updateHotel(ExMapping exMapping) {
		
		if(null == exMapping) { 
			log.error("映射编码不存在");
			return 1;
		}
		
		OTRequestHARQ req = new OTRequestHARQ();
		HotelAvailRQ availRq = new HotelAvailRQ();
		req.setHotelAvailRQ(availRq);
		HotelAvailCriteria availCriteria = new HotelAvailCriteria();
		availRq.setHotelAvailCriteria(availCriteria);
		HotelSearchCriteria searchCriteria = new HotelSearchCriteria();
		availCriteria.setHotelSearchCriteria(searchCriteria);
		// 查询完整的酒店信息还是只查询价格、房态、政策等动态信息。 
		// noneStatics:只包含动态信息/includeStatic:含动态、静态信息(默认值)
		availCriteria.setAvailReqType("noneStatics");  
		HotelRef hotelRef = new HotelRef();
		searchCriteria.setHotelRef(hotelRef);
		hotelRef.setHotelCode(exMapping.getHotelcodeforchannel()); // 酒店编码
		StayDateRange dateRange = new StayDateRange();
		dateRange.setCheckInDate(checkInDate); // 入住日期
		dateRange.setCheckOutDate(checkOutDate); // 离店日期
		availCriteria.setStayDateRange(dateRange);
		/*RatePlanCandidates cands = new RatePlanCandidates();
		availCriteria.setRatePlanCandidates(cands);
		RatePlanCandidate cand = new RatePlanCandidate();
		cands.setRatePlanCandidate(cand);
		VendorsIncluded vi = new VendorsIncluded();
		cand.setVendorsIncluded(vi);		
		Vendor vendor = new Vendor();
		vi.setVendor(vendor);
		vendor.setVendorCode(exMapping.getRoomtypecode());*/ // 供应商代码, 这里用roomtypecode字段来表示中航信酒店的供应商代码
		OTResponseHARS response;
		try {
			// 调中航信单酒店查询接口
			response = zhxService.querySingleAvail(req);	
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return 1;
		}
		
		OTResponseHARS.Errors errors = response.getErrors();
		if(null != errors) { // ZHX系统级错误
			OTResponseHARS.Errors.Error error = errors.getError();
			log
					.info("Zhx update price exception, ZHX inteface system error, errorCode : "
							+ error.getErrorCode()
							+ ", errorDesc : "
							+ error.getErrorDesc()
							+ ", hotel(mango) : "
							+ exMapping.getHotelname()
							+ "("
							+ exMapping.getHotelid() + ")");
			return 1;
		}
		
		OTResponseHARS.HotelAvailRS availRs = response.getHotelAvailRS();
		OTResponseHARS.HotelAvailRS.Errors errorsAvail = availRs.getErrors();
		if(null != errors) { // ZHX业务级错误
			OTResponseHARS.HotelAvailRS.Errors.Error error = errorsAvail.getError();
			log
					.info("Zhx update price exception, ZHX inteface business error, errorCode : "
							+ error.getErrorCode()
							+ ", errorDesc : "
							+ error.getErrorDesc()
							+ ", hotel(mango) : "
							+ exMapping.getHotelname()
							+ "("
							+ exMapping.getHotelid() + ")");
			return 1;
		}
		
		OTResponseHARS.HotelAvailRS.RoomStays roomStays 
			=  availRs.getRoomStays();
		if(null == roomStays) {
			return 1;
		}
		
		List<ExMapping> listPlan = zhxMappingService.getRatePlanByHotel(exMapping.getHotelid());
		existPlan.clear();
		for(ExMapping plan : listPlan) {
			existPlan.put(plan.getRoomtypecodeforchannel() + plan.getRateplancode(), plan);
		}
		int success = 0, fail = 0;
		List<RoomStay> lstRoomStay = roomStays.getRoomStay();
		for (int i = 0; i < lstRoomStay.size(); i++) {
			RoomStay roomStay = lstRoomStay.get(i);
			RoomRates roomRates = roomStay.getRoomRates();
			List<RoomRate> lstRoomRate = roomRates.getRoomRate();
			for (int y = 0; y < lstRoomRate.size(); y++) {
				RoomRate roomRate = lstRoomRate.get(y);
				
				// 支付方式
				String zhxPayMethod = roomRate.getPayment();
				if("Y".equals(zhxPayMethod)) { // "中航信的预付方式"无需考虑
					continue;
				}
				String payMethod = "pay";
				if("S".equals(zhxPayMethod)) {
					payMethod = "pre-pay";
				}
				
				// 如果激活的映射价格计划中没有该价格计划, 则继续
				ExMapping plan = existPlan.get(roomRate.getRoomTypeCode() + roomRate.getRatePlanCode()); 
				if(null == plan) {
					continue;
				}				
				
				// 如果mapping还没有设置供应商代码则设置
				if(!StringUtil.isValidStr(plan.getRoomtypecode())) {
					plan.setRoomtypecode(roomRate.getVendorCode());
					zhxMappingService.saveMapping(plan);
				}
				
				Rates rates = roomRate.getRates();
				List<Rate> liRate = rates.getRate();
				boolean sucRate = true;
				for(Rate rate : liRate) {
					String startDate = rate.getStartDate();
					String endDate = rate.getEndDate();
					if(!StringUtil.isValidStr(startDate) || !StringUtil.isValidStr(endDate)) {
						sucRate = false;
						log.info("Zhx update price exception, startDate or endDate is null, hotel(mango) : " 
								+ plan.getHotelname() + "(" + plan.getHotelid() + "), ratePlan(zhx) : "
								+ plan.getRateplanname() + "(" + plan.getRateplancode() + ")");
						continue;
					}
					
					// 调存储过程更新价格
					long retId = zhxManage.updatePrice(Long.valueOf(plan.getPriceTypeId()), 
							payMethod, startDate, endDate, Double.valueOf(rate.getAmountPrice()));		
					if(0 == retId) {						
						sucRate = false;
						log.info("Zhx update price exception, database procedure error, hotel(mango) : " 
								+ plan.getHotelname() + "(" + plan.getHotelid() + "), ratePlan(zhx) : "
								+ plan.getRateplanname() + "(" + plan.getRateplancode() + ")");
					}					
				}
				if(sucRate) {
					success ++;
				} else {
					fail ++;
				}
			}
		}
		
		if(0 == fail) { // 成功
			return 0;
		} else if(0 < success) { // 部分失败
			return 2;
		} else { // 失败
			return 1;
		}
	}

	public void setZhxService(IZhxService zhxService) {
		this.zhxService = zhxService;
	}

	public void setZhxMappingService(IZhxMappingService zhxMappingService) {
		this.zhxMappingService = zhxMappingService;
	}

	public void setZhxScheduleService(IZhxScheduleService zhxScheduleService) {
		this.zhxScheduleService = zhxScheduleService;
	}

	public String getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}

	public void setZhxManage(IZhxManage zhxManage) {
		this.zhxManage = zhxManage;
	}

}
