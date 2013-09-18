package com.mangocity.util.dao.paging;

import java.io.Serializable;

import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.mangocity.util.log.MyLog;

/**
 * 替换select count(*)语句
 * 
 * @author chenkeming
 * 
 */
public class IBatisSqlExecutor extends SqlExecutor implements Serializable {

	private static final long serialVersionUID = -5320917941755236158L;
	private static final MyLog log = MyLog.getLogger(IBatisSqlExecutor.class);
	@Override
    public String getTotalCountSql(String sql) {
        // int fromIndex = sql.toLowerCase().indexOf("from");
        StringBuffer countStr = new StringBuffer("select count(*) as rowcount from ( ");
        // int sql_orderby = sql.indexOf("order by");
        // if(sql_orderby > 0)
        // countStr.append(sql.substring(fromIndex, sql_orderby));
        // else
        countStr.append(sql.trim()).append(" ) ");

        String strSql = countStr.toString();
        log.info((new StringBuilder("\u8BA1\u7B97\u603B\u884C\u6570:")).append(strSql));
        return strSql;
    }

}
