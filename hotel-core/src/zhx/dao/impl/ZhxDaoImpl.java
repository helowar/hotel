package zhx.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;

import zhx.dao.IZhxDao;

import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 * 
 * 中航信Dao实现类
 * 
 * @author chenkeming
 *
 */
public class ZhxDaoImpl extends DAOHibernateImpl implements IZhxDao {
		
	private static final long serialVersionUID = -2034781008710305988L;
	
	private static final MyLog log = MyLog.getLogger(ZhxDaoImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
	/**
     * 按参数名查询参数列表
     * @param paramName
     * @return
     */
    public OrParam getOrParam(String paramName){
    	String hsql = "from OrParam a " + "where " + "a.name = ? ";
        OrParam param = (OrParam) super.find(hsql,new Object[]{paramName});
        return param;
    }
	
    /**
     * 更新参数
     * @param orParam
     */
    public void updateOrParam(OrParam orParam){
    	super.saveOrUpdate(orParam);
    }
    
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
}
