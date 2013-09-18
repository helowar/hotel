
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/** 
 *  114历史订单文件记录
 *  
 *  @author chenkeming
 */
public class Gen114File implements Entity {

    private static final long serialVersionUID = -7098516400081590933L;
    
    /**
	 * ID <pk>
	 */
    private Long ID;
    
    /**
	 * 和Gen114Member关联
	 */
    private Gen114Member genMember;
    
    /**
	 * 具体文件目录路径
	 */
    private String filePath;
    
    /**
	 * URL文件目录路径
	 */
    private String webPath;
    
    /**
	 * 实际文件名
	 */
    private String fileName;
    
    /**
	 * 文件大小
	 */
    private long fileSize;
    
    /**
	 * 类型. 1: 保存的是当天下定的订单, 2: 保存的是当天日审通过的订单
	 */
    private int type;
    
    /**
	 * 文件类型. 1: TXT, 2: EXCEL
	 */
    private int fileType;
    
    /**
	 * 日期类型. 1: 每天的数据, 2: 某一个月份的数据
	 */
    private int dayType;
    
    /**
	 * 订单数
	 */
    private long counts;
    
    /**
	 * 给定日期的订单数据,如果FILETYPE=2,则为该月第1天
	 */
    private Date fileDate;
    
    /**
	 * 创建者工号
	 */
    private String creator;
    
    /**
	 * 创建时间
	 */
    private Date createTime;

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getDayType() {
        return dayType;
    }

    public void setDayType(int dayType) {
        this.dayType = dayType;
    }

    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public Gen114Member getGenMember() {
        return genMember;
    }

    public void setGenMember(Gen114Member genMember) {
        this.genMember = genMember;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getWebPath() {
        return webPath;
    }

    public void setWebPath(String webPath) {
        this.webPath = webPath;
    }

    public long getCounts() {
        return counts;
    }

    public void setCounts(long counts) {
        this.counts = counts;
    }

}
