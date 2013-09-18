package com.mangocity.hotel.dreamweb.comment.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mangocity.hotel.dreamweb.comment.model.DaoDaoBasicComment;
import com.mangocity.hotel.dreamweb.comment.service.impl.DaoDaoCommentServiceImpl;
import com.mangocity.util.log.MyLog;

public class SaxXmlHandler extends DefaultHandler {
	
	private static final MyLog log = MyLog.getLogger(DaoDaoCommentServiceImpl.class);
	private List<DaoDaoBasicComment> basicComments = new ArrayList<DaoDaoBasicComment>();
    private DaoDaoBasicComment basicComment;
    private String tagName;
    private StringBuilder content = new StringBuilder();
    public static String COMPLETE;
    public static String NOTFOUND;
    public static String URL_FRONT;
    public static String URL_TAIL;
    public static String URL_REGEX;
    public static String READ_PROPERTIES_SUCCESS;
    public static String IS_RATING_IMAGE_FROMDAODAO;
    
    public SaxXmlHandler(){
    	
    	Properties prop = new Properties();
    	InputStream in =DaoDaoCommentServiceImpl.class.getResourceAsStream("/daodao.properties");
    	try {
    		prop.load(in);
    		COMPLETE = prop.getProperty("daodao.parseXmlStatus.complete");
    		NOTFOUND = prop.getProperty("daodao.parseXmlStatus.nocomment");
    		URL_FRONT = prop.getProperty("daodao.ratingImage.url.front");
    		URL_TAIL = prop.getProperty("daodao.ratingImage.url.tail");
    		URL_REGEX = prop.getProperty("daodao.ratingImage.url.regex");
    		READ_PROPERTIES_SUCCESS = prop.getProperty("daodao.readProperties.success");
    		IS_RATING_IMAGE_FROMDAODAO = prop.getProperty("daodao.ratingImage.fromDaoDao");
    		log.info(READ_PROPERTIES_SUCCESS);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
	

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {		
		String subContent = new String(ch,start,length).trim();		
		content.append(subContent);
	}


	@Override
	public void endDocument() throws SAXException {
		if(0==this.basicComments.size()){
			 throw new SAXException(NOTFOUND);
		}else {
			log.info(COMPLETE+this.basicComments.size());
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
        if("loc".endsWith(qName)){
		   this.basicComments.add(this.basicComment);
		} 
		  if("name".equals(this.tagName)){
				this.basicComment.setHotelName(content.toString());			
		}else if("daodao_id".equals(this.tagName)){
				this.basicComment.setDaodaoId(Long.parseLong(content.toString()));
		}else if("overall_rating_url".equals(this.tagName)){
				String ratingUrl = content.toString();
				if(!"true".equals(IS_RATING_IMAGE_FROMDAODAO)){  //点评分数的图片是用我们自己服务器的图片，不访问
					                                             //到到网服务器，这里需要做路径转化
					int beginIndex = ratingUrl.indexOf(URL_REGEX);
					if(beginIndex!=-1&&ratingUrl.length()>beginIndex+11){
						beginIndex = beginIndex+9;
						int endIndex = beginIndex+3;
						String ratingNum = ratingUrl.substring(beginIndex, endIndex);
						ratingUrl=URL_FRONT+ratingNum+URL_TAIL;
					}
				}				
				this.basicComment.setRatingUrl(ratingUrl);
		}else if("total_review_number".equals(this.tagName)){
				this.basicComment.setTotalNumber(Integer.parseInt(content.toString()));
		}else if("mangocity_id".equals(this.tagName)){
			this.basicComment.setHotelId(Long.parseLong(content.toString()));	
		}
		this.content.delete(0, content.length());
		this.tagName=null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		  this.tagName=qName;
		  if("loc".equals(qName)){
			 this.basicComment = new DaoDaoBasicComment();
		  }

	}

	public List<DaoDaoBasicComment> getBasicComments() {
		return basicComments;
	}

	public void setBasicComments(List<DaoDaoBasicComment> basicComments) {
		this.basicComments = basicComments;
	}
	public DaoDaoBasicComment getBasicComment() {
		return basicComment;
	}

	public void setBasicComment(DaoDaoBasicComment basicComment) {
		this.basicComment = basicComment;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
    
	
	
	
}
