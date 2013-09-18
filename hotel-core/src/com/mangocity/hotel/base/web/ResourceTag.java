package com.mangocity.hotel.base.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.mangocity.hotel.base.resource.ResourceManager;
import com.mangocity.util.StringUtil;
import com.mangocity.util.collections.OrderedMap;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.web.WebUtil;

/**
 * 资源标签，用于格式化页面数据，将ID转化成名称之类的
 * 
 * @author zhengxin
 * 
 */
public class ResourceTag extends BodyTagSupport implements Serializable {
    private String name;

    private String param;

    private String value;

    protected Map params;

    protected void writeHtml(JspWriter out, String html) throws IOException {
        if ((-1 == html.indexOf('<')) && (-1 == html.indexOf('>')) && (-1 == html.indexOf('&'))) {
            out.print(html);
        } else {
            int len = html.length();
            for (int i = 0; i < len; i++) {
                char c = html.charAt(i);
                if ('<' == c) {
                    out.print("<");
                } else if ('>' == c) {
                    out.print(">");
                } else if ('&' == c) {
                    out.print("&");
                } else {
                    out.print(c);
                }
            }
        }
    }

    private void init() {
        value = null;
        params = null;
    }

    protected Map getParams() {
        return params;
    }

    public void addParams(String name, String value) {
        params.put(name, value);
    }

    @Override
    public int doStartTag() throws JspException {
        params = new HashMap();
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            ResourceManager resourceManager = (ResourceManager) WebUtil.getBean(pageContext
                .getServletContext(), "resourceManager");

            Map resourceMap = new OrderedMap();
            Object obj = null;
            if (name.equals("resource_boolean")) {
                if ("true".equals(value)) {
                    value = "1";
                } else if ("false".equals(value))
                    value = "0";
            }
            if (BaseConstant.COMMON_DATA_FLAG.equalsIgnoreCase(name)) {
                if (StringUtil.isValidStr(param)) {
                    obj = getValueByKey(param.trim(), value.trim());
                } else {
                    resourceMap = resourceManager.getResourceMapByParam(name, getParams());
                    obj = getValueByKey(value, resourceMap);
                }
            } else if (InitServlet.mapHotelCommmonObj.containsKey(name)) {
                obj = getValueByKey(value, InitServlet.mapHotelCommmonObj.get(name));
                String temp = null == obj ? "" : obj.toString();
                if (0 == temp.trim().length()) {
                    resourceMap = resourceManager.getResourceMapByParam(name, getParams());
                    obj = getValueByKey(value, resourceMap);
                }
            } else {
                resourceMap = resourceManager.getResourceMapByParam(name, getParams());
                obj = getValueByKey(value, resourceMap);
            }

            String paramValue = null == obj ? "" : obj.toString();

            writeHtml(pageContext.getOut(), paramValue);

            return SKIP_BODY;
        } catch (java.io.IOException ioe) {
            throw new JspException(ioe);
        }
    }

    @Override
    public void release() {
        init();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 根据酒店基础数据类型的 key 返回 value 主要是针对 key 封装时存在逗号的情况
     * 
     * @param keyParam
     *            String
     * @param mapParam
     *            Map
     * @return String
     */
    protected String getValueByKey(String keyParam, Map mapParam) {
        StringBuffer buffer = new StringBuffer();
        String tempKeyStr = keyParam;
        Map tempMapObj = mapParam;
        String[] keyStrArray = tempKeyStr.split(",");
        for (int m = 0; m < keyStrArray.length; m++) {
            if (StringUtil.isValidStr(keyStrArray[m])) {
                if (tempMapObj.containsKey(keyStrArray[m])) {
                    String descr = (String) tempMapObj.get(keyStrArray[m]);
                    if (StringUtil.isValidStr(descr)) {
                        buffer.append(descr).append(",");
                    }
                }
            }
        }
        int index = buffer.lastIndexOf(",");
        if (0 <= index) {
            buffer.deleteCharAt(index);
        }
        return buffer.toString();
    }

    protected String getValueByKey(String property, String value) {
        if (BaseConstant.COUNTRY_FLAG.equalsIgnoreCase(property)) {
            if (InitServlet.CountryObj.containsKey(value)) {
                return InitServlet.CountryObj.get(value);
            }
        } else if (BaseConstant.PROVINCE_FLAG.equalsIgnoreCase(property)
            || "STATE".equalsIgnoreCase(property)) {
            if (InitServlet.ProvinceObj.containsKey(value)) {
                return InitServlet.ProvinceObj.get(value);
            }
        } else if (BaseConstant.CITY_FLAG.equalsIgnoreCase(property)
            || "CITYID".equalsIgnoreCase(property)) {
            if (InitServlet.cityObj.containsKey(value)) {
                return InitServlet.cityObj.get(value);
            }
        } else if (BaseConstant.DISTRICT_FLAG.equalsIgnoreCase(property)
            || "ZONE".equalsIgnoreCase(property)) {
            if (InitServlet.citySozeObj.containsKey(value)) {
                return InitServlet.citySozeObj.get(value);
            }
        } else if (BaseConstant.BUSINESS_FLAG.equalsIgnoreCase(property)
            || "BIZZONE".equalsIgnoreCase(property) || "BIZ_ZONE".equalsIgnoreCase(property)) {
            if (InitServlet.businessSozeObj.containsKey(value)) {
                return InitServlet.businessSozeObj.get(value);
            }
        } else if (BaseConstant.SALOON_FLAG.equalsIgnoreCase(property)) {
            if (InitServlet.saloonSozeObj.containsKey(value)) {
                return InitServlet.saloonSozeObj.get(value);
            }
        } else if (BaseConstant.DELIVERY_CITY_FLAG.equalsIgnoreCase(property)) {
            if (InitServlet.diliveryCityObj.containsKey(value)) {
                return InitServlet.diliveryCityObj.get(value);
            }
        } else if (BaseConstant.DELIVERY_CITY_DISTRICT_FLAG.equalsIgnoreCase(property)) {
            if (InitServlet.diliveryCityZoneObj.containsKey(value)) {
                return InitServlet.diliveryCityZoneObj.get(value);
            }
        }
        return "";
    }

}
