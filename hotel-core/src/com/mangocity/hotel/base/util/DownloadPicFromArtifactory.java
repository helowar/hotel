package com.mangocity.hotel.base.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DownloadPicFromArtifactory {
	private static Log log = LogFactory.getLog(DownloadPicFromArtifactory.class);
	private static String hostIdStr;
	//从Artifactory下载图片
	public static  int doBusinessByHttpClient(String localPath,String artiUrl,String[] artiInfo) throws IOException {
		log.info("doBusinessByHttpClient--begin----localPath:"+localPath+"/artiUrl:"+artiUrl+"/artiInfo:"+artiInfo.toString());
		 int staut=200;
		HttpClient httpClient = new HttpClient();
		  //创建GET方法的实例
		  GetMethod getMethod = new GetMethod(artiUrl);
		  httpClient.getState().setCredentials(new AuthScope(hostIdStr, 80, AuthScope.ANY_HOST),new UsernamePasswordCredentials(artiInfo[0], artiInfo[1]));
		FileOutputStream fos = null;
		try {
			staut = httpClient.executeMethod(getMethod);
			//从Artifactory上获取该图片的byte[]
			 byte[] responseBody = getMethod.getResponseBody();
	         // 处理内容，解析为图片格式　
			 File imgFile = new File(localPath);
			 fos=new FileOutputStream (imgFile);
             fos.write(responseBody);
             fos.flush();
		} catch (HttpException e1) {
			log.error("artifactory upload error:",e1);
			throw e1;
		} catch (IOException e) {
			log.error("artifactory upload error:",e);
			throw e;
		} finally{
            fos.close();
            fos = null; 
		}
		getMethod.releaseConnection();
		log.info("doBusinessByHttpClient--end--staut:"+staut);
		  return staut;

	}
}
