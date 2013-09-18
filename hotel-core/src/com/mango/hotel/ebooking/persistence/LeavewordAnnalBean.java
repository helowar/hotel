/**
 * @author chenjuesu 功能说明：实现了HTL_Ebooking_Leaveword_Annal的实体类 参数说明：无 创建时间：2009-6-16
 */
package com.mango.hotel.ebooking.persistence;

import java.io.Serializable;
import java.util.Date;

import com.mangocity.util.log.MyLog;

/**
 * @author yangshaojun
 */
public class LeavewordAnnalBean implements Serializable,Cloneable {
	
	private static final MyLog log = MyLog.getLogger(LeavewordAnnalBean.class);
    @Override
	public Object clone(){
		// TODO Auto-generated method stub
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}

	private static final long serialVersionUID = 885529214448113946L;

    /**
     * 当发件人为MangoCity时,我们设置为此常量
     */
    public static final String MANGOCITY = "0";

    /**
     * 留言记录ID
     */
    private long leaveWordID;

    /**
     * 发件人，为酒店ID或者是MangoCity常量
     */
    private String addresser;

    /**
     * 收件人，为酒店ID或者是MangoCity常量
     */
    private String addressee;

    /**
     * 主题
     */
    private String topic;

    /**
     * 主要内容
     */
    private String content;

    /**
     * 操作人
     */
    private String operationer;

    /**
     * 操作人ID
     */
    private String operationerID;

    /**
     * 操作日期
     */
    private Date operationdate;
    /**
     * 上传文件名(",")分隔
     */
    private String uploadfilename;

    /**
     * 查阅人
     */
    private String referer;

    /**
     * 查阅时间
     */
    private Date referdate;

    /**
     * 是否回复(1表示是回复,0表示否)
     */
    private long revert;
    /**
     * 1 表示标记为已读
     */
    private int hasRead;
    /**
     * 1 标记是已锁
     */
    private int hasLock;
    
    /**
	 * 留言回复类型(0无效1房态2价格)
	 */
    private Long leaveType;
    /**
     * 下面三个标记是谁点击“已查阅”操作的
     */
    private String hasReadId;
    
    private String hasReadName;
    
    private Date hasReadTime;
    
    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getAddresser() {
        return addresser;
    }

    public void setAddresser(String addresser) {
        this.addresser = addresser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getLeaveWordID() {
        return leaveWordID;
    }

    public void setLeaveWordID(long leaveWordID) {
        this.leaveWordID = leaveWordID;
    }

    public Date getOperationdate() {
        return operationdate;
    }

    public void setOperationdate(Date operationdate) {
        this.operationdate = operationdate;
    }

    public String getOperationer() {
        return operationer;
    }

    public void setOperationer(String operationer) {
        this.operationer = operationer;
    }

    public String getOperationerID() {
        return operationerID;
    }

    public void setOperationerID(String operationerID) {
        this.operationerID = operationerID;
    }

    public Date getReferdate() {
        return referdate;
    }

    public void setReferdate(Date referdate) {
        this.referdate = referdate;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public long getRevert() {
        return revert;
    }

    public void setRevert(long revert) {
        this.revert = revert;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

	public int getHasLock() {
		return hasLock;
	}

	public void setHasLock(int hasLock) {
		this.hasLock = hasLock;
	}

	public int getHasRead() {
		return hasRead;
	}

	public void setHasRead(int hasRead) {
		this.hasRead = hasRead;
	}

	public String getHasReadId() {
		return hasReadId;
	}

	public void setHasReadId(String hasReadId) {
		this.hasReadId = hasReadId;
	}

	public String getHasReadName() {
		return hasReadName;
	}

	public void setHasReadName(String hasReadName) {
		this.hasReadName = hasReadName;
	}

	public Date getHasReadTime() {
		return hasReadTime;
	}

	public void setHasReadTime(Date hasReadTime) {
		this.hasReadTime = hasReadTime;
	}

	public String getUploadfilename() {
		return uploadfilename;
	}

	public void setUploadfilename(String uploadfilename) {
		this.uploadfilename = uploadfilename;
	}

	public Long getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(Long leaveType) {
		this.leaveType = leaveType;
	}

}
