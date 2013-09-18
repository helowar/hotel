package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mangocity.hdl.constant.ChannelType;
import com.mangocity.hdl.hotel.dto.MGExHotel;
import com.mangocity.hdl.hotel.dto.QueryHotelInfoExRequest;
import com.mangocity.hdl.hotel.dto.QueryHotelInfoExResponse;
import com.mangocity.hdl.hotel.dto.QueryRoomTypeInfoExRequest;
import com.mangocity.hdl.hotel.dto.QueryRoomTypeInfoExResponse;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.manage.HotelManageGroup;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.HtlCtlDsply;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlHotelExt;
import com.mangocity.hotel.base.persistence.HtlRoomtype;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.hotel.constant.HotelMappingType;

/**
 * 处理酒店映射的Action
 * 
 * @author chenjiajie V2.5
 */
public class HotelMappingAction extends PersistenceAction {

    private final int cooperatorCount = ChannelType.TOTEL_CHANNEL_COUNT; // 合作商总个数，用于界面新增按钮的控制

    private HtlHotel htlHotel;

    private HotelManage hotelManage;

    /**
     * 酒店映射对象(ExMapping)
     */
    private List<ExMapping> hotelMappingList;

    /**
     * 价格计划映射(ExMapping)
     */
    private List<ExMapping> priceMappingList;

    /**
     * 房型映射(ExMapping)
     */
    private List<ExMapping> roomMappingList;

    /**
     * 本酒店合作方的个数
     */
    private int cooperatorNum;

    /**
     * 价格计划的个数
     */
    private int priceNum;

    /**
     * 房型的个数
     */
    private int roomNum;

    /**
     * 合作商开关
     */
    private long cooperatorChecked = -1;

    /**
     * 价格计划开关
     */
    private String priceChecked;

    /**
     * 房型开关
     */
    private String roomChecked;

    /**
     * add by shizhongwen 2009-2-5 是否可修改开关
     */
    private long ismodifyChecked = -1;

    /**
     * add by shizhongwen 2009-2-5 酒店扩展表对象(HtlHotelExt)
     */
    private List<HtlHotelExt> hotelExtList;

    private long hotelId;

    /**
     * 合作商号码
     */
    private long channeltype;

    /**
     * HDL接口
     */
    private IHDLService hdlService;

    private HotelManageGroup hotelManageGroup;

    /**
     * 芒果网房型Id
     */
    private String roomTypeId;

	/**
	 * 取得酒店对象
	 */
	private HtlHotel hotel;
	
    /**
     * 港中旅酒店映射实体(用于传递到界面显示) V2.8 by chenjiajie 2009-03-16
     */
    private ExMapping ctsHotelMapping;

    /**
     * 港中旅酒店控制平台显示 v2.8 add by shizhongwen 2009-03-27
     */
    private List<HtlCtlDsply> lstCtlDsply;

    /**
     * 显示酒店房型标识（ishkroomtype:1 显示港中旅酒店的房型, ishkroomtype：0 非港中旅酒店的房型）
     */
    private String ishkroomtype;
    
    private String hotelcodeforchannel;
    
    private String hotelName;
    

    /**
     * 编辑映射酒店 add by chenjiajie 2008-11-24 Vesion 2.5
     * 
     * @return
     */
    public String editMappingHotel() {
        htlHotel = hotelManage.findHotel(hotelId);
        hotelMappingList = hotelManage.findHotelMapping(hotelId, HotelMappingType.HOTEL_TYPE);

        // 用于记录CTS酒店映射在hotelMappingList中的下标
        int ctsMappingIndex = -1;
        for (int i = 0; i < hotelMappingList.size(); i++) {
            ExMapping hotelMapping = hotelMappingList.get(i);
            // 是否启用(港中旅映射不参加判断)
            if (hotelMapping.getChanneltype().intValue() != ChannelType.CHANNEL_CTS
                && "1".equals(hotelMapping.getIsActive())) {
                cooperatorChecked = hotelMapping.getChanneltype(); // 渠道类型 各第三方平台系统编号
            }
            /**
             * 如果该映射记录是CTS酒店映射，则提取出来，赋值给ctsHotelMapping V2.8 by chenjiajie 2009-03-16
             */
            if (hotelMapping.getChanneltype().intValue() == ChannelType.CHANNEL_CTS) {
                ctsHotelMapping = hotelMapping;
                ctsMappingIndex = i;
            }
        }
        // add by shizhongwen hotel_v2.5 酒店订单是否可修改
        hotelExtList = hotelManage.findHotelExt(hotelId);
        for (HtlHotelExt hotelext : hotelExtList) {
            if (null != hotelext.getIsModify() && 1 == hotelext.getIsModify().intValue()) {
                ismodifyChecked = 1;
            } else {
                ismodifyChecked = 0;
            }
        }

        /**
         * 因为界面需要单独显示CTS酒店映射，所以hotelMappingList需要移除该对象 V2.8 by chenjiajie 2009-03-16
         */
        if (-1 < ctsMappingIndex) {
            hotelMappingList.remove(ctsMappingIndex);
        }
        if(null != ctsHotelMapping){
        	setCtsHotelMapping(ctsHotelMapping);
        }

        this.setCooperatorNum(hotelMappingList.size()); // 本酒店合作方的个数
        return "editMappingHotel";
    }
    
    /**
     * 转自签为艺龙酒店
     * @return
     */
    public String traditionToElong(){
    	hotelManage.traditionToElong(hotelId, hotelcodeforchannel, hotelName);
    	return "redirecteditMappingHotel";
    }
    
    /**
     * 艺龙转自签
     * 删除艺龙酒店映射，转成自签酒店，修改htl_hotel_ext的channel
     * hotelId
     * @return
     */
    public String elongToTradition(){
    	hotelManage.elongToTradition(hotelId);
    	return "redirecteditMappingHotel";
    }
    
    
    
    @SuppressWarnings("unchecked")
	private void fillHotelExmapping(List<ExMapping> objToSave, UserWrapper user, HtlHotel htlHotel, ExMapping emGroup){
    	List myRoomTypeLis = hotelManage.findRoomTypeLis(hotelId); // 芒果网该酒店的所有房型
    	if (myRoomTypeLis.isEmpty()) {
    		return;
    	}
    	
    	String hotelChnName = htlHotel.getChnName();
		String htlGroupId = htlHotel.getParentHotelGroup();
		List<ExMapping> emHotelList = hotelManage.findHotelMapping(hotelId, HotelMappingType.HOTEL_TYPE, channeltype);
		
		for (ExMapping emHotel : emHotelList) {
			/** 判断该酒店映射对应的房型映射是否存在，不存在则新增 Begin * */			
			List<ExMapping> tempRoomMappingList = hotelManage.findHotelMapping(
					hotelId, HotelMappingType.ROOM_TYPE, emHotel.getChanneltype().longValue());
			// 对比两个集合，有相同项把从目标集合移除
			compareList(tempRoomMappingList, myRoomTypeLis, HotelMappingType.ROOM_TYPE);
			for (Iterator iterator = myRoomTypeLis.iterator(); iterator.hasNext();) {
				List myRoomType = (List) iterator.next();

				String typeName = (String) myRoomType.get(0); // 芒果网酒店的房型名称
				Long typeId = (Long) myRoomType.get(1); // 芒果网酒店的房型编码
				String typeIdStr = String.valueOf(typeId);

				ExMapping tempEmRoom = new ExMapping();
				tempEmRoom.setChanneltype(emHotel.getChanneltype()); // 合作商
				tempEmRoom.setHotelid(hotelId); // 酒店ID
				tempEmRoom.setHotelcode(htlHotel.getHotelCd()); // 芒果酒店编号
				tempEmRoom.setHotelname(hotelChnName); // 渠道的酒店名称(默认芒果网的酒店中文名)
				tempEmRoom.setHotelcodeforchannel(emHotel.getHotelcodeforchannel());
				tempEmRoom.setRoomTypeId(typeIdStr); // 芒果网酒店的房型Id
				tempEmRoom.setRoomtypename(typeName); // 渠道的房型名称(默认芒果网房型名称)
				tempEmRoom.setRoomtypenameForMango(typeName); // 芒果网房型名称
				tempEmRoom.setType(HotelMappingType.ROOM_TYPE); // 映射类别
				if (null != user) {
					tempEmRoom.setModiby(user.getLoginName()); // 修改人登陆工号
					tempEmRoom.setModifiername(user.getName()); // 最后修改人中英文姓名
				}
				tempEmRoom.setModitime(DateUtil.getSystemDate()); // 修改时间
				tempEmRoom.setCode(typeIdStr); // 芒果编号
				tempEmRoom.setIsActive("0"); // 不启用
				if (null != htlGroupId && !htlGroupId.equals("")) {
					tempEmRoom.setGroupCodeForMango(htlGroupId); // 该酒店所属集团的芒果id
				}
				if (null != emGroup) {
					tempEmRoom.setGroupcode(emGroup.getCodeforchannel()); // 该酒店所属集团的编码
				}
				objToSave.add(tempEmRoom); // 加入批量保存
			}
		}
	}

    @SuppressWarnings("unchecked")
	private void fillPriceExmapping(List<ExMapping> objToSave, UserWrapper user, HtlHotel htlHotel, ExMapping emGroup){
    	List myPriceTypeLis = hotelManage.findPriceTypeLis(hotelId); // 芒果网该酒店的所有价格类型
    	if (myPriceTypeLis.isEmpty()) {
    		return;
    	}
    		
    	String hotelChnName = htlHotel.getChnName();
		String htlGroupId = htlHotel.getParentHotelGroup();
		List<ExMapping> emHotelList = hotelManage.findHotelMapping(hotelId, HotelMappingType.HOTEL_TYPE, channeltype);
		for (ExMapping emHotel:emHotelList) {
            /** 判断该酒店映射对应的价格映射是否存在，不存在则新增 Begin **/            
            List tempPriceMappingList = hotelManage.findHotelMapping(hotelId,
                HotelMappingType.PRICE_TYPE, emHotel.getChanneltype().longValue());
            
            // 对比两个集合，有相同项把从目标集合移除
            compareList(tempPriceMappingList, myPriceTypeLis, HotelMappingType.PRICE_TYPE);
            
            for (Iterator iterator = myPriceTypeLis.iterator(); iterator.hasNext();) {
                List myPriceType = (List) iterator.next();
                String roomChildRoomName = (String) myPriceType.get(0); // 芒果网酒店的价格类型名称
                Long roomChildRoomId = (Long) myPriceType.get(1); // 芒果网酒店的价格类型编码
                String roomChildRoomIdStr = String.valueOf(roomChildRoomId);
                String roomParentName = (String) myPriceType.get(2); // 芒果网酒店的房型名称
                Long roomParentId = (Long) myPriceType.get(3); // 芒果网酒店的房型编码

                ExMapping tempEmPrice = new ExMapping();
                tempEmPrice.setChanneltype(emHotel.getChanneltype()); // 合作商
                tempEmPrice.setHotelid(hotelId); // 酒店ID
                tempEmPrice.setHotelcode(htlHotel.getHotelCd()); // 芒果酒店编号
                tempEmPrice.setHotelname(hotelChnName); // 渠道的酒店名称(默认芒果网的酒店中文名)
                tempEmPrice.setHotelcodeforchannel(emHotel.getHotelcodeforchannel());
                tempEmPrice.setRoomTypeId(String.valueOf(roomParentId)); // 芒果网酒店的房型Id
                tempEmPrice.setRoomtypename(roomParentName); // 渠道的房型名称(默认芒果网房型名称)
                tempEmPrice.setRoomtypenameForMango(roomParentName); // 芒果网房型名称
                tempEmPrice.setPriceTypeId(roomChildRoomIdStr); // 芒果网酒店的价格类型Id
                tempEmPrice.setPricetypename(roomChildRoomName); // 芒果网酒店的价格类型名称
                tempEmPrice.setRateplanname(roomChildRoomName); // 渠道的价格类型中文名(默认芒果网酒店的价格类型名称)
                tempEmPrice.setType(HotelMappingType.PRICE_TYPE); // 映射类别
                if (null != user) {
                    tempEmPrice.setModiby(user.getLoginName()); // 修改人登陆工号
                    tempEmPrice.setModifiername(user.getName()); // 最后修改人中英文姓名
                }
                tempEmPrice.setModitime(DateUtil.getSystemDate()); // 修改时间
                tempEmPrice.setCode(roomChildRoomIdStr); // 芒果编号
                tempEmPrice.setIsActive("0"); // 不启用
                if (null != htlGroupId && !htlGroupId.equals("")) {
                    tempEmPrice.setGroupCodeForMango(htlGroupId); // 该酒店所属集团的芒果id
                }
                if (null != emGroup) {
                    tempEmPrice.setGroupcode(emGroup.getCodeforchannel()); // 该酒店所属集团的编码
                }
                objToSave.add(tempEmPrice); // 加入批量保存
            }
        }
    }
    	 
    /**
	 * 编辑映射酒店的房型和价格计划 add by chenjiajie 2008-11-24 Vesion 2.5
	 * 
	 * @return
	 */
    public String editMappingHotelDetail() {
        List<ExMapping> objToSave = new ArrayList<ExMapping>(); // 保存批量保存的对象
        UserWrapper user = super.getOnlineRoleUser();
        htlHotel = hotelManage.findHotel(hotelId);
        String htlGroupId = htlHotel.getParentHotelGroup();
        ExMapping emGroup = null;
        if (null != htlGroupId && !htlGroupId.equals("")) {
            emGroup = hotelManageGroup.queryHtlGroupMapping(Long.parseLong(htlGroupId), channeltype);
        }
        // 当前的映射酒店
        /** 判断该酒店映射对应的房型映射是否存在，不存在则新增 Begin **/
        fillHotelExmapping(objToSave,user,htlHotel,emGroup);
        /** 判断该酒店映射对应的房型映射是否存在，不存在则新增 End **/

       /** 判断该酒店映射对应的价格映射是否存在，不存在则新增 Begin **/
        fillPriceExmapping(objToSave,user,htlHotel,emGroup);
       /** 判断该酒店映射对应的价格映射是否存在，不存在则新增 End **/
        // 批量保存
        super.getEntityManager().saveOrUpdateAll(objToSave);

        // add by shizhongwen 2009-04-18
        List<ExMapping> searchroomMappingList = hotelManage.findHotelMapping(hotelId,
            HotelMappingType.ROOM_TYPE, channeltype);
        List<ExMapping> searchpriceMappingList = hotelManage.findHotelMapping(hotelId,
            HotelMappingType.PRICE_TYPE, channeltype);

        // modify by shizhongwen 2009-04-22显示中各自的所有房型(中旅，非中旅)
        roomMappingList = getCompareMappingList(ishkroomtype, searchroomMappingList);
        priceMappingList = getCompareMappingList(ishkroomtype, searchpriceMappingList);


        roomNum = roomMappingList.size();
        priceNum = priceMappingList.size();

        return "editMappingRoomOrPrice";
    }

    public List<ExMapping> getCompareMappingList(String isroomtyptag, List<ExMapping> mappinglist) {
        // 显示酒店房型标识（ishkroomtype:1 显示港中旅酒店的房型, ishkroomtype：0 非港中旅酒店的房型）
        // ishkroomtype:0 如果为0或者为空，就直接返回。add by shizhongwen 2009-05-21 
        if (null == isroomtyptag || "0".equals(isroomtyptag)) {
            return mappinglist;
        }
        
        List<ExMapping> comparelist = new ArrayList<ExMapping>();
        String roomtypid = "";
        for (ExMapping exmapping : mappinglist) {
            roomtypid = exmapping.getRoomTypeId();
            HtlRoomtype roomtype = hotelManage.findHtlRoomtype(Long.parseLong(roomtypid));
            if (null != roomtype && isroomtyptag.equals(roomtype.getIshkroomtype())) {
                comparelist.add(exmapping);
            } 
            log.debug("----------*************roomtypid:" + roomtypid + " isroomtyptag" + isroomtyptag);
        }
        return comparelist;
    }

    /**
     * 保存映射酒店 add by chenjiajie 2008-11-24 Vesion 2.5
     * 
     * @return
     */
    public String saveOrUpdateMappingHotel() {
        List<ExMapping> objToSave = new ArrayList<ExMapping>(); // 保存批量保存的对象
        Map params = super.getParams();
        UserWrapper user = super.getOnlineRoleUser();
        htlHotel = hotelManage.findHotel(hotelId);
        String hotelCd = htlHotel.getHotelCd();
        String hotelChnName = htlHotel.getChnName();
        String htlGroupId = htlHotel.getParentHotelGroup();
        // 参数解析
        List<ExMapping> lsExMapping = MyBeanUtil.getBatchObjectFromParam(params, ExMapping.class,
            cooperatorNum);
        // 该酒店下(房型，价格类型)类别的mapping对象
        List<ExMapping> primaryMappingList = hotelManage.findHotelMapping(hotelId, new long[] {
            HotelMappingType.ROOM_TYPE, HotelMappingType.PRICE_TYPE });
        String tag = "0"; // 记录启用合作商，没有开启则为空
        for (ExMapping emHotel:lsExMapping) {
            // 合作方&&合作方对应酒店编码不能为空
            if (null != emHotel.getChanneltype() && 0 < emHotel.getChanneltype()
                && null != emHotel.getCodeforchannel() && !"".equals(emHotel.getCodeforchannel())) {
                ExMapping emGroup = null;
                if (null != htlGroupId && !htlGroupId.equals("")) {
                    emHotel.setGroupCodeForMango(htlGroupId.trim()); // 该酒店所属集团的芒果id
                    emGroup = hotelManageGroup.queryHtlGroupMapping(Long.parseLong(htlGroupId),
                        emHotel.getChanneltype().longValue());
                }
                /** 新增或更新酒店映射 Begin **/
                // 是否启用的判断
                if (emHotel.getChanneltype().longValue() == cooperatorChecked) {
                    emHotel.setIsActive("1");
                    tag = String.valueOf(emHotel.getChanneltype());
                } else {
                    emHotel.setIsActive("0");
                }
                emHotel.setHotelid(hotelId); // 芒果酒店ID
                emHotel.setHotelcode(hotelCd); // 芒果酒店编号
                emHotel.setHotelname(hotelChnName); // 渠道的酒店名称(默认芒果网的酒店中文名)
                emHotel.setType(HotelMappingType.HOTEL_TYPE); // 映射类别
                if (null != user) {
                    emHotel.setModiby(user.getLoginName()); // 修改人登陆工号
                    emHotel.setModifiername(user.getName()); // 最后修改人中英文姓名
                }
                emHotel.setModitime(DateUtil.getSystemDate()); // 修改时间
                emHotel.setCode(hotelCd); // 芒果编号
                emHotel.setHotelcodeforchannel(emHotel.getCodeforchannel().trim());
                emHotel.setCodeforchannel(emHotel.getCodeforchannel().trim());
                // hotelCode(channel)渠道的酒店编码
                if (null != emGroup) {
                    emHotel.setGroupcode(emGroup.getCodeforchannel().trim()); // 该酒店所属集团的编码
                }
                objToSave.add(emHotel); // 加入批量保存
                /** 新增或更新酒店映射 End **/

                // 判断该酒店(房型，价格类型)类型已存在的映射的(渠道的酒店编码,渠道的酒店名称)数据是否和更新后的数据一致，否则同步
                for (ExMapping primaryMapping:primaryMappingList) {
                    // 数据不一致
                    if (primaryMapping.getChanneltype().equals(emHotel.getChanneltype())
                        && !primaryMapping.getHotelcodeforchannel().equals(
                            emHotel.getHotelcodeforchannel())) {
                        primaryMapping.setHotelcodeforchannel(emHotel.getHotelcodeforchannel().trim());
                        if (null != emGroup) {
                            primaryMapping.setGroupcode(emGroup.getCodeforchannel().trim());
                        }
                        objToSave.add(primaryMapping); // 加入批量保存
                    }
                }
                
                //批量更新当前合作方的集团编码.
                if (null != emGroup) {
                    if (null != emGroup.getGroupcode()) {
                        Long groupCode = Long.valueOf(emGroup.getGroupcode());
                        hotelManage.updateAllMappingGroupCode(hotelId, groupCode, emGroup
                            .getChanneltype());
                    }
                }
            }
        }

        /** 维护港中旅酒店映射 V2.8 by chenjiajie 2009-03-16 begin **/
        ctsHotelMapping = (ExMapping) request.getAttribute("ctsHotelMapping");
        List<ExMapping> mappinglist = hotelManage.findHotelMappingbyHKHotelId(ctsHotelMapping
            .getHotelcodeforchannel());
        // modify by shizhongwen 2009-04-29
        for (ExMapping exmapping : mappinglist) {
            if (!(exmapping.getHotelid().equals(hotelId))) {
                log.error("香港酒店编码已存在,请重新输入香港酒店编码!");
                setErrorMessage("香港酒店编码已存在,请重新输入香港酒店编码");
                return "forwardToError";
            }
        }
        fillCtsHotelMapping(objToSave, user, hotelCd, hotelChnName, htlGroupId, ctsHotelMapping);
        /** 维护港中旅酒店映射 end **/

        // 批量保存
        super.getEntityManager().saveOrUpdateAll(objToSave);

        // 把Hotel的扩展表的COOPERATE_CHANNEL设为当前启用的合作商代号,ISCTSHOTEL标志是否激活为港中旅港澳直联酒店
        List<HtlHotelExt> extList = htlHotel.getHtelHotelExt();
        HtlHotelExt htlExt = null;
        if (extList.isEmpty()) {
            htlExt = new HtlHotelExt();
            htlExt.setHtlHotel(htlHotel);
            htlExt.setCooperateChannel(tag);
            // 保存ISCTSHOTEL字段 V2.8 chenjiajie 2009-03-16
            if (null != ctsHotelMapping) {
                String ctsIsActive = ctsHotelMapping.getIsActive();
                if (StringUtil.isValidStr(ctsIsActive)) {
                    htlExt.setIsCTSHotel(ctsIsActive);
                } else {
                    htlExt.setIsCTSHotel("0");
                }
            }
        } else {
                htlExt = (HtlHotelExt) extList.get(0);
                htlExt.setCooperateChannel(tag);
                // 保存ISCTSHOTEL字段 V2.8 chenjiajie 2009-03-16
                if (null != ctsHotelMapping) {
                    String ctsIsActive = ctsHotelMapping.getIsActive();
                    if (StringUtil.isValidStr(ctsIsActive)) {
                        htlExt.setIsCTSHotel(ctsIsActive);
                    } else {
                        htlExt.setIsCTSHotel("0");
                    }
                }
        }
        hotelManage.saveOrUpdateExt(htlExt);

        // add by shizhongwen 2009-2-6 增加HtlHotelExt中是否修改状态(isModify)的保存
        hotelExtList = hotelManage.findHotelExt(hotelId);
        List<HtlHotelExt> extListSave = new ArrayList<HtlHotelExt>();
        for (HtlHotelExt hotelext : hotelExtList) {
            if (ismodifyChecked == 1) {
                hotelext.setIsModify(1L);
            } else {
                hotelext.setIsModify(0L);
            }
            extListSave.add(hotelext);
        }
        // 批量保存
        super.getEntityManager().saveOrUpdateAll(extListSave);
        // add by shizhognwen end

        return editMappingHotel();
    }

    /**
     * 重构 填充港中旅港澳直联酒店映射实体
     * 
     * @param objToSave
     *            : 批量保存的列表
     * @param user
     *            : 当前登陆的用户
     * @param hotelCd
     *            : 芒果网酒店编码
     * @param hotelChnName
     *            : 芒果网酒店中文名
     * @param htlGroupId
     *            : 芒果网酒店集团编码
     * @param ctsHotelMapping
     *            : 接受界面数据的映射实体
     */
    private void fillCtsHotelMapping(List<ExMapping> objToSave, UserWrapper user, String hotelCd,
        String hotelChnName, String htlGroupId, ExMapping ctsHotelMapping) {
        if (null == ctsHotelMapping || !StringUtil.isValidStr(ctsHotelMapping.getCodeforchannel())) {
        	return;
        }
        
        // 当有输入酒店映射编码则保存该记录
        ExMapping emGroup = null;
        ctsHotelMapping.setCodeforchannel(ctsHotelMapping.getCodeforchannel().trim());
        ctsHotelMapping.setChanneltype((long) ChannelType.CHANNEL_CTS); // 渠道类别
        ctsHotelMapping.setHotelid(hotelId); // 芒果酒店ID
        ctsHotelMapping.setHotelcode(hotelCd); // 芒果酒店编号
        ctsHotelMapping.setHotelname(hotelChnName); // 渠道的酒店名称(默认芒果网的酒店中文名)
        ctsHotelMapping.setType(HotelMappingType.HOTEL_TYPE); // 映射类别
        if (null != user) {
            ctsHotelMapping.setModiby(user.getLoginName()); // 修改人登陆工号
            ctsHotelMapping.setModifiername(user.getName()); // 最后修改人中英文姓名
        }
        ctsHotelMapping.setModitime(DateUtil.getSystemDate()); // 修改时间
        ctsHotelMapping.setCode(hotelCd); // 芒果编号
        ctsHotelMapping.setHotelcodeforchannel(ctsHotelMapping.getCodeforchannel());
        // hotelCode(channel)渠道的酒店编码
        if (StringUtil.isValidStr(htlGroupId)) {
            ctsHotelMapping.setGroupCodeForMango(htlGroupId); // 该酒店所属集团的芒果id
            emGroup = hotelManageGroup.queryHtlGroupMapping(Long.parseLong(htlGroupId),
                (long) ChannelType.CHANNEL_CTS);
        }
        if (null != emGroup) {
            ctsHotelMapping.setGroupcode(emGroup.getCodeforchannel()); // 该酒店所属集团的编码
        }
        objToSave.add(ctsHotelMapping); // 加入批量保存
    }

    /**
     * 保存映射房型 add by chenjiajie 2008-11-24 Vesion 2.5
     * 
     * @return
     */
    public String saveOrUpdateMappingRoom() {
        htlHotel = hotelManage.findHotel(hotelId);
        htlHotel.getHotelCd();
        htlHotel.getChnName();
        String htlGroupId = htlHotel.getParentHotelGroup();
        ExMapping emGroup = null;
        if (null != htlGroupId && !htlGroupId.equals("")) {
            emGroup = hotelManageGroup.queryHtlGroupMapping(Long.parseLong(htlGroupId), channeltype);
        }
        List<ExMapping> objToSave = new ArrayList<ExMapping>(); // 保存批量保存的对象
        Map params = super.getParams();
        UserWrapper user = super.getOnlineRoleUser();
        String[] roomCheckedArray = null;
        if (null != roomChecked && !roomChecked.equals("")) {
            roomCheckedArray = roomChecked.split(",");
        }
        // 参数解析
        List<ExMapping> lsExMapping = MyBeanUtil.getBatchObjectFromParam(params, ExMapping.class, roomNum
            + priceNum);
        // 该酒店下(价格类型)类别的mapping对象
        List<ExMapping> primaryMappingList = hotelManage.findHotelMapping(hotelId,
            new long[] { HotelMappingType.PRICE_TYPE });
        for (ExMapping emRoom:lsExMapping) {
            // 只保存房型映射
            if (null != emRoom.getExMappingId()
                && emRoom.getType().longValue() == HotelMappingType.ROOM_TYPE) {
                // 有房型记录为确认状态
                if (null != roomCheckedArray && 0 < roomCheckedArray.length) {
                    Long tag = -1L; // 判断是否确认状态的临时标记
                    for (int i = 0; i < roomCheckedArray.length; i++) {
                        if (roomCheckedArray[i].equals(emRoom.getExMappingId().toString())) {
                            tag = emRoom.getExMappingId();
                            break;
                        }
                    }
                    emRoom.setIsActive(tag.longValue()>0?"1":"0");
                } else {
                    emRoom.setIsActive("0");
                }
                emRoom.setHotelid(hotelId); // 酒店ID
                emRoom.setChanneltype(channeltype); // 合作商
                emRoom.setCode(emRoom.getRoomTypeId().trim()); // 芒果编号
                emRoom.setRoomtypecodeforchannel(emRoom.getCodeforchannel().trim()); // 各渠道的房型编码
                emRoom.setCodeforchannel(emRoom.getCodeforchannel().trim()); //codeChannel
                if (null != user) {
                    emRoom.setModiby(user.getLoginName()); // 修改人登陆工号
                    emRoom.setModifiername(user.getName()); // 最后修改人中英文姓名
                }
                emRoom.setModitime(DateUtil.getSystemDate()); // 修改时间
                if (null != htlGroupId && !htlGroupId.equals("")) {
                    emRoom.setGroupCodeForMango(htlGroupId.trim()); // 该酒店所属集团的芒果id
                }
                if (null != emGroup) {
                    emRoom.setGroupcode(emGroup.getCodeforchannel().trim()); // 该酒店所属集团的编码
                }
                objToSave.add(emRoom); // 加入批量保存

                // 判断该酒店(价格类型)类型已存在的映射的(各渠道的房型编码,渠道的房型名称,芒果网房型名称)数据是否和更新后的数据一致，否则同步
                for (ExMapping primaryMapping : primaryMappingList) {
                    // 数据不一致
                    if (primaryMapping.getChanneltype().equals(channeltype)
                        && primaryMapping.getRoomTypeId().equals(emRoom.getRoomTypeId())) {
                        String roomtypecodeforchannel = primaryMapping.getRoomtypecodeforchannel();
                        String roomtypename = primaryMapping.getRoomtypename();
                        String roomtypenameForMango = primaryMapping.getRoomtypenameForMango();
                        if (null == roomtypecodeforchannel || null == roomtypename
                            || null == roomtypenameForMango
                            || !roomtypecodeforchannel.equals(emRoom.getRoomtypecodeforchannel())
                            || !roomtypename.equals(emRoom.getRoomtypename())
                            || !roomtypenameForMango.equals(emRoom.getRoomtypenameForMango())) {
                            primaryMapping.setRoomtypecodeforchannel(emRoom
                                .getRoomtypecodeforchannel().trim());
                            primaryMapping.setRoomtypename(emRoom.getRoomtypename().trim());
                            primaryMapping
                                .setRoomtypenameForMango(emRoom.getRoomtypenameForMango().trim());
                            if(!"1".equals(emRoom.getIsActive())) {
                            	primaryMapping.setIsActive("0");	
                            }                            
                            if (null != emGroup) {
                                primaryMapping.setGroupcode(emGroup.getCodeforchannel().trim());
                                // 该酒店所属集团的编码
                            }
                            objToSave.add(primaryMapping); // 加入批量保存
                        }
                    }
                }

            }
        }
        // 批量保存
        super.getEntityManager().saveOrUpdateAll(objToSave);
        // add by shizhongwen 调用存储过程，完成所有的显示控制房型的记录写入
        hotelManage.InsertHtlCtlDsply(hotelId);
        return "reEditMappingRoomOrPrice";
    }

    /**
     * 保存映射价格计划 add by chenjiajie 2008-11-24 Vesion 2.5
     * 
     * @return
     */
    public String saveOrUpdateMappingPrice() {
    	htlHotel = hotelManage.findHotel(hotelId);
        htlHotel.getHotelCd();
        htlHotel.getChnName();
        String htlGroupId = htlHotel.getParentHotelGroup();
        ExMapping emGroup = null;
        if (null != htlGroupId && !htlGroupId.equals("")) {
            emGroup = hotelManageGroup.queryHtlGroupMapping(Long.parseLong(htlGroupId), channeltype);
        }
        List<ExMapping> objToSave = new ArrayList<ExMapping>(); // 保存批量保存的对象
        Map params = super.getParams();
        UserWrapper user = super.getOnlineRoleUser();
        String[] priceCheckedArray = null;
        if (null != priceChecked && !priceChecked.equals("")) {
            priceCheckedArray = priceChecked.split(",");
        }
        // 参数解析
        List<ExMapping> lsExMapping = MyBeanUtil.getBatchObjectFromParam(params, ExMapping.class,
            roomNum + priceNum);

        /**
         * ＊ 将房型id与合作方房型价格计划编码设置成MAP modify by guojun 2009-08-18 11:39 BEGIN
         */
        Map<String, String> roomMap = new HashMap<String, String>();
        for (ExMapping exMapping : lsExMapping) {
            if (null != exMapping.getRoomTypeId() && null == exMapping.getPriceTypeId()) {
                if (null != exMapping.getCodeforchannel()) {
                    roomMap.put(exMapping.getRoomTypeId(), exMapping.getCodeforchannel().trim());
                }
            }
        }
        /**
         * ＊ 将房型id与合作方房型价格计划编码设置成MAP modify by guojun 2009-08-18 11:39 END
         */

        for (ExMapping emPrice : lsExMapping) {
            // 只保存房型映射
            if (null != emPrice.getExMappingId()
                && emPrice.getType().longValue() == HotelMappingType.PRICE_TYPE) {
                // 有房型记录为确认状态
                if (null != priceCheckedArray && 0 < priceCheckedArray.length) {
                    Long tag = Long.valueOf(-1); // 判断是否确认状态的临时标记
                    for (int i = 0; i < priceCheckedArray.length; i++) {
                        if (priceCheckedArray[i].equals(emPrice.getExMappingId().toString())) {
                            tag = emPrice.getExMappingId();
                            break;
                        }
                    }
                    emPrice.setIsActive(tag.longValue()>0?"1":"0");
                } else {
                    emPrice.setIsActive("0");
                }
                emPrice.setHotelid(hotelId); // 酒店ID
                emPrice.setChanneltype(channeltype); // 合作商
                emPrice.setRateplancode(emPrice.getCodeforchannel().trim()); // 价格代码(channel) 渠道的价格类型编码
                emPrice.setCodeforchannel(emPrice.getCodeforchannel().trim()); // codechannel
                emPrice.setCode(emPrice.getPriceTypeId().trim()); // 芒果编号

                /**
                 * 新增加的价格计划需要增加合作方的映射编码 modify by guojun 2009-08-18 11:39 BEGIN
                 */
                if (null == emPrice.getRoomtypecodeforchannel() || "".equals(emPrice.getRoomtypecodeforchannel())) {
                    if (null != emPrice.getRoomTypeId()) {
                        emPrice.setRoomtypecodeforchannel(roomMap.get(emPrice.getRoomTypeId()).trim());
                    }
                }
                /**
                 * 新增加的价格计划需要增加合作方的映射编码 modify by guojun 2009-08-18 11:39 END
                 */

                if (null != user) {
                    emPrice.setModiby(user.getLoginName()); // 修改人登陆工号
                    emPrice.setModifiername(user.getName()); // 最后修改人中英文姓名
                }
                emPrice.setModitime(DateUtil.getSystemDate()); // 修改时间
                if (null != htlGroupId && !htlGroupId.equals("")) {
                    emPrice.setGroupCodeForMango(htlGroupId.trim()); // 该酒店所属集团的芒果id
                }
                if (null != emGroup) {
                    emPrice.setGroupcode(emGroup.getCodeforchannel().trim()); // 该酒店所属集团的编码
                }
                objToSave.add(emPrice); // 加入批量保存
            }
        }
        // 批量保存
        super.getEntityManager().saveOrUpdateAll(objToSave);
        // add by shizhongwen 调用存储过程，完成所有的显示控制房型的记录写入
        this.hotelManage.InsertHtlCtlDsply(hotelId);
        return "reEditMappingRoomOrPrice";
    }

    /**
     * 保存映射房型和价格计划 add by chenjiajie 2008-11-24 Vesion 2.5
     * 
     * @return
     */
    public String saveOrUpdateMappingRoomAndPrice() {
        saveOrUpdateMappingPrice();
        return saveOrUpdateMappingRoom();
    }

    /**
     * 对比两个集合，有相同项把从目标集合移除
     * 
     * @param primaryList
     *            原集合
     * @param targetList
     *            被比较集合
     * @param type
     *            比较项的类别 add by chenjiajie 2008-11-27 Vesion 2.5
     */
    protected void compareList(List primaryList, List targetList, long type) {
        // 原集合有记录才需要匹配
        if (0 < primaryList.size()) {
            for (int i = 0; i < primaryList.size(); i++) {
                ExMapping emObj = (ExMapping) primaryList.get(i);
                // 房型
                if (type == HotelMappingType.ROOM_TYPE) {
                    if (null != emObj.getRoomTypeId() && !emObj.getRoomTypeId().equals("")) {
                        // 匹配房型映射表和芒果网该酒店的房型,有相同项,从目标集合移除
                        for (int j = 0; j < targetList.size(); j++) {
                            List item = (List) targetList.get(j);
                            Long itemId = (Long) item.get(1);
                            String itemIdStr = String.valueOf(itemId);
                            // 从目标集合移除
                            if (itemIdStr.equals((emObj.getRoomTypeId()))) {
                                targetList.remove(item);
                            }
                        }
                    }
                }
                // 价格类型
                else if (type == HotelMappingType.PRICE_TYPE) {
                    if (null != emObj.getPriceTypeId() && !emObj.getPriceTypeId().equals("")) {
                        // 匹配价格类型映射表和芒果网该酒店的价格类型,有相同项,从目标集合移除
                        for (int j = 0; j < targetList.size(); j++) {
                            List item = (List) targetList.get(j);
                            Long itemId = (Long) item.get(1);
                            String itemIdStr = String.valueOf(itemId);
                            // 从目标集合移除
                            if (itemIdStr.equals((emObj.getPriceTypeId()))) {
                                targetList.remove(item);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 查看合作方实时的酒店基本信息内容 add by chenjiajie 2008-12-02 Vesion 2.5
     * 
     * @return
     */
    public String viewChannelHotelInfo() {
        QueryHotelInfoExRequest req = new QueryHotelInfoExRequest();
        req.setChannelCode(Long.valueOf(channeltype).intValue());
        req.getHotelId().add(hotelId);
        try {
            QueryHotelInfoExResponse res = hdlService.queryHotelInfo(req);
            List<MGExHotel> mappingHotels = res.getHotels();
            request.setAttribute("mappingHotels", mappingHotels);
        } catch (Exception e) {
            log.error("查看合作方实时的酒店基本信息内容:出错");
            log.error(e.getMessage(),e);
        }
        return "mappingHotelInfo";
    }

    /**
     * 查看合作方实时的房型基本信息内容 add by chenjiajie 2008-12-02 Vesion 2.5
     * 
     * @return
     */
    public String viewChannelRoomTypeInfo() {
        QueryRoomTypeInfoExRequest req = new QueryRoomTypeInfoExRequest();
        req.setChannelCode(Long.valueOf(channeltype).intValue());
        req.setHotelCode(String.valueOf(hotelId));
        req.getRoomTypeCodes().add(roomTypeId);
        try {
            QueryRoomTypeInfoExResponse res = hdlService.queryRoomTypeInfo(req);
            if (null != res) {
            	request.setAttribute("mappingRooms", res.getRoomTypeInfos());
            }            
        } catch (Exception e) {
            log.error("查看合作方实时的房型基本信息内容:出错");
            log.error(e.getMessage(),e);
        }
        return "mappingRoomTypeInfo";
    }

    /**
     * 用来显示控制销售渠道是否可见的设定界面
     * 
     * @return
     */
    public String controlDisplay() {
        htlHotel = hotelManage.findHotel(hotelId);
        if (null == htlHotel) {
            log.error("获得酒店为空");
            setErrorMessage("根据酒店id找不到该酒店");
            return "forwardToError";
        }
        lstCtlDsply = hotelManage.queryHtlCtlDsply(hotelId);
        if (null != lstCtlDsply) {
            roomNum = lstCtlDsply.size();
        } else {
            roomNum = 0;
        }
        return "controlDisplay";
    }

    public String saveControlDisplay() {
        Map params = super.getParams();
        List<HtlCtlDsply> objToSave = new ArrayList<HtlCtlDsply>(); // 保存批量保存的对象
        // 参数解析
        List<HtlCtlDsply> lsCtldsply = MyBeanUtil.getBatchObjectFromParam(params, HtlCtlDsply.class, roomNum);
        for (HtlCtlDsply htlctl : lsCtldsply) {
            htlctl.setHotelID(hotelId);
            objToSave.add(htlctl);
        }
        // 批量保存
        super.getEntityManager().saveOrUpdateAll(objToSave);
        return "recontrolDisplay";
    }

    /**
     * 根据酒店id删除此酒店与香港的所有映射 add by shizhongwen 时间:Apr 21, 2009 5:19:30 PM
     * 
     * @return
     */
    public String deleteMappingHotel() {
        htlHotel = hotelManage.findHotel(hotelId);
        List<HtlHotelExt> extList = htlHotel.getHtelHotelExt();
        HtlHotelExt htlExt = null;
        if (0 == extList.size()) {
            htlExt = new HtlHotelExt();
            htlExt.setHtlHotel(htlHotel);
            htlExt.setIsCTSHotel("0");
        } else {
                htlExt = (HtlHotelExt) extList.get(0);
                htlExt.setIsCTSHotel("0");
        }
        // 更新酒店扩展表的isCTSHotel
        hotelManage.saveOrUpdateExt(htlExt);

        try {
            hotelManage.deleteALLHotelMapping(hotelId);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        
        return "redirecteditMappingHotel";
    }
    
	/**
	 * 跳转到房态与价格解绑的房态查询 add by guojun 2009-11-25
	 */
	public String toRoomStatusUpdate() {
		hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class,hotelId);
		return "toExRoomStatus";
	}

	/**
	 * 跳转到房态与价格解绑的价格查询 add by guojun 2009-11-25
	 */
	public String toAmoutUpdate() {
		hotel = (HtlHotel) super.getEntityManager().find(HtlHotel.class,
				hotelId);
		return "toExPrice";
	}
	
    /** getter and setter begin **/
    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

    public void setCooperatorNum(int cooperatorNum) {
        this.cooperatorNum = cooperatorNum;
    }

    public void setPriceMappingList(List<ExMapping> priceMappingList) {
        this.priceMappingList = priceMappingList;
    }

    public void setRoomMappingList(List<ExMapping> roomMappingList) {
        this.roomMappingList = roomMappingList;
    }

    public void setCooperatorChecked(long cooperatorChecked) {
        this.cooperatorChecked = cooperatorChecked;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public void setChanneltype(long channeltype) {
        this.channeltype = channeltype;
    }

    public void setHotelMappingList(List<ExMapping> hotelMappingList) {
        this.hotelMappingList = hotelMappingList;
    }

    public void setPriceChecked(String priceChecked) {
        this.priceChecked = priceChecked;
    }

    public void setRoomChecked(String roomChecked) {
        this.roomChecked = roomChecked;
    }

    public void setPriceNum(int priceNum) {
        this.priceNum = priceNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public void setHdlService(IHDLService hdlService) {
        this.hdlService = hdlService;
    }

    public void setHotelManageGroup(HotelManageGroup hotelManageGroup) {
        this.hotelManageGroup = hotelManageGroup;
    }

    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public void setHotelExtList(List<HtlHotelExt> hotelExtList) {
        this.hotelExtList = hotelExtList;
    }

    public void setIsmodifyChecked(long ismodifyChecked) {
        this.ismodifyChecked = ismodifyChecked;
    }

    public void setCtsHotelMapping(ExMapping ctsHotelMapping) {
        this.ctsHotelMapping = ctsHotelMapping;
    }

    public void setLstCtlDsply(List<HtlCtlDsply> lstCtlDsply) {
        this.lstCtlDsply = lstCtlDsply;
    }

    public HtlHotel getHtlHotel() {
		return htlHotel;
	}

	public HotelManage getHotelManage() {
		return hotelManage;
	}

	public List<ExMapping> getHotelMappingList() {
		return hotelMappingList;
	}

	public List<ExMapping> getPriceMappingList() {
		return priceMappingList;
	}

	public List<ExMapping> getRoomMappingList() {
		return roomMappingList;
	}

	public int getCooperatorNum() {
		return cooperatorNum;
	}

	public int getPriceNum() {
		return priceNum;
	}

	public int getRoomNum() {
		return roomNum;
	}

	public long getCooperatorChecked() {
		return cooperatorChecked;
	}

	public String getPriceChecked() {
		return priceChecked;
	}

	public String getRoomChecked() {
		return roomChecked;
	}

	public long getIsmodifyChecked() {
		return ismodifyChecked;
	}

	public List<HtlHotelExt> getHotelExtList() {
		return hotelExtList;
	}

	public long getHotelId() {
		return hotelId;
	}

	public long getChanneltype() {
		return channeltype;
	}

	public IHDLService getHdlService() {
		return hdlService;
	}

	public HotelManageGroup getHotelManageGroup() {
		return hotelManageGroup;
	}

	public String getRoomTypeId() {
		return roomTypeId;
	}

	public HtlHotel getHotel() {
		return hotel;
	}

	public ExMapping getCtsHotelMapping() {
		return ctsHotelMapping;
	}

	public List<HtlCtlDsply> getLstCtlDsply() {
		return lstCtlDsply;
	}

	public String getIshkroomtype() {
		return ishkroomtype;
	}

	public void setIshkroomtype(String ishkroomtype) {
        this.ishkroomtype = ishkroomtype;
    }

	public void setHotel(HtlHotel hotel) {
		this.hotel = hotel;
	}

	public int getCooperatorCount() {
		return cooperatorCount;
	}

	public String getHotelcodeforchannel() {
		return hotelcodeforchannel;
	}

	public void setHotelcodeforchannel(String hotelcodeforchannel) {
		this.hotelcodeforchannel = hotelcodeforchannel;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

    /** getter and setter end **/
}
