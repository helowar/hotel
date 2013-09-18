package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 * HTL_BOOK_MODIFY_FIELD 记录预订条款修改字段定义字段 hotel2.9.2 初始数据从HTL_BOOK_CAUL_CLAUSE中移植过来
 * 
 * @author chenjiajie
 * 
 */
public class HtlBookModifyField extends CEntity implements Entity {

    public Long getID() {
        // TODO Auto-generated method stub
        return this.ID;
    }

    /**
     * 主键
     */
    private Long ID;

    /**
     * 关联的酒店id
     */
    private Long hotelId;

    /**
     * 预订条款修改字段定义字段
     */
    private String modifyField;

    /** getter and setter **/
    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getModifyField() {
        return modifyField;
    }

    public void setModifyField(String modifyField) {
        this.modifyField = modifyField;
    }

    public void setID(Long id) {
        ID = id;
    }

}
