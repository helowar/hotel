package com.mangocity.hotel.base.web;

import java.io.Serializable;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;


/**
 */
public class ParamTag extends BodyTagSupport implements Serializable {

    protected String name;

    protected String value;

    public ParamTag() {
        super();
        init();
    }

    private void init() {
        value = null;
        name = value;
    }

    @Override
    public int doEndTag() throws JspException {
        Tag t = findAncestorWithClass(this, ResourceTag.class);
        if (null == t)
            throw new JspTagException("no found");

        // take no action for null or empty names
        if (null == name || name.equals(""))
            return EVAL_PAGE;

        // send the parameter to the appropriate ancestor
        String value = this.value;
        if (null == value) {
            if (null == bodyContent || null == bodyContent.getString())
                value = "";
            else
                value = bodyContent.getString().trim();
        }

        ResourceTag rt = (ResourceTag) t;

        rt.addParams(name, value);

        /**
         * if (encode) { // FIXME: revert to java.net.URLEncoder.encode(s, enc) once // we have a
         * dependency on J2SE 1.4+. String enc = pageContext.getResponse().getCharacterEncoding();
         * parent.addParameter(Util.URLEncode(name, enc), Util.URLEncode( value, enc)); } else {
         * parent.addParameter(name, value); }
         * 
         */
        return EVAL_PAGE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
