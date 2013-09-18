/**
 * 
 *  联系人预订信息表
 */

package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class Linkman implements Entity {

    // ID <pk>
    private Long ID; 

    // 联系人名称
    private String linkName;

    // 联系人称谓
    private String appellative;

    // 联系人手机号
    private String mobile;

    // 联系人座机
    private String telephone;

    // 联系人传真
    private String fax;

    // 联系人电邮
    private String email;

    // 交通工具类型
    private String vehicleType;
    
    // 交通工具车次或航班
    private String vehicleNo;
    
    // 附加服务
    private String addOnService;
    
    // 客人特殊要求
    private String specialDemand;
    
    // 最早到店时间
    private String earlyArriveTime;
    
    // 最晚到店时间
    private String lateArriveTime;
    
    // 客人要求付款时间
    private Date demandPayTime;

    public String getAddOnService() {
        return addOnService;
    }

    public void setAddOnService(String addOnService) {
        this.addOnService = addOnService;
    }

    public String getAppellative() {
        return appellative;
    }

    public void setAppellative(String appellative) {
        this.appellative = appellative;
    }

    public Date getDemandPayTime() {
        return demandPayTime;
    }

    public void setDemandPayTime(Date demandPayTime) {
        this.demandPayTime = demandPayTime;
    }

    public String getEarlyArriveTime() {
        return earlyArriveTime;
    }

    public void setEarlyArriveTime(String earlyArriveTime) {
        this.earlyArriveTime = earlyArriveTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getLateArriveTime() {
        return lateArriveTime;
    }

    public void setLateArriveTime(String lateArriveTime) {
        this.lateArriveTime = lateArriveTime;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long linkId) {
        this.ID = linkId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSpecialDemand() {
        return specialDemand;
    }

    public void setSpecialDemand(String specialDemand) {
        this.specialDemand = specialDemand;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }


}
