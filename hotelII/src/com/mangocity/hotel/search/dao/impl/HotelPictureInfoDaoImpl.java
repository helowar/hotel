package com.mangocity.hotel.search.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.dreamweb.displayvo.HtlAlbumVO;
import com.mangocity.hotel.dreamweb.displayvo.HtlPictureSizeVO;
import com.mangocity.hotel.dreamweb.displayvo.HtlPictureVO;
import com.mangocity.hotel.search.dao.HotelPictureInfoDao;
import com.mangocity.hotel.search.model.HotelAppearanceAlbum;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class HotelPictureInfoDaoImpl extends GenericDAOHibernateImpl implements HotelPictureInfoDao {

	public List<HtlAlbumVO> queryHotelPictureInfo(Long hotelId) {
		
		StringBuilder builder = new StringBuilder(" select b.classify,b.albumname,b.coverid,b.coverurl,c.pictureid,c.picturename,d.picturetype,d.pictureurl  ");
		builder.append(" from htl_pic_album b, htl_pic_picture c, htl_pic_picurl d ");
		builder.append("  where d.picturetype <> 1 and c.albumid = b.albumid and c.pictureid = d.pictureid and b.coverid <> 0 and c.active = 1 ");
		builder.append(" and b.albumid in ");
		builder.append(" (select a.albumid from htl_alb_hotel_relationship a  where a.active = 1 and a.objecttype = 1 and a.objectid = ? union ");
		builder.append("  select a.albumid from htl_alb_hotel_relationship a where a.active = 1 and a.objecttype = 2 and a.objectid in ");
		builder.append("  (select b.room_type_id from htl_roomtype b where b.hotel_id = ?)) ");
		builder.append("  order by b.classify,b.albumname, d.pictureid, d.picturetype ");
		
		
		
		Object[] paramValues = new Object[]{hotelId,hotelId};
		List<Object[]> objects = super.queryByNativeSQL(builder.toString(), paramValues);
		
		List<HtlAlbumVO> htlAlbumVOList = new ArrayList<HtlAlbumVO>();
		int classifyFlag = -1;
		for(Object[] obj:objects){
			int classify = ((BigDecimal)obj[0]).intValue();
			if(classifyFlag != classify){
				classifyFlag = classify;
				//相册类型不相等 新建相册对象
				HtlAlbumVO htlAlbumVO = new HtlAlbumVO();
				htlAlbumVO.setAlbumClassify(classify);
//				if(0 != classify){
					htlAlbumVO.setAlbumName((String)obj[1]);
					htlAlbumVO.setAlbumCoverId(((BigDecimal)obj[2]).longValue());
					htlAlbumVO.setAlbumCoverUrl((String)obj[3]);
//				}
				htlAlbumVOList.add(htlAlbumVO);
			}		
		}
		
		long pictureIdFlag = -1;
		for(HtlAlbumVO htlAlbum : htlAlbumVOList){
			List<HtlPictureVO> htlPictureVOList = new ArrayList<HtlPictureVO>();
			for(Object[] obj:objects){
				long pictureId = ((BigDecimal)obj[4]).longValue();
				int classify = ((BigDecimal)obj[0]).intValue();
				if(classify == htlAlbum.getAlbumClassify() && pictureIdFlag!=pictureId){ //相册类型相等&&不是同一张图片
					pictureIdFlag = pictureId;
					HtlPictureVO htlPictureVO = new HtlPictureVO();
					htlPictureVO.setPictureId(pictureId);
					htlPictureVO.setPictureName((String)obj[5]);
					htlPictureVOList.add(htlPictureVO);
				}
			}
			htlAlbum.setHtlPictureVOList(htlPictureVOList);
		}
		
		for(HtlAlbumVO htlAlbum : htlAlbumVOList){
			for(HtlPictureVO htlPicture : htlAlbum.getHtlPictureVOList()){
				List<HtlPictureSizeVO> htlPictureSizeVOList = new ArrayList<HtlPictureSizeVO>();
				for(Object[] obj:objects){
					long pictureId = ((BigDecimal)obj[4]).longValue();
					if(pictureId == htlPicture.getPictureId()){
						HtlPictureSizeVO htlPictureSizeVO = new HtlPictureSizeVO();
						htlPictureSizeVO.setPictureType(((BigDecimal)obj[6]).intValue());
						htlPictureSizeVO.setPictureUrl((String)obj[7]);
						htlPictureSizeVOList.add(htlPictureSizeVO);
					}
				}
				htlPicture.setHtlPictureSizeVOList(htlPictureSizeVOList);
			}
		}
		
		return htlAlbumVOList;
	}
	
	
	public List<HotelAppearanceAlbum> queryAppearanceAlbum(String hotelIds){
		StringBuilder builder = new StringBuilder(" select a.objectid,a.albumid, d.picturetype, d.pictureurl ");
		
		builder.append("from htl_alb_hotel_relationship a, htl_pic_album b, htl_pic_picurl d");
		
		builder.append(" where a.objectid in ( ").append(hotelIds).append(" )");
		
		builder.append(" and a.active = 1  and a.objecttype = 1  and a.albumid = b.albumid  and b.classify = 1 and d.pictureid = b.coverid  order by a.objectid, d.picturetype");
		
		Object[] paramValues = new Object[]{};
		List<Object[]> objects = super.queryByNativeSQL(builder.toString(), paramValues);
		
		List<HotelAppearanceAlbum> hotelAppearanceAlbumList = new ArrayList<HotelAppearanceAlbum>();
		
		for(Object[] obj:objects){
			HotelAppearanceAlbum hotelAppearanceAlbum = new HotelAppearanceAlbum();
			hotelAppearanceAlbum.setHotelId(((BigDecimal)obj[0]).longValue());
			hotelAppearanceAlbum.setPictureId(((BigDecimal)obj[1]).longValue());
			hotelAppearanceAlbum.setPictureType(((BigDecimal)obj[2]).intValue());
			hotelAppearanceAlbum.setPrictureUrl((String)obj[3]);
			hotelAppearanceAlbumList.add(hotelAppearanceAlbum);
		}
		return hotelAppearanceAlbumList;
	}

}
