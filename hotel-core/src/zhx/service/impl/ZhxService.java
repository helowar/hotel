package zhx.service.impl;



import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import zhx.constant.ResultConstant;
import zhx.constant.ZhxChannelType;
import zhx.constant.ZhxErrorMsg;
import zhx.dto.HeaderType;
import zhx.dto.IdentityInfoType;
import zhx.dto.OTRequestHARQ;
import zhx.dto.OTResponseHARS;
import zhx.dto.ObjectFactory;
import zhx.dto.ObjectFactoryHARS;
import zhx.dto.Source;
import zhx.dto.OTRequestHARQ.HotelAvailRQ;
import zhx.dto.OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria;
import zhx.dto.OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.HotelSearchCriteria;
import zhx.dto.OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.StayDateRange;
import zhx.dto.OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.HotelSearchCriteria.HotelRef;
import zhx.service.IZhxMappingService;
import zhx.service.IZhxService;
import zhx.vo.HdlRes;
import zhx.vo.SingleHotelReq;

import com.mangocity.hdl.hotel.dto.MGExResult;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.util.log.MyLog;

/**
 * 中航信接口实现
 * 
 * @author chenkeming
 *
 */
public class ZhxService implements IZhxService {
	
	private static final MyLog log = MyLog.getLogger(ZhxService.class);
	
	/**
	 * zhx接口的url
	 */
	private String url;
	
	/**
	 * zhx接口的officeId
	 */
	private String officeId;
	
	/**
	 * zhx接口的userId
	 */
	private String userId;
	
	/**
	 * zhx接口的password
	 */
	private String password;
	
	/**
	 * zhx接口的bookingChannel
	 */
	private String bookingChannel = "HOTELBE";
	
	/**
	 * zhx接口的application
	 */
	private String application = "hotelbe";
	
	/**
	 * 公用的ObjectFactory
	 */
	private ObjectFactory zhxFactory;
	
	/**
	 * 公用的context
	 */
	private static JAXBContext context;
	
	/**
	 * 中航信的映射service
	 */
	private IZhxMappingService zhxMappingService;
	
	static {
		try {
			context = JAXBContext.newInstance(ObjectFactory.class);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
	}	
	
	/**
	 * 查询单个酒店
	 * 
	 * @param req
	 * @return
	 */
	public OTResponseHARS querySingleAvail(OTRequestHARQ req) throws Exception {
			
		// 设置req的认证信息
		HeaderType header = new HeaderType();
		req.setHeader(header);
		header.setApplication(application);	
		header.setLanguage("CN");
		req.setTransactionName("TH_HotelSingleAvailRQ");
		IdentityInfoType identityInfo = new IdentityInfoType();
		req.setIdentityInfo(identityInfo);
        JAXBElement<String> e = zhxFactory.createIdentityInfoTypeOfficeID(officeId);
		identityInfo.setOfficeID(e);
		identityInfo.setUserID(userId);
		identityInfo.setPassword(password);			
		Source source = new Source();
		req.setSource(source);
		source.setOfficeCode(officeId);		

		// marshall
		Marshaller marshaller = context.createMarshaller();
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		marshaller.marshal(req, bao);
		String s = new String(bao.toByteArray(), "UTF-8");
		String strRes = getHttpData(s);

		// unmarshall
		JAXBContext jc = JAXBContext.newInstance(ObjectFactoryHARS.class);
		Unmarshaller un = jc.createUnmarshaller();
		OTResponseHARS response = (OTResponseHARS) un
				.unmarshal(new StreamSource(new StringReader(strRes)));
		return response;
	
	}
	
	
	/**
	 * CC或网站查询单个酒店
	 * 
	 * @param shr
	 * @return
	 */
	public HdlRes<OTResponseHARS> querySingleAvailForOrder(SingleHotelReq shr) throws Exception {
		
		HdlRes<OTResponseHARS> res = new HdlRes<OTResponseHARS>();
		MGExResult result = new MGExResult();
		res.setResult(result);
		
		ExMapping mapping = zhxMappingService.getMapping(ZhxChannelType.CHANNEL_ZHX, shr.getHotelId(), 
				shr.getRoomTypeId(), shr.getChildRoomTypeId());
		if(null == mapping) {
			result.setValue(ResultConstant.RESULT_MANGO_ERROR);
			result.setMessage("产品映射管理中,没有找到对应的合作方的编码!!");
			return res;
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
		hotelRef.setHotelCode(mapping.getHotelcodeforchannel()); // 酒店编码
		StayDateRange dateRange = new StayDateRange();
		dateRange.setCheckInDate(shr.getCheckInDate()); // 入住日期
		dateRange.setCheckOutDate(shr.getCheckOutDate()); // 离店日期
		availCriteria.setStayDateRange(dateRange);				
		
		OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.HotelSearchCriteria.RoomStayCandidates
			stayCandidates = new OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.HotelSearchCriteria.RoomStayCandidates();
		searchCriteria.setRoomStayCandidates(stayCandidates);
		OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.HotelSearchCriteria.RoomStayCandidates.RoomStayCandidate 
			stayCandidate = new OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.HotelSearchCriteria.RoomStayCandidates.RoomStayCandidate();
		stayCandidates.setRoomStayCandidate(stayCandidate);
		stayCandidate.setRoomTypeCode(mapping.getRoomtypecodeforchannel()); // 房型代码
		stayCandidate.setQuantity(String.valueOf(shr.getQuantity())); // 房间数量
		
		OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.RatePlanCandidates cands = new OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.RatePlanCandidates();
		availCriteria.setRatePlanCandidates(cands);
		OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.RatePlanCandidates.RatePlanCandidate 
			cand = new OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.RatePlanCandidates.RatePlanCandidate();
		cands.setRatePlanCandidate(cand);
		cand.setRatePlanCode(mapping.getRateplancode()); // 价格代码
		OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.RatePlanCandidates.RatePlanCandidate.VendorsIncluded 
			vi = new OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.RatePlanCandidates.RatePlanCandidate.VendorsIncluded();
		cand.setVendorsIncluded(vi);		
		OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.RatePlanCandidates.RatePlanCandidate.VendorsIncluded.Vendor 
			vendor = new OTRequestHARQ.HotelAvailRQ.HotelAvailCriteria.RatePlanCandidates.RatePlanCandidate.VendorsIncluded.Vendor();
		vi.setVendor(vendor);
		vendor.setVendorCode(mapping.getRoomtypecode()); // 供应商代码, 这里用roomtypecode字段来表示中航信酒店的供应商代码
		
			
		// 设置req的认证信息
		HeaderType header = new HeaderType();
		req.setHeader(header);
		header.setApplication(application);	
		header.setLanguage("CN"); // 默认中文形式
		req.setTransactionName("TH_HotelSingleAvailRQ");
		IdentityInfoType identityInfo = new IdentityInfoType();
		req.setIdentityInfo(identityInfo);
        JAXBElement<String> e = zhxFactory.createIdentityInfoTypeOfficeID(officeId);
		identityInfo.setOfficeID(e);
		identityInfo.setUserID(userId);
		identityInfo.setPassword(password);			
		Source source = new Source();
		req.setSource(source);
		source.setOfficeCode(officeId);		

		// marshall
		Marshaller marshaller = context.createMarshaller();
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		marshaller.marshal(req, bao);
		String s = new String(bao.toByteArray(), "UTF-8");
		String strRes = getHttpData(s);

		// unmarshall
		JAXBContext jc = JAXBContext.newInstance(ObjectFactoryHARS.class);
		Unmarshaller un = jc.createUnmarshaller();
		OTResponseHARS response = (OTResponseHARS) un
				.unmarshal(new StreamSource(new StringReader(strRes)));
				
		OTResponseHARS.Errors errors = response.getErrors();
		if(null != errors) { // ZHX系统级错误
			OTResponseHARS.Errors.Error error = errors.getError();
			log
					.info("Zhx querySingleAvailForOrder price exception, ZHX inteface system error, errorCode : "
							+ error.getErrorCode()
							+ ", errorDesc : "
							+ error.getErrorDesc()
							+ ", hotel(mango) : "
							+ mapping.getHotelname()
							+ "("
							+ mapping.getHotelid() + ")");
			result.setValue(ResultConstant.RESULT_CHANNEL_ERROR);
			// result.setMessage(ZhxErrorMsg.MsgMap.get(error.getErrorCode()));
			result.setMessage(error.getErrorDesc());
			return res;
		}
		
		OTResponseHARS.HotelAvailRS availRs = response.getHotelAvailRS();
		OTResponseHARS.HotelAvailRS.Errors errorsAvail = availRs.getErrors();
		if(null != errorsAvail) { // ZHX业务级错误
			OTResponseHARS.HotelAvailRS.Errors.Error error = errorsAvail.getError();
			log
					.info("Zhx update price exception, ZHX inteface business error, errorCode : "
							+ error.getErrorCode()
							+ ", errorDesc : "
							+ error.getErrorDesc()
							+ ", hotel(mango) : "
							+ mapping.getHotelname()
							+ "("
							+ mapping.getHotelid() + ")");
			result.setValue(ResultConstant.RESULT_CHANNEL_ERROR);
			// result.setMessage(ZhxErrorMsg.MsgMap.get(error.getErrorCode()));
			result.setMessage(error.getErrorDesc());
			return res;
		}
		
		// 正常返回结果
		result.setValue(ResultConstant.RESULT_SUCCESS);
		res.setObj(response);
		
		return res;
	
	}
	
	/**
	 * 查询多个酒店
	 * 
	 * @param req
	 * @return
	 */
	public OTResponseHARS queryMultiAvail(OTRequestHARQ req) throws Exception {
		
		// 设置req的认证信息
		HeaderType header = new HeaderType();
		req.setHeader(header);
		header.setApplication(application);	
		header.setLanguage("CN");
		req.setTransactionName("TH_HotelMultiAvailRQ");
		IdentityInfoType identityInfo = new IdentityInfoType();
		req.setIdentityInfo(identityInfo);
        JAXBElement<String> e = zhxFactory.createIdentityInfoTypeOfficeID(officeId);
		identityInfo.setOfficeID(e);
		identityInfo.setUserID(userId);
		identityInfo.setPassword(password);
		Source source = new Source();
		req.setSource(source);
		source.setOfficeCode(officeId);		

		// marshall
		Marshaller marshaller = context.createMarshaller();
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		marshaller.marshal(req, bao);		
		String s = new String(bao.toByteArray(), "UTF-8");
		String strRes = getHttpData(s);

		// unmarshall
		JAXBContext jc = JAXBContext.newInstance(ObjectFactoryHARS.class);
		Unmarshaller un = jc.createUnmarshaller();
		OTResponseHARS response = (OTResponseHARS) un
				.unmarshal(new StreamSource(new StringReader(strRes)));
		return response;
	}
	
	/**
	 * http连接获取接口数据
	 * 
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	private String getHttpData(String xml) throws Exception {				
		
		PostMethod postMethod = new PostMethod(url);
		postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
				
		NameValuePair nvp = new NameValuePair();
		nvp.setName("request");
		nvp.setValue(xml);
//		postMethod.setQueryString(new NameValuePair[]{ nvp });
		postMethod.setRequestBody(new NameValuePair[]{ nvp });
		StringBuffer html = new StringBuffer(1024);				
		HttpClient httpClient = new HttpClient();
		try {
			/*int status = */httpClient.executeMethod(postMethod);
//			String resStr = postMethod.getResponseBodyAsString();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					postMethod.getResponseBodyAsStream(), postMethod
							.getResponseCharSet()));
			String tempbf;
			while ((tempbf = br.readLine()) != null) {
				html.append(tempbf);
			}
			br.close();
//			return resStr;
		} finally {
			postMethod.releaseConnection();	
		}			
		
		return html.toString();		
	}


	public void setBookingChannel(String bookingChannel) {
		this.bookingChannel = bookingChannel;
	}


	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public void setZhxFactory(ObjectFactory zhxFactory) {
		this.zhxFactory = zhxFactory;
	}


	public void setZhxMappingService(IZhxMappingService zhxMappingService) {
		this.zhxMappingService = zhxMappingService;
	}
}
