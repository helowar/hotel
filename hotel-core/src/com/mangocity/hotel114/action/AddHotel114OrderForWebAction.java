package com.mangocity.hotel114.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.assistant.OrderUtil;
import com.mangocity.hotel.order.web.PopulateOrderAction;
import com.mangocity.hotel114.manage.Hotel114ManageWeb;
import com.mangocity.hotel114.util.CategoryInfo;
import com.mangocity.hotelweb.persistence.AlertMessageBean;
import com.mangocity.hotelweb.persistence.HotelFormIpsBean;
import com.mangocity.hotelweb.persistence.HotelOrderFromBean;
import com.mangocity.hotelweb.persistence.QueryHotelFactorageResult;
import com.mangocity.hotelweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.util.ConfigParaBean;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * 网站面付订单处理
 * 
 * @author neil date: 2007-11-17
 */
public class AddHotel114OrderForWebAction extends PopulateOrderAction {

    private static final long serialVersionUID = -5282964405195722181L;

    /**
     * 入住晚数
     */
    private int nightNum;

    private Hotel114ManageWeb hotel114ManageWeb;

    private int weekTotal;

    private int num;

    private String url;

    private HotelOrderFromBean hotelOrderFromBean;

    private String forward;

    private String EmialHtml;

    private AlertMessageBean alertMessage = new AlertMessageBean();

    private HotelFormIpsBean hotelFormIpsBean = new HotelFormIpsBean();

    private ConfigParaBean configParaBean;

    private List<QueryHotelForWebSaleItems> priceLis = new ArrayList<QueryHotelForWebSaleItems>();

    private long memberId;

    private String memberName;

    private String memberCD;

    // 为渠道增加begin
    private String telephone; // 联系电话

    private String email;// 电邮

    private String fax; // 传真

    private String logo; // 渠道logo

    private String title; // 页头title

    private String bgColor; // 背景颜色

    private String color; // 行颜色

    // 为渠道增加end

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public int getNightNum() {
        return nightNum;
    }

    public void setNightNum(int nightNum) {
        this.nightNum = nightNum;
    }

    public Hotel114ManageWeb getHotel114ManageWeb() {
        return hotel114ManageWeb;
    }

    public void setHotel114ManageWeb(Hotel114ManageWeb hotel114ManageWeb) {
        this.hotel114ManageWeb = hotel114ManageWeb;
    }

    public int getWeekTotal() {
        return weekTotal;
    }

    public void setWeekTotal(int weekTotal) {
        this.weekTotal = weekTotal;
    }

    public List<QueryHotelForWebSaleItems> getPriceLis() {
        return priceLis;
    }

    public void setPriceLis(List<QueryHotelForWebSaleItems> priceLis) {
        this.priceLis = priceLis;
    }

    /**
     * 定位到新增订单页面
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public String addOrder() {
        Map params = super.getParams();
        order = new OrOrder();
        MyBeanUtil.copyProperties(order, params);
        if (null == hotelOrderFromBean) {
            hotelOrderFromBean = new HotelOrderFromBean();
        }

        MemberDTO member1 = (MemberDTO) getFromSession("member");
        if (null != member1) {
            memberId = member1.getId();
            memberName = member1.getName();
            memberCD = member1.getMembercd();
        }
        QueryHotelFactorageResult factorage = (QueryHotelFactorageResult) 
        getFromSession("facrotage");
        if (null == factorage) {
            factorage = hotel114ManageWeb.queryHotelFactorageForWeb(memberId);
        }
        telephone = factorage.getTelephone();
        email = factorage.getEmail();
        fax = factorage.getFax();
        logo = factorage.getLogo();
        title = factorage.getTitle();
        bgColor = factorage.getBgColor();
        color = factorage.getColor();

        MyBeanUtil.copyProperties(hotelOrderFromBean, params);
        // 填充入住人
        List fellowList = MyBeanUtil.getBatchObjectFromParam(params, OrFellowInfo.class, num);
        order.setFellowList(fellowList);
        order.setSum(hotelOrderFromBean.getPriceNum()
            * Integer.parseInt(hotelOrderFromBean.getRoomQuantity()));
        hotelOrderFromBean.setPriceNum(hotelOrderFromBean.getPriceNum()
            * Integer.parseInt(hotelOrderFromBean.getRoomQuantity()));
        hotelOrderFromBean.setFellowList(fellowList);
        putSession("fellowList", hotelOrderFromBean);
        // 查询预定的价格列表
        // priceLis = hotel114ManageWeb.queryPriceForWeb(hotelOrderFromBean.getChildRoomTypeId(),
        // hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(),
        // hotelOrderFromBean.getMinPrice(),
        // hotelOrderFromBean.getMaxPrice(),hotelOrderFromBean.getPayMethod(),
        // hotelOrderFromBean.getQuotaType());
        priceLis = copyDatePriceStr(hotelOrderFromBean.getDatePriceStr());
        nightNum = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean
            .getCheckoutDate());
        // 计算周需要显示的行数
        weekTotal = priceLis.size() / 7;
        return "add";
    }

    /**
     * 保存订单
     * 
     * @return
     */
    public String saveOrder() {
        Map params = super.getParams();
        order = new OrOrder();
        if (null == hotelOrderFromBean) {
            hotelOrderFromBean = new HotelOrderFromBean();
        }
        hotelOrderFromBean = (HotelOrderFromBean) getFromSession("fellowList");

        order = copyProperties(order, hotelOrderFromBean, params);
        log.info("AddHotel114OrderForWebAction.action saveOrder:request isUserUlmPoint="
            + hotelOrderFromBean.isUseUlmPoint());
        // 订单总价=单价 * 房间数
        order.setSum(hotelOrderFromBean.getPriceNum()); // 原币种价格

        order.setPaymentCurrency(hotelOrderFromBean.getCurrency());
        order.setFellowList(hotelOrderFromBean.getFellowList());

        String exchange = CurrencyBean.rateMap.get(hotelOrderFromBean.getCurrency());

        MemberDTO member1 = (MemberDTO) getFromSession("member");
        if (null != member1) {
            memberId = member1.getId();
            memberName = member1.getName();
            memberCD = member1.getMembercd();
        }
        QueryHotelFactorageResult factorage = (QueryHotelFactorageResult)
        getFromSession("facrotage");
        if (null == factorage) {
            factorage = hotel114ManageWeb.queryHotelFactorageForWeb(memberId);
        }
        telephone = factorage.getTelephone();
        email = factorage.getEmail();
        fax = factorage.getFax();
        logo = factorage.getLogo();
        title = factorage.getTitle();
        bgColor = factorage.getBgColor();
        color = factorage.getColor();

        double dbExchange = 0;
        try {
            dbExchange = StringUtil.getStrTodouble(exchange);
        } catch (Exception ex) {
            dbExchange = 1;
            log.error("AddHotel114OrderForWebAction forward get rate error,the cause is " + ex);
        }

        order.setRateId(dbExchange);// 汇率

        if (0 == Double.valueOf(dbExchange).intValue()) {
            order.setRateId(1);// 汇率
            dbExchange = 1;
            log
                .error("CurrencyBean.rateMap.get(" +
                        "hotelOrderFromBean.getCurrency()) result = 0, " +
                        "set default rate is 1");
        }
        order.setSumRmb(hotelOrderFromBean.getPriceNum()
            * Integer.parseInt(hotelOrderFromBean.getRoomQuantity()) * dbExchange);
        order.setToMidTime(DateUtil.getSystemDate());
        order.setHotelStar(hotelOrderFromBean.getHotelStar());
        // 客户确认方式
        if ("电话确认".equals(hotelOrderFromBean.getConfirmtype())) {
            order.setConfirmType(ConfirmType.PHONE);
        } else if ("电子邮件确认".equals(hotelOrderFromBean.getConfirmtype())) {
            order.setConfirmType(ConfirmType.EMAIL);
        } else if ("传真确认".equals(hotelOrderFromBean.getConfirmtype())) {
            order.setConfirmType(ConfirmType.FAX);
        } else if ("短信确认".equals(hotelOrderFromBean.getConfirmtype())) {
            order.setConfirmType(ConfirmType.SMS);
        }

        order.setSource(OrderSource.FROM_WEB);
        // 设置订单紧急程度
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("vipLevel", "0");
        if (null == member) {
            member = new MemberDTO();
        }
        MyBeanUtil.copyProperties(member, member1);
        order.setEmergencyLevel(orderAssist.getEmergency(order, new Date(), member, map));

        List<OrFellowInfo> fellowList = order.getFellowList();
        String fellowNames = "";
        for (OrFellowInfo fellow : fellowList) {
            fellow.setOrder(order);
            fellowNames += " " + fellow.getFellowName();// 加起来
        }

        order.setFellowNames(fellowNames);

        order.setMemberState(member.getState());

        // String strHotelStar = resourceManager.getDescription(
        // "res_hotelStarToNum", info.getHotelStar());
        // order.setHotelStar(hotelStar)

        //if (log.isInfoEnabled())
            log
                .info("AddHotel114OrderForWebAction.action saveOrder:request " +
                        "is pay and save the order begin...");
        hotelOrderFromBean.setPayMethod("pay");
        order.setOrderState(OrderState.SUBMIT_TO_MID);
        order.setType(com.mangocity.hotel.order.constant.OrderType.TYPE_114);
        order.setQuotaTypeOld(hotelOrderFromBean.getQuotaType());
        order.setMemberId(memberId);
        order.setMemberName(memberName);
        order.setMemberCd(memberCD);

        order.setCity(hotelOrderFromBean.getCityId());
        OrderUtil.updateStayInMid(order);
        orderAssist.setOrderHraType(order);
        order.setIsStayInMid(true);

        Serializable orderId = orderService.saveOrUpdate(order);
        String orderCD = orderService.getOrderCDByID(Long.valueOf(orderId.toString()));

        //if (log.isInfoEnabled())
            log
                .info("AddHotel114OrderForWebAction.action saveOrder:save " +
                        "order success, the orderCD is "
                    + orderCD);
        EmialHtml = CategoryInfo.TELCOM_114_PAYMETHOD.EMAIL_HTML_APPEND
            + CategoryInfo.TELCOM_114_PAYMETHOD.EMAIL_HTML_VAR + orderCD
            + CategoryInfo.TELCOM_114_PAYMETHOD.EMAIL_HTEML_END + EmialHtml;
        String message = CategoryInfo.TELCOM_114_PAYMETHOD.MESSAGE_PREVIOUS
            + hotelOrderFromBean.getConfirmtype() + CategoryInfo.TELCOM_114_PAYMETHOD.MESSAGE_LAST;
        alertMessage.setTitle(CategoryInfo.TELCOM_114_PAYMETHOD.ORDER_TITLE);
        message += CategoryInfo.TELCOM_114_PAYMETHOD.ORDER_AMOUNT + order.getSum();
        message += CategoryInfo.TELCOM_114_PAYMETHOD.INQUIRE + title
            + CategoryInfo.TELCOM_114_PAYMETHOD.INQUIREEND;
        alertMessage.setMessage(message);
        // alertMessage.setReturnURL("/CTII114/queryHotelWeb114!allForward.action?forward=query");
       // if (log.isInfoEnabled())
            log.info("AddHotel114OrderForWebAction.action saveOrder, invoke pay method complete.");

        return "save";
    }

    /**
     * 点击查询酒店结果页面的预订按钮
     * 
     * @return
     */
    public String forward() {
        Map params = super.getParams();
        if (null == hotelOrderFromBean) {
            hotelOrderFromBean = new HotelOrderFromBean();
        }

        String hotelName = (String) params.get("hotelName1");

        MyBeanUtil.copyProperties(hotelOrderFromBean, params);
        if (null == hotelName) {
            hotelName = (String) getFromSession("hotelName");
        }
        hotelOrderFromBean.setHotelName(hotelName);

        MemberDTO member1 = (MemberDTO) getFromSession("member");
        if (null != member1) {
            memberId = member1.getId();
            memberName = member1.getName();
            memberCD = member1.getMembercd();
        }
        QueryHotelFactorageResult factorage = (QueryHotelFactorageResult) 
        getFromSession("facrotage");
        if (null == factorage) {
            factorage = hotel114ManageWeb.queryHotelFactorageForWeb(memberId);
        }
        telephone = factorage.getTelephone();
        email = factorage.getEmail();
        fax = factorage.getFax();
        logo = factorage.getLogo();
        title = factorage.getTitle();
        bgColor = factorage.getBgColor();
        color = factorage.getColor();

        int days = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean
            .getCheckoutDate());
        hotelOrderFromBean.setDays(days);

        hotelOrderFromBean.setCityName((String) params.get("cityName"));
        Object hotelStar = params.get("hotelStar");
        if (null != hotelStar) {
            hotelOrderFromBean.setHotelStar(Float.parseFloat(hotelStar.toString()));
        }
        hotelOrderFromBean.setQuotaType((String) params.get("quotaTypeOld"));

        // 查询预定的价格列表
        // priceLis = hotel114ManageWeb.queryPriceForWeb(hotelOrderFromBean.getChildRoomTypeId(),
        // hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(),
        // hotelOrderFromBean.getMinPrice(),
        // hotelOrderFromBean.getMaxPrice(),hotelOrderFromBean.getPayMethod(),
        // hotelOrderFromBean.getQuotaType());
        priceLis = copyDatePriceStr(hotelOrderFromBean.getDatePriceStr());
        nightNum = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean
            .getCheckoutDate());
        // 计算周需要显示的行数
        weekTotal = priceLis.size() / 7;

        return "query";
    }

    /**
     * 会员登录后重定向到订单显示页面
     * 
     * @return
     */
    public String reforward() {
        Map params = super.getParams();
        hotelOrderFromBean = new HotelOrderFromBean();
        MyBeanUtil.copyProperties(hotelOrderFromBean, params);
        order = new OrOrder();
        String hotelName = getFromSession("hotelName").toString();
        String childRoomTypeName = getFromSession("childRoomTypeName").toString();
        String roomTypeName = getFromSession("roomTypeName").toString();

        MemberDTO member1 = (MemberDTO) getFromSession("member");
        if (null != member1) {
            memberId = member1.getId();
            memberName = member1.getName();
            memberCD = member1.getMembercd();
        }
        QueryHotelFactorageResult factorage = (QueryHotelFactorageResult) 
        getFromSession("facrotage");
        if (null == factorage) {
            factorage = hotel114ManageWeb.queryHotelFactorageForWeb(memberId);
        }
        telephone = factorage.getTelephone();
        email = factorage.getEmail();
        fax = factorage.getFax();
        logo = factorage.getLogo();
        title = factorage.getTitle();
        bgColor = factorage.getBgColor();
        color = factorage.getColor();

        hotelOrderFromBean.setHotelName(hotelName);
        hotelOrderFromBean.setChildRoomTypeName(childRoomTypeName);
        hotelOrderFromBean.setRoomTypeName(roomTypeName);
        MyBeanUtil.copyProperties(order, hotelOrderFromBean);
        order.setSum(hotelOrderFromBean.getPriceNum());
        int days = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean
            .getCheckoutDate());
        hotelOrderFromBean.setDays(days);
        // 查询预定的价格列表
        // priceLis = hotel114ManageWeb.queryPriceForWeb(hotelOrderFromBean.getChildRoomTypeId(),
        // hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean.getCheckoutDate(),
        // hotelOrderFromBean.getMinPrice(),
        // hotelOrderFromBean.getMaxPrice(),hotelOrderFromBean.getPayMethod(),
        // hotelOrderFromBean.getQuotaType());

        priceLis = copyDatePriceStr(hotelOrderFromBean.getDatePriceStr());

        nightNum = DateUtil.getDay(hotelOrderFromBean.getCheckinDate(), hotelOrderFromBean
            .getCheckoutDate());
        // 计算周需要显示的行数
        weekTotal = priceLis.size() / 7;

        return "query";
    }

    public String mangonotice() {
        QueryHotelFactorageResult factorage = (QueryHotelFactorageResult) 
        getFromSession("facrotage");
        if (null == factorage) {
            factorage = hotel114ManageWeb.queryHotelFactorageForWeb(memberId);
        }
        telephone = factorage.getTelephone();
        email = factorage.getEmail();
        fax = factorage.getFax();
        logo = factorage.getLogo();
        title = factorage.getTitle();
        bgColor = factorage.getBgColor();
        color = factorage.getColor();
        return "mangonotice";
    }

    /**
     * 拷贝输入参数
     * 
     * @param order
     * @param hotelOrderFromBean
     * @param params
     * @return
     */
    private OrOrder copyProperties(OrOrder order, HotelOrderFromBean 
        hotelOrderFromBean, Map params) {
        // hotelOrderFromBean = (HotelOrderFromBean)getFromSession("fellowList");
        MyBeanUtil.copyProperties(order, params);
        MyBeanUtil.copyProperties(order, hotelOrderFromBean);
        order.setNum((String) params.get("num"));
        order.setLinkMan((String) params.get("linkMan"));
        order.setMobile((String) params.get("mobile"));
        order.setTelephone((String) params.get("telephone"));
        order.setEmail((String) params.get("email"));
        order.setCustomerFax((String) params.get("customerFax"));
        return order;
    }

    /**
     * 转换存放面付的每天日期和价格集合String 为QueryHotelForWebSaleItems 类的List
     * 
     * @return
     */
    private List<QueryHotelForWebSaleItems> copyDatePriceStr(String datePriceStr) {

        List<QueryHotelForWebSaleItems> datePriceLis = new ArrayList<QueryHotelForWebSaleItems>();

        if (null != datePriceStr && 0 < datePriceStr.length()) {

            String[] tempStr = datePriceStr.split("/");
            int mm = 0;
            int yy = 0;
            for (int j = 0; j < tempStr.length; j++) {
                QueryHotelForWebSaleItems qh = new QueryHotelForWebSaleItems();

                String[] stStr = tempStr[j].split(":");
                for (int i = 0; i < stStr.length; i++) {
                    switch (i) {
                    case 0:

                        if (0 == j) {
                            for (int k = 1; k < Integer.valueOf(stStr[i]).intValue(); k++) {
                                QueryHotelForWebSaleItems qh2 = new QueryHotelForWebSaleItems();
                                datePriceLis.add(qh2);
                            }
                            mm = Integer.valueOf(stStr[i]).intValue();
                        }
                        if (0 < j && j == tempStr.length - 1) {
                            /*
                             * for(int k=Integer.valueOf(stStr[i]).intValue();k<7;k++){
                             * QueryHotelForWebSaleItems qh2 = new QueryHotelForWebSaleItems();
                             * datePriceLis.add(qh2); }
                             */
                            yy = Integer.valueOf(stStr[i]).intValue();
                        }
                        break;
                    case 1:
                        qh.setFellowDate(DateUtil.getDate(stStr[i]));
                        break;
                    case 2:
                        qh.setSalePrice(Double.valueOf(stStr[i]));
                        break;
                    }
                }
                datePriceLis.add(qh);

                if (1 == tempStr.length) {
                    for (int k = mm; 7 > k; k++) {
                        QueryHotelForWebSaleItems qh2 = new QueryHotelForWebSaleItems();
                        datePriceLis.add(qh2);
                    }
                }
                if (tempStr.length == j + 1) {
                    for (int k = yy; 7 > k; k++) {
                        QueryHotelForWebSaleItems qh2 = new QueryHotelForWebSaleItems();
                        datePriceLis.add(qh2);
                    }
                }
            }
        }

        return datePriceLis;
    }

    public String retIPS() {
        // 判断是否成功
        return null;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public HotelOrderFromBean getHotelOrderFromBean() {
        return hotelOrderFromBean;
    }

    public void setHotelOrderFromBean(HotelOrderFromBean hotelOrderFromBean) {
        this.hotelOrderFromBean = hotelOrderFromBean;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmialHtml() {
        return EmialHtml;
    }

    public void setEmialHtml(String emialHtml) {
        EmialHtml = emialHtml;
    }

    public AlertMessageBean getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(AlertMessageBean alertMessage) {
        this.alertMessage = alertMessage;
    }

    public HotelFormIpsBean getHotelFormIpsBean() {
        return hotelFormIpsBean;
    }

    public void setHotelFormIpsBean(HotelFormIpsBean hotelFormIpsBean) {
        this.hotelFormIpsBean = hotelFormIpsBean;
    }

    public ConfigParaBean getConfigParaBean() {
        return configParaBean;
    }

    public void setConfigParaBean(ConfigParaBean configParaBean) {
        this.configParaBean = configParaBean;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberCD() {
        return memberCD;
    }

    public void setMemberCD(String memberCD) {
        this.memberCD = memberCD;
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
