package com.mangocity.hotel.base.manage;

import com.mangocity.hotel.base.persistence.HtlPreconcertItemTemplet;

/**
 */
public interface ClauseTemplateManage {

    /*
     * 新增酒店预订担保预付条款模板
     */
    public long createPreconcertItemTemplet(HtlPreconcertItemTemplet htlPreconcertItemTemplet);

    /*
     * 根据酒店预订担保预付条款模板ID，查询酒店预订担保预付条款模板信息
     */
    public HtlPreconcertItemTemplet findHtlPreconITById(Long ID);

    /*
     * 根据酒店预订担保预付条款模板ID，删除酒店预订担保预付条款模板信息
     */
    public int delHtlPreconItTpltInfo(HtlPreconcertItemTemplet htlPreconcertItemTemplet);

}
