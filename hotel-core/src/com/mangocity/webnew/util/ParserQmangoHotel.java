package com.mangocity.webnew.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mangocity.hweb.persistence.QHotelInfo;

/**
 * 
 * @author alfred
 *
 */
public class ParserQmangoHotel extends DefaultHandler{
	
    /**
     * 
     */
	private List<QHotelInfo> qhotels;
	private QHotelInfo qhotel;
	private String pretag;
	//解决SAX脏数据问题
	private String tmp = "";
	public static Log log = LogFactory.getLog(ParserQmangoHotel.class);
	public static List getQhotels(InputStream xmlStream) throws ParserConfigurationException, SAXException, IOException,Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ParserQmangoHotel handler = new ParserQmangoHotel(); 
		
		BufferedInputStream bufferStream=new BufferedInputStream(xmlStream);
	    Thread.sleep(300);

	    int available=bufferStream.available();
	
        log.info("qmango xml size :"+available);
		if(available > 45){
		 InputStreamReader inputStreamReader=new InputStreamReader(xmlStream,"UTF-8");		
		 parser.parse(new InputSource(inputStreamReader), handler);
		}
		return handler.getHotels();
	}
	/**
	 * 
	 * @return
	 */
	private List getHotels() {
		// TODO Auto-generated method stub
		return qhotels;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override  
	public void startDocument() throws SAXException {
		
		qhotels = new ArrayList<QHotelInfo>();
		super.startDocument();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override 
    public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if("hotelInfo".equals(qName)) {
			qhotel = new QHotelInfo();
		}
		pretag = qName;
	}
    /*
     * (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override  
    public void endElement(String uri, String localName, String qName)
			throws SAXException {
    	if("hotelId".equals(pretag)) {
    		qhotel.setHotelId(Long.valueOf(tmp));
    		tmp = "";
    	}else if("city".equals(pretag)) {
    		qhotel.setCity(tmp);
    		tmp = "";
    	}else if("biz_zone".equals(pretag)) {
    		qhotel.setBizZone(tmp);
    		tmp = "";
    	}else if("chn_name".equals(pretag)) {
    		qhotel.setChnName(tmp);
    		tmp = "";
    	}else if("eng_name".equals(pretag)) {
    		qhotel.setEngName(tmp);
    		tmp = "";
    	}else if("bookingFlag".equals(pretag)) {
    		qhotel.setBookingFlag("true".equals(tmp));
    		tmp = "";
    	}else if("minSalePrice".equals(pretag)) {
    		qhotel.setMinSalePrice(tmp);
    		tmp = "";
    	}else if("currency".equals(pretag)) {
    		qhotel.setCurrency(tmp);
    		tmp = "";
    	}else if("hotelURL".equals(pretag)) {
    		qhotel.setHotelURL(tmp);
    		tmp = "";
    	}else if("recommend".equals(pretag)) {
    		qhotel.setRecommend("true".equals(tmp));
    		tmp = "";
    	}else if("hotelInfo".equals(qName)) {
			qhotels.add(qhotel);
			qhotel = null;
		}
		pretag = null;
	}
    
    /*
     * (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override  
    public void characters(char ch[], int start, int length)
			throws SAXException {
    	String content = new String(ch,start,length);
    	if("hotelId".equals(pretag)) {
    		tmp += content;
    	}else if("city".equals(pretag)) {
    		tmp += content;
    	}else if("biz_zone".equals(pretag)) {
    		tmp += content;
    	}else if("chn_name".equals(pretag)) {
    		tmp += content;
    	}else if("eng_name".equals(pretag)) {
    		tmp += content;
    	}else if("bookingFlag".equals(pretag)) {
    		tmp += content;
    	}else if("minSalePrice".equals(pretag)) {
    		tmp += content;
    	}else if("currency".equals(pretag)) {
    		tmp += content;
    	}else if("hotelURL".equals(pretag)) {
    		tmp += content;
    	}else if("recommend".equals(pretag)) {
    		tmp += content;
    	}
    }
    
    


}
