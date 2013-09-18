package com.mangocity.hotel.base.web;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.ServletContext;

import org.ecside.core.TableModel;
import org.ecside.core.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;

import com.mangocity.hotel.base.resource.IResourceDescriptor;
import com.mangocity.hotel.base.resource.ResourceDescriptorFactory;
import com.mangocity.util.StringUtil;
import com.mangocity.util.hotel.constant.BaseConstant;
import com.mangocity.util.web.WebUtil;

/**
 */
public class ResourceCell extends AbstractCell implements Serializable {

    protected String getCellValue(TableModel model, Column column) {
        String value = column.getPropertyValueAsString();
        column.getParse();

        if (!StringUtil.isValidStr(value))
            return "";
        /*
         * if (!StringUtil.isValidStr(logicStr)) { log.error("parse tag is not allowed empty!");
         * return "parse tag is not allowed empty!"; }
         */

        ServletContext servletContext = model.getContext().getServletContext();
        ResourceDescriptorFactory resourceFactory = (ResourceDescriptorFactory) WebUtil.getBean(
            servletContext, "resourceFactory");

        if (null == resourceFactory)
            return "服务器加载错误!";

        Map resource = null;
        IResourceDescriptor descriptor = null;

        String resourceID = column.getFormat();
        String property = column.getProperty();
        if (!StringUtil.isValidStr(resourceID)) {
            return "format属性不能为空，且必须是资源ID！";
        } else if (BaseConstant.COMMON_DATA_FLAG.equalsIgnoreCase(resourceID)) {
            if (StringUtil.isValidStr(property)) {
                return getValueByKey(property, value);
            }
        } else if (InitServlet.mapHotelCommmonObj.containsKey(resourceID)) {
            resource = InitServlet.mapHotelCommmonObj.get(resourceID);
            String temp = getValueByKey(value, resource);
            temp = null == temp ? "" : temp;
            if (0 == temp.trim().length()) {
                descriptor = resourceFactory.getResouceDescriptor(resourceID);
                if (null == descriptor) {
                    return "找不到此资源数据，资源ID:" + resourceID;
                }
                resource = descriptor.getDescription();
                // Map descriptioin = descriptor.getDescr()
            }
        } else {
            descriptor = resourceFactory.getResouceDescriptor(resourceID);
            if (null == descriptor) {
                return "找不到此资源数据，资源ID:" + resourceID;
            }
            resource = descriptor.getDescription();
            // Map descriptioin = descriptor.getDescr()
        }
        return getValueByKey(value, resource);
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
