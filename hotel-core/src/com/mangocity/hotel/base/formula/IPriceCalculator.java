package com.mangocity.hotel.base.formula;

import com.mangocity.hotel.base.manage.assistant.InputPrice;

/**
 * 价格计算器
 * 
 * @author zhengxin 读取公式脚本，根据公式和输入数据（InputPrice），计算结果。
 */
public interface IPriceCalculator {

    /**
     * 
     * @param price
     *            输入的价格数据
     */
    public void compute(InputPrice price);

}