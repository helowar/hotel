/*
 * Copyright 2005-2010 the original author or authors.
 *
 * you may not use this file on any business environment.
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mangocity.util.dao;

import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.mangocity.util.dao.paging.ReflectUtil;

public class GenericDAOIBatisImpl extends SqlMapClientDaoSupport implements IbatisDao {

	private SqlExecutor sqlExecutor;

	public GenericDAOIBatisImpl() {
		super();
	}

	/**
	 * 
	 */
	public void initialize() {
		if (null != sqlExecutor) {
			SqlMapClient sqlMapClient = getSqlMapClientTemplate()
					.getSqlMapClient();
			if (sqlMapClient instanceof ExtendedSqlMapClient) {
				ReflectUtil.setFieldValue(((ExtendedSqlMapClient) sqlMapClient)
						.getDelegate(), "sqlExecutor", SqlExecutor.class,
						sqlExecutor);
			}
		}
	}

	/**
	 *  调用存储过程 add by kun.chen 2007-9-14 ◎param id--存储过程ID ◎param params
	 * --存储过程参数列表 ◎return
	 */
	public Object queryForObject(String id, Map params) {
		return super.getSqlMapClientTemplate().queryForObject(id, params);
	}

	public SqlExecutor getSqlExecutor() {
		return sqlExecutor;
	}

	public void setSqlExecutor(SqlExecutor sqlExecutor) {
		this.sqlExecutor = sqlExecutor;
	}
}
