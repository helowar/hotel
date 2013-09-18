package com.mangocity.hotel.order.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mangocity.hotel.order.constant.MemberConfirmSmsStutas;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.util.log.MyLog;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Fax;
import com.mangoctiy.communicateservice.domain.Mail;
import com.mangoctiy.communicateservice.domain.Sms;

public class MessageServerAdaptor extends OrderAction{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private static final MyLog log = MyLog.getLogger(MessageServerAdaptor.class);
    /**
     * 传真邮件辅助类
     */
    private MsgAssist msgAssist;
    /**
     * message接口
     */
    private CommunicaterService communicaterService;

    //收件人
	private String addressee;
	
	//邮件主题
	private String subject;
	
	//xml编号
	private String xmlNo;
	
	//emailFile　xml文件
	private File emailFile;
	
	//email Text Area
	private String emailTextArea;
	
	//emailFile名称
	private String emailFileFileName;
	
	//emailFile类型
	private String emailFileContentType;
	
	//faxNo传真号码
	private String faxNo;
	
	//faxID传真ID
	private String faxID;
	
	//传真发送者
	private String faxSender;
	
	//fax Text Area
	private String faxTextArea;
	
	//传真文件
	private File faxFile;
	
	//传真XML文件名称
	private String faxFileFileName;
	
	//传真XML文件类型
	private String faxFileContentType;
	
	//短信电话号码
	private String phoneNo;

	//短信内容
	private String msgContext;
	
	//操作人员
	private String operator;
	
	//邮件来源
	private String emailFrom;
	
	/**
	 * 发送传真接口
	 * @return
	 */
	public String toSendFax(){
		String xmlString = "";
        Fax fax = new Fax();
        //fax文件流名称
        //xmlString = getFileContent(faxFile);
        //fax.setXml(xmlString);
        fax.setXml(faxTextArea);
        //fax应用名称
        fax.setApplicationName("hotel");
        //fax编号
        fax.setTemplateFileName(faxID);
        //传真号码
        fax.setTo(new String[] { faxNo });
        //操作人员
        fax.setFrom(operator);
        Long ret = communicaterService.sendFax(fax);
        log.info(" faxNo:"+faxNo);
        log.info(" setTemplateFileName:"+faxID);
        log.info(" toSendFax ret:"+ret);
        log.info(" faxTextArea:"+faxTextArea);
        String resultXml = "";
        //发送邮件失败
        if (null == ret || 0 >= ret) {
        	resultXml = resultXml + "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        	resultXml = resultXml + "<MangoSendFax>" ;
        	resultXml = resultXml + "<result>FAILURE</result>" ;
        	resultXml = resultXml + "</MangoSendFax>" ;
        } else {
        	resultXml = resultXml + "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        	resultXml = resultXml + "<MangoSendFax>" ;
        	resultXml = resultXml + "<result>SUCCESS</result>" ;
        	resultXml = resultXml + "</MangoSendFax>" ;
        	//发送邮件成功
        }
        request.setAttribute("result", resultXml);
        return "toRes"; //"toFax"; 
	}
	
	/**
	 * 发送邮件接口
	 * @return
	 */
	public String toSendEmail(){
		String xmlString="";
        Mail mail = new Mail();
        //应用名称
        mail.setApplicationName("hotel");
        //发送地址
        mail.setTo(new String[] {addressee});
        //邮件主题
        mail.setSubject(subject);
        //xsl文件编号
        mail.setTemplateFileName(xmlNo);
        //读取emailFile文件xml内容,
        //xmlString = getFileContent(emailFile);
        //将emailFile文件xml内容赋值给mail里面的xml元素,
        //mail.setXml(xmlString);
        mail.setXml(emailTextArea);
        //邮件来源
        mail.setFrom(emailFrom);
        //操作人员
        mail.setUserLoginId(operator);
        
        Long ret = null;
        try {
            ret = communicaterService.sendEmail(mail);
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }
        
        log.info(" emailFrom: "+emailFrom);
        log.info(" addressee: "+addressee);
        log.info(" setTemplateFileName: "+xmlNo);
        log.info(" emailTextArea: "+emailTextArea);
        log.info(" operator: "+operator);
        
        int sendedStatus = MemberConfirmSmsStutas.SENDING; // 发送状态
        String resultXml = "";
        //发送邮件失败
        if (null == ret || 0 >= ret) {
        	resultXml = resultXml + "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        	resultXml = resultXml + "<MangoSendEmail>" ;
        	resultXml = resultXml + "<result>FAILURE</result>" ;
        	resultXml = resultXml + "</MangoSendEmail>" ;
        } else {
        	resultXml = resultXml + "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        	resultXml = resultXml + "<MangoSendEmail>" ;
        	resultXml = resultXml + "<result>SUCCESS</result>" ;
        	resultXml = resultXml + "</MangoSendEmail>" ;
        	//发送邮件成功
        }
        log.info(" toEmail ret: "+ret);
        
        request.setAttribute("result", resultXml);
		return "toRes"; //"toRes";
	}
	
	/**
	 * 发送短信接口
	 * @return
	 */
	public String toSendMsg(){
        Sms sms = new Sms();
        //应用名称
        sms.setApplicationName("hotel");
        //电话号码
        sms.setTo(new String[] { phoneNo });
        //短信内容
        sms.setMessage(msgContext);
        //操作人员
        sms.setFrom(operator);
        Long ret = communicaterService.sendSms(sms);
        String resultXml = "";
        //发送邮件失败
        if (null == ret || 0 >= ret) {
        	resultXml = resultXml + "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        	resultXml = resultXml + "<MangoSendMsg>" ;
        	resultXml = resultXml + "<result>FAILURE</result>" ;
        	resultXml = resultXml + "</MangoSendMsg>" ;
        } else {
        	resultXml = resultXml + "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        	resultXml = resultXml + "<MangoSendMsg>" ;
        	resultXml = resultXml + "<result>SUCCESS</result>" ;
        	resultXml = resultXml + "</MangoSendMsg>" ;
        	//发送邮件成功
        }
        request.setAttribute("result", resultXml);
        return "toRes";
	}

	public String getFileContent(File src){
		InputStream in = null;
		String result = "";
		StringBuffer sbuffer = new StringBuffer();
		try {
			in = new BufferedInputStream(new FileInputStream(src));
			int i = 0;
			while ((i = in.read()) > 0) {
				sbuffer.append((char) i);
			}
			result = new String(sbuffer.toString().getBytes("iso-8859-1"),"UTF-8");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					log.error(e.getMessage(),e);
				}
			}
		}
		result = sbuffer.toString();
		return result;
	}
	
	public String getAddressee() {
		return addressee;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getXmlNo() {
		return xmlNo;
	}

	public void setXmlNo(String xmlNo) {
		this.xmlNo = xmlNo;
	}

	public File getEmailFile() {
		return emailFile;
	}

	public void setEmailFile(File emailFile) {
		this.emailFile = emailFile;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getFaxID() {
		return faxID;
	}

	public void setFaxID(String faxID) {
		this.faxID = faxID;
	}

	public String getFaxSender() {
		return faxSender;
	}

	public void setFaxSender(String faxSender) {
		this.faxSender = faxSender;
	}

	public File getFaxFile() {
		return faxFile;
	}

	public void setFaxFile(File faxFile) {
		this.faxFile = faxFile;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getMsgContext() {
		return msgContext;
	}

	public void setMsgContext(String msgContext) {
		this.msgContext = msgContext;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public MsgAssist getMsgAssist() {
		return msgAssist;
	}

	public void setMsgAssist(MsgAssist msgAssist) {
		this.msgAssist = msgAssist;
	}

	public CommunicaterService getCommunicaterService() {
		return communicaterService;
	}

	public void setCommunicaterService(CommunicaterService communicaterService) {
		this.communicaterService = communicaterService;
	}

	public String getEmailFileFileName() {
		return emailFileFileName;
	}

	public void setEmailFileFileName(String emailFileFileName) {
		this.emailFileFileName = emailFileFileName;
	}

	public String getEmailFileContentType() {
		return emailFileContentType;
	}

	public void setEmailFileContentType(String emailFileContentType) {
		this.emailFileContentType = emailFileContentType;
	}

	public String getFaxFileFileName() {
		return faxFileFileName;
	}

	public void setFaxFileFileName(String faxFileFileName) {
		this.faxFileFileName = faxFileFileName;
	}

	public String getFaxFileContentType() {
		return faxFileContentType;
	}

	public void setFaxFileContentType(String faxFileContentType) {
		this.faxFileContentType = faxFileContentType;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public String getEmailTextArea() {
		return emailTextArea;
	}

	public void setEmailTextArea(String emailTextArea) {
		this.emailTextArea = emailTextArea;
	}

	public String getFaxTextArea() {
		return faxTextArea;
	}

	public void setFaxTextArea(String faxTextArea) {
		this.faxTextArea = faxTextArea;
	}
}
