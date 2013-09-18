package com.mangocity.hotel.base.web.webwork;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

/**
 */
public class JSTLRequestWrapper// parasoft-suppress SERIAL.SNNAC "类名修改牵涉太广，暂不修改。"
        extends HttpServletRequestWrapper 
                                 implements Serializable {

    /**
     *JTEST 静态分析serialization是引用JAR包中没 Serializable
     */
    private static final long serialVersionUID = 7106345501127083559L;

    public JSTLRequestWrapper(HttpServletRequest arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Object getAttribute(String key) {
        Object result = super.getAttribute(key);

        if (null == result) {
            ValueStack valueStack = ActionContext.getContext().getValueStack();

            if (null != valueStack)
                result = valueStack.findValue(key);
        }

        return result;
    }

}
