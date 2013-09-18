package com.mangocity.hotel.base.log.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.springframework.aop.MethodBeforeAdvice;

import com.mangocity.hotel.base.constant.FavourableTypeUtil;
import com.mangocity.hotel.base.manage.ContractManage;
import com.mangocity.hotel.base.persistence.HtlEveningsRent;
import com.mangocity.hotel.base.persistence.HtlFavouraParameter;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
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
public class LogFavourableclause extends DAOHibernateImpl implements
        MethodBeforeAdvice {
	
	private static final MyLog log = MyLog.getLogger(LogFavourableclause.class);
	private  ContractManage contractManageTarget;
	
	public void before(Method arg0, Object[] arg1, Object arg2)
            throws Throwable {
    	
        
        //优惠条款实体类
        HtlFavourableclause htlFavourableclause = new HtlFavourableclause();
        
        htlFavourableclause = (HtlFavourableclause)arg1[0];
        
        if(htlFavourableclause!=null){
        	
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
            //日志类
            HtlPublicOperationlog htlFavourableclauseLog = new HtlPublicOperationlog();
        
        //新增操作
        if("createFavourableclause".equals(arg0.getName())){
        	   // 操作方式
            htlFavourableclauseLog
                    .setOperationmode((long)LogOperationMode.ADD);
        	insertlogBuffer.append( onlineRoleUser.getName()+"在 "+DateUtil.datetimeToString(DateUtil.getSystemDate())+" 增加了一条优惠信息。");
        	insertlogBuffer.append("价格类型："+htlFavourableclause.getPriceTypeName()+";");
        	insertlogBuffer.append("优惠时间："+DateUtil.datetimeToString(htlFavourableclause.getBeginDate())+"至"+DateUtil.datetimeToString(htlFavourableclause.getEndDate())+"；");
        	insertlogBuffer.append("优惠类型：");
        	
        	
        	List<HtlFavouraParameter>  htlFavouraParameterLis = new ArrayList<HtlFavouraParameter>();
        	
        	//连住N晚送M晚
        	if(FavourableTypeUtil.continueDonate.equals(htlFavourableclause.getFavourableType())){
        		
        		htlFavouraParameterLis = htlFavourableclause.getLstPackagerate();
        		
        		if(!htlFavouraParameterLis.isEmpty()){
        			
    	        		HtlFavouraParameter htlFavouraParameter = htlFavouraParameterLis.get(0);
    	        		insertlogBuffer.append("连住"+htlFavouraParameter.getContinueNight()+"晚送"+htlFavouraParameter.getDonateNight()+"晚，");
    	        		if(1 == htlFavouraParameter.getCirculateType()){
    	        			insertlogBuffer.append("循环；");
    	        		}else{
    	        			insertlogBuffer.append("不循环；");
    	        		}
    	        		List<HtlEveningsRent> htlEveningsRentLis = htlFavouraParameter.getLstEveningsRent();
    	        		for(int i=0;i<htlEveningsRentLis.size();i++){
    	        			HtlEveningsRent htlEveningsRent=htlEveningsRentLis.get(i);
    	        			insertlogBuffer.append("第"+htlEveningsRent.getNight()+"晚，售价:"+htlEveningsRent.getSalePrice()+"，佣金:"+htlEveningsRent.getCommission()+"；");
    	        		}
            	
        			
        		}
        		
        	
        	}else if(FavourableTypeUtil.discount.equals(htlFavourableclause.getFavourableType())){//原价打折
        		
        		htlFavouraParameterLis = htlFavourableclause.getLstPackagerate();
        		
        		if(!htlFavouraParameterLis.isEmpty()){
        			
            		
    	        		HtlFavouraParameter  htlFavouraParameter = htlFavouraParameterLis.get(0);
    	        		
    	        		insertlogBuffer.append("原价打"+htlFavouraParameter.getDiscount()+"折,");
    	        		if(1 == htlFavouraParameter.getDecimalPointType()){
    	        			insertlogBuffer.append("售价小数点的处理采用：逢一进十。");
    	        		}else if(2 == htlFavouraParameter.getDecimalPointType()){
    	        			insertlogBuffer.append("售价小数点的处理采用：四舍五入保留一位小数。");
    	        		}else if(3 == htlFavouraParameter.getDecimalPointType()){
    	        			insertlogBuffer.append("售价小数点的处理采用：直接取整。");
    	        		}else if(4 == htlFavouraParameter.getDecimalPointType()){
    	        			insertlogBuffer.append("售价小数点的处理采用：四舍五入取整。");
    	        		}
        			
        		}
        		

        	}else if(FavourableTypeUtil.packagerate.equals(htlFavourableclause.getFavourableType())){ //连住包价
        		
        		htlFavouraParameterLis = htlFavourableclause.getLstPackagerate();
        		
        		if(!htlFavouraParameterLis.isEmpty()){
        			
    	        		HtlFavouraParameter  htlFavouraParameter = htlFavouraParameterLis.get(0);
    	        		insertlogBuffer.append("连住("+htlFavouraParameter.getPackagerateNight()+")晚包价，");
    	        		insertlogBuffer.append("包价售价:"+htlFavouraParameter.getPackagerateSaleprice()+"。");
    	        		insertlogBuffer.append("包价佣金:"+htlFavouraParameter.getPackagerateCommission()+"。");
        			
        		}
        		
        	}
        	
        }
        //删除操作
        if("deleteFavClauseObj".equals(arg0.getName())){
        	   // 操作方式
            htlFavourableclauseLog
                    .setOperationmode((long)LogOperationMode.DELETE);
        	insertlogBuffer.append(onlineRoleUser.getName()+"在"+DateUtil.datetimeToString(DateUtil.getSystemDate())+ "进行了删除优惠条款操作，");
        	insertlogBuffer.append("删除了优惠条款ID为："+htlFavourableclause.getId()+" 的记录。");
        	insertlogBuffer.append("该优惠条款类型："+this.returnFavType(htlFavourableclause.getFavourableType())+";");
        	insertlogBuffer.append("价格类型："+htlFavourableclause.getPriceTypeName()+";");
        	insertlogBuffer.append("开始日期"+DateUtil.datetimeToString(htlFavourableclause.getBeginDate())+";");
        	insertlogBuffer.append("结束日期"+DateUtil.datetimeToString(htlFavourableclause.getEndDate())+";");
        	
        }
        
        //修改操作
        if("modifyFavClause".equals(arg0.getName())){
        	   // 操作方式
            htlFavourableclauseLog
                    .setOperationmode((long)LogOperationMode.MODIFY);
        	
        	String difLogstr = "";
        	String difLogstr2 = "";
        	String difLogstr3 = "";
        	
        	HtlFavourableclause favourableOldObj = new HtlFavourableclause();
        	favourableOldObj = contractManageTarget.getFavClauseById(htlFavourableclause.getId());
        	//old的优惠参数实体类
        	List<HtlFavouraParameter> HtlFavouraParameterOldList= favourableOldObj.getLstPackagerate();
        	
        	insertlogBuffer.append(onlineRoleUser.getName()+"在"+DateUtil.datetimeToString(DateUtil.getSystemDate())+ "进行修改优惠条款操作，");
        	String [] props1 =new String []{"开始日期-beginDate","结束日期-endDate"};
        	//比较HtlFavourableclause表对应字段不同
        	String difLogstr1 = objectDOEquals(favourableOldObj,htlFavourableclause,props1);
        	
        	//新的优惠参数实体类
        	List<HtlFavouraParameter> HtlFavouraParameterNewList = htlFavourableclause.getLstPackagerate();
        
				//比较HtlFavouraParameter表对应字段不同
        	if(!HtlFavouraParameterNewList.isEmpty()){
        			
        			HtlFavouraParameter htlFavouraParameterNewObj = HtlFavouraParameterNewList.get(0);
        			HtlFavouraParameter htlFavouraParameterOldObj = HtlFavouraParameterOldList.get(0);
        			
            		if(FavourableTypeUtil.continueDonate.equals(htlFavourableclause.getFavourableType())){
            			
            			String [] props2 =new String []{"连住N晚-continueNight","送M晚-donateNight","类推形式-circulateType"};
                		difLogstr2 = difLogstr1+objectDOEquals(htlFavouraParameterOldObj,htlFavouraParameterNewObj,props2);
                		difLogstr = difLogstr2;
                		
            			List<HtlEveningsRent> htlEveningsRentNewList = htlFavouraParameterNewObj.getLstEveningsRent();
            			List<HtlEveningsRent> htlEveningsRentOldList= htlFavouraParameterOldObj.getLstEveningsRent();
            			
            			StringBuffer continueDonateBuff =  new StringBuffer();
            			
            			if(!htlEveningsRentOldList.isEmpty()){
            				
            				continueDonateBuff.append("每晚的售价和佣金由原来的：");
                			for(int j=0;j < htlEveningsRentOldList.size();j++){
                				
                				HtlEveningsRent htlEveningsRentOldObj = htlEveningsRentOldList.get(j);
                				continueDonateBuff.append("第"+htlEveningsRentOldObj.getNight()+"晚，售价:"+htlEveningsRentOldObj.
                						getSalePrice()+",佣金:"+htlEveningsRentOldObj.getCommission()+";");
                				
                			}
            				
            			}
            			
            			if(!htlEveningsRentNewList.isEmpty()){
            				
            				continueDonateBuff.append("修改为现在的：");
                			for(int j=0;j < htlEveningsRentNewList.size();j++){
                				
                				HtlEveningsRent htlEveningsRentNewObj = htlEveningsRentNewList.get(j);
                				continueDonateBuff.append("第"+htlEveningsRentNewObj.getNight()+"晚，售价:"+htlEveningsRentNewObj.
                						getSalePrice()+",佣金:"+htlEveningsRentNewObj.getCommission()+";");
                				
                			}
            				
            			}
            			
            			difLogstr = difLogstr+ continueDonateBuff.toString();
            			
            		}else if(FavourableTypeUtil.discount.equals(htlFavourableclause.getFavourableType())){
            			String [] props2 =new String []{"折扣-discount","小数点类型-decimalPointType"};
                		difLogstr2 = difLogstr1+objectDOEquals(htlFavouraParameterOldObj,htlFavouraParameterNewObj,props2);
                		difLogstr = difLogstr2;
            			
            		}else if(FavourableTypeUtil.packagerate.equals(htlFavourableclause.getFavourableType())){
            			String [] props2 =new String []{"包价售价-packagerateSaleprice","包价佣金-packagerateCommission","包价晚数-packagerateNight"};
                		difLogstr2 = difLogstr1+objectDOEquals(htlFavouraParameterOldObj,htlFavouraParameterNewObj,props2);
                		difLogstr = difLogstr2;
            		}
            		
        		
        		
        	}
        	
        	
        	if(null == difLogstr|| "".equals(difLogstr)){
        		insertlogBuffer.append("但没有改动任何值！");
        	}else{
        		insertlogBuffer.append(difLogstr);
        		
        	}
        	
        	
        	
        }
        
       

     

        // 操作内容：
        htlFavourableclauseLog
                .setOperationcontent(insertlogBuffer
                        .toString());

        // 原记录表名：
        htlFavourableclauseLog.setTablename("HTL_FAVOURABLECLAUSE");

//      // 原记录表ID:
//      htlFavourableclauseLog.setTableid(htlFavourableclause.getId());
        
        // 功能模块ID
        htlFavourableclauseLog
                .setFunctionalmoduleid((long)FunctionalModuleUtil.HTL_FAVOURABLECLAUSE);

        // 酒店ID:
        htlFavourableclauseLog.setHotelid(htlFavourableclause.getHotelId());

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


	public void setContractManageTarget(ContractManage contractManageTarget) {
		this.contractManageTarget = contractManageTarget;
	}

}


