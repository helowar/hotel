package com.mangocity.hotel.order.service.impl;

import com.mangocity.hotel.order.dao.impl.OrMemberConfirmDao;
import com.mangocity.hotel.order.persistence.OrMemberConfirm;
import com.mangocity.hotel.order.service.IMemberConfirmService;
import com.mangocity.util.StringUtil;

/**
 * 处理给客人发短信的业务逻辑实现
 * 
 * @author chenjiajie
 * @version V2.7.1
 * 
 */
public class MemberConfirmService implements IMemberConfirmService {

    private OrMemberConfirmDao orMemberConfirmDao;

    public int getSuccessedConfirmNum(Long orderId) {
        StringBuffer sql = new StringBuffer("");
        sql.append("select count(*) as countRow from or_memberconfirm m ");
        sql.append(" where m.orderid = " + orderId);
        sql.append(" and m.channel = 3 and m.sendStatus = 1  ");
        return orMemberConfirmDao.getNum(sql.toString());
    }

    public int getConfirmNum(Long orderId) {
        StringBuffer sql = new StringBuffer("");
        sql.append("select count(*) as countRow from or_memberconfirm m ");
        sql.append(" where m.orderid = " + orderId);
        sql.append(" and m.channel = 3 ");
        return orMemberConfirmDao.getNum(sql.toString());
    }

    public void updateSmsStatus(Long memberConfirmId, int status) {
        OrMemberConfirm orMemberConfirm = (OrMemberConfirm) orMemberConfirmDao
            .loadObject(memberConfirmId);
        orMemberConfirm.setSendStatus(status);
        orMemberConfirmDao.updateObject(orMemberConfirm);
    }

    public void updateSmsNotes(String hidMemberConfirmNotes) {
        // 判断是否有效字符串，空串为无效串
        if (StringUtil.isValidStr(hidMemberConfirmNotes)) {
            String hql = "update OrMemberConfirm m set m.notes = ? where m.ID = ?";
            Object[] params = new Object[2];
            String[] memberConfirmArr = hidMemberConfirmNotes.split("\\|");
            for (int i = 0; i < memberConfirmArr.length; i++) {
                String[] itemStrArr = memberConfirmArr[i].split("\\#");
                String memberConfirmId = itemStrArr[0].trim();
                String memberConfirmNotes = "";
                // 界面的备注不为空
                if (1 < itemStrArr.length) {
                    memberConfirmNotes = null == itemStrArr[1] || "".equals(itemStrArr[1]) ? ""
                        : itemStrArr[1].trim();
                }
                params[0] = memberConfirmNotes;
                params[1] = Long.valueOf(memberConfirmId);
                orMemberConfirmDao.doUpdateBatch(hql, params);
            }
        }
    }

    /** setter and getter **/
    public OrMemberConfirmDao getOrMemberConfirmDao() {
        return orMemberConfirmDao;
    }

    public void setOrMemberConfirmDao(OrMemberConfirmDao orMemberConfirmDao) {
        this.orMemberConfirmDao = orMemberConfirmDao;
    }
}
