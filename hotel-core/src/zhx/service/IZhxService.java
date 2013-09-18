package zhx.service;

import zhx.dto.OTRequestHARQ;
import zhx.dto.OTResponseHARS;
import zhx.vo.HdlRes;
import zhx.vo.SingleHotelReq;



/**
 * 提供给HOP等应用的接口(连接中航信)
 * 
 * @author chenkeming
 *
 */
public interface IZhxService {

	/**
	 * 查询单个酒店
	 * 
	 * @param req
	 * @return
	 */
	public OTResponseHARS querySingleAvail(OTRequestHARQ req) throws Exception;
	
	/**
	 * CC或网站查询单个酒店
	 * 
	 * @param shr
	 * @return
	 */
	public HdlRes<OTResponseHARS> querySingleAvailForOrder(SingleHotelReq shr) throws Exception;
	
	
	/**
	 * 查询多个酒店
	 * 
	 * @param req
	 * @return
	 */
	public OTResponseHARS queryMultiAvail(OTRequestHARQ req) throws Exception;
}
