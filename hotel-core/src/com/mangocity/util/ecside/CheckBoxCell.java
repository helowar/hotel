package com.mangocity.util.ecside;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.ecside.common.util.HtmlBuilder;
import org.ecside.core.TableModel;
import org.ecside.core.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;

/**
 */
public class CheckBoxCell extends AbstractCell implements Serializable {

    protected String getCellValue(TableModel model, Column column) {
        String value = column.getPropertyValueAsString();
        String checkBoxName = column.getParse();
        HtmlBuilder build = new HtmlBuilder();

        if (StringUtils.isNotBlank(value)) {
            build.input("checkbox").name(checkBoxName).value(value).append("/>");

        }

        return build.toString();
    }

}
