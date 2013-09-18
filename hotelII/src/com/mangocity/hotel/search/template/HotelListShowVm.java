package com.mangocity.hotel.search.template;

import java.io.StringWriter;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import com.mangocity.hotel.search.vo.HotelResultVO;


public class HotelListShowVm {
	
    private static Template template= null;
	
	private Properties getInitVelocityProp() 
	{
		Properties p = new Properties();
		p.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
		p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		p.setProperty(VelocityEngine.INPUT_ENCODING, "UTF-8");
		p.setProperty(VelocityEngine.OUTPUT_ENCODING, "UTF-8");
		p.setProperty(VelocityEngine.ENCODING_DEFAULT, "UTF-8");
		p.setProperty(VelocityEngine.COUNTER_NAME, "loopCounter");
		p.setProperty(VelocityEngine.COUNTER_INITIAL_VALUE, "0");
		return p;
	}
	
	private VelocityContext getContext(HotelResultVO hotelVO){
		VelocityContext context = new VelocityContext();
		context.put("hotelVO", hotelVO);
		return context;
	}
		
	public String getHotelListWithTemplate(HotelResultVO hotelVO) throws Exception{
		if(template==null){	
			Velocity.init(getInitVelocityProp());		
			template = Velocity.getTemplate("com/mangocity/hotel/search/template/vm/hotelList.vm");
		}
		StringWriter writer = new StringWriter();
		template.merge(getContext(hotelVO),writer);
		writer.close();
		return writer.toString();
	}
	
	
}
