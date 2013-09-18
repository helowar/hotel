package com.mangocity.util.ecside;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.ecside.core.TableModel;
import org.ecside.core.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;

/**
 */
public class LogicCell extends AbstractCell implements Serializable {

    protected String getCellValue(TableModel model, Column column) {
        String value = column.getPropertyValueAsString();
        String logicStr = column.getParse();

        if (StringUtils.isNotBlank(value)) {
            boolean logic = false;
            if (value.equals("true") || value.equals("1") || value.equals("y")) {
                logic = true;
            }
            String[] boolStr = logicStr.split(";");

            if (logic) {
                value = boolStr[0];
            } else {
                value = boolStr[1];
            }
        }

        return value;
    }
}
