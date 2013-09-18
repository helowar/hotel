package zhx.service.impl;



import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;
import zhx.dao.IZhxDao;
import zhx.service.IZhxManage;

import com.mangocity.util.log.MyLog;

/**
 * 中航信的业务层
 * 
 * @author chenkeming
 *
 */
public class ZhxManage implements IZhxManage {
	
	private static final MyLog log = MyLog.getLogger(ZhxManage.class);
	
	private IZhxDao zhxDao;
	
	/**
	 * 
	 * 调用存储过程更新价格
	 * 
	 * @param childRoomType
	 * @param payMethod
	 * @param startDate
	 * @param endDate
	 * @param salePrice
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public long updatePrice(Long childRoomType, String payMethod, String startDate, 
			String endDate, Double salePrice) {
		
		CallableStatement cstmt = null;
		long retId = 0;
		try {
			String procedureName = "{call pkg_zhx.prcUpdatePrice(?,?,?,?,?,?)}";
			cstmt = zhxDao.getCurrentSession().connection().prepareCall(
					procedureName);
			cstmt.setLong(1, childRoomType);
			cstmt.setString(2, payMethod);
			cstmt.setString(3, startDate);
			cstmt.setString(4, endDate);
			cstmt.setDouble(5, salePrice);
			cstmt.registerOutParameter(6, OracleTypes.NUMERIC);
			cstmt.execute();

			Object resObj = cstmt.getObject(6);
			retId = ((BigDecimal) resObj).longValue();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			retId = 0;
		} finally {
			if (null != cstmt) {
				try {
					cstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					log.error(e.getMessage(), e);
				}
			}
		}
		
		return retId;		
	}

	public void setZhxDao(IZhxDao zhxDao) {
		this.zhxDao = zhxDao;
	}
	
}
