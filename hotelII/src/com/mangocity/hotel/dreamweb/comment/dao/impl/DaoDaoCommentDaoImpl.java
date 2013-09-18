package com.mangocity.hotel.dreamweb.comment.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.mangocity.hotel.dreamweb.comment.dao.IDaoDaoCommentDao;
import com.mangocity.hotel.dreamweb.comment.model.DaoDaoBasicComment;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class DaoDaoCommentDaoImpl extends GenericDAOHibernateImpl implements IDaoDaoCommentDao {
	
	/**
	 * 通过hotelId从数据库中获取到到网点评的基本信息（包括点评条数，
	 * 评分图片的URL，对应到到网点评详情信息所对应的daodaoId)
	 * @param hotelId
	 * @return
	 */
	public DaoDaoBasicComment getDaoDaoCommentByHotelId(Long hotelId) {
		   
		   return this.get(DaoDaoBasicComment.class, hotelId);
	}
    
	/**
	 * 保存或更新到到网点评基本信息
	 * @param daodaoComments
	 */
	public void saveOrUpdateDaoDaoComments(List<DaoDaoBasicComment> daodaoComments) {
		SessionFactory sessionFactory = this.getSessionFactory();
		Session session = sessionFactory.openSession();  
		Transaction tx = session.beginTransaction(); 
		  int i=1;
		   for (DaoDaoBasicComment daoDaoBasicComment:daodaoComments) {   
		    session.saveOrUpdate(daoDaoBasicComment);  
		    i++;
		    if ( i % 100== 0 ) { //20, same as the JDBC batch size //20,与JDBC批量设置相同  
		        //flush a batch of inserts and release memory:  
		        //将本批插入的对象立即写入数据库并释放内存  
		        session.flush();  
		        session.clear();  
		    }  
		}  
		     
		tx.commit();  
		session.close();  

	}
    
	
	/**
	 * 判断该实体是否存在于hibernate的session缓存范围内
	 * @param entitiy
	 * @return
	 */
	public boolean contains(Object entitiy){
		return this.contains(entitiy);
	}
	
	/**
	 * 获取表的记录条数
	 */
	public int size() {
		String hql ="select count(*) from DaoDaoBasicComment ";
		int size=Integer.parseInt(this.getHibernateTemplate().find(hql).get(0).toString());
		return size;
	}
	
	
}
