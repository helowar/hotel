package com.mangocity.hotel114.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.hotelweb.persistence.HotelOrderGroupBean;
import com.mangocity.hotelweb.persistence.QueryHotelFactorageResult;
import com.mangocity.proxy.member.service.MemberInterfaceService;
import com.mangocity.util.ConfigParaBean;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Mail;

/**
 */
public class Hotel114OrderGroupWebAction extends PersistenceAction {

    private static final long serialVersionUID = -5385214263589942217L;

    private HotelOrderGroupBean hotelOrderGroupBean;

    /**
     * message接口
     */
    private CommunicaterService communicaterService;

    private MemberInterfaceService memberInterfaceService;

    private ConfigParaBean configParaBean;

    /**
     * 传真邮件辅助类
     */
    private MsgAssist msgAssist;

    private long memberId;

    private String memberName;

    // 为渠道增加begin
    private String telephone; // 联系电话

    private String email;// 电邮

    private String fax; // 传真

    private String logo; // 渠道logo

    private String title; // 页头title

    private String bgColor; // 背景颜色

    private String color; // 行颜色

    // 为渠道增加end

    public HotelOrderGroupBean getHotelOrderGroupBean() {
        return hotelOrderGroupBean;
    }

    public void setHotelOrderGroupBean(HotelOrderGroupBean hotelOrderGroupBean) {
        this.hotelOrderGroupBean = hotelOrderGroupBean;
    }

    public MsgAssist getMsgAssist() {
        return msgAssist;
    }

    public void setMsgAssist(MsgAssist msgAssist) {
        this.msgAssist = msgAssist;
    }

    public String forward() {
    	MemberDTO member1 = (MemberDTO) getFromSession("member");
        if (null != member1) {
            memberId = member1.getId();
            memberName = member1.getName();
        }
        QueryHotelFactorageResult facrotage = (QueryHotelFactorageResult) 
        getFromSession("facrotage");
        telephone = facrotage.getTelephone();
        email = facrotage.getEmail();
        fax = facrotage.getFax();
        logo = facrotage.getLogo();
        title = facrotage.getTitle();
        bgColor = facrotage.getBgColor();
        color = facrotage.getColor();
        //	
        // memberName = "深圳号码百事通";

        return VIEW;
    }

    public String sendEmail() {
        QueryHotelFactorageResult facrotage = (QueryHotelFactorageResult)
        getFromSession("facrotage");
        email = facrotage.getEmail();
        title = facrotage.getTitle();

        List lst = new ArrayList();
        Map params = super.getParams();
        lst = MyBeanUtil.getBatchObjectFromParam(params, HotelOrderGroupBean.class, 1);
        for (int i = 0; i < lst.size(); i++) {
            setHotelOrderGroupBean((HotelOrderGroupBean) lst.get(i));
        }
        Mail mail = new Mail();
        mail.setApplicationName("hotel");
        mail.setTo(new String[] { getConfigParaBean().getWebRecEmail() });
        String subject = title + "网站集体定房";
        String templateNo = FaxEmailModel.EMAIL_FOR_HOTELWEB_GROUP_ORDER;
        String xmlString = "";

        try {
        	MemberDTO member1 = (MemberDTO) getFromSession("member");
            if (null != member1) {
                memberId = member1.getId();
                memberName = member1.getName();
            }
            getHotelOrderGroupBean().setMemberName(memberName);
            getHotelOrderGroupBean().setMemberStr(member1.getBindtelephone());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
            Date in = sdf.parse(getHotelOrderGroupBean().getInDate());
            Date out = sdf.parse(getHotelOrderGroupBean().getOutDate());
            int dateNum = DateUtil.getDay(in, out);

            getHotelOrderGroupBean().setDateNum(dateNum);
            xmlString = msgAssist.genGroupOrderMailXmlForHotelWeb(getHotelOrderGroupBean());
            mail.setSubject(subject);
            mail.setTemplateFileName(templateNo);
            mail.setXml(xmlString);
            mail.setFrom(email);
            communicaterService.sendEmail(mail);
        } catch (Exception ex) {
            log.error("Hotel114OrderGroupWebAction sendEmail exception, the cause is:" + ex);
        }
        return SAVE_SUCCESS;
    }

    public ConfigParaBean getConfigParaBean() {
        return configParaBean;
    }

    public void setConfigParaBean(ConfigParaBean configParaBean) {
        this.configParaBean = configParaBean;
    }

    public CommunicaterService getCommunicaterService() {
        return communicaterService;
    }

    public void setCommunicaterService(CommunicaterService communicaterService) {
        this.communicaterService = communicaterService;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public MemberInterfaceService getMemberInterfaceService() {
        return memberInterfaceService;
    }

    public void setMemberInterfaceService(MemberInterfaceService memberService) {
        this.memberInterfaceService = memberService;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
