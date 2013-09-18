package com.mangocity.hotel.base.web.webwork;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 异常捕捉Action 将异常打印出来， 获取错误信息，并跳往错误页面
 * 
 * @author zhengxin
 * 
 */
public class ExceptionHandleAction extends GenericAction {

    @Override
    public String execute() {

        String errorMessage = null;
        try {

            ValueStack valueStack = ActionContext.getContext().getValueStack();

            /**
             * 从堆栈中取出Exception对象
             */
            Exception e = (Exception) request.getAttribute("exception");

            if (null == e && null != valueStack)
                e = (Exception) valueStack.findValue("exception");

            if (null != e) {

            	log.error(e);
                errorMessage = e.toString();
            } else {
                errorMessage = "发生错误，但没有收到异常！";
            }

            return super.forwardError(errorMessage);
        } catch (Exception e) {
            /** 捕捉此异常，主要是为了防御，以防Action出现内部异常，导至于循环调用 */
            errorMessage = "ExceptionHandleAction发生内部异常:" + e;

            return super.forwardError(errorMessage);

        }
    }

}