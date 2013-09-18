package com.mangocity.hotel.dreamweb.comment.service;

import java.util.List;
import com.mangocity.hotel.dreamweb.comment.model.DaoDaoBasicComment;

/***
 * @author panjianping
 * service API for operating basic comments from daodaowang
 *
 */

public interface IDaoDaoCommentService {
	
	/**
	 * 通过hotelId从数据库中获取到到网点评的基本信息（包括点评条数，
	 * 评分图片的URL，对应到到网点评详情信息所对应的daodaoId)
	 * @param hotelId
	 * @return
	 */
	public DaoDaoBasicComment getBasicCommentByHotelId(Long hotelId);
	
	
	/**
	 * 判断该实体是否存在于hibernate的session缓存范围内
	 * @param entitiy
	 * @return
	 */
	public boolean contains(Object entitiy);
	
	
	/**
	 * 从到到网下载含有到到网点评基本信息的XML文件
	 */
	public void downloadXmlFromDaoDao();
	
	
	/**
	 * 判断到到网点评信息是否已同步到数据库中
	 * （注：这是实际上是判断xml文件是否已经创建，因为从创建
	 * xml文件，到下载文件写入该xml文件中，再到同步到数据库
	 * 是一个事务，用spring定时器触发）
	 * @return
	 */
	public boolean hasDaoDaoCommentTBBeenInitted();
	
	
	/**
	 * 解析从到到网服务器下载的xml文件
	 * @return
	 */
	public List<DaoDaoBasicComment> parseXml();
	
	/**
	 * 用解析xml文件生成的DaoDaoBasicComment对象List更新数据库，
	 * 如果数据库中没有该对象对应的记录，则保存该对象
	 * @param daoDaoBasicComments
	 */
	public void saveOrUpdateDaoDaoCommentsInDB(List<DaoDaoBasicComment> daoDaoBasicComments);
	
	
	/**
	 * 该方法是从下载xml文件、解析xml文件、把解析的对象同步到数据库中的一些列完成的过程
	 * 
	 */
	public void updateHtlCommentDaoDaoTable();

}
