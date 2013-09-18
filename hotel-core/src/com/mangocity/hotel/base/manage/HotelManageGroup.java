package com.mangocity.hotel.base.manage;

import java.util.Collection;
import java.util.List;

import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.HtlContactInfo;
import com.mangocity.hotel.base.persistence.HtlGroupChinaHq;
import com.mangocity.hotel.base.persistence.HtlGroupHQ;
import com.mangocity.hotel.base.persistence.HtlGroupOffshootOrgan;
import com.mangocity.hotel.base.persistence.HtlHotelGroup;
import com.mangocity.hotel.base.persistence.HtlNameplate;
import com.mangocity.hotel.base.persistence.HtlNameplateChinaHQ;
import com.mangocity.hotel.base.persistence.HtlNameplateOffshootOrgan;

/**
 */
public interface HotelManageGroup {

    /**
     * 增加一个酒店管理集团
     */
    public long createHotelManageGroup(HtlHotelGroup htlHotelGroup);

    /**
     * 增加或修改一个酒店管理集团
     */
    public long saveOrUpdateHotelManageGroup(HtlHotelGroup htlHotelGroup);

    /**
     * 删除一个酒店管理集团
     */
    public long delHotelManageGroup(long htlHotelGroupid);

    /**
     * 修改一个酒店管理集团
     */
    public long upHotelManageGroup(HtlHotelGroup htlHotelGroup);

    /**
     * 查询一个酒店管理集团
     */
    public HtlHotelGroup queryHotelManageGroup(long htlHotelGroupid);

    /**
     * 增加一个酒店管理集团分支机构联系信息
     */
    public long createHMLinkInfo(HtlContactInfo htlContactInfo);

    /**
     * 删除一个酒店管理集团分支机构联系信息
     */
    public long delHMLinkInfo(long htlContactInfoID);

    /**
     * 修改一个酒店管理集团分支机构联系信息
     */
    public long upHMLinkInfo(HtlContactInfo htlContactInfo);

    /**
     * 查询一个酒店管理集团分支机构联系信息
     */
    public HtlContactInfo queryHMLinkInfo(long htlContactInfoID);

    /**
     * 增加或修改一个酒店管理集团分支机构联系信息
     */
    public long saveOrUpdateHMLinkInfo(HtlContactInfo htlContactInfo);

    /**
     * 查询一个酒店管理集团分支机构联系信息
     */
    public HtlGroupOffshootOrgan queryHtlGroupOffshootOrgan(long htlContactInfoID);

    /**
     * 查询一个酒店管理集团分支机构联系信息
     */
    public List queryLisHtlGroupOffshootOrgan(long htlHotelGroupid);

    /**
     * 增加或修改一个酒店管理集团分支机构联系信息
     */
    public long saveOrUpdateHtlGroupOffshootOrgan(HtlGroupOffshootOrgan htlGroupOffshootOrgan);

    /**
     * 删除一个酒店管理集团分支机构联系信息
     */
    public long delHtlGroupOffshootOrgan(long htlContactInfoID);

    /**
     * 查询一个酒店管理集团总部机构联系信息
     */
    public HtlGroupHQ queryHtlGroupHQ(long htlHotelGroupid);

    /**
     * 增加或修改一个酒店管理集团总部机构联系信息
     */
    public long saveOrUpdateHtlGroupHQ(HtlGroupHQ htlGroupHQ);

    /**
     * 查询一个酒店管理集团中国总部机构联系信息
     */
    public HtlGroupChinaHq queryHtlGroupChinaHq(long htlHotelGroupid);

    /**
     * 增加或修改一个酒店管理集团中国总部机构联系信息
     */
    public long saveOrUpdateHtlGroupChinaHq(HtlGroupChinaHq htlGroupChinaHq);

    /**
     * 增加或修改品牌信息
     */
    public long saveOrUpdateNameplate(HtlNameplate htlNameplate);

    /**
     * 查询一个品牌信息
     */
    public HtlNameplate queryNameplate(long htlNameplateID);

    /**
     * 删除一个品牌信息
     */
    public long delNameplate(long htlNameplateID);

    /**
     * 增加或修改品牌总部信息
     */
    public long saveOrUpdateNameplateChinaHQ(HtlNameplateChinaHQ htlNameplateChinaHQ);

    /**
     * 查询一个品牌总部信息
     */
    public HtlNameplateChinaHQ queryHotelNameplateChinaHQ(long htlNameplateID);

    /**
     * 查询品牌总部信息list
     */
    public List queryLisNameplate(long htlHotelGroupid);

    /**
     * 删除一个品牌分支机构信息
     */
    public long delNameplateOffshootOrgan(long htlNameplateOffshootOrganID);

    /**
     * 增加或修改品牌分支机构信息
     */
    public long saveOrUpdateNameplateOffshootOrgan(
        HtlNameplateOffshootOrgan htlNameplateOffshootOrgan);

    /**
     * 查询一个品牌分支机构信息
     */
    public HtlNameplateOffshootOrgan queryNameplateOffshootOrgan(long nameplateOffshootOrganID);

    /**
     * 查询一个品牌分支机构联系信息
     */
    public List queryLisHtlNameplateOffshootOrgan(long htlNameplateID);

    /**
     * 查询酒店集团的映射信息
     * 
     * @param groupId
     *            :集团信息ID
     * @return
     * @author chenjiajie V2.5 2008-12-10
     */
    public List queryHtlGroupMapping(long groupId, long[] type);

    /**
     * 批量保存
     * 
     * @param entities
     * @author chenjiajie V2.5 2008-12-10
     */
    public void saveOrUpdateAll(Collection entities);

    /**
     * 查询某个渠道下的酒店集团映射
     * 
     * @param groupId
     * @param channelType
     * @return
     * @author chenjiajie V2.5 2008-12-10
     */
    public ExMapping queryHtlGroupMapping(long groupId, long channelType);

    /**
     * 更新某个渠道下酒店集团对应的酒店，房型，价格计划的渠道集团编码
     * 
     * @param htlGroupId
     *            芒果网酒店集团的ID
     * @param channelType
     *            渠道
     * @param groupCodeForChannel
     *            渠道集团编码
     * @return
     * @author chenjiajie V2.5 2008-12-10
     */
    public int saveOrUpdateExMappingGroup(String htlGroupId, long channelType,
        String groupCodeForChannel);
    /**
     * 查询酒店所有品牌
     * @return
     */
    public List getPlate(String idLst);

}
