package com.mangocity.hotel.base.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.base.service.ICityLookUpService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.util.log.MyLog;

/**
 * 生成所有城市JS
 * 
 * @author zhuangzhineng
 */
public class GenAllCity implements Serializable {
	private static final MyLog log = MyLog.getLogger(GenAllCity.class);
    private ICityLookUpService cityLookUpService;

  
	public void genJs() {
        List qResList = new ArrayList();
        log.info("----------------------------------");
        qResList = cityLookUpService.queryAllCity();
        log.info("qResList.size() = " + qResList.size());
        StringBuffer strbf = new StringBuffer();
        String firstLine = "\tvar citysForHotel=new Array();\n";
        String basePath = InitServlet.saveHWEBpath;
        strbf.append(firstLine);
        HtlArea htlArea = new HtlArea();

        for (int i = 0; i < qResList.size(); i++) {
            htlArea = (HtlArea) qResList.get(i);
            String addStr = "\tcitysForHotel[" + i + "]=new Array('" + htlArea.getCityCode()
                + "','" + htlArea.getCityName() + "','" + htlArea.getQpinyin() + "','"
                + htlArea.getJpinyin() + "');\n";
            strbf.append(addStr);
        }
        strbf.append("citys = citysForHotel;\n");
        String filePath = basePath + "/allCity.js";
        log.info(filePath);
        saveStrToFile(filePath, strbf.toString());
    }

    /**
     * 将字符串写到文本文件里
     * 
     * @param pathStr
     *            文件路径(包含文件名)
     * @param buffer
     *            要写入的字符串(包括换行符等)
     * @return 创建的文件名,如果失败则返回null
     */
    private static long saveStrToFile(String pathStr, String buffer) {
        OutputStream os = null;
        OutputStreamWriter osw = null;
        BufferedWriter bfw = null;
        try {
            // 指定生成文件及路径
            os = new FileOutputStream(pathStr);
            // 创建使用指定字符集UTF-8的writer
            osw = new OutputStreamWriter(os, "UTF-8");
            bfw = new BufferedWriter(osw);
            // 新String对象使用UTF-8来解码字节数组，以UTF-8编码输出文件
            bfw.write(new String(buffer.getBytes("UTF-8"), "UTF-8"));
            bfw.flush();
            bfw.close();
            bfw = null;
            // buffer.delete(0,buffer.length()-1);
            return 1;
        } catch (IOException e) {
        	log.error("新建文件操作出错 : " + pathStr);
            return -1;
        } catch (Exception e) {
        	log.error("另外异常 : " + e);
            return -1;
        }finally{
            try {
            	if(null != bfw) {
            		bfw.close();	
            	}                
                osw.close();
                os.close();
            } catch (IOException e) {
            	log.error(e.getMessage(),e);
            }
        }
    }

  
    public ICityLookUpService getCityLookUpService() {
		return cityLookUpService;
	}

	public void setCityLookUpService(ICityLookUpService cityLookUpService) {
		this.cityLookUpService = cityLookUpService;
	}

}
