package com.mangocity.hotel.base.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.ecside.core.TableModel;
import org.ecside.core.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;

import com.mangocity.hotel.base.resource.IResourceDescriptor;
import com.mangocity.hotel.base.resource.ResourceDescriptorFactory;
import com.mangocity.util.StringUtil;
import com.mangocity.util.web.WebUtil;

/**
 * 处理酒店房型的cell
 * 
 * @author xiaowumi
 * 
 */
public class HotelRoomTypeCell extends AbstractCell implements Serializable {

    @Override
    protected String getCellValue(TableModel model, Column column) {
        String value = column.getPropertyValueAsString();
        column.getParse();
        if (!StringUtil.isValidStr(value))
            return "";

        ServletContext servletContext = model.getContext().getServletContext();
        ResourceDescriptorFactory resourceFactory = (ResourceDescriptorFactory) WebUtil.getBean(
            servletContext, "resourceFactory");

        if (null == resourceFactory)
            return "服务器加载错误!";

        String strFormat = column.getFormat();
        if (!StringUtil.isValidStr(strFormat))
            return "format属性不能为空，且必须是资源ID,hotelId,roomTypeId！";
        // 参数是以'resourceId,hotelId,roomTypeId'传入
        String[] params = strFormat.split(",");

        String resourceID = params[0];

        IResourceDescriptor descriptor = resourceFactory.getResouceDescriptor(resourceID);

        HashMap map = new HashMap();
        map.put("roomTypeId", value);
        Map resultMap = descriptor.getDescription(map);

        String descr = (String) resultMap.get(value);

        return descr;
    }

}
