package com.mangocity.util.ecside;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Locale;

import oracle.sql.DATE;
import oracle.sql.TIMESTAMP;

import org.apache.commons.lang.StringUtils;
import org.ecside.common.util.ExtremeUtils;
import org.ecside.core.TableModel;
import org.ecside.core.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;

import com.mangocity.util.log.MyLog;

/**
 */
public class DateCell extends AbstractCell implements Serializable {
	private static final MyLog log = MyLog.getLogger(DateCell.class);
    protected String getCellValue(TableModel model, Column column) {

        Object obj = column.getPropertyValue();

        if (obj instanceof TIMESTAMP) {
            TIMESTAMP oracleObj = (TIMESTAMP) obj;

            try {
                obj = oracleObj.timestampValue();
            } catch (SQLException e) {
            	log.error(e);
            }
        }

        if (obj instanceof DATE) {
            DATE oracleObj = (DATE) obj;

            obj = oracleObj.timestampValue();
        }

        String value = column.getPropertyValueAsString();
        if (StringUtils.isNotBlank(value)) {
            Locale locale = model.getLocale();
            value = ExtremeUtils.formatDate(column.getParse(), column.getFormat(), obj, locale);
        }

        return value;
    }

}
