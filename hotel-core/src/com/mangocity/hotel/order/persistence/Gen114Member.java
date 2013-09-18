
package com.mangocity.hotel.order.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;


/**
 * 
 *  <br>114会员
 *  <br>同一省的只用1个
 *  <br>同一城市的也只用1个
 *  
 *  @author chenkeming
 */
public class Gen114Member implements Entity {

    private static final long serialVersionUID = -6873985839312992521L;
    
    /**
	 * ID <pk>
	 */
    private Long ID;
    
    /**
	 * 114会员编号,如果不为空说明为指定的某个城市(如深圳)114会员,如果为空则依据memberstate判断
	 */
    private String memberCD;
    
    /**
	 * 114会员所在省份(包括网通,联通等), 三字编码
	 */
    private String memberState;
    
    /**
	 * 是否生成TXT文件, 0:不生成, 1: 生成
	 */
    private boolean genTxt;
    
    /**
	 * 是否生成EXCEL文件, 0:不生成, 1: 生成
	 */
    private boolean genExcel;
    
    /**
	 * 创建时间
	 */
    private Date createTime;
    
    /**
	 * 和Gen114File关联
	 */
    private List<Gen114File> genFiles = new ArrayList<Gen114File>();
    
    
    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public boolean isGenExcel() {
        return genExcel;
    }

    public void setGenExcel(boolean genExcel) {
        this.genExcel = genExcel;
    }

    public boolean isGenTxt() {
        return genTxt;
    }

    public void setGenTxt(boolean genTxt) {
        this.genTxt = genTxt;
    }

    public String getMemberCD() {
        return memberCD;
    }

    public void setMemberCD(String memberCD) {
        this.memberCD = memberCD;
    }

    public String getMemberState() {
        return memberState;
    }

    public void setMemberState(String memberState) {
        this.memberState = memberState;
    }

    public List<Gen114File> getGenFiles() {
        return genFiles;
    }

    public void setGenFiles(List<Gen114File> genFiles) {
        this.genFiles = genFiles;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
}
