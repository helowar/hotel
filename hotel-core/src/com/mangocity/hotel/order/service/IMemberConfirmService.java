package com.mangocity.hotel.order.service;

/**
 * 处理给客人发短信的业务逻辑接口
 * 
 * @author chenjiajie
 * @version V2.7.1
 * 
 */
public interface IMemberConfirmService {

    /**
     * 查询某订单发送给客人短信成功或失败已确认的记录数
     * 
     * @param orderId
     * @return
     */
    public int getSuccessedConfirmNum(Long orderId);

    /**
     * 查询某订单发送给客人短信的记录数
     * 
     * @param orderId
     * @return
     */
    public int getConfirmNum(Long orderId);

    /**
     * 更新短信状态
     * 
     * @param memberConfirmId
     * @param status
     */
    public void updateSmsStatus(Long memberConfirmId, int status);

    /**
     * 更新短信备注
     * 
     * @param hidMemberConfirmNotes
     *            : Action从界面获取的组合字符串,格式为：ID1#备注1|ID2#备注2
     */
    public void updateSmsNotes(String hidMemberConfirmNotes);
}
