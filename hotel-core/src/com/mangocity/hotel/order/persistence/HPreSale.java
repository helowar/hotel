package com.mangocity.hotel.order.persistence;

import com.mangocity.util.HEntity;


/**
 * 历史订单绑定的酒店促销信息
 * @author chenkeming Feb 18, 2009 3:36:26 PM
 */
public class HPreSale implements HEntity {
    
    private static final long serialVersionUID = -5912860877922426277L;

    /**
	 * ID <pk>
	 */
    private Long hisID;
    
    /**
	 * 1:酒店促销,2:芒果促销
	 * @author chenkeming Feb 18, 2009 3:37:11 PM
	 */
    private int type;
    
    /**
	 * 促销内容
	 * @author chenkeming Feb 18, 2009 3:38:05 PM
	 */
    private String content;
    
    /**
	 * 日期范围字符串
	 * @author chenkeming Feb 18, 2009 3:38:21 PM
	 */
    private String beginEnd;
    
    /**
	 * 芒果促销:促销名称,酒店促销:房型(子房型)
	 * @author chenkeming Feb 18, 2009 3:39:10 PM
	 */
    private String nameStr;
    
    /**
	 * 芒果促销的促销URL
	 * @author chenkeming Feb 18, 2009 3:40:06 PM
	 */
    private String url;
    
    /**
	 * 和HOrder关联
	 * @author chenkeming Feb 18, 2009 3:43:37 PM
	 */
    private HOrder orderH;

    public String getBeginEnd() {
        return beginEnd;
    }

    public void setBeginEnd(String beginEnd) {
        this.beginEnd = beginEnd;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNameStr() {
        return nameStr;
    }

    public void setNameStr(String name) {
        this.nameStr = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getHisID() {
        return hisID;
    }

    public void setHisID(Long hisID) {
        this.hisID = hisID;
    }

    public HOrder getOrderH() {
        return orderH;
    }

    public void setOrderH(HOrder orderH) {
        this.orderH = orderH;
    }    
    
}
