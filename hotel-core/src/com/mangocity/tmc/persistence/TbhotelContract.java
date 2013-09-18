package com.mangocity.tmc.persistence;

import java.util.Date;

/**
 * 商旅公司与酒店签订的合同
 * @author:shizhongwen
 * 创建日期:Sep 24, 2009,3:42:31 PM
 * 描述：
 */
public class TbhotelContract implements java.io.Serializable{

    private Long hotelcontractid; //合同ID

    private Long companyid; //公司ID

    private String contractcode; //合同编码

    private Long hotelid; //酒店ID

    private String hotelname; //酒店名称

    private Date inuredate; //合同开始日期

    private Date abatedate; //合同终止日期

    private Double addprice; //加床的价格

    private String fundway; //政府基金 （是百分比还是绝对值）

    private Double fundamount;//政府基金值

    private String note;//备注

    private String isincluefund;//是否含

    private String taxway;//税费（是百分比还是绝对值）

    private Double taxamount;//税费值

    private String isincluetax;//是否含税费

    private String holdtime;//保留时限

    private String status; //status为2表示合同有效 ，1表示合同无效，只是暂时保存

    private String creator;

    private Date createdate;

    private Long showindex;

    private String creatorname;

    private String reserveitem;//预订条款

    public Long getHotelcontractid() {
        return hotelcontractid;
    }

    public void setHotelcontractid(Long hotelcontractid) {
        this.hotelcontractid = hotelcontractid;
    }

    public Long getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Long companyid) {
        this.companyid = companyid;
    }

    public String getContractcode() {
        return contractcode;
    }

    public void setContractcode(String contractcode) {
        this.contractcode = contractcode;
    }

    public Long getHotelid() {
        return hotelid;
    }

    public void setHotelid(Long hotelid) {
        this.hotelid = hotelid;
    }

    public String getHotelname() {
        return hotelname;
    }

    public void setHotelname(String hotelname) {
        this.hotelname = hotelname;
    }

    public Date getInuredate() {
        return inuredate;
    }

    public void setInuredate(Date inuredate) {
        this.inuredate = inuredate;
    }

    public Date getAbatedate() {
        return abatedate;
    }

    public void setAbatedate(Date abatedate) {
        this.abatedate = abatedate;
    }

    public Double getAddprice() {
        return addprice;
    }

    public void setAddprice(Double addprice) {
        this.addprice = addprice;
    }

    public String getFundway() {
        return fundway;
    }

    public void setFundway(String fundway) {
        this.fundway = fundway;
    }

    public Double getFundamount() {
        return fundamount;
    }

    public void setFundamount(Double fundamount) {
        this.fundamount = fundamount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIsincluefund() {
        return isincluefund;
    }

    public void setIsincluefund(String isincluefund) {
        this.isincluefund = isincluefund;
    }

    public String getTaxway() {
        return taxway;
    }

    public void setTaxway(String taxway) {
        this.taxway = taxway;
    }

    public Double getTaxamount() {
        return taxamount;
    }

    public void setTaxamount(Double taxamount) {
        this.taxamount = taxamount;
    }

    public String getIsincluetax() {
        return isincluetax;
    }

    public void setIsincluetax(String isincluetax) {
        this.isincluetax = isincluetax;
    }

    public String getHoldtime() {
        return holdtime;
    }

    public void setHoldtime(String holdtime) {
        this.holdtime = holdtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Long getShowindex() {
        return showindex;
    }

    public void setShowindex(Long showindex) {
        this.showindex = showindex;
    }

    public String getCreatorname() {
        return creatorname;
    }

    public void setCreatorname(String creatorname) {
        this.creatorname = creatorname;
    }

    public String getReserveitem() {
        return reserveitem;
    }

    public void setReserveitem(String reserveitem) {
        this.reserveitem = reserveitem;
    }
}
