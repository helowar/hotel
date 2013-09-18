package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 用于auditDetail.jsp的显示
 * 
 * @author chenkeming
 * 
 */
public class AuditDetailRowInfo implements Serializable {
    private List items = new ArrayList();

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

}
