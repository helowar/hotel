package com.mangocity.hotel.dreamweb.comment;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mangocity.hotel.comment.api.CommentDetailQueryRemote;
import com.mangocity.hotel.comment.api.CommentModifyRemote;
import com.mangocity.hotel.comment.api.CommentSeqInfoRemote;
import com.mangocity.hotel.comment.api.CommentSummaryRemote;
import com.mangocity.hotel.comment.api.HotelInfoQueryRemote;
import com.mangocity.hotel.comment.dto.CommentConstantDTO;
import com.mangocity.hotel.comment.dto.CommentCountDTO;
import com.mangocity.hotel.comment.dto.CommentQueryDTO;
import com.mangocity.hotel.comment.dto.CommentSessionDTO;
import com.mangocity.hotel.comment.dto.HtlBaseInfoDTO;
import com.mangocity.hotel.comment.dto.HtlEvaluationDTO;
import com.mangocity.hotel.comment.dto.PaginationSupport;

/**
 * 
 * TODO liting: 酒店详情页中的点评信息列表，通过EJB来进行查询
 * @version   Revision History
 * <pre>
 * Author     Version       Date        Changes
 * liting    1.0           Feb 17, 2012     Created
 *
 * </pre>
 * @since 1.
 */
public class AccessCommentDetail {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CommentModifyRemote commentModifyRemote;
	private CommentDetailQueryRemote commentDetailQueryRemote;
	private CommentSeqInfoRemote commentSeqInfoRemote;
	private HotelInfoQueryRemote hotelInfoQueryRemote;

	private CommentSummaryRemote commentSummaryRemote;

	private static final Logger log = Logger.getLogger(AccessCommentDetail.class);

	public HtlEvaluationDTO getHtlEvaluation(Long hotelId) {
		return hotelInfoQueryRemote.queryHtlEvaluation(hotelId);
	}

	/**
	 * 查询日期
	 * @param commentCountDTO
	 * @return 
	 */

	public String queryDate(CommentCountDTO commentCountDTO) {
		return commentDetailQueryRemote.queryCommentCountByIp(commentCountDTO);
	}

	/**
	 * 保存有用或无用的数据
	 * @param recordIp
	 */
	public void saveCommentCount(CommentCountDTO commentCountDTO) {
		commentModifyRemote.createCommentCount(commentCountDTO);
	}

	/**
	 * 查询点评计数表的Id
	 */
	public Long queryCountId() {
		return commentSeqInfoRemote.queryCommentCountSeq();
	}

	/**
	 * 查询点评信息的总记录数
	 */
	public Long querySumRow(CommentSessionDTO commentSession) {

		return commentDetailQueryRemote.queryCommentRecordSumCount(commentSession);
	}

	/**
	 * 获取所有前15条点评
	 */
	@SuppressWarnings("unchecked")
	public PaginationSupport getPaginationSupport(CommentQueryDTO commentQueryDTO) {

		return commentDetailQueryRemote.queryComments(commentQueryDTO);
	}

	/**
	 * 获取所有前15条点评
	 */
	/*
	@SuppressWarnings("unchecked")
	public PaginationSupport getPaginationSupport(CommentQueryDTO commentQueryDTO) {
			
		return commentDetailQueryRemote.queryComments(commentQueryDTO);
	}
	 */
	/**
	获取推荐指数
	 */
	public String getCommentIndex(Long hotelId) {
		HtlBaseInfoDTO htlBaseInfoDTO = hotelInfoQueryRemote.queryHotelbaseInfo(hotelId);
		DecimalFormat df = new DecimalFormat("###");
		if (htlBaseInfoDTO != null) {
			return df.format(htlBaseInfoDTO.getRecommendIndex() * 100) + "%";
		} else {
			return "0%";
		}

	}

	public Long getCountId() {
		return commentSeqInfoRemote.queryCommentCountSeq();
	}

	//新
	public Map<Long, String> queryGradedConstantInfo() {
		Map<Long, String> gradedMap = new HashMap<Long, String>();
		List<CommentConstantDTO> commentConstantDTOList = commentDetailQueryRemote.queryCommentConstantInfo(1);// 查询评分常量
		if (commentConstantDTOList != null) {
			for (CommentConstantDTO obj : commentConstantDTOList) {
				gradedMap.put(obj.getConstantid(), obj.getConstantname());
			}
		}

		return gradedMap;
	}

	public Map<Long, String> queryImpressionConstantInfo() {

		Map<Long, String> impressionMap = new HashMap<Long, String>();
		List<CommentConstantDTO> commentConstantDTOList = commentDetailQueryRemote.queryCommentConstantInfo(2);// 查询评分常量
		if (commentConstantDTOList != null) {
			for (CommentConstantDTO obj : commentConstantDTOList) {
				impressionMap.put(obj.getConstantid(), obj.getConstantname());
			}
		}

		return impressionMap;
	}

	public String queryUploadPicsURL() {

		return commentSummaryRemote.queryUploadPicsURL();
	}

	public CommentDetailQueryRemote getCommentDetailQueryRemote() {
		return commentDetailQueryRemote;
	}

	public void setCommentDetailQueryRemote(CommentDetailQueryRemote commentDetailQueryRemote) {
		this.commentDetailQueryRemote = commentDetailQueryRemote;
	}

	public HotelInfoQueryRemote getHotelInfoQueryRemote() {
		return hotelInfoQueryRemote;
	}

	public void setHotelInfoQueryRemote(HotelInfoQueryRemote hotelInfoQueryRemote) {
		this.hotelInfoQueryRemote = hotelInfoQueryRemote;
	}

	public CommentModifyRemote getCommentModifyRemote() {
		return commentModifyRemote;
	}

	public void setCommentModifyRemote(CommentModifyRemote commentModifyRemote) {
		this.commentModifyRemote = commentModifyRemote;
	}

	public CommentSeqInfoRemote getCommentSeqInfoRemote() {
		return commentSeqInfoRemote;
	}

	public void setCommentSeqInfoRemote(CommentSeqInfoRemote commentSeqInfoRemote) {
		this.commentSeqInfoRemote = commentSeqInfoRemote;
	}

	public CommentSummaryRemote getCommentSummaryRemote() {
		return commentSummaryRemote;
	}

	public void setCommentSummaryRemote(CommentSummaryRemote commentSummaryRemote) {
		this.commentSummaryRemote = commentSummaryRemote;
	}

}
