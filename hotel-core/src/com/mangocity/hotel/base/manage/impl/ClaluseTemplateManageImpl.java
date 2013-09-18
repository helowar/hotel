package com.mangocity.hotel.base.manage.impl;

import com.mangocity.hotel.base.manage.ClauseTemplateManage;
import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 */
public class ClaluseTemplateManageImpl extends DAOHibernateImpl implements ClauseTemplateManage {

    /*
     * 添加酒店预定担保预付条款模板
     * 
     * @see com.mangocity.hotel.base.manage. ClauseTemplateManage#createPreconcertItemTemplet
     * (com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet)
     */
    public long createPreconcertItemTemplet(HtlPreconcertItemTemplet htlPreconcertItemTemplet) {

        super.save(htlPreconcertItemTemplet);

        return 0;

    }

    /*
     * 根据酒店预订担保预付条款模板ID，查询酒店预订担保预付条款模板信息
     */

    public HtlPreconcertItemTemplet findHtlPreconITById(Long ID) {

        return (HtlPreconcertItemTemplet) super.find(HtlPreconcertItemTemplet.class, ID);
    }

    /*
     * 根据酒店预订担保预付条款模板ID，删除酒店预订担保预付条款模板信息
     * 
     * @see com.mangocity.hotel.base.manage.
     * ClauseTemplateManage#delHtlPreconItTpltInfo(java.lang.Long)
     */
    public int delHtlPreconItTpltInfo(HtlPreconcertItemTemplet htlPreconcertItemTemplet) {

        Long ID = htlPreconcertItemTemplet.getID();

        HtlPreconcertItemTemplet htlPreconItTplt = (HtlPreconcertItemTemplet) super.find(
            HtlPreconcertItemTemplet.class, ID);

        // 设置修改人，修改时间；
        htlPreconItTplt.setModifyBy(htlPreconcertItemTemplet.getModifyBy());
        htlPreconItTplt.setModifyByID(htlPreconcertItemTemplet.getModifyByID());
        htlPreconItTplt.setModifyTime(htlPreconcertItemTemplet.getModifyTime());

        // 是否删除的标记；
        htlPreconItTplt.setActive(htlPreconcertItemTemplet.getActive());

        // 设置删除人和删除时间；
        htlPreconItTplt.setDelBy(htlPreconcertItemTemplet.getDelBy());
        htlPreconItTplt.setDelByID(htlPreconcertItemTemplet.getDelByID());
        htlPreconItTplt.setDelTime(htlPreconcertItemTemplet.getDelTime());

        super.update(htlPreconItTplt);

        return 0;

    }

}
