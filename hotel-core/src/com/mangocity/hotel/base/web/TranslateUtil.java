package com.mangocity.hotel.base.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.mangocity.hotel.base.persistence.BtMember;
import com.mangocity.hotel.base.util.CreditCardWrapper;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.security.domain.OrgInfo;
import com.mangocity.security.domain.User;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;

/**
 * 对外面接口返回对象进行转换 2007-07-31
 * 
 */

public class TranslateUtil implements Serializable {

	private static final MyLog log = MyLog.getLogger(TranslateUtil.class);
    private DAOIbatisImpl queryDao;// parasoft-suppress SERIAL.NSFSC "暂不修改" 

    /**
     * 把登录权限用户User对象转换成UserWrapper
     * 
     * @param user
     * @return UserWrapper
     */
    public static UserWrapper translateUser(User user) {
        if (null == user) {
            return null;
        }
        UserWrapper userWrapper = new UserWrapper();
        MyBeanUtil.copyProperties(userWrapper, user);
        userWrapper.setRoles(user.getRoles());
        userWrapper.setOrgInfo(user.getOrgInfo());

        if (userWrapper.isOrg114()) {
            OrgInfo orgInfo = userWrapper.getOrgInfo();
            if (null != orgInfo && null != orgInfo.getOrgId()) {
                userWrapper.setState(orgInfo.getLinkman());
            }
        }

        return userWrapper;
    }

    public DAOIbatisImpl getQueryDao() {
        return queryDao;
    }

    public void setQueryDao(DAOIbatisImpl queryDao) {
        this.queryDao = queryDao;
    }

}
