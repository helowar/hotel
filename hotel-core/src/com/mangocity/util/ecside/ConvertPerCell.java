package com.mangocity.util.ecside;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.ecside.common.util.ExtremeUtils;
import org.ecside.core.TableModel;
import org.ecside.core.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;

/**
 */
public class ConvertPerCell extends AbstractCell implements Serializable {

    protected String getCellValue(TableModel model, Column column) {
        String value = column.getPropertyValueAsString();
        if (StringUtils.isNotBlank(value)) {
            Locale locale = model.getLocale();
            value = ExtremeUtils.formatNumber("###0.00", value, locale) + "%";
        }

        return value;
    }
}
