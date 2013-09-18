package com.mangocity.hotel.base.constant;

/**
 * 本部取消修改条款类型
 * 
 * @author lihaibo
 * 
 */
public interface ClaueType {

    /**
     * 担保取消修改条款类型
     */
    String ASSURE_TYPE_ONE = "1";
    String ASSURE_TYPE_TWO = "2";
    String ASSURE_TYPE_THREE = "3";
    String ASSURE_TYPE_FORV = "4";
    String ASSURE_TYPE_FIVE = "5";
    int ASSURE_TYPE_ONE_INT = 1;
    int ASSURE_TYPE_TWO_INT = 2;
    int ASSURE_TYPE_THREE_INT = 3;
    int ASSURE_TYPE_FORV_INT = 4;
    int ASSURE_TYPE_FIVE_INT = 5; 

    /**
     * 预付取消修改条款类型
     */
    String PREPAY_TYPE_ONE = "1";
    String PREPAY_TYPE_TWO = "2";
    String PREPAY_TYPE_THREE = "3";
    String PREPAY_TYPE_FORV = "4";
    String PREPAY_TYPE_FIVE = "5";
    int PREPAY_TYPE_ONE_INT = 1;
    int PREPAY_TYPE_TWO_INT = 2;
    int PREPAY_TYPE_THREE_INT = 3;
    int PREPAY_TYPE_FOUR_INT = 4;
    int PREPAY_TYPE_FIVE_INT = 5;
    

    /**
     * 是否绑定
     */
    String BINDING_TRUE = "1"; // 绑定

    String BINDING_FALSE = "0";// 解除绑定

    /**
     * 标记是否是单条款，多条款，按日期绑定条款
     */
    String CLAUS_ONLY = "1";

    String CLAUS_MANY = "2";

    String CLAUS_DATE = "3";

    // add by shengwei.zuo 2009-04-14 预定条款模板标记
    String CLAUS_TMPLT = "4";

}
