package com.mangocity.hotel.base.manage;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mangocity.hotel.base.constant.WorkType;
import com.mangocity.hotel.base.dao.OrWorkStatesDao;
import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.hotel.base.persistence.WorkStatesSkill;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.log.MyLog;

/**
 * OrWorkStates Dao类
 * 
 * @author HWL
 * 
 */
public class OrWorkStatesManager {
	private static final MyLog log = MyLog.getLogger(OrWorkStatesManager.class);
    private OrWorkStatesDao orWorkStatesDao;

    public OrWorkStates loadObject(Serializable objID) {
        return (OrWorkStates) orWorkStatesDao.loadObject(objID);
    }

    /**
     * 插入或更新obj
     * 
     * @param obj
     */
    public void saveOrUpdate(OrWorkStates obj) {
        orWorkStatesDao.saveOrUpdate(obj);
    }

    /**
     * 获取随机操作员
     * 
     * @return
     */
    public OrWorkStates getRandomUser() {
        String logonId = "hotel5990";
        int index = Double.valueOf((Math.random() * 10.)).intValue() + 1;
        if (10 == index) {
            logonId = "hotel59910";
        }
        logonId += index;
        return returnWorkStatesBylogonId(logonId);
    }

    /**
     * 查出指定工号的workStates记录
     * 
     * @param longnId
     *            工号
     * @return workStates对象;
     */

    public OrWorkStates returnWorkStatesBylogonId(String logonId) {

        OrWorkStates orWorkStates = null;
        String hsql = "FROM OrWorkStates  WHERE logonId= ?";
        List li = orWorkStatesDao.query(hsql, logonId);
        if (0 < li.size()) {
            orWorkStates = (OrWorkStates) li.get(0);
        }
        return orWorkStates;
    }
    
    /**
     * 根据指定的工号查找workStatesSkill记录
     * @param logonId
     * @return
     */
    public List returnWorkStateSkillByLogonId(String logonId) {
		String hsql="From WorkStatesSkill where logonId=? order by groupId asc";
		return orWorkStatesDao.query(hsql,logonId);
	}

    /**
     * 处理从权限系统登陆的用户，如果OrWorkStates表没有则添加
     */
    public OrWorkStates handleLoginUser(String logonId, String name, int type) {
        OrWorkStates orWorkStates = returnWorkStatesBylogonId(logonId);
        if (null == orWorkStates) {
            orWorkStates = new OrWorkStates();
            orWorkStates.setLogonId(logonId);
            orWorkStates.setName(name);
            orWorkStates.setType(type);
        } else {
            if (!orWorkStates.getName().equals(name)) {
                orWorkStates.setName(name);
            }
            if (orWorkStates.getType() != type) {
                orWorkStates.setType(type);
            }
        }
        saveOrUpdate(orWorkStates);
        return orWorkStates;
    }

    /**
     * 查出所有workStates表中的记录
     * 
     * @param type
     *            类型，如HRA或日审 1为HRA，2为日审
     * @return workStates 的 List;
     */
    public List lstWorkStatesByType(int type) {
        String hsql = "FROM OrWorkStates  WHERE type= ?";
        List li = orWorkStatesDao.query(hsql, type);
        return li;
    }

    public List lstWorkStatesByType(int type, boolean bChooseOpen) {
        String hsql = "FROM OrWorkStates  WHERE type= ?";
        if (bChooseOpen) {
            hsql += " and state=1";
        }
        List li = orWorkStatesDao.query(hsql, type);

        return li;
    }
    
    public List lstWorkStatesByLogonId(String logonId){
    	String hsql = "FROM OrWorkStates  WHERE logonId in('"+logonId+"')";
    	List li = orWorkStatesDao.query(hsql, null);
    	return li;
    }

    /**
     * 根据HRA类型查出符合要求的HRA工作人员
     * 
     * @param groups
     * @param bChooseOpen
     *            - true:只查open状态的用户, false:查所有
     * @return
     */
    public List lstHraWorkerByGroup(int[] groups, boolean bChooseOpen) {
        String hsql = "FROM OrWorkStates WHERE type=1 ";
        if (0 < groups.length) {
            int group = groups[0];
            hsql += " and (groups like '%" + group + "%' ";
            for (int i = 1; i < groups.length; i++) {
                group = groups[i];
                hsql += " or groups like '%" + group + "%' ";
            }
            hsql += " ) ";
        }
        if (bChooseOpen) {
            hsql += " and state=1";
        }
        List li = orWorkStatesDao.query(hsql, null);
        return li;
    }

    /**
     * 根据HRA类型查出符合要求的HRA工作人员
     * 
     * @param group
     * @param bChooseOpen
     *            - true:只查open状态的用户, false:查所有
     * @return
     */
    public List lstHraWorkerByGroup(int group, boolean bChooseOpen) {
        String hsql = "FROM OrWorkStates WHERE type=1 and groups like '%" + group + "%'";
        if (bChooseOpen) {
            hsql += " and state=1";
        }
        List li = orWorkStatesDao.query(hsql, null);
        return li;
    }

    /**
     * 根据HRA类型查出不符合组别要求的HRA工作人员
     * 
     * @param group
     *            组别
     * @param bChooseOpen
     *            - true:只查open状态的用户, false:查所有
     * @return
     */
    public List lstHraWorkerNotByGroup(int group, boolean bChooseOpen) {
        String hsql = "FROM OrWorkStates WHERE type=1 and groups not like '%" + group + "%'";
        if (bChooseOpen) {
            hsql += " and state=1";
        }
        List li = orWorkStatesDao.query(hsql, null);
        return li;
    }

    /**
     * 根据group查出符合要求的日审工作人员
     * 
     * @param group
     * @param bChooseOpen
     *            - true:只查open状态的用户, false:查所有
     * @return
     */
    public List lstAuditWorkersByGroup(int group, boolean bChooseOpen) {
        String hsql = "FROM OrWorkStates WHERE type=2 and groups is not null and groups like '%" + group + "%'";
        if (bChooseOpen) {
            hsql += " and state=1";
        }
        List li = orWorkStatesDao.query(hsql, null);
        return li;
    }

    /**
     * 删除workstates
     * 
     * @param ids
     * @return
     */
    public boolean batchDel(String[] ids) {
        StringBuffer hsql = new StringBuffer().append(" delete OrWorkStates a ").append(
            " where a.logonId in (");
        Object[] obj = new Object[ids.length];
        for (int i = 0; i < ids.length; i++) {
            obj[i] = ids[i];
            if (0 == i) {
                hsql.append("?");
            } else {
                hsql.append(",?");
            }
        }
        hsql.append(")");

        orWorkStatesDao.doUpdateBatch(hsql.toString(), obj);

        return true;
    }

    /**
     * 按查询条件动态生生HQL查询工作状态 add by chenjiajie 2008-10-21
     * 
     * @param workStatesCondition
     * @param noAssignTo
     *            查询未分配 标识
     * @return
     */
    public List lstWorkStatesDynamic(OrWorkStates workStatesCondition, int noAssignTo) {
        StringBuffer hsql = new StringBuffer();
         hsql.append("FROM OrWorkStates WHERE lastLoginTime >= (sysdate-1)"); // 正常用
        //  hsql.append("FROM OrWorkStates WHERE lastLoginTime >= (sysdate-200)"); // 测试用
        if (null != workStatesCondition.getName() && !"".equals(workStatesCondition.getName())) {
            hsql.append(" and name like '%" + workStatesCondition.getName() + "%'");
        }
        if (null != workStatesCondition.getLogonId()
            && !"".equals(workStatesCondition.getLogonId())) {
        	String logonStr="";
        	if(workStatesCondition.getLogonId().contains(",")){
        		logonStr=workStatesCondition.getLogonId().replaceAll(",","','");
        	}else{
        		logonStr=workStatesCondition.getLogonId();
        	}
        	
            hsql.append(" and logonId in( '" + logonStr + "')");
        }
        if (-1 != workStatesCondition.getState()) {
            hsql.append(" and state = " + workStatesCondition.getState());
        }
        int nType = workStatesCondition.getType();
        if (1 <= nType && 4 >= nType) { // 加入选择部门条件
            hsql.append(" and type=" + workStatesCondition.getType());
            if (-1 == noAssignTo) { // 查询未分配
                hsql.append(" and (groups is null or groups = '') ");
            } else if (null != workStatesCondition.getGroups()
                && !"".equals(workStatesCondition.getGroups())) { // 帅选组别
                String[] groups = workStatesCondition.getGroups().split(",");
                if (0 < groups.length) {
                    String group = groups[0];
                    hsql.append(" and ((groups like '%" + group + "%' ");
                    hsql.append(" and groups not like '%9" + group + "%' "); // 解决工作状态查询组别不准确的问题
                    hsql.append(" and groups not like '%1" + group + "%') ");
                    for (int i = 1; i < groups.length; i++) {
                        group = groups[i];
                        hsql.append(" or (groups like '%" + group + "%' ");
                        hsql.append(" and groups not like '%9" + group + "%' ");
                        hsql.append(" and groups not like '%1" + group + "%') ");
                    }
                    hsql.append(" ) ");
                }
            }
        }
        hsql.append(" order by type,state desc ");
        log.info("==============" + hsql.toString());
        List li = orWorkStatesDao.query(hsql.toString(), null);
        return li;
    }

    /**
     * 根据RAC类型查出符合要求的RSC工作人员
     * 
     * @param group
     * @param bChooseOpen
     *            - true:只查open状态的用户, false:查所有
     * @return
     */
    public List lstRSCWorkerByGroup(int group, boolean bChooseOpen) {
        String hsql = "FROM OrWorkStates WHERE type=3 and groups like '%" + group + "%'";
        if (bChooseOpen) {
            hsql += " and state=1";
        }
        List li = orWorkStatesDao.query(hsql, null);
        return li;
    }

    /**
     * 记录中台人员最后登陆时间
     * 
     * @param userWrapper
     *            add by chenjiajie 2008-12-29
     */
    public void saveOrgMidLoginTime(UserWrapper userWrapper) {
        String loginId = userWrapper.getLoginName();
        String hqlStr = "from OrWorkStates s where s.logonId = ?";
        List workStatesList = orWorkStatesDao.doquery(hqlStr, loginId, false);
        if (0 < workStatesList.size()) {
            OrWorkStates workState = (OrWorkStates) workStatesList.get(0);
            workState.setLastLoginTime(new Date());
            orWorkStatesDao.saveOrUpdate(workState);
        }
    }

    /** getter and setter begin */

    public OrWorkStatesDao getOrWorkStatesDao() {
        return orWorkStatesDao;
    }

    public void setOrWorkStatesDao(OrWorkStatesDao orWorkStatesDao) {
        this.orWorkStatesDao = orWorkStatesDao;
    }

	public void markRSCRights(UserWrapper roleUser) {
		if(null != roleUser){
			int permiss = orWorkStatesDao.markRSCRights(roleUser.getLoginName());
			log.info("=====================permiss:"+permiss);
			if(permiss > -1){
				roleUser.setRSCUser(true);
				roleUser.setRSCAdmin(permiss == 1);
			}
		}
	}

	public void deleteWorkStateSkillByLogonId(String logonId) {
		orWorkStatesDao.remove("delete from WorkStatesSkill a where a.logonId=?  ", new Object[]{logonId});
	}
	
	public void saveWorkStateSkill(List sList,String logonId){
		//deleteWorkStateSkillByLogonId(logonId);
		for(Iterator i = sList.iterator();i.hasNext();){
			WorkStatesSkill workStatesSkill=(WorkStatesSkill) i.next();
			orWorkStatesDao.saveOrUpdate(workStatesSkill);
		}
	}
	
	
	public List lstWorkStatesByTypeNew(String logonId){
		String hqlStr = "from WorkStatesSkill s where s.logonId = ?";
        List workStatesList = orWorkStatesDao.doquery(hqlStr, logonId, false);
        return workStatesList;
	}
	

	public List lstWorkStatesByGroups(String logonId,String groups) {
        StringBuilder sqlStr= new StringBuilder();
        
        //select * from or_workstates where instr(groups,'2')>0 or instr(groups,'6')>0 or instr(groups,'4')>0
        sqlStr.append("select logonid,name from or_workstates w");
        sqlStr.append(" where state=1 and type=").append(WorkType.HRA);//可以查询自己
        if(null!=groups&&!"".equals(groups)){
        	sqlStr.append(" and ( ");
        	if(groups.contains(",")){
            	String[] groupArray=groups.split(",");
            	int len=groupArray.length;
            	for(int i=0;i<len;i++){
            		sqlStr.append( "instr(groups,'"+groupArray[i]+"')>0");
            		if(i<len-1){
            			sqlStr.append(" OR ");
            		}
            	}
            }else{
            	sqlStr.append( "instr(groups,'"+groups+"')>0");
            }
        	sqlStr.append(" ) ");
        }
        
        log.info("SQL==========================================="+sqlStr.toString());
		return orWorkStatesDao.doquerySQL(sqlStr.toString(), false);
	}

	public List lstWorkStatesByOtherGroups(String logonId,String groups) {
        StringBuilder sqlStr= new StringBuilder();
        sqlStr.append(" select logonid,name from or_workstates w where  state=1 and type=").append(WorkType.HRA);
        if(Integer.parseInt(groups) > 9)//如果是中台的人员分配房控的订单，则只出现专家组
        	sqlStr.append(" and instr(groups,6) > 0");
        if(null!=groups&&!"".equals(groups)){
        	sqlStr.append(" and ( ");
        	if(groups.contains(",")){
            	String[] groupArray=groups.split(",");
            	int len=groupArray.length;
            	for(int i=0;i<len;i++){
            		sqlStr.append( "instr(groups,'"+groupArray[i]+"')=0");
            		if(i<len-1){
            			sqlStr.append(" OR ");
            		}
            	}
            }else{
            	sqlStr.append( "instr(groups,'"+groups+"')=0");
            }
        	sqlStr.append(" ) ");
        }
        log.info("SQL2==========================================="+sqlStr.toString());
		return orWorkStatesDao.doquerySQL(sqlStr.toString(), false);
	}
    /** getter and setter end */
}
