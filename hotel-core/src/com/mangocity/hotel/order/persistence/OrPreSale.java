package com.mangocity.hotel.order.persistence;

import com.mangocity.util.Entity;


/**
 * 订单绑定的酒店促销信息
 * @author chenkeming Feb 18, 2009 3:36:26 PM
 */
public class OrPreSale implements Entity {

    private static final long serialVersionUID = 670975866532414767L;
    
    /**
	 * ID <pk>
	 */
    private Long ID;
    
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
	 * 芒果促销:促销名称,酒店促销:酒店促销or房型促销
	 * @author chenkeming Feb 18, 2009 3:39:10 PM
	 */
    private String nameStr;
    
    /**
	 * 芒果促销的促销URL
	 * @author chenkeming Feb 18, 2009 3:40:06 PM
	 */
    private String url;
    
    /**
	 * 和OrOrder关联
	 * @author chenkeming Feb 18, 2009 3:43:37 PM
	 */
    private OrOrder order;
    
    /**
     * 不在外网显示<br>
     * 默认为不显示 0 '0' : 不显示<br>
     * '1' : 显示
     */
    private String webShow;

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

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

    public OrOrder getOrder() {
        return order;
    }

    public void setOrder(OrOrder order) {
        this.order = order;
    }

	public String getWebShow() {
		return webShow;
	}

	public void setWebShow(String webShow) {
		this.webShow = webShow;
	}
    
    
}
