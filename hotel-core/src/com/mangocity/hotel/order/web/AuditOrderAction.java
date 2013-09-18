package com.mangocity.hotel.order.web;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mangocity.hdl.hotel.dto.DailyAuditExRequest;
import com.mangocity.hdl.hotel.dto.DailyAuditExResponse;
import com.mangocity.hdl.hotel.dto.MGExDailyAudit;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.hotel.base.constant.WorkType;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.base.service.IHotelService;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.base.service.assistant.URLClient;
import com.mangocity.hotel.base.web.TranslateUtil;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.OrdailyauditStatus;
import com.mangocity.hotel.order.constant.OrderAuditState;
import com.mangocity.hotel.order.constant.OrderItemAuditState;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.manager.HraManager;
import com.mangocity.hotel.order.persistence.Audit;
import com.mangocity.hotel.order.persistence.AuditHotel;
import com.mangocity.hotel.order.persistence.AuditItem;
import com.mangocity.hotel.order.persistence.OrDailyAudit;
import com.mangocity.hotel.order.persistence.OrPaperContact;
import com.mangocity.hotel.order.persistence.OrPaperDailyAudit;
import com.mangocity.hotel.order.persistence.OrPaperDailyAuditItem;
import com.mangocity.hotel.order.persistence.VOrOrder;
import com.mangocity.hotel.order.persistence.VOrOrderItem;
import com.mangocity.hotel.order.service.IAuditService;
import com.mangocity.hotel.order.service.assistant.AuditDetailInfo;
import com.mangocity.hotel.order.service.assistant.AuditDetailRowInfo;
import com.mangocity.hotel.order.service.assistant.MsgAssist;
import com.mangocity.hotel.order.service.assistant.OrderFax;
import com.mangocity.hotel.order.service.assistant.OrderItemFax;
import com.mangocity.proxy.member.service.MemberInterfaceService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.hotel.constant.FaxEmailModel;
import com.mangocity.util.hotel.constant.IsAutomatismSendFax;
import com.mangoctiy.communicateservice.CommunicaterService;
import com.mangoctiy.communicateservice.domain.Fax;

/**
 * 日审订单相关操作
 * 
 * @author chenkeming
 * 
 */
public class AuditOrderAction extends PersistenceAction {

    /**
     * message接口
     */
    private CommunicaterService communicaterService;

    /**
     * 传真邮件辅助类
     */
    private MsgAssist msgAssist;

    /**
     * 日审接口
     */
    private IAuditService auditService;

    private HraManager hraManager;
    
    private SystemDataService systemDataService;

	private IHotelService hotelService;

    /**
     * 调转常量
     */
    private static final String AUDIT_ALLOT_LIST = "audit_allot_hotel_list";

    private static final String AUDIT_SEND_HOTEL_LIST = "audit_send_hotel_list";

    private static final String AUDIT_EDIT_HOTEL_LIST = "audit_edit_hotel_list";

    private static final String AUDIT_ADD_HOTEL_LIST = "audit_add_hotel_list";

    private static final String AUDIT_HOTEL_LIST = "audit_hotel_list";

    private static final String AUDIT_FAX_LIST = "audit_fax_list";

    private static final String AUDIT_MY_LIST = "audit_my_list";

    private static final String AUDIT_BATCH_ASSIGN = "audit_batch_assign";

    private static final String SHOW_AUDIT_BY_HOTEL = "show_audit_by_hotel";

    private static final String AUDIT_BY_HOTEL = "audit_by_hotel";

    private static final String AUDIT_DETAIL = "audit_detail";

    private static final String WORK_SEND_AUDIT = "work_send_audit";

    private static final String AUDIT_SETUP = "audit_setup";

    private static final String AUDIT_INFO_LIST = "audit_info_list";

    private static final String AUDIT_DIRECTION = "audit_direction";

    /**
     * 酒店id参数
     */
    private long auditHotelId;

    /**
     * 日审单(Audit)id参数
     */
    private long auditId;

    private long sendAuditId;

    private int orderAuditType;

    /**
     * 批量获取
     */
    private Long[] selID;

    private long SUCCESSCOUNT;

    protected List userList;

    private String selIDs;

    private String orderID;

    private String hName;

    private String fax;

    private String faxConfirm;

    private long LOSTCOUNT;

    private List auditHotelList;

    private List auditUsers;

    private List auditList;

    private List auditRows;

    private List orderItem;

    private List orderItem_romm;

    private List hotels;

    private int totalDays;

    private List dateStrs;

    private AuditHotel auditHotel;

    private Audit audit;

    private String auditItemStr;

    private String auditItemNotStr;

    private Long hotelId;

    private Date checkNight;

    private String night;

    private String state;

    private String rState;

    private String rStateNot;

    private String rID;

    private List sendFaxNum;

    private List auditLogList;

    private ArrayList newHotel;

    private ArrayList editHotel;

    private String HOP_TMC_ORDER;

    private String page;

    private String URL;

    private String FAXID;

    private String FAXFILE;

    private String hotelName;

    private String telephone;
    /**
     * 标记财务是否获取,true 是,false 没有
     */
    private boolean bGet = false;

    /**
     * 酒店直联
     * 
     * @author guojun 2008 12-12-12 17:30
     */
    private String channel;

    private IHDLService hdlService;

    private List<MGExDailyAudit> daList;

    // 如果出现错误,将错误信息返回给页面,
    private String errorInfo;

    /** add by chenjiajie 2008-10-16 begin **/

    /**
     * 会员接口
     */
    protected MemberInterfaceService memberInterfaceService;

    /**
     * 会员转换辅助
     */
    protected TranslateUtil translateUtil;

    /**
     * v2.4.2上线日期
     */
    public static final String RELEASED_DATE = "2009-01-06";

    /** add by chenjiajie 2008-10-16 end **/

    protected Class getEntityClass() {
        return OrPaperContact.class;
    }

    /**
     * 日审详细页面,具体日审某订单某一天的的入住情况 -- 显示
     * 
     * @return
     */
    public String auditDetail() {
        audit = auditService.getAudit(auditId);
        List auditItems = auditService.getAuditItemByAudit(audit);
        totalDays = DateUtil.getDay(audit.getOrder().getCheckinDate(), audit.getCheckinDate()) + 1;

        auditItemStr = "";
        int weekDays = 4;
        auditRows = new ArrayList();
        AuditDetailInfo detailInfo;
        AuditDetailRowInfo rowInfo = null;
        int rooms = audit.getOrder().getRoomQuantity();
        for (int i = 0; i < rooms; i++) {
            boolean bHasRoomNo = false;
            detailInfo = new AuditDetailInfo();
            for (int j = 0; j < totalDays; j++) {
                if (0 == j % weekDays) {
                    rowInfo = new AuditDetailRowInfo();
                }
                AuditItem curItem = (AuditItem) auditItems.get(i * totalDays + j);
                rowInfo.getItems().add(curItem);
                if ((j % weekDays == (weekDays - 1)) || j >= totalDays - 1) {
                    detailInfo.getRows().add(rowInfo);
                    if (j >= totalDays - 1) {
                        if (0 < i) {
                            auditItemStr += "," + curItem.getID();
                        } else {
                            auditItemStr += "" + curItem.getID();
                        }
                    }
                }
                if (!bHasRoomNo && null != curItem.getRoomNo() 
                    && 0 < curItem.getRoomNo().length()) {
                    detailInfo.setRoomNo(curItem.getRoomNo());
                    bHasRoomNo = true;
                }
            }
            auditRows.add(detailInfo);
        }

        return AUDIT_DETAIL;
    }

    /**
     * 日审详细页面,具体日审某订单某一天的的入住情况 -- 处理
     * 
     * @return
     */
    public String auditDetailProc() {
        if (null == auditItemStr || 0 >= auditItemStr.length()) {
            return forwardError("没有修改的房间!");
        }
        String[] ids = auditItemStr.split(",");
        if (0 >= ids.length) {
            return forwardError("没有修改的房间!");
        }

        Map params = super.getParams();
        selID = new Long[ids.length];
        String[] states = new String[ids.length];
        String[] roomNos = new String[ids.length];
        String[] fellowNames = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            selID[i] = Long.parseLong(ids[i]);
            states[i] = (String) params.get("auditItemState" + (i + 1));
            roomNos[i] = (String) params.get("roomNo" + (i + 1));
            fellowNames[i] = (String) params.get("fellowName" + (i + 1));
        }

        auditService.batchSetAuditItem(selID, roomNos, fellowNames, states);

        return forwardMsgBox("修改日审成功！", "refreshAudit()");
    }

    /**
     * 日审工作档案中, 根据选中酒店列出该酒店日审单
     * 
     * @return
     */
    public String showAuditByHotel() {
        auditList = auditService.getAuditByHotel(auditHotelId);
        auditHotel = auditService.getAuditHotel(auditHotelId);
        return SHOW_AUDIT_BY_HOTEL;
    }

    public String auditByHotel() {
        auditList = auditService.getAuditByHotel(auditHotelId);
        auditHotel = auditService.getAuditHotel(auditHotelId);
        return AUDIT_BY_HOTEL;
    }

    /**
     * 左边菜单链接: 日审工作档案
     * 
     * @return
     */
    public String myList() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        user = super.getOnlineWorkStates();
        return AUDIT_MY_LIST;
    }

    /**
     * 左边菜单链接: 分配日审
     * 
     * @return
     */
    public String allotAuditHotel() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        roleUser = getOnlineRoleUser();
        user = super.getOnlineWorkStates();
        userList = workStatesManager.lstWorkStatesByType(WorkType.AUDIT);
        return AUDIT_ALLOT_LIST;
    }

    /**
     * 左边菜单链接: 查看日审记录
     * 
     * @return
     */
    public String queryAuditInfo() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        roleUser = getOnlineRoleUser();
        user = super.getOnlineWorkStates();
        userList = workStatesManager.lstWorkStatesByType(WorkType.AUDIT);
        return AUDIT_INFO_LIST;
    }

    /**
     * 左边菜单链接: 增加纸质日审酒店
     * 
     * @return
     */
    public String addHotelList() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        user = super.getOnlineWorkStates();
        return AUDIT_ADD_HOTEL_LIST;
    }

    /**
     * 左边菜单链接: 管理纸质日审酒店
     * 
     * @return
     */
    public String editHotelList() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        user = super.getOnlineWorkStates();
        return AUDIT_EDIT_HOTEL_LIST;
    }

    public String sendHotelFax() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        user = super.getOnlineWorkStates();
        return AUDIT_SEND_HOTEL_LIST;
    }

    /**
     * 左边菜单链接: 电话日审记录
     * 
     * @return
     */
    public String auditTelList() {
        user = super.getOnlineWorkStates();
        return AUDIT_HOTEL_LIST;
    }

    /**
     * 左边菜单链接: 传真日审记录
     * 
     * @return
     */
    public String auditFaxList() {
        user = super.getOnlineWorkStates();
        return AUDIT_FAX_LIST;
    }

    /**
     * 左边菜单链接: 手工发送日审
     * 
     * @return
     */
    public String workSendAudit() {
        // 记录有中台权限的人员的登陆时间
        super.markOrgMidLoginTime();
        user = super.getOnlineWorkStates();
        return WORK_SEND_AUDIT;
    }

    /**
     * 左边菜单链接: 日审参数设置
     * 
     * @return
     */
    public String auditSetup() {
        user = super.getOnlineWorkStates();
        return AUDIT_SETUP;
    }

    /**
     * 分配日审酒店
     * 
     * @return
     */
    public String allot() {
        userList = workStatesManager.lstAuditWorkersByGroup(1, true);
        Map params = super.getParams();
        selIDs = (String) params.get("selIDs");
        return "assign_order";
    }

    /**
     * 自动分配日审酒店
     * 
     * @return
     */
    public String automatismAllot() {
        Date date = DateUtil.getDate(checkNight);
        userList = workStatesManager.lstAuditWorkersByGroup(1, true);
        // 输入参数true表明自动分配也只在工作状态为open的操作人间进行，不包括处于close状态的
        boolean bResult = auditService.automatismAllotManager(date, userList);
        if (bResult) {
            return forwardMsgBox("自动分配成功！", "refreshSelf()");
        } else {
            return forwardMsgBox("没有需要分配的酒店订单或没有操作人员！", "refreshSelf()");
        }
    }

    /**
     * 处理日审单 -- 查看
     * 
     * @return
     */
    public String referOrder() {
        log.info(orderAuditType);
        auditList = auditService.getViewOrders(sendAuditId, orderAuditType);
        VOrOrder order = (VOrOrder) auditList.get(0);
        orderID = order.getOrderCD();
        auditList = auditService.getViewOrderItemTypes(sendAuditId, orderAuditType);
        return "audit_refer_list";
    }

    /**
     * 补登日审 -- 显示
     */
    public String patchAudit() {
        VOrOrder auditList = auditService.getVOrder(Long.valueOf(orderID));
        request.setAttribute("auditList", auditList);
        orderItem = auditService.getVOrderItem(Long.parseLong(orderID), Integer.valueOf(1));
        if(bGet)
        	request.setAttribute("bGetMsg", "财务已获取数据,不能更改!");
        return "audit_patch_list";
    }

    /**
     * 补登日审 -- noshow 提前退房
     * 
     * @return
     */
    public String patchAuditWorkNoShow() {
        patchAuditWorkNoShowExt();
        return patchAudit();
    }

    private void patchAuditWorkNoShowExt() {
        Map params = super.getParams();
        user = super.getOnlineWorkStates();
        Date modifiedTime = new Date();
        orderID = (String) params.get("orderID");
        int orderState = Integer.parseInt(params.get("state").toString());
        VOrOrder order = auditService.getVOrder(Long.parseLong(orderID));
        order.setAuditState(orderState);
        if (13 == orderState) {
            VOrOrderItem orderItem = noshowDispose();
            auditService.sendAuditDetail(orderItem);
            try {
                String noshowCode = (String) params.get("noshowCode");
                if (null != noshowCode & !noshowCode.equals("")) {
                    order.setNoshowCode(Integer.parseInt(noshowCode));
                }
            } catch (Exception e) {
            	log.error(e.getMessage(),e);
            }

            order.setNoshowReason((String) params.get("noshowReason"));
        } else if (5 == orderState) {
            VOrOrderItem orderItem = fadeRoomDispose();
            auditService.sendAuditDetail(orderItem);
        }
        order.setSpecialNote((String) params.get("specialNote"));
        order.setModifierName(user.getName());
        // QC444日审操作后最后修改人中文名和工号对不上add by chenjiajie@2009-05-05
        order.setModifier(user.getLogonId());
        order.setModifiedTime(modifiedTime);
        auditService.sendAuditViewOrder(order);
    }

    /**
     * 处理日审单 -- 修改操作状态
     * 
     * @return
     */
    public VOrOrderItem patchEditWorkState(String orderID, int state) {
        user = super.getOnlineWorkStates();
        VOrOrderItem orOrderItem = null;
        Date noteTime = new Date();
        List workState = auditService.getVOrderItem(Long.parseLong(orderID), Integer.valueOf(1));
        for (int i = 0; i < workState.size(); i++) {
            orOrderItem = (VOrOrderItem) workState.get(i);
            orOrderItem.setAuditState(state);
            orOrderItem.setNotesMan(user.getName());
            orOrderItem.setNoteTime(noteTime);
        }
        log.info(workState.size());
        return orOrderItem;
    }

    /**
     * 补登日审 -- 完成
     * 
     * @return
     */
    public String patchAuditFulfill() {
        Map params = super.getParams();
        Date now = new Date();
        user = super.getOnlineWorkStates();
        orderID = (String) params.get("orderID");
        String specialNote = (String) params.get("specialNote");
        // hotelId = Long.parseLong(params.get("hotelId").toString());
        int state = OrderItemAuditState.ACHIEVE;

        // 取得该订单操作状态
        VOrOrderItem orOrderItem = patchEditWorkState(orderID, state);

        VOrOrder order = auditService.getVOrder(Long.parseLong(orderID));

        order.setModifierName(user.getName());
        // QC444日审操作后最后修改人中文名和工号对不上add by chenjiajie@2009-05-05
        order.setModifier(user.getLogonId());
        order.setModifiedTime(now);
        order.setSpecialNote(specialNote);
        int orderState = order.getAuditState();
        /*
         * 看用户是不是这样子，点完成的时候，如果不曾保存也要相当于点保存。 if (orderState==0){///如果没有点击保存直接点完成 orderState =
         * Integer.parseInt(params.get("state").toString()); if (orderState==13){// no show
         * patchAuditWorkNoShowExt(); }else { patchAuditWorkExt(); } }
         */
        if (6 == orderState) {
            order.setAuditState(OrderState.NORMAL_QUIT);
            order.setOrderState(OrderState.NORMAL_QUIT);
            auditService.saveOrderItemAndOrOrder(orOrderItem, order);
        } else if (7 == orderState) {
            order.setAuditState(OrderState.EXTEND);
            order.setOrderState(OrderState.EXTEND);
            auditService.saveOrderItemAndOrOrder(orOrderItem, order);
        } else if (5 == orderState) {
            order.setAuditState(OrderState.EARLY_QUIT);
            order.setOrderState(OrderState.EARLY_QUIT);
            auditService.saveOrderItemAndOrOrder(orOrderItem, order);
        } else if (13 == orderState) {
            order.setAuditState(OrderState.NOSHOW);
            order.setOrderState(OrderState.NOSHOW);
            String noshowCode = (String) params.get("noshowCode");
            // add by haibo.li V2.4.1 补登日审 -- 完成 无法保存值进去
            if (null != noshowCode && !noshowCode.equals("")) {
                order.setNoshowCode(Integer.parseInt(noshowCode));
            }
            String noshowReason = (String) params.get("noshowReason");
            if (null != noshowReason && !noshowReason.equals("")) {
                order.setNoshowReason(noshowReason);
            }

            auditService.saveOrderItemAndOrOrder(orOrderItem, order);
        }
        /** 2.9.3 add chenjuesu 当所有房间有结果时,设置日审状态为已完成 begin **/
        orderItem = auditService.getVOrderItem(Long.parseLong(orderID), Integer.valueOf(1));
        boolean bFinish = true;
        for (Object obj : orderItem) {
        	VOrOrderItem item = (VOrOrderItem)obj;
        	if(item.getOrderState() == 0){
        		bFinish = false;
        	}
		}
        if(bFinish){
        	order.setOrderAuditState(OrderAuditState.FINISH);
        	auditService.sendAuditViewOrder(order);
        }
        /** 2.9.3 add chenjuesu 当所有房间有结果时,设置日审状态为已完成 end **/
        return patchAudit();
    }

    /**
     * 补登日审 -- 已入住
     * 
     * @return
     */
    public String patchAuditWork() {
        patchAuditWorkExt();
        return patchAudit();
    }

    private void patchAuditWorkExt() {
        Map params = super.getParams();
        user = super.getOnlineWorkStates();
        Date modifiedTime = new Date();
        orderID = (String) params.get("orderID");
        VOrOrder order = auditService.getVOrder(Long.parseLong(orderID));
        String bare = "bare";
        int state = OrderItemAuditState.ALREADY_SAVE;
        // 已入住操作
        String auditItemStr = params.get("rState").toString();
        int orderState = Integer.parseInt(params.get("state").toString());
        if (auditItemStr.equals(bare)) {
        	log.info("没有入住的房间!");
        } else {
            String[] ids = auditItemStr.split(",");
            selID = new Long[ids.length];
            for (int i = 0; i < selID.length; i++) {
                String[] idAndType = ids[i].split("/");
                selID[i] = Long.parseLong(idAndType[1]);
                int auditType = Integer.parseInt(idAndType[0]);
                orderItem = auditService.getViewOrderItems(selID[i], auditType);
                VOrOrderItem orOrderItem = (VOrOrderItem) orderItem.get(0);
                /** 2.9.3 add chenjuesu 当财务已获取后,不能再更改ITEM 表 begin **/
                if(orOrderItem.isSettlement()){
                	bGet = true;
                	return;
                }
                /** 2.9.3 add chenjuesu 当所有房间有结果时,设置日审状态为已完成 end **/
                orOrderItem.setOrderState(1);
                orOrderItem.setAuditState(state);
                orOrderItem.setNotesMan(user.getName());
                orOrderItem.setNoteTime(modifiedTime);
                auditService.sendViewAuditDetail(orOrderItem);
            }
        }
        // 未入住操作

        String auditItemNotStr = params.get("rStateNot").toString();
        if (auditItemNotStr.equals(bare)) {
        	log.info("全部房间都已入住!");
        } else {
            String[] ids1 = auditItemNotStr.split(",");
            selID = new Long[ids1.length];
            for (int i = 0; i < ids1.length; i++) {
                String[] idAndType = ids1[i].split("/");
                selID[i] = Long.parseLong(idAndType[1]);
                int auditType = Integer.parseInt(idAndType[0]);
                orderItem = auditService.getViewOrderItems(selID[i], auditType);
                VOrOrderItem orOrderItem = (VOrOrderItem) orderItem.get(0);
                /** 2.9.3 add chenjuesu 当财务已获取后,不能再更改ITEM 表 begin **/
                if(orOrderItem.isSettlement()){
                	bGet = true;
                	return;
                }
                /** 2.9.3 add chenjuesu 当所有房间有结果时,设置日审状态为已完成 end **/
                orOrderItem.setOrderState(2);
                orOrderItem.setAuditState(state);
                orOrderItem.setNotesMan(user.getName());
                orOrderItem.setNoteTime(modifiedTime);
                auditService.sendViewAuditDetail(orOrderItem);
            }
        }
        // 修改房间号
        String auditRoomStr = params.get("rID").toString();
        String[] rId = auditRoomStr.split(",");
        String[] roomNos = new String[rId.length];
        selID = new Long[rId.length];
        for (int i = 0; i < rId.length - 1; i++) {
            String[] idAndType = rId[i + 1].split("/");
            selID[i] = Long.parseLong(idAndType[1]);
            int auditType = Integer.parseInt(idAndType[0]);
            // selID[i] = Long.parseLong(rId[i+1]);
            roomNos[i] = (String) params.get("roomNo_" + orderID + "_" + (i + 1));
            orderItem = auditService.getViewOrderItems(selID[i], auditType);
            VOrOrderItem orOrderItem = (VOrOrderItem) orderItem.get(0);
            orOrderItem.setRoomNo(roomNos[i]);
            auditService.sendViewAuditDetail(orOrderItem);
        }
        // 修改订单状态
        order.setAuditState(orderState);
        order.setSpecialNote((String) params.get("specialNote"));
        order.setModifierName(user.getName());
        // QC444日审操作后最后修改人中文名和工号对不上add by chenjiajie@2009-05-05
        order.setModifier(user.getLogonId());
        order.setModifiedTime(modifiedTime);
        auditService.sendAuditViewOrder(order);
    }

    /**
     * 处理日审单 -- 显示
     * 
     * @return
     */
    public String noteAuditList() {
        System.currentTimeMillis();
        Map params = super.getParams();
        Date date = null;
        // TMC订单记录查询
        HOP_TMC_ORDER = URLClient.HOP_TMC_ORDER;
        // HOP_TMC_ORDER = "http://10.10.1.140/TMC/orderFlow/ProtocolOrderAction!view.action";
        if (null == night) {
            date = DateUtil.getDate(checkNight);
        } else {
            date = DateUtil.getDate(params.get("night").toString());
        }
        checkNight = date;
        // 获取该酒店当天的日审记录
        HtlHotel hotel = hotelService.findHotel(hotelId);
        hotelName = hotel.getChnName();
        telephone = hotel.getTelephone();
        hotels = auditService.getViewOrder(hotelId, date);
        if (0 >= hotels.size()) {
            return forwardMsgBox("日审明细不存在！", "refreshSelf()");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        Date date_1 = DateUtil.getDate(df
            .format(new Date(date.getTime() - 1 * 24 * 60 * 60 * 1000)));

        // 获取当天的日审记录
        orderItem = auditService.getViewOrderItem(hotelId, date);
        // 获取前一天的日审记录 方便房间号读取
        orderItem_romm = auditService.getViewOrderItem(hotelId, date_1, date);

        // 将订单所对应的房间号显示

        for (int j = 0; j < orderItem.size(); j++) {
            VOrOrderItem orOrderItem = (VOrOrderItem) orderItem.get(j);
            if (orOrderItem.isFirstNight()) {
                String fellowNames = null;
                for (int k = 0; k < hotels.size(); k++) {
                    VOrOrder orOrder = (VOrOrder) hotels.get(k);
                    if (orOrder.getID().equals(orOrderItem.getOrderID())) {
                        fellowNames = orOrder.getFellowNames();
                        break;
                    }
                }
                if (null != fellowNames && null == orOrderItem.getFellowName()) {
                    String[] fellowName1 = fellowNames.split(" ");
                    int ri = orOrderItem.getRoomIndex();
                    if (fellowName1.length <= orOrderItem.getRoomIndex()) {
                        ri = fellowName1.length - 1;
                    }
                    orOrderItem.setFellowName(fellowName1[ri]);
                }
            }
            for (int i = 0; i < orderItem_romm.size(); i++) {
                VOrOrderItem order_romm = (VOrOrderItem) orderItem_romm.get(i);
                String roomNo = order_romm.getRoomNo();
                String fellowName;
                if (!orOrderItem.isFirstNight()
                    && null == orOrderItem.getFellowName()
                    && orOrderItem.getRoomIndex() == order_romm.getRoomIndex()
                    && orOrderItem.getOrderID().toString().equals(
                        order_romm.getOrderID().toString())) {
                    fellowName = order_romm.getFellowName();
                    orOrderItem.setFellowName(fellowName);
                }
                // 比较订单ID 房间index 修改后房间号不再读取前一天的
                if (orOrderItem.getOrderID().toString().equals(order_romm.getOrderID().toString())
                    && orOrderItem.getRoomIndex() == order_romm.getRoomIndex()
                    && null == orOrderItem.getRoomNo()) {
                    orOrderItem.setRoomNo(roomNo);
                }
            }
            /** 增加获取会员姓名功能 add by chenjiajie 2008-10-16 begin **/
            for (int k = 0; k < hotels.size(); k++) {
                VOrOrder orOrder = (VOrOrder) hotels.get(k);

                // v2.5 add by chenkeming@2009-04-16
                if (0 < orOrder.getChannel()) {
                    channel = String.valueOf(orOrder.getChannel());
                }

                if (orOrder.getID().equals(orOrderItem.getOrderID())) {
                    // CC订单直接获取会员姓名，条件:会员姓名不为空
                    if (null != orOrder.getMemberName() && !orOrder.getMemberName().equals("")) {
                        orOrderItem.setMemberName(orOrder.getMemberName());
                    } else {
                    	MemberDTO loginMember = getMemberSimpleInfo(String.valueOf(orOrder
                            .getMemberCd()), true);
                        orOrderItem.setMemberName(loginMember.getName());
                    }
                    break;
                }
            }
            /** 增加获取会员姓名功能 add by chenjiajie 2008-10-16 begin **/
        }

      
        if (null != page && page.equals("queryAuditInfo")) {
            return "query_audit_info_list";
        }
        return "note_audit_list";
    }

    /**
     * 直联酒店显示对方系统的日审记录 create guojun 2009-1-9
     * 
     * @return
     */
    public String directionAuditList() {
        /**
         * 直联酒店
         * 
         * @author guojun 2008-12-12 17:30
         */
        Map params = super.getParams();

        if (null == params.get("night")) {
            errorInfo = "日审的间夜不能为空！";
            return AUDIT_DIRECTION;
        }

        // 取得日审的间夜
        night = (String) params.get("night");

        if (null == params.get("hotelId")) {
            errorInfo = "日审酒店ID不能为空！";
            return AUDIT_DIRECTION;
        }
        // 取得日审的酒店ID
        hotelId = Long.valueOf((String) params.get("hotelId"));

        if (null == params.get("checkNight")) {
            errorInfo = "日审日期不能为空！";
            return AUDIT_DIRECTION;
        }

        // 取得日审的酒店渠道
        checkNight = DateUtil.getDateFromOriDateStr((String) params.get("checkNight"));

        Date date = null;
        if (null == night) {
            date = DateUtil.getDate(checkNight);
        } else {
            date = DateUtil.getDate(params.get("night").toString());
        }
        hotels = auditService.getViewOrder(hotelId, date);
        if (0 >= hotels.size()) {
            errorInfo = "日审明细不存在！";
            return AUDIT_DIRECTION;
        }
        errorInfo = null;
        int channelType = 0;

        DailyAuditExResponse dailyAuditExResponse = null;
        DailyAuditExRequest dailyAuditExRequest = new DailyAuditExRequest();
        dailyAuditExRequest.setChainCode(null);
        dailyAuditExRequest.setHotelId(hotelId);
        for (int k = 0; k < hotels.size(); k++) {
            VOrOrder orOrder = (VOrOrder) hotels.get(k);
            channelType = orOrder.getChannel();
            if (0 < channelType) {
                dailyAuditExRequest.setChannelCode(channelType);
                dailyAuditExRequest.getOrderCode().add(String.valueOf(orOrder.getID()));
            }
        }
        dailyAuditExRequest.setAuditDate(DateUtil.dateToString(date));
        try {
            dailyAuditExResponse = hdlService.dailyAudit(dailyAuditExRequest);
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }
        if (null == dailyAuditExResponse) {
            // 如果日审为空,
            daList = null;

        } else {
            if (null == dailyAuditExResponse.getDailyAudits()) {
                daList = null;
            } else {
                daList = dailyAuditExResponse.getDailyAudits();
                return AUDIT_DIRECTION;
            }
        }

        if (null == daList) {
            errorInfo = "日审明细不存在！";
        }
        return AUDIT_DIRECTION;
    }

    /**
     * 处理日审单 -- 已入住
     * 
     * @return
     */
    public String auditWork() {
        long lBegin = System.currentTimeMillis();
        Map params = super.getParams();
        user = super.getOnlineWorkStates();
        Date modifiedTime = new Date();
        int state = OrderItemAuditState.ALREADY_SAVE;
        Date date = DateUtil.getDate(night);
        orderID = (String) params.get("orderID");
        hotelId = Long.parseLong(params.get("hotelId").toString());
        VOrOrder order = auditService.getVOrder(Long.parseLong(orderID));
        String bare = "bare";
        // 已入住操作
        String auditItemStr = params.get("rState").toString();
        int orderState = Integer.parseInt(params.get("state").toString());
        if (auditItemStr.equals(bare)) {
        	log.info("没有入住的房间!");
        } else {
            String[] ids = auditItemStr.split(",");
            selID = new Long[ids.length];
            for (int i = 0; i < selID.length; i++) {
                String[] idAndType = ids[i].split("/");
                selID[i] = Long.parseLong(idAndType[1]);
                int auditType = Integer.parseInt(idAndType[0]);
                orderItem = auditService.getViewOrderItems(selID[i], auditType);
                VOrOrderItem orOrderItem = (VOrOrderItem) orderItem.get(0);
                orOrderItem.setOrderState(1);
                orOrderItem.setAuditState(state);
                orOrderItem.setNotesMan(user.getName());
                orOrderItem.setNoteTime(modifiedTime);
                // 修改未入住 2007-12-29
                VOrOrderItem orOrderShow = editOrderNotShow(orderID, date, false, orOrderItem
                    .getRoomIndex());
                auditService.saveAuditInfo(orOrderItem, orOrderShow);
                // auditService.sendViewAuditDetail(orOrderItem);
            }
        }
        // 未入住操作
        String auditItemNotStr = params.get("rStateNot").toString();
        if (auditItemNotStr.equals(bare)) {
        	log.info("全部房间都已入住!");
        } else {
            String[] ids1 = auditItemNotStr.split(",");
            selID = new Long[ids1.length];
            for (int i = 0; i < ids1.length; i++) {
                String[] idAndType = ids1[i].split("/");
                selID[i] = Long.parseLong(idAndType[1]);
                int auditType = Integer.parseInt(idAndType[0]);
                orderItem = auditService.getViewOrderItems(selID[i], auditType);
                VOrOrderItem orOrderItem = (VOrOrderItem) orderItem.get(0);
                orOrderItem.setOrderState(2);
                orOrderItem.setAuditState(state);
                orOrderItem.setNotesMan(user.getName());
                orOrderItem.setNoteTime(modifiedTime);
                // 修改未入住 2007-12-29
                VOrOrderItem orOrderShow = editOrderNotShow(orderID, date, true, orOrderItem
                    .getRoomIndex());
                auditService.saveAuditInfo(orOrderItem, orOrderShow);
                // auditService.sendViewAuditDetail(orOrderItem);
            }
        }
        String auditRoomStr = params.get("rID").toString();
        String[] rId = auditRoomStr.split(",");
        String[] roomNos = new String[rId.length];
        selID = new Long[rId.length];
        for (int i = 0; i < rId.length - 1; i++) {
            String[] idAndType = rId[i + 1].split("/");
            selID[i] = Long.parseLong(idAndType[1]);
            int auditType = Integer.parseInt(idAndType[0]);
            // selID[i] = Long.parseLong(rId[i+1]);
            roomNos[i] = (String) params.get("roomNo_" + orderID + "_" + (i + 1));
            orderItem = auditService.getViewOrderItems(selID[i], auditType);
            VOrOrderItem orOrderItem = (VOrOrderItem) orderItem.get(0);
            // VOrOrderItem orOrderItem = auditService.getViewOrderItem(selID[i]);
            orOrderItem.setRoomNo(roomNos[i]);
            auditService.sendViewAuditDetail(orOrderItem);
        }

        // 修改订单状态
        params.get("lastNight");
        // 记录日审状态
        order.setAuditState(orderState);

        order.setSpecialNote((String) params.get("specialNote"));
        order.setModifierName(user.getName());
        // QC444日审操作后最后修改人中文名和工号对不上add by chenjiajie@2009-05-05
        order.setModifier(user.getLogonId());
        order.setModifiedTime(modifiedTime);
        // VOrOrderItem orOrderItem = editWorkState(orderID,date,roomIndex,state);
        auditService.sendAuditViewOrder(order);
        log.info("记录日审共花 : " + (System.currentTimeMillis() - lBegin) + "毫秒");
        return noteAuditList();
    }

    /**
     * 处理日审单 -- 修改为入住情况下的显示状态
     * 
     * @return
     */
    public VOrOrderItem editOrderNotShow(String orderID, Date date, boolean show, int roomIndex) {
        VOrOrderItem orOrderItem = null;
        List workState = auditService.findSimilarViewOrOrderShow(Long.parseLong(orderID), date,
            roomIndex);
        for (int i = 0; i < workState.size(); i++) {
            orOrderItem = (VOrOrderItem) workState.get(i);
            if (show) {
                orOrderItem.setOrderState(2);
            }
            orOrderItem.setShow(show);
        }
        log.info(workState.size());
        return orOrderItem;
    }

    /*
     * public OrOrderItemTemp insertOrderItemTemp(String orderID, Date date ,int state){ List
     * workState = auditService.findSimilarOrOrderItemTemp(Long.parseLong(orderID), date);
     * OrOrderItemTemp orOrderItemTemp = null; for (int i=0;i<workState.size();i++){ VOrOrderItem
     * orOrderItem = (VOrOrderItem)workState.get(i); orOrderItemTemp = new OrOrderItemTemp();
     * orOrderItemTemp.setAuditState(0); orOrderItemTemp.setAuditType(orOrderItem.getAuditType());
     * orOrderItemTemp.setHotelId(orOrderItem.getHotelId()); SimpleDateFormat df=new
     * SimpleDateFormat("yyyy-MM-dd"); Date date_1 = DateUtil.getDate(df.format(new
     * Date(date.getTime() + 1 * 24 * 60 * 60 * 1000))); orOrderItemTemp.setNight(date_1);
     * orOrderItemTemp.setOrderId(orOrderItem.getOrderID());
     * orOrderItemTemp.setRoomIndex(orOrderItem.getRoomIndex());
     * orOrderItemTemp.setRoomNo(orOrderItem.getRoomNo()); orOrderItemTemp.setOrderState(2);
     * orOrderItemTemp.setShow(false); } System.out.println(orOrderItemTemp.getID()); return
     * orOrderItemTemp; }
     */

    /**
     * noshow情况下的状态处理
     * 
     */
    public VOrOrderItem noshowDispose() {
        user = super.getOnlineWorkStates();
        Map params = super.getParams();
        Date modifiedTime = new Date();
        ;
        orderID = (String) params.get("orderID");
        List oItem = auditService.getVOrderItem(Long.parseLong(orderID), Integer.valueOf(1));
        VOrOrderItem orderItem = null;
        for (int i = 0; i < oItem.size(); i++) {
            orderItem = (VOrOrderItem) oItem.get(i);
            orderItem.setOrderState(2);
            orderItem.setNotesMan(user.getName());
            orderItem.setNoteTime(modifiedTime);
        }
        return orderItem;
    }

    /**
     * 提前退房情况下的状态处理
     * 
     */
    public VOrOrderItem fadeRoomDispose() {
        user = super.getOnlineWorkStates();
        Date modifiedTime = new Date();
        Map params = super.getParams();
        orderID = (String) params.get("orderID");
        Date date = DateUtil.getDate(params.get("night").toString());
        List oItem = auditService.getVOrderItem1(Long.parseLong(orderID), date);
        VOrOrderItem orderItem = null;
        for (int i = 0; i < oItem.size(); i++) {
            orderItem = (VOrOrderItem) oItem.get(i);
            orderItem.setOrderState(2);
            orderItem.setNotesMan(user.getName());
            orderItem.setNoteTime(modifiedTime);
        }
        return orderItem;
    }

    /**
     * 处理日审单 -- 修改操作状态
     * 
     * @return
     */
    public VOrOrderItem editOrderShow(String orderID, Date date, boolean show) {
        VOrOrderItem orOrderItem = null;
        List workState = auditService.findSimilarViewOrOrderShow(Long.parseLong(orderID), date);
        for (int i = 0; i < workState.size(); i++) {
            orOrderItem = (VOrOrderItem) workState.get(i);
            orOrderItem.setShow(show);
        }
        log.info(workState.size());
        return orOrderItem;
    }

    /**
     * 处理日审单 -- noshow 提前退房
     * 
     * @return
     */
    /*
     * public String auditNotWork(){ Map params = super.getParams(); user =
     * super.getOnlineWorkStates(); Date modifiedTime = new Date(); int roomIndex = 0; int state =
     * OrderItemAuditState.ALREADY_SAVE; orderID = (String)params.get("orderID"); VOrOrder order =
     * auditService.getVOrder(Long.parseLong(orderID)); hotelId =
     * Long.parseLong(params.get("hotelId").toString()); Date date =
     * DateUtil.getDate(params.get("night").toString()); String firstNight =
     * (String)params.get("firstNight"); if(firstNight.equals("true")){ //noshow
     * order.setAuditState(OrderState.NOSHOW); VOrOrderItem orderItem = noshowDispose();
     * auditService.sendViewAuditDetail(orderItem);
     * order.setNoshowReason((String)params.get("noshowReason")); }else{ //提前退房
     * order.setAuditState(OrderState.EARLY_QUIT); VOrOrderItem orderItem = fadeRoomDispose();
     * auditService.sendViewAuditDetail(orderItem);
     * 
     * } order.setSpecialNote((String)params.get("specialNote"));
     * order.setModifierName(user.getName()); order.setModifiedTime(modifiedTime); VOrOrderItem
     * orOrderItem = editWorkState(orderID,date,roomIndex,state);
     * auditService.saveNoShow(order,orOrderItem); return noteAuditList(); }
     */

    /**
     * 处理日审单 -- noshow 提前退房
     * 
     * @return
     */
    public String auditWorkNoShow() {
        Map params = super.getParams();
        user = super.getOnlineWorkStates();
        Date modifiedTime = new Date();
        int roomIndex = 0;
        int state = OrderItemAuditState.ALREADY_SAVE;
        orderID = (String) params.get("orderID");
        hotelId = Long.parseLong(params.get("hotelId").toString());
        Date date = DateUtil.getDate(params.get("night").toString());
        int orderState = Integer.parseInt(params.get("state").toString());
        VOrOrder order = auditService.getVOrder(Long.parseLong(orderID));
        order.setAuditState(orderState);
        if (13 == orderState) {
            VOrOrderItem orderItem = noshowDispose();
            auditService.sendAuditDetail(orderItem);
            String noshowCode = (String) params.get("noshowCode");
            if (null != noshowCode && !noshowCode.equals("")) {
                order.setNoshowCode(Integer.parseInt(noshowCode));
            }
            order.setNoshowReason((String) params.get("noshowReason"));
        } else if (5 == orderState) {
            VOrOrderItem orderItem = fadeRoomDispose();
            auditService.sendAuditDetail(orderItem);
        }
        order.setSpecialNote((String) params.get("specialNote"));
        order.setModifierName(user.getName());
        // QC444日审操作后最后修改人中文名和工号对不上add by chenjiajie@2009-05-05
        order.setModifier(user.getLogonId());
        order.setModifiedTime(modifiedTime);
        // auditService.sendAuditOrder(order);
        VOrOrderItem orOrderItem = editWorkState(orderID, date, roomIndex, state);
        // OrOrderItem orOrderShow = editOrderShow(orderID,date,show);
        // auditService.saveOrderItem(orOrderItem);
        log.info(orOrderItem.getAuditState());
        auditService.saveNoShow(order, orOrderItem);

        return noteAuditList();
    }

    /**
     * 处理日审单 -- 待审核
     * 
     * @return
     */
    public String auditNotAuditing() {
        Map params = super.getParams();
        int roomIndex = 0;
        boolean bResult = false;
        int state = OrderItemAuditState.STAY_AUDITING;
        Date date = DateUtil.getDate(night);
        orderID = (String) params.get("orderID");
        hotelId = Long.parseLong(params.get("hotelId").toString());
        List hotels = auditService.findSimilarDailyAudit(hotelId, date);
        OrDailyAudit orDailyAudit = (OrDailyAudit) hotels.get(0);
        orDailyAudit.setStatus(OrdailyauditStatus.STAY_AUDITING);

        VOrOrderItem orOrderItem = editWorkState(orderID, date, roomIndex, state);
        auditService.sendAuditDetail(orOrderItem);

        List hotels1 = auditService.getOrderItemNum(hotelId, date, roomIndex);
        for (int i = 0; i < hotels1.size(); i++) {
            VOrOrderItem orderItem = (VOrOrderItem) hotels1.get(i);
            if (2 == orderItem.getAuditState()) {
                bResult = true;
            }
        }
        // 待审核状态，清除操作人，将日审记录返回分配池。
        if (bResult) {
            orDailyAudit.setAssignTo("");
        }
        // 增加“变成待审核状态”的时间。stringToDateTime

        orDailyAudit.setDailyAuditTime(DateUtil.stringToDatetime(DateUtil
            .datetimeToString(new Date())));

        auditService.saveOrDailyAudit(orDailyAudit);
        // auditService.saveOrDailyAuditAndOrderItem(orOrderItem,orDailyAudit);
        // if(bResult){
        // return "audit_my_list";
        // }
        return noteAuditList();

    }

    /**
     * 处理日审单 -- 修改操作状态
     * 
     * @return
     */
    public VOrOrderItem editWorkState(String orderID, Date date, int roomIndex, int state) {
        user = super.getOnlineWorkStates();
        VOrOrderItem orOrderItem = null;
        Date noteTime = new Date();
        List workState = auditService.findSimilarOrOrderItem(Long.parseLong(orderID), date);
        for (int i = 0; i < workState.size(); i++) {
            orOrderItem = (VOrOrderItem) workState.get(i);
            orOrderItem.setAuditState(state);
            orOrderItem.setNotesMan(user.getName());
            orOrderItem.setNoteTime(noteTime);
        }
        log.info(workState.size());
        return orOrderItem;
    }

    /**
     * 处理日审单 -- 正常退房
     * 
     * @return
     */
    /**
     * public String auditNormalQuit(){ Map params = super.getParams(); Date date =
     * DateUtil.getDate(night); user = super.getOnlineWorkStates(); Date now = new Date(); orderID =
     * (String)params.get("orderID"); hotelId = Long.parseLong(params.get("hotelId").toString());
     * int roomIndex = 0; int state = OrderItemAuditState.ALREADY_SAVE; VOrOrderItem orOrderItem =
     * editWorkState(orderID,date,roomIndex,state); orOrderItem.setOrderState(1); VOrOrder order =
     * auditService.getVOrder(Long.parseLong(orderID));
     * 
     * order.setModifierName(user.getName()); order.setModifiedTime(now);
     * order.setAuditState(OrderState.NORMAL_QUIT);
     * auditService.saveOrderItemAndOrOrder(orOrderItem,order); return noteAuditList(); }
     **/
    /**
     * 处理日审单 -- 延住
     * 
     * @return
     */
    /**
     * public String auditExtend(){ Map params = super.getParams(); Date date =
     * DateUtil.getDate(night); user = super.getOnlineWorkStates(); Date now = new Date(); orderID =
     * (String)params.get("orderID"); hotelId = Long.parseLong(params.get("hotelId").toString());
     * int roomIndex = 0; int state = OrderItemAuditState.ALREADY_SAVE; VOrOrderItem orOrderItem =
     * editWorkState(orderID,date,roomIndex,state); orOrderItem.setOrderState(1); VOrOrder order =
     * auditService.getVOrder(Long.parseLong(orderID));
     * 
     * order.setModifierName(user.getName()); order.setModifiedTime(now);
     * order.setAuditState(OrderState.EXTEND);
     * auditService.saveOrderItemAndOrOrder(orOrderItem,order); return noteAuditList(); }
     **/
    /**
     * 处理日审单 -- 完成
     * 
     * @return
     */
    public String auditFulfill() {
        Map params = super.getParams();
        boolean bResult = false;
        Date date = DateUtil.getDate(night);
        Date now = new Date();
        user = super.getOnlineWorkStates();
        orderID = (String) params.get("orderID");
        hotelId = Long.parseLong(params.get("hotelId").toString());
        int roomIndex = 0;
        int state = OrderItemAuditState.ACHIEVE;
        // 取得该酒店当天的所有订单数量
        hotels = auditService.getOrderItemNum(hotelId, date, roomIndex);
        // 取得该订单操作状态
        VOrOrderItem orOrderItem = editWorkState(orderID, date, roomIndex, state);

        VOrOrder order = auditService.getVOrder(Long.parseLong(orderID));

        order.setModifierName(user.getName());
        // QC444日审操作后最后修改人中文名和工号对不上add by chenjiajie@2009-05-05
        order.setModifier(user.getLogonId());
        order.setModifiedTime(now);
        int orderState = order.getAuditState();
        if (5 == orderState) {
            VOrOrderItem orOrderShow = editOrderShow(orderID, date, true);
            order.setAuditState(OrderState.EARLY_QUIT);
            order.setOrderState(OrderState.EARLY_QUIT);
            auditService.saveFulfill(orOrderItem, orOrderShow, order);
        } else if (13 == orderState) {
            VOrOrderItem orOrderShow = editOrderShow(orderID, date, true);
            order.setAuditState(OrderState.NOSHOW);
            order.setOrderState(OrderState.NOSHOW);
            auditService.saveFulfill(orOrderItem, orOrderShow, order);
        } else if (6 == orderState) {
            VOrOrderItem orOrderShow = editOrderShow(orderID, date, false);
            order.setAuditState(OrderState.NORMAL_QUIT);
            order.setOrderState(OrderState.NORMAL_QUIT);
            auditService.saveFulfill(orOrderItem, orOrderShow, order);
        } else if (7 == orderState) {
            VOrOrderItem orOrderShow = editOrderShow(orderID, date, false);
            order.setAuditState(OrderState.EXTEND);
            order.setOrderState(OrderState.EXTEND);
            auditService.saveFulfill(orOrderItem, orOrderShow, order);
        } else {
            order.setOrderState(OrderState.CHECKIN);
            order.setAuditState(OrderState.CHECKIN);
            auditService.saveOrderItemAndOrOrder(orOrderItem, order);
        }
        // 针对订单最后一天的操作
        // String lastNight = (String)params.get("lastNight");
        // if(lastNight.equals("1")){
        // System.out.println(lastNight);
        // OrOrderItemTemp orOrderItemTemp = insertOrderItemTemp(orderID,date,state);
        // auditService.saveOrOrderItemTemp(orOrderItemTemp);
        // }
        for (int i = 0; i < hotels.size(); i++) {
            VOrOrderItem orderItem = (VOrOrderItem) hotels.get(i);
            if (3 == orderItem.getAuditState()) {
            } else if (2 == orderItem.getAuditState()) {
                bResult = true;
            } else {
                return noteAuditList();
            }
        }
        List hotels = auditService.findSimilarDailyAudit(hotelId, date);
        OrDailyAudit orDailyAudit = (OrDailyAudit) hotels.get(0);
        orDailyAudit.setCompleteTime(now);
        if (!bResult) {
            orDailyAudit.setStatus(OrdailyauditStatus.ACHIEVE);
        } else {
            orDailyAudit.setAssignTo("");
            orDailyAudit.setStatus(OrdailyauditStatus.STAY_AUDITING);
        }
        auditService.saveOrDailyAudit(orDailyAudit);
        if (bResult) {
            return "audit_my_list";
        }
        return noteAuditList();
    }

    /**
     * 批量分配订单
     * 
     * @return
     */
    public String batchPutToOrderList() {
        Map params = super.getParams();
        boolean bResult = false;
        OrWorkStates user = workStatesManager.returnWorkStatesBylogonId((String) params
            .get("Operator"));
        selIDs = (String) params.get("checkedId");
        String[] ids = selIDs.split(",");
        selID = new Long[ids.length];
        for (int i = 0; i < selID.length; i++) {
            selID[i] = Long.parseLong(ids[i]);
        }
        bResult = auditService.batchArrangeAuditHotel(selID, user);
        if (bResult) {
            return forwardMsgBox("分配给:" + user.getName() + ",成功！", "refreshSelf()");
        } else {
            return forwardMsgBox("分配给:" + user.getName() + ",失败！", null);
        }

    }

    /**
     * 将日审酒店分配到自己的工作档案
     * 
     * @return
     */
    public String putToMyList() {
        user = super.getOnlineWorkStates();
        boolean bResult = auditService.arrangeAuditHotel(sendAuditId, user);
        if (bResult) {
            return forwardMsgBox("获取订单成功！", "refreshSelf()");
        } else {
            return forwardMsgBox("获取订单失败！", null);
        }
    }

    /**
     * 批处理将日审酒店分配到自己的工作档案
     * 
     * @return
     */
    public String batchPutToMyList() {
        Map params = super.getParams();
        selIDs = (String) params.get("selIDs");
        String[] ids = selIDs.split(",");
        selID = new Long[ids.length];
        for (int i = 0; i < selID.length; i++) {
            selID[i] = Long.parseLong(ids[i]);
        }
        if (0 >= selID.length) {
            return forwardError("请选择要获取的酒店!");
        }
        user = super.getOnlineWorkStates();
        boolean bResult = auditService.batchArrangeAuditHotel(selID, user);

        if (bResult) {
            return forwardMsgBox("获取订单成功！", "refreshSelf()");
        } else {
            return forwardMsgBox("获取订单失败！", null);
        }
    }

    /**
     * 批量分配日审酒店 -- 显示
     * 
     * @return
     */
    public String batchAssign() {
        Map params = super.getParams();
        String selIDs = (String) params.get("selIDs");
        String[] ids = selIDs.split(",");

        if (0 >= selID.length) {
            return forwardError("请选择要分配的酒店!");
        }

        selID = new Long[ids.length];
        for (int i = 0; i < selID.length; i++) {
            selID[i] = Long.parseLong(ids[i]);
        }

        auditHotelList = auditService.batchGetAuditHotel(selID);
        auditUsers = workStatesManager.lstWorkStatesByType(WorkType.AUDIT);
        user = getOnlineWorkStates();
        auditUsers.add(user);

        return AUDIT_BATCH_ASSIGN;
    }

    /**
     * 批量分配日审酒店 -- 处理
     * 
     * @return
     */
    public String batchAssignProc() {

        Map params = super.getParams();
        String selIDs = (String) params.get("selIDs");
        String[] ids = selIDs.split(",");

        if (0 >= selID.length) {
            return forwardError("请选择要分配的酒店!");
        }

        selID = new Long[ids.length];
        for (int i = 0; i < selID.length; i++) {
            selID[i] = Long.parseLong(ids[i]);
        }

        String auditorId = (String) params.get("auditorId");
        user = workStatesManager.returnWorkStatesBylogonId(auditorId);
        boolean bResult = auditService.batchArrangeAuditHotel(selID, user);

        if (bResult) {
            return super.forwardMsgBox("分配给:" + user.getName() + ",成功！", "refreshAudit()");
        } else {
            return super.forwardMsgBox("分配给:" + user.getName() + ",失败！", "refreshAudit()");
        }
    }

    /**
     * 批量添加纸质日审酒店
     * 
     * @return
     */
    public String addAuditList() {

        OrPaperContact aHotel = null;
        Map params = super.getParams();
        selIDs = (String) params.get("selIDs");
        String[] ids = selIDs.split(",");
        selID = new Long[ids.length];
        for (int i = 0; i < selID.length; i++) {
            selID[i] = Long.parseLong(ids[i]);
        }
        if (0 >= selID.length) {
            return forwardError("请选择要添加的酒店!");
        }
        boolean bResult = false;
        //HtlHotel hotel = null;
        user = super.getOnlineWorkStates();
        Calendar ca = Calendar.getInstance();
        Date createDate = ca.getTime();
        for (int i = 0; i < selID.length; i++) {
            aHotel = new OrPaperContact();
           // hotel = hotelManage.findHotel(selID[i]);
            aHotel.setHotelId(selID[i]);
            aHotel.setFaxConfirm(true);
            aHotel.setCreatedBy(user.getLogonId());
            aHotel.setCreateDate(createDate);
            bResult = auditService.saveOrUpdate(aHotel);
        }
        if (bResult) {
            return forwardMsgBox("添加酒店成功！", "refreshSelf()");
        } else {
            return forwardMsg("添加酒店失败！");
        }
    }

    /**
     * 修改日审酒店传真号码
     * 
     * @return
     */
    public String editHotelFax() {
        Map params = super.getParams();
        Long ID = Long.parseLong(params.get("checkedId").toString());
        fax = (String) params.get("fax");
        String faxConfirm = params.get("FAXCONFIRM").toString();
        boolean bResult = auditService.editFax(ID, fax, faxConfirm);
        if (bResult) {
            return forwardMsgBox("修改成功！", "refreshSelf()");
        } else {
            return forwardMsg("修改失败！");
        }
    }

    /**
     *修改纸质日审酒店传真
     * 
     * @return
     */
    public String editHotelFaxList() {

        HtlHotel hotel = null;
        Map params = super.getParams();
        selIDs = (String) params.get("selIDs");
        fax = (String) params.get("fax");
        faxConfirm = params.get("faxConfirm").toString();
        Long hID = Long.parseLong(params.get("hID").toString());
        hotel = hotelService.findHotel(hID);
        hName = hotel.getChnName();
        return "edit_hotelfax";
    }

    /**
     *手工生成日审
     * 
     * @return
     */
    public String workSendAuditList() {
        // 生成日审酒店
        OrDailyAudit orDailyAudit = null;
        Date addDate = new Date();
        Map params = super.getParams();
        Date auditDate = DateUtil.getDate((String) params.get("auditDate"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        // Date date = DateUtil.getDate(df.format(new Date(auditDate.getTime() - 1 * 24 * 60 * 60 *
        // 1000)));
        Date date = DateUtil.getDate(df.format(new Date(auditDate.getTime())));
        // 去掉重复值
        List order = auditService.findSimilarOrder1(date);

        log.info("开始");
        log.info("order========" + order.size());
       // boolean bResult = false;
        newHotel = new ArrayList();
        editHotel = new ArrayList();
        for (int i = 0; i < order.size(); i++) {
            Object[] orOrder = (Object[]) order.get(i);
            Long hotelId = (Long) orOrder[0];
            // 查询该酒店是否已经存在
            List hotels = auditService.findSimilarDailyAudit(hotelId, date);
            List orderNumbers = auditService.findSimilarOrderNumbers(hotelId, date);
            List orderItem = auditService.findReturnDailyAudit(hotelId, date);
            if (0 == hotels.size()) {
                // 生成日审酒店记录
                orDailyAudit = new OrDailyAudit();
                orDailyAudit.setHotelId(hotelId);
                orDailyAudit.setHotelName((String) orOrder[1]);
                orDailyAudit.setCheckNight(date);
                orDailyAudit.setAddTime(addDate);
                orDailyAudit.setOrderNumbers(orderNumbers.size());
                newHotel.add(orDailyAudit);
            } else {
                // 修改订单数量
                orDailyAudit = (OrDailyAudit) hotels.get(0);
                orDailyAudit.setOrderNumbers(orderNumbers.size());
                editHotel.add(orDailyAudit);
            }
            // 回写orderitem表 ID号
            Long id = orDailyAudit.getID();
            for (int j = 0; j < orderItem.size(); j++) {
                VOrOrderItem vOrOrderItem = (VOrOrderItem) orderItem.get(j);
                vOrOrderItem.setAuditId(id);
                auditService.sendAuditDetail(vOrOrderItem);
            }
           // bResult = auditService.saveOrDailyAudit(orDailyAudit);

        }
        log.info("结束");
        return WORK_SEND_AUDIT;
    }

    /**
     * 批量删除纸质日审酒店
     * 
     * @return
     */
    public String delAuditList() {
        Map params = super.getParams();
        selIDs = (String) params.get("selIDs");
        String[] ids = selIDs.split(",");
        selID = new Long[ids.length];
        for (int i = 0; i < selID.length; i++) {
            selID[i] = Long.parseLong(ids[i]);
        }
        if (0 >= selID.length) {
            return forwardError("请选择要移除的酒店!");
        }

        boolean bResult = auditService.batchDelHotel(selID);

        if (bResult) {
            return forwardMsgBox("移除酒店成功！", "refreshSelf()");
        } else {
            return forwardMsgBox("移除酒店失败！", null);
        }

    }

    /**
     * 发送日审传真预览
     * 
     * @return
     */
    public String previewAuditFax() {

        // TODO: 等待日审真实数据
        Long auditId = Long.valueOf((String)getParams().get("auditId"));
        List itemList = auditService.getOrderItemsForAuditFax(auditId);

        // 房号回写 add by baofeng.si V2.3 2008-6-19 Start
        if (null != itemList && 0 < itemList.size()) {
            List<VOrOrderItem> orderItemList = new ArrayList<VOrOrderItem>(0);
            // 取itemList的第一条数据，一个批次的日审具有相同的hotelId，date和orderId
            Object[] itemObject = (Object[]) itemList.get(0);
            Long hotelId = (Long) itemObject[8];
            Date date = (Date) itemObject[9];
            Long orderId = (Long) itemObject[10];

            // 计算前一天的日期
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
            Date date_1 = DateUtil.getDate(df.format(new Date(date.getTime() - 1 * 24 * 60 * 60
                * 1000)));
            // 查询前一天日审的订单明细
            orderItemList = auditService.getViewOrderItem(hotelId, date_1, date);
            // 遍历当天日审明细
            for (int i = 0; i < itemList.size(); i++) {
                if (null != orderItemList && 0 < orderItemList.size()) {
                    // 遍历前一天的日审明细
                    for (VOrOrderItem orderItem : orderItemList) {
                        Long itemOrderId = orderItem.getOrderID();
                        int roomIndex = ((Integer) itemObject[11]).intValue();
                        // orderId一致，房间索引一致，且当天的房号不为空，满足上述条件时将前一天的房号写入到当天日审明细中的房号中
                        if (null != itemOrderId && itemOrderId.longValue() == orderId.longValue()
                            && roomIndex == orderItem.getRoomIndex() && null == itemObject[7]) {
                            itemObject[7] = orderItem.getRoomNo();
                        }
                    }
                }
            }
        }
        // 房号回写 add by baofeng.si V2.3 2008-6-19 End

        /**
         * v2.4.2把入住人姓名拆分到OrderItem中，在该版本上线之前有可能为空 因此在使用OrderItem的入住人姓名之前先判断订单创建日期是否在v2.4.2上线之后，
         * 再判断入住人是否为空，否则使用原来订单主表的入住人姓名 by chenjiajie 2008-12-31 begin
         **/
        for (int i = 0; i < itemList.size(); i++) {
            Object[] itemVal = (Object[]) itemList.get(i);
            if (0 < DateUtil.compare(DateUtil.getDate(RELEASED_DATE),(Date)itemVal[13])) {
                String itemFellowName = (String) itemVal[12];
                if (null != itemFellowName && !itemFellowName.equals("")) {
                    itemVal[2] = itemFellowName;
                }
            }
        }
        /** by chenjiajie 2008-12-31 end **/

        request.setAttribute("itemList", itemList);

        Object[] res = auditService.getHotelInfoForAuditFax(auditId);
        if (null != res) {
            String hotelName = (String) res[0];
            Date toNight = (Date) res[1];
            Date retTime = DateUtil.getDate(toNight, +1);
            retTime = DateUtil.getDateByHour(retTime, 22);
            String hotelFax = (String) res[2];
            request.setAttribute("hotelName", hotelName);
            request.setAttribute("toNight", toNight);
            request.setAttribute("hotelFax", hotelFax);
            request.setAttribute("retTime", retTime);
            return "paperSendView";
        } else {
            return super.forwardMsgBox("非纸质日审酒店", "refreshSelf()");
        }

    }

    /**
     * 发送日审传真
     * 
     * @param auditId
     *            OrDailyAudit表的ID
     * @param faxNo
     *            传真号
     * @return ret:返回ID
     */
    private Long sendAuditFax(Long auditId, String faxNo, OrDailyAudit orDailyAudit) {

        // TODO: 等待日审真实数据
        List itemList = auditService.getOrderItemsForAuditFax(auditId);

        // 房号回写 add by baofeng.si V2.3 2008-6-19 Start
        if (null != itemList && 0 < itemList.size()) {
            List<VOrOrderItem> orderItemList = new ArrayList<VOrOrderItem>(0);
            // 取itemList的第一条数据，一个批次的日审具有相同的hotelId，date和orderId
            Object[] itemObject = (Object[]) itemList.get(0);
            Long hotelId = (Long) itemObject[8];
            Date date = (Date) itemObject[9];
            Long orderId = (Long) itemObject[10];

            // 计算前一天的日期
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
            Date date_1 = DateUtil.getDate(df.format(new Date(date.getTime() - 1 * 24 * 60 * 60
                * 1000)));
            // 查询前一天日审的订单明细
            orderItemList = auditService.getViewOrderItem(hotelId, date_1, date);
            // 遍历当天日审明细
            for (int i = 0; i < itemList.size(); i++) {
                if (null != orderItemList && 0 < orderItemList.size()) {
                    // 遍历前一天的日审明细
                    for (VOrOrderItem orderItem : orderItemList) {
                        Long itemOrderId = orderItem.getOrderID();
                        int roomIndex = ((Integer) itemObject[11]).intValue();
                        // orderId一致，房间索引一致，且当天的房号不为空，满足上述条件时将前一天的房号写入到当天日审明细中的房号中
                        if (null != itemOrderId && itemOrderId.longValue() == orderId.longValue()
                            && roomIndex == orderItem.getRoomIndex() && null == itemObject[7]) {
                            itemObject[7] = orderItem.getRoomNo();
                        }
                    }
                }
            }
        }
        // 房号回写 add by baofeng.si V2.3 2008-6-19 End

        Object[] res = auditService.getHotelInfoForAuditFax(auditId);
        if (null == res || null == itemList || 0 >= itemList.size()) {
            return null;
        }

        OrderFax orderFax = new OrderFax();
        for (int i = 0; i < itemList.size(); i++) {
            Object[] itemVal = (Object[]) itemList.get(i);
            OrderItemFax item = new OrderItemFax();
            item.setItemOrderCD((String) itemVal[0]);
            item.setItemName((String) itemVal[1]);
            /**
             * v2.4.2把入住人姓名拆分到OrderItem中，在该版本上线之前有可能为空 因此在使用OrderItem的入住人姓名之前先判断订单创建日期是否在v2.4.2上线之后，
             * 再判断入住人是否为空，否则使用原来订单主表的入住人姓名 by chenjiajie 2008-12-31 begin
             **/
            if (0 < DateUtil.compare(DateUtil.getDate(RELEASED_DATE),(Date)itemVal[13])) {
                String itemFellowName = (String) itemVal[12];
                if (null == itemFellowName || itemFellowName.equals("")) {
                    itemFellowName = (String) itemVal[2];
                }
                item.setItemFellowNames(itemFellowName);
            } else {
                item.setItemFellowNames((String) itemVal[2]);
            }
            /** by chenjiajie 2008-12-31 end **/
            item.setItemConfirmNo((String) itemVal[4]);
            Date checkInDate = (Date) itemVal[5];
            Date checkOutDate = (Date) itemVal[6];
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
            item.setItemCheckInDate(df.format(checkInDate));
            item.setItemCheckOutDate(df.format(checkOutDate));
            item.setRoomNo((String) itemVal[7]);
            orderFax.getOrderItemList().add(item);
        }
        orderFax.setHotelName((String) res[0]);
        orderFax.setHotelFax((String) res[2]);
        Date toNight = (Date) res[1];
        orderFax.setArrivalTimeStart(DateUtil.dateToString(toNight));
        Date retTime = DateUtil.getDate(toNight, +1);
        retTime = DateUtil.getDateByHour(retTime, 22);
        orderFax.setArrivalTimeEnd(DateUtil.datetimeToString(retTime));
        // System.out.println("=============="+orDailyAudit.getID());
        orderFax.setBarCode("HB" + orDailyAudit.getID());
        String xmlString = msgAssist.genOrderFaxXml(orderFax);

        Fax fax = new Fax();
        fax.setXml(xmlString);
        fax.setApplicationName("hotel");
        fax.setTemplateFileName(FaxEmailModel.DAY_CHECK_FORM);
        fax.setTo(new String[] { faxNo });
        // fax.setTo(new String[] { "075582876159" });
        Long ret = communicaterService.sendFax(fax);

        return ret;
    }

    /**
     * 弹出发送传真窗口
     * 
     */
    public String sendFaxToCus() {
        OrDailyAudit orDailyAudit = auditService.getOrDailyAudit(sendAuditId);
        List orPaperContacts = auditService.getOrPaperContact(orDailyAudit.getHotelId());
        OrPaperContact orPaperContact = (OrPaperContact) orPaperContacts.get(0);
        request.setAttribute("Faxvalue", orPaperContact.getFax());
        request.setAttribute("SendAuditId", sendAuditId);
        return "sendFaxToCus";
    }

    /**
     * 发送日审酒店传真
     * 
     * @return
     */

    public String sendAuditHotel() {

        // 操作结果
        int returnvalue = 0;
        // boolean isSendSucceed = false;
        Long ret = null;
        boolean bResult = false;
        user = super.getOnlineWorkStates();
        Calendar ca = Calendar.getInstance();
        Date createDate = ca.getTime();
        OrDailyAudit orDailyAudit = auditService.getOrDailyAudit(sendAuditId);
        List orPaperContacts = auditService.getOrPaperContact(orDailyAudit.getHotelId());
        OrPaperContact orPaperContact = (OrPaperContact) orPaperContacts.get(0);
        String faxNo = orPaperContact.getFax();
        if (!("".equals(fax))) {
            // 发送传真
            ret = sendAuditFax(sendAuditId, fax, orDailyAudit);
        } else {
            return forwardMsg("确认传真号码！");
        }

        // 数据库操作
        if (null != ret) {
            orDailyAudit.setFaxFile("" + ret);
            OrPaperDailyAudit aHotel = null;

            // OrDailyAudit orDailyAudit = auditService.getOrDailyAudit(sendAuditId);
            // OrPaperContact orPaperContact =
            // auditService.getOrPaperContact(orDailyAudit.getHotelId());

            List aHotels = auditService.findSimilarAudit(orDailyAudit);
            OrPaperDailyAuditItem orPaperDailyAuditItem = new OrPaperDailyAuditItem();
            if (0 >= aHotels.size()) {
                aHotel = new OrPaperDailyAudit();
                aHotel.setHotelId(orDailyAudit.getHotelId());
                aHotel.setAuditId(sendAuditId);
                if (0 != returnvalue) {
                    aHotel.setLostCount(1);
                    aHotel.setSuccessCount(0);
                } else {
                    aHotel.setLostCount(0);
                    aHotel.setSuccessCount(1);
                }
            } else {
                OrPaperDailyAudit isFaxNum = (OrPaperDailyAudit) aHotels.get(0);
                aHotel = auditService.getOrPaperDailyAudit(isFaxNum.getID());
                if (0 != returnvalue) {

                    aHotel.setLostCount(isFaxNum.getLostCount() + 1);
                    // orPaperDailyAuditItem.setSendId((long)isFaxNum.getLostCount());
                } else {
                    aHotel.setSuccessCount(isFaxNum.getSuccessCount() + 1);
                    // orPaperDailyAuditItem.setSendId((long)isFaxNum.getSuccessCount());
                }
            }
            aHotel.setFax(faxNo);
            aHotel.setCreatedBy(user.getLogonId());
            aHotel.setCreateDate(createDate);
            // 操作纸质日审明细
            orPaperDailyAuditItem.setHotelId(orDailyAudit.getHotelId());
            orPaperDailyAuditItem.setPaperAudit(aHotel);
            orPaperDailyAuditItem.setFax(aHotel.getFax());
            orPaperDailyAuditItem.setCreatedBy(user.getLogonId());
            orPaperDailyAuditItem.setCreateDate(DateUtil.getDate(createDate));
            orPaperDailyAuditItem.setSendState(returnvalue);
            // orPaperDailyAuditItem.setFaxId(""+ret);
            aHotel.getItems().add(orPaperDailyAuditItem);
            bResult = auditService.sendAuditFax(aHotel, orDailyAudit);
        }

        if (bResult) {
            return forwardMsgBox("发送传真成功！", "refreshSelf()");
        } else {
            return forwardMsgBox("发送传真失败！", "refreshSelf()");
        }
    }

    /**
     * 批量发送日审酒店传真
     * 
     * @return
     */
    public String sendAllAuditHotel() {
        // 操作结果
        int returnvalue = 0;
        boolean bResult = false;
        // boolean isSendSucceed = false;
        user = super.getOnlineWorkStates();
        Calendar ca = Calendar.getInstance();
        Date createDate = ca.getTime();
        OrPaperDailyAudit aHotel = null;
        Map params = super.getParams();
        selIDs = (String) params.get("selIDs");
        String[] ids = selIDs.split(",");
        selID = new Long[ids.length];
        for (int i = 0; i < selID.length; i++) {
            Long ret = null;
            selID[i] = Long.parseLong(ids[i]);
            OrDailyAudit orDailyAudit = auditService.getOrDailyAudit(selID[i]);
            // 获取该酒店确认的传真号码
            List orPaperContacts = auditService.getOrPaperContact(orDailyAudit.getHotelId());
            OrPaperContact orPaperContact = (OrPaperContact) orPaperContacts.get(0);
            String faxNo = orPaperContact.getFax();
            // 发送传真
            if (!("".equals(faxNo))) {
                ret = sendAuditFax(selID[i], faxNo, orDailyAudit);
                // Thread.sleep(5000);
            } else {
                return forwardMsg("请确认传真号码！");
            }
            if (null != ret) {
                orDailyAudit.setFaxFile("" + ret);
                OrPaperDailyAuditItem orPaperDailyAuditItem = new OrPaperDailyAuditItem();
                List aHotels = auditService.findSimilarAudit(orDailyAudit);
                if (0 >= aHotels.size()) {
                    aHotel = new OrPaperDailyAudit();
                    aHotel.setHotelId(orDailyAudit.getHotelId());
                    aHotel.setAuditId(selID[i]);
                    if (0 != returnvalue) {
                        aHotel.setLostCount(1);
                        aHotel.setSuccessCount(0);
                    } else {
                        aHotel.setLostCount(0);
                        aHotel.setSuccessCount(1);
                    }
                } else {
                    OrPaperDailyAudit isFaxNum = (OrPaperDailyAudit) aHotels.get(0);
                    aHotel = auditService.getOrPaperDailyAudit(isFaxNum.getID());
                    if (0 != returnvalue) {

                        aHotel.setLostCount(isFaxNum.getLostCount() + 1);
                        orPaperDailyAuditItem.setSendId((long) isFaxNum.getLostCount());
                    } else {

                        aHotel.setSuccessCount(isFaxNum.getSuccessCount() + 1);
                        orPaperDailyAuditItem.setSendId((long) isFaxNum.getSuccessCount());
                    }
                }
                aHotel.setFax(faxNo);
                aHotel.setCreatedBy(user.getLogonId());
                aHotel.setCreateDate(createDate);
                orPaperDailyAuditItem.setHotelId(orDailyAudit.getHotelId());
                orPaperDailyAuditItem.setPaperAudit(aHotel);
                orPaperDailyAuditItem.setFax(aHotel.getFax());
                orPaperDailyAuditItem.setCreatedBy(user.getLogonId());
                orPaperDailyAuditItem.setCreateDate(DateUtil.getDate(createDate));
                orPaperDailyAuditItem.setSendState(returnvalue);
                aHotel.getItems().add(orPaperDailyAuditItem);

                bResult = auditService.sendAuditFax(aHotel, orDailyAudit);
            } else {
                return forwardMsg(selID[i] + "发送传真失败！");
            }
        }
        if (0 >= selID.length) {
            return forwardError("请选择要发送的酒店!");
        }

        if (bResult) {
            return forwardMsgBox("发送传真成功！", "refreshSelf()");
        } else {
            return forwardMsgBox("发送传真失败！", "refreshSelf()");
        }

    }

    /**
     * 发送酒店传真次数--显示
     * 
     * @return
     */
    public String sendFaxNum() {
        sendFaxNum = auditService.getOrPaperDailyAuditItem(hotelId);
        return "send_Audit_FaxTime";
    }

    /**
     * 自动发送日审传真
     * 
     * @return
     */
    public String validateSendState() {
        OrParam orParam = systemDataService.getSysParamByName("IS_SEND_FAX");
        try {
            // request.setCharacterEncoding("utf-8");
            // String hotelName=request.getParameter("hotelName");
            new String(request.getParameter("hotelName").trim()
                .getBytes("ISO8859_1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
        	log.error(e.getMessage(),e);
        }
        user = super.getOnlineWorkStates();
        Map params = super.getParams();
        params.put("hotelName", hotelName);
        if (orParam.isSendFax()) {
            // List hotels = auditService.gethotels(params);
            // if(hotels.size()>0){
            orParam.setValue(IsAutomatismSendFax.SENDWORKING);
            systemDataService.updateSysParamByName(orParam);

            AutomatismSendFax asf = new AutomatismSendFax(params, user, orParam, auditService,
                communicaterService, systemDataService, msgAssist);
            asf.start();
            // }else{
            // return forwardMsgBox("没有可供发送的酒店传真！", "refreshSelf()");
            // }
        } else {
            return forwardMsgBox("传真已再发送中，请稍候再试！", "refreshSelf()");
        }
        return forwardMsgBox("发送传真成功！", "refreshSelf()");
    }

    public String faxLogList() {
        Map params = super.getParams();
        String auditDate = (String) params.get("auditDate");
        Date date = DateUtil.getDate(auditDate);
        auditLogList = auditService.getAuditLogList(date);
        return "faxLogList";
    }

    /**
     * 根据memberid获取会员简单信息 add by chenjiajie 2008-10-16
     * 
     * @param memberid
     * @return
     */
    protected MemberDTO getMemberSimpleInfo(String memberCd, boolean needCred) {
    	MemberDTO loginMember = null;
        try {
                loginMember = memberInterfaceService
                    .getMemberByCode(memberCd);
            return loginMember;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /** setter and getter */

    public IAuditService getAuditService() {
        return auditService;
    }

    public void setAuditService(IAuditService auditService) {
        this.auditService = auditService;
    }

    public long getAuditHotelId() {
        return auditHotelId;
    }

    public void setAuditHotelId(long auditHotelId) {
        this.auditHotelId = auditHotelId;
    }

    public Long[] getSelID() {
        return selID;
    }

    public void setSelID(Long[] selID) {
        this.selID = selID;
    }

    public List getAuditHotelList() {
        return auditHotelList;
    }

    public void setAuditHotelList(List auditHotelList) {
        this.auditHotelList = auditHotelList;
    }

    public List getAuditUsers() {
        return auditUsers;
    }

    public void setAuditUsers(List auditUsers) {
        this.auditUsers = auditUsers;
    }

    public List getAuditList() {
        return auditList;
    }

    public void setAuditList(List auditList) {
        this.auditList = auditList;
    }

    public AuditHotel getAuditHotel() {
        return auditHotel;
    }

    public void setAuditHotel(AuditHotel auditHotel) {
        this.auditHotel = auditHotel;
    }

    public long getAuditId() {
        return auditId;
    }

    public void setAuditId(long auditId) {
        this.auditId = auditId;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public List getDateStrs() {
        return dateStrs;
    }

    public void setDateStrs(List dateStrs) {
        this.dateStrs = dateStrs;
    }

    public List getAuditRows() {
        return auditRows;
    }

    public void setAuditRows(List auditRows) {
        this.auditRows = auditRows;
    }

    public String getAuditItemStr() {
        return auditItemStr;
    }

    public void setAuditItemStr(String auditItemStr) {
        this.auditItemStr = auditItemStr;
    }

    public void setHotelService(IHotelService hotelService) {
        this.hotelService = hotelService;
    }

    public long getSendAuditId() {
        return sendAuditId;
    }

    public void setSendAuditId(long sendAuditId) {
        this.sendAuditId = sendAuditId;
    }

    public long getLOSTCOUNT() {
        return LOSTCOUNT;
    }

    public void setLOSTCOUNT(long lostcount) {
        LOSTCOUNT = lostcount;
    }

    public long getSUCCESSCOUNT() {
        return SUCCESSCOUNT;
    }

    public void setSUCCESSCOUNT(long successcount) {
        SUCCESSCOUNT = successcount;
    }

    public String getSelIDs() {
        return selIDs;
    }

    public void setSelIDs(String selIDs) {
        this.selIDs = selIDs;
    }

    public List getUserList() {
        return userList;
    }

    public void setUserList(List userList) {
        this.userList = userList;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getHName() {
        return hName;
    }

    public void setHName(String name) {
        hName = name;
    }

    public Date getCheckNight() {
        return checkNight;
    }

    public void setCheckNight(Date checkNight) {
        this.checkNight = checkNight;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public List getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List orderItem) {
        this.orderItem = orderItem;
    }

    public List getHotels() {
        return hotels;
    }

    public void setHotels(List hotels) {
        this.hotels = hotels;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getNight() {
        return night;
    }

    public void setNight(String night) {
        this.night = night;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRState() {
        return rState;
    }

    public void setRState(String state) {
        rState = state;
    }

    public String getRStateNot() {
        return rStateNot;
    }

    public void setRStateNot(String stateNot) {
        rStateNot = stateNot;
    }

    public String getAuditItemNotStr() {
        return auditItemNotStr;
    }

    public void setAuditItemNotStr(String auditItemNotStr) {
        this.auditItemNotStr = auditItemNotStr;
    }

    public List getOrderItem_romm() {
        return orderItem_romm;
    }

    public void setOrderItem_romm(List orderItem_romm) {
        this.orderItem_romm = orderItem_romm;
    }

    public String getRID() {
        return rID;
    }

    public void setRID(String rid) {
        rID = rid;
    }

    public List getSendFaxNum() {
        return sendFaxNum;
    }

    public void setSendFaxNum(List sendFaxNum) {
        this.sendFaxNum = sendFaxNum;
    }

    public ArrayList getEditHotel() {
        return editHotel;
    }

    public void setEditHotel(ArrayList editHotel) {
        this.editHotel = editHotel;
    }

    public ArrayList getNewHotel() {
        return newHotel;
    }

    public void setNewHotel(ArrayList newHotel) {
        this.newHotel = newHotel;
    }

    public CommunicaterService getCommunicaterService() {
        return communicaterService;
    }

    public void setCommunicaterService(CommunicaterService communicaterService) {
        this.communicaterService = communicaterService;
    }

    public MsgAssist getMsgAssist() {
        return msgAssist;
    }

    public void setMsgAssist(MsgAssist msgAssist) {
        this.msgAssist = msgAssist;
    }

    public String getFaxConfirm() {
        return faxConfirm;
    }

    public void setFaxConfirm(String faxConfirm) {
        this.faxConfirm = faxConfirm;
    }

    public String getHOP_TMC_ORDER() {
        return HOP_TMC_ORDER;
    }

    public void setHOP_TMC_ORDER(String hop_tmc_order) {
        HOP_TMC_ORDER = hop_tmc_order;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String url) {
        URL = url;
    }

    public String getFAXFILE() {
        return FAXFILE;
    }

    public void setFAXFILE(String faxfile) {
        FAXFILE = faxfile;
    }

    public String getFAXID() {
        return FAXID;
    }

    public void setFAXID(String faxid) {
        FAXID = faxid;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getOrderAuditType() {
        return orderAuditType;
    }

    public void setOrderAuditType(int orderAuditType) {
        this.orderAuditType = orderAuditType;
    }

    public HraManager getHraManager() {
        return hraManager;
    }

    public void setHraManager(HraManager hraManager) {
        this.hraManager = hraManager;
    }

    public List getAuditLogList() {
        return auditLogList;
    }

    public void setAuditLogList(List auditLogList) {
        this.auditLogList = auditLogList;
    }

    public MemberInterfaceService getMemberInterfaceService() {
        return memberInterfaceService;
    }

    public void setMemberInterfaceService(MemberInterfaceService memberInterfaceService) {
        this.memberInterfaceService = memberInterfaceService;
    }

    public TranslateUtil getTranslateUtil() {
        return translateUtil;
    }

    public void setTranslateUtil(TranslateUtil translateUtil) {
        this.translateUtil = translateUtil;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<MGExDailyAudit> getDaList() {
        return daList;
    }

    public void setDaList(List<MGExDailyAudit> daList) {
        this.daList = daList;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public IHDLService getHdlService() {
        return hdlService;
    }

    public void setHdlService(IHDLService hdlService) {
        this.hdlService = hdlService;
    }
    
    public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}

}