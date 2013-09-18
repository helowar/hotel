package zhx.service;



/**
 * 中航信的业务层
 * 
 * @author chenkeming
 *
 */
public interface IZhxManage {
	
	/**
	 * 
	 * 调用存储过程更新价格
	 * 
	 * @param childRoomType
	 * @param payMethod
	 * @param startDate
	 * @param endDate
	 * @param salePrice
	 * @return
	 */
	public long updatePrice(Long childRoomType, String payMethod, String startDate, 
			String endDate, Double salePrice);
	
}
