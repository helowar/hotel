package hk.com.cts.ctcp.hotel.service.impl;

import hk.com.cts.ctcp.hotel.dao.IExDao;
import hk.com.cts.ctcp.hotel.service.ICtsAuditManager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;

import com.mangocity.util.DateUtil;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.log.MyLog;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Mail;

/**
 * 
 * 对中旅订单进行日审
 * 
 * @author chenkeming
 */
public class CtsAuditManager implements ICtsAuditManager {
	
	private static final MyLog log = MyLog.getLogger(CtsAuditManager.class);
	
	/**
	 * 中旅dao
	 */
	private IExDao exDao;
    
    /**
     * 邮件等service
     */
    private CommunicaterService communicaterService;
    
    /**
     * 对中旅订单进行日审
     */
    @SuppressWarnings("unchecked")
    public void getCtsAuditOrder() {
    	
    	// 获取中旅订单信息
		List liHk = exDao.getHKOrderInfos();
		Map mapHk = new HashMap();
		if (null != liHk && 0 < liHk.size()) {
			for (int i = 0; i < liHk.size(); i++) {
				Object obj = (Object) liHk.get(i);
				if (null != obj) {
					String txnNo = obj.toString();
					mapHk.put(txnNo, "1");
				}
			}
		}
		
		StringBuffer buf = new StringBuffer("");
		
		// 获取芒果中旅单信息
        List liHotel = exDao.getMangoHKOrderInfos();
		if (null != liHotel && 0 < liHotel.size()) {
			for (int i = 0; i < liHotel.size(); i++) {
				Object[] obj = (Object[]) liHotel.get(i);
				if (null != obj[0]) {
					String txnNo = obj[0].toString();
					if(!mapHk.containsKey(txnNo)) {
						buf.append("中旅订单号:" + txnNo);
						String orderId = null != obj[1] ? obj[1].toString() : "0";
						List liOrder = exDao.getMangoOrderCd(orderId);
						if(null != liOrder && 0 < liOrder.size()) {							
							buf.append(" ,酒店订单号:" + liOrder.get(0).toString());
							buf.append("(链接:http://10.10.5.166/HOP/order/orderOperate!edit.action?orderId=" + orderId + ")");							
						}
						buf.append("\n");
					}
				}
			}
		}
		buf.append("\n");
		
		// 获取度假中旅单信息
		List liPkg = exDao.getPkgHKOrderInfos();
		if (null != liPkg && 0 < liPkg.size()) {
			for (int i = 0; i < liPkg.size(); i++) {
				Object[] obj = (Object[]) liPkg.get(i);
				if (null != obj[0]) {
					String txnNo = obj[0].toString();
					if (!mapHk.containsKey(txnNo)) {
						buf.append("中旅订单号:" + txnNo);
						String orderId = null != obj[1] ? obj[1].toString()
								: "0";
						List liOrder = exDao.getPkgOrderCd(orderId);
						if (null != liOrder && 0 < liOrder.size()) {
							buf.append(",度假订单号:" + liOrder.get(0).toString());							
						}
						buf.append("\n");
					}
				}
			}
		}
		buf.append("\n");
		
		if(buf.length() < 14) {
			buf.append("本日无问题中旅单\n");
		}
		
		// 以下发邮件
        Mail mail = new Mail();
        mail.setApplicationName("hotel");
        String templateNo = FaxEmailModel.HOTEL_CTS_ORDER;        
        String[] toAddress = {"hrateam.financing@mangocity.com", "pzl.cc"};        
        mail.setTo(toAddress);        
        Date now = new Date();
        Date today = DateUtil.getDate(now);
        Date yesterday = DateUtil.getDate(today, -1);        
        String title = "中旅单日审邮件(" + DateUtil.dateToString(yesterday) + ")";        
        mail.setSubject(title);
        mail.setTemplateFileName(templateNo);
        mail.setFrom("cs@mangocity.com");
        mail.setUserLoginId("hotelcc");
                
        org.dom4j.Document document = DocumentHelper.createDocument();
        org.dom4j.Element rootElementE = document.addElement("QuestionOrder");        
        org.dom4j.Element orderUrl = rootElementE.addElement("orderUrl");
        orderUrl.setText(buf.toString());                      
        mail.setXml(document.asXML());

        Long ret = 0L;
        try {
            ret = communicaterService.sendEmail(mail);
        } catch (Exception e) {
        	log.error("中旅单日审邮件发送报异常！" + e);     
        }
        if (null == ret || 0 >= ret) {
        	log.info("中旅单日审邮件发送失败,ret=" + ret);
        }		
        
        // 新增房型提醒
        buf.delete(0,buf.length());
		liPkg = exDao.getHKRoomInfos();
		String strDelRt = "";
		if (null != liPkg && 0 < liPkg.size()) {
			for (int i = 0; i < liPkg.size(); i++) {
				Object[] obj = (Object[]) liPkg.get(i);
				if (null != obj[0]) {
					Integer hotelId = Integer.parseInt(obj[0].toString());
					if(null == hotelId) {						
						continue;
					}
					buf.append("新增房型:" + obj[2].toString() + "(" + (null != obj[3] ? obj[3].toString() : "") 
							+ ")," + "所属酒店:"); 
					liHk = exDao.getMangoHotelName(hotelId);
					if (null != liHk && 0 < liHk.size()) {
						Object hotelName = (Object)liHk.get(0);
						if(null == hotelName) {
							continue;
						}					
						buf.append(hotelName.toString());
					}
					buf.append("(酒店id:" + hotelId + "),创建时间:" + obj[4].toString() + ";\n");
					strDelRt += obj[1].toString();
					if(i == 0) {
						strDelRt += ",";
					}
				}
			}
		}
		buf.append("\n");
		
        // 新增价格类型提醒
		liPkg = exDao.getHKPriceInfos();
		String strDelCrt = "";
		if (null != liPkg && 0 < liPkg.size()) {
			for (int i = 0; i < liPkg.size(); i++) {
				Object[] obj = (Object[]) liPkg.get(i);
				if (null != obj[0]) {
					Integer roomTypeId = Integer.parseInt(obj[0].toString());
					if(null == roomTypeId) {						
						continue;
					}
					buf.append("新增价格类型:" + obj[2].toString()  
							+ "(价格类型id:" + obj[1].toString() + "),所属房型:"); 
					liHk = exDao.getMangoRoomName(roomTypeId);
					if (null != liHk && 0 < liHk.size()) {
						Object hotelName = (Object)liHk.get(0);
						if(null == hotelName) {
							continue;
						}					
						buf.append(hotelName.toString());
					}
					buf.append("(房型id:" + roomTypeId + "),创建时间:" + obj[3].toString() + ";\n");
					strDelCrt += obj[1].toString();
					if(i == 0) {
						strDelCrt += ",";
					}
				}
			}
		}
		
		// 以下发邮件
        mail = new Mail();
        mail.setApplicationName("hotel");
        templateNo = FaxEmailModel.HOTEL_CTS_ALERT;                       
        mail.setTo(new String[]{"hrateam.financing@mangocity.com"});                
        title = "中旅提醒邮件(" + DateUtil.dateToString(yesterday) + "):有新增中旅房型";        
        mail.setSubject(title);
        mail.setTemplateFileName(templateNo);
        mail.setFrom("cs@mangocity.com");
        mail.setUserLoginId("hotelcc");
                
        document = DocumentHelper.createDocument();
        rootElementE = document.addElement("CtsInfoHead");        
        orderUrl = rootElementE.addElement("CtsInfo");
        orderUrl.setText(buf.toString());                      
        mail.setXml(document.asXML());
        ret = 0L;
        boolean bSuc = true;
        try {
            ret = communicaterService.sendEmail(mail);
        } catch (Exception e) {
        	log.error("中旅单提醒邮件发送报异常！" + e);     
        	bSuc = false;
        }
        if (null == ret || 0 >= ret) {
        	log.info("中旅单提醒邮件发送失败,ret=" + ret);
        	bSuc = false;
        }
        if (bSuc) {
			String sql = "update htl_roomtype_cts set active='1' where active='0' and comm_level = 0 and room_type_id in ("
					+ strDelRt + ")";
			exDao.insertlog(sql);
			sql = "update htl_roomtype_cts set active='1' where active='0' and comm_level = 1 and room_type_id in ("
				+ strDelCrt + ")";
			exDao.insertlog(sql);
		}
    }

	public void setCommunicaterService(CommunicaterService communicaterService) {
		this.communicaterService = communicaterService;
	}

	public void setExDao(IExDao exDao) {
		this.exDao = exDao;
	}
    
}
