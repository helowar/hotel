package com.mangocity.hotel.order.web;

import hk.com.cts.ctcp.hotel.constant.ResultConstant;
import hk.com.cts.ctcp.hotel.constant.TxnStatusType;
import hk.com.cts.ctcp.hotel.service.HKService;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.TxnStatusData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BasicData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.BeginData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.CalAmtData;
import hk.com.cts.ctcp.hotel.webservice.saleservice.CustInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrChannelNo;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrRemark;
import com.mangocity.util.DateUtil;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.CurrencyBean;

/**
 * 中旅订单action
 * 
 * @author chenkeming Mar 16, 2009 5:56:03 PM
 */
public class CtsOrderAction extends OrderAction {

    private HKService hkService;

    /**
     * 提交中旅订单
     * 
     * @author chenkeming Apr 16, 2009 10:06:54 AM
     * @return
     */
    public String addCtsOrder() {

        order = getOrder(orderId);
        if (null == order) {
            return forwardError("order对象为空！");
        }

        if (!order.isCtsHK()) {
            return forwardError("该订单非中旅订单！");
        }

        roleUser = getOnlineRoleUser();
        if (null == roleUser) {
            return forwardError("操作员请先登录！");
        }
        
        // 计算港币汇率
        String rateStr = CurrencyBean.rateMap.get(CurrencyBean.HKD);
        double rate = StringUtil.isValidStr(rateStr) ? Double.valueOf(rateStr) : 1.0;

        List<OrChannelNo> li = order.getChannelList();
        
        //当前中旅订单在中旅系统的状态是否已经commit
        boolean isCommit = false;
        StringBuffer strBuf = new  StringBuffer();
        for(OrChannelNo orderChannel : li){
        	strBuf.append("订单:"+orderChannel.getOrderChannel());
        	// 查询中旅订单状态
			try {
				TxnStatusData eRet = hkService.enqTxnStatus(orderChannel
						.getOrderChannel());
				int nRet = eRet.getNRet();
				if (TxnStatusType.Commited == nRet) {
					orderChannel.setStatus(TxnStatusType.Commited);
					orderChannel.setCommitTime(DateUtil.getSystemDate());
					strBuf.append("，在中旅系统已预订成功,订单状态已修改为<font color='red'>已提交</font>");
					isCommit = true;
				}else if(nRet < ResultConstant.RESULT_SUCCESS){
					strBuf.append(" 查询中旅订单状态失败，错误代码："+nRet);
					strBuf.append("，中旅系统原因请重试或联系IT！");
					isCommit = true;
				}
			} catch (Exception e) {
				log.error("查询中旅订单状态失败, 芒果订单编号:" + order.getOrderCD()
						+ ", 中旅订单号:" + orderChannel.getOrderChannel());
				log.error(e.getMessage(), e);
				strBuf.append(" 查询中旅订单状态失败");
				strBuf.append("，中旅系统原因请重试或联系IT！");
				isCommit = true;
			}
        	
        }
        //如果当前确认号在中旅系统预订成功或查询系统出错，直接返回页面提示
        if(isCommit){
        	String str = strBuf.toString();
        	addOrderLog(str);
        	str = str.replaceAll("<font color='red'>已提交</font>", "已提交");
			saveOrUpdateOrder(order);
			this.setErrorMessage(str);
        	return "ctsOrderError";
        }
        boolean bSuc = true;
        int nSize = li.size();
        OrRemark orderRemark = order.getRemark();
        String remark = "";
        if (null != orderRemark) {
            remark = order.getRemark().getHotelRemark();
        }
        int nDays = DateUtil.getDay(order.getCheckinDate(), order.getCheckoutDate());
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < nSize; i++) {
            OrChannelNo orderChannel = li.get(i);
            BeginData beginData = hkService.saleBegin();
            if (0 > beginData.getNRet()) {
                return forwardError(beginData.getSMessage());
            }
            String sTxnNo = beginData.getSTxnNo();
            orderChannel.setOrderChannel(sTxnNo);
            BasicData ret = new BasicData();
            try {
                ret = hkService.holdRoom(sTxnNo, order.getHotelId(), order.getCheckinDate(), order
                    .getCheckoutDate(), order.getRoomTypeId(), order.getChildRoomTypeId(),
                    orderChannel.getQuantity());
                if (ret.getNRet() < ResultConstant.RESULT_SUCCESS) {
                    log.error("中旅订单hold配额失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                        + orderChannel.getOrderChannel() + ", WebService错误代码: " + ret.getNRet()
                        + ", 错误信息:" + ret.getSMessage());
                    bSuc = false;
                }
            } catch (Exception e) {
                bSuc = false;
                ret.setSMessage("中旅订单hold配额出异常!");
                log.error(e.getMessage(),e);
            }

            // 如果中旅订单hold配额失败
            if (!bSuc) {
                // 回滚释放之前hold的配额
                for (int j = 0; j < i; j++) {
                    OrChannelNo rollChannel = li.get(j);
                    int status = rollChannel.getStatus();
                    if (TxnStatusType.Begin == status) {
                        try {
                            BasicData retRoll = hkService.saleRollback(rollChannel
                                .getOrderChannel());
                            if (retRoll.getNRet() < ResultConstant.RESULT_SUCCESS) {
                                log.error("释放中旅订单配额失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                                    + rollChannel.getOrderChannel() + ", WebService错误代码: "
                                    + retRoll.getNRet() + ", 错误信息:" + retRoll.getSMessage());
                            }
                        } catch (Exception e2) {
                        	log.error(e2.getMessage(),e2);
                        }
                    } else if(TxnStatusType.Commited == status) {
						sb.append(" 撤消多余中旅单(" + rollChannel.getOrderChannel()
								+ ")失败,请人工联系中旅方撤消 ");
                    }
                }
                if(0 < sb.length()) {
                	addOrderLog(sb.toString());
                	saveOrUpdateOrder(order);
                }
                return forwardError(ret.getSMessage() + sb.toString());
            } else {
                orderChannel.setStatus(TxnStatusType.Begin);

                // 添加入住人
                boolean bSucCus = true;
                String[] names = orderChannel.getFellows().split("#");
                List<CustInfo> liCust = new ArrayList<CustInfo>();
                for (int k = 0; k < names.length; k++) {
                    CustInfo custom = new CustInfo();
                    custom.setSName(names[k]);
                    custom.setSPhone("");
                    liCust.add(custom);
                }
                ret = new BasicData();
                try {
                    if (null == remark) {
                        remark = "";
                    }
                    ret = hkService.saleAddCustInfo(sTxnNo, liCust, remark);
                    if (ret.getNRet() < ResultConstant.RESULT_SUCCESS) {
                        log.error("中旅订单添加入住人信息失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
                            + orderChannel.getOrderChannel() + ", WebService错误代码: " + ret.getNRet()
                            + ", 错误信息:" + ret.getSMessage());
                        bSucCus = false;
                    }
                } catch (Exception e) {
                    bSucCus = false;
                    ret.setSMessage("中旅订单添加入住人信息出异常!");
                    log.error(e.getMessage(),e);
                }

                // 如果中旅订单添加入住人信息失败
                if (!bSucCus) {
                    // 回滚释放之前hold的配额
                    for (int j = 0; j < i; j++) {
                        OrChannelNo rollChannel = li.get(j);
                        int status = rollChannel.getStatus();
                        if (TxnStatusType.Begin == status) {
                            try {
                                BasicData retRoll = hkService.saleRollback(rollChannel
                                    .getOrderChannel());
                                if (retRoll.getNRet() < ResultConstant.RESULT_SUCCESS) {
                                    log.error("释放中旅订单配额失败, 芒果订单编号:" + order.getOrderCD()
                                        + ", 中旅订单号:" + rollChannel.getOrderChannel()
                                        + ", WebService错误代码: " + retRoll.getNRet() + ", 错误信息:"
                                        + retRoll.getSMessage());
                                }
                            } catch (Exception e2) {
                            	log.error(e2.getMessage(),e2);
                            }
                        } else if(TxnStatusType.Commited == status) {
    						sb.append(" 撤消多余中旅单(" + rollChannel.getOrderChannel()
    								+ ")失败,请人工联系中旅方撤消 ");
                        }
                    }
                    if(0 < sb.length()) {
                    	addOrderLog(sb.toString());
                    	saveOrUpdateOrder(order);
                    }
                    return forwardError(ret.getSMessage() + sb.toString());
                } 
            }
        }
        
        
   	 	// 如果添加入住人信息成功则提交中旅单        
		for (int i = 0; i < nSize; i++) {

			OrChannelNo orderChannel = li.get(i);
			BasicData ret = new BasicData();

			boolean bSucCommit = true;
			try {
				ret = hkService.saleCommit(orderChannel.getOrderChannel(),
						nDays * orderChannel.getQuantity());
				if (ret.getNRet() >= ResultConstant.RESULT_SUCCESS) {
					orderChannel.setStatus(ret.getNRet());
				}
			} catch (Exception e1) { // 如果提交出异常
				ret.setNRet(ResultConstant.RESULT_FAIL);
				log.error(e1.getMessage(), e1);
				bSucCommit = false;
			}

			// 如果提交返回错误
			if (bSucCommit && ret.getNRet() < ResultConstant.RESULT_SUCCESS) {
				bSucCommit = false;
				log.error("提交中旅订单失败, 芒果订单编号:" + order.getOrderCD() + ", 中旅订单号:"
						+ orderChannel.getOrderChannel() + ", WebService错误代码: "
						+ ret.getNRet() + ", 错误信息:" + ret.getSMessage());
			}

			// 查询中旅订单状态
			try {
				Thread.sleep(100); // 先等0.1秒再查询
				TxnStatusData eRet = hkService.enqTxnStatus(orderChannel
						.getOrderChannel());
				int nRet = eRet.getNRet();
				if (TxnStatusType.Rollbacked == nRet
						|| TxnStatusType.Commited != nRet) {
					bSucCommit = false;
				} else if (nRet < ResultConstant.RESULT_SUCCESS) {
					bSucCommit = false;
				}
			} catch (Exception e) {
				log.error("提交中旅订单后查询中旅订单状态失败, 芒果订单编号:" + order.getOrderCD()
						+ ", 中旅订单号:" + orderChannel.getOrderChannel());
				log.error(e.getMessage(), e);
				bSucCommit = false;
			}

			// 如果提交失败回滚中旅订单
			if (!bSucCommit) {
				for (int j = 0; j < i; j++) {
					OrChannelNo rollChannel = li.get(j);
					if (TxnStatusType.Commited != rollChannel.getStatus()) {
						try {
							BasicData retRoll = hkService.saleRollback(rollChannel
									.getOrderChannel());
							if (retRoll.getNRet() < ResultConstant.RESULT_SUCCESS) {
								log.error("回滚中旅订单失败, 芒果订单编号:" + order.getOrderCD()
										+ ", 中旅订单号:"
										+ rollChannel.getOrderChannel()
										+ ", WebService错误代码: " + retRoll.getNRet()
										+ ", 错误信息:" + retRoll.getSMessage());
							}
						} catch (Exception e2) {
							log.error(e2.getMessage(), e2);
						} finally {
							rollChannel.setStatus(TxnStatusType.Rollbacked);
						}	
					} else {
						sb.append(" 撤消多余中旅单(" + rollChannel.getOrderChannel()
								+ ")失败,请人工联系中旅方撤消 ");						
					}
				}

				// 增加操作日志
				addOrderLog("中台下订单到中旅失败" + sb.toString());
				saveOrUpdateOrder(order);
				return forwardError("中台下订单到中旅失败" + sb.toString());
			} else { // 提交中旅单成功
				orderChannel.setStatus(TxnStatusType.Commited);
				orderChannel.setCommitTime(new Date()); // 设置提交成功时间

				// 查询中旅单金额
				try {
					CalAmtData amtData = hkService.saleCalAmt(orderChannel
							.getOrderChannel());
					orderChannel.setOrderValRmb(BigDecimal.valueOf(
							amtData.getNNetAmt() * rate).setScale(0,
							BigDecimal.ROUND_HALF_UP).doubleValue());
				} catch (Exception e) {
					log.error("查询中旅订单净金额失败, 芒果订单编号:" + order.getOrderCD()
							+ ", 中旅订单号:" + orderChannel.getOrderChannel());
					log.error(e.getMessage(), e);
				}

			}

		}

        // 增加操作日志
        addOrderLog("中台下订单到中旅成功");
        saveOrUpdateOrder(order);
        return "SUCCESS";
    }
    
    /**
     * 
     * 增加操作日志
     * 
     * @param msg
     */
    private void addOrderLog(String msg) {
        OrHandleLog handleLog = new OrHandleLog();
        handleLog.setOrder(order);
        handleLog.setModifiedTime(new Date());
        handleLog.setModifierName(roleUser.getName());
        handleLog.setModifierRole(roleUser.getLoginName());
        order.getLogList().add(handleLog);
        handleLog.setContent(msg);
    }

    public HKService getHkService() {
        return hkService;
    }

    public void setHkService(HKService hkService) {
        this.hkService = hkService;
    }

}