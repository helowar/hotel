/**
 * 
 */
package com.mangocity.hotel.order.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrUserPower;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 * @author zhuangzhineng
 * 
 */
public class OrUserPowerDao extends DAOHibernateImpl {
	private static final MyLog log = MyLog.getLogger(OrUserPowerDao.class);
    /**
     * 增加
     * 
     * @param orUserPower
     * @return
     */
    public String saveUserPower(OrUserPower orUserPower) {

        super.save(orUserPower);
        return "saveSucess";
    }

    /**
     * 更新
     * 
     * @param orUserPower
     * @return
     */
    public String updateUserPower(OrUserPower orUserPower) {
        super.update(orUserPower);
        return "updateSucess";
    }

    /**
     * 删除
     * 
     * @param orUserPower
     * @return
     */
    public String delUserPower(Long orUserPowerId) {
        super.remove(OrUserPower.class, orUserPowerId);
        return "delSucess";
    }

    /**
     * 查询是否存在
     * 
     * @param memberId
     * @return
     */
    public Boolean queryUserPower(String memberCD) {
        // String hsql = "from OrUserPower where userId=? and (powerId = ? or powerId like ?)";
        String hsql = "from OrUserPower where userId=? and powerId = ?";
        List resList = new ArrayList();
        try {
            // resList = super.query(hsql, new Object[]{memberCD,"1","%1,%"});
            resList = super.query(hsql, new Object[] { memberCD, "1" });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return 0 < resList.size();
    }

}
