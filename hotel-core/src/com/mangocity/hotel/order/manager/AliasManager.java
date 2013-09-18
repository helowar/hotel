package com.mangocity.hotel.order.manager;

import java.util.List;

import com.mangocity.hotel.order.dao.IaliasDao;

/**
 * 会员联名商家业务类
 * 
 * @author shizhongwen
 * 
 */
public class AliasManager {

    private IaliasDao aliasDao;

    /**
     * 通过会员的联名商家ID查询联名商家中文名
     * 
     * @param aliasId
     * @return
     */
    public String getNamebyAliasCode(String aliasId) {
        String aliasName = "";
        String sql = "select distinct t.name from t_memberaliascard t where t.code = '"
            + aliasId.trim() + "'";
        List result = aliasDao.queryNamebyCode(sql);
        if (null != result && !result.isEmpty()) {
            aliasName = (String) result.get(0);
        }
        return aliasName;
    }

    public IaliasDao getAliasDao() {
        return aliasDao;
    }

    public void setAliasDao(IaliasDao aliasDao) {
        this.aliasDao = aliasDao;
    }

}
