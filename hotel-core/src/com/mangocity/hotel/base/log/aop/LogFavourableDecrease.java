package com.mangocity.hotel.base.log.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.springframework.aop.MethodBeforeAdvice;

import com.mangocity.hotel.base.manage.ClauseManage;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.persistence.HtlPublicOperationlog;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.hotel.constant.FunctionalModuleUtil;
import com.mangocity.util.hotel.constant.LogOperationMode;
import com.mangocity.util.log.MyLog;
import com.opensymphony.xwork2.ActionContext;




/**
 * 优惠条款日志记录
 * 
 * @author zuoshengwei
 * 
 */
public class LogFavourableDecrease extends DAOHibernateImpl implements
        MethodBeforeAdvice {
	
	private static final MyLog log = MyLog.getLogger(LogFavourableDecrease.class);
	private  ClauseManage clauseManage;
	
	public void before(Method arg0, Object[] arg1, Object arg2)
            throws Throwable {
    	
        
        //优惠立减实体类
        HtlFavourableDecrease htlFavourableDecrease = new HtlFavourableDecrease();
        
        htlFavourableDecrease = (HtlFavourableDecrease)arg1[0];
       
        //日志类
        HtlPublicOperationlog htlFavourableclauseLog = new HtlPublicOperationlog();
        
        if(htlFavourableDecrease!=null){
        	
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
        if("createFavourableDecrease".equals(arg0.getName())){
        	// 操作方式
            htlFavourableclauseLog.setOperationmode((long)LogOperationMode.ADD);
            insertlogBuffer.append(onlineRoleUser.getName()+"在"+DateUtil.datetimeToString(DateUtil.getSystemDate())+"新增一条立减优惠条款;");
            insertlogBuffer.append("价格类型为："+htlFavourableDecrease.getPriceTypeName()+";");
            insertlogBuffer.append("立减优惠时间："+DateUtil.datetimeToString(htlFavourableDecrease.getBeginDate())+"至"+DateUtil.datetimeToString(htlFavourableDecrease.getEndDate())+",");
            insertlogBuffer.append("星期:"+htlFavourableDecrease.getWeek()+";");
            insertlogBuffer.append("立减金额："+htlFavourableDecrease.getDecreasePrice());
            if("1".equals(htlFavourableDecrease.getPayMethod())){
            	insertlogBuffer.append("适用价格：面付");
            	
            }
            if("2".equals(htlFavourableDecrease.getPayMethod())){
            	insertlogBuffer.append("适用价格：预付");
            	
            }
            if("1".equals(htlFavourableDecrease.getHotelPayType())){
            	insertlogBuffer.append("酒店支付方式：底价支付酒店方式");
            	
            }
            if("2".equals(htlFavourableDecrease.getHotelPayType())){
            	insertlogBuffer.append("酒店支付方式：售价支付酒店方式");
            	
            }
         }
        //删除操作
        if("deleteFavDecreaseObj".equals(arg0.getName())){
        	// 操作方式
            htlFavourableclauseLog.setOperationmode((long)LogOperationMode.DELETE);
            insertlogBuffer.append(onlineRoleUser.getName()+"在"+DateUtil.datetimeToString(DateUtil.getSystemDate())+"删除了一条立减优惠条款;");
            insertlogBuffer.append("价格类型为："+htlFavourableDecrease.getPriceTypeName()+";");
            insertlogBuffer.append("立减优惠时间："+DateUtil.datetimeToString(htlFavourableDecrease.getBeginDate())+"至"+DateUtil.datetimeToString(htlFavourableDecrease.getEndDate())+",");
            insertlogBuffer.append("星期:"+htlFavourableDecrease.getWeek()+";");
            insertlogBuffer.append("立减金额："+htlFavourableDecrease.getDecreasePrice());
            if("1".equals(htlFavourableDecrease.getPayMethod())){
            	insertlogBuffer.append("适用价格：面付");
            	
            }
            if("2".equals(htlFavourableDecrease.getPayMethod())){
            	insertlogBuffer.append("适用价格：预付");
            	
            }
            if("1".equals(htlFavourableDecrease.getHotelPayType())){
            	insertlogBuffer.append("酒店支付方式：底价支付酒店方式");
            	
            }
            if("2".equals(htlFavourableDecrease.getHotelPayType())){
            	insertlogBuffer.append("酒店支付方式：售价支付酒店方式");
            	
            }

        }
        
        //修改操作
        if("modifyFavClause".equals(arg0.getName())){
        	// 操作方式
            htlFavourableclauseLog.setOperationmode((long)LogOperationMode.MODIFY);
            insertlogBuffer.append(onlineRoleUser.getName()+"在"+DateUtil.datetimeToString(DateUtil.getSystemDate())+"修改了一条立减优惠条款;");
            
            HtlFavourableDecrease oldFavourableDecrease = new HtlFavourableDecrease();
            
            oldFavourableDecrease = clauseManage.getFavDecreaseById(htlFavourableDecrease.getId());
            
            
            String[] props = new String[]{"开始日期-beginDate","结束日期-endDate","星期-week","底价价格-basePrice","立减金额-decreasePrice","酒店支付方式-hotelPayType"};
            
            String differentStr = objectDOEquals(oldFavourableDecrease,htlFavourableDecrease,props);
            
            if("".equals(differentStr)){
            	
            	insertlogBuffer.append("但没有改变任何值！");
            	
            }else{
            	   insertlogBuffer.append(differentStr);
            	
            }
         
         }
        
       

        
        // 操作内容：
        htlFavourableclauseLog
                .setOperationcontent(insertlogBuffer.toString());

        // 原记录表名：
        htlFavourableclauseLog.setTablename("HTL_FAVOURABLE_DECREASE");

//      // 原记录表ID:
//      htlFavourableclauseLog.setTableid(htlFavourableclause.getId());
        
        // 功能模块ID
        htlFavourableclauseLog
                .setFunctionalmoduleid((long)FunctionalModuleUtil.HTL_FAVOURABLEDECREASE);

        // 酒店ID:
        htlFavourableclauseLog.setHotelid(htlFavourableDecrease.getHotelId());

        // 操作人
        htlFavourableclauseLog.setOperationer(onlineRoleUser.getName());

        // 操作人ID
        htlFavourableclauseLog.setOperationerid(onlineRoleUser.getLoginName());

        // 操作日期
        htlFavourableclauseLog.setOperationdate(DateUtil.getSystemDate());

        super.save(htlFavourableclauseLog);
        
        
        }else{
        	
        	log.error("优惠条款内容为空，无法记录日志！");
        	
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
    
    
    /**
     * 返回优惠类型
     * add by shengwei.zuo 2009-19-17
     * @param favType
     * @return
     */
    public String returnFavType(String favType) {
    	
        String favTypeStr = "";
        
        if (null != favType && !favType.equals("")) {
        	
            if ("1".equals(favType) || favType=="1") {
            	
            	favTypeStr = "连住N晚送M晚";
            	
            }else if ("2".equals(favType) || favType=="2") {
            	
            	favTypeStr = "原价打折";
            	
            }else if ("3".equals(favType) || favType=="3") {
            	
            	favTypeStr = "连住包价";
            	
            }
            
        } else {
        	
        	favTypeStr = "无";
        	
        }
        
        return favTypeStr;
        
    }

	public ClauseManage getClauseManage() {
		return clauseManage;
	}


	public void setClauseManage(ClauseManage clauseManage) {
		this.clauseManage = clauseManage;
	}





}


