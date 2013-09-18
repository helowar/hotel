package com.mangocity.hotel114.util;

import java.io.Serializable;

/**
 */
public class CategoryInfo implements Serializable {

    /**
     */
    public static interface WEB_PAYMETHOD {
        public static final String EMAIL_HTML_APPEND = "";// <table cellspacing='1' cellpadding='0'
                                                          // width='544' align='center'
                                                          // border='0'><tr><td
                                                          // background='images/pic_bg3.jpg'
                                                          // height='34'><img height='24'
                                                          // src='images/fligt_icon.gif' width='24'
                                     // align='absMiddle'><strong>特别提示</strong></td></tr><tr><td>";

        public static final String EMAIL_HTML_VAR = "预订成功，订单编号为：";

        public static final String EMAIL_HTEML_END = "";// </td></tr></table>";

        public static final String MESSAGE_PREVIOUS = "您的订单已经提交,同时我们会以";

        public static final String MESSAGE_LAST = "方式与您确认,当您收到我们的确认信息即表示你的预订成功!";

        public static final String ORDER_TITLE = "订单资料";

        public static final String ORDER_AMOUNT = "订单的总金额为:";

        public static final String INQUIRE = "!<br><br>"
            + "如需更改订单，请致电芒果网客服中心40066-40066咨询，将有专人为您服务。";

        public static final String PAID_SUCCESS_MESSAGE = "支付成功！！请及时查收确认信息。" +
                "                                            <br>&nbsp;&nbsp;&nbsp;&nbsp;订单编号：";

        public static final String PAID_FAILED_MESSAGE = "对不起，在线支付不成功,"
            + "同时我们已经保留您的订单，您可选择以下服务或直接致电芒果网客服中心40066-40066进行预订或稍后有专人回复，谢谢您的支持！";

        public static final String DEDUCT_QUOTA_FAIL_MESSAGE = "请致电芒果网客服中心40066-40066咨询，将有专人为您服务。";

        public static final String NO_FELLOW_NAME = "您没有输入入住人姓名，请重新输入预订酒店。";
    }

    /**
     */
    public static interface TELCOM_114_PAYMETHOD {
        public static final String EMAIL_HTML_APPEND = "";

        public static final String EMAIL_HTML_VAR = "预订成功，订单编号为：";

        public static final String EMAIL_HTEML_END = "</td></tr></table>";

        public static final String MESSAGE_PREVIOUS = "您的订单已经提交,同时我们会以";

        public static final String MESSAGE_LAST = "方式与您确认,当您收到<br>我们的确认信息即表示你的预订成功!";

        public static final String ORDER_TITLE = "订单资料";

        public static final String ORDER_AMOUNT = "订单的总金额为:";

        public static final String INQUIRE = "!<br><br>" + "如需更改订单，请致电";

        public static final String INQUIREEND = "客服中心咨询，将有专人为您服务。";
    }

    /**
     */
    public static interface WEB_CMB_PAYMENT {

        public static final String TITLE = "芒果提示";

        public static final String MESSAGE = "系统正忙,请稍后再试或直接致电芒果网客服中心40066-40066咨询!";
    }

    /**
     */
    public static interface TELCOM114_CMB_PAYMENT {

        public static final String TITLE = "114提示";

        public static final String MESSAGE = "系统正忙,请稍后再试或直接致电114客服中心咨询!";
    }

    /**
     */
    public static interface HOTEL_GROUP_BOOK {

        public static final String TITLE = "芒果提示";

        public static final String MESSAGE = "发送客户集体订房请求邮件成功";

        public static final String ERR_MESSAGE = "发送客户集体订房请求邮件失败";
    }
}
