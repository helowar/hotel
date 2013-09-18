package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.constant.ClaueType;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.ManyManage;
import com.mangocity.hotel.base.persistence.HtlBinding;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemBatch;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.bean.MyBeanUtil;

/**
 * 
 * 多条款绑定业务action
 * 
 * @author lihaibo
 * 
 */

public class ManyClauseAction extends PersistenceAction {
    private ManyManage manyManage;

    private long hotelId;// 酒店ID
    
    private String hotelChnName;
    
    private  HotelManage hotelManage;

    private long contractId;// 合同id

    private long modelid;// 预定条款id

    private List lstPriceType = new ArrayList();

    private List lstModel = new ArrayList();

    private Date beginDate;// 合同开始时间

    private Date endDate;// 合同结束时间

    private String reservationName;// 预定条款模板名称

    private List lstModelContent = new ArrayList();

    private int onlyNum;

    private List lstHtlBatch = new ArrayList();

    private static int countNum = 0;

    private List<HtlBinding> htlBinding = new ArrayList<HtlBinding>();

    private HtlPreconcertItemTemplet preconcert;

    private List<HtlContract> lstContract = new ArrayList<HtlContract>();

    private final String pweeks = "1,2,3,4,5,6,7,";

    /*
     * 用于页面跳转的常量
     * 
     * @see com.mangocity.util.webwork.PersistenceAction#getEntityClass()
     */
    private String FORWARD;

    @Override
    protected Class getEntityClass() {

        return HtlPreconcertItemTemplet.class;

    }

    public List getLstModelContent() {
        return lstModelContent;
    }

    public void setLstModelContent(List lstModelContent) {
        this.lstModelContent = lstModelContent;
    }

    public HtlPreconcertItemTemplet getPreconcert() {
        return preconcert;
    }

    public void setPreconcert(HtlPreconcertItemTemplet preconcert) {
        this.preconcert = preconcert;
    }

    public int getOnlyNum() {
        return onlyNum;
    }

    public void setOnlyNum(int onlyNum) {
        this.onlyNum = onlyNum;
    }

    // 跳转时查询预定条款和价格类型
    public String readForwoad() {
        lstPriceType = manyManage.queryPriceType(hotelId);
        // 获取该酒店下是否有价格类型，没有的话，跳转到提示页面；add by shengwei.zuo 2009-04-28 hotel V2.6
        if (null == lstPriceType || 0 == lstPriceType.size()) {

            FORWARD = "roomTypeIsNull";
            return FORWARD;

        }

        lstModel = manyManage.queryModel(hotelId);
        // 获取该酒店下是否有预定模板，没有的话，跳转到提示页面；add by shengwei.zuo 2009-04-28 hotel V2.6
        /*
         * if(lstModel==null||lstModel.size()==0){
         * 
         * FORWARD="TmpltIsNull"; return FORWARD;
         * 
         * }
         */

        lstContract = manyManage.queryHtlContract(contractId);
        lstHtlBatch = manyManage.queryHtlBatch(hotelId);
        countNum = 0;
        for (int i = 0; i < lstContract.size(); i++) {
            HtlContract ht = lstContract.get(i);
            beginDate = ht.getBeginDate();
            endDate = ht.getEndDate();
        }
        /*************************add by yong.zeng************************************/
    	String changePriceHint = "";
    	HtlHotel curhotel = hotelManage.findHotel(hotelId);
    	if(null!=curhotel){
        hotelChnName = curhotel.getChnName();
        
        changePriceHint = curhotel.getChangePriceHint();
    	}
        request.setAttribute("changePriceHint", changePriceHint);
        

        FORWARD = "manyForword";
        return FORWARD;
    }

    // 下拉预定条款查询内容
    public String queryModelAll() {
    	/*************************add by yong.zeng************************************/
    	String changePriceHint = "";
    	HtlHotel curhotel = hotelManage.findHotel(hotelId);
    	if(null!=curhotel){
        hotelChnName = curhotel.getChnName();
        
        changePriceHint = curhotel.getChangePriceHint();
    	}
        request.setAttribute("changePriceHint", changePriceHint);
        
        
        super.setEntity(super.populateEntity());
        this.setPreconcert((HtlPreconcertItemTemplet) this.getEntity());
        Map params = super.getParams();
        htlBinding = MyBeanUtil.getBatchObjectFromParam(params, HtlBinding.class, onlyNum);
        onlyNum = htlBinding.size();
        countNum = htlBinding.size();
        lstModel = manyManage.queryModel(hotelId);
        lstPriceType = manyManage.queryPriceType(hotelId);
        lstModelContent = manyManage.quertHotelAjaxModel(modelid, hotelId);
        lstContract = manyManage.queryHtlContract(contractId);
        lstHtlBatch = manyManage.queryHtlBatch(hotelId);
        for (int i = 0; i < lstContract.size(); i++) {
            HtlContract ht = lstContract.get(i);
            beginDate = ht.getBeginDate();
            endDate = ht.getEndDate();
        }
        return "manyForword";
    }

    // 预览跳转
    public String reviewForword() {
        super.setEntity(super.populateEntity());
        this.setPreconcert((HtlPreconcertItemTemplet) this.getEntity());
        Map params = super.getParams();
        htlBinding = MyBeanUtil.getBatchObjectFromParam(params, HtlBinding.class, onlyNum);
        lstModel = manyManage.queryModel(hotelId);
        lstPriceType = manyManage.queryPriceType(hotelId);
        super.putSession("htlBindingTwo", htlBinding);
        return "viewForword";
    }

    @Override
    public String save() {
        super.setEntity(super.populateEntity());
        this.setPreconcert((HtlPreconcertItemTemplet) this.getEntity());
        htlBinding = (List) super.getFromSession("htlBindingTwo");
        for (int i = 0; i < htlBinding.size(); i++) {
            HtlBinding hb = htlBinding.get(i);
            for (int j = 0; j < hb.getPricetypes().length; j++) {
                HtlPreconcertItemBatch htlPreconcertItemBatch = new HtlPreconcertItemBatch();
                if (null != hb.getTiaokuan() && !hb.getTiaokuan().equals("")) { // 如果选择了条款
                    List list = manyManage.queryModelOnly(Long.valueOf(hb.getTiaokuan()));
                    HtlPreconcertItemTemplet pt = (HtlPreconcertItemTemplet) list.get(0);
                    htlPreconcertItemBatch.setReservationName(pt.getReservationName());
                    htlPreconcertItemBatch.setReservationTemplateId(Long.valueOf(hb.getTiaokuan()));
                }
                htlPreconcertItemBatch.setHotelId(hotelId);
                htlPreconcertItemBatch.setContractId(contractId);
                if (null != hb.getPricetypes()[j] && !hb.getPricetypes()[j].equals("")) {
                    htlPreconcertItemBatch.setPriceTypeId(Long.valueOf(hb.getPricetypes()[j]));
                }
                htlPreconcertItemBatch.setPayToPrepay(hb.getPaytoprepay());
                if (hb.getTactive().equals(ClaueType.BINDING_TRUE)) {
                    htlPreconcertItemBatch.setActive(ClaueType.BINDING_TRUE);// 是否绑定标志
                    // 1
                    // 为绑定
                    if (null != super.getOnlineRoleUser()) {
                        // 增加创建人..创建时间
                        htlPreconcertItemBatch.setCreateBy(
                            super.getOnlineRoleUser().getName());// 创建人工号
                        htlPreconcertItemBatch.setCreateById(super.getOnlineRoleUser()
                            .getLoginName());// 创建人名称
                        htlPreconcertItemBatch.setCreateTime(DateUtil.getSystemDate()); // 创建时间

                        // add by shengwei.zuo 2009-04-14 用于操作日志的查询 begin
                        // 增加修改人，修改时间
                        htlPreconcertItemBatch.setModifyBy(super.getOnlineRoleUser().getName());
                        htlPreconcertItemBatch.setModifyById(super.getOnlineRoleUser()
                            .getLoginName());
                        htlPreconcertItemBatch.setModifyTime(DateUtil.getSystemDate());

                        // add by shengwei.zuo 2009-04-14 用于操作日志的查询 end

                    } else {
                        super.getSession().remove("htlBindingTwo");// 清除session中的数据
                        super.clearSession();
                        return super.forwardError("获取登陆用户信息失效,请重新登陆");
                    }

                }
                if (hb.getTactive().equals(ClaueType.BINDING_FALSE)) {
                    htlPreconcertItemBatch.setActive(ClaueType.BINDING_FALSE);// 0为解除绑定
                    htlPreconcertItemBatch.setCreateTime(DateUtil.getSystemDate()); // 创建时间
                    if (null != super.getOnlineRoleUser()) {
                        // 增加创建人..创建时间
                        // htlPreconcertItemBatch.setCreateBy
                        // (super.getOnlineRoleUser().getName());//创建人工号
                        // htlPreconcertItemBatch.setCreateById
                        // (super.getOnlineRoleUser().getLoginName());//创建人名称
                        htlPreconcertItemBatch.setDelBy(
                            super.getOnlineRoleUser().getName());// 删除人工号
                        htlPreconcertItemBatch.setDelById(
                            super.getOnlineRoleUser().getLoginName());// 删除人名称
                        htlPreconcertItemBatch.setDelTime(DateUtil.getSystemDate());// 删除时间

                        // add by shengwei.zuo 2009-04-14 用于操作日志的查询 begin
                        // 增加修改人，修改时间
                        htlPreconcertItemBatch.setModifyBy(super.getOnlineRoleUser().getName());
                        htlPreconcertItemBatch.setModifyById(super.getOnlineRoleUser()
                            .getLoginName());
                        htlPreconcertItemBatch.setModifyTime(DateUtil.getSystemDate());

                        // add by shengwei.zuo 2009-04-14 用于操作日志的查询 end

                    } else {
                        super.getSession().remove("htlBindingTwo");// 清除session中的数据
                        super.clearSession();
                        return super.forwardError("获取登陆用户信息失效,请重新登陆");
                    }

                }
                // htlPreconcertItemBatch.setReservationName(hb.getReservationName());
                htlPreconcertItemBatch.setBeginDate(hb.getBeginDate());// 开始时间
                htlPreconcertItemBatch.setEndDate(hb.getEndDate());// 结束时间
                htlPreconcertItemBatch.setDoubletofalg(ClaueType.CLAUS_MANY);// 多条款标志
                if (null == hb.getWeeks()) {// 如果页面没有设置星期，默认为7天全选
                    hb.setWeeks(pweeks);
                    htlPreconcertItemBatch.setWeeks(hb.getWeeks());// 设置星期
                } else {
                    htlPreconcertItemBatch.setWeeks(hb.getWeeks());// 设置星期
                }
                try {
                    boolean falg = manyManage.saveOrupdateAll(htlPreconcertItemBatch);
                    long id = htlPreconcertItemBatch.getId();
                    if (falg) {
                    	log.info("多条款插入批次表中，成功:"
								+ htlPreconcertItemBatch.getModify_time()
								+ "," + hotelId + "," + id + "," + ","
								+ contractId + "," + hb.getPricetypes()[j]
								+ "," + hb.getBeginDate() + ","
								+ hb.getEndDate() + "," + hb.getTactive());
                    	
                        boolean falgTwo = manyManage.saveOrupdatePro(hotelId, id, contractId, Long
                            .valueOf(hb.getPricetypes()[j]), hb.getBeginDate(), hb.getEndDate(), hb
                            .getTactive());
                        if (falgTwo) {
                        	log.info("多条款调用存储过程,成功"+htlPreconcertItemBatch.getModify_time());
                            super.getSession().remove("htlBindingTwo");// 清除session中的数据
                            super.clearSession();
                        } else {
                        	log.info("多条款调用存储过程,失败"+htlPreconcertItemBatch.getModify_time());
                            // 保存不成功也要清楚session
                            super.getSession().remove("htlBindingTwo");// 清除session中的数据
                            super.clearSession();
                        }
                    }else{
                    	log.info("多条款插入批次表中，失败:"+ htlPreconcertItemBatch.getModify_time());
                    }

                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                    super.forwardError("插入失败");
                }

            }
        }
        return "SuccessForword";
    }

    // 返回方法
    public String back() {
        htlBinding = (List) super.getFromSession("htlBindingTwo");
        if (null != htlBinding && 1 == htlBinding.size() && 0 == onlyNum) {
            onlyNum += 1;
        }
        lstContract = manyManage.queryHtlContract(contractId);
        lstHtlBatch = manyManage.queryHtlBatch(hotelId);
        lstPriceType = manyManage.queryPriceType(hotelId);
        lstModel = manyManage.queryModel(hotelId);
        for (int i = 0; i < lstContract.size(); i++) {
            HtlContract ht = lstContract.get(i);
            beginDate = ht.getBeginDate();
            endDate = ht.getEndDate();
        }
        super.getSession().remove("htlBindingTwo");
        super.clearSession();
        return "manyForword";
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public List getLstModel() {
        return lstModel;
    }

    public void setLstModel(List lstModel) {
        this.lstModel = lstModel;
    }

    public List getLstPriceType() {
        return lstPriceType;
    }

    public void setLstPriceType(List lstPriceType) {
        this.lstPriceType = lstPriceType;
    }

    public ManyManage getManyManage() {
        return manyManage;
    }

    public void setManyManage(ManyManage manyManage) {
        this.manyManage = manyManage;
    }

    public List<HtlBinding> getHtlBinding() {
        return htlBinding;
    }

    public void setHtlBinding(List<HtlBinding> htlBinding) {
        this.htlBinding = htlBinding;
    }

    public long getModelid() {
        return modelid;
    }

    public void setModelid(long modelid) {
        this.modelid = modelid;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public static int getCountNum() {
        return countNum;
    }

    public static void setCountNum(int countNum) {
        ManyClauseAction.countNum = countNum;
    }

    public List<HtlContract> getLstContract() {
        return lstContract;
    }

    public void setLstContract(List<HtlContract> lstContract) {
        this.lstContract = lstContract;
    }

    public List getLstHtlBatch() {
        return lstHtlBatch;
    }

    public void setLstHtlBatch(List lstHtlBatch) {
        this.lstHtlBatch = lstHtlBatch;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }

    public String getFORWARD() {
        return FORWARD;
    }

    public void setFORWARD(String forward) {
        FORWARD = forward;
    }

	public String getHotelChnName() {
		return hotelChnName;
	}

	public void setHotelChnName(String hotelChnName) {
		this.hotelChnName = hotelChnName;
	}

	public HotelManage getHotelManage() {
		return hotelManage;
	}

	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

}
