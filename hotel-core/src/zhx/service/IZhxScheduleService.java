package zhx.service;


/**
 * 
 * 中航信定时器参数Service接口
 * 
 * @author chenkeming
 *
 */
public interface IZhxScheduleService {

	/**
	 * 判断中航信更新价格定时器是否需要更新价格
	 * @return true:需要|false:不需要
	 */
	public boolean needUpdatePriceForZhx();
	
}
