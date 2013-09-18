package zhx.service.impl;

import zhx.constant.ZhxParamConstant;
import zhx.dao.IZhxDao;
import zhx.service.IZhxScheduleService;

import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.util.DateUtil;

/**
 * 
 * 中航信定时参数相关service
 * 
 * @author chenkeming
 *
 */
public class ZhxScheduleService implements IZhxScheduleService {


	private IZhxDao zhxDao;
	
	/**
	 * 判断中航信更新价格定时器是否需要更新价格
	 * @return true:需要|false:不需要
	 */
	public boolean needUpdatePriceForZhx() {
		boolean returnValue;
		OrParam param = zhxDao.getOrParam(ZhxParamConstant.IS_UPDATE_ZHX_PRICE);
		if(null != param){
			if (DateUtil.MINUTE > (DateUtil.getSystemDate().getTime() - param.getModifyTime().getTime())) {
                // 如果当前日期与param的时间之差小于1分钟,则是集群重新发送,
                // 采用不响应方式
				returnValue = false;
            } else {
                param.setValue(ZhxParamConstant.WORKING);
                param.setModifyTime(DateUtil.getSystemDate());
                zhxDao.updateOrParam(param); // 更数数据
				returnValue = true;
            }
		}else{//如果没有结果新增一条记录
            param = new OrParam();
            param.setName(ZhxParamConstant.IS_UPDATE_ZHX_PRICE);
            param.setValue(ZhxParamConstant.WORKING);
            param.setModifyTime(DateUtil.getSystemDate());
            zhxDao.updateOrParam(param);
			returnValue = true;
		}
		return returnValue;
	}

	public void setZhxDao(IZhxDao zhxDao) {
		this.zhxDao = zhxDao;
	}

}
