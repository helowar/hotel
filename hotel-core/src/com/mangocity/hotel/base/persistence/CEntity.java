package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class CEntity implements Serializable {

    private String create_by_id;

    private String create_by;

    private String modify_by_id;

    private String modify_by;

    private Date create_time;

    private Date modify_time;

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getCreate_by_id() {
        return create_by_id;
    }

    public void setCreate_by_id(String create_by_id) {
        this.create_by_id = create_by_id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getModify_by() {
        return modify_by;
    }

    public void setModify_by(String modify_by) {
        this.modify_by = modify_by;
    }

    public String getModify_by_id() {
        return modify_by_id;
    }

    public void setModify_by_id(String modify_by_id) {
        this.modify_by_id = modify_by_id;
    }

    public Date getModify_time() {
        return modify_time;
    }

    public void setModify_time(Date modify_time) {
        this.modify_time = modify_time;
    }

}
