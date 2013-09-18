/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.mangocity.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.Reader;
import java.io.StringReader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.mangocity.util.collections.FormatMap;
import com.mangocity.util.exception.ConfigException;

/**
 */
public class XMLUtils implements Serializable {

    /**
     * 转化为Object
     * @param root
     * @param name
     * @return
     * @throws Exception
     */
	public static Object toObject(Element root, String name) throws Exception{
		String className = root.getAttributeValue(name);
		if (!StringUtil.isValidStr(className))
			className = root.getChildTextNormalize(name);
		if (StringUtil.isValidStr(className)) {
			return Class.forName(className).newInstance();
		}
		return null;
	}

	/**
	 * 
	 * @param in
	 * @return
	 * @throws ConfigException
	 */
	public static Element getRootByName(InputStream in) throws ConfigException {
	    try {
	        return getElementByXmlStream(in);
	    } catch (Exception je) {
	        throw new ConfigException(je.getMessage());
	    }
	}
	
	/**
	 * 读取XML文件流
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static Element getElementByXmlStream(InputStream in) throws Exception {
	    SAXBuilder builder = new SAXBuilder(false);
	    Document doc = builder.build(in);
		return doc.getRootElement();
	}
    
	/**
     * 读取XML文件的Char类型
     * @param in
     * @return
     * @throws IOException
     */
    public static Element getElementByReadChar(Reader in) throws Exception {
        SAXBuilder builder = new SAXBuilder(false);
        Document doc = builder.build(in);
        return doc.getRootElement();
    }
    
    /**
     * 读取XML文件的String类型
     * @param xmlStr String类型的XML
     * @return
     */
    public static Element getElementByXmlString(String xmlStr) throws Exception {
        Reader xmlRead = new StringReader(xmlStr); 
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(new InputSource(xmlRead));
        return doc.getRootElement();
    }
    
    /**
     * 根据Element生成XML
     * @param soap
     */
    public static String setStringByElement(Element element){
        XMLOutputter xmlOut = new XMLOutputter();  
        return xmlOut.outputString(element);
    }

    /**
     * 转化为XML
     * @param records
     * @return
     */
    public static String convertXML(List<FormatMap> records) {
    	StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>\n");
		xml.append("<data>\n");

		for (int m = 0; m < records.size(); m++) {
			FormatMap row = records.get(m);
			xml.append("<row>\n");
			Iterator<Map.Entry<String, String>> iter = row.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> e = iter.next();
				xml.append("<").append(e.getKey()).append(">").append(
				    e.getValue()).append("</").append(e.getKey()).append(">\n");
			}
			xml.append("</row>\n");
		}
		xml.append("</data>\n");
		return xml.toString();
    }        

}
