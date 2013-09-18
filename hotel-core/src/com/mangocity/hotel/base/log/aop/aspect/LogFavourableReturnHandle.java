/**
 * 
 */
package com.mangocity.hotel.base.log.aop.aspect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import com.mangocity.hotel.base.persistence.HtlFavourableReturn;
import com.mangocity.hotel.base.persistence.HtlPublicOperationlog;
import com.mangocity.hotel.base.service.IHotelFavourableReturnService;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAO;
import com.mangocity.util.hotel.constant.FunctionalModuleUtil;
import com.mangocity.util.hotel.constant.LogOperationMode;
import com.mangocity.util.log.MyLog;
import com.opensymphony.xwork2.ActionContext;

/**
 * AOP 现金返还日志 类
 * @author xiongxiaojun 2010-10-13
 *
 */
public class LogFavourableReturnHandle {
    private static final MyLog log = MyLog.getLogger(LogFavourableReturnHandle.class);
    private DAO entityManager;
    private IHotelFavourableReturnService returnService;
    
    public void logFavourableReturn(JoinPoint joinPoint){
    	Object[] objLog = joinPoint.getArgs();
    	String methodStr = joinPoint.getSignature().getName();
    	
    	log.info("==========================现金返还规则日志的写入============AOP method："+methodStr);
    	//现金返还实体类
    	HtlFavourableReturn ret = new HtlFavourableReturn();
    	ret = (HtlFavourableReturn) objLog[0];
    	
    	//日志类
    	HtlPublicOperationlog htlFavourableReturnLog = new HtlPublicOperationlog();
    	if(null!= ret){
    		//用户实体类
        	UserWrapper onlineRoleUser = new UserWrapper();
        	
        	// 从session中取出当前的登录用户信息
            WebContext wct = WebContextFactory.get();
            if (null != wct) {
                HttpSession session = wct.getSession();
                onlineRoleUser = (UserWrapper) session
                        .getAttribute("onlineRoleUser");
            }

            Map map = (Map) ActionContext.getContext().get("session");
            if (null != map) {
                onlineRoleUser = (UserWrapper) map.get("onlineRoleUser");
            }
            
            
            // 记录增加连住优惠条款的日志写入
            StringBuffer insertlogBuffer = new StringBuffer();
            //新增操作
            if("createFavourableReturn".equals(methodStr)){
            	//操作方式
            	htlFavourableReturnLog.setOperationmode((long)LogOperationMode.ADD);
            	insertlogBuffer.append(onlineRoleUser.getName()+"在"+DateUtil.datetimeToString(DateUtil.getSystemDate())+"新增一条现金返还规则;");
            	insertlogBuffer.append("价格类型为："+ret.getPriceTypeName()+";");
                insertlogBuffer.append("现金返还时间范围："+DateUtil.datetimeToString(ret.getBeginDate())+"至"+DateUtil.datetimeToString(ret.getEndDate())+",");
                insertlogBuffer.append("星期:"+ret.getWeek()+";");
                insertlogBuffer.append("返现比例或金额："+ret.getReturnScale());
                //面付
                if("1".equals(ret.getPayMethod())){
                	insertlogBuffer.append("适用价格：面付");
                }
                //预付
                if("2".equals(ret.getPayMethod())){
                	insertlogBuffer.append("适用价格：预付");
                }
            }
            
            //删除操作
            if("deleteFavReturnObj".equals(methodStr)){
            	// 操作方式
            	htlFavourableReturnLog.setOperationmode((long)LogOperationMode.DELETE);
                insertlogBuffer.append(onlineRoleUser.getName()+"在"+DateUtil.datetimeToString(DateUtil.getSystemDate())+"删除了一条现金返还规则;");
                insertlogBuffer.append("价格类型为："+ret.getPriceTypeName()+";");
                insertlogBuffer.append("现金返还时间范围："+DateUtil.datetimeToString(ret.getBeginDate())+"至"+DateUtil.datetimeToString(ret.getEndDate())+",");
                insertlogBuffer.append("星期:"+ret.getWeek()+";");
                insertlogBuffer.append("返现比例或金额："+ret.getReturnScale());
                if("1".equals(ret.getPayMethod())){
                	insertlogBuffer.append("适用价格：面付");
                }
                if("2".equals(ret.getPayMethod())){
                	insertlogBuffer.append("适用价格：预付");
                }
            }
            
            //修改操作
            if("modifyFavReturn".equals(methodStr)){
            	// 操作方式
            	htlFavourableReturnLog.setOperationmode((long)LogOperationMode.MODIFY);
                insertlogBuffer.append(onlineRoleUser.getName()+"在"+DateUtil.datetimeToString(DateUtil.getSystemDate())+"修改了一条现金返还规则;");
                
                HtlFavourableReturn oldReturn = new HtlFavourableReturn();
                oldReturn = returnService.getFavReturnById(ret.getId());
                
                String[] props = new String[]{"开始日期-beginDate","结束日期-endDate","星期-week","支付方式-payMethod","返现额度-returnScale"};
                String differentStr = objectDOEquals(oldReturn,ret,props);
                //判断修改内容
                if("".equals(differentStr)){
                	insertlogBuffer.append("但没有改变任何值!");
                }else{
                	insertlogBuffer.append(differentStr);
                }
            }
            
            //操作内容：
            htlFavourableReturnLog.setOperationcontent(insertlogBuffer.toString());
            
            //原纪录表名:
            htlFavourableReturnLog.setTablename("HTL_FAVOURABLE_RETURN");
            //原纪录ID:
            htlFavourableReturnLog.setTableid(ret.getId());
            
            //功能模块ID
            htlFavourableReturnLog.setFunctionalmoduleid((long)FunctionalModuleUtil.HTL_FAVOURABLERETURN);
            
            //酒店ID
            htlFavourableReturnLog.setHotelid(ret.getHotelId());
            
            //操作人
            htlFavourableReturnLog.setOperationer(onlineRoleUser.getName());
            
            //操作人ID
            htlFavourableReturnLog.setOperationerid(onlineRoleUser.getLoginName());
            
            //操作日期
            htlFavourableReturnLog.setOperationdate(DateUtil.getSystemDate());
            
            entityManager.save(htlFavourableReturnLog);
    	}else{
    		log.info("现金返还内容为空，无法记录日志!");
    	}
    }
    
    
  //比较两个对象的属性的不同
    public static String objectDOEquals(Object a,Object b,String [] props){
	      
	      Object[] args = new Object[0];
	      Class oClass = a.getClass();
	      Method[] methods = oClass.getMethods();
	      
	      StringBuffer diffLogStr= new StringBuffer(); 
	      
	      for(Method method : methods){
	    	  	
	      		for(String prop : props){
	      			
	      			String []  name = new String [2];
	      			
	      			name = prop.split("-");
	      			
	      			String methodName="get"+name[1].substring(0,1).toUpperCase()+name[1].substring(1);
	      			
	      		    if(method.getName().equals(methodName)){
	      		    	
	      		       try{
	      		       		
	      		       			Object oa = method.invoke(a,args);
	      		       			Object ob = method.invoke(b,args);
	      		       			
	      		       			//做不为空的校验
	      		       			if(((null == oa||("").equals(oa))&&(oa == ob))||(null == oa && ("").equals(ob))||(("").equals(oa) && null == ob))
	      		       			{
	      		       				continue;
	      		       			}
	      		       			//Date类型转换为String类型
	      		       			if(oa instanceof Date){
		       				    	oa=DateUtil.datetimeToString((Date)oa);
		       				    }
		       				    if(ob instanceof Date){
		       				    	ob=DateUtil.datetimeToString((Date)ob);
		       				    }
	      		       			
	      		       			if(oa==null || !oa.equals(ob)){
	      		       				diffLogStr.append(name[0]+"的值: 由 "+oa+"改为"+ob+";");
	      		       			}
	      		       
	      		       }catch (IllegalArgumentException e){
							
	      		    	   log.error(e);
							
						}catch (IllegalAccessException e){
							
							log.error(e);
						}catch (InvocationTargetException e){
							
							log.error(e);
						}catch (SecurityException 	e){
							
							log.error(e);
						}    	
	      		    
	      		    }
	      		
	      		}
	      
	      }
	      
	      return diffLogStr.toString();

	}
	public DAO getEntityManager() {
		return entityManager;
	}
	public void setEntityManager(DAO entityManager) {
		this.entityManager = entityManager;
	}
	public IHotelFavourableReturnService getReturnService() {
		return returnService;
	}
	public void setReturnService(IHotelFavourableReturnService returnService) {
		this.returnService = returnService;
	}
    
}
