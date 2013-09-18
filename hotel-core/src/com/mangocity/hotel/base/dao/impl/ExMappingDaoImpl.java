package com.mangocity.hotel.base.dao.impl;
/**
 * 酒店映射表Dao
 * @author huanglingfeng
 *
 */
import com.mangocity.hotel.base.dao.ExMappingDao;
import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class ExMappingDaoImpl  extends GenericDAOHibernateImpl implements ExMappingDao {
	/**
	 * 查询价格类型级别的映射
	 * @param mgPriceTypeId 芒果价格类型Id
	 * @return
	 */
	public ExMapping queryExPrice(Long mgPriceTypeId) {
		String hql="from ExMapping e where e.priceTypeId="+mgPriceTypeId+" and e.type=3 and e.isActive=1 ";
		return (ExMapping) query(hql, new Object[]{}).get(0);
	}

}
