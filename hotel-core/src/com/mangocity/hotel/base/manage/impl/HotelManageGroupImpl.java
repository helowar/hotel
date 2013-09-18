package com.mangocity.hotel.base.manage.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mangocity.hotel.base.manage.HotelManageGroup;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hotel.base.persistence.HtlContactInfo;
import com.mangocity.hotel.base.persistence.HtlGroupChinaHq;
import com.mangocity.hotel.base.persistence.HtlGroupHQ;
import com.mangocity.hotel.base.persistence.HtlGroupOffshootOrgan;
import com.mangocity.hotel.base.persistence.HtlHotelGroup;
import com.mangocity.hotel.base.persistence.HtlNameplate;
import com.mangocity.hotel.base.persistence.HtlNameplateChinaHQ;
import com.mangocity.hotel.base.persistence.HtlNameplateOffshootOrgan;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.hotel.constant.HotelMappingType;

/**
 */
public class HotelManageGroupImpl extends DAOHibernateImpl implements HotelManageGroup {

    public long createHotelManageGroup(HtlHotelGroup htlHotelGroup) {
        super.save(htlHotelGroup);
        return 0;
    }

    public long createHMLinkInfo(HtlContactInfo htlContactInfo) {
        super.save(htlContactInfo);
        return 0;
    }

    public HtlHotelGroup queryHotelManageGroup(long htlHotelGroupid) {
        return (HtlHotelGroup) super.find(HtlHotelGroup.class, htlHotelGroupid);
    }

    public long delHotelManageGroup(long htlHotelGroupid) {
        super.remove(HtlHotelGroup.class, htlHotelGroupid);
        return 0;
    }

    public long upHotelManageGroup(HtlHotelGroup htlHotelGroup) {
        super.update(htlHotelGroup);
        return 0;
    }

    public long delHMLinkInfo(long htlContactInfoID) {
        super.remove(HtlContactInfo.class, htlContactInfoID);
        return 0;
    }

    public HtlContactInfo queryHMLinkInfo(long htlContactInfoID) {
        return (HtlContactInfo) super.find(HtlContactInfo.class, htlContactInfoID);
    }

    public long upHMLinkInfo(HtlContactInfo htlContactInfo) {
        super.update(htlContactInfo);
        return 0;
    }

    public long saveOrUpdateHotelManageGroup(HtlHotelGroup htlHotelGroup) {
        super.saveOrUpdate(htlHotelGroup);
        return 0;
    }

    public long saveOrUpdateHMLinkInfo(HtlContactInfo htlContactInfo) {
        super.saveOrUpdate(htlContactInfo);
        return 0;
    }

    public HtlGroupOffshootOrgan queryHtlGroupOffshootOrgan(long htlContactInfoID) {
        return (HtlGroupOffshootOrgan) super.find(HtlGroupOffshootOrgan.class, htlContactInfoID);
    }

    public long saveOrUpdateHtlGroupOffshootOrgan(HtlGroupOffshootOrgan htlGroupOffshootOrgan) {
        super.saveOrUpdate(htlGroupOffshootOrgan);
        return 0;
    }

    public long delHtlGroupOffshootOrgan(long htlContactInfoID) {
        super.remove(HtlGroupOffshootOrgan.class, htlContactInfoID);
        return 0;
    }

    public HtlGroupChinaHq queryHtlGroupChinaHq(long htlHotelGroupid) {
        String hsql = "from HtlContactInfo where hotel_group_id = ? "
            + "and COMMUNICATE_TYPE = 'GroupChinaHQ'";
        HtlGroupChinaHq htlGroupChinaHq = new HtlGroupChinaHq();

        htlGroupChinaHq = (HtlGroupChinaHq) super.find(hsql, htlHotelGroupid);
        // return (HtlGroupChinaHq)super.find(HtlGroupChinaHq.class, htlHotelGroupid);

        return htlGroupChinaHq;
    }

    public HtlGroupHQ queryHtlGroupHQ(long htlHotelGroupid) {
        // return (HtlGroupHQ)super.find(HtlGroupHQ.class, htlHotelGroupid);
        String hsql = "from HtlContactInfo where hotel_group_id = ?"
            + " and COMMUNICATE_TYPE = 'GroupHQ'";
        HtlGroupHQ htlGroupHQ = new HtlGroupHQ();
        htlGroupHQ = (HtlGroupHQ) super.find(hsql, htlHotelGroupid);
        return htlGroupHQ;
    }

    public long saveOrUpdateHtlGroupChinaHq(HtlGroupChinaHq htlGroupChinaHq) {
        super.saveOrUpdate(htlGroupChinaHq);
        return 0;
    }

    public long saveOrUpdateHtlGroupHQ(HtlGroupHQ htlGroupHQ) {
        super.saveOrUpdate(htlGroupHQ);
        return 0;
    }

    public List<HtlGroupOffshootOrgan> queryLisHtlGroupOffshootOrgan(long htlHotelGroupid) {
        String hsql = "from HtlContactInfo where hotel_group_id = ? "
            + "and COMMUNICATE_TYPE = 'GroupOffshootOrgan'";
        List<HtlGroupOffshootOrgan> lisHtlGroupOffshootOrgan = 
            new ArrayList<HtlGroupOffshootOrgan>();
        lisHtlGroupOffshootOrgan = super.query(hsql, htlHotelGroupid);
        // htlGroupHQ = (HtlGroupHQ)super.find(hsql, htlHotelGroupid);
        return lisHtlGroupOffshootOrgan;
    }

    public HtlNameplate queryNameplate(long htlNameplateID) {

        return (HtlNameplate) super.find(HtlNameplate.class, htlNameplateID);
    }

    public long saveOrUpdateNameplate(HtlNameplate htlNameplate) {
        super.saveOrUpdate(htlNameplate);
        return 0;
    }

    public long delNameplate(long htlNameplateID) {
        super.remove(HtlNameplate.class, htlNameplateID);
        return 0;
    }

    public HtlNameplateChinaHQ queryHotelNameplateChinaHQ(long htlNameplateID) {
        String hsql = "from HtlContactInfo where NAMEPLATE_ID = ? "
            + "and COMMUNICATE_TYPE = 'NameplateChinaHQ'";
        HtlNameplateChinaHQ htlNameplateChinaHQ = new HtlNameplateChinaHQ();
        htlNameplateChinaHQ = (HtlNameplateChinaHQ) super.find(hsql, htlNameplateID);
        return htlNameplateChinaHQ;
    }

    public long saveOrUpdateNameplateChinaHQ(HtlNameplateChinaHQ htlNameplateChinaHQ) {
        super.saveOrUpdate(htlNameplateChinaHQ);
        return 0;
    }

    public long delNameplateOffshootOrgan(long htlNameplateOffshootOrganID) {
        super.remove(HtlNameplateOffshootOrgan.class, htlNameplateOffshootOrganID);
        return 0;
    }

    public List queryLisHtlNameplateOffshootOrgan(long htlNameplateID) {
        String hsql = "from HtlContactInfo where NAMEPLATE_ID = ? "
            + "and COMMUNICATE_TYPE = 'NameplateOffshootOrgan'";
        List lisHtlNameplateOffshootOrgan = new ArrayList();
        lisHtlNameplateOffshootOrgan = super.query(hsql, htlNameplateID);
        return lisHtlNameplateOffshootOrgan;
    }

    public HtlNameplateOffshootOrgan queryNameplateOffshootOrgan(long nameplateOffshootOrganID) {

        return (HtlNameplateOffshootOrgan) super.find(HtlNameplateOffshootOrgan.class,
            nameplateOffshootOrganID);
    }

    public long saveOrUpdateNameplateOffshootOrgan(
        HtlNameplateOffshootOrgan htlNameplateOffshootOrgan) {
        super.saveOrUpdate(htlNameplateOffshootOrgan);
        return 0;
    }

    public List queryLisNameplate(long htlHotelGroupid) {
        String hsql = "from HtlNameplate where HOTEL_GROUP_ID = ? ";
        List lisHtlNameplate = new ArrayList();
        lisHtlNameplate = super.query(hsql, htlHotelGroupid);
        return lisHtlNameplate;
    }

    public List queryHtlGroupMapping(long groupId, long[] type) {
        String typeStr = new String();
        for (int i = 0; i < type.length; i++) {
            typeStr += String.valueOf(type[i]);
            if (i < type.length - 1) {
                typeStr += ",";
            }
        }
        String hql = "from ExMapping em where em.code = ? and em.type in (" + typeStr + ")";
        List lisHtlNameplate = new ArrayList();
        lisHtlNameplate = super.query(hql, String.valueOf(groupId));
        return lisHtlNameplate;
    }

    //public void saveOrUpdateAll(Collection entities) {
//        super.saveOrUpdateAll(entities);
//    }


    public ExMapping queryHtlGroupMapping(long groupId, long channelType) {
        String hql = "from ExMapping em where em.code = ? and em.channeltype = ? "
            + "and em.type = " + HotelMappingType.HOTEL_GROUP_TYPE;
        ExMapping em = new ExMapping();
        em = (ExMapping) super.find(hql, new Object[] { String.valueOf(groupId), channelType });
        return em;
    }

    public int saveOrUpdateExMappingGroup(final String htlGroupId, final long channelType,
        final String groupCodeForChannel) {
        HibernateCallback cb = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String Hql = "update ExMapping em set em.groupcode = ?"
                    + " where em.channeltype = ? " + " and em.groupCodeForMango = ? "
                    + " and em.type in (" + HotelMappingType.HOTEL_TYPE + ","
                    + HotelMappingType.ROOM_TYPE + "," + HotelMappingType.PRICE_TYPE + ")";
                Query query = session.createQuery(Hql);
                query.setString(0, groupCodeForChannel);
                query.setLong(1, channelType);
                query.setString(2, htlGroupId);
                query.executeUpdate();
                return 0;
            }
        };
        Integer ret = (Integer) getHibernateTemplate().execute(cb);
        if (null == ret) {
            return -1;
        } else {
            return ret.intValue();
        }
    }

	public List getPlate(String idLst) {
		String hsql;
		if(null!=idLst&&!"".equals(idLst)){
			hsql = "from HtlNameplate where HOTEL_GROUP_ID in(?) ";
			return super.query(hsql, idLst);
		}else{
			hsql = "from HtlNameplate  ";
			return super.query(hsql);
		}
	}

}
