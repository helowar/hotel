package com.mangocity.hotel.base.persistence;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.mangocity.util.StringUtil;

/**
 * TMC会员类,由于使用到tmc数据中的tbmember,故创建一个同义词 tmc_member,此类与之对应
 * 
 * @author:shizhongwen 创建日期:Jul 22, 2009,2:48:58 PM 描述：
 */
public class BtMember implements java.io.Serializable {

    private Long memberid; // 商旅会员ID

    private String memberno = "";// 商旅编号

    private String chnsurname = "";// 中文姓

    private String chnname = "";// 中文名

    private String title = "";// 中文称谓

    private String engsurname = "";// 英文姓

    private String engmidname = "";// 英文中间名

    private String engname = "";// 英文名

    private String sex = "";// 性别

    private String costcenter = "";// 成本中心

    private String chnjob = "";// 中文职位

    private String engjob = "";// 英文职位

    private String empno = "";// 员工编号

    private String postcode = "";// 邮编

    private String fax = "";// 传真

    private String memberrole = "";// 商旅角色

    private Long membersec;// 商旅秘书

    private String viplevel = "";// VIP级别

    private String chncontactaddress = "";// 中文联系地址

    private String engcontactaddress = "";// 英文联系地址

    private String idcardno = "";// 身份证号码

    private Date birthday;// 生日

    private String mobile = "";// 手机

    private String phonein = "";// 电话分机

    private String phoneout = "";// 电话外线

    private String email1 = "";// email1

    private String email2 = "";// email2

    private String email3 = "";// email3

    private String familyphone = "";// 家庭电话

    private String loveseat = "";// 座位偏好

    private String lovefood = "";// 饮食偏好

    private String note = "";// 备注

    private String active = "";// 是否有效

    private String mgcmemberno = "";// 会员编号

    private String nationality = "";// ; 国籍

    private Long mgcmemberid;// 会员ID

    private Long points;// 会员积分,此属性不保存在本地数据库

    private Long mainmember;// 父节点

    private Set memberCards = new HashSet(0);// 会员卡

    private Set commonLinks = new HashSet(0);// 联系人

    private Set commonChecks = new HashSet(0);// 审批人

    private Set children = new HashSet(0);// 子节点

    private Set memberCreditcards = new HashSet(0);// 信用卡

    private Set memberFarecards = new HashSet(0);// 常旅客卡

    private Set memberDescriptions = new HashSet(0);// 会员描述

    // taonenggong Add 2009-3-31 RMS25472 新增填写部门需求
    private String department;

    /** ***********NormandyV1.0 added by masiquan***************** */
    private Long companyid;// 公司ID

    private String companyName;// 公司名称

    private String companycd;// 公司商旅编码

    private String embranchmentName;// 分支机构商旅编码

    // Property accessors

    public Long getMgcmemberid() {
        return mgcmemberid;
    }

    public void setMgcmemberid(Long mgcmemberid) {
        this.mgcmemberid = mgcmemberid;
    }

    public String getMemberno() {
        return this.memberno;
    }

    public void setMemberno(String memberno) {
        this.memberno = memberno;
    }

    public String getChnsurname() {
        return this.chnsurname;
    }

    public void setChnsurname(String chnsurname) {
        this.chnsurname = chnsurname;
    }

    public String getChnname() {
        return this.chnname;
    }

    public void setChnname(String chnname) {
        this.chnname = chnname;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEngsurname() {
        return this.engsurname;
    }

    public void setEngsurname(String engsurname) {
        this.engsurname = engsurname;
    }

    public String getEngmidname() {
        return this.engmidname;
    }

    public void setEngmidname(String engmidname) {
        this.engmidname = engmidname;
    }

    public String getEngname() {
        return this.engname;
    }

    public void setEngname(String engname) {
        this.engname = engname;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCostcenter() {
        return this.costcenter;
    }

    public void setCostcenter(String costcenter) {
        this.costcenter = costcenter;
    }

    public String getChnjob() {
        return this.chnjob;
    }

    public void setChnjob(String chnjob) {
        this.chnjob = chnjob;
    }

    public String getEngjob() {
        return this.engjob;
    }

    public void setEngjob(String engjob) {
        this.engjob = engjob;
    }

    public String getEmpno() {
        return this.empno;
    }

    public void setEmpno(String empno) {
        this.empno = empno;
    }

    public String getMemberrole() {
        return this.memberrole;
    }

    public void setMemberrole(String memberrole) {
        this.memberrole = memberrole;
    }

    public Long getMembersec() {
        return this.membersec;
    }

    public void setMembersec(Long membersec) {
        this.membersec = membersec;
    }

    public String getViplevel() {
        return this.viplevel;
    }

    public void setViplevel(String viplevel) {
        this.viplevel = viplevel;
    }

    public String getChncontactaddress() {
        return this.chncontactaddress;
    }

    public void setChncontactaddress(String chncontactaddress) {
        this.chncontactaddress = chncontactaddress;
    }

    public String getEngcontactaddress() {
        return this.engcontactaddress;
    }

    public void setEngcontactaddress(String engcontactaddress) {
        this.engcontactaddress = engcontactaddress;
    }

    public String getIdcardno() {
        return this.idcardno;
    }

    public void setIdcardno(String idcardno) {
        this.idcardno = idcardno;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhonein() {
        return this.phonein;
    }

    public void setPhonein(String phonein) {
        this.phonein = phonein;
    }

    public String getPhoneout() {
        return this.phoneout;
    }

    public void setPhoneout(String phoneout) {
        this.phoneout = phoneout;
    }

    public String getEmail1() {
        return this.email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return this.email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return this.email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getFamilyphone() {
        return this.familyphone;
    }

    public void setFamilyphone(String familyphone) {
        this.familyphone = familyphone;
    }

    public String getLoveseat() {
        return this.loveseat;
    }

    public void setLoveseat(String loveseat) {
        this.loveseat = loveseat;
    }

    public String getLovefood() {
        return this.lovefood;
    }

    public void setLovefood(String lovefood) {
        this.lovefood = lovefood;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getActive() {
        return this.active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getMgcmemberno() {
        return this.mgcmemberno;
    }

    public void setMgcmemberno(String mgcmemberno) {
        this.mgcmemberno = mgcmemberno;
    }

    public Long getMainmember() {
        return mainmember;
    }

    public void setMainmember(Long mainmember) {
        this.mainmember = mainmember;
    }

    public Set getMemberCards() {
        return this.memberCards;
    }

    public void setMemberCards(Set memberCards) {
        this.memberCards = memberCards;
    }

    public Set getCommonChecks() {
        return commonChecks;
    }

    public void setCommonChecks(Set commonChecks) {
        this.commonChecks = commonChecks;
    }

    public Set getCommonLinks() {
        return commonLinks;
    }

    public void setCommonLinks(Set commonLinks) {
        this.commonLinks = commonLinks;
    }

    public Set getChildren() {
        return this.children;
    }

    public void setChildren(Set children) {
        this.children = children;
    }

    public Set getMemberCreditcards() {
        return this.memberCreditcards;
    }

    public void setMemberCreditcards(Set memberCreditcards) {
        this.memberCreditcards = memberCreditcards;
    }

    public Set getMemberFarecards() {
        return this.memberFarecards;
    }

    public void setMemberFarecards(Set memberFarecards) {
        this.memberFarecards = memberFarecards;
    }

    public Set getMemberDescriptions() {
        return this.memberDescriptions;
    }

    public void setMemberDescriptions(Set memberDescriptions) {
        this.memberDescriptions = memberDescriptions;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCompanycd() {
        return companycd;
    }

    public void setCompanycd(String companycd) {
        this.companycd = companycd;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmbranchmentName() {
        return embranchmentName;
    }

    public void setEmbranchmentName(String embranchmentName) {
        this.embranchmentName = embranchmentName;
    }

    public Long getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Long companyid) {
        this.companyid = companyid;
    }

    public Long getMemberid() {
        return memberid;
    }

    public void setMemberid(Long memberid) {
        this.memberid = memberid;
    }

    /**
     * 获取商旅会员的名称，如果有中文取中文，没有则取英文,都没有返回空串
     * 
     * @return 商旅会员的名称
     */
    public String getName() {
        String fullName = "";

        if (null != this.chnsurname && null != this.chnname) {
            fullName += this.chnsurname + this.chnname;
        } else {
            if (StringUtil.isValidStr(this.engsurname)) {
                fullName = this.engsurname;
            }

            if (StringUtil.isValidStr(this.engmidname)) {
                if (StringUtil.isValidStr(fullName)) {
                    fullName += ".";
                }
                fullName += this.engmidname;
            }

            if (StringUtil.isValidStr(this.engname)) {
                if (StringUtil.isValidStr(fullName)) {
                    fullName += ".";
                }
                fullName += this.engname;
            }
        }

        return fullName;
    }

    /**
     * 获取Email
     * @return Email
     */
    public String getEmail() {
        String email = "";
        if (StringUtil.isValidStr(this.email1)) {
            email = this.email1;
        } else if (StringUtil.isValidStr(this.email2)) {
            email = this.email2;
        } else if (StringUtil.isValidStr(this.email3)) {
            email = this.email3;
        }
        return email;
    }

}