package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mangocity.hotel.base.dao.IContractDao;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.assistant.InputPriceType;
import com.mangocity.hotel.base.persistence.CEntityEvent;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.HtlAddBedPrice;
import com.mangocity.hotel.base.persistence.HtlChargeBreakfast;
import com.mangocity.hotel.base.persistence.HtlChildWelcomePrice;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlInternet;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.persistence.HtlWelcomePrice;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.base.util.SendLuceneMQ;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.HotelMappingType;

/**
 */
public class RoomTypeAction extends PersistenceAction {
    private Long hotelID;

    private String FORWARD;

    private IContractDao contractDao;

    private String roomTypeID;

    private List roomTypes = new ArrayList();

    private String List = "list";

    private String Select = "select";

    private String[] roomChildRoomType;

    private String[] bedType;
    
    private ContractManage contractManage;

    private List bedPriceList = new ArrayList();

    private List internetList = new ArrayList();

    private List welcomeList = new ArrayList();

    private List breakfastList = new ArrayList();

    private HotelManage hotelManage;
    
    private ResourceManager resourceManager;                     

    /**
     * 持久化实体
     */
    private Object entity;

    // 接送详细信息数据
    private List priceTypeList;
    
    // 选择的渠道值列表值数组
    private String []selChannels;
    
    // 渠道显示数组，只用于查看合同显示渠道
    private String []viewChannels;
    
    //多渠道预定中渠道默认值，与数据库中默认值相同，只有当数据库中值为空值时才使用
    private String defaultSaleChn = "4294967295";

    // 详细信息行数
    private int priceTypeNum;

    // 批量查询
    private static final String BATCH_QUERY = "batchQuery";
    
    private String remark;
    
    // 发送MQ功能 add by chenkeming
    private SendLuceneMQ sendLuceneMQ;
    
    //请求来源。
    private String requestChannel;

    protected Class getEntityClass() {
        return HtlRoomtype.class;
    }

    public String save() {
        HtlRoomtype roomtype = (HtlRoomtype) super.populateEntity();
        
        new BizRuleCheck();
        roomtype.setActive(BizRuleCheck.getTrueString());
        if (null == roomtype.getID() || 0 == roomtype.getID()) {
            roomtype.setActive(BizRuleCheck.getTrueString());
            if (null == roomtype.getID() || 0 == roomtype.getID()) {
                if (null != super.getOnlineRoleUser()) {
                    roomtype.setCreateBy(super.getOnlineRoleUser().getName());
                }
                roomtype.setCreateTime(new Date());
            }
        }
        if (null != super.getOnlineRoleUser()) {
            roomtype.setModifyBy(super.getOnlineRoleUser().getName());
        }
        roomtype.setModifyTime(new Date());
        
        roomtype.setBedType(BizRuleCheck.ArrayToString(bedType));
        List ls = MyBeanUtil.getBatchObjectFromParam(super.getParams(), InputPriceType.class,
            priceTypeNum);
        List lsprice = new ArrayList();
        for (int i = 0; i < roomtype.getLstPriceType().size(); i++) {
            lsprice.add(roomtype.getLstPriceType().get(i));
        }
        
        roomtype.getLstPriceType().clear();

        int length = 0;
        int len = 0;
        StringBuilder sb = null;
        int lsSize = ls.size();
        ////从配置文件中读取复选框列表的Map值
        Map<?, ?> map = resourceManager.getDescription("priceTypeMultiChannel");  
        for (int m = 0; m < lsSize; m++) {
            InputPriceType inputPriceType = (InputPriceType) ls.get(m);
            HtlPriceType priceType = new HtlPriceType();
            priceType.setRoomType(roomtype);
            
            /** 多渠道预定 added by xieyanhui 2011-07-28begin **/
            String [] channelValue = inputPriceType.getMultiChannel();
            String preMultiChannel = inputPriceType.getPreMmultiChannel();
            
            
             //为空表示新增加，设为默认值4294967295
            if(null == preMultiChannel || "".equals(preMultiChannel)){//32位的二进制数的十进制值
        	  preMultiChannel = defaultSaleChn;
            } 
          
            // 价格类型中渠道的二进制字符串
        	sb = new StringBuilder(Long.toBinaryString(StringUtil.getStrTolong(preMultiChannel)));
			len = sb.length();

			// 复选框所对应的数字与渠道的map键值对,初始化为0
			Map<String, String> selChnMap = new HashMap<String, String>();
			if (0 < map.size()) {
				Set<?> keys = map.keySet();
				for (Iterator<?> it = keys.iterator(); it.hasNext();) {
					selChnMap.put((String) it.next(), "0");
				}
			}
			if(null != channelValue){//如果所有选项都没有选择时，channelValue为null值
				length = channelValue.length;
				// 如果选中，则selChnMap中相应的key的值设为1
				for (int i = 0; i < length; i++) {
					selChnMap.put(channelValue[i], "1");
				} 
			}
			
			//组装渠道二进制数字符串
        	if (0 < selChnMap.size()) {
				Set keys = selChnMap.keySet();
				for (Iterator it = keys.iterator(); it.hasNext();) {
					String key = (String) it.next();
					Double value = StringUtil.getStrTodouble(key);
					value = Math.log(value)/Math.log(2);
	            	if(len > value.intValue() && value.intValue() >= 0){
	            		sb.replace(len-value.intValue()-1, len-value.intValue(), (String)selChnMap.get(key));
	            	}
				}
			}
        	priceType.setMulti_channel(StringUtil.getBinaryStrToLong(sb.toString()));
        	
            if(null != inputPriceType.getSupplierID() && !"".equals(inputPriceType.getSupplierID())){
            	priceType.setSupplierID(Long.valueOf(inputPriceType.getSupplierID()));
            }
            
            if(null!=inputPriceType.getShowMemberPrice()&&"1".equals(inputPriceType.getShowMemberPrice())){
            	priceType.setShowMemberPrice(true);
            }
            else{
            	priceType.setShowMemberPrice(false);
            }
        	 /** 多渠道预定 added by xieyanhui 2011-07-28end **/
            
            priceType.setID(inputPriceType.getKeyId());
            priceType.setPriceType(inputPriceType.getPriceTypeName());
            if (null != super.getOnlineRoleUser()) {
                priceType = (HtlPriceType) CEntityEvent.setCEntity(priceType, super
                    .getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());
            }
            roomtype.getLstPriceType().add(priceType);
        }

        /** 房型映射修改芒果网房型，同步映射表信息 add by chenjiajie 2008-12-04 V2.5 begin **/
        List primaryMappingList = hotelManage.findHotelMapping(hotelID, new long[] {
            HotelMappingType.ROOM_TYPE, HotelMappingType.PRICE_TYPE });
        List objToSave = new ArrayList(); // 保存批量保存的对象
        for (Iterator it = primaryMappingList.iterator(); it.hasNext();) {
            ExMapping em = (ExMapping) it.next();
            if (em.getRoomTypeId().equals(roomtype.getID().toString())) {
                em.setRoomtypecode(roomtype.getRoomTypeNo());
                em.setRoomtypenameForMango(roomtype.getRoomName());
                // 价格类型变化同步到ex_mapping表中,add by diandian.hou 2010-10-17
                List LstPriceType = roomtype.getLstPriceType();
                if(null != LstPriceType){
                     for(Iterator itPriceTypes=LstPriceType.iterator();itPriceTypes.hasNext();){
                    	 HtlPriceType priceType = (HtlPriceType) itPriceTypes.next();
                    	 // 判断2个表中的价格类型是否相同 
                    	 if(null == priceType.getID() || null == em.getPriceTypeId()){
                    		 continue;
                    	 }
                    	 boolean flag = priceType.getID().toString().equals(em.getPriceTypeId());
                    	 if(flag){
                               em.setPricetypename(priceType.getPriceType());
                    	  } 
                     }	 
                }
                objToSave.add(em);
            }
        }

        /**
         * 生产bug394系统外订单增加不了房型，新增系统外酒店不存在映射，判断objToSave则不会保存 modify by chenjiajie 2009-04-28 begin
         **/
        if (!objToSave.isEmpty()) {
            // 批量保存
            super.getEntityManager().saveOrUpdateAll(objToSave);
        }
        /** 生产bug394 modify by chenjiajie 2009-04-28 end **/

        if (null != FORWARD && FORWARD.equals("roomTypeForMapping")) {
            hotelManage.modifyRoomType(roomtype);
            FORWARD = "editRoomTypeForMapping";            
        } else if (null != FORWARD && FORWARD.equals("sysEditRoom")) {
            hotelManage.createRoomType(roomtype);
            FORWARD = "sysEditRoom";
        } else if (null != FORWARD && FORWARD.equals("sysUpRoom")) {
            hotelManage.modifyRoomType(roomtype);
            FORWARD = "afterSysUpRoom";
        } 
        if ("editRoomTypeForMapping".equals(FORWARD)
				|| "sysEditRoom".equals(FORWARD)
				|| "afterSysUpRoom".equals(FORWARD)) {

        	if(!"HOP".equals(requestChannel)) {
        		// 增加发送mq信息 add by chenkeming
            	sendLuceneMQ.send("hotelInfo#" + hotelID);
        	}
			return FORWARD;
		}
        /** 房型映射修改芒果网房型，同步映射表信息 add by chenjiajie 2008-12-04 V2.5 end * */
        
        
        super.getEntityManager().saveOrUpdate(roomtype);
        hotelManage.updateRoomState(roomtype.getID(), roomtype.getBedType());
        
    	if(!"HOP".equals(requestChannel)) {
    		// 增加发送mq信息 add by chenkeming
        	sendLuceneMQ.send("hotelInfo#" + hotelID);
    	}
        
        return SAVE_SUCCESS;
    }

    /**
     * 真删除
     * 
     * @return
     */
    public String deletePrice() {
        hotelManage.delHtlPrice(super.getEntityID());
        // hotelManage.deleteAllRoomType(super.getEntityID());
        return SAVE_SUCCESS;
    }

    /**
     * 查看
     * 
     * @return
     */
    public String create() {
        // HtlRoomtype roomtype = (HtlRoomtype) super.populateEntity();
        entity = super.populateEntity();
        /*
         * modify by alfred
         * 对来自于cc的请求，不发送mq消息
         */
        if(!"HOP".equals(requestChannel)) {
            // 增加发送mq信息 add by chenkeming
            sendLuceneMQ.send("hotelInfo#" + hotelID);
        }
        if (null != FORWARD && FORWARD.equals("sysEditRoom")) {
            return "syscreate";
        }
        return CREATE;
    }

    /**
     * 查看
     * 
     * @return
     */
    public String view() {
        if (null == super.getEntityID()) {
            String error = "entityID不能为空!，请传入entityID参数!";

            return super.forwardError(error);
        }
        super.setEntity(super.populateEntity());

        if (null == super.getEntity()) {
            String error = "找不到实体对象,id:" + super.getEntityID() + "; 类：" + getEntityClass();
            return super.forwardError(error);
        }

        super.setEntityForm(populateFormBean(super.getEntity()));
        super.getEntityForm();
        this.setEntity(super.getEntity());
        /** 多渠道预定 added by xieyanhui 2011-07-28begin **/
        setMultiChannels();
        /** 多渠道预定 added by xieyanhui 2011-07-28 end **/
        if("viewDetailOfRoomType".equals(FORWARD)){
        	/** 多渠道预定查看合同中显示渠道 added by xieyanhui 2011-08-12begin **/
        	Map<?, ?> map = resourceManager.getDescription("priceTypeMultiChannel");
        	StringBuilder sb = null;
        	int selChnSize = selChannels.length;
        	viewChannels = new String[selChnSize];
        	for(int i = 0 ; i <  selChnSize; i++){
        		sb = new StringBuilder();
        		if(null != selChannels[i] && !"".equals(selChannels[i]) ){
            		String []chns = selChannels[i].split(",");
            		for(String chn : chns){
            			sb.append(map.get(chn)).append("、");
            		}
            	   viewChannels[i] = sb.deleteCharAt(sb.length()-1).toString();
        		}else{
        			viewChannels[i] = "无";
        		}
        		
        	}
        	/** 多渠道预定查看合同中显示渠道 added by xieyanhui 2011-08-12end **/
        	return "viewDetailOfRoomType";
        }
        return VIEW;
    }

	@Override
    public String edit() {
        if (null != FORWARD && FORWARD.equals("queryRoomTypeForCC")) {
            if (null == super.getEntity()) {
                super.setEntity(populateEntity());
            }
            entity = super.getEntity();
            super.setEntityForm(populateFormBean(super.getEntity()));
            HtlRoomtype roomtype = (HtlRoomtype) super.getEntity();
            String str0 = roomtype.getRemark();
            String temp0 = new String();
            if (null != str0) {
                temp0 = str0.replace("\r\n", "");
            }
            roomtype.setRemark(temp0);
            // roomtype.setModifyBy(super.getOnlineRoleUser().getName());
            // roomtype.setModifyTime(DateUtil.getDate(new Date()));

            // CC查看相关时间段内的加早价、加床价、接送价、宽带收费情况 Modified by Wuyun
            Map params = super.getParams();
            Date beginDate = DateUtil.getDate((String) params.get("beginDate"));
            Date endDate = DateUtil.getDate((String) params.get("endDate"));
            hotelID = Long.parseLong((String) params.get("hotelId"));
            long roomTypeId = Long.parseLong((String) params.get("entityID"));
            HtlContract contract = contractManage.checkContractDateNew(hotelID, beginDate);
            if (null != contract) {
                List alist = contract.getHtlAddBedPrices();
                if (null != alist) {
                    for (int i = 0; i < alist.size(); i++) {
                        HtlAddBedPrice bedprice = (HtlAddBedPrice) alist.get(i);
                        if (null != bedprice.getRoomType()
                            && roomTypeId == Long.parseLong(bedprice.getRoomType())) {
                            if ((beginDate.after(bedprice.getBeginDate()) && beginDate
                                .before(bedprice.getEndDate()))
                                || (endDate.after(bedprice.getBeginDate()) && endDate
                                    .before(bedprice.getEndDate()))
                                || (beginDate.before(bedprice.getBeginDate()) && endDate
                                    .after(bedprice.getEndDate()))) {
                                bedPriceList.add(bedprice);
                            }
                        }
                    }
                }
                alist = contract.getHtlInternet();
                if (null != alist) {
                    for (int j = 0; j < alist.size(); j++) {
                        HtlInternet internet = (HtlInternet) alist.get(j);
                        if (roomTypeId == internet.getRoomTypeId()) {
                            if ((beginDate.after(internet.getInternetBeginDate()) && beginDate
                                .before(internet.getInternetEndDate()))
                                || (endDate.after(internet.getInternetBeginDate()) && endDate
                                    .before(internet.getInternetEndDate()))
                                || (beginDate.before(internet.getInternetBeginDate()) && endDate
                                    .after(internet.getInternetEndDate()))) {
                                internetList.add(internet);
                            }
                        }
                    }
                }
                alist = contract.getHtlChargeBreakfasts();
                if (null != alist) {
                    for (int j = 0; j < alist.size(); j++) {
                        HtlChargeBreakfast breakfast = (HtlChargeBreakfast) alist.get(j);
                        if ((beginDate.after(breakfast.getBeginDate()) && beginDate
                            .before(breakfast.getEndDate()))
                            || (endDate.after(breakfast.getBeginDate()) && endDate.before(breakfast
                                .getEndDate()))
                            || (beginDate.before(breakfast.getBeginDate()) && endDate
                                .after(breakfast.getEndDate()))) {
                            breakfastList.add(breakfast);
                        }
                    }
                }
                alist = contract.getHtlWelcomePrices();
                if (null != alist) {
                    for (int j = 0; j < alist.size(); j++) {
                        HtlWelcomePrice welcome = (HtlWelcomePrice) alist.get(j);
                        if ((beginDate.after(welcome.getBeginDate()) && beginDate.before(welcome
                            .getEndDate()))
                            || (endDate.after(welcome.getBeginDate()) && endDate.before(welcome
                                .getEndDate()))
                            || (beginDate.before(welcome.getBeginDate()) && endDate.after(welcome
                                .getEndDate()))) {
                            welcomeList.add(welcome);
                            List child = welcome.getWelcomeFees();
                            for (int m = 0; m < child.size(); m++) {
                                HtlChildWelcomePrice childprice = (HtlChildWelcomePrice) child
                                    .get(m);
                                String str = childprice.getMemo();
                                String temp = new String();
                                if (null != str) {
                                    temp = str.replace("\r\n", "");
                                }
                                childprice.setMemo(temp);
                            }

                        }
                    }
                }
            }
            //			
            return FORWARD;
        }
        if (null != FORWARD && FORWARD.equals("sysUpRoom")) {
            if (null == super.getEntity()) {
                super.setEntity(populateEntity());
            }
            entity = super.getEntity();
            super.setEntityForm(populateFormBean(super.getEntity()));
            return FORWARD;
        }
        /** 房型映射点击修改进入芒果网房型修改页面 add by chenjiajie 2008-12-04 V2.5 begin **/
        if (null != FORWARD && FORWARD.equals("roomTypeForMapping")) {
            if (null == super.getEntity()) {
                super.setEntity(populateEntity());
            }
            entity = super.getEntity();
            super.setEntityForm(populateFormBean(super.getEntity()));
            /** 多渠道预定 added by xieyanhui 2011-07-28begin **/
            setMultiChannels();
            /** 多渠道预定 added by xieyanhui 2011-07-28 end **/
            return FORWARD;
        }
        /** 房型映射点击修改进入芒果网房型修改页面 add by chenjiajie 2008-12-04 V2.5 end **/
        if (null == super.getEntity()) {
            super.setEntity(populateEntity());
        }
        HtlRoomtype roomtype = (HtlRoomtype) super.getEntity();
        if (null != super.getOnlineRoleUser()) {
            roomtype.setModifyBy(super.getOnlineRoleUser().getName());
        }
        roomtype.setModifyTime(DateUtil.getDate(new Date()));
        super.setEntity(roomtype);
        super.edit();
        this.setEntity(super.getEntity());       
        /** 多渠道预定 added by xieyanhui 2011-07-28begin **/
        setMultiChannels();
        /** 多渠道预定 added by xieyanhui 2011-07-28 end **/
        return SAVE_SUCCESS;
    }
	
	private void setMultiChannels(){
        ////从配置文件中读取复选框列表的Map值
        Map<?, ?> map = resourceManager.getDescription("priceTypeMultiChannel");  
        //得到数据库中的值，组装成以逗号分开的字符串
        List<HtlPriceType> ptList = ((HtlRoomtype)entity).getLstPriceType();
        if(!ptList.isEmpty()){
            int priceTypeSize = ptList.size();
            selChannels = new String[priceTypeSize];
            for (int m = 0; m < priceTypeSize; m++) {
            	HtlPriceType priceType = ptList.get(m);
            	System.out.println("++++++++++++++++++"+priceType.getMulti_channel()+"++++++++++++++++++++");
            	long longPT = 0;
            	if(null == priceType.getMulti_channel() || "".equals(priceType.getMulti_channel())){
            		longPT = StringUtil.getStrTolong(defaultSaleChn);
            		priceType.setMulti_channel(longPT);
            	}else{
            		longPT = priceType.getMulti_channel();
            	}
            	StringBuilder sb = new StringBuilder(Long.toBinaryString(longPT));
            	StringBuilder changedMultiChn = new StringBuilder();
            	int length = sb.length();
                if (0 < map.size()) {
    				Set keys = map.keySet();
    				for (Iterator it = keys.iterator(); it.hasNext();) {
    					String key = (String) it.next();
    					Double value = StringUtil.getStrTodouble(key);
    					Double value1 = Math.log(value)/Math.log(2);
    					if (length > value1.intValue() && value1.intValue() >= 0) {
    						changedMultiChn.append(sb.charAt(length-value1.intValue()-1) == '1' ? value.intValue()+"," : "");
    					}
    				}
    				if(changedMultiChn.length()>0){
    					changedMultiChn.substring(0,changedMultiChn.length()-2);//去掉最后一个空格
    					String str = changedMultiChn.toString();
    					str = str.substring(0, str.length()-1);
    					selChannels[m] = str;
    				}                  
    			}
            }
        } 
	}

    public String saveFor() {

        this.save();

        return FORWARD;
    }

    public String delFor() {

        // super.delete();
    	
        hotelManage.deleteRoomType(super.getEntityID());        
        
        return FORWARD;
    }

    /**
     * 真删除
     * 
     * @return
     */
    public String delete() {

        hotelManage.deleteAllRoomType(super.getEntityID());

        hotelManage.deleteAllMappingRoomType(super.getEntityID());
        
        // 增加发送mq信息 add by chenkeming
        sendLuceneMQ.send("hotelInfo#" + hotelID);
        
        return DELETED;
    }

    public String list() {

        if (null == hotelID)
            super.forwardError("hotelID不能为空!");

        roomTypes = contractDao.getRoomTypes(hotelID);

        return List;
    }
    
    // add by diandian.hou 
    public String viewRoomTypeInContract(){
    	 if (null == hotelID)
             super.forwardError("hotelID不能为空!");
         roomTypes = contractDao.getRoomTypes(hotelID);
         return "viewRTInContract";
    }

    public String select() {// parasoft-suppress NAMING.GETA "STRUTS命名没有问题"

        return Select;
    }
    
    public String selectRoom(){
    	return "selectRoom";
    }

    public Long getHotelID() {
        return hotelID;
    }

    public void setHotelID(Long hotelID) {
        this.hotelID = hotelID;
    }

    public List getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List roomTypes) {
        this.roomTypes = roomTypes;
    }

    public IContractDao getContractDao() {
        return contractDao;
    }

    public void setContractDao(IContractDao contractDao) {
        this.contractDao = contractDao;
    }

    public String[] getRoomChildRoomType() {
        return roomChildRoomType;
    }

    public void setRoomChildRoomType(String[] roomChildRoomType) {
        this.roomChildRoomType = roomChildRoomType;
    }

    public String getList() {
        return List;
    }

    public void setList(String list) {
        List = list;
    }

    public String getSelect() {
        return Select;
    }

    public void setSelect(String select) {
        Select = select;
    }

    public String getFORWARD() {
        return FORWARD;
    }

    public void setFORWARD(String forward) {
        FORWARD = forward;
    }

    public String getRoomTypeID() {
        return roomTypeID;
    }

    public void setRoomTypeID(String roomTypeID) {
        this.roomTypeID = roomTypeID;
    }

    public String[] getBedType() {
        return bedType;
    }

    public void setBedType(String[] bedType) {
        this.bedType = bedType;
    }

    public String[] getSelChannels() {
		return selChannels;
	}

	public void setSelChannels(String[] selChannels) {
		this.selChannels = selChannels;
	}

	public List getPriceTypeList() {
        return priceTypeList;
    }

    public void setPriceTypeList(List priceTypeList) {
        this.priceTypeList = priceTypeList;
    }

    public int getPriceTypeNum() {
        return priceTypeNum;
    }

    public void setPriceTypeNum(int priceTypeNum) {
        this.priceTypeNum = priceTypeNum;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public ContractManage getContractManage() {
        return contractManage;
    }

    public void setContractManage(ContractManage contractManage) {
        this.contractManage = contractManage;
    }

    public List getBedPriceList() {
        return bedPriceList;
    }

    public void setBedPriceList(List bedPriceList) {
        this.bedPriceList = bedPriceList;
    }

    public List getBreakfastList() {
        return breakfastList;
    }

    public void setBreakfastList(List breakfastList) {
        this.breakfastList = breakfastList;
    }

    public List getInternetList() {
        return internetList;
    }

    public void setInternetList(List internetList) {
        this.internetList = internetList;
    }

    public List getWelcomeList() {
        return welcomeList;
    }

    public void setWelcomeList(List welcomeList) {
        this.welcomeList = welcomeList;
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public void setSendLuceneMQ(SendLuceneMQ sendLuceneMQ) {
		this.sendLuceneMQ = sendLuceneMQ;
	}

	public String getRequestChannel() {
		return requestChannel;
	}

	public void setRequestChannel(String requestChannel) {
		this.requestChannel = requestChannel;
	}

	public String[] getViewChannels() {
		return viewChannels;
	}

	public void setViewChannels(String[] viewChannels) {
		this.viewChannels = viewChannels;
	}

}
