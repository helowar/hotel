package com.mangocity.hotel.base.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.IGeograpPositionDAO;
import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 * 地点DAO的实现类
 * 
 * @author zuoshengwei
 * 
 */
public class GeograpPositionDAOImpl extends DAOHibernateImpl implements IGeograpPositionDAO {

    /**
     * 新增一个地点
     * 
     * @param htlGeographicalposition
     * @return
     */
    public Long createGeograpPosition(HtlGeographicalposition htlGeographicalposition) {
        super.save(htlGeographicalposition);
        return htlGeographicalposition.getID();

    }

    /**
     * 根据指定ID获取地点信息
     * 
     * @param id
     * @return
     */
    public HtlGeographicalposition queryGeopositionById(long id) {

        String hsql = "from HtlGeographicalposition where ID =? and isActive='1'";

        Object[] obj = new Object[] { id};
        HtlGeographicalposition lisHtlGeographicalposition = (HtlGeographicalposition) super.find(
            hsql, obj);

        return lisHtlGeographicalposition;
    }

    /**
     * 修改地点信息
     * 
     * @param geographicalposition
     * @return
     */
    public int updateGeoposition(HtlGeographicalposition geographicalposition) {

        super.update(geographicalposition);
        return 0;

    }

    /**
     * 删除地点信息
     * 
     * @param id
     * @return
     */
    public int deleteGeoposition(long id) {
      
    	String hsql = "update HtlGeographicalposition set operationDate=sysdate,isActive='0' where ID = ?";
    	Object[] obj = new Object[1];
        obj[0] = Long.valueOf(id);
        super.remove(hsql, obj);
        return 0;
    }

    /**
     * 根据城市名称查询出地点类型
     */
    public List queryAddressTypeByCityName(String cityName) {

        String sql = "select distinct   t.gptype_id  from "
            + "htl_geographicalposition t  where t.city_name='" + cityName + "' " + "and t.isActive = '1'";

        List list = super.getSession().createSQLQuery(sql).list();
        if (null == list || 0 == list.size()) {
            return null;
        }
    
        return list;
    }

    /**
     * 根据类型ID和城市名获得对应的地点信息
     * 
     * @param name
     * @return
     */

    public List<HtlGeographicalposition> queryNameByAddType(long addressType, String cityName) {

        List<HtlGeographicalposition> lisAddress = new ArrayList<HtlGeographicalposition>();

        lisAddress = super.queryByNamedQuery("queryGPByName",
            new Object[] { addressType, cityName });

        return lisAddress;

    }

	public List<HtlGeographicalposition> queryPositionByType(long type,
			String cityName) {
		String hql="select new HtlGeographicalposition(ID ,name) from HtlGeographicalposition where cityName=? and gptypeId=? and isActive='1'";
		return super.query(hql, new Object[]{cityName,type});
	}

	public List queryBusinessForCityName(String cityName) {
		String sql = "select id,title,treepath, name from cmd.t_cdm_basedata where parent_id in (select id from cmd.t_cdm_basedata where parent_id in(select id from cmd.t_cdm_basedata where title=? and levels=5))";
		return super.doquerySQL(sql, new Object[]{cityName}, false);
	}
	
	public List queryBusinessForCityCode(String cityCode) {
		String sql = "select id,title,treepath, name from cmd.t_cdm_basedata where parent_id in (select id from cmd.t_cdm_basedata where parent_id in(select id from cmd.t_cdm_basedata where name=? and levels=5))";
		return super.doquerySQL(sql, new Object[]{cityCode}, false);
	}

	public List queryGeograpPosition(String cityName, Long gptypeId,
			String name) {
		String hql="select new HtlGeographicalposition(ID ,name) from HtlGeographicalposition where cityName=? and gptypeId=? and name=? and isActive='1'";
		return super.query(hql, new Object[]{cityName,gptypeId,name});
	}

	public List queryAllCity() {
		String sql="select citycode,cityname,statename from htl_area order by statename";
		return super.doquerySQL(sql, false);
	}

	public List queryPostionListByCityCode(String cityCode, Integer type) {
		String hql="from HtlGeographicalposition where cityCode=? and gptypeId=? and isActive='1'";
		return super.query(hql, new Object[]{cityCode,Long.valueOf(type)});
	}

	public List queryAllPosition() {
		String hql="from HtlGeographicalposition where isActive='1'";
		return super.query(hql);
	}
	
	public List queryPositions(Date date){
		String hql="from HtlGeographicalposition where operationDate>=?";
		return super.query(hql, new Object[]{date});
	}

	public List findCityByCode(String cityCode) {
		String sql="select citycode,cityname from htl_area where statename=(select statename from htl_area where citycode='"+cityCode+"')";
		List list=super.doquerySQL(sql, false);
		return list;
	}

	

}
