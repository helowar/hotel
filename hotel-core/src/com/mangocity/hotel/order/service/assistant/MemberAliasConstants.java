/**
 * 
 */
package com.mangocity.hotel.order.service.assistant;

import java.util.HashMap;
import java.util.Map;

import com.mangocity.hotel.ext.member.dto.MemberDTO;
import com.mangocity.hotel.order.constant.CooperateChannel;

/**
 * @author xiongxiaojun
 *
 */
public class MemberAliasConstants {
	 public static final String WTT ="8500200002";//广东联通
	 public static final String GDLT ="8500200002";//广东联通    
	 	public static final String BJWT = "8500200001"; //北京网通
	    public static final String LTT ="9308400001";//广东联通商旅服务
	    public static final String WTBJ ="9305300001"; //北京联通    
	    public static final String NHZY ="9200600002";//南航95539   
	    
	    public static final String JIAOHANG="9101800003";//交行积分乐园
	    public static final String ZSJT = "9311100001";//中升集团
	    public static final String DL ="9400100001";//代理           
	    
	    
	    
	    
	 //   public static final String ZSJT = "8500100020";//5.18测试环境会员Aliasid
	    
	    // 交行全卡商旅等渠道, 会员的合作项目号(aliasid)到Project Code的Map add by chenkeming
	    public static Map<String, String> mapAlias2ProjectCode = new HashMap<String, String>();    
	    static {
	    	mapAlias2ProjectCode.put(JIAOHANG, CooperateChannel.JIAOHANG_CHANNEL_CODE);
	    }
	    
	    /**
	     * 广东联通  
	     * @param member
	     * @return
	     */
	    public static boolean isGDLT(MemberDTO member){
	    	return GDLT.equals(member.getAliasid());
	    }
	    
	    /**
	     * 中升集团
	     */
	    public static boolean isZSJT(MemberDTO member){
	    	return ZSJT.equals(member.getAliasid());
	    }
	    /**
	     * 是否是北京联通
	     * @param member
	     * @return
	     */
	    public static boolean isWTBJ(MemberDTO member){
	    	return WTBJ.equals(member.getAliasid());
	    }
}
