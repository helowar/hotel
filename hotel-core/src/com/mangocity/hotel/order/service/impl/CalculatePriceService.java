package com.mangocity.hotel.order.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.mangocity.hotel.base.constant.HotelCalcuAssuAmoType;
import com.mangocity.hotel.base.manage.ClauseManage;
import com.mangocity.hotel.base.persistence.HtlBookCaulClause;
import com.mangocity.hotel.base.persistence.HtlBookModifyField;
import com.mangocity.hotel.base.persistence.OrGuaranteeItem;
import com.mangocity.hotel.base.service.assistant.ReservationAssist;
import com.mangocity.hotel.order.constant.GuaranteeType;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.service.IcalculatePriceService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.ValidationUtil;

/**
 * @author zhuangzhineng
 * 
 */
public class CalculatePriceService implements IcalculatePriceService {

    /**
     * 预订条款相关管理接口 
     */
    private ClauseManage clauseManage;

    /**
     * 计算订单担保金额
     * 
     * @return
     */
    public double getcaluclateVouchPrice(ReservationAssist reservationAssist, OrOrder orOrder) {
        // 获取酒店的修改字段和担保计算规则
    	resetClauseRuleModifyFieldOfOrOrder(orOrder);
    	
        String calcuType = orOrder.getReservation().getClauseRule();
        if(reservationAssist == null || ValidationUtil.isEmpty(calcuType)) {
        	return 0.0D;
        }
        
        //计算担保金额
        double vouchAmount = countVouchAmount(orOrder, calcuType);
        return roundPrice(vouchAmount);
    }

	private double countVouchAmount(OrOrder orOrder, String calcuType) {
		List<OrGuaranteeItem> orGuaranteeItemList = orOrder.getReservation().getGuarantees();
		List<OrPriceDetail> orPriceDetailList = orOrder.getPriceList();// 取得每天的价格详情
		double vouchAmount = 0.0D;
		double fristDayPrice = 0.0D;
		boolean readFlag = true;            
		
		for (OrGuaranteeItem orGuaranteeItem : orGuaranteeItemList) {
		    for (OrPriceDetail orPriceDetail : orPriceDetailList) {
		        if (0 == DateUtil.compare(orPriceDetail.getNight(), orOrder.getCheckinDate())) {
		            fristDayPrice = orPriceDetail.getSalePrice();
		        }
		        
		        if (!(DateUtil.isSameDate(orGuaranteeItem.getNight(), orPriceDetail.getNight()))) {
		        	continue;
		        }
		        
		        // 当为按checkInDate方式计算: 如果第一天要担保，只返回第一天的价格
		        if ((String.valueOf(HotelCalcuAssuAmoType.CHECKIN)).equals(calcuType)
		            && 0 == DateUtil.compare((orGuaranteeItem.getNight()),orOrder.getCheckinDate())) {
		        	vouchAmount = orPriceDetail.getSalePrice();
		            vouchAmount = roundPrice(vouchAmount);
		            return vouchAmount;
		        }
		        // 当为按全额的方式: 有一天是全额则返回整个订单的金额
		        else if ((String.valueOf(HotelCalcuAssuAmoType.ALLAMOUNT)).equals(calcuType)) {
		            if (String.valueOf(GuaranteeType.ALL_DAY).equals(orGuaranteeItem.getAssureType())) {
		                vouchAmount = orOrder.getSum();
		                vouchAmount = roundPrice(vouchAmount);
		                return vouchAmount;
		            } else {
		                vouchAmount = fristDayPrice;
		            }
		        }
		        // 当为按累加的方式: 即使有一天全额也是按天累加
		        else {
		            // 累加方式bug 有天有担保条款，并且是第一天或者是全额担保计算bug
		            if (String.valueOf(GuaranteeType.ALL_DAY).equals(orGuaranteeItem.getAssureType())) {
		                vouchAmount += orPriceDetail.getSalePrice();
		                readFlag = false;
		            } else if (readFlag) {
		                vouchAmount = fristDayPrice;
		                readFlag = false;
		            }
		        }
		    }
		}
		
		return vouchAmount;
	}

	private void resetClauseRuleModifyFieldOfOrOrder(OrOrder orOrder) {
		// 按酒店id和时间段查询计算规则 
        List<HtlBookCaulClause> htlBookCaulClauseList = clauseManage.searchBookCaulByDateRange(
            orOrder.getHotelId(), orOrder.getCheckinDate(), orOrder.getCheckoutDate());        

        // 判断该酒店下是否设置了预定条款计算规则。
        if (ValidationUtil.isEmpty(htlBookCaulClauseList)) {
            orOrder.getReservation().setClauseRule("3");
            orOrder.getReservation().setModifyField("3");
        } else {
            // 取出计算规则中最严格的计算规则 
        	String calcuType = clauseManage.drawoutHtlBookCaulClause(htlBookCaulClauseList);
            orOrder.getReservation().setClauseRule(calcuType);
            
            // 根据酒店id查询修改字段定义，返回一条记录 
            HtlBookModifyField htlBookModifyField = clauseManage.searchBookModifyFieldByHTLID(orOrder.getHotelId());
            if (htlBookModifyField != null) {
            	// 改用HtlBookModifyField.modifyField存储修改字段定义 
                orOrder.getReservation().setModifyField(htlBookModifyField.getModifyField());
            }            
        }
	}

    /**
     * 对担保金额四舍五入 ，精确到两位小数
     * 
     * @param vouchAmountIn
     * @return
     */
    private double roundPrice(double vouchAmountIn) {
        double vouchAmountOut = BigDecimal.valueOf(vouchAmountIn).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return vouchAmountOut;
    }

    public void setClauseManage(ClauseManage clauseManage) {
        this.clauseManage = clauseManage;
    }

}
