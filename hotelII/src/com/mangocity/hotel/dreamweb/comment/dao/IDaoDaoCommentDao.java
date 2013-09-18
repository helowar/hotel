package com.mangocity.hotel.dreamweb.comment.dao;

import java.util.List;

import com.mangocity.hotel.dreamweb.comment.model.DaoDaoBasicComment;
/**
 * 到到网点评信息数据库接口
 * @author panjianping
 *
 */
public interface IDaoDaoCommentDao {
	
	/**
	 * 保存或更新到到网点评基本信息
	 * @param daodaoComments
	 */
	public void saveOrUpdateDaoDaoComments(List<DaoDaoBasicComment> daodaoComments);
	
	
	/**
	 * 通过hotelId从数据库中获取到到网点评的基本信息（包括点评条数，
	 * 评分图片的URL，对应到到网点评详情信息所对应的daodaoId)
	 * @param hotelId
	 * @return
	 */
	public DaoDaoBasicComment getDaoDaoCommentByHotelId(Long hotelId);
	
	
	/**
	 * 判断该实体是否存在于hibernate的session缓存范围内
	 * @param entitiy
	 * @return
	 */
	public boolean contains(Object entitiy);
    
	/**
	 * 获取表的记录条数
	 */
	public int size();
}
