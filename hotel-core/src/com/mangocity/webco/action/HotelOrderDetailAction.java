package com.mangocity.webco.action;

import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.webnew.util.action.GenericWebAction;

/**
 * 订单详情查看Action处理
 * @author chenjiajie
 *
 */
public class HotelOrderDetailAction extends GenericWebAction {

    /**
	 * 
	 */
	private static final long serialVersionUID = 438439991370285928L;
	
	/**
	 * Action主方法
	 */
	public String execute() {
		super.fillOrderBaseInfo();
		order = super.getOrder(orderId);
		int orderStatus = 0;
		if(null != order){
			//未提交 1
			if(OrderState.NOT_SUBMIT == order.getOrderState()){
				orderStatus = OrderState.NOT_SUBMIT;
			}
			//已提交，待处理 2
			else if(OrderState.HAS_SUBMIT == order.getOrderState()){
				orderStatus = OrderState.HAS_SUBMIT;
			}
			//处理中 3
			else if(OrderState.SUBMIT_TO_MID == order.getOrderState()){
				orderStatus = OrderState.SUBMIT_TO_MID;
			}
			//预订成功 4
			else if(OrderState.HAS_SUBMIT == order.getOrderState()){
				orderStatus = OrderState.HAS_SUBMIT;
			}
			//交易成功 5
			else if(OrderState.HAS_SUBMIT < order.getOrderState()
					&& OrderState.NOSHOW >= order.getOrderState()){
				orderStatus = 5;
			}
			//交易取消 6
			else if(OrderState.CANCEL == order.getOrderState()){
				orderStatus = 6;
			}
		}else{
			orderStatus = 1;
		}
		request.setAttribute("orderStatus", orderStatus);
		return SUCCESS;
	}

	/** getter and setter **/
}
