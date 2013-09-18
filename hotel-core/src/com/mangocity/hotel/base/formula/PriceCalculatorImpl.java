package com.mangocity.hotel.base.formula;

import java.io.Serializable;
import java.util.Map;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;

import com.mangocity.hotel.base.manage.assistant.InputPrice;
import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.exception.ConfigException;
import com.mangocity.util.log.MyLog;

/**
 * @author zhengxin 价格计算器的BeanShell实现
 * 
 *         公式以BeanShell脚本的形式，存储。
 * 
 *         由计算器根据公式ID，调用不同的脚本，即不同的计算公式计算，并将计算结果，赋给InputPrice类.
 * 
 *         InputPrice，即做输入计算数据之用，又接收计算结果。
 */
public class PriceCalculatorImpl implements IPriceCalculator, Serializable {

	private static final MyLog log = MyLog.getLogger(PriceCalculatorImpl.class);

    private ResourceManager resourceManager;// parasoft-suppress SERIAL.NSFSC "暂不修改" 

    /** 由spring来配置，不在代码中写死 */
    private String resourceID;

    public void compute(InputPrice price) {

        if (!StringUtil.isValidStr(resourceID))
            throw new ConfigException("没有配置公式脚本！");

        Map formulas = resourceManager.getDescription(resourceID);

        String script = (String) formulas.get(price.getFormulaId());

        if (!StringUtil.isValidStr(script))
            throw new ConfigException("没有对应的公式计算脚本：" + price.getFormulaId());

        Interpreter i = new Interpreter();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            if (null != loader) {
                i.setClassLoader(loader);
            }

            i.set("price", price);

            i.eval(script);

        } catch (TargetError targetError) {
        	log.error(targetError.getMessage(),targetError);
            Map info = MyBeanUtil.describe(price);

            String message = "发生错误：" + targetError.getMessage() + "\r\n数据：" + info
                + "，\r\n不能执行价格计算公式，请检查公式脚本:\r\n" + script;

            log.error(message);
            throw new ConfigException(message);
        } catch (EvalError e) {
        	log.error(e.getMessage(),e);
            Map info = MyBeanUtil.describe(price);

            String message = "发生错误：" + e.getMessage() + "\r\n数据：" + info
                + "，\r\n不能执行价格计算公式，请检查公式脚本:\r\n" + script;
            log.error(message);

            throw new ConfigException(message);
        } finally {
            if (null != loader) {
                i.setClassLoader(null);
            }
        }
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

}
