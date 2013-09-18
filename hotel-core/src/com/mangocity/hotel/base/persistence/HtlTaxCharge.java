package com.mangocity.hotel.base.persistence;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 税费设定
 * 
 * @author xiaowumi
 * 
 */
public class HtlTaxCharge implements Entity {

    private Long ID;

    private Long htlTaxChargeID;

    /**
     * 开始日期
     */
    private Date taxBeginDate;

    /**
     * 结束日期
     */
    private Date taxEndDate;

    /**
     *房费是否含税
     */
    private String roomIncTax;

    /**
     * 佣金是否含税
     */
    private String commIncTax;

    /**
     * 佣金税
     */
    private Double commTax;

    /**
     * 房费税
     */
    private Double roomTax;

    /**
     * 酒店id
     */
    private long hotelId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人ID
     */
    private String createById;

    /**
     * 修改人
     */
    private String modifyBy;

    /**
     * 修改人ID
     */
    private String modifyById;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 是否有效
     */
    private String active;

    /**
     * 房税名称
     */
    private String roomTaxName;

    /**
     * 房税单位
     */
    private String roomTaxUnit;

    // private HtlContract htlContract;

    private Long contractId;

    public Double getCommTax() {
        return commTax;
    }

    public void setCommTax(Double commTax) {
        // 拥金税费加对小数位数的精度，四舍五入到小数点后4位。modify by zhineng.zhuang
        // 格式化
        DecimalFormat df = new DecimalFormat("#.#####");
        commTax = Double.parseDouble(df.format(commTax));
        // 四舍五入
        BigDecimal db = new BigDecimal(commTax);
        commTax = db.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        this.commTax = commTax;
    }

    public Double getRoomTax() {
        return roomTax;
    }

    public void setRoomTax(Double roomTax) {
        // 房费税费对小数位数的精度，四舍五入到小数点后3位。modify by zhineng.zhuang
        DecimalFormat df = new DecimalFormat("#.####");
        roomTax = Double.parseDouble(df.format(roomTax));
        BigDecimal db = new BigDecimal(roomTax);
        roomTax = db.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        this.roomTax = roomTax;
    }

    public Date getTaxBeginDate() {
        return taxBeginDate;
    }

    public void setTaxBeginDate(Date taxBeginDate) {
        this.taxBeginDate = taxBeginDate;
    }

    public Date getTaxEndDate() {
        return taxEndDate;
    }

    public void setTaxEndDate(Date taxEndDate) {
        this.taxEndDate = taxEndDate;
    }

    // public HtlContract getHtlContract() {
    // return htlContract;
    // }
    // public void setHtlContract(HtlContract htlContract) {
    // this.htlContract = htlContract;
    // }

    public void setCommIncTax(String commIncTax) {
        this.commIncTax = commIncTax;
    }

    public void setRoomIncTax(String roomIncTax) {
        this.roomIncTax = roomIncTax;
    }

    public String getCommIncTax() {
        return commIncTax;
    }

    public String getRoomIncTax() {
        return roomIncTax;
    }

    public Long getHtlTaxChargeID() {
        return htlTaxChargeID;
    }

    public void setHtlTaxChargeID(Long htlTaxChargeID) {
        this.htlTaxChargeID = htlTaxChargeID;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getModifyById() {
        return modifyById;
    }

    public void setModifyById(String modifyById) {
        this.modifyById = modifyById;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public String getRoomTaxName() {
        return roomTaxName;
    }

    public void setRoomTaxName(String roomTaxName) {
        this.roomTaxName = roomTaxName;
    }

    public String getRoomTaxUnit() {
        return roomTaxUnit;
    }

    public void setRoomTaxUnit(String roomTaxUnit) {
        this.roomTaxUnit = roomTaxUnit;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

}
