package com.mangocity.hotel.base.service.assistant;

import java.util.Date;

import com.mangocity.util.hotel.constant.AssignMode;
import com.mangocity.util.hotel.constant.AutoSendSMSConstant;
import com.mangocity.util.hotel.constant.IsAutomatismSendFax;

public class OperationParam {
	 private static final long serialVersionUID = 2842797496022530935L;

	    /**
		 * ID <pk>
		 */
	    private Long ID;
	    
	    /**
		 * 参数名
		 */
	    private String name;

	    /**
		 * 参数值
		 */
	    private String value;
	    
	    /**
		 * 修改时间
		 */
	    private Date modifyTime;
	    
	    public boolean isAuto() {
	        return name.equals("ASSIGN_MODE") && value.equals(AssignMode.AUTO);
	    }
	    
	    public boolean isWorking() {
	        return name.equals("ASSIGN_MODE") && value.equals(AssignMode.WORKING);
	    }

	    public boolean isSendFax() {
	        return name.equals("IS_SEND_FAX") && value.equals(IsAutomatismSendFax.SENDSTOP);
	    }    
	    
	    /**
		 * 短信是否可发送
		 * add by shizhongwen
		 * 时间:Mar 6, 2009  6:25:19 PM
		 * @return
		 */
	    public boolean isSendSMS(){    
	        return name.equals(AutoSendSMSConstant.NAME) && value.equals(
	                AutoSendSMSConstant.SENDWORKING);
	    }
	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getValue() {
	        return value;
	    }

	    public void setValue(String value) {
	        this.value = value;
	    }

	    public Long getID() {
	        return ID;
	    }

	    public void setID(Long id) {
	        ID = id;
	    }

	    public Date getModifyTime() {
	        return modifyTime;
	    }

	    public void setModifyTime(Date modifyTime) {
	        this.modifyTime = modifyTime;
	    }    


}
